package com.kasite.client.business.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.client.rf.bean.dto.ReportDate;
import com.kasite.client.rf.bean.dto.StdReporDate;
import com.kasite.client.rf.dao.IReportDateMapper;
import com.kasite.client.rf.dao.IStdReportMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.serviceinterface.module.pay.dto.BillCountByDateVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 支付方式日统计作业
 * 
 * @author zhaoy
 *
 */
@Component
public class GenPayWayDayStatisticsJob {

	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	IBillMapper billMapper;
	
	@Autowired
	IStdReportMapper stdReportMapper;
	
	@Autowired
	IReportDateMapper reportDateMapper; 
	
	/**
	 * 支付方式日统计作业
	 * 			--每天10点20分执行
	 * 
	 * @Description:
	 */
	public void execute(){
		if (flag && config.isStartJob(this.getClass())) {
			flag = false;
			log.info("----------开始统计--------------");
			
			try {
				String startDate = DateUtils.getYesterdayStartString(new Date(), "yyyy-MM-dd");
				String endDate = DateOper.formatDate(new Date(), "yyyy-MM-dd");
				String date = DateUtils.getYesterdayString(new Date(), "yyyy-MM-dd");
				
//				String startDate = "2018-09-13";
//				String endDate = "2018-09-14";
//				String date = "2018-09-15";
				
				deal(startDate, endDate, date);
				
				flag = true;
			} catch (Exception e) {
				
			}
		}	
	}
	
	public void deal(String startDate, String endDate, String date) {
		Example example = new Example(StdReporDate.class);
		example.createCriteria().andBetween("sumDate", startDate, endDate);
		stdReportMapper.deleteByExample(example);
		
		List<StdReporDate> stdReporDateList = this.buildStdReporDate(startDate, endDate, date);
		List<StdReporDate> stdReporDateListCount = this.buildStdReporDateCount(startDate, endDate, date);
		
		if((stdReporDateList == null || stdReporDateList.size() < 1) || (stdReporDateListCount == null || stdReporDateListCount.size() < 1)) {
			return;
		}
		for (StdReporDate stdReporDate : stdReporDateList) {
			stdReportMapper.insertSelective(stdReporDate);
		}
		for (StdReporDate stdReporDate : stdReporDateListCount) {
			stdReportMapper.insertSelective(stdReporDate);
		}
		
		Example reportExample = new Example(ReportDate.class);
		reportExample.createCriteria().andIn("dataType", Arrays.asList(new Integer[] {5,6})).andEqualTo("sumDate", date);
		reportDateMapper.deleteByExample(reportExample);
		
		Map<String, Object[]> map = this.buildBillCount(date);
		if(map != null) {
			for (String channelId : map.keySet()) {
				String channelName = KasiteConfig.getChannelById(channelId);
				Object[] array = map.get(channelId);
				//交易笔数
				ReportDate countObj = new ReportDate();
				countObj.setChannelId(channelId);
				countObj.setChannelName(channelName);
				countObj.setDataType(5);
				countObj.setDataValue(array[0] + "");
				countObj.setSumDate(date);
				//交易金额
				ReportDate moneyObj = new ReportDate();
				moneyObj.setChannelId(channelId);
				moneyObj.setChannelName(channelName);
				moneyObj.setDataType(6);
				moneyObj.setDataValue(array[1] + "");
				moneyObj.setSumDate(date);
				
				reportDateMapper.insertSelective(countObj);
				reportDateMapper.insertSelective(moneyObj);
			}
		}
		
		log.info("统计结束:共生成" + stdReporDateList.size() + "条金额统计记录," + stdReporDateListCount.size() + "条交易笔数统计记录!");
	}
	
