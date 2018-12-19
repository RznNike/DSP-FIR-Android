package com.dsp.fir.presentation.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dsp.fir.global.AddToEndSingleByTagStateStrategy;

public interface MainView extends MvpView {
    String PROGRESS_TAG = "progress";

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void showProgress();

    @StateStrategyType(value = AddToEndSingleByTagStateStrategy.class, tag = PROGRESS_TAG)
    void hideProgress();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);
}
