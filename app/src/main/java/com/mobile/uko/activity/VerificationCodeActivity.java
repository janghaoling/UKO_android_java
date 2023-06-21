package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile.uko.R;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.raycoarana.codeinputview.CodeInputView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {

    private ImageView imgClose;
    private CodeInputView codeInputView;
    private Button btn_next;
    APIClient apiClient;
    String email = "", token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        apiClient = new APIClient();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
        }

        initUI();
    }

    private void initUI() {
        imgClose = (ImageView) findViewById(R.id.img_close);
        codeInputView = (CodeInputView) findViewById(R.id.codeInputView);
        btn_next = (Button) findViewById(R.id.btn_next);

        imgClose.setOnClickListener(v -> {
            finishAffinity();
            startActivity(new Intent(VerificationCodeActivity.this, ForgotPasswordActivity.class));
        });

        btn_next.setOnClickListener(v -> {
            if (validate()) {
                verifyToken();
            }
        });
    }

    private boolean validate() {
        String code = codeInputView.getCode();
        if (code.isEmpty() || code.length() < 6) {
            codeInputView.setError("Ups! Try with other code.");
            return false;
        }
        return true;
    }

    private void verifyToken() {
        Common.showLoading(this);

        token = codeInputView.getCode();

        Call<ResponseBody> response = apiClient.getServices().verifyToken(token);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String msg = object.getString("message");
                        Toast.makeText(VerificationCodeActivity.this, msg, Toast.LENGTH_LONG).show();

                        gotoNextPage();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(VerificationCodeActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("token", token);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}