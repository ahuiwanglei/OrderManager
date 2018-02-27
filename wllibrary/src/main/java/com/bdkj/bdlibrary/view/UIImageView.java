package com.bdkj.bdlibrary.view;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by macchen on 15/4/13.
 */
public class UIImageView extends ImageView {

    public final float[] BT_SELECTED = new float[] {1,0,0,0,-50,0,1,0,0,-50,0,0,1,0,-50,0,0,0,1,0};
    public final float[] BT_NOT_SELECTED = new float[] {1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0};
    public UIImageView(Context context) {
        super(context);
    }

    public UIImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable drawable = getBackground();
        if(drawable!=null&&drawable instanceof BitmapDrawable)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    drawable.setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                     setBackgroundDrawable(drawable);
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    drawable.setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                    setBackgroundDrawable(drawable);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
