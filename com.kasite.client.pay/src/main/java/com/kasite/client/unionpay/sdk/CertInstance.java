package com.kasite.client.unionpay.sdk;

import static com.kasite.client.unionpay.sdk.SDKConstants.UNIONPAY_CNNAME;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.client.unionpay.constants.UnionPayConstant;
import com.kasite.client.unionpay.sdk.CertUtil.CerFilter;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.UnionPayEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;

/**
 * 银联整证书实体类
 * @author linjf
 * TODO
 */
public class CertInstance {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	/** 证书容器，存储对商户请求报文签名私钥证书. */
	private  KeyStore keyStore = null;
	/** 敏感信息加密公钥证书 */
	private  X509Certificate encryptCert = null;
	/** 磁道加密公钥 */
	private  PublicKey encryptTrackKey = null;
	/** 验证银联返回报文签名证书. */
	private  X509Certificate validateCert = null;
	/** 验签中级证书 */
	private  X509Certificate middleCert = null;
	/** 验签根证书 */
	private  X509Certificate rootCert = null;
	/** 验证银联返回报文签名的公钥证书存储Map. */
	private  Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	/** 商户私钥存储Map */
	private Map<String, KeyStore> keyStoreMap = new ConcurrentHashMap<String, KeyStore>();
	
	private String configKey;
	
	public CertInstance(String configKey) {
		this.configKey = configKey;
	}
	
	/**
	 * 用配置文件acp_sdk.properties中配置的私钥路径和密码 加载签名证书
	 */
	public void initSignCert() {
		if(!"01".equals(UnionPayConstant.SIGNMETHOD)){
			LogUtil.info(log,"非rsa签名方式，不加载签名证书。");
			return;
		}
		if (KasiteConfig.getUnionPay(UnionPayEnum.signCertPath, configKey) == null 
				|| KasiteConfig.getUnionPay(UnionPayEnum.signCertPwd, configKey) == null
				|| KasiteConfig.getUnionPay(UnionPayEnum.signCertType, configKey) == null) {
			LogUtil.info(log,"WARN: signCertPath或signCertPwd或signCertType为空。 停止加载签名证书。");
			return;
		}
		if (null != keyStore) {
			keyStore = null;
		}
		try {
			keyStore = getKeyInfo(KasiteConfig.getUnionPay(UnionPayEnum.signCertPath, configKey),
					KasiteConfig.getUnionPay(UnionPayEnum.signCertPwd, configKey),
					KasiteConfig.getUnionPay(UnionPayEnum.signCertType, configKey));
			LogUtil.info(log,"InitSignCert Successful. CertId=["
					+ getSignCertId() + "]");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error(log,e);
		}
	}
	
	
	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	public void initMiddleCert() {
		LogUtil.info(log,"加载中级证书==>"+KasiteConfig.getUnionPay(UnionPayEnum.middleCertPath, configKey));
		if (!StringUtil.isEmpty(KasiteConfig.getUnionPay(UnionPayEnum.middleCertPath, configKey))) {
			middleCert = initCert(KasiteConfig.getUnionPay(UnionPayEnum.middleCertPath, configKey));
			LogUtil.info(log,"Load MiddleCert Successful");
		} else {
			LogUtil.info(log,"WARN: acpsdk.middle.path is empty");
		}
	}
	
	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	public  void initRootCert() {
		LogUtil.info(log,"加载根证书==>"+KasiteConfig.getUnionPay(UnionPayEnum.rootCertPath, configKey));
		if (!StringUtil.isEmpty(KasiteConfig.getUnionPay(UnionPayEnum.rootCertPath, configKey))) {
			rootCert = initCert(KasiteConfig.getUnionPay(UnionPayEnum.rootCertPath, configKey));
			LogUtil.info(log,"Load RootCert Successful");
		} else {
			LogUtil.info(log,"WARN: acpsdk.rootCert.path is empty");
		}
	}
	
	/**
	 * 用配置文件acp_sdk.properties配置路径 加载银联公钥上级证书（中级证书）
	 */
	public  void initEncryptCert() {
		LogUtil.info(log,"加载敏感信息加密证书==>"+KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey));
		if (!StringUtil.isEmpty(KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey))) {
			encryptCert = initCert(KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey));
			LogUtil.info(log,"Load EncryptCert Successful");
		} else {
			LogUtil.info(log,"WARN: acpsdk.encryptCert.path is empty");
		}
	}
	
	/**
	 * 用配置文件acp_sdk.properties配置路径 加载磁道公钥
	 */
	public void initTrackKey() {
		if (!StringUtil.isEmpty(KasiteConfig.getUnionPay(UnionPayEnum.encryptTrackKeyModulus, configKey))
				&& !StringUtil.isEmpty(KasiteConfig.getUnionPay(UnionPayEnum.encryptTrackKeyExponent, configKey))) {
			encryptTrackKey = getPublicKey(KasiteConfig.getUnionPay(UnionPayEnum.encryptTrackKeyModulus, configKey), 
					KasiteConfig.getUnionPay(UnionPayEnum.encryptTrackKeyExponent, configKey));
			LogUtil.info(log,"LoadEncryptTrackKey Successful");
		} else {
			LogUtil.info(log,"WARN: acpsdk.encryptTrackKey.modulus or acpsdk.encryptTrackKey.exponent is empty");
		}
	}
	
	/**
	 *  加载验证签名证书
	 */
	public void initValidateCertFromDir() {
		if(!"01".equals(UnionPayConstant.SIGNMETHOD)){
			LogUtil.info(log,"非rsa签名方式，不加载验签证书。");
			return;
		}
		certMap.clear();
		String dir = KasiteConfig.getUnionPay(UnionPayEnum.validateCertDir, configKey);
		LogUtil.info(log,"加载验证签名证书目录==>" + dir +" 注：如果请求报文中version=5.1.0那么此验签证书目录使用不到，可以不需要设置（version=5.0.0必须设置）。");
		if (StringUtil.isEmpty(dir)) {
			LogUtil.info(log,"WARN: acpsdk.validateCert.dir is empty");
			return;
		}
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
		}catch (NoSuchProviderException e) {
			e.printStackTrace();
			LogUtil.error(log,"LoadVerifyCert Error: No BC Provider", e);
			return ;
		} catch (CertificateException e) {
			e.printStackTrace();
			LogUtil.error(log,"LoadVerifyCert Error", e);
			return ;
		}
		File fileDir = new File(dir);
		File[] files = fileDir.listFiles(new CerFilter());
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				in = new FileInputStream(file.getAbsolutePath());
				validateCert = (X509Certificate) cf.generateCertificate(in);
				if(validateCert == null) {
					LogUtil.info(log,"Load verify cert error, " + file.getAbsolutePath() + " has error cert content.");
					continue;
				}
				certMap.put(validateCert.getSerialNumber().toString(),
						validateCert);
				// 打印证书加载信息,供测试阶段调试
				LogUtil.info(log,"[" + file.getAbsolutePath() + "][CertId="
						+ validateCert.getSerialNumber().toString() + "]");
			} catch (CertificateException e) {
				e.printStackTrace();
				LogUtil.error(log,"LoadVerifyCert Error",e);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
				LogUtil.error(log,"LoadVerifyCert Error File Not Found",e);
			}finally {
				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
						LogUtil.error(log,e);
					}
				}
			}
		}
		LogUtil.info(log,"LoadVerifyCert Finish");
	}

	/**
	 * 用给定的路径和密码 加载签名证书，并保存到certKeyStoreMap
	 * 
	 * @param certFilePath
	 * @param certPwd
	 */
	public void loadSignCert(String certFilePath, String certPwd) {
		KeyStore keyStore = null;
		try {
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			keyStoreMap.put(certFilePath, keyStore);
			LogUtil.info(log,"LoadRsaCert Successful");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error(log,"LoadRsaCert Error", e);
		}
	}
	
	/**
	 * 通过keyStore 获取私钥签名证书PrivateKey对象
	 * 
	 * @return
	 */
	public PrivateKey getSignCertPrivateKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
					KasiteConfig.getUnionPay(UnionPayEnum.signCertPwd, configKey).toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKey Error",e);
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKey Error",e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKey Error",e);
			return null;
		}
	}
	
	/**
	 * 通过指定路径的私钥证书  获取PrivateKey对象
	 * 
	 * @return
	 */
	public PrivateKey getSignCertPrivateKeyByStoreMap(String certPath,
			String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			loadSignCert(certPath, certPwd);
		}
		try {
			Enumeration<String> aliasenum = keyStoreMap.get(certPath)
					.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStoreMap.get(certPath)
					.getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKeyByStoreMap Error",e);
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKeyByStoreMap Error",e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertPrivateKeyByStoreMap Error",e);
			return null;
		}
	}
	
	/**
	 * 通过证书路径初始化为公钥证书
	 * @param path
	 * @return
	 */
	private X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			LogUtil.info(log,"[" + path + "][CertId="
					+ encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			e.printStackTrace();
			LogUtil.error(log,"InitCert Error", e);
		} catch (FileNotFoundException e) {
			LogUtil.error(log,"InitCert Error File Not Found", e);
		} catch (NoSuchProviderException e) {
			LogUtil.error(log,"LoadVerifyCert Error No BC Provider", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.error(log,e);
				}
			}
		}
		return encryptCertTemp;
	}
	
	/**
	 * 获取敏感信息加密证书PublicKey
	 * 
	 * @return
	 */
	public PublicKey getEncryptCertPublicKey() {
		if (null == encryptCert) {
			String path = KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey);
			if (!StringUtil.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else {
				LogUtil.info(log,"acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getPublicKey();
		}
	}

	
	/**
	 * 重置敏感信息加密证书公钥
	 */
	public void resetEncryptCertPublicKey() {
		encryptCert = null;
	}
	
	/**
	 * 获取磁道加密证书PublicKey
	 * 
	 * @return
	 */
	public PublicKey getEncryptTrackPublicKey() {
		if (null == encryptTrackKey) {
			initTrackKey();
		}
		return encryptTrackKey;
	}
	
	/**
	 * 通过certId获取验签证书Map中对应证书PublicKey
	 * 
	 * @param certId 证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public PublicKey getValidatePublicKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir();
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				LogUtil.info(log,"缺少certId=[" + certId + "]对应的验签证书.");
				return null;
			}
		}
	}
	
	/**
	 * 获取配置文件acp_sdk.properties中配置的签名私钥证书certId
	 * 
	 * @return 证书的物理编号
	 */
	public String getSignCertId() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore
					.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"getSignCertId Error",e);
			return null;
		}
	}
	
	/**
	 * 获取敏感信息加密证书的certId
	 * 
	 * @return
	 */
	public String getEncryptCertId() {
		if (null == encryptCert) {
			String path = KasiteConfig.getUnionPay(UnionPayEnum.encryptCertPath, configKey);
			if (!StringUtil.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else {
				LogUtil.info(log,"acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getSerialNumber().toString();
		}
	}
	/* 通过签名私钥证书路径，密码获取私钥证书certId
	 * @param certPath
	 * @param certPwd
	 * @return
	 */
	public String getCertIdByKeyStoreMap(String certPath, String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			// 缓存中未查询到,则加载RSA证书
			loadSignCert(certPath, certPwd);
		}
		return getCertIdIdByStore(keyStoreMap.get(certPath));
	}
	
	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的中级证书
	 * @return
	 */
	public X509Certificate getMiddleCert() {
		if (null == middleCert) {
			String path = KasiteConfig.getUnionPay(UnionPayEnum.middleCertPath, configKey);
			if (!StringUtil.isEmpty(path)) {
				initMiddleCert();
			} else {
				LogUtil.info(log,"middleCertPath not set in KasiteConfig");
				return null;
			}
		}
		return middleCert;
	}
	
	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的根证书
	 * @return
	 */
	public X509Certificate getRootCert() {
		if (null == rootCert) {
			String path =  KasiteConfig.getUnionPay(UnionPayEnum.rootCertPath, configKey);
			if (!StringUtil.isEmpty(path)) {
				initRootCert();
			} else {
				LogUtil.info(log, "rootCertPath not set in KasiteConfig" );
				return null;
			}
		}
		return rootCert;
	}

	/**
	 * 验证书链。
	 * @param cert
	 * @return
	 */
	private boolean verifyCertificateChain(X509Certificate cert){
		
		if ( null == cert) {
			LogUtil.info(log,"cert must Not null");
			return false;
		}
		
		X509Certificate middleCert = getMiddleCert();
		if (null == middleCert) {
			LogUtil.info(log,"middleCert must Not null");
			return false;
		}
		
		X509Certificate rootCert = getRootCert();
		if (null == rootCert) {
			LogUtil.info(log,"rootCert or cert must Not null");
			return false;
		}
		
		try {
		
	        X509CertSelector selector = new X509CertSelector();
	        selector.setCertificate(cert);
	        
	        Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
	        trustAnchors.add(new TrustAnchor(rootCert, null));
	        PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(
			        trustAnchors, selector);
	
	        Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
	        intermediateCerts.add(rootCert);
	        intermediateCerts.add(middleCert);
	        intermediateCerts.add(cert);
	        
	        pkixParams.setRevocationEnabled(false);
	
	        CertStore intermediateCertStore = CertStore.getInstance("Collection",
	                new CollectionCertStoreParameters(intermediateCerts), "BC");
	        pkixParams.addCertStore(intermediateCertStore);
	
	        CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");
	        
        	@SuppressWarnings("unused")
			PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder
                .build(pkixParams);
			LogUtil.info(log,"verify certificate chain succeed.");
			return true;
        } catch (java.security.cert.CertPathBuilderException e){
        	e.printStackTrace();
			LogUtil.error(log,"verify certificate chain fail.",e);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"verify certificate chain exception: ",e);
		}
		return false;
	}
	
	/**
	 * 检查证书链
	 * 
	 * @param rootCerts
	 *            根证书
	 * @param cert
	 *            待验证的证书
	 * @return
	 */
	public  boolean verifyCertificate(X509Certificate cert) {
		
		if ( null == cert) {
			LogUtil.info(log,"cert must Not null");
			return false;
		}
		try {
			cert.checkValidity();//验证有效期
//			cert.verify(middleCert.getPublicKey());
			if(!verifyCertificateChain(cert)){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"verifyCertificate fail",e);
			return false;
		}
		boolean ifValidateCNName = new Boolean(KasiteConfig.getUnionPay(UnionPayEnum.ifValidateCNName, configKey)).booleanValue();
		if(ifValidateCNName){
			// 验证公钥是否属于银联
			if(!UNIONPAY_CNNAME.equals(getIdentitiesFromCertficate(cert))) {
				LogUtil.info(log,"cer owner is not CUP:" + getIdentitiesFromCertficate(cert));
				return false;
			}
		} else {
			// 验证公钥是否属于银联
			if(!UNIONPAY_CNNAME.equals(getIdentitiesFromCertficate(cert)) 
					&& !"00040000:SIGN".equals(getIdentitiesFromCertficate(cert))) {
				LogUtil.info(log,"cer owner is not CUP:" + getIdentitiesFromCertficate(cert));
				return false;
			}
		}
		return true;		
	}

	/**
	 * 将签名私钥证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 * @throws IOException 
	 */
	private KeyStore getKeyInfo(String pfxkeyfile, String keypwd,
			String type) throws IOException {
		LogUtil.info(log,"加载签名证书==>" + pfxkeyfile);
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance(type, "BC");
			LogUtil.info(log,"Load RSA CertPath=[" + pfxkeyfile + "],Pwd=["+ keypwd + "],type=["+type+"]");
			fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null: keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"getKeyInfo Error",e);
			return null;
		} finally {
			if(null!=fis)
				fis.close();
		}
	}
	
	/**
	
	/**
	 * 通过keystore获取私钥证书的certId值
	 * @param keyStore
	 * @return
	 */
	private String getCertIdIdByStore(KeyStore keyStore) {
		Enumeration<String> aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore
					.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (KeyStoreException e) {
			e.printStackTrace();
			LogUtil.error(log,"getCertIdIdByStore Error",e);
			return null;
		}
	}
	
	/**
	 * 使用模和指数生成RSA公钥 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	private  PublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"构造RSA公钥失败：",e);
			return null;
		}
	}
	
	/**
	 * 将字符串转换为X509Certificate对象.
	 * 
	 * @param x509CertString
	 * @return
	 */
	public  X509Certificate genCertificateByStr(String x509CertString) {
		X509Certificate x509Cert = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC"); 
			InputStream tIn = new ByteArrayInputStream(
					x509CertString.getBytes("ISO-8859-1"));
			x509Cert = (X509Certificate) cf.generateCertificate(tIn);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,"gen certificate error",e);
		}
		return x509Cert;
	}
	
	
	/**
	 * 获取证书的CN
	 * @param aCert
	 * @return
	 */
	private  String getIdentitiesFromCertficate(X509Certificate aCert) {
		String tDN = aCert.getSubjectDN().toString(); 
		String tPart = "";
		if ((tDN != null)) {
			String tSplitStr[] = tDN.substring(tDN.indexOf("CN=")).split("@");
			if (tSplitStr != null && tSplitStr.length > 2
					&& tSplitStr[2] != null)
				tPart = tSplitStr[2];
		}
		return tPart;
	}
	
}
