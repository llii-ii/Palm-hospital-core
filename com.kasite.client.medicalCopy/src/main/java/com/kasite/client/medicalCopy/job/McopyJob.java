package com.kasite.client.medicalCopy.job;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.medicalCopy.dao.ExpressOrderMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.ExpressOrder;

@Component
public class McopyJob {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	@Autowired
	private ExpressOrderMapper expressOrderMapper;
	
	public void orderNoPay(){
//		//log.info("病历复印订单检查支付是否超过10分钟");
//		ExpressOrder expressOrder = new ExpressOrder();
//		//expressOrder.setPayState("2");
//		List<ExpressOrder> list = expressOrderMapper.select(expressOrder);
//		for (int i = 0; i < list.size(); i++) {
//			String updateTime = list.get(i).getUpdateTime().toString();
//			long diff;
//			try {
//				diff = getDiffMin(updateTime);
//				if(diff >= 10) {
//					String id = list.get(i).getId();
//					expressOrder.setId(id);
//					expressOrder.setPayState("1");
//					expressOrder.setWxOrderId("0");
//					expressOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//					expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
//				}
//			} catch (ParseException e) {
//				log.error(e);
//				e.printStackTrace();
//				LogUtil.error(log, e);
//			}
//
//		}
	}


	private long getDiffMin(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date old = df.parse(date);
		long l = now.getTime() - old.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if(day > 0 || hour > 0) {
			return 100;
		}else {
			return min;
		}
	}
}
