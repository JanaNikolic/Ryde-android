package com.example.app_tim17.fragments.passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.FavoriteRouteAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerFavoriteRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerFavoriteRouteFragment extends Fragment {


    private final String[] startAddress = {"Bulevar Oslobođenja 15", "Bulevar Oslobođenja 150",
            "Železnička 12", "Fruškogorska 12", "Fruškogorska 18", "Fruškogorska 15"};
    private final String[] endAddress = {"Fruškogorska 15", "Železnička 12", "Fruškogorska 12",
            "Bulevar Oslobođenja 150", "Nemanjina 18", "Bulevar Oslobođenja 15"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerFavoriteRouteFragment() {
        // Required empty public constructor
    }

    public static PassengerFavoriteRouteFragment newInstance(String param1, String param2) {
        PassengerFavoriteRouteFragment fragment = new PassengerFavoriteRouteFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger_favorite_route, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_view_fav_route);
        FavoriteRouteAdapter favRouteAdapter = new FavoriteRouteAdapter(getActivity(), startAddress, endAddress);
        listView.setAdapter(favRouteAdapter);


        return view;
    }
}