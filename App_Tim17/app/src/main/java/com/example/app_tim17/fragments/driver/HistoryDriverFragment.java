package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.DriverRideHistoryAdapter;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.TokenUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryDriverFragment extends Fragment {
    private static RecyclerView recyclerView;
    private DriverRideHistoryAdapter rideHistoryAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private RetrofitService retrofitService;
    private DriverService driverService;
    private TokenUtils tokenUtils;
    private List<Ride> driverRides;



    public HistoryDriverFragment() {
        // Required empty public constructor
    }

    public static HistoryDriverFragment newInstance(String param1, String param2) {
        HistoryDriverFragment fragment = new HistoryDriverFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_driver, container, false);

        retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        String token = getCurrentToken();
        Long id = tokenUtils.getId(token);

        Call<RideResponse> call = driverService.getDriversRides(id, "Bearer " + token);

        call.enqueue(new Callback<RideResponse>() {
            @Override
            public void onResponse(Call<RideResponse> call, Response<RideResponse> response) {
                RideResponse rides = response.body();

                if (rides != null) {
                    driverRides = rides.getRides();
                    Log.d("rides size", String.valueOf(rides.getRides().size()));

                    if (driverRides.size() > 0) {
                        recyclerView = view.findViewById(R.id.my_recycler_view);
                        recyclerView.setHasFixedSize(true);
                        rideHistoryAdapter = new DriverRideHistoryAdapter(getContext(), driverRides);

                        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(rideHistoryAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "You have no rides in history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RideResponse> call, Throwable t) {
                call.cancel();
            }
        });


        return view;
    }
    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}