package be.krivi.plutus.android.network.volley;

import com.android.volley.VolleyError;

/**
 * Created by Jan on 22/12/2015.
 */
public interface VolleyCallback{

    void onSuccess( String string );

    void onFailure( VolleyError error );

}
