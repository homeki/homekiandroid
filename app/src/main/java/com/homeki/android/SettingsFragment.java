package com.homeki.android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.homeki.android.reporter.GeofencingIntentService;
import com.homeki.android.reporter.ReporterTask;
import com.homeki.android.server.RestClient;
import com.homeki.android.misc.Settings;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Preference button = findPreference("client_registering");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference pref) {
				setClientRegistering_Click(pref.getContext());
				return true;
			}
		});
	}

	private void setClientRegistering_Click(final Context context) {
		AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				boolean enabled = Settings.isClientRegisteringEnabled(context);

				if (enabled) {
					RestClient client = new RestClient(context);
					Settings.setServerLocation(context, client.getServerLocation());
				} else {
					// unregister one last time
					Settings.setHome(context, false);
					new ReporterTask(context).run();
				}

				GeofencingIntentService.configureGeofence(context, enabled);
			}
		});
	}
}
