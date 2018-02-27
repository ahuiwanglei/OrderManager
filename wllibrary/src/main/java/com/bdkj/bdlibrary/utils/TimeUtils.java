/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 * Created by macchen on 15/3/26.
 */
public class TimeUtils {
    /**
     * 默认的时间格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认的日期格式
     */
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";

    /**
     * 禁止方法构造
     */
    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * 按指定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @param format       转换格式
     * @return 返回转换后的字符串
     */
    public static String formatMillisTo(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 按固定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @return 返回转换后的字符串
     */
    public static String formatMillisToTime(long timeInMillis) {
        return formatMillisTo(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 按固定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @return 返回转换后的字符串
     */
    public static String formatMillisToDate(long timeInMillis) {
        return formatMillisTo(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * @return 返回当前毫秒数
     */
    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @return 返回当前毫秒数转换后的字符串
     */
    public static String getCurrentTimeInString() {
        return getCurrentTimeInString(DEFAULT_DATE_FORMAT);
    }

    /**
     * 返回当前毫秒数转换后的字符串
     *
     * @param format 转换格式
     * @return
     */
    public static String getCurrentTimeInString(String format) {
        return formatMillisTo(getCurrentMillis(), format);
    }

    /**
     * 字符串指定格式转换成Date对象
     *
     * @param strDate 需要转换的字符串对象
     * @param format  转换格式
     * @return
     */
    public static Date stringToDate(String strDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(strDate, pos);
        return strToDate;
    }

    /**
     * 字符串固定格式转换成Date对象
     *
     * @param strDate 需要转换的字符串对象
     * @return
     */
    public static Date stringToDate(String strDate) {
        return stringToDate(strDate, DEFAULT_DATE_FORMAT);
    }


}
