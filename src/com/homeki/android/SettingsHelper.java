package com.homeki.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsHelper {	
	public static String getStringValue(Context context, String key) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, "no_value");
	}

	public static void putStringValue(Context context, String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(key, value);
		e.commit();	
	}	
}
