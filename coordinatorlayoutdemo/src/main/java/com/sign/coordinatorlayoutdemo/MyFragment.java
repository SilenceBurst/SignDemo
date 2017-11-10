package com.sign.coordinatorlayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/*
 * 项目名:    CoordinatorLayoutDemo
 * 包名       com.sign.coordinatorlayoutdemo
 * 文件名:    MyFragment
 * 创建者:    CYS
 * 创建时间:  2017/5/15 0015 on 10:06
 * 描述:     TODO
 */
public class MyFragment extends Fragment {

//    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<String> list;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my, container, false);
        initRecycler();
        return v;
    }


    private void initRecycler() {
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("我是条目" + i);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
//        refreshLayout = (TwinklingRefreshLayout) v.findViewById(R.id.refresh);
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                //下拉刷新
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        list.clear();
//                        for (int i = 0; i < 50; i++) {
//                            list.add("我是刷新条目" + i);
//                        }
//                        adapter.notifyDataSetChanged();
//                        refreshLayout.finishRefreshing();
//                    }
//                }, 2000);
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                //上拉加载
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 50; i < 100; i++) {
//                            list.add("我是加载条目" + i);
//                        }
//                        adapter.notifyDataSetChanged();
//                        refreshLayout.finishLoadmore();
//                    }
//                }, 2000);
//            }
//        });
    }
}
