package com.kasite.client.crawler.modules.api.vo;
/*
"errorCode": "",
"errorMsg": "",
"receiverTradeNum": "",
"correlationId": "",
"asyncAsk": "0",
"callback":" http://127.0.0.1:8080/xxxx/xxxx.do", 
"curDllAddr":""
 */
public class AdditionInfoVo {

	private String errorCode;
	private String errorMsg;
	private String receiverTradeNum;
	private String correlationId;
	private String asyncAsk;
	private String callback;
	private String curDllAddr;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getReceiverTradeNum() {
		return receiverTradeNum;
	}
	public void setReceiverTradeNum(String receiverTradeNum) {
		this.receiverTradeNum = receiverTradeNum;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public String getAsyncAsk() {
		return asyncAsk;
	}
	public void setAsyncAsk(String asyncAsk) {
		this.asyncAsk = asyncAsk;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getCurDllAddr() {
		return curDllAddr;
	}
	public void setCurDllAddr(String curDllAddr) {
		this.curDllAddr = curDllAddr;
	}
	
	
	
}
