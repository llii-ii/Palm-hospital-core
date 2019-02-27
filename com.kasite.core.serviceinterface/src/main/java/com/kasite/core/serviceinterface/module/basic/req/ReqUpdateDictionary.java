package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqUpdateDictionary
 * @author: lcz
 * @date: 2018年8月28日 上午10:08:33
 */
public class ReqUpdateDictionary extends AbsReq{

	private Long id;
	
	private Long upId;
	
	private String dicType;
	
	private String keyword;
	
	private String value;
	
	private Integer status;
	
	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @Title: ReqUpdateDictionary
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqUpdateDictionary(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.id = getDataJs().getLong("Id");
			this.upId = getDataJs().getLong("UpId");
			this.keyword = getDataJs().getString("Keyword");
			this.value = getDataJs().getString("Value");
			this.dicType = getDataJs().getString("DicType");
			this.status = getDataJs().getInteger("Status");
		}
	}

}
