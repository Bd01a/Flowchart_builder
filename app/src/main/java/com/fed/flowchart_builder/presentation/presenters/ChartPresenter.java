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

    public ChartPresenter(ChartActivity view, ChartRepository chartRepository) {
        mView = view;
        mRepository = chartRepository;
        mRepository.setChartContract(new ChartRepository.RepositoryChartPresenterContract() {
            @Override
            public void getBlocksByChartNameIsCompleted(List<ChartBlock> blocks) {
                mView.loadingBlockByChartNameIsCompleted(blocks);
            }

            @Override
            public void getLinesByChartNameIsCompleted(List<ChartLine> lines) {
//                mView.loadingLinesByChartNameIsCompleted(lines);
            }

        });
    }


    public void loadBlocksByChartName(String chartName) {
        mRepository.getBlocksByChartName(chartName);
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
