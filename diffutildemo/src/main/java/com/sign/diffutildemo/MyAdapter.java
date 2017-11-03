package com.sign.diffutildemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author CYS
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TestBean> list;

    public MyAdapter(List<TestBean> list) {
        this.list = list;
    }

    public void setList(List<TestBean> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvContent.setText(list.get(position).getName());
    }

//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
//            //文艺青年中的文青
//            Bundle payload = (Bundle) payloads.get(0);//取出我们在getChangePayload（）方法返回的bundle
//            TestBean bean = list.get(position);//取出新数据源，（可以不用）
//            for (String key : payload.keySet()) {
//                switch (key) {
//                    case "KEY_DESC":
//                        //这里可以用payload里的数据，不过data也是新的 也可以用
//                        holder.tvContent.setText(bean.getName());
//                        break;
//                    case "KEY_PIC":
////                        holder.iv.setImageResource(payload.getInt(key));
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
