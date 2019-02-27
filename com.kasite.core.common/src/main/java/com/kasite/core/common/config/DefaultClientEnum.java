package com.kasite.core.common.config;
/**
 * 默认渠道
 * @author daiyanshui
 * 系统默认支持的渠道
 */
public enum DefaultClientEnum {
	_100123("100123","微信渠道"),
	_100125("100125","支付宝渠道"),
	_100127("100127","号池渠道"),
	_100129("100129","健康之路渠道"),
	wristbandcodepay("wristbandcodepay","腕带付渠道"),
	doctorstationpay("doctorstationpay","医生工作站快捷付渠道"),
	minipay("minipay","Mini付渠道"),
	sweepcodepay("sweepcodepay","当面付渠道"),
	prescriptioncodepay("prescriptioncodepay","处方付渠道"),
	selfservicepay("selfservicepay","自助机扫码付渠道"),
	cardfacecodepay("cardfacecodepay","卡面付渠道"),
	smallpro("smallpro","微信"),
	;
	private String clientId;
	private String clientName;
	
	DefaultClientEnum(String clientId,String name){
		this.clientId = clientId;
		this.clientName = name;
	}
	public String getClientId() {
		return clientId;
	}
	public String getClientName() {
		return clientName;
	}
}
