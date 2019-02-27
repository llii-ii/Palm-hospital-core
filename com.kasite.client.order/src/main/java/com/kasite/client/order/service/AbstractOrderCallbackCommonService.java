package com.kasite.client.order.service;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.order.IBizExecuteHandler;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.OrderCallbackService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqPayForCompletion;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.pay.IPayMerchantService;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.kasite.core.serviceinterface.module.pay.req.ReqAddMerchantNotifyFailure;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateMerchantNotifyById;
import com.kasite.core.serviceinterface.module.pay.resp.RespGetMerchantNotifyById;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 订单回调类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:11:57
 */
public abstract class AbstractOrderCallbackCommonService implements OrderCallbackService {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);
	
	protected IBizExecuteHandler getBizExecuteHandler(String serviceId) {
		return HandlerBuilder.get().getBizExecuteHandler(serviceId);
	}
	
	
	/** 默认3次 */
	private static int reTryTimes = 3;
	
	@Autowired
	private IOrderMapper orderMapper;
	
//	@Autowired
//	protected IOrderExtensionMapper orderExtensionMapper;
	
	@Autowired
	protected IOrderService orderService;
	
	@Autowired
	protected IPayService payService;
	
	@Autowired
	protected IPayMerchantService payMerchantService;
	
	@Override
	public void orderCallback(InterfaceMessage msg,MerchantNotify merchantNotify) throws Exception {
		AuthInfoVo authInfoVo = KasiteConfig.getAuthInfo(msg);
		String logTag = this.getClass().getName()+".orderCallback"; 
		String orderId = merchantNotify.getOrderId();
		LogUtil.info(log, logTag + "INFO[执行订单支付完成后的回调业务！|"+Thread.currentThread().getName()+"]");
		//调用支付完成接口
		ReqPayForCompletion reqPayForCompletion = new ReqPayForCompletion(msg,merchantNotify.getOrderId(),merchantNotify.getTransactionNo(),merchantNotify.getPrice(),merchantNotify.getAccNo());
		CommonResp<RespMap> resp = orderService.payForCompletion(new CommonReq<ReqPayForCompletion>(reqPayForCompletion));
		LogUtil.info(log, logTag + "INFO[执行 payForCompletion！|"+Thread.currentThread().getName()+"|"+ resp.toJSONResult()+"]");
		if(StringUtil.isEmpty(merchantNotify.getOrderId())) {
			LogUtil.info(log,new LogBody(authInfoVo).set("MerchantNotifyID",merchantNotify.getId()).set("msg","MerchantNotify对象入参异常！"));
			return;
		}
		LogUtil.info(log,new LogBody(authInfoVo).set("orderId",orderId).set("msg", MessageFormat.format("订单号: {0} 查询是否之前有支付回调过该接口。", orderId)));
		//判断数据库是否有支付的OrderCallback记录
		ReqGetMerchantNotifyById reqGetMerchantNotifyById= new ReqGetMerchantNotifyById(msg,merchantNotify.getId());
		CommonResp<RespGetMerchantNotifyById> commonResp = payMerchantService.getMerchantNotifyById(new CommonReq<ReqGetMerchantNotifyById>(reqGetMerchantNotifyById));
		RespGetMerchantNotifyById existMerchantNotify = null;
		if(KstHosConstant.SUCCESSCODE.equals(commonResp.getCode()) && !StringUtil.isEmpty(commonResp.getData())) {
			LogUtil.info(log,new LogBody(authInfoVo).set("orderId",orderId).set("msg", MessageFormat.format("订单号: {0} 数据库中已经存在支付回调记录，不用记录新的回调结果。", orderId)));
			//如果数据库确实存在回调记录
			existMerchantNotify = commonResp.getData().get(0);
			//简单判断订单号是否与数据库一致，防止入参MerchantNotify对象被修改
			if( !merchantNotify.getOrderId().equals(existMerchantNotify.getOrderId())) {
				LogUtil.info(log,new LogBody(authInfoVo).set("orderId",orderId).set("msg", "回调信息与数据库不一致！paramOrderid:" + merchantNotify.getOrderId()+"|dbOrderId:"+existMerchantNotify.getOrderId()));
				return;
			}
			//-1表示失败，可以执行（0表示执行中，1表示执行成功）
			if(existMerchantNotify.getState().intValue()!=-1) {
				LogUtil.info(log,new LogBody(authInfoVo).set("orderId",merchantNotify.getOrderId()).set("msg","该订单正在处理，或者已经处理完成！orderId" + merchantNotify.getOrderId()));
				return;
			}
		}
		LogUtil.info(log,new LogBody(authInfoVo).set("MerchantNotifyID",merchantNotify.getId()).set("msg",MessageFormat.format("订单号: {0} 执行支付完成后的业务逻辑。执行次数：第【{1}】次，总共重复次数：【{2}】", orderId,existMerchantNotify.getRetryNum(),reTryTimes)));
		// 超过重试次数，HIS处理还是失败，则触发告警机制
		if (existMerchantNotify.getRetryNum() > reTryTimes) {
			LogUtil.info(log, new LogBody(authInfoVo).set("msg","OrderCallback-orderId：" + existMerchantNotify.getOrderId() + ",重试" + reTryTimes + "次失败！"));
			failWarn(msg,merchantNotify);
			return;
		}
		OrderView orderView = null;
		try {
			orderView = orderMapper.getOrderViewByOrderId(merchantNotify.getOrderId(),KasiteConfig.getHistoryOrderListDays());
		} catch (Exception e) {
			e.printStackTrace();
			failWarn(msg,merchantNotify);
		}
		if (orderView == null) {
			LogUtil.info(log,new LogBody(authInfoVo).set("msg","OrderCallback-orderId：" + merchantNotify.getOrderId() + ",查询OrderView为null，请查看订单是否过期！"));
			return;
		}
		
		// 设置成0状态，表示这个orderCallback正在处理
		ReqUpdateMerchantNotifyById reqUpdateMerchantNotifyById = new ReqUpdateMerchantNotifyById (msg,merchantNotify.getId(),0,merchantNotify.getRetryNum() + 1);
		CommonResp<RespMap> updateResp = payMerchantService.updateMerchantNotifyById(new CommonReq<ReqUpdateMerchantNotifyById>(reqUpdateMerchantNotifyById));
		if( !KstHosConstant.SUCCESSCODE.equals(updateResp.getCode())) {
			failWarn(msg,merchantNotify);
			LogUtil.info(log,new LogBody(authInfoVo).set("msg", "OrderCallback-orderId:" + merchantNotify.getOrderId() + ",更新订单回调表失败：updateResult=" + updateResp.getMessage()));
			return;
		}
//		OrderExtension orderExtension = null;
//		try {
//			orderExtensionMapper.selectByPrimaryKey(merchantNotify.getOrderId());
//		} catch (Exception e3) {
//			e3.printStackTrace();
//			LogUtil.error(log, "OrderCallback-orderId:" + merchantNotify.getOrderId() + ",获取订单扩展表异常",e3);
//		}
		
		//判断该渠道是否调用HIS业务接口
		if(!KasiteConfig.caseChannelIsCallHis(orderView.getChannelId(), orderView.getConfigKey(), orderView.getServiceId())) {
			updateFinshedState(msg,merchantNotify);
			//不需要调用HIS，则更新商户通知的状态，直接返回
			LogUtil.info(log, new LogBody(authInfoVo).set("msg", "OrderCallback-orderId：" + existMerchantNotify.getOrderId() + ",判断订单不需要执行HIS业务逻辑直接返回完成" + reTryTimes + "次失败！"));
			return ;
		}
		
		// 处理订单
		RespPayEndBizOrderExecute payEndBizResp = dealBiz(msg, merchantNotify, orderView);
		int result = payEndBizResp.getBizDealState().getValue().intValue();
		switch (result) {
		case KstHosConstant.HIS_DEAL_STATE_1:
			// 设置成1状态,表示orderCallback处理成功
			updateFinshedState(msg,merchantNotify);
			ReqBizForCompletion reqBizForCompletion = new ReqBizForCompletion(msg, merchantNotify.getOrderId(), 
					orderView.getOperatorId(), orderView.getOperatorName(),payEndBizResp.getOutBizOrderId());
			orderService.bizForCompletion(new CommonReq<ReqBizForCompletion>(reqBizForCompletion));
			break;
		// 明确失败
		case KstHosConstant.HIS_DEAL_STATE_0:
			//调用HIS明确失败，则调用his订单状态确认接口，如果成功，则走成功结果
			if (KstHosConstant.HIS_DEAL_STATE_1 == checkHisOrderStatus(msg,merchantNotify,orderView).getValue()) {
				// 验证成功走成功逻辑
				updateFinshedState(msg,merchantNotify);
				reqBizForCompletion = new ReqBizForCompletion(msg, merchantNotify.getOrderId(), 
						orderView.getOperatorId(), orderView.getOperatorName());
				orderService.bizForCompletion(new CommonReq<ReqBizForCompletion>(reqBizForCompletion));
				break;
			}
			// 设置成1状态,表示orderCallback处理完成
			updateFinshedState(msg, merchantNotify);
			try {;
				Integer price = orderView.getPrice();
				Integer refundPrice = orderView.getPrice();
				String operatorId = orderView.getOperatorId();
				String operatorName = orderView.getOperatorName();
				String channelId = orderView.getChannelId();
				String reason = "HIS返回结果明确退费，系统退费！";
				//执行取消订单操作校验如果失败则抛出异常
				orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(
						new ReqOrderIsCancel(msg, merchantNotify.getOrderId(), price, refundPrice, operatorId, operatorName, channelId, reason)))
				.check();
			}catch (Exception e) {
				e.printStackTrace();
				failWarn(msg,merchantNotify);
				LogUtil.error(log,authInfoVo,
						"HOrderCallback-orderId:" + merchantNotify.getOrderId() + "，调用orderIsCancelAPI异常：" + e.getMessage(),e);
			}
			break;
		// 不明确失败
		case KstHosConstant.HIS_DEAL_STATE_2:
			//调用HIS不明确失败，则调用his订单状态确认接口，如果成功，则走成功结果
			if (KstHosConstant.HIS_DEAL_STATE_1 == checkHisOrderStatus(msg,merchantNotify,orderView).getValue()) {
				// 验证成功走成功逻辑
				updateFinshedState(msg, merchantNotify);
				reqBizForCompletion = new ReqBizForCompletion(msg, merchantNotify.getOrderId(), 
						orderView.getOperatorId(), orderView.getOperatorName());
				orderService.bizForCompletion(new CommonReq<ReqBizForCompletion>(reqBizForCompletion));
				break;
			}
			// 设置成-1状态,表示orderCallback处理失败，需要重试
			reqUpdateMerchantNotifyById = new ReqUpdateMerchantNotifyById (msg,merchantNotify.getId(),-1,null);
			updateResp = payMerchantService.updateMerchantNotifyById(new CommonReq<ReqUpdateMerchantNotifyById>(reqUpdateMerchantNotifyById));
			if( !KstHosConstant.SUCCESSCODE.equals(updateResp.getCode())) {
				failWarn(msg,merchantNotify);
				LogUtil.info(log,new LogBody(authInfoVo).set("msg","OrderCallback-orderId:" + merchantNotify.getOrderId() + "，回调处理返回未知错误，保存订单回调表异常"));
			}
			//写入失败重试表
			ReqAddMerchantNotifyFailure reqAddMerchantNotifyFailure = new ReqAddMerchantNotifyFailure(msg, merchantNotify.getId());
			payMerchantService.addMerchantNotifyFailure(new CommonReq<ReqAddMerchantNotifyFailure> (reqAddMerchantNotifyFailure));
			break;
		/** hiS订单金额与交易支付金额不一致，需要退款的 */
		case KstHosConstant.HIS_DEAL_STATE_3:
			// 设置成1状态,表示orderCallback处理成功
			updateFinshedState(msg, merchantNotify);
			try {
				Integer price = orderView.getPrice();
				Integer refundPrice = orderView.getPrice();
				String operatorId = orderView.getOperatorId();
				String operatorName = orderView.getOperatorName();
				String channelId = orderView.getChannelId();
				String reason = "HIS订单金额与交易支付金额不一致,系统退费!";
				//执行取消订单操作校验如果失败则抛出异常
				orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(
						new ReqOrderIsCancel(msg, merchantNotify.getOrderId(), price, refundPrice, operatorId, operatorName, channelId, reason)))
				.check();
				// 诊间退费笔数+1推送到云报表
				//reportFormUtil.dataCloudCollection(msg,orderView.getChannelId(), 109, 1, "1");
			} catch (Exception e1) {
				e1.printStackTrace();
				failWarn(msg,merchantNotify);
				LogUtil.error(log,authInfoVo,
						"HOrderCallback-orderId:" + merchantNotify.getOrderId() + "，调用orderIsCancelAPI异常：" + e1.getMessage(),e1);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 处理状态为成功的订单
	 * 
	 * @param orderCallback
	 * @param orderView
	 * @throws Exception 
	 */
	private void updateFinshedState(InterfaceMessage msg,MerchantNotify merchantNotify) throws Exception{
		ReqUpdateMerchantNotifyById reqUpdateMerchantNotifyById = new ReqUpdateMerchantNotifyById (msg,merchantNotify.getId(),1,null);
		payMerchantService.updateMerchantNotifyById(new CommonReq<ReqUpdateMerchantNotifyById>(reqUpdateMerchantNotifyById));
	}
}
