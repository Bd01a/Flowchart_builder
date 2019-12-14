package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;

import java.util.List;

public class ChartPresenter implements ChartContracts.Presenter {

    private ChartRepository mRepository;
    private ChartContracts.View mView;

    public ChartPresenter(ChartContracts.View view, ChartRepository chartRepository) {
        mView = view;
        mRepository = chartRepository;
        mRepository.setChartContract(new ChartRepository.RepositoryChartPresenterContract() {
            @Override
            public void getBlocksByChartNameIsCompleted(List<ChartBlock> blocks) {
                mView.loadingBlockByChartNameIsCompleted(blocks);
            }

            @Override
            public void getLinesByChartNameIsCompleted(List<ChartLine> lines) {
                mView.loadingLinesByChartNameIsCompleted(lines);
            }

        });
    }

    @Override
    public void loadBlocksByChartName(String chartName) {
        mRepository.getBlocksByChartName(chartName);
    }

    @Override
    public void loadLinesByChartName(String chartName) {
        mRepository.getLinesByChartName(chartName);
    }

    @Override
    public void replaceViewGroupChilds(String chartName, List<ChartBlock> blocks, List<ChartLine> lines) {
        mRepository.deleteAllByChartName(chartName);
        mRepository.addBlocks(blocks);
        mRepository.addLines(lines);
    }

}
