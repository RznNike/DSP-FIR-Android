package com.dsp.fir.di.modules.app;

import com.dsp.fir.global.flow.CiceroneHolder;
import com.dsp.fir.global.flow.FlowRouter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;

@Module
public class NavigationModule {
    private Cicerone<FlowRouter> cicerone;

    public NavigationModule() {
        cicerone = Cicerone.create(new FlowRouter());
    }

    @Provides
    @Singleton
    FlowRouter provideFlowRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    NavigatorHolder provideNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }

    @Provides
    @Singleton
    CiceroneHolder provideLocalNavigationHolder() {
        return new CiceroneHolder();
    }
}