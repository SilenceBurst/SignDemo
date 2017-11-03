package com.sign.signview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sign.signview.activity.BezierActivity;
import com.sign.signview.activity.FaceActivity;
import com.sign.signview.activity.GuaGuaActivity;
import com.sign.signview.activity.MyCardActivity;
import com.sign.signview.activity.HeartActivity;
import com.sign.signview.activity.WaveActivity;
import com.sign.signview.adapter.MainAdapter;
import com.sign.signview.utils.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;
    private List<String> mainList;
    private MainAdapter mainAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        ToolbarUtil.initToolbar(this, false, "自定义view demo");

        initList();

        initRecyclerView();

        initListener();
    }

    private void initListener() {
        mainAdapter.setMainAdapterCallback(new MainAdapter.MainAdapterCallback() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(context, HeartActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(context, BezierActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(context, WaveActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context, FaceActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(context, MyCardActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(context, GuaGuaActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        mainAdapter = new MainAdapter(mainList);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(mainAdapter);
    }

    private void initList() {
        mainList = new ArrayList<>();
        mainList.add("丘比特  咻~");
        mainList.add("二阶贝塞尔");
        mainList.add("水波纹");
        mainList.add("百变心情");
        mainList.add("边缘凹凸卡片");
        mainList.add("刮刮卡");
    }
}
