package com.example.app_tim17.fragments.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.ReviewDriverAndVehicleFragment;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDriverFragment extends Fragment {

    private DriverService driverService;
    private TokenUtils tokenUtils;

    public ProfileDriverFragment() {
        // Required empty public constructor
    }


    public static ProfileDriverFragment newInstance(String param1, String param2) {
        ProfileDriverFragment fragment = new ProfileDriverFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_driver, container, false);
        tokenUtils = new TokenUtils();
        RetrofitService retrofitService = new RetrofitService();

        driverService = retrofitService.getRetrofit().create(DriverService.class);

        initializeComponents(view);

        Button editProfile = (Button) view.findViewById(R.id.edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                transaction.add(R.id.activity_driver_account, new ReviewDriverAndVehicleFragment()); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        TextView fullName = view.findViewById(R.id.full_name_profile);
        TextView email = view.findViewById(R.id.email_profile);
        TextView model = view.findViewById(R.id.car_model);
        TextView licenseNumber = view.findViewById(R.id.license_number);
        TextView phoneNumber = view.findViewById(R.id.phone_num_profile);

        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Long id = tokenUtils.getId(token);

        Call<DriverResponse> call = driverService.getDriver(id, "Bearer " + token);

        Call<VehicleResponse> callVehicle = driverService.getDriversVehicle(id, "Bearer " + token);

        callVehicle.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                VehicleResponse vehicle = response.body();

                if (vehicle != null) {
                    model.setText(vehicle.getModel());
                    licenseNumber.setText(vehicle.getLicenseNumber());
                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                callVehicle.cancel();
            }
        });
        call.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {

//                Log.d("TAG",response.code()+"");

                DriverResponse driver = response.body();
                if (driver != null) {
                    String fullNameStr = driver.getName() + " " + driver.getSurname();
                    fullName.setText(fullNameStr);
                    email.setText(driver.getEmail());
                    phoneNumber.setText(driver.getTelephoneNumber());
                    // TODO add profile picture
                }
            }
            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}