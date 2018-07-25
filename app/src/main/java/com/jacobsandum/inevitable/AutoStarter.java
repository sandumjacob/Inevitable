package com.jacobsandum.inevitable;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AutoStarter extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notif", "Restarting receiver AFTER BOOT");
        Intent serviceIntent = new Intent(context,NotificationService.class);
        if (!isMyServiceRunning(context, NotificationService.class) && !isMyServiceRunning(context, Receiver.class)) {
            Log.d("Notif", "Starting Service since it's not already running, AFTER BOOT");
            context.startService(serviceIntent);
        }
    }
    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
