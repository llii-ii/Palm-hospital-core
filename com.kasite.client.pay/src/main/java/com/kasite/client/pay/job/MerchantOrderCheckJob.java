package com.kasite.client.pay.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.kasite.client.pay.bean.dbo.MerchantOrderCheck;
import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.client.pay.dao.IMerchantOrderCheckMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 商户订单确认job
 * 目前只针对，扫码付生成的，且处于支付中的状态的订单，进行再次核实
 */
@Component
public class MerchantOrderCheckJob {
	
	private boolean flag = true;
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	
	@Autowired
	IMerchantOrderCheckMapper merchantOrderCheckMapper;
	
	@Autowired
	KasiteConfigMap config;
	@Autowired
	IMerchantNotifyMapper merchantNotifyMapper;
	/**
	 * 系统启动5秒启动job,上一次执行完毕，延迟5秒再执行
	 */
	public void merchantOrderCheck() {
		try {
			if (flag && config.isStartJob(MerchantOrderCheckJob.class)) {
				flag = false; 
				//查询所有的需要待核实的订单
				List<MerchantOrderCheck> list = merchantOrderCheckMapper.selectAll();
//				String beginTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
				if( !CollectionUtils.isEmpty(list)) {
					for( MerchantOrderCheck orderCheck : list) {
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(MerchantOrderCheckJob.class, "","<Req><Data></Data></Req>", 
								orderCheck.getOrderId(), null, orderCheck.getClientId(), orderCheck.getConfigKey(),null);
						KstHosConstant.cachedThreadPool.execute(new MerchantOrderCheckThread(msg,orderCheck));
						merchantOrderCheckMapper.deleteByPrimaryKey(orderCheck.getId());
					}
				}
				//调试用
//				LogUtil.warn(log, new LogBody(KasiteConfig.createAuthInfoVo(MerchantOrderCheckJob.class)).set("beginTime", beginTime)
//						.set("endTime", DateOper.getNow("yyyy-MM-dd HH:mm:ss")).set("cachedThreadPool.queue", KstHosConstant.cachedThreadPool.getQueue().size())
//						.set("cachedThreadPool.ActiveCount", KstHosConstant.cachedThreadPool.getActiveCount())
//						.set("list", list.size()));
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.error(log, e);
			e.printStackTrace();
		} finally {
			flag = true;
		}
	}
}
