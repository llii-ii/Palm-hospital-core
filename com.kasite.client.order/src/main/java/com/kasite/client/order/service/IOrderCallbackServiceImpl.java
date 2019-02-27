package com.kasite.client.order.service;

import java.text.MessageFormat;

import com.kasite.core.common.util.DateOper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.client.order.circuitbreaker.HisBizHystrixCommand;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqMaintenanceMsg;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.OrderCallbackService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.req.ReqPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.yihu.wsgw.api.InterfaceMessage;;


/**
 * @author linjf
 * TODO
 */
@Service
public class IOrderCallbackServiceImpl extends AbstractOrderCallbackCommonService implements OrderCallbackService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);
	
	@Autowired
	private IMsgService msgService;
	
	/**
	 * @param msg
	 * @param orderCallback
	 */
	@Override
	public void failWarn(InterfaceMessage msg,MerchantNotify merchantNotify) {
		try {
			ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
					"支付回调HIS异常", null, "紧急", "调用HIS重试" + merchantNotify.getRetryNum() + "次失败！orderId" + merchantNotify.getId(), null);
			msgService.sendMaintenancenMsg(new  CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
	}
	
	@Override
	public RespPayEndBizOrderExecute dealBiz(InterfaceMessage msg, MerchantNotify merchantNotify, OrderView orderView) throws Exception {
		AuthInfoVo authInfo = KasiteConfig.parse(msg);
		authInfo.setClientId(merchantNotify.getClientId());
		authInfo.setConfigKey(merchantNotify.getConfigKey());
		msg.setAuthInfo(authInfo.toString());
		RespPayEndBizOrderExecute respPayEndBizOrderExecute = null;
		String orderId = null == orderView?"":orderView.getOrderId();
		if(null != orderView && StringUtil.isNotBlank(orderView.getServiceId())) {
			String serviceId = orderView.getServiceId();
			IBizExecuteHandler handler = getBizExecuteHandler(serviceId);
			//执行该项业务支付完成后的业务逻辑。
			if(null != handler) {
				try {
					LogUtil.info(log,new LogBody(authInfo).set("msg", MessageFormat.format("订单号: {2} 获取到业务类型：{0} 对应的执行器：{1} 开始执行业务支付完成后动作。",
							serviceId,handler.getClass().getName(),orderId)));
					ReqPayEndBizOrderExecute reqPayEndBizOrderExecute = new ReqPayEndBizOrderExecute(msg, merchantNotify.getOrderId(),
							authInfo.getSign(),authInfo.getSign(), merchantNotify.getTransactionNo(), merchantNotify.getPrice(), null);
					respPayEndBizOrderExecute = handler.bizPayEndExecute(new CommonReq<ReqPayEndBizOrderExecute>(reqPayEndBizOrderExecute));
//					respPayEndBizOrderExecute = new HisBizHystrixCommand(handler, reqPayEndBizOrderExecute).execute();
					LogUtil.info(log,new LogBody(authInfo).set("msg", MessageFormat.format("订单号: {3} 业务类型：{0} 对应的执行器：{1} 执行业务支付完成。结果：{2}",
							serviceId,handler.getClass().getName(),respPayEndBizOrderExecute.getBizDealState().getValue(),orderId)));
					return respPayEndBizOrderExecute;
				}catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,authInfo,MessageFormat.format("订单:{0} 支付后完成业务的时候发生异常",orderId),e);
					LogUtil.error(log, e);
					failWarn(msg, merchantNotify);
				}
			}
			LogUtil.info(log,new LogBody(authInfo).set("msg", MessageFormat.format("该业务类型订单: {0} 未实现支付完成后的回调接口代码！！！",serviceId)));
		}else {
			throw new RRException(MessageFormat.format("该订单: {0} 未定义业务类型无法执行业务闭环，请核实代码！！！",orderId));
		}
		respPayEndBizOrderExecute = new RespPayEndBizOrderExecute();
		respPayEndBizOrderExecute.setBizDealState(BizDealState.BIZ_DEAL_STATE_2);
		return respPayEndBizOrderExecute;
	}

	@Override
	public BizDealState checkHisOrderStatus(InterfaceMessage msg,MerchantNotify merchantNotify,OrderView orderView) {
		
		AuthInfoVo authInfo = KasiteConfig.parse(msg);
		String orderId = null == orderView?"":orderView.getOrderId();
		if(null != orderView && StringUtil.isNotBlank(orderView.getServiceId())) {
			String serviceId = orderView.getServiceId();
			IBizExecuteHandler handler = getBizExecuteHandler(serviceId);
			//执行该项业务确认订单的业务逻辑。
			if(null != handler) {
				try {
					ReqPayEndBizOrderExecute reqPayEndBizOrderExecute;
					LogUtil.debug(log,new LogBody(authInfo).set("msg", MessageFormat.format("订单号: {2} 获取到业务类型：{0} 对应的执行器：{1} 开始执行业务确认bizCheckExecute动作。",
							serviceId,handler.getClass().getName(),orderId)));
					reqPayEndBizOrderExecute = new ReqPayEndBizOrderExecute(msg, merchantNotify.getOrderId(),authInfo.getSign(),
						authInfo.getSign(), merchantNotify.getTransactionNo(), merchantNotify.getPrice(),
						orderView.getCardNo(),orderView.getCardType(), null,
						DateOper.formatDate(orderView.getBeginDate(),"yyyy-MM-dd HH:mm:ss") );
					BizDealState retVal = getBizExecuteHandler(serviceId).bizCheckExecute(new CommonReq<ReqPayEndBizOrderExecute>
								(reqPayEndBizOrderExecute));
					LogUtil.debug(log,new LogBody(authInfo).set("msg", MessageFormat.format("订单号: {3} 业务类型：{0} 对应的执行器：{1} 执行业务确认bizCheckExecute动作。结果：{2}",
							serviceId,handler.getClass().getName(),retVal.getValue(),orderId)));
					return retVal;
				}catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log,authInfo,MessageFormat.format("订单:{0} 支付后bizCheckExecute确认业务的时候发生异常",orderId),e);
					failWarn(msg, merchantNotify);
				}
			}else {
				LogUtil.info(log,new LogBody(authInfo).set("msg", MessageFormat.format("该业务类型订单: {0} 未实现支付完成后bizCheckExecute的回调接口代码！！！",serviceId)));
			}
		}else {
			throw new RRException(MessageFormat.format("该订单: {0} 未定义业务类型无法执行业务闭环，请核实代码！！！",orderId));
		}
		return BizDealState.BIZ_DEAL_STATE_2;
	}
	
	

}
