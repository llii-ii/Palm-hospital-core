package com.kasite.client.rf.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.coreframework.util.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kasite.client.rf.util.DataCloudCollectionThread;
import com.kasite.client.rf.util.DataCollectionThread;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.ReqString;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.serviceinterface.module.channel.dto.ChannelVo;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.kasite.core.serviceinterface.module.rf.dto.MapVo;
import com.kasite.core.serviceinterface.module.rf.dto.StdReportVo;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCLine;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDCbar;
import com.kasite.core.serviceinterface.module.rf.req.ReqGetDataCollectionGrid;
import com.kasite.core.serviceinterface.module.rf.req.ReqQueryBillRf;
import com.kasite.core.serviceinterface.module.rf.resp.RespQueryStdReport;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:57:14 TODO 数据报表实现类
 */
@Service("rf.rfApi")
public class IReportFormsServiceImpl extends AbstractRfCommonService implements IReportFormsService {
	
	@Override
	public void dataCollection(InterfaceMessage msg,String channelId, String channelName, String api, Integer dataType, Integer dataValue,
			String remark) {
		KstHosConstant.cachedThreadPool.execute(new DataCollectionThread(msg,this, channelId, 
				channelName, api, dataType, dataValue, remark));
	}

	@Override
	public void dataCloudCollection(InterfaceMessage msg,String channelId, Integer dataType, Integer dataCount, String targetType) {
		KstHosConstant.cachedThreadPool.execute(new DataCloudCollectionThread(msg,this, channelId, dataType, dataCount, targetType));		
	}

	@Override
	public CommonResp<RespMap> getDCSummary(CommonReq<ReqString> req) throws Exception {
		ReqString strReq = req.getParam();
		String sumDate = null;
		if(StringUtil.isNotBlank(strReq.getParam()) && "Y".equals(strReq.getParam())) {
			sumDate = DateOper.addDate(DateOper.getNow("yyyy-MM-dd"), -1);
		}
		List<Map<String, Object>> list = reportDateMapper.getDCSummary(sumDate);
		List<RespMap> respList = new ArrayList<RespMap>();
		for (Map<String, Object> map : list) {
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.getDCSummary.DataType, map.get("DATATYPE"));
			respMap.put(ApiKey.getDCSummary.DataValue, map.get("DATAVALUE"));
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	@Override
	public CommonResp<RespMap> getDataCollectionGrid(CommonReq<ReqGetDataCollectionGrid> req) throws Exception {
		ReqGetDataCollectionGrid reqObj = req.getParam();
		
		if(reqObj.getPage()==null || reqObj.getPage().getPIndex()==null || reqObj.getPage().getPSize()==null || reqObj.getPage().getPSize()<=0 || reqObj.getPage().getPSize()>100) {
			return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"分页参数不能为空，且每页最多只能查询100条数据");
		}
		Page<Object> page = PageHelper.startPage(reqObj.getPage().getPIndex(), reqObj.getPage().getPSize());
		List<Map<String, Object>> list = reportDateMapper.getDataCollectionGrid(reqObj.getStartDate(),reqObj.getEndDate());
		reqObj.getPage().setPCount(Integer.parseInt(Long.toString(page.getTotal())));
		List<RespMap> respList = new ArrayList<RespMap>();
		for (Map<String, Object> map : list) {
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.getDataCollectionGrid.OperTime, map.get("OPERTIME"));
			respMap.put(ApiKey.getDataCollectionGrid.Type1, map.get("TYPE1"));
			respMap.put(ApiKey.getDataCollectionGrid.Type2, map.get("TYPE2"));
			respMap.put(ApiKey.getDataCollectionGrid.Type3, map.get("TYPE3"));
			respMap.put(ApiKey.getDataCollectionGrid.Type4, map.get("TYPE4"));
			respMap.put(ApiKey.getDataCollectionGrid.Type5, map.get("TYPE5"));
			respMap.put(ApiKey.getDataCollectionGrid.Type6, map.get("TYPE6"));
			respList.add(respMap);
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,reqObj.getPage());
	}
	@Override
	public CommonResp<RespMap> getDCLine(CommonReq<ReqGetDCLine> req) throws Exception {
		ReqGetDCLine reqObj = req.getParam();
		Map<String, Object> result = new LinkedHashMap<>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", reqObj.getStartDate());
		paramMap.put("endDate", reqObj.getEndDate());
		paramMap.put("dataType", reqObj.getDataType());
		//查询所有的数据
		List<Map<String, Object>> allList = reportDateMapper.getDCLineAll(paramMap);
		for (Map<String, Object> map : allList) {
			RespMap respMap = new RespMap();
			respMap.put(ApiKey.getDCLine.OperTime, map.get("OPERTIME"));
			JSONArray channelData = new JSONArray();
			respMap.put(ApiKey.getDCLine.ChannelData, channelData);
			JSONObject dataJs = new JSONObject();
			dataJs.put("ChannelName", "所有");
			dataJs.put("DataValue", map.get("DATAVALUE"));
			channelData.add(dataJs);
			result.put(map.get("OPERTIME").toString(), respMap);
		}
		//查询有数据的渠道
		List<Map<String, Object>> ll = reportDateMapper.getDCLineChannelList(paramMap);
		
		if(ll!=null && ll.size()>0) {
			//拼接查询内容
			Map<String,String> channelMap = new HashMap<String, String>();
			StringBuffer select = new StringBuffer();
			for (Map<String, Object> map : ll) {
				String channelId = map.get("CHANNELID").toString();
				select.append("IF(DATATYPE = 6,SUM(IF(CHANNELID='").append(channelId).append("', DATAVALUE,0))/100,SUM(IF(CHANNELID='").append(channelId).append("', DATAVALUE,0))) AS '").append(channelId).append("',");
				ChannelVo vo = getChannelById(channelId);
				if(vo!=null) {
					channelMap.put(channelId, vo.getChannelName());
				}
			}
			//其他渠道数据
			paramMap.put("selectColumn", select.substring(0, select.length()-1));
			List<Map<String, Object>> otherList = reportDateMapper.getDCLineForChannel(paramMap);
			for (Map<String, Object> map : otherList) {
				String operTime = map.get("OPERTIME").toString();
				if(result.containsKey(operTime)) {
					RespMap respMap = (RespMap)result.get(operTime);
					if(respMap.getMap().containsKey(ApiKey.getDCLine.ChannelData)) {
						for(Entry<String, String> entry : channelMap.entrySet()) {
							String channelId = entry.getKey();
							String channelName = entry.getValue();
							JSONObject dataJs = new JSONObject();
							dataJs.put("ChannelName", channelName);
							dataJs.put("DataValue", map.get(channelId));
							((JSONArray)respMap.getMap().get(ApiKey.getDCLine.ChannelData)).add(dataJs);
						}
					}
				}else {
					RespMap respMap = new RespMap();
					respMap.put(ApiKey.getDCLine.OperTime, map.get("OPERTIME"));
					JSONArray channelData = new JSONArray();
					respMap.put(ApiKey.getDCLine.ChannelData, channelData);
					for(Entry<String, String> entry : channelMap.entrySet()) {
						String channelId = entry.getKey();
						String channelName = entry.getValue();
						JSONObject dataJs = new JSONObject();
						dataJs.put("ChannelName", channelName);
						dataJs.put("DataValue", map.get(channelId));
						channelData.add(dataJs);
					}
					result.put(operTime, respMap);
				}
			}
		}
		
		List<RespMap> respList = new ArrayList<>();
		for(Entry<String, Object> entry : result.entrySet()) {
			respList.add((RespMap)entry.getValue());
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	@Override
	public CommonResp<RespMap> getDCbar(CommonReq<ReqGetDCbar> req) throws Exception {
		ReqGetDCbar reqObj = req.getParam();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startDate", reqObj.getStartDate());
		paramMap.put("endDate", reqObj.getEndDate());
		paramMap.put("dataType", reqObj.getDataType());
		List<Map<String, Object>> list = reportDateMapper.getDCbar(paramMap);
		List<RespMap> respList = new ArrayList<RespMap>();
		Map<String, ChannelVo> channelMap = getAllChannel();
		List<ChannelVo> channelList = new ArrayList<>();
		for (String key : channelMap.keySet()) {
			channelList.add(channelMap.get(key));
		}
		for (ChannelVo channel : channelList) {
			String channelName = channel.getChannelName();
			String channelId = channel.getChannelId();
			for (Map<String, Object> map : list) {
				if(channelId.equals(map.get("CHANNELID"))) {
					RespMap respMap = new RespMap();
					respMap.put(ApiKey.getDCbar.ChannelName, channelName);
					respMap.put(ApiKey.getDCbar.ChannelId, map.get("CHANNELID"));
					respMap.put(ApiKey.getDCbar.DataValue, map.get("DATAVALUE"));
					respList.add(respMap);
				}
			}
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public CommonResp<RespMap> queryTransationInfo(CommonReq<ReqQueryBillRf> commReq) throws Exception {
		String queryDate1 = DateUtils.format(new Date(), "yyyyMMdd");
		Double yesterdayMoney = stdReportMapper.findTotalYesterday(0, queryDate1);
		Double yesterdayCount = stdReportMapper.findTotalYesterday(1, queryDate1);
		String queryDate2 = DateUtils.format(new Date(), "yyyy-MM-dd");
		Double yesterdayWXMoney = stdReportMapper.findMoneyYesterday(1, queryDate2);
		Double yesterdayZFBMoney = stdReportMapper.findMoneyYesterday(2, queryDate2);
		Map<String, ChannelVo> channelMap = getAllChannel();
		List<ChannelVo> channelList = new ArrayList<>();
		for (String key : channelMap.keySet()) {
			channelList.add(channelMap.get(key));
		}
		RespMap respMap = new RespMap();
		respMap.add(ApiKey.BillRFPro.YesterdayMoney, yesterdayMoney);
		respMap.add(ApiKey.BillRFPro.YesterdayCount, yesterdayCount);
		respMap.add(ApiKey.BillRFPro.YesterdayWXMoney, yesterdayWXMoney);
		respMap.add(ApiKey.BillRFPro.YesterdayZFBMoney, yesterdayZFBMoney);
		respMap.add(ApiKey.BillRFPro.ChannelList, channelList);
		
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespQueryStdReport> queryEchatsShow(CommonReq<ReqQueryBillRf> commReq) throws Exception {
		ReqQueryBillRf req = commReq.getParam();
		Integer time = req.getTime(); //0-代表月 1-日
		String type = req.getType(); //各种渠道场景
		
		List<StdReportVo> retList = null;
		String queryDate = DateUtils.format(new Date(), "yyyyMMdd");
//		String queryDate = "20181011";
		//月趋势
		if(time.equals(0)) {
			retList = stdReportMapper.findMoneyYesterdayForMonth(type, queryDate);
//			retList = stdReportMapper.findTrendForMonth(type, queryDate);
		}else if(time.equals(1)){ //日趋势
			retList = stdReportMapper.findMoneyYesterdayForDate(type, queryDate);
//			retList = stdReportMapper.findTrendForDate(type, queryDate);
		}
		List<RespQueryStdReport> respList = new ArrayList<>();
		if(retList != null) {
			for (StdReportVo vo : retList) {
				RespQueryStdReport resp = new RespQueryStdReport();
				String rq = vo.getQueryDate();
				if(time.equals(0)) {
					String a = rq.substring(0,4);
					String b = rq.substring(4);
					rq = a+"年"+b+"月";
				}else {
					String a = rq.substring(4);
					String b = a.substring(0,2);
					String c = a.substring(2);
					rq = b+"月"+c+"日";
				}
				Double dou = (Double)vo.getTotal();
				dou/=1000000;  //万元单位
				BigDecimal bg = new BigDecimal(dou).setScale(2, RoundingMode.UP);
			    dou = bg.doubleValue();
			    
			    resp.setrQ(rq);
			    resp.setTotal(dou);
				respList.add(resp);
			}
		}
		
		return new CommonResp<RespQueryStdReport>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> queryYearCount(CommonReq<ReqQueryBillRf> commReq) throws Exception {
		RespMap respMap = new RespMap();
		
		List<MapVo> channelList = stdReportMapper.findChannelCountYear();
		List<MapVo> payList = stdReportMapper.findPayCountYear();
		
		if(channelList != null) {
			channelList.forEach((MapVo map) -> {
				Map<String, String> m = new HashMap<>();
				m.put("color", "#f39800");
				map.setItemStyle(m);
			});
		}
		if(payList != null) {
			payList.forEach((MapVo map) -> {
				Map<String, String> m = new HashMap<>();
				m.put("color", "#f39800");
				map.setItemStyle(m);
			});
		}
		
		respMap.add(ApiKey.BillRFPro.ChannelList, channelList);
		respMap.add(ApiKey.BillRFPro.PayList, payList);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	/**
	 * 获取全部渠道
	 * 
	 * @return
	 */
	private Map<String, ChannelVo> getAllChannel(){
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		Map<String, ChannelVo> map = new TreeMap<>();
		for (String[] channel : channelSet) {
			ChannelVo vo = new ChannelVo();
			String key = channel[0];
			String value = channel[1];
			String isOpen = channel[2];
			if("true".equals(isOpen)) {
				vo.setChannelId(key);
				vo.setChannelName(value);
				map.put(key, vo);
			}
		}
		return map;
	}
	/**
	 * 获取全部渠道
	 * 
	 * @return
	 */
	private ChannelVo getChannelById(String channelId){
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		for (String[] channel : channelSet) {
			String key = channel[0];
			String value = channel[1];
			if(channelId.equals(key)) {
				ChannelVo vo = new ChannelVo();
				vo.setChannelId(key);
				vo.setChannelName(value);
				return vo;
			}
		}
		return null;
	}
}
