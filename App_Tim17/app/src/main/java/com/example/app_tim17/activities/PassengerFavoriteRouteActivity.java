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

import Adapters.FavoriteRouteAdapter;


public class PassengerFavoriteRouteActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private final String[] startAddress = {"Bulevar Oslobođenja 15", "Bulevar Oslobođenja 150",
            "Železnička 12", "Fruškogorska 12", "Fruškogorska 18", "Fruškogorska 15"};
    private final String[] endAddress = {"Fruškogorska 15", "Železnička 12", "Fruškogorska 12",
            "Bulevar Oslobođenja 150", "Nemanjina 18", "Bulevar Oslobođenja 15"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_favorite_route);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view_fav_route);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(this);
        ListView listView = (ListView) findViewById(R.id.list_view_fav_route);
        FavoriteRouteAdapter favRouteAdapter = new FavoriteRouteAdapter(this, startAddress, endAddress);
        listView.setAdapter(favRouteAdapter);


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
}