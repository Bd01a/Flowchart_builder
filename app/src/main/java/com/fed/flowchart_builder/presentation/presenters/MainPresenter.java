package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;

import java.util.List;

public class MainPresenter implements MainContracts.Presenter {

    private MainContracts.View mView;
    private ChartRepository mRepository;

    public MainPresenter(MainContracts.View view, ChartRepository repository) {
        mView = view;
        mRepository = repository;
        mRepository.setMainContract(new ChartRepository.RepositoryMainPresenterContract() {
            @Override
            public void getChartNamesIsCompleted(List<String> names) {
                mView.loadingChartNamesIsCompleted(names);
            }
        });
    }

    @Override
    public void deleteAllByChartName(String chartName) {
        mRepository.deleteAllByChartName(chartName);
    }

    @Override
    public void loadChartNames() {
        mRepository.getChartNames();
    }


}
