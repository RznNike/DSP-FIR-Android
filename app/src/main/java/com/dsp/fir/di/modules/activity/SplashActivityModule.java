package com.dsp.fir.di.modules.activity;

import com.dsp.fir.di.PerFragment;
import com.dsp.fir.ui.fragment.splash.SplashFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface SplashActivityModule {
    @PerFragment
    @ContributesAndroidInjector
    SplashFragment splashFragmentInjector();
}