package com.homeki.android.reporter;

import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.homeki.android.R;
import com.homeki.android.server.RestClient;
import com.homeki.android.settings.Settings;

import java.util.UUID;

public class ReporterAsyncTask extends AsyncTask<Void, Void, Void> {
  private static String TAG = ReporterAsyncTask.class.getSimpleName();

  private final Context context;

  public ReporterAsyncTask(Context context) {
    this.context = context;
  }

  @Override
  protected Void doInBackground(Void... params) {
    try {
      if (notifyThresholdPassed()) return null;

      String ssid = getSsid(context);
      String homeSsid = Settings.getHomeSsid(context);

      boolean stateHome = Settings.getOnHomeNetwork(context) != null;
      Log.i(TAG, "stateHome is " + stateHome);

      if (stateHome && !homeSsid.equals(ssid)) {
        reportNotHome(context);
        ReporterAlarmReceiver.cancelAlarm(context);
      } else if (!stateHome && homeSsid.equals(ssid)) {
        reportHome(context);
        ReporterAlarmReceiver.cancelAlarm(context);
      }
    } catch (Exception e) {
      Log.e(TAG, "Failed to run ReporterAsyncTask.", e);
    }

    return null;
  }

  private boolean notifyThresholdPassed() {
    long diff = System.currentTimeMillis() - Settings.getAlarmStartTime(context);
    diff /= 60000;

    if (diff > 120) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
          .setSmallIcon(R.drawable.ic_launcher)
          .setContentTitle("Homeki")
          .setContentText("Failed to register/unregister client to Homeki server for 2 hours, giving up.");
      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(1221, builder.getNotification());
      ReporterAlarmReceiver.cancelAlarm(context);
      return true;
    }

    return false;
  }

  private void reportHome(Context context) {
    RestClient client = new RestClient(context);
    String guid = UUID.randomUUID().toString();
    try {
      client.registerClient(guid);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Settings.setOnHomeNetwork(context, guid);
    Log.i(TAG, "Successfully registered client as home with guid " + guid + ".");
  }

  private void reportNotHome(Context context) {
    RestClient client = new RestClient(context);
    String guid = Settings.getOnHomeNetwork(context);
    try {
      client.unregisterClient(guid);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    Settings.setOnHomeNetwork(context, null);
    Log.i(TAG, "Successfully unregistered client as home with guid " + guid + ".");
  }
  private String getSsid(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    return wifiInfo.getSSID();
  }
}
