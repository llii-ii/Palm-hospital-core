package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @version 1.0
 * 2017-7-24 下午2:29:38
 */
public class ReqAddBatUser extends AbsReq {
	private String openId;
	private String nickName;
	private String accountId;
	private Integer sex;
	private String city;
	private String country;
	private String province;
	private String language;
	private String headImgUrl;
	private Integer subscribe;
	private Integer subscribeTime;
	private String unionId;
	private String remark;
	private Integer groupId;

	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public Integer getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}
	public Integer getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(Integer subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}


	public ReqAddBatUser(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.openId = XMLUtil.getString(dataEl, "OpenId", false,super.getOpenId());
		this.nickName = XMLUtil.getString(dataEl, "NickName", false);
		this.accountId = XMLUtil.getString(dataEl, "AccountId", false);
		this.sex = XMLUtil.getInt(dataEl, "Sex", false);
		this.city = XMLUtil.getString(dataEl, "City", false);
		this.country = XMLUtil.getString(dataEl, "Country", false);
		this.province = XMLUtil.getString(dataEl, "Province", false);
		this.language = XMLUtil.getString(dataEl, "Language", false);
		this.headImgUrl = XMLUtil.getString(dataEl, "HeadImgUrl", false);
		this.subscribe = XMLUtil.getInt(dataEl, "Subscribe", false);
		this.subscribeTime = XMLUtil.getInt(dataEl, "SubscribeTime", false);
		this.unionId = XMLUtil.getString(dataEl, "UnionId", false);
		this.remark = XMLUtil.getString(dataEl, "Remark", false);
		this.groupId = XMLUtil.getInt(dataEl, "GroupId", false);
	}

}
