package com.dsp.fir.global;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dsp.fir.R;
import com.google.common.base.Preconditions;
import com.dsp.fir.global.fixmoxy.MvpAppCompatDialogFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public abstract class BaseDialogFragment extends MvpAppCompatDialogFragment implements HasSupportFragmentInjector {
    @Inject DispatchingAndroidInjector<Fragment> childFragmentInjector;

    private MaterialDialog dialog;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    protected void showProgressDialog() {
        Preconditions.checkNotNull(getContext());
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(getContext())
                    .content(R.string.loading)
                    .progress(true, 0)
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
        hideProgressDialog();

        if (getActivity() != null)
            KeyboardHelper.hideKeyboard(getActivity());

        super.onDestroyView();
    }
}
