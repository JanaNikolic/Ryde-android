package com.example.app_tim17.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private List<String> Types = new ArrayList<>();
    private Activity context;

    public InboxList(Activity context, List<Chat> inbox) {
        super(context, R.layout.inbox_row, inbox);
        this.context = context;
        for (Chat chat : inbox) {
            this.UserNames.add(chat.getUser().getName() + " " + chat.getUser().getSurname());
            this.Times.add(chat.getLastMessage().getTimeOfSending().substring(11, 16));
            this.Messages.add(chat.getLastMessage().getMessage());
            this.Types.add(chat.getLastMessage().getType());
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.inbox_row, null, true);
        ShapeableImageView image = (ShapeableImageView) row.findViewById(R.id.profile_pic);
        TextView userName = (TextView) row.findViewById(R.id.username);
        TextView time = (TextView) row.findViewById(R.id.time);
        TextView message = (TextView) row.findViewById(R.id.message);
        ShapeableImageView profilePicture = (ShapeableImageView) row.findViewById(R.id.profile_pic);
        Log.d("TAG", Types.get(position));
        if (Types.get(position).equals("SUPPORT")) {
            image.setStrokeColor(ColorStateList.valueOf(Color.BLUE));
        } else if (Types.get(position).equals("RIDE")) {
            image.setStrokeColor(ColorStateList.valueOf(Color.GREEN));
        } else {
            image.setStrokeColor(ColorStateList.valueOf(Color.RED));
        }
        userName.setText(UserNames.get(position));
        time.setText(Times.get(position));
        profilePicture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.profile_picture, null));
        message.setText(Messages.get(position));

        return  row;
    }
}
