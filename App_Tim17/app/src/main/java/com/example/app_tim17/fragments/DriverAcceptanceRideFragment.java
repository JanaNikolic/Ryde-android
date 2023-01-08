package com.example.app_tim17.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.app_tim17.R;

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
//                getActivity().onBackPressed();
                getParentFragmentManager().beginTransaction().remove(DriverAcceptanceRideFragment.this).commit();
            }
        });

        return view;
    }
}