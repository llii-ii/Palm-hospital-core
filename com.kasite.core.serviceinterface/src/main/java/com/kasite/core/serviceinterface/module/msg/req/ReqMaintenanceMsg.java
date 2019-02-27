package com.kasite.core.serviceinterface.module.msg.req;


import org.dom4j.Element;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author caiyouhong 2017年12月11日 17:31:15 
 * TODO 运维消息推送请求
 */
public class ReqMaintenanceMsg extends AbsReq{

	/**
	 * 消息归属的app
	 */
	private String appId;
	/**
	 * 推送title
	 */
	private String title;
	/**
	 * 推送title显示的颜色默认#FF0000
	 */
	private String color;
	/**
	 * 告警级别默认一般
	 */
	private String level;
	/**
	 * 告警具体消息 默认无
	 */
	private String remark;
	/**
	 * 消息模块的ip
	 */
	private String ip;
	/**
	 * 告警消息对应的URL地址
	 */
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ReqMaintenanceMsg(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.appId =  XMLUtil.getString(dataEL, "AppId", false,KasiteConfig.getAppId());
		this.title =  XMLUtil.getString(dataEL, "Title", true);
		this.color = XMLUtil.getString(dataEL, "Color", false, "#FF0000");
		
		this.level = XMLUtil.getString(dataEL, "Level", false,"一般");
		this.remark = XMLUtil.getString(dataEL, "Remark", false,"无");
		this.ip = XMLUtil.getString(dataEL, "Ip", false,"");
	}

	public ReqMaintenanceMsg(InterfaceMessage msg, String appId, String title, String color, String level,
			String remark, String ip) throws AbsHosException {
		super(msg);
		this.appId = appId;
		this.title = title;
		this.color = color;
		this.level = level;
		this.remark = remark;
		this.ip = ip;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	
}
