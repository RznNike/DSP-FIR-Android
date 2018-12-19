package com.dsp.fir.domain.global;

import io.reactivex.Scheduler;

public interface SchedulersProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler trampoline();

    Scheduler newThread();

    Scheduler io();
}
