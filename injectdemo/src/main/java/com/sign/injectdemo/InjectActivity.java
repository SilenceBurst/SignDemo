package com.sign.injectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InjectActivity extends AppCompatActivity implements View.OnClickListener {
    @ViewInject(value = R.id.tv_can_click, click = true)
    TextView tvCanClick;
    @ViewInject(value = R.id.tv_can_not_click)
    TextView tvCanNotClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);
        ViewInjectUtil.inject(this, null);
        tvCanClick.setText("我是可点击的   已被find");
        tvCanNotClick.setText("我是不可点击的    已被find");
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "我被点击了", Toast.LENGTH_LONG).show();
    }
}
