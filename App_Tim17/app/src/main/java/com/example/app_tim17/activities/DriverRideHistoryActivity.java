package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Adapters.DriverRideHistoryAdapter;

public class DriverRideHistoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private final String[] dates = {"11. Oct", "18. Oct", "18. Oct", "18. Oct", "19. Oct", "19. Oct"};
    private final String[] startTime = {"11:30", "16:30", "15:30", "14:30", "13:30", "12:30"};
    private final String[] endTime = {"11:45", "17:00", "15:40", "14:45", "13:50", "12:55"};
    private final String[] duration = {"15:00", "30:00", "10:00", "15:00", "20:00", "25:00"};
    private final String[] price= {"150", "300", "100", "150", "200", "250"};
    private final String[] numOfPassengers = {"1", "1", "2", "1", "1", "2"};
    private final String[] startAddress = {"Bulevar Oslobođenja 15", "Bulevar Oslobođenja 150", "Železnička 12", "Fruškogorska 12", "Fruškogorska 18", "Fruškogorska 15"};
    private final String[] endAddress = {"Fruškogorska 15", "Železnička 12", "Fruškogorska 12", "Bulevar Oslobođenja 150", "Nemanjina 18", "Bulevar Oslobođenja 15"};
    private final String[] roadLength = {"1.5", "2.12", "1.28", "2.24", "3.95", "1.25"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_hystory);
        bottomNavigationView = findViewById(R.id.nav_view_history);
        bottomNavigationView.setSelectedItemId(R.id.history_driver);
        bottomNavigationView.setOnItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        DriverRideHistoryAdapter rideHistoryAdapter = new DriverRideHistoryAdapter(this, dates, startTime, endTime, duration, price, numOfPassengers, startAddress, endAddress,roadLength);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rideHistoryAdapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_driver, menu);
        setTitle(R.string.ride_history);
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView = findViewById(R.id.nav_view_history);
        bottomNavigationView.setSelectedItemId(R.id.history_driver);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox_driver:
                startActivity(new Intent(getApplicationContext(), DriverMainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), DriverMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            case R.id.history_driver:
//                startActivity(new Intent(getApplicationContext(), DriverRideHistoryActivity.class));
//                overridePendingTransition(0, 0);
                return true;
            case R.id.profile_driver:
                startActivity(new Intent(getApplicationContext(), DriverAccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}