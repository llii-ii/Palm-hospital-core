/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28      MPI工具类
 * =============================================================================
 */
package com.kasite.client.unionpay.sdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.UnionPayEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import static com.kasite.client.unionpay.sdk.SDKConstants.CERTTYPE_01;
import static com.kasite.client.unionpay.sdk.SDKConstants.CERTTYPE_02;
import static com.kasite.client.unionpay.sdk.SDKConstants.POINT;
import static com.kasite.client.unionpay.sdk.SDKConstants.SIGNMETHOD_RSA;
import static com.kasite.client.unionpay.sdk.SDKConstants.SIGNMETHOD_SHA256;
import static com.kasite.client.unionpay.sdk.SDKConstants.SIGNMETHOD_SM3;
import static com.kasite.client.unionpay.sdk.SDKConstants.VERSION_5_0_0;
import static com.kasite.client.unionpay.sdk.SDKConstants.VERSION_1_0_0;
import static com.kasite.client.unionpay.sdk.SDKConstants.VERSION_5_1_0;
import static com.kasite.client.unionpay.sdk.SDKConstants.VERSION_5_0_1;
import static com.kasite.client.unionpay.sdk.SDKConstants.param_signMethod;
/**
 * 
 * @ClassName SDKUtil
 * @Description acpsdk工具类
 * @date 2016-7-22 下午4:06:18
 *
 */
