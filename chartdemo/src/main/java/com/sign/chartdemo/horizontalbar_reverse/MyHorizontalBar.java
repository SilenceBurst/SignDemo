package com.sign.chartdemo.horizontalbar_reverse;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.HorizontalBarChart;

/**
 * 项目名:    SignDemo
 * 包名:      com.sign.chartdemo.compare
 * 文件名:    MyHorizontalBar
 * 创建者:    CYS
 * 创建时间:  2017/12/12 0012 on 15:47
 * 描述:     TODO
 */
public class MyHorizontalBar extends HorizontalBarChart {
    public MyHorizontalBar(Context context) {
        super(context);
    }

    public MyHorizontalBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new MyHorizontalBarChartRenderer(this, mAnimator, mViewPortHandler);
    }
}
