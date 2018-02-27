/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import java.lang.reflect.Field;

import com.bdkj.library.R;

/**
 * 资源管理工具类
 * Created by macchen on 15/3/26.
 */
public class ResourceUtils {

    /**
     * 禁止方法构造
     */
    private ResourceUtils() {
        throw new AssertionError();
    }

    /**
     * 通过字符串找到资源的id
     *
     * @param myClass 资源的类型
     *                <p>
     *                R.drawable.class
     *                R.layout.class
     *                R.raw.class
     *                R.style.class
     *                R.id.class
     *                R.string.class
     *                R.color.class
     *                </p>
     * @param name    资源名称
     * @return
     */
    public static int getResouceIdByName(Class<?> myClass, String name) {

        int id = 0;
        try {
            Field field = myClass.getField(name);
            id = field.getInt(myClass.newInstance());
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 根据名称获取资源的id <br/>
     *
     * @param name 图片资源的名称
     * @return
     */
    public static int getDrawable(String name) {
        return getResouceIdByName(R.drawable.class, name);
    }

    /**
     * 根据名称布局资源的id <br/>
     *
     * @param name 布局资源的名称
     * @return
     */
    public static int getLayout(String name) {
        return getResouceIdByName(R.layout.class, name);
    }

}
