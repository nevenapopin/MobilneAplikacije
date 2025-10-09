package com.example.kolokvijum1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences; // Dodato za SharedPreferences
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast; // Dodato za Toast poruke

public class FirstFragment extends Fragment {

    private CheckBox wifiCheck;
    private Switch wifiSwitch;
    private SharedPreferences sharedPreferences; // Instanca SharedPreferences

    // Ključ za čuvanje Wi-Fi statusa
    private static final String PREF_WIFI_STATUS = "wifi_status";
    // Ime SharedPreferences fajla
    private static final String PREF_NAME = "Kolokvijum1Prefs";

    public FirstFragment() {
        // Obavezan prazan konstruktor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate-ovanje layout-a za ovaj fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Pronalaženje UI elemenata po ID-u
        wifiCheck = view.findViewById(R.id.wifiCheck);
        wifiSwitch = view.findViewById(R.id.wifiSwitch);

        // Inicijalizacija SharedPreferences
        // Koristimo CONTEXT_PRIVATE da samo ova aplikacija može da pristupi podacima
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Postavi početno stanje Switch-a na osnovu sačuvane vrednosti
        boolean savedWifiStatus = sharedPreferences.getBoolean(PREF_WIFI_STATUS, false); // Default je false
        wifiSwitch.setChecked(savedWifiStatus);


        // Listener za promenu stanja Checkbox-a
        wifiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent serviceIntent = new Intent(requireContext(), MyService.class);
                if (isChecked) {
                    // Pokretanje MyService-a ako je Checkbox čekiran
                    requireContext().startService(serviceIntent);
                    Log.d("FirstFragment", "MyService pokrenut.");
                    Toast.makeText(getContext(), "Wi-Fi provera pokrenuta.", Toast.LENGTH_SHORT).show();
                } else {
                    // Zaustavljanje MyService-a ako je Checkbox odčekiran
                    requireContext().stopService(serviceIntent);
                    Log.d("FirstFragment", "MyService zaustavljen.");
                    Toast.makeText(getContext(), "Wi-Fi provera zaustavljena.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener za promenu stanja Switch-a
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Provera Wi-Fi statusa
                ConnectivityManager connManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                boolean isWifiEnabled = wifiInfo != null && wifiInfo.isConnected();

                // Sačuvaj Wi-Fi status u SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_WIFI_STATUS, isWifiEnabled); // Čuva boolean vrednost
                editor.apply(); // Primeni promene asinhrono

                Log.d("FirstFragment", "Wi-Fi status '" + isWifiEnabled + "' sačuvan u SharedPreferences.");
                Toast.makeText(getContext(), "Wi-Fi status sačuvan: " + (isWifiEnabled ? "Uključen" : "Isključen"), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}