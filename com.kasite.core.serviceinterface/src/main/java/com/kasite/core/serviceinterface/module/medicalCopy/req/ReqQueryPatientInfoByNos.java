package com.kasite.core.serviceinterface.module.medicalCopy.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryPatientInfoByNos
 * @author: cjy
 * @date: 2018年9月13日 上午10:00:37
 */
public class ReqQueryPatientInfoByNos extends AbsReq{

	private String name;  //姓名
	private String cardNo; //证件号码
	
	/**
	 * 证件类型
	 * 2：就诊卡；
	 * 5：身份证号；
	 * 6：病案号
	 */
	private String cardType; 
	private String orgCode; //机构代码，固定值AH01
	private String inPara; //预留字段
	

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @Title: ReqCaseList
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryPatientInfoByNos(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.cardNo=XMLUtil.getString(ser, "cardNo", true);
		this.cardType = XMLUtil.getString(ser, "cardType", true);
		this.orgCode = XMLUtil.getString(ser, "orgCode", false);
		this.name = XMLUtil.getString(ser, "name", true);
	}
	
}
