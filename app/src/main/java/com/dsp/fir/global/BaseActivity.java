package com.dsp.fir.global;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dsp.fir.R;
import com.dsp.fir.global.fixmoxy.MvpAppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;

public abstract class BaseActivity extends MvpAppCompatActivity implements HasSupportFragmentInjector {
    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;
    @Inject NavigatorHolder navigatorHolder;

    private MaterialDialog dialog;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        updateTaskDescripion();
    }

    private void updateTaskDescripion() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                getString(R.string.app_name),
                bm,
                getResources().getColor(R.color.colorTaskDescription)
        );
        setTaskDescription(taskDesc);
    }

    @LayoutRes
    protected abstract int getContentView();

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(getNavigator());
    }

    protected abstract Navigator getNavigator();

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        KeyboardHelper.hideKeyboard(this);
        super.onPause();
    }

    public void showSnackMessage(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showProgressDialog() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(this)
                    .content(R.string.loading)
                    .progress(true, 0)
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .build();
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }
}
