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
 * 设置默认就诊人入参类
 * @author lcy
 * @version 1.0 
 * 2017-7-4上午9:11:45
 */
public class ReqSetDefaultMember extends AbsReq{
	/**
	 * 唯一ID
	 */
	private String memberId;
	/**
	 * 用户在渠道的唯一id
	 */
	private String opId;
	/**
	 * 默认卡0为非  1为是
	 */
	private Integer isDefault;
	/**
	 * 默认就诊人 0为非  1为是
	 */
	private Integer isDefaultMember;
	
	public ReqSetDefaultMember(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.memberId=XMLUtil.getString(ser, "MemberId", true);
		this.opId=XMLUtil.getString(ser, "OpId", false,super.getOpenId());
		this.isDefault=XMLUtil.getInt(ser, "IsDefault", false);
		this.isDefaultMember=XMLUtil.getInt(ser, "IsDefaultMember", true);
	}

	/**
	 * @return the isDefaultMember
	 */
	public Integer getIsDefaultMember() {
		return isDefaultMember;
	}

	/**
	 * @param isDefaultMember the isDefaultMember to set
	 */
	public void setIsDefaultMember(Integer isDefaultMember) {
		this.isDefaultMember = isDefaultMember;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
}
