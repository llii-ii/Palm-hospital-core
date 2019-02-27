package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 扫描二维码信息点记录
 */
public class ReqScanGuide extends AbsReq {

	/**
	 * 用户openId
	 */
	private String openId;
	
	/**
	 * 信息点ID
	 */
	private String guideId;

	
	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getGuideId() {
		return guideId;
	}


	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}


	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqScanGuide(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.guideId = XMLUtil.getString(dataEl, "GuideId", true);
		this.openId = XMLUtil.getString(dataEl, "OpenId", true);
	}

}
