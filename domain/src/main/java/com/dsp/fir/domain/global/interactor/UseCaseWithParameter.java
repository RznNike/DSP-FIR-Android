package com.dsp.fir.domain.global.interactor;

import io.reactivex.Observable;

public interface UseCaseWithParameter<P, T> {
    Observable<T> execute(P parameter);
}
