package com.dsp.fir.ui.fragment.charts;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.dsp.fir.R;
import com.dsp.fir.data.preference.Preferences;
import com.dsp.fir.global.BaseFragment;
import com.dsp.fir.global.flow.FlowRouter;
import com.dsp.fir.presentation.charts.ChartsPresenter;
import com.dsp.fir.presentation.charts.ChartsView;
import com.dsp.fir.ui.item.ChartItem;
import com.dsp.fir.util.ChartData;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChartsFragment extends BaseFragment implements ChartsView {
    @Inject FlowRouter router;
    @InjectPresenter ChartsPresenter presenter;
    @Inject Provider<ChartsPresenter> presenterProvider;
    @Inject Preferences prefs;

    @BindView(R.id.recyclerView) RecyclerView recycler;

    private FastAdapter<AbstractItem> adapter;
    private ItemAdapter<AbstractItem> itemAdapter;

    public static ChartsFragment getInstance() {
        return new ChartsFragment();
    }

    @ProvidePresenter
    ChartsPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_charts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initItemAdapter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initItemAdapter() {
        itemAdapter = new ItemAdapter<>();
        ItemAdapter<ProgressItem> footerAdapter = new ItemAdapter<>();
        adapter = FastAdapter.with(Arrays.asList(itemAdapter, footerAdapter));
        adapter.setHasStableIds(true);
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
    public void showMessage(String message) {
        showSnackMessage(message);
    }

    private void bindAdapter() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public void populateData(List<ChartData> chartDataList) {
        List<AbstractItem> resultList = new ArrayList<>();

        for (ChartData chartData : chartDataList) {
            resultList.add(new ChartItem(chartData));
        }

        itemAdapter.setNewList(resultList);
    }
}