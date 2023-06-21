package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPasswordActivity extends AppCompatActivity {

    private ImageView imgClose;
    private TextInputLayout ti_conf_password;
    private EditText ed_conf_password;
    private Button btn_confirm;
    private TextView tv_security;

    APIClient apiClient;
    String email = "", token = "", password = "", flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        apiClient = new APIClient();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            token = bundle.getString("token");
            password = bundle.getString("password");
            flag = bundle.getString("flag");
        }

        initUI();
    }

    private void initUI() {
        imgClose = (ImageView) findViewById(R.id.img_close);
        ti_conf_password = (TextInputLayout) findViewById(R.id.input_conf_password);
        ed_conf_password = (EditText) findViewById(R.id.ed_conf_password);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_security = (TextView) findViewById(R.id.txt_security);

        if (flag.equalsIgnoreCase("profile")) {
            imgClose.setImageResource(R.drawable.ic_arrow_back);
            tv_security.setVisibility(View.VISIBLE);
        } else {
            imgClose.setImageResource(R.drawable.ic_close);
            tv_security.setVisibility(View.GONE);
        }

        imgClose.setOnClickListener(v -> {
            if (flag.equalsIgnoreCase("profile")) {
                onBackPressed();
            } else {
                finishAffinity();
                startActivity(new Intent(ConfirmPasswordActivity.this, ChangePasswordActivity.class));
            }
        });

        btn_confirm.setOnClickListener(v -> {
            if (validate())
                setNewPassword();
        });
    }

    private boolean validate() {
        String c_password = ed_conf_password.getText().toString();
        if (c_password.isEmpty() || c_password.length() < 6 || !c_password.equalsIgnoreCase(password)) {
            ti_conf_password.setError(getString(R.string.invalid_password));
            return false;
        } else {
            ti_conf_password.setError(null);
        }
        return true;
    }

    private void setNewPassword() {
        Common.showLoading(this);

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("email", email);
            paramObject.put("token", token);
            paramObject.put("password", ed_conf_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> response = apiClient.getServices().resetPassword(paramObject.toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    Toast.makeText(ConfirmPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                    gotoNextPage();
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(ConfirmPasswordActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
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

    private void gotoNextPage() {
        SharedHelper.putKey(this, "user_email", email);

        finishAffinity();
        startActivity(new Intent(this, LoginActivity.class));
    }
}