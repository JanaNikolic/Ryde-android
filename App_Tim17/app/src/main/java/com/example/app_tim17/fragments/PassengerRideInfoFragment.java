package com.example.app_tim17.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app_tim17.R;

public class PassengerRideInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_ride_info, container, false);



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
//                ConstraintLayout inside = view.findViewById(R.id.pass_info_inside_ride_history);
                // TODO izbaciti
//                ConstraintLayout v = view.findViewById(R.id.passenger_info);

//                view.findViewById(R.id.ride_info_fragment).addView();
//                inside.setVisibility(View.VISIBLE);
//                v.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                //TODO add passenger_info fragment
                transaction.add(R.id.passenger_info, PassengerRideInfoFragment.this);
                transaction.commit();


//                ImageView exit = transaction.findViewById(R.id.exit_pass_info);
//                exit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        inside.setVisibility(View.GONE);
//                    }
//                });


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