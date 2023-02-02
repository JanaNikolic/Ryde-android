package com.example.app_tim17.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.RideInfoFragment;
import com.example.app_tim17.fragments.passenger.ChatFragment;
import com.example.app_tim17.fragments.passenger.PassengerRideInfoFragment;
import com.example.app_tim17.model.response.ride.Ride;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PassengerRideHistoryAdapter extends RecyclerView.Adapter<PassengerRideHistoryAdapter.ViewHolder>{
    private List<Ride> rides;

    private Context context;

    public PassengerRideHistoryAdapter(Context context, List<Ride>rides) {
        super();
        this.context = context;
        this.rides = rides;

    }
    @NonNull
    @Override
    public PassengerRideHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_history_view, parent, false);
        return new PassengerRideHistoryAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PassengerRideHistoryAdapter.ViewHolder holder, int position) {
        Ride ride = rides.get(position);


        holder.dateTextView.setText(rides.get(position).getStartTime().split("T")[0]);
        holder.startTimeTextView.setText(rides.get(position).getStartTime().split("T")[1].split("\\.")[0]);
        String endTimeText;
        String durationRide;
        if(rides.get(position).getEndTime() == null){
            endTimeText = "Not finished";
            durationRide = (rides.get(position).getEstimatedTimeInMinutes() +" min");
        }
        else{
            endTimeText = rides.get(position).getEndTime().split("T")[1].split("\\.")[0];
            int endTimeHour = Integer.parseInt(endTimeText.split(":")[0]);
            int endTimeMinute = Integer.parseInt(endTimeText.split(":")[1]);
            int endTimeSeconds = Integer.parseInt(endTimeText.split(":")[2]);
            LocalTime endTimeDur = LocalTime.of(endTimeHour, endTimeMinute, endTimeSeconds);
            int startTimeHour = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[0]);
            int startTimeMinute = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[1]);
            int startTimeSeconds = Integer.parseInt(ride.getStartTime().split("T")[1].split("\\.")[0].split(":")[2]);
            LocalTime startTimeDur = LocalTime.of(startTimeHour, startTimeMinute, startTimeSeconds);
            long minutes = ChronoUnit.MINUTES.between(startTimeDur, endTimeDur);
            durationRide = String.valueOf(minutes + " Min");

        }
        holder.endTimeTextView.setText(endTimeText);

        holder.durationTextView.setText(durationRide);
        String totalCost = (rides.get(position).getTotalCost() + " rsd");
        holder.priceTextView.setText(totalCost);
        String numOfPassenger = String.valueOf(rides.get(position).getPassengers().size());
        holder.numOfPassengersTextView.setText(numOfPassenger);
        holder.startAddressTextView.setText(rides.get(position).getLocations().get(0).getDeparture().getAddress());
        holder.endAddressTextView.setText(rides.get(position).getLocations().get(0).getDestination().getAddress());
        holder.roadLengthTextView.setText(R.string.roadLength);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                PassengerRideInfoFragment fragment = new PassengerRideInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("ride_id", ride.getId());
                fragment.setArguments(bundle);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();

            }
        });
        holder.itemView.findViewById(R.id.messages_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                Bundle arg = new Bundle();


                arg.putLong("userId", ride.getDriver().getId());
                arg.putString("userName", ride.getDriver().getEmail());
                arg.putString("type", "RIDE");
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setArguments(arg);
                transaction.replace(R.id.fragment_container, chatFragment).addToBackStack(null);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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
