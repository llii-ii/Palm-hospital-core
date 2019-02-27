package com.kasite.client.business.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.business.module.backstage.bill.dao.BankCheckDao;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.pay.dbo.BankMoneyCheck;

import tk.mybatis.mapper.entity.Example;

/**
 * 银行到账勾兑账单生成
 * 
 * @author zhaoy
 *
 */
@Component
public class GenBankMoneyCheckJob {

	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	BankCheckDao bankCheckDao;
	
	@Autowired
	IBillMapper billMapper;
	
	/**
	 * 银行到账勾兑账单
	 * 			--每天12点执行
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				log.info("----------开始对账--------------");

				String startDate = DateUtils.getYesterdayStartString(new Date());
				String endDate = DateUtils.getYesterdayEndString(new Date());
				String date = DateUtils.getYesterdayString(new Date());
				
//				String startDate = "2018-09-13";
//				String endDate = "2018-09-14";
//				String date = "2018-09-15";
				
				Timestamp checkDate = DateOper.getNowDateTime();
				
				deal(startDate, endDate, date, checkDate);
				
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	public void deal(String startDate, String endDate, String date, Timestamp checkDate) {
		//1.删除旧有重复数据
		Example example = new Example(BankMoneyCheck.class);
		example.createCriteria().andEqualTo("date",date);
		bankCheckDao.deleteByExample(example);
		//2.开始下载账单
		List<BankMoneyCheck> addList = buildBankCheck(startDate, endDate, date, checkDate);
		if(addList != null) {
			for (BankMoneyCheck bankMoneyCheck : addList) {
				bankCheckDao.insertSelective(bankMoneyCheck);
			}
		}
		log.info("对账结束:共生成账单" + addList.size() + "条记录!");
	}

	/**
	 * 构建生成的账单实体类
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<BankMoneyCheck> buildBankCheck(String startDate, String endDate, String date, Timestamp checkDate) {
		Map<String, List<String>> map = buildBankMap(startDate, endDate, date);
		List<BankMoneyCheck> retList = new ArrayList<>();
		if(map != null) {
			for(String key: map.keySet()) {
				BankMoneyCheck bankMoneyCheck = new BankMoneyCheck();
				bankMoneyCheck.setDate(date);
				Long payMoneySum = 0L;
				Long refundMoneySum = 0L;
				for (String configkey: map.get(key)) {
					Long payMoney = billMapper.findCountPayMoney(startDate, endDate, configkey);
					Long refundMoney = billMapper.findCountRefundMoney(startDate, endDate, configkey);
					if(payMoney != null) {
						payMoneySum = payMoneySum + payMoney;
					}
					if(refundMoney != null) {
						refundMoneySum = refundMoneySum + refundMoney;
					}
				}
				String[] arr = key.split("_");
				bankMoneyCheck.setBankNo(arr[0]);
				bankMoneyCheck.setBankName(arr[1]);
				Long payAbleMoney = 0L;
				payAbleMoney = payMoneySum - refundMoneySum;
				bankMoneyCheck.setCheckState(9);  //默认值9,未勾兑状态
				bankMoneyCheck.setPayAbleMoney(payAbleMoney);
				bankMoneyCheck.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
				bankMoneyCheck.setCreateDate(checkDate);
//				bankMoneyCheck.setUpdateBy(KstHosConstant.SYSOPERATORID);  
//				bankMoneyCheck.setUpdateDate(nowTime);
				
				retList.add(bankMoneyCheck);
			}
		}
		return retList;
	}
	
	/**
	 * 构建银行卡号下对应的configkey集合
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private Map<String, List<String>> buildBankMap(String startDate, String endDate, String date) {
		Map<String, List<String>> map = new HashMap<>();
		List<String> configkeyList = billMapper.findAllConfigkeyByDate(startDate, endDate);
		if(configkeyList != null) {
			for (String configkey: configkeyList) {
				List<String> childList = null;
				String bankNo = null;
				String bankName = null;
				ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
				if(payInfo == null) {
					continue;
				}
				if(ChannelTypeEnum.zfb.equals(payInfo)) {
					bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configkey);
					bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configkey);
				}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
					bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configkey);
					bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configkey);
				}
				String key = bankNo + "_" + bankName;
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
