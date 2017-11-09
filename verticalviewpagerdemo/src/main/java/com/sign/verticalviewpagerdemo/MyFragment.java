package com.sign.verticalviewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 项目名:    ServerMonitor
 * 包名:      com.hjtech.servermonitor
 * 文件名:    TvFragment
 * 创建者:    CYS
 * 创建时间:  2017/11/9 0009 on 9:11
 * 描述:     fragment
 */
public class MyFragment extends Fragment {

    @BindView(R.id.text_view)
    TextView textView;
    Unbinder unbinder;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, v);
        index = getArguments().getInt("index");

        textView.setText(String.valueOf(index));
        return v;
    }

    public static MyFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
