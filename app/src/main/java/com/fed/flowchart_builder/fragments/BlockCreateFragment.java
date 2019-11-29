package com.fed.flowchart_builder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.FlowChartViewGroup;
import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.adapters.BlocksAdapter;
import com.fed.flowchart_builder.blocks.SimpleBlockView;
import com.fed.flowchart_builder.data.BlockDescription;

public class BlockCreateFragment extends Fragment {

    private OnActivityRequest mOnActivityRequest;

    public BlockCreateFragment() {
        super(R.layout.block_create_fragment);
    }

    public BlockCreateFragment(OnActivityRequest onActivityRequest) {
        super(R.layout.block_create_fragment);
        mOnActivityRequest = onActivityRequest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView recyclerViewBlocks = layout.findViewById(R.id.recycler_view_blocks);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewBlocks.setLayoutManager(mLayoutManager);
        BlocksAdapter blocksAdapter = new BlocksAdapter(BlockDescription.values(), getContext());
        blocksAdapter.setOnItemClickListener(new BlocksAdapter.OnItemClickListener() {
            @Override
            public void onClick(BlockDescription blockDescription) {
                SimpleBlockView blockView = blockDescription.getBlock(getContext());
                mOnActivityRequest.getFlowChartViewGroup().addView(blockView);
            }
        });
        recyclerViewBlocks.setAdapter(blocksAdapter);
//        recyclerViewBlocks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        recyclerViewBlocks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        return layout;
    }

    public interface OnActivityRequest {
        FlowChartViewGroup getFlowChartViewGroup();
    }
}
