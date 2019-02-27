package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 参与人req
 * 
 * @author 無
 *
 */
public class ReqToMember extends AbsReq {

	public ReqToMember(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(ser, "id", false);
		this.uid = XMLUtil.getString(ser, "uid", false);
		this.memberid = XMLUtil.getString(ser, "memberId", false);
		this.membertype = XMLUtil.getInt(ser, "memberType", false);
	}

	private Long id;

	/**
	 * 成员对应的订单ID、会议ID等
	 */
	private String uid;

	/**
	 * 人员ID、或者部门ID
	 */
	private String memberid;

	/**
	 * 人员名称、或者部门名称
	 */
	private String membername;

	/**
	 * 是否部门 默认0=否 1=是
	 */
	private int isDept;

	/**
	 * 类型 0=参与人 1=会议记录人 2=参加（报名） 3=请假 4=维修人 5=可见人员
	 */
	private Integer membertype;

	/**
	 * 创建时间
	 */
	private String inserttime;

	public int getIsDept() {
		return isDept;
	}

	public void setIsDept(int isDept) {
		this.isDept = isDept;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public Integer getMembertype() {
		return membertype;
	}

	public void setMembertype(Integer membertype) {
		this.membertype = membertype;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

}
