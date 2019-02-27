package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

/**
 * B_MEMBER、BAT_USER关联表对象
 * @className: UserMember
 * @author: lcz
 * @date: 2018年8月9日 下午8:01:00
 */
@Table(name="B_USER_MEMBER")
public class UserMember extends BaseDbo{
	
	private String openId;
	private String memberId;
	private Integer isDefaultMember;
	
	
	/**
	 * @return the isDefaultMember
	 */
	public Integer getIsDefaultMember() {
		return isDefaultMember;
	}
	/**
	 * @param isDefaultMember the isDefaultMember to set
	 */
	public void setIsDefaultMember(Integer isDefaultMember) {
		this.isDefaultMember = isDefaultMember;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	
	
	
}
