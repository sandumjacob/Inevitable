package com.jacobsandum.inevitable;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.REBOOT},1);
        Intent serviceIntent = new Intent(this,NotificationService.class);
        if (!isMyServiceRunning(NotificationService.class) && !isMyServiceRunning(Receiver.class)){
            Log.d("Notif", "Starting Service since it's not already running");
            startService(serviceIntent);
            ServiceConnection mConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder service) {

                }

                public void onServiceDisconnected(ComponentName className) {
                }
            };
            //bindService(serviceIntent,mConnection,Context.BIND_AUTO_CREATE);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

