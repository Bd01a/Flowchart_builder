package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class CycleBlockView extends SimpleBlockView {
    public CycleBlockView(Context context) {
        super(context);
    }

    public CycleBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CycleBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getSelfType() {
        return BlockDescription.CYCLE_BLOCK.getId();
    }
}
