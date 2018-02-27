/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.lidroid.xutils.event;

import android.widget.ExpandableListView;

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
        listenerType = ExpandableListView.OnGroupClickListener.class,
        listenerSetter = "setOnGroupClickListener",
        methodName = "onGroupClick")
public @interface OnGroupClick {
    int[] value();

    int[] parentId() default 0;
}
