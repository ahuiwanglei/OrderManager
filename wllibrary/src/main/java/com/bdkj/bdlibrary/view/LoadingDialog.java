/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.bdkj.bdlibrary.utils.WindowUtils;
import com.bdkj.library.R;

/**
 * 正在加载对话框
 * Created by macchen on 15/3/31.
 */
public class LoadingDialog extends ProgressDialog {
    private ImageView loadingView;
    private TextView tvMsg;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setOwnerActivity((Activity) context);
    }

    public LoadingDialog(Context context) {
        super(context, R.style.my_loading_dialog);
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }

    @Override
    public void show() {
        super.show();
        if (loadingView != null) {
            loadingView.clearAnimation();
            RotateAnimation animation = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(1000);
            animation.setRepeatCount(-1);
            animation.setRepeatMode(Animation.RESTART);
            loadingView.startAnimation(animation);
        }
    }

    /**
     * 显示自定义布局的加载框
     *
     * @param iconRes 旋转图标资源
     * @param message 消息内容
     */
    public void show(int iconRes, String message) {
        super.show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_dialog, null);
        loadingView = (ImageView) view.findViewById(R.id.lv_loading_image);
        tvMsg = (TextView) view.findViewById(R.id.tv_loading_msg);
        int width = WindowUtils.getWidth(getContext());
        tvMsg.setMinWidth(width * 2 / 5);
        tvMsg.setMaxWidth(width * 2 / 3);
        tvMsg.setText(message);
        loadingView.setImageResource(iconRes);
        loadingView.clearAnimation();
        RotateAnimation animation = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        loadingView.startAnimation(animation);
        setContentView(view);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (loadingView != null) {
            loadingView.clearAnimation();
        }
    }
}
