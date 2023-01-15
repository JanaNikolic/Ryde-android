package com.example.app_tim17.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.chat.Chat;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;


public class InboxList extends ArrayAdapter{
    private List<String> UserNames = new ArrayList<>();
    private List<String> Times = new ArrayList<>();
    private List<String> Messages = new ArrayList<>();
    private Activity context;

    public InboxList(Activity context, List<Chat> inbox) {
        super(context, R.layout.inbox_row, inbox);
        this.context = context;
        for (Chat chat : inbox) {
            this.UserNames.add(chat.getUser().getName() + " " + chat.getUser().getSurname());
            this.Times.add(chat.getLastMessage().getTimeOfSending().substring(11, 16));
            this.Messages.add(chat.getLastMessage().getMessage());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.inbox_row, null, true);
        TextView userName = (TextView) row.findViewById(R.id.username);
        TextView time = (TextView) row.findViewById(R.id.time);
        TextView message = (TextView) row.findViewById(R.id.message);
        ShapeableImageView profilePicture = (ShapeableImageView) row.findViewById(R.id.profile_pic);

        userName.setText(UserNames.get(position));
        time.setText(Times.get(position));
        profilePicture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.profile_picture, null));
        message.setText(Messages.get(position));

        return  row;
    }
}
