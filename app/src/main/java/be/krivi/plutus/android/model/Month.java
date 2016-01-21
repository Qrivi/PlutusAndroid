package be.krivi.plutus.android.model;

import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.PlutusAndroid;

/**
 * Created by Krivi on 24/12/15.
 */
public enum Month{
    NONE( R.string.not_applicable_short, R.string.not_applicable_full ),
    JANUARY( R.string.january_short, R.string.january_full ),
    FEBRUARY( R.string.february_short, R.string.february_full ),
    MARCH( R.string.march_short, R.string.march_full ),
    APRIL( R.string.april_short, R.string.april_full ),
    MAY( R.string.may_short, R.string.may_full ),
    JUNE( R.string.june_short, R.string.june_full ),
    JULY( R.string.july_short, R.string.july_full ),
    AUGUST( R.string.august_short, R.string.august_full ),
    SEPTEMBER( R.string.september_short, R.string.september_full ),
    OCTOBER( R.string.october_short, R.string.october_full ),
    NOVEMBER( R.string.november_short, R.string.november_full ),
    DECEMBER( R.string.december_short, R.string.december_full );

    private int shortName;
    private int fullName;

    Month( int shortName, int fullName ){
        this.shortName = shortName;
        this.fullName = fullName;
    }

    @Override
    public String toString(){
        return PlutusAndroid.getAppContext().getString( fullName );
    }

    public String toShortString(){
        return PlutusAndroid.getAppContext().getString( shortName );
    }
}
