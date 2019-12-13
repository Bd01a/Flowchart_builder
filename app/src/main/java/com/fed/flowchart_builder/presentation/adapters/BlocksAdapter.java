package com.fed.flowchart_builder.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.BlockDescription;

import java.util.Arrays;
import java.util.List;

public class BlocksAdapter extends RecyclerView.Adapter<BlocksAdapter.BlockViewHolder> {

    private List<BlockDescription> mBlocks;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public BlocksAdapter(BlockDescription[] blocks, Context context) {
        mContext = context;
        mBlocks = Arrays.asList(blocks);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlockViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blocks_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, final int position) {
        holder.bind(mBlocks.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(mBlocks.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mBlocks.size();
    }

    public interface OnItemClickListener {
        void onClick(BlockDescription blockDescription);
    }

    class BlockViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mTextView;

        BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.imageview_icon);
            mTextView = itemView.findViewById(R.id.textview_label);
        }

        void bind(BlockDescription block) {
            mIcon.setImageDrawable(mContext.getResources().getDrawable(block.getIcon()));
            mTextView.setText(mContext.getString(block.getName()));
        }

    }
}
