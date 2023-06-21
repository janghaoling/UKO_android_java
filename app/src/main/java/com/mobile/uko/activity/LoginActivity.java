package com.mobile.uko.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mobile.uko.MvpApplication;
import com.mobile.uko.R;
import com.mobile.uko.model.LoginToken;
import com.mobile.uko.model.ServerError;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.Global;
import com.mobile.uko.utils.SharedHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginToken> {

    private ImageView img_close;
    private EditText edt_email, edt_password;
    private TextView txt_forgot_password;
    private Button btn_login;
    private LinearLayout linear_explore;
    private TextInputLayout ti_email, ti_password;
    APIClient apiClient;
    private String tag = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiClient = new APIClient();

        initUI();
    }

    private void initUI() {
        img_close = (ImageView) findViewById(R.id.img_close);
        edt_email = (EditText) findViewById(R.id.ed_email);
        edt_password = (EditText) findViewById(R.id.ed_password);
        txt_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        linear_explore = (LinearLayout) findViewById(R.id.linear_explore);
        ti_email = (TextInputLayout) findViewById(R.id.input_email);
        ti_password = (TextInputLayout) findViewById(R.id.input_password);

//        String email = String.valueOf(SharedHelper.getKey(this, "user_email"));
//        if (!email.isEmpty())
//            edt_email.setText(email);

        img_close.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        linear_explore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                startActivity(new Intent(this, OnBoardActivity.class));
                finish();
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                finish();
                break;
            case R.id.btn_login:
                if (validate()) {
                    login();
                }
                break;
            case R.id.linear_explore:
                startActivity(new Intent(this, ExploreActivity.class));
                finish();
                break;

        }
    }

    private boolean validate() {
        if (edt_email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
            ti_email.setError(getString(R.string.invalid_email));
            return false;
        } else {
            ti_email.setError(null);
        }

        if (edt_password.getText().toString().isEmpty() || edt_password.getText().length() < 6) {
            ti_password.setError(getString(R.string.invalid_password));
            return false;
        } else {
            ti_password.setError(null);
        }

        return true;
    }

    private void login() {
        Common.showLoading(this);

        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        Log.d(tag, "Login request params:::::" + email + ":::" + password);

        final LoginToken login = new LoginToken(email, password);
        Call<LoginToken> response = apiClient.getServices().login(login);
        response.enqueue(this);
    }

    @Override
    public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
        Common.hideLoading();

        if (response.isSuccessful()) {
            LoginToken loginToken = response.body();

            String token = loginToken.getToken();
            String isProfileVerified = loginToken.getIsProfileVerified();
            String isProfileApproved = loginToken.getIsProfileApproved();
            int location_id = loginToken.getPropertyLocationId();
            String userStatus = loginToken.getUserStatus();
            Log.d(tag, "Access Token in Login:::::" + token);

            SharedHelper.putKey(this, "access_token", token);
            SharedHelper.putKey(this, "logged_in", true);
            SharedHelper.putKey(this, "location_id", location_id);
            SharedHelper.putKey(this, "user_status", userStatus);
            Global.accessToken = SharedHelper.getKey(this, "access_token");
            Global.userStatus = SharedHelper.getKey(this, "user_status");

//            if (isProfileVerified.equalsIgnoreCase("Y")) {
//                SharedHelper.putKey(this, "profile_verified", true);
//            } else {
//                SharedHelper.putKey(this, "profile_verified", false);
//            }
//
//            if (isProfileApproved.equalsIgnoreCase("Y")) {
//                SharedHelper.putKey(this, "profile_approved", true);
//            } else {
//                SharedHelper.putKey(this, "profile_approved", false);
//            }

            checkActivity();
        } else {

//            try {
//                JSONObject object = new JSONObject(response.errorBody().string());
//                String error_msg = object.getString("error");
//                if (error_msg.equalsIgnoreCase("User is not verified")) {
//                    showDialog();
//                } else {
//                    Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
            if (serverError.getError().contains("User is not verified")) {
                showDialog();
            } else {
                Toast.makeText(LoginActivity.this, serverError.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<LoginToken> call, Throwable t) {
        Common.hideLoading();
    }

    private void showDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.profile_not_verified_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        dialogView.findViewById(R.id.btn_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void checkActivity() {
        if (Global.userStatus.equalsIgnoreCase("A") || Global.userStatus.equalsIgnoreCase("V") || Global.userStatus.equalsIgnoreCase("R")) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
//        if (SharedHelper.getBoolKey(this, "profile_verified", false) && SharedHelper.getBoolKey(this, "profile_approved", false)) {
//            finishAffinity();
//            startActivity(new Intent(this, HomeActivity.class));
//        }
//
//        if (SharedHelper.getBoolKey(this, "profile_verified", false) && !SharedHelper.getBoolKey(this, "profile_approved", false)) {
//            finishAffinity();
//            startActivity(new Intent(this, HomeActivity.class));
//        }
    }
}