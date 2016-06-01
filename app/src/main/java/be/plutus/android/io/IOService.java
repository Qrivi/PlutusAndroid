package be.plutus.android.io;

import android.content.Context;
import be.plutus.android.model.Transaction;
import be.plutus.android.model.User;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Jan on 10/12/2015.
 */
public class IOService
{

    private DBAdapter dbAdapter;
    private SPAdapter spAdapter;

    public IOService( Context context )
    {
        this.dbAdapter = new DBAdapter( context );
        this.spAdapter = new SPAdapter( context );
    }

    public void saveHomeScreen( String homeScreen )
    {
        spAdapter.saveHomeScreen( homeScreen );
    }

    public void saveGaugeValue( float value )
    {
        spAdapter.saveGaugeValue( value );
    }

    public void saveCreditRepresentation( boolean bool )
    {
        spAdapter.saveCreditRepresentation( bool );
    }

    public void saveCreditRepresentationMin( int value )
    {
        spAdapter.saveCreditRepresentationMin( value );
    }

    public void saveCreditRepresentationMax( int value )
    {
        spAdapter.saveCreditRepresentationMax( value );
    }

    public void saveLanguageTag( String tag )
    {
        spAdapter.saveLanguageTag( tag );
    }

    public String getHomeScreen()
    {
        return spAdapter.getHomeScreen();
    }

    public float getGaugeValue()
    {
        return spAdapter.getGaugeValue();
    }

    public boolean getCreditRepresentation()
    {
        return spAdapter.getCreditRepresentation();
    }

    public int getCreditRepresentationMin()
    {
        return spAdapter.getCreditRepresentationMin();
    }

    public int getCreditRepresentationMax()
    {
        return spAdapter.getCreditRepresentationMax();
    }

    public String getLanguageTag()
    {
        return spAdapter.getLanguageTag();
    }

    public boolean isUserSaved()
    {
        return spAdapter.isUserSaved();
    }

    public void saveCredentials( User user )
    {
        spAdapter.saveCredentials( user );
    }

    public void saveCredit( double credit )
    {
        spAdapter.saveCredit( credit );
    }

    public void saveFetchDate( Date fetchDate )
    {
        spAdapter.saveFetchDate( fetchDate );
    }

    public void cleanSharedPreferences()
    {
        spAdapter.cleanSharedPreferences();
    }

    public void clearSharedPreferences()
    {
        spAdapter.clearSharedPreferences();
    }


    public String getStudentId()
    {
        return spAdapter.getStudentId();
    }

    public String getPassword()
    {
        return spAdapter.getPassword();
    }

    public double getCredit()
    {
        return spAdapter.getCredit();
    }

    public Date getFetchDate()
    {
        return spAdapter.getFetchDate();
    }

    public String getFirstName()
    {
        return spAdapter.getFirstName();
    }

    public String getLastName()
    {
        return spAdapter.getLastName();
    }

    public boolean writeTransactions( List<Transaction> transactions )
    {
        for ( Transaction transaction : transactions )
        {
            if ( insertTransaction( transaction ) )
                return false;
        }
        return true;
    }

    public boolean insertTransaction( Transaction t )
    {
        dbAdapter.insertLocation( t.getLocation() );
        return dbAdapter.insertTransaction( t ) < 0;
    }

    public List<Transaction> getAllTransactions() throws ParseException
    {
        return dbAdapter.getAllTransactions();
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException
    {
        return dbAdapter.getTransaction( timestamp );
    }

    public void cleanDatabase()
    {
        dbAdapter.truncateTables();
        spAdapter.clearCredit();
    }

    public void clearDatabase()
    {
        dbAdapter.dropTables();
        dbAdapter.deleteDatabase();
    }


    public boolean isNewInstallation()
    {
        return spAdapter.isNewInstallation();
    }

    public boolean isDatabaseIncomplete()
    {
        return spAdapter.isDatabaseIncomplete();
    }

    public void saveNewInstallation( boolean bool )
    {
        spAdapter.saveNewInstallation( bool );
    }

    public void saveDatabaseIncomplete( boolean bool )
    {
        spAdapter.saveDatabaseIncomplete( bool );
    }
}
