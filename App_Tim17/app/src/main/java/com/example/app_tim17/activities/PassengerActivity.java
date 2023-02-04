package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.ChangePasswordFragment;
import com.example.app_tim17.fragments.driver.MainDriverFragment;
import com.example.app_tim17.fragments.passenger.ChatFragment;
import com.example.app_tim17.fragments.passenger.HistoryPassengerFragment;
import com.example.app_tim17.fragments.passenger.InboxPassengerFragment;
import com.example.app_tim17.fragments.passenger.MainPassengerFragment;
import com.example.app_tim17.fragments.passenger.PassengerCreateRideFragment;
import com.example.app_tim17.fragments.passenger.PassengerCurrentRideFragment;
import com.example.app_tim17.fragments.passenger.ProfilePassengerFragment;
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.ride.FavoriteRoute;
import com.example.app_tim17.model.response.ride.FavoriteRouteResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

public class PassengerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private StompClient mStompClient;
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;
    private DriverService driverService;
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;
    private Gson mGson = new GsonBuilder().create();
    Timer timer;
    BottomNavigationView bottomNavigationView;
    ChatFragment fragment;
    Long driverId;
    String driverName, driverPhoneNumber, driverImage, vehicleLicensePlate, vehicleModel;
    Gson gson = new Gson();
    private MainPassengerFragment main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag("ChatFragment");
        setContentView(R.layout.activity_passenger);
        tokenUtils = new TokenUtils();
        if (main == null) {
            main = new MainPassengerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.fragment_passenger_container, main, MainPassengerFragment.class.getName());
            transaction.commit();
        }

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(this);

        retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);

        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.1.7:8080/example-endpoint/websocket");
        connectStomp();
        createNotificationChannel();

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
            case R.id.change_password: {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_passenger_container, new ChangePasswordFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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

        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f: fragments) {
            if (f.getTag()!= null && !f.getTag().equals(MainPassengerFragment.class.getName())) {
                transaction.remove(f);
            }
        }

        switch (item.getItemId()) {
            case R.id.inbox:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_passenger_container, InboxPassengerFragment.class, null, InboxPassengerFragment.class.getName());
                transaction.hide(main);
                transaction.commit();
                getSupportActionBar().setTitle("Inbox");
                return true;
            case R.id.home:
                transaction.show(main);
                transaction.commit();
                getSupportActionBar().setTitle("Ryde");
                return true;
            case R.id.history:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_passenger_container, HistoryPassengerFragment.class, null, HistoryPassengerFragment.class.getName());
                transaction.hide(main);
                transaction.commit();
                getSupportActionBar().setTitle("History");
                return true;
            case R.id.profile:
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.fragment_passenger_container, ProfilePassengerFragment.class, null, ProfilePassengerFragment.class.getName());
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
        Disposable dispTopic = mStompClient.topic("/socket-publisher/" + TokenUtils.getId(getCurrentToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP", "Received " + topicMessage.getPayload());
                    Toast.makeText(getApplicationContext(), "You got a message!", Toast.LENGTH_LONG).show();
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
                }, throwable -> {
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
        timer.cancel();
        super.onDestroy();
    }

    private String getCurrentToken() {
        SharedPreferences sp = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public void scheduledRide(long rideId) {
        Disposable dispTopic = mStompClient.topic("/topic/ride/" + rideId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Ride ride = gson.fromJson(topicMessage.getPayload(), Ride.class);
                    if (ride.getStatus().equals("ACCEPTED")) {
                        Toast.makeText(getApplicationContext(), "You're ride will be there soon!", Toast.LENGTH_LONG).show();
                        driverId = ride.getDriver().getId();
                        Call<DriverResponse> call = driverService.getDriver(driverId, "Bearer " + getCurrentToken());
                        call.enqueue(new Callback<DriverResponse>() {
                            @Override
                            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                                DriverResponse driver = response.body();
                                if (driver != null) {
                                    driverId = driver.getId();
                                    driverName = driver.getName() + " " + driver.getSurname();
                                    if (driver.getProfilePicture() != null) {
                                        driverImage = driver.getProfilePicture();
                                    }
                                    driverPhoneNumber = driver.getTelephoneNumber();
                                }
                            }

                            @Override
                            public void onFailure(Call<DriverResponse> call, Throwable t) {
                                Toast.makeText(PassengerActivity.this, "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Call<VehicleResponse> call2 = driverService.getDriversVehicle(driverId, "Bearer " + getCurrentToken());

                        call2.enqueue(new Callback<VehicleResponse>() {
                            @Override
                            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                                VehicleResponse vehicle = response.body();
                                if (vehicle != null) {
                                    vehicleLicensePlate = vehicle.getLicenseNumber();
                                    vehicleModel = vehicle.getModel();
                                }
                            }

                            @Override
                            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                                Toast.makeText(PassengerActivity.this, "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Bundle args = new Bundle();
                                args.putString("driverName", driverName);
                                args.putString("licensePlate", vehicleLicensePlate);
                                args.putString("vehicleModel", vehicleModel);
                                args.putString("driverPhoneNumber", driverPhoneNumber);
                                args.putString("driverImage", driverImage);
                                args.putString("startAddress", ride.getLocations().get(0).getDeparture().getAddress());
                                args.putString("endAddress", ride.getLocations().get(0).getDestination().getAddress());
                                args.putString("price", ride.getTotalCost().toString());
                                args.putString("timeStart", ride.getStartTime());
                                args.putLong("rideId", rideId);
                                args.putLong("driverId", driverId);
                                args.putString("time", ride.getEstimatedTimeInMinutes().toString());

                                PassengerCurrentRideFragment fragment = new PassengerCurrentRideFragment();
                                fragment.setArguments(args);

                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().findFragmentById(R.id.fragment_passenger_container).getChildFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.currentRide, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }, 1500);


                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                                .setSmallIcon(R.drawable.standard_car_selected)
                                .setContentTitle("Scheduled ride reminder")
                                .setContentText("Your ride will arrive in 15 minutes.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
                        notificationManager.notify(1, builder.build());
                        timer = new Timer();
                        TimerTask tt = new TimerTask() {
                            public void run() {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(PassengerActivity.this, "notify")
                                        .setSmallIcon(R.drawable.standard_car_selected)
                                        .setContentTitle("Scheduled ride reminder")
                                        .setContentText("Your ride will arrive in 10 minutes.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(2, builder.build());
                            }

                            ;
                        };
                        timer.schedule(tt, 300000);

                        TimerTask t2 = new TimerTask() {
                            public void run() {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(PassengerActivity.this, "notify")
                                        .setSmallIcon(R.drawable.standard_car_selected)
                                        .setContentTitle("Scheduled ride reminder")
                                        .setContentText("Your ride will arrive in 5 minutes.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                notificationManager.notify(3, builder.build());
                            }

                            ;
                        };
                        timer.schedule(t2, 600000);
                    } else if (ride.getStatus().equals("REJECTED")) {
                        Toast.makeText(PassengerActivity.this, "Ride has been rejected", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Log.e("STOMP", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}