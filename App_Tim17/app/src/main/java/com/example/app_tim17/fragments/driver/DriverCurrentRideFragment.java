package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.fragments.passenger.PassengerCreateRideFragment;
import com.example.app_tim17.fragments.passenger.ReviewDriverAndVehicleFragment;
import com.example.app_tim17.model.request.PanicRequest;
import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.tools.FragmentTransition;
import com.example.app_tim17.tools.Utils;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverCurrentRideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverCurrentRideFragment extends Fragment {
    TextView timer;
    CountDownTimer countDownTimer;
    int time;
    private RideService rideService;
    private RetrofitService retrofitService;
    private PassengerService passengerService;
    private PassengerResponse passenger;
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private Long rideId;

    public DriverCurrentRideFragment() {
        // Required empty public constructor
    }

    public static DriverCurrentRideFragment newInstance(String param1, String param2) {
        DriverCurrentRideFragment fragment = new DriverCurrentRideFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_current_ride, container, false);
//        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "ws://192.168.43.198:8080/example-endpoint/websocket");
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);


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
        rideId = ride.getId();

        Call<PassengerResponse> call = passengerService.getPassenger("Bearer " + getCurrentToken(), ride.getPassengers().get(0).getId());

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                if (response.body() != null) {
                    passenger = response.body();
                    if (passenger.getProfilePicture() != null) {
                        Bitmap photo = Utils.StringToBitMap(passenger.getProfilePicture());
                        passPicture.setImageBitmap(photo);
                    }
                    passName.setText(passenger.getName() + " " + passenger.getSurname());
                }
            }

            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + passenger.getTelephoneNumber()));
                startActivity(intent);
            }
        });

        if (ride != null) {
            startTime.setText(ride.getStartTime().split("T")[1].split("\\.")[0]);
            startAddress.setText(ride.getLocations().get(0).getDeparture().getAddress());
            endAddress.setText(ride.getLocations().get(0).getDestination().getAddress());
            String text = ride.getTotalCost() + " RSD";
            price.setText(text);
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


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle arg = new Bundle();


                arg.putLong("userId", ride.getPassengers().get(0).getId());
                arg.putString("userName", ride.getPassengers().get(0).getEmail());
                arg.putString("type", "RIDE");
                arg.putLong("ride", ride.getId());

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