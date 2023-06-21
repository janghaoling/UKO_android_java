package com.mobile.uko.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.uko.R;

import java.util.ArrayList;
import java.util.List;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.MyViewHolder> {

    private Context context;
    private List<String> list;

    public NoticesAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String msg = list.get(position);
        if (msg != null)
            holder.tv_notice.setText(msg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_notice;

        MyViewHolder(View view) {
            super(view);
            tv_notice = (TextView) view.findViewById(R.id.txt_description);
        }
    }
}
