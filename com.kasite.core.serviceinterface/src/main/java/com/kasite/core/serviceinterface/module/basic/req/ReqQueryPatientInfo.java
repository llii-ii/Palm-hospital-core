package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO   查询患者信息请求入参对象
 */
public class ReqQueryPatientInfo extends AbsReq{

	/**
	 * 患者手机号
	 */
	private String mobile;
	
	/**
	 * 患者身份证
	 */
	private String idCardNo;
	 
	/**
	 * 患者就诊卡
	 */
	private String clinicCard;
	
	/**
	 * 患者微信或支付宝唯一标识
	 */
	private String openId;
	private String memberId;
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



	public String getIdCardNo() {
		return idCardNo;
	}



	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}



	public String getClinicCard() {
		return clinicCard;
	}



	public void setClinicCard(String clinicCard) {
		this.clinicCard = clinicCard;
	}



	public String getOpenId() {
		return openId;
	}



	public void setOpenId(String openId) {
		this.openId = openId;
	}



	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryPatientInfo(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.mobile=XMLUtil.getString(dataEl, "Mobile", false);
		this.memberId=XMLUtil.getString(dataEl, "MemberId", false);
		this.idCardNo = XMLUtil.getString(dataEl, "IdCardNo", false);
		this.openId = XMLUtil.getString(dataEl, "OpenId", false);
		this.clinicCard = XMLUtil.getString(dataEl, "ClinicCard", false);
		if(StringUtil.isEmpty(mobile) && StringUtil.isEmpty(idCardNo)
				&& StringUtil.isEmpty(openId) && StringUtil.isEmpty(clinicCard)) {
			throw new ParamException("查询条件不能全部为空！");
		}
		
	}

}
