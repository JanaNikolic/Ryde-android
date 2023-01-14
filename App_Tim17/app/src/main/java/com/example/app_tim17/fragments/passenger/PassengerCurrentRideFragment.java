package com.example.app_tim17.fragments.passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app_tim17.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class PassengerCurrentRideFragment extends Fragment {

    TextView timer;
    CountDownTimer countDownTimer;
    int time = 3 * 1000; //  TODO get form database
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
}