package com.jsong.android.library.api;

import android.content.Context;

public interface NetRequestInterface {
	/**
	 * 请求类型参数
	 */
	public static String REQUESTYPE = "requestype";
	/**
	 * 请求类型参数值 post
	 */
	public static String REQUESTBYPOST = "post";
	/**
	 * 请求类型参数值 get
	 */
	public static String REQUESTBYGET = "get";

	void dorequest(final NetParameters params,
			final NetResponseListener responselistener, final Context context,
			final int tag);
}