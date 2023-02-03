package com.example.app_tim17.fragments.passenger;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import android.widget.PopupMenu;
import android.widget.Toast;


import com.example.app_tim17.R;
import com.example.app_tim17.fragments.EditProfileFragment;
import com.example.app_tim17.fragments.UserInfoFragment;

import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;

import com.example.app_tim17.fragments.driver.DriverStatisticsFragment;
import com.example.app_tim17.model.request.DriverUpdateRequest;
import com.example.app_tim17.model.request.PassengerUpdateRequest;

import com.example.app_tim17.model.response.PassengerResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.model.response.UserResponse;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePassengerFragment extends Fragment {
    private PassengerService passengerService;
    private TokenUtils tokenUtils;
    private String thisMonthStart;
    private String thisMonthEnd;
    private UserResponse passenger;
    private ShapeableImageView profilePic;

    public ProfilePassengerFragment() {
        // Required empty public constructor
    }

    public static ProfilePassengerFragment newInstance(String param1, String param2) {
        ProfilePassengerFragment fragment = new ProfilePassengerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_passenger, container, false);

        tokenUtils = new TokenUtils();
        RetrofitService retrofitService = new RetrofitService();
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        thisMonthStart = firstDay.toString();
        thisMonthEnd = lastDay.toString();
        initializeComponents(view);

        profilePic = (ShapeableImageView) view.findViewById(R.id.profile_pic_pass);
        profilePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                popupMenu(view);
                return true;
            }
        });

        Call<PassengerResponse> call = passengerService.getPassenger("Bearer " + getCurrentToken(), TokenUtils.getId(getCurrentToken()));

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {
                passenger = response.body();
                if (passenger != null) {
                    if (passenger.getProfilePicture() != null) {
                        byte[] decodedString = Base64.decode(passenger.getProfilePicture(), Base64.DEFAULT);
                        Bitmap picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profilePic.setImageBitmap(picture);
                    }
                }
            }
            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {
                call.cancel();
            }
        });

        Button favBtn = (Button) view.findViewById(R.id.favRouteBtn);
        Button edit = (Button) view.findViewById(R.id.editBtn);
        Button report = (Button) view.findViewById(R.id.reportBtn);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_passenger_container, new PassengerFavoriteRouteFragment(), PassengerFavoriteRouteFragment.class.getName());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("name", passenger.getName());
                args.putString("surname", passenger.getSurname());
                args.putString("address", passenger.getAddress());
                args.putString("phoneNumber", passenger.getTelephoneNumber());
                args.putString("email", passenger.getEmail());
                editProfileFragment.setArguments(args);
                transaction.add(R.id.fragment_passenger_container, editProfileFragment, EditProfileFragment.class.getName());

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_passenger_container, new DriverStatisticsFragment(), DriverStatisticsFragment.class.getName());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
    private void initializeComponents(View view) {

        TextView fullName = view.findViewById(R.id.full_name_profile_pass);
        ImageView profilePic = view.findViewById(R.id.profile_pic_pass);
        TextView email = view.findViewById(R.id.email_profile_pass);
        TextView phoneNumber = view.findViewById(R.id.phone_num_profile_pass);
        TextView address = view.findViewById(R.id.address_profile);
        TextView fullname = view.findViewById(R.id.fullname_field_pass);
        TextView emailfield = view.findViewById(R.id.email_field_pass);
        TextView numberOfRides = (TextView) view.findViewById(R.id.number_of_rides_pass);
        TextView moneyAmount = (TextView) view.findViewById(R.id.money_amount_pass);
        TextView kilometers = (TextView) view.findViewById(R.id.kilometers_pass);
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Long id = tokenUtils.getId(token);
        Call<PassengerResponse> call = passengerService.getPassenger("Bearer " + token, id);

        Call<RideStatisticsResponse> rideCount = passengerService.getRideCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<MoneyStatisticsResponse> moneyCount = passengerService.getMoneyCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<DistanceStatisticsResponse> distanceCount = passengerService.getDistanceCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        call.enqueue(new Callback<PassengerResponse>() {
            @Override
            public void onResponse(Call<PassengerResponse> call, Response<PassengerResponse> response) {

//                Log.d("TAG",response.code()+"");

                passenger = response.body();
                System.out.println(response);
                if (passenger != null) {
                    String fullNameStr = passenger.getName() + " " + passenger.getSurname();
                    fullName.setText(fullNameStr);
                    fullname.setText(fullNameStr);
                    address.setText(passenger.getAddress());
                    email.setText(passenger.getEmail());
                    emailfield.setText(passenger.getEmail());
                    phoneNumber.setText(passenger.getTelephoneNumber());
                }
            }
            @Override
            public void onFailure(Call<PassengerResponse> call, Throwable t) {
                call.cancel();
            }
        });
        rideCount.enqueue(new Callback<RideStatisticsResponse>() {
            @Override
            public void onResponse(Call<RideStatisticsResponse> call, Response<RideStatisticsResponse> response) {
                RideStatisticsResponse stats = response.body();

                if (stats != null) {
                    numberOfRides.setText(stats.getTotalCount().toString());
                }
            }

            @Override
            public void onFailure(Call<RideStatisticsResponse> call, Throwable t) {
                rideCount.cancel();
            }
        });

        moneyCount.enqueue(new Callback<MoneyStatisticsResponse>() {
            @Override
            public void onResponse(Call<MoneyStatisticsResponse> call, Response<MoneyStatisticsResponse> response) {
                MoneyStatisticsResponse stats = response.body();

                if (stats != null) {
                    moneyAmount.setText(Float.toString(stats.getTotalCount()));
                }
            }

            @Override
            public void onFailure(Call<MoneyStatisticsResponse> call, Throwable t) {
                moneyCount.cancel();
            }
        });

        distanceCount.enqueue(new Callback<DistanceStatisticsResponse>() {
            @Override
            public void onResponse(Call<DistanceStatisticsResponse> call, Response<DistanceStatisticsResponse> response) {
                DistanceStatisticsResponse stats = response.body();

                if (stats != null) {
                    kilometers.setText(Float.toString(stats.getTotalCount()));
                }
            }

            @Override
            public void onFailure(Call<DistanceStatisticsResponse> call, Throwable t) {
                distanceCount.cancel();
            }
        });

    }

    private void popupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.profile_pic_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.camera:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        try {
                            startActivityForResult(takePictureIntent, 1);
                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }
                        return true;
                    case R.id.galery:

                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                        return true;
                    default:
                        return true;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            if (selectedImage == null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                profilePic.setImageBitmap(photo);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                passenger.setProfilePicture(encoded);
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap photo = BitmapFactory.decodeFile(picturePath);
                profilePic.setImageBitmap(photo);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                passenger.setProfilePicture(encoded);
            }

            updateProfilePic();
        }
    }

    private void updateProfilePic() {
        Call<PassengerUpdateRequest> update = passengerService.updatePassenger("Bearer " + getCurrentToken(), new PassengerUpdateRequest(passenger), TokenUtils.getId(getCurrentToken()));
        update.enqueue(new Callback<PassengerUpdateRequest>() {
            @Override
            public void onResponse(Call<PassengerUpdateRequest> call, Response<PassengerUpdateRequest> response) {
                Toast.makeText(getContext(), "Successfully updated profile picture!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PassengerUpdateRequest> call, Throwable t) {

            }
        });
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}