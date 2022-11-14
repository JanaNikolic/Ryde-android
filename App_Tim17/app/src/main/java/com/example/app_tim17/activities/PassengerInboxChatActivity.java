package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.app_tim17.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import Adapters.MessageListAdapter;

public class PassengerInboxChatActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<Integer> messageList = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_inbox_chat);
        messageList.add(1);
        messageList.add(2);
        messageList.add(3);
        messageList.add(4);
        messageList.add(5);
        messageList.add(6);
        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view_inbox2);
        bottomNavigationView.setSelectedItemId(R.id.inbox);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        setTitle("Other person");
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), PassengerMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            case R.id.history:
                return true;
            case R.id.profile:
                return true;
            case R.id.inbox:
                startActivity(new Intent(getApplicationContext(), PassengerInboxActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return false;
    }
}