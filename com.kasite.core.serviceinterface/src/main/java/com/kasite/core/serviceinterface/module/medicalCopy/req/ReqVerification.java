package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqVerification extends AbsReq{

	private String token;
	private String picId;
	private String pcId;
	private String mobile;
	private String code;
	private String idCard;
	private String patientName;
	private String filePath;//图片路径
	private String type;//1身份证2户口本
	public ReqVerification(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.token=XMLUtil.getString(ser, "token", false);
		this.picId=XMLUtil.getString(ser, "picId", false);
		this.pcId=XMLUtil.getString(ser, "pcId", false);
		this.mobile=XMLUtil.getString(ser, "mobile", false);
		this.code=XMLUtil.getString(ser, "code", false);
		this.idCard=XMLUtil.getString(ser, "idCard", false);
		this.patientName=XMLUtil.getString(ser, "patientName", false);
		this.filePath=XMLUtil.getString(ser, "filePath", false);
		this.type=XMLUtil.getString(ser, "type", false);
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getPicId() {
		return picId;
	}


	public void setPicId(String picId) {
		this.picId = picId;
	}


	public String getPcId() {
		return pcId;
	}


	public void setPcId(String pcId) {
		this.pcId = pcId;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getIdCard() {
		return idCard;
	}


	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}


	public String getPatientName() {
		return patientName;
	}


	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	
	
	
}
