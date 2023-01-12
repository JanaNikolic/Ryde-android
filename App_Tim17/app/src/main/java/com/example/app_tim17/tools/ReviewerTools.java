package com.example.app_tim17.tools;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

//Pomocna klasa koja proverava da li je uredjaj povezan na internet
public class ReviewerTools {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11(R) or above
            // https://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("REZ", "Connected to the WIFI");
                    return TYPE_WIFI;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("REZ", "Connected mobile data");
                    return TYPE_MOBILE;
                }
            }
        }else{
            // https://developer.android.com/reference/android/net/NetworkInfo
            //Android is below R
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
        }

        Log.i("REZ", "No internet connection");
        return TYPE_NOT_CONNECTED;
    }

    public static int calculateTimeTillNextSync(int minutes){
        return 1000 * 60 * minutes;
    }
}
