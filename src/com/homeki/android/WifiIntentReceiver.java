package com.homeki.android;

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
	    if (!networkInfo.isConnected())
	    	return;
	    
	    Log.d(TAG, "Device connected to WLAN");
	    
	    final String ip = getWifiIPAddress(context);
	    Log.d(TAG, "IP address on WLAN: " + getWifiIPAddress(context));
	    
	    new Thread() {
	    	public void run() {
	    		Log.i(TAG, "Registering client to Homeki server.");
	    		try {
		    	    RestClient client = new RestClient(context);
		    	    client.registerClient(ip);
	    		}
	    		catch (Exception e) {
	    			Log.e(TAG, "Failed to register client to Homeki server, message: " + e.getMessage());
	    		}
	    	};
	    }.start();
	}
	
	public String getWifiIPAddress(Context context) {
	   WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	   int ip = wifiInfo.getIpAddress();

	   String ipString = String.format("%d.%d.%d.%d",
			   (ip & 0xff),
			   (ip >> 8 & 0xff),
			   (ip >> 16 & 0xff),
			   (ip >> 24 & 0xff));

	   return ipString;
	}
}
