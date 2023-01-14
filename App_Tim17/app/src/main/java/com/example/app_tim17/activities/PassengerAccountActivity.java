package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerAccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);
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

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView = findViewById(R.id.nav_view_driver_account);
        bottomNavigationView.setSelectedItemId(R.id.profile);
    }
}