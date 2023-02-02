package com.example.app_tim17.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.model.request.ResetPasswordRequest;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    private final String patternPassword = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$";
    private final String patternEmail = "^[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private Boolean validPassword = false;
    private String email;
    private RetrofitService retrofitService;
    private UserService userService;
    private String newPassword;
    private String code;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reset_password, container, false);

        retrofitService = new RetrofitService();
        userService = retrofitService.getRetrofit().create(UserService.class);

        EditText emailInput = (EditText) view.findViewById(R.id.email);
        EditText newPass = (EditText) view.findViewById(R.id.new_password);
        EditText confirmPassword = (EditText) view.findViewById(R.id.confirm_password);
        EditText codeInput = (EditText) view.findViewById(R.id.reset_code);
        Button btnAccept = view.findViewById(R.id.btnSubmit);
        Button btnSendResetCode = view.findViewById(R.id.btnSendResetCode);

        ImageView showPass = view.findViewById(R.id.showPassBtn);
        ImageView showConfPass = view.findViewById(R.id.showConfPassBtn);


        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    ((ImageView)(view)).setImageResource(R.drawable.ic_hide_password);

                    //Show Password
                    newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    ((ImageView)(view)).setImageResource(R.drawable.ic_show_password);

                    //Hide Password
                    newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        showConfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    ((ImageView)(view)).setImageResource(R.drawable.ic_hide_password);

                    //Show Password
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    ((ImageView)(view)).setImageResource(R.drawable.ic_show_password);

                    //Hide Password
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        ConstraintLayout sendResetCode = view.findViewById(R.id.send_reset_code_layout);
        ConstraintLayout newPasswordLay = view.findViewById(R.id.new_password_layout);

        btnSendResetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailInput.getText().toString().trim().matches(patternEmail)) {

                    email = emailInput.getText().toString().trim();
                    Call<String> call = userService.resetCode(email);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            sendResetCode.setVisibility(View.GONE);
                            newPasswordLay.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "Opss, something went wrong!", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });


                } else {
                    Toast.makeText(getContext(), "Wrong input", Toast.LENGTH_SHORT).show();
                }
            }
        });



        newPass.addTextChangedListener(new TextWatcher() {
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

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals(newPass.getEditableText().toString())){
                    validPassword = false;
                } else {
                    validPassword = true;
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword = newPass.getText().toString().trim();
                code = codeInput.getText().toString().trim();
                if (validPassword && newPassword.equals(confirmPassword.getEditableText().toString()) && !code.equals("")) {
                    resetPassword();
                } else {
                    Toast.makeText(getContext(), "Wrong input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void ShowHidePassReset(View view){
        if(view.getId()==R.id.showPassBtn){
            EditText passwordInput = view.findViewById(R.id.new_password);
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
        } else if(view.getId()==R.id.showConfPassBtn){
            EditText passwordInput = view.findViewById(R.id.confirm_password);
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

    private void resetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, code);
        Call<String> call = userService.resetPassword(email, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Password successfully changed!", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.remove(ResetPasswordFragment.this);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.remove(ResetPasswordFragment.this);
                transaction.commit();
                call.cancel();
            }
        });
    }

}