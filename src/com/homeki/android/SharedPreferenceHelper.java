package com.homeki.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceHelper {	
	
	public static String getStringValue(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences("", Context.MODE_PRIVATE);		
		return sp.getString(key, "no_value");
	}

	public static void putStringValue(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("", Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(key, value);
		e.commit();	
	}	
}
