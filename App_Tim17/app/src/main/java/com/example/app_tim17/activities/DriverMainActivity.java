package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.RideInfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    Menu menu;
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
        this.menu = menu;
        TextView text = (TextView) this.menu.findItem(R.id.online_offline_bar).getActionView();
        text.setText(R.string.online);
        text.setTextColor(getResources().getColor(R.color.yellow));
        RelativeLayout button_layout = (RelativeLayout) this.menu.findItem(R.id.toggle_action_bar).getActionView();
        SwitchCompat toggle = (SwitchCompat) button_layout.findViewById(R.id.toggleButton);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle.isChecked()) {
                    text.setText(R.string.online);

                    text.setTextColor(getResources().getColor(R.color.yellow));
                } else {
                    text.setText(R.string.offline);

                    text.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()) {
            case R.id.inbox_driver:
                startActivity(new Intent(getApplicationContext(), DriverInboxActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.history_driver:
                startActivity(new Intent(getApplicationContext(), DriverRideHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile_driver:
                startActivity(new Intent(getApplicationContext(), DriverAccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.home_driver:
                return true;
        }
        return false;
    }

}