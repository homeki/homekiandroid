package com.homeki.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClientRegistrationReceiver extends BroadcastReceiver {
	private static String TAG = ClientRegistrationReceiver.class.getName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "OnReceive(): " + intent.getAction() + " isHome: " + intent.getExtras().getBoolean("isHome"));
	}

}
