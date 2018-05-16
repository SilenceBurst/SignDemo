package com.sign.itemtouchhelperdemo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by cys on 2018/5/15 0015.
 */

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private ItemMoveListener itemMoveListener;

    public MyItemTouchHelperCallback(ItemMoveListener itemMoveListener) {
        this.itemMoveListener = itemMoveListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //列表滚动方向的动作标识 eg：滚动方向为竖直，即上下
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //与列表滚动方向垂直方向的动作标识 eg：滚动方向为竖直，即左右
        int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     *
     * @param recyclerView
     * @param viewHolder 被拖拽的条目的ViewHolder
     * @param target 被拖拽条目下面条目ViewHolder
     * @return ToDo
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return itemMoveListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * 是否开启item长按拖拽功能
     *
     * @return 默认为true
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }
}
