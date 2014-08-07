package com.homeki.android.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.gms.maps.model.LatLng;

public class Settings {
	public static void setServerUrl(Context context, String serverUrl) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("server_url", serverUrl).commit();
	}

	public static String getServerUrl(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("server_url", "");
	}

	public static int getServerPort(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Integer.parseInt(prefs.getString("server_port", "5000"));
	}

	public static void setServerPort(Context context, int port) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("server_port", String.valueOf(port)).commit();
	}

	public static String getRegisteredClientGuid(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("registered_client_guid", null);
	}

	public static void setRegisteredClientGuid(Context context, String clientGuid) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString("registered_client_guid", clientGuid).commit();
	}

	public static boolean isClientRegisteringEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("client_registering", false);
	}

	public static float getClientRegisteringRadius(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return Float.valueOf(prefs.getString("client_registering_radius", "150"));
	}

	public static void setHome(Context context, boolean home) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putBoolean("is_home", home).commit();
	}

	public static boolean isHome(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("is_home", false);
	}

	public static void setServerLocation(Context context, LatLng serverLocation) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit()
			.putLong("server_location_latitude", Double.doubleToRawLongBits(serverLocation.latitude))
			.putLong("server_location_longitude", Double.doubleToRawLongBits(serverLocation.longitude))
			.commit();
	}

	public static LatLng getServerLocation(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long latBits = prefs.getLong("server_location_latitude", Double.doubleToRawLongBits(0.0));
		long lonBits = prefs.getLong("server_location_longitude", Double.doubleToRawLongBits(0.0));
		return new LatLng(Double.longBitsToDouble(latBits), Double.longBitsToDouble(lonBits));
	}
}
