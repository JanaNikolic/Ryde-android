package com.example.app_tim17.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.app_tim17.R;
import com.example.app_tim17.fragments.ResetPasswordFragment;
import com.example.app_tim17.fragments.passenger.InboxPassengerFragment;
import com.example.app_tim17.model.request.LoginRequest;
import com.example.app_tim17.model.response.LoginResponse;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {
    
    private UserService userService;

    private TokenUtils tokenUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenUtils = new TokenUtils();
        setContentView(R.layout.activity_user_login);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView forgotPass = findViewById(R.id.forgotPassword);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.add(R.id.login_activity, ResetPasswordFragment.class, null);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitService retrofitService = new RetrofitService();

                userService = retrofitService.getRetrofit().create(UserService.class);

                EditText email = findViewById(R.id.emailInput);
                EditText password = findViewById(R.id.passwordInput);

                LoginRequest user = new LoginRequest();
                if (email.getText().length() == 0) {
                    Toast.makeText(UserLoginActivity.this, "Email field must not be empty", Toast.LENGTH_SHORT).show();
                } else if (password.getText().length() == 0) {
                    Toast.makeText(UserLoginActivity.this, "Password field must not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());

                    Call<LoginResponse> call = userService.login(user);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse token = response.body();

                            if (token != null) {
                                SharedPreferences sharedPreferences = getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPreferences.edit();
                                String saveToken = token.getAccessToken();
                                edit.putString("token", saveToken);
                                Log.i("Login",saveToken);
                                edit.commit();

                                String role = tokenUtils.getRole(saveToken);
                                Log.i("role", role);

                                if (role.equals("ROLE_DRIVER")) {
                                    Intent intent = new Intent(UserLoginActivity.this, DriverActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (role.equals("ROLE_PASSENGER")) {
                                    Intent intent = new Intent(UserLoginActivity.this, PassengerActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UserLoginActivity.this, "...", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserLoginActivity.this, "Email or password not valid", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(UserLoginActivity.this, "Email or password not valid", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                }
            }
        });
        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, PassengerRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ShowHidePass(View view){
        if(view.getId()==R.id.showPassBtn){
            EditText passwordInput = findViewById(R.id.passwordInput);
            if(passwordInput.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_hide_password);

                //Show Password
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_show_password);

                //Hide Password
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

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
    protected void onDestroy() {
        super.onDestroy();
    }
}