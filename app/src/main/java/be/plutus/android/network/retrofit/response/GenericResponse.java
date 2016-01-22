package be.plutus.android.network.retrofit.response;

import com.google.gson.annotations.SerializedName;

public class GenericResponse <T extends Object>
{

    @SerializedName( "error" )
    private String error;

    @SerializedName( "data" )
    private T data;

    public String getError()
    {
        return error;
    }

    public T getData()
    {
        return data;
    }

}