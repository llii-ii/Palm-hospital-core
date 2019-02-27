package com.kasite.client.business.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.pay.bean.dbo.HisBillEntity;
import com.kasite.client.pay.dao.IHisBillMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * 下载BILL账单JOB
 * 
 * @author zhaoy
 *
 */
@Component
public class DownloadHisBillJob {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	private boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	IHisBillMapper hisBillMpapper;
	
	/**
	 * His原始账单下载
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = true;
				log.info("开始下载HIS门诊对账单");
				
				String startDate = DateUtils.getYesterdayStartString(new Date(), "yyyyMMdd");
				String endDate = DateUtils.getYesterdayEndString(new Date(), "yyyyMMdd");
				String startDateDel = DateUtils.getYesterdayStartString(new Date());
				String endDateDel = DateUtils.getYesterdayEndString(new Date());
				
				deal(startDate, endDate, startDateDel, endDateDel);
				
				flag = true;
				log.info("结束下载HIS门诊对账单");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	public void deal(String startDate, String endDate, String startDateDel, String endDateDel) throws Exception{
		Example example = new Example(HisBillEntity.class);
		example.createCriteria().andBetween("transDate", startDateDel, endDateDel);
		hisBillMpapper.deleteByExample(example);
		
		Map<String,String> paramMap = new HashMap<String,String>(16);
		paramMap.put("beginDate", startDate);
		paramMap.put("endDate", endDate);
		InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DownloadHisBillJob.class,"queryHisOrderBillList", 
				KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
		String hosid = KasiteConfig.getOrgCode();
		CommonResp<RespQueryHisBill> resp = HandlerBuilder.get().getCallHisService(hosid).queryHisOrderBillList(msg, paramMap);
		if(resp.getCode().equals(KstHosConstant.SUCCESSCODE)) {
			List<RespQueryHisBill> respList = resp.getList();
			for (RespQueryHisBill respQueryHisBill : respList) {
				HisBillEntity hisBillInfo = new HisBillEntity();
				BeanCopyUtils.copyProperties(respQueryHisBill, hisBillInfo, null);
				hisBillMpapper.insertSelective(hisBillInfo);
			}
		}
	}

}
