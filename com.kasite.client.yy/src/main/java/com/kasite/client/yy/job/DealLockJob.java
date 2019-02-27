package com.kasite.client.yy.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.yy.bean.dbo.YyLock;
import com.kasite.client.yy.constant.Constant;
import com.kasite.client.yy.dao.IYyLockMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderListLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqUnlock;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 解锁JOB
 * 
 * @author linjf TODO
 */
@Component
public class DealLockJob{
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private boolean flag = true;
	
	@Autowired
	IYyLockMapper lockMapper;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IYYService yyService;

	@Autowired
	KasiteConfigMap config;
	/**
	 * 解锁
	 * 每5分钟执行
	 * @Description:
	 */
	public void dealLockData() {
		
		if(flag && config.isStartJob(this.getClass())) {
			flag = false;
			try {
				List<YyLock> list = lockMapper.queryYyLock(null, 2);
				if (list == null || list.size() <= 0) {
					log.error("本次未查询到需要解锁的数据");
					return;
				}
				for (int i = 0; i < list.size(); i++) {
					try {
						Document document = DocumentHelper.createDocument();
						Element req = document.addElement(Constant.REQ);
						XMLUtil.addElement(req, Constant.TRANSACTIONCODE, Constant.UNLOCKCODE);
						Element service = req.addElement(Constant.DATA);
						XMLUtil.addElement(service, "OrderId", list.get(i).getOrderId());
						
						InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(DealLockJob.class,ApiModule.YY.UnLock.getName(),
								document.asXML(), list.get(i).getOrderId(), "dealLockData", null, null,list.get(i).getOpenId());
						ReqUnlock t = new ReqUnlock(msg);
						CommonReq<ReqUnlock> commReq = new CommonReq<ReqUnlock>(t);
						CommonResp<RespMap> resp = yyService.unlock(commReq);//(msg);
						if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
							//调用his或内部 解锁成功。
							
							//TODO 判断解锁成功
							ReqOrderListLocal orderListReq = new ReqOrderListLocal(msg, 
									null, null, null, list.get(i).getOrderId(), null, 
									null, null, null, null, null, null, null);
							CommonResp<RespOrderLocalList> orderListResp =  orderService.orderListLocal(new CommonReq<ReqOrderListLocal>(orderListReq));
							if(orderListResp!=null && KstHosConstant.SUCCESSCODE.equals(orderListResp.getCode()) && null != orderListResp.getData()
									&& orderListResp.getData().size()>0) {
								RespOrderLocalList orderResp = orderListResp.getData().get(0);
								boolean isCancelOrder = (orderResp != null && KstHosConstant.ORDERPAY_0.equals(orderResp.getPayState())
										&& KstHosConstant.ORDERBIZSTATE_0.equals(orderResp.getBizState())
										&& (!KstHosConstant.ORDEROVER_5.equals(orderResp.getOverState())
												|| !KstHosConstant.ORDEROVER_6.equals(orderResp.getOverState())));
								if (isCancelOrder) {
									// 如果有挂号记录且不是状态是待支付，解锁号的同时需要将订单状态取消
									ReqCancelOrder canOrderReq = new ReqCancelOrder(msg, 
											list.get(i).getOrderId(), orderResp.getOperatorId(), orderResp.getOperatorName());
									CommonResp<RespMap> canResp = orderService.cancelOrder(new CommonReq<ReqCancelOrder>(canOrderReq));
									if(canResp==null || KstHosConstant.SUCCESSCODE.equals(canResp.getCode())) {
										throw new RRException(RetCode.Common.CallHOPClientError, (canResp!=null?canResp.getMessage():"取消订单异常：返回值为空"));
									}
								}
								LogUtil.info(log, "OrderId:" + "" + list.get(i).getOrderId() + "自动解锁成功");
							}
						}else {
							//TODO 解锁失败在10分钟内继续调用如果失败就不做处理

							LogUtil.info(log, "OrderId:" + "" + list.get(i).getOrderId() + "自动解锁失败。"+ resp.getMessage());
							
						}
						
					} catch (Exception e) {
//						log.error("作业解锁异常：OrderId:" + "" + list.get(i).getOrderId(), e);
						e.printStackTrace();
						LogUtil.error(log, "OrderId:" + "" + list.get(i).getOrderId(),e);
						continue;
					}
//					log.error("OrderId:" + "" + list.get(i).getOrderId()+ "自动解锁成功");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.error(log, e);
			}finally {
				flag = true;
			}
			
			
		}
		
		
	}
}
