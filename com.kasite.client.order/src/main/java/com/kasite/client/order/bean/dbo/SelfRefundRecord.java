package com.kasite.client.order.bean.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf
 * TODO
 */
@Table(name="O_SELFREFUND_RECORD")
public class SelfRefundRecord {

	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	
	private String cardNo;
	
	private String cardType;
	
	private String hisMemberId;
	
	/**
	 * 卡余额
	 */
	private Integer balance;
	
	/**
	 * 可退余额
	 */
	private Integer refundableBalance;
	
	/**
	 * 可退订单笔数
	 */
	private Integer refundableCount;
	
	/**
	 * 已退款金额
	 */
	private Integer refundAmount;
	
	/**
	 * 已退款订单笔数
	 */
	private Integer refundCount;
	
	/**
	 * 状态,0初始1退费中2全成功3部分失败4全部失败
	 */
	private Integer state;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	private String operatorId;
	
	private String operatorName;
	
	private String memberId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getRefundableBalance() {
		return refundableBalance;
	}

	public void setRefundableBalance(Integer refundableBalance) {
		this.refundableBalance = refundableBalance;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
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

	public Integer getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Integer refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getRefundableCount() {
		return refundableCount;
	}

	public void setRefundableCount(Integer refundableCount) {
		this.refundableCount = refundableCount;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	
	
}
