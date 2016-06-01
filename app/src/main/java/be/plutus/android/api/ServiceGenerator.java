package be.plutus.android.api;

import android.util.Base64;
import be.plutus.android.application.Config;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

public class ServiceGenerator
{

    private static OkHttpClient httpClient = new OkHttpClient();
    private static com.google.gson.Gson Gson = new GsonBuilder().setDateFormat( "yyyy-MM-dd'T'HH:mm:ss" )
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl( Config.API_URL + Config.API_VERSION + "/" )
            .addConverterFactory( GsonConverterFactory.create( Gson ) );

    public static <S> S create( Class<S> serviceClass )
    {
        final String credentials = Config.API_LOGIN + ":" + Config.API_PASSWORD;
        final String basic = "Basic " + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP );

        httpClient.interceptors()
                .clear();
        httpClient.interceptors()
                .add( new Interceptor()
                {
                    @Override
                    public Response intercept( Chain chain ) throws IOException
                    {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .header( "Authorization", basic )
                                .method( original.method(), original.body() );

                        Request request = requestBuilder.build();
                        return chain.proceed( request );
                    }
                } );

        Retrofit retrofit = builder.client( httpClient )
                .build();
        return retrofit.create( serviceClass );
    }

}