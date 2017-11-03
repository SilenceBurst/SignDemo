package com.sign.gradientdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class CommonHolder<T> extends RecyclerView.ViewHolder {

    public CommonHolder(Context context, ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(context).inflate(layoutRes, root, false));
        ButterKnife.bind(this, itemView);
    }

    /**
     * 此适配器是为了能让详情页共用列表页的ViewHolder，一般情况无需重写该构造器
     */
    public CommonHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public Context getContext() {
        return itemView.getContext();
    }

    /**
     * 用给定的 data 对 holder 的 view 进行赋值
     */
    public abstract void bindData(T t, int position);

    public void bindHeadData() {
    }

    public void bindBottomData() {
    }

    /**
     * 通知适配器更新布局
     */
    public interface OnNotifyChangeListener {
        void onNotify();
    }

    private OnNotifyChangeListener listener;


    public void setOnNotifyChangeListener(OnNotifyChangeListener listener) {
        this.listener = listener;
    }


    public String chooseStatus(int status) {
        String[] statusStr = {"待备货", "待出库", "待发车", "待收货", "待处理", "已中断", "已完成", "待取货", "待退货"};
        return statusStr[status - 1];
    }

    public void notifyChange() {
        listener.onNotify();
    }
}