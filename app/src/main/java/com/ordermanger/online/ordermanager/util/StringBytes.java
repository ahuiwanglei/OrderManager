package com.ordermanger.online.ordermanager.util;

import java.util.Locale;

/**
 * Created by admin on 2017/5/17.
 */

public class StringBytes {
    public static String toHexStr(byte[] key) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < key.length; i++) {
            b.append(toHexStr(key[i]));
        }
        return b.toString();
    }

    public static String toHexStr(byte bValue) {
        int i, j;
        char c1, c2;
        String s;
        i = (bValue & 0xf0) >>> 4;
        j = bValue & 0xf;
        if (i > 9)
            c1 = (char) (i - 10 + 'A');
        else
            c1 = (char) (i + '0');
        if (j > 9)
            c2 = (char) (j - 10 + 'A');
        else
            c2 = (char) (j + '0');
        s = String.valueOf(c1) + String.valueOf(c2);
        return s;
    }

    public static byte[] hexStr2Bytes(String src){
	        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //处理值初始化
        int m=0,n=0;
        int iLen=src.length()/2; //计算长度
        byte[] ret = new byte[iLen]; //分配存储空间

        for (int i = 0; i < iLen; i++){
            m=i*2+1;
            n=m+1;
            ret[i] = (byte)(Integer.decode("0x"+ src.substring(i*2, m) + src.substring(m,n)) & 0xFF);
        }
        return ret;
    }
}
