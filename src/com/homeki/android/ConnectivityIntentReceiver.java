package com.homeki.android;

import java.util.UUID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.homeki.android.server.RestClient;
import com.homeki.android.settings.Settings;

public class ConnectivityIntentReceiver extends BroadcastReceiver {
	private static String TAG = ConnectivityIntentReceiver.class.getSimpleName();
	private static final Object syncLock = new Object();

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (!Settings.isClientRegisteringEnabled(context))
			return;

		if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
			return;

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo == null)
			return;

		if (networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//			reportHome(context);
			Intent startIntent = new Intent();
			startIntent.setAction("com.homeki.android.CLIENT_CONNECTION");
			startIntent.putExtra("isHome", true);
			context.sendBroadcast(startIntent);
		}
		else if (networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
			reportNotHome(context);
	}

	private void reportHome(final Context context) {
		try {
			if (Settings.getOnHomeNetwork(context) != null)
				return;

			String ssid = getSsid(context);
			String homeSsid = Settings.getHomeSsid(context);
			String guid = UUID.randomUUID().toString();

			if (!homeSsid.equals(ssid))
				return;

			RestClient client = new RestClient(context);
			client.registerClient(guid);
			Settings.setOnHomeNetwork(context, guid);
			Log.i(TAG, "Registered client to Homeki server.");
		} catch (Exception e) {
			Log.e(TAG, "Failed to register client to Homeki server, message: " + e.getMessage());
			addNotification(context, "Failed to register client",
					"Homeki tried to register this device as a currently connected client after connecting to the WLAN, but failed. In future versions, it will be possible to manually retry the register attempt here.");
		}

	}

	private void reportNotHome(final Context context) {
		new Thread() {
			public void run() {
				synchronized (syncLock) {
					try {
						String guid = Settings.getOnHomeNetwork(context);

						if (guid == null)
							return;

						RestClient client = new RestClient(context);
						client.unregisterClient(guid);
						Settings.setOnHomeNetwork(context, null);
						Log.i(TAG, "Unregistered client to Homeki server.");
					} catch (Exception e) {
						Log.e(TAG, "Failed to unregister client to Homeki server, message: " + e.getMessage());
						addNotification(context, "Failed to unregister client",
								"Homeki tried to unregister this device as a currently connected client after leaving the WLAN, but failed. In future versions, it will be possible to manually retry the unregister attempt here.");
					}
				}
			};
		}.start();
	}

	private void addNotification(Context context, String title, String text) {
		// Prepare intent which is triggered if the
		// notification is selected
		/*
		 * Intent intent = new Intent(this, NotificationReceiver.class);
		 * PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
		 * 0);
		 */

		Notification noti = new Notification.Builder(context).setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.ic_launcher).getNotification();
		/*
		 * .setContentIntent(pIntent) .addAction(R.drawable.icon, "Call",
		 * pIntent) .addAction(R.drawable.icon, "More", pIntent)
		 * .addAction(R.drawable.icon, "And more", pIntent)
		 */

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}

	public String getSsid(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getSSID();
	}
}
