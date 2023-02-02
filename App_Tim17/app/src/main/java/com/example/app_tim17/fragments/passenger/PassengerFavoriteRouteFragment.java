package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.FavoriteRouteAdapter;
import com.example.app_tim17.adapters.InboxList;
import com.example.app_tim17.model.response.chat.Chat;
import com.example.app_tim17.model.response.chat.ChatResponse;
import com.example.app_tim17.model.response.ride.FavoriteRoute;
import com.example.app_tim17.model.response.ride.FavoriteRouteResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.RideService;
import com.example.app_tim17.service.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerFavoriteRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerFavoriteRouteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<FavoriteRoute> favoriteRoutes;
    private RetrofitService retrofitService;
    private RideService rideService;
    private TokenUtils tokenUtils;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerFavoriteRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerFavoriteRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerFavoriteRouteFragment newInstance(String param1, String param2) {
        PassengerFavoriteRouteFragment fragment = new PassengerFavoriteRouteFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_favorite_route, container, false);
        tokenUtils = new TokenUtils();
        retrofitService = new RetrofitService();
        rideService = retrofitService.getRetrofit().create(RideService.class);
        ListView listView = (ListView) view.findViewById(R.id.list_view_fav_route);
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Call<FavoriteRouteResponse> call = rideService.getFavoriteRoutes("Bearer " + token);

        call.enqueue(new Callback<FavoriteRouteResponse>() {
            @Override
            public void onResponse(Call<FavoriteRouteResponse> call, Response<FavoriteRouteResponse> response) {
                Log.d("WTF", response.body().toString());
                if (response.isSuccessful()) {
                    Log.d("WTF", response.body().toString());
                    FavoriteRouteResponse favRouteResponse = response.body();
                    Log.d("WTF", favRouteResponse.toString());
                    favoriteRoutes = new ArrayList<>();
                    if (favRouteResponse.getResults() != null) {
                        for (FavoriteRoute favoriteRoute : favRouteResponse.getResults()) {
                            favoriteRoutes.add(favoriteRoute);
                            Log.d("Message", "one message");
                        }
                        FavoriteRouteAdapter favRouteAdapter = new FavoriteRouteAdapter(getActivity(), favoriteRoutes);
                        listView.setAdapter(favRouteAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<FavoriteRouteResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "NOT WORKING", Toast.LENGTH_SHORT);
            }
        });



        return view;
    }
}