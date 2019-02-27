/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**查询号源出参
 * @author lsq
 * version 1.0
 * 2017-6-20下午4:14:56
 * 
 */
public class HisQueryNumbers  extends AbsResp{
	/**号源唯一id*/
	private String sourceCode;
	/**就诊序号*/
	private String sqNo;
	/**分时开始时间*/
	private String startTime;
	/**分时结束时间*/
	private String endTime;
	/**个性化描述号源信息 如果有这个节点的值返回 则直接显示这个值不做拼装直接展示*/
	private String numberInfo;
	/**号源信息*/
	private String store;
	
	public String getNumberInfo() {
		return numberInfo;
	}
	/**
	 * 个性化描述号源信息 如果有这个节点的值返回 则直接显示这个值不做拼装直接展示
	 */
	public void setNumberInfo(String numberInfo) {
		this.numberInfo = numberInfo;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getSqNo() {
		return sqNo;
	}
	public void setSqNo(String sqNo) {
		this.sqNo = sqNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
