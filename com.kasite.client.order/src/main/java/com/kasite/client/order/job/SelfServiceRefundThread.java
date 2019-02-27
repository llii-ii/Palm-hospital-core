package com.kasite.client.order.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.order.bean.dbo.SelfRefundRecord;
import com.kasite.client.order.bean.dbo.SelfRefundRecordOrder;
import com.kasite.client.order.dao.ISelfRefundRecordMapper;
import com.kasite.client.order.dao.ISelfRefundRecordOrderMapper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPayState;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
/**
 * @author linjf
 * TODO
 */
public class SelfServiceRefundThread extends Thread{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_SMARTPAY);
	
	private InterfaceMessage msg;
	
	private IOrderService orderService;
	
	private SelfRefundRecord selfRefundRecord;
	
	private ISelfRefundRecordMapper selfRefundRecordMapper;
	
	private ISelfRefundRecordOrderMapper selfRefundRecordOrderMapper;
	
	private List<SelfRefundRecordOrder> selfRefundRecordOrderList;
	
	private List<RespMap> refundableList;
	 
	public SelfServiceRefundThread(IOrderService orderService,InterfaceMessage msg,SelfRefundRecord selfRefundRecord,
			ISelfRefundRecordMapper selfRefundRecordMapper,ISelfRefundRecordOrderMapper selfRefundRecordOrderMapper,List<SelfRefundRecordOrder> selfRefundRecordOrderList,
			List<RespMap> refundableList) {
		this.msg = msg;
		this.orderService = orderService;
		this.selfRefundRecord = selfRefundRecord;
		this.selfRefundRecordMapper = selfRefundRecordMapper;
		this.selfRefundRecordOrderList = selfRefundRecordOrderList;
		this.refundableList = refundableList;
		this.selfRefundRecordOrderMapper= selfRefundRecordOrderMapper;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		AuthInfoVo authInfo = KasiteConfig.getAuthInfo(msg);
		String cardNo = selfRefundRecord.getCardNo();
		String cardType = selfRefundRecord.getCardType();
		String hisMemberId = selfRefundRecord.getHisMemberId();
		//日志
		LogUtil.info(log, new LogBody(authInfo).set("apiName", "用户自助退费").set("ThreadName", this.getName())
				.set("openId", selfRefundRecord.getOperatorId()).set("operatorName", selfRefundRecord.getOperatorName()).set("cardNo", cardNo)
				.set("balance", selfRefundRecord.getBalance()).set("hisMemberId", hisMemberId));
		int successRefundPrice = 0;
		int successRefundCount = 0 ;
		int leftBalance = selfRefundRecord.getRefundableBalance();
		//日志记录
		JSONObject logJosn = new JSONObject();
		logJosn.put("selfRefundRecordOrderList",JSONObject.toJSON(selfRefundRecordOrderList));
		logJosn.put("refundableList",JSONObject.toJSON(refundableList));
		LogUtil.info(log, new LogBody(authInfo).set(logJosn));
		selfRefundRecord.setState(1);//更新为处理中状态
		selfRefundRecord.setUpdateTime(DateOper.getNowDateTime());
		selfRefundRecordMapper.updateByPrimaryKey(selfRefundRecord);
		for(RespMap respMap : refundableList) {
			
			Integer orderPrice = respMap.getInteger(ApiKey.QueryRefundableOrderListResp.Price);
			String orderId = respMap.getString(ApiKey.QueryRefundableOrderListResp.OrderId);
			String transactionNo = respMap.getString(ApiKey.QueryRefundableOrderListResp.TransactionNo);
			String channelId = respMap.getString(ApiKey.QueryRefundableOrderListResp.ChannelId);
			String configKey = respMap.getString(ApiKey.QueryRefundableOrderListResp.ConfigKey);
			leftBalance = leftBalance-orderPrice.intValue();
			int refunPrice = 0;
			if( leftBalance>=0 ) {//如果还有余额，说明这笔订单可以整退
				refunPrice = orderPrice.intValue();
			}else {//如果没有余额，说明这笔订单部分退
				refunPrice = orderPrice.intValue()+leftBalance;
			}
			String freezeRespCode = null;
			String refundRespCode = null;
			String writeOffRespCode= null;
			String failReason = null;//失败原因
			try {
				//冻结HIS
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("orderId", orderId);
				paramMap.put("cardNo", cardNo);
				paramMap.put("cardType", cardType);
				paramMap.put("hisMemberId", hisMemberId);
				paramMap.put("transNo", transactionNo);
				paramMap.put("price", refunPrice+"");
				paramMap.put("operatorId", selfRefundRecord.getOperatorId());
				paramMap.put("operatorName", selfRefundRecord.getOperatorName());
				paramMap.put("channelId", channelId);
				paramMap.put("configKey", configKey);
				CommonResp<RespMap> freezeHisResp = HandlerBuilder.get().getCallHisService(authInfo).oPDRechargeRefundFreeze(msg, paramMap);
				String refundOrderId = null;
	//			String refundOrderId = CommonUtil.getUUID();//测试用
				freezeRespCode = freezeHisResp.getCode();
				if(RetCode.Success.RET_10000.getCode().toString().equals(freezeHisResp.getCode())) {//冻结成功，则执行退费
					RespMap freezeHisRespMap = freezeHisResp.getResultData();
					String outRefundOrderId = null;
					if( freezeHisRespMap!=null ) {
						outRefundOrderId = freezeHisRespMap.getString(ApiKey.oPDRechargeRefundFreezeResp.OutRefundOrderId);
					} 
					CommonResp<RespMap> refundResp = null;
					try {//全额退
						ReqOrderIsCancel reqOrderIsCancel = new ReqOrderIsCancel(msg, orderId, orderPrice,
								refunPrice, selfRefundRecord.getOperatorId(), transactionNo, 
								authInfo.getClientId(), "用户自助退费！",outRefundOrderId);
						refundResp = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(reqOrderIsCancel));
						refundRespCode = refundResp.getCode();
//						refundResp =  new CommonResp<RespMap>(new CommonReq<ReqOrderIsCancel>(reqOrderIsCancel), KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
					} catch (Exception e) {
						e.printStackTrace();
						//LogUtil.error(log,e);
						LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费异常-全流程退款异常").set("ThreadName", this.getName())
								.set(e));
						//退费抛异常，告警开发
						//TODO
					}
					if(refundResp!=null && RetCode.Success.RET_10000.getCode().toString().equals(refundResp.getCode())) {
						refundOrderId = refundResp.getResultData().getString(ApiKey.OrderIsCancel.RefundOrderId);
						successRefundPrice+=refunPrice;
						successRefundCount++;
						//退款成功，核销HIS
						try {
							paramMap.put("Type", KstHosConstant.STRING_1);
							paramMap.put("refundOrderId",refundOrderId );
							paramMap.put("orderId",orderId );
							CommonResp<RespMap> writeOffHisResp = HandlerBuilder.get().getCallHisService(authInfo).oPDRechargeRefundWriteOff(msg, paramMap);
							writeOffRespCode = writeOffHisResp.getCode();
							if(!writeOffHisResp.getCode().equals(RetCode.Success.RET_10000.getCode().toString())) {
								//告警开发
								LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费异常-退款成功，核销失败！").set("ThreadName", this.getName())
										.set("hisInParam", paramMap));
							}
						} catch (Exception e) {
							e.printStackTrace();
							//LogUtil.error(log,e);
							LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费异常--退款成功，核销HIS异常").set("ThreadName", this.getName())
									.set("hisInParam", paramMap).set(e));
							//核销抛异常，告警开发
							//TODO
							//捕获异常告警下，再抛到上一层，防止执行后面
							//throw e;
						}
					}else if( refundResp!=null && RetCode.Pay.ERROR_REFUND.getCode().toString().equals(refundResp.getCode())) {
						LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费-退费失败").set("ThreadName", this.getName())
								.set("orderId", orderId).set("refunPrice",refunPrice).set("respCode",refundResp!=null?refundResp.getCode():"null")
								.set("respMsg", refundResp!=null?refundResp.getMessage():"null"));
						//如果明确商户退费失败，则查询退款状态
						refundOrderId = refundResp.getResultData().getString(ApiKey.OrderIsCancel.RefundOrderId);
						ReqQueryOrderPayState reqQueryOrderPayState = new ReqQueryOrderPayState(msg, null, refundOrderId, null);
						CommonResp<RespMap> queryOrderStateResp = orderService.queryOrderState(new CommonReq<ReqQueryOrderPayState>(reqQueryOrderPayState));
						if(queryOrderStateResp!=null&& RetCode.Success.RET_10000.getCode().toString().equals(queryOrderStateResp.getCode()) ) {
							RespMap queryOrderStateRespMap =queryOrderStateResp.getResultData(); 
							Integer refundState = queryOrderStateRespMap.getInteger(ApiKey.QueryOrderState.OrderState);
							if( KstHosConstant.ORDERPAY_7.intValue() == refundState.intValue()) {
								//明确退款失败，则调用HIS退款解冻
								failReason = "商户退款失败！";
								paramMap.put("Type", KstHosConstant.STRING_2);
								CommonResp<RespMap> writeOffHisResp = HandlerBuilder.get().getCallHisService(authInfo).oPDRechargeRefundWriteOff(msg, paramMap);
								LogUtil.info(log, new LogBody(authInfo).set("apiName", "用户自助退费失败，HIS解冻").set("ThreadName", this.getName())
										.set("orderId", orderId).set("refunPrice", refunPrice)
										.set("respCode", writeOffHisResp.getCode()).set("respMsg",writeOffHisResp.getMessage()));		
							}else if( KstHosConstant.ORDERPAY_4.intValue() == refundState.intValue()) {
								//退款成功.核销HIS
								paramMap.put("Type", KstHosConstant.STRING_1);
								paramMap.put("refundOrderId",refundOrderId );
								CommonResp<RespMap> writeOffHisResp = HandlerBuilder.get().getCallHisService(authInfo).oPDRechargeRefundWriteOff(msg, paramMap);
								writeOffRespCode = writeOffHisResp.getCode();
								if(!writeOffHisResp.getCode().equals(RetCode.Success.RET_10000.getCode().toString())) {
									//告警开发
									LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费异常-退款成功，核销失败！").set("ThreadName", this.getName())
											.set("hisInParam", paramMap));
								}
							}else {
								LogUtil.warn(log, new LogBody(authInfo).set("apiName","用户自助退费-退费失败-查询商户订单异常").set("ThreadName", this.getName())
										.set("orderId", orderId).set("refunPrice", refunPrice)
										.set("respCode", queryOrderStateResp.getCode()).set("respMsg",queryOrderStateResp.getMessage())
										.set("OrderState",refundState.intValue()));		
							}	
						}else {
							LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费-退费失败-查询商户订单异常").set("ThreadName", this.getName())
									.set("message",queryOrderStateResp!=null?queryOrderStateResp.getMessage():"null")
									.set("retCode",queryOrderStateResp!=null?queryOrderStateResp.getRetCode().toString():"null"));
						}
					}else {
						//TODO 其他异常情况待补充
						failReason = "商户退款异常！";
						LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费-退费失败-其他异常").set("ThreadName", this.getName())
								.set("message",refundResp!=null?refundResp.getMessage():"null")
								.set("retCode",refundResp!=null?refundResp.getRetCode().toString():"null"));
						if( refundResp!=null && refundResp.getResultData()!=null) {
							refundOrderId = refundResp.getResultData().getString(ApiKey.OrderIsCancel.RefundOrderId);
						}
					}
				}else {
					failReason = "冻结失败！";
					LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费-冻结HIS失败").set("ThreadName", this.getName())
							.set("orderId", orderId).set("selfRefundRecordId",selfRefundRecord.getId()).set("refundOrderId",refundOrderId));
				}
				try {
					if( !StringUtil.isEmpty(refundOrderId)) {
						SelfRefundRecordOrder selfRefundRecordOrder = new SelfRefundRecordOrder();
						selfRefundRecordOrder.setRefundOrderId(refundOrderId);
						selfRefundRecordOrder.setRemark(failReason);
						Example example = new Example(SelfRefundRecordOrder.class);
						example.createCriteria().andEqualTo("orderId", orderId).andEqualTo("selfRefundRecordId",selfRefundRecord.getId());
						selfRefundRecordOrderMapper.updateByExampleSelective(selfRefundRecordOrder, example);
					}
				} catch (Exception e) {
					LogUtil.error(log,e);
					LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费-保存退费记录异常").set("ThreadName", this.getName())
							.set("orderId", orderId).set("selfRefundRecordId",selfRefundRecord.getId()).set("refundOrderId",refundOrderId));
				}
			} catch (Exception e) {
				e.printStackTrace();
				//LogUtil.error(log,e);
				LogUtil.warn(log, new LogBody(authInfo).set("apiName", "用户自助退费异常").set("ThreadName", this.getName())
						.set("orderId", orderId).set("refunPrice", refunPrice)
						.set("orderPrice", orderPrice).set("freezeRespCode",freezeRespCode)
						.set("refundRespCode", refundRespCode).set("writeOffRespCode", writeOffRespCode)
						.set("leftBalance", leftBalance).set(e));
			}
			if( leftBalance<=0) {//最后一笔退出循环
				break;
			}
		}
		selfRefundRecord.setRefundAmount(successRefundPrice);
		selfRefundRecord.setRefundCount(successRefundCount);
		if( successRefundPrice==selfRefundRecord.getRefundableBalance().intValue()) {
			//全部退款成功
			selfRefundRecord.setState(2);
		}else if( successRefundPrice==0) {
			selfRefundRecord.setState(4);
			//全部退款失败
		}else if(successRefundPrice>0&& successRefundPrice<selfRefundRecord.getRefundableBalance().intValue()) {
			//部分退款失败
			selfRefundRecord.setState(3);
		}
		selfRefundRecord.setUpdateTime(DateOper.getNowDateTime());
		selfRefundRecordMapper.updateByPrimaryKey(selfRefundRecord);
	}
	
	
	

}
