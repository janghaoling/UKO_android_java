package com.mobile.uko.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mobile.uko.activity.EnquireActivity;
import com.mobile.uko.model.PropertiesLocations;
import com.mobile.uko.utils.DownloadImageTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OurLocationAdapter extends RecyclerView.Adapter<OurLocationAdapter.MyViewHolder> {

    private Context context;
    private List<PropertiesLocations> list;
    private onItemClick mCallback;

    public OurLocationAdapter(Context context, List<PropertiesLocations> list, onItemClick listener) {
        this.context = context;
        this.list = list;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_location_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PropertiesLocations obj = list.get(position);
        if (obj != null) {
            Glide.with(context)
                    .load(obj.getPropertiesLocationImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.our_location).dontAnimate().error(R.drawable.our_location))
                    .into(holder.image);

            holder.name.setText(obj.getName());
        }

        holder.btn_open.setOnClickListener(v -> {
            if (obj != null)
                mCallback.onClick(obj.getId());
        });
        holder.itemView.setOnClickListener(v -> {
            if (obj != null)
                mCallback.onClick(obj.getId());
        });
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
        }
    }

    public interface onItemClick {
        void onClick(int id);
    }
}
