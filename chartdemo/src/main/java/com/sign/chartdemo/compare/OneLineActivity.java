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
import com.sign.chartdemo.horizontalbar_reverse.MyHorizontalBar;

import java.util.ArrayList;
import java.util.List;

public class OneLineActivity extends DemoBase {
    protected MyHorizontalBar mLeftChart;
    protected HorizontalBarChart mRightChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_line);
        mLeftChart = findViewById(R.id.left_horizontal_bar);
        mRightChart = findViewById(R.id.right_horizontal_bar);

        mLeftChart.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener(mLeftChart));

        //不显示描述
        Description leftDescription = mLeftChart.getDescription();
        leftDescription.setEnabled(false);

        mLeftChart.setBackgroundColor(Color.WHITE);
        Legend leftLegend = mLeftChart.getLegend();
        leftLegend.setEnabled(false);

        //设置左侧标签
        XAxis xLeftAxis = mLeftChart.getXAxis();
        xLeftAxis.setEnabled(false);

        //顶层标签禁用
        mLeftChart.getAxisLeft().setEnabled(false);
        mLeftChart.getAxisRight().setEnabled(false);
        mLeftChart.getAxisLeft().setAxisMinimum(0.0f);
        mLeftChart.getAxisRight().setAxisMinimum(0.0f);
        mLeftChart.getAxisRight().setInverted(true);
        mLeftChart.getAxisLeft().setInverted(true);
        mLeftChart.setDrawValueAboveBar(true);

        setLeftData(12, 100);

        mRightChart.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener(mRightChart));

        //不显示描述
        Description rightDescription = mRightChart.getDescription();
        rightDescription.setEnabled(false);

        mRightChart.setBackgroundColor(Color.WHITE);
        Legend rightLegend = mRightChart.getLegend();
        rightLegend.setEnabled(false);

        //设置左侧标签
        XAxis xRightAxis = mRightChart.getXAxis();
        //标签位置
        xRightAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] xValues = {"相关色温", "黑体线距离Duv", "显色指数CRI", "Re(R1~R15)", "光色品质CQS", "TLCI", "GAI", "光照度", "呎烛光", "峰值波长", "主波长", "色纯度"};
        //标签个数
        xRightAxis.setLabelCount(xValues.length);
        xRightAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xRightAxis.setTextColor(Color.BLUE);

        xRightAxis.setEnabled(false);
        xRightAxis.setDrawAxisLine(false);
        xRightAxis.setDrawGridLines(false);

        //顶层标签禁用
        mRightChart.getAxisLeft().setEnabled(false);
        mRightChart.getAxisRight().setEnabled(false);
        mRightChart.getAxisLeft().setAxisMinimum(0.0f);
        mRightChart.getAxisRight().setAxisMinimum(0.0f);
        mRightChart.getAxisRight().setTextColor(Color.RED);

        setRightData(12, 100);
    }

    private void setRightData(int count, float range) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entries.add(new BarEntry(i, ((float) Math.random() * range)));
        }
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(Color.RED);
        set.setValueTextColor(Color.RED);
        BarData data = new BarData(set);

        //设置柱的宽度
        data.setBarWidth(0.7f);
        mRightChart.setData(data);
        mRightChart.setFitBars(true); // make the x-axis fit exactly all bars

        //刷新数据
        mRightChart.invalidate();
    }

    private void setLeftData(int count, float range) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entries.add(new BarEntry(i, ((float) Math.random() * range)));
        }
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(Color.GREEN);
        set.setValueTextColor(Color.GREEN);
        BarData data = new BarData(set);

        //设置柱的宽度
        data.setBarWidth(0.7f);
        data.setDrawValues(true);
        mLeftChart.setData(data);
        mLeftChart.setFitBars(true); // make the x-axis fit exactly all bars

        //刷新数据
        mLeftChart.invalidate();
    }

    class MyOnChartValueSelectedListener implements OnChartValueSelectedListener {
        private HorizontalBarChart mChart;

        public MyOnChartValueSelectedListener(HorizontalBarChart mChart) {
            this.mChart = mChart;
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
}
