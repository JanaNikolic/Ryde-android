package com.example.app_tim17.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.ReviewerPreferenceActivity;
import com.example.app_tim17.service.SyncService;
import com.example.app_tim17.tools.ReviewerTools;

public class SyncReceiver extends BroadcastReceiver {

    private static int notificationID = 1;
    private static String CHANNEL_ID = "Zero channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("REZ", "onReceive");
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);


//        boolean allowSyncNotif = sharedPreferences.getBoolean(context.getString(R.string.notif_on_sync_key), true);

//        if(intent.getAction().equals(MainActivity.SYNC_DATA)){
//            if(allowSyncNotif){
//                int resultCode = intent.getExtras().getInt(SyncService.RESULT_CODE);
//
//                Bitmap bm = null;
//
//                Intent wiFiintent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                PendingIntent pIntent = PendingIntent.getActivity(context, 0, wiFiintent, 0);
//
//                Intent settingsIntent = new Intent(context, ReviewerPreferenceActivity.class);
//                PendingIntent pIntentSettings = PendingIntent.getActivity(context, 0, settingsIntent, 0);
//
//                if(resultCode == ReviewerTools.TYPE_NOT_CONNECTED){
//                    bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_network_wifi);
//                    mBuilder.setSmallIcon(R.drawable.ic_action_error);
//                    mBuilder.setContentTitle(context.getString(R.string.autosync_problem));
//                    mBuilder.setContentText(context.getString(R.string.no_internet));
//                    mBuilder.addAction(R.drawable.ic_action_network_wifi, context.getString(R.string.turn_wifi_on), pIntent);
//                    mBuilder.addAction(R.drawable.ic_action_settings, context.getString(R.string.turn_notif_on), pIntentSettings);
//                }else if(resultCode == ReviewerTools.TYPE_MOBILE){
//                    bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_network_cell);
//                    mBuilder.setSmallIcon(R.drawable.ic_action_warning);
//                    mBuilder.setContentTitle(context.getString(R.string.autosync_warning));
//                    mBuilder.setContentText(context.getString(R.string.connect_to_wifi));
//                    mBuilder.addAction(R.drawable.ic_action_network_wifi, context.getString(R.string.turn_wifi_on), pIntent);
//                    mBuilder.addAction(R.drawable.ic_action_settings, context.getString(R.string.turn_notif_on), pIntentSettings);
//                }else{
////                    bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//                    mBuilder.setSmallIcon(R.drawable.ic_action_refresh_w);
//                    mBuilder.setContentTitle(context.getString(R.string.autosync));
//                    mBuilder.setContentText(context.getString(R.string.good_news_sync));
//                    mBuilder.addAction(R.drawable.ic_action_settings, context.getString(R.string.turn_notif_on), pIntentSettings);
//                }
//
//
//                mBuilder.setLargeIcon(bm);
//                // notificationID allows you to update the notification later on.
//                notificationManager.notify(notificationID, mBuilder.build());
//            }
//        }
//
    }

}
