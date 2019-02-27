package com.kasite.core.common.util.rsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;



public class KasiteRSAUtil {

	/** RSA最大加密明文大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** RSA最大解密密文大小 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	
	/**
	 * 公钥加密
	 * 
	 * @param content   待加密内容
	 * @param publicKey 公钥
	 * @param charset   字符集，如UTF-8, GBK, GB2312
	 * @return 密文内容
	 * @throws KasiteRSAException
	 */
	public static String rsaEncrypt(String content, String publicKey, String charset) throws KasiteRSAException {
		try {
			PublicKey pubKey = getPublicKeyFromX509(KasiteRSAConstants.SIGN_TYPE_RSA,
					new ByteArrayInputStream(publicKey.getBytes()));
			Cipher cipher = Cipher.getInstance(KasiteRSAConstants.SIGN_TYPE_RSA);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] data = StringUtils.isEmpty(charset) ? content.getBytes() : content.getBytes(charset);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
			out.close();
			return StringUtils.isEmpty(charset) ? new String(encryptedData) : new String(encryptedData, charset);
		} catch (Exception e) {
			throw new KasiteRSAException("EncryptContent = " + content + ",charset = " + charset, e);
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param content    待解密内容
	 * @param privateKey 私钥
	 * @param charset    字符集，如UTF-8, GBK, GB2312
	 * @return 明文内容
	 * @throws KasiteRSAException
	 */
	public static String rsaDecrypt(String content, String privateKey, String charset) throws KasiteRSAException {
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8(KasiteRSAConstants.SIGN_TYPE_RSA,
					new ByteArrayInputStream(privateKey.getBytes()));
			Cipher cipher = Cipher.getInstance(KasiteRSAConstants.SIGN_TYPE_RSA);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			byte[] encryptedData = StringUtils.isEmpty(charset) ? Base64.decodeBase64(content.getBytes())
					: Base64.decodeBase64(content.getBytes(charset));
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();

			return StringUtils.isEmpty(charset) ? new String(decryptedData) : new String(decryptedData, charset);
		} catch (Exception e) {
			throw new KasiteRSAException("EncodeContent = " + content + ",charset = " + charset, e);
		}
	}

