package com.kasite.core.elastic.log.bo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.annotation.JSONField;
import com.kasite.core.elastic.log.enums.MethodEnums;

/**
 * 
 * @className: CallApiEsLog
 * @author: lcz
 * @date: 2018年5月28日 下午8:23:09
 */
public class EsLog implements Serializable{
	
	
	
	/**
	 * @Fields serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String logId;
	private String orderId;
	private MethodEnums methodName;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Timestamp operTime;
	private String params;
	private String results;
	
	
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Timestamp getOperTime() {
		return operTime;
	}
	public void setOperTime(Timestamp operTime) {
		this.operTime = operTime;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public MethodEnums getMethodName() {
		return methodName;
	}
	public void setMethodName(MethodEnums methodName) {
		this.methodName = methodName;
	}
}
