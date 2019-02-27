package com.kasite.client.crawler.config.data.vo;

import com.kasite.core.httpclient.http.StringUtils;

public class PingAnPkVo {
	/**
	 * 字段编号
	 */
	private String no;
	/**
	 * 字段描述
	 */
	private String name;
	/**
	 * 字段名称
	 */
	private String privateName;
	/**
	 * 字段属性
	 */
	private String privateType;
	/**
	 * 是否为空
	 */
	private String isNull;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 内部映射字段  用于查询的时候调用各个接口的时候使用
	 */
	private String inQueryPrivate;
	/**
	 * 请求主键  索引
	 */
	private String key;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrivateName() {
		return privateName;
	}
	public void setPrivateName(String privateName) {
		this.privateName = privateName;
	}
	public String getPrivateType() {
		return privateType;
	}
	public void setPrivateType(String privateType) {
		this.privateType = privateType;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInQueryPrivate() {
		return inQueryPrivate;
	}
	public void setInQueryPrivate(String inQueryPrivate) {
		this.inQueryPrivate = inQueryPrivate;
	}
	/**
	 * 判断当前字段是否一定不能为空
	 * @return
	 */
	public boolean IsNotNull() {
		if(StringUtils.isNotBlank(isNull) && "非空".equals(isNull.trim())) {
			return true;
		}
		return false;
	}
	
	
}
