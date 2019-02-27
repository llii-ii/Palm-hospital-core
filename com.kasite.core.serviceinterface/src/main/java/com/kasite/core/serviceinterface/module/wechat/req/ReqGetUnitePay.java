package com.kasite.core.serviceinterface.module.wechat.req;

import org.dom4j.Element;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author cyh
 * @version 1.0
 * 2017-11-17 下午2:29:38
 */
public class ReqGetUnitePay extends AbsReq{
	
	private String orderId;
	private String priceName;
	private String openid;
	private String remoteAddr;
	private String clientId;




	public String getOrderId() {
		return orderId;
	}




	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}




	public String getPriceName() {
		return priceName;
	}




	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}




	public String getOpenid() {
		return openid;
	}




	public void setOpenid(String openid) {
		this.openid = openid;
	}




	public String getRemoteAddr() {
		return remoteAddr;
	}




	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public ReqGetUnitePay (InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.remoteAddr = "";//JSONObject.fromObject(msg.getAuthInfo()).getString("RemoteAddr");
		this.clientId = getClientId();//msg.getClientId();
		this.orderId = XMLUtil.getString(dataEl, "orderId", true);
		this.priceName = XMLUtil.getString(dataEl, "priceName", true);
		this.openid = XMLUtil.getString(dataEl, "PARAM", false);
		this.remoteAddr = XMLUtil.getString(dataEl, "PARAM", false);
	}
}
