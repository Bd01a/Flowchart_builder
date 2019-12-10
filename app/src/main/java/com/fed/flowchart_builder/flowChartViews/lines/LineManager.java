package com.fed.flowchart_builder.flowChartViews.lines;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fed.flowchart_builder.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.flowChartViews.blocks.SimpleBlockView;

import java.util.ArrayList;
import java.util.List;

public class LineManager {
    public static final String TAG = "LineManager";
    List<SimpleLine> mLines;

    private Context mContext;
    private FlowChartViewGroup mViewGroup;

    private SimpleBlockView mBlock1;
    private SimpleLine.BlockSide mSide1;

    public LineManager(Context context, FlowChartViewGroup flowChartViewGroup) {
        mLines = new ArrayList<>();
        mContext = context;
        mViewGroup = flowChartViewGroup;
        findLines();
    }

    public void findLines() {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View child = mViewGroup.getChildAt(i);
            if (child instanceof SimpleLine) {
                Log.d(TAG, "find line");
                mLines.add((SimpleLine) child);
                ((SimpleLine) child).findViewGroup();
            }
        }
    }

    private void addFirstBlock(SimpleBlockView block1, SimpleLine.BlockSide side1) {
        mBlock1 = block1;
        mSide1 = side1;
        Toast.makeText(mContext, "Add first", Toast.LENGTH_SHORT).show();
    }

    public void addBlock(SimpleBlockView block, SimpleLine.BlockSide side) {
        if (mBlock1 != null) {
            SimpleLine line = new SimpleLine(mContext);
            mLines.add(line);
            mViewGroup.addView(line, 0);
            line.findViewGroup();
            line.addBlocks(mBlock1, mSide1, block, side);
            mBlock1 = null;
            mSide1 = null;
        } else {
            addFirstBlock(block, side);
        }
    }

    public void update() {
        for (SimpleLine line : mLines) {
            line.update();
        }
    }
}
