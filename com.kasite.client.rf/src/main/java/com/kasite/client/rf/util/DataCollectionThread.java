package com.kasite.client.rf.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCollection;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: DataCollectionThread
 * @author: lcz
 * @date: 2018年7月6日 下午5:24:51
 */
public class DataCollectionThread implements Runnable{
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_REPORTFROMS);
	
	private String channelId;
	private String channelName;
	private Integer dataType;
	private Integer dataValue;
	private String remark;
	private IReportFormsService reportFormService;
	private InterfaceMessage msg;
	
	public DataCollectionThread(InterfaceMessage msg,IReportFormsService reportFormService,String channelId,String channelName,
			String api,Integer dataType,Integer dataValue,String remark) {
		this.channelId = channelId;
		this.channelName = channelName;
		this.dataType = dataType;
		this.dataValue = dataValue;
		this.remark = remark;
		this.reportFormService = reportFormService;
		this.msg = msg;
	}
	@Override
	public void run() {
		try {
			ReqDataCollection reqData = new ReqDataCollection(msg, channelId, channelName, null, dataType, Integer.toString(dataValue), remark, KstHosConstant.DATACOLLECTIONCODE);
			CommonResp<RespMap> commonResp = reportFormService.dataCollection(new CommonReq<ReqDataCollection>(reqData));
			if(commonResp==null || !KstHosConstant.SUCCESSCODE.equals(commonResp.getCode())) {
				LogUtil.info(log, commonResp!=null?commonResp.toResult():"返回值为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		
	}
	
	
}
