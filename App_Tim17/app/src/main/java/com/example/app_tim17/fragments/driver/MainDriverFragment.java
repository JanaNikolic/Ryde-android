package com.example.app_tim17.fragments.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.MapsFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDriverFragment extends Fragment implements OnMapReadyCallback {

    private BottomSheetBehavior sheetBehavior;

    public MainDriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainDriverFragment newInstance(String param1, String param2) {
        MainDriverFragment fragment = new MainDriverFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_driver, container, false);
        sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.sheet));
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Fragment fragment = new MapsFragment();

        // Open fragment
        getChildFragmentManager()
                .beginTransaction().replace(R.id.map_container,fragment)
                .commit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onStart() {
        super.onStart();
        sheetBehavior.setPeekHeight(300);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}