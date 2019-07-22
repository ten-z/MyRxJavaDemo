package com.zt.myrxjavademo1.myoperator;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.plugins.RxJavaHooks;

/**
 * myOperator中传入的OnSubscribe，在逻辑和其他OnSubscribe基本一样，主要是MyFunc.call的调用时机是在MyOnSubscribe中，而不是内部类MySubscriber中。
 * 从而在"上行"事件触发流中执行MyFunc.call().
 *
 * @author ten-z
 */
public class MyOnSubscribe<T> implements Observable.OnSubscribe<T> {
    final Observable<T> source;

    final MyFunc func;

    public MyOnSubscribe(Observable<T> source, MyFunc func) {
        this.source = source;
        this.func = func;
    }

    @Override
    public void call(final Subscriber o) {
        MyOnSubscribe.MySubscriber<T> parent = new MyOnSubscribe.MySubscriber<T>(o);
        o.add(parent);
        try {
            func.call();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            return;
        }
        source.unsafeSubscribe(parent);
    }

    static final class MySubscriber<T> extends Subscriber<T> {

        final Subscriber actual;

        boolean done;

        public MySubscriber(Subscriber actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            if (done) {
                RxJavaHooks.onError(e);
                return;
            }
            done = true;

            actual.onError(e);
        }


        @Override
        public void onCompleted() {
            if (done) {
                return;
            }
            actual.onCompleted();
        }

        @Override
        public void setProducer(Producer p) {
            actual.setProducer(p);
        }
    }
}
