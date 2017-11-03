package com.sign.constaintlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GoneMarginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gone_margin);
        //当button2（约束者）被gone掉时，被约束者会获得一个 约束点 的新margin来补偿约束者的消失（原来约束点的margin不起作用）
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btn_2).setVisibility(View.GONE);
            }
        });
    }
}
