package com.homeki.android;

import java.util.UUID;

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
	    		}
	    	};
	    }.start();		
	}
	
	public String getSsid(Context context) {
	   WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	   WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	   return wifiInfo.getSSID();
	}
}
