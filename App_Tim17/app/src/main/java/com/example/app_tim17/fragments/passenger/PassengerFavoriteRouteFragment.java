package com.example.app_tim17.fragments.passenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.app_tim17.R;

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

        ListView listView = (ListView) view.findViewById(R.id.list_view_fav_route);
        Adapters.FavoriteRouteAdapter favRouteAdapter = new Adapters.FavoriteRouteAdapter(getActivity(), startAddress, endAddress);
        listView.setAdapter(favRouteAdapter);

        return view;
    }
}