package com.fed.flowchart_builder.flowChartViews.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

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

//    @Override
//    public void drawSelected(Canvas canvas) {
//        canvas.drawOval(getRect(), getPaint());
//    }

    @Override
    public void drawUnSelected(Canvas canvas) {
        super.drawUnSelected(canvas);
        canvas.drawRect(getRect(), getPaint());

    }

//    @Override
//    public boolean isInBlock(float x, float y) {
//        if((x<getRect().right) &&  (x>getRect().left) && (y<getRect().bottom) && (y>getRect().top)){
//            return true;
//        }
//        return false;
//    }

}
