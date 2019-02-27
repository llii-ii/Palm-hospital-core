package com.kasite.client.pay.job;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;

import tk.mybatis.mapper.entity.Example;

/**
 * @author linjf
 * TODO 商户异步通知处理结果未知告警
 */
@Component
public class MerchantNotifyUnknowWarnJob {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	@Autowired
	IMerchantNotifyMapper merchantNotifyMapper;
	@Autowired
	KasiteConfigMap config;
	/**
	 *每2小时执行一次
	 */
	public void merchantNotifyUnknowWarn() {

		
		try {
			if(config.isStartJob(MerchantNotifyUnknowWarnJob.class)) {
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.MINUTE, 60);
				//查询orderType=1，并且state=0 ，并且updateTime<(当前时间-1小时)
				//查询支付通知，超过1个小时还在处理中的，该通知为异常需要告警
				Example example = new Example(MerchantNotify.class);
				example.createCriteria()
				.andEqualTo("orderType", 1)
				.andEqualTo("state",0)
				.andLessThan("updateTime",c.getTime());
				int count = merchantNotifyMapper.selectCountByExample(example);
				
				if( count>0) {
					String remark = "系统存在"+count+"条超过1小时处于处理中的异常商户通知！";
					String level = "紧急";
					HandlerBuilder.get().sendNotify(null, level, remark, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		
	}
}
