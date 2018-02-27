/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.lidroid.xutils.event;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wyouflf
 * Date: 13-8-16
 * Time: 下午2:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(
        listenerType = View.OnFocusChangeListener.class,
        listenerSetter = "setOnFocusChangeListener",
        methodName = "onFocusChange")
public @interface OnFocusChange {
    int[] value();

    int[] parentId() default 0;
}
