package com.example.app_tim17.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.app_tim17.R;
import com.google.android.material.imageview.ShapeableImageView;

public class DriveHistoryList extends ArrayAdapter {
    private String[] startLocations;
    private String[] endLocations;
    private String[] startTimes;
    private String[] durations;
    private String[] prices;
    private Activity context;

    public DriveHistoryList(Activity context, String[] startLocations, String[] endLocations, String[] startTimes, String[] durations, String[] prices) {
        super(context, R.layout.inbox_row, startTimes);
        this.context = context;
        this.startLocations = startLocations;
        this.endLocations = endLocations;
        this.startTimes = startTimes;
        this.durations = durations;
        this.prices = prices;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            view = inflater.inflate(R.layout.drive_history_view, null, true);

        TextView startTime = (TextView) view.findViewById(R.id.startTime);
        TextView duration = (TextView) view.findViewById(R.id.duration);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView startLocation = (TextView) view.findViewById(R.id.startLocation);
        TextView endLocation = (TextView) view.findViewById(R.id.endLocation);
        ShapeableImageView profilePicture = (ShapeableImageView) view.findViewById(R.id.rider_profile);


        startTime.setText(startTimes[position]);
        duration.setText(durations[position]);
        profilePicture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.profile_picture, null));
        price.setText(prices[position]);
        startLocation.setText(startLocations[position]);
        endLocation.setText(endLocations[position]);


        return  view;
    }

}
