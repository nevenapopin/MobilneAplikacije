package com.example.kolokvijum1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    public static final String CHANNEL_ID_1 = "channel_1";
    public static final String CHANNEL_NAME_1 = "Wi-Fi Provera";
    private static final String ACTION_CHECK_WIFI = "com.example.kolokvijum1.ACTION_CHECK_WIFI";
    private static final int NOTIFICATION_ID_WIFI = 1;

    private Handler handler;
    private Runnable runnable;
    private WifiReceiver wifiReceiver;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel1();

        handler = new Handler();
        wifiReceiver = new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(ACTION_CHECK_WIFI));

        runnable = new Runnable() {
            @Override
            public void run() {
                // Pokreće BroadcastReceiver za proveru Wi-Fi-ja
                Intent intent = new Intent(ACTION_CHECK_WIFI);
                sendBroadcast(intent);
                handler.postDelayed(this, 10000); // Ponovi za 10 sekundi
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(runnable); // Pokreni proveru odmah
        return START_STICKY; // Servis se ponovo pokreće ako ga sistem ubije
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Zaustavi ponavljanje provere
        unregisterReceiver(wifiReceiver); // Odjavi BroadcastReceiver
        Log.d("MyService", "Service stopped and Wi-Fi check callbacks removed.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_1,
                    CHANNEL_NAME_1,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Kanal za notifikacije o Wi-Fi statusu");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}