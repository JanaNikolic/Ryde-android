package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import Adapters.InboxList;

public class PassengerInboxActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    BottomNavigationView bottomNavigationView;
    private final String[] userNames = {"User001", "User002", "User003", "User004", "User005", "User006", "User007", "User008", "User009", "User010"};
    private final String times[] = {"12:03", "12:05", "09:30", "03:33", "12:03", "12:05", "09:30", "03:33", "11:09", "17:22"};
    private final String messages[] = {"Hello", "Alright", "Listen here, I know I am late, but I am trying my best, with this traffic", "What!? NO!", "Yes", "Why?", "Cya", "Thanks, man.", "Hm", "xD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_inbox);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_inbox);
        bottomNavigationView.setSelectedItemId(R.id.inbox);
        bottomNavigationView.setOnItemSelectedListener(this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        InboxList inboxList = new InboxList(this, userNames, times, messages);
        listView.setAdapter(inboxList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getApplicationContext(), PassengerInboxChatActivity.class));
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle(R.string.inbox);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox:
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

                startActivity(new Intent(getApplicationContext(), PassengerAccountActivity.class));
                overridePendingTransition(0, 0);

                return true;
        }
        return false;
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
    protected void onResume() {
        super.onResume();
        bottomNavigationView = findViewById(R.id.nav_view_inbox);
        bottomNavigationView.setSelectedItemId(R.id.inbox);
    }
}