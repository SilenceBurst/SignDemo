package com.sign.signview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview.view
 * 文件名:    MyCardView
 * 创建者:    CYS
 * 创建时间:  2017/9/12 0012 on 14:45
 * 描述:     边缘凹凸卡片
 */
public class MyCardView extends LinearLayout {
    private Paint mPaint;
    private int radius = 30;
    private int margin = 30;

    private int mWidth;
    private int mHeight;

    public MyCardView(Context context) {
        this(context, null);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int num = mWidth / (2 * margin + 2 * radius);//一行画多少个半圆
        //左右边缘空出来不画圆，且相等
        float last = (mWidth - num * (2 * radius) - (num - 1) * 2 * margin) / 2;
        for (int i = 0; i < num; i++) {
            canvas.drawCircle(last + 2 * i * (radius + margin) + radius, 0, radius, mPaint);
            canvas.drawCircle(last + 2 * i * (radius + margin) + radius, mHeight, radius, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
