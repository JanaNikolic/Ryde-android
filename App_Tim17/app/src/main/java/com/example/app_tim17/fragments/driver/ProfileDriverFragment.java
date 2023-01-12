package com.example.app_tim17.fragments.driver;

import android.annotation.SuppressLint;
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
import com.google.android.gms.maps.SupportMapFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDriverFragment extends Fragment {

    DriverService driverService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileDriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDriverFragment newInstance(String param1, String param2) {
        ProfileDriverFragment fragment = new ProfileDriverFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_driver, container, false);

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

        Call<DriverResponse> call = driverService.getDriver(1001L);

        Call<VehicleResponse> callVehicle = driverService.getDriversVehicle(1001L);

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
                }
            }
            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}