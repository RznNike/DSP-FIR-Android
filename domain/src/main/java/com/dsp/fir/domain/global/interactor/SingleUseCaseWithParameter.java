package com.dsp.fir.domain.global.interactor;

import io.reactivex.Single;

public interface SingleUseCaseWithParameter<P, T> {
    Single<T> execute(P parameter);
}