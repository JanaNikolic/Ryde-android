package com.example.app_tim17.fragments.passenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.PassengerSingularRideHistoryActivity;
import com.example.app_tim17.adapters.DriveHistoryList;
import com.example.app_tim17.adapters.DriverRideHistoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryPassengerFragment extends Fragment {

    private static RecyclerView recyclerView;
    private DriverRideHistoryAdapter rideHistoryAdapter;
    private static LinearLayoutManager linearLayoutManager;
    private final String[] dates = {"11. Oct", "18. Oct", "18. Oct", "18. Oct", "19. Oct", "19. Oct"};
    private final String[] startTime = {"11:30", "16:30", "15:30", "14:30", "13:30", "12:30"};
    private final String[] endTime = {"11:45", "17:00", "15:40", "14:45", "13:50", "12:55"};
    private final String[] duration = {"15:00", "30:00", "10:00", "15:00", "20:00", "25:00"};
    private final String[] price= {"150", "300", "100", "150", "200", "250"};
    private final String[] numOfPassengers = {"1", "1", "2", "1", "1", "2"};
    private final String[] startAddress = {"Bulevar Oslobođenja 15", "Bulevar Oslobođenja 150", "Železnička 12", "Fruškogorska 12", "Fruškogorska 18", "Fruškogorska 15"};
    private final String[] endAddress = {"Fruškogorska 15", "Železnička 12", "Fruškogorska 12", "Bulevar Oslobođenja 150", "Nemanjina 18", "Bulevar Oslobođenja 15"};
    private final String[] roadLength = {"1.5", "2.12", "1.28", "2.24", "3.95", "1.25"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryPassengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryPassengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryPassengerFragment newInstance(String param1, String param2) {
        HistoryPassengerFragment fragment = new HistoryPassengerFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_passenger, container, false);
        recyclerView = view.findViewById(R.id.pass_history_cards);
        recyclerView.setHasFixedSize(true);
//        rideHistoryAdapter = new DriverRideHistoryAdapter(getContext(), dates, startTime, endTime, duration, price, numOfPassengers, startAddress, endAddress,roadLength);

        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rideHistoryAdapter);

        return view;
    }
}