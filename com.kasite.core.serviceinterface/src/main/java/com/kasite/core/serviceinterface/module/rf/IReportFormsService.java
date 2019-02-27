package com.kasite.core.serviceinterface.module.rf;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCloudCollection;
import com.kasite.core.serviceinterface.module.rf.req.ReqDataCollection;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCLine;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCbar;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDataCollectionGrid;
import com.kasite.core.serviceinterface.module.rf.req.ReqQueryBillRf;
import com.kasite.core.serviceinterface.module.rf.resp.RespQueryStdReport;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:55:00 TODO 数据报表接口类
 */
public interface IReportFormsService {

	/**
	 * 数据收集
	 * 
	 * @param msg
	 * @return
	 */
	public String DataCollection(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> dataCollection(CommonReq<ReqDataCollection> commReq) throws Exception;
	/**
	 * 云数据收集
	 * 
	 * @param msg
	 * @return
	 */
	public String DataCloudCollection(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> dataCloudCollection(CommonReq<ReqDataCloudCollection> commReq) throws Exception;
	
	public void dataCollection(final InterfaceMessage msg,final String channelId, final String channelName, String api,
			final Integer dataType, final Integer dataValue, final String remark);

	/**
	 * @param dataType
	 *            数据类型指标 101.用户关注数102 用户绑卡数103 就诊卡充值笔数104 就诊卡充值金额
	 *            105挂号笔数106挂号金额107诊间支付笔数108诊间支付金额109门诊退费笔数110门诊退费金额
	 *            111住院预缴金笔数112住院预交金金额113住院退费笔数114住院退费金额 201窗口付笔数202窗口付金额
	 *            203腕带付笔数204腕带付金额 205 在线付笔数206在线付金额207 迷你付笔数208迷你付金额209
	 *            处方付笔数210处方付金额 211卡面付笔数212卡面付金额213 护士工作站笔数214护士工作站金额215
	 *            医生工作站笔数216医生工作站金额
	 * 
	 * @param targetType
	 *            指标类型 1业务指标2产品指标
	 */
	public void dataCloudCollection(final InterfaceMessage msg,String channelId, Integer dataType, Integer dataCount,
			String targetType);
	
	
	CommonResp<RespMap> getDCSummary(CommonReq<ReqString> req) throws Exception;
	
	CommonResp<RespMap> getDataCollectionGrid(CommonReq<ReqGetDataCollectionGrid> req) throws Exception;
	CommonResp<RespMap> getDCLine(CommonReq<ReqGetDCLine> req) throws Exception;
	CommonResp<RespMap> getDCbar(CommonReq<ReqGetDCbar> req) throws Exception;
	
	/**
	 * 获取昨日交易数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> queryTransationInfo(CommonReq<ReqQueryBillRf> commReq) throws Exception;
	
	/**
	 * 获取交易渠道的图表数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespQueryStdReport> queryEchatsShow(CommonReq<ReqQueryBillRf> commReq) throws Exception;
	
	/**
	 * 获取年度统计数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> queryYearCount(CommonReq<ReqQueryBillRf> commReq) throws Exception;
}
