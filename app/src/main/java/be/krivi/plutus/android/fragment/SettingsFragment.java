package be.krivi.plutus.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.Language;
import be.krivi.plutus.android.application.Window;
import be.krivi.plutus.android.dialog.BaseDialog;
import be.krivi.plutus.android.dialog.ConfirmationDialog;
import be.krivi.plutus.android.dialog.EditTextDialog;
import be.krivi.plutus.android.dialog.RadioButtonDialog;
import be.krivi.plutus.android.view.CollapseAnimation;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jan on 27/12/2015.
 */
public class SettingsFragment extends BaseFragment implements EditTextDialog.NoticeDialogListener{

    @Bind( R.id.pref_credit_gaugeMinMaxWrapper )
    LinearLayout mCreditMinMaxWrapper;

    @Bind( R.id.pref_credit_gaugeSwitch )
    SwitchCompat mCreditGaugeSwitch;

    @Bind( R.id.pref_notifications_switch )
    SwitchCompat mNotificationsSwitch;

    @Bind( R.id.pref_hintCreditGaugeMin )
    TextView mCreditGaugeMinHint;

    @Bind( R.id.pref_hintCreditGaugeMax )
    TextView mCreditGaugeMaxHint;

    @Bind( R.id.pref_application_languageHint )
    TextView mApplicationLanguageHint;

    @Bind( R.id.pref_application_homeScreenHint )
    TextView mApplicationHomeScreenHint;

    List<Integer> languages;
    List<Integer> windows;

    private Animation collapse, expand;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        collapse = new CollapseAnimation( mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.COLLAPSE );
        expand = new CollapseAnimation( mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.EXPAND );

        languages = new LinkedList<>();
        windows = new LinkedList<>();

        for( Language l : Language.values() )
            languages.add( l.getId() );

        for( Window w : Window.values() )
            windows.add( w.getId() );

        updateView();
        return view;
    }

    @Override
    public void updateView(){

        mCreditMinMaxWrapper.setVisibility( View.GONE );
        if( app.getCreditRepresentation() ){
            mCreditGaugeSwitch.setChecked( true );
            mCreditMinMaxWrapper.setVisibility( View.VISIBLE );
        }

        mApplicationHomeScreenHint.setText( getString( app.getHomeScreen().getId() ) );
        mCreditGaugeMinHint.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMin() );
        mCreditGaugeMaxHint.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMax() );
        mApplicationLanguageHint.setText( getString( app.getLanguage().getId() ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for CREDIT
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_credit_gaugeSwitchWrapper )
    public void onGaugeSwitchWrapperClicked(){
        mCreditGaugeSwitch.setChecked( !mCreditGaugeSwitch.isChecked() );
        onGaugeSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_credit_gaugeSwitch )
    public void onGaugeSwitchChanged(){
        app.setCreditRepresentation( mCreditGaugeSwitch.isChecked() );
        if( mCreditGaugeSwitch.isChecked() )
            mCreditMinMaxWrapper.startAnimation( expand );
        else
            mCreditMinMaxWrapper.startAnimation( collapse );
    }

    @OnClick( R.id.pref_credit_gaugeMinWrapper )
    public void onWrapperGaugeMinClicked(){
        createEditTextDialog( getString( R.string.set_minimum ), getString( R.string.setting_minimum_message ) );
    }

    @OnClick( R.id.pref_credit_gaugeMaxWrapper )
    public void onWrapperGaugeMaxClicked(){
        createEditTextDialog( getString( R.string.set_maximum ), getString( R.string.settings_maximum_message ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_notifications_switchWrapper )
    public void onNotificationSwitchClicked(){
        onNotificationsSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_notifications_switch )
    public void onNotificationsSwitchChanged(){
        mNotificationsSwitch.setChecked( false );
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_languageWrapper )
    public void onLanguageWrapperClicked(){
        createRadioButtonDialog( getString( R.string.set_language ), getString( R.string.set_language_message ), app.getLanguage().getPos(), languages );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for APPLICATION
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_homeScreenWrapper )
    public void onHomeScreenClicked(){
        createRadioButtonDialog( getString( R.string.set_home_screen ), getString( R.string.set_home_screen_message ), app.getHomeScreen().getPos(), windows );
    }

    @OnClick( R.id.pref_application_buttonResetApplication )
    public void onResetApplicationButtonClicked(){
        createConfirmationDialog( getString( R.string.reset_application ), getString( R.string.reset_warning ), true );
    }

    @OnClick( R.id.pref_application_buttonResetDatabase )
    public void onResetDatabaseButtonClicked(){
        createConfirmationDialog( getString( R.string.reset_info_database ), getString( R.string.reset_info ), false );
    }


    @Override
    public void onDialogPositiveClick( BaseDialog dialog, int id ){
        if( dialog.getType().equals( getString( R.string.set_minimum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            if( !edit.getText().toString().equals( "" ) )
                app.setCreditRepresentationMin( Integer.parseInt( edit.getText().toString() ) );
        }else if( dialog.getType().equals( getString( R.string.set_maximum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            if( !edit.getText().toString().equals( "" ) )
                app.setCreditRepresentationMax( Integer.parseInt( edit.getText().toString() ) );
        }else if( dialog.getType().equals( getString( R.string.set_language ) ) ){
            switch( id ){
                case 0:
                    app.setLanguage( Language.DEFAULT );
                    break;
                case 1:
                    app.setLanguage( Language.ENGLISH );
                    break;
                case 2:
                    app.setLanguage( Language.DUTCH );
                    break;
                case 3:
                    app.setLanguage( Language.FRENCH );
                    break;
            }
            dialog.getDialog().cancel();

            Intent intent = new Intent( main, MainActivity.class );
            intent.putExtra( "localization", "updated" );
            intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
            startActivity( intent );
            main.finish();

        }else if( dialog.getType().equals( getString( R.string.set_home_screen ) ) ){
            switch( id ){
                case 0:
                    app.setHomeScreen( Window.CREDIT );
                    break;
                case 1:
                    app.setHomeScreen( Window.TRANSACTIONS );
                    break;
                case 2:
                    app.setHomeScreen( Window.SETTINGS );
                    break;
            }
            dialog.getDialog().cancel();
        }else if( dialog.getType().equals( getString( R.string.reset_application ) ) ){
            app.resetApp();
            createConfirmationDialog( getString( R.string.reset_info_application ), getString( R.string.reset_info ), false );
        }else if( dialog.getType().equals( getString( R.string.reset_info_application ) ) ){
            exitApplication();
        }else if( dialog.getType().equals( getString( R.string.reset_info_database ) ) ){
            app.resetDatabase();
            exitApplication();
        }
        updateView();
    }

    @Override
    public void onDialogNegativeClick( BaseDialog dialog ){
        dialog.getDialog().cancel();
    }

    private void createEditTextDialog( String type, String message ){
        EditTextDialog dialog = EditTextDialog.newInstance( type, message );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createConfirmationDialog( String type, String message, boolean isReset ){
        ConfirmationDialog dialog;
        if( isReset )
            dialog = ConfirmationDialog.newInstance( type, message, isReset );
        else
            dialog = ConfirmationDialog.newInstance( type, message );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createRadioButtonDialog( String type, String message, int currentId, List<Integer> options ){
        RadioButtonDialog dialog = RadioButtonDialog.newInstance( getContext(), type, message, currentId, options );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void exitApplication(){
        main.finish();
        System.exit( 0 );
    }


}
