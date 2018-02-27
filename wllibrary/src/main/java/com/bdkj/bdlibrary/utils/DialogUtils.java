/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import com.bdkj.bdlibrary.view.LoadingDialog;
import com.bdkj.library.R;

/**
 * 对话框工具类
 * <ul>
 * 几个重点的方法
 * <li>{@link #showAlertDialog(android.content.Context, String, String, String, String, android.view.View.OnClickListener, android.view.View.OnClickListener, int)}</li>
 * <li>{@link #showAlert(android.content.Context, String, String, String, android.view.View.OnClickListener, int)}</li>
 * <li>{@link #showAlertNoTitle(android.content.Context, String, String, android.view.View.OnClickListener)}</li>
 * <li>{@link #showConfirm(android.content.Context, String, String, String, android.view.View.OnClickListener, String, android.view.View.OnClickListener, int)}</li>
 * <li>{@link #showConfirmNoTitle(android.content.Context, String, String, android.view.View.OnClickListener, String, android.view.View.OnClickListener, int)}</li>
 * <li>{@link #showLoading(android.content.Context, int, String, boolean)}</li>
 * <li>{@link #showLoading(android.content.Context, int, int, boolean)} </li>
 * </ul>
 * Created by macchen on 15/3/30.
 */
public class DialogUtils {

