package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.EditProfileFragment;
import com.example.app_tim17.fragments.UserInfoFragment;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.UserResponse;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePassengerFragment extends Fragment {
    private PassengerService passengerService;
    private TokenUtils tokenUtils;
    private String thisMonthStart;
    private String thisMonthEnd;
    private UserResponse passenger;

    public ProfilePassengerFragment() {
        // Required empty public constructor
    }

    public static ProfilePassengerFragment newInstance(String param1, String param2) {
        ProfilePassengerFragment fragment = new ProfilePassengerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_passenger, container, false);

        tokenUtils = new TokenUtils();
        RetrofitService retrofitService = new RetrofitService();
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        thisMonthStart = firstDay.toString();
        thisMonthEnd = lastDay.toString();

        Call<PassengerResponse> call = passengerService.getPassenger("Bearer " + getCurrentToken(), TokenUtils.getId(getCurrentToken()));

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                passenger = response.body();
            }
            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {
                call.cancel();
            }
        });

        Button favBtn = (Button) view.findViewById(R.id.favRouteBtn);
        Button edit = (Button) view.findViewById(R.id.reportBtn);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_passenger_container, new PassengerFavoriteRouteFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("name", passenger.getName());
                args.putString("surname", passenger.getSurname());
                args.putString("address", passenger.getAddress());
                args.putString("phoneNumber", passenger.getTelephoneNumber());
                args.putString("email", passenger.getEmail());
                editProfileFragment.setArguments(args);
                transaction.add(R.id.fragment_passenger_container, editProfileFragment); // give your fragment container id in first parameter

                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.add(R.id.fragment_passenger_container, new PassengerReportFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        return view;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}