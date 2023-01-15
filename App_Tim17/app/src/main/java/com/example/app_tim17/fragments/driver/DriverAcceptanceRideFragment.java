package com.example.app_tim17.fragments.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.passenger.SuccesfullSearchFragment;

public class DriverAcceptanceRideFragment extends Fragment {
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
        Button accept = view.findViewById(R.id.accept_btn);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().remove(DriverAcceptanceRideFragment.this).commit();

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.currentRide, new DriverOnRouteFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}