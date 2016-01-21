package be.krivi.plutus.android.application;

import be.krivi.plutus.android.R;

import java.util.Locale;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Language{
    DEFAULT( R.string.language_system, 0, "System default", "", Locale.getDefault() ),
    ENGLISH( R.string.language_english, 1, "English", "en", Locale.forLanguageTag( "en" ) ),
    DUTCH( R.string.language_dutch, 2, "Nederlands", "nl", Locale.forLanguageTag( "nl" ) ),
    FRENCH( R.string.language_french, 3, "Français", "fr", Locale.forLanguageTag( "fr" ) );

    private int id;
    private int pos;
    private String localized;
    private String tag;
    private Locale locale;

    Language( int id, int pos, String localized, String tag, Locale locale ){
        this.id = id;
        this.pos = pos;
        this.localized = localized;
        this.tag = tag;
        this.locale = locale;
    }

    public int getId(){
        return id;
    }

    public int getPos(){
        return pos;
    }

    @Override
    public String toString(){
        return localized;
    }

    public String getTag(){
        return tag;
    }

    public Locale getLocale(){
        return locale;
    }
}