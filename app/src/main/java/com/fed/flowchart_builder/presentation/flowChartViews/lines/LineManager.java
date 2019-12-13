package com.fed.flowchart_builder.presentation.flowChartViews.lines;

import android.content.Context;

import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;

import java.util.ArrayList;
import java.util.List;

public class LineManager {
    public static final String TAG = "LineManager";
    private List<SimpleLine> mLines;

    private Context mContext;
    private FlowChartViewGroup mViewGroup;

    private SimpleBlockView mBlock1;
    private SimpleLine.BlockSide mSide1;

    private boolean mIsDrawDeleteIcons;

    public LineManager(Context context, FlowChartViewGroup flowChartViewGroup) {
        mLines = new ArrayList<>();
        mContext = context;
        mViewGroup = flowChartViewGroup;
    }


    private void addFirstBlock(SimpleBlockView block1, SimpleLine.BlockSide side1) {
        mBlock1 = block1;
        mSide1 = side1;
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
            mViewGroup.showAllAddLineIcons(false);
        } else {
            addFirstBlock(block, side);
            mViewGroup.showAllAddLineIcons(true);
        }
    }

    public void update() {
        for (SimpleLine line : mLines) {
            line.update();
        }
    }

    public void showDeleteIcons(boolean isShow) {
        mIsDrawDeleteIcons = isShow;
        for (SimpleLine line : mLines) {
            line.isDrawDeleteIcon(isShow);
            line.invalidate();
        }
    }

    public boolean isDrawDeleteIcons() {
        return mIsDrawDeleteIcons;
    }

    public void checkBlocks() {
        for (SimpleLine line : mLines) {
            line.checkBlocks();
        }
    }
}
