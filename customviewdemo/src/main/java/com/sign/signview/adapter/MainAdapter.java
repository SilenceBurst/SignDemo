package com.sign.signview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sign.signview.R;

import java.util.List;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview
 * 文件名:    MainAdapter
 * 创建者:    CYS
 * 创建时间:  2017/8/29 0029 on 9:08
 * 描述:     主页面适配器
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<String> list;

    public MainAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.tvName.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainAdapterCallback != null) {
                    mainAdapterCallback.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MainViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private MainAdapterCallback mainAdapterCallback;

    public void setMainAdapterCallback(MainAdapterCallback mainAdapterCallback) {
        this.mainAdapterCallback = mainAdapterCallback;
    }

    public interface MainAdapterCallback {
        void onItemClick(int position);
    }
}
