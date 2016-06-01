package be.plutus.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils
{

    /**
     * Formats a date using the HH:mm format
     *
     * @param date The date that will be formatted
     * @return a formatted date using the HH:mm format
     */
    public static String toTime( Date date )
    {
        SimpleDateFormat format = new SimpleDateFormat( "HH:mm", Locale.getDefault() );
        return format.format( date );
    }

    /**
     * Formats a date using the locale date format
     *
     * @param date The date that will be formatted
     * @return if the locale is US a date of the format 'EEEE MMMM d, yyyy' will be returned, otherwise a date of the format 'EEEE d MMMM yyyy' will be returned
     */
    public static String toDate( Date date )
    {
        SimpleDateFormat format;
        Locale locale = Locale.getDefault();

        if ( locale.equals( Locale.US ) )
            format = new SimpleDateFormat( "EEEE MMMM d, yyyy", Locale.US );
        else
            format = new SimpleDateFormat( "EEEE d MMMM yyyy", Locale.getDefault() );

        return format.format( date );
    }

}
