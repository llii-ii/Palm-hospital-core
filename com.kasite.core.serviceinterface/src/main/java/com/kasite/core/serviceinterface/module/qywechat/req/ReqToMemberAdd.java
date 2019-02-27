package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ添加参与人员
 * 
 * @author 無
 *
 */
public class ReqToMemberAdd extends AbsReq {
	public ReqToMemberAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.uid = XMLUtil.getString(dataEl, "uid", false);
		this.memberId = XMLUtil.getString(dataEl, "memberIds", true);
		this.memberName = XMLUtil.getString(dataEl, "memberNames", true);
		this.memberType = XMLUtil.getInt(dataEl, "memberType", false, null);
	}

	/**
	 * 成员对应的订单ID、会议ID等
	 */
	private String uid;

	/**
	 * 人员ID、或者部门ID
	 */
	private String memberId;

	/**
	 * 人员名称、或者部门名称
	 */
	private String memberName;

	/**
	 * 类型 0=参与人 1=会议记录人 2=参加（报名） 3=请假 4=维修人 5=可见人员
	 */
	private Integer memberType;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

}