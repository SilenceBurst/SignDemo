package com.sign.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by cys on 2018/5/15 0015.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ItemMoveListener {
    private List<String> stringList;

    public MyAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public boolean onMove(int fromPosition, int toPosition) {
        Collections.swap(stringList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(stringList.get(position));
        holder.itemView.findViewById(R.id.iv_drag).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (itemDragListener != null) {
                    itemDragListener.onStartDrags(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    ItemDragListener itemDragListener;

    public void setItemDragListener(ItemDragListener itemDragListener) {
        this.itemDragListener = itemDragListener;
    }

    interface ItemDragListener {
        void onStartDrags(RecyclerView.ViewHolder viewHolder);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}