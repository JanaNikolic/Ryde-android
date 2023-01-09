package com.example.app_tim17.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app_tim17.R;

public class RideInfoFragment extends Fragment {

//    public RideInfoFragment() {
//        // Required empty public constructor
//    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_info, container, false);



        LinearLayout reviewCardsLayout = view.findViewById(R.id.review_card);

        CardView reviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);
        CardView reviewCard2 = (CardView) inflater.inflate(R.layout.review_card, container, false);
        CardView reviewCard3 = (CardView) inflater.inflate(R.layout.review_card, container, false);

        TextView passInfo = reviewCard.findViewById(R.id.review_passenger);
        passInfo.setText("Pera Peric");
        passInfo = reviewCard.findViewById(R.id.review_content);
        passInfo.setText("Voznja je bila prijatna.");
        reviewCardsLayout.addView(reviewCard);

        reviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout v = view.findViewById(R.id.passenger_info);
                v.setVisibility(View.VISIBLE);

                ImageView exit = v.findViewById(R.id.exit_pass_info);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        v.setVisibility(View.GONE);
                    }
                });


            }
        });


        passInfo = reviewCard2.findViewById(R.id.review_passenger);
        passInfo.setText("Mila Peric");
        passInfo = reviewCard2.findViewById(R.id.review_content);
        passInfo.setText("Voznja je bila udobna.");
        reviewCardsLayout.addView(reviewCard2);

        passInfo = reviewCard3.findViewById(R.id.review_passenger);
        passInfo.setText("Mika Peric");
        passInfo = reviewCard3.findViewById(R.id.review_content);
        passInfo.setText("Voznja je bila prijatna.");
        reviewCardsLayout.addView(reviewCard3);
        return view;
    }
}