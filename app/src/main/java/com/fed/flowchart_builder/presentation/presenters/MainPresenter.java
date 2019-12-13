package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;

import java.util.List;

public class MainPresenter implements MainContracts.Presenter {

    private MainContracts.View mView;
    private ChartRepository mRepository;

    public MainPresenter(MainContracts.View view) {
        mView = view;


        mRepository = ChartRepository.getChartRepository(mView.getContext());

        mRepository.setMainContract(new ChartRepository.RepositoryMainPresenterContract() {
            @Override
            public void getChartNamesIsCompleted(List<String> names) {
                mView.loadingChartNamesIsCompleted(names);
            }
        });

    }


    @Override
    public void addBlocks(List<ChartBlock> blocks) {
        mRepository.addBlocks(blocks);
    }


    @Override
    public void loadChartNames() {
        mRepository.getChartNames();
    }


}
