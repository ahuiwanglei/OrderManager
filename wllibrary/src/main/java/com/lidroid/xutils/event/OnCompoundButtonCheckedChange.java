/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.lidroid.xutils.event;

import android.widget.CompoundButton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wyouflf
 * Date: 13-8-16
 * Time: 下午2:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(
        listenerType = CompoundButton.OnCheckedChangeListener.class,
        listenerSetter = "setOnCheckedChangeListener",
        methodName = "onCheckedChanged")
public @interface OnCompoundButtonCheckedChange {
    int[] value();

    int[] parentId() default 0;
}
