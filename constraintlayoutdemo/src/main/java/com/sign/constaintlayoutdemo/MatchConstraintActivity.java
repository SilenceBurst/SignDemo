package com.sign.constaintlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MatchConstraintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_constraint);
        //0dp 等于 MATCH_CONSTRAINT 充满约束（margin 从约束中扣除）
        //所以0dp的方向要和约束的方向一致，否则无效
    }
}
