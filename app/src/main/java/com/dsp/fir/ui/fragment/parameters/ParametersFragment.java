package com.dsp.fir.ui.fragment.parameters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.dsp.fir.R;
import com.dsp.fir.domain.global.SchedulersProvider;
import com.dsp.fir.global.BaseFragment;
import com.dsp.fir.global.StateTextWatcher;
import com.dsp.fir.global.flow.FlowRouter;
import com.dsp.fir.presentation.parameters.ParametersPresenter;
import com.dsp.fir.presentation.parameters.ParametersView;
import com.dsp.fir.util.ChartsParametersStorage;
import com.google.common.base.Preconditions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ParametersFragment extends BaseFragment implements ParametersView {
    private static final int READ_REQUEST_CODE = 100;

    @InjectPresenter ParametersPresenter presenter;
    @Inject Provider<ParametersPresenter> presenterProvider;
    @Inject FlowRouter router;
    @Inject SchedulersProvider schedulersProvider;
    @BindView(R.id.editTextSamplingFrequency) EditText editTextSamplingFrequency;
    @BindView(R.id.editTextFirstSignalFrequency) EditText editTextFirstSignalFrequency;
    @BindView(R.id.editTextSecondSignalFrequency) EditText editTextSecondSignalFrequency;
    @BindView(R.id.editTextCounts) EditText editTextCounts;
    @BindView(R.id.checkBoxDrawFirstSignal) CheckBox checkBoxDrawFirstSignal;
    @BindView(R.id.checkBoxDrawSecondSignal) CheckBox checkBoxDrawSecondSignal;
    @BindView(R.id.checkBoxDrawSumSignal) CheckBox checkBoxDrawSumSignal;
    @BindView(R.id.checkBoxDrawImpulseResponse) CheckBox checkBoxDrawImpulseResponse;
    @BindView(R.id.checkBoxDrawFilteredSignal) CheckBox checkBoxDrawFilteredSignal;
    @BindView(R.id.checkBoxDrawFirstSignalWithOffset) CheckBox checkBoxDrawFirstSignalWithOffset;
    @BindView(R.id.radioButtonSourceDefault) RadioButton radioButtonSourceDefault;

    public static Fragment getInstance() {
        return new ParametersFragment();
    }

    @ProvidePresenter
    ParametersPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_parameters;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preconditions.checkNotNull(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEditTextListeners();
        presenter.onLoad();
    }

    private void initEditTextListeners() {
        editTextSamplingFrequency.addTextChangedListener(new StateTextWatcher() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                presenter.onChangeSamplingFrequency(newValue);
            }
        });
        editTextFirstSignalFrequency.addTextChangedListener(new StateTextWatcher() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                presenter.onChangeFirstSignalFrequency(newValue);
            }
        });
        editTextSecondSignalFrequency.addTextChangedListener(new StateTextWatcher() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                presenter.onChangeSecondSignalFrequency(newValue);
            }
        });
        editTextCounts.addTextChangedListener(new StateTextWatcher() {
            @Override
            public void onValueChanged(String oldValue, String newValue) {
                presenter.onChangeCounts(newValue);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        showSnackMessage(message);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void populateData(ChartsParametersStorage chartsParametersStorage) {
        editTextSamplingFrequency.setText(String.valueOf(chartsParametersStorage.getSamplingFrequency()));
        editTextFirstSignalFrequency.setText(String.valueOf(chartsParametersStorage.getFirstSignalFrequency()));
        editTextSecondSignalFrequency.setText(String.valueOf(chartsParametersStorage.getSecondSignalFrequency()));
        editTextCounts.setText(String.valueOf(chartsParametersStorage.getCounts()));

        checkBoxDrawFirstSignal.setChecked(chartsParametersStorage.isDrawFirstSignal());
        checkBoxDrawSecondSignal.setChecked(chartsParametersStorage.isDrawSecondSignal());
        checkBoxDrawSumSignal.setChecked(chartsParametersStorage.isDrawSumSignal());
        checkBoxDrawImpulseResponse.setChecked(chartsParametersStorage.isDrawImpulseResponse());
        checkBoxDrawFilteredSignal.setChecked(chartsParametersStorage.isDrawFilteredSignal());
        checkBoxDrawFirstSignalWithOffset.setChecked(chartsParametersStorage.isDrawFirstSignalWithOffset());
    }

    @Override
    public void openChartsTab() {
        AHBottomNavigation navigation = Objects.requireNonNull(getActivity()).findViewById(R.id.navigationView);
        navigation.setCurrentItem(1);
    }

    @OnCheckedChanged(R.id.checkBoxDrawFirstSignal)
    public void onCheckBoxDrawFirstSignalCheckedChanged() {
        presenter.onDrawFirstSignalCheckedChanged(checkBoxDrawFirstSignal.isChecked());
    }

    @OnCheckedChanged(R.id.checkBoxDrawSecondSignal)
    public void onCheckBoxDrawSecondSignalCheckedChanged() {
        presenter.onDrawSecondSignalCheckedChanged(checkBoxDrawSecondSignal.isChecked());
    }

    @OnCheckedChanged(R.id.checkBoxDrawSumSignal)
    public void onCheckBoxDrawSumSignalCheckedChanged() {
        presenter.onDrawSumSignalCheckedChanged(checkBoxDrawSumSignal.isChecked());
    }

    @OnCheckedChanged(R.id.checkBoxDrawImpulseResponse)
    public void onCheckBoxDrawImpulseResponseCheckedChanged() {
        presenter.onDrawImpulseResponseCheckedChanged(checkBoxDrawImpulseResponse.isChecked());
    }

    @OnCheckedChanged(R.id.checkBoxDrawFilteredSignal)
    public void onCheckBoxDrawFilteredSignalCheckedChanged() {
        presenter.onDrawFilteredSignalCheckedChanged(checkBoxDrawFilteredSignal.isChecked());
    }

    @OnCheckedChanged(R.id.checkBoxDrawFirstSignalWithOffset)
    public void onCheckBoxDrawFirstSignalWithOffsetCheckedChanged() {
        presenter.onDrawFirstSignalWithOffsetCheckedChanged(checkBoxDrawFirstSignalWithOffset.isChecked());
    }

    @OnClick(R.id.radioButtonSourceDefault)
    public void onRadioButtonSourceDefaultClick() {
        presenter.onSourceDefaultClick();
    }

    @OnClick(R.id.radioButtonSourceFile)
    public void onRadioButtonSourceFileClick() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @OnClick(R.id.buttonShowCoefficients)
    public void onButtonShowCoefficientsClick() {
        double[] coefficients = presenter.getImpulseResponse();
        StringBuilder message = new StringBuilder();
        for (double coef : coefficients) {
            message.append(String.format(Locale.getDefault(), "%.21f", coef)).append("\n");
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialog.setTitle(getString(R.string.impulse_response_coefficients));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(getString(R.string.close), (dialog, arg1) -> {});
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(dialog -> { });
        alertDialog.show();
    }

    @OnClick(R.id.buttonDrawCharts)
    public void onButtonDrawChartsClick() {
        presenter.onDrawCharts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri uri = Objects.requireNonNull(data.getData());
                    if ((uri.toString().endsWith(".fcf"))
                            || (uri.toString().endsWith(".txt"))) {
                        try {
                            InputStream inputStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(uri);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    inputStream));
                            List<String> strings = new ArrayList<>();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                strings.add(line);
                            }
                            Objects.requireNonNull(inputStream).close();
                            reader.close();
                            presenter.parseCoefficientsFromFile(strings);
                        } catch (IOException e) {
                            showMessage(getString(R.string.error_in_file_loading));
                            radioButtonSourceDefault.setChecked(true);
                        }
                    } else {
                        showMessage(getString(R.string.unsupported_file_format));
                        radioButtonSourceDefault.setChecked(true);
                    }
                } catch (Exception e) {
                    showMessage(getString(R.string.unknown_error));
                    radioButtonSourceDefault.setChecked(true);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage(getString(R.string.file_choosing_cancelled));
                radioButtonSourceDefault.setChecked(true);
            } else {
                showMessage(getString(R.string.unknown_error));
                radioButtonSourceDefault.setChecked(true);
            }
        }
    }
}
