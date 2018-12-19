package com.dsp.fir.global.flow;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.dsp.fir.R;
import com.dsp.fir.global.BaseActivity;
import com.dsp.fir.global.fixcicerone.SupportFragmentNavigator;

import ru.terrakok.cicerone.commands.BackTo;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;

public abstract class FlowNavigator extends SupportFragmentNavigator {
    private static final int DEFAULT_FLOW_REQUEST_CODE = -1;

    protected final BaseActivity activity;

    public FlowNavigator(BaseActivity fragmentActivity, int containerId) {
        super(fragmentActivity.getSupportFragmentManager(), containerId);
        activity = fragmentActivity;
    }

    public void setLaunchScreen(String screenKey) {
        applyCommands(new Command[]{
                new BackTo(null),
                new Replace(screenKey, null)
        });
    }

    public void setLaunchScreen(String screenKey, Object data) {
        applyCommands(new Command[]{
                new BackTo(null),
                new Replace(screenKey, data)
        });
    }

    @Override
    protected void applyCommand(Command command) {
        if (command instanceof StartFlow) {
            startFlow(((StartFlow) command).getScreenKey(), ((StartFlow) command).getTransitionData());
        } else if (command instanceof FinishFlow) {
            finishFlow();
        } else {
            super.applyCommand(command);
        }
    }

    private void startFlow(String screenKey, Object transitionData) {
        Intent intent = createFlowIntent(screenKey, transitionData);
        if (intent != null) {
            activity.startActivityForResult(intent, getRequestCodeForFlow(screenKey));
            activity.overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
        }
    }

    protected Intent createFlowIntent(String flowKey, Object data) {
        return null;
    }

    protected int getRequestCodeForFlow(String flowKey) {
        return DEFAULT_FLOW_REQUEST_CODE;
    }

    private void finishFlow() {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter_anim, R.anim.activity_exit_anim);
    }

    protected Intent createFlowResult(Object data) {
        return null;
    }

    @Override
    protected void exit() {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    protected void showSystemMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}