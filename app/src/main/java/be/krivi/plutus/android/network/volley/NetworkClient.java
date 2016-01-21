package be.krivi.plutus.android.network.volley;

import java.util.Map;

/**
 * Created by Jan on 17/12/2015.
 */
public class NetworkClient{

    private Client client;

    public NetworkClient(){
        this.client = new Client();
    }

    public void contactAPI( Map<String, String> params, String endpoint, final VolleyCallback callback ) {
        client.contactAPI( params, endpoint, callback );
    }

}
