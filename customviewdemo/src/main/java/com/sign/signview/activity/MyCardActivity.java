package com.sign.signview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sign.signview.R;
import com.sign.signview.utils.ToolbarUtil;

public class MyCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        ToolbarUtil.initToolbar(this, true, "边缘凹凸卡片");
    }
}
