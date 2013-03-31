package com.homeki.android;

import java.util.UUID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.homeki.android.server.RestClient;
import com.homeki.android.settings.Settings;

public class WifiIntentReceiver extends BroadcastReceiver {
	private static String TAG = WifiIntentReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		if (!Settings.isClientRegisteringEnabled(context))
			return;
		
		Log.d(TAG, "Received event.");
		
		if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
			return;
		
		NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
	    
		if (networkInfo.isConnected())
	    	reportHome(context);
	    else
	    	reportNotHome(context);
	}
	
	private void reportHome(final Context context) {
		String ssid = getSsid(context);
		String homeSsid = Settings.getHomeSsid(context);
		final String guid = UUID.randomUUID().toString();
		
		Log.d(TAG, "SSID: " + ssid + ", home SSID: " + homeSsid + ".");
		
		if (!homeSsid.equals(ssid))
			return;
			
	    new Thread() {
	    	public void run() {
	    		Log.i(TAG, "Registering client to Homeki server.");
	    		try {
		    	    RestClient client = new RestClient(context);
		    	    client.registerClient(guid);
		    	    Settings.setOnHomeNetwork(context, guid);
	    		}
	    		catch (Exception e) {
	    			Log.e(TAG, "Failed to register client to Homeki server, message: " + e.getMessage());
	    			addNotification(context, "Failed to register client", "Homeki tried to register this device as a currently connected client after connecting to the WLAN, but failed. In future versions, it will be possible to manually retry the register attempt here.");
	    		}
	    	};
	    }.start();			
	}
	
	private void reportNotHome(final Context context) {
		final String guid = Settings.getOnHomeNetwork(context);
		
		if (guid == null)
			return;
		
	    new Thread() {
	    	public void run() {
	    		Log.i(TAG, "Unregistering client to Homeki server.");
	    		try {
		    	    RestClient client = new RestClient(context);
		    	    client.unregisterClient(guid);
		    	    Settings.setOnHomeNetwork(context, null);
	    		}
	    		catch (Exception e) {
	    			Log.e(TAG, "Failed to unregister client to Homeki server, message: " + e.getMessage());
	    			addNotification(context, "Failed to unregister client", "Homeki tried to unregister this device as a currently connected client after leaving the WLAN, but failed. In future versions, it will be possible to manually retry the unregister attempt here.");
	    		}
	    	};
	    }.start();		
	}
	
	private void addNotification(Context context, String title, String text) {
		// Prepare intent which is triggered if the
		// notification is selected
		/*Intent intent = new Intent(this, NotificationReceiver.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);*/

		Notification noti = new Notification.Builder(context)
		        .setContentTitle(title)
		        .setContentText(text)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .getNotification();
		        /*.setContentIntent(pIntent)
		        .addAction(R.drawable.icon, "Call", pIntent)
		        .addAction(R.drawable.icon, "More", pIntent)
		        .addAction(R.drawable.icon, "And more", pIntent)*/

		    
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti); 
	}
	
	public String getSsid(Context context) {
	   WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	   return wifiInfo.getSSID();
	}
}
