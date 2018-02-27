/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.lidroid.xutils.annotation;

import com.lidroid.xutils.view.ResType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResInject {
    int id();

    ResType type();
}
