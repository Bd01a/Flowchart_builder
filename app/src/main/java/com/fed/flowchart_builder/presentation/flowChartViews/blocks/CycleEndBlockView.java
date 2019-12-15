package com.fed.flowchart_builder.presentation.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.fed.flowchart_builder.data.BlockDescription;

public class CycleEndBlockView extends SimpleBlockView {

    private Path mPath = new Path();

    public CycleEndBlockView(Context context) {
        super(context);
    }

    public CycleEndBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CycleEndBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getSelfType() {
        return BlockDescription.CYCLE_END_BLOCK.getId();
    }

    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);
        mPath.reset();

        float xl = getRect().left + getRect().width() / 6;
        float xr = getRect().left + getRect().width() / 6 * 5;

        float y1 = getRect().bottom - getRect().height() / 6;

        mPath.moveTo(getRect().right, getRect().top);
        mPath.lineTo(getRect().left, getRect().top);
        mPath.lineTo(getRect().left, y1);
        mPath.lineTo(xl, getRect().bottom);
        mPath.lineTo(xr, getRect().bottom);
        mPath.lineTo(getRect().right, y1);
        mPath.lineTo(getRect().right, getRect().top - getPaint().getStrokeWidth() / 2);
        canvas.drawPath(mPath, getPaint());
    }
}
