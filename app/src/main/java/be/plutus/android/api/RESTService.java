package be.plutus.android.api;

import be.plutus.android.api.response.CreditResponse;
import be.plutus.android.api.response.TransactionsResponse;
import be.plutus.android.api.response.VerifyResponse;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

public interface RESTService
{

    @FormUrlEncoded
    @POST( "verify" )
    Call<VerifyResponse> verify( @Field( "studentId" ) String studentId, @Field( "password" ) String password );

    @FormUrlEncoded
    @POST( "credit" )
    Call<CreditResponse> credit( @Field( "studentId" ) String studentId, @Field( "password" ) String password );

    @FormUrlEncoded
    @POST( "transactions/{page}" )
    Call<TransactionsResponse> transactions( @Field( "studentId" ) String studentId, @Field( "password" ) String password, @Path( "page" ) int page );

}
