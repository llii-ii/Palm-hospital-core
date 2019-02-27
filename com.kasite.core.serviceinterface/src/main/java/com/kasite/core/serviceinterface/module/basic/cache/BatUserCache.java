package com.kasite.core.serviceinterface.module.basic.cache;

/**
 * 
 * @className: BatUserCache
 * @author: lcz
 * @date: 2018年8月8日 下午5:49:15
 */
public class BatUserCache {
	private String openId;
	private String nickName;
	private Integer sex;
	private String city;
	private String country;
	private String province;
	private String language;
	private String headImgUrl;
	private Integer subscribe;
	private Integer subscribeTime;
	private String unionId;
	private Integer groupId;
	private String remark;
	private String sysId;
	private String configKey;
	
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	private Long syncTime;//同步时间
	
	public Long getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Long syncTime) {
		this.syncTime = syncTime;
	}
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
}
