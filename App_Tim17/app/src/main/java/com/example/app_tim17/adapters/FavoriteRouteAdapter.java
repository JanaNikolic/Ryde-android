package Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.app_tim17.R;
import com.google.android.material.imageview.ShapeableImageView;

public class FavoriteRouteAdapter extends ArrayAdapter {
    private String[] startAddresses;
    private String[] endAddresses;
    private Activity context;

    public FavoriteRouteAdapter(Activity context,  String[] startAddresses, String[] endAddresses) {
        super(context, R.layout.fav_route_adapt, startAddresses);
        this.context = context;
        this.startAddresses = startAddresses;
        this.endAddresses = endAddresses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            view = inflater.inflate(R.layout.fav_route_adapt, null, true);
        TextView from = (TextView) view.findViewById(R.id.from);
        TextView to = (TextView) view.findViewById(R.id.to);
        Button orderBtn = view.findViewById(R.id.btnOrder);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Order", Toast.LENGTH_SHORT).show();
            }
        });
        Button deleteBtn = view.findViewById(R.id.btnDeleteFav);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


        from.setText(startAddresses[position]);
        to.setText(endAddresses[position]);


        return  view;
    }
}