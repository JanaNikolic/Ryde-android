package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;

import Adapters.DriveHistoryList;
import Adapters.InboxList;

public class PassengerRideHistoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final String[] startLocations = {"Nikole Tesle 76", "Karadjordjeva 22",
            "Strazilovska 5", "Masarikova 19", "Maksima Gorkog 55",
            "Futoska 18", "Omladinska 44", "Proleterska 6",
            "Narodnog fronta 66", "Janka Cmelika 1"};
    private final String[] endLocations = {"Strazilovska 3", "Proleterska 6",
            "Nikole Tesle 86", "Omladinska 4", "Ive Lole Ribara 13",
            "Maksima Gorkog 7", "Masarikova 2", "Karadjordjeva 12",
            "Maksima Gorkog 44", "Branka Bajica 22"};
    private final String[] startTimes = {"18/09/2022", "21/09/2022",
            "30/09/2022", "18/10/2022", "21/10/2022",
            "26/10/2022", "30/10/2022", "10/11/2022",
            "11/11/2022", "12/11/2022"};
    private final String durations[] = {"26:21", "51:05", "09:30",
            "03:33", "12:03", "12:05", "09:30",
            "03:33", "11:09", "17:22"};
    private final String prices[] = {"66$", "68$", "96$", "90$",
            "107$", "45$", "55$", "78$", "98$", "34$"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_history);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        DriveHistoryList driveHistory = new DriveHistoryList(this, startLocations,
                endLocations, startTimes, durations, prices);
        listView.setAdapter(driveHistory);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getApplicationContext(), PassengerSingularRideHistoryActivity.class));

                       }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle("History");
        return true;
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
                this.finish();
                return true;
            case R.id.history:
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), PassengerAccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }



}