package com.example.fitrack.charts;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public final class FitnessChartBinder {
    private FitnessChartBinder() {
    }

    public static void bindLineChart(LineChart chart, List<Entry> entries, String label, int lineColor) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(lineColor);
        dataSet.setCircleColor(lineColor);
        dataSet.setValueTextColor(lineColor);
        dataSet.setLineWidth(2.2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);

        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.getAxisLeft().setTextColor(lineColor);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setTextColor(lineColor);
        Description description = new Description();
        description.setText(label);
        chart.setDescription(description);
        chart.getLegend().setTextColor(lineColor);
        chart.invalidate();
    }

    public static class SimpleValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(value);
        }
    }
}

