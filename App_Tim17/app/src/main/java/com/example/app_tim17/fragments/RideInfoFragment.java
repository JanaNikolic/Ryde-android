package com.example.app_tim17.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.ride.PassengerRideResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RideInfoFragment extends Fragment {

    private Ride ride;
    private PassengerRideResponse passengerRideResponse;
    private RetrofitService retrofitService;
    private PassengerService passengerService;
    private TokenUtils tokenUtils;

    public RideInfoFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ride = Utils.getGsonParser().fromJson(getArguments().getString("ride"), Ride.class);
            passengerRideResponse = ride.getPassengers().get(0);
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
        //TODO Distance

        date.setText(ride.getStartTime().split("T")[0]);
        startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
        endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
        startTime.setText(ride.getStartTime().split("T")[1].substring(0, 5));
        endTime.setText(ride.getEndTime().split("T")[1].substring(0, 5));
        price.setText(String.format("%d RSD", ride.getTotalCost()));

        Call<PassengerResponse> call = passengerService.getPassenger(passengerRideResponse.getId(), "Bearer " + getCurrentToken());

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                PassengerResponse passenger = response.body();

                if (passenger != null) {
                    LinearLayout reviewCardsLayout = view.findViewById(R.id.review_card);

                    CardView reviewCard = (CardView) inflater.inflate(R.layout.review_card, container, false);


                    TextView passInfo = reviewCard.findViewById(R.id.review_passenger);
                    passInfo.setText(String.format("%s %s", passenger.getName(), passenger.getSurname()));
                    passInfo = reviewCard.findViewById(R.id.review_content);
                    passInfo.setText("Voznja je bila prijatna."); // TODO get review
                    reviewCardsLayout.addView(reviewCard);

                    reviewCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                            //TODO add passenger_info fragment
                            transaction.add(R.id.ride_info_fragment, new UserInfoFragment());
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