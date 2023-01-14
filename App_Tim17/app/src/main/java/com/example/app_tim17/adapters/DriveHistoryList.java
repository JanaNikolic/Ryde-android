package com.example.app_tim17.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.PassengerRideInfoFragment;
import com.example.app_tim17.fragments.RideInfoFragment;
import com.google.android.material.imageview.ShapeableImageView;

public class DriveHistoryList extends RecyclerView.Adapter<DriveHistoryList.ViewHolder> {
    private String[] dates;
    private String[] startTimes;
    private String[] endTimes;
    private String[] durations;
    private String[] prices;
    private String[] numOfPassengers;
    //    private int[] rating;
    private String[] startAddresses;
    private String[] endAddresses;
    private String[] roadLengths;
    private Context context;

    public DriveHistoryList(Context context, String[] date, String[] startTime, String[] endTime,
                            String[] duration, String[] price, String[] numOfPassengers,
                            String[] startAddress, String[] endAddress, String[] roadLength) {
        super();
        this.context = context;
        this.dates = date;
        this.startTimes = startTime;
        this.endTimes = endTime;
        this.durations = duration;
        this.prices = price;
        this.numOfPassengers = numOfPassengers;
//        this.rating = rating;
        this.startAddresses = startAddress;
        this.endAddresses = endAddress;
        this.roadLengths = roadLength;
    }
    @NonNull
    @Override
    public DriveHistoryList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_history_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.dateTextView.setText(dates[position]);
        holder.startTimeTextView.setText(startTimes[position]);
        holder.endTimeTextView.setText(endTimes[position]);
        holder.durationTextView.setText(durations[position]);
        holder.priceTextView.setText(prices[position]);
        holder.numOfPassengersTextView.setText(numOfPassengers[position]);
        holder.startAddressTextView.setText(startAddresses[position]);
        holder.endAddressTextView.setText(endAddresses[position]);
        holder.roadLengthTextView.setText(roadLengths[position] + " km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                PassengerRideInfoFragment fragment = new PassengerRideInfoFragment();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container2, fragment).addToBackStack(null);
                transaction.commit();

            }
        });

    }
    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return 6;
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
        }
    }



}
