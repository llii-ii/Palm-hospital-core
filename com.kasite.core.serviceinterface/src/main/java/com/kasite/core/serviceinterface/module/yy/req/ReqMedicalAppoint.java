package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqMedicalAppoint extends AbsReq{

	private String cardNo;
	private Integer cardType;
	private String labName;
	private String channelType;
	private String hisKey;
	private String sourceCode;
	
	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getHisKey() {
		return hisKey;
	}

	public void setHisKey(String hisKey) {
		this.hisKey = hisKey;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
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
	private String memberId;
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public ReqMedicalAppoint(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(ser, "CardNo", true);
		this.cardType = XMLUtil.getInt(ser, "CardType", true);
		this.labName = XMLUtil.getString(ser, "LabName", true);
		this.hisKey = XMLUtil.getString(ser, "HisKey", true);
		this.memberId = XMLUtil.getString(ser, "MemberId", true);
		this.sourceCode = XMLUtil.getString(ser, "SourceCode", false);
	}


	
	
	
	
}
