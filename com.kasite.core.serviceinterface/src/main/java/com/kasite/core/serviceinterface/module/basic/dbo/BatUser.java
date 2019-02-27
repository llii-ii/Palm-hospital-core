package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author lq
 * @version 1.0
 * 2017-7-24 下午2:29:38
 */
@Table(name="BAT_USER")
public class BatUser extends BaseDbo{
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private String id;
	/**第三方用户Id**/
	private String openId;
	/**渠道ID**/
	private String channelId;
	/**关注状态**/
	private Integer subscribe;
	/**关注时间**/
	private Integer subscribeTime;
	/**第三方用户unionId**/
	private String unionId;
	/**微信appId*/
	private String accountId;
	/**微信configkey*/
	private String configKey;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	

}
