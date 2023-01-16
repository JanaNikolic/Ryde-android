package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.FragmentTransition;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class PassengerCurrentRideFragment extends Fragment {
    private CompositeDisposable compositeDisposable;
    private StompClient mStompClient;
    private RetrofitService retrofitService;
    private TokenUtils tokenUtils;
    Gson gson = new Gson();
    String rideId;
    TextView timer;
    CountDownTimer countDownTimer;
    int time;
    int interval = 1000; // 1 second

    public PassengerCurrentRideFragment() {
        // Required empty public constructor
    }

    public static PassengerCurrentRideFragment newInstance() {
        PassengerCurrentRideFragment fragment = new PassengerCurrentRideFragment();
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
        View view = inflater.inflate(R.layout.fragment_passenger_current_ride, container, false);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.0.16:8080/example-endpoint/websocket");
        retrofitService = new RetrofitService();
        Bundle args = getArguments();
        rideId = args.getString("rideId");
        connectStomp();

        TextView startAddress = (TextView) view.findViewById(R.id.start_address);
        TextView endAddress = (TextView) view.findViewById(R.id.end_address);
        TextView startTime = (TextView) view.findViewById(R.id.start_time);
        TextView price = (TextView) view.findViewById(R.id.ride_price);
        TextView driverName = (TextView) view.findViewById(R.id.driver_name);
        TextView licenseNumber = (TextView) view.findViewById(R.id.license_number_ride);
        TextView model = (TextView) view.findViewById(R.id.model);
        ShapeableImageView driverPicture = (ShapeableImageView) view.findViewById(R.id.profile_photo);

        Button phone = (Button) view.findViewById(R.id.call_driver);
        Button message = (Button) view.findViewById(R.id.message_driver);

        String number = args.getString("driverPhoneNumber");
        //Log.i("brtelefona", number);

        driverName.setText(args.getString("driverName"));
        licenseNumber.setText(args.getString("licensePlate"));
        model.setText(args.getString("vehicleModel"));

        time = Integer.parseInt(args.getString("time")) * 1000 * 60;
        startAddress.setText(args.getString("startAddress"));
        endAddress.setText(args.getString("endAddress"));
        price.setText(args.getString("price"));

        startTime.setText(args.getString("timeStart").split("T")[1].split("\\.")[0]);
        Bundle route = getArguments().getBundle("route");

        if (route != null) {
            DrawRouteFragment draw = DrawRouteFragment.newInstance();
            draw.setArguments(route);
            FragmentTransition.to(draw, getActivity(), false);
        }
//        args.getString("driverImage"); // TODO

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle arg = new Bundle();


                arg.putLong("userId", args.getLong("driverId"));
                arg.putString("userName", args.getString("driverName"));

                ChatFragment chatPassengerFragment = new ChatFragment();
                chatPassengerFragment.setArguments(arg);

                transaction.add(R.id.currentRide, chatPassengerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        timer = (TextView) view.findViewById(R.id.ride_timer);

//        countDownTimer = new CountDownTimer(time, interval) {
//            public void onTick(long millisUntilFinished) {
//                timer.setText(getDateFromMillis(time - millisUntilFinished));
//            }
//
//            public void onFinish() {
//                timer.setText("FINISHED");
//                //TODO finished ride fragment
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.main_pass, new ReviewDriverAndVehicleFragment());
//
//                transaction.commit();
//
//                transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.currentRide, new PassengerCreateRideFragment());
//                transaction.commit();
//
//            }
//        };
//
//        countDownTimer.start();

        return view;
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
                    Ride ride = gson.fromJson(topicMessage.getPayload(), Ride.class);
                    if (ride.getStatus().equals("ACTIVE")) {
                        countDownTimer = new CountDownTimer(time, interval) {
                            public void onTick(long millisUntilFinished) {
                                Toast.makeText(getContext(), "Ride has ended!", Toast.LENGTH_SHORT).show();
                                timer.setText(getDateFromMillis(time - millisUntilFinished));
                            }

                            public void onFinish() {
                                timer.setText("FINISHED");
                            }
                        };
                        countDownTimer.start();
                    } else if (ride.getStatus().equals("FINISHED")) {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.main_pass, new ReviewDriverAndVehicleFragment());

                        transaction.commit();

                        transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.currentRide, new PassengerCreateRideFragment());
                        transaction.commit();
                    }
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


    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}