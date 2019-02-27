package com.kasite.core.serviceinterface.module.pay.dbo;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author linjf
 * 商户异步通知实体类
 */
@Table(name="P_MERCHANT_FAILURE")
public class MerchantNotifyFailure {

	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	
	/**
	 * 异步通知ID
	 */
	private Long  merchantNotifyId;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMerchantNotifyId() {
		return merchantNotifyId;
	}

	public void setMerchantNotifyId(Long merchantNotifyId) {
		this.merchantNotifyId = merchantNotifyId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
