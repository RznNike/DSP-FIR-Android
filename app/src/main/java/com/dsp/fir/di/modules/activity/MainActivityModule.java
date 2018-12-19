package com.dsp.fir.di.modules.activity;

import com.dsp.fir.di.PerFragment;
import com.dsp.fir.di.modules.fragment.MainFragmentModule;
import com.dsp.fir.ui.fragment.main.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface MainActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = {MainFragmentModule.class})
    MainFragment MainFragmentInjector();
}