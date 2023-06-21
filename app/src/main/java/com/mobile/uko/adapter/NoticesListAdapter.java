package com.mobile.uko.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.uko.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mobile.uko.MvpApplication.formatDateFromString;

public class NoticesListAdapter extends RecyclerView.Adapter<NoticesListAdapter.MyViewHolder> {

    private Context context;
    private List<String> details;
    private List<String> date;

    public NoticesListAdapter(Context con, List<String> details, List<String> date) {
        this.context = con;
        this.details = details;
        this.date = date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notices_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String content = details.get(position);
        holder.tv_detail.setText(content);


        if (!date.get(position).isEmpty() && date.get(position) != null) {
            String r_date = date.get(position);
            String subDate = r_date.substring(0, 10);
            String dateAfter = formatDateFromString("yyyy-MM-dd", "dd, MMM yyyy", subDate);
            holder.tv_date.setText(dateAfter);
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
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
