package com.example.app_tim17.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_tim17.R;
import com.example.app_tim17.activities.UserLoginActivity;
import com.example.app_tim17.model.request.PasswordChangeRequest;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.TokenUtils;
import com.example.app_tim17.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private RetrofitService retrofitService;
    private UserService userService;
    String patternPassword = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$";
    Boolean validPassword = false;
    private TokenUtils tokenUtils;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        retrofitService = new RetrofitService();
        userService = retrofitService.getRetrofit().create(UserService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        EditText newPass = (EditText) view.findViewById(R.id.newPass);
        EditText oldPass = (EditText) view.findViewById(R.id.oldPass);

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
                    Toast.makeText(getActivity(), "Password must be 8 characters long and contain a digit.", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove("token");
                    edit.commit();
                    startActivity(new Intent(getContext(), UserLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    getActivity().finish();
                } else {
                    validPassword = true;
                }
            }
        });

        Button submit = (Button) view.findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> changePass = userService.changePassword(new PasswordChangeRequest(newPass.getEditableText().toString(), oldPass.getEditableText().toString()), "Bearer " + getCurrentToken(), TokenUtils.getId(getCurrentToken()));
                changePass.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(getActivity(), "Password successfully changed!", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.remove(ChangePasswordFragment.this);
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }

    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}