package com.kasite.core.serviceinterface.module.rf.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqGetDataCollectionGrid
 * @author: lcz
 * @date: 2018年10月8日 下午11:17:22
 */
public class ReqGetDataCollectionGrid extends AbsReq{
	/**
	 * @Title: ReqGetDataCollectionGrid
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetDataCollectionGrid(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.startDate = getDataJs().getString("StartDate");
			this.endDate = getDataJs().getString("EndDate");
		}else {
			this.startDate =  XMLUtil.getString(getData(), "StartDate", true);
			this.endDate =  XMLUtil.getString(getData(), "EndDate", true);
		}
	}
	private String startDate;
	private String endDate;
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
