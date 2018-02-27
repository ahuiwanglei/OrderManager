/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bdkj.library.R;

/**
 * Toast显示工具类
 * Created by Chenwei on 15/3/26.
 */
public class ToastUtils {

    /**
     * 禁止方法构造
     */
    private ToastUtils() {
        throw new AssertionError();
    }

    /**
     * 显示Toast
     * <p>自定义的Toast布局，统一程序在不同手机上的显示效果。</p>
     * <p>调用{@link Toast#setView(android.view.View)}传入自定义的布局。</p>
     *
     * @param context    上下文
     * @param layoutId   布局Id
     * @param drawableId 图标资源
     * @param msg        信息
     * @param duration   显示时长
     * @param gravity    在页面中的位置
     * @param offetX     位置偏移X
     * @param offetY     位置偏移Y
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, String msg, int duration, int gravity, int offetX, int offetY) {
        Toast toast = Toast.makeText(context, msg, duration);
        View contentView = LayoutInflater.from(context).inflate(layoutId, null, false);
        toast.setView(contentView);
        TextView tvContent = (TextView) contentView.findViewById(android.R.id.message);
        tvContent.setText(msg);
        ImageView ivIcon = (ImageView) contentView.findViewById(R.id.iv_toast_icon);
        if (drawableId != -1) {
            ivIcon.setImageResource(drawableId);
        } else {
            ivIcon.setVisibility(View.GONE);
        }
        toast.setGravity(gravity, offetX, offetY);
        toast.setDuration(duration);
        toast.show();
        return toast;
    }

    /**
     * 显示Toast
     *
     * @param context    上下文
     * @param layoutId   布局Id
     * @param drawableId 图标资源
     * @param msgId      信息资源id
     * @param duration   显示时长
     * @param gravity    在页面中的位置
     * @param offetX     位置偏移X
     * @param offetY     位置偏移Y
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, int msgId, int duration, int gravity, int offetX, int offetY) {
        return show(context, layoutId, drawableId, context.getString(msgId), duration, gravity, offetX, offetY);
    }

    /**
     * 显示默认位置偏移的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msg        字符串信息
     * @param duration   显示时长
     * @param gravity    显示位置
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, String msg, int duration, int gravity) {
        return show(context, layoutId, drawableId, msg, duration, gravity, 0, 0);
    }

    /**
     * 显示默认位置偏移的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msgId      字符串信息资源id
     * @param duration   显示时长
     * @param gravity    显示位置
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, int msgId, int duration, int gravity) {
        return show(context, layoutId, drawableId, context.getString(msgId), duration, gravity);
    }


    /**
     * 显示默认位置的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msg        字符串信息
     * @param duration   显示时长
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, String msg, int duration) {
        return show(context, layoutId, drawableId, msg, duration, Gravity.CENTER);
    }


    /**
     * 显示默认位置的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msgId      字符串信息资源id
     * @param duration   显示时长
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, int msgId, int duration) {
        return show(context, layoutId, drawableId, context.getString(msgId), duration);
    }


    /**
     * 显示默认的Toast时长的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msg        字符串信息
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, String msg) {
        return show(context, layoutId, drawableId, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示默认的Toast时长的Toast
     *
     * @param context    上下文
     * @param layoutId   布局id
     * @param drawableId 图标资源id
     * @param msgId      字符串信息资源id
     * @return
     */
    public static Toast show(Context context, int layoutId, int drawableId, int msgId) {
        return show(context, layoutId, drawableId, context.getString(msgId));
    }


    /**
     * 显示默认布局的Toast
     *
     * @param context    上下文
     * @param drawableId 图标资源id
     * @param msg        显示的字符串
     * @return
     */
    public static Toast show(Context context, int drawableId, String msg) {
        return show(context, R.layout.layout_toast_default, drawableId, msg);
    }

    /**
     * 显示默认布局的Toast
     *
     * @param context    上下文
     * @param drawableId 图标资源id
     * @param msgId      显示的字符串资源id
     * @return
     */
    public static Toast show(Context context, int drawableId, int msgId) {
        return show(context, R.layout.layout_toast_default, drawableId, context.getString(msgId));
    }

    /**
     * 显示没有图标的Toast
     *
     * @param context 上下文
     * @param msg     显示的字符串
     * @return
     */
    public static Toast show(Context context, String msg) {
        return show(context, -1, msg);
    }

    /**
     * 显示没有图标的Toast
     *
     * @param context 上下文
     * @param msgId   显示的字符串资源id
     * @return
     */
    public static Toast show(Context context, int msgId) {
        return show(context, -1, context.getString(msgId));
    }

    /**
     * 指定时长显示没有图标的Toast
     *
     * @param context 上下文
     * @param msg     显示的字符串
     * @return
     */
    public static Toast show(Context context, String msg, int duration) {
        return show(context, R.layout.layout_toast_default, -1, msg, duration);
    }


    /**
     * 显示错误Toast
     *
     * @param context 上下文
     * @param msg     错误信息
     * @return
     */
    public static Toast showError(Context context, String msg) {
        return show(context, R.drawable.img_dialog_error, msg);
    }

    /**
     * 显示错误Toast
     *
     * @param context 上下文
     * @param msgId   错误信息资源id
     * @return
     */
    public static Toast showError(Context context, int msgId) {
        return showError(context, context.getString(msgId));
    }

    /**
     * 显示成功Toast
     *
     * @param context 上下文
     * @param msg     成功信息
     * @return
     */
    public static Toast showSuccess(Context context, String msg) {
        return show(context, R.drawable.img_dialog_sure, msg);
    }

    /**
     * 显示成功Toast
     *
     * @param context 上下文
     * @param msgId   成功信息资源id
     * @return
     */
    public static Toast showSuccess(Context context, int msgId) {
        return show(context, R.drawable.img_dialog_sure, context.getString(msgId));
    }

    /**
     * 显示警告Toast
     *
     * @param context 上下文
     * @param msg     警告信息
     * @return
     */
    public static Toast showWarn(Context context, String msg) {
        return show(context, R.drawable.img_dialog_warning, msg);
    }

    /**
     * 显示警告Toast
     *
     * @param context 上下文
     * @param msgId   警告信息资源id
     * @return
     */
    public static Toast showWarn(Context context, int msgId) {
        return show(context, R.drawable.img_dialog_warning, context.getString(msgId));
    }

    /**
     * 显示信息Toast
     *
     * @param context 上下文
     * @param msg     提示信息
     * @return
     */
    public static Toast showInfo(Context context, String msg) {
        return show(context, R.drawable.img_dialog_info, msg);
    }

    /**
     * 显示信息Toast
     *
     * @param context 上下文
     * @param msgId   提示信息资源id
     * @return
     */
    public static Toast showInfo(Context context, int msgId) {
        return show(context, R.drawable.img_dialog_info, context.getString(msgId));
    }
}
