package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class InletBlockView extends SimpleBlockView {
    public InletBlockView(Context context) {
        super(context);
    }

    public InletBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InletBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getSelfType() {
        return BlockDescription.INLET_BLOCK.getId();
    }
}
