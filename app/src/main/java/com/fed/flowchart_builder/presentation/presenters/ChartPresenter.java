package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartLiveData;
import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;
import com.fed.flowchart_builder.presentation.view.ChartActivity;

import java.util.List;

public class ChartPresenter {

    private ChartRepository mRepository;
    private ChartActivity mView;

    public ChartPresenter(ChartActivity view) {
        mView = view;
        mRepository = ChartRepository.getChartRepository(view.getApplicationContext());
    }


    public void loadBlocksByChartName(ChartLiveData<List<ChartBlock>> liveData, String chartName) {
        mRepository.getBlocksByChartName(liveData, chartName);
    }


    public void loadLinesByChartName(ChartLiveData<List<ChartLine>> liveData, String chartName) {
        mRepository.getLinesByChartName(liveData, chartName);
    }


    public void replaceViewGroupChilds(String chartName, List<ChartBlock> blocks, List<ChartLine> lines) {
        mRepository.deleteAllByChartName(chartName);
        mRepository.addBlocks(blocks);
        mRepository.addLines(lines);
    }

}
