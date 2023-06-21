package com.mobile.uko.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.R;
import com.mobile.uko.fragment.BottomEventsFragment;
import com.mobile.uko.fragment.BottomResidentFragment;
import com.mobile.uko.model.EventsAll;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private Context context;
    private List<EventsAll> list;
    private onItemClick mCallback;

    public EventsAdapter(Context context, List<EventsAll> list) {
        this.context = context;
        this.list = list;
//        this.mCallback = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_events_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventsAll model = list.get(position);

//            Glide.with(context)
//                    .load(model.getEventImage())
//                    .apply(RequestOptions.placeholderOf(R.drawable.err_blank).dontAnimate().error(R.drawable.err_blank))
//                    .into(holder.imageView);
        if (model.getEventImage() != null && !model.getEventImage().isEmpty())
            Picasso.get().load(model.getEventImage()).into(holder.imageView);

        if (!model.getVenueDateTime().isEmpty()) {
            String dateTime = model.getVenueDateTime();
            String strDate = dateTime.substring(0, 10);
            String strTime = dateTime.substring(11, 16);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Boolean isCurrent = false;
            try {
                date = format.parse(strDate);
                isCurrent = isDateInCurrentWeek(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            String realTime = "";
            try {
                Date date1 = format1.parse(strTime);
                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm aa");
                realTime = format2.format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String dayOfWeek = new SimpleDateFormat("EEEE").format(date);

            if (isCurrent) {
                holder.tv_date.setText("THIS " + dayOfWeek.toUpperCase());
            } else {
                holder.tv_date.setText("NEXT " + dayOfWeek.toUpperCase());
            }

            holder.tv_title.setText(model.getTitle());
            holder.tv_time.setText(realTime);
        }

        holder.mainLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", model.getId());
            BottomEventsFragment bottomEventsFragment = new BottomEventsFragment();
            bottomEventsFragment.setArguments(bundle);
            bottomEventsFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), bottomEventsFragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onItemClick {
        void onClick(int id);
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tv_title, tv_date, tv_time;
        private LinearLayout mainLayout;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            tv_title = (TextView) view.findViewById(R.id.txt_title);
            tv_date = (TextView) view.findViewById(R.id.txt_date);
            tv_time = (TextView) view.findViewById(R.id.txt_time);
            mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        }
    }
}
