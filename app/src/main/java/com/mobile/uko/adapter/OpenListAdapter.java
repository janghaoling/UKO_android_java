package com.mobile.uko.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.uko.R;
import com.mobile.uko.model.Maintenance;

import java.util.List;

import static com.mobile.uko.MvpApplication.formatDateFromString;

public class OpenListAdapter extends RecyclerView.Adapter<OpenListAdapter.MyViewHolder>{

    private Context context;
    private List<Maintenance> list;

    public OpenListAdapter(Context context, List<Maintenance> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_requests_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Maintenance model = list.get(position);

        holder.tv_detail.setText(model.getNote());
        if (!model.getCreatedAt().isEmpty() && model.getCreatedAt() != null) {
            String r_date = model.getCreatedAt();
            String subDate = r_date.substring(0, 10);
            String dateAfter = formatDateFromString("yyyy-MM-dd", "dd MMM yyyy", subDate);
            holder.tv_date.setText(dateAfter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_detail, tv_date;

        MyViewHolder(View view) {
            super(view);
            tv_detail = (TextView) view.findViewById(R.id.tv_notice_title);
            tv_date = (TextView) view.findViewById(R.id.tv_notice_date);
        }
    }
}
