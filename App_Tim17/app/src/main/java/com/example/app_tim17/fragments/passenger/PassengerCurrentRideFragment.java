package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.driver.ChatDriverFragment;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class PassengerCurrentRideFragment extends Fragment {

    TextView timer;
    CountDownTimer countDownTimer;
    private TokenUtils tokenUtils;
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

        Bundle args = getArguments();

        String number = args.getString("driverPhoneNumber");; //TODO
        Log.i("brtelefona", number);

        driverName.setText(args.getString("driverName"));
        licenseNumber.setText(args.getString("licensePlate"));
        model.setText(args.getString("vehicleModel"));

        time = Integer.parseInt(args.getString("time")) * 1000 * 60;
        startAddress.setText(args.getString("startAddress"));
        endAddress.setText(args.getString("endAddress"));
        price.setText(args.getString("price"));

        startTime.setText(args.getString("timeStart").split("T")[1].split("\\.")[0]);


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

        countDownTimer = new CountDownTimer(time, interval) {
            public void onTick(long millisUntilFinished) {
                timer.setText(getDateFromMillis(time - millisUntilFinished));
            }

            public void onFinish() {
                timer.setText("FINISHED");
                //TODO finished ride fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.main_pass, new ReviewDriverAndVehicleFragment());

                transaction.commit();

                transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.currentRide, new PassengerCreateRideFragment());
                transaction.commit();

            }
        };

        countDownTimer.start();

        return view;
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