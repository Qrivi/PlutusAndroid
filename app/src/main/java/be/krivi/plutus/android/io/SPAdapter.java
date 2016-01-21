package be.krivi.plutus.android.io;

import android.content.Context;
import android.content.SharedPreferences;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.model.User;

import java.util.Date;


/**
 * Created by Krivi on 10/12/15.
 */
public class SPAdapter{

    // TODO encrypt/decrypt password rather that saving/reading plain text

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SPAdapter( Context context ){
        sharedPreferences = context.getSharedPreferences( "PlutusPrefs", Context.MODE_PRIVATE );
        editor = sharedPreferences.edit();
    }


    public void saveHomeScreen( String homeScreen ){
        editor.putString( "home_screen", homeScreen );
        editor.commit();
    }

    public void saveGaugeValue( float value ){
        editor.putFloat( "gauge_value", value );
        editor.commit();
    }

    public void saveCreditRepresentation( boolean bool ){
        editor.putString( "credit_rep", bool + "" );
        editor.commit();
    }

    public void saveCreditRepresentationMin( int value ){
        editor.putInt( "credit_rep_min", value );
        editor.commit();
    }

    public void saveCreditRepresentationMax( int value ){
        editor.putInt( "credit_rep_max", value );
        editor.commit();
    }

    public void saveLanguageTag( String tag ){
        editor.putString( "language_tag", tag );
        editor.commit();
    }

    public String getHomeScreen(){
        return sharedPreferences.getString( "home_screen", Config.SETTINGS_DEFAULT_HOME_SCREEN );
    }

    public float getGaugeValue(){
        return sharedPreferences.getFloat( "gauge_value", 0.0f );
    }

    public boolean getCreditRepresentation(){
        return Boolean.parseBoolean( sharedPreferences.getString( "credit_rep", Config.SETTINGS_DEFAULT_CREDIT_GAUGE + "" ) );
    }

    public int getCreditRepresentationMin(){
        return sharedPreferences.getInt( "credit_rep_min", Config.SETTINGS_DEFAULT_CREDIT_GAUGE_MIN );
    }

    public int getCreditRepresentationMax(){
        return sharedPreferences.getInt( "credit_rep_max", Config.SETTINGS_DEFAULT_CREDIT_GAUGE_MAX );
    }

    public String getLanguageTag(){
        return sharedPreferences.getString( "language_tag", "" );
    }


    public boolean isUserSaved(){
        return ( sharedPreferences.contains( "student_id" ) && sharedPreferences.contains( "password" ) );
    }

    public boolean isNewInstallation(){
        return Boolean.parseBoolean( sharedPreferences.getString( "new_install", "true" ) );
    }

    public boolean isDatabaseIncomplete(){
        return Boolean.parseBoolean( sharedPreferences.getString( "incomplete_db", "true" ) );
    }

    public void saveNewInstallation( boolean bool ){
        editor.putString( "new_install", bool + "" );
        editor.commit();
    }

    public void saveDatabaseIncomplete( boolean bool ){
        editor.putString( "incomplete_db", bool + "" );
        editor.commit();
    }

    public void saveCredentials( User user ){
        editor.putString( "student_id", user.getStudentId() );
        editor.putString( "password", user.getPassword() );
        editor.putString( "first_name", user.getFirstName() );
        editor.putString( "last_name", user.getLastName() );
        editor.commit();
    }

    public void saveCredit( double credit ){
        editor.putString( "credit", credit + "" );
        editor.commit();
    }

    public void saveFetchDate( Date fetchDate ){
        if( fetchDate == null )
            editor.remove( "fetch_date" );
        else
            editor.putLong( "fetch_date", fetchDate.getTime() );
        editor.commit();
    }

    public void cleanSharedPreferences(){
        editor.remove( "password" );
        editor.remove( "first_name" );
        editor.remove( "last_name" );
        editor.remove( "credit" );
        editor.remove( "fetch_date" );
        editor.remove( "incomplete_db" );
        editor.commit();
    }

    public void clearSharedPreferences(){
        editor.clear();
        editor.commit();
    }

    public String getStudentId(){
        return sharedPreferences.getString( "student_id", "" );
    }

    public String getPassword(){
        return sharedPreferences.getString( "password", "" );
    }

    public double getCredit(){
        return Double.parseDouble( sharedPreferences.getString( "credit", "0" ) );
    }

    public String getFirstName(){
        return sharedPreferences.getString( "first_name", "" );
    }

    public String getLastName(){
        return sharedPreferences.getString( "last_name", "" );
    }

    public Date getFetchDate(){
        long milliseconds = sharedPreferences.getLong( "fetch_date", 0 );
        if( milliseconds == 0 )
            return null;
        return new Date( milliseconds );
    }
}
