package com.dsp.fir.global;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

import java.util.Iterator;
import java.util.List;

public class AddToEndSingleByTagStateStrategy implements StateStrategy {
    @Override
    public <View extends MvpView> void beforeApply(List<ViewCommand<View>> state, ViewCommand<View> command) {
        Iterator<ViewCommand<View>> iterator = state.iterator();

        while (iterator.hasNext()) {
            ViewCommand<View> entry = iterator.next();

            if (entry.getTag().equals(command.getTag())) {
                iterator.remove();
                break;
            }
        }

        state.add(command);
    }

    @Override
    public <View extends MvpView> void afterApply(List<ViewCommand<View>> state, ViewCommand<View> command) {
    }
}