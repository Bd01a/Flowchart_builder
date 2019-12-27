package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.presentation.view.MainActivity;

import java.util.List;

public class MainPresenter {

    private MainActivity mView;
    private ChartRepository mRepository;

    public MainPresenter(MainActivity view, ChartRepository repository) {
        mView = view;
        mRepository = repository;
        mRepository.setMainContract(new ChartRepository.RepositoryMainPresenterContract() {
            @Override
            public void getChartNamesIsCompleted(List<String> names) {
                mView.loadingChartNamesIsCompleted(names);
            }
        });
    }

    public void deleteAllByChartName(String chartName) {
        mRepository.deleteAllByChartName(chartName);
    }

    public void loadChartNames() {
        mRepository.getChartNames();
    }


    public void prepareChartToShare(String chartName) {

    }

//    public void chartIsLoaded(){
//        if (isBlocksLoaded && isLinesLoaded) {
//            for (int i = 0; i < mBlocks.size(); i++) {
//                SimpleBlockView blockView = null;
//                BlockDescription[] blockDescriptions = BlockDescription.values();
//                for (BlockDescription description : blockDescriptions) {
//                    if (mBlocks.get(i).getBlockType() == description.getId()) {
//                        blockView = description.getBlock(getContext());
//                        if (blockView != null) {
//                            blockView.setAttr(mBlocks.get(i));
//                            mFlowChartViewGroup.addView(blockView);
//                        }
//                    }
//                }
//
//            }
//            for (int i = 0; i < mLines.size(); i++) {
//                SimpleBlockView blockView1 = (SimpleBlockView) mFlowChartViewGroup.
//                        getChildAt(i + mLines.get(i).getNumBlock1());
//                SimpleBlockView blockView2 = (SimpleBlockView) mFlowChartViewGroup.
//                        getChildAt(i + mLines.get(i).getNumBlock2());
//
//                SimpleLineView.BlockSide side1 = SimpleLineView.BlockSide.getBlockSide(mLines.get(i).getSide1());
//                SimpleLineView.BlockSide side2 = SimpleLineView.BlockSide.getBlockSide(mLines.get(i).getSide2());
//
//                mFlowChartViewGroup.getLineManager().addBlock(blockView1, side1);
//                mFlowChartViewGroup.getLineManager().addBlock(blockView2, side2);
//            }
//            mFlowChartViewGroup.setVisibility(View.VISIBLE);
//        }
//    }


}
