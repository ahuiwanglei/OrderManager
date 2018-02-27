/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity启动Intent工具类
 * Created by macchen on 15/3/26.
 */
public class IntentUtils {

    /**
     * 禁止构造
     */
    private IntentUtils() {
        throw new AssertionError();
    }

    /**
     * 启动界面
     *
     * @param context     应用上下文
     * @param toClass     跳转Activity
     * @param extras      Bundle数据
     * @param requestCode 请求code,用于startActivityForResult,回调{@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * @param flag        启动Flag
     */
    public static void launcher(Context context, Class<?> toClass, Bundle extras, int requestCode, int flag) {

        Intent intent = new Intent(context, toClass);
        intent.setFlags(flag);
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (requestCode != -1) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * 按{@link android.content.Intent#FLAG_ACTIVITY_CLEAR_TOP}模式启动Activity
     * 不在Activity间传递数据
     * requestCode为-1，指定以{@link android.app.Activity#startActivity(Intent)}启动
     *
     * @param context 应用上下文
     * @param toClass 跳转Activity
     */
    public static void launcher(Context context, Class<?> toClass) {
        launcher(context, toClass, null, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 按{@link android.content.Intent#FLAG_ACTIVITY_CLEAR_TOP}模式启动Activity
     * 在Activity间传递数据
     * requestCode为-1，指定以{@link android.app.Activity#startActivity(Intent)}启动
     *
     * @param context 应用上下文
     * @param toClass 跳转Activity
     * @param extras  Activity间传递的数据
     */
    public static void launcher(Context context, Class<?> toClass, Bundle extras) {

        launcher(context, toClass, extras, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 按{@link android.content.Intent#FLAG_ACTIVITY_CLEAR_TOP}模式启动Activity
     * 在Activity间传递数据
     * requestCode!=-1，指定以{@link android.app.Activity#startActivityForResult(android.content.Intent, int)} 启动
     *
     * @param context     应用上下文
     * @param toClass     跳转Activity
     * @param extras      Activity间传递的数据
     * @param requestCode 请求code,用于startActivityForResult,回调{@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     */
    public static void launcher(Context context, Class<?> toClass, Bundle extras, int requestCode) {

        launcher(context, toClass, extras, requestCode, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 指定flag模式启动Activity
     * 不在Activity间传递数据
     * requestCode为-1，指定以{@link android.app.Activity#startActivity(Intent)}启动
     *
     * @param context 应用上下文
     * @param toClass 跳转Activity
     * @param flag    启动模式
     */
    public static void launcher(Context context, Class<?> toClass, int flag) {

        launcher(context, toClass, null, -1, flag);
    }

    /**
     * 指定flag模式启动Activity
     * 在Activity间以键值对的方式传递数据{@link android.os.Bundle}
     * requestCode为-1，指定以{@link android.app.Activity#startActivity(Intent)}启动
     *
     * @param context 应用上下文
     * @param toClass 跳转Activity
     * @param flag    启动模式
     * @param extras  传递的数据
     */
    public static void launcher(Context context, Class<?> toClass, int flag, Bundle extras) {

        launcher(context, toClass, extras, -1, flag);
    }

    /**
     * 指定flag模式启动Activity
     * 不在Activity间以键值对的方式传递数据{@link android.os.Bundle}
     * requestCode!=-1，指定以{@link android.app.Activity#startActivityForResult(android.content.Intent, int)} 启动
     *
     * @param context     应用上下文
     * @param toClass     跳转Activity
     * @param requestCode 请求code,用于{@link android.app.Activity#startActivityForResult(android.content.Intent, int)},回调{@link android.app.Activity#onActivityResult(int, int, android.content.Intent)}
     * @param flag        启动模式
     */
    public static void launcher(Context context, Class<?> toClass, int requestCode, int flag) {

        launcher(context, toClass, null, requestCode, flag);
    }
}
