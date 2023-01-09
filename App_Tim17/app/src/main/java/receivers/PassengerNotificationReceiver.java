package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.PassengerAccountActivity;

public class PassengerNotificationReceiver extends BroadcastReceiver {
    private static int NOTIFICATION_ID = 1;
    private static String CHANNEL_ID = "Zero channel";
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Bitmap bm;
        // notificationId is a unique int for each notification that you must define

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if(intent.getAction().equals(PassengerAccountActivity.SYNC_DATA)){
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_account_circle_24);
            mBuilder.setSmallIcon(R.drawable.car_picture);
            mBuilder.setLargeIcon(bm);
            mBuilder.setContentTitle("Notifikacija");
            mBuilder.setContentText("Stigla vam je poruka");

        }
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}
