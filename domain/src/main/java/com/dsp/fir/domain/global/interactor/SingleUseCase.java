package com.dsp.fir.domain.global.interactor;

import io.reactivex.Single;

public interface SingleUseCase<T> {
    Single<T> execute();
}