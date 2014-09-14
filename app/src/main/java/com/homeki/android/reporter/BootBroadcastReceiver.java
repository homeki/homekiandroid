package com.homeki.android.reporter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.homeki.android.misc.Settings;

public class BootBroadcastReceiver extends BroadcastReceiver {
	private static String TAG = GeofencingIntentService.class.getSimpleName();

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i(TAG, "At boot, checking if geofencing should be re-enabled (lost when rebooting).");
		AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				GeofencingIntentService.configureGeofence(context, Settings.isClientRegisteringEnabled(context));
			}
		});
	}
}
