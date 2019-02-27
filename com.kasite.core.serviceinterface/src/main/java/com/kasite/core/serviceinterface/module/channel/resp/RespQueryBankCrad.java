package com.kasite.core.serviceinterface.module.channel.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 银行卡配置信息
 * 
 * @author zhaoy
 *
 */
public class RespQueryBankCrad extends AbsResp {

	private String bankNo;
	
	private String bankName;

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
