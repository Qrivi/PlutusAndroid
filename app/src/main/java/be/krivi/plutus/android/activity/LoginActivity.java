package be.krivi.plutus.android.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.BuildConfig;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity{

    @Bind( R.id.wrapperInput )
    LinearLayout mWrapperInput;

    @Bind( R.id.txt_noInternet )
    TextView mInfo;

    @Bind( R.id.txt_studentId )
    EditText mStudentId;

    @Bind( R.id.txt_password )
    EditText mPassword;

    @Bind( R.id.studentIdStyle )
    TextInputLayout mStudentIdStyle;

    @Bind( R.id.passwordStyle )
    TextInputLayout mPasswordStyle;

    @Bind( R.id.btn_submit )
    Button mSubmit;

    Animation aFadeIn;
    Animation aFadeOut;

    boolean busy;
    boolean connected;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        ButterKnife.bind( this );

        if( app.isNewInstallation() && Config.APP_IS_BETA )
            Message.toast( this, BuildConfig.VERSION_NAME );

        if( app.isUserSaved() ){
            initializeMainWindow();
        }else{
            initializeLoginWindow();
            checkConnectivity();
        }
    }

    private void checkConnectivity(){
        if( app.isNetworkAvailable() ){
            connected = true;
            mInfo.setText( "" );
            mSubmit.setText( R.string.sign_in );
            showError( "OK", "OK" );
        }else{
            connected = false;
            mInfo.setText( R.string.there_is_no_active_internet_connection );
            mSubmit.setText( R.string.try_again );
            showError( "OK", "OK" );
        }
    }

    @OnClick( R.id.btn_info )
    public void launchWebsite(){
        startActivity(
                new Intent( Intent.ACTION_VIEW ).setData(
                        Uri.parse( Config.APP_URL + Config.APP_PRIVACY_POLICY ) ) );
    }

    @OnClick( R.id.btn_submit )
    public void verifyCredentials(){
        if( !connected ){
            checkConnectivity();
        }else if( !busy ){
            busy = true;

            String studentId = mStudentId.getText().toString().toLowerCase();
            String password = mPassword.getText().toString();

            String statusStudentId = app.verifyStudentId( studentId );
            String statusPassword = app.verifyPassword( password );

            if( statusStudentId.equals( "OK" ) && statusPassword.equals( "OK" ) )
                verifyCredentials( studentId, password );
            else
                showError( statusStudentId, statusPassword );
        }
    }

    private void verifyCredentials( final String studentId, final String password ){
        showFadeOut();

        Map<String, String> params = new HashMap<>();
        params.put( "studentId", studentId );
        params.put( "password", password );

        app.contactAPI( params, Config.API_ENDPOINT_VERIFY, new VolleyCallback(){
            @Override
            public void onSuccess( String response ){
                try{
                    JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                    if( data.getBoolean( "valid" ) ){
                        app.initializeUser( studentId, password, data.getString( "firstName" ), data.getString( "lastName" ) );
                        initializeMainWindow();
                    }else{
                        showError( "OK", getString( R.string.password_is_incorrect ) );
                    }
                }catch( JSONException e ){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( VolleyError error ){
                Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_endpoint_verify ) );
                checkConnectivity();
                mWrapperInput.startAnimation( aFadeIn );
                busy = false;
            }
        } );
    }

    private void showFadeOut(){
        mWrapperInput.startAnimation( aFadeOut );
    }

    private void showError( String errorStudentId, String errorPassword ){

        mWrapperInput.startAnimation( aFadeIn );

        mStudentIdStyle.setError( "" );
        mPasswordStyle.setError( "" );
        mPassword.setText( "" );

        if( !errorStudentId.equals( "OK" ) )
            mStudentIdStyle.setError( errorStudentId );
        if( !errorPassword.equals( "OK" ) )
            mPasswordStyle.setError( errorPassword );

        busy = false;
    }

    private void initializeMainWindow(){
        app.loadData();
        startActivity( new Intent( this, MainActivity.class ) );
        finish();
    }

    private void initializeLoginWindow(){
        if( !app.getStudentId().equals( "" ) )
            mStudentId.setText( app.getStudentId() );

        aFadeIn = AnimationUtils.loadAnimation( app, R.anim.fade_in );
        aFadeOut = AnimationUtils.loadAnimation( app, R.anim.fade_out );

        mPasswordStyle.setTypeface( Typeface.SANS_SERIF );
        mPassword.setOnEditorActionListener( new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ){
                verifyCredentials();
                return true;
            }
        } );
    }
}
