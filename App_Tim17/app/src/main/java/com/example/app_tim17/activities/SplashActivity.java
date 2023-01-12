package com.example.app_tim17.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


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
//
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//    cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//    build = new Builder(Context);
//
//    if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//            .isConnectedOrConnecting()
//            || cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//                    .isConnectedOrConnecting()// if connection is
//    // there screen goes
//    // to next screen
//    // else shows
//    // message toast
//    ) {
//        Log.e("cm value", "" + cm.getAllNetworkInfo().toString());
//        Toast.makeText(SplashScreen.this, "Internet is active", 2000)
//                .show();
//        Thread mythread = new Thread() {
//            public void run() {
//                try {
//
//                    sleep(5000);
//
//                } catch (Exception e) {
//                } finally {
//                    Intent intent = new Intent(SplashScreen.this,
//                            Yournextactivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//        mythread.start();
//    } else {
//
//        build.setMessage("This application requires Internet connection.Would you connect to internet ?");
//        build.setPositiveButton("Yes", new OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                finish();
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//
//            }
//        });
//        build.setNegativeButton("No", new OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                build.setMessage("Are sure you want to exit?");
//                build.setPositiveButton("Yes", new OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        finish();
//                    }
//                });
//                build.setNegativeButton("NO", new OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        finish();
//                        Intent intent = new Intent(SplashScreen.this,
//                                SplashScreen.class);
//                        startActivity(intent);
//
//                        dialog.dismiss();
//
//                    }
//                });
//                dailog = build.create();
//                dailog.show();
//            }
//        });
//        dailog = build.create();
//        dailog.show();
//
//    }
//
//}
//
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.main, menu);
//    return true;
//}
}