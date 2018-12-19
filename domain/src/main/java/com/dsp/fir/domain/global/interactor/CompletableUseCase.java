package com.dsp.fir.domain.global.interactor;

import io.reactivex.Completable;

public interface CompletableUseCase {
    Completable execute();
}