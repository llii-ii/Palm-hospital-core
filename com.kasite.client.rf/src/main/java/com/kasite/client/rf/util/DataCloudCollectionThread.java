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
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCloudCollection;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: DataCloudCollectionThread
 * @author: lcz
 * @date: 2018年7月6日 下午5:19:22
 */
public class DataCloudCollectionThread implements Runnable{
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_REPORTFROMS);
	
	private String channelId;
	private Integer dataType;
	private Integer dataCount;
	private String targetType;
	private IReportFormsService reportFormService;
	private InterfaceMessage msg;
	
	public DataCloudCollectionThread(InterfaceMessage msg,IReportFormsService reportFormService,String channelId,Integer dataType,Integer dataCount,String targetType){
		this.reportFormService = reportFormService;
		this.channelId = channelId;
		this.dataType = dataType;
		this.dataCount = dataCount;
		this.targetType = targetType;
		this.msg = msg;
	}
	
	@Override
	public void run() {
		// 调用消息推送接口
		try {
			ReqDataCloudCollection reqDataCloud = new ReqDataCloudCollection(msg, null, null, Integer.parseInt(targetType), Integer.toString(dataType), dataCount, null, null, channelId);
			CommonResp<RespMap>  commonResp = reportFormService.dataCloudCollection(new CommonReq<ReqDataCloudCollection>(reqDataCloud));
			if(commonResp==null || !KstHosConstant.SUCCESSCODE.equals(commonResp.getCode())) {
				LogUtil.info(log, commonResp!=null?commonResp.toResult():"返回值为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		
	}

}
