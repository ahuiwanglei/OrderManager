package com.ordermanger.online.ordermanager.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;


public class Des {
	private static byte[] Keys = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90,
			(byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
	private static String password = "1P#&)2.0";
	// 测试
	public static void main(String args[]) {
		// 待加密内容
		String str = "127";
		// 密码，长度要是8的倍数

//		try {
//			str = MD5.getMd5(str).toUpperCase();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
		System.out.println("MD5：" + str);
		byte[] result = encrypt(str.getBytes(), password);
		System.out.println("加密后：" + StringBytes.toHexStr(result));
		// 直接将如上内容解密
		try {
			byte[] decryResult = decrypt(result, password);
			System.out.println("解密参数：" + new String(decryResult));

			String str2 = "CF16C403D5D4E48B542D7F4C287FD69346898740A646B04D34E2F42D94F113293D84AE8AD82F0258072A94926D7B34120AC2283825D2F8A95CA07B7F963D762E8DF797568679346CA886D5C0BB0F99894A7F2A4AFBDAE95A659666CCAEA3199CC074D3435F2F0F7E7D80EF4608DBA40DE0699B5CDBB664056A8C7B2FDC10A7C427B77B37578DE5E8180943D0E92E1E172E652490F79DD7876E1B43DA71E82989D3E56EFC87C8DDB1D4B025224F80B12F";
			
		    System.out.println("解密返回值："
					+ new String(decrypt(StringBytes.hexStr2Bytes(str2), password)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encrypt(String str){
		byte[] bytes = encrypt(str.getBytes(), password);
		return StringBytes.toHexStr(bytes);
	}

	public static String decrypt(byte[] b){
		try {
			byte[] bytes = decrypt(b, password);
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			AlgorithmParameterSpec iv = new IvParameterSpec(Keys);
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		AlgorithmParameterSpec iv = new IvParameterSpec(Keys);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}


}
