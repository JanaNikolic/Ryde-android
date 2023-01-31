package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.fragments.passenger.ChatFragment;
import com.example.app_tim17.fragments.passenger.PassengerCreateRideFragment;
import com.example.app_tim17.fragments.passenger.ReviewDriverAndVehicleFragment;
import com.example.app_tim17.model.request.PanicRequest;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.tools.FragmentTransition;
import com.example.app_tim17.tools.Utils;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverCurrentRideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverCurrentRideFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView timer;
    CountDownTimer countDownTimer;
    int time;
    private RideService rideService;
    private RetrofitService retrofitService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverCurrentRideFragment() {
        // Required empty public constructor
    }

    public static DriverCurrentRideFragment newInstance(String param1, String param2) {
        DriverCurrentRideFragment fragment = new DriverCurrentRideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_current_ride, container, false);
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);

        TextView startAddress = (TextView) view.findViewById(R.id.start_address);
        TextView endAddress = (TextView) view.findViewById(R.id.end_address);
        TextView startTime = (TextView) view.findViewById(R.id.start_time);
        TextView price = (TextView) view.findViewById(R.id.ride_price);
        TextView passName = (TextView) view.findViewById(R.id.pass_name);
        ShapeableImageView passPicture = (ShapeableImageView) view.findViewById(R.id.profile_picture);

        Button phone = (Button) view.findViewById(R.id.call_pass);
        Button message = (Button) view.findViewById(R.id.message_pass);
        Button end = (Button) view.findViewById(R.id.end_btn);
        Button panic = (Button) view.findViewById(R.id.panic_btn);
        Bundle args = getArguments();

        Ride ride = Utils.getGsonParser().fromJson(args.getString("ride"), Ride.class);

        if (ride != null) {
            startTime.setText(ride.getStartTime().split("T")[1].split("\\.")[0]);
            startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
            endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
            String text = ride.getTotalCost() + " RSD";
            price.setText(text);
            passName.setText(ride.getPassengers().get(0).getEmail());
            time = ride.getEstimatedTimeInMinutes() * 1000 * 60;
        }

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = "Bearer " + getCurrentToken();
                Call<Ride> call = rideService.endRide(token, ride.getId());

                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        DrawRouteFragment draw = DrawRouteFragment.newInstance();
                        FragmentTransition.to(draw, getActivity(), false);
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, new NoActiveRideFragment());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        call.cancel();
                    }
                });

            }
        });

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = "Bearer " + getCurrentToken();
                Call<Ride> call = rideService.panic(token, ride.getId(), new PanicRequest());

                call.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        DrawRouteFragment draw = DrawRouteFragment.newInstance();
                        FragmentTransition.to(draw, getActivity(), false);
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, new NoActiveRideFragment());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO get passenger phone number
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:" + number));
//                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle arg = new Bundle();


                arg.putLong("userId", ride.getPassengers().get(0).getId());
                arg.putString("userName", ride.getPassengers().get(0).getEmail());

                ChatDriverFragment chatFragment = new ChatDriverFragment();
                chatFragment.setArguments(arg);

                transaction.add(R.id.currentRide, chatFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        timer = (TextView) view.findViewById(R.id.ride_timer);

        countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(getDateFromMillis(time - millisUntilFinished));
            }

            public void onFinish() {
                timer.setText("FINISHED");
                //TODO finished ride fragment

            }
        };

        countDownTimer.start();

        return view;
    }

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}