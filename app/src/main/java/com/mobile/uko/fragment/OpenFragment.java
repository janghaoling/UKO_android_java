package com.mobile.uko.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobile.uko.R;
import com.mobile.uko.adapter.NoticesAdapter;
import com.mobile.uko.adapter.OpenListAdapter;
import com.mobile.uko.model.Maintenance;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.model.ServerError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenFragment extends Fragment {

    private String TAG = "OpenFragment";

    @BindView(R.id.rv_opens)
    RecyclerView rv_open;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private Context context;
    APIClient apiClient = new APIClient();
    private String status = "Opened";
    List<Maintenance> maintenanceList = new ArrayList<Maintenance>();
    OpenListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        maintenanceList.clear();

        getOpenedData();
    }

    private void getOpenedData() {
        Common.showLoading(context);

        Call<List<Maintenance>> call = apiClient.getAPIClient().getMaintenanceAll(status);
        call.enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    maintenanceList = response.body();

                    if (maintenanceList.size() > 0) {
                        rv_open.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);

                        rv_open.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        rv_open.setItemAnimator(new DefaultItemAnimator());
                        adapter = new OpenListAdapter(context, maintenanceList);
                        rv_open.setAdapter(adapter);
                        Log.d(TAG, "Response list size :::" + maintenanceList.size());
                    } else {
                        rv_open.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
                } else {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    Toast.makeText(context, serverError.getError(), Toast.LENGTH_SHORT).show();
                    rv_open.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Maintenance>> call, Throwable t) {
                Common.hideLoading();
                Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                rv_open.setVisibility(View.GONE);
                tv_no_data.setVisibility(View.VISIBLE);
            }
        });
    }


}
