package be.plutus.android.network.retrofit;

import be.plutus.android.network.retrofit.model.Verify;
import be.plutus.android.network.retrofit.response.GenericResponse;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface RESTService
{

    @FormUrlEncoded
    @POST( "verify" )
    Call<GenericResponse<Verify>> verify( @Field( "studentId" ) String studentId, @Field( "password" ) String password );

}
