package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import receivers.PassengerNotificationReceiver;
import services.PassengerMessageSyncService;

public class PassengerAccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private PassengerNotificationReceiver receiver;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    public static String SYNC_DATA = "SYNC_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);

        createNotificationChannel();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view_driver_account);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(this);

        Button reportBtn = findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerAccountActivity.this, PassengerReportActivity.class);
                startActivity(intent);
            }
        });

        Button favRouteBtn = findViewById(R.id.favRouteBtn);
        favRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerAccountActivity.this, PassengerFavoriteRouteActivity.class);
                startActivity(intent);
            }
        });
        setUpReceiver();
    }

    private void setUpReceiver(){
        //kreira instancu receivera
        receiver = new PassengerNotificationReceiver();

        //definisemo manager i kazemo kada je potrebno da se ponavlja
        /*
        parametri:
            context: this - u komkontekstu zelimo da se intent izvrsava
            requestCode: 0 - nas jedinstev kod
            intent: intent koji zelimo da se izvrsi kada dodje vreme
            flags: 0 - flag koji opisuje sta da se radi sa intent-om kada se poziv desi
            detaljnije:https://developer.android.com/reference/android/app/PendingIntent.html#getService
            (android.content.Context, int, android.content.Intent, int)
        */
        Intent alarmIntent = new Intent(this, PassengerMessageSyncService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);

        //koristicemo sistemski AlarmManager pa je potrebno da dobijemo
        //njegovu instancu.

        //BITNO ALARM MENADZER
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle("Profile");
        return true;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox:
                startActivity(new Intent(getApplicationContext(), PassengerInboxActivity.class));
                overridePendingTransition(0, 0);
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
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
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
        bottomNavigationView = findViewById(R.id.nav_view_driver_account);
        bottomNavigationView.setSelectedItemId(R.id.profile);
    }


    private static String CHANNEL_ID = "Zero channel";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //chanel id sluzi nam da u notifikaacionoj liniji kad dobijemo poruku mozemo da reagujemo na nju ?????
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onPause() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }

        //osloboditi resurse
        if(receiver != null){
            unregisterReceiver(receiver);
        }

        super.onPause();

    }
}