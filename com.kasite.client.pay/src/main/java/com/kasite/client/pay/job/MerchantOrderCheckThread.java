package com.kasite.client.pay.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.client.pay.bean.dbo.MerchantOrderCheck;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.DefaultClientEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqMaintenanceMsg;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqRevokeOrder;
import com.kasite.core.serviceinterface.module.pay.IPaymentGateWayService;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqClose;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespClose;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.yihu.wsgw.api.InterfaceMessage;


/**
 * 处理微信支付结果未知的订单
 * 
 * @author 無
 *
 */
public class MerchantOrderCheckThread extends Thread {
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	/** 重复执行次数 间隔5秒 60秒超时 */
	private Integer count = 12;
	
	private MerchantOrderCheck merchantOrderCheck;
	
	private PayBackCallUtil payBackCallUtil; 
	
	private IOrderService orderService;
	
	private IMsgService msgService;
	
	private InterfaceMessage msg;
	
	private AuthInfoVo authInfoVo;
	
	private IPaymentGateWayService payGateWayService;
	
	private String swiftpassMchType = KstHosConstant.STRING_EMPTY;
	
	public MerchantOrderCheckThread(InterfaceMessage msg,MerchantOrderCheck merchantOrderCheck) {
		this.merchantOrderCheck = merchantOrderCheck;
		this.payBackCallUtil =  SpringContextUtil.getBean(PayBackCallUtil.class);
		this.orderService = SpringContextUtil.getBean(IOrderService.class);
		this.msgService = SpringContextUtil.getBean(IMsgService.class);
		this.msg = msg;
		this.authInfoVo = KasiteConfig.getAuthInfo(msg);
		ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(merchantOrderCheck.getClientId(),merchantOrderCheck.getConfigKey());
		if( ChannelTypeEnum.swiftpass.equals(channelTypeEnum)) {
			swiftpassMchType = KasiteConfig.getSwiftpass(SwiftpassEnum.swiftpass_mch_type,merchantOrderCheck.getConfigKey());
		}
		payGateWayService = HandlerBuilder.get().getPayGateWayInstance(channelTypeEnum.name()+swiftpassMchType);
	}

	@Override
	public void run() {
		try {
			if( payGateWayService == null ) {
				//如果实现类为空，则直接接口
				LogUtil.warn(log, new LogBody(authInfoVo).set("configKey", authInfoVo.getConfigKey())
						.set("orderId", merchantOrderCheck.getOrderId()).set("MchType", swiftpassMchType)
						.set("msg", "未获取支付网关实现类！"));
				return ;
			}
			commonCheckMerchantOrder(merchantOrderCheck);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			//调用出现异常，重新进入重试
			if (merchantOrderCheck.getTimes().intValue() > this.count) {
				//告警
				if(msgService!=null ) {
					try {
						ReqMaintenanceMsg reqMaintenanceMsg = new ReqMaintenanceMsg(msg, KasiteConfig.getAppId(), 
							"当面付待待支付订单超时！",null, "紧急","当面付待待支付订单超时！orderId="+merchantOrderCheck.getOrderId(), null);
						msgService.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(reqMaintenanceMsg));
					} catch (Exception e1) {
						e1.printStackTrace();
						LogUtil.error(log, e);
					}
				}
			}else {
				payBackCallUtil.addMerchantOrderCheck(authInfoVo,merchantOrderCheck.getOrderId(), merchantOrderCheck.getTransactionNo(), 
						merchantOrderCheck.getTimes()+1, merchantOrderCheck.getConfigKey(),merchantOrderCheck.getClientId());
			}
			
		}
	}
	
	private void commonCheckMerchantOrder(MerchantOrderCheck merchantOrderCheck) throws Exception {
			PgwReqQueryOrder pgwReqQueryOrder = new PgwReqQueryOrder();
			pgwReqQueryOrder.setOrderId(merchantOrderCheck.getOrderId());
			pgwReqQueryOrder.setTransactionNo(merchantOrderCheck.getTransactionNo());
			PgwRespQueryOrder pgwRespQueryOrder = payGateWayService.queryOrder(authInfoVo, pgwReqQueryOrder);
			LogUtil.info(log, new LogBody(authInfoVo).set("msg", "扫码付-未知结果订单查询").set("orderId", merchantOrderCheck.getOrderId())
					.set("time", merchantOrderCheck.getTimes()).set("orderQuery", pgwRespQueryOrder.getOrderState().toString()));
			if(RetCode.Success.RET_10000.equals(pgwRespQueryOrder.getRespCode()) ) {
				if( KstHosConstant.I2.equals(pgwRespQueryOrder.getOrderState()) ) {
					payBackCallUtil.addPayNotify(msg,merchantOrderCheck.getOrderId(), pgwRespQueryOrder.getTransactionNo(), merchantOrderCheck.getClientId(),
							pgwRespQueryOrder.getPrice(),merchantOrderCheck.getConfigKey());
				}else if( KstHosConstant.I0.equals(pgwRespQueryOrder.getOrderState())
						||KstHosConstant.I1.equals(pgwRespQueryOrder.getOrderState())) {
					/** 超时-撤销微信订单、撤销本地订单 */
					if (merchantOrderCheck.getTimes().intValue() >= this.count) {
						if( DefaultClientEnum.sweepcodepay.getClientId().equals(merchantOrderCheck.getClientId()) ) {
							//当面付渠道的撤销
							commonRevoke();
						}
//						else {//其他渠道关闭订单
//							commonClose();
//						}
					} else {
						payBackCallUtil.addMerchantOrderCheck(authInfoVo,merchantOrderCheck.getOrderId(), merchantOrderCheck.getTransactionNo(), 
								merchantOrderCheck.getTimes()+1, merchantOrderCheck.getConfigKey(),merchantOrderCheck.getClientId());
					}
				}else if(KstHosConstant.I5.equals(pgwRespQueryOrder.getOrderState())
						||KstHosConstant.I6.equals(pgwRespQueryOrder.getOrderState())) {
					//已关闭，已撤销，直接跳出
					return ;
				}else {
					//TODO 其他的订单状态待完善，目前暂时不做处理，跳出。
					//commonRevoke();
					return;
				}
			} else if(RetCode.Pay.ERROR_MERCHANTORDER.equals(pgwRespQueryOrder.getRespCode())) {
				//不存在商户订单，则直接跳出
				return ;
			} else {
				//调用查询商户订单失败，则重新进入重试队列
				payBackCallUtil.addMerchantOrderCheck(authInfoVo,merchantOrderCheck.getOrderId(), merchantOrderCheck.getTransactionNo(), 
						merchantOrderCheck.getTimes()+1, merchantOrderCheck.getConfigKey(),merchantOrderCheck.getClientId());
			}
	}
	
	private void commonRevoke() throws Exception {
		PgwReqRevoke pgwReqRevoke = new PgwReqRevoke();
		pgwReqRevoke.setOrderId(merchantOrderCheck.getOrderId());
		PgwRespRevoke pgwRespRevoke = payGateWayService.revoke(authInfoVo,pgwReqRevoke);
		if (RetCode.Success.RET_10000.equals(pgwRespRevoke.getRespCode())) {
			//调用订单撤销1
			ReqRevokeOrder reqRevokeOrder = new ReqRevokeOrder(msg, merchantOrderCheck.getOrderId(), System.currentTimeMillis()+"", this.getName(), 0);
			orderService.revokeOrder(new CommonReq<ReqRevokeOrder>(reqRevokeOrder));
			LogUtil.info(log, new LogBody(authInfoVo).set("msg", "扫码付-撤销订单").set("orderId", merchantOrderCheck.getOrderId())
					.set("time", merchantOrderCheck.getTimes()).set("撤销结果", pgwRespRevoke.getRespCode().toString()));
		}
	}

	private void commonClose() throws Exception {
		PgwReqClose pgwReqClose = new PgwReqClose();
		pgwReqClose.setOrderId(merchantOrderCheck.getOrderId());
		PgwRespClose pgwRespClose = payGateWayService.close(authInfoVo,pgwReqClose);
		if (RetCode.Success.RET_10000.equals(pgwRespClose.getRespCode())) {
			//调用订单撤销1
			ReqCancelOrder reqCancelOrder = new ReqCancelOrder(msg,merchantOrderCheck.getOrderId(), this.getName(), this.getName());
			orderService.cancelOrder(new CommonReq<ReqCancelOrder>(reqCancelOrder));
			LogUtil.info(log, new LogBody(authInfoVo).set("msg", "扫码付-撤销订单").set("orderId", merchantOrderCheck.getOrderId())
					.set("time", merchantOrderCheck.getTimes()).set("撤销结果", pgwRespClose.getRespCode().toString()));
		}
	}
}
