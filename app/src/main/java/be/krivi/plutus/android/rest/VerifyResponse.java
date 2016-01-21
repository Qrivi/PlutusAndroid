package be.krivi.plutus.android.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jan on 15/12/2015.
 */
public class VerifyResponse{


    @SerializedName( "data" )
    private Data data;


    public Data getData(){
        return data;
    }

    public static class Data{

        @SerializedName( "valid" )
        private boolean valid;


        public boolean isValid(){
            return valid;
        }
    }
}
