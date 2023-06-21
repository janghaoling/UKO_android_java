package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.model.RegisterModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_close;
    private EditText edt_email, edt_password, edt_conf_password;
    private TextInputLayout ti_email, ti_password, ti_confirm_password;
    private Button btn_complete;
    private TextView txt_by_sign, txt_goto_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
    }

    private void initUI() {
        img_close = (ImageView) findViewById(R.id.img_close);
        edt_email = (EditText) findViewById(R.id.ed_email);
        edt_password = (EditText) findViewById(R.id.ed_password);
        edt_conf_password = (EditText) findViewById(R.id.ed_conf_password);
        btn_complete = (Button) findViewById(R.id.btn_complete_profile);
        txt_by_sign = (TextView) findViewById(R.id.tv_by_signing);
        txt_goto_login = (TextView) findViewById(R.id.tv_goto_login);
        ti_email = (TextInputLayout) findViewById(R.id.input_email);
        ti_password = (TextInputLayout) findViewById(R.id.input_password);
        ti_confirm_password = (TextInputLayout) findViewById(R.id.input_confirm_password);

        txt_by_sign.setText(Html.fromHtml(getString(R.string.by_signing_up) + " " + "<font color=\"#000000\">" + getString(R.string.terms_conditions) + "</font>" + "  "
                + getString(R.string.standard_operator)));

        img_close.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        txt_by_sign.setOnClickListener(this);
        txt_goto_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finishAffinity();
                startActivity(new Intent(this, OnBoardActivity.class));
                break;
            case R.id.btn_complete_profile:
                if (validate()) {
                    gotoNextPage();
                }
                break;
            case R.id.tv_by_signing:
                Toast.makeText(this, "Go To terms & conditions!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_goto_login:
                finishAffinity();
                startActivity(new Intent(this, LoginActivity.class));
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

        if (!edt_conf_password.getText().toString().equalsIgnoreCase(edt_password.getText().toString())) {
            ti_confirm_password.setError(getString(R.string.invalid_conf_password));
            return false;
        } else {
            ti_confirm_password.setError(null);
        }
        return true;
    }

    private void gotoNextPage() {
        RegisterModel model = new RegisterModel();
        model.setEmail(edt_email.getText().toString());
        model.setPassword(edt_password.getText().toString());

        Intent intent = new Intent(SignUpActivity.this, CompleteProfileActivity.class);
        intent.putExtra("sign_model", model);
        startActivity(intent);
    }
}