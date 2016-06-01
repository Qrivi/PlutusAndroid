package be.plutus.android.api.response;

import be.plutus.android.api.model.meta.DefaultMeta;
import com.google.gson.annotations.SerializedName;

public abstract class GenericResponse <M extends DefaultMeta, O extends Object>
{

    @SerializedName( "error" )
    private String error;

    @SerializedName( "meta" )
    private M meta;

    @SerializedName( "data" )
    private O data;

    public String getError()
    {
        return error;
    }

    public M getMeta()
    {
        return meta;
    }

    public O getData()
    {
        return data;
    }

}