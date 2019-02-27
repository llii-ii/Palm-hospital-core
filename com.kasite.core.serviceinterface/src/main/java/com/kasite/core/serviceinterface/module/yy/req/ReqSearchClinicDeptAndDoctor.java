package com.kasite.core.serviceinterface.module.yy.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqSearchClinicDeptAndDoctor
 * @author: lcz
 * @date: 2018年9月18日 下午4:32:37
 */
public class ReqSearchClinicDeptAndDoctor extends AbsReq{

	private String searchLike;
	private String hosId;
	
	/**
	 * @return the hosId
	 */
	public String getHosId() {
		return hosId;
	}
	/**
	 * @param hosId the hosId to set
	 */
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	/**
	 * @return the searchLike
	 */
	public String getSearchLike() {
		return searchLike;
	}
	/**
	 * @param searchLike the searchLike to set
	 */
	public void setSearchLike(String searchLike) {
		this.searchLike = searchLike;
	}


	/**
	 * @Title: ReqSearchClinicDeptAndDoctor
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqSearchClinicDeptAndDoctor(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()!=0) {
			this.hosId = XMLUtil.getString(getData(), "HosId", true);
			this.searchLike = XMLUtil.getString(getData(), "SearchLike", false);
		}else {
			this.hosId = getDataJs().getString("HosId");
			this.searchLike = getDataJs().getString("SearchLike");
		}
	}

}
