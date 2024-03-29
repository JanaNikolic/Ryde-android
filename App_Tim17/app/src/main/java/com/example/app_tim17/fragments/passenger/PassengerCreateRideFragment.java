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
import com.example.app_tim17.fragments.DrawRouteFragment;
import com.example.app_tim17.activities.PassengerActivity;
import com.example.app_tim17.model.request.PassengerRequest;
import com.example.app_tim17.model.request.RideRequest;
import com.example.app_tim17.model.response.Location;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.ride.FavoriteRoute;
import com.example.app_tim17.model.response.ride.FavoriteRouteResponse;
import com.example.app_tim17.model.response.ride.LocationForRide;
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.model.response.ride.RideResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.tools.FragmentTransition;
import com.shuhart.stepview.StepView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private LocalDate date;
    private LocalTime time;
    private View dateTimePicker;
    private ImageView standradCar, luxCar, van;
    int mediumAnimationDuration;
    private StepView stepView;
    FavoriteRouteResponse favoritesResponse;
    private View locations, preferences, dateTime;
    AutoCompleteTextView departure, destination;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String localDateTime = "%sT%sZ", localTime, localDate;
    DateTimeFormatter requestFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private RideRequest rideRequest = new RideRequest();
    private List<String> departures = new ArrayList<>(), destinations = new ArrayList<>();
    int position = 0;
    private String[] streets, streets2;
    private RideService rideService;
    private RetrofitService retrofitService;

    public PassengerCreateRideFragment() {}

    public static PassengerCreateRideFragment newInstance(String param1, String param2) {
        PassengerCreateRideFragment fragment = new PassengerCreateRideFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tokenUtils = new TokenUtils();
        View v = inflater.inflate(R.layout.fragment_passenger_create_ride, container, false);
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);

        departure = (AutoCompleteTextView) v.findViewById(R.id.from);
        destination = (AutoCompleteTextView) v.findViewById(R.id.to);
        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departure.showDropDown();
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destination.showDropDown();
            }
        });
        Call<FavoriteRouteResponse> favorites = rideService.getFavoriteRoutes("Bearer " + getCurrentToken());
        favorites.enqueue(new Callback<FavoriteRouteResponse>() {
            @Override
            public void onResponse(Call<FavoriteRouteResponse> call, Response<FavoriteRouteResponse> response) {
                favoritesResponse = response.body();
                if (favoritesResponse != null) {
                    for (FavoriteRoute route : favoritesResponse.getResults()) {
                        departures.add(route.getLocations().get(0).getDeparture().getAddress());
                        destinations.add(route.getLocations().get(0).getDestination().getAddress());
                    }
                    streets = departures.toArray(new String[0]);
                    streets2 = destinations.toArray(new String[0]);

                    if (getActivity() != null) {
                        destination.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, streets2));
                        departure.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, streets));
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteRouteResponse> call, Throwable t) {

            }
        });


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
                            LocalDate date = LocalDate.of(year, monthOfYear+1, dayOfMonth);
                            localDate = date.format(requestFormat);
                            txtDate.setText(localDate);

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
                            LocalTime time = LocalTime.of(hourOfDay, minute);
                            localTime = time.format(timeFormat);
                            txtTime.setText(localTime);
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

                        Bundle route = new Bundle();

                        route.putString("fromAddress", departure.getEditableText().toString());
                        route.putString("toAddress", destination.getEditableText().toString());

                        DrawRouteFragment draw = DrawRouteFragment.newInstance();
                        draw.setArguments(route);
                        FragmentTransition.to(draw, getActivity(), false);

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
                    rideRequest.setPassengers(new ArrayList<>());

                    Log.d("ride", rideRequest.toString());
                    stepView.done(true);
                    stepView.go(2, true);

                    if (futureOrder.isChecked()) {
                        localDateTime = String.format(localDateTime, localDate, localTime);
                        rideRequest.setScheduledTime(localDateTime);
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
        String token = "Bearer " + getCurrentToken();
        rideRequest.setScheduledTime(localDateTime);
        Call<Ride> call = rideService.createRide(token, rideRequest);

        call.enqueue(new Callback<Ride>() {
            @Override
            public void onResponse(Call<Ride> call, Response<Ride> response) {
                Ride ride = response.body();
                if (ride != null) {
                    if (ride.getStatus().equals("SCHEDULED")) {
                        if (getActivity() != null) {
                            PassengerActivity activity = (PassengerActivity) getActivity();
                            activity.scheduledRide(ride.getId());

                            Toast.makeText(getContext(), "Ride successfully scheduled!", Toast.LENGTH_SHORT).show();

                            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.currentRide, new PassengerCreateRideFragment());
                            fragmentTransaction.commit();
                        }
                    } else {
                        Log.d("response", ride.toString());
                        SuccesfullSearchFragment succesfullSearchFragment = new SuccesfullSearchFragment();
                        Bundle args = new Bundle();
                        Bundle route = new Bundle();

                        route.putString("fromAddress", ride.getLocations().get(0).getDeparture().getAddress());
                        route.putString("toAddress", ride.getLocations().get(0).getDestination().getAddress());

                        args.putBundle("route", route);

                        List<LocationForRide> locs = ride.getLocations();
                        for (LocationForRide l : locs) {
                            args.putString("startAddress", l.getDeparture().getAddress());
                            args.putString("endAddress", l.getDestination().getAddress());
                        }

                        args.putLong("driverId", ride.getDriver().getId());
                        args.putString("time", ride.getEstimatedTimeInMinutes().toString());
                        args.putLong("rideId", ride.getId());

                        args.putString("price", ride.getTotalCost().toString() + " RSD");
                        args.putString("timeStart", ride.getStartTime());

                        succesfullSearchFragment.setArguments(args);

                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, succesfullSearchFragment);
                        fragmentTransaction.commit();
                    }
                }
            }
            @Override
            public void onFailure(Call<Ride> call, Throwable t) {
                Log.d("Oops", "Something went wrong!");
                Toast.makeText(getActivity(), "There are currently no active drivers! Try again later.", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }
}