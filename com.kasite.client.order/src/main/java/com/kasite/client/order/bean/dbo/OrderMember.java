package com.kasite.client.order.bean.dbo;

import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

/**
 * 
 * @className: OrderMember
 * @author: lcz
 * @date: 2018年8月16日 下午6:18:17
 */
@Table(name="O_ORDER_MEMBER")
public class OrderMember extends BaseDbo{
	
	private String orderId;
	private String openId;
	private String memberName;
	private String mobile;
	private String idCardNo;
	private Integer sex;
	private String birthdate;
	private String address;
	private Integer isChildren;
	private String memberId;
	private String hisMemberId;
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getIsChildren() {
		return isChildren;
	}
	public void setIsChildren(Integer isChildren) {
		this.isChildren = isChildren;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	
	
	
}
