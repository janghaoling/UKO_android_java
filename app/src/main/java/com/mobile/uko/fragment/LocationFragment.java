package com.mobile.uko.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.R;
import com.mobile.uko.adapter.EnquiryAdapter;
import com.mobile.uko.model.LocationIdModel;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Global;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView tv_title;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    @BindView(R.id.img_stanmore_left)
    ImageView leftArrow;
    @BindView(R.id.img_stanmore_right)
    ImageView rightArrow;
    @BindView(R.id.rv_stanmore_spaces)
    RecyclerView rv_spaces;
    @BindView(R.id.tv_hotel_title)
    TextView tv_hotel;
    @BindView(R.id.tv_hotel_distance)
    TextView tv_hotel_distance;
    @BindView(R.id.tv_train_title)
    TextView tv_train;
    @BindView(R.id.tv_train_distance)
    TextView tv_train_distance;
    @BindView(R.id.tv_dining_title)
    TextView tv_dining;
    @BindView(R.id.tv_dining_distance)
    TextView tv_dining_distance;
    @BindView(R.id.tv_home_title)
    TextView tv_home;
    @BindView(R.id.tv_home_distance)
    TextView tv_home_distance;
    @BindView(R.id.txt_stanmore_spaces)
    TextView tv_stanmore_title;

    private Context context;
    EnquiryAdapter enquiryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);

        if (Global.locationIdModel != null)
            updateUI();

        return view;
    }

    private void updateUI() {
        if (Global.locationIdModel.getPropertyLocationSpaces().get(0).getPropertyLocationSpacesImages().get(0).getImageUrl() != null) {
            Glide.with(context)
                    .load(Global.locationIdModel.getPropertyLocationSpaces().get(0).getPropertyLocationSpacesImages().get(0).getImageUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.enquiry_top).dontAnimate().error(R.drawable.enquiry_top))
                    .into(imgLocation);
        }

        tv_title.setText(Global.locationIdModel.getName());
        tv_stanmore_title.setText(Global.locationIdModel.getName());

        if (Global.locationIdModel.getPropertyLocationSpaces() != null) {
            enquiryAdapter = new EnquiryAdapter(context, Global.locationIdModel.getPropertyLocationSpaces());
            rv_spaces.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            rv_spaces.setItemAnimator(new DefaultItemAnimator());
            rv_spaces.setAdapter(enquiryAdapter);
        }

        if (Global.locationIdModel.getPropertiesNearbyLocations() != null) {
            int size = Global.locationIdModel.getPropertiesNearbyLocations().size();
            if (Global.locationIdModel.getPropertiesNearbyLocations().get(0) != null) {
                tv_hotel.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(0).getDescription());
                tv_hotel_distance.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(0).getDistance());
            }
            if (Global.locationIdModel.getPropertiesNearbyLocations().get(1) != null) {
                tv_train.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(1).getDescription());
                tv_train_distance.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(1).getDistance());
            }
            if (Global.locationIdModel.getPropertiesNearbyLocations().get(2) != null) {
                tv_dining.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(2).getDescription());
                tv_dining_distance.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(2).getDistance());
            }
            if (size > 3 && Global.locationIdModel.getPropertiesNearbyLocations().get(3) != null) {
                tv_home.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(3).getDescription());
                tv_home_distance.setText(Global.locationIdModel.getPropertiesNearbyLocations().get(3).getDistance());
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
        }
    }
}
