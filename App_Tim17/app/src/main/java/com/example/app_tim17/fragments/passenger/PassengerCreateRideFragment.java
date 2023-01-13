package com.example.app_tim17.fragments.passenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.app_tim17.R;
import com.shuhart.stepview.StepView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerCreateRideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerCreateRideFragment extends Fragment {
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int position = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerCreateRideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerCreateRideFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        View v = inflater.inflate(R.layout.fragment_passenger_create_ride, container, false);
        AutoCompleteTextView textView1 = (AutoCompleteTextView) v.findViewById(R.id.from);
        AutoCompleteTextView textView2 = (AutoCompleteTextView) v.findViewById(R.id.to);
        String[] streets1 = getResources().getStringArray(R.array.streets);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, streets1);
        textView1.setAdapter(adapter);
        textView2.setAdapter(adapter);

        StepView stepView = v.findViewById(R.id.step_view);
        stepView.done(false);
        View locations = v.findViewById(R.id.locations);
        View preferences = v.findViewById(R.id.preferences);
        View dateTime = v.findViewById(R.id.time);

        int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        Button button = v.findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0: {
                        position = 1;
                        stepView.done(false);
                        button.setText("Next");

                        locations.animate()
                                .alpha(0f)
                                .setDuration(shortAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        locations.setVisibility(View.GONE);
                                        preferences.setAlpha(0f);
                                        preferences.setVisibility(View.VISIBLE);
                                        preferences.animate()
                                                .alpha(1f)
                                                .setDuration(shortAnimationDuration)
                                                .setListener(null);
                                    }
                                });

                        stepView.go(position, true);
                        break;
                    }
                    case 1: {
                        //
                        position = 2;
                        stepView.done(false);
                        button.setText("ORDER");

                        preferences.animate()
                                .alpha(0f)
                                .setDuration(shortAnimationDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        preferences.setVisibility(View.GONE);
                                        dateTime.setAlpha(0f);
                                        dateTime.setVisibility(View.VISIBLE);
                                        dateTime.animate()
                                                .alpha(1f)
                                                .setDuration(shortAnimationDuration)
                                                .setListener(null);
                                    }
                                });

                        stepView.go(position, true);
                        break;
                    }
                    default: {
                        position = 0;
                        stepView.done(true);
                        stepView.go(2, true);

                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentRide, new SuccesfullSearchFragment());
                        fragmentTransaction.commit();
                        break;
                    }
                }
            }
        });

        ImageView standradCar = v.findViewById(R.id.standard_car);
        ImageView luxCar = v.findViewById(R.id.lux_car);
        ImageView van = v.findViewById(R.id.van);

        standradCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                standradCar.setImageResource(R.drawable.standard_car_selected);
                luxCar.setImageResource(R.drawable.electric_car_not_selected);
                van.setImageResource(R.drawable.van_not_selected);
            }
        });

        luxCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                standradCar.setImageResource(R.drawable.standard_car_not_selected);
                luxCar.setImageResource(R.drawable.electric_car_selected);
                van.setImageResource(R.drawable.van_not_selected);
            }
        });

        van.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                standradCar.setImageResource(R.drawable.standard_car_not_selected);
                luxCar.setImageResource(R.drawable.electric_car_not_selected);
                van.setImageResource(R.drawable.van_selected);
            }
        });

        CheckBox futureOrder = v.findViewById(R.id.checkOrderFuture);
        View dateTimePicker = v.findViewById(R.id.date_time_picker);
        futureOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) dateTimePicker.setVisibility(View.VISIBLE);
                else dateTimePicker.setVisibility(View.GONE);
            }
        });

        btnDatePicker=(Button)v.findViewById(R.id.btn_date);
        btnTimePicker=(Button)v.findViewById(R.id.btn_time);
        txtDate=(EditText)v.findViewById(R.id.in_date);
        txtTime=(EditText)v.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        return v;
    }


}