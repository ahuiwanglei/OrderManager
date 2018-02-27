/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * ImageLoad图片加载库配置
 * <ul>
 * <li>结合<a href="https://github.com/nostra13/Android-Universal-Image-Loader/tree/v1.9.3">universal-image-loader-1.9.2.jar</a>使用</li>
 * <li>需要在AndroidManifest.xml中添加android.permission.INTERNET和android.permission.WRITE_EXTERNAL_STORAGE权限</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class ImageLoaderConfig {

    /**
     * 禁止构造
     */
    private ImageLoaderConfig() {
        throw new AssertionError();
    }
    /**
     * 生成{@link com.nostra13.universalimageloader.core.ImageLoader}的基本配置
     * <p>如果输入的file参数为空，图片将缓存在储存器的android/data/包名/cache目录下</p>
     *
     * @param context 应用程序上下文
     * @param file    文件缓存的目录
     * @return
     */
    public static ImageLoaderConfiguration getImageLoaderConfig(
            Context context, File file) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context).threadPoolSize(5).memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSizePercentage(13).writeDebugLogs();
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs();
            }
            builder.diskCache(new LruDiscCache(file, new HashCodeFileNameGenerator(), 30 * 1024 * 1024)).diskCacheFileCount(100);
        }
        ImageLoaderConfiguration config2 = builder.build();
        return config2;
    }

    /**
     * 生成{@link com.nostra13.universalimageloader.core.ImageLoader}的可选项功能
     * <ul>
     * <li>配置是否进行内存缓存</li>
     * <li>是否进行磁盘文件缓存</li>
     * <li>设置默认的加载图、失败图、空地址的图片</li>
     * </ul>
     *
     * @return 配置内存和磁盘双缓存
     */
    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
    }

    /**
     * 生成{@link com.nostra13.universalimageloader.core.ImageLoader}的可选项功能
     * <ul>
     * <li>配置是否进行内存缓存</li>
     * <li>是否进行磁盘文件缓存</li>
     * <li>设置默认的加载图、失败图、空地址的图片</li>
     * </ul>
     *
     * @param defaultImage 默认的图片（加载、失败、空地址使用同一张图片)
     * @return 配置内存、磁盘缓存，并设置默认的加载图片
     */
    public static DisplayImageOptions getOptions(int defaultImage) {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageForEmptyUri(defaultImage).showImageOnFail(defaultImage).showImageOnLoading(defaultImage).cacheOnDisk(true).build();
    }


    /**
     * 生成{@link com.nostra13.universalimageloader.core.ImageLoader}的可选项功能
     * <ul>
     * <li>配置是否进行内存缓存</li>
     * <li>是否进行磁盘文件缓存</li>
     * <li>设置默认的加载图、失败图、空地址的图片</li>
     * </ul>
     *
     * @param loadingImage 加载图
     * @param emptyImage   空地址时显示的图片
     * @param failImage    下载失败时显示的图片
     * @return 配置内存、磁盘缓存，并设置加载时、空地址时、下载失败时的不同显示图片
     */
    public static DisplayImageOptions getOptions(int loadingImage, int emptyImage, int failImage) {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageForEmptyUri(emptyImage).showImageOnFail(failImage).showImageOnLoading(loadingImage).cacheOnDisk(true).build();
    }

}
