package com.homeki.android.reporter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.homeki.android.settings.Settings;

public class GeofencingReceiver extends BroadcastReceiver {
	private static String TAG = GeofencingReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!Settings.isClientRegisteringEnabled(context)) return;
		if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) return;


	}

    public static void registerGeofence(Context context) {
        //GoogleApiClient gc;
    }
}
