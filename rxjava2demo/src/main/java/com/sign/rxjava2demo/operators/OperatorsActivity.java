package com.sign.rxjava2demo.operators;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sign.rxjava2demo.R;

public class OperatorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
    }

    public void just(View view) {
        startActivity(new Intent(this, JustActivity.class));
    }

    public void map(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void interval(View view) {
        startActivity(new Intent(this, IntervalActivity.class));
    }
}
