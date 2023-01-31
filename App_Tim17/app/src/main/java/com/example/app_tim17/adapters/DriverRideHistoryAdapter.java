package com.example.app_tim17.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.RideInfoFragment;
import com.example.app_tim17.fragments.driver.ChatDriverFragment;
import com.example.app_tim17.model.response.review.DriverReview;
import com.example.app_tim17.model.response.review.RideReviewsResponse;
import com.example.app_tim17.model.response.review.VehicleReview;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.ReviewService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRideHistoryAdapter extends RecyclerView.Adapter<DriverRideHistoryAdapter.ViewHolder> {
    private List<Ride> rides;
    private Context context;
    private Ride ride;
    private RetrofitService retrofitService;
    private TokenUtils tokenUtils;
    private ReviewService reviewService;
    private RideReviewsResponse reviews;
    private float rating;


    public DriverRideHistoryAdapter(Context context, List<Ride> rides) {
        super();
        this.context = context;
        this.rides = rides;
        this.retrofitService = new RetrofitService();
        this.tokenUtils = new TokenUtils();
        this.reviewService = retrofitService.getRetrofit().create(ReviewService.class);
    }
    @NonNull
    @Override
    public DriverRideHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_ride_history_card_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d(TAG, "position = " + holder.getBindingAdapterPosition());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                RideInfoFragment fragment = new RideInfoFragment();
                Log.i("click RIDE ID", String.valueOf(holder.ride.getId()));

                Bundle args = new Bundle();
                args.putString("ride", Utils.getGsonParser().toJson(holder.ride));
                if (holder.rideReview != null) {
                    args.putString("reviews", Utils.getGsonParser().toJson(holder.rideReview));
                }
                fragment.setArguments(args);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();
            }
        });

        holder.messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                Bundle arg = new Bundle();


                arg.putLong("userId", ride.getPassengers().get(0).getId());
                arg.putString("userName", ride.getPassengers().get(0).getEmail());
                arg.putString("type", "RIDE");

                ChatDriverFragment chatFragment = new ChatDriverFragment();
                chatFragment.setArguments(arg);

                transaction.add(R.id.fragment_container, chatFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ride = rides.get(position);
        holder.ride = ride;
        holder.dateTextView.setText(ride.getStartTime().split("T")[0]);
        holder.startTimeTextView.setText(ride.getStartTime().split("T")[1].substring(0, 5));
        holder.endTimeTextView.setText(ride.getEndTime().split("T")[1].substring(0, 5));
        holder.durationTextView.setText(ride.getEstimatedTimeInMinutes() + " min");
        holder.priceTextView.setText(ride.getTotalCost() + " RSD");
//                    holder.numOfPassengersTextView.setText();
        holder.startAddressTextView.setText(ride.getLocations().get(0).getDeparture().getAddress());
        holder.endAddressTextView.setText(ride.getLocations().get(0).getDestination().getAddress());
        holder.roadLengthTextView.setText(ride.getDistance()/1000 + " km");
        Log.i("RIDE ID", String.valueOf(ride.getId()));

        Call<RideReviewsResponse> call = reviewService.getRideReviews("Bearer " + getCurrentToken(), ride.getId());

        call.enqueue(new Callback<RideReviewsResponse>() {
            @Override
            public void onResponse(Call<RideReviewsResponse> call, Response<RideReviewsResponse> response) {
                RideReviewsResponse rideReviews = response.body();

                if (response.isSuccessful() && rideReviews != null) {
                    holder.rideReview = rideReviews;
                    float sum = 0;
                    int br = 0;
                    for (DriverReview driverReview: rideReviews.getDriverReview()) {
                        sum += driverReview.getRating();
                        ++br;
                    }
                    for (VehicleReview vehicleReview: rideReviews.getVehicleReview()) {
                        sum += vehicleReview.getRating();
                        ++br;
                    }
                    if (br == 0) {
                        rating = 0;
                        reviews = null;
                    } else {
                        rating = sum/br;
                        reviews = rideReviews;
                    }

                    holder.reviews.setRating(rating);

                }
            }

            @Override
            public void onFailure(Call<RideReviewsResponse> call, Throwable t) {

            }
        });
    }

    private String getCurrentToken() {
        SharedPreferences sp = context.getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return rides.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView startTimeTextView;
        private final TextView endTimeTextView;
        private final TextView durationTextView;
        private final TextView priceTextView;
        private final TextView numOfPassengersTextView;
        private final TextView startAddressTextView;
        private final TextView endAddressTextView;
        private final TextView roadLengthTextView;
        private final RatingBar reviews;
        private Ride ride;
        private RideReviewsResponse rideReview;
        private final ImageView messages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            startTimeTextView = itemView.findViewById(R.id.time_start);
            endTimeTextView = itemView.findViewById(R.id.time_end);
            durationTextView = itemView.findViewById(R.id.duration);
            priceTextView = itemView.findViewById(R.id.price);
            numOfPassengersTextView = itemView.findViewById(R.id.num_of_passengers);
            startAddressTextView = itemView.findViewById(R.id.start_address);
            endAddressTextView = itemView.findViewById(R.id.end_address);
            roadLengthTextView = itemView.findViewById(R.id.road_length);
            reviews = itemView.findViewById(R.id.stars);
            ride = null;
            rideReview = null;
            messages = itemView.findViewById(R.id.message);
        }

    }

}
