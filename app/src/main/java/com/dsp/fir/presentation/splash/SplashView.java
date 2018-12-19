package com.dsp.fir.presentation.splash;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SplashView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);
}
