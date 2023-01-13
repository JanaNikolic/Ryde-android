package com.example.app_tim17.fragments.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.app_tim17.R;
import com.example.app_tim17.adapters.MessageListAdapter;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.TokenUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatDriverFragment extends Fragment {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<Message> messageList = new ArrayList<Message>();
    private MessageService messageService;
    private TokenUtils tokenUtils;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatDriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatDriverFragment newInstance(String param1, String param2) {
        ChatDriverFragment fragment = new ChatDriverFragment();
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
        View view = inflater.inflate(R.layout.fragment_chat_driver, container, false);
        Button btn = view.findViewById(R.id.button_gchat_send);
        EditText text = view.findViewById(R.id.edit_gchat_message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(text.getText().toString());
            }
        });
        RetrofitService retrofitService = new RetrofitService();
        messageService = retrofitService.getRetrofit().create(MessageService.class);
        //String token = getCurrentToken();
        Call<MessagesResponse> call = messageService.getMessages(1009L);

        call.enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                MessagesResponse messagesResponse = response.body();
                if (messagesResponse != null) {
                    for (Message message : messagesResponse.getMessages()) {
                        messageList.add(message);;
                    }
                    mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
                    mMessageAdapter = new MessageListAdapter(getContext(), messageList);
                    mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    mMessageRecycler.setAdapter(mMessageAdapter);

                }
            }

            @Override
            public void onFailure(Call<MessagesResponse> call, Throwable t) {
                call.cancel();
            }
        });
        return view;

    }

    private void sendMessage(String text) {

    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}