package com.dsp.fir.domain.global.interactor;

import io.reactivex.Completable;

public interface CompletableUseCaseWithParameter<P> {
    Completable execute(P parameter);
}