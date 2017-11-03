package com.sign.gradientdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommonHolder.OnNotifyChangeListener, View.OnClickListener {
    private List<T> dataList = new ArrayList<>();
    private boolean enableHead = false;//是否添加头布局
    private boolean enableBottom = false;//是否添加底部布局
    CommonHolder headHolder;
    CommonHolder BottomHolder;
    ViewGroup rootView;
    public final static int TYPE_HEAD = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_Bottom = 2;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        rootView = parent;
        //设置ViewHolder
//        int type = getItemViewType(position);
        int type = position;
        if (type == TYPE_HEAD) {
            return headHolder;
        } else if (type == TYPE_Bottom) {
            return BottomHolder;
        } else {
            return setViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
        runEnterAnimation(holder.itemView, position);
        //数据绑定
        if (enableHead && enableBottom) {
            if (position == 0) {
                ((CommonHolder) holder).bindHeadData();
            } else if (position == dataList.size() + 1) {
                ((CommonHolder) holder).bindBottomData();
            } else {
                ((CommonHolder) holder).bindData(dataList.get(position - 1), position);
            }
        } else if (enableHead) {
            if (position == 0) {
                ((CommonHolder) holder).bindHeadData();
            } else {
                ((CommonHolder) holder).bindData(dataList.get(position - 1), position);
            }
        } else if (enableBottom) {
            if (position == dataList.size()) {
                ((CommonHolder) holder).bindBottomData();
            } else {
                ((CommonHolder) holder).bindData(dataList.get(position), position);
            }
        } else {
            ((CommonHolder) holder).bindData(dataList.get(position), position);
        }
        ((CommonHolder) holder).setOnNotifyChangeListener(this);
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    @Override
    public int getItemCount() {
        if (enableHead && enableBottom) {
            return dataList.size() + 2;
        }
        if (enableHead) {
            return dataList.size() + 1;
        }
        if (enableBottom) {
            return dataList.size() + 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (enableHead && enableBottom) {
            if (position == 0) {
                return TYPE_HEAD;
            } else if (position >= dataList.size() + 1) {
                return TYPE_Bottom;
            } else {
                return TYPE_CONTENT;
            }
        } else if (enableHead) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_CONTENT;
            }
        } else if (enableBottom) {
            if (position >= dataList.size()) {
                return TYPE_Bottom;
            } else {
                return TYPE_CONTENT;
            }
        } else {
            return TYPE_CONTENT;
        }
    }

    private int lastAnimatedPosition = -1;
    protected boolean animationsLocked = true;
    private boolean delayEnterAnimation = true;

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(DensityUtil.dip2px(view.getContext(), 100));//(position+1)*50f
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    }).start();
        }
    }

    /**
     * 开启动画效果
     */
    public void openAnimate() {
        animationsLocked = false;
    }

    @Override
    public void onNotify() {
        //提供给CommonHolder方便刷新视图
        notifyDataSetChanged();
    }

    public void setDataList(List<T> datas) {
        dataList.clear();
        if (null != datas) {
            dataList.addAll(datas);
        }
        notifyDataSetChanged();
    }


    public void clearDatas() {
        dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取插入适配器中所有的数据
     *
     * @return
     */
    public List<T> getdata() {
        return dataList;
    }


    /**
     * 添加数据到前面
     */
    public void addItemsAtFront(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(0, datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到尾部
     */
    public void addItems(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加单条数据
     */
    public void addItem(T data) {
        if (null == data) return;
        dataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 删除单条数据
     */
    public void deletItem(T data) {
        dataList.remove(data);
        Log.d("deletItem: ", dataList.remove(data) + "");
        notifyDataSetChanged();
    }

    /**
     * 删除单条数据
     */
    public void deletItem(int position) {
        dataList.remove(position);
        Log.d("deletItem: ", position + "");
        notifyDataSetChanged();
    }

    /**
     * 添加单条数据
     */
    public void addItemToPosition(T data, int position) {
        if (null == data) return;
        dataList.add(position, data);
        notifyDataSetChanged();
    }

    /**
     * 设置是否显示head
     *
     * @param ifEnable 是否显示头部
     */
    public void setEnableHead(boolean ifEnable) {
        enableHead = ifEnable;
    }

    public void setHeadHolder(CommonHolder headHolder1) {
        enableHead = true;
        headHolder = headHolder1;
    }

    public void setHeadHolder(View itemView) {
        enableHead = true;
        headHolder = new HeadHolder(itemView);
        notifyItemInserted(0);
    }

    public CommonHolder getHeadHolder() {
        return headHolder;
    }

    /**
     * 设置是否显示Bottom
     *
     * @param ifEnable 是否显示底部布局
     */
    public void setEnableBottom(boolean ifEnable) {
        enableBottom = ifEnable;
    }

    public void setBottomHolder(CommonHolder headHolder1) {
        enableBottom = true;
        BottomHolder = headHolder1;
    }

    public void setBottomHolder(View itemView) {
        enableBottom = true;
        BottomHolder = new BottomHolder(itemView);
        if (enableHead) {
            notifyItemInserted(dataList.size() + 1);
        } else {
            notifyItemInserted(dataList.size());
        }
    }

    public CommonHolder getBottomHolder() {
        return BottomHolder;
    }

    /**
     * 子类重写实现自定义ViewHolder
     */
    public abstract CommonHolder<T> setViewHolder(ViewGroup parent);

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public class HeadHolder extends CommonHolder<Void> {
        public HeadHolder(View itemView) {
            super(itemView);
        }

        public HeadHolder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        public void bindData(Void aVoid, int position) {//不用实现
        }
    }

    public class BottomHolder extends CommonHolder<Void> {
        public BottomHolder(View itemView) {
            super(itemView);
        }

        public BottomHolder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        public void bindData(Void aVoid, int position) {//不用实现
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private OnItemClickListener itemClickListener;
}