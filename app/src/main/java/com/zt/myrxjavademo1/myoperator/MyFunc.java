package com.zt.myrxjavademo1.myoperator;

import rx.functions.Function;

/**
 * Represents a function with no argument.
 *
 * @author ten-z
 */
public interface MyFunc<T> extends Function {
    void call();
}
