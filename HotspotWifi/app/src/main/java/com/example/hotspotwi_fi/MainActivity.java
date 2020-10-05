package com.example.hotspotwi_fi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout buttonWifi;
    private LinearLayout layoutRede;
    private LinearLayout layoutPassword;

    private TextView textInfoWifi;
    private TextView textRede;
    private TextView textPassword;

    private boolean isActived = false;

    private HotspotManager mHotspotManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonWifi = findViewById(R.id.wifi_button);
        textInfoWifi = findViewById(R.id.text_wifi_info);
        layoutRede = findViewById(R.id.layout_rede);
        layoutPassword = findViewById(R.id.layout_password);
        textRede = findViewById(R.id.rede_text);
        textPassword = findViewById(R.id.password_text);

        buttonWifi.setOnClickListener(this);

        mHotspotManager = new HotspotManager(this, this);
    }

    public void setTextRede(String text){
        textRede.setText(text);
    }

    public void setTextPassword(String text){
        textPassword.setText(text);
    }

    public void turnOnState(){
        buttonWifi.setBackground(getDrawable(R.drawable.db_circle_red));
        textInfoWifi.setText(R.string.actived_wifi_text);
        layoutRede.setVisibility(View.VISIBLE);
        layoutPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(!isPermitted()) {
            requestPermission();
        }else {
            if (!isActived) {
                if(isActivedLocation()){
                    changeState();

                    mHotspotManager.turnOn();
                }else {
                    turnOnLocation();
                }
            } else {
                changeState();

                buttonWifi.setBackground(getDrawable(R.drawable.db_circle_green));
                textInfoWifi.setText(R.string.disabled_wifi_text);
                layoutRede.setVisibility(View.GONE);
                layoutPassword.setVisibility(View.GONE);

                mHotspotManager.turnOff();

                textRede.setText("");
                textPassword.setText("");
            }
        }
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE)
                    == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                        1);
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CHANGE_WIFI_STATE)
                    == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                        1);
            }
        }
    }

    private boolean isPermitted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED){
                return false;
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED){
                return false;
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_WIFI_STATE)
                    == PackageManager.PERMISSION_DENIED){
                return false;
            }
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CHANGE_WIFI_STATE)
                    == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    private boolean isActivedLocation(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

    private void turnOnLocation(){
        Toast.makeText(this, "É preciso habilitar sua localização", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void changeState(){
        isActived = isActived ? false : true;
    }

    @Override
    protected void onStart() {
        if(!isPermitted()) {
            requestPermission();
        }
        super.onStart();
    }
}