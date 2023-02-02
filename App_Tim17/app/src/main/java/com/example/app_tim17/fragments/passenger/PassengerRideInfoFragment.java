package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.EditProfileFragment;
import com.example.app_tim17.fragments.UserInfoFragment;
import com.example.app_tim17.model.request.FavoriteRouteRequest;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.review.Review;
import com.example.app_tim17.model.response.review.ReviewRideResponse;
import com.example.app_tim17.model.response.ride.FavoriteRoute;
import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.ReviewService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideInfoFragment extends Fragment {
    private Ride ride;
    private ReviewRideResponse rideReview;
    private RideService rideService;
    private TokenUtils tokenUtils;
    private ReviewService reviewService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_ride_info, container, false);
        Long id = getArguments().getLong("ride_id");
        tokenUtils = new TokenUtils();
        RetrofitService retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        reviewService = retrofitService.getRetrofit().create(ReviewService.class);

        LinearLayout reviewCardsLayout = view.findViewById(R.id.review_card);
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Call<ReviewRideResponse> call = reviewService.getRideReviews("Bearer " + token, id);
        call.enqueue(new Callback<ReviewRideResponse>() {
            @Override
            public void onResponse(Call<ReviewRideResponse> call, Response<ReviewRideResponse> response) {

                rideReview = response.body();
                System.out.println(response);
                if (rideReview != null) {
                    for (Review vehicleReview: rideReview.getVehicleReview()) {
                        CardView reviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);
                        TextView passInfo = reviewCard.findViewById(R.id.review_passenger);
                        passInfo.setText(vehicleReview.getPassenger().getEmail());
                        TextView comment = reviewCard.findViewById(R.id.review_content);
                        String fullComent = "Vehicle review: " + vehicleReview.getComment();
                        comment.setText(fullComent);
                        RatingBar ratingBar = reviewCard.findViewById(R.id.ratingBarHistory);
                        ratingBar.setRating(vehicleReview.getRating());
                        reviewCardsLayout.addView(reviewCard);
                    }
                    for (Review driverReview: rideReview.getDriverReview()) {
                        CardView reviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);
                        TextView passInfo = reviewCard.findViewById(R.id.review_passenger);
                        passInfo.setText(driverReview.getPassenger().getEmail());
                        TextView comment = reviewCard.findViewById(R.id.review_content);
                        String fullComent = "Driver review: " + driverReview.getComment();
                        comment.setText(fullComent);
                        RatingBar ratingBar = reviewCard.findViewById(R.id.ratingBarHistory);
                        ratingBar.setRating(driverReview.getRating());
                        reviewCardsLayout.addView(reviewCard);
                    }
                }
            }
            @Override
            public void onFailure(Call<ReviewRideResponse> call, Throwable t) {
                call.cancel();
            }
        });
        initializeComponents(view, id);

//        reviewCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ConstraintLayout inside = view.findViewById(R.id.pass_info_inside_ride_history);
//                // TODO izbaciti
////                ConstraintLayout v = view.findViewById(R.id.passenger_info);
////                view.findViewById(R.id.ride_info_fragment).addView();
////                inside.setVisibility(View.VISIBLE);
////                v.setVisibility(View.VISIBLE);
//                //TODO add passenger_info fragment
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.add(R.id.ride_info_fragment, new UserInfoFragment());
//                transaction.addToBackStack(null);
//                transaction.commit();
//
//
////                ImageView exit = transaction.findViewById(R.id.exit_pass_info);
////                exit.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        inside.setVisibility(View.GONE);
////                    }
////                });
//
//
//            }
//        });
//
//
        Button addReview = (Button) view.findViewById(R.id.addReview);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Objects.equals(ride.getStatus(), "FINISHED")) {
                    Toast.makeText(getActivity(), "Ride must finish!", Toast.LENGTH_LONG).show();
                } else {
                    String time = ride.getEndTime().split("T")[0];
                    int year = Integer.parseInt(time.split("-")[0]);
                    int month = Integer.parseInt(time.split("-")[1]);
                    int day = Integer.parseInt(time.split("-")[2]);
                    LocalDate date2 = LocalDate.of(year, month, day);
                    LocalDate date3 = LocalDate.now();
                    long days = ChronoUnit.DAYS.between(date2, date3);
                    if (days >= 3) {
                        Toast.makeText(getActivity(), "3 Days have passed, can't create review anymore", Toast.LENGTH_LONG).show();
                    } else {
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        ReviewFromHistory reviewFromHistory = new ReviewFromHistory();
                        Bundle args = new Bundle();
                        args.putLong("ride_id", id);
                        reviewFromHistory.setArguments(args);
                        transaction.add(R.id.fragment_passenger_container, reviewFromHistory); // give your fragment container id in first parameter
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                    }
                }
            }
        });
        Button addFavorite = (Button) view.findViewById(R.id.add_favorite);
        addFavorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                FavoriteRouteRequest favoriteRoute = new FavoriteRouteRequest();
                favoriteRoute.setFavoriteName("Home - Work");
                favoriteRoute.setLocations(ride.getLocations());
                favoriteRoute.setBabyTransport(ride.getBabyTransport());
                List<PassengerRideResponse> pass = new ArrayList<>();
                favoriteRoute.setPassengers(pass);
                favoriteRoute.setPetTransport(ride.getPetTransport());
                favoriteRoute.setVehicleType(ride.getVehicleType());
                Call<FavoriteRoute> call = rideService.createFavorite("Bearer " + token, favoriteRoute);
                call.enqueue(new Callback<FavoriteRoute>() {
                    @Override
                    public void onResponse(Call<FavoriteRoute> call, Response<FavoriteRoute> response) {
                        System.out.println(response.body());
                        Toast.makeText(getActivity(), "Added favorite route!", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onFailure(Call<FavoriteRoute> call, Throwable t) {
                        call.cancel();
                    }
                });

            }
        });
        return view;
    }
    private void initializeComponents(View view, Long id) {
        TextView startAddress = view.findViewById(R.id.start_address_pass);
        TextView endAddress = view.findViewById(R.id.end_address_pass);
        TextView price = view.findViewById(R.id.price_pass);
        TextView date = view.findViewById(R.id.date_pass);
        TextView endTime = view.findViewById(R.id.end_time_pass);
        TextView startTime = view.findViewById(R.id.start_time_pass);
        TextView duration = view.findViewById(R.id.ride_duration_pass);
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Call<Ride> call = rideService.getRide("Bearer " + token, id);
        call.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {

                ride = response.body();
                System.out.println(response);
                if (ride != null) {
                    date.setText((ride.getStartTime().split("T")[0]));
                    startTime.setText(ride.getStartTime().split("T")[1].split("\\.")[0]);
                    String endTimeText;
                    if(ride.getEndTime() == null){
                        endTimeText = "Not finished";
                        String durationRide = String.valueOf(ride.getEstimatedTimeInMinutes() + " Min");
                        duration.setText(durationRide);
                    }
                    else{
                        endTimeText = ride.getEndTime().split("T")[1].split("\\.")[0];
                        int endTimeHour = Integer.parseInt(endTimeText.split(":")[0]);
                        int endTimeMinute = Integer.parseInt(endTimeText.split(":")[1]);
                        int endTimeSeconds = Integer.parseInt(endTimeText.split(":")[2]);
                        LocalTime endTimeDur = LocalTime.of(endTimeHour, endTimeMinute, endTimeSeconds);
                        int startTimeHour = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[0]);
                        int startTimeMinute = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[1]);
                        int startTimeSeconds = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[2]);
                        LocalTime startTimeDur = LocalTime.of(startTimeHour, startTimeMinute, startTimeSeconds);
                        long minutes = ChronoUnit.MINUTES.between(startTimeDur, endTimeDur);
                        String durationRide = String.valueOf(minutes + " Min");
                        duration.setText(durationRide);
                    }



                    endTime.setText(endTimeText);
                    String cost = String.valueOf(ride.getTotalCost() + " RSD");
                    price.setText(cost);


                    startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
                    endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
                }
            }
            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                call.cancel();
            }
        });
    }
}