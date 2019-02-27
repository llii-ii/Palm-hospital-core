package com.kasite.core.serviceinterface.module.rf.req;

import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqGetDCLine
 * @author: lcz
 * @date: 2018年10月9日 上午9:11:46
 */
public class ReqGetDCbar extends AbsReq{
	
	/**
	 * @Title: ReqGetDCLine
	 * @Description: 
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqGetDCbar(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.startDate = getDataJs().getString("StartDate");
			this.endDate = getDataJs().getString("EndDate");
			this.dataType = getDataJs().getString("DataType");
		}else {
			this.startDate =  XMLUtil.getString(getData(), "StartDate", true);
			this.endDate =  XMLUtil.getString(getData(), "EndDate", true);
			this.dataType = XMLUtil.getString(getData(), "DataType", true);
		}
	}
	private String startDate;
	private String endDate;
	private String dataType;
	
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
