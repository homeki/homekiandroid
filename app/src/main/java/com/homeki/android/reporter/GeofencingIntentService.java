package com.homeki.android.reporter;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.homeki.android.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeofencingIntentService extends IntentService {
	private static String TAG = GeofencingIntentService.class.getSimpleName();

    public GeofencingIntentService() {
        super("Homeki Geofencing Intent Service");
    }

    public static void configureGeofence(Context context, boolean enable) {
        GoogleApiClient client = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();

        client.blockingConnect();

        if (enable) {
            LatLng location = Settings.getServerLocation(context);
            float radius = Settings.getClientRegisteringRadius(context);

            Log.i(TAG, "Enabling geofence for Homeki server location " + location + ".");

            List<Geofence> geofences = new ArrayList<>();
            geofences.add(new Geofence.Builder()
                    .setRequestId("homeki_geofence")
                    .setCircularRegion(location.latitude, location.longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            Intent intent = new Intent(context, GeofencingIntentService.class);
            LocationServices.GeofencingApi.addGeofences(client, geofences, PendingIntent.getService(context, 0, intent, 0));
        } else {
            Log.i(TAG, "Disabling geofence for Homeki server location.");
            LocationServices.GeofencingApi.removeGeofences(client, Arrays.asList("homeki_geofence"));
        }

        client.disconnect();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        new ReporterTask(this, event.getGeofenceTransition()).run();
    }
}
