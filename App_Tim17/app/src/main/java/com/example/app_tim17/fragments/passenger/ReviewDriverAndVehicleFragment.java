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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.app_tim17.R;

public class ReviewDriverAndVehicleFragment extends Fragment {


    public static ReviewDriverAndVehicleFragment newInstance() {
        return new ReviewDriverAndVehicleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_review_driver_and_vehicle, container, false);

        RatingBar driverRatingBar = (RatingBar) view.findViewById(R.id.ratingBarDriver);
        RatingBar vehicleRatingBar = (RatingBar) view.findViewById(R.id.ratingBarVehicle);

        EditText driverFeedback = (EditText) view.findViewById(R.id.driverFeedback);
        EditText vehicleFeedback = (EditText) view.findViewById(R.id.vehicleFeedback);
        Button sendFeedback = (Button) view.findViewById(R.id.submitBtn);
        Button laterBtn = (Button) view.findViewById(R.id.laterBtn);

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driverFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in driver feedback text box", Toast.LENGTH_LONG).show();
                } else if (vehicleFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in driver feedback text box", Toast.LENGTH_LONG).show();
                }
                else {
                    // TODO send request
                    driverFeedback.setText("");
                    vehicleFeedback.setText("");
                    driverRatingBar.setRating(0);
                    vehicleRatingBar.setRating(0);
                    Toast.makeText(getActivity(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }
// TODO Otvaranje fragmenta u aktivnosti
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.activity_driver_account, new ReviewDriverAndVehicleFragment()); // activity_driver_account -> id layouta aktivnosti u kojoj otvaramo fragment
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();

}