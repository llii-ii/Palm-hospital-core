package com.kasite.core.serviceinterface.module.rf.req;

import java.sql.Timestamp;

import org.dom4j.Element;

import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 云报表数据
 */
public class ReqDataCloudCollection extends AbsReq {

	/**
	 * 医院ID
	 */
	private String hosId;
	
	/**
	 * 报表日期 yyyy-mm-dd
	 */
	private String date;
	
	/**
	 * 指标类型 1业务指标2产品指标
	 */
	private Integer targetType;
	/**
	 * 数据类型指标
	 * 101.用户关注数102 用户绑卡数103 就诊卡充值笔数104 就诊卡充值金额 105挂号笔数106挂号金额107诊间支付笔数108诊间支付金额109门诊退费笔数110门诊退费金额
	 * 111住院预缴金笔数112住院预交金金额113住院退费笔数114住院退费金额
	 * 201窗口付笔数202窗口付金额  203腕带付笔数204腕带付金额 205 在线付笔数206在线付金额207 迷你付笔数208迷你付金额209 处方付笔数210处方付金额
	 * 211卡面付笔数212卡面付金额213  护士工作站笔数214护士工作站金额215 医生工作站笔数216医生工作站金额
	 */
	private String dataType;
	
	/**
	 * 数据统计
	 */
	private Integer dataCount;
	
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateDate;
	
	
	/**
	 * 渠道ID
	 */
	private String channelId;

	public String getHosId() {
		return hosId;
	}


	public void setHosId(String hosId) {
		this.hosId = hosId;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public Integer getTargetType() {
		return targetType;
	}


	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}


	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public Integer getDataCount() {
		return dataCount;
	}


	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}


	public Timestamp getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


	public Timestamp getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}


	public String getChannelId() {
		return channelId;
	}


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public ReqDataCloudCollection(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = this.__DATA__;
		if(dataEl == null){
			dataEl = root.element("Service");
		}
		if(dataEl==null){
			throw new ParamException("传入参数中["+ IConstant.DATA +"]节点不能为空。");
		}
		this.channelId =  XMLUtil.getString(dataEl, "ChannelId", true);
		this.dataType = XMLUtil.getString(dataEl, "DataType", true);
		this.dataCount = XMLUtil.getInt(dataEl, "DataCount", true);
		this.targetType = XMLUtil.getInt(dataEl, "TargetType", false);
	
	}


	/**
	 * @Title: ReqDataCloudCollection
	 * @Description: 
	 * @param msg
	 * @param hosId
	 * @param date
	 * @param targetType
	 * @param dataType
	 * @param dataCount
	 * @param createDate
	 * @param updateDate
	 * @param channelId
	 * @throws AbsHosException
	 */
	public ReqDataCloudCollection(InterfaceMessage msg, String hosId, String date, Integer targetType, String dataType, Integer dataCount, Timestamp createDate, Timestamp updateDate, String channelId) throws AbsHosException {
		super(msg);
		this.hosId = hosId;
		this.date = date;
		this.targetType = targetType;
		this.dataType = dataType;
		this.dataCount = dataCount;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.channelId = channelId;
	}
	
	
}
