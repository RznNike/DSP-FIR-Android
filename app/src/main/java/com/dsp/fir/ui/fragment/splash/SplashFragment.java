package com.dsp.fir.ui.fragment.splash;

import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.dsp.fir.R;
import com.dsp.fir.global.BaseFragment;
import com.dsp.fir.presentation.splash.SplashPresenter;
import com.dsp.fir.presentation.splash.SplashView;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class SplashFragment extends BaseFragment implements SplashView {
    @InjectPresenter SplashPresenter presenter;
    @Inject Provider<SplashPresenter> presenterProvider;
    @BindView(R.id.imageLogo) ImageView logoImage;

    public static SplashFragment getInstance() {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @ProvidePresenter
    SplashPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        animateLogo(() -> presenter.onLoadingFinish());
    }

    private void animateLogo(Runnable endAction) {
        logoImage.setAlpha(0f);
        logoImage.setScaleX(0f);
        logoImage.setScaleY(0f);
        logoImage.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator(2F))
                .withEndAction(endAction);
    }

    @Override
    public void showMessage(String message) {
        showSnackMessage(message);
    }
}
