package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.view.GaugeAnimation;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind( R.id.wrapperCredit )
    SwipeRefreshLayout mSwipeRefresh;

    @Bind( R.id.gaugeBg )
    ProgressBar mGaugeBg;

    @Bind( R.id.gauge )
    ProgressBar mGauge;

    @Bind( R.id.txt_euros )
    TextView mEuros;

    @Bind( R.id.txt_cents )
    TextView mCents;

    @Bind( R.id.txt_currency )
    TextView mCurrency;

    @Bind( R.id.txt_fetchDate )
    TextView mDate;

    @Bind( R.id.txt_fetchTime )
    TextView mTime;

    private DecimalFormat df;
    private SimpleDateFormat sdfTime;
    private SimpleDateFormat sdfDate;

    public CreditFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_credit, container, false );
        ButterKnife.bind( this, view );

        df = new DecimalFormat( "#0.00", DecimalFormatSymbols.getInstance( Locale.getDefault() ) );

        mGaugeBg.setMax(100);
        mGauge.setMax(100);
        mGaugeBg.setVisibility( View.INVISIBLE );
        mGauge.setVisibility( View.INVISIBLE );

        mSwipeRefresh.setOnRefreshListener( this );
        mSwipeRefresh.setColorSchemeResources(
                R.color.ucll_dark_blue,
                R.color.ucll_light_blue,
                R.color.ucll_pink );

        updateView();
        return view;
    }

    @Override
    public void updateView(){
        mSwipeRefresh.setRefreshing( false );

        String amount = df.format( app.getCurrentUser().getCredit() );
        String euros = amount.substring( 0, amount.length() - 3 );
        String cents = amount.substring( amount.length() - 3, amount.length() );

        mEuros.setText( euros );
        mCents.setText( cents );
        mCurrency.setText( Config.API_DEFAULT_CURRENCY );

        if( app.getCreditRepresentation() ){
            mGaugeBg.setVisibility( View.VISIBLE );
            mGauge.setVisibility( View.VISIBLE );

            float gaugeValue = app.getGaugeValue();

            double dividend = app.getCurrentUser().getCredit() - app.getCreditRepresentationMin();
            int divider = app.getCreditRepresentationMax() - app.getCreditRepresentationMin();

            float newValue = (float)dividend / divider * 75;
            if( newValue < 1 ) newValue = 1;
            else if( newValue > 75 ) newValue = 75;

            GaugeAnimation animation = new GaugeAnimation( mGauge, gaugeValue, newValue );
            animation.setDuration( 1000 );

            mGauge.startAnimation( animation );
            app.setGaugeValue( newValue );
        }else{
            mGaugeBg.setVisibility( View.INVISIBLE );
            mGauge.setVisibility( View.INVISIBLE );
        }

        if( app.getCurrentUser().getFetchDate() == null ){
            mDate.setText( getString( R.string.plutus_has_not_yet_checked_your_credit ) );
            mTime.setText( getString( R.string.pull_down_to_refresh ) );
        }else{
            Date fetchDate = app.getCurrentUser().getFetchDate();

            try{
                //TODO add user pref for 24H or AM/PM
                sdfTime = new SimpleDateFormat( "HH:mm", Locale.getDefault() );
                sdfDate = new SimpleDateFormat( "EEEE d MMMM yyyy", Locale.getDefault() );
                if( Locale.getDefault().toString().equals( "en-US" ) )
                    sdfDate = new SimpleDateFormat( "EEEE MMMM d, yyyy", Locale.US );
                // in US English day is usually put after month

                String date = sdfDate.format( fetchDate );
                String time = sdfTime.format( fetchDate );

                mDate.setText( getString( R.string.on_date, date ) );
                mTime.setText( getString( R.string.at_time, time ) );
            }catch( Exception e ){
                e.printStackTrace();
                // onmogelijk hier te geraken
            }
        }
    }

    @Override
    public void onRefresh(){
        if( app.isNetworkAvailable() ){
            main.fetchCreditData();
        }else{
            Message.snack( main.mDrawerLayout, getString( R.string.no_internet_connection ) );
            mSwipeRefresh.setRefreshing( false );
        }
    }
}
