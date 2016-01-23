package be.plutus.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.plutus.android.R;
import be.plutus.android.activity.MainActivity;
import be.plutus.android.application.Config;
import be.plutus.android.application.Language;
import be.plutus.android.application.Window;
import be.plutus.android.dialog.ConfirmationDialog;
import be.plutus.android.dialog.Dialog;
import be.plutus.android.dialog.EditTextDialog;
import be.plutus.android.dialog.RadioButtonDialog;
import be.plutus.android.view.CollapseAnimation;
import be.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import java.util.LinkedList;
import java.util.List;

public class SettingsFragment extends BaseFragment
{

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
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        collapse = new CollapseAnimation( mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.COLLAPSE );
        expand = new CollapseAnimation( mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.EXPAND );

        languages = new LinkedList<>();
        windows = new LinkedList<>();

        for ( Language l : Language.values() )
            languages.add( l.getId() );

        for ( Window w : Window.values() )
            windows.add( w.getId() );

        updateView();
        return view;
    }

    @Override
    public void updateView()
    {

        mCreditMinMaxWrapper.setVisibility( View.GONE );
        if ( app.getCreditRepresentation() )
        {
            mCreditGaugeSwitch.setChecked( true );
            mCreditMinMaxWrapper.setVisibility( View.VISIBLE );
        }

        mApplicationHomeScreenHint.setText( getString( app.getHomeScreen()
                .getId() ) );
        mCreditGaugeMinHint.setText( String.format( "%s %s", Config.API_DEFAULT_CURRENCY_SYMBOL, app.getCreditRepresentationMin() ) );
        mCreditGaugeMaxHint.setText( String.format( "%s %s", Config.API_DEFAULT_CURRENCY_SYMBOL, app.getCreditRepresentationMax() ) );
        mApplicationLanguageHint.setText( getString( app.getLanguage()
                .getId() ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for CREDIT
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_credit_gaugeSwitchWrapper )
    public void onGaugeSwitchWrapperClicked()
    {
        mCreditGaugeSwitch.setChecked( !mCreditGaugeSwitch.isChecked() );
        onGaugeSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_credit_gaugeSwitch )
    public void onGaugeSwitchChanged()
    {
        app.setCreditRepresentation( mCreditGaugeSwitch.isChecked() );
        mCreditMinMaxWrapper.startAnimation( ( mCreditGaugeSwitch.isChecked() ) ? expand : collapse );
    }

    @OnClick( R.id.pref_credit_gaugeMinWrapper )
    public void onWrapperGaugeMinClicked()
    {
        EditTextDialog dialog = Dialog.text( getContext(), getString( R.string.set_minimum ), getString( R.string.setting_minimum_message ), value -> {
            app.setCreditRepresentationMin( Integer.parseInt( value ) );
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_minimum ) );
    }

    @OnClick( R.id.pref_credit_gaugeMaxWrapper )
    public void onWrapperGaugeMaxClicked()
    {
        EditTextDialog dialog = Dialog.text( getContext(), getString( R.string.set_maximum ), getString( R.string.settings_maximum_message ), value -> {
            app.setCreditRepresentationMax( Integer.parseInt( value ) );
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_maximum ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_notifications_switchWrapper )
    public void onNotificationSwitchClicked()
    {
        onNotificationsSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_notifications_switch )
    public void onNotificationsSwitchChanged()
    {
        mNotificationsSwitch.setChecked( false );
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_languageWrapper )
    public void onLanguageWrapperClicked()
    {
        RadioButtonDialog dialog = Dialog.radio( getContext(), getString( R.string.set_language ), getString( R.string.set_language_message ), app.getLanguage()
                .getPos(), languages, value -> {
            Language language = Language.getByPos( value );
            app.setLanguage( language );

            Intent intent = new Intent( main, MainActivity.class );
            intent.putExtra( "localization", "updated" );
            intent.addFlags( Intent.FLAG_ACTIVITY_NO_ANIMATION );
            startActivity( intent );

            main.finish();
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_language ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for APPLICATION
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_homeScreenWrapper )
    public void onHomeScreenClicked()
    {
        RadioButtonDialog dialog = Dialog.radio( getContext(), getString( R.string.set_home_screen ), getString( R.string.set_home_screen_message ), app.getHomeScreen()
                .getPos(), windows, value -> {
            Window window = Window.getByPos( value );
            app.setHomeScreen( window );
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_language ) );
    }

    @OnClick( R.id.pref_application_buttonResetApplication )
    public void onResetApplicationButtonClicked()
    {
        // todo update string to reflect proper button behaviour
        ConfirmationDialog dialog = Dialog.reset( getContext(), getString( R.string.reset_application ), getString( R.string.reset_warning ), () -> {
            app.resetApp();
            exitApplication();
        } );
        dialog.show( getFragmentManager(), getString( R.string.reset_application ) );
    }

    @OnClick( R.id.pref_application_buttonResetDatabase )
    public void onResetDatabaseButtonClicked()
    {
        // todo update string to reflect proper button behaviour
        ConfirmationDialog dialog = Dialog.reset( getContext(), getString( R.string.reset_info_database ), getString( R.string.reset_info ), () -> {
            app.resetDatabase();
            exitApplication();
        } );
        dialog.show( getFragmentManager(), getString( R.string.reset_info_database ) );
    }

    private void exitApplication()
    {
        main.finish();
        System.exit( 0 );
    }

}
