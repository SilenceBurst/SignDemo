package com.sign.verticalviewpagerdemo;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 项目名:    ServerMonitor
 * 包名:      com.hjtech.servermonitor
 * 文件名:    MyPageTransformer
 * 创建者:    CYS
 * 创建时间:  2017/11/9 0009 on 9:27
 * 描述:     viewpager切换动画
 */
public class MyPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationY(vertMargin - horzMargin / 2);
            } else {
                view.setTranslationY(-vertMargin + horzMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
