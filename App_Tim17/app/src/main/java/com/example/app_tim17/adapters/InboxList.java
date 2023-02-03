package com.example.app_tim17.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Base64;
import java.util.List;


public class InboxList extends ArrayAdapter{
    private List<String> UserNames = new ArrayList<>();
    private List<String> Times = new ArrayList<>();
    private List<String> Messages = new ArrayList<>();
    private List<String> Types = new ArrayList<>();
    private List<String> ProfilePictures = new ArrayList<>();
    private Activity context;

    public InboxList(Activity context, List<Chat> inbox) {
        super(context, R.layout.inbox_row, inbox);
        this.context = context;
        for (Chat chat : inbox) {
            this.UserNames.add(chat.getUser().getName() + " " + chat.getUser().getSurname());
            this.Times.add(chat.getLastMessage().getTimeOfSending().substring(11, 16));
            this.Messages.add(chat.getLastMessage().getMessage());
            this.Types.add(chat.getType());
            this.ProfilePictures.add(chat.getUser().getProfilePicture());
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
        TextView type = (TextView) row.findViewById(R.id.type);

        ShapeableImageView profilePicture = (ShapeableImageView) row.findViewById(R.id.profile_pic);
        if (Types.get(position).equals("SUPPORT")) {
            image.setStrokeColor(ColorStateList.valueOf(Color.BLUE));
            type.setText(Types.get(position));
            type.setTextColor(ColorStateList.valueOf(Color.BLUE));
        } else {
            image.setStrokeColor(ColorStateList.valueOf(Color.GREEN));
            type.setText(Types.get(position));
            type.setTextColor(ColorStateList.valueOf(Color.GREEN));
        }
        userName.setText(UserNames.get(position));
        time.setText(Times.get(position));
        if (ProfilePictures.get(position) == null || ProfilePictures.get(position).equals("")) {
            profilePicture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.profile_picture, null));
        } else {
            Log.d("profilePic", ProfilePictures.get(position));
            byte[] decodedString = Base64.getDecoder().decode(ProfilePictures.get(position));
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(decodedByte);
        }
        message.setText(Messages.get(position));

        return  row;
    }
}
