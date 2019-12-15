package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class InletBlockView extends SimpleBlockView {
    private Path mPath = new Path();
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

    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);
        mPath.reset();
        float middleX = (getRect().right - getRect().left) / 2;

        float xr = middleX + getRect().width() / 4;
        float xl = middleX - getRect().width() / 4;
        float yt = getRect().top;

        float xrr = getRect().right;
        float xll = getRect().left;

        float yb = getRect().bottom;


        mPath.moveTo(xr, yt);
        mPath.arcTo(middleX, yt, xrr, yb, -90, 180, true);
        mPath.lineTo(xl, yb);
        mPath.arcTo(xll, yt, middleX, yb, 90, 180, true);
        mPath.lineTo(xr, yt);

        canvas.drawPath(mPath, getPaint());

    }
}
