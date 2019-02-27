package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqQueryDictList
 * @author: lcz
 * @date: 2018年8月27日 下午5:25:37
 */
public class ReqQueryDictList extends AbsReq{
	
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


	/**
	 * @Title: ReqQueryDictList
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqQueryDictList(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.id = getDataJs().getLong("Id");
			this.value = getDataJs().getString("Value");
			this.keyword = getDataJs().getString("Keyword");
			this.upId = getDataJs().getLong("UpId");
			this.dicType = getDataJs().getString("DicType");
			this.status = getDataJs().getInteger("Status");
		}
	}
	
}
