package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.DriverRideHistoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.app_tim17.adapters.DriveHistoryList;

public class PassengerRideHistoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static RecyclerView recyclerView;
    private DriveHistoryList rideHistoryAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private final String[] dates = {"11. Oct", "18. Oct", "18. Oct", "18. Oct", "19. Oct", "19. Oct"};
    private final String[] startTime = {"11:30", "16:30", "15:30", "14:30", "13:30", "12:30"};
    private final String[] endTime = {"11:45", "17:00", "15:40", "14:45", "13:50", "12:55"};
    private final String[] duration = {"15:00", "30:00", "10:00", "15:00", "20:00", "25:00"};
    private final String[] price= {"150", "300", "100", "150", "200", "250"};
    private final String[] numOfPassengers = {"1", "1", "2", "1", "1", "2"};
    private final String[] startAddress = {"Bulevar Oslobođenja 15", "Bulevar Oslobođenja 150", "Železnička 12", "Fruškogorska 12", "Fruškogorska 18", "Fruškogorska 15"};
    private final String[] endAddress = {"Fruškogorska 15", "Železnička 12", "Fruškogorska 12", "Bulevar Oslobođenja 150", "Nemanjina 18", "Bulevar Oslobođenja 15"};
    private final String[] roadLength = {"1.5", "2.12", "1.28", "2.24", "3.95", "1.25"};

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_history);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnItemSelectedListener(this);
        recyclerView = findViewById(R.id.my_recycler_view2);
        recyclerView.setHasFixedSize(true);
        rideHistoryAdapter = new DriveHistoryList(this, dates, startTime, endTime, duration, price, numOfPassengers, startAddress, endAddress,roadLength);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rideHistoryAdapter);
    }
    public void ShowMessages(View view){
        Toast.makeText(this, "Selected messages", Toast.LENGTH_SHORT).show();
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
                overridePendingTransition(0,0);
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

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.history);
    }

}