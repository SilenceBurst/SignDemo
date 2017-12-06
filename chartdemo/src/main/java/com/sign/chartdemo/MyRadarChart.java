package com.sign.chartdemo;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.RadarChart;

/**
 * 项目名:    ChartDemo
 * 包名:      com.sign.chartdemo
 * 文件名:    MyRadarChart
 * 创建者:    CYS
 * 创建时间:  2017/12/5 0005 on 17:53
 * 描述:     TODO
 */
public class MyRadarChart extends RadarChart {
    public MyRadarChart(Context context) {
        super(context);
    }

    public MyRadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mXAxisRenderer = new MyXAxisRendererRadarChart(mViewPortHandler, mXAxis, this);
    }
}
