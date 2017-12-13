package com.sign.chartdemo.compare;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.sign.chartdemo.DemoBase;
import com.sign.chartdemo.R;

import java.util.ArrayList;
import java.util.List;

public class TwoLineActivity extends DemoBase implements OnChartValueSelectedListener {
    protected HorizontalBarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_line);
        mChart = findViewById(R.id.horizontal_bar);

        mChart.setOnChartValueSelectedListener(this);

        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawValueAboveBar(true);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
        //不显示描述
        Description description = mChart.getDescription();
        description.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMinimum(0.0f);
        //标签位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //保证标签在小组中间
        xAxis.setCenterAxisLabels(true);
        final String[] xLable = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9"};
//        标签个数
        xAxis.setLabelCount(xLable.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLable));
        xAxis.setTextColor(Color.BLUE);
        xAxis.setAxisMaximum(9f);

        //顶层标签禁用
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setAxisMinimum(0.0f);
        mChart.getAxisRight().setAxisMinimum(0.0f);

        setData(9, 100);

        mChart.setFitBars(true);
        mChart.animateY(2500);
    }

    private void setData(int count, float range) {
        List<BarEntry> entries1 = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entries1.add(new BarEntry(i, ((float) Math.random() * range)));
            entries2.add(new BarEntry(i, ((float) Math.random() * range)));
        }
        BarDataSet set1 = new BarDataSet(entries1, "BarDataSet1");
        BarDataSet set2 = new BarDataSet(entries2, "BarDataSet2");
        set1.setColors(Color.RED);
        set1.setValueTextColor(Color.RED);
        set2.setColors(Color.GREEN);
        set2.setValueTextColor(Color.GREEN);

        BarData barData = new BarData(set1, set2);
        barData.setBarWidth(0.45f);
        mChart.setData(barData);

        //设置分组
        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        mChart.groupBars(0f, groupSpace, barSpace);

        //刷新数据
        mChart.invalidate();
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null) {
            return;
        }

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);

        MPPointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {
    }
}
