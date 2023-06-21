package com.mobile.uko.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.uko.R;
import com.mobile.uko.adapter.NoticesListAdapter;
import com.mobile.uko.network.APIClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NoticesFragment extends Fragment {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rv_notices)
    RecyclerView rv_notices;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private Context context;
    ArrayList<String> details = new ArrayList<String>();
    ArrayList<String> dates = new ArrayList<String>();
    NoticesListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

        if (getArguments() != null) {
            details = getArguments().getStringArrayList("details");
            dates = getArguments().getStringArrayList("date");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);
        ButterKnife.bind(this, view);

        if (details.size() == 0) {
            rv_notices.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        } else {
            rv_notices.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            rv_notices.setLayoutManager(manager);
            adapter = new NoticesListAdapter(context, details, dates);
            rv_notices.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
