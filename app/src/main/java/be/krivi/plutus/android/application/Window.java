package be.krivi.plutus.android.application;

import be.krivi.plutus.android.R;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Window{
    CREDIT( R.string.credit, 0, "credit" ),
    TRANSACTIONS( R.string.transactions, 1, "transactions" ),
    SETTINGS( R.string.settings, 2, "settings" );

    private int id;
    private int pos;
    private String origin;

    Window( int id, int pos, String origin ){
        this.id = id;
        this.pos = pos;
        this.origin = origin;
    }

    public int getId(){
        return id;
    }

    public int getPos(){
        return pos;
    }

    @Override
    public String toString(){
        return origin;
    }
}