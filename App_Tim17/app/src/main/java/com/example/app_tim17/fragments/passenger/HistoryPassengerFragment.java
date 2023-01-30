package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryPassengerFragment extends Fragment {

    private static RecyclerView recyclerView;
    private PassengerRideHistoryAdapter rideHistoryAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private List<String> dates;
    private List<String> startTimes;
    private List<String> endTimes;
    private List<String> durations;
    private List<String> totalCosts;
    private List<String> numOfPassengers;
    private TokenUtils tokenUtils;
    private List<String> startAddresses;
    private RetrofitService retrofitService;
    private PassengerService passengerService;
    private List<String> endAddresses;
    private List<String> roadLengths;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                    dates = new ArrayList<>();
                    startTimes = new ArrayList<>();
                    endTimes = new ArrayList<>();
                    durations = new ArrayList<>();
                    totalCosts = new ArrayList<>();
                    numOfPassengers = new ArrayList<>();
                    startAddresses = new ArrayList<>();
                    endAddresses = new ArrayList<>();
                    endAddresses = new ArrayList<>();
                    roadLengths = new ArrayList<>();
                    if (rideResponse != null) {
                        for (Ride ride : rideResponse.getRides()) {
                            dates.add(ride.getStartTime().split("T")[0]);
                            startTimes.add(ride.getStartTime().split("T")[1].split("\\.")[0]);
                            System.out.println(startTimes);
                            endTimes.add(ride.getEndTime().split("T")[1].split("\\.")[0]);
                            durations.add(String.valueOf(ride.getEstimatedTimeInMinutes())+" min");
                            totalCosts.add(String.valueOf(ride.getTotalCost())+" rsd");
                            numOfPassengers.add(String.valueOf(ride.getPassengers().size()));
                            startAddresses.add(ride.getLocations().get(0).getDeparture().getAddress());
                            endAddresses.add(ride.getLocations().get(0).getDestination().getAddress());
                            roadLengths.add(String.valueOf(45));


                        }
                        rideHistoryAdapter = new PassengerRideHistoryAdapter(getContext(), dates, startTimes, endTimes, durations, totalCosts, numOfPassengers, startAddresses, endAddresses,roadLengths);
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