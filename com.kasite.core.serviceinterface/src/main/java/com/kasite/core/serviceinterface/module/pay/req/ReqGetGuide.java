package com.kasite.core.serviceinterface.module.pay.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 扫描二维码信息点记录
 */
public class ReqGetGuide extends AbsReq {
	
	/**
	 * 信息点ID
	 */
	private String guideId;

	/**
	 * 订单唯一ID
	 */
	private String orderId;
	private String openId;
	
	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getGuideId() {
		return guideId;
	}


	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}

	public ReqGetGuide(InterfaceMessage msg, String guideId) throws AbsHosException {
		super(msg);
		this.guideId = guideId;
	}

	public ReqGetGuide(InterfaceMessage msg, String openId, String guideId) throws AbsHosException {
		super(msg);
		this.openId = openId;
		this.guideId = guideId;
	}

	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetGuide(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(null != root) {
			Element dataEl = root.element(KstHosConstant.DATA);
			this.guideId = XMLUtil.getString(dataEl, "GuideId", false);
			this.orderId = XMLUtil.getString(dataEl, "OrderId", false);
			if(StringUtil.isBlank(guideId) && StringUtil.isBlank(orderId)) {
				throw new RRException("GuideId，OrderId 参数不能同时为空");
			}
		}
	}

}
