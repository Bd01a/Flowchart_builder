package com.fed.flowchart_builder.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fed.flowchart_builder.R;
import com.fed.flowchart_builder.data.BlockDescription;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;
import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.ConditionBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.CycleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.InletBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.OperationBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.SimpleLine;
import com.fed.flowchart_builder.presentation.fragments.BlockCreateFragment;
import com.fed.flowchart_builder.presentation.presenters.ChartContracts;
import com.fed.flowchart_builder.presentation.presenters.ChartPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements ChartContracts.View {

    boolean isBlocksLoaded;
    boolean isLinesLoaded;
    List<ChartBlock> mBlocks;
    List<ChartLine> mLines;
    String mChartName;
    private FlowChartViewGroup mFlowChartViewGroup;
    private FloatingActionButton mFloatingActionButton;
    private ChartPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mPresenter = new ChartPresenter(this);
        mChartName = getIntent().getStringExtra(MainActivity.CHART_NAME);
        mFlowChartViewGroup = findViewById(R.id.flowchart_view_group);

        if (savedInstanceState == null) {
            mPresenter.loadBlocksByChartName(mChartName);
            mPresenter.loadLinesByChartName(mChartName);
            mFlowChartViewGroup.setVisibility(View.INVISIBLE);
        }

        final BlockCreateFragment blockCreateFragment = new BlockCreateFragment(new BlockCreateFragment.OnActivityRequest() {
            @Override
            public FlowChartViewGroup getFlowChartViewGroup() {
                return mFlowChartViewGroup;
            }
        });

        mFloatingActionButton = findViewById(R.id.btn_block_create);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.additional_functionality_fragment, blockCreateFragment)
                        .commit();
                mFloatingActionButton.hide();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mFloatingActionButton.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFloatingActionButton.show();
    }

    @Override
    public void loadingBlockByChartNameIsCompleted(List<ChartBlock> blocks) {
        isBlocksLoaded = true;
        mBlocks = blocks;
        endLoading();
    }

    @Override
    public void loadingLinesByChartNameIsCompleted(List<ChartLine> lines) {
        isLinesLoaded = true;
        mLines = lines;
        endLoading();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void endLoading() {
        if (isBlocksLoaded && isLinesLoaded) {
            for (int i = 0; i < mBlocks.size(); i++) {
                SimpleBlockView blockView = null;
                if (mBlocks.get(i).getBlockType() == BlockDescription.OPERATION_BLOCK.getId()) {
                    blockView = new OperationBlockView(getContext());
                } else if (mBlocks.get(i).getBlockType() == BlockDescription.CONDITION_BLOCK.getId()) {
                    blockView = new ConditionBlockView(getContext());
                } else if (mBlocks.get(i).getBlockType() == BlockDescription.CYCLE_BLOCK.getId()) {
                    blockView = new CycleBlockView(getContext());
                } else if (mBlocks.get(i).getBlockType() == BlockDescription.INLET_BLOCK.getId()) {
                    blockView = new InletBlockView(getContext());
                }
                if (blockView != null) {
                    blockView.setAttr(mBlocks.get(i));
                    mFlowChartViewGroup.addView(blockView);
                }

            }
            for (int i = 0; i < mLines.size(); i++) {
                SimpleBlockView blockView1 = (SimpleBlockView) mFlowChartViewGroup.
                        getChildAt(i + mLines.get(i).getNumBlock1());
                SimpleBlockView blockView2 = (SimpleBlockView) mFlowChartViewGroup.
                        getChildAt(i + mLines.get(i).getNumBlock2());

                SimpleLine.BlockSide side1 = SimpleLine.BlockSide.getBlockSide(mLines.get(i).getSide1());
                SimpleLine.BlockSide side2 = SimpleLine.BlockSide.getBlockSide(mLines.get(i).getSide2());

                mFlowChartViewGroup.getLineManager().addBlock(blockView1, side1);
                mFlowChartViewGroup.getLineManager().addBlock(blockView2, side2);
            }
            mFlowChartViewGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        List<ChartBlock> blocks = new ArrayList<>();
        List<ChartLine> lines = new ArrayList<>();
        for (int i = 0; i < mFlowChartViewGroup.getChildCount(); i++) {
            if (mFlowChartViewGroup.getChildAt(i) instanceof SimpleBlockView) {
                ChartBlock chartBlock = ((SimpleBlockView) mFlowChartViewGroup.getChildAt(i)).save();
                chartBlock.setChartName(mChartName);
                blocks.add(chartBlock);
            } else if (mFlowChartViewGroup.getChildAt(i) instanceof SimpleLine) {
                ChartLine chartLine = ((SimpleLine) mFlowChartViewGroup.getChildAt(i)).save();
                chartLine.setChartName(mChartName);
                lines.add(chartLine);
            }
        }
        mPresenter.replaceViewGroupChilds(mChartName, blocks, lines);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
