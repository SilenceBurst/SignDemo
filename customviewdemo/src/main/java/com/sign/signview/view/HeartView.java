package com.sign.signview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sign.signview.R;

/*
 * 项目名:    PathDemo
 * 包名       com.sign.pathdemo
 * 文件名:    PathView
 * 创建者:    CYS
 * 创建时间:  2017/8/25 0025 on 16:57
 * 描述:     丘比特之箭 咻咻咻
 */
public class HeartView extends View {
    private Paint paint;
    private Path innerCircle;//左心路径
    private Path outerCircle;//右心路径
    private Path line;//水平线路径
    private Path arrowOn;//上箭头路径
    private Path arrowUnder;//下箭头路径
    private Path drawPath;//截取的路径
    private PathMeasure pathMeasure;
    private Handler mHandler;
    private State mCurrentState = State.CIRCLE_STATE;
    private float distance;//当前动画执行的百分比取值为0-1
    private ValueAnimator valueAnimator;
    private ValueAnimator valueAnimator2;
    private long duration = 3000;
    private long duration2 = 1000;

    //三个阶段的枚举
    private enum State {
        CIRCLE_STATE,//心形阶段
        TRANGLE_STATE,//箭头阶段
        FINISH_STATE//完成阶段
    }

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        canvas.drawText("情人节快乐", 150, 800, paint);
        canvas.save();
        switch (mCurrentState) {
            case CIRCLE_STATE:
                //画左心
                drawPath.reset();
                pathMeasure.setPath(innerCircle, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                //画右心
                drawPath.reset();
                pathMeasure.setPath(outerCircle, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;
            case TRANGLE_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                //画水平线
                drawPath.reset();
                pathMeasure.setPath(line, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;
            case FINISH_STATE:
                canvas.drawPath(innerCircle, paint);
                canvas.drawPath(outerCircle, paint);
                canvas.drawPath(line, paint);
                //画上箭头
                drawPath.reset();
                pathMeasure.setPath(arrowOn, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                //画下箭头
                drawPath.reset();
                pathMeasure.setPath(arrowUnder, false);
                pathMeasure.getSegment(0, distance * pathMeasure.getLength(), drawPath, true);
                canvas.drawPath(drawPath, paint);
                break;

        }
        canvas.restore();
    }

    private void init() {
        initPaint();

        initPath();

        initHandler();

        initAnimator();

        mCurrentState = State.CIRCLE_STATE;

        valueAnimator.start();
    }

    private void initAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        valueAnimator2 = ValueAnimator.ofFloat(0, 1).setDuration(duration2);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (mCurrentState) {
                    case CIRCLE_STATE:
                        mCurrentState = State.TRANGLE_STATE;
                        valueAnimator2.start();
                        break;
                    case TRANGLE_STATE:
                        mCurrentState = State.FINISH_STATE;
                        valueAnimator2.start();
                        break;
                }

            }
        };
    }

    private void initPath() {
        innerCircle = new Path();
        outerCircle = new Path();
        arrowOn = new Path();
        arrowUnder = new Path();
        drawPath = new Path();
        line = new Path();

        innerCircle.addArc(50, 150, 200, 350, -225, 225);
        innerCircle.arcTo(200, 150, 350, 350, -180, 225, false);
        innerCircle.lineTo(200, 500);
        innerCircle.close();

        outerCircle.addArc(300, 150, 450, 350, -225, 225);
        outerCircle.arcTo(450, 150, 600, 350, -180, 225, false);
        outerCircle.lineTo(450, 500);
        outerCircle.close();

        line.moveTo(20, 300);
        line.lineTo(650, 300);

        arrowOn.moveTo(650, 300);
        arrowOn.lineTo(630, 325);

        arrowUnder.moveTo(650, 300);
        arrowUnder.lineTo(630, 275);

        pathMeasure = new PathMeasure();
        pathMeasure.setPath(innerCircle, false);
    }

    //初始化画笔
    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(100);
        paint.setStrokeWidth(9);
        paint.setStrokeCap(Paint.Cap.ROUND);//圆头线条
        paint.setStrokeJoin(Paint.Join.BEVEL);//拐角为平角
        paint.setShadowLayer(9, 0, 0, Color.WHITE);//绘制内容下加一层阴影
    }
}
