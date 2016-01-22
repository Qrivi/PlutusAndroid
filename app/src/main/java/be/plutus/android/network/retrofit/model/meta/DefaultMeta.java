package be.plutus.android.network.retrofit.model.meta;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DefaultMeta
{

    @SerializedName( "endpoint" )
    private String endpoint;

    @SerializedName( "timestamp" )
    private Date timestamp;

    @SerializedName( "studentId" )
    private String studentId;

    public String getEndpoint()
    {
        return endpoint;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getStudentId()
    {
        return studentId;
    }

}
