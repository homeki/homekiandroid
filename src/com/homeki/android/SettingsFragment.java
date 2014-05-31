package com.homeki.android;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.homeki.android.settings.Settings;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		addPreferencesFromResource(R.xml.preferences);
		
		Preference button = findPreference("set_home_ssid");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference pref) {
        setHomeSsid_Click(pref.getContext());
        return true;
      }
    });
	}
	
	private void setHomeSsid_Click(Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		
		if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
			Toast.makeText(context, "Not connected to any WLAN, can't set home network.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String ssid = wifiInfo.getSSID();
		Settings.setHomeSsid(context, ssid);
		Toast.makeText(context, "Currently connected network (" + ssid + ") set as home network.", Toast.LENGTH_SHORT).show();
	}
}
