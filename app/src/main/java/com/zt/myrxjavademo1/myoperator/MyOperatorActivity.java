package com.zt.myrxjavademo1.myoperator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zt.myrxjavademo1.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 通过使用自定义Rx操作符
 * 实现在"上行"事件触发流中输出当前线程名
 * 可以验证subscribeOn在"上行"事件触发流中切换的每个线程，也可执行事件下发前的其他预操作。
 * 如果有多个subscribeOn，离create（事件下发）最近的那个线程为事件下发执行的线程，直至observeOn切换至其他线程为止
 *
 * @author ten-z
 */
public class MyOperatorActivity extends AppCompatActivity {

    String s = "";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        textView = findViewById(R.id.tv_show);

        useMyOperator();
    }

    private void useMyOperator() {
        MyObservable.myCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //将线程日志作为事件下发
                Log.d("tag", "Observable.create thread is " + Thread.currentThread().getName());
                subscriber.onNext("Observable.create thread is " + Thread.currentThread().getName() + "\n");
                subscriber.onCompleted();
            }
        })
                .myMap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //每个map操作符在事件后加上自己所在线程的日志
                        Log.d("tag", "first map thread is " + Thread.currentThread().getName());
                        return s + "first map thread is " + Thread.currentThread().getName() + "\n";
                    }
                })
                .mySubscribeOn(Schedulers.newThread())

                //自定义操作符，在"上行"事件触发流中执行call中的代码
                //这里是输出了当前线程，也可执行事件下发前的其他预操作。
                .myOperator(new MyFunc() {
                    @Override
                    public void call() {
                        Log.d("tag", "myOperator thread is " + Thread.currentThread().getName());
                        s += "myOperator thread is " + Thread.currentThread().getName() + "\n";
                    }
                })

                .myMap(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d("tag", "second map thread is " + Thread.currentThread().getName());
                        return s + "second map thread is " + Thread.currentThread().getName() + "\n";
                    }
                })
                .mySubscribeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d("tag", "third map thread is " + Thread.currentThread().getName());
                        return s + "third map thread is " + Thread.currentThread().getName() + "\n";
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d("tag", "forth map thread is " + Thread.currentThread().getName());
                        return s + "third map thread is " + Thread.currentThread().getName() + "\n";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        Log.d("tag", "onCompleted:");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d("tag", "onNext thread is " + Thread.currentThread().getName());
                        s += (String) o.toString() + "onNext thread is " + Thread.currentThread().getName();
                        textView.setText(s);
                    }
                });
    }
}
