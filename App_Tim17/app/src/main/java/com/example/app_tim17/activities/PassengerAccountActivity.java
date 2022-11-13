package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Adapters.PassengerProfile;

public class PassengerAccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);
        bottomNavigationView = findViewById(R.id.nav_view_profile);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(this);
        ListView listview = findViewById(R.id.list_view_profile);
        PassengerProfile adapter = new PassengerProfile(this);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle("Profile");
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
                startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile:
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}