package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.HistoryPassengerFragment;
import com.example.app_tim17.fragments.InboxPassengerFragment;
import com.example.app_tim17.fragments.MainPassengerFragment;
import com.example.app_tim17.fragments.ProfilePassengerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(this);
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
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {
            case R.id.inbox:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_passenger_container, InboxPassengerFragment.class, null);
                transaction.commit();
                return true;
            case R.id.home:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_passenger_container, MainPassengerFragment.class, null);
                transaction.commit();
                return true;
            case R.id.history:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_passenger_container, HistoryPassengerFragment.class, null);
                transaction.commit();
                return true;
            case R.id.profile:
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.fragment_passenger_container, ProfilePassengerFragment.class, null);
                transaction.commit();
                return true;
        }
        return false;
    }
}