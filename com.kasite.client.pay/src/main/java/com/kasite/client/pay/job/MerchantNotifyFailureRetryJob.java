package com.kasite.client.pay.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.DateOper;
import com.kasite.client.pay.dao.IMerchantNotifyFailureBakMapper;
import com.kasite.client.pay.dao.IMerchantNotifyFailureMapper;
import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.client.pay.util.MerchantNotifyExcutor;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotifyFailure;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotifyFailureBak;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO 商户异步通知失败重试JOB
 */
@Component
public class MerchantNotifyFailureRetryJob {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	@Autowired
	IMerchantNotifyFailureMapper merchantNotifyFailureMapper;
	
	@Autowired
	IMerchantNotifyFailureBakMapper merchantNotifyFailureBakMapper;
	
	@Autowired
	IMerchantNotifyMapper merchantNotifyMapper;
	@Autowired
	KasiteConfigMap config;
	/**
	 * 上一次执行完毕，延迟5秒再执行
	 */
	public void merchantNotifyFailureRetry() {
		if(config.isStartJob(MerchantNotifyFailureRetryJob.class)) {
			String nowDate = DateOper.getDateMilliFormat();
			InterfaceMessage msg;
			try {
				msg = KasiteConfig.createJobInterfaceMsg(MerchantNotifyFailureRetryJob.class, "MerchantNotifyFailureRetryJob", 
						null, nowDate, null, null, null,null);	
				List<MerchantNotifyFailure> list = merchantNotifyFailureMapper.selectAll();
				if(null != list && list.size() > 0) {
					LogUtil.info(log, "MerchantNotifyFailureRetryJob-needRetryCount:"+list.size());
				}
				for(MerchantNotifyFailure merchantNotifyFailure : list) {
					Long merchantNotifyId = merchantNotifyFailure.getMerchantNotifyId();
					MerchantNotify merchantNotify = merchantNotifyMapper.selectByPrimaryKey(merchantNotifyId);
					MerchantNotifyExcutor merchantNotifyExcutor = new MerchantNotifyExcutor(msg,merchantNotify,merchantNotifyMapper);
					merchantNotifyExcutor.setName("payNotifyRetry-thread-orderId="+merchantNotify.getOrderId());
					KstHosConstant.cachedThreadPool.execute(merchantNotifyExcutor);
					//数据迁移到bak表
					MerchantNotifyFailureBak back = BeanCopyUtils.copyProperties(merchantNotifyFailure, new MerchantNotifyFailureBak(), null);
					back.setUpdateTime(DateOper.getNowDateTime());
					merchantNotifyFailureBakMapper.insertOnDuplicateKey(back);
					merchantNotifyFailureMapper.deleteByPrimaryKey(merchantNotifyFailure.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}
		}
	}
}
