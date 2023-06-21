package com.mobile.uko.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.adapter.SpinnerLocationAdapter;
import com.mobile.uko.model.LocationAllModel;
import com.mobile.uko.model.RegisterModel;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.DateInputMask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_back;
    private EditText ed_first_name, ed_last_name, ed_phone, ed_country, ed_birth_date,
        ed_employment, ed_facebook, ed_qualification;
    private TextInputLayout ti_first_name, ti_last_name, ti_phone, ti_country, ti_date_birth, ti_employment;
    private Button btn_continue;
    private Spinner spin_location, spin_gender;
    APIClient apiClient;
    List<LocationAllModel> model;
    SpinnerLocationAdapter adapter;
    RegisterModel registerModel;
    ArrayList<String> locationName = new ArrayList<String>();
    int location_id = 0;
    String[] genderList = {"Male", "Female", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        apiClient = new APIClient();
        model = new ArrayList<LocationAllModel>();
        registerModel = new RegisterModel();

        if (getIntent().getExtras() != null) {
            registerModel = (RegisterModel) getIntent().getSerializableExtra("sign_model");
        }

        initUI();
        getLocationData();
    }

    private void initUI() {
        img_back = (ImageView) findViewById(R.id.img_close);
        ed_first_name = (EditText) findViewById(R.id.ed_first_name);
        ed_last_name = (EditText) findViewById(R.id.ed_last_name);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_country = (EditText) findViewById(R.id.ed_country);
        ed_birth_date = (EditText) findViewById(R.id.ed_birth);
        ed_employment = (EditText) findViewById(R.id.ed_employment);
        ed_facebook = (EditText) findViewById(R.id.ed_facebook);
        ed_qualification = (EditText) findViewById(R.id.ed_qualification);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        spin_location = (Spinner) findViewById(R.id.spin_location);
        spin_gender = (Spinner) findViewById(R.id.spin_gender);

        ti_first_name = (TextInputLayout) findViewById(R.id.input_first_name);
        ti_last_name = (TextInputLayout) findViewById(R.id.input_last_name);
        ti_phone = (TextInputLayout) findViewById(R.id.input_phone);
        ti_country = (TextInputLayout) findViewById(R.id.input_country);
        ti_date_birth = (TextInputLayout) findViewById(R.id.input_birth_date);
        ti_employment = (TextInputLayout) findViewById(R.id.input_employment);

        img_back.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        new DateInputMask(ed_birth_date);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(R.layout.spin_list_item);
        spin_gender.setAdapter(adapter);
        spin_gender.setSelection(0);

        spin_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_gray));
                if (position < model.size()) {
                    location_id = model.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                if (registerModel != null)
                    registerModel = new RegisterModel();

                startActivity(new Intent(this, OnBoardActivity.class));
                finishAffinity();
                break;
            case R.id.btn_continue:
                if (validate()) {
                    gotoNextPage();
                }
                break;
        }
    }

    private void getLocationData() {
        Common.showLoading(this);

        Call<List<LocationAllModel>> response = apiClient.getServices().getLocationAll();
        response.enqueue(new Callback<List<LocationAllModel>>() {
            @Override
            public void onResponse(Call<List<LocationAllModel>> call, Response<List<LocationAllModel>> response) {
                Common.hideLoading();

                if (response.isSuccessful()) {
                    model = response.body();

                    adapter = new SpinnerLocationAdapter(getApplicationContext(), R.layout.spin_list_item);

                    int count = model.size();
                    for (int i=0; i<count; i++) {
                        locationName.add(model.get(i).getName());
                    }

                    adapter.addAll(locationName);
                    adapter.add(getString(R.string.location));
//                    spin_location.setPrompt(getString(R.string.location));
                    spin_location.setAdapter(adapter);
                    spin_location.setSelection(adapter.getCount());

//                    addSpinValue();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LocationAllModel>> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }

    private boolean validate() {
        if (ed_first_name.getText().toString().isEmpty()) {
            ti_first_name.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_first_name.setError(null);
        }

        if (ed_last_name.getText().toString().isEmpty()) {
            ti_last_name.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_last_name.setError(null);
        }

        if (ed_phone.getText().toString().isEmpty()) {
            ti_phone.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_phone.setError(null);
        }

        if (ed_country.getText().toString().isEmpty()) {
            ti_country.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_country.setError(null);
        }

        if (ed_birth_date.getText().toString().isEmpty()) {
            ti_date_birth.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_date_birth.setError(null);
        }

        if (ed_employment.getText().toString().isEmpty()) {
            ti_employment.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_employment.setError(null);
        }

        if (location_id == 0) {
            Toast.makeText(this, getString(R.string.select_location_id), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void gotoNextPage() {
        registerModel.setPropertyLocationId(location_id);
        registerModel.setFirstName(ed_first_name.getText().toString());
        registerModel.setLastName(ed_last_name.getText().toString());
        registerModel.setEducation(ed_qualification.getText().toString());
        registerModel.setEmployment(ed_employment.getText().toString());
        registerModel.setCountryOrigin(ed_country.getText().toString());
        registerModel.setFacebookId(ed_facebook.getText().toString());
        registerModel.setDateOfBirth(ed_birth_date.getText().toString());
        registerModel.setPhoneNumber(ed_phone.getText().toString());
        registerModel.setGender(spin_gender.getSelectedItem().toString());
//        if (spin_gender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
//            registerModel.setGender("M");
//        } else if (spin_gender.getSelectedItem().toString().equalsIgnoreCase("Female")) {
//            registerModel.setGender("F");
//        } else {
//            registerModel.setGender("O");
//        }

        Intent intent = new Intent(CompleteProfileActivity.this, SignupFinal.class);
        intent.putExtra("sign_model", registerModel);
        startActivity(intent);
        finishAffinity();
    }

    private void addSpinValue() {
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.spin_list_item, locationName) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.dark_gray));
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinAdapter.setDropDownViewResource(R.layout.spin_list_item);
        spin_location.setAdapter(spinAdapter);

    }
}