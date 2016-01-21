package be.krivi.plutus.android.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBAdapter{

    DBHelper helper;
    SQLiteDatabase db;
    Context context;

    public DBAdapter( Context context ){
        helper = new DBHelper( context );
        db = helper.getWritableDatabase();
        this.context = context;
    }

    public long insertLocation( Location l ){

        if( checkIfDataAlreadyExist( DBHelper.LOCATIONS_TABLE_NAME, DBHelper.LOCATIONS_NAME, l.getName() ) )
            return -1;

        ContentValues values = new ContentValues();
        values.put( DBHelper.LOCATIONS_NAME, l.getName() );
        values.put( DBHelper.LOCATIONS_LNG, l.getLng() );
        values.put( DBHelper.LOCATIONS_LAT, l.getLat() );
        return db.insert( DBHelper.LOCATIONS_TABLE_NAME, null, values );
    }

    public long insertTransaction( Transaction t ){

       DateFormat f = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US );
        if( checkIfDataAlreadyExist( DBHelper.TRANSACTIONS_TABLE_NAME, DBHelper.TRANSACTIONS_TIMESTAMP, f.format( t.getTimestamp() ) ) )
            return -1;

        ContentValues values = new ContentValues();
        values.put( DBHelper.TRANSACTIONS_TIMESTAMP, f.format( t.getTimestamp() ) );
        values.put( DBHelper.TRANSACTIONS_AMOUNT, t.getAmount() );
        values.put( DBHelper.TRANSACTIONS_TYPE, t.getType() );
        values.put( DBHelper.TRANSACTIONS_TITLE, t.getTitle() );
        values.put( DBHelper.TRANSACTIONS_DESCRIPTION, t.getDescription() );
        values.put( DBHelper.TRANSACTIONS_LOCATION, t.getLocation().getName() );
        return db.insert( DBHelper.TRANSACTIONS_TABLE_NAME, null, values );
    }

    public List<Transaction> getAllTransactions() throws ParseException{

        String query = "SELECT * " +
                "FROM " + DBHelper.TRANSACTIONS_TABLE_NAME + " t JOIN " + DBHelper.LOCATIONS_TABLE_NAME + " l " +
                "ON t." + DBHelper.TRANSACTIONS_LOCATION + " = l." + DBHelper.LOCATIONS_NAME +
                " ORDER BY " + DBHelper.TRANSACTIONS_TIMESTAMP + " DESC";


        Cursor cursor = db.rawQuery( query, null );

        List<Transaction> transactions = new LinkedList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US );

        while( cursor.moveToNext() ){

            Date timestamp = dateFormat.parse( cursor.getString( cursor.getColumnIndex( DBHelper.TRANSACTIONS_TIMESTAMP ) ) );
            double amount = cursor.getDouble( cursor.getColumnIndex( DBHelper.TRANSACTIONS_AMOUNT ) );
            String type = cursor.getString( cursor.getColumnIndex( DBHelper.TRANSACTIONS_TYPE ) );
            String title = cursor.getString( cursor.getColumnIndex( DBHelper.TRANSACTIONS_TITLE ) );
            String description = cursor.getString( cursor.getColumnIndex( DBHelper.TRANSACTIONS_DESCRIPTION ) );
            String location = cursor.getString( cursor.getColumnIndex( DBHelper.TRANSACTIONS_LOCATION ) );
            Double lat = cursor.getDouble( cursor.getColumnIndex(DBHelper.LOCATIONS_LAT) );
            Double lng = cursor.getDouble( cursor.getColumnIndex(DBHelper.LOCATIONS_LNG) );

            transactions.add( new Transaction( timestamp, amount, type, title, description, new Location( location, lat, lng) ) );

        }
        cursor.close();
        return transactions;
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException{

        DateFormat f = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US );

        String[] columns = {DBHelper.TRANSACTIONS_TIMESTAMP, DBHelper.TRANSACTIONS_AMOUNT, DBHelper.TRANSACTIONS_TYPE,
                DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_DESCRIPTION,
                DBHelper.LOCATIONS_NAME, DBHelper.LOCATIONS_LNG, DBHelper.LOCATIONS_LAT};
        Cursor cursor = db.query(
                DBHelper.TRANSACTIONS_TABLE_NAME + "INNER JOIN" + DBHelper.LOCATIONS_TABLE_NAME +
                        "USING(" + DBHelper.LOCATIONS_NAME + ")",
                columns,
                DBHelper.TRANSACTIONS_TIMESTAMP + "= '" + f.format( timestamp ) + "'",
                null, null, null, DBHelper.TRANSACTIONS_TIMESTAMP );


        cursor.moveToNext();

        Date t = f.parse( cursor.getString( 1 ) );
        double amount = cursor.getDouble( 2 );
        String type = cursor.getString( 3 );
        String title = cursor.getString( 4 );
        String description = cursor.getString( 5 );
        String location = cursor.getString( 6 );
        double lng = cursor.getDouble( 7 );
        double lat = cursor.getDouble( 8 );

        cursor.close();

        return new Transaction( t, amount, type, title, description, new Location( location, lng, lat ) );
    }

    public boolean checkIfDataAlreadyExist( String tableName, String dbfield, String fieldValue ){
        String query = "SELECT * FROM " + tableName + " WHERE " + dbfield + " = '" + fieldValue + "' ";

        Cursor cursor = db.rawQuery( query, null );
        if( cursor.getCount() <= 0 ){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void dropTables(){
        db.execSQL( "DROP TABLE IF EXISTS " + DBHelper.TRANSACTIONS_TABLE_NAME );
        db.execSQL( "DROP TABLE IF EXISTS " + DBHelper.LOCATIONS_TABLE_NAME );
    }

    public void deleteDatabase() {
        context.deleteDatabase( DBHelper.DB_NAME );
    }

    public void truncateTables(){
        db.execSQL( "DELETE FROM " + DBHelper.TRANSACTIONS_TABLE_NAME );
        db.execSQL( "DELETE FROM " + DBHelper.LOCATIONS_TABLE_NAME );
        db.execSQL( "VACUUM" );
    }

    static class DBHelper extends SQLiteOpenHelper{

        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "PlutusDB";

        private static final String TRANSACTIONS_TABLE_NAME = "transactions";
        private static final String TRANSACTIONS_TIMESTAMP = "_Timestamp";
        private static final String TRANSACTIONS_AMOUNT = "Amount";
        private static final String TRANSACTIONS_TYPE = "Type";
        private static final String TRANSACTIONS_TITLE = "Title";
        private static final String TRANSACTIONS_DESCRIPTION = "Description";
        private static final String TRANSACTIONS_LOCATION = "Location";

        private static final String LOCATIONS_TABLE_NAME = "locations";
        private static final String LOCATIONS_NAME = "Name";
        private static final String LOCATIONS_LNG = "Lng";
        private static final String LOCATIONS_LAT = "Lat";

        public DBHelper( Context context ){
            super( context, DB_NAME, null, DB_VERSION );
        }

        @Override
        public void onCreate( SQLiteDatabase db ){
            String query = "CREATE TABLE " + LOCATIONS_TABLE_NAME + " ( " +
                    LOCATIONS_NAME + " VARCHAR(45) PRIMARY KEY NOT NULL, " +
                    LOCATIONS_LNG + " DOUBLE NOT NULL, " +
                    LOCATIONS_LAT + " DOUBLE NOT NULL );";
            String query2 =
                    "CREATE TABLE " + TRANSACTIONS_TABLE_NAME + " ( " +
                            TRANSACTIONS_TIMESTAMP + " TIMESTAMP PRIMARY KEY NOT NULL, " +
                            TRANSACTIONS_AMOUNT + " DOUBLE NOT NULL, " +
                            TRANSACTIONS_TYPE + " VARCHAR(7) NOT NULL, " +
                            TRANSACTIONS_TITLE + " VARCHAR(25) NOT NULL, " +
                            TRANSACTIONS_DESCRIPTION + " VARCHAR(255) NOT NULL, " +
                            TRANSACTIONS_LOCATION + " VARCHAR(25) NOT NULL, " +
                            " FOREIGN KEY( " + TRANSACTIONS_LOCATION + " ) REFERENCES " + LOCATIONS_TABLE_NAME + " ( " + LOCATIONS_NAME + " ))";
            try{
                db.execSQL( "PRAGMA foreign_keys=ON;" );
                db.execSQL( query );
                db.execSQL( query2 );
            }catch( SQLException e ){
                //TODO write exception
            }
        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int i, int i1 ){

            try{
                db.execSQL( "DROP TABLE IF EXIST" + TRANSACTIONS_LOCATION );
                db.execSQL( "DROP TABLE IF EXIST" + LOCATIONS_TABLE_NAME );
                onCreate( db );
            }catch( SQLException e ){
                //TODO write exception
            }
        }
    }
}
