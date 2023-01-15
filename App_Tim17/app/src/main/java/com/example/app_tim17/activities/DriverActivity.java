package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.driver.ChatDriverFragment;
import com.example.app_tim17.fragments.driver.HistoryDriverFragment;
import com.example.app_tim17.fragments.driver.InboxDriverFragment;
import com.example.app_tim17.fragments.driver.MainDriverFragment;
import com.example.app_tim17.fragments.driver.ProfileDriverFragment;
import com.example.app_tim17.fragments.passenger.ChatFragment;
import com.example.app_tim17.fragments.passenger.InboxPassengerFragment;
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tech.gusavila92.websocketclient.WebSocketClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class DriverActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Menu menu;
    private StompClient mStompClient;
    private TokenUtils tokenUtils;
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;
    private Gson mGson = new GsonBuilder().create();
    private WebSocketClient webSocketClient;
    ChatDriverFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        fragment = (ChatDriverFragment) getSupportFragmentManager().findFragmentByTag("ChatDriverFragment");
        tokenUtils = new TokenUtils();
        bottomNavigationView = findViewById(R.id.nav_view_driver);
        bottomNavigationView.setSelectedItemId(R.id.home_driver);
        bottomNavigationView.setOnItemSelectedListener(this);

        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.1.7:8080/example-endpoint/websocket");
        connectStomp();
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
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove("token");
                edit.commit();

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
                transaction.replace(R.id.fragment_driver_container, InboxDriverFragment.class, null);
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

    public static final String LOGIN = "login";
    public static final String PASSCODE = "passcode";

    public void connectStomp() {

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));
        mStompClient.connect();
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();




        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Toast.makeText(getApplicationContext(), "Stomp connection opened", Toast.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            Log.e("STOMP", "Stomp connection error", lifecycleEvent.getException());
                            Toast.makeText(getApplicationContext(), "Stomp connection error", Toast.LENGTH_SHORT).show();
                            break;
                        case CLOSED:
                            Toast.makeText(getApplicationContext(), "Stomp connection closed", Toast.LENGTH_SHORT).show();
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Toast.makeText(getApplicationContext(), "Stomp failed server heartbeat", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/socket-publisher/" + tokenUtils.getId(getCurrentToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP", "Received " + topicMessage.getPayload());
                    Toast.makeText(getApplicationContext(), "you got a message!", Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Log.e("STOMP", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);
    }

    public void sendEchoViaStomp(MessageRequest message) {
//        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send("/topic/hello-msg-mapping", mGson.toJson(message))
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d("STOMP", "STOMP echo send successfully");
                    Toast.makeText(getApplicationContext(), "Sent message", Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Log.e("STOMP", "Error send STOMP echo", throwable);
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG);

                }));
    }

    public void disconnectStomp(View view) {
        mStompClient.disconnect();
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

    private String getCurrentToken() {
        SharedPreferences sp = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}