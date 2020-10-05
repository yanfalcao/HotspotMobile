package com.example.hotspotwi_fi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

public class HotspotManager {
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    private WifiManager mWifiManager;
    private Context context;
    private MainActivity view;

    public HotspotManager(Context context, MainActivity view){
        this.context = context;
        this.view = view;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void turnOn(){
        mWifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                Toast.makeText(context, "WiFi Hotspot está ativo!", Toast.LENGTH_SHORT).show();
                mReservation = reservation;
                view.setTextRede(reservation.getWifiConfiguration().SSID);
                view.setTextPassword(reservation.getWifiConfiguration().preSharedKey);
                view.turnOnState();
                super.onStarted(reservation);
            }

            @Override
            public void onStopped() {
                super.onStopped();
                Toast.makeText(context, "WiFi Hotspot foi desligado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Toast.makeText(context, "WiFi Hotspot falhou!", Toast.LENGTH_SHORT).show();
            }
        }, new Handler());
    }

    public void turnOff(){
        if(mReservation != null){
            mReservation.close();
            mReservation = null;
            Toast.makeText(context, "WiFi Hotspot foi desligado!", Toast.LENGTH_SHORT).show();
        }
    }
}
