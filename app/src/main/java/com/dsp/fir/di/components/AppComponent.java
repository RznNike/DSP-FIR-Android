package com.dsp.fir.di.components;

import android.content.Context;

import com.dsp.fir.App;
import com.dsp.fir.di.modules.app.AppModule;
import com.dsp.fir.di.modules.app.NavigationModule;
import com.dsp.fir.di.modules.app.PreferenceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NavigationModule.class,
        PreferenceModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        AppComponent build();
    }

    void inject(App app);
}