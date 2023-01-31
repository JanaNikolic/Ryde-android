package com.example.app_tim17.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.app_tim17.model.request.DriverUpdateRequest;
import com.example.app_tim17.model.request.PassengerUpdateRequest;
import com.example.app_tim17.retrofit.RetrofitService;
import com.example.app_tim17.service.DriverService;
import com.example.app_tim17.service.PassengerService;
import com.example.app_tim17.service.TokenUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    private TokenUtils tokenUtils;
    private RetrofitService retrofitService;
    private DriverService driverService;
    private PassengerService passengerService;
    private DriverUpdateRequest updateRequest;
    private String nameS, surnameS, addressS, telephoneNumberS, emailS;
    String patternPassword = "^(?=.*\\d)(?=.*[A-Z])(?!.*[^a-zA-Z0-9@#$^+=])(.{8,15})$";
    String patternEmail = "^[\\w\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    String patternPhone = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    Boolean validName = false, validSurname= false, validEmail= false, validAddress= false, validPhoneNumber= false, validPassword= false;
    EditText name, surname, email, address, phoneNumber, password,confirmPasword;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            nameS = args.getString("name");
            surnameS = args.getString("surname");
            addressS = args.getString("address");
            telephoneNumberS = args.getString("phoneNumber");
            emailS = args.getString("email");
        }

        retrofitService = new RetrofitService();
        tokenUtils = new TokenUtils();
        driverService = retrofitService.getRetrofit().create(DriverService.class);
        passengerService = retrofitService.getRetrofit().create(PassengerService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        Button edit = view.findViewById(R.id.btnSubmit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tokenUtils.getRole(getCurrentToken()).equals("ROLE_PASSENGER")) {
                    Call<PassengerUpdateRequest> passengerUpdate = passengerService.updatePassenger(getCurrentToken(),
                            new PassengerUpdateRequest(name.getEditableText().toString(), surname.getEditableText().toString(), null,
                                    phoneNumber.getEditableText().toString(),  email.getEditableText().toString(), address.getEditableText().toString()), TokenUtils.getId(getCurrentToken()));
                    passengerUpdate.enqueue(new Callback<PassengerUpdateRequest>() {
                        @Override
                        public void onResponse(Call<PassengerUpdateRequest> call, Response<PassengerUpdateRequest> response) {
                            PassengerUpdateRequest user = response.body();
                            Toast.makeText(getContext(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.remove(EditProfileFragment.this);
                            transaction.commit();
                        }

                        @Override
                        public void onFailure(Call<PassengerUpdateRequest> call, Throwable t) {
                            Toast.makeText(getContext(), "Error while editing.", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                } else {
                    Call<String> driverUpdate = driverService.driverUpdateRequest(getCurrentToken(),
                            new DriverUpdateRequest(name.getEditableText().toString(), surname.getEditableText().toString(), null,
                                    phoneNumber.getEditableText().toString(),  email.getEditableText().toString(), address.getEditableText().toString(), null));
                    driverUpdate.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String answer = response.body();
                            Toast.makeText(getContext(), "Edit request sent!", Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.remove(EditProfileFragment.this);
                            transaction.commit();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "Error while editing.", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                }
            }
        });

        name = (EditText) view.findViewById(R.id.editTextName);
        name.setText(nameS);
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
        surname = (EditText) view.findViewById(R.id.editTextLastName);
        surname.setText(surnameS);
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
        email = (EditText) view.findViewById(R.id.editEmail);
        email.setText(emailS);
        email.setEnabled(false);

        address = (EditText) view.findViewById(R.id.editTextAddress);
        address.setText(addressS);
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
        phoneNumber = (EditText) view.findViewById(R.id.editTextPhone);
        phoneNumber.setText(telephoneNumberS);
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
        return view;
    }


    private String getCurrentToken() {
        SharedPreferences sp = getActivity().getSharedPreferences("com.example.app_tim17_preferences", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }
}