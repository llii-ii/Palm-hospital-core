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
 * 住院号检验入参类
 * @author lcy
 * @version 1.0 
 * 2017-8-3上午9:25:50
 */
public class ReqCheckHospitalNo extends AbsReq {
	/**
	 * 用户姓名
	 */
	private String memberName;
	/**
	 * 电话
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idCardNo;
	/**
	 * 住院号
	 */
	private String hospitalNo;
	
	/**
	 * 用户唯一ID
	 */
	private String memberId;
	
	public ReqCheckHospitalNo(InterfaceMessage msg)
			throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.memberId=XMLUtil.getString(ser, "MemberId", true);
		this.memberName=XMLUtil.getString(ser, "MemberName", true);
		this.mobile = XMLUtil.getString(ser, "Mobile", true);
		this.idCardNo = XMLUtil.getString(ser, "IdCardNo", true);
		this.hospitalNo=XMLUtil.getString(ser, "HospitalNo", true);
	}
	
	
	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
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
	public String getHospitalNo() {
		return hospitalNo;
	}
	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

}
