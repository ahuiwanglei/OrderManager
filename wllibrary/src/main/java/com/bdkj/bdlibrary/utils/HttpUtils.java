/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;

/**
 * 网络通信工具类
 * Created by macchen on 15/3/26.
 * <ul>
 * <li>结合<a href="https://github.com/loopj/android-async-http/tree/1.4.6">android-async-http-1.4.5.jar</a>使用</li>
 * <li>需要在AndroidManifest.xml中添加android.permission.INTERNET权限</li>
 * </ul>
 */
public class HttpUtils {
    /**
     * 单例AsyncHttpClient对象
     */
    private static AsyncHttpClient client = new AsyncHttpClient();
    /**
     * 链接默认的超时时间
     */
    private final static int HTTP_UTILS_DEFAULT_TIME_OUT = 30 * 1000;

    static {
        // 设置链接超时，如果不设置，默认为10s
        client.setTimeout(HTTP_UTILS_DEFAULT_TIME_OUT);
    }

    /**
     * 禁止构造
     */
    private HttpUtils() {
        throw new AssertionError();
    }

    /**
     * 不带参数的GET请求
     *
     * @param context   上下文
     * @param urlString 通信地址
     * @param res       异步回调接口
     */
    public static void get(Context context, String urlString, AsyncHttpResponseHandler res) {

        client.get(context, urlString, res);
    }

    /**
     * 带参数的GET请求
     *
     * @param context   上下文
     * @param urlString 通信地址
     * @param params    参数组合
     * @param res       异步回调接口
     */
    public static void get(Context context, String urlString, RequestParams params, AsyncHttpResponseHandler res) {

        client.get(context, urlString, params, res);
    }

    /**
     * 不带参数的GET请求
     *
     * @param context   上下文
     * @param urlString 通信地址
     * @param res       异步回调接口(返回的Json数据)
     */
    public static void get(Context context, String urlString, JsonHttpResponseHandler res) {

        client.get(context, urlString, res);
    }

    /**
     * 带参数的GET请求
     *
     * @param context   上下文
     * @param urlString 通信地址
     * @param params    参数组合
     * @param res       异步回调接口(返回的JSON数据)
     */
    public static void get(Context context, String urlString, RequestParams params, JsonHttpResponseHandler res) {

        client.get(context, urlString, params, res);
    }

    /**
     * 不带参数的GET请求
     *
     * @param context  上下文
     * @param uString  通信地址
     * @param bHandler 异步回调接口(返回的二进制数据)
     */
    public static void get(Context context, String uString, BinaryHttpResponseHandler bHandler) {

        client.get(context, uString, bHandler);
    }

    /**
     * 带参数的POST请求
     *
     * @param context                 上下文
     * @param url                     通信地址
     * @param params                  参数组合
     * @param jsonHttpResponseHandler 异步回调接口(返回的JSON数据)
     */
    public static void post(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {

        client.post(context, url, params, jsonHttpResponseHandler);
    }

    /**
     * 表单的POST请求
     *
     * @param context                 上下文
     * @param url                     通信地址
     * @param entity                  表单数据
     * @param contentType             请求type
     * @param jsonHttpResponseHandler 异步回调接口(返回的JSON数据)
     */
    public static void post(Context context, String url, HttpEntity entity, String contentType, JsonHttpResponseHandler jsonHttpResponseHandler) {

        client.post(context, url, entity, contentType, jsonHttpResponseHandler);
    }

    /**
     * @return 返回AsyncHttpClient对象
     */
    public static AsyncHttpClient getClient() {

        return client;
    }
}
