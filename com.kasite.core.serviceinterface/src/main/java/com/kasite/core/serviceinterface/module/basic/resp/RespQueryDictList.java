package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryDictList
 * @author: lcz
 * @date: 2018年8月27日 下午5:53:19
 */
public class RespQueryDictList extends AbsResp {
	
	private Long id;
	/**字典值**/
	private String value;
	/**类型（KEY)**/
	private String keyword;
	/**上级ID根节点默认-1**/
	private Long upId;
	/**字典类型**/
	private String dicType;
	/**状态 0无效1有效**/
	private Integer status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getUpId() {
		return upId;
	}
	public void setUpId(Long upId) {
		this.upId = upId;
	}
	public String getDicType() {
		return dicType;
	}
	public void setDicType(String dicType) {
		this.dicType = dicType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
