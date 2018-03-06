package com.sign.injectdemo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cys on 2018/3/1 0001.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    int value();

    boolean click() default false;
}
