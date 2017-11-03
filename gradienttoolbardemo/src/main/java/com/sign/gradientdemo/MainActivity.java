package com.sign.gradientdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refresh)
    TwinklingRefreshLayout refresh;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    private MyAdapter adapter;
    private List<String> list;
    private Handler handler = new Handler();
    private int mDistanceY;
    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("测试数据" + i);
        }
        adapter = new MyAdapter();
        adapter.addItems(list);
        headView = getLayoutInflater().inflate(R.layout.headview, null);
        headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        adapter.setHeadHolder(headView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        refresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearDatas();
                        adapter.addItems(list);
                        refresh.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(list);
                        refresh.finishLoadmore();
                    }
                }, 2000);
            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动的距离
                mDistanceY += dy;
                //上方图片的高度
                int headHeight = headView.getHeight();
                if (mDistanceY < headHeight) {
                    //滑动距离小于上方图片的1/2时，设置白色搜索按钮，透明度从0-255
                    if (mDistanceY < headHeight / 2) {
                        imgSearch.setImageResource(R.mipmap.search_white);
                        float scale = (float) mDistanceY / (headHeight / 2);
                        float alpha = scale * 255;
                        llSearch.getBackground().setAlpha((int) alpha);
                    } else {//滑动距离大于上方图片的1/2并小于上方图片时，设置黑色搜索按钮，透明度从0-255
                        imgSearch.setImageResource(R.mipmap.search_black);
                        float scale = (float) (mDistanceY - headHeight / 2) / (headHeight / 2);
                        float alpha = scale * 255;
                        llSearch.getBackground().setAlpha((int) alpha);
                    }
                } else {
                    //当快速往下滑时，llSearch最后设置的alpha不约等于255，测试的为132,所以要再设置
                    llSearch.getBackground().setAlpha(255);
                }
            }
        });
    }
}
