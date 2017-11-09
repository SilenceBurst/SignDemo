package com.sign.verticalviewpagerdemo;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * ViewPager 滚动速度设置
 */
public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 2000;             // 滑动速度

    /**
     * 设置速度速度
     *
     * @param duration
     */
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
    }

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }


    public void initViewPagerScroll(VerticalViewPager viewPager) {
        try {
            Field mScroller = VerticalViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
