package com.dsp.fir.global;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {
    private CompositeDisposable disposables = new CompositeDisposable();

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
