package com.example.app_tim17.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.app_tim17.R;
import com.example.app_tim17.model.response.driver.DriverResponse;
import com.example.app_tim17.model.response.vehicle.VehicleResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    DriverService driverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);
        bottomNavigationView = findViewById(R.id.nav_view_driver_account);
        bottomNavigationView.setSelectedItemId(R.id.profile_driver);
        bottomNavigationView.setOnItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();

        RetrofitService retrofitService = new RetrofitService();

        driverService = retrofitService.getRetrofit().create(DriverService.class);

        initializeComponents();

    }

    private void initializeComponents() {
        TextView fullName = findViewById(R.id.fullname_field);
        TextView email = findViewById(R.id.email_field);
        TextView model = findViewById(R.id.car_model);
        TextView licenseNumber = findViewById(R.id.license_number);

        Call<DriverResponse> call = driverService.getDriver(1001L);

        Call<VehicleResponse> callVehicle = driverService.getDriversVehicle(1001L);

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

                DriverResponse driver = response.body();
                if (driver != null) {
                    String fullNameStr = driver.getName() + " " + driver.getSurname();
                    fullName.setText(fullNameStr);
                    email.setText(driver.getEmail());

                }

            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                call.cancel();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView = findViewById(R.id.nav_view_driver_account);
        bottomNavigationView.setSelectedItemId(R.id.profile_driver);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                // navigate to settings screen
                return true;
            }
            case R.id.action_logout: {
                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inbox_driver:
                startActivity(new Intent(getApplicationContext(), DriverInboxActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.home_driver:
                startActivity(new Intent(getApplicationContext(), DriverMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(0,0);
                this.finish();
                return true;
            case R.id.history_driver:
                startActivity(new Intent(getApplicationContext(), DriverRideHistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.profile_driver:
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }

}