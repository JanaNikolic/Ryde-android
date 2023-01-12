package com.example.app_tim17.activities;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
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

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private static AlertDialog dialog;
    private static LocationManager locationManager;
    int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void startApplication() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                //                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                //                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                //                if (mAuth.getCurrentUser() == null) {
                //                    Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                //                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                    startActivity(intent);
                //                    finish();
                //
                //                }
                //                else {
                //                    // TODO Check if driver or passenger
                //                    Intent intent = new Intent(SplashActivity.this, DriverMainActivity.class);
                //                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                    startActivity(intent);
                //                    finish();
                //                }
                startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                finish();
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