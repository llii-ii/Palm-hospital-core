package com.kasite.client.rf.service;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.rf.bean.dto.ReportDate;
import com.kasite.client.rf.bean.dto.RfCloudData;
import com.kasite.client.rf.dao.ICloudDataMapper;
import com.kasite.client.rf.dao.IReportDateMapper;
import com.kasite.client.rf.dao.IStdReportMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCloudCollection;
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCollection;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 数据报表抽象类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:22:16
 */
public abstract class AbstractRfCommonService implements IReportFormsService {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_REPROT);

	@Autowired
	IReportDateMapper reportDateMapper;
	@Autowired
	ICloudDataMapper cloudDataMapper;
	@Autowired
	IStdReportMapper stdReportMapper;

	@Override
	public String DataCollection(InterfaceMessage msg) throws Exception {
		return this.dataCollection(new CommonReq<ReqDataCollection>(new ReqDataCollection(msg))).toResult();
	}

	@Override
	public String DataCloudCollection(InterfaceMessage msg) throws Exception {
		return this.dataCloudCollection(new CommonReq<ReqDataCloudCollection>(new ReqDataCloudCollection(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> dataCollection(CommonReq<ReqDataCollection> commReq) throws Exception {
		ReqDataCollection reqDataCollection = commReq.getParam();
		try {
			ReportDate reportDate = new ReportDate();
			reportDate.setChannelId(reqDataCollection.getChannelId());
			if(StringUtil.isBlank(reqDataCollection.getChannelName())) {
				reportDate.setChannelName(KasiteConfig.getChannelById(reqDataCollection.getChannelId()));
			}else {
				reportDate.setChannelName(reqDataCollection.getChannelName());
			}
			reportDate.setDataType(reqDataCollection.getDataType());
			reportDate.setDataValue(reqDataCollection.getDataValue());
			reportDate.setSumDate(DateOper.formatDate(new Date(), "yyyy-MM-dd"));
			reportDateMapper.insertOrUpdateReportDate(reportDate);
			// 统计数据不影响业务逻辑出错了直接return出去
			return new CommonResp<RespMap>(commReq, KstHosConstant.DATACOLLECTIONCODE, RetCode.Success.RET_10000);
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,e,reqDataCollection.getAuthInfo());
			return new CommonResp<RespMap>(commReq, KstHosConstant.DATACOLLECTIONCODE, RetCode.ReportForms.ERROR_CALLDataCollection,e.getLocalizedMessage());
		}
	}

	@Override
	public CommonResp<RespMap> dataCloudCollection(CommonReq<ReqDataCloudCollection> commReq) throws Exception {
		ReqDataCloudCollection reqCloudData = commReq.getParam();
		try {
			int num = cloudDataMapper.updateCloundData(reqCloudData.getDataCount(), reqCloudData.getUpdateDate(), reqCloudData.getChannelId(), reqCloudData.getDataType(), DateOper.getNow("yyyy-MM-dd"));
			if (num <= 0) {
				// 先修改如果更新数量为0则新增
				RfCloudData cloudData = new RfCloudData();
				cloudData.setDate(DateOper.formatDate(DateOper.getNowDateTime(), "yyyy-MM-dd"));
				cloudData.setCreateDate(DateOper.getNowDateTime());
				cloudData.setUpdateDate(DateOper.getNowDateTime());
				cloudData.setHosId(commReq.getParam().getHosId());
				cloudData.setTargetType(reqCloudData.getTargetType());
				cloudData.setChannelId(reqCloudData.getChannelId());
				cloudData.setDataCount(reqCloudData.getDataCount());
				cloudData.setDataType(reqCloudData.getDataType());
				cloudDataMapper.insertSelective(cloudData);
			}
			return new CommonResp<RespMap>(commReq, KstHosConstant.DATACOLLECTIONCODE, RetCode.Success.RET_10000);
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log,e,reqCloudData.getAuthInfo());
			return new CommonResp<RespMap>(commReq, KstHosConstant.DATACOLLECTIONCODE, RetCode.ReportForms.ERROR_CALLDataCollection,e.getLocalizedMessage());
		}
		
		
	}

	
	
}
