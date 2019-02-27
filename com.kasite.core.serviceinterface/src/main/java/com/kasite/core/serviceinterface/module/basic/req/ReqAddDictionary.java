package com.kasite.core.serviceinterface.module.basic.req;

import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqAddDictionary
 * @author: lcz
 * @date: 2018年8月28日 上午10:16:02
 */
public class ReqAddDictionary extends AbsReq{

	/**字典值**/
	private String value;
	/**类型（KEY)**/
	private String keyword;
	/**上级ID根节点默认-1**/
	private Long upId;
	/**字典类型**/
	private String dicType;
	
	
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


	/**
	 * @Title: ReqAddDictionary
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddDictionary(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.upId = getDataJs().getLong("UpId");
			this.dicType = getDataJs().getString("DicType");
			this.keyword = getDataJs().getString("Keyword");
			this.value = getDataJs().getString("Value");
		}
	}

}
