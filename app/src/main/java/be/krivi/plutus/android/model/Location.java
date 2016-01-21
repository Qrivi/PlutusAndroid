package be.krivi.plutus.android.model;

/**
 * Created by Jan on 27/11/2015.
 */
public class Location{

    private String name;
    private double lat;
    private double lng;


    public Location( String name, double lat, double lng ){
        setName( name );
        setLat( lat );
        setLng( lng );
    }

    public String getName(){
        return name;
    }

    private void setName( String name ){
        this.name = name;
    }

    public double getLat(){
        return lat;
    }

    private void setLat( double lat ){
        this.lat = lat;
    }

    public double getLng(){
        return lng;
    }

    private void setLng( double lng ){
        this.lng = lng;
    }

}
