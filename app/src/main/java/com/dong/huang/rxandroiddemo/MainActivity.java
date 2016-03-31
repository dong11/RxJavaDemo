package com.dong.huang.rxandroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Error!");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "Next:" + s);
            }
        };

        //Subscriber是Observer的实现抽象类
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "----->" + s);
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
                subscriber.onCompleted();
            }
        });

//        observable.subscribe(subscriber);

        //以上可以简化为：
        Observable observable1 = Observable.just("Hello,World!");
        Action1<String> onNext = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "----->" + s);
            }
        };
        observable1.subscribe(onNext);

        //--->再简化
        Observable.just("Hello,World!").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "---->" + s);
            }
        });

        //改变对订阅者的修改
        Observable.just("Hello,World!")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "-dong";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(TAG, "---->" + s);
                    }
                });
    }
}
