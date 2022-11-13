package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverRideHistoryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_hystory);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_history);
        bottomNavigationView.setSelectedItemId(R.id.history);
//        bottomNavigationView.setOnItemSelectedListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle(R.string.ride_history);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox:
                overridePendingTransition(0, 0);
                return true;
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), DriverMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), DriverRideHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(), DriverMainActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}