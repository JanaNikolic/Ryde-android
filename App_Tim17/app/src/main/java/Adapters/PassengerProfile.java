package Adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.google.android.material.imageview.ShapeableImageView;

import Model.Passenger;

public class PassengerProfile extends BaseAdapter {
    private Activity activity;
    Passenger p = new Passenger("Bogdan", "Bogdanovic",
            "0652778154", "Bogdan1@gmail.com", "Masarikova 17", R.drawable.profile_picture);
    public PassengerProfile(Activity activity) {this.activity = activity;}

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return p;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView==null)
            view = activity.getLayoutInflater().inflate(R.layout.passenger_profile_list, null);

        TextView name = (TextView)view.findViewById(R.id.name);
        TextView lastName = (TextView)view.findViewById(R.id.lastName);
        TextView email = (TextView)view.findViewById(R.id.email);
        TextView phone = (TextView)view.findViewById(R.id.phone);
        TextView address = (TextView)view.findViewById(R.id.address);
        ShapeableImageView profilePicture = (ShapeableImageView) view.findViewById(R.id.item_icon);

        name.setText(p.getName());
        lastName.setText(p.getLastname());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());
        address.setText(p.getAddress());
        profilePicture.setImageResource(p.getAvatar());



        return  view;
    }
}