    /**
     * 显示弹出框<br/>
     * <p>调用{@link android.app.Dialog#setContentView(android.view.View)}自定义布局</p>
     * <ul>
     * <li>传入的title参数为空时隐藏标题栏</li>
     * <li>传入的message参数为空时显示信息为""</li>
     * <li>传入的positionButton或negativeButton为空时隐藏</li>
     * </ul>
     *
     * @param context          上下文
     * @param title            弹出框标题
     * @param message          弹出信息内容
     * @param positionButton   确认按钮
     * @param negativeButton   取消按钮
     * @param positionListener 确认事件
     * @param negativeListener 取消事件
     * @param msgGravity       内容对齐方式
     * @return
     */
    public static Dialog showAlertDialog(Context context, String title, String message, String positionButton, String negativeButton, final View.OnClickListener positionListener, final View.OnClickListener negativeListener, int msgGravity) {
        final Dialog dialog = new Dialog(context, R.style.my_loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_default, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_dialog_message);
        Button btnPositive = (Button) view.findViewById(R.id.btn_dialog_positive);
        Button btnNegative = (Button) view.findViewById(R.id.btn_dialog_negative);
        View horizontalLine = view.findViewById(R.id.view_horizontal_line);
        View verticalLine = view.findViewById(R.id.view_vertical_line);
        tvTitle.setVisibility(title == null ? View.GONE : View.VISIBLE);
        horizontalLine.setVisibility(title == null ? View.GONE : View.VISIBLE);
        tvTitle.setText(title == null ? "" : title);
        tvMsg.setText(message == null ? "" : message);
        int width = WindowUtils.getWidth(context);
        tvMsg.setMinWidth(width * 3 / 5);
        tvMsg.setMaxWidth(width * 2 / 3);
        tvMsg.setGravity(msgGravity);
        if (positionButton != null || negativeButton != null) {
            btnPositive.setBackgroundResource(negativeButton == null ? R.drawable.bg_dialog_button_when_one : R.drawable.bg_dialog_left_button);
            btnNegative.setBackgroundResource(positionButton == null ? R.drawable.bg_dialog_button_when_one : R.drawable.bg_dialog_right_button);
        }
        btnPositive.setText(positionButton == null ? "" : positionButton);
        btnPositive.setVisibility(positionButton == null ? View.GONE : View.VISIBLE);
        btnNegative.setText(negativeButton == null ? "" : negativeButton);
        btnNegative.setVisibility(negativeButton == null ? View.GONE : View.VISIBLE);
        verticalLine.setVisibility(positionButton != null && negativeButton != null ? View.VISIBLE : View.GONE);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionListener != null) {
                    positionListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (negativeListener != null) {
                    negativeListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowUtils.getWidth(context) * 3 / 4;
        window.setAttributes(layoutParams);
        dialog.show();
        return dialog;
    }

    //===============================
    //
    //  信息提示框
    //
    //================================

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     *
     * @param context      上下文
     * @param title        标题
     * @param message      显示的消息内容
     * @param positiveName 确认按钮文字
     * @param listener     确认按钮点击事件
     * @param msgGrivity   消息内容对齐方式
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message, String positiveName, View.OnClickListener listener, int msgGrivity) {
        return showAlertDialog(context, title, message, positiveName, null, listener, null, msgGrivity);
    }

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER_VERTICAL}和{@link android.view.Gravity#LEFT}</p>
     *
     * @param context      上下文
     * @param title        标题
     * @param message      显示的消息内容
     * @param positiveName 确认按钮文字
     * @param listener     确认按钮点击事件
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message, String positiveName, View.OnClickListener listener) {
        return showAlertDialog(context, title, message, positiveName, null, listener, null, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     *
     * @param context    上下文
     * @param title      标题
     * @param message    显示的消息内容
     * @param listener   确认按钮点击事件
     * @param msgGrivity 文字对齐方式
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message, View.OnClickListener listener, int msgGrivity) {
        return showAlert(context, title, message, context.getString(R.string.dialog_default_positive_name), listener, msgGrivity);
    }

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER_VERTICAL}和{@link android.view.Gravity#LEFT}</p>
     *
     * @param context  上下文
     * @param title    标题
     * @param message  显示的消息内容
     * @param listener 确认按钮点击事件
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message, View.OnClickListener listener) {
        return showAlert(context, title, message, context.getString(R.string.dialog_default_positive_name), listener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认点击确定按钮关闭弹出框</p>
     *
     * @param context    上下文
     * @param title      标题
     * @param message    显示的消息内容
     * @param msgGrivity 文字对齐方式
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message, int msgGrivity) {
        return showAlert(context, title, message, null, msgGrivity);
    }

    /**
     * 显示消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认点击确定按钮关闭弹出框</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER_VERTICAL}和{@link android.view.Gravity#LEFT}</p>
     *
     * @param context 上下文
     * @param title   标题
     * @param message 显示的消息内容
     * @return
     */
    public static Dialog showAlert(Context context, String title, String message) {
        return showAlert(context, title, message, null, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    //===============================
    //
    //   没有标题的信息提示框
    //
    //================================

    /**
     * 显示没有标题的消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER}</p>
     *
     * @param context      上下文
     * @param message      显示的消息内容
     * @param positiveName 确定按钮文字
     * @param listener     确定按钮点击事件
     * @return
     */
    public static Dialog showAlertNoTitle(Context context, String message, String positiveName, View.OnClickListener listener) {
        return showAlertDialog(context, null, message, positiveName, null, listener, null, Gravity.CENTER);
    }

    /**
     * 显示没有标题的消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER}</p>
     *
     * @param context  上下文
     * @param message  显示的消息内容
     * @param listener 确定按钮点击事件
     * @return
     */
    public static Dialog showAlertNoTitle(Context context, String message, View.OnClickListener listener) {
        return showAlertNoTitle(context, message, context.getString(R.string.dialog_default_positive_name), listener);
    }

    /**
     * 显示没有标题的消息提示框<br/>
     * <p>默认只有确定按钮，没有取消按钮</p>
     * <p>默认确定按钮为的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认点击确定按钮关闭弹出框</p>
     * <p>默认对齐方式为{@link android.view.Gravity#CENTER}</p>
     *
     * @param context 上下文
     * @param message 显示的消息内容
     * @return
     */
    public static Dialog showAlertNoTitle(Context context, String message) {
        return showAlertNoTitle(context, message, null);
    }

    //===============================
    //
    //  消息确认框
    //
    //================================

    /**
     * 显示确认框<br/>
     * <p>调用{@link #showAlertDialog(android.content.Context, String, String, String, String, android.view.View.OnClickListener, android.view.View.OnClickListener, int)}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @param negativeName     取消按钮名称
     * @param negativeListener 取消按钮点击事件
     * @param msgGravity       消息内容的对齐方式
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, View.OnClickListener negativeListener, int msgGravity) {
        return showAlertDialog(context, title, message, positiveName, negativeName, positiveListener, negativeListener, msgGravity);
    }

    /**
     * 显示确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @param negativeName     取消按钮名称
     * @param negativeListener 取消按钮点击事件
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, View.OnClickListener negativeListener) {
        return showConfirm(context, title, message, positiveName, positiveListener, negativeName, negativeListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @param negativeName     取消按钮名称
     * @param msgGravity       消息内容对齐方式
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, int msgGravity) {
        return showConfirm(context, title, message, positiveName, positiveListener, negativeName, null, msgGravity);
    }

    /**
     * 显示确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @param negativeName     取消按钮名称
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener, String negativeName) {
        return showConfirm(context, title, message, positiveName, positiveListener, negativeName, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @param msgGravity       消息内容对齐方式
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener, int msgGravity) {
        return showConfirm(context, title, message, positiveName, positiveListener, context.getString(R.string.dialog_default_negative_name), msgGravity);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveName     确认按钮名称
     * @param positiveListener 确认按钮点击事件
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, String positiveName, View.OnClickListener positiveListener) {
        return showConfirm(context, title, message, positiveName, positiveListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认确定按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveListener 确认按钮点击事件
     * @param msgGravity       消息内容对齐方式
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, View.OnClickListener positiveListener, int msgGravity) {
        return showConfirm(context, title, message, context.getString(R.string.dialog_default_positive_name), positiveListener, msgGravity);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认确定按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     *
     * @param context          上下文
     * @param title            标题
     * @param message          消息内容
     * @param positiveListener 确认按钮点击事件
     * @return
     */
    public static Dialog showConfirm(Context context, String title, String message, View.OnClickListener positiveListener) {
        return showConfirm(context, title, message, positiveListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认确定按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     * <p>默认弹出框的标题为{@link com.bdkj.bdlibrary.R.string#dialog_default_title}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveListener 确认按钮点击事件
     * @return
     */
    public static Dialog showConfirm(Context context, String message, View.OnClickListener positiveListener) {
        return showConfirm(context, context.getString(R.string.dialog_default_title), message, positiveListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    //===============================
    //
    //  没有标题的确认框
    //
    //================================

    /**
     * 显示没有标题的确认框<br/>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @param negativeName     取消按钮文字
     * @param negativeListener 取消按钮的点击事件
     * @param msgGravity       消息内容的对齐方式
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, View.OnClickListener negativeListener, int msgGravity) {
        return showAlertDialog(context, null, message, positiveName, negativeName, positiveListener, negativeListener, msgGravity);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @param negativeName     取消按钮文字
     * @param negativeListener 取消按钮的点击事件
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, View.OnClickListener negativeListener) {
        return showConfirmNoTitle(context, message, positiveName, positiveListener, negativeName, negativeListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @param negativeName     取消按钮文字
     * @param msgGravity       消息内容的对齐方式
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener, String negativeName, int msgGravity) {
        return showConfirmNoTitle(context, message, positiveName, positiveListener, negativeName, null, msgGravity);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @param negativeName     取消按钮文字
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener, String negativeName) {
        return showConfirmNoTitle(context, message, positiveName, positiveListener, negativeName, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @param msgGravity       消息内容的对齐方式
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener, int msgGravity) {
        return showConfirmNoTitle(context, message, positiveName, positiveListener, context.getString(R.string.dialog_default_negative_name), msgGravity);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveName     确定按钮文字
     * @param positiveListener 确定按钮的点击事件
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, String positiveName, View.OnClickListener positiveListener) {
        return showConfirmNoTitle(context, message, positiveName, positiveListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认确定按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveListener 确定按钮的点击事件
     * @param msgGravity       消息内容对齐方式
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, View.OnClickListener positiveListener, int msgGravity) {
        return showConfirmNoTitle(context, message, context.getString(R.string.dialog_default_positive_name), positiveListener, msgGravity);
    }

    /**
     * 显示没有标题的确认框<br/>
     * <p>默认文字的对齐方式为{@link android.view.Gravity#LEFT}和{@link android.view.Gravity#CENTER_VERTICAL}</p>
     * <p>默认点击取消后关闭对话框，什么也不做</p>
     * <p>默认取消按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_negative_name}</p>
     * <p>默认确定按钮文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_positive_name}</p>
     *
     * @param context          上下文
     * @param message          消息内容
     * @param positiveListener 确定按钮的点击事件
     * @return
     */
    public static Dialog showConfirmNoTitle(Context context, String message, View.OnClickListener positiveListener) {
        return showConfirmNoTitle(context, message, positiveListener, Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    //===============================
    //
    //    正在加载框
    //
    //================================

    /**
     * 显示正在加载对话框<br/>
     *
     * @param context    上下文
     * @param iconRes    旋转图标资源
     * @param message    显示的消息
     * @param cancelable 能否取消
     * @return
     */
    public static LoadingDialog showLoading(Context context, int iconRes, String message, boolean cancelable) {
        final LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show(iconRes, message);
        return dialog;
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认加载对话框能通过返回键关闭</p>
     *
     * @param context 上下文
     * @param iconRes 旋转图标资源
     * @param message 显示的消息
     * @return
     */
    public static LoadingDialog showLoading(Context context, int iconRes, String message) {
        return showLoading(context, iconRes, message, true);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     *
     * @param context    上下文
     * @param message    显示的消息
     * @param cancelable 能否取消
     * @return
     */
    public static LoadingDialog showLoading(Context context, String message, boolean cancelable) {
        return showLoading(context, R.drawable.ic_loading_01, message, cancelable);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     * <p>默认加载对话框能通过返回键关闭</p>
     *
     * @param context 上下文
     * @param message 显示的消息
     * @return
     */
    public static LoadingDialog showLoading(Context context, String message) {
        return showLoading(context, message, true);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     * <p>默认加载时的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_msg}</p>
     *
     * @param context    上下文
     * @param cancelable 能否取消
     * @return
     */
    public static LoadingDialog showLoading(Context context, boolean cancelable) {
        return showLoading(context, context.getString(R.string.dialog_default_msg), cancelable);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     * <p>默认加载时的文字为{@link com.bdkj.bdlibrary.R.string#dialog_default_msg}</p>
     * <p>默认加载对话框能通过返回键关闭</p>
     *
     * @param context 上下文
     * @return
     */
    public static LoadingDialog showLoading(Context context) {
        return showLoading(context, true);
    }

    /**
     * 显示正在加载对话框<br/>
     *
     * @param context    上下文
     * @param iconRes    加载图标资源
     * @param msgId      消息内容id
     * @param cancelable 能否取消
     * @return
     */
    public static LoadingDialog showLoading(Context context, int iconRes, int msgId, boolean cancelable) {
        return showLoading(context, iconRes, context.getString(msgId), cancelable);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认加载对话框能通过返回键关闭</p>
     *
     * @param context 上下文
     * @param iconRes 旋转图标资源
     * @param msgId   消息内容资源id
     * @return
     */
    public static LoadingDialog showLoading(Context context, int iconRes, int msgId) {
        return showLoading(context, iconRes, msgId, true);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     *
     * @param context    上下文
     * @param msgId      消息内容资源id
     * @param cancelable 能否取消
     * @return
     */
    public static LoadingDialog showLoading(Context context, int msgId, boolean cancelable) {
        return showLoading(context, R.drawable.ic_loading_01, msgId, cancelable);
    }

    /**
     * 显示正在加载对话框<br/>
     * <p>默认旋转图为{@link com.bdkj.bdlibrary.R.drawable#ic_loading_01}</p>
     * <p>默认加载对话框能通过返回键关闭</p>
     *
     * @param context 上下文
     * @param msgId   消息资源id
     * @return
     */
    public static LoadingDialog showLoading(Context context, int msgId) {
        return showLoading(context, msgId, true);
    }

}