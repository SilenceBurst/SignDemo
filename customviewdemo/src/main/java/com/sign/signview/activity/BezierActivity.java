package com.sign.signview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sign.signview.R;
import com.sign.signview.utils.ToolbarUtil;

public class BezierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        ToolbarUtil.initToolbar(this,true,"二阶贝塞尔");
    }
}
