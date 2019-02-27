package com.kasite.core.serviceinterface.module.order.resp;

import java.sql.Timestamp;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author linjf
 * TODO
 */
public class RespGetOrder extends AbsResp{
	/**
 	 * 订单号
 	 */
 	private String orderId;
	/**
 	 * 用户姓名
 	 */
 	private String memberName;
 	/**
 	 * 联系电话
 	 */
 	private String mobile;
 	/**
 	 * 性别
 	 */
 	private Integer sex;
 	/**
 	 * 身份证
 	 */
 	private String idcardno;
 	/**
 	 * 出生日期
 	 */
 	private String birthDay;
 	/**
 	 * 家庭住址
 	 */
 	private String address;
 	/**
 	 * 是否儿童
 	 */
 	private Integer isChildren;
 	/**服务ID*/
 	private String serviceId;
 	/**收费名称*/
 	private String priceName;
 	/**订单全部金额（分）*/
 	private Integer totalPrice;
 	/**订单支付金额（分）*/
 	private Integer price;
 	/**订单描述*/
 	private String orderMemo;
 	/**卡号（证件号）*/
 	private String cardNo;
 	/**卡类型（证件类型）*/
 	private String cardType;
 	/**是否支持在线支付*/
 	private Integer isOnLinePay;
 	/**操作人ID*/
 	private String operatorId; 
 	/**操作人名称*/
 	private String operatorName;
 	/**订单新增日期*/
 	private Timestamp beginDate; 
 	/**订单结束日（有效日期）*/
 	private Timestamp endDate;
 	/**医院ID*/
 	private String hosId;
 	/**渠道ID*/
 	private String channelId;
 	/**渠道名称*/
 	private String channelName;
 	/**处方号，或HIS订单ID*/
 	private String prescNo;
 	/**设备类型*/
 	private Integer eqptType;
 	/**
 	 * 配置key
 	 */
 	private String configkey;
 	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public String getPriceName() {
		return priceName;
	}
	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}
	public String getOrderMemo() {
		return orderMemo;
	}
	public void setOrderMemo(String orderMemo) {
		this.orderMemo = orderMemo;
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
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getIdcardno() {
		return idcardno;
	}
	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
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
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getIsOnLinePay() {
		return isOnLinePay;
	}
	public void setIsOnLinePay(Integer isOnLinePay) {
		this.isOnLinePay = isOnLinePay;
	}
	public Timestamp getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public Integer getEqptType() {
		return eqptType;
	}
	public void setEqptType(Integer eqptType) {
		this.eqptType = eqptType;
	}
	public String getConfigkey() {
		return configkey;
	}
	public void setConfigkey(String configkey) {
		this.configkey = configkey;
	}
	
}
