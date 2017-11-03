package com.sign.signview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sign.signview.R;
import com.sign.signview.utils.ToolbarUtil;

public class FaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        ToolbarUtil.initToolbar(this,true,"百变心情");
    }
}
