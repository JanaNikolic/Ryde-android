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

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.model.response.Location;
import com.example.app_tim17.model.response.ride.LocationForRide;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.tools.FragmentTransition;
import com.example.app_tim17.tools.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverOnRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverOnRouteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RetrofitService retrofitService;
    private RideService rideService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverOnRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverOnRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverOnRouteFragment newInstance(String param1, String param2) {
        DriverOnRouteFragment fragment = new DriverOnRouteFragment();
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
        View view = inflater.inflate(R.layout.fragment_driver_on_route, container, false);

        Button start = view.findViewById(R.id.start_btn);

        TextView startAddress = view.findViewById(R.id.start_address);
        TextView endAddress = view.findViewById(R.id.end_address);
        TextView price = view.findViewById(R.id.price);
        TextView duration = view.findViewById(R.id.duration);
        TextView distance = view.findViewById(R.id.distance); // caluclate

        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        Bundle sentArgs = getArguments();
        Ride ride = null;
        if (sentArgs != null && sentArgs.containsKey("ride")) {
            ride = Utils.getGsonParser().fromJson(sentArgs.getString("ride"), Ride.class);

            startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
            endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
            String priceStr = ride.getTotalCost() + " RSD";
            price.setText(priceStr);

            priceStr =  ride.getEstimatedTimeInMinutes() + " min";
            duration.setText(priceStr);
        }


        Ride finalRide = ride;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle route = new Bundle();
//                route.putDouble("fromLat", finalRide.getLocations().get(0).getDeparture().getLatitude());
//                route.putDouble("fromLng", finalRide.getLocations().get(0).getDeparture().getLongitude());
                route.putDouble("fromLat", 45.257430);
                route.putDouble("fromLng", 19.840850);
//                route.putDouble("toLat", finalRide.getLocations().get(0).getDestination().getLatitude());
//                route.putDouble("toLng", finalRide.getLocations().get(0).getDestination().getLongitude());
                route.putDouble("toLat", 45.241290);
                route.putDouble("toLng", 19.847320);
//                DrawRouteFragment draw = DrawRouteFragment.newInstance();
//                draw.setArguments(route);
//                FragmentTransition.to(draw, getActivity(), false);

                String token = "Bearer " + getCurrentToken();

                Call<Ride> call = rideService.startRide(token, finalRide.getId());


                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        Ride ride = response.body();

                        if (ride != null) {
                            sentArgs.putString("ride", Utils.getGsonParser().toJson(ride));

                            DriverCurrentRideFragment currentRideFragment = new DriverCurrentRideFragment();
                            currentRideFragment.setArguments(sentArgs);

                            getParentFragmentManager().beginTransaction().remove(DriverOnRouteFragment.this).commit();

                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.currentRide, currentRideFragment);
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




        return view;
    }
    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}