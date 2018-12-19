package com.dsp.fir.presentation.parameters;

import android.content.res.Resources;

import com.arellomobile.mvp.InjectViewState;
import com.dsp.fir.R;
import com.dsp.fir.global.BasePresenter;
import com.dsp.fir.util.ChartsParametersStorage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@InjectViewState
@Slf4j
public class ParametersPresenter extends BasePresenter<ParametersView> {
    private final Resources res;

    private String samplingFrequency;
    private String firstSignalFrequency;
    private String secondSignalFrequency;
    private String counts;
    private double[] impulseResponse;
    private boolean drawFirstSignal;
    private boolean drawSecondSignal;
    private boolean drawSumSignal;
    private boolean drawImpulseResponse;
    private boolean drawFilteredSignal;
    private boolean drawFirstSignalWithOffset;

    @Inject
    ParametersPresenter(Resources res) {
        this.res = res;

        drawFirstSignal = true;
        drawSecondSignal = true;
        drawSumSignal = true;
        drawImpulseResponse = true;
        drawFilteredSignal = true;
        drawFirstSignalWithOffset = true;
    }

    public void onLoad() {
        ChartsParametersStorage params = ChartsParametersStorage.getInstance();
        impulseResponse = params.getImpulseResponse();
        getViewState().populateData(params);
    }

    public void onChangeSamplingFrequency(String newValue) {
        samplingFrequency = newValue;
    }

    public void onChangeFirstSignalFrequency(String newValue) {
        firstSignalFrequency = newValue;
    }

    public void onChangeSecondSignalFrequency(String newValue) {
        secondSignalFrequency = newValue;
    }

    public void onChangeCounts(String newValue) {
        counts = newValue;
    }

    public void onDrawFirstSignalCheckedChanged(boolean isChecked) {
        drawFirstSignal = isChecked;
    }

    public void onDrawSecondSignalCheckedChanged(boolean isChecked) {
        drawSecondSignal = isChecked;
    }

    public void onDrawSumSignalCheckedChanged(boolean isChecked) {
        drawSumSignal = isChecked;
    }

    public void onDrawImpulseResponseCheckedChanged(boolean isChecked) {
        drawImpulseResponse = isChecked;
    }

    public void onDrawFilteredSignalCheckedChanged(boolean isChecked) {
        drawFilteredSignal = isChecked;
    }

    public void onDrawFirstSignalWithOffsetCheckedChanged(boolean isChecked) {
        drawFirstSignalWithOffset = isChecked;
    }

    public double[] getImpulseResponse() {
        return impulseResponse;
    }

    public void onDrawCharts() {
        try {
            ChartsParametersStorage params = ChartsParametersStorage.getInstance();
            params.setSamplingFrequency(Integer.valueOf(samplingFrequency));
            params.setFirstSignalFrequency(Integer.valueOf(firstSignalFrequency));
            params.setSecondSignalFrequency(Integer.valueOf(secondSignalFrequency));
            params.setCounts(Integer.valueOf(counts));
            params.setImpulseResponse(impulseResponse);
            params.setDrawFirstSignal(drawFirstSignal);
            params.setDrawSecondSignal(drawSecondSignal);
            params.setDrawSumSignal(drawSumSignal);
            params.setDrawImpulseResponse(drawImpulseResponse);
            params.setDrawFilteredSignal(drawFilteredSignal);
            params.setDrawFirstSignalWithOffset(drawFirstSignalWithOffset);
            getViewState().openChartsTab();
        } catch (Exception e) {
            getViewState().showMessage(res.getString(R.string.invalid_input));
        }
    }

    public void onSourceDefaultClick() {
        ChartsParametersStorage.getInstance().fillDefaultImpulseResponse();
    }

    public void parseCoefficientsFromFile(List<String> strings) {
        List<Double> parsedValues = new ArrayList<>();
        for (String line : strings) {
            try {
                parsedValues.add(Double.valueOf(line));
            } catch (Exception ignored) {}
        }
        impulseResponse = new double[parsedValues.size()];
        for (int i = 0; i < impulseResponse.length; i++) {
            impulseResponse[i] = parsedValues.get(i);
        }
        if (impulseResponse.length > 0) {
            getViewState().showMessage(res.getString(R.string.successfull_file_loading));
        } else {
            getViewState().showMessage(res.getString(R.string.error_in_file_loading));
        }
    }
}
