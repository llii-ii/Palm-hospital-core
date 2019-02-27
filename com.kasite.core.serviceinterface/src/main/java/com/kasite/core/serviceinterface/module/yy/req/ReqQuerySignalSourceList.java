package com.kasite.core.serviceinterface.module.yy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqQuerySignalSourceList extends AbsReq{

	private String cardNo;
	private Integer cardType;
	private String labCode;
	private String departCode;
	private String examDept;
	private String bookDate;
	private String memberId;
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getLabCode() {
		return labCode;
	}

	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}

	public String getDepartCode() {
		return departCode;
	}

	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}

	public String getExamDept() {
		return examDept;
	}

	public void setExamDept(String examDept) {
		this.examDept = examDept;
	}

	public String getBookDate() {
		return bookDate;
	}

	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
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
	public ReqQuerySignalSourceList(InterfaceMessage reqXml) throws AbsHosException {
		super(reqXml);
		Element ser = root.element(KstHosConstant.DATA);
		if(ser==null){
			throw new ParamException("传入参数中[Service]节点不能为空。");
		}
		this.cardNo = XMLUtil.getString(ser, "CardNo", true);
		this.cardType = XMLUtil.getInt(ser, "CardType", true);
		this.labCode = XMLUtil.getString(ser, "LabCode", true);
		this.departCode = XMLUtil.getString(ser, "DepartCode", true);
		this.examDept = XMLUtil.getString(ser, "ExamDept", true);
		this.bookDate = XMLUtil.getString(ser, "BookDate", true);
		this.memberId = XMLUtil.getString(ser, "MemberId", true);
	}


	
	
	
	
}
