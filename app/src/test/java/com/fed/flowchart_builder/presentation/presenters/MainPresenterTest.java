package com.fed.flowchart_builder.presentation.presenters;

import com.fed.flowchart_builder.data.ChartRepository;
import com.fed.flowchart_builder.presentation.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class MainPresenterTest {

    private MainPresenter mMainPresenter;
    private ChartRepository mChartRepository;

    @Before
    public void setUp() {

        mChartRepository = mock(ChartRepository.class);
        MainActivity view = mock(MainActivity.class);
        mMainPresenter = new MainPresenter(view, mChartRepository);

    }


    @Test
    public void testLoadChartNames() {
        mMainPresenter.loadChartNames();
        Mockito.verify(mChartRepository).getChartNames();
    }

    @Test
    public void testDeleteAllByChartName() {
        String string = "string";
        mMainPresenter.deleteAllByChartName(string);
        Mockito.verify(mChartRepository).deleteAllByChartName(string);
    }

}