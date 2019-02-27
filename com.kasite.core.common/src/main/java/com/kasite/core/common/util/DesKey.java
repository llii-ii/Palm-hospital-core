package com.kasite.core.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 加密、解密工具类
 * 
 * @author 無
 *
 */
public class DesKey {
	private byte[] desKey;

	/** 解密数据 */
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	public static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}
			hexString.append(plainText);
		}

		return hexString.toString();
	}

	/**
	 * 获得加密字符串
	 * 
	 * @param encryptString
	 *            明文字符串
	 * @param key
	 * @return
	 */
	public static String getEncString(String encryptString, String key) {
		if (encryptString != null && !"".equals(encryptString)) {
			try {
				return toHexString(encrypt(encryptString, key)).toUpperCase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "";
			}
		} else {
			return encryptString;
		}
	}

	/**
	 * 获得解密字符串
	 * 
	 * @param decryptString
	 *            密文字符串
	 * @param key
	 * @return
	 */
	public static String getDesString(String decryptString, String key) {
		if (decryptString != null && !"".equals(decryptString)) {
			try {
				return java.net.URLDecoder.decode(decrypt(decryptString, key), "utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "";
			}
		} else {
			return decryptString;
		}
	}

	public static void main(String[] args) throws Exception {
		String key = "fuckfuck";
		String value = "abcd";
		String jiami = java.net.URLEncoder.encode(value, "utf-8").toLowerCase();

//		KasiteConfig.print("加密数据:" + jiami);
		String a = toHexString(encrypt(jiami, key)).toUpperCase();

//		KasiteConfig.print("加密后的数据为:" + a);
		String b = java.net.URLDecoder.decode(decrypt(a, key), "utf-8");
//		KasiteConfig.print("解密后的数据:" + b);

	}

}
