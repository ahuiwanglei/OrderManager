/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列号操作的工具类
 * <ul>
 * <li>{@link #readObject(java.io.File)}从文件中获取序列化对象</li>
 * <li>{@link #writeObject(java.io.File, java.lang.Object)}将序列化对象写入到文件中</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class SerializeUtils {

    /**
     * 禁止构造
     */
    private SerializeUtils() {
        throw new AssertionError();
    }

    /**
     * 从文件中读取一个实现了{@link java.io.Serializable}接口的Object对象
     *
     * @param file 文件路径
     * @return
     */
    public static Object readObject(File file) {
        Object obj = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            obj = in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 向文件中写入实现了{@link java.io.Serializable}接口的Object对象
     *
     * @param file 文件的路径
     * @param obj  实现了{@link java.io.Serializable}接口的Object对象
     * @return
     */
    public static boolean writeObject(File file, Object obj) {
        ObjectOutputStream out = null;
        boolean isSuccess = false;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(obj);
            out.close();
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }
}
