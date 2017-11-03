package com.sign.constaintlayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.gone_margin).setOnClickListener(this);
        findViewById(R.id.bias).setOnClickListener(this);
        findViewById(R.id.match_constraint).setOnClickListener(this);
        findViewById(R.id.ratio).setOnClickListener(this);
        findViewById(R.id.chain).setOnClickListener(this);
        findViewById(R.id.guideline).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.gone_margin:
                intent.setClass(this, GoneMarginActivity.class);
                break;
            case R.id.bias:
                intent.setClass(this, BiasActivity.class);
                break;
            case R.id.match_constraint:
                intent.setClass(this, MatchConstraintActivity.class);
                break;
            case R.id.ratio:
                intent.setClass(this, RatioActivity.class);
                break;
            case R.id.chain:
                intent.setClass(this, ChainActivity.class);
                break;
            case R.id.guideline:
                intent.setClass(this, GuideLineActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
