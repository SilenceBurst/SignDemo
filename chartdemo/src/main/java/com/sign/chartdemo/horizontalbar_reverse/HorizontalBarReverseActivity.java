package com.sign.chartdemo.horizontalbar_reverse;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.sign.chartdemo.DemoBase;
import com.sign.chartdemo.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalBarReverseActivity extends DemoBase implements OnChartValueSelectedListener {

    protected MyHorizontalBar mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_bar_reverse);
        mChart = findViewById(R.id.horizontal_bar);

        mChart.setOnChartValueSelectedListener(this);
        //绘制图表边框
        mChart.setDrawBorders(true);
        mChart.setBorderColor(Color.WHITE);

        //不显示描述
        Description description = mChart.getDescription();
        description.setEnabled(false);

        mChart.setBackgroundColor(Color.BLACK);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        //设置左侧标签
        XAxis xAxis = mChart.getXAxis();
        //标签位置
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] xValues = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15"};
        //标签个数
        xAxis.setLabelCount(xValues.length);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        //顶层标签禁用
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisLeft().setAxisMinimum(0.0f);
        mChart.getAxisRight().setAxisMinimum(0.0f);
        mChart.getAxisRight().setTextColor(Color.WHITE);
        mChart.getAxisRight().setInverted(true);
        mChart.getAxisLeft().setInverted(true);
        mChart.setDrawValueAboveBar(true);

        setData(15, 100);
    }

    private void setData(int count, float range) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entries.add(new BarEntry(i, ((float) Math.random() * range)));
        }
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setValueTextColor(Color.WHITE);
        BarData data = new BarData(set);

        //设置柱的宽度
        data.setBarWidth(0.7f);
        data.setDrawValues(true);
        mChart.setData(data);
        mChart.setFitBars(true); // make the x-axis fit exactly all bars

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
