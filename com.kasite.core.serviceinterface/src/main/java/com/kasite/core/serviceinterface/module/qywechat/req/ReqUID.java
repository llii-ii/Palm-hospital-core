package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ通用UID入参
 * 
 * @author 無
 *
 */
public class ReqUID extends AbsReq {
	public ReqUID(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getString(dataEl, "id", true);
		this.tag = XMLUtil.getString(dataEl, "tag", false, null);
		// 企微渠道id、CONFIGKEY
		this.qyClientId = XMLUtil.getString(dataEl, "qyClientId", false);
		this.qyConfigKey = XMLUtil.getString(dataEl, "qyConfigKey", false);
	}

	/**
	 * 主键或者UID
	 */
	private String id;

	private String tag;

	private String qyClientId;
	private String qyConfigKey;

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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
