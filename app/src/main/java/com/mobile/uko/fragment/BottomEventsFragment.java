package com.mobile.uko.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.mobile.uko.R;
import com.mobile.uko.model.EventStatusUpdate;
import com.mobile.uko.model.EventsIdModel;
import com.mobile.uko.model.ServerError;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomEventsFragment extends BottomSheetDialogFragment {

    private String TAG = "BottomEventsFragment";

    @BindView(R.id.txt_date)
    TextView tv_date;
    @BindView(R.id.txt_month)
    TextView tv_month;
    @BindView(R.id.txt_title)
    TextView tv_title;
    @BindView(R.id.btn_going)
    Button btnGoing;
    @BindView(R.id.btn_not_going)
    Button btnNotGoing;
    @BindView(R.id.txt_timing)
    TextView tv_timing;
    @BindView(R.id.txt_mapping)
    TextView tv_mapping;
    @BindView(R.id.txt_details_title)
    TextView tv_detailsTitle;
    @BindView(R.id.txt_details)
    TextView tv_details;

    private Context context;
    private APIClient apiClient;
    private Integer eventId = 0;
    EventsIdModel idModel = new EventsIdModel();

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View view = View.inflate(getContext(), R.layout.bottom_events_fragment, null);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);

        context = getContext();
        apiClient = new APIClient();

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = getArguments().getInt("id");
            Log.d(TAG, "Selected item id is::::" + eventId);

            getEventsDetail(eventId);
        }
    }

    private void getEventsDetail(int id) {
        Call<EventsIdModel> call = apiClient.getAPIClient().getEventsId(id);
        call.enqueue(new Callback<EventsIdModel>() {
            @Override
            public void onResponse(Call<EventsIdModel> call, Response<EventsIdModel> response) {
                if (response.isSuccessful()) {
                    idModel = response.body();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        updateUI();
                    }
                } else {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    Toast.makeText(context, serverError.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventsIdModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUI() {
        if (idModel.getEventStatus().equalsIgnoreCase("Going")) {
            btnGoing.setBackgroundColor(context.getColor(R.color.black));
            btnGoing.setTextColor(context.getColor(R.color.white));
        } else {
            btnNotGoing.setBackgroundColor(context.getColor(R.color.black));
            btnNotGoing.setTextColor(context.getColor(R.color.white));
        }
        tv_title.setText(idModel.getTitle());
        tv_details.setText(idModel.getDescription());
        tv_mapping.setText(idModel.getVeneue());

        String venueDate = idModel.getVenueDateTime();
        String s_time = venueDate.substring(11, 16);
        String updatedTime = changeTime(s_time);

        String venueToDate = idModel.getVenueToDateTime();
        String e_time = venueToDate.substring(11,16);
        String updatedToTime = changeTime(e_time);
        tv_timing.setText(updatedTime + " - " + updatedToTime);

        String dt = venueDate.substring(0, 10);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String day = "", week = "";
        try {
            Date date = format1.parse(dt);
            DateFormat format2 = new SimpleDateFormat("dd");
            day = format2.format(date);
            DateFormat format3 = new SimpleDateFormat("MMM");
            week = format3.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_date.setText(day);
        tv_month.setText(week.toUpperCase());
        Log.d(TAG, "Venue DateTime::::" + day + ":::" + week);
    }

    private String changeTime(String time) {
        String u_time = "";
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = format1.parse(time);
            SimpleDateFormat format2 = new SimpleDateFormat("h:mm aa");
            u_time = format2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return u_time;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.btn_going, R.id.btn_not_going})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_going:
                btnGoing.setBackgroundColor(context.getColor(R.color.black));
                btnGoing.setTextColor(context.getColor(R.color.white));
                btnNotGoing.setBackgroundColor(context.getColor(R.color.btn_secondary_color));
                btnNotGoing.setTextColor(context.getColor(R.color.black));
                statusUpdate("Going");
                break;
            case R.id.btn_not_going:
                btnNotGoing.setBackgroundColor(context.getColor(R.color.black));
                btnNotGoing.setTextColor(context.getColor(R.color.white));
                btnGoing.setBackgroundColor(context.getColor(R.color.btn_secondary_color));
                btnGoing.setTextColor(context.getColor(R.color.black));
                statusUpdate("Not Going");
                break;
        }
    }

    private void statusUpdate(String status) {
        Common.showLoading(context);
        EventStatusUpdate statusUpdate = new EventStatusUpdate(status, eventId);
        Call<EventStatusUpdate> call = apiClient.getAPIClient().updateEventStatus(statusUpdate);
        call.enqueue(new Callback<EventStatusUpdate>() {
            @Override
            public void onResponse(Call<EventStatusUpdate> call, Response<EventStatusUpdate> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    Toast.makeText(context, getString(R.string.status_update_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    Toast.makeText(context, serverError.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventStatusUpdate> call, Throwable t) {
                Common.hideLoading();
                Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
