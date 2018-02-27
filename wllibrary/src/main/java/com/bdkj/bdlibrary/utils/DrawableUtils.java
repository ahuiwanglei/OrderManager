package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Drawable图片处理类
 * Created by macchen on 15/4/13.
 */
public class DrawableUtils {

    public StateListDrawable getDrawable(Context context,int drawableId)
    {
        Drawable resDrawable = context.getResources().getDrawable(drawableId);
        if(resDrawable!=null) {
            StateListDrawable drawables = new StateListDrawable();
            Drawable press = getPressDrawable(resDrawable);
            Drawable disable = getDisabledDrawable(resDrawable);
            drawables.addState(null,disable);
            drawables.addState(new int[]{android.R.attr.state_enabled},resDrawable);
            drawables.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},press);
            return drawables;
        }
        return null;
    }

    private Drawable getPressDrawable(Drawable drawable) {
        return null;
    }

    private Drawable getDisabledDrawable(Drawable drawable) {
        return null;
    }

    public StateListDrawable getDrawable(Bitmap bitmap)
    {
        return null;
    }
    public StateListDrawable getDrawable(Drawable drawable)
    {
        return null;
    }
}
