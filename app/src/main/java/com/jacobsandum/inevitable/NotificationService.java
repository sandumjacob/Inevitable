package com.jacobsandum.inevitable;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationService extends IntentService {
    Receiver rec = new Receiver();

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        rec.setAlarm(this);

    }

}
