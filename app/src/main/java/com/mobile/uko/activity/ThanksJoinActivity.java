package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.mobile.uko.R;

public class ThanksJoinActivity extends AppCompatActivity {

    private Button btn_okay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_join);

        initUI();
    }

    private void initUI() {
        btn_okay = (Button) findViewById(R.id.btn_okay);
        btn_okay.setOnClickListener(v -> {
            finishAffinity();
            startActivity(new Intent(ThanksJoinActivity.this, LoginActivity.class));
        });
    }
}