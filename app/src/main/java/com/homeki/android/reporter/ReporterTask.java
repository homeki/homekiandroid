package com.homeki.android.reporter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.homeki.android.R;
import com.homeki.android.server.ApiClient;
import com.homeki.android.misc.Settings;

import java.util.UUID;

public class ReporterTask implements Runnable {
	private static final String TAG = ReporterTask.class.getSimpleName();

	private final Context context;

	public ReporterTask(Context context) {
		this.context = context;
	}

	private void postFailureNotification() {
		Intent intent = new Intent(context, RetryIntentService.class);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Homeki")
			.setContentText("Failed to register/unregister client to Homeki server.")
			.addAction(R.drawable.ic_action_refresh, "Retry", PendingIntent.getService(context, 0, intent, 0));
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1221, builder.build());
	}

	private void reportHome(Context context) {
		if (Settings.getRegisteredClientGuid(context) != null) {
			Log.i(TAG, "Already have client guid, skipped reporting home.");
			return;
		}

		ApiClient client = new ApiClient(context);
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

		ApiClient client = new ApiClient(context);
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
			if (Settings.isHome(context)) {
				reportHome(context);
			} else {
				reportNotHome(context);
			}
		} catch (Exception e) {
			postFailureNotification();
		}
	}
}
