/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 手机工具类
 * <ul>
 * <li>{@link #getPhoneNumber(android.content.Context)}</li>
 * <li>{@link #getIMEI(android.content.Context)}</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class PhoneUtils {
    /**
     * 获取手机号码(如果本机号码被写入到sim卡中才能读取到，否则返回空字符串)
     * <p>在AndroidManifest.xml中添加<uses-permission android:name="android.permission.READ_PHONE_STATE"/> </p>
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = manager.getLine1Number();
        return phoneNumber == null ? "" : phoneNumber;
    }

    /**
     * 获取手机的IMEI号
     * <p>在AndroidManifest.xml中添加<uses-permission android:name="android.permission.READ_PHONE_STATE"/> </p>
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = manager.getDeviceId();
        return (imei == null || imei.equals("")) ? "" : imei;
//        return "866085020079079";
    }

}
