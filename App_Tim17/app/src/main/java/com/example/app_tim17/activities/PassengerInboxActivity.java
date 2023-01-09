package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

import Adapters.InboxList;
import receivers.PassengerNotificationReceiver;
import services.PassengerMessageSyncService;

public class PassengerInboxActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private PassengerNotificationReceiver receiver;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    public static String SYNC_DATA = "SYNC_DATA";
    BottomNavigationView bottomNavigationView;
    public static String[] userNames = {"User001", "User002", "User003", "User004", "User005", "User006", "User007", "User008", "User009", "User010"};
    public static String times[] = {"12:03", "12:05", "09:30", "03:33", "12:03", "12:05", "09:30", "03:33", "11:09", "17:22"};
    public static String messages[] = {"Hello", "Alright", "Listen here, I know I am late, but I am trying my best, with this traffic", "What!? NO!", "Yes", "Why?", "Cya", "Thanks, man.", "Hm", "xD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_inbox);
        createNotificationChannel();
        Handler handler = new Handler(Looper.getMainLooper());
        Activity context = this;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_inbox);
        bottomNavigationView.setSelectedItemId(R.id.inbox);
        bottomNavigationView.setOnItemSelectedListener(this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        InboxList inboxList = new InboxList(this, userNames, times, messages);
        listView.setAdapter(inboxList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getApplicationContext(), PassengerInboxChatActivity.class));
                overridePendingTransition(0,0);
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("REZ", "Background work here");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        InboxList inboxList = new InboxList(context, userNames, times, messages);
                        listView.setAdapter(inboxList);
                        Intent ints = new Intent(PassengerAccountActivity.SYNC_DATA);
                        getApplicationContext().sendBroadcast(ints);

                    }
                });

            }
        });
        setUpReceiver();
    }
    private static String CHANNEL_ID = "Zero channel";
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void setUpReceiver(){

        receiver = new PassengerNotificationReceiver();
        Intent alarmIntent = new Intent(this, PassengerMessageSyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle(R.string.inbox);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox:
                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(0,0);
                this.finish();
                return true;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), PassengerRideHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile:

                startActivity(new Intent(getApplicationContext(), PassengerAccountActivity.class));
                overridePendingTransition(0, 0);

                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                // navigate to settings screen
                return true;
            }
            case R.id.action_logout: {
                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static int calculateTimeTillNextSync(int minutes){
        return 1000 * 60 * minutes;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager == null) {
            setUpReceiver();
        }

        /*
         * Kada zelimo da se odredjeni zadaci ponavljaju, potrebno je
         * da registrujemo manager koji ce motriti kada je vreme da se
         * taj posao obavi. Kada registruje vreme za pokretanje zadatka
         * on emituje Intent operativnom sistemu sta je potrebno da se izvrsi.
         * Takodje potrebno je da definisemo ponavljanja tj. na koliko
         * vremena zelimo da se posao ponovo obavi
         * */
        int interval = calculateTimeTillNextSync(1);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);
        registerReceiver(receiver, filter);
        bottomNavigationView = findViewById(R.id.nav_view_inbox);
        bottomNavigationView.setSelectedItemId(R.id.inbox);
    }
}