package com.example.app_tim17.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.ride.FavoriteRoute;
import com.example.app_tim17.model.response.ride.FavoriteRouteResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRouteAdapter extends ArrayAdapter {
    private List<FavoriteRoute> favoriteRoutes;
    private RetrofitService retrofitService;
    private RideService rideService;
    private TokenUtils tokenUtils;

    private Activity context;

    public FavoriteRouteAdapter(Activity context, List<FavoriteRoute> favoriteRoutes ) {
        super(context, R.layout.fav_route_adapt, favoriteRoutes);
        this.favoriteRoutes = favoriteRoutes;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FavoriteRoute favoriteRoute = favoriteRoutes.get(position);
        View view=convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            view = inflater.inflate(R.layout.fav_route_adapt, null, true);
        TextView from = (TextView) view.findViewById(R.id.from);
        TextView to = (TextView) view.findViewById(R.id.to);
        Button deleteBtn = view.findViewById(R.id.btnDeleteFav);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tokenUtils = new TokenUtils();
                retrofitService = new RetrofitService();
                rideService = retrofitService.getRetrofit().create(RideService.class);
                SharedPreferences sp = context.getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                Long id = tokenUtils.getId(token);
                Call<String> call = rideService.deleteFavoriteRoute("Bearer " + token, favoriteRoute.getId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("WTF", String.valueOf(response));
                        if (response.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Favorite ride with id: " + favoriteRoute.getId() + " Deleted!", Toast.LENGTH_SHORT).show();
                            Log.d("WTF", String.valueOf(response));

                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "NOT WORKING", Toast.LENGTH_SHORT);
                    }
                });

            }
        });

        from.setText(favoriteRoutes.get(position).getLocations().get(0).getDeparture().getAddress());
        to.setText(favoriteRoutes.get(position).getLocations().get(0).getDestination().getAddress());


        return  view;
    }
}