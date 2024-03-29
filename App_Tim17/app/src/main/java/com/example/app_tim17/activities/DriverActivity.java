package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
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
import com.example.app_tim17.fragments.ChangePasswordFragment;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.fragments.driver.DriverAcceptanceRideFragment;
import com.example.app_tim17.fragments.driver.HistoryDriverFragment;
import com.example.app_tim17.fragments.driver.InboxDriverFragment;
import com.example.app_tim17.fragments.driver.MainDriverFragment;
import com.example.app_tim17.fragments.driver.NoActiveRideFragment;
import com.example.app_tim17.fragments.driver.ProfileDriverFragment;
import com.example.app_tim17.model.WorkingHour;
import com.example.app_tim17.model.request.EndWorkingHour;
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.request.StartWorkingHour;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.FragmentTransition;
import com.example.app_tim17.tools.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class DriverActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Menu menu;
    private StompClient mStompClient;
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;
    private RideService rideService;
    private DriverService driverService;
    private CompositeDisposable compositeDisposable;
    private Gson mGson = new GsonBuilder().create();
    MainDriverFragment mainFragment;
    private WorkingHour workingHour;
    private MainDriverFragment main;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        tokenUtils = new TokenUtils();
        if (main == null) {
            main = new MainDriverFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.fragment_driver_container, main, MainDriverFragment.class.getName());
            transaction.commit();
        }


        bottomNavigationView = findViewById(R.id.nav_view_driver);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_driver);

        retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);


        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.1.7:8080/example-endpoint/websocket");
        connectStomp();
        startWorkingHour();
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
                    startWorkingHour();
                } else {
                    text.setText(R.string.offline);
                    text.setTextColor(getResources().getColor(R.color.white));
                    if (workingHour != null) endWorkingHour();
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
                if (workingHour != null) endWorkingHour();
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.remove("token");
                edit.commit();

                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            }
            case R.id.change_password: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_driver_container, new ChangePasswordFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f: fragments) {
            if (f.getTag()!= null && !f.getTag().equals(MainDriverFragment.class.getName())) {
                transaction.remove(f);
            }
        }

        switch (item.getItemId()) {
            case R.id.inbox_driver:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_driver_container, InboxDriverFragment.class, null, InboxDriverFragment.class.getName());
                transaction.hide(main);
                transaction.commit();
                getSupportActionBar().setTitle("Inbox");
//                activeFragment = fragmentManager.findFragmentByTag(InboxDriverFragment.class.getName());
                return true;
            case R.id.home_driver:
                transaction.show(main);
                transaction.commit();
                getSupportActionBar().setTitle("Ryde");
//                activeFragment = main;
                return true;
            case R.id.history_driver:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_driver_container, HistoryDriverFragment.class, null, HistoryDriverFragment.class.getName());
                transaction.hide(main);
                transaction.commit();
                getSupportActionBar().setTitle("History");
                return true;
            case R.id.profile_driver:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_driver_container, ProfileDriverFragment.class, null, ProfileDriverFragment.class.getName());
                transaction.hide(main);
                transaction.commit();
                getSupportActionBar().setTitle("Profile");
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

        Disposable dispTopic2 = mStompClient.topic("/topic/driver/" + tokenUtils.getId(getCurrentToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP", "Received " + topicMessage.getPayload());
                    Ride ride = gson.fromJson(topicMessage.getPayload(), Ride.class);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (ride.getStatus().equals("FINISHED")) {
                        Toast.makeText(getApplicationContext(), "Ride has ended!", Toast.LENGTH_SHORT).show();

                        DrawRouteFragment draw = DrawRouteFragment.newInstance();
                        FragmentTransition.to(draw, DriverActivity.this, false);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().findFragmentById(R.id.fragment_driver_container).getChildFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, new NoActiveRideFragment());
                        fragmentTransaction.commit();
                    } else {
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        for (Fragment f: fragments) {
                            if (f.getTag()!= null && f.getTag().equals(InboxDriverFragment.class.getName())) {
                                transaction.remove(f);
                            } else if (f.getTag()!= null && f.getTag().equals(HistoryDriverFragment.class.getName())) {
                                transaction.remove(f);
                            } else if (f.getTag()!= null && f.getTag().equals(ProfileDriverFragment.class.getName())) {
                                transaction.remove(f);
                            }
                        }
                        transaction.setReorderingAllowed(true);
    //                    transaction.addToBackStack(null);
                        transaction.show(main);
                        transaction.commit();
                        getSupportActionBar().setTitle("Ryde");
                        openAcceptanceRide();
                    }
                }, throwable -> {
                    Log.e("STOMP", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic2);
    }

    public void openAcceptanceRide() {
        Toast.makeText(getApplicationContext(), "NEW RIDE", Toast.LENGTH_SHORT).show();

        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        String token = "Bearer " + getCurrentToken();

        tokenUtils = new TokenUtils();
        Call<Ride> call = rideService.getActiveRide(token, tokenUtils.getId(getCurrentToken()));

        Bundle finalArgs = new Bundle();
        call.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                Ride ride = response.body();

                if (ride != null) {
                    finalArgs.putString("ride", Utils.getGsonParser().toJson(ride));
                    DriverAcceptanceRideFragment acceptanceRideFragment = new DriverAcceptanceRideFragment();
                    acceptanceRideFragment.setArguments(finalArgs);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map_container, acceptanceRideFragment)
                            .commitAllowingStateLoss();
                }
            }

            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                call.cancel();
            }
        });
    }


    public void sendEchoViaStomp(MessageRequest message) {
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
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
        if (workingHour != null) endWorkingHour();
    }

    private String getCurrentToken() {
        SharedPreferences sp = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    private void startWorkingHour() {
        Call<WorkingHour> call = driverService.startShift("Bearer " + getCurrentToken(), TokenUtils.getId(getCurrentToken()), new StartWorkingHour(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))));
        call.enqueue(new Callback<WorkingHour>() {
            @Override
            public void onResponse(Call<WorkingHour> call, Response<WorkingHour> response) {
                workingHour = response.body();
                Toast.makeText(getApplicationContext(), "Started shift!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WorkingHour> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void endWorkingHour() {
        Call<WorkingHour> call = driverService.endShift("Bearer " + getCurrentToken(), workingHour.getId(), new EndWorkingHour(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))));
        call.enqueue(new Callback<WorkingHour>() {
            @Override
            public void onResponse(Call<WorkingHour> call, Response<WorkingHour> response) {
                Toast.makeText(getApplicationContext(), "Ended shift!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<WorkingHour> call, Throwable t) {
                call.cancel();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(DriverActivity.this, "on Pause Driver", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(DriverActivity.this, "on Stop Driver", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(DriverActivity.this, "on Resume Driver", Toast.LENGTH_LONG).show();

    }
}