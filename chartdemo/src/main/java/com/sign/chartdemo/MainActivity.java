package com.sign.chartdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sign.chartdemo.compare.OneLineActivity;
import com.sign.chartdemo.compare.TwoLineActivity;
import com.sign.chartdemo.horizontalbar.HorizontalBarActivity;
import com.sign.chartdemo.horizontalbar_reverse.HorizontalBarReverseActivity;
import com.sign.chartdemo.radar.RadarChartActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_radar).setOnClickListener(this);
        findViewById(R.id.btn_horizontal_bar).setOnClickListener(this);
        findViewById(R.id.btn_horizontal_bar_reverse).setOnClickListener(this);
        findViewById(R.id.btn_one_line).setOnClickListener(this);
        findViewById(R.id.btn_two_line).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //雷达图
            case R.id.btn_radar:
                intent.setClass(this, RadarChartActivity.class);
                break;
            //横向柱状图
            case R.id.btn_horizontal_bar:
                intent.setClass(this, HorizontalBarActivity.class);
                break;
            //横向柱状图反转
            case R.id.btn_horizontal_bar_reverse:
                intent.setClass(this, HorizontalBarReverseActivity.class);
                break;
            //数据对比 单行显示
            case R.id.btn_one_line:
                intent.setClass(this, OneLineActivity.class);
                break;
            //数据对比  双行显示
            case R.id.btn_two_line:
                intent.setClass(this, TwoLineActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
