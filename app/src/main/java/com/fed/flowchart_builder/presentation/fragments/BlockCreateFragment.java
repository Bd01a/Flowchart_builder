package com.fed.flowchart_builder.presentation.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.BlockDescription;
import com.fed.flowchart_builder.presentation.adapters.BlocksAdapter;
import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;

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
                FlowChartViewGroup flowChartViewGroup = mOnActivityRequest.getFlowChartViewGroup();
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point displaySize = new Point();
                display.getSize(displaySize);
                SimpleBlockView blockView = blockDescription.getBlock(getContext());
                blockView.translate((-flowChartViewGroup.getScrollX() - displaySize.x / 2) / flowChartViewGroup.getCurrentScale(),
                        (-flowChartViewGroup.getScrollY() - displaySize.y / 2) / flowChartViewGroup.getCurrentScale());
                flowChartViewGroup.addView(blockView);
            }

        });
        recyclerViewBlocks.setAdapter(blocksAdapter);
        return layout;
    }

    public interface OnActivityRequest {
        FlowChartViewGroup getFlowChartViewGroup();
    }

}
