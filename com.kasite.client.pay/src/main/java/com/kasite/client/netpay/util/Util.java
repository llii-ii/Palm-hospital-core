package com.kasite.client.netpay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Util {
	//公钥                  
	// 商户密钥  
	public static final String SECRET_KEY = "1234567890abcABC";
	public static final String CHARSET = "GBK";



	/**
	 * 发送POST请求
	 */
	public static String uploadParam(String jsonParam, String url, String charset) {
		System.out.println("URL:　" + url);
		System.out.println(jsonParam);
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection urlCon = (HttpURLConnection) httpUrl.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			urlCon.setDoOutput(true);
			urlCon.setDoInput(true);
			urlCon.setReadTimeout(5 * 1000);
			out = new OutputStreamWriter(urlCon.getOutputStream(), charset);// 指定编码格式
			out.write("jsonRequestData=" + jsonParam);
			out.flush();

			in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), charset));
			String str = null;
			while ((str = in.readLine()) != null) {
				result.append(str);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * 获取当前时间戳
	 */
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(System.currentTimeMillis());
	}

	/**
	 * DES加密
	 */
	public static String DesEncrypt(byte[] plain, byte[] key) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKeySpec = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKeySpec);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");// DES/ECB/PKCS5Padding
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			byte[] byteBuffer = cipher.doFinal(plain);
			// 將 byte转换为16进制string
			StringBuffer strHexString = new StringBuffer();
			for (int i = 0; i < byteBuffer.length; i++) {
				String hex = Integer.toHexString(0xff & byteBuffer[i]);
				if (hex.length() == 1) {
					strHexString.append('0');
				}
				strHexString.append(hex);
			}
			return strHexString.toString();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RC4加密
	 */
	public static String RC4Encrypt(byte[] plain, byte[] key) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, "RC4");
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("RC4");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			// 现在，获取数据并加密
			// 正式执行加密操作
			byte[] byteBuffer = cipher.doFinal(plain);
			// 將 byte转换为16进制string
			StringBuffer strHexString = new StringBuffer();
			for (int i = 0; i < byteBuffer.length; i++) {
				String hex = Integer.toHexString(0xff & byteBuffer[i]);
				if (hex.length() == 1) {
					strHexString.append('0');
				}
				strHexString.append(hex);
			}
			return strHexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
