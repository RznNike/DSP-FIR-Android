package com.dsp.fir.ui.item;

import android.graphics.Color;
import android.view.View;

import com.dsp.fir.R;
import com.dsp.fir.util.ChartData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

@Getter
public class ChartItem extends AbstractItem<ChartItem, ChartItem.ViewHolder> {
    private ChartData chartData;

    public ChartItem(ChartData chartData) {
        this.chartData = chartData;
    }

    @Override
    public int getType() {
        return R.id.chartItem;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_chart;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View v) {
        return new ViewHolder(v);
    }

    class ViewHolder extends FastAdapter.ViewHolder<ChartItem> {
        View view;
        @BindView(R.id.chart) LineChart chart;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        @Override
        public void bindView(@NonNull ChartItem item, @NonNull List<Object> payloads) {
            List<Entry> entries = new ArrayList<>();
            double minimum = Double.MAX_VALUE;
            double maximum = -Double.MAX_VALUE;
            double[] values = item.chartData.getValues();
            for (int i = 0; i < values.length; i++) {
                entries.add(new Entry((float)i, (float)values[i]));
                minimum = minimum < values[i] ? minimum : values[i];
                maximum = maximum > values[i] ? maximum : values[i];
            }
            minimum = ((int)(minimum * 10 - 0.95)) / 10d;
            maximum = ((int)(maximum * 10 + 0.95)) / 10d;

            LineDataSet dataSet = new LineDataSet(entries, item.chartData.getHeader());
            dataSet.setColor(Color.argb(255, 255, 175, 0));
            dataSet.setCircleColor(Color.argb(0, 0, 0, 0));
            dataSet.setCircleColorHole(Color.argb(0, 0, 0, 0));
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);
            chart.setData(lineData);
            Description description = new Description();
            description.setText("");
            chart.setDescription(description);
            chart.setTouchEnabled(false);
            chart.getAxisRight().setEnabled(false);
            YAxis yAxis = chart.getAxisLeft();
            yAxis.setAxisMinimum((float)minimum);
            yAxis.setAxisMaximum((float)maximum);
            yAxis.setLabelCount(5, true);
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAxisMaximum(values.length);
            chart.invalidate();
        }

        @Override
        public void unbindView(@NonNull ChartItem item) {
        }
    }
}
