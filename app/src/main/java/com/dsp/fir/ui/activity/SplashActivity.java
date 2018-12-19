package com.dsp.fir.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dsp.fir.R;
import com.dsp.fir.Screens;
import com.dsp.fir.global.BaseActivity;
import com.dsp.fir.global.flow.FlowNavigator;
import com.dsp.fir.global.flow.FlowRouter;
import com.dsp.fir.ui.fragment.splash.SplashFragment;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import ru.terrakok.cicerone.Navigator;

public class SplashActivity extends BaseActivity {
    @Inject FlowRouter router;

    private FlowNavigator navigator = new FlowNavigator(this, R.id.container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case Screens.SPLASH_SCREEN:
                    return SplashFragment.getInstance();
                default:
                    return null;
            }
        }

        @Override
        protected Intent createFlowIntent(String flowKey, Object data) {
            return Screens.getFlowIntent(SplashActivity.this, flowKey, data);
        }

        @Override
        protected void exit() {
            super.exit();
        }
    };

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_container;
    }

    @Override
    protected Navigator getNavigator() {
        return navigator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.SPLASH_SCREEN);
        }

    }

    @Override
    public void onBackPressed() {
        router.exit();
    }
}
