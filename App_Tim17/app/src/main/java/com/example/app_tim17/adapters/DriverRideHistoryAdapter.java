package com.example.app_tim17.adapters;

import android.content.Context;
import android.os.Bundle;
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
import com.example.app_tim17.model.response.ride.Ride;
import com.example.app_tim17.tools.Utils;

import java.util.List;

public class DriverRideHistoryAdapter extends RecyclerView.Adapter<DriverRideHistoryAdapter.ViewHolder> {
    private List<Ride> rides;
    private Context context;
    private Ride ride;

    public DriverRideHistoryAdapter(Context context, List<Ride> rides) {
        super();
        this.context = context;
        this.rides = rides;
    }
    @NonNull
    @Override
    public DriverRideHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_ride_history_card_view, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ride = rides.get(position);

        holder.dateTextView.setText(ride.getStartTime().split("T")[0]);
        holder.startTimeTextView.setText(ride.getStartTime().split("T")[1].substring(0, 5));
        holder.endTimeTextView.setText(ride.getEndTime().split("T")[1].substring(0, 5));
        holder.durationTextView.setText(ride.getEstimatedTimeInMinutes() + " min");
        holder.priceTextView.setText(ride.getTotalCost() + " RSD");
//        holder.numOfPassengersTextView.setText();
        holder.startAddressTextView.setText(ride.getLocations().get(0).getDeparture().getAddress());
        holder.endAddressTextView.setText(ride.getLocations().get(0).getDestination().getAddress());
        holder.roadLengthTextView.setText(" km"); //TODO add length

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                RideInfoFragment fragment = new RideInfoFragment();

                Bundle args = new Bundle();
                args.putString("ride", Utils.getGsonParser().toJson(ride));
                fragment.setArguments(args);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
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
