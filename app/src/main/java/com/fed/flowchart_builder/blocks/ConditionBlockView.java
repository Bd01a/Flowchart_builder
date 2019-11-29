package com.fed.flowchart_builder.blocks;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class ConditionBlockView extends SimpleBlockView {
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

        float tgAlpha = (getRect().bottom - getRect().top) / (getRect().right - getRect().left);
        double alpha = Math.atan(tgAlpha);
        float deltaX = (float) (getPaint().getStrokeWidth() / 2 * Math.cos(alpha));
        float deltaY = (float) (getPaint().getStrokeWidth() / 2 * Math.sin(alpha));

        canvas.drawLine(getRect().left + middleX - deltaX, getRect().top - deltaY,
                getRect().right + deltaX, getRect().top + middleY + deltaY, getPaint());
        canvas.drawLine(getRect().left + middleX + deltaX, getRect().top - deltaY,
                getRect().left - deltaX, getRect().top + middleY + deltaY, getPaint());
        canvas.drawLine(getRect().left + middleX - deltaX, getRect().bottom + deltaY,
                getRect().right + deltaX, getRect().top + middleY - deltaY, getPaint());
        canvas.drawLine(getRect().left + middleX + deltaX, getRect().bottom + deltaY,
                getRect().left - deltaX, getRect().top + middleY - deltaY, getPaint());
    }
}
