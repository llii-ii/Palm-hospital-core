/**
 * 
 */
package com.kasite.core.serviceinterface.module.his.resp;

import javax.validation.constraints.NotBlank;

import com.kasite.core.common.resp.AbsResp;
import com.kasite.core.common.validator.group.AddGroup;

/**锁号出参
 * @author lsq
 * version 1.0
 * 2017-7-4下午3:01:26
 */
public class HisLockOrder  extends AbsResp{
	/**锁号订单*/
	@NotBlank(message="锁号订单 hisOrderId 不能为空", groups = {AddGroup.class})
	private String hisOrderId;
	/**锁定的号数*/
	private String sqNo;
	/**费用：需要收取的费用总额 单位 ：分*/
	private Integer fee;
	/**费用描述 ： 如果有返回 则前端会显示费用 描述，如果不返回则不显示该内容*/
	private String feeInfo;
	/**
	 * 是否在线缴费  1在线缴费  2非在线缴费  如果不返回默认是取系统配置的
	 */
	private Integer isOnlinePay;
	/**返回码*/
	private String respCode;
	/**返回信息*/
	private String respMessage;
	/**调用HIS返回的结果集*/
	private String store;
	
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	
	public Integer getIsOnlinePay() {
		return isOnlinePay;
	}
	public void setIsOnlinePay(Integer isOnlinePay) {
		this.isOnlinePay = isOnlinePay;
	}
	public String getHisOrderId() {
		return hisOrderId;
	}
	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}
	public String getSqNo() {
		return sqNo;
	}
	public void setSqNo(String sqNo) {
		this.sqNo = sqNo;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getFeeInfo() {
		return feeInfo;
	}
	public void setFeeInfo(String feeInfo) {
		this.feeInfo = feeInfo;
	}
	
	
}
