package com.zt.myrxjavademo1.myaspectj;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 利用Aspectj注入代码，在subscribeOn()切换线程后输出线程名
 * 这里用的沪江的gradle_plugin_android_aspectjx<a href="https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx">，具体使用方式就不再赘述了
 *
 * @author ten-z
 */
@Aspect
public class MyAspectTest {
    private static final String TAG = "tag";

    //连接点可选择的地方很多，这里选择subscribeOn()执行切换线程后，调用SubscribeOnSubscriber.call之前切入，输出切换后的线程名
    @Before("execution(* rx.internal.operators.OperatorSubscribeOn.SubscribeOnSubscriber.call**(..))")
    public void newMyAspect(JoinPoint joinPoint) throws Throwable {
        Log.d(TAG, " --> Current Thread == " + Thread.currentThread().getName());
    }
}
