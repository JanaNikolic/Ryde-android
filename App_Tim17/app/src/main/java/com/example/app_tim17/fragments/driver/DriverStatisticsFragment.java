package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverStatisticsFragment extends Fragment {
    String startDate, endDate;
    RetrofitService retrofitService;
    DriverService driverService;
    PassengerService passengerService;
    TokenUtils tokenUtils;
    GraphView graphEarnings, graphDistance, graphRides;
    SimpleDateFormat outputDateFormat, requestFormat;
    TextView totalRides, avgRides, totalEarned, avgEarned, totalDistance, avgDistance;
    Call<RideStatisticsResponse> rideCount;
    Call<MoneyStatisticsResponse> moneyCount;
    Call<DistanceStatisticsResponse> distanceCount;
    TextView titleErnings;
    TextView avgEarntxt;
    TextView sumEarnTxt;


    public DriverStatisticsFragment() {
        // Required empty public constructor
    }

    public static DriverStatisticsFragment newInstance(String param1, String param2) {
        DriverStatisticsFragment fragment = new DriverStatisticsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);
        requestFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        tokenUtils = new TokenUtils();
        outputDateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_statistics, container, false);
        Button button = (Button) view.findViewById(R.id.rangeBtn);
        titleErnings = (TextView) view.findViewById(R.id.title_ernings);
        avgEarntxt = (TextView) view.findViewById(R.id.avgEarntxt);
        sumEarnTxt = (TextView) view.findViewById(R.id.sumEarnTxt);

        initializeGraphs(view);

        totalRides = view.findViewById(R.id.sumRide);
        avgRides = view.findViewById(R.id.AvgRide);
        totalEarned = view.findViewById(R.id.sumEarn);
        avgEarned = view.findViewById(R.id.AvgEarn);
        totalDistance = view.findViewById(R.id.sumDistance);
        avgDistance = view.findViewById(R.id.AvgDistance);


        MaterialDatePicker dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select dates")
                        .setSelection(
                                new Pair(
                                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                                        MaterialDatePicker.todayInUtcMilliseconds()
                                )
                        )
                        .build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateRangePicker.show(getParentFragmentManager(), "DateRangePicker");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        startDate = requestFormat.format(selection.first);
                        endDate = requestFormat.format(selection.second);
                        getStatistics(startDate, endDate);
                    }
                });
                }
        });
        return view;
    }

    private void getStatistics(String startDate, String endDate) {
        if (tokenUtils.getRole(getCurrentToken()).equals("ROLE_PASSENGER")) {
            rideCount = passengerService.getRideCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
            moneyCount = passengerService.getMoneyCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
            distanceCount = passengerService.getDistanceCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
            titleErnings.setText("Spendings");
            avgEarntxt.setText("Avg spendings");
            sumEarnTxt.setText("Total spendings");

        } else {
            rideCount = driverService.getRideCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
            moneyCount = driverService.getMoneyCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
            distanceCount = driverService.getDistanceCount(TokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken(), startDate, endDate);
        }
        rideCount.enqueue(new Callback<RideStatisticsResponse>() {
            @Override
            public void onResponse(Call<RideStatisticsResponse> call, Response<RideStatisticsResponse> response) {
                RideStatisticsResponse stats = response.body();

                if (stats != null) {
                    try {
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPoint(stats.getCountsByDay()));
                        graphRides.addSeries(series);
                        totalRides.setText(stats.getTotalCount().toString());
                        avgRides.setText(Float.toString(stats.getAverageCount()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RideStatisticsResponse> call, Throwable t) {
                rideCount.cancel();
            }
        });

        distanceCount.enqueue(new Callback<DistanceStatisticsResponse>() {
            @Override
            public void onResponse(Call<DistanceStatisticsResponse> call, Response<DistanceStatisticsResponse> response) {
                DistanceStatisticsResponse stats = response.body();

                if (stats != null) {
                    try {
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPointFloat(stats.getKilometersByDay()));
                        graphDistance.addSeries(series);
                        totalDistance.setText(Float.toString(stats.getTotalCount()));
                        avgDistance.setText(Float.toString(stats.getAverageCount()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceStatisticsResponse> call, Throwable t) {
                distanceCount.cancel();
            }
        });

        moneyCount.enqueue(new Callback<MoneyStatisticsResponse>() {
            @Override
            public void onResponse(Call<MoneyStatisticsResponse> call, Response<MoneyStatisticsResponse> response) {
                MoneyStatisticsResponse stats = response.body();

                if (stats != null) {
                    try {
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getDataPoint(stats.getKilometersByDay()));
                        graphEarnings.addSeries(series);
                        totalEarned.setText(Float.toString(stats.getTotalCount()));
                        avgEarned.setText(Float.toString(stats.getAverageCount()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MoneyStatisticsResponse> call, Throwable t) {
                moneyCount.cancel();
            }
        });
    }

    private DataPoint[] getDataPoint(Map<String, Integer> countsByDay) throws ParseException {
        Integer size = countsByDay.size();
        DataPoint[] dp = new DataPoint[size];
        String[] d = countsByDay.keySet().toArray(new String[size]);
        for (int i = 0; i<countsByDay.size(); i++) {
            Date x = requestFormat.parse(d[i]);
            Integer y = countsByDay.get(d[i]);
            DataPoint v = new DataPoint(x, y);
            dp[i] = v;
        }
        if (size > 6 && size < 11) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
        } else if (size > 11 && size < 20) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
        } else if (size > 19 && size < 30) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
        } else if (size < 5){
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size);
        }
        return dp;
    }

    private DataPoint[] getDataPointFloat(Map<String, Float> countsByDay) throws ParseException {
        Integer size = countsByDay.size();
        DataPoint[] dp = new DataPoint[size];
        String[] d = countsByDay.keySet().toArray(new String[size]);
        for (int i = 0; i<countsByDay.size(); i++) {
            Date x = requestFormat.parse(d[i]);
            Float y = countsByDay.get(d[i]);
            DataPoint v = new DataPoint(x, y);
            dp[i] = v;
        }
        if (size > 5 && size < 11) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 2);
        } else if (size > 10 && size < 20) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 3);
        } else if (size > 19 && size < 30) {
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size / 4);
        } else if (size < 5){
            graphEarnings.getGridLabelRenderer().setNumHorizontalLabels(size);
            graphDistance.getGridLabelRenderer().setNumHorizontalLabels(size);
            graphRides.getGridLabelRenderer().setNumHorizontalLabels(size);
        }
        return dp;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    private void initializeGraphs(View view) {
        graphEarnings = (GraphView) view.findViewById(R.id.graph);
        graphRides = (GraphView) view.findViewById(R.id.graph2);
        graphDistance = (GraphView) view.findViewById(R.id.graph3);

        graphEarnings.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return outputDateFormat.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphRides.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return outputDateFormat.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphDistance.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return outputDateFormat.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }
}