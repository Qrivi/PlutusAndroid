package be.plutus.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import be.plutus.android.R;
import be.plutus.android.application.Window;
import be.plutus.android.fragment.BaseFragment;
import be.plutus.android.fragment.CreditFragment;
import be.plutus.android.fragment.SettingsFragment;
import be.plutus.android.fragment.TransactionsFragment;
import be.plutus.android.model.Transaction;
import be.plutus.android.model.User;
import be.plutus.android.api.RESTService;
import be.plutus.android.api.model.Credit;
import be.plutus.android.api.model.meta.DefaultMeta;
import be.plutus.android.api.response.CreditResponse;
import be.plutus.android.api.response.TransactionsResponse;
import be.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{

    @Bind( R.id.wrapperMain )
    public DrawerLayout mDrawerLayout;
    // public for easy access setting SnackBars from elsewhere

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.wrapperDrawer )
    NavigationView mNavigationView;

    Menu mToolbarMenu;
    ActionBarDrawerToggle mDrawerToggle;

    BaseFragment currentFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_main );
        ButterKnife.bind( this );

        setSupportActionBar( mToolbar );

        if ( getIntent().getStringExtra( "localization" ) == null )
        {
            setFragment( app.getHomeScreen() );
        }

        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer )
        {
            public void onDrawerClosed( View view )
            {
                super.onDrawerClosed( view );
            }

            public void onDrawerOpened( View drawerView )
            {
                super.onDrawerOpened( drawerView );
            }
        };

        mDrawerLayout.setDrawerListener( mDrawerToggle );
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener( this );

        View headerView = mNavigationView.getHeaderView( 0 );
        TextView lbl_studentId = (TextView) headerView.findViewById( R.id.lbl_studentId );
        lbl_studentId.setText( app.getCurrentUser()
                .getStudentId() );
        TextView lbl_studentName = (TextView) headerView.findViewById( R.id.lbl_studentName );
        lbl_studentName.setText( String.format( "%s %s", app.getCurrentUser()
                .getFirstName(), app.getCurrentUser()
                .getLastName() ) );

        if ( app.isNewInstallation() )
            mDrawerLayout.openDrawer( GravityCompat.START );

        if ( app.isDatabaseIncomplete() )
            Message.snack( mDrawerLayout, getString( R.string.database_setting_up ) );

        canConnectToInternet();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if ( getIntent().getStringExtra( "localization" ) != null )
        {
            Log.i( "Localization", "Application Locale was updated" );
            setFragment( Window.SETTINGS );
            getIntent().removeExtra( "localization" );
        }

        if ( app.fetchRequired() )
        {
            fetchAllData();
            Log.i( "Data status", "outdated -- fetching..." );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mToolbarMenu = menu;
        getMenuInflater().inflate( R.menu.menu_main, menu );

        SearchView searchView = (SearchView) MenuItemCompat.getActionView( mToolbarMenu.findItem( R.id.menu_search ) );
        searchView.setQueryHint( getString( R.string.find_a_transaction ) );
        searchView.setSubmitButtonEnabled( false );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit( String query )
            {
                // filter alle transacties ooit
                // TODO niet in versie voor Vogels
                return false;
            }

            @Override
            public boolean onQueryTextChange( String text )
            {
                if ( currentFragment instanceof TransactionsFragment )
                {
                    TransactionsFragment fragment = (TransactionsFragment) currentFragment;
                    fragment.filterTransactions( text );
                }
                return false;
            }
        } );
        searchView.addOnAttachStateChangeListener( new View.OnAttachStateChangeListener()
        {
            @Override
            public void onViewDetachedFromWindow( View arg0 )
            {
                if ( currentFragment instanceof TransactionsFragment )
                {
                    TransactionsFragment fragment = (TransactionsFragment) currentFragment;
                    fragment.updateView();
                }
            }

            @Override
            public void onViewAttachedToWindow( View arg0 )
            {
                // search was opened
            }
        } );

        setMenuOptions( app.getHomeScreen() );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        if ( item.getItemId() == R.id.menu_filter )
        {
            // TODO filters
            Message.toast( this, getString( R.string.not_in_beta ) );
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onBackPressed()
    {
        if ( mToolbar.getTitle()
                .toString()
                .equals( getString( app.getHomeScreen()
                        .getId() ) ) )
            finish();
        else
            setFragment( app.getHomeScreen() );
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item )
    {

        // if user taps active item, no new fragments need to load
        if ( !item.isChecked() )
        {
            switch ( item.getItemId() )
            {
                case R.id.navigation_signout:
                    logout();
                    return true;
                case R.id.navigation_settings:
                    setFragment( Window.SETTINGS );
                    break;
                case R.id.navigation_transactions:
                    setFragment( Window.TRANSACTIONS );
                    break;
                case R.id.navigation_credit:
                    setFragment( Window.CREDIT );
                    break;

            }
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public void setFragment( Window window )
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction switcher = manager.beginTransaction();
        int nav = 0;

        switch ( window.getPos() )
        {
            case 0:
                nav = R.id.navigation_credit;
                currentFragment = new CreditFragment();
                break;
            case 1:
                nav = R.id.navigation_transactions;
                currentFragment = new TransactionsFragment();
                break;
            case 2:
                nav = R.id.navigation_settings;
                currentFragment = new SettingsFragment();
                break;
        }

        setMenuOptions( window );
        switcher.replace( R.id.wrapperFragment, currentFragment );

        if ( nav != 0 )
            mNavigationView.getMenu()
                    .findItem( nav )
                    .setChecked( true );
        if ( getSupportActionBar() != null )
            getSupportActionBar().setTitle( getString( window.getId() ) );

        mToolbar.setTitle( getString( window.getId() ) );
        switcher.commit();
    }

    private void setMenuOptions( Window window )
    {

        if ( mToolbarMenu != null )
        {
            MenuItem search = mToolbarMenu.findItem( R.id.menu_filter );
            MenuItem filter = mToolbarMenu.findItem( R.id.menu_search );

            switch ( window.getPos() )
            {
                case 1:
                    search.setVisible( true );
                    filter.setVisible( true );
                    break;
                default:
                    search.setVisible( false );
                    filter.setVisible( false );
                    break;
            }
        }
    }

    public void fetchAllData()
    {
        if ( canConnectToInternet() )
        {
            fetchCreditData();
            fetchTransactionsData();
        }
    }

    public void fetchCreditData()
    {
        if ( canConnectToInternet() )
        {
            RESTService service = app.getRESTService();

            User current = app.getCurrentUser();

            Call<CreditResponse> call = service.credit( current.getStudentId(), current.getPassword() );
            call.enqueue( new Callback<CreditResponse>()
            {
                @Override
                public void onResponse( Response<CreditResponse> response, Retrofit retrofit )
                {
                    CreditResponse creditResponse = response.body();
                    Credit credit = creditResponse.getData();
                    DefaultMeta meta = creditResponse.getMeta();

                    app.writeUserCredit( credit.getAmount(), meta.getTimestamp() );
                    updateFragment();
                }

                @Override
                public void onFailure( Throwable t )
                {
                    Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_endpoint_credit ) );
                    updateFragment();
                }
            } );
        }
    }

    public void fetchTransactionsData()
    {
        if ( canConnectToInternet() )
        {
            User current = app.getCurrentUser();
            RESTService service = app.getRESTService();

            Call<TransactionsResponse> call = service.transactions( current.getStudentId(), current.getPassword(), 1 );
            call.enqueue( new Callback<TransactionsResponse>()
            {
                @Override
                public void onResponse( Response<TransactionsResponse> response, Retrofit retrofit )
                {
                    TransactionsResponse transactionsResponse = response.body();

                    List<Transaction> transactions = Stream.of( transactionsResponse.getData() )
                            .map( be.plutus.android.api.model.Transaction::convert )
                            .collect( Collectors.toList() );

                    app.writeTransactions( transactions );
                    app.completeDatabase( 2 );

                    updateFragment();
                }

                @Override
                public void onFailure( Throwable t )
                {
                    Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_endpoint_transactions ) );
                    updateFragment();
                }
            } );
        }
    }

    public void updateFragment()
    {
        currentFragment.updateView();
    }

    private boolean canConnectToInternet()
    {

        if ( !app.isNetworkAvailable() )
        {
            Message.snack( mDrawerLayout, getString( R.string.no_internet_connection ) );
            return false;
        }
        return true;

    }

    private void logout()
    {
        app.logoutUser();
        startActivity( new Intent( app.getApplicationContext(), LoginActivity.class ) );
        finish();
    }

}

