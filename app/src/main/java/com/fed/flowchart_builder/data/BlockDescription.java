package com.fed.flowchart_builder.data;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.flowChartViews.blocks.ConditionBlockView;
import com.fed.flowchart_builder.flowChartViews.blocks.OperationBlockView;
import com.fed.flowchart_builder.flowChartViews.blocks.SimpleBlockView;

public enum BlockDescription {
    OPERATION_BLOCK(R.string.block_operation, R.mipmap.ic_launcher, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    }),
    CONDITION_BLOCK(R.string.block_condition, R.mipmap.ic_launcher, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new ConditionBlockView(context);
        }
    }),
    CYCLE_BLOCK(R.string.block_cycle, R.mipmap.ic_launcher, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    }),
    INLET_BLOCK(R.string.block_inlet, R.mipmap.ic_launcher, new ObjectCreator() {
        @Override
        public SimpleBlockView getBlock(Context context) {
            return new OperationBlockView(context);
        }
    });


    private int mName;
    private int mIcon;
    private ObjectCreator mObjectCreator;

    BlockDescription(@StringRes int name, @DrawableRes int icon, ObjectCreator objectCreator) {
        mName = name;
        mIcon = icon;
        mObjectCreator = objectCreator;
    }

    public int getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public SimpleBlockView getBlock(Context context) {
        return mObjectCreator.getBlock(context);
    }

    interface ObjectCreator {
        public SimpleBlockView getBlock(Context context);
    }

}
