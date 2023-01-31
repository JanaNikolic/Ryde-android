package com.example.app_tim17.fragments.driver;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import static com.google.gson.internal.$Gson$Types.arrayOf;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.EditProfileFragment;
import com.example.app_tim17.model.request.DriverUpdateRequest;
import com.example.app_tim17.model.response.DistanceStatisticsResponse;
import com.example.app_tim17.model.response.MoneyStatisticsResponse;
import com.example.app_tim17.model.response.RideStatisticsResponse;
import com.example.app_tim17.model.response.UserResponse;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.TokenUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDriverFragment extends Fragment {

    private DriverService driverService;
    private TokenUtils tokenUtils;
    private String thisMonthStart;
    private String thisMonthEnd;
    private UserResponse driver;
    private ShapeableImageView profilePic;


    public ProfileDriverFragment() {
        // Required empty public constructor
    }


    public static ProfileDriverFragment newInstance(String param1, String param2) {
        ProfileDriverFragment fragment = new ProfileDriverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_driver, container, false);
        tokenUtils = new TokenUtils();
        RetrofitService retrofitService = new RetrofitService();
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        thisMonthStart = firstDay.toString();
        thisMonthEnd = lastDay.toString();
        initializeComponents(view);


        profilePic = (ShapeableImageView) view.findViewById(R.id.profile_pic);
        profilePic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                popupMenu(view);
                return true;
            }
        });

        Button edit = (Button) view.findViewById(R.id.editBtn);
        Button report = (Button) view.findViewById(R.id.reportBtn);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle args = new Bundle();
                args.putString("name", driver.getName());
                args.putString("surname", driver.getSurname());
                args.putString("address", driver.getAddress());
                args.putString("phoneNumber", driver.getTelephoneNumber());
                args.putString("email", driver.getEmail());
                editProfileFragment.setArguments(args);
                transaction.add(R.id.fragment_driver_container, editProfileFragment); // give your fragment container id in first parameter

                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_driver_container, new DriverStatisticsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void initializeComponents(View view) {
        TextView fullName = view.findViewById(R.id.full_name_profile);
        TextView email = view.findViewById(R.id.email_profile);
        TextView model = view.findViewById(R.id.car_model);
        TextView licenseNumber = view.findViewById(R.id.license_number);
        TextView phoneNumber = view.findViewById(R.id.phone_num_profile);
        TextView fullname = view.findViewById(R.id.fullname_field);
        TextView emailfield = view.findViewById(R.id.email_field);

        TextView numberOfRides = (TextView) view.findViewById(R.id.number_of_rides);
        TextView moneyAmount = (TextView) view.findViewById(R.id.money_amount);
        TextView kilometers = (TextView) view.findViewById(R.id.kilometers);

        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        Long id = tokenUtils.getId(token);

        Call<DriverResponse> call = driverService.getDriver(id, "Bearer " + token);

        Call<VehicleResponse> callVehicle = driverService.getDriversVehicle(id, "Bearer " + token);

        Call<RideStatisticsResponse> rideCount = driverService.getRideCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<MoneyStatisticsResponse> moneyCount = driverService.getMoneyCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        Call<DistanceStatisticsResponse> distanceCount = driverService.getDistanceCount(id, "Bearer " + token, thisMonthStart, thisMonthEnd);

        callVehicle.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                VehicleResponse vehicle = response.body();

                if (vehicle != null) {
                    model.setText(vehicle.getModel());
                    licenseNumber.setText(vehicle.getLicenseNumber());
                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                callVehicle.cancel();
            }
        });
        call.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {

//                Log.d("TAG",response.code()+"");

                driver = response.body();
                if (driver != null) {
                    String fullNameStr = driver.getName() + " " + driver.getSurname();
                    fullName.setText(fullNameStr);
                    fullname.setText(fullNameStr);
                    email.setText(driver.getEmail());
                    emailfield.setText(driver.getEmail());
                    phoneNumber.setText(driver.getTelephoneNumber());
                }
            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
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
                driver.setProfilePicture(encoded);
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
                driver.setProfilePicture(encoded);
            }

            updateDriver();
        }
    }

    private void updateDriver() {
        Call<String> update = driverService.driverUpdateRequest("Bearer " + getCurrentToken(), new DriverUpdateRequest(driver));
        update.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Successfully updated profile picture!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

}