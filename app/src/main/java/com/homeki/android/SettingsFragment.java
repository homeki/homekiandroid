package com.homeki.android;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.homeki.android.reporter.GeofencingIntentService;
import com.homeki.android.settings.Settings;

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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                GeofencingIntentService.configureGeofence(context, Settings.isClientRegisteringEnabled(context));
                return null;
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}
