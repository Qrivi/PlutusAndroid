package be.plutus.android.api.model;

import be.plutus.android.model.Location;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Transaction
{

    @SerializedName( "amount" )
    private double amount;

    @SerializedName( "type" )
    private String type;

    @SerializedName( "timestamp" )
    private Date timestamp;

    @SerializedName( "details" )
    private Details details;

    @SerializedName( "location" )
    private Location location;

    public double getAmount()
    {
        return amount;
    }

    public String getType()
    {
        return type;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public Details getDetails()
    {
        return details;
    }

    public Location getLocation()
    {
        return location;
    }

    public static class Details
    {
        @SerializedName( "title" )
        private String title;

        @SerializedName( "description" )
        private String description;

        public String getTitle()
        {
            return title;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public be.plutus.android.model.Transaction convert()
    {
        return new be.plutus.android.model.Transaction( getTimestamp(), getAmount(), getType(), details.getTitle(), details.getDescription(), getLocation() );
    }

}
