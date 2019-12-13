package com.fed.flowchart_builder.data;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.ConditionBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.OperationBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;

public enum BlockDescription {
    OPERATION_BLOCK(R.string.block_operation, R.mipmap.ic_launcher, 1, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    }),
    CONDITION_BLOCK(R.string.block_condition, R.mipmap.ic_launcher, 2, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new ConditionBlockView(context);
        }
    }),
    CYCLE_BLOCK(R.string.block_cycle, R.mipmap.ic_launcher, 3, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    }),
    INLET_BLOCK(R.string.block_inlet, R.mipmap.ic_launcher, 4, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    });


    private int mName;
    private int mIcon;
    private ObjectCreator mObjectCreator;
    private int mId;

    BlockDescription(@StringRes int name, @DrawableRes int icon, int id, ObjectCreator objectCreator) {
        mName = name;
        mIcon = icon;
        mObjectCreator = objectCreator;
        mId = id;
    }

    public int getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getId() {
        return mId;
    }

    public SimpleBlockView getBlock(Context context) {
        return mObjectCreator.getBlock(context);
    }

    interface ObjectCreator {
        public SimpleBlockView getBlock(Context context);
    }

}
