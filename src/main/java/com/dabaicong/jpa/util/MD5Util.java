package com.dabaicong.jpa.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MD5Util {

	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getMD5(String sourceStr, String charset)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] strTemp;
		if (charset == null) {
			strTemp = sourceStr.getBytes();
		} else {
			strTemp = sourceStr.getBytes(charset);
		}

		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
			// 为逻辑右移，将符号位一起右移

			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str);
	}
	
	
	/**
	 * 新加密算法
	 * @param s
	 * @return
	 */
	public final static String getMD5ofStr2(String s){ 
		char hexDigits[] = { 
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 
				'e', 'f'}; 
		try { 
			byte[] strTemp = s.getBytes(); 
			MessageDigest mdTemp = MessageDigest.getInstance("MD5"); 
			mdTemp.update(strTemp); 
			byte[] md = mdTemp.digest(); 
			int j = md.length; 
			char str[] = new char[j * 2]; 
			int k = 0; 
			for (int i = 0; i < j; i++) { 
				byte byte0 = md[i]; 
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
				str[k++] = hexDigits[byte0 & 0xf]; 
			} 
			return new String(str); 
		} 
		catch (Exception e){ 
			return null; 
		} 
	}
	
	public static final String getPassWd(String pw){
		return getMD5ofStr2(getMD5ofStr2(pw).substring(0, 20) + "password").toLowerCase();
	}
	

	/**
	 * utf-8的加密
	 * @param str 加密内容
	 * */
	public static String toMd5(String str){
       try{
    	   return getMD5(str, "UTF-8");
       }catch(Exception e){
    	   return null;
       }
     
	}
	
	public static String MD5(String content) throws UnsupportedEncodingException{
		MD5 md5 = new MD5();
		md5.update(content,  "UTF-8");
		return md5.getHashString();
	}
	/**
	 *  
	 * @param str 加密内容,charset 加密字符编码
	 * */
	public static String toMd5(String str,String charset){
	       try{
	    	   return getMD5(str, charset);
	       }catch(Exception e){
	    	   return null;
	       }
	     
		}
}
