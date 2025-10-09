package com.example.kolokvijum1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.example.kolokvijum1.ACTION_CHECK_WIFI")) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            boolean isWifiConnected = wifiInfo != null && wifiInfo.isConnected();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (isWifiConnected) {
                Log.d("WifiReceiver", "Wi-Fi je uključen. Sve je u redu!");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyService.CHANNEL_ID_1)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Wi-Fi Status")
                        .setContentText("Sve je u redu!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, builder.build()); // ID 1 za Wi-Fi status
            } else {
                Log.d("WifiReceiver", "Wi-Fi nije uključen. Uključi Wi-Fi!");

                Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, wifiSettingsIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyService.CHANNEL_ID_1)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Wi-Fi Status")
                        .setContentText("Uključi Wi-Fi!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .addAction(android.R.drawable.ic_menu_rotate, "Podešavanja", pendingIntent);
                notificationManager.notify(1, builder.build()); // ID 1 za Wi-Fi status
            }
        }
    }
}