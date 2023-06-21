package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageView img_close;
    private EditText ed_email;
    private TextInputLayout ti_email;
    private Button btn_submit;
    APIClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        apiClient = new APIClient();
        initUI();
    }

    private void initUI() {
        img_close = (ImageView) findViewById(R.id.img_close);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ti_email = (TextInputLayout) findViewById(R.id.input_email);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        setClickListener();
    }

    private void setClickListener() {
        img_close.setOnClickListener(v -> {
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        });

        btn_submit.setOnClickListener(v -> {
            if (validate()) {
                forgotPassword();
            }
        });
    }

    private boolean validate() {
        if (ed_email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString()).matches()) {
            ti_email.setError(getString(R.string.invalid_email));
            return false;
        } else {
            ti_email.setError(null);
        }

        return true;
    }

    private void forgotPassword() {
        Common.showLoading(this);

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("email", ed_email.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> response = apiClient.getServices().forgotPassword(paramObject.toString());
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.sent_code_successfully), Toast.LENGTH_SHORT).show();

                    gotoNextPage();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }

    private void gotoNextPage() {
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", ed_email.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}