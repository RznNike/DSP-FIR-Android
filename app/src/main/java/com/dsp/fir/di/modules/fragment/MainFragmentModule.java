package com.dsp.fir.di.modules.fragment;

import com.dsp.fir.di.PerChildFragment;
import com.dsp.fir.ui.fragment.charts.ChartsFragment;
import com.dsp.fir.ui.fragment.parameters.ParametersFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface MainFragmentModule {
    @PerChildFragment
    @ContributesAndroidInjector
    ParametersFragment parametersFragmentInjector();

    @PerChildFragment
    @ContributesAndroidInjector
    ChartsFragment chartsFragmentInjector();
}
