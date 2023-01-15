package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.driver.HistoryDriverFragment;
import com.example.app_tim17.fragments.driver.MainDriverFragment;
import com.example.app_tim17.fragments.driver.ProfileDriverFragment;
import com.example.app_tim17.fragments.passenger.InboxPassengerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.inbox_driver:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_driver_container, InboxPassengerFragment.class, null);
                transaction.commit();
                return true;
            case R.id.home_driver:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_driver_container, MainDriverFragment.class, null);
                transaction.commit();
                return true;
            case R.id.history_driver:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_driver_container, HistoryDriverFragment.class, null);
                transaction.commit();
                return true;
            case R.id.profile_driver:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_driver_container, ProfileDriverFragment.class, null);
                transaction.commit();
                return true;
        }
        return false;
    }
}