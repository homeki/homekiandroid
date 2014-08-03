package com.homeki.android.reporter;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

public class RetryIntentService extends IntentService {
    public RetryIntentService() {
        super("Homeki Retry Intent Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1221);

        new ReporterTask(this).run();
    }
}
