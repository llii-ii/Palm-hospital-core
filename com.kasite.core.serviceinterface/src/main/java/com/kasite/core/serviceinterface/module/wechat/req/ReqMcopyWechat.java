package com.kasite.core.serviceinterface.module.wechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class ReqMcopyWechat extends AbsReq{

	public String wxKey;
	public String type;//(config签名jsapi_ticket：jsapi，微信卡券api_ticket：wx_card)
	public String mediaId;//临时素材id
	public String mediaName;//下载素材命名
	public String url;//获取签名时需要的url
	//public String openId;
	
	public ReqMcopyWechat(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.wxKey = XMLUtil.getString(dataEl, "wxKey", false);
		this.type = XMLUtil.getString(dataEl, "type", false);
		this.mediaId = XMLUtil.getString(dataEl, "mediaId", false);
		this.mediaName = XMLUtil.getString(dataEl, "mediaName", false);
		this.url = XMLUtil.getString(dataEl, "url", false);
		//this.openId = XMLUtil.getString(dataEl, "openId", false);
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMediaId() {
		return mediaId;
	}


	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}


	public String getWxKey() {
		return wxKey;
	}


	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}


	public String getMediaName() {
		return mediaName;
	}


	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


//	public String getOpenId() {
//		return openId;
//	}
//
//
//	public void setOpenId(String openId) {
//		this.openId = openId;
//	}


	
	
	
}
