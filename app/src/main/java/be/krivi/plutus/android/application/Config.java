package be.krivi.plutus.android.application;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Krivi on 13/12/15.
 */
public abstract class Config{

    public static String APP_URL_REPO = "https://qrivi.github.io/Plutus";

    public static String APP_URL = "http://www.plutus.be";
    public static String APP_PRIVACY_POLICY = "/";

    public static String API_URL = "http://plutus.be/api"; // will of course be https in the future!
    public static String API_VERSION = "/v1";

    public static String API_LOGIN = "Plutus";
    public static String API_PASSWORD = "6298e5dbc7c0475c2273a8e2371695d4756b8f45";

    public static String API_ENDPOINT_VERIFY = "/verify";
    public static String API_ENDPOINT_CREDIT = "/credit";
    public static String API_ENDPOINT_TRANSACTIONS = "/transactions";

    public static String API_DEFAULT_CURRENCY = "EUR";
    public static String API_DEFAULT_CURRENCY_SYMBOL = "€";

    public static int APP_DEFAULT_SNOOZE_TIME = 60;
    public static int APP_DEFAULT_LIST_SIZE = 25;
    public static LatLng APP_DEFAULT_MAP_LATLNG = new LatLng( 50.711243,4.768066 ); // center of BE
    public static int APP_DEFAULT_MAP_ZOOM = 20;

    public static boolean SETTINGS_DEFAULT_CREDIT_GAUGE = true;
    public static int SETTINGS_DEFAULT_CREDIT_GAUGE_MIN = 10;
    public static int SETTINGS_DEFAULT_CREDIT_GAUGE_MAX = 50;
    public static boolean SETTINGS_DEFAULT_NOTIFICATIONS = false;
    public static String SETTINGS_DEFAULT_HOME_SCREEN = "credit";

    public static boolean APP_IS_BETA = true;

}
