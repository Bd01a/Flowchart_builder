package com.fed.flowchart_builder.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;

import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ChartViewHolder> {

    private List<String> mNames;
    private OnItemClickListener mOnItemClickListener;

    public ChartsAdapter(List<String> names) {
        mNames = names;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChartViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.chart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder holder, int position) {
        holder.bind(mNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public interface OnItemClickListener {
        void onClick(String chartName);

        void onLongClick(String chartName, View view);
    }

    class ChartViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        View mParentView;

        ChartViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview_chart_name);
            mParentView = itemView;

        }

        void bind(final String name) {
            mTextView.setText(name);
            mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(name);
                }
            });
            mParentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(name, mParentView);
                    return false;
                }
            });
        }
    }
}
