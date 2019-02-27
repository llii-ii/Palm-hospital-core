package com.kasite.core.common.req;

import com.alibaba.fastjson.JSON;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.yihu.wsgw.api.InterfaceMessage;

public class CommonReq<T extends AbsReq> {
	// 当前页数量
	private Integer PSize = 0;
	// 当前页码
	private Integer PIndex = 0;
	// 总记录数
	private Long PCount = 0L;
//	private List<T> list;
	public CommonReq(T t) {
		this.param = t;
	}
	public InterfaceMessage getMsg() throws RRException {
		if(null != param) {
			return param.getMsg();
		}
		throw new RRException(RetCode.Common.ERROR_NOTMESSAGE);
	}
	
	public Integer getPSize() {
		return PSize;
	}
	public void setPSize(Integer pSize) {
		PSize = pSize;
	}
	public Integer getPIndex() {
		return PIndex;
	}
	public void setPIndex(Integer pIndex) {
		PIndex = pIndex;
	}
	public Long getPCount() {
		return PCount;
	}
	public void setPCount(Long pCount) {
		PCount = pCount;
	}
//	public List<T> getList() {
//		return list;
//	}
//	public void setList(List<T> list) {
//		this.list = list;
//	}
	private T param;
	
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		if(null != param) {
			sbf.append(param.getClass().getName()).append("\r\n");
			sbf.append(JSON.toJSON(param));
		}
		if(null != PIndex) {
			sbf.append("\r\nPIndex=").append(PIndex);
		}
		if(null != PSize) {
			sbf.append("\r\nPSize=").append(PSize);
		}
		if(null != PCount) {
			sbf.append("\r\nPCount=").append(PCount);
		}
		return sbf.toString();
	}
	
	
}
