package com.sign.constaintlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bias);
        //倾向 范围0-1
        //比例的计算从1.0处，可看到，1.0右边与父布局右边对齐，将1.0的text改变，1.0的text也均能显示
        //比例计算为：当前view左边/上边离父布局距离 = （父布局长/宽-当前view的长/宽）*bias值
    }
}
