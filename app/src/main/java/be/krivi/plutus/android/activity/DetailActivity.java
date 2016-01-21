package be.krivi.plutus.android.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback{

    @Bind( R.id.wrapperSlide )
    SlidingUpPanelLayout mSlide;

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.det_dragZone )
    RelativeLayout mDragZone;

    @Bind( R.id.det_title )
    TextView mTitle;

    @Bind( R.id.det_location )
    TextView mLocation;

    @Bind( R.id.det_amount )
    TextView mAmount;

    @Bind( R.id.det_day )
    TextView mDay;

    @Bind( R.id.det_month )
    TextView mMonth;

    @Bind( R.id.det_date )
    TextView mDate;

    @Bind( R.id.det_description )
    TextView mDescription;

    private GoogleMap map;
    private LatLng latLng;

    private DetailActivity detail;
    private Transaction transaction;

    private DecimalFormat df;
    private SimpleDateFormat sdfTime;
    private SimpleDateFormat sdfDate;

    int colorLight, colorLightBlue, colorCurrent;
    boolean sliding;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );
        ButterKnife.bind( this );
        detail = this;

        this.df = new DecimalFormat( "#0.00", DecimalFormatSymbols.getInstance( Locale.getDefault() ) );
        this.colorLight = ContextCompat.getColor( detail, R.color.ucll_light );
        this.colorLightBlue = ContextCompat.getColor( detail, R.color.ucll_light_blue );
        this.colorCurrent = colorLightBlue;

        setSupportActionBar( mToolbar );
        if( getSupportActionBar() != null )
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        mSlide.setPanelSlideListener( new SlidingUpPanelLayout.PanelSlideListener(){
            public void onPanelSlide( View panel, float slideOffset ){
                if( !sliding && colorCurrent == colorLight ){
                    colorizeDragZone( colorLightBlue );
                    sliding = true;
                }
            }

            public void onPanelAnchored( View panel ){
            }

            public void onPanelHidden( View panel ){
            }

            public void onPanelCollapsed( View panel ){
                if( colorCurrent == colorLightBlue )
                    colorizeDragZone( colorLight );
                moveMapIfCollapsed();
                sliding = false;
            }

            public void onPanelExpanded( View panel ){
                if( colorCurrent == colorLight )
                    colorizeDragZone( colorLightBlue );
                moveMapIfExpanded();
                sliding = false;
            }
        } );
    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpView();
        setUpMap();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        switch( item.getItemId() ){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void setUpView(){
        this.transaction = app.getTransactionDetail();
        if( transaction != null ){

            latLng = new LatLng( transaction.getLocation().getLat(), transaction.getLocation().getLng() );

            mTitle.setText( transaction.getTitle() );
            mLocation.setText( transaction.getLocation().getName() );
            mAmount.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + df.format( transaction.getAmount() ) );

            switch( transaction.getType() ){
                case "expense":
                    mAmount.setTextColor( ContextCompat.getColor( this, R.color.transaction_expense ) );
                    break;
                case "topup":
                    mAmount.setTextColor( ContextCompat.getColor( this, R.color.transaction_topup ) );
                    break;
            }

            mDay.setText( transaction.getDay() + "" );
            mMonth.setText( transaction.getMonth( "short" ) );

            Date timestamp = transaction.getTimestamp();

            try{
                //TODO add user pref for 24H or AM/PM
                sdfTime = new SimpleDateFormat( "HH:mm", Locale.getDefault() );
                sdfDate = new SimpleDateFormat( "EEEE d MMMM yyyy", Locale.getDefault() );
                if( Locale.getDefault().toString().equals( "en_US" ) )
                    sdfDate = new SimpleDateFormat( "EEEE MMMM d, yyyy", Locale.US );
                // in US English day is usually put after month

                String date = sdfDate.format( timestamp );
                String time = sdfTime.format( timestamp );

                mDate.setText( getString( R.string.on_timestamp, date, time ) );
            }catch( Exception e ){
                e.printStackTrace();
                // onmogelijk hier te geraken
            }

            mDescription.setText( Html.fromHtml( transaction.getDescription() ) );
        }
    }

    private void setUpMap(){
        if( map == null ){
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById( R.id.map );
            mapFragment.getMapAsync( this );
        }
    }

    @Override
    public void onMapReady( GoogleMap map ){
        this.map = map;

        if( transaction == null ){
            Message.obtrusive( this, getString( R.string.error_loading_transaction ) );
        }else{
            map.addMarker( new MarkerOptions()
                    .position( latLng )
                    .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_location_on_pink_48dp ) ) );

            map.getUiSettings().setMapToolbarEnabled( false );

            map.moveCamera( CameraUpdateFactory.newLatLngZoom( Config.APP_DEFAULT_MAP_LATLNG, Config.APP_DEFAULT_MAP_ZOOM ) );
            moveMapIfExpanded();
        }
    }

    private void moveMapIfCollapsed(){
        if( map != null )
            map.animateCamera( CameraUpdateFactory.newLatLngZoom( latLng, 15 ) );
    }

    private void moveMapIfExpanded(){
        if( map != null ){
            double lat = latLng.latitude - .004;
            double lng = latLng.longitude;
            map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( lat, lng ), 15 ) );
        }
    }

    private void colorizeDragZone( int color ){
        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                mDragZone,
                "backgroundColor",
                new ArgbEvaluator(),
                colorCurrent,
                color
        );
        colorFade.setDuration( 500 );
        colorFade.start();
        colorCurrent = color;
    }

    @OnClick( R.id.det_btnMaps )
    public void launchMaps(){
        if( transaction != null ){
            Uri intentUri = Uri.parse( "geo:"
                    + transaction.getLocation().getLat()
                    + ","
                    + transaction.getLocation().getLng()
                    + "?q="
                    + transaction.getLocation().getName()
            );
            Intent mapIntent = new Intent( Intent.ACTION_VIEW, intentUri );
            // mapIntent.setPackage( "com.google.android.apps.maps" );
            if( mapIntent.resolveActivity( getPackageManager() ) == null ){
                Message.obtrusive( this, getString( R.string.error_no_maps_app_installed ) );
            }else{
                startActivity( mapIntent );
            }
        }
    }
}
