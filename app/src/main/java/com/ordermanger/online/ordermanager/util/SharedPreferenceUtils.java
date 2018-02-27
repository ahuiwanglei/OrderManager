package com.ordermanger.online.ordermanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 存储数据
 * 
 * @author Administrator
 * 
 */
public class SharedPreferenceUtils
{
	/*
	 * name is SharedPreferences name
	 */
	public static String getString(Context ctx, String name, String key)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		return share.getString(key, "");
	}

	public static void putString(Context ctx, String name, String key, String value)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_APPEND);
		Editor edit = share.edit();
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 
	 * @param ctx
	 * @param name
	 * @param key
	 * @return
	 */
	public static int getInt(Context ctx, String name, String key)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		return share.getInt(key, 0);
	}

	/**
	 * 
	 * @param ctx
	 * @param name
	 * @param key
	 * @param value
	 */
	public static void putInt(Context ctx, String name, String key, int value)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_APPEND);
		Editor edit = share.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	/**
	 * 
	 * @param ctx
	 * @param name
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context ctx, String name, String key)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		return share.getBoolean(key, false);
	}

	/**
	 * 
	 * @param ctx
	 * @param name
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context ctx, String name, String key, boolean value)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_APPEND);
		Editor edit = share.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	public static void putLong(Context ctx, String name, String key, long value)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_APPEND);
		Editor edit = share.edit();
		edit.putLong(key, value);
		edit.commit();
	}
	
	public static long getLong(Context ctx, String name, String key)
	{
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_PRIVATE);
		return share.getLong(key, 0);
	}
	
	public static void clear(Context ctx, String name){
		SharedPreferences share = ctx.getSharedPreferences(name, Activity.MODE_APPEND);
		Editor edit = share.edit();
		edit.clear();
		edit.commit();
	}

}
