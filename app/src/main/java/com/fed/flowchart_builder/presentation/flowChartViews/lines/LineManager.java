package com.fed.flowchart_builder.presentation.flowChartViews.lines;

import android.content.Context;

import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all {@link SimpleLineView}
 *
 * @author Sergey Fedorov
 */
public class LineManager {
    public static final String TAG = "LineManager";

    private List<SimpleLineView> mLines;

    /**
     * need to create new {@link SimpleLineView}
     */
    private Context mContext;

    /**
     * ViewGroup where all {@link LineManager#mLines} are located
     */
    private FlowChartViewGroup mViewGroup;


    /**
     * need to create new {@link SimpleLineView}. Firsly, saved first block, then create new line with
     * this block and new block, that is passed through {@link LineManager#addBlock(SimpleBlockView, SimpleLineView.BlockSide)}
     */
    private SimpleBlockView mBlock1;

    /**
     * side of {@link LineManager#mBlock1} to which the line will be attached
     */
    private SimpleLineView.BlockSide mSide1;

    private boolean mIsDrawDeleteIcons;

    public LineManager(Context context, FlowChartViewGroup flowChartViewGroup) {
        mLines = new ArrayList<>();
        mContext = context;
        mViewGroup = flowChartViewGroup;
    }


    /**
     * @param block1 initialize {@link LineManager#mBlock1}
     * @param side1 initialize {@link LineManager#mSide1}
     */
    private void addFirstBlock(SimpleBlockView block1, SimpleLineView.BlockSide side1) {
        mBlock1 = block1;
        mSide1 = side1;
    }

    /**
     * At the first call, the data will be saved in {@link LineManager#mBlock1} and {@link LineManager#mSide1}.
     * At the second call, create {@link SimpleLineView} and add it to {@link LineManager#mViewGroup}
     */
    public void addBlock(SimpleBlockView block, SimpleLineView.BlockSide side) {
        if (mBlock1 != null) {
            SimpleLineView line = new SimpleLineView(mContext);
            mLines.add(line);
            mViewGroup.addView(line, 0);
            line.findViewGroup();
            line.addBlocks(mBlock1, mSide1, block, side);
            mBlock1 = null;
            mSide1 = null;
            line.checkBlocks();
            mViewGroup.showAllAddLineIcons(false);
        } else {
            addFirstBlock(block, side);
            mViewGroup.showAllAddLineIcons(true);
        }
    }

    /**
     * calls {@link SimpleLineView#update()} at all {@link LineManager#mLines}
     */
    public void update() {
        for (SimpleLineView line : mLines) {
            line.update();
        }
    }

    /**
     * calls {@link SimpleLineView#isDrawDeleteIcon(boolean)} at all {@link LineManager#mLines}
     */
    public void showDeleteIcons(boolean isShow) {
        mIsDrawDeleteIcons = isShow;
        for (SimpleLineView line : mLines) {
            line.isDrawDeleteIcon(isShow);
            line.invalidate();
        }
    }

    public boolean isDrawDeleteIcons() {
        return mIsDrawDeleteIcons;
    }

    /**
     * check all {@link LineManager#mLines} for nulls in related {@link SimpleBlockView}
     */
    public void checkBlocks() {
        for (SimpleLineView line : mLines) {
            line.checkBlocks();
        }
    }

    public float determineLeft(float l) {
        for (SimpleLineView line : mLines) {
            l = Math.min(l, line.getLineX());
        }
        return l;
    }

    public float determineRight(float r) {
        for (SimpleLineView line : mLines) {
            r = Math.max(r, line.getLineX() + line.getLineWidth());
        }
        return r;
    }

    public float determineTop(float t) {
        for (SimpleLineView line : mLines) {
            t = Math.min(t, line.getLineY());
        }
        return t;
    }

    public float determineBottom(float b) {
        for (SimpleLineView line : mLines) {
            b = Math.max(b, line.getLineY() + line.getLineHeight());
        }
        return b;
    }
}
