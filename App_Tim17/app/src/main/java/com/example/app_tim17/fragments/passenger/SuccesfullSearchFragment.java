package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.VehicleRequest;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuccesfullSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccesfullSearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RetrofitService retrofitService;
    private TokenUtils tokenUtils;
    private DriverService driverService;
    private String driverName;
    private String driverImage;
    private String vehicleModel;
    private String vehicleLicensePlate;
    private String driverPhoneNumber;

    public SuccesfullSearchFragment() {
        // Required empty public constructor
    }
    public static SuccesfullSearchFragment newInstance(String param1, String param2) {
        SuccesfullSearchFragment fragment = new SuccesfullSearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_succesfull_search, container, false);

        String driverId = getArguments().getString("driverId");
        retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        String token = "Bearer " + getCurrentToken();

        Call<DriverResponse> call = driverService.getDriver(Long.valueOf(driverId), token);

        call.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                DriverResponse driver = response.body();
                if (driver != null) {
                    driverName = driver.getName() + " " + driver.getSurname();
                    driverImage = driver.getProfilePicture(); // TODO

                }
            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                getChildFragmentManager().popBackStack(); // TODO check
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

                PassengerCurrentRideFragment fragment = new PassengerCurrentRideFragment();
                fragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.currentRide, fragment);
                fragmentTransaction.addToBackStack(null);// TODO for messages
                fragmentTransaction.commit();}
        }, 4800);


    return v;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}