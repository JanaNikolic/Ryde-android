package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.EditProfileFragment;
import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.model.response.UserResponse;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.TokenUtils;

import java.sql.Driver;
import java.time.LocalDate;

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
    private String thisMonthStart;
    private String thisMonthEnd;
    private UserResponse driver;


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
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        thisMonthStart = firstDay.toString();
        thisMonthEnd = lastDay.toString();
        initializeComponents(view);

        Button edit = (Button) view.findViewById(R.id.editBtn);
        Button report = (Button) view.findViewById(R.id.reportBtn);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("name", driver.getName());
                args.putString("surname", driver.getSurname());
                args.putString("address", driver.getAddress());
                args.putString("phoneNumber", driver.getTelephoneNumber());
                args.putString("email", driver.getEmail());
                editProfileFragment.setArguments(args);
                transaction.add(R.id.fragment_driver_container, editProfileFragment); // give your fragment container id in first parameter

                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_driver_container, new DriverStatisticsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        TextView fullName = view.findViewById(R.id.full_name_profile);
        ImageView profilePic = view.findViewById(R.id.profile_pic);
        TextView email = view.findViewById(R.id.email_profile);
        TextView model = view.findViewById(R.id.car_model);
        TextView licenseNumber = view.findViewById(R.id.license_number);
        TextView phoneNumber = view.findViewById(R.id.phone_num_profile);
        TextView fullname = view.findViewById(R.id.fullname_field);
        TextView emailfield = view.findViewById(R.id.email_field);

        TextView numberOfRides = (TextView) view.findViewById(R.id.number_of_rides);
        TextView moneyAmount = (TextView) view.findViewById(R.id.money_amount);
        TextView kilometers = (TextView) view.findViewById(R.id.kilometers);

        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Long id = tokenUtils.getId(token);

        Call<DriverResponse> call = driverService.getDriver(id, "Bearer " + token);

        Call<VehicleResponse> callVehicle = driverService.getDriversVehicle(id, "Bearer " + token);

        Call<RideStatisticsResponse> rideCount = driverService.getRideCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<MoneyStatisticsResponse> moneyCount = driverService.getMoneyCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<DistanceStatisticsResponse> distanceCount = driverService.getDistanceCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

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

                driver = response.body();
                if (driver != null) {
                    String fullNameStr = driver.getName() + " " + driver.getSurname();
                    fullName.setText(fullNameStr);
                    fullname.setText(fullNameStr);
                    email.setText(driver.getEmail());
                    emailfield.setText(driver.getEmail());
                    phoneNumber.setText(driver.getTelephoneNumber());
                }
            }
            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                call.cancel();
            }
        });

        rideCount.enqueue(new Callback<RideStatisticsResponse>() {
            @Override
            public void onResponse(Call<RideStatisticsResponse> call, Response<RideStatisticsResponse> response) {
                RideStatisticsResponse stats = response.body();

                if (stats != null) {
                    numberOfRides.setText(stats.getTotalCount().toString());
                }
            }

            @Override
            public void onFailure(Call<RideStatisticsResponse> call, Throwable t) {
                rideCount.cancel();
            }
        });

        moneyCount.enqueue(new Callback<MoneyStatisticsResponse>() {
            @Override
            public void onResponse(Call<MoneyStatisticsResponse> call, Response<MoneyStatisticsResponse> response) {
                MoneyStatisticsResponse stats = response.body();

                if (stats != null) {
                    moneyAmount.setText(Float.toString(stats.getTotalCount()));
                }
            }

            @Override
            public void onFailure(Call<MoneyStatisticsResponse> call, Throwable t) {
                moneyCount.cancel();
            }
        });

        distanceCount.enqueue(new Callback<DistanceStatisticsResponse>() {
            @Override
            public void onResponse(Call<DistanceStatisticsResponse> call, Response<DistanceStatisticsResponse> response) {
                DistanceStatisticsResponse stats = response.body();

                if (stats != null) {
                    kilometers.setText(Float.toString(stats.getTotalCount()));
                }
            }

            @Override
            public void onFailure(Call<DistanceStatisticsResponse> call, Throwable t) {
                distanceCount.cancel();
            }
        });
    }
}