package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ查询参与人员
 * 
 * @author 無
 *
 */
public class ReqToMemberQuery extends AbsReq {
	public ReqToMemberQuery(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.uid = XMLUtil.getString(dataEl, "uid", false);
		this.memberType = XMLUtil.getInt(dataEl, "memberType", false);
		this.membername = XMLUtil.getString(dataEl, "membername", false);
		this.memberId = XMLUtil.getString(dataEl, "memberId", false);
		this.isDept = XMLUtil.getInt(dataEl, "isDept", false);
	}

	/**
	 * 成员对应的订单ID、会议ID等
	 */
	private String uid;

	/**
	 * 类型 0=参与人 1=会议记录人 2=参加（报名） 3=请假 4=维修人 5=可见人员
	 */
	private Integer memberType;

	/**
	 * 人员、部门名
	 */
	private String membername;
	
	/**
	 * 人员、部门id
	 */
	private String memberId;

	/**
	 * 是否部门 默认0=否 1=是
	 */
	private int isDept;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public int getIsDept() {
		return isDept;
	}

	public void setIsDept(int isDept) {
		this.isDept = isDept;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}