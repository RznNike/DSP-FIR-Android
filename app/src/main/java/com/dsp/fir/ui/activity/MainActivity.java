package com.dsp.fir.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.dsp.fir.R;
import com.dsp.fir.Screens;
import com.dsp.fir.global.BaseActivity;
import com.dsp.fir.global.flow.FlowNavigator;
import com.dsp.fir.global.flow.FlowRouter;
import com.dsp.fir.ui.fragment.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import ru.terrakok.cicerone.Navigator;

public class MainActivity extends BaseActivity {
    @Inject FlowRouter router;

    private FlowNavigator navigator = new FlowNavigator(this, R.id.container) {
        private boolean doubleBackToExitPressedOnce;

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case Screens.MAIN_SCREEN:
                    return MainFragment.getInstance();
                default:
                    return null;
            }
        }

        @Override
        protected Intent createFlowIntent(String flowKey, Object data) {
            return Screens.getFlowIntent(MainActivity.this, flowKey, data);
        }

        @Override
        protected void exit() {
            if (doubleBackToExitPressedOnce) {
                super.exit();
                return;
            }
            doubleBackToExitPressedOnce = true;
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(rootView, R.string.double_back_to_exit, getResources().getInteger(R.integer.snackbar_duration)).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, getResources().getInteger(R.integer.exit_duration));
        }
    };

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
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
            router.newRootScreen(Screens.MAIN_SCREEN);
        }
    }

    @Override
    public void onBackPressed() {
        router.exit();
    }
}
