package com.homeki.android.reporter;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeofencingReceiver extends BroadcastReceiver {
	private static String TAG = GeofencingReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
	}

    public static void configureGeofence(Context context, boolean enable) {
        GoogleApiClient gc = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();

        gc.blockingConnect();

        if (enable) {
            Log.i(TAG, "Enabling geofence for Homeki server location.");

            Intent intent = new Intent("homeki_geofence");
            List<Geofence> geofences = new ArrayList<>();
            geofences.add(new Geofence.Builder()
                    .setRequestId("homeki_geofence")
                    .setCircularRegion(59.327643, 18.049979, 150f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            LocationServices.GeofencingApi.addGeofences(gc, geofences, PendingIntent.getBroadcast(context, 0, intent, 0));
        } else {
            Log.i(TAG, "Disabling geofence for Homeki server location.");
            LocationServices.GeofencingApi.removeGeofences(gc, Arrays.asList("homeki_geofence"));
        }

        gc.disconnect();
    }
}
