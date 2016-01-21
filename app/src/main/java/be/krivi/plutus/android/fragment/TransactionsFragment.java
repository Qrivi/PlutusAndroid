package be.krivi.plutus.android.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.DetailActivity;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import be.krivi.plutus.android.view.TransactionsAdapter;
import be.krivi.plutus.android.view.TransactionsOnClickListener;
import be.krivi.plutus.android.view.TransactionsOnScrollListener;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TransactionsOnClickListener{

    @Bind( R.id.swipeRefreshTransactions )
    SwipeRefreshLayout mSwipeRefresh;

    @Bind( R.id.recyclerTransactions )
    RecyclerView mRecycler;

    private TransactionsAdapter adapter;
    private List<Transaction> transactions;
    private TransactionsOnScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private int set;

    public TransactionsFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_transactions, container, false );
        ButterKnife.bind( this, view );

        set = 0;
        linearLayoutManager = new LinearLayoutManager( getActivity() );
        transactions = new LinkedList<>();
        adapter = new TransactionsAdapter( getActivity(), transactions, this );

        mRecycler.setAdapter( adapter );
        mRecycler.setLayoutManager( linearLayoutManager );

        mSwipeRefresh.setOnRefreshListener( this );
        mSwipeRefresh.setColorSchemeResources(
                R.color.ucll_dark_blue,
                R.color.ucll_light_blue,
                R.color.ucll_pink );

        scrollListener = getOnScrollListener( linearLayoutManager );
        mRecycler.addOnScrollListener( scrollListener );

        updateView();
        return view;
    }

    private TransactionsOnScrollListener getOnScrollListener( LinearLayoutManager linearLayoutManager ){

        return new TransactionsOnScrollListener( linearLayoutManager ){
            @Override
            public void onLoadMore( int current_set ){
                if( app.isNetworkAvailable() ){
                    //Message.toast( getContext(), "loading set " + current_set );
                    List<Transaction> newTransactions = app.getTransactionsSet( current_set );

                    if( newTransactions != null ){
                        List<Transaction> updatedList = new LinkedList<>();
                        updatedList.addAll( transactions );
                        updatedList.addAll( newTransactions );

                        transactions = updatedList;
                        adapter.setRowData( transactions );
                    }
                }
            }
        };
    }

    @Override
    public void onRefresh(){
        if( app.isNetworkAvailable() ){
            set = 0;
            main.fetchTransactionsData();
            mRecycler.removeOnScrollListener( scrollListener );
            scrollListener = getOnScrollListener( linearLayoutManager );
            mRecycler.addOnScrollListener( scrollListener );
        }else{
            Message.snack( main.mDrawerLayout, getString( R.string.no_internet_connection ) );
            mSwipeRefresh.setRefreshing( false );
        }
    }


    @Override
    public void updateView(){
        transactions = app.getTransactionsSet( set );
        adapter.setRowData( transactions );

        main.mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
        mSwipeRefresh.setEnabled( true );
        mSwipeRefresh.setRefreshing( false );

        //Message.toast( app.getApplicationContext(), transactions.size() + " loads" );
    }

    public void filterTransactions( String filter ){
        List<Transaction> filteredList = new LinkedList<>();
        CharSequence f = filter.toLowerCase();

        for( Transaction t : transactions )
            if( t.getTitle().toLowerCase().contains( f ) )
                filteredList.add( t );

        main.mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
        mSwipeRefresh.setEnabled( false );
        adapter.setRowData( filteredList );
    }

    @Override
    public void onTransactionClicked( View view, Transaction transaction ){
        app.setTransactionDetail( transaction );
        // startActivity( new Intent( getContext(), DetailActivity.class ));

        View smallDateHolder = view.findViewById( R.id.tr_wrapperDate );
        smallDateHolder.setTransitionName( "transition_date" );
        View smallDateDay = smallDateHolder.findViewById( R.id.tr_day );
        smallDateDay.setTransitionName( "transition_date_day" );
        View smallDateMonth = smallDateHolder.findViewById( R.id.tr_month );
        smallDateMonth.setTransitionName( "transition_date_month" );

        Pair<View, String> holder = Pair.create( smallDateHolder, "transition_date" );
        Pair<View, String> day = Pair.create( smallDateDay, "transition_date_day" );
        Pair<View, String> month = Pair.create( smallDateMonth, "transition_date_month" );

        Intent intent = new Intent( getContext(), DetailActivity.class );
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                main,
                holder, day, month
        );

        main.startActivity( intent, options.toBundle() );
        //main.overridePendingTransition( R.anim.pull_up, R.anim.push_out );
    }
}
