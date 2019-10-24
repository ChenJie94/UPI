package com.example.pay.util;

import org.springframework.util.DigestUtils;

public class Md5Utility {
	/**
	 * 字符串MD5加密 32位
	 * 
	 * @param str
	 * @return
	 */
	public static String String2MD5(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	/**
	 * 字符串MD5加密 16位
	 * 
	 * @param str
	 * @return
	 */
	public static String String2MD5x16(String str) {
		return String2MD5(str).substring(8, 24);
	}
}
