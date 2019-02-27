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
 *   xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package com.kasite.client.unionpay.sdk;

import java.io.File;
import java.io.FilenameFilter;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;

/**
 * @ClassName: CertUtil
 * @Description: acpsdk证书工具类，主要用于对证书的加载和使用
 * @date 2016-7-22 下午2:46:20
 *
 */
public class CertUtil {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	private static Map<String,CertInstance> certUtilMap = new HashMap<String,CertInstance>(16);
	
	static {
		addProvider();//向系统添加BC provider
	}
	/**
	 * 初始化所有证书.
	 */
	public static CertInstance init(String configKey) {
		try {
			if(!certUtilMap.containsKey(configKey)) {
				CertInstance certInstance = new CertInstance(configKey);
				certInstance.initSignCert();//初始化签名私钥证书
				certInstance.initMiddleCert();//初始化验签证书的中级证书
				certInstance.initRootCert();//初始化验签证书的根证书
				certInstance.initEncryptCert();//初始化加密公钥
				certInstance.initTrackKey();//构建磁道加密公钥
				certInstance.initValidateCertFromDir();//初始化所有的验签证书
				certUtilMap.put(configKey,certInstance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,e);
		}
		return certUtilMap.get(configKey);
	}
	/**
	 * 添加签名，验签，加密算法提供者
	 */
	private static void addProvider(){
		if (Security.getProvider("BC") == null) {
			LogUtil.info(log,"add BC provider");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} else {
			Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			LogUtil.info(log,"re-add BC provider");
		}
		printSysInfo();
	}

	/**
	 * 打jre中印算法提供者列表
	 */
	private static void printProviders(StringBuffer str) {
		str.append("Providers List:\\r\\n");
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			str.append(i + 1 + "." + providers[i].getName()+"\\r\\n");
		}
	}
	/**
	 * 证书文件过滤器
	 * 
	 */
	static class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}
		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

	
	/**
	 * 打印系统环境信息
	 */
	private static void printSysInfo() {
		StringBuffer str = new StringBuffer();
		str.append("================= SYS INFO begin====================\\r\\n");
		str.append("os_name:" + System.getProperty("os.name")+"\\r\\n");
		str.append("os_arch:" + System.getProperty("os.arch")+"\\r\\n");
		str.append("os_version:" + System.getProperty("os.version")+"\\r\\n");
		str.append("java_vm_specification_version:"
				+ System.getProperty("java.vm.specification.version")+"\\r\\n");
		str.append("java_vm_specification_vendor:"
				+ System.getProperty("java.vm.specification.vendor")+"\\r\\n");
		str.append("java_vm_specification_name:"
				+ System.getProperty("java.vm.specification.name")+"\\r\\n");
		str.append("java_vm_version:"
				+ System.getProperty("java.vm.version")+"\\r\\n");
		str.append("java_vm_name:" + System.getProperty("java.vm.name")+"\\r\\n");
		str.append("java.version:" + System.getProperty("java.version")+"\\r\\n");
		str.append("java.vm.vendor=[" + System.getProperty("java.vm.vendor") + "]\\r\\n");
		str.append("java.version=[" + System.getProperty("java.version") + "]\\r\\n");
		printProviders(str);
		str.append("================= SYS INFO end=====================");
		KasiteConfig.print(str.toString());
	}

}
