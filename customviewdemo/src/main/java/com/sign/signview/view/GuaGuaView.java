package com.sign.signview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sign.signview.R;

/*
 * 项目名:    SignView
 * 包名       com.sign.signview.view
 * 文件名:    GuaGuaView
 * 创建者:    CYS
 * 创建时间:  2017/9/12 0012 on 17:32
 * 描述:     刮刮卡
 * 1.两张画布，底部画布上写中奖信息，顶层画布画出圆角、灰色背景、刮刮卡图片（drawBitmap以适合比例显示）
 * 2.给setXfermode，只在源图像和目标图像相交的地方绘制透明
 * 3.getPixels获取图像上的所有像素值，并计算比例，到达阈值全部显示，并给用户提示
 */
public class GuaGuaView extends View {
    private String mText;
    private Rect mTextRect;
    private Paint mTextPaint;
    private int mTextSize;
    private int mTextColor;

    private Paint mTopPaint;
    private Bitmap topBitmap;
    private Canvas mCanvas;

    //手指的路径
    private Path mPath;
    //手指上次停留的位置
    private float mLastX;
    private float mLastY;

    //用户是否刮到阈值
    private volatile boolean mIsComplete = false;
    private OnGuaGuaCompleteListener onGuaGuaCompleteListener;
    private PorterDuffXfermode porterDuffXfermode;

    public void setOnGuaGuaCompleteListener(OnGuaGuaCompleteListener onGuaGuaCompleteListener) {
        this.onGuaGuaCompleteListener = onGuaGuaCompleteListener;
    }

    /**
     * 抬起手指时，若刮开区域大于阈值，给用户提示
     */
    public interface OnGuaGuaCompleteListener {
        void onComplete();
    }

    public GuaGuaView(Context context) {
        this(context, null);
    }

    public GuaGuaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaGuaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray typedArray = null;
        try {
            typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GuaGuaView, defStyleAttr, 0);

            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.GuaGuaView_text:
                        mText = typedArray.getString(attr);
                        break;
                    case R.styleable.GuaGuaView_textSize:
                        //22sp转化为像素值
                        mTextSize = (int) typedArray.getDimension(attr,
                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.GuaGuaView_textColor:
                        mTextColor = typedArray.getColor(attr, 0x000000);
                        break;
                }
            }
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
        } finally {
            if (typedArray != null)
                typedArray.recycle();
        }
    }

    private void init() {
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mText = "谢谢惠顾";
        mTextRect = new Rect();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, getResources().getDisplayMetrics());
        mTextPaint.setStyle(Paint.Style.STROKE);

        mTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTopPaint.setColor(Color.parseColor("#c0c0c0"));
        mTopPaint.setStyle(Paint.Style.FILL);
        mTopPaint.setDither(true);
        mTopPaint.setStrokeCap(Paint.Cap.ROUND);
        mTopPaint.setStrokeJoin(Paint.Join.ROUND);
        mTopPaint.setStrokeWidth(30);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获得文本的边界信息
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        /**
         * text 要绘制的文字
         * x 文字左端坐标
         * y 文字基线y值
         * paint 画笔
         */
        canvas.drawText(mText, getWidth() / 2 - mTextRect.width() / 2, getHeight() / 2 + mTextRect.height() / 2, mTextPaint);

        if (!mIsComplete) {
            mTopPaint.setStyle(Paint.Style.STROKE);
            //只在源图像和目标图像相交的地方绘制透明
            mTopPaint.setXfermode(porterDuffXfermode);
            mCanvas.drawPath(mPath, mTopPaint);
            //还原混合模式
            mTopPaint.setXfermode(null);

            canvas.drawBitmap(topBitmap, 0, 0, null);
        } else {
            if (onGuaGuaCompleteListener != null) {
                onGuaGuaCompleteListener.onComplete();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - mLastX);
                float dy = Math.abs(event.getY() - mLastY);
                //手指移动的距离过小，不擦除
                if (dx > 5 || dy > 5) {
                    mLastX = event.getX();
                    mLastY = event.getY();
                    mPath.lineTo(mLastX, mLastY);
                }
                if (!mIsComplete) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsComplete) {
                    new Thread(mRunnable).start();
                }
                break;
        }
        return true;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int width = getWidth();
            int height = getHeight();

            int[] pixels = new int[width * height];
            float wipeArea = 0;
            float totalArea = width * height;

            /**
             * getPixels()函数把一张图片，从指定的偏移位置（offset），指定的位置（x,y）截取指定的宽高（width,height）
             * 把所得图像的每个像素颜色转为int值，存入pixels。
             * stride 参数指定在行之间跳过的像素的数目。图片是二维的，存入一个一维数组中，那么就需要这个参数来指定多少个像素换一行。
             * 详见：http://blog.csdn.net/xx326664162/article/details/52240795
             */
            topBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int index = i + j * width;
                    if (pixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }

            float progress = wipeArea / totalArea;
            Log.d("test", "progress-----" + progress);
            if (progress > 0.6) {
                mIsComplete = true;
                postInvalidate();
            }

        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        topBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(topBitmap);
        Bitmap bottomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fg_guaguaka);
        //将图片以合适比例显示
        //先画灰色背景，再画图片
        mCanvas.drawRoundRect(new RectF(0, 0, w, h), 50, 50, mTopPaint);

        /**
         * bitmap 要绘制的位图对象
         * src 要裁切的位置，若是null则显示整张图片
         * dst 裁切后的图片显示的区域
         * paint 画笔
         */
        mCanvas.drawBitmap(bottomBitmap, null, new RectF(0, 0, w, h), null);
    }

    //设置中奖信息
    public void setText(String mText) {
        this.mText = mText;
    }
}
