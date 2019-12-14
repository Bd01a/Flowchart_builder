package com.fed.flowchart_builder.presentation.presenters;

import android.content.Context;

import java.util.List;

public interface MainContracts {
    interface View {
        Context getContext();

        void loadingChartNamesIsCompleted(List<String> names);
    }

    interface Presenter {
        void deleteAllByChartName(String chartName);
        void loadChartNames();


    }

}
