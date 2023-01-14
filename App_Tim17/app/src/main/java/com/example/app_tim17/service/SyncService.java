package com.example.app_tim17.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.app_tim17.tools.ReviewerTools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SyncService extends Service {

    public static String RESULT_CODE = "RESULT_CODE";
    ExecutorService executor = Executors.newSingleThreadExecutor();     //kreira samo jedan thread
    Handler handler = new Handler(Looper.getMainLooper()); //handler koji upravlja glavim thread-om od aplikacije (applications main thread)

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
        //ima konekcije ka netu skini sta je potrebno i sinhronizuj bazu
        if(status == ReviewerTools.TYPE_WIFI || status == ReviewerTools.TYPE_MOBILE){
            // Alternativa za SyncTask
            executor.execute(() -> {
                //Background work here
                Log.i("REZ", "Background work here");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> {
                    //UI Thread work here
                    Log.i("REZ", "UI Thread work here");
                    // TODO
//                    Intent ints = new Intent(MainActivity.SYNC_DATA);
//                    int intsStatus = ReviewerTools.getConnectivityStatus(getApplicationContext());
//                    ints.putExtra(RESULT_CODE, intsStatus);
//                    getApplicationContext().sendBroadcast(ints);
                });
            });
        }
        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
