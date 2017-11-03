package com.sign.signview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview.view
 * 文件名:    WaveView
 * 创建者:    CYS
 * 创建时间:  2017/9/11 0011 on 11:57
 * 描述:     水波纹
 */
public class WaveView extends View {
    private int mWidth;
    private int mHeight;
    private PointF pointStart;
    private Paint mPaint;
    private Path wavePath;
    private int cycle = 90;//1/4振幅的宽度
    private int waveHeight = 100;//控制点到正弦曲线x轴的距离

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        pointStart = new PointF(-4 * cycle, mHeight / 2);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);

        wavePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        wavePath.reset();
        wavePath.moveTo(pointStart.x, pointStart.y);
        int j = 1;
        for (int i = 1; i < 9; i++) {
            if (i % 2 == 0) {
                wavePath.quadTo(pointStart.x + cycle * (2 * i - 1), pointStart.y + waveHeight, pointStart.x + cycle * 2 * i, pointStart.y);
            } else {
                wavePath.quadTo(pointStart.x + cycle * (2 * i - 1), pointStart.y - waveHeight, pointStart.x + cycle * 2 * i, pointStart.y);
            }
            j = j + 2;
        }
        wavePath.lineTo(mWidth, mHeight);
        wavePath.lineTo(pointStart.x, mHeight);
        wavePath.lineTo(pointStart.x, pointStart.y);

        canvas.drawPath(wavePath, mPaint);

        if (pointStart.x == 0) {
            pointStart.x = -4 * cycle;
        }
        pointStart.x += 30;
        postInvalidateDelayed(150);
    }
}
