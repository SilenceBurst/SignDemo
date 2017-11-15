package com.sign.bluetoothlowenergydemo;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 */
public class MyAdapter extends RecyclerView.Adapter {

    private List<BluetoothDevice> list;

    public MyAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText("name:" + list.get(position).getName() + "  address:" + list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void addDatas(List<BluetoothDevice> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(BluetoothDevice item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public void setList(List<BluetoothDevice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear(){
        this.list.clear();
        notifyDataSetChanged();
    }

    public List<BluetoothDevice> getList() {
        return list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
