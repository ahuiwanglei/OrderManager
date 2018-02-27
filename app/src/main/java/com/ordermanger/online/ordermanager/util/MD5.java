package com.ordermanger.online.ordermanager.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5 {
	/**
	 * 密码MD5加密方法
	 * @param password
	 * @param randomKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMd5(String password, String randomKey) throws NoSuchAlgorithmException{
		String md5 = getMd5(password);
		return getMd5(md5 + randomKey);
	}
	
	public static String getMd5(String source) throws NoSuchAlgorithmException{
		MessageDigest s = MessageDigest.getInstance("MD5");
		s.update(source.getBytes());		
		byte[] bytes = s.digest();
		return StringBytes.toHexStr(bytes);
	}
	
	public static String createMd5Key(){
		//生成密码和key
		Random r = new Random();
		String key = String.valueOf(r.nextInt(10000000));
		while(key.length() < 8){
			key = "0" + key;
		}
		return key;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
	}
}
