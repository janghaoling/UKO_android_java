package com.mobile.uko.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.R;
import com.mobile.uko.model.PropertiesLocations;
import com.mobile.uko.model.PropertyLocationSpaces;

import java.util.List;

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.MyViewHolder> {

    private Context context;
    private List<PropertyLocationSpaces> list;

    public EnquiryAdapter(Context context, List<PropertyLocationSpaces> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_location_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PropertyLocationSpaces obj = list.get(position);
        if (obj.getPropertyLocationSpacesImages().size() > 0) {
            Glide.with(context)
                    .load(obj.getPropertyLocationSpacesImages().get(0).getImageUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.our_location).dontAnimate().error(R.drawable.our_location))
                    .into(holder.image);
        }
        holder.name.setText(obj.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout itemView;
        private ImageView image;
        private TextView name;
        private Button btn_open;

        MyViewHolder(View view) {
            super(view);
            itemView = view.findViewById(R.id.itemView);
            image = view.findViewById(R.id.imageView);
            name = view.findViewById(R.id.txt_location_list);
            btn_open = view.findViewById(R.id.btn_now_open);
            btn_open.setVisibility(View.GONE);
        }
    }
}
