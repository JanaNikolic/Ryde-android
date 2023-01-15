package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.MapsFragment;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDriverFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RetrofitService retrofitService;
    private RideService rideService;
    private TokenUtils tokenUtils;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainDriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainDriverFragment newInstance(String param1, String param2) {
        MainDriverFragment fragment = new MainDriverFragment();
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_driver, container, false);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.sheet));
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Fragment fragment = new MapsFragment();

        // Open fragment
        getChildFragmentManager()
                .beginTransaction().replace(R.id.map_container,fragment)
                .commit();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Bundle args = fragment.getArguments();
                if (args == null)
                    args = new Bundle();

                retrofitService = new RetrofitService();
                rideService = retrofitService.getRetrofit().create(RideService.class);
                String token = "Bearer " + getCurrentToken();


                tokenUtils = new TokenUtils();
                Call<Ride> call = rideService.getActiveRide(token, tokenUtils.getId(getCurrentToken()));

                Bundle finalArgs = args;
                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        Ride ride = response.body();



                        if (ride != null) {
                            finalArgs.putString("ride", Utils.getGsonParser().toJson(ride));
                            DriverAcceptanceRideFragment acceptanceRideFragment = new DriverAcceptanceRideFragment();
                            acceptanceRideFragment.setArguments(finalArgs);

                            getChildFragmentManager().beginTransaction()
                                    .add(R.id.map_container, acceptanceRideFragment)
                                    .commit();
                        }

                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        }, 5000);

        return view;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}