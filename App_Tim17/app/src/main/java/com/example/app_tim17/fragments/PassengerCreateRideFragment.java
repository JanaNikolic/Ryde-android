package com.example.app_tim17.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.app_tim17.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerCreateRideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerCreateRideFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerCreateRideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerCreateRideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerCreateRideFragment newInstance(String param1, String param2) {
        PassengerCreateRideFragment fragment = new PassengerCreateRideFragment();
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
        View v = inflater.inflate(R.layout.fragment_passenger_create_ride, container, false);
        AutoCompleteTextView textView1 = (AutoCompleteTextView) v.findViewById(R.id.from);
        AutoCompleteTextView textView2 = (AutoCompleteTextView) v.findViewById(R.id.to);
        String[] streets1 = getResources().getStringArray(R.array.streets);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, streets1);
        textView1.setAdapter(adapter);
        textView2.setAdapter(adapter);
        Button buttonName = v.findViewById(R.id.btnSearch);
        buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.currentRide, new SuccesfullSearchFragment());
                fragmentTransaction.commit();
            }
        });
        return v;
    }
}