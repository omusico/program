package com.dabaicong.program;


import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5 {
	
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
	public static String lottery(String str) {
		String newstr = ""; // 加密后的字符串
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newstr;
	}
	public static String mgr(String password){
		return MD5.getMD5ofStr2(MD5.getMD5ofStr2(password).substring(0, 20) + "dajiang365").toLowerCase();
	}
	public static void main(String[] args) {
	}
}
