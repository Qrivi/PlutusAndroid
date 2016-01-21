package be.krivi.plutus.android.model;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Jan on 27/11/2015.
 */
public class Transaction{

    private Calendar cal;

    private Date timestamp;
    private int day;
    private Month month;
    private int year;
    private String time;

    private String title, description;
    private Location location;

    private double amount;
    private String type;

    public Transaction( Date timestamp, double amount, String type, String title, String description, Location location ){
        cal = Calendar.getInstance();
        cal.setTime( timestamp );

        setTimestamp( timestamp );
        setDay();
        setMonth();
        setYear();
        setTime();

        setTitle( title );
        setDescription( description );
        setLocation( location );

        setAmount( amount );
        setType( type );
    }

    public double getAmount(){
        return amount;
    }

    private void setAmount( double amount ){
        this.amount = amount;
    }

    public String getType(){
        return type;
    }

    private void setType( String type ){
        this.type = type;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    private void setTimestamp( Date timestamp ){
        this.timestamp = timestamp;
    }

    public String getTitle(){
        return title;
    }

    private void setTitle( String title ){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    private void setDescription( String description ){
        this.description = description;
    }

    public Location getLocation(){
        return location;
    }

    private void setLocation( Location location ){
        this.location = location;
    }

    public int getDay(){
        return day;
    }

    private void setDay(){
        day = cal.get( Calendar.DAY_OF_MONTH );
    }

    public String getTime(){
        return time;
    }

    private void setTime(){
        time = cal.get( Calendar.HOUR_OF_DAY ) + ":" + cal.get( Calendar.MINUTE );
    }

    public int getYear(){
        return year;
    }

    private void setYear(){
        year = cal.get( Calendar.YEAR );
    }

    public String getMonth( String format ){
        if( format.equals( "short" ) )
            return month.toShortString();
        else if( format.equals( "full" ) )
            return month.toString();
        return "";
    }

    public void setMonth(){
        switch( cal.get( Calendar.MONTH ) + 1 ){
            case 1:
                month = Month.JANUARY;
                break;
            case 2:
                month = Month.FEBRUARY;
                break;
            case 3:
                month = Month.MARCH;
                break;
            case 4:
                month = Month.APRIL;
                break;
            case 5:
                month = Month.MAY;
                break;
            case 6:
                month = Month.JUNE;
                break;
            case 7:
                month = Month.JULY;
                break;
            case 8:
                month = Month.AUGUST;
                break;
            case 9:
                month = Month.SEPTEMBER;
                break;
            case 10:
                month = Month.OCTOBER;
                break;
            case 11:
                month = Month.NOVEMBER;
                break;
            case 12:
                month = Month.DECEMBER;
                break;
            default:
                month = Month.NONE;
        }
    }
}
