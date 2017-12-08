package com.sign.chartdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sign.chartdemo.horizontalbar.HorizontalBarActivity;
import com.sign.chartdemo.radar.RadarChartActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_radar).setOnClickListener(this);
        findViewById(R.id.btn_horizontal_bar).setOnClickListener(this);
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
            default:
                break;
        }
        startActivity(intent);
    }
}
