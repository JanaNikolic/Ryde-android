package com.example.app_tim17.fragments.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.DriverInboxChatActivity;
import com.example.app_tim17.adapters.InboxList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxDriverFragment extends Fragment {
    private final String[] userNames = {"User001", "User002", "User003", "User004", "User005", "User006", "User007", "User008", "User009", "User010"};
    private final String times[] = {"12:03", "12:05", "09:30", "03:33", "12:03", "12:05", "09:30", "03:33", "11:09", "17:22"};
    private final String messages[] = {"Hello", "Alright", "Listen here, I know I am late, but I am trying my best, with this traffic", "What!? NO!", "Yes", "Why?", "Cya", "Thanks, man.", "Hm", "xD"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InboxDriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InboxDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxDriverFragment newInstance(String param1, String param2) {
        InboxDriverFragment fragment = new InboxDriverFragment();
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
        View view= inflater.inflate(R.layout.fragment_inbox_driver, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list_view);
        InboxList inboxList = new InboxList(getActivity(), userNames, times, messages);
        listView.setAdapter(inboxList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(view.getContext(), DriverInboxChatActivity.class));
            }
        });
        return view;
    }
}