package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.adapter.SpinnerLocationAdapter;
import com.mobile.uko.model.LocationAllModel;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupEnquireActivity extends AppCompatActivity {

    private ImageView imgClose;
    private EditText ed_email, ed_phone, ed_comments;
    private TextInputLayout ti_email, ti_phone, ti_comments;
    private Spinner spin_location;
    private Button btn_enquiry;
    private APIClient apiClient;
    SpinnerLocationAdapter adapter;
    List<LocationAllModel> model;
    ArrayList<String> locationName = new ArrayList<String>();
    int location_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_enquire);

        apiClient = new APIClient();
        model = new ArrayList<LocationAllModel>();

        initUI();
        getLocationData();
    }

    private void initUI() {
        imgClose = (ImageView) findViewById(R.id.img_close);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_comments = (EditText) findViewById(R.id.ed_comments);
        ti_email = (TextInputLayout) findViewById(R.id.input_email);
        ti_phone = (TextInputLayout) findViewById(R.id.input_phone);
        ti_comments = (TextInputLayout) findViewById(R.id.input_comments);
        spin_location = (Spinner) findViewById(R.id.spin_location);
        btn_enquiry = (Button) findViewById(R.id.btn_enquiry);

        spin_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < model.size()) {
                    location_id = model.get(position).getId();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imgClose.setOnClickListener(v -> {
            finishAffinity();
            startActivity(new Intent(this, OnBoardActivity.class));
        });

        btn_enquiry.setOnClickListener(v -> {
            if (validate()) {
                enquirySign();
            }
        });
    }

    private boolean validate() {
        if (ed_email.getText().toString().isEmpty()) {
            ti_email.setError(getString(R.string.invalid_email));
            return false;
        } else {
            ti_email.setError(null);
        }
        if (ed_phone.getText().toString().isEmpty()) {
            ti_phone.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_phone.setError(null);
        }
        if (ed_comments.getText().toString().isEmpty()) {
            ti_comments.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_comments.setError(null);
        }

        if (location_id == 0) {
            Toast.makeText(this, getString(R.string.select_location_id), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                    spin_location.setAdapter(adapter);
                    spin_location.setSelection(adapter.getCount());
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

    private void enquirySign() {
        Common.showLoading(this);

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("email", ed_email.getText().toString());
            paramObject.put("location", spin_location.getSelectedItem().toString());
            paramObject.put("ukoMessage", ed_comments.getText().toString());
            paramObject.put("propertyLocationId", location_id);
            paramObject.put("phoneNumber", ed_phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> response = apiClient.getServices().enquirySignUp(paramObject.toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {

                    startActivity(new Intent(SignupEnquireActivity.this, EnquireActivity.class));
                    finish();
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String error_msg = object.getString("error");
                        Toast.makeText(SignupEnquireActivity.this, error_msg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hideLoading();
            }
        });

    }
}