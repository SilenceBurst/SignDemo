package com.sign.gradientdemo;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

public class MyAdapter extends BaseRecyclerAdapter<String> {


    @Override
    public CommonHolder<String> setViewHolder(ViewGroup parent) {
        return new MyHolder(parent.getContext(), parent);
    }

    class MyHolder extends CommonHolder<String> {

        @BindView(R.id.txt_item)
        TextView txtItem;

        public MyHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.item_view);
        }

        @Override
        public void bindData(String s, int position) {
            txtItem.setText(s);
        }
    }
}
