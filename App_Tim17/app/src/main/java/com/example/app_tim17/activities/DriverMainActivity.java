package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        bottomNavigationView = findViewById(R.id.nav_view_driver);
        bottomNavigationView.setSelectedItemId(R.id.home_driver);
        bottomNavigationView.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_driver, menu);
        return true;
    }
    @Override
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
        bottomNavigationView = findViewById(R.id.nav_view_driver);
        bottomNavigationView.setSelectedItemId(R.id.home_driver);
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

    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.inbox_driver:
                startActivity(new Intent(getApplicationContext(), DriverInboxActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.home_driver:
                return true;
            case R.id.history_driver:
                startActivity(new Intent(getApplicationContext(), DriverHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile_driver:
                startActivity(new Intent(getApplicationContext(), DriverAccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

}