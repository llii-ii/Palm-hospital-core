package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ增加附件
 * 
 * @author 無
 *
 */
public class ReqAttachmentAdd extends AbsReq {
	public ReqAttachmentAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.uid = XMLUtil.getString(dataEl, "uid", true);
		this.attachmentUrl = XMLUtil.getString(dataEl, "attachmentUrl", true);
		this.attachmentName = XMLUtil.getString(dataEl, "attachmentName", true);
	}

	/**
	 * 附件对应的订单ID、会议ID等 32位UID
	 */
	private String uid;
	/**
	 * 图片、附件地址
	 */
	private String attachmentUrl;
	/**
	 * 图片、附件名称
	 */
	private String attachmentName;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

}
