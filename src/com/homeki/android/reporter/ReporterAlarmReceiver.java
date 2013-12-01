package com.homeki.android.reporter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.homeki.android.settings.Settings;

public class ReporterAlarmReceiver extends BroadcastReceiver {
  private static String TAG = ReporterAlarmReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    new ReporterAsyncTask(context).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    Log.i(TAG, "Added ReporterAsyncTask for serial execution.");
  }

  public static void cancelAlarm(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    PendingIntent pendingIntent = ReporterAlarmReceiver.getPendingIntent(context);
    alarmManager.cancel(pendingIntent);
    pendingIntent.cancel();
    Settings.setAlarmStartTime(context, -1);
    Log.i(TAG, "Cancelled alarm.");
  }

  public static void setAlarm(Context context) {
    if (Settings.getAlarmStartTime(context) == -1)
      Settings.setAlarmStartTime(context, System.currentTimeMillis());

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    PendingIntent pendingIntent = getPendingIntent(context);
    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
  }

  private static PendingIntent getPendingIntent(Context context) {
    Intent reporterIntent = new Intent(context, ReporterAlarmReceiver.class);
    return PendingIntent.getBroadcast(context, 0, reporterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
