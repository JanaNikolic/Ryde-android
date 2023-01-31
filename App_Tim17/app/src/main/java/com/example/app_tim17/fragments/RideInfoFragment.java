package com.example.app_tim17.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.review.DriverReview;
import com.example.app_tim17.model.response.review.RideReviewsResponse;
import com.example.app_tim17.model.response.review.VehicleReview;
import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RideInfoFragment extends Fragment {

    private Ride ride;
    private PassengerRideResponse passengerRideResponse;
    private RetrofitService retrofitService;
    private PassengerService passengerService;
    private RideReviewsResponse rideReviews;
    private CardView driverReviewCard;
    private CardView vehicleReviewCard;

    public RideInfoFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ride = Utils.getGsonParser().fromJson(getArguments().getString("ride"), Ride.class);
            passengerRideResponse = ride.getPassengers().get(0);
            if (getArguments().containsKey("reviews")) {
                Log.i("sent reviews", getArguments().getString("reviews"));
                rideReviews = Utils.getGsonParser().fromJson(getArguments().getString("reviews"), RideReviewsResponse.class);
            } else {
                rideReviews = null;
            }
        }
        retrofitService = new RetrofitService();

        passengerService = retrofitService.getRetrofit().create(PassengerService.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_info, container, false);

        TextView date = view.findViewById(R.id.date);
        TextView startAddress = view.findViewById(R.id.start_address);
        TextView endAddress = view.findViewById(R.id.end_address);

        TextView startTime = view.findViewById(R.id.start_time);
        TextView endTime = view.findViewById(R.id.end_time);
        TextView price = view.findViewById(R.id.price);
        TextView distance= view.findViewById(R.id.ride_length);



        date.setText(ride.getStartTime().split("T")[0]);
        startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
        endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
        startTime.setText(ride.getStartTime().split("T")[1].substring(0, 5));
        endTime.setText(ride.getEndTime().split("T")[1].substring(0, 5));
        price.setText(String.format("%d RSD", ride.getTotalCost()));
        distance.setText(ride.getDistance()/1000 + " km");

        Call<PassengerResponse> call = passengerService.getPassenger("Bearer " + getCurrentToken(), passengerRideResponse.getId());

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                PassengerResponse passenger = response.body();
                LinearLayout reviewCardsLayout = view.findViewById(R.id.review_card);

                vehicleReviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);
                driverReviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);

                if (passenger != null) {
                    Log.i("jeeeeeeees", "");
                    if (rideReviews != null) {
                        Log.i("jeeeeeeees", "reviews not null");
                        for (DriverReview driverReview: rideReviews.getDriverReview()) {
                            Log.i("jeeeeeeees", "for petlja");

                            if (Objects.equals(passenger.getId(), driverReview.getPassenger().getId())) {
                                Log.i("jeeeeeeees", "if pass ...");

                                TextView passInfo = driverReviewCard.findViewById(R.id.review_passenger);
                                passInfo.setText(String.format("%s %s", passenger.getName(), passenger.getSurname()));
                                passInfo = driverReviewCard.findViewById(R.id.review_content);
                                passInfo.setText(driverReview.getComment());
                                reviewCardsLayout.addView(driverReviewCard);
                                RatingBar rating = driverReviewCard.findViewById(R.id.stars);
                                rating.setRating(driverReview.getRating());
                                //TODO set photo
                                break;
                            }
                        }

                        for (VehicleReview vehicleReview: rideReviews.getVehicleReview()) {
                            if (Objects.equals(passenger.getId(), vehicleReview.getPassenger().getId())) {

                                TextView passInfo = vehicleReviewCard.findViewById(R.id.review_passenger);
                                passInfo.setText(String.format("%s %s", passenger.getName(), passenger.getSurname()));
                                passInfo = vehicleReviewCard.findViewById(R.id.review_content);
                                passInfo.setText(vehicleReview.getComment());
                                reviewCardsLayout.addView(vehicleReviewCard);
                                RatingBar rating = vehicleReviewCard.findViewById(R.id.stars);
                                rating.setRating(vehicleReview.getRating());
                                //TODO set photo

                                break;
                            }
                        }
                    } else {
                        TextView passInfo = vehicleReviewCard.findViewById(R.id.review_passenger);
                        passInfo.setText(String.format("%s %s", passenger.getName(), passenger.getSurname()));
                        passInfo = vehicleReviewCard.findViewById(R.id.review_content);
                        passInfo.setText("No vehicle review yet");
                        reviewCardsLayout.addView(vehicleReviewCard);
                        RatingBar rating = vehicleReviewCard.findViewById(R.id.stars);
                        rating.setRating(0);
                        //TODO set photo

                        TextView passInfo2 = driverReviewCard.findViewById(R.id.review_passenger);
                        passInfo2.setText(String.format("%s %s", passenger.getName(), passenger.getSurname()));
                        passInfo2 = driverReviewCard.findViewById(R.id.review_content);
                        passInfo2.setText("No driver review yet");
                        reviewCardsLayout.addView(driverReviewCard);
                        RatingBar rating2 = driverReviewCard.findViewById(R.id.stars);
                        rating2.setRating(0);
                        //TODO set photo

                    }
                    Bundle args = new Bundle();
                    args.putString("passenger", Utils.getGsonParser().toJson(passenger));

                    driverReviewCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            UserInfoFragment fragment = new UserInfoFragment();
                            fragment.setArguments(args);
                            transaction.add(R.id.ride_info_fragment,fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    });

                    vehicleReviewCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                            UserInfoFragment fragment = new UserInfoFragment();
                            fragment.setArguments(args);
                            transaction.add(R.id.ride_info_fragment,fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {

            }
        });
        return view;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}