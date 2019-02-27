package com.kasite.core.serviceinterface.module.his.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: HisMemberAutoUnbind
 * @author: lcz
 * @date: 2018年9月14日 下午2:45:12
 */
public class HisCheckEntityCard  extends AbsResp{
	/**
	 * 用户就诊卡
	 */
	private String cardNo;
	/**
	 * 卡类型 
	 */
	private String cardType;
	/**
	 * HIS内部用户唯一ID
	 */
	private String hisMemberId;
	/**
	 * 用户姓名
	 */
	private String memberName;
	/**
	 * 实体余额 单位（分）
	 */
	private Integer balance;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 性别 1男2女0未知
	 */
	private Integer sex;
	/**
	 * 年龄
	 */
	private Integer age;
	
	/**
	 * 身份证号
	 */
	private String idCardId;
	
	
 
	public String getIdCardId() {
		return idCardId;
	}
	public void setIdCardId(String idCardId) {
		this.idCardId = idCardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getHisMemberId() {
		return hisMemberId;
	}
	public void setHisMemberId(String hisMemberId) {
		this.hisMemberId = hisMemberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
}
