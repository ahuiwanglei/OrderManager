/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 文件操作功能工具类
 */
public class FileUtils {

    /**
     * 文件扩展名分隔符
     */
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 禁止构造
     */
    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * 判断文件或目录是否存在
     * 调用{@link java.io.File#exists()} 返回结果
     *
     * @param file 文件File对象
     * @return 如果文件存在返回true, 否则返回false
     */
    public static boolean exist(File file) {
        return file.exists();
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 如果文件存在返回true, 否则返回false,调用{@link #exist(java.io.File)}
     */
    public static boolean exist(String strFile) {
        return exist(new File(strFile));
    }

    /**
     * 创建文件<br/>
     * <ul>
     * <li>如果文件不存在返回false</li>
     * <li>如果创建文件时抛出{@link java.io.IOException},返回false</li>
     * <li>创建成功返回true</li>
     * </ul>
     *
     * @param file 文件File对象
     * @return 如果文件创建成功则返回true, 否则返回false
     */
    public static boolean createFile(File file) {
        if (file.isDirectory())
            return false;
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建文件<br/>
     * 调用{@link #createFolder(java.io.File)}
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return {@link #createFolder(java.io.File)}
     */
    public static boolean createFile(String strFile) {
        return createFile(new File(strFile));
    }

    /**
     * 创建文件夹<br/>
     * <ul>
     * <li>如果传入的参数是文件，返回false</li>
     * <li>{@link java.io.File#mkdirs()}返回成功，则返回true,否则返回false</li>
     * </ul>
     *
     * @param folder 文件夹File对象
     * @return 返回是否成功创建文件夹
     */
    public static boolean createFolder(File folder) {
        if (folder.isFile())
            return false;
        else
            return folder.mkdirs();
    }

    /**
     * 创建文件夹
     *
     * @param strFile 文件夹字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return {@link #createFile(java.io.File)}
     */
    public static boolean createFolder(String strFile) {
        return createFolder(new File(strFile));
    }

    /**
     * 删除文件<br/>
     * <ul>
     * <li>如果传入的参数是文件夹，则返回false</li>
     * <li>如果传入的参数是文件，则返回{@link java.io.File#delete()}</li>
     * </ul>
     *
     * @param file 文件File对象
     * @return 返回是否删除成功
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory())
            return false;
        if(!file.exists())
            return true;
        return file.delete();
    }

    /**
     * 删除文件
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return {@link #deleteFile(java.io.File)} 返回是否删除成功
     */
    public static boolean deleteFile(String strFile) {
        return deleteFile(new File(strFile));
    }


    /**
     * 删除文件夹<br/>
     * <ul>
     * <li>如果传入的参数是文件，则返回false</li>
     * <li>如果传入的参数是文件夹，并且文件夹已不存在则返回true}</li>
     * <li>逐条删除指定文件夹下的文件或文件夹</li>
     * </ul>
     *
     * @param folder 文件夹File对象
     * @return 是否成功删除文件夹
     */
    public static boolean deleteFolder(File folder) {
        if (folder.isFile())
            return false;
        if (!folder.exists())
            return true;
        for (File f : folder.listFiles()) {
            if (f.isFile())
                f.delete();
            else if (f.isDirectory())
                deleteFolder(f);
        }
        return folder.delete();

    }

    /**
     * 删除文件夹 <br/>
     * 直接调用{@link #deleteFolder(java.io.File)}
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return {@link #deleteFolder(java.io.File)}是否成功删除文件夹
     */
    public static boolean deleteFolder(String strFile) {
        return deleteFolder(new File(strFile));
    }

    /**
     * 阅读文本文件内容<br/>
     * <ul>
     * <li>如果传入的参数为空或file对象不是文件，返回null</li>
     * <li>读取文本文件出现{@link java.io.IOException}异常返回null</li>
     * <li>文件读取完成返回{@link java.lang.StringBuilder},其中包括文件内容</li>
     * </ul>
     *
     * @param file        文件File对象
     * @param charsetName 字符集，支持{@link java.nio.charset.Charset <code>charset</code>}
     * @return 文本内容, 如果文件不存在或传入的参数不是文件或文件不存在，则返回null
     */
    public static StringBuilder readFile(File file, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile() || !file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 阅读文本文件内容<br/>
     *
     * @param strFile     文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param charsetName 字符集，支持{@link java.nio.charset.Charset <code>charset</code>}
     * @return 文本内容, 如果文件不存在或传入的参数不是文件或文件不存在，则返回null，直接调用{@link #readFile(java.io.File, String)}
     */
    public static StringBuilder readFile(String strFile, String charsetName) {
        return readFile(new File(strFile), charsetName);
    }

    /**
     * 向文件写入文本内容 <br/>
     * <ul>
     * <li>传入的content参数是空或空字符串{@link android.text.TextUtils#isEmpty(CharSequence)}或传入的File对象是文件夹或文件不存在则返回false</li>
     * <li>写文件过程发生{@link java.io.IOException},返回false</li>
     * <li>文件完全写入成功，返回true</li>
     * </ul>
     *
     * @param file    文件路径
     * @param content 内容
     * @param append  是否向文件末尾追加
     * @return 是否写入成功
     */
    public static boolean writeFile(File file, String content, boolean append) {
        if (TextUtils.isEmpty(content) || file.isDirectory() || !file.exists()) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 向文件写入文本内容 <br/>
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param content 文本内容
     * @param append  是否向文件末尾追加
     * @return {@link #writeFile(File, String, boolean)}的结果
     */
    public static boolean writeFile(String strFile, String content, boolean append) {
        return writeFile(new File(strFile), content, append);
    }

    /**
     * 向文件写入文本内容<br/>
     * <p>默认写入的内容不向文件末尾追加</p>
     *
     * @param file    文件路径对象
     * @param content 写入的内容
     * @return {@link #writeFile(java.io.File, String, boolean)}的结果
     */
    public static boolean writeFile(File file, String content) {
        return writeFile(file, content, false);
    }

    /**
     * 向文件写入文本内容<br/>
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param content 写入的内容
     * @return {@link #writeFile(java.io.File, String)}的结果
     */
    public static boolean writeFile(String strFile, String content) {
        return writeFile(new File(strFile), content);
    }

    /**
     * 将输入流中的内容，写入到File对象指定的文件中<br/>
     * <p>默认写入的内容不向文件末尾追加</p>
     *
     * @param file   写入的文件对象
     * @param stream 读取的输入流
     * @return {@link #writeFile(java.io.File, java.io.InputStream, boolean)}的结果
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * 将输入流中的内容，写入到strFile对象指定的文件中<br/>
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param stream  读取的输入流
     * @return {@link #writeFile(java.io.File, java.io.InputStream)}的结果
     */
    public static boolean writeFile(String strFile, InputStream stream) {
        return writeFile(new File(strFile), stream);
    }

    /**
     * 读取输入流中的内容写入到File对象指定的文件中<br/>
     * <ul>
     * <li>如果输入流为空或文件是目录或文件不存在的情况下，返回false</li>
     * <li>文件读取和写入的过程中，如果发生{@link java.io.IOException}或{@link java.io.FileNotFoundException}等异常，则返回false</li>
     * <li>流正常读取与写入完成，返回true</li>
     * </ul>
     *
     * @param file   将要写入文件的File对象
     * @param stream 输入流
     * @param append 是否向文件后追加
     * @return 是否写入成功
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        if (stream == null || file.isDirectory() || !file.exists()) {
            return false;
        }
        OutputStream o = null;
        try {
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 读取输入流中的内容写入到strFile对象指定的文件中<br/>
     *
     * @param strFile 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param stream  输入流
     * @param append  是否向文件后追加
     * @return {@link #writeFile(java.io.File, java.io.InputStream, boolean)}是否写入成功
     */
    public static boolean writeFile(String strFile, InputStream stream, boolean append) {
        return writeFile(new File(strFile), stream, append);
    }

    /**
     * 移动文件<br/>
     * <p>如果文件的路径为空或源文件不存在则返回false</p>
     * <p>调用{@link java.io.File#renameTo(java.io.File)}修改文件的名称</p>
     *
     * @param srcFile 源文件路径File对象
     * @param dstFile 目标文件路径File对象
     * @return 是否移动成功
     */
    public static boolean move(File srcFile, File dstFile) {
        if (TextUtils.isEmpty(srcFile.getAbsolutePath()) || TextUtils.isEmpty(dstFile.getAbsolutePath()) || !srcFile.exists())
            return false;
        return srcFile.renameTo(dstFile);
    }

    /**
     * 移动文件<br/>
     *
     * @param strSrcFile 源文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param strDstFile 目标文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return {@link #move(java.io.File, java.io.File)} 是否移动成功
     */
    public static boolean move(String strSrcFile, String strDstFile) {
        return move(new File(strSrcFile), new File(strDstFile));
    }

    /**
     * 复制文件<br/>
     * <p>如果源文件不存在或路径无效，则返回false</p>
     * <p>定义{@link java.io.FileInputStream}时出现{@link java.io.FileNotFoundException}异常时返回false</p>
     * <p>调用{@link #writeFile(File, java.io.InputStream)}</p>
     *
     * @param srcFile 源文件路径File对象
     * @param dstFile 目标文件路径File对象
     * @return 是否复制成功
     */
    public static boolean copyFile(File srcFile, File dstFile) {
        if (TextUtils.isEmpty(srcFile.getAbsolutePath()) || TextUtils.isEmpty(dstFile.getAbsolutePath()) || !srcFile.exists())
            return false;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(srcFile);
        } catch (FileNotFoundException e) {
            return false;
        }
        return writeFile(dstFile, inputStream);
    }

    /**
     * 复制文件<br/>
     * <p>调用{@link #copyFile(java.io.File, java.io.File)}返回结果</p>
     *
     * @param strSrc 源文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @param strDst 目标文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 是否复制成功
     */
    public static boolean copyFile(String strSrc, String strDst) {
        return copyFile(new File(strSrc), new File(strDst));
    }

    /**
     * 获取文件名（不包括扩展名)<br/>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 返回文件名称
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * 获取文件的名称（包括扩展名)
     * <p>如果传入的路径为空或空字符串{@link android.text.TextUtils#isEmpty(CharSequence)},则返回原参数</p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 返回文件名称
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 获取文件夹的名称<br/>
     * <p>传入的参数是空或空字符串则返回源参数</p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath 文件夹字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 返回文件夹的名称，如果参数为空{@link android.text.TextUtils#isEmpty(CharSequence)}则返回源参数
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取文件的扩展名<br/>
     * <p>传入参数为空或空字符串则返回空字符串</p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 返回文件的扩展名，如果传入的参数为空则返回空字符串
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 获取文件大小<br/>
     * <p>如果传入的路径错误或不是文件则返回-1</p>
     *
     * @param path 文件字符串路径 = {@link java.io.File#getAbsoluteFile()}
     * @return 返回文件的长度，如果路径为空或路径不是文件则返回-1
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
}
