package com.kasite.core.serviceinterface.module.rf.req;


import org.dom4j.Element;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf	2017年11月14日 17:36:04 
 * TODO 数据收集请求对象
 */
public class ReqDataCollection extends AbsReq{
	private String channelId;
	private String channelName;
	private String api;
	private Integer dataType;
	private String dataValue;
	private String remark;
	private String transActionCode;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}

	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransActionCode() {
		return transActionCode;
	}
	public void setTransActionCode(String transActionCode) {
		this.transActionCode = transActionCode;
	}
	public ReqDataCollection(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl == null){
			dataEl = root.element("Service");
		}
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.channelId =  XMLUtil.getString(dataEl, "ChannelId", true);
		this.channelName = XMLUtil.getString(dataEl, "ChannelName", false);
		this.api = XMLUtil.getString(dataEl, "Api", false);
		this.dataType = XMLUtil.getInt(dataEl, "DataType", true);
		this.dataValue = XMLUtil.getString(dataEl, "DataValue", true);
		this.remark = XMLUtil.getString(dataEl, "Remark", false);
	}
	/**
	 * @Title: ReqDataCollection
	 * @Description: 
	 * @param msg
	 * @param channelId
	 * @param channelName
	 * @param api
	 * @param dataType
	 * @param dataValue
	 * @param remark
	 * @param transActionCode
	 * @throws AbsHosException
	 */
	public ReqDataCollection(InterfaceMessage msg, String channelId, String channelName, String api, Integer dataType, String dataValue, String remark, String transActionCode) throws AbsHosException {
		super(msg);
		this.channelId = channelId;
		this.channelName = channelName;
		this.api = api;
		this.dataType = dataType;
		this.dataValue = dataValue;
		this.remark = remark;
		this.transActionCode = transActionCode;
	}
	
	
}
