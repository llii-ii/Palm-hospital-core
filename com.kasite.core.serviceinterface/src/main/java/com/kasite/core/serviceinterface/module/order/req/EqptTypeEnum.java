package com.kasite.core.serviceinterface.module.order.req;
/**
 * 设备类型
 * @author daiyanshui
 */
public enum EqptTypeEnum {
//	1.微信公众号/服务窗2.MINI机3.MobileApp 4.PCWEB 5.扫码枪6.PCAPP
	wechat_zfb(1,"微信公众号_服务窗"),
	mini(2,"MINI机"),
	mobileApp(3,"手机APP"),
	PCWEB(4,"PCWeb"),
	San(5,"扫码枪"),
	;
	private Integer code;
	private String typeName;
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	EqptTypeEnum(int code,String typeName) {
		this.code = code;
		this.typeName = typeName;
	}
}
