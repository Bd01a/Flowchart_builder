package com.fed.flowchart_builder.presentation.presenters;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.lifecycle.Observer;

import com.fed.flowchart_builder.data.BlockDescription;
import com.fed.flowchart_builder.data.ChartLiveData;
import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;
import com.fed.flowchart_builder.presentation.flowChartViews.FlowChartViewGroup;
import com.fed.flowchart_builder.presentation.flowChartViews.blocks.SimpleBlockView;
import com.fed.flowchart_builder.presentation.flowChartViews.lines.SimpleLineView;
import com.fed.flowchart_builder.presentation.view.MainActivity;

import java.util.List;

public class MainPresenter {

    private MainActivity mView;
    private ChartRepository mRepository;

    private boolean mIsBlocksLoaded = false;
    private boolean mIsLinesLoaded = false;

    private List<ChartBlock> mBlocks;
    private List<ChartLine> mLines;

    public MainPresenter(MainActivity view) {
        mView = view;
        mRepository = ChartRepository.getChartRepository(view.getApplicationContext());
    }

    public void deleteAllByChartName(String chartName) {
        mRepository.deleteAllByChartName(chartName);
    }

    public void loadChartNames(ChartLiveData<List<String>> liveData) {
        mRepository.getChartNames(liveData);
    }


    public void prepareChartToShare(String chartName) {
        ChartLiveData<List<ChartBlock>> blocksLiveData = new ChartLiveData<>();
        blocksLiveData.observe(mView, new Observer<List<ChartBlock>>() {
            @Override
            public void onChanged(List<ChartBlock> blocks) {
                mIsBlocksLoaded = true;
                mBlocks = blocks;
                chartIsLoaded();
            }
        });
        mRepository.getBlocksByChartName(blocksLiveData, chartName);
        ChartLiveData<List<ChartLine>> linesLiveData = new ChartLiveData<>();
        linesLiveData.observe(mView, new Observer<List<ChartLine>>() {
            @Override
            public void onChanged(List<ChartLine> lines) {
                mIsLinesLoaded = true;
                mLines = lines;
                chartIsLoaded();
            }
        });
        mRepository.getLinesByChartName(linesLiveData, chartName);
    }

    private void chartIsLoaded() {
        if (mIsBlocksLoaded && mIsLinesLoaded) {
            FlowChartViewGroup flowChartViewGroup = new FlowChartViewGroup(mView.getContext());

            float l = 0;
            float r = 0;
            float t = 0;
            float b = 0;

            for (int i = 0; i < mBlocks.size(); i++) {
                SimpleBlockView blockView = null;
                BlockDescription[] blockDescriptions = BlockDescription.values();
                for (BlockDescription description : blockDescriptions) {
                    if (mBlocks.get(i).getBlockType() == description.getId()) {
                        blockView = description.getBlock(mView.getContext());
                        if (blockView != null) {
                            blockView.setAttr(mBlocks.get(i));
                            flowChartViewGroup.addView(blockView);
                            blockView.updateGeomOptions();

                            if (i == 0) {
                                l = blockView.getPosition().x - blockView.getOriginalWidth() / 2
                                        - blockView.getPaint().getStrokeWidth();
                                r = blockView.getPosition().x + blockView.getOriginalWidth() / 2
                                        + blockView.getPaint().getStrokeWidth();
                                t = blockView.getPosition().y - blockView.getOriginalHeight() / 2
                                        - blockView.getPaint().getStrokeWidth();
                                b = blockView.getPosition().y + blockView.getOriginalHeight() / 2
                                        + blockView.getPaint().getStrokeWidth();
                            } else {
                                l = Math.min(l, blockView.getPosition().x - blockView.getOriginalWidth() / 2
                                        - blockView.getPaint().getStrokeWidth());
                                r = Math.max(r, blockView.getPosition().x + blockView.getOriginalWidth() / 2
                                        + blockView.getPaint().getStrokeWidth());
                                t = Math.min(t, blockView.getPosition().y - blockView.getOriginalHeight() / 2
                                        - blockView.getPaint().getStrokeWidth());
                                b = Math.max(b, blockView.getPosition().y + blockView.getOriginalHeight() / 2
                                        + blockView.getPaint().getStrokeWidth());
                            }
                        }
                    }
                }

            }
            for (int i = 0; i < mLines.size(); i++) {
                SimpleBlockView blockView1 = (SimpleBlockView) flowChartViewGroup.
                        getChildAt(i + mLines.get(i).getNumBlock1());
                SimpleBlockView blockView2 = (SimpleBlockView) flowChartViewGroup.
                        getChildAt(i + mLines.get(i).getNumBlock2());

                SimpleLineView.BlockSide side1 = SimpleLineView.BlockSide.getBlockSide(mLines.get(i).getSide1());
                SimpleLineView.BlockSide side2 = SimpleLineView.BlockSide.getBlockSide(mLines.get(i).getSide2());

                flowChartViewGroup.getLineManager().addBlock(blockView1, side1);
                flowChartViewGroup.getLineManager().addBlock(blockView2, side2);


            }
            l = flowChartViewGroup.getLineManager().determineLeft(l);
            b = flowChartViewGroup.getLineManager().determineBottom(b);
            r = flowChartViewGroup.getLineManager().determineRight(r);
            t = flowChartViewGroup.getLineManager().determineTop(t);

            Bitmap bitmap = Bitmap.createBitmap((int) (r - l), (int) (b - t), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(-l, -t);

            flowChartViewGroup.layout((int) l, (int) t, (int) r, (int) b);
            flowChartViewGroup.draw(canvas);

            mView.shareChart(bitmap);
        }
    }


}
