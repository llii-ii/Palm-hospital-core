package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 活动邀请消息推送req
 * 
 * @author 無
 *
 */
public class ReqActivityMeetingPush extends AbsReq {

	public ReqActivityMeetingPush(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.uid = XMLUtil.getString(ser, "uid", true);
		this.type = XMLUtil.getString(ser, "type", true);
		this.memberIds = XMLUtil.getString(ser, "memberIds", true);
		this.qyClientId = XMLUtil.getString(ser, "qyClientId", true);
		this.qyConfigKey = XMLUtil.getString(ser, "qyConfigKey", true);
		this.leaveStatus = XMLUtil.getString(ser, "leaveStatus", false);
	}

	/**
	 * uid
	 */
	private String uid;

	/**
	 * 0=活动 1=会议
	 */
	private String type;

	/**
	 * 参与人ids
	 */
	private String memberIds;

	/**
	 * 渠道ID
	 */
	private String qyClientId;

	/**
	 * ConfigKey
	 */
	private String qyConfigKey;

	/**
	 * 请假审批结果
	 */
	private String leaveStatus;
	

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQyClientId() {
		return qyClientId;
	}

	public void setQyClientId(String qyClientId) {
		this.qyClientId = qyClientId;
	}

	public String getQyConfigKey() {
		return qyConfigKey;
	}

	public void setQyConfigKey(String qyConfigKey) {
		this.qyConfigKey = qyConfigKey;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

}
