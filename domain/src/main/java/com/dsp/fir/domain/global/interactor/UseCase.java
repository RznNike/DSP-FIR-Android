package com.dsp.fir.domain.global.interactor;


import io.reactivex.Observable;

public interface UseCase<T> {
    Observable<T> execute();
}
