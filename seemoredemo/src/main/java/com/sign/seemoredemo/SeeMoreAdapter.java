package com.sign.seemoredemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class SeeMoreAdapter extends RecyclerView.Adapter<SeeMoreAdapter.SeeMoreViewHolder> {

    private final static int TYPE_NORMAL = 0;//正常条目
    private final static int TYPE_SEE_MORE = 1;//查看更多
    private final static int TYPE_HIDE = 2;//收起
    private List<String> mList;
    private boolean mOpen = false;//是否是展开状态

    SeeMoreAdapter(List<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public SeeMoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);
        return new SeeMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeMoreViewHolder seeMoreViewHolder, int position) {
        TextView textView = (TextView) seeMoreViewHolder.itemView;
        if (getItemViewType(position) == TYPE_HIDE) {
            textView.setText("收起");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOpen = false;
                    notifyDataSetChanged();
                }
            });
        } else if (getItemViewType(position) == TYPE_SEE_MORE) {
            textView.setText("查看更多");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOpen = true;
                    notifyDataSetChanged();
                }
            });
        } else {
            textView.setText(mList.get(position));
            textView.setClickable(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.size() <= 4) {
            return TYPE_NORMAL;
        }
        if (mOpen) {
            if (position == mList.size()) {
                return TYPE_HIDE;
            } else {
                return TYPE_NORMAL;
            }
        } else {
            if (position == 3) {
                return TYPE_SEE_MORE;
            } else {
                return TYPE_NORMAL;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null || mList.size() == 0) {
            return 0;
        }
        if (mList.size() > 4) {
            //若现在是展开状态 条目数量需要+1 "收起"条目
            if (mOpen) {
                return mList.size() + 1;
            } else {
                return 4;
            }
        } else {
            return mList.size();
        }
    }

    class SeeMoreViewHolder extends RecyclerView.ViewHolder {

        public SeeMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}