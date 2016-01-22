package be.plutus.android.network.retrofit.model;

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
