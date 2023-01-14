package com.example.app_tim17.fragments.passenger;

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
import com.example.app_tim17.model.request.MessageRequest;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.model.response.message.MessagesResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.MessageService;
import com.example.app_tim17.service.TokenUtils;

import java.util.ArrayList;

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
    private ArrayList<Message> messageList = new ArrayList<Message>();
    private MessageService messageService;
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;

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
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatDriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tokenUtils = new TokenUtils();
        retrofitService = new RetrofitService();
        messageService = retrofitService.getRetrofit().create(MessageService.class);

        Call<MessagesResponse> call = messageService.getMessages(tokenUtils.getId(getCurrentToken()));

        call.enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                MessagesResponse messagesResponse = response.body();
                if (messagesResponse != null) {
                    for (Message message : messagesResponse.getMessages()) {
                        messageList.add(message);
                        Log.d("Message", "one message");
                    }
                    loadMessages(view);
                }
            }

            @Override
            public void onFailure(Call<MessagesResponse> call, Throwable t) {
                call.cancel();
            }
        });

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
                sendMessage(text.getText().toString(), 1006L);
                text.getText().clear();
            }
        });


        return view;

    }

    private void loadMessages(View view) {
        mMessageRecycler = (RecyclerView) view.findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(getContext(), messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(layoutManager);
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
        MessageRequest messageRequest = new MessageRequest(receiverId, text, "SUPPORT", null);

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
}