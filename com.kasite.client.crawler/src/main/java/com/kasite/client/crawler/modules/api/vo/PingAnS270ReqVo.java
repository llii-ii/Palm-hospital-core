package com.kasite.client.crawler.modules.api.vo;

/**
 * 
 * @className: PingAnS270
 * @author: lcz
 * @date: 2018年6月12日 下午5:21:13
 */
public class PingAnS270ReqVo {
	/**结算流水号**/
	private String settleSerialNum;
	/**退费日期**/
	private String revokeDate;
	/**经办人**/
	private String updateBy;
	/**是否保存处方标志**/
	private String isRetainedFlg;
	
	
	public String getSettleSerialNum() {
		return settleSerialNum;
	}
	public void setSettleSerialNum(String settleSerialNum) {
		this.settleSerialNum = settleSerialNum;
	}
	public String getRevokeDate() {
		return revokeDate;
	}
	public void setRevokeDate(String revokeDate) {
		this.revokeDate = revokeDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getIsRetainedFlg() {
		return isRetainedFlg;
	}
	public void setIsRetainedFlg(String isRetainedFlg) {
		this.isRetainedFlg = isRetainedFlg;
	}
	
	
	
}
