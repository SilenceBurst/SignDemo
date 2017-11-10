package com.sign.recyclerdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
 * 项目名:    RecyclerDemo
 * 包名       com.sign.recyclerdemo
 * 文件名:    MyRecyclerAdapter
 * 创建者:    CYS
 * 创建时间:  2017/7/31 0031 on 15:16
 * 描述:     TODO
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<String> list;


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(list.get(position));
    }

    public void addData(List<String> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
