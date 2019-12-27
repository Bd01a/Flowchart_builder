package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class OperationBlockView extends SimpleBlockView {

    public OperationBlockView(Context context) {
        super(context);
    }

    public OperationBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OperationBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);
        canvas.drawRect(getRect(), getPaint());

    }


    @Override
    int getSelfType() {
        return BlockDescription.OPERATION_BLOCK.getId();
    }


}
