package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.mobile.uko.R;

public class OnBoardActivity extends AppCompatActivity {

    private Button btn_login, btn_signup;
    private TextView txt_explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        initUI();

    }

    private void initUI() {
        btn_login = (Button) findViewById(R.id.sign_in);
        btn_signup = (Button) findViewById(R.id.sign_up);
        txt_explore = (TextView) findViewById(R.id.tv_explore);

        btn_login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        btn_signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        txt_explore.setOnClickListener(v -> {
            startActivity(new Intent(this, ExploreActivity.class));
        });
    }

}