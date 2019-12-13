package com.fed.flowchart_builder.presentation.presenters;

import android.content.Context;

import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;

import java.util.List;

public interface ChartContracts {
    interface View {
        void loadingBlockByChartNameIsCompleted(List<ChartBlock> blocks);

        void loadingLinesByChartNameIsCompleted(List<ChartLine> lines);

        Context getContext();
    }

    interface Presenter {
        void loadBlocksByChartName(String chartName);

        void loadLinesByChartName(String chartName);

        void replaceViewGroupChilds(String chartName, List<ChartBlock> blocks, List<ChartLine> lines);
    }
}
