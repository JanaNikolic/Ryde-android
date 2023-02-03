package com.example.app_tim17.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.tools.Utils;
import com.google.android.material.imageview.ShapeableImageView;

public class UserInfoFragment extends Fragment {


    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        TextView name = view.findViewById(R.id.name);
        ShapeableImageView photo = view.findViewById(R.id.profile);
        TextView email = view.findViewById(R.id.email);

        if (getArguments() != null) {
            PassengerResponse passenger = Utils.getGsonParser().fromJson(getArguments().getString("passenger"), PassengerResponse.class);

            name.setText(passenger.getName() + " " + passenger.getSurname());
            email.setText(passenger.getEmail());
            //TODO set photo
        }

        Button exit = (Button) view.findViewById(R.id.exit_pass_info);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }



}