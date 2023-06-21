package com.mobile.uko.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mobile.uko.MvpApplication;
import com.mobile.uko.R;
import com.mobile.uko.activity.ExploreActivity;
import com.mobile.uko.activity.LoginActivity;
import com.mobile.uko.activity.PaymentActivity;
import com.mobile.uko.activity.SplashActivity;
import com.mobile.uko.adapter.EventsAdapter;
import com.mobile.uko.adapter.NoticesAdapter;
import com.mobile.uko.model.EventsAll;
import com.mobile.uko.model.LeaseAgreementHome;
import com.mobile.uko.model.LocationAllModel;
import com.mobile.uko.model.LocationIdModel;
import com.mobile.uko.model.ServerError;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.Global;
import com.mobile.uko.utils.SharedHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    @BindView(R.id.rel_top_bar)
    RelativeLayout rl_top_banner;
    @BindView(R.id.txt_title)
    TextView tv_banner_title;
    @BindView(R.id.txt_description)
    TextView tv_banner_detail;
    @BindView(R.id.card_request)
    CardView card_request;
    @BindView(R.id.rv_notices)
    RecyclerView rv_notices;
    @BindView(R.id.img_resident_top)
    ImageView residentImage;
    @BindView(R.id.txt_resident_top)
    TextView tv_resident_text;
    @BindView(R.id.rv_events)
    RecyclerView rv_events;
    @BindView(R.id.tv_no_events)
    TextView tv_no_events;
    @BindView(R.id.txt_error_notice)
    TextView tv_no_notice;
    @BindView(R.id.linear_no_notice)
    LinearLayout li_no_notice;

    private Context context;
    APIClient apiClient;
    ArrayList<String> notices = new ArrayList<String>();
    ArrayList<String> noticesDate = new ArrayList<String>();
    NoticesAdapter noticeAdapter;
    EventsAdapter eventsAdapter;
    List<EventsAll> eventsAllList = new ArrayList<EventsAll>();
    LeaseAgreementHome agreementHome = new LeaseAgreementHome();
    private Integer locationId = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        apiClient = new APIClient();

        locationId = SharedHelper.getIntKey(context, "location_id");
        Log.e(TAG, "Location Id in Home Fragment:::::::::" + locationId + ":::" + Global.userStatus);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_home, container, false);
        ButterKnife.bind(this, view);

        initUI();

        return view;
    }

    private void initUI() {
        if (Global.userStatus.equalsIgnoreCase("V")) {
            rl_top_banner.setVisibility(View.VISIBLE);
            card_request.setVisibility(View.GONE);
            tv_banner_title.setText(getString(R.string.profile_verified));
            tv_banner_detail.setText(getString(R.string.sign_lease_agreement));
        } else if (Global.userStatus.equalsIgnoreCase("A")) {
            rl_top_banner.setVisibility(View.VISIBLE);
            card_request.setVisibility(View.VISIBLE);
            tv_banner_title.setText(getString(R.string.lease_agreement_accepted));
            tv_banner_detail.setText(getString(R.string.proceed_payment));
        } else {
            rl_top_banner.setVisibility(View.GONE);
            card_request.setVisibility(View.GONE);
        }

//        if (Global.locationIdModel != null)
//            updateUI();
    }

    private void updateUI() {
        if (Global.locationIdModel != null) {
            Glide.with(context)
                    .load(Global.locationIdModel.getPropertiesLocationImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.enquiry_top).dontAnimate().error(R.drawable.enquiry_top))
                    .into(residentImage);
            tv_resident_text.setText(Global.locationIdModel.getDescription());
        }
    }

    @OnClick({R.id.rel_top_bar, R.id.txt_view_all, R.id.img_resident_top, R.id.linear_request, R.id.linear_concierge})
    public void onViewClicked(View view) {
        FragmentManager manager = getFragmentManager();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.rel_top_bar:
//                BottomResidentFragment bottomResidentFragment = new BottomResidentFragment();
//                bottomResidentFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), bottomResidentFragment.getTag());

                if (Global.userStatus.equalsIgnoreCase("A")) {
                    startActivity(new Intent(context, PaymentActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                } else if (Global.userStatus.equalsIgnoreCase("V")) {
                    BottomResidentFragment bottomResidentFragment = new BottomResidentFragment();
                    bottomResidentFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomResidentFragment.getTag());
                }
                break;
            case R.id.txt_view_all:
                NoticesFragment noticesFragment = new NoticesFragment();
                args.putStringArrayList("details", notices);
                args.putStringArrayList("date", noticesDate);
                noticesFragment.setArguments(args);
                manager.beginTransaction()
                        .replace(R.id.main_container, noticesFragment, noticesFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.img_resident_top:
                LocationFragment locationFragment = new LocationFragment();
                manager.beginTransaction()
                        .replace(R.id.main_container, locationFragment, locationFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.linear_request:
                RequestsFragment requestsFragment = new RequestsFragment();
                manager.beginTransaction()
                        .replace(R.id.main_container, requestsFragment, requestsFragment.getTag())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.linear_concierge:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        rv_notices.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_notices.setItemAnimator(new DefaultItemAnimator());
        noticeAdapter = new NoticesAdapter(context, notices);
        rv_notices.setAdapter(noticeAdapter);
        notices.clear();

        eventsAllList.clear();

        if (locationId != 0)
            getLocation();

//        getLeaseDetails();
        getNoticeAll();
        getEventsAll();
    }

    private void getLocation() {
        Common.showLoading(context);

        Call<LocationIdModel> call = apiClient.getServices().getLocationId(locationId);
        call.enqueue(new Callback<LocationIdModel>() {
            @Override
            public void onResponse(Call<LocationIdModel> call, Response<LocationIdModel> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    Global.locationIdModel = response.body();

                    updateUI();
                } else {
                    Common.hideLoading();
                    Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationIdModel> call, Throwable t) {
                Common.hideLoading();
                Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLeaseDetails() {
        Common.showLoading(context);

        Call<LeaseAgreementHome> call = apiClient.getAPIClient().getAgreementHome();
        call.enqueue(new Callback<LeaseAgreementHome>() {
            @Override
            public void onResponse(Call<LeaseAgreementHome> call, Response<LeaseAgreementHome> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    agreementHome = response.body();
                    Global.userStatus = agreementHome.getUserStatus();

                    updateUI();
                } else {
                    Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeaseAgreementHome> call, Throwable t) {
                Common.hideLoading();
                Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNoticeAll() {
        Call<ResponseBody> call = apiClient.getServices().getNoticesAll();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray array = new JSONArray(response.body().string());
                        for (int i = 0; i < array.length(); i++) {
                            String object = array.getJSONObject(i).getString("noticeDescription");
                            String date = array.getJSONObject(i).getString("createdAt");
                            notices.add(object);
                            noticesDate.add(date);
                        }

                        rv_notices.setVisibility(View.VISIBLE);
                        li_no_notice.setVisibility(View.GONE);
                        noticeAdapter.notifyDataSetChanged();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
//                    Toast.makeText(context, serverError.getError(), Toast.LENGTH_SHORT).show();
                    rv_notices.setVisibility(View.GONE);
                    li_no_notice.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                rv_notices.setVisibility(View.GONE);
                li_no_notice.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getEventsAll() {
        Call<List<EventsAll>> call = apiClient.getAPIClient().getEventsAll();
        call.enqueue(new Callback<List<EventsAll>>() {
            @Override
            public void onResponse(Call<List<EventsAll>> call, Response<List<EventsAll>> response) {
                if (response.isSuccessful()) {
                    eventsAllList = response.body();

                    rv_events.setVisibility(View.VISIBLE);
                    tv_no_events.setVisibility(View.GONE);

                    rv_events.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    rv_events.setItemAnimator(new DefaultItemAnimator());
                    eventsAdapter = new EventsAdapter(context, eventsAllList);
                    rv_events.setAdapter(eventsAdapter);
//                    eventsAdapter.notifyDataSetChanged();
                } else {
//                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
//                    Toast.makeText(context, serverError.getError(), Toast.LENGTH_SHORT).show();

                    rv_events.setVisibility(View.GONE);
                    tv_no_events.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<EventsAll>> call, Throwable t) {
                t.printStackTrace();
                rv_events.setVisibility(View.GONE);
                tv_no_events.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
