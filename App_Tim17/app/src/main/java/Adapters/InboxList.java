package Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.app_tim17.R;
<<<<<<< Updated upstream:App_Tim17/app/src/main/java/Adapters/InboxList.java
=======
import com.example.app_tim17.model.response.chat.Chat;
>>>>>>> Stashed changes:App_Tim17/app/src/main/java/com/example/app_tim17/adapters/InboxList.java
import com.google.android.material.imageview.ShapeableImageView;

import io.getstream.avatarview.AvatarView;

public class InboxList extends ArrayAdapter{
    private String[] UserNames;
    private String[] Times;
    private String[] Messages;
    private Activity context;

    public InboxList(Activity context, String[] userNames, String[] times, String[] messages) {
        super(context, R.layout.inbox_row, userNames);
        this.context = context;
        this.UserNames = userNames;
        this.Times = times;
        this.Messages = messages;
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

        userName.setText(UserNames[position]);
        time.setText(Times[position]);
        profilePicture.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.profile_picture, null));
        message.setText(Messages[position]);

        return  row;
    }
}
