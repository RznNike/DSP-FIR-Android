package com.dsp.fir.presentation.parameters;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dsp.fir.global.AddToEndSingleByTagStateStrategy;
import com.dsp.fir.util.ChartsParametersStorage;

public interface ParametersView extends MvpView {
    String PROGRESS_TAG = "progress";

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void showProgress();

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void hideProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void populateData(ChartsParametersStorage chartsParametersStorage);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void openChartsTab();
}