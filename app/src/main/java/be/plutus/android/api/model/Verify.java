package be.plutus.android.api.model;

import com.google.gson.annotations.SerializedName;

public class Verify
{

    @SerializedName( "valid" )
    private boolean valid;

    @SerializedName( "firstName" )
    private String firstName;

    @SerializedName( "lastName" )
    private String lastName;

    public boolean isValid()
    {
        return valid;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

}
