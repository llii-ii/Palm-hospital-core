package com.kasite.core.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * 计算字符串的hash值工具类。
 * 
 * @author daiys
 *
 */
public class HashCodeUtil {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 将字节数组转换为16进制的字符串
	 * 
	 * @param byteArray
	 *            字节数组
	 * @return 16进制的字符串
	 */
	private static String byteArrayToHexString(byte[] byteArray) {
		StringBuffer sb = new StringBuffer();
		for (byte byt : byteArray) {
			sb.append(byteToHexString(byt));
		}
		return sb.toString();
	}

	/**
	 * 将字节转换为16进制字符串
	 * 
	 * @param byt
	 *            字节
	 * @return 16进制字符串
	 */
	private static String byteToHexString(byte b) {
		return hexDigits[(b >> 4) & 0x0f] + hexDigits[b & 0x0f];
	}

	/**
	 * 将摘要信息转换为相应的编码
	 * 
	 * @param code
	 *            编码类型
	 * @param message
	 *            摘要信息
	 * @return 相应的编码字符串
	 */
	private static String hash(String code, String message) {
		MessageDigest md;
		String encode = null;

		try {
			md = MessageDigest.getInstance(code);
			encode = byteArrayToHexString(md.digest(message.getBytes()));

			return encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 计算message的MD5编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return MD5编码之后的字符串
	 */
	public static String md5Hash(String message) {
		return hash("MD5", message);
	}

	/**
	 * 计算message的SHA编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA编码之后的字符串
	 */
	public static String shaHash(String message) {
		return hash("SHA", message);
	}

	/**
	 * 计算message的SHA-256编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA-256编码之后的字符串
	 */
	public static String sha256Hash(String message) {
		return hash("SHA-256", message);
	}

	/**
	 * 计算message的SHA-512编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA-512编码之后的字符串
	 */
	public static String sha512Hash(String message) {
		return hash("SHA-512", message);
	}

	//获取文件的MD5值
    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
   //获取文件的md5值
   public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

}
