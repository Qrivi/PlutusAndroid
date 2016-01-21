package be.krivi.plutus.android.network.volley;

import android.util.Base64;
import be.krivi.plutus.android.application.Config;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 8/12/2015.
 */
public class Client{

    private RequestQueue mRequestQueue;

    public Client(){
        this.mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    public void contactAPI( final Map<String, String> params, String endpoint, final VolleyCallback callback ){

        //Log.v( "Contacting API", endpoint);
        final String URL = Config.API_URL + Config.API_VERSION + endpoint;

        StringRequest request = new StringRequest( Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        callback.onSuccess( response );
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        callback.onFailure( error );
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        request.setShouldCache(false);
        mRequestQueue.add( request );
    }

    public Map<String, String> getCustomHeaders() throws AuthFailureError{
        Map<String, String> headers = new HashMap<>();
        String credentials = Config.API_LOGIN + ":" + Config.API_PASSWORD;
        String auth = "Basic "
                + Base64.encodeToString( credentials.getBytes(),
                Base64.NO_WRAP );
        headers.put( "Authorization", auth );
        return headers;
    }

}



