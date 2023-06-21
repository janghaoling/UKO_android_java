package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mobile.uko.R;
import com.mobile.uko.model.LocationIdModel;
import com.mobile.uko.model.ServerError;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.Global;
import com.mobile.uko.utils.SharedHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    private ImageView imgView;
    private Integer loc_id = 0;
    APIClient apiClient = new APIClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Global.accessToken = SharedHelper.getKey(SplashActivity.this, "access_token");
        loc_id = SharedHelper.getIntKey(SplashActivity.this, "location_id");
        Log.e(TAG, "Token in Splash:::" + Global.accessToken);
        Log.e(TAG, "Location Id is:::" + loc_id);

        logoAnimation();
        init();
    }

    private void init() {
        new Handler().postDelayed(() -> {
            String token = String.valueOf(SharedHelper.getKey(SplashActivity.this, "access_token"));
//            startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));

            if (SharedHelper.getBoolKey(SplashActivity.this, "logged_in", false)) {
                if (Common.isNetworkAvailable(SplashActivity.this)) {
                    getLocationId();
                } else {

                }
            } else {
                startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
                finish();
            }
        }, 3000);
    }

    private void logoAnimation() {
        imgView = (ImageView) findViewById(R.id.imageView);
        imgView.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imgView.setImageResource(R.drawable.logo);
                        imgView.animate().alpha(1).setDuration(1000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
    }

    private void getLocationId() {
        Call<LocationIdModel> call = apiClient.getServices().getLocationId(loc_id);
        call.enqueue(new Callback<LocationIdModel>() {
            @Override
            public void onResponse(Call<LocationIdModel> call, Response<LocationIdModel> response) {
                if (response.isSuccessful()) {
                    Global.locationIdModel = response.body();
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else {
//                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
//                    Toast.makeText(SplashActivity.this, serverError.getError(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LocationIdModel> call, Throwable t) {
                Toast.makeText(SplashActivity.this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, OnBoardActivity.class));
                finish();
            }
        });
    }
}