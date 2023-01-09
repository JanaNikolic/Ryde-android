package services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.app_tim17.activities.PassengerInboxActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PassengerMessageSyncService extends Service {
    public static String RESULT_CODE = "RESULT_CODE";

    ExecutorService executor = Executors.newSingleThreadExecutor(); //kreira samo jedan thread
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("REZ", "SyncService onStartCommand");


            executor.execute(() -> {

                try {
                    //Sinhronizuj podatke
                    PassengerInboxActivity.userNames = new String[]{"User111", "User002", "User003", "User004", "User005", "User006", "User007", "User008", "User009", "User010"};
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> {
                    Intent ints = new Intent(PassengerInboxActivity.SYNC_DATA);
                    getApplicationContext().sendBroadcast(ints);

                });
            });
//
        stopSelf();


        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}