package com.zt.myrxjavademo1.myoperator;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.internal.operators.OnSubscribeCreate;
import rx.internal.operators.OnSubscribeMap;
import rx.internal.operators.OperatorSubscribeOn;
import rx.plugins.RxJavaHooks;

/**
 * 通过继承的方式，在MyObservable实现自定义myOperator操作符
 *
 * @author ten-z
 */
public class MyObservable<T> extends Observable {

    OnSubscribe onSubscribe;

    protected MyObservable(OnSubscribe f) {
        super(f);
        onSubscribe = f;
    }

    /**
     * 自定义操作符，在"上行"事件触发流中执行MyFunc中的call方法。
     * 在mySubscribeOn之前使用，可以观察到mySubscribeOn在上行流时切换的每个线程。
     * 也可执行事件下发前的其他预操作。
     */
    public final MyObservable<T> myOperator(MyFunc<? super T> func) {
        return myCreate(new MyOnSubscribe<T>(this, func));
    }

    /**
     * 下面的myCreate、mySubscribeOn、myMap和Observable中的各方法实现是一样的
     * 为了避免调用myOperator时向下转型丢{@code ClassCastException}，
     * 这里把myOperator之前调用到的操作符全部重新实现了一遍，仅仅是返回对象换成了子类MyObservable
     */
    public static <T> MyObservable<T> myCreate(OnSubscribe<T> f) {
        return new MyObservable<T>(RxJavaHooks.onCreate(f));
    }

    public final MyObservable<T> mySubscribeOn(Scheduler scheduler) {
        return mySubscribeOn(scheduler, !(onSubscribe instanceof OnSubscribeCreate));
    }

    public final MyObservable<T> mySubscribeOn(Scheduler scheduler, boolean requestOn) {
        return myCreate(new OperatorSubscribeOn<T>(this, scheduler, requestOn));
    }

    public final <R> MyObservable<R> myMap(Func1<? super T, ? extends R> func) {
        return myCreate(new OnSubscribeMap<T, R>(this, func));
    }
}
