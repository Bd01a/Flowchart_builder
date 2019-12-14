package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.data.ChartRoom.ChartBlock;
import com.fed.flowchart_builder.data.ChartRoom.ChartLine;
import com.fed.flowchart_builder.presentation.view.ChartActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ChartPresenterTest {

    private ChartPresenter mChartPresenter;
    private ChartRepository mChartRepository;

    @Before
    public void setUp() {

        mChartRepository = mock(ChartRepository.class);
        ChartActivity view = mock(ChartActivity.class);
        mChartPresenter = new ChartPresenter(view, mChartRepository);

    }

    @Test
    public void testLoadBlocksByChartName() {
        String name = "name";
        mChartPresenter.loadBlocksByChartName(name);
        Mockito.verify(mChartRepository).getBlocksByChartName(name);
    }

    @Test
    public void testLoadLinesByChartName() {
        String name = "name";
        mChartPresenter.loadLinesByChartName(name);
        Mockito.verify(mChartRepository).getLinesByChartName(name);
    }

    @Test
    public void testReplaceViewGroupChilds() {
        String name = "name";
        List<ChartBlock> chartBlocks = new ArrayList<>();
        List<ChartLine> chartLines = new ArrayList<>();
        ChartBlock chartBlock = new ChartBlock();
        ChartLine chartLine = new ChartLine();
        chartBlocks.add(chartBlock);
        chartLines.add(chartLine);
        mChartPresenter.replaceViewGroupChilds(name, chartBlocks, chartLines);
        InOrder inOrder = Mockito.inOrder(mChartRepository);
        inOrder.verify(mChartRepository).deleteAllByChartName(name);
        inOrder.verify(mChartRepository).addBlocks(chartBlocks);
        inOrder.verify(mChartRepository).addLines(chartLines);
    }
}