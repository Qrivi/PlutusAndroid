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
import be.plutus.android.animations.CollapseAnimation;
import be.plutus.android.config.Config;
import be.plutus.android.model.Language;
import be.plutus.android.model.Window;
import be.plutus.android.dialog.ConfirmDialog;
import be.plutus.android.dialog.Dialog;
import be.plutus.android.dialog.InputDialog;
import be.plutus.android.dialog.OptionDialog;
import be.plutus.android.utils.Message;
import be.plutus.android.activity.MainActivity;
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
        InputDialog dialog = Dialog.input( getContext(), getString( R.string.set_minimum ), getString( R.string.setting_minimum_message ), d -> {
            app.setCreditRepresentationMin( Integer.parseInt( ( (InputDialog) d ).getValue() ) );
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_minimum ) );
    }

    @OnClick( R.id.pref_credit_gaugeMaxWrapper )
    public void onWrapperGaugeMaxClicked()
    {
        InputDialog dialog = Dialog.input( getContext(), getString( R.string.set_maximum ), getString( R.string.settings_maximum_message ), d -> {
            app.setCreditRepresentationMax( Integer.parseInt( ( (InputDialog) d ).getValue() ) );
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
        OptionDialog dialog = Dialog.option( getContext(), getString( R.string.set_language ), getString( R.string.set_language_message ), app.getLanguage()
                .getPos(), languages, d -> {
            int pos = ( (OptionDialog) d ).getValue();

            if ( pos == -1 )
                return;

            Language language = Language.getByPos( pos );
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
        OptionDialog dialog = Dialog.option( getContext(), getString( R.string.set_home_screen ), getString( R.string.set_home_screen_message ), app.getHomeScreen()
                .getPos(), windows, d -> {
            int pos = ( (OptionDialog) d ).getValue();

            if ( pos == -1 )
                return;

            Window window = Window.getByPos( pos );
            app.setHomeScreen( window );
            updateView();
        } );
        dialog.show( getFragmentManager(), getString( R.string.set_language ) );
    }

    @OnClick( R.id.pref_application_buttonResetApplication )
    public void onResetApplicationButtonClicked()
    {
        ConfirmDialog dialog = Dialog.reset( getContext(), getString( R.string.reset_application ), getString( R.string.reset_info_application ), d -> {
            app.resetApp();
            exitApplication();
        } );
        dialog.show( getFragmentManager(), getString( R.string.reset_application ) );
    }

    @OnClick( R.id.pref_application_buttonResetDatabase )
    public void onResetDatabaseButtonClicked()
    {
        ConfirmDialog dialog = Dialog.reset( getContext(), getString( R.string.reset_database ), getString( R.string.reset_info_database ), d -> {
            app.resetDatabase();
            exitApplication();
        } );
        dialog.show( getFragmentManager(), getString( R.string.reset_database ) );
    }

    private void exitApplication()
    {
        main.finish();
        System.exit( 0 );
    }

}
