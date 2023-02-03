package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.ReviewRequest;
import com.example.app_tim17.model.response.review.DriverReview;
import com.example.app_tim17.model.response.review.Review;
import com.example.app_tim17.model.response.review.ReviewRideResponse;
import com.example.app_tim17.model.response.review.VehicleReview;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.ReviewService;
import com.example.app_tim17.service.TokenUtils;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFromHistory extends Fragment {
    ReviewService reviewService;

    public static ReviewFromHistory newInstance() {
        return new ReviewFromHistory();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Long id = getArguments().getLong("ride_id");


        View view = inflater.inflate(R.layout.fragment_review_from_history, container, false);

        RetrofitService retrofitService = new RetrofitService();
        reviewService = retrofitService.getRetrofit().create(ReviewService.class);

        RatingBar driverRatingBar = (RatingBar) view.findViewById(R.id.ratingBarDriver);
        RatingBar vehicleRatingBar = (RatingBar) view.findViewById(R.id.ratingBarVehicle);

        EditText driverFeedback = (EditText) view.findViewById(R.id.driverFeedback);
        EditText vehicleFeedback = (EditText) view.findViewById(R.id.vehicleFeedback);
        Button sendFeedback = (Button) view.findViewById(R.id.submitBtn_history);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.remove(ReviewFromHistory.this);
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
                    System.out.println();

                    SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                    String token = sp.getString("token", "");

                    ReviewRequest driverReview = new ReviewRequest();
                    driverReview.setRating((int) driverRatingBar.getRating());
                    driverReview.setComment(String.valueOf(driverFeedback.getText()));
                    Call<DriverReview> call = reviewService.postDriverReview("Bearer " + token, id, driverReview);
                    call.enqueue(new Callback<DriverReview>() {
                        @Override
                        public void onResponse(Call<DriverReview> call, Response<DriverReview> response) {

                        }
                        @Override
                        public void onFailure(Call<DriverReview> call, Throwable t) {
                            call.cancel();
                        }
                    });

                    ReviewRequest vehicleReview = new ReviewRequest();
                    vehicleReview.setRating((int) vehicleRatingBar.getRating());
                    vehicleReview.setComment(String.valueOf(vehicleFeedback.getText()));
                    Call<VehicleReview> call2 = reviewService.postVehicleReview("Bearer " + token, id, vehicleReview);
                    call2.enqueue(new Callback<VehicleReview>() {
                        @Override
                        public void onResponse(Call<VehicleReview> call, Response<VehicleReview> response) {

                        }
                        @Override
                        public void onFailure(Call<VehicleReview> call, Throwable t) {
                            call.cancel();
                        }
                    });

                    Toast.makeText(getActivity(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.remove(ReviewFromHistory.this);
                    transaction.commit();
                }
            }
        });
        return view;
    }
}
