package com.dsp.fir.global;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dsp.fir.R;
import com.google.common.base.Preconditions;
import com.dsp.fir.global.fixmoxy.MvpAppCompatFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends MvpAppCompatFragment implements HasSupportFragmentInjector {
    @Inject DispatchingAndroidInjector<Fragment> childFragmentInjector;

    private Unbinder unbinder;
    private MaterialDialog dialog;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @LayoutRes
    protected abstract int getContentView();

    protected void showSnackMessage(String message) {
        Preconditions.checkNotNull(getView());
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showProgressDialog() {
        Preconditions.checkNotNull(getContext());
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getContext())
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
    public void onDestroyView() {
        unbinder.unbind();
        hideProgressDialog();

        if (getActivity() != null)
            KeyboardHelper.hideKeyboard(getActivity());

        super.onDestroyView();
    }

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
