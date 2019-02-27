package com.kasite.core.serviceinterface.module.wechat.req;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;


/**
 * 
     * @className:WxPayMsg 微信支付信息获取类
     * @author : cwf
     * @date : 2018年7月25日 下午3:20:19
 */

@Table(name="wx_pay_msg")
public class WxPayMsg {
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	
	private String appid;
	
	@Column(name="bank_type")
	private String bankType;
	
	@Column(name="cash_fee")
	private String cashFee;
	
	@Column(name="device_info")
	private String deviceInfo;
	
	@Column(name="fee_type")
	private String feeType;
	
	@Column(name="is_subscribe")
	private String isSubscribe;
	
	@Column(name="mch_id")
	private String mchId;
	
	@Column(name="nonce_str")
	private String nonceStr;
	
	@Column(name="openid")
	private String openid;
	
	@Column(name="out_trade_no")
	private String outTradeNo;
	
	@Column(name="result_code")
	private String resultCode;
	
	@Column(name="return_code")
	private String returnCode;
	
	@Column(name="sign")
	private String sign;
	
	@Column(name="sub_appid")
	private String subAppid;
	
	@Column(name="sub_is_subscribe")
	private String subIsSubscribe;
	
	@Column(name="sub_mch_id")
	private String subMchId;
	
	@Column(name="sub_openid")
	private String subOpenid;
	
	@Column(name="time_end")
	private String timeEnd;
	
	@Column(name="total_fee")
	private String totalFee;
	
	@Column(name="trade_type")
	private String tradeType;
	
	@Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="create_time")
	private Timestamp createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCashFee() {
		return cashFee;
	}
	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getIsSubscribe() {
		return isSubscribe;
	}
	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSubAppid() {
		return subAppid;
	}
	public void setSubAppid(String subAppid) {
		this.subAppid = subAppid;
	}
	public String getSubIsSubscribe() {
		return subIsSubscribe;
	}
	public void setSubIsSubscribe(String subIsSubscribe) {
		this.subIsSubscribe = subIsSubscribe;
	}
	public String getSubMchId() {
		return subMchId;
	}
	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}
	public String getSubOpenid() {
		return subOpenid;
	}
	public void setSubOpenid(String subOpenid) {
		this.subOpenid = subOpenid;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	
}
