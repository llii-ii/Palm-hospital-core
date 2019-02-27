package com.kasite.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="sms") 
public class AliYunSmsConfig {
    private String accessId;
    private String accessKey;
    private String signName;
    private String codeTemplate;
    private String product;     //短信API产品名称（短信产品名固定，无需修改）
    private String domain;      //dysmsapi.aliyuncs.com
    
    public AliYunSmsConfig() {
    	//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    	KasiteConfig.setValue("sun.net.client.defaultReadTimeout", "10000");
    	KasiteConfig.setValue("sun.net.client.defaultConnectTimeout", "10000");
    }
    
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		KasiteConfig.setValue("sms.accessId", accessId);
		this.accessId = accessId;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		KasiteConfig.setValue("sms.accessKey", accessId);
		this.accessKey = accessKey;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		KasiteConfig.setValue("sms.signName", signName);
		this.signName = signName;
	}
	public String getCodeTemplate() {
		return codeTemplate;
	}
	public void setCodeTemplate(String codeTemplate) {
		KasiteConfig.setValue("sms.codeTemplate", codeTemplate);
		this.codeTemplate = codeTemplate;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		KasiteConfig.setValue("sms.product", product);
		this.product = product;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		KasiteConfig.setValue("sms.domain", domain);
		this.domain = domain;
	}
    
    
}