public class SDKUtil {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	/**
	 * 根据signMethod的值，提供三种计算签名的方法
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @return 签名是否成功
	 */
	public static boolean sign(Map<String, String> data, String encoding,String configKey) {
		
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		String signMethod = data.get(param_signMethod);
		String version = data.get(SDKConstants.param_version);
		if (!VERSION_1_0_0.equals(version) && !VERSION_5_0_1.equals(version) && isEmpty(signMethod)) {
			LogUtil.info(log,"signMethod must Not null");
			return false;
		}
		
		if (isEmpty(version)) {
			LogUtil.info(log,"version must Not null");
			return false;
		}
		if (SIGNMETHOD_RSA.equals(signMethod)|| VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
			if (VERSION_5_0_0.equals(version)|| VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
				// 设置签名证书序列号
				data.put(SDKConstants.param_certId, CertUtil.init(configKey).getSignCertId());
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(data);
				LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
				byte[] byteSign = null;
				String stringSign = null;
				try {
					// 通过SHA1进行摘要并转16进制
					byte[] signDigest = SecureUtil
							.sha1X16(stringData, encoding);
					byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(
							CertUtil.init(configKey).getSignCertPrivateKey(), signDigest));
					stringSign = new String(byteSign);
					// 设置签名域值
					data.put(SDKConstants.param_signature, stringSign);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,"Sign Error", e);
					return false;
				}
			} else if (VERSION_5_1_0.equals(version)) {
				// 设置签名证书序列号
				data.put(SDKConstants.param_certId, CertUtil.init(configKey).getSignCertId());
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(data);
				LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
				byte[] byteSign = null;
				String stringSign = null;
				try {
					// 通过SHA256进行摘要并转16进制
					byte[] signDigest = SecureUtil
							.sha256X16(stringData, encoding);
					byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft256(
							CertUtil.init(configKey).getSignCertPrivateKey(), signDigest));
					stringSign = new String(byteSign);
					// 设置签名域值
					data.put(SDKConstants.param_signature, stringSign);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,"Sign Error", e);
					return false;
				}
			}
		}else if (SIGNMETHOD_SHA256.equals(signMethod)) {
			return signBySecureKey(data, KasiteConfig.getUnionPay(UnionPayEnum.secureKey, configKey), encoding);
		} else if (SIGNMETHOD_SM3.equals(signMethod)) {
			return signBySecureKey(data, KasiteConfig.getUnionPay(UnionPayEnum.secureKey, configKey), encoding);
		}
		return false;
	}

	/**
	 * 通过传入的证书绝对路径和证书密码读取签名证书进行签名并返回签名值<br>
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @param certPath
	 *            证书绝对路径
	 * @param certPwd
	 *            证书密码
	 * @return 签名值
	 */
	public static boolean signBySecureKey(Map<String, String> data, String secureKey,
			String encoding) {
		
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		if (isEmpty(secureKey)) {
			LogUtil.info(log,"secureKey is empty");
			return false;
		}
		String signMethod = data.get(param_signMethod);
		if (isEmpty(signMethod)) {
			LogUtil.info(log,"signMethod must Not null");
			return false;
		}
		
		if (SIGNMETHOD_SHA256.equals(signMethod)) {
			// 将Map信息转换成key1=value1&key2=value2的形式
			String stringData = coverMap2String(data);
			LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
			String strBeforeSha256 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sha256X16Str(secureKey, encoding);
			String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
					encoding);
			// 设置签名域值
			data.put(SDKConstants.param_signature, strAfterSha256);
			return true;
		} else if (SIGNMETHOD_SM3.equals(signMethod)) {
			String stringData = coverMap2String(data);
			LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
			String strBeforeSM3 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sm3X16Str(secureKey, encoding);
			String strAfterSM3 = SecureUtil.sm3X16Str(strBeforeSM3, encoding);
			// 设置签名域值
			data.put(SDKConstants.param_signature, strAfterSM3);
			return true;
		}
		return false;
	}

	/**
	 * 通过传入的签名密钥进行签名并返回签名值<br>
	 * 
	 * @param data
	 *            待签名数据Map键值对形式
	 * @param encoding
	 *            编码
	 * @param certPath
	 *            证书绝对路径
	 * @param certPwd
	 *            证书密码
	 * @return 签名值
	 */
	public static boolean signByCertInfo(Map<String, String> data,
			String certPath, String certPwd, String encoding,String configKey) {
		
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		if (isEmpty(certPath) || isEmpty(certPwd)) {
			LogUtil.info(log,"CertPath or CertPwd is empty");
			return false;
		}
		String signMethod = data.get(param_signMethod);
		String version = data.get(SDKConstants.param_version);
		if (!VERSION_1_0_0.equals(version) && !VERSION_5_0_1.equals(version) && isEmpty(signMethod)) {
			LogUtil.info(log,"signMethod must Not null");
			return false;
		}
		if (isEmpty(version)) {
			LogUtil.info(log,"version must Not null");
			return false;
		}
		
		if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
			if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
				// 设置签名证书序列号
				data.put(SDKConstants.param_certId, CertUtil.init(configKey).getCertIdByKeyStoreMap(certPath, certPwd));
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(data);
				LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
				byte[] byteSign = null;
				String stringSign = null;
				try {
					// 通过SHA1进行摘要并转16进制
					byte[] signDigest = SecureUtil
							.sha1X16(stringData, encoding);
					byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(
							CertUtil.init(configKey).getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
					stringSign = new String(byteSign);
					// 设置签名域值
					data.put(SDKConstants.param_signature, stringSign);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,"Sign Error", e);
					return false;
				}
			} else if (VERSION_5_1_0.equals(version)) {
				// 设置签名证书序列号
				data.put(SDKConstants.param_certId, CertUtil.init(configKey).getCertIdByKeyStoreMap(certPath, certPwd));
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(data);
				LogUtil.info(log,"待签名请求报文串:[" + stringData + "]");
				byte[] byteSign = null;
				String stringSign = null;
				try {
					// 通过SHA256进行摘要并转16进制
					byte[] signDigest = SecureUtil
							.sha256X16(stringData, encoding);
					byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft256(
							CertUtil.init(configKey).getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
					stringSign = new String(byteSign);
					// 设置签名域值
					data.put(SDKConstants.param_signature, stringSign);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,"Sign Error", e);
					return false;
				}
			}
			
		} 
		return false;
	}
	
	/**
	 * 验证签名
	 * 
	 * @param resData
	 *            返回报文数据
	 * @param encoding
	 *            编码格式
	 * @return
	 */
	public static boolean validateBySecureKey(Map<String, String> resData, String secureKey, String encoding) {
		LogUtil.info(log,"验签处理开始");
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		String signMethod = resData.get(SDKConstants.param_signMethod);
		if (SIGNMETHOD_SHA256.equals(signMethod)) {
			// 1.进行SHA256验证
			String stringSign = resData.get(SDKConstants.param_signature);
			LogUtil.info(log,"签名原文：["+stringSign+"]");
			// 将Map信息转换成key1=value1&key2=value2的形式
			String stringData = coverMap2String(resData);
			LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
			String strBeforeSha256 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sha256X16Str(secureKey, encoding);
			String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
					encoding);
			return stringSign.equals(strAfterSha256);
		} else if (SIGNMETHOD_SM3.equals(signMethod)) {
			// 1.进行SM3验证
			String stringSign = resData.get(SDKConstants.param_signature);
			LogUtil.info(log,"签名原文：["+stringSign+"]");
			// 将Map信息转换成key1=value1&key2=value2的形式
			String stringData = coverMap2String(resData);
			LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
			String strBeforeSM3 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sm3X16Str(secureKey, encoding);
			String strAfterSM3 = SecureUtil
					.sm3X16Str(strBeforeSM3, encoding);
			return stringSign.equals(strAfterSM3);
		}
		return false;
	}
	
	/**
	 * 验证签名
	 * 
	 * @param resData
	 *            返回报文数据
	 * @param encoding
	 *            编码格式
	 * @return
	 */
	public static boolean validate(Map<String, String> resData, String encoding,String configKey) {
		LogUtil.info(log,"验签处理开始");
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		String signMethod = resData.get(SDKConstants.param_signMethod);
		String version = resData.get(SDKConstants.param_version);
		if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
			// 获取返回报文的版本号
			if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
				String stringSign = resData.get(SDKConstants.param_signature);
				LogUtil.info(log,"签名原文：["+stringSign+"]");
				// 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
				String certId = resData.get(SDKConstants.param_certId);
				LogUtil.info(log,"对返回报文串验签使用的验签公钥序列号：["+certId+"]");
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(resData);
				LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
				try {
					// 验证签名需要用银联发给商户的公钥证书.
					return SecureUtil.validateSignBySoft(CertUtil.init(configKey)
							.getValidatePublicKey(certId), SecureUtil
							.base64Decode(stringSign.getBytes(encoding)),
							SecureUtil.sha1X16(stringData, encoding));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					LogUtil.error(log,e.getMessage(), e);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,e.getMessage(), e);
				}
			} else if (VERSION_5_1_0.equals(version)) {
				// 1.从返回报文中获取公钥信息转换成公钥对象
				String strCert = resData.get(SDKConstants.param_signPubKeyCert);
//				LogUtil.info(log,"验签公钥证书：["+strCert+"]");
				X509Certificate x509Cert = CertUtil.init(configKey).genCertificateByStr(strCert);
				if(x509Cert == null) {
					LogUtil.info(log,"convert signPubKeyCert failed");
					return false;
				}
				// 2.验证证书链
				if (!CertUtil.init(configKey).verifyCertificate(x509Cert)) {
					LogUtil.info(log,"验证公钥证书失败，证书信息：["+strCert+"]");
					return false;
				}
				
				// 3.验签
				String stringSign = resData.get(SDKConstants.param_signature);
				LogUtil.info(log,"签名原文：["+stringSign+"]");
				// 将Map信息转换成key1=value1&key2=value2的形式
				String stringData = coverMap2String(resData);
				LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
				try {
					// 验证签名需要用银联发给商户的公钥证书.
					boolean result = SecureUtil.validateSignBySoft256(x509Cert
							.getPublicKey(), SecureUtil.base64Decode(stringSign
							.getBytes(encoding)), SecureUtil.sha256X16(
							stringData, encoding));
					LogUtil.info(log,"验证签名" + (result? "成功":"失败"));
					return result;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					LogUtil.error(log,e.getMessage(), e);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,e.getMessage(), e);
				}
			}

		} else if (SIGNMETHOD_SHA256.equals(signMethod)) {
			// 1.进行SHA256验证
			String stringSign = resData.get(SDKConstants.param_signature);
			LogUtil.info(log,"签名原文：["+stringSign+"]");
			// 将Map信息转换成key1=value1&key2=value2的形式
			String stringData = coverMap2String(resData);
			LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
			String strBeforeSha256 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sha256X16Str(KasiteConfig.getUnionPay(UnionPayEnum.secureKey, configKey), encoding);
			String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
					encoding);
			boolean result =  stringSign.equals(strAfterSha256);
			LogUtil.info(log,"验证签名" + (result? "成功":"失败"));
			return result;
		} else if (SIGNMETHOD_SM3.equals(signMethod)) {
			// 1.进行SM3验证
			String stringSign = resData.get(SDKConstants.param_signature);
			LogUtil.info(log,"签名原文：["+stringSign+"]");
			// 将Map信息转换成key1=value1&key2=value2的形式
			String stringData = coverMap2String(resData);
			LogUtil.info(log,"待验签返回报文串：["+stringData+"]");
			String strBeforeSM3 = stringData
					+ SDKConstants.AMPERSAND
					+ SecureUtil.sm3X16Str(KasiteConfig.getUnionPay(UnionPayEnum.secureKey, configKey), encoding);
			String strAfterSM3 = SecureUtil
					.sm3X16Str(strBeforeSM3, encoding);
			boolean result =  stringSign.equals(strAfterSM3);
			LogUtil.info(log,"验证签名" + (result? "成功":"失败"));
			return result;
		}
		return false;
	}

	/**
	 * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (SDKConstants.param_signature.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue()
					+ SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}


	/**
	 * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> coverResultString2Map(String result) {
		return convertResultStringToMap(result);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map =null;
		try {

			if (StringUtils.isNotBlank(result)) {
				if (result.startsWith("{") && result.endsWith("}")) {
					result = result.substring(1, result.length() - 1);
				}
				map = parseQString(result);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LogUtil.error(log,e);
		}
		return map;
	}

	
	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseQString(String str)
			throws UnsupportedEncodingException {

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;//值里有嵌套
		char openName = 0;
		if(len>0){
			for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey) {// 如果当前生成的是key
					
					if (curChar == '=') {// 如果读取到=分隔符 
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else  {// 如果当前生成的是value
					if(isOpen){
						if(curChar == openName){
							isOpen = false;
						}
						
					}else{//如果没开启嵌套
						if(curChar == '{'){//如果碰到，就开启嵌套
							isOpen = true;
							openName ='}';
						}
						if(curChar == '['){
							isOpen = true;
							openName =']';
						}
					}
					if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					}else{
						temp.append(curChar);
					}
				}
				
			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
			String key, Map<String, String> map)
			throws UnsupportedEncodingException {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

	/**
	 * 
	 * 获取应答报文中的加密公钥证书,并存储到本地,并备份原始证书<br>
	 * 更新成功则返回1，无更新返回0，失败异常返回-1。
	 * 
	 * @param resData
	 * @param encoding
	 * @return
	 */
	public static int getEncryptCert(Map<String, String> resData,
			String encoding,String configKey) {
		String strCert = resData.get(SDKConstants.param_encryptPubKeyCert);
		String certType = resData.get(SDKConstants.param_certType);
		if (isEmpty(strCert) || isEmpty(certType))
			return -1;
		X509Certificate x509Cert = CertUtil.init(configKey).genCertificateByStr(strCert);
		if (CERTTYPE_01.equals(certType)) {
			// 更新敏感信息加密公钥
			if (!CertUtil.init(configKey).getEncryptCertId().equals(
					x509Cert.getSerialNumber().toString())) {
				// ID不同时进行本地证书更新操作
				String localCertPath = KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey);
				String newLocalCertPath = genBackupName(localCertPath);
				// 1.将本地证书进行备份存储
				if (!copyFile(localCertPath, newLocalCertPath))
					return -1;
				// 2.备份成功,进行新证书的存储
				if (!writeFile(localCertPath, strCert, encoding))
					return -1;
				LogUtil.info(log,"save new encryptPubKeyCert success");
				CertUtil.init(configKey).resetEncryptCertPublicKey();
				return 1;
			}else {
				return 0;
			}

		} else if (CERTTYPE_02.equals(certType)) {
//			// 更新磁道加密公钥
//			if (!CertUtil.init(configKey).getEncryptTrackCertId().equals(
//					x509Cert.getSerialNumber().toString())) {
//				// ID不同时进行本地证书更新操作
//				String localCertPath = SDKConfig.getConfig().getEncryptTrackCertPath();
//				String newLocalCertPath = genBackupName(localCertPath);
//				// 1.将本地证书进行备份存储
//				if (!copyFile(localCertPath, newLocalCertPath))
//					return -1;
//				// 2.备份成功,进行新证书的存储
//				if (!writeFile(localCertPath, strCert, encoding))
//					return -1;
//				LogUtil.info(log,"save new encryptPubKeyCert success");
//				CertUtil.init(configKey).resetEncryptTrackCertPublicKey();
//				return 1;
//			}else {
				return 0;
//			}
		}else {
			LogUtil.info(log,"unknown cerType:"+certType);
			return -1;
		}
	}
	
	/**
	 * 文件拷贝方法
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFile(String srcFile, String destFile) {
		boolean flag = false;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			// 获取源文件和目标文件的输入输出流
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(destFile);
			// 获取输入输出通道
			fcin = fin.getChannel();
			fcout = fout.getChannel();
			// 创建缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (true) {
				// clear方法重设缓冲区，使它可以接受读入的数据
				buffer.clear();
				// 从输入通道中将数据读到缓冲区
				int r = fcin.read(buffer);
				// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
				if (r == -1) {
					flag = true;
					break;
				}
				// flip方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();
				// 从输出通道中将数据写入缓冲区
				fcout.write(buffer);
			}
			fout.flush();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error(log,"CopyFile fail",e);
		} finally {
			try {
				if (null != fin)
					fin.close();
				if (null != fout)
					fout.close();
				if (null != fcin)
					fcin.close();
				if (null != fcout)
					fcout.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				LogUtil.error(log,"Releases any system resources fail",ex);
			}
		}
		return flag;
	}
	
	/**
	 * 写文件方法
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static boolean writeFile(String filePath, String fileContent,
			String encoding) {
		FileOutputStream fout = null;
		FileChannel fcout = null;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		
		try {
			fout = new FileOutputStream(filePath);
			// 获取输出通道
			fcout = fout.getChannel();
			// 创建缓冲区
			// ByteBuffer buffer = ByteBuffer.allocate(1024);
			ByteBuffer buffer = ByteBuffer.wrap(fileContent.getBytes(encoding));
			fcout.write(buffer);
			fout.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LogUtil.error(log,"WriteFile fail",e);
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			LogUtil.error(log,"WriteFile fail",ex);
			return false;
		} finally {
			try {
				if (null != fout)
					fout.close();
				if (null != fcout)
					fcout.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				LogUtil.error(log,"Releases any system resources fail",ex);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 将传入的文件名(xxx)改名 <br>
	 * 结果为： xxx_backup.cer
	 * 
	 * @param fileName
	 * @return
	 */
	public static String genBackupName(String fileName) {
		if (isEmpty(fileName))
			return "";
		int i = fileName.lastIndexOf(POINT);
		String leftFileName = fileName.substring(0, i);
		String rightFileName = fileName.substring(i + 1);
		String newFileName = leftFileName + "_backup" + POINT + rightFileName;
		return newFileName;
	}
	

	public static byte[] readFileByNIO(String filePath) {
		FileInputStream in = null;
		FileChannel fc = null;
		ByteBuffer bf = null;
		try {
			in = new FileInputStream(filePath);
			fc = in.getChannel();
			bf = ByteBuffer.allocate((int) fc.size());
			fc.read(bf);
			return bf.array();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,e);
			return null;
		} finally {
			try {
				if (null != fc) {
					fc.close();
				}
				if (null != in) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log,e);
				return null;
			}
		}
	}
	
	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * @param contentData
	 * @return
	 */
	public static Map<String, String> filterBlank(Map<String, String> contentData){
		LogUtil.info(log,"打印请求报文域 :");
		Map<String, String> submitFromData = new HashMap<String, String>();
		Set<String> keyset = contentData.keySet();
		
		for(String key:keyset){
			String value = contentData.get(key);
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(key, value.trim());
				LogUtil.info(log,key + "-->" + String.valueOf(value));
			}
		}
		return submitFromData;
	}
	
	/**
	 * 解压缩.
	 * 
	 * @param inputByte
	 *            byte[]数组类型的数据
	 * @return 解压缩后的数据
	 * @throws IOException
	 */
	public static byte[] inflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (Exception ex) {
			System.err.println("Data format error!\n");
			ex.printStackTrace();
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

	/**
	 * 压缩.
	 * 
	 * @param inputByte
	 *            需要解压缩的byte[]数组
	 * @return 压缩后的数据
	 * @throws IOException
	 */
	public static byte[] deflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}
	
	/**
	 * 判断字符串是否为NULL或空
	 * 
	 * @param s
	 *            待判断的字符串数据
	 * @return 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}
	
	 /**
	 * 功能：解析全渠道商户对账文件中的ZM文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZM文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer, String>> parseZMFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,21,2,32,2,6,10,13,13,4,15,2,2,6,2,4,32,1,21,15,1,15,32,13,13,8,32,13,13,12,2,1,32,98};
		return parseFile(filePath,lengthArray);
	}
	
	/**
	 * 功能：解析全渠道商户 ZM,ZME对账文件
	 * @param filePath
	 * @param lengthArray 参照《全渠道平台接入接口规范 第3部分 文件接口》 全渠道商户对账文件 6.1 ZM文件和6.2 ZME 文件 格式的类型长度组成int型数组
	 * @return
	 */
	private static List<Map<Integer, String>> parseFile(String filePath,int lengthArray[]){
	 	List<Map<Integer, String>> ZmDataList = new ArrayList<Map<Integer, String>>();
	 	try {
            String encoding="gbk"; //文件是gbk编码
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), "iso-8859-1");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	byte[] bs = lineTxt.getBytes("iso-8859-1");
                	//解析的结果MAP，key为对账文件列序号，value为解析的值
        		 	Map<Integer,String> ZmDataMap = new LinkedHashMap<Integer,String>();
                    //左侧游标
                    int leftIndex = 0;
                    //右侧游标
                    int rightIndex = 0;
                    for(int i=0;i<lengthArray.length;i++){
                    	rightIndex = leftIndex + lengthArray[i];
                    	String filed = new String(Arrays.copyOfRange(bs, leftIndex,rightIndex), encoding);
                    	leftIndex = rightIndex+1;
                    	ZmDataMap.put(i, filed);
                    }
                    ZmDataList.add(ZmDataMap);
                }
                read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
	 	
		return ZmDataList;	
	}
	
	public static List<String> unzip(String zipFilePath,String outPutDirectory){
		List<String> fileList = new ArrayList<String>();
		try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFilePath));//输入源zip路径  
            BufferedInputStream bin = new BufferedInputStream(zin);
            BufferedOutputStream bout = null;
            File file=null;  
            ZipEntry entry;
            try {
                while((entry = zin.getNextEntry())!=null && !entry.isDirectory()){
                	file = new File(outPutDirectory,entry.getName());  
                    if(!file.exists()){  
                        (new File(file.getParent())).mkdirs();  
                    }
                    bout = new BufferedOutputStream(new FileOutputStream(file));  
                    int b;
                    while((b=bin.read())!=-1){  
                    	bout.write(b);  
                    }
                    bout.flush();
                    fileList.add(file.getAbsolutePath());
                    System.out.println(file+"解压成功");
                }
            } catch (IOException e) {  
                e.printStackTrace();  
            }finally{
                try {
					bin.close();
					zin.close();
					if(bout!=null){
						bout.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}  
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
		return fileList;
	}

	public static String getFileContentTable(List<Map> dataList, String file) {
		StringBuffer tableSb = new StringBuffer(
				"对账文件的规范参考 https://open.unionpay.com/ajweb/help/file/ 产品接口规范->平台接口规范:文件接口</br> 文件【" + file
						+ "】解析后内容如下：");
		tableSb.append("<table border=\"1\">");
		if (dataList.size() > 0) {
			Map<Integer, String> dataMapTmp = dataList.get(0);
			tableSb.append("<tr>");
			for (Iterator<Integer> it = dataMapTmp.keySet().iterator(); it.hasNext();) {
				Integer key = it.next();
				String value = dataMapTmp.get(key);
				System.out.println("序号：" + (key + 1) + " 值: '" + value + "'");
				tableSb.append("<td>序号" + (key + 1) + "</td>");
			}
			tableSb.append("</tr>");
		}

		for (int i = 0; i < dataList.size(); i++) {
			System.out.println("行数: " + (i + 1));
			Map<Integer, String> dataMapTmp = dataList.get(i);
			tableSb.append("<tr>");
			for (Iterator<Integer> it = dataMapTmp.keySet().iterator(); it.hasNext();) {
				Integer key = it.next();
				String value = dataMapTmp.get(key);
				System.out.println("序号：" + (key + 1) + " 值: '" + value + "'");
				tableSb.append("<td>" + value + "</td>");
			}
			tableSb.append("</tr>");
		}
		tableSb.append("</table>");
		return tableSb.toString();
	}
	
	/**
	 * 功能：解析全渠道商户对账文件中的ZME文件并以List<Map>方式返回
	 * 适用交易：对账文件下载后对文件的查看
	 * @param filePath ZME文件全路径
	 * @return 包含每一笔交易中 序列号 和 值 的map序列
	 */
	public static List<Map<Integer, String>> parseZMEFile(String filePath){
		int lengthArray[] = {3,11,11,6,10,19,12,4,2,2,6,10,4,12,13,13,15,15,1,12,2,135};
		return parseFile(filePath,lengthArray);
	}
}
