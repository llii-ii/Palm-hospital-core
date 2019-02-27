package com.kasite.client.crawler.modules.api.vo;

/*
"head": {
"busseID": " C100",
"senderCode": "发送方编码",
"senderName": "发送方名称",
"receiverCode": "接收方编码",
"receiverName": "接收方名称", "intermediaryCode": "医保编码", "intermediaryName": "医保名称",
"hosorgNum": "操作员编号",
"hosorgName": "操作员姓名",
"systemType": "1",
"busenissType": "2",
"sendTradeNum": "20171201144523-400000981-9134",
"standardVersionCode": "version:1.0.0", 
"clientmacAddress": "00:00:00:00:00:00",
"recordCount": "1"
}
 */
public class HeadRespVo {
	private String busseID;
	private String sendTradeNum;
	private String senderCode;
	private String senderName;
	private String receiverCode;
	private String receiverName;
	private String intermediaryCode;
	private String intermediaryName;
	private String hosorgNum;
	private String hosorgName;
	private String systemType;
	private String busenissType;
	private String standardVersionCode;
	private String version;
	private String clientmacAddress;
	private String recordCount;
	public String getBusseID() {
		return busseID;
	}
	public void setBusseID(String busseID) {
		this.busseID = busseID;
	}
	public String getSendTradeNum() {
		return sendTradeNum;
	}
	public void setSendTradeNum(String sendTradeNum) {
		this.sendTradeNum = sendTradeNum;
	}
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverCode() {
		return receiverCode;
	}
	public void setReceiverCode(String receiverCode) {
		this.receiverCode = receiverCode;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getIntermediaryCode() {
		return intermediaryCode;
	}
	public void setIntermediaryCode(String intermediaryCode) {
		this.intermediaryCode = intermediaryCode;
	}
	public String getIntermediaryName() {
		return intermediaryName;
	}
	public void setIntermediaryName(String intermediaryName) {
		this.intermediaryName = intermediaryName;
	}
	public String getHosorgNum() {
		return hosorgNum;
	}
	public void setHosorgNum(String hosorgNum) {
		this.hosorgNum = hosorgNum;
	}
	public String getHosorgName() {
		return hosorgName;
	}
	public void setHosorgName(String hosorgName) {
		this.hosorgName = hosorgName;
	}
	public String getSystemType() {
		return systemType;
	}
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	public String getBusenissType() {
		return busenissType;
	}
	public void setBusenissType(String busenissType) {
		this.busenissType = busenissType;
	}
	public String getStandardVersionCode() {
		return standardVersionCode;
	}
	public void setStandardVersionCode(String standardVersionCode) {
		this.standardVersionCode = standardVersionCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getClientmacAddress() {
		return clientmacAddress;
	}
	public void setClientmacAddress(String clientmacAddress) {
		this.clientmacAddress = clientmacAddress;
	}
	public String getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(String recordCount) {
		this.recordCount = recordCount;
	}
	
}
