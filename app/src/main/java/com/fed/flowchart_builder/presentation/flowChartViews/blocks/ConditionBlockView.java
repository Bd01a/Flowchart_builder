package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class ConditionBlockView extends SimpleBlockView {

    private Path mPath = new Path();
    ;

    public ConditionBlockView(Context context) {
        super(context);
    }

    public ConditionBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);

        float middleX = (getRect().right - getRect().left) / 2;
        float middleY = (getRect().bottom - getRect().top) / 2;

        mPath.reset();

        float x1 = getRect().left + middleX;
        float y1 = getRect().top;
        float x2 = getRect().right;
        float y2 = getRect().top + middleY;
        float x3 = getRect().left + middleX;
        float y3 = getRect().bottom;
        float x4 = getRect().left;
        float y4 = getRect().top + middleY;

        mPath.moveTo((x1 + x2) / 2, (y1 + y2) / 2);
        mPath.lineTo(x2, y2);
        mPath.lineTo(x3, y3);
        mPath.lineTo(x4, y4);
        mPath.lineTo(x1, y1);
        mPath.lineTo((x1 + x2) / 2, (y1 + y2) / 2);
        canvas.drawPath(mPath, getPaint());

    }

    @Override
    int getSelfType() {
        return BlockDescription.CONDITION_BLOCK.getId();
    }
}