	/**
	 * 生成公钥、私钥字符串
	 * @throws KasiteRSAException
	 */
	public static void getPrivateAndPublicKey() throws KasiteRSAException {
		try {
			// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		
	        // 初始化密钥对生成器，密钥大小为96-1024位  
	        keyPairGen.initialize(1024,new SecureRandom());  
	        // 生成一个密钥对，保存在keyPair中  
	        KeyPair keyPair = keyPairGen.generateKeyPair();  
	        // 得到私钥  
	        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
	        // 得到公钥  
	        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
	        // 得到公钥字符串  
	        String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());  
	        // 得到私钥字符串  
	        String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());  
	        System.out.println("公钥："+publicKeyString);
	        System.out.println("私钥："+privateKeyString);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new KasiteRSAException("没有找到加密算法", e);
		}  
	}
	
	/**
	 * rsa内容签名
	 * 
	 * @param content
	 * @param privateKey
	 * @param charset
	 * @return
	 * @throws KasiteRSAException
	 */
	public static String rsaSign(String content, String privateKey, String charset, String signType)
			throws KasiteRSAException {
		java.security.Signature signature = null;
		try {
			if (KasiteRSAConstants.SIGN_TYPE_RSA.equals(signType)) {
				signature = java.security.Signature.getInstance(KasiteRSAConstants.SIGN_ALGORITHMS);
			} else if (KasiteRSAConstants.SIGN_TYPE_RSA2.equals(signType)) {
				signature = java.security.Signature.getInstance(KasiteRSAConstants.SIGN_SHA256RSA_ALGORITHMS);
			} else {
				throw new KasiteRSAException("没有找到对应的签名算法 : signType=" + signType);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new KasiteRSAException("初始化签名对象异常 : signType=" + signType);
		}
		try {
			PrivateKey priKey = getPrivateKeyFromPKCS8(KasiteRSAConstants.SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
			signature.initSign(priKey);
			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed));
		} catch (InvalidKeySpecException ie) {
			throw new KasiteRSAException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
		} catch (Exception e) {
			throw new KasiteRSAException("RSAcontent = " + content + "; charset = " + charset, e);
		}
	}
	/**
	 * 	RSA签名
	 * @param params
	 * @param privateKey
	 * @param charset
	 * @return
	 * @throws KasiteRSAException
	 */
	public static String rsaSign(Map<String, String> params, String privateKey, String charset)
			throws KasiteRSAException {
		String signContent = getSignContent(params);
		return rsaSign(signContent, privateKey, charset,KasiteRSAConstants.SIGN_TYPE_RSA2);
	}

	/**
	 * 	根据加密算法获取对应的私钥key
	 * @param algorithm
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
		if (ins == null || StringUtils.isEmpty(algorithm)) {
			return null;
		}
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		byte[] encodedKey = StreamUtils.readText(ins).getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}
	/**
	 * 根据算法获取公钥key
	 * @param algorithm
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		StringWriter writer = new StringWriter();
		StreamUtils.io(new InputStreamReader(ins), writer);
		byte[] encodedKey = writer.toString().getBytes();
		encodedKey = Base64.decodeBase64(encodedKey);
		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
	}

	/**
	 * 	签名验证
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @param charset
	 * @param signType
	 * @return
	 * @throws KasiteRSAException
	 */
	public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType)
			throws KasiteRSAException {
		java.security.Signature signature = null;
		try {
			if (KasiteRSAConstants.SIGN_TYPE_RSA.equals(signType)) {
					signature = java.security.Signature.getInstance(KasiteRSAConstants.SIGN_ALGORITHMS);
			} else if (KasiteRSAConstants.SIGN_TYPE_RSA2.equals(signType)) {
				signature = java.security.Signature.getInstance(KasiteRSAConstants.SIGN_SHA256RSA_ALGORITHMS);
			} else {
				throw new KasiteRSAException("没有找到对应的签名算法 : signType=" + signType);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new KasiteRSAException("初始化签名对象异常 : signType=" + signType);
		}
		try {
			PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
			signature.initVerify(pubKey);
			if (StringUtils.isEmpty(charset)) {
				signature.update(content.getBytes());
			} else {
				signature.update(content.getBytes(charset));
			}
			return signature.verify(Base64.decodeBase64(sign.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new KasiteRSAException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, e);
		}

	}
	/**
	 * 	获取需要签名的内容
	 * 	将Map内容排序拼接成 &key=value的形式
	 * @param sortedParams
	 * @return
	 */
	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParams.get(key);
			if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				index++;
			}
		}
		return content.toString();
	}
	
	/**
	 * 	获取需要验证签名的内容
	 * @param params
	 * @return
	 */
	public static String getSignCheckContent(Map<String, String> params) {
		if (params == null) {
			return null;
		}
		params.remove("sign");
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}

		return content.toString();
	}

	/**
	 * 	签名验证
	 * @param params
	 * @param publicKey
	 * @param charset
	 * @return
	 * @throws KasiteRSAException
	 */
	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset)
			throws KasiteRSAException {
		String sign = params.get("sign");
		String content = getSignCheckContent(params);

		return rsaCheck(content, sign, publicKey, charset,KasiteRSAConstants.SIGN_TYPE_RSA2);
	}
	/**
	 * 	签名验证
	 * @param params
	 * @param publicKey
	 * @param charset
	 * @param signType
	 * @return
	 * @throws KasiteRSAException
	 */
	public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset, String signType)
			throws KasiteRSAException {
		String sign = params.get("sign");
		String content = getSignCheckContent(params);

		return rsaCheck(content, sign, publicKey, charset, signType);
	}
	

	public static void testSwPay() throws KasiteRSAException {
		String content = "<Req>" + 
				"<TransactionCode></TransactionCode>" + 
				"<Data>" + 
				"<HisOrderId>6cb8252217f44f0e853a41df4ab2b86a</HisOrderId> " + 
				"<TotalFee>1</TotalFee> " + 
				"<Subject>测试当面付</Subject>" + 
				"<OrderMemo>订单说明:测试当面付</OrderMemo>" + 
				"<HisMemberId>his患者唯一标识</HisMemberId>" + 
				"<CardNo>C00123</CardNo> " + 
				"<CardType>1</CardType> " + 
				"<OperatorId>操作人ID</OperatorId> " + 
				"<OperatorName>操作人姓名</OperatorName> " + 
				"<ServiceId>006</ServiceId> " + 
				"<EqptType>5</EqptType> " + 
				"<AuthCode>134866057602026711【微信收钱授权码-扫码枪扫出来的】</AuthCode> " + 
				"<DeviceInfo>扫码枪</DeviceInfo> " + 
				"</Data>" + 
				"</Req>";
		//解密使用的私钥   -- 提供给请求发起方
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJUtQjAcWlNfWpEGAJwlQ8yarb8r0CqIKjIlGg8XSde1elVYv/+Qqt4caSwsNewek8WodyN9dZVlX8mwZ9dKFsENmYc2LT1C0OQmmBA7L6y0wIFPDjB1JsBGGOEPybnZProrH1KX/o+gywEVHA6Jc0kkOk4Su6mMchQHxlpnB2lFAgMBAAECgYBsIlyCJ2tKWCp8SY+ToGefHtinZQkGa6G6q65SFh2O4ZwF1cmHZcSWMJ4ESR/lQtz3QIDsFvtvCgb/WoJLSwdmVBGxCrV37APIyNv4HTEQNqxHgtCw9OIhVCqY/S21+HdPd/EevJI4L5kzizk/iIZuZScrfU/Yz4gKIjaTCn7bGQJBAPCUvRkFCjdCfIF2CxrQBlSMlBSO13hviCk7UjNtkB8Tg7pYm2ZfOmQwPa9XwhRJcg4gS3NskWFSPZedvDVO91cCQQCevNUVVGqbO92lNw15C5aDOjK/slDoAY/UdkfvSBxJX9yMeLFobpp5EfJJ2/6XQM1gwqIFK+epcaFr++JbT87DAkBhTSb12Y36L4+/pIh1PbFxvLGfZu5KrYE6e92RIzzo9LGQSon/a4BoSQKJykqafeH9SJ57fmuGGwcCLS+Tt+5dAkBnnOaRp6p2bVs97nNy4Wd7fM+HZUN+TRavHg3SWIVuN45kepuCiT93a4l1QI4r+HaWhDTxnzj112AAdkfIRo21AkEAogyby6Zd4z/oav2/nj2QxK/toSecHJAdzkM4WB9s8PSNRtV9Dyic48js+HUiWhrXUX5wJITdKIDQejrzqZg0rQ==";
		String encryptAndSign = KasiteRSAUtil.rsaSign(content, privateKey, "utf-8", "RSA2");
		
		
		
		System.out.println(encryptAndSign);
	}
	
	
	public static void main(String[] args) throws Exception {
		
//		testSwPay();
		
		getPrivateAndPublicKey();
		//非对称加密说明
		//请求方请求的报文
		String request = "<Request><content>RSA加密、解密、签名、验证测试。。。。。。。。。。。。。</content><sign>签名</sign></Request>";
		//需要加签名的内容
		String requestContent = "<content>RSA加密、解密、签名、验证测试。。。。。。。。。。。。。</content>";
		//加密使用公钥	    -- 内部自己使用
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVLUIwHFpTX1qRBgCcJUPMmq2/K9AqiCoyJRoPF0nXtXpVWL//kKreHGksLDXsHpPFqHcjfXWVZV/JsGfXShbBDZmHNi09QtDkJpgQOy+stMCBTw4wdSbARhjhD8m52T66Kx9Sl/6PoMsBFRwOiXNJJDpOErupjHIUB8ZaZwdpRQIDAQAB";
		//解密使用的私钥   -- 提供给请求发起方
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJUtQjAcWlNfWpEGAJwlQ8yarb8r0CqIKjIlGg8XSde1elVYv/+Qqt4caSwsNewek8WodyN9dZVlX8mwZ9dKFsENmYc2LT1C0OQmmBA7L6y0wIFPDjB1JsBGGOEPybnZProrH1KX/o+gywEVHA6Jc0kkOk4Su6mMchQHxlpnB2lFAgMBAAECgYBsIlyCJ2tKWCp8SY+ToGefHtinZQkGa6G6q65SFh2O4ZwF1cmHZcSWMJ4ESR/lQtz3QIDsFvtvCgb/WoJLSwdmVBGxCrV37APIyNv4HTEQNqxHgtCw9OIhVCqY/S21+HdPd/EevJI4L5kzizk/iIZuZScrfU/Yz4gKIjaTCn7bGQJBAPCUvRkFCjdCfIF2CxrQBlSMlBSO13hviCk7UjNtkB8Tg7pYm2ZfOmQwPa9XwhRJcg4gS3NskWFSPZedvDVO91cCQQCevNUVVGqbO92lNw15C5aDOjK/slDoAY/UdkfvSBxJX9yMeLFobpp5EfJJ2/6XQM1gwqIFK+epcaFr++JbT87DAkBhTSb12Y36L4+/pIh1PbFxvLGfZu5KrYE6e92RIzzo9LGQSon/a4BoSQKJykqafeH9SJ57fmuGGwcCLS+Tt+5dAkBnnOaRp6p2bVs97nNy4Wd7fM+HZUN+TRavHg3SWIVuN45kepuCiT93a4l1QI4r+HaWhDTxnzj112AAdkfIRo21AkEAogyby6Zd4z/oav2/nj2QxK/toSecHJAdzkM4WB9s8PSNRtV9Dyic48js+HUiWhrXUX5wJITdKIDQejrzqZg0rQ==";
		//第一步 请求方  将请求报文内容需要加签部分进行加签
		String encryptAndSign = KasiteRSAUtil.rsaSign(requestContent, privateKey, "utf-8", "RSA2");
		System.out.println("签名  Sign = "+encryptAndSign);
		//第二步将签名加入到请求报文内
		request = "<Request>" + 
				requestContent
				+ "<sign>"+
				encryptAndSign
				+"</sign>"
				+ "</Request>";
		System.out.println("请求方的请求报文内容："+request);
		
		//接收方
		//第一步： 通过 签名验证签名的内容
		boolean bb = KasiteRSAUtil.rsaCheck(requestContent, encryptAndSign, publicKey, "utf-8", "RSA2");
		System.out.println("验签结果："+bb);
		//第二步：通过加密方式加密返回的报文内容
		//应答方返回的报文
		String content = "<Response><title>测试</title><content>RSA加密、解密、签名、验证测试。。。。。。。。。。。。。</content></Response>";
		String encryContent = KasiteRSAUtil.rsaEncrypt(content, publicKey, "utf-8");
		System.out.println("公钥加密后的请求内容："+encryContent);
		//请求方收到应答报文
		//进行报文解密
		String decryContent = KasiteRSAUtil.rsaDecrypt("IY4gq8PsSv9yF/VNZelgrBgrpcAXyWFY4Sp9hBxCicnWdYwHDvFs8PSNBFOkQ1y4hn6Jina0cdcvRk9/33XJceP7L//nPikw1UuqYk/ctBj4K/f434Zd7hS/gWrRe7IPspCnK3P/4AAxbkl+h4mrN/7EBVMDbDiOPJ0zYURmywUvfCAyTQxiSbmDotRwgkG/JRzQL4I7wwJVwKyAsSNemF1KOqVfyxVE0ctKp3SIFXLNzyCS3LjI5/tH0YUcUffq/qPi/0ooZUB7SMPR6dFu71LBISrh7GDvDGkagnVz0uEv+g7s6y4PCPt3vAJDlcXi2gTM27bHo+a/sNkC5ySbWFhV8N/A8Hqr8O37+c9S6us8OT9kwpMBZVrAKAMxgrkckDBT86+xU9xOBNoUqH6Pr4MpOub9dP2JHW8XaXPifKbCxkU0fKpNfMkFn+mHbSa+cOorZtPJUu23vooPHR3sTzndyeF0QtxmgdPhAgePq2UA3sAIa0A3UyAv+CygdLmm", privateKey, "utf-8");
		System.out.println("私钥解密后的请求内容："+decryContent);
		
	}
	
	
}
