package com.sign.zxing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    public final static String SCAN_RESULT = "SCAN_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        String content = getIntent().getStringExtra(SCAN_RESULT);
        if (!TextUtils.isEmpty(content)) {
            TextView tvContent = findViewById(R.id.tv_content);
            tvContent.setText(content);
        }
    }
}
