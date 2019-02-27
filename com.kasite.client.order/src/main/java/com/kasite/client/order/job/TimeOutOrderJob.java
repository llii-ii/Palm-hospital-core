package com.kasite.client.order.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.coreframework.util.DateOper;
import com.kasite.client.order.bean.dbo.Order;
import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 超过10分钟未支付订单，自动设置为超时订单
 */
@Component
public class TimeOutOrderJob {
	
	private boolean flag = true;
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	@Autowired
	IOrderMapper orderMapper;
	
	@Autowired
	IOrderService orderService;

	@Autowired
	KasiteConfigMap config;
	/**
	 * 每10分钟执行一次
	 */
	@Transactional
	public void cancelTimeOutOrder() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				List<Order> orderList = orderMapper.queryTimeOutOrder();
				if( !CollectionUtils.isEmpty(orderList)) {
					for(Order order : orderList) {
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(TimeOutOrderJob.class, "TimeOutOrderJob",
								null, order.getOrderId(), "TimeOutOrderJob", order.getChannelId(), null,null);
						ReqCancelOrder reqCancelOrder = new ReqCancelOrder(msg, order.getOrderId(),"TimeOutOrderJob", DateOper.getNowDateTime().toString());
						orderService.cancelOrder(new CommonReq<ReqCancelOrder>(reqCancelOrder));
					}
				}
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			flag = true;
		}
	}
}
