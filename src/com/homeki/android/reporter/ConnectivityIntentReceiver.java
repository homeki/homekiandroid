package com.homeki.android.reporter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.homeki.android.settings.Settings;

public class ConnectivityIntentReceiver extends BroadcastReceiver {
	private static String TAG = ConnectivityIntentReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!Settings.isClientRegisteringEnabled(context)) return;
		if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) return;

    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();

    if (networkInfo == null) return;
    if (!networkInfo.isConnectedOrConnecting()) return;

    ReporterAlarmReceiver.setAlarm(context);
    Log.i(TAG, "Connectivity action received, alarm set.");
	}
}
