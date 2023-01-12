package com.example.app_tim17.fragments.passenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.PassengerSingularRideHistoryActivity;
import com.example.app_tim17.adapters.DriveHistoryList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryPassengerFragment extends Fragment {

    private final String[] startLocations = {"Nikole Tesle 76", "Karadjordjeva 22",
            "Strazilovska 5", "Masarikova 19", "Maksima Gorkog 55",
            "Futoska 18", "Omladinska 44", "Proleterska 6",
            "Narodnog fronta 66", "Janka Cmelika 1"};
    private final String[] endLocations = {"Strazilovska 3", "Proleterska 6",
            "Nikole Tesle 86", "Omladinska 4", "Ive Lole Ribara 13",
            "Maksima Gorkog 7", "Masarikova 2", "Karadjordjeva 12",
            "Maksima Gorkog 44", "Branka Bajica 22"};
    private final String[] startTimes = {"18/09/2022", "21/09/2022",
            "30/09/2022", "18/10/2022", "21/10/2022",
            "26/10/2022", "30/10/2022", "10/11/2022",
            "11/11/2022", "12/11/2022"};
    private final String durations[] = {"26:21", "51:05", "09:30",
            "03:33", "12:03", "12:05", "09:30",
            "03:33", "11:09", "17:22"};
    private final String prices[] = {"66$", "68$", "96$", "90$",
            "107$", "45$", "55$", "78$", "98$", "34$"};
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
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        DriveHistoryList driveHistory = new DriveHistoryList(getActivity(), startLocations,
                endLocations, startTimes, durations, prices);
        listView.setAdapter(driveHistory);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(view.getContext(), PassengerSingularRideHistoryActivity.class));

            }
        });
        return view;
    }
}