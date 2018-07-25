package com.jacobsandum.inevitable;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Jacob S on 12/14/2017.
 */

public class Receiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 3;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notif", "Receiver received intent");
        Notification.Builder builder = new Notification.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("InevitableChannel",
                    "InevitableChannel",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Notification Channel for Inevitable");
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("InevitableChannel");
        }
        builder.setContentTitle("Consider");
        String quote = randomQuote(context);
        Log.d("Notif", "" + quote);
        builder.setContentText(quote);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent notifyIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

        /*//Notification Channel for Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("InevitableChannel", "Inevitable Channel", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("Notification Channel for Inevitable Notifications");
        }*/

        cancelAlarm(context);
        setAlarm(context);

        Log.d("Notif", "Receive");
    }

    public void setAlarm(Context context) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Receiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        Random r = new Random();
        int ri = r.nextInt(24 - 1) + 1; // Random integer between 24 (inclusive) and 1 (exclusive),
        int delay = 1000 * 60 * 60 * ri; // Millisec * Second * Minute * hours
        //int delay = 1000 * 6;

        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi); //Delay
        Log.d("Notif", "Delay set to: " +delay/1000/60/60 + " Hours" );
        Log.d("Notif", "Milli: " + delay);

    }

    public String randomQuote(Context context) {
        Log.d("Notif", "Random Quote");
        ArrayList<String> stringList = new ArrayList<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("quotes.txt")));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
               stringList.add(line);
               Log.d("Notif", "Line:" + line);
            } bufferedReader.close();
        } catch (IOException e) {
            //Error handling
            Log.d("Notif", "IOException");
            Log.d("Notif", e.toString());
        }

        Random r = new Random();
        int i = r.nextInt(stringList.size()-1) + 0;
        return stringList.get(i);
    }

    public void cancelAlarm(Context context){
        Intent intent = new Intent(context, Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



}
