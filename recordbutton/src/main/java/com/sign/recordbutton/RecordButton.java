package com.sign.recordbutton;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 录音按钮
 * Created by xuan on 2016/6/8.
 */
public class RecordButton extends Button {
    private final int Volume_What_100 = 100;
    private final int Time_What_101 = 101;
    private final int CancelRecordWhat_102 = 102;
    private String mFilePath = "";
    private String mFileName = System.currentTimeMillis() + ".mp3";
    private OnFinishedRecordListener finishedListener;
    private long mStartTime;
    private Dialog mDialog;
    private ImageView mImageView;
    private TextView mTitleTv, mTimeTv;
    private MediaRecorder mRecorder;
    private ObtainDecibelThread mthread;
    private Handler mVolumeHandler;
    /**
     * 文件路径
     **/
    private String mFile;
    /**
     * 最短录音时间
     **/
    private int MIN_INTERVAL_TIME = 1000;
    /**
     * 最长录音时间
     **/
    private int MAX_INTERVAL_TIME = 1000 * 60;
    /**
     * 默认上滑取消距离
     */
    private int CANCLE_LENGTH = -200;
    /**
     * 文件名前缀
     */
    private String mPrefix = "";
    /***
     * 时间太短提示语
     */
    private String tooShortToastMessage = "";


    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 保存路径，为空取默认值
     *
     * @param path
     */
    public void setSavePath(String path) {
        if (!TextUtils.isEmpty(path)) {
            mFilePath = path;
        }
    }

    /**
     * 设置默认路径
     *
     * @return
     */
    private String getDefaultPath() {
        return getContext().getExternalFilesDir("audio").getAbsolutePath();
    }

    /***
     * 设置默认名字
     *
     * @return
     */
    private String getDefaultName() {
        return mPrefix + "_" + UUID.randomUUID().toString();
    }

    /****
     * 设置最大时间
     *
     * @param time 单位毫秒
     */
    public void setMaxIntervalTime(int time) {
        MAX_INTERVAL_TIME = time;
    }

    /**
     * 设置最短录音时间
     *
     * @param time 时间毫秒
     */
    public void setMinIntervalTime(int time) {
        MIN_INTERVAL_TIME = time;
    }


    public void setTooShortToastMessage(String pTooShortToastMessage) {
        tooShortToastMessage = pTooShortToastMessage;
    }

    /**
     * 录音完成的回调
     *
     * @param listener
     */
    public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
        finishedListener = listener;
    }


    private void init() {
        mVolumeHandler = new ShowVolumeHandler();
    }

    int startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getContext() instanceof Activity) {
            if (!PermissionUtils.checkPermission(getContext(), Manifest.permission.RECORD_AUDIO, 100)) {
                return true;
            }
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (finishedListener != null) {
                    finishedListener.onActionDown();
                }
                startY = (int) event.getY();
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
                if (finishedListener != null) {
                    finishedListener.onActionUp();
                }
                int endY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (endY - startY < CANCLE_LENGTH) {
                    cancelRecord();
                } else {
                    finishRecord();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (finishedListener != null) {
                    finishedListener.onActionMove();
                }
                int tempNowY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (tempNowY - startY < CANCLE_LENGTH) {
                    mTitleTv.setText("松开手指，取消发送");
                    if (finishedListener != null) {
                        finishedListener.readCancel();
                    }
                } else {
                    mTitleTv.setText("手指上滑，取消发送");
                    if (finishedListener != null) {
                        finishedListener.noCancel();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cancelRecord();
                break;
        }

        return true;
    }

    private void initDialogAndStartRecord() {
        CANCLE_LENGTH = -(this.getMeasuredHeight() / 2);
        //
        String tempFilePath = "";
        if (TextUtils.isEmpty(mFilePath)) {
            tempFilePath = getDefaultPath();
        } else {
            tempFilePath = mFilePath;
        }
        String tempFileName = "";
        if (TextUtils.isEmpty(mFileName)) {
            tempFileName = getDefaultName();
        } else {
            tempFileName = mFileName;
        }
        mFile = tempFilePath + "/" + tempFileName;
        mStartTime = System.currentTimeMillis();
        mDialog = new Dialog(getContext(), R.style.recordbutton_alert_dialog);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_recordbutton_alert_dialog, null);
        mImageView = (ImageView) contentView.findViewById(R.id.dialog_imageview);
        mTimeTv = (TextView) contentView.findViewById(R.id.dialog_time_tv);
        mTitleTv = (TextView) contentView.findViewById(R.id.dialog_title_tv);
        mDialog.setContentView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.setOnDismissListener(onDismiss);
        startRecording();
        mDialog.show();
    }

    private void finishRecord() {
        stopRecording();
        mDialog.dismiss();
        long intervalTime = System.currentTimeMillis() - mStartTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            if (TextUtils.isEmpty(tooShortToastMessage)) {
                Toast.makeText(getContext(), "时间太短", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), tooShortToastMessage, Toast.LENGTH_SHORT).show();
            }
            File file = new File(mFile);
            if (file.exists())
                file.delete();
            return;
        }

        if (finishedListener != null)
            finishedListener.onFinishedRecord(mFile);
    }

    private void cancelRecord() {
        stopRecording();
        mDialog.dismiss();
        File file = new File(mFile);
        if (file.exists())
            file.delete();
    }

    private void startRecording() {
        if (mRecorder != null) {
            mRecorder.reset();
        } else {
            mRecorder = new MediaRecorder();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFile);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
        mthread = new ObtainDecibelThread();
        mthread.start();

    }

    private void stopRecording() {
        if (mthread != null) {
            mthread.exit();
            mthread = null;
        }
        if (mRecorder != null) {
            try {
                mRecorder.stop();//停止时没有prepare，就会报stop failed
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            } catch (RuntimeException pE) {
                pE.printStackTrace();
            } finally {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mRecorder == null || !running) {
                    break;
                }
                if (System.currentTimeMillis() - mStartTime >= MAX_INTERVAL_TIME) {
                    // 如果超过最长录音时间
                    mVolumeHandler.sendEmptyMessage(CancelRecordWhat_102);
                }
                //发送时间
                mVolumeHandler.sendEmptyMessage(Time_What_101);
                //
                int x = mRecorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (20 * Math.log(x) / Math.log(10));
                    Message msg = new Message();
                    msg.obj = f;
                    msg.what = Volume_What_100;
                    mVolumeHandler.sendMessage(msg);
                }

            }
        }

    }

    private DialogInterface.OnDismissListener onDismiss = new DialogInterface.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            stopRecording();
        }
    };

    class ShowVolumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Volume_What_100:
                    int tempVolumeMax = (int) msg.obj;
                    setLevel(tempVolumeMax);
                    break;
                case Time_What_101:
                    long nowTime = System.currentTimeMillis();
                    int time = ((int) (nowTime - mStartTime) / 1000);
                    int second = time % 60;
                    int mil = time / 60;
                    if (mil < 10) {
                        if (second < 10)
                            mTimeTv.setText("0" + mil + ":0" + second);
                        else
                            mTimeTv.setText("0" + mil + ":" + second);
                    } else if (mil >= 10 && mil < 60) {
                        if (second < 10)
                            mTimeTv.setText(mil + ":0" + second);
                        else
                            mTimeTv.setText(mil + ":" + second);
                    }
                    break;
                case CancelRecordWhat_102:
                    finishRecord();
                    break;
            }
        }
    }

    private void setLevel(int level) {
        if (mImageView != null)
            mImageView.getDrawable().setLevel(4000 + 6000 * level / 90);
    }

    public interface OnFinishedRecordListener {
        void onFinishedRecord(String audioPath);

        /**
         * 手指上滑，准备取消录音
         **/
        void readCancel();

        /**
         * 手指回退，准备继续录音
         **/
        void noCancel();

        /***
         * 手指按下回调
         */
        void onActionDown();

        /***
         * 手指抬起回调
         */
        void onActionUp();

        /***
         * 手指移动回调
         */
        void onActionMove();

    }
}
