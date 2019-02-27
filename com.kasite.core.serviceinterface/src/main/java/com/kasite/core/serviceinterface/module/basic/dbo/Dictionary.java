package com.kasite.core.serviceinterface.module.basic.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 
 * @className: Dictionary
 * @author: lcz
 * @date: 2018年8月6日 下午2:53:04
 */
@Table(name="DICTIONARY")
public class Dictionary extends BaseDbo{
	/**自增主键**/
	@Id
	@KeySql(useGeneratedKeys=true)
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
