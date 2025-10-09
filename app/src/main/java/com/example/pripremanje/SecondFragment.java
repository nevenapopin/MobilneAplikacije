package com.example.kolokvijum1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SecondFragment extends Fragment {

    private EditText notificationText;
    private Button saveButton;

    public static final String CHANNEL_ID_2 = "channel_2";
    public static final String CHANNEL_NAME_2 = "Tekst Notifikacije";
    private int notificationIdCounter = 0; // Brojač za jedinstvene ID-ove notifikacija

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        notificationText = view.findViewById(R.id.notificationText);
        saveButton = view.findViewById(R.id.saveButton);

        createNotificationChannel2(); // Kreiranje drugog kanala za notifikacije

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = notificationText.getText().toString();
                if (!text.isEmpty()) {
                    sendNotification2(text);
                }
            }
        });

        return view;
    }

    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_2,
                    CHANNEL_NAME_2,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Kanal za notifikacije sa unetim tekstom");
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void sendNotification2(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID_2)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Obaveštenje")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(++notificationIdCounter, builder.build()); // Koristi brojač za jedinstven ID
    }
}