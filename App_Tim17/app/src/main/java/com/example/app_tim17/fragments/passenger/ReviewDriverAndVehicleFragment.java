package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.ReviewRequest;
import com.example.app_tim17.model.response.review.DriverReview;
import com.example.app_tim17.model.response.review.VehicleReview;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.ReviewService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDriverAndVehicleFragment extends Fragment {

    private RetrofitService retrofitService;
    private ReviewService reviewService;
    private Long rideId;

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

        retrofitService = new RetrofitService();
        reviewService = retrofitService.getRetrofit().create(ReviewService.class);

        if (getArguments() != null && getArguments().containsKey("rideId")) {
            rideId = getArguments().getLong("rideId");
        } else {
            Toast.makeText(getActivity(), "Oops, something went wrong, try again later!", Toast.LENGTH_LONG).show();
            return view;
        }


        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.remove(ReviewDriverAndVehicleFragment.this);
                transaction.commit();

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
                    ReviewRequest driverReview = new ReviewRequest();
                    driverReview.setRating(driverRatingBar.getRating());
                    driverReview.setComment(driverFeedback.getText().toString());

                    ReviewRequest vehicleReview = new ReviewRequest();
                    vehicleReview.setRating(vehicleRatingBar.getRating());
                    vehicleReview.setComment(vehicleFeedback.getText().toString());


                    driverFeedback.setText("");
                    vehicleFeedback.setText("");
                    driverRatingBar.setRating(0);
                    vehicleRatingBar.setRating(0);
                    Toast.makeText(getActivity(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                    Call<DriverReview> call = reviewService.postDriverReview("Bearer " + getCurrentToken(), rideId, driverReview);

                    Call<VehicleReview> call2 = reviewService.postVehicleReview("Bearer " + getCurrentToken(), rideId, vehicleReview);

                    call.enqueue(new Callback<DriverReview>() {
                        @Override
                        public void onResponse(Call<DriverReview> call, Response<DriverReview> response) {

                        }

                        @Override
                        public void onFailure(Call<DriverReview> call, Throwable t) {

                        }
                    });

                    call2.enqueue(new Callback<VehicleReview>() {
                        @Override
                        public void onResponse(Call<VehicleReview> call, Response<VehicleReview> response) {

                        }

                        @Override
                        public void onFailure(Call<VehicleReview> call, Throwable t) {

                        }
                    });

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.remove(ReviewDriverAndVehicleFragment.this);
                    transaction.commit();
                }
            }
        });
        return view;
    }
    private String getCurrentToken() {
        SharedPreferences sp = getContext().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}