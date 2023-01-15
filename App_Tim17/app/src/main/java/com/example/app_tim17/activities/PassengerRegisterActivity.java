package com.example.app_tim17.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.app_tim17.R;
import com.example.app_tim17.model.request.UserRequest;
import com.example.app_tim17.model.response.LoginResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.UserService;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PassengerRegisterActivity extends AppCompatActivity {

    EditText name, surname, email, address, phoneNumber, password,confirmPasword;
    RetrofitService mRetrofitService;
    PassengerService passengerService;
    Boolean validName = false, validSurname= false, validEmail= false, validAddress= false, validPhoneNumber= false, validPassword= false;
    String patternPassword = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$";
    String patternEmail = "^[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    String patternPhone = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);
        mRetrofitService = new RetrofitService();
        passengerService = mRetrofitService.getRetrofit().create(PassengerService.class);

        Button btnAccept = findViewById(R.id.btnAccept);
        name = (EditText) findViewById(R.id.editTextName);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")){
                    validName = false;
                } else {
                    validName = true;
                }
            }
        });
        surname = (EditText) findViewById(R.id.editTextLastName);
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")){
                    validSurname = false;
                } else {
                    validSurname = true;
                }
            }
        });
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().matches(patternEmail)){
                    validEmail = false;
                } else {
                    validEmail = true;
                }
            }
        });
        address = (EditText) findViewById(R.id.editTextAddress);
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")){
                    validAddress = false;
                } else {
                    validAddress = true;
                }
            }
        });
        phoneNumber = (EditText) findViewById(R.id.editTextPhone);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().matches(patternPhone)){
                    validPhoneNumber = false;
                } else {
                    validPhoneNumber = true;
                }
            }
        });
        password = (EditText) findViewById(R.id.editTextTextPassword1);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().matches(patternPassword)){
                    validPassword = false;
                } else {
                    validPassword = true;
                }
            }
        });
        confirmPasword = (EditText) findViewById(R.id.editTextTextPassword2);
        confirmPasword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals(password.getEditableText().toString())){
                    validPassword = false;
                } else {
                    validPassword = true;
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< Updated upstream
                Intent intent = new Intent(PassengerRegisterActivity.this, PassengerMainActivity.class);
                startActivity(intent);
=======
                if (validName && validAddress && validSurname && validEmail && validPassword && validPhoneNumber && password.getEditableText().toString().equals(confirmPasword.getEditableText().toString())) {
                    register();
                } else {
                }
>>>>>>> Stashed changes
            }
        });
    }

    private void register() {
        UserRequest userRequest = new UserRequest(name.getEditableText().toString(), surname.getEditableText().toString(), null,
                 phoneNumber.getEditableText().toString(),  email.getEditableText().toString(), address.getEditableText().toString(), password.getEditableText().toString());
        Call<UserRequest> call = passengerService.register(userRequest);

        call.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                UserRequest user = response.body();

                Intent intent = new Intent(PassengerRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {
                Toast.makeText(PassengerRegisterActivity.this, "Error while registering", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

        Toast.makeText(getApplicationContext(), "NO!", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}