package com.homeki.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	public static void setServerUrl(Context context, String serverUrl) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("server_url", serverUrl).commit();
	}

	public static String getServerUrl(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("server_url", "");
	}

	public static String getServerPort(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("server_port", "5000");
	}
	
	public static boolean isClientRegisteringEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("client_registering", false);
	}
	
	public static String getHomeSsid(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("home_ssid", "");
	}
	
	public static void setHomeSsid(Context context, String homeSsid) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("home_ssid", homeSsid).commit();
	}
	
	public static void setOnHomeNetwork(Context context, String guid) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("home_client_guid", guid).commit();
	}
	
	public static String getOnHomeNetwork(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("home_client_guid", null);
	}
}
