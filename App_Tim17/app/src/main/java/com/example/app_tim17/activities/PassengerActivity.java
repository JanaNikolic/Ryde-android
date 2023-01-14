package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< Updated upstream
=======
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> Stashed changes

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.passenger.HistoryPassengerFragment;
import com.example.app_tim17.fragments.passenger.InboxPassengerFragment;
import com.example.app_tim17.fragments.passenger.MainPassengerFragment;
import com.example.app_tim17.fragments.passenger.ProfilePassengerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class PassengerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    private WebSocketClient webSocketClient;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(this);
        createWebSocketClient();
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

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.1.7:8080/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }
}