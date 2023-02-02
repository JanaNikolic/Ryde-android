package com.example.app_tim17.fragments.passenger;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.PassengerSingularRideHistoryActivity;
import com.example.app_tim17.adapters.DriveHistoryList;
import com.example.app_tim17.adapters.DriverRideHistoryAdapter;
import com.example.app_tim17.adapters.InboxList;
import com.example.app_tim17.adapters.PassengerRideHistoryAdapter;
import com.example.app_tim17.model.response.chat.Chat;
import com.example.app_tim17.model.response.chat.ChatResponse;
import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryPassengerFragment extends Fragment implements SensorEventListener{
    SensorManager sensorManager;
    private boolean order;
    private static RecyclerView recyclerView;
    private PassengerRideHistoryAdapter rideHistoryAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private List<Ride> rides;
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;
    private PassengerService passengerService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryPassengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryPassengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryPassengerFragment newInstance(String param1, String param2) {
        HistoryPassengerFragment fragment = new HistoryPassengerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    private static final int SHAKE_THRESHOLD = 10;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (order){
                        Collections.sort(rides, compareByEndDate.reversed());
                        System.out.println(rides);
                        order = false;
                    }
                    else{
                        Collections.sort(rides, compareByEndDate);
                        System.out.println(rides);
                        order = true;
                    }


                    System.out.println("Accelerometer: shaking \n [" + x + ", " + y + ", " + z + "]");
                }else{
                    System.out.println("Accelerometer: not shaking \n [" + x + ", " + y + ", " + z + "]");
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }

        }
    }
    Comparator<Ride> compareByEndDate =

            (Ride ride1, Ride ride2) -> ride1.getStartTime().compareTo( ride2.getStartTime() );

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_passenger, container, false);
        tokenUtils = new TokenUtils();
        retrofitService = new RetrofitService();
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);
        recyclerView = view.findViewById(R.id.pass_history_cards);

        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Long id = tokenUtils.getId(token);
        Call<RideResponse> call = passengerService.getPassengerRides("Bearer " + token, id);
        call.enqueue(new Callback<RideResponse>() {
            @Override
            public void onResponse(Call<RideResponse> call, Response<RideResponse> response) {
                Log.d("WTF", response.body().toString());
                if (response.isSuccessful()) {
                    Log.d("WTF", response.body().toString());
                    RideResponse rideResponse = response.body();
                    Log.d("WTF", rideResponse.toString());

                    rides = new ArrayList<>();
                    if (rideResponse.getRides() != null) {
                        for (Ride ride : rideResponse.getRides()) {
                            rides.add(ride);

                        }
                        rideHistoryAdapter = new PassengerRideHistoryAdapter(getContext(), rides);
                        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(rideHistoryAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<RideResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "NOT WORKING", Toast.LENGTH_SHORT);
            }
        });





        return view;
    }
    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}