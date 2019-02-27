package com.kasite.core.serviceinterface.module.wechat.req;

import org.dom4j.Element;

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
public class ReqGetUserGroup extends AbsReq{
	
	private String appId;
	private String secret;
	
	
	
	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}



	public String getSecret() {
		return secret;
	}



	public void setSecret(String secret) {
		this.secret = secret;
	}



	public ReqGetUserGroup (InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.appId = XMLUtil.getString(dataEl, "APPID", true);
		this.secret = XMLUtil.getString(dataEl, "SECRET", true);
	}
}
