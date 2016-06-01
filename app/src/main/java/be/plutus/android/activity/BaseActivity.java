package be.plutus.android.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.view.inputmethod.InputMethodManager;
import be.plutus.android.R;
import be.plutus.android.model.Language;
import be.plutus.android.PlutusAndroid;

import java.util.Locale;

/**
 * Created by Krivi on 14/12/15.
 */
public abstract class BaseActivity extends AppCompatActivity{

    PlutusAndroid app;
    InputMethodManager imm;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        app = (PlutusAndroid)getApplicationContext();
        imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE );

        Language language = app.getLanguage();
        Locale locale = language == Language.DEFAULT ? app.getDefaultLocale() : language.getLocale();

        Locale.setDefault( locale );
        Configuration config = new Configuration();
        config.locale = locale;
        this.getBaseContext().getResources().updateConfiguration( config, null );

        //if( app.getAndroidApiVersion() >= android.os.Build.VERSION_CODES.LOLLIPOP ){
            Transition slide = new Slide();
            slide.excludeTarget( android.R.id.statusBarBackground, true );
            slide.excludeTarget( R.id.toolbar, true );
            slide.excludeTarget( android.R.id.navigationBarBackground, true );
            getWindow().setEnterTransition( slide );
        //}
    }

    @Override
    protected void onPause(){
        super.onPause();
        app.setCurrentActivity( null );
    }

    @Override
    protected void onResume(){
        super.onResume();
        app.setCurrentActivity( this );
        imm.toggleSoftInput( InputMethodManager.HIDE_IMPLICIT_ONLY, 0 );
    }


}
