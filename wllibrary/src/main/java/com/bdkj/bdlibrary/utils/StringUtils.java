/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * <ul>
 * <li>{@link #isEmail(String)}</li>
 * <li>{@link #isPhoneNumber(String)}</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class StringUtils {
    /**
     * 邮箱验证表达式
     */
    private final static String EMAIL_REGULAR_EXPRESSION = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 手机号码的表达式
     */
    private final static String PHONE_REGULAR_EXPRESSION = "^[1][3-9]{1}[0-9]{9}$";

    /**
     * 禁止方法构造
     */
    private StringUtils() {
        throw new AssertionError();
    }


    /**
     * 验证email是否有效
     * <pre>
     *     isEmail("aaaa") = false
     *     isEmail("aaaaa@163.com) = true
     * </pre>
     *
     * @param email 需要验证的Email地址
     * @return
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;
        Pattern emailer = Pattern.compile(EMAIL_REGULAR_EXPRESSION);
        return emailer.matcher(email).matches();

    }

    /**
     * 验证手机号是否有效
     * <pre>
     *     isPhoneNumber("123456") = false
     *     isPhoneNumber("12345678912") = false
     *     isPhoneNumber("13612345678") = true
     * </pre>
     *
     * @param phone 需要验证的手机号码
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone))
            return false;
        Pattern phoner = Pattern.compile(PHONE_REGULAR_EXPRESSION);
        return phoner.matcher(phone).matches();
    }


}
