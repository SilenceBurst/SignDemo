package com.sign.signview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview.view
 * 文件名:    BezierView
 * 创建者:    CYS
 * 创建时间:  2017/9/11 0011 on 10:24
 * 描述:     笑脸
 */
public class FaceView extends View {

    private int viewWidth;
    private int viewHeight;

    private PointF pointStart;
    private PointF pointEnd;
    private PointF pointControl;
    private PointF pointLeft;//左眼控制点
    private PointF pointRight;//右眼控制点

    private Paint paintMouth;
    private Paint paintEye;

    private Path mouthPath;//嘴巴path
    private Path leftPath;//左眼path
    private Path rightPath;//右眼path
    private float[] center;//起始嘴巴最低点的坐标

    public FaceView(Context context) {
        this(context, null);
    }

    public FaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mouthPath = new Path();
        leftPath = new Path();
        rightPath = new Path();

        paintEye = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintEye.setStyle(Paint.Style.STROKE);
        paintEye.setStrokeWidth(8);
        paintEye.setColor(Color.BLACK);

        paintMouth = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMouth.setStyle(Paint.Style.STROKE);
        paintMouth.setStrokeWidth(5);
        paintMouth.setColor(Color.BLACK);

        //获取开始时嘴巴最低点的坐标
        Path mouth = new Path();
        mouth.moveTo(viewWidth / 4, viewHeight / 2);
        mouth.quadTo(viewWidth / 2, viewHeight / 4 * 3, viewWidth / 4 * 3, viewHeight / 2);

        center = new float[2];
        PathMeasure measure = new PathMeasure(mouth, false);
        measure.getPosTan(measure.getLength() / 2, center, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointControl.x = event.getX();
        pointControl.y = event.getY();
        //在标题栏上，event.getY()小于0
        if (pointControl.y > 0) {
            if (pointControl.y > center[1]) {
                pointLeft.y = pointLeft.y - ((pointControl.y - viewHeight / 2) / viewHeight / 2) * 80;
                pointRight.y = pointRight.y - ((pointControl.y - viewHeight / 2) / viewHeight / 2) * 80;
            } else {
                pointLeft.y = pointLeft.y + ((pointControl.y - viewHeight / 2) / viewHeight / 2) * 80;
                pointRight.y = pointRight.y + ((pointControl.y - viewHeight / 2) / viewHeight / 2) * 80;
            }

            //眼睛弯曲的最大、最小值
            if (pointLeft.y < viewHeight / 4 - 200) {
                pointLeft.y = viewHeight / 4 - 200;
                pointRight.y = viewHeight / 4 - 200;
            }
            if (pointLeft.y > viewHeight / 4 + 200) {
                pointLeft.y = viewHeight / 4 + 200;
                pointRight.y = viewHeight / 4 + 200;
            }

            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画左右眼
        leftPath.reset();
        rightPath.reset();
        leftPath.moveTo(pointLeft.x - 50, viewHeight / 4);
        leftPath.quadTo(pointLeft.x, pointLeft.y, pointLeft.x + 50, viewHeight / 4);

        leftPath.moveTo(pointRight.x - 50, viewHeight / 4);
        leftPath.quadTo(pointRight.x, pointRight.y, pointRight.x + 50, viewHeight / 4);

        //画嘴巴
        mouthPath.reset();
        mouthPath.moveTo(pointStart.x, pointStart.y);
        mouthPath.quadTo(pointControl.x, pointControl.y, pointEnd.x, pointEnd.y);

        canvas.drawPath(rightPath, paintEye);
        canvas.drawPath(leftPath, paintEye);
        canvas.drawPath(mouthPath, paintMouth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        pointLeft = new PointF(viewWidth / 4, viewHeight / 4 - 80);
        pointRight = new PointF(viewWidth / 4 * 3, viewHeight / 4 - 80);

        pointStart = new PointF(viewWidth / 4, viewHeight / 2);
        pointEnd = new PointF(viewWidth / 4 * 3, viewHeight / 2);
        pointControl = new PointF(viewWidth / 2, viewHeight / 4 * 3);
    }
}
