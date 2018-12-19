package com.dsp.fir.global.flow;

import android.annotation.SuppressLint;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.result.ResultListener;

public class FlowRouter extends Router {

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, List<ResultListener>> resultListeners = new HashMap<>();

    /**
     * @deprecated Use {@link #addResultListener(Integer, ResultListener)} instead.
     */
    @Deprecated
    @Override
    public void setResultListener(Integer resultCode, ResultListener listener) {
        if (!resultListeners.containsKey(resultCode) || resultListeners.get(resultCode) == null)
            resultListeners.put(resultCode, new ArrayList<>());

        resultListeners.get(resultCode).clear();
        resultListeners.get(resultCode).add(listener);
    }

    public ResultListener addResultListener(Integer resultCode, ResultListener listener) {
        if (!resultListeners.containsKey(resultCode) || resultListeners.get(resultCode) == null)
            resultListeners.put(resultCode, new ArrayList<>());

        resultListeners.get(resultCode).add(listener);
        return listener;
    }

    /**
     * @deprecated Use {@link #removeResultListener(ResultListener)} instead.
     */
    @Deprecated
    @Override
    public void removeResultListener(Integer resultCode) {
        if (!resultListeners.containsKey(resultCode))
            return;

        resultListeners.remove(resultCode);
    }

    public void removeResultListener(ResultListener listener) {
        Stream.of(resultListeners)
                .filter(x -> x.getValue() != null && x.getValue().size() > 0)
                .forEach(x -> {
                    if (x.getValue().contains(listener))
                        x.getValue().remove(listener);
                });
    }

    @Override
    protected boolean sendResult(Integer resultCode, Object result) {
        AtomicBoolean success = new AtomicBoolean(false);
        Stream.of(resultListeners)
                .filter(x -> x.getKey().equals(resultCode) && x.getValue() != null && x.getValue().size() > 0)
                .forEach(x -> Stream.of(x.getValue())
                        .withoutNulls()
                        .forEach(listener -> {
                            listener.onResult(result);
                            success.set(true);
                        })
                );

        return success.get();
    }

    public void startFlow(String flowKey) {
        executeCommands(new StartFlow(flowKey, null));
    }

    public void startFlow(String flowKey, Object data) {
        executeCommands(new StartFlow(flowKey, data));
    }

    public void finishFlow() {
        executeCommands(new FinishFlow());
    }

    public void finishFlowWithResult(Integer resultCode, Object result) {
        executeCommands(new FinishFlow());
        sendResult(resultCode, result);
    }

    public void cancelFlow() {
        finishChain();
    }
}