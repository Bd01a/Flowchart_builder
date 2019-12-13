package com.fed.flowchart_builder.presentation.presenters;

import android.content.Context;

import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;

import java.util.List;

public interface MainContracts {
    interface View {
        Context getContext();

        void loadingChartNamesIsCompleted(List<String> names);
    }

    interface Presenter {
        void addBlocks(List<ChartBlock> blocks);

        void loadChartNames();


    }

}
