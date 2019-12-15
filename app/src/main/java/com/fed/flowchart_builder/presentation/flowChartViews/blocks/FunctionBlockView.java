package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class FunctionBlockView extends SimpleBlockView {
    public FunctionBlockView(Context context) {
        super(context);
    }

    public FunctionBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FunctionBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getSelfType() {
        return BlockDescription.FUNCTION_BLOCK.getId();
    }


    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);
        canvas.drawRect(getRect(), getPaint());
        float x1 = getRect().left + getRect().width() / 8;
        float x2 = getRect().left + getRect().width() / 8 * 7;
        canvas.drawLine(x1, getRect().top, x1, getRect().bottom, getPaint());
        canvas.drawLine(x2, getRect().top, x2, getRect().bottom, getPaint());
    }
}
