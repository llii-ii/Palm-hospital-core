package com.kasite.core.serviceinterface.module.basic.dbo;


import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author lcy
 * 验证码实体类
 * @version 1.0 
 * 2017-7-2下午2:59:51
 */
@Table(name="B_PROVINGCODE")
public class ProvingCode extends BaseDbo{
	@Id
	@KeySql(useGeneratedKeys = true)
	private String pCId;
	private String mobile;
	private String provingCode;
	private Timestamp validTime;
	
	
	public String getpCId() {
		return pCId;
	}
	public void setpCId(String pCId) {
		this.pCId = pCId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvingCode() {
		return provingCode;
	}
	public void setProvingCode(String provingCode) {
		this.provingCode = provingCode;
	}
	public Timestamp getValidTime() {
		return validTime;
	}
	public void setValidTime(Timestamp validTime) {
		this.validTime = validTime;
	}
	
}
