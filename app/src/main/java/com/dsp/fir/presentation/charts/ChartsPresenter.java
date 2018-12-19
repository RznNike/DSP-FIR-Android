package com.dsp.fir.presentation.charts;

import android.content.res.Resources;

import com.arellomobile.mvp.InjectViewState;
import com.dsp.fir.R;
import com.dsp.fir.domain.global.SchedulersProvider;
import com.dsp.fir.global.BasePresenter;
import com.dsp.fir.util.ChartData;
import com.dsp.fir.util.ChartsParametersStorage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class ChartsPresenter extends BasePresenter<ChartsView> {
    private final SchedulersProvider schedulersProvider;
    private final Resources res;

    @Inject
    ChartsPresenter(
            SchedulersProvider schedulersProvider,
            Resources res) {
        this.schedulersProvider = schedulersProvider;
        this.res = res;
    }

    public void onResume() {;
        Disposable disposable = findData()
                .observeOn(schedulersProvider.ui())
                .subscribeOn(schedulersProvider.io())
                .subscribe(this::onFindDataComplete, this::onError);
        unsubscribeOnDestroy(disposable);
    }

    private Single<List<ChartData>> findData() {
        List<ChartData> chartDataList = new ArrayList<>();

        ChartsParametersStorage params = ChartsParametersStorage.getInstance();

        double[] firstSignal = new double[params.getCounts()];
        generateSinusWave(firstSignal, params.getFirstSignalFrequency(), params.getSamplingFrequency());
        if (params.isDrawFirstSignal()) {
            chartDataList.add(new ChartData(res.getString(R.string.first_signal), firstSignal));
        }

        double[] secondSignal = new double[params.getCounts()];
        generateSinusWave(secondSignal, params.getSecondSignalFrequency(), params.getSamplingFrequency());
        if (params.isDrawSecondSignal()) {
            chartDataList.add(new ChartData(res.getString(R.string.second_signal), secondSignal));
        }

        double[] sumSignal = new double[params.getCounts()];
        for (int i = 0; i < sumSignal.length; i++) {
            sumSignal[i] = firstSignal[i] + secondSignal[i];
        }
        if (params.isDrawSumSignal()) {
            chartDataList.add(new ChartData(res.getString(R.string.sum_signal), sumSignal));
        }

        if (params.isDrawImpulseResponse()) {
            chartDataList.add(new ChartData(res.getString(R.string.impulse_response), params.getImpulseResponse()));
        }

        double[] filteredSignal = filterSignal(sumSignal, params.getImpulseResponse());
        if (params.isDrawFilteredSignal()) {
            chartDataList.add(new ChartData(res.getString(R.string.filtered_signal), filteredSignal));
        }

        double[] firstSignalWithOffset = new double[params.getCounts()];
        int offset = params.getImpulseResponse().length / 2;
        System.arraycopy(firstSignal, 0, firstSignalWithOffset, offset, firstSignal.length - offset);
        if (params.isDrawFirstSignalWithOffset()) {
            chartDataList.add(new ChartData(res.getString(R.string.first_signal_with_offset), firstSignalWithOffset));
        }

        return Single.just(chartDataList);
    }

    private void generateSinusWave(double[] firstSignal, int signalFrequency, int samplingFrequency) {
        for (int i = 0; i < firstSignal.length; i++) {
            double arg = i * 2 * Math.PI * signalFrequency / samplingFrequency;
            firstSignal[i] = Math.sin(arg);
        }
    }

    private double[] filterSignal(double[] inputSignal, double[] impulseResponse) {
        double[] result = new double[inputSignal.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
            for (int n = 0; (n < impulseResponse.length) && ((i - n) >= 0); n++) {
                result[i] += inputSignal[i - n] * impulseResponse[n];
            }
        }
        return result;
    }

    private void onFindDataComplete(List<ChartData> chartDataList) {
        getViewState().populateData(chartDataList);
    }

    private void onError(Throwable throwable) {
        getViewState().showMessage(res.getString(R.string.unknown_error));
    }
}