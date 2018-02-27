/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 包管理工具类
 * Created by macchen on 15/3/26.
 */
public class PackageUtils {

    /**
     * 禁止构造
     */
    private PackageUtils() {
        throw new AssertionError();
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param apkPath apk路径
     * @return
     */
    public static boolean installApk(Context context, String apkPath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(apkPath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;

    }

    /**
     * 卸载软件
     *
     * @param context     上下文
     * @param packageName 需要卸载软件的包名
     * @return
     */
    public static boolean uninstallApk(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
                .append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

}
