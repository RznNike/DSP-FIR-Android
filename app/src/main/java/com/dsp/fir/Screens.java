package com.dsp.fir;

import android.content.Context;
import android.content.Intent;

import com.dsp.fir.ui.activity.MainActivity;
import com.dsp.fir.ui.activity.SplashActivity;

public final class Screens {
    public static final String SPLASH_FLOW = "splash flow";
    public static final String SPLASH_SCREEN = "splash screen";

    public static final String MAIN_FLOW = "main flow";
    public static final String MAIN_SCREEN = "main screen";

    public static Intent getFlowIntent(Context context, String flowKey, Object data) {
        switch (flowKey) {
            case SPLASH_FLOW:
                return SplashActivity.getStartIntent(context);
            case MAIN_FLOW:
                return MainActivity.getStartIntent(context);
            default:
                return null;
        }
    }
}
