/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Asset数据库操作类
 * Created by macchen on 15/3/26.
 * <ul>
 * <li>自动从assets下拷贝数据库到/data/data/package_name/databases</li>
 * <li>你能使用{@link android.database.sqlite.SQLiteDatabase}的
 * {@link android.database.sqlite.SQLiteDatabase #getWritableDatabase()}去创建或打开一个数据库</li>
 * </ul>
 */
public class AssetDataBaseOpenHelper {
    /**
     * 应用上下文
     */
    private Context context;
    /**
     * 数据库路径
     */
    private String databaseName;

    /**
     * 构造方法
     *
     * @param context      上下文
     * @param databaseName 数据库路径
     */
    public AssetDataBaseOpenHelper(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
    }

    /**
     * @return 返回可写的数据库对象
     */
    public synchronized SQLiteDatabase getWriteableDatabase() {
        File dbFile = context.getDatabasePath(databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                return null;
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * @return 返回可读的数据库对象
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        File dbFile = context.getDatabasePath(databaseName);
        if (dbFile != null && !dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                return null;
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * @return 返回数据库名称
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * 复制数据库文件
     * 调用{@link com.bdkj.bdlibrary.utils.FileUtils#writeFile(java.io.File, java.io.InputStream)}复制文件
     *
     * @param dbFile 数据库文件路径
     * @throws IOException
     */
    private void copyDatabase(File dbFile) throws IOException {
        InputStream stream = context.getAssets().open(databaseName);
        FileUtils.writeFile(dbFile, stream);
        stream.close();
    }
}
