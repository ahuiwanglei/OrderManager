/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理工具类
 * <ul>
 * <li>{@link #getRoundedCornerBitmap(android.graphics.Bitmap, float)}</li>
 * <li>{@link #saveImage(String, android.graphics.Bitmap, int)}</li>
 * <li>{@link #scaleSmallBitmap(android.graphics.Bitmap, int)}</li>
 * <li>{@link #scaleBigBitmap(String, int)}</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class ImageUtils {

    /**
     * 获得圆角图片的方法
     * <pre>
     *     getRoundedCornerBitmap(null,10)   = null
     *     getRoundedCornerBitmap(Bitmap,10) = Bitmap
     * </pre>
     *
     * @param bitmap
     * @param roundPx 一般设成14
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 缩放图片大小(适合于小图片）
     * <pre>
     *     scaleSmallBitmap("",100)                   = null
     *     scaleSmallBitmap(null,100)                 = null
     *     scaleSmallBitmap("/sdcard/bitmap.jpg",-1)  = null
     *     scaleSmallBitmap("/sdcard/bitmap.jpg",100) = Bitmap
     * </pre>
     *
     * @param bitmap   图片对象
     * @param maxWidth 缩放的图片最大宽度
     * @return
     */
    public static Bitmap scaleSmallBitmap(Bitmap bitmap, int maxWidth) {
        if (bitmap == null || maxWidth <= 0)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == 0 || height == 0)
            return null;
        Matrix matrix = new Matrix();
        if (width >= height) {
            float scale = ((float) maxWidth) / width;
            matrix.postScale(scale, scale);
        } else {
            float scale = ((float) maxWidth) / height;
            matrix.postScale(scale, scale);
        }
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizeBitmap;
    }

    /**
     * 压缩图片(适合于大图片）
     * <pre>
     *     scaleBigBitmap("",100)                   = null
     *     scaleBigBitmap(null,100)                 = null
     *     scaleBigBitmap("/sdcard/bitmap.jpg",-1)  = null
     *     scaleBigBitmap("/sdcard/bitmap.jpg",100) = Bitmap
     * </pre>
     *
     * @param filePath 图片文件路径
     * @param maxWidth 缩放的图片最大宽度
     * @return
     */
    public static Bitmap scaleBigBitmap(String filePath, int maxWidth) {
        if (TextUtils.isEmpty(filePath) || maxWidth <= 0 || !new File(filePath).exists())
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width <= 0 || height <= 0) {
            return null;
        }
        int inSampleSize = 1;
        if ((width > height && width > maxWidth) || (height > width && height > maxWidth)) {
            inSampleSize = Math.round((float) (width >= height ? width : height) / (float) maxWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 保存图片到指定目录
     * <pre>
     *     saveImage("",null,100)                    = false
     *     saveImage("",Bitmap,100)                  = false
     *     saveImage("/sdcard/aa.jpg",null,100)      = false
     *     saveImage("/sdcard/aa.jpg",Bitmap,100)    = true
     * </pre>
     *
     * @param filePath 保存的文件路径
     * @param bitmap   图片对象
     * @param quality  图片保存的质量
     * @return
     */
    public static boolean saveImage(String filePath, Bitmap bitmap, int quality) {
        if (TextUtils.isEmpty(filePath) || bitmap == null)
            return false;
        File file = new File(filePath);
        if (file.isDirectory())
            return false;
        FileUtils.createFolder(file.getParent());
        FileUtils.deleteFile(file);
        BufferedOutputStream bos = null;
        boolean isSuccess = false;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            bos = null;
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;

    }
}
