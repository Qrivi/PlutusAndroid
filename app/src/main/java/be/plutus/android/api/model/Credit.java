package be.plutus.android.api.model;

import com.google.gson.annotations.SerializedName;

public class Credit
{

    @SerializedName( "amount" )
    private double amount;

    public double getAmount()
    {
        return amount;
    }

}
