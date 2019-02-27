package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 创建处方付二维码请求头
 * @author linjf
 * TODO
 */
public class ReqCreatePrescriptionQrCode extends AbsReq {
	
	private String memberName;
	
	private String doctorName;
	
	private String idCardNo;
	
	private String hisOrderId;
	
	private String prescNo;
	
	private String cardNo;
	
	private Integer cardType;
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqCreatePrescriptionQrCode(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.memberName = XMLUtil.getString(dataEl, "MemberName", true);
		this.doctorName = XMLUtil.getString(dataEl, "DoctorName", true);
		this.idCardNo = XMLUtil.getString(dataEl, "IdCardNo", true);
		this.hisOrderId = XMLUtil.getString(dataEl, "HisOrderId", false);
		this.prescNo = XMLUtil.getString(dataEl, "PrescNo", false);
		this.cardNo = XMLUtil.getString(dataEl, "CardNo", true);
		this.cardType = XMLUtil.getInt(dataEl, "CardType", true);
		if( StringUtil.isEmpty(hisOrderId) && StringUtil.isEmpty(prescNo)){
			throw new ParamException("[HisOrderId][PrescNo]不能同时为空！");
		}
	}

	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getDoctorName() {
		return doctorName;
	}


	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}


	public String getIdCardNo() {
		return idCardNo;
	}


	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}


	public String getHisOrderId() {
		return hisOrderId;
	}


	public void setHisOrderId(String hisOrderId) {
		this.hisOrderId = hisOrderId;
	}


	public String getPrescNo() {
		return prescNo;
	}


	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCardType() {
		return cardType;
	}

	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	
}
