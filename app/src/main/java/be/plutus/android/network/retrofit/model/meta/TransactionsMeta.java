package be.plutus.android.network.retrofit.model.meta;

import com.google.gson.annotations.SerializedName;

public class TransactionsMeta extends DefaultMeta
{

    @SerializedName( "page" )
    private int page;

    public int getPage()
    {
        return page;
    }

}
