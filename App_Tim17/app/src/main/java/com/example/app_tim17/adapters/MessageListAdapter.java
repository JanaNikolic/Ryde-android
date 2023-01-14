
package com.example.app_tim17.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.message.Message;
import com.example.app_tim17.service.TokenUtils;

import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private TokenUtils tokenUtils = new TokenUtils();

    public MessageListAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.incoming_message, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);
        //SharedPreferences prefs= mContext.getSharedPreferences("com.example.app_tim17_preferences",Context.MODE_PRIVATE);
        //String token = prefs.getString("token","");

        //if (message.getSenderId().equals(tokenUtils.getId(token))) {
        if (message.getSenderId().equals(1009L)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);
        Log.d("ADAPTER", "RADI");

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeOfSending().toString());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeOfSending().toString());

            nameText.setText(message.getSenderId().toString());

            // Insert the profile image from the URL into the ImageView.
            profileImage.setImageResource(R.drawable.profile_picture);
        }
    }
}
