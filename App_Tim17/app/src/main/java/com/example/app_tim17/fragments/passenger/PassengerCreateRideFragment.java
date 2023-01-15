package com.example.app_tim17.fragments.passenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.PassengerRequest;
import com.example.app_tim17.model.request.RideRequest;
import com.example.app_tim17.model.response.Location;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.ride.LocationForRide;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerCreateRideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerCreateRideFragment extends Fragment implements View.OnClickListener {
    TokenUtils tokenUtils;
    private Button btnDatePicker, btnTimePicker, buttonNext;
    private EditText txtDate, txtTime;
    private Boolean selectedType = false, babyTransport = false, petTransport = false;
    private CheckBox futureOrder, babyTransportCheck, petTransportCheck;
    private List<LocationForRide> locationForRidelist = new ArrayList<LocationForRide>();
    private List<PassengerRequest> passengers = new ArrayList<PassengerRequest>();
    private LocationForRide locationsForRide = new LocationForRide();
    private Location location;
    private View dateTimePicker;
    private ImageView standradCar, luxCar, van;
    int mediumAnimationDuration;
    private StepView stepView;
    private View locations, preferences, dateTime;
    AutoCompleteTextView departure, destination;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String localDateTime = "%sT%s.000Z";
    private String localTime, localDate;
    private RideRequest rideRequest = new RideRequest();
    int position = 0;

    private RideService rideService;
    private RetrofitService retrofitService;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PassengerCreateRideFragment() {}

    public static PassengerCreateRideFragment newInstance(String param1, String param2) {
        PassengerCreateRideFragment fragment = new PassengerCreateRideFragment();
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
        tokenUtils = new TokenUtils();
        View v = inflater.inflate(R.layout.fragment_passenger_create_ride, container, false);
        departure = (AutoCompleteTextView) v.findViewById(R.id.from);
        destination = (AutoCompleteTextView) v.findViewById(R.id.to);
        String[] streets1 = getResources().getStringArray(R.array.streets);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, streets1);
        departure.setAdapter(adapter);
        destination.setAdapter(adapter);

        stepView = v.findViewById(R.id.step_view);
        stepView.done(false);

        locations = v.findViewById(R.id.locations);
        preferences = v.findViewById(R.id.preferences);
        dateTime = v.findViewById(R.id.time);

        mediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);

        buttonNext = v.findViewById(R.id.btnSearch);
        buttonNext.setOnClickListener(this);
        standradCar = v.findViewById(R.id.standard_car);
        standradCar.setOnClickListener(this);
        luxCar = v.findViewById(R.id.lux_car);
        luxCar.setOnClickListener(this);
        van = v.findViewById(R.id.van);
        van.setOnClickListener(this);


        futureOrder = v.findViewById(R.id.checkOrderFuture);
        babyTransportCheck = v.findViewById(R.id.baby_transport);
        petTransportCheck = v.findViewById(R.id.pet_transport);
        dateTimePicker = v.findViewById(R.id.date_time_picker);

        futureOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    dateTimePicker.setVisibility(View.VISIBLE);
                }
                else {
                    dateTimePicker.setVisibility(View.GONE);
                    rideRequest.setScheduledTime(null);
                }
            }
        });

        babyTransportCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) babyTransport = true;
                else babyTransport = false;
            }
        });

        petTransportCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) petTransport = true;
                else petTransport = false;
            }
        });

        btnDatePicker=(Button)v.findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker=(Button)v.findViewById(R.id.btn_time);
        btnTimePicker.setOnClickListener(this);
        txtDate=(EditText)v.findViewById(R.id.in_date);
        txtTime=(EditText)v.findViewById(R.id.in_time);

        return v;
    }


    @Override
    public void onClick(View view) {
        if (view == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            localDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if (view == btnTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSecond = c.get(Calendar.SECOND);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            localTime = hourOfDay + ":" + minute + ":" + "00";
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute,true);
            timePickerDialog.show();

        }
        else if (view == standradCar) {
            selectedType = true;
            rideRequest.setVehicleType("STANDARD");
            standradCar.setImageResource(R.drawable.standard_car_selected);
            luxCar.setImageResource(R.drawable.electric_car_not_selected);
            van.setImageResource(R.drawable.van_not_selected);
        }
        else if (view == luxCar) {
            selectedType = true;
            rideRequest.setVehicleType("LUXURY");
            standradCar.setImageResource(R.drawable.standard_car_not_selected);
            luxCar.setImageResource(R.drawable.electric_car_selected);
            van.setImageResource(R.drawable.van_not_selected);
        }
        else if (view == van) {
            selectedType = true;
            rideRequest.setVehicleType("VAN");
            standradCar.setImageResource(R.drawable.standard_car_not_selected);
            luxCar.setImageResource(R.drawable.electric_car_not_selected);
            van.setImageResource(R.drawable.van_selected);
        }
        else if ((Button) view == buttonNext) {
            switch (position) {
                case 0: {

                    if (departure.getEditableText().toString().equals("") || destination.getEditableText().toString().equals("")) {
                        Toast.makeText(getContext(),"Fields cannot be empty!",Toast.LENGTH_LONG).show();
                    } else {
                        location = new Location();
                        location.setAddress(departure.getEditableText().toString());
                        location.setLatitude(46.0);
                        location.setLongitude(46.0);
                        locationsForRide.setDeparture(location);
                        location = new Location();
                        location.setAddress(destination.getEditableText().toString());
                        location.setLatitude(46.0);
                        location.setLongitude(46.0);
                        locationsForRide.setDestination(location);
                        locationForRidelist.add(locationsForRide);
                        rideRequest.setLocations(locationForRidelist);

                        position = 1;
                        stepView.done(false);
                        buttonNext.setText("Next");
                        locations.animate()
                            .alpha(0f)
                            .setDuration(mediumAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    locations.setVisibility(View.GONE);
                                    preferences.setAlpha(0f);
                                    preferences.setVisibility(View.VISIBLE);
                                    preferences.animate()
                                            .alpha(1f)
                                            .setDuration(mediumAnimationDuration)
                                            .setListener(null);
                                    stepView.go(position, true);
                                }
                            });
                    }
                    break;
                }
                case 1: {
                    if (!selectedType) {
                        Toast.makeText(getContext(),"Must select a car type!",Toast.LENGTH_LONG).show();
                    } else {
                        position = 2;
                        stepView.done(false);

                        rideRequest.setBabyTransport(babyTransport);
                        rideRequest.setPetTransport(petTransport);

                        preferences.animate()
                                .alpha(0f)
                                .setDuration(mediumAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        preferences.setVisibility(View.GONE);
                                        dateTime.setAlpha(0f);
                                        dateTime.setVisibility(View.VISIBLE);
                                        dateTime.animate()
                                                .alpha(1f)
                                                .setDuration(mediumAnimationDuration)
                                                .setListener(null);
                                        buttonNext.setText("ORDER");
                                        stepView.go(position, true);
                                    }
                                });
                    }
                    break;
                }
                default: {
                    String token = getCurrentToken();
                    String email = tokenUtils.getEmail(token);
                    Long id = tokenUtils.getId(token);

                    passengers.add(new PassengerRequest(id, email));
                    rideRequest.setPassengers(passengers);

                    Log.d("ride", rideRequest.toString());
                    position = 0;
                    stepView.done(true);
                    stepView.go(2, true);

                    if (futureOrder.isChecked()) {
                        localDateTime = String.format(localDateTime, localDate, localTime);
                        rideRequest.setScheduledTime(localDateTime);
                        Toast.makeText(getContext(),localDateTime,Toast.LENGTH_LONG).show();
                    } else {
                        rideRequest.setScheduledTime(null);
                    }
                    sendRequest(rideRequest);

                    break;
                }
            }
        }
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    private void sendRequest(RideRequest rideRequest) {
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        String token = "Bearer " + getCurrentToken();

        Call<Ride> call = rideService.createRide(token, rideRequest);

        call.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                Ride ride = response.body();
                if (ride != null) {
                    Log.d("response", ride.toString());
                    SuccesfullSearchFragment succesfullSearchFragment = new SuccesfullSearchFragment();
                    Bundle args = new Bundle();

                    List<LocationForRide> locs = ride.getLocations();
                    for (LocationForRide l: locs ) {
                        args.putString("startAddress", l.getDeparture().getAddress());
                        args.putString("endAddress", l.getDestination().getAddress());
                    }

                    args.putString("driverId", ride.getDriver().getId().toString());
                    args.putString("time", ride.getEstimatedTimeInMinutes().toString());


                    args.putString("price", ride.getTotalCost().toString() + " RSD");
                    args.putString("timeStart", ride.getStartTime());

                    succesfullSearchFragment.setArguments(args);

                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.currentRide, succesfullSearchFragment);
                    fragmentTransaction.commit();
                }
            }
            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                Log.d("Opps", "1Something went wrong!");
//                onResume(); //TODO
                call.cancel();
            }
        });

    }
}