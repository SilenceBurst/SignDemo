package com.sign.signview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.sign.signview.R;
import com.sign.signview.view.RoundProgressBar;

public class RoundProgressbarActivity extends AppCompatActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 100) {
                progress = 0;
            }
            progressBar.setProgress(msg.arg1);
        }
    };
    private RoundProgressBar progressBar;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_progressbar);
        progressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(100);
                    Message message = new Message();
                    progress++;
                    message.arg1 = progress;
                    handler.sendMessage(message);
                }
            }
        }).start();

    }

}
