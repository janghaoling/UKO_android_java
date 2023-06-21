package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.mobile.uko.R;
import com.mobile.uko.model.Question;
import com.mobile.uko.model.RegisterModel;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFinal extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_close;
    private EditText ed_how_long_stay, ed_when_move, ed_how_hear, ed_why_uko;
    private TextInputLayout ti_stay, ti_move, ti_hear, ti_why;
    private Button btn_signup;
    RegisterModel registerModel;
    APIClient apiClient;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_final);

        context = this;
        apiClient = new APIClient();
        registerModel = new RegisterModel();

        if (getIntent().getExtras() != null) {
            registerModel = (RegisterModel) getIntent().getSerializableExtra("sign_model");
        }

        initUI();
    }

    private void initUI() {
        img_close = (ImageView) findViewById(R.id.img_close);
        ed_how_long_stay = (EditText) findViewById(R.id.ed_how_long_stay);
        ed_when_move = (EditText) findViewById(R.id.ed_when_move);
        ed_how_hear = (EditText) findViewById(R.id.ed_how_hear);
        ed_why_uko = (EditText) findViewById(R.id.ed_why_uko);
        btn_signup = (Button) findViewById(R.id.btn_singup);

        ti_stay = (TextInputLayout) findViewById(R.id.input_stay);
        ti_move = (TextInputLayout) findViewById(R.id.input_move);
        ti_hear = (TextInputLayout) findViewById(R.id.input_hear);
        ti_why = (TextInputLayout) findViewById(R.id.input_why);

        img_close.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
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
            case R.id.btn_singup:
                if (validate()) {
                    addValueToModel();
                }
                break;
        }
    }

    private boolean validate() {
        if (ed_how_long_stay.getText().toString().isEmpty()) {
            ti_stay.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_stay.setError(null);
        }

        if (ed_when_move.getText().toString().isEmpty()) {
            ti_move.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_move.setError(null);
        }

        if (ed_how_hear.getText().toString().isEmpty()) {
            ti_hear.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_hear.setError(null);
        }

        if (ed_why_uko.getText().toString().isEmpty()) {
            ti_why.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_why.setError(null);
        }

        return true;
    }

    private void addValueToModel() {
        List<Question> qList = new ArrayList<Question>();
        Question q_1 = new Question();
        q_1.setQuestion(getString(R.string.how_about_stay));
        q_1.setAnswer(ed_how_long_stay.getText().toString());
        qList.add(q_1);

        Question q_2 = new Question();
        q_2.setQuestion(getString(R.string.when_you_move));
        q_2.setAnswer(ed_when_move.getText().toString());
        qList.add(q_2);

        Question q_3 = new Question();
        q_3.setQuestion(getString(R.string.how_hear_about_us));
        q_3.setAnswer(ed_how_hear.getText().toString());
        qList.add(q_3);

        Question q_4 = new Question();
        q_4.setQuestion(getString(R.string.why_uko));
        q_4.setAnswer(ed_why_uko.getText().toString());
        qList.add(q_4);

        registerModel.setQuestions(qList);

        signup();
    }

    private void signup() {
        Common.showLoading(this);

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String json = gson.toJson(registerModel);
//        Log.d("11111111111111111111", "Register request param:::::" + json);

        Call<ResponseBody> response = apiClient.getServices().register(registerModel);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();

                if (response.isSuccessful()) {
                    Toast.makeText(context, getString(R.string.you_have_been_successfully_registered), Toast.LENGTH_SHORT).show();
                    SharedHelper.putKey(context, "logged_in", true);
                    finishAffinity();
                    startActivity(new Intent(context, ThanksJoinActivity.class));
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(SignupFinal.this, object.getString("error"), Toast.LENGTH_LONG).show();
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