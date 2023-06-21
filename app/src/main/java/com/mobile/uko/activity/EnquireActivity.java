package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.R;
import com.mobile.uko.adapter.EnquiryAdapter;
import com.mobile.uko.adapter.OurLocationAdapter;
import com.mobile.uko.model.LocationIdModel;
import com.mobile.uko.model.PropertiesNearbyLocations;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnquireActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBack, img_location, managerAvatar;
    private Button btn_signup, btn_enquire;
    private TextView tv_hotel, tv_hotel_distance, tv_train, tv_train_distance, tv_dining, tv_dining_distance, tv_home, tv_home_distance,
            tv_topText, tv_stanmore_title, tv_manager_name, tv_manager_description;
    private RecyclerView rv_spaces;
    private int location_id;
    private APIClient apiClient;
    LocationIdModel model = new LocationIdModel();
    EnquiryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquire);

        Bundle bundle = getIntent().getExtras();
        location_id = bundle.getInt("id");

        apiClient = new APIClient();
        initUI();
        getPropertyLocations();
    }

    private void initUI() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        img_location = (ImageView) findViewById(R.id.img_location);
        btn_signup = (Button) findViewById(R.id.enquire_signup);
        btn_enquire = (Button) findViewById(R.id.enquire);
        tv_hotel = (TextView) findViewById(R.id.tv_hotel_title);
        tv_hotel_distance = (TextView) findViewById(R.id.tv_hotel_distance);
        tv_train = (TextView) findViewById(R.id.tv_train_title);
        tv_train_distance = (TextView) findViewById(R.id.tv_train_distance);
        tv_dining = (TextView) findViewById(R.id.tv_dining_title);
        tv_dining_distance = (TextView) findViewById(R.id.tv_dining_distance);
        tv_home = (TextView) findViewById(R.id.tv_home_title);
        tv_home_distance = (TextView) findViewById(R.id.tv_home_distance);
        rv_spaces = (RecyclerView) findViewById(R.id.rv_stanmore_spaces);
        tv_topText = (TextView) findViewById(R.id.txt_stanmore);
        tv_stanmore_title = (TextView) findViewById(R.id.txt_stanmore_spaces);
        tv_manager_name = (TextView) findViewById(R.id.tv_manager_name);
        tv_manager_description = (TextView) findViewById(R.id.tv_manager_description);
        managerAvatar = (ImageView) findViewById(R.id.img_manager);

        imgBack.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_enquire.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finishAffinity();
                startActivity(new Intent(EnquireActivity.this, ExploreActivity.class));
                break;
            case R.id.enquire_signup:
                startActivity(new Intent(EnquireActivity.this, SignUpActivity.class));
                break;
            case R.id.enquire:
                startActivity(new Intent(EnquireActivity.this, SignupEnquireActivity.class));
                break;
        }
    }

    private void getPropertyLocations() {
        Common.showLoading(this);

        Call<LocationIdModel> response = apiClient.getServices().getLocationId(location_id);
        response.enqueue(new Callback<LocationIdModel>() {
            @Override
            public void onResponse(Call<LocationIdModel> call, Response<LocationIdModel> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    model = response.body();

                    updateUI(model);
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(EnquireActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationIdModel> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }

    private void updateUI(LocationIdModel model) {
        if (model != null) {
            tv_topText.setText(model.getDescription());
            tv_stanmore_title.setText(model.getName());

            if (model.getPropertyManager() != null) {
                if (model.getPropertyManager().getUserImages().size() > 0) {
                    Glide.with(this)
                            .load(model.getPropertyManager().getUserImages().get(0))
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_default_avatar).dontAnimate().error(R.drawable.ic_default_avatar))
                            .into(managerAvatar);
                }
                tv_manager_name.setText(model.getPropertyManager().getFirstName() + " " + model.getPropertyManager().getLastName());
            }

            Glide.with(this)
                    .load(model.getPropertiesLocationImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.enquiry_top).dontAnimate().error(R.drawable.enquiry_top))
                    .into(img_location);

            adapter = new EnquiryAdapter(this, model.getPropertyLocationSpaces());
            rv_spaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rv_spaces.setItemAnimator(new DefaultItemAnimator());
            rv_spaces.setAdapter(adapter);

            int size = model.getPropertiesNearbyLocations().size();
            if (model.getPropertiesNearbyLocations().get(0) != null) {
                tv_hotel.setText(model.getPropertiesNearbyLocations().get(0).getDescription());
                tv_hotel_distance.setText(model.getPropertiesNearbyLocations().get(0).getDistance());
            }
            if (model.getPropertiesNearbyLocations().get(1) != null) {
                tv_train.setText(model.getPropertiesNearbyLocations().get(1).getDescription());
                tv_train_distance.setText(model.getPropertiesNearbyLocations().get(1).getDistance());
            }
            if (model.getPropertiesNearbyLocations().get(2) != null) {
                tv_dining.setText(model.getPropertiesNearbyLocations().get(2).getDescription());
                tv_dining_distance.setText(model.getPropertiesNearbyLocations().get(2).getDistance());
            }
            if (size > 3 && model.getPropertiesNearbyLocations().get(3) != null) {
                tv_home.setText(model.getPropertiesNearbyLocations().get(3).getDescription());
                tv_home_distance.setText(model.getPropertiesNearbyLocations().get(3).getDistance());
            }
        }
    }
}