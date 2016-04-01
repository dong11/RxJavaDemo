package com.dong.huang.rxandroiddemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
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

        //打印集合元素
        String[] strs = {"Hello,World!", "Hello,World!", "Hello,World!"};
        Observable.from(strs).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "----->" + s);
            }
        });

        //利用Observable.timer来代替Handler的定时操作，3秒后自动跳转Activity
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        List<Student> students = new ArrayList<>();
        List<String> courses = new ArrayList<>();
        courses.add("语文");
        courses.add("数学");
        courses.add("英语");
        students.add(new Student("Zhang", courses));
        students.add(new Student("Li", courses));
        students.add(new Student("Rex", courses));

        //打印学生名字
        Observable.from(students).map(new Func1<Student, String>() {
            @Override
            public String call(Student student) {
                return student.getName();
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "----->" + s);
            }
        });

        //打印每位学生的课程
        Observable.from(students).subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                for (String str : student.getCourses()){
                    Log.i(TAG, "---str-->" + str);
                }
            }
        });

        //简化：不想在subscribe中做for循环操作
        Observable.from(students).flatMap(new Func1<Student, Observable<String>>() {
            @Override
            public Observable<String> call(Student student) {
                return Observable.from(student.getCourses());
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG, "---s-->" + s);
            }
        });
    }
}
