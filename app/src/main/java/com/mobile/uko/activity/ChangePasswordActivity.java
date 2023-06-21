package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView imgClose;
    private TextInputLayout ti_password;
    private EditText ed_password;
    private Button btn_next;
    private TextView tv_security;
    String email = "", token = "";
    String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            token = bundle.getString("token");
            flag = bundle.getString("flag");
        }

        initUI();
    }

    private void initUI() {
        imgClose = (ImageView) findViewById(R.id.img_close);
        ti_password = (TextInputLayout) findViewById(R.id.input_password);
        ed_password = (EditText) findViewById(R.id.ed_password);
        btn_next = (Button) findViewById(R.id.btn_change);
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
                startActivity(new Intent(ChangePasswordActivity.this, VerificationCodeActivity.class));
            }
        });

        btn_next.setOnClickListener(v -> {
            if (validate()) {
                gotoNextPage();
            }
        });
    }

    private boolean validate() {
        if (ed_password.getText().toString().isEmpty() || ed_password.getText().length() < 6) {
            ti_password.setError(getString(R.string.invalid_password));
            return false;
        } else {
            ti_password.setError(null);
        }
        return true;
    }

    private void gotoNextPage() {
        Intent intent = new Intent(this, ConfirmPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("token", token);
        bundle.putString("password", ed_password.getText().toString());
        if (flag.equalsIgnoreCase("profile"))
            bundle.putString("flag", "profile");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}