package com.sign.rxjava2demo.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sign.rxjava2demo.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        txt = (TextView) findViewById(R.id.txt_map);
    }

    public void map(View view) {
        Observable.create(new ObservableOnSubscribe<FirstType>() {

            @Override
            public void subscribe(ObservableEmitter<FirstType> e) throws Exception {
                Log.d("test", "subscribe      " + e.isDisposed());
                //运行在后台线程，子线程不能动态更新UI，这行代码会出错
                //txt.append("subscribe      " + e.isDisposed() + "\n");
                if (!e.isDisposed()) {
                    e.onNext(new FirstType("James"));
                    e.onNext(new FirstType("Durant"));
                    e.onComplete();
                }
            }
        })
                //在后台线程上运行 主要改变的是订阅的线程，subscribe()执行的线程
                .subscribeOn(Schedulers.io())
                //在主线程上通知 主要改变的是发送的线程，onNext()执行的线程
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<FirstType, SecondType>() {
                    @Override
                    public SecondType apply(@NonNull FirstType firstType) throws Exception {
                        return new SecondType(firstType.FirstName);
                    }
                })
                .subscribe(new Observer<SecondType>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        txt.append("onSubscribe      " + d.isDisposed() + "\n");
                        Log.d("test", "onSubscribe   d.isDisposed()     " + d.isDisposed());
                    }

                    @Override
                    public void onNext(SecondType secondType) {
                        txt.append("onNext      " + secondType.secondName + "\n");
                        Log.d("test", "onNext  " + secondType.secondName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        txt.append("onError" + "\n");
                        Log.d("test", "onError " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        txt.append("onComplete" + "\n");
//                        CompositeDisposable
                        Log.d("test", "onComplete ");
                    }
                });
    }

    class FirstType {
        String FirstName;

        public FirstType(String firstName) {
            FirstName = firstName;
        }
    }

    class SecondType {
        String secondName;

        public SecondType(String secondName) {
            this.secondName = secondName;
        }

    }
}

