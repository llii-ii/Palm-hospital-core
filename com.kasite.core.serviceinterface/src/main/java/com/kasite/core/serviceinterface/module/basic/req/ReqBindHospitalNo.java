/**
 * 
 */
package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/** 
 * 绑定住院号入参类
 * @author lcy
 * @version 1.0 
 * 2017-7-4下午2:21:55
 */
public class ReqBindHospitalNo extends AbsReq {
	private String pCId;
	private String memberId;
	private String mobile;
	private String provingCode;
	private String hospitalNo;
	private String openId;
	private String hisMemberId;
	
	/**
	 * 是否需要校验卡是否有效 默认是 true 如果设置成false 则不校验
	 * */
	private Boolean isValidateCard = true;
	
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	public Boolean getIsValidateCard() {
		return isValidateCard;
	}
	public void setIsValidateCard(Boolean isValidateCard) {
		this.isValidateCard = isValidateCard;
	}
	public ReqBindHospitalNo(InterfaceMessage msg)throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.pCId = XMLUtil.getString(ser, "PCId", false);
		this.memberId=XMLUtil.getString(ser, "MemberId", true);
		this.provingCode=XMLUtil.getString(ser, "ProvingCode", false);
		this.mobile=XMLUtil.getString(ser, "Mobile", true);
		this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", true);
		this.openId=XMLUtil.getString(ser, "OpenId", false,super.getOpenId());
	}

	
	/**
	 * @Title: ReqBindHospitalNo
	 * @Description: 
	 * @param msg
	 * @param pCId
	 * @param memberId
	 * @param mobile
	 * @param provingCode
	 * @param hospitalNo
	 * @throws AbsHosException
	 */
	public ReqBindHospitalNo(InterfaceMessage msg, String pCId, String memberId, String mobile, String provingCode, String hospitalNo,String openId) throws AbsHosException {
		super(msg);
		this.pCId = pCId;
		this.memberId = memberId;
		this.mobile = mobile;
		this.provingCode = provingCode;
		this.hospitalNo = hospitalNo;
		this.openId = openId;
	}


 
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}


	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getpCId() {
		return pCId;
	}

	public void setpCId(String pCId) {
		this.pCId = pCId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvingCode() {
		return provingCode;
	}

	public void setProvingCode(String provingCode) {
		this.provingCode = provingCode;
	}

	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}
	
}
