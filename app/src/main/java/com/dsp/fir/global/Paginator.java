package com.dsp.fir.global;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("ALL")
@Slf4j
public class Paginator<T> {
    private static final int EMPTY_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 20;
    private final List<T> currentData = new ArrayList<>();
    private final List<T> bufferData = new ArrayList<>();
    private BiFunction<Integer, Integer, Single<List<T>>> requestFactory;
    private ViewController<T> viewController;
    private State<T> currentState = new EMPTY();
    private int limit;
    private Disposable disposable;

    public Paginator(BiFunction<Integer, Integer, Single<List<T>>> requestFactory, ViewController<T> viewController) {
        this(DEFAULT_LIMIT, requestFactory, viewController);
    }

    public Paginator(int limit, BiFunction<Integer, Integer, Single<List<T>>> requestFactory, ViewController<T> viewController) {
        this.requestFactory = requestFactory;
        this.viewController = viewController;
        this.limit = limit;
    }

    public void addBufferData(T data) {
        bufferData.add(data);
    }

    public void restart() {
        try {
            bufferData.clear();
            currentState.restart();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void refresh() {
        try {
            bufferData.clear();
            currentState.refresh();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void loadNext() {
        try {
            currentState.loadNext();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void release() {
        try {
            currentState.release();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void load(int offset) throws Exception {
        if (disposable != null)
            disposable.dispose();

        disposable = requestFactory.apply(offset, limit)
                .subscribe(result -> currentState.newData(result), error -> currentState.fail(error));
    }

    public List<T> getData() {
        List<T> data = new ArrayList<>(currentData);
        data.addAll(0, bufferData);
        return data;
    }

    public List<T> getCurrentData() {
        return currentData;
    }

    public interface ViewController<T> {
        default void showEmptyProgress(boolean show) {
        }

        default void showEmptyError(boolean show, Throwable error) {
        }

        default void showEmptyView(boolean show) {
        }

        default void showData(boolean show, List<T> data) {
        }

        default void showErrorMessage(Throwable error) {
        }

        default void showRefreshProgress(boolean show) {
        }

        default void showPageProgress(boolean show) {
        }
    }

    private interface State<T> {
        default void restart() throws Exception {
        }

        default void refresh() throws Exception {
        }

        default void loadNext() throws Exception {
        }

        default void release() {
        }

        default void newData(List<T> data) {
        }

        default void fail(Throwable error) {
        }
    }

    private class EMPTY implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showData(false, new ArrayList<>());
            viewController.showPageProgress(false);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void refresh() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class EMPTY_PROGRESS implements State<T> {
        @Override
        public void restart() throws Exception {
            load(EMPTY_OFFSET);
        }

        @Override
        public void newData(List<T> data) {
            if (data.isEmpty() && bufferData.isEmpty()) {
                currentState = new EMPTY_DATA();
                viewController.showEmptyProgress(false);
                viewController.showEmptyView(true);
            } else if (data.size() < limit) {
                currentState = new ALL_DATA();
                currentData.clear();
                currentData.addAll(data);
                viewController.showData(true, getData());
                viewController.showEmptyProgress(false);
                viewController.showEmptyView(false);
            } else {
                currentState = new DATA();
                currentData.clear();
                currentData.addAll(data);
                viewController.showData(true, getData());
                viewController.showEmptyProgress(false);
                viewController.showEmptyView(false);
            }
        }

        @Override
        public void fail(Throwable error) {
            currentState = new EMPTY_ERROR();
            viewController.showEmptyProgress(false);
            viewController.showEmptyError(true, error);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class EMPTY_ERROR implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showEmptyError(false, null);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void refresh() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showEmptyError(false, null);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class EMPTY_DATA implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showEmptyView(false);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void refresh() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showEmptyView(false);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class DATA implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showData(false, new ArrayList<>());
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void refresh() throws Exception {
            currentState = new REFRESH();
            viewController.showRefreshProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void loadNext() throws Exception {
            currentState = new PAGE_PROGRESS();
            viewController.showPageProgress(true);
            load(currentData.size());
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class REFRESH implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showData(false, new ArrayList<>());
            viewController.showRefreshProgress(false);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void newData(List<T> data) {
            if (data.isEmpty() && bufferData.isEmpty()) {
                currentState = new EMPTY_DATA();
                currentData.clear();
                viewController.showData(false, new ArrayList<>());
                viewController.showRefreshProgress(false);
                viewController.showEmptyView(true);
            } else if (data.size() < limit) {
                currentState = new ALL_DATA();
                currentData.clear();
                currentData.addAll(data);
                viewController.showRefreshProgress(false);
                viewController.showData(true, getData());
            } else {
                currentState = new DATA();
                currentData.clear();
                currentData.addAll(data);
                viewController.showRefreshProgress(false);
                viewController.showData(true, getData());
            }
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }

        @Override
        public void fail(Throwable error) {
            currentState = new DATA();
            viewController.showRefreshProgress(false);
            viewController.showErrorMessage(error);
        }
    }

    private class PAGE_PROGRESS implements State<T> {
        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showData(false, new ArrayList<>());
            viewController.showPageProgress(false);
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void newData(List<T> data) {
            if (data.isEmpty() || data.size() < limit) {
                currentState = new ALL_DATA();
                currentData.addAll(data);
                viewController.showPageProgress(false);
                viewController.showData(true, getData());
            } else {
                currentState = new DATA();
                currentData.addAll(data);
                viewController.showPageProgress(false);
                viewController.showData(true, getData());
            }
        }

        @Override
        public void refresh() throws Exception {
            currentState = new REFRESH();
            viewController.showPageProgress(false);
            viewController.showRefreshProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void fail(Throwable error) {
            currentState = new DATA();
            viewController.showPageProgress(false);
            viewController.showErrorMessage(error);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class ALL_DATA implements State<T> {

        @Override
        public void restart() throws Exception {
            currentState = new EMPTY_PROGRESS();
            viewController.showData(false, new ArrayList<>());
            viewController.showEmptyProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void refresh() throws Exception {
            currentState = new REFRESH();
            viewController.showRefreshProgress(true);
            load(EMPTY_OFFSET);
        }

        @Override
        public void release() {
            currentState = new RELEASED();
            disposable.dispose();
        }
    }

    private class RELEASED implements State<T> {

    }
}
