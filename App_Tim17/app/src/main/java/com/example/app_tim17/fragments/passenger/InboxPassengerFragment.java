package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.InboxList;
import com.example.app_tim17.model.response.chat.Chat;
import com.example.app_tim17.model.response.chat.ChatResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.TokenUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxPassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxPassengerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String[] userNames = {"User001", "User002", "User003", "User004", "User005", "User006", "User007", "User008", "User009", "User010"};
    private final String times[] = {"12:03", "12:05", "09:30", "03:33", "12:03", "12:05", "09:30", "03:33", "11:09", "17:22"};
    private final String messages[] = {"Hello", "Alright", "Listen here, I know I am late, but I am trying my best, with this traffic", "What!? NO!", "Yes", "Why?", "Cya", "Thanks, man.", "Hm", "xD"};
    private RetrofitService retrofitService;
    private MessageService messageService;
    private TokenUtils tokenUtils;
    private InboxList inboxList;
    private List<Chat> mChatList;
    private Gson gson = new Gson();
    public InboxPassengerFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static InboxPassengerFragment newInstance(String param1, String param2) {
        InboxPassengerFragment fragment = new InboxPassengerFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox_passenger, container, false);
        tokenUtils = new TokenUtils();

        retrofitService = new RetrofitService();
        messageService = retrofitService.getRetrofit().create(MessageService.class);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        Call<ChatResponse> call = messageService.getChats(tokenUtils.getId(getCurrentToken()), "Bearer " + getCurrentToken());

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful()) {
                    ChatResponse chatResponse = response.body();
                    mChatList = new ArrayList<>();
                    if (chatResponse != null) {
                        for (Chat chat : chatResponse.getChats()) {
                            mChatList.add(chat);
                        }
                        inboxList = new InboxList(getActivity(), mChatList);
                        listView.setAdapter(inboxList);
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "NOT WORKING", Toast.LENGTH_SHORT);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ChatFragment chatFragment = ChatFragment.newInstance();
                Bundle args = new Bundle();
                Chat chat = (Chat) inboxList.getItem(position);
                args.putLong("userId", chat.getUser().getId());
                args.putString("userName", chat.getUser().getName() + " " + chat.getUser().getSurname());
                args.putString("type", chat.getType());
                chatFragment.setArguments(args);
                replaceFragment(chatFragment);
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getParentFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_passenger_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}