package com.sign.constaintlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RatioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratio);
        /**
         * layout_constraintDimensionRatio
         * 有约束 且 至少一个0dp
         *
         * (1) a:b 即宽:高= width：height =a:b
         * (2) W/H,a:b
         *  1.一个约束纬度为0dp（另一约束纬度有值）
         *    约束纬度不为0的取原值
         *    eg:width=0 height=100 W,3:1          width=300
         *       width=100 height=0 W,3:1          height=300
         *       width=100 height=0 H,3:1          height=30
         *       width=0 height=100 H,3:1          height=30
         *    若为W: 另一纬度为 （已知纬度值）*a/b
         *    若为H：另一纬度为 （已知纬度值）*b/a
         *
         *  2.两个约束纬度为0dp
         *    eg: H,2:1 宽按width约束值，高为height*1/2
         *    W,a:b 宽按width约束值， 高为width*a/b
         *    H,a:b 高按height约束值，宽为height*b/a
         */
    }
}
