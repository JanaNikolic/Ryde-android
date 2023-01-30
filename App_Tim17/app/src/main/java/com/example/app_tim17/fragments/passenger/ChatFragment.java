package com.example.app_tim17.fragments.passenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.PassengerActivity;
import com.example.app_tim17.adapters.MessageListAdapter;
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.TokenUtils;

import java.util.ArrayList;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<Message> messageList = new ArrayList<>();
    private ArrayList<Message> newMessageList;
    private MessageService messageService;
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;
    CountDownTimer timer;
    private String name;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ChatDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tokenUtils = new TokenUtils();
        Bundle args = getArguments();
        Long userId = args.getLong("userId");
        name = args.getString("userName");

        retrofitService = new RetrofitService();
        messageService = retrofitService.getRetrofit().create(MessageService.class);
        mMessageAdapter = new MessageListAdapter(getContext(), messageList, name);

        timer = new CountDownTimer(Long.MAX_VALUE, 2000) {
            @Override
            public void onTick(long l) {
                Call<MessagesResponse> call = messageService.getMessages(userId, "Bearer " + getCurrentToken());

                call.enqueue(new Callback<MessagesResponse>() {
                    @Override
                    public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                        MessagesResponse messagesResponse = response.body();
                        newMessageList = new ArrayList<>();
                        if (messagesResponse != null) {
                            for (Message message : messagesResponse.getMessages()) {
                                newMessageList.add(message);
                            }
                            if (newMessageList.size() > mMessageAdapter.getItemCount()) {
                                messageList = newMessageList;
                                loadMessages(view);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessagesResponse> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
            @Override
            public void onFinish() {
            }
        };

        timer.start();

        View message = view.findViewById(R.id.edit_gchat_message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessageRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageRecycler.scrollToPosition(mMessageRecycler.getAdapter().getItemCount() - 1);
                    }
                }, 500);
            }
        });

        Button btn = view.findViewById(R.id.button_gchat_send);
        EditText text = view.findViewById(R.id.edit_gchat_message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(text.getText().toString(), userId);
                text.getText().clear();
            }
        });


        return view;

    }

    public void loadMessages(View view) {
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageAdapter = new MessageListAdapter(getContext(), messageList, name);
        mMessageRecycler.setAdapter(mMessageAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(layoutManager);
    }

    public void sendSocketMessage(MessageRequest messageRequest) {
        Log.i("WebSocket", "Button was clicked");
        PassengerActivity pa = (PassengerActivity) getActivity();
        pa.sendEchoViaStomp(messageRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMessages(getView());
    }

    private void sendMessage(String text, Long receiverId) {
        retrofitService = new RetrofitService();
        messageService = retrofitService.getRetrofit().create(MessageService.class);
        String token = "Bearer " + getCurrentToken();
        MessageRequest messageRequest = new MessageRequest(receiverId, text,"SUPPORT", null);

        sendSocketMessage(messageRequest);

        Call<Message> call = messageService.sendMessage(receiverId, token, messageRequest);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message message = response.body();
                if (message != null) {
                    Integer size = messageList.size();
                    messageList.add(message);
                    mMessageAdapter.notifyItemInserted(size);
                    mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount()-1);
                }
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }
}