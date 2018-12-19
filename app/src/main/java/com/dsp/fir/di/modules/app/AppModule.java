package com.dsp.fir.di.modules.app;

import android.content.Context;
import android.content.res.Resources;

import com.dsp.fir.di.PerActivity;
import com.dsp.fir.di.modules.activity.MainActivityModule;
import com.dsp.fir.di.modules.activity.SplashActivityModule;
import com.dsp.fir.domain.global.SchedulersProvider;
import com.dsp.fir.global.ErrorHandler;
import com.dsp.fir.ui.activity.MainActivity;
import com.dsp.fir.ui.activity.SplashActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module(includes = {AndroidSupportInjectionModule.class})
public interface AppModule {
    @Provides
    @Singleton
    static Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    static ErrorHandler provideErrorHandler(Resources res) {
        return new ErrorHandler(res);
    }

    @Provides
    @Singleton
    static SchedulersProvider provideSchedulersProvider() {
        return new SchedulersProvider() {
            @Override
            public Scheduler ui() {
                return AndroidSchedulers.mainThread();
            }

            @Override
            public Scheduler computation() {
                return Schedulers.computation();
            }

            @Override
            public Scheduler trampoline() {
                return Schedulers.trampoline();
            }

            @Override
            public Scheduler newThread() {
                return Schedulers.newThread();
            }

            @Override
            public Scheduler io() {
                return Schedulers.io();
            }
        };
    }

    @PerActivity
    @ContributesAndroidInjector(modules = {SplashActivityModule.class})
    SplashActivity splashActivityInjector();

    @PerActivity
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    MainActivity mainActivityInjector();
}
