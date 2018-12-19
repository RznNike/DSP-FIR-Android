package com.dsp.fir.global.flow;

import ru.terrakok.cicerone.commands.Command;

public class StartFlow implements Command {
    private String screenKey;
    private Object transitionData;

    public StartFlow(String screenKey, Object transitionData) {
        this.screenKey = screenKey;
        this.transitionData = transitionData;
    }

    public String getScreenKey() {
        return screenKey;
    }

    public Object getTransitionData() {
        return transitionData;
    }
}