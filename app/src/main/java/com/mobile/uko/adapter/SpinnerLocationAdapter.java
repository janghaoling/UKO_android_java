package com.mobile.uko.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SpinnerLocationAdapter extends ArrayAdapter<String> {

    public SpinnerLocationAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1: count;
    }
}
