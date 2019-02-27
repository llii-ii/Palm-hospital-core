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

import com.kasite.client.basic.cache.DictLocalCache;
import com.kasite.client.business.module.backstage.bill.dao.ChannelBankCheckDao;
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
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.pay.dbo.ChannelBankCheck;
import com.kasite.core.serviceinterface.module.pay.dto.ChannelOfConfigKeyVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 银行到账勾兑账单生成
 * 
 * @author zhaoy
 *
 */
@Component
public class GenChannelBankCheckJob {

	private static final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private static boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	ChannelBankCheckDao channelBankCheckDao;
	
	@Autowired
	IBillMapper billMapper;
	
	@Autowired
	protected DictLocalCache dictLocalCache;
	
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
		Example example = new Example(ChannelBankCheck.class);
		example.createCriteria().andEqualTo("date",date);
		channelBankCheckDao.deleteByExample(example);
		//2.开始下载账单
		List<ChannelBankCheck> addList = buildChannelBankCheck(startDate, endDate, date, checkDate);
		if(addList != null) {
			for (ChannelBankCheck channelBankCheck : addList) {
				channelBankCheckDao.insertSelective(channelBankCheck);
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
	private List<ChannelBankCheck> buildChannelBankCheck(String startDate, String endDate, String date, Timestamp checkDate) {
		List<ChannelBankCheck> thisList = new ArrayList<>();
		List<ChannelOfConfigKeyVo> payVoList = billMapper.findMoneyChannelOfConfigkey(startDate, endDate, KstHosConstant.BILL_ORDER_TYPE_1);
		List<ChannelOfConfigKeyVo> refundVoList = billMapper.findMoneyChannelOfConfigkey(startDate, endDate, KstHosConstant.BILL_ORDER_TYPE_2);
		Map<String, ChannelOfConfigKeyVo> payMap = new HashMap<>();
		Map<String, ChannelOfConfigKeyVo> refundMap = new HashMap<>();
		for (ChannelOfConfigKeyVo vo : payVoList) {
			payMap.put(vo.getChannelId() + "-" + vo.getConfigKey() + "-" + vo.getServiceId(), vo);
		}
		for (ChannelOfConfigKeyVo vo : refundVoList) {
			refundMap.put(vo.getChannelId() + "-" + vo.getConfigKey() + "-" + vo.getServiceId(), vo);
		}
		//门诊的服务ID集合
		Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
		//住院的服务ID集合
		Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
		for(String key: payMap.keySet()) {
			ChannelOfConfigKeyVo payVo = payMap.get(key);
			ChannelBankCheck channelBankCheck = new ChannelBankCheck();
			String channelId = payVo.getChannelId();
			String configKey = payVo.getConfigKey();
			String bankNo = "";
			String bankShortName = "";
			String bankName = "";
			ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
			if(payInfo == null) {
				continue;
			}
			if(ChannelTypeEnum.zfb.equals(payInfo)) {
				bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configKey);
				bankShortName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configKey);
				bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configKey);
			}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
				bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configKey);
				bankShortName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configKey);
				bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configKey);
			}
			channelBankCheck.setDate(date);
			channelBankCheck.setChannelId(channelId);
			channelBankCheck.setChannelName(payVo.getChannelName());
			Dictionary dictionary = dictLocalCache.get("serviceid", payVo.getServiceId());
			if(dictionary.getUpId().equals(pOutPatient.getId())) {
				channelBankCheck.setServiceId(pOutPatient.getKeyword());
			}else if(dictionary.getUpId().equals(pHospitalization.getId())) {
				channelBankCheck.setServiceId(pHospitalization.getKeyword());
			}
			channelBankCheck.setConfigKey(configKey);
			channelBankCheck.setPayMethod(payInfo.name());
			channelBankCheck.setPayMethodName(payInfo.getTitle());
			channelBankCheck.setBankNo(bankNo);
			channelBankCheck.setBankShortName(bankShortName);
			channelBankCheck.setBankName(bankName);
			
			Long paideMoney = payVo.getPaideMoney();
			Long payAbleMoney = payVo.getPayableMoney();
			Long diffMoney = payVo.getDiffMoney();
			
			if(refundMap.containsKey(key)) {
				ChannelOfConfigKeyVo refundVo = refundMap.get(key);
				paideMoney = paideMoney - refundVo.getPaideMoney();
				payAbleMoney = payAbleMoney - refundVo.getPayableMoney();
				diffMoney = diffMoney - refundVo.getDiffMoney();
			}
			channelBankCheck.setPaideMoney(paideMoney);
			channelBankCheck.setPayAbleMoney(payAbleMoney);
			channelBankCheck.setDiffMoney(diffMoney);
			List<Integer> listState = billMapper.findBillCheck0(startDate, endDate, payVo.getServiceId(), channelId, configKey);
			if(listState.contains(KstHosConstant.BILL_CHECK_STATE_1) || listState.contains(KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE)) {
				channelBankCheck.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_0);
			}else {
				channelBankCheck.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);
			}
			channelBankCheck.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
			channelBankCheck.setCreateDate(checkDate);
			
			thisList.add(channelBankCheck);
		}
		return thisList;
	}
}
