package com.sign.rxjava2demo.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sign.rxjava2demo.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JustActivity extends AppCompatActivity {

    private TextView txt;
    private Disposable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just);
        txt = (TextView) findViewById(R.id.txt_just);
    }

    public void just(View view) {
        Observable.just("Kobe", "Curry")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        JustActivity.this.d = d;
                        txt.append("onSubscribe d.isDisposed        " + d.isDisposed() + "\n");
                    }

                    @Override
                    public void onNext(String s) {
                        txt.append("onNext        " + s + "\n");
                        d.dispose();
                        txt.append("onNext d.isDisposed        " + d.isDisposed() + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        txt.append("onError" + e.getMessage() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        txt.append("onComplete" + "\n");
                    }
                });
    }
}
