package com.kasite.core.serviceinterface.module.pay.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 银行勾兑信息更新请求实体类
 * 
 * @author zhaoy
 *
 */
public class ReqUpdateBankCheckInfo extends AbsReq {

	public ReqUpdateBankCheckInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if (msg.getParamType() == 0) {
			this.date = getDataJs().getString("Date");
			this.bankNo = getDataJs().getString("BankNo");
			this.paideMoney = getDataJs().getString("PaideMoney");
			this.bankFlowNo = getDataJs().getString("BankFlowNo");
			this.updateBy = super.getOpenId();
		}
	}
	
	private String date;
	
	private String bankNo;
	
	private String paideMoney;
	
	/**
	 * 银行到款流水号
	 */
	private String bankFlowNo;
	
	/**
	 * 更新用户
	 */
	private String updateBy;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getPaideMoney() {
		return paideMoney;
	}

	public void setPaideMoney(String paideMoney) {
		this.paideMoney = paideMoney;
	}

	public String getBankFlowNo() {
		return bankFlowNo;
	}

	public void setBankFlowNo(String bankFlowNo) {
		this.bankFlowNo = bankFlowNo;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
