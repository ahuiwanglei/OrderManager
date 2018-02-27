/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * dp与px互相转换的工具类
 * <ul>
 * <strong>dp与px互相转换</strong>
 * <li>{@link ScreenUtils#dpToPx(Context, float)}</li>
 * <li>{@link ScreenUtils#pxToDp(Context, float)}</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class ScreenUtils {
    /**
     * 禁止构造
     */
    private ScreenUtils() {
        throw new AssertionError();
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dp      dp值
     * @return
     */
    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param px      px值
     * @return
     */
    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spVal   sp值大小
     * @return
     */
    public static float spToPx(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());

    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxVal   px大小
     * @return
     */
    public static float pxTosp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
