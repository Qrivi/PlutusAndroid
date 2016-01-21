package be.krivi.plutus.android.network.retrofit;

import be.krivi.plutus.android.rest.VerifyResponse;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Jan on 14/12/2015.
 */
public interface Client{

    @FormUrlEncoded
    @POST( "/verify" )
    Call<VerifyResponse> verify(
            @Field( "studentId" ) String studentId,
            @Field( "password" ) String password );
}
