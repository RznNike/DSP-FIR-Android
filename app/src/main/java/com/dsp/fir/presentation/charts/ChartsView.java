package com.dsp.fir.presentation.charts;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dsp.fir.global.AddToEndSingleByTagStateStrategy;
import com.dsp.fir.util.ChartData;

import java.util.List;

public interface ChartsView extends MvpView {
    String PROGRESS_TAG = "progress";

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void showProgress();

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void hideProgress();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void populateData(List<ChartData> chartDataList);
}
