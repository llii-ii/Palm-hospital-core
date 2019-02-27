package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * Req获取企业微信config信息
 * 
 * @author 無
 *
 */
public class ReqQyWxConfigInfoGet extends AbsReq {

	public ReqQyWxConfigInfoGet(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.wxkey = XMLUtil.getString(ser, "wxkey", false);
		this.url = XMLUtil.getString(ser, "url", true);
	}

	private String url;
	private String wxkey;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWxkey() {
		return wxkey;
	}

	public void setWxkey(String wxkey) {
		this.wxkey = wxkey;
	}
}