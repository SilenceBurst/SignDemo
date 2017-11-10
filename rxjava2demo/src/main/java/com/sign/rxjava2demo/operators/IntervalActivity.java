package com.sign.rxjava2demo.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sign.rxjava2demo.R;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class IntervalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);

        Flowable.interval(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(@NonNull Long aLong) throws Exception {
                        return null;
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object object) throws Exception {

                    }
                });
    }
}
