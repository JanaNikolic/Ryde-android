package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.VehicleRequest;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.google.gson.Gson;

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
import retrofit2.Retrofit;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccesfullSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccesfullSearchFragment extends Fragment {

    private StompClient mStompClient;
    private RetrofitService retrofitService;
    private TokenUtils tokenUtils;
    private DriverService driverService;
    private String driverName;
    private String driverImage;
    private String vehicleModel;
    private String vehicleLicensePlate;
    private String driverPhoneNumber;
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;
    private String token;
    private Long driverId, rideId;
    Gson gson = new Gson();

    public SuccesfullSearchFragment() {
        // Required empty public constructor
    }
    public static SuccesfullSearchFragment newInstance(String param1, String param2) {
        SuccesfullSearchFragment fragment = new SuccesfullSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_succesfull_search, container, false);
        rideId = getArguments().getLong("rideId");

        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.1.7:8080/example-endpoint/websocket");
        retrofitService = new RetrofitService();
        connectStomp();

        driverId = getArguments().getLong("driverId");
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        token = "Bearer " + getCurrentToken();


    return v;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
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
        Disposable dispTopic = mStompClient.topic("/topic/ride/" + rideId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP", "Received " + topicMessage.getPayload());
                    Ride ride = gson.fromJson(topicMessage.getPayload(), Ride.class);
                    if (ride.getStatus().equals("REJECTED")) {
                        Toast.makeText(getContext(), "Ride was rejected, please try again.", Toast.LENGTH_SHORT).show();
                        PassengerCreateRideFragment fragment = new PassengerCreateRideFragment();

                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        Call<DriverResponse> call = driverService.getDriver(driverId, token);

                        call.enqueue(new Callback<DriverResponse>() {
                            @Override
                            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                                DriverResponse driver = response.body();
                                if (driver != null) {
                                    driverId = driver.getId();
                                    driverName = driver.getName() + " " + driver.getSurname();
                                    driverImage = driver.getProfilePicture();
                                }
                            }

                            @Override
                            public void onFailure(Call<DriverResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                                getChildFragmentManager().popBackStack();
                            }
                        });

                        Call<VehicleResponse> call2 = driverService.getDriversVehicle(Long.valueOf(driverId), token);

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
                                Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                                getChildFragmentManager().popBackStack(); // TODO check
                            }
                        });

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Bundle args = getArguments();
                                args.putString("driverName", driverName);
                                args.putString("licensePlate", vehicleLicensePlate);
                                args.putString("vehicleModel", vehicleModel);
                                args.putString("driverPhoneNumber", driverPhoneNumber);
                                args.putString("driverImage", driverImage);
                                args.putLong("rideId", rideId);
                                args.putLong("driverId", driverId);
                                args.putString("time", ride.getEstimatedTimeInMinutes().toString());

                                PassengerCurrentRideFragment fragment = new PassengerCurrentRideFragment();
                                fragment.setArguments(args);

                                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.currentRide, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();}
                        }, 800);}
                }, throwable -> {
                    Log.e("STOMP", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }

}