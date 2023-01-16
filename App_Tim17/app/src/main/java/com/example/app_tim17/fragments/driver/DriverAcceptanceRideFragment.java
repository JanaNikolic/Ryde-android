package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.fragments.passenger.SuccesfullSearchFragment;
import com.example.app_tim17.model.response.Location;
import com.example.app_tim17.model.response.ride.LocationForRide;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.tools.FragmentTransition;
import com.example.app_tim17.tools.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverAcceptanceRideFragment extends Fragment {
    private RetrofitService retrofitService;
    private RideService rideService;

    public DriverAcceptanceRideFragment() {
        super(R.layout.fragment_driver_acceptance_ride);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_acceptance_ride, container, false);
        TextView startAddress = view.findViewById(R.id.start_address);
        TextView endAddress = view.findViewById(R.id.end_address);
        TextView price = view.findViewById(R.id.price);
        TextView duration = view.findViewById(R.id.duration);
        TextView distance = view.findViewById(R.id.distance); // caluclate

        Button accept = view.findViewById(R.id.accept_btn);
        Button reject = view.findViewById(R.id.decline);
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);

        Bundle args = getArguments();
        Ride ride = null;
        if (args != null && args.containsKey("ride")) {
            ride = Utils.getGsonParser().fromJson(args.getString("ride"), Ride.class);

            startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
            endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
            String priceStr = ride.getTotalCost() + " RSD";
            price.setText(priceStr);

            priceStr = ride.getEstimatedTimeInMinutes() + " min";
            duration.setText(priceStr);
        }

        Ride finalRide = ride;
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = "Bearer " + getCurrentToken();

                Call<Ride> call = rideService.acceptRide(token, finalRide.getId()); // TODO change ride and id

                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        Ride ride = response.body();
                        if (ride != null) {

                            Bundle args = new Bundle();
                            Bundle route = new Bundle();

                            args.putString("ride", Utils.getGsonParser().toJson(ride));

                            Bundle sentArgs = getArguments();

                            if (sentArgs != null && sentArgs.containsKey("currentLat") && sentArgs.containsKey("currentLng")) {
                                // from driver accepting ride, his current location routing to the pickup location
                                route.putDouble("fromLat", sentArgs.getDouble("currentLat"));
                                route.putDouble("fromLng", sentArgs.getDouble("currentLng"));
                                Log.i("prva", "prvi if");
                                route.putDouble("toLat", 45.257430);
                                route.putDouble("toLng", 19.840850);
                            } else {
                                Toast.makeText(view.getContext(), "Oops, something went wrong...", Toast.LENGTH_SHORT).show();
                            }

                            Log.i("fromLat", String.valueOf(route.getDouble("fromLat")));
                            Log.i("fromLng", String.valueOf(route.getDouble("fromLng")));
                            Log.i("toLat", String.valueOf(route.getDouble("toLat")));
                            Log.i("toLng", String.valueOf(route.getDouble("toLng")));


                            DrawRouteFragment draw = DrawRouteFragment.newInstance();
                            draw.setArguments(route);
                            FragmentTransition.to(draw, getActivity(), false);

                            DriverOnRouteFragment driverOnRouteFragment = new DriverOnRouteFragment();
                            driverOnRouteFragment.setArguments(args);

                            getParentFragmentManager().beginTransaction().remove(DriverAcceptanceRideFragment.this).commit();

                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.currentRide, driverOnRouteFragment);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = "Bearer " + getCurrentToken();

                Call<Ride> call = rideService.rejectRide(token, finalRide.getId()); // TODO change ride and id

                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        getParentFragmentManager().beginTransaction().remove(DriverAcceptanceRideFragment.this).commit();

                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.map_container, new DeclineMessageFragment());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });

        return view;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}