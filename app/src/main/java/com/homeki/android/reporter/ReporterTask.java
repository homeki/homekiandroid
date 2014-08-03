package com.homeki.android.reporter;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.homeki.android.R;
import com.homeki.android.server.RestClient;
import com.homeki.android.settings.Settings;

import java.util.UUID;

public class ReporterTask implements Runnable {
    private static final String TAG = ReporterTask.class.getSimpleName();

    private final Context context;
    private final int transition;

    public ReporterTask(Context context, int transition) {
        this.context = context;
        this.transition = transition;
    }

    private void postFailureNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Homeki")
                .setContentText("Failed to register/unregister client to Homeki server.");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1221, builder.getNotification());
    }

    private void reportHome(Context context) {
        if (Settings.getRegisteredClientGuid(context) != null) {
            Log.i(TAG, "Already have client guid, skipped reporting home.");
            return;
        }

        RestClient client = new RestClient(context);
        String guid = UUID.randomUUID().toString();
        try {
            client.registerClient(guid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Settings.setRegisteredClientGuid(context, guid);
        Log.i(TAG, "Successfully registered client as home with guid " + guid + ".");
    }

    private void reportNotHome(Context context) {
        if (Settings.getRegisteredClientGuid(context) == null) {
            Log.i(TAG, "No client guid, skipped reporting not home.");
            return;
        }

        RestClient client = new RestClient(context);
        String guid = Settings.getRegisteredClientGuid(context);
        try {
            client.unregisterClient(guid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Settings.setRegisteredClientGuid(context, null);
        Log.i(TAG, "Successfully unregistered client as home with guid " + guid + ".");
    }

    @Override
    public void run() {
        try {
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                reportHome(context);
            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                reportNotHome(context);
            } else {
                Log.i(TAG, "Received transition not handled.");
            }
        } catch (Exception e) {
            postFailureNotification();
        }
    }
}
