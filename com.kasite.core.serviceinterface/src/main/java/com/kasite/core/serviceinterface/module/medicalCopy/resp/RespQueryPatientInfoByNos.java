package com.kasite.core.serviceinterface.module.medicalCopy.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryPatientInfoByNos
 * @author: cjy
 * @date: 2018年9月13日 上午10:20:19
 */
public class RespQueryPatientInfoByNos extends AbsResp {
	
	private String mcopyUserId;
	
	private String cardNo; //证件号码
	
	/**
	 * 证件类型
	 * 2：就诊卡；
	 * 5：身份证号；
	 * 6：病案号
	 */
	private String cardType; 
	private String orgCode; //机构代码

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getMcopyUserId() {
		return mcopyUserId;
	}

	public void setMcopyUserId(String mcopyUserId) {
		this.mcopyUserId = mcopyUserId;
	}


	
}
