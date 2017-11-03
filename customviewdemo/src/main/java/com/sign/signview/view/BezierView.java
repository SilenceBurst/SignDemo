package com.sign.signview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
 * 描述:     贝塞尔曲线
 */
public class BezierView extends View {
    private int viewWidth;
    private int viewHeight;
    private PointF pointStart;
    private PointF pointEnd;
    private PointF pointControl;

    private Paint paintBezier;
    private Paint paintPoint;
    private Paint paintLine;
    private Path path;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();

        paintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBezier.setStyle(Paint.Style.STROKE);
        paintBezier.setStrokeWidth(5);
        paintBezier.setColor(Color.BLUE);

        paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setColor(Color.YELLOW);
        paintPoint.setStrokeWidth(20);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.RED);
        paintLine.setStyle(Paint.Style.FILL);
        paintLine.setStrokeWidth(5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointControl.x = event.getX();
        pointControl.y = event.getY();

        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画起始点和控制点
        canvas.drawPoint(pointStart.x, pointStart.y, paintPoint);
        canvas.drawPoint(pointEnd.x, pointEnd.y, paintPoint);
        canvas.drawPoint(pointControl.x, pointControl.y, paintPoint);
        //画辅助线
        canvas.drawLine(pointStart.x, pointStart.y, pointControl.x, pointControl.y, paintLine);
        canvas.drawLine(pointControl.x, pointControl.y, pointEnd.x, pointEnd.y, paintLine);
        //画贝塞尔曲线
        path.reset();
        path.moveTo(pointStart.x, pointStart.y);
        path.quadTo(pointControl.x, pointControl.y, pointEnd.x, pointEnd.y);
        canvas.drawPath(path, paintBezier);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;

        pointStart = new PointF(viewWidth / 4, viewHeight / 2);
        pointEnd = new PointF(viewWidth / 4 * 3, viewHeight / 2);
        pointControl = new PointF(viewWidth / 2, viewHeight / 4);
    }
}
