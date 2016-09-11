package airpnp.pennapps.com.airpnp;

import android.app.Application;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class MyApplication extends Application {

    public static HashMap<String, Marker> markerHashMap = new HashMap<>();
}