	/**
	 *@Description:获取支付方式日统计交易额的list 集合
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private List<StdReporDate> buildStdReporDate(String startDate, String endDate, String date){
		Map<String, List<String>> payWayMap = buildPayWayMap(startDate, endDate);
		List<StdReporDate> stdReporDateList = new ArrayList<>();
		for (String payWay: payWayMap.keySet()) {
			List<String> configkeyList = payWayMap.get(payWay);
			List<BillVo> list = billMapper.findPayWayCountMoney(date, configkeyList);
			if(list == null || list.size()==0) {
				continue;
			}
			for (BillVo billVo : list) {
				Integer diff = billVo.getDiffPrice();
				StdReporDate srd = new StdReporDate();
				srd.setDataParentType(1);
				Integer type = null;
				if(ChannelTypeEnum.wechat.name().equals(payWay)) {
					type=1;
				}else if(ChannelTypeEnum.zfb.name().equals(payWay)) {
					type=2;
				}else if(ChannelTypeEnum.unionpay.name().equals(payWay)) {
					type=3;
				}
				srd.setDataType(type);
				srd.setDataValue(diff+"");
				srd.setSumDate(date);
				srd.setRemark(null);
				stdReporDateList.add(srd);
			}
		}
		return stdReporDateList;
	}
	/**
	 *@Description:获取支付方式日统计交易数的list 集合
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private List<StdReporDate> buildStdReporDateCount(String startDate, String endDate, String date){
		Map<String, List<String>> payWayMap = buildPayWayMap(startDate, endDate);
		List<StdReporDate> stdReporDateList = new ArrayList<>();
		for (String payWay: payWayMap.keySet()) {
			List<String> configkeyList = payWayMap.get(payWay);
			List<BillVo> list = billMapper.findPayWayCount(date, configkeyList);
			if(list == null || list.size()==0) {
				continue;
			}
			for (BillVo billVo : list) {
				Integer count = billVo.getBillCount();
				StdReporDate srd = new StdReporDate();
				srd.setDataParentType(2);
				Integer type = null;
				if(ChannelTypeEnum.wechat.name().equals(payWay)) {
					type=1;
				}else if(ChannelTypeEnum.zfb.name().equals(payWay)) {
					type=2;
				}else if(ChannelTypeEnum.unionpay.name().equals(payWay)) {
					type=3;
				}
				srd.setDataType(type);
				srd.setDataValue(count+"");
				srd.setSumDate(date);
				srd.setRemark(null);
				stdReporDateList.add(srd);
			}
		}
		return stdReporDateList;
	}
	
	/**
	 * 渠道统计日报表
	 * 
	 * @param queryDate
	 * @return
	 */
	private Map<String, Object[]> buildBillCount(String queryDate){
		Map<String, Object[]> resultMap = new LinkedMap<>();
		List<BillCountByDateVo> payVoList = billMapper.findBillPayCountByDate(queryDate);
		List<BillCountByDateVo> refundVoList = billMapper.findBillCountRefundByDate(queryDate);
		if(payVoList != null && payVoList.size() > 0) {
			for (BillCountByDateVo payVo : payVoList) {
				Object[] array = new Object[2];
				array[0] = payVo.getCount();
				array[1] = payVo.getMoneySum();
				resultMap.put(payVo.getChannelId(), array);
			}
		}
		
		if(refundVoList != null && refundVoList.size() > 0) {
			for (BillCountByDateVo refundVo : refundVoList) {
				Object[] array = null;
				String channelId = refundVo.getChannelId();
				Integer refundCount = refundVo.getCount();
				Long refundMoneySum = refundVo.getMoneySum();
				if(resultMap.containsKey(channelId)) {
					array = resultMap.get(channelId);
					array[0] = (int)array[0] + refundCount;
					array[1] = (long)array[1] - refundMoneySum;
				}else {
					array = new Object[2];
					array[0] = refundCount;
					array[1] = 0 - refundMoneySum;
				}
				resultMap.put(channelId, array);
			}
		}
		 return resultMap;
	}
	
	/**
	 * 构建支付方式下的configkey集合
	 * 
	 * @return
	 */
	private Map<String, List<String>> buildPayWayMap(String startDate, String endDate){
		Map<String, List<String>> map = new HashMap<>();
		List<String> configkeyList = billMapper.findAllConfigkeyByDate(startDate, endDate);
		if(configkeyList != null) {
			for (String configkey: configkeyList) {
				List<String> childList = null;
				ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
				if(payInfo == null) {
					continue;
				}
				String key = payInfo.name();
				if(map != null && map.containsKey(key)) {
					childList = map.get(key);
					childList.add(configkey);
				}else {
					childList = new ArrayList<>();
					childList.add(configkey);
				}
				map.put(key, childList);
			}
		}
		return map;
	}
	
}
