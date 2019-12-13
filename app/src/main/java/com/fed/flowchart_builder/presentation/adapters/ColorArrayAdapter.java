package com.fed.flowchart_builder.presentation.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ColorArrayAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private List<Integer> mColors;
    private int mViewId;


    public ColorArrayAdapter(@NonNull Context context, int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        mColors = new ArrayList<>(objects);
        mContext = context;
        mViewId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view;

        if (convertView == null) {
            view = View.inflate(mContext, mViewId, null);
        } else {
            view = convertView;
        }
        view.setBackgroundColor(mColors.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = View.inflate(mContext, mViewId, null);
        } else {
            view = convertView;
        }
        view.setBackgroundColor(mColors.get(position));
        return view;
    }
}
