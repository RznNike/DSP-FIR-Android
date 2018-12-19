package com.dsp.fir.presentation.splash;

import com.arellomobile.mvp.InjectViewState;
import com.dsp.fir.Screens;
import com.dsp.fir.global.BasePresenter;
import com.dsp.fir.global.flow.FlowRouter;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {
    private final FlowRouter router;

    @Inject
    SplashPresenter(FlowRouter router) {
        this.router = router;
    }


    public void onLoadingFinish() {
        router.startFlow(Screens.MAIN_FLOW);
    }
}
