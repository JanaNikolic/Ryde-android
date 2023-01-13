package com.example.app_tim17.activities;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import com.example.app_tim17.R;
import com.example.app_tim17.dialogs.LocationDialog;
import com.example.app_tim17.service.TokenUtils;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private static AlertDialog dialog;
    private static LocationManager locationManager;
    private final int SPLASH_TIME_OUT = 5000;
    private TokenUtils tokenUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tokenUtils = new TokenUtils();
    }

    private void startApplication() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");

                if (token == null) {
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    finish();
                } else {
                    String role = tokenUtils.getRole(token);

                    if (role.equals("ROLE_DRIVER")) {
                        startActivity(new Intent(SplashActivity.this, DriverActivity.class));
                        finish();
                    } else if (role.equals("ROLE_PASSENGER")) {
                        startActivity(new Intent(SplashActivity.this, PassengerActivity.class));
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public static boolean isNetworkAvailable(Context context) {
        if(context == null)  return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return true;
                }
            }
        }
        Log.i("update_status","Network is available : FALSE ");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = isNetworkAvailable(SplashActivity.this);

        Log.i("gps con", String.valueOf(gps));
        Log.i("wifi con", String.valueOf(wifi));

        if (!gps) {
            Log.i("ASD", "ASD resume map");
            showLocationDialog(SplashActivity.this);
        } else if (!wifi) {
            Toast.makeText(SplashActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            startApplication();
        }
// TODO close application
    }

    private static void showLocationDialog(Context context) {
        if (dialog == null) {
            dialog = new LocationDialog(context).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }
}