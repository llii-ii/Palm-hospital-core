package com.kasite.client.pay.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.kasite.client.pay.bean.dbo.MerchantOrderCheck;
import com.kasite.client.pay.dao.IMerchantNotifyMapper;
import com.kasite.client.pay.dao.IMerchantOrderCheckMapper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 支付或退费回调帮助类
 * 通过HTTP请求调用业务实例
 * 
 * @className: PayBackCallUtil
 * @author: lcz
 * @date: 2018年7月5日 下午9:06:44
 */
@Component
public class PayBackCallUtil {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_DEFAULT);
	
	@Autowired
	IMerchantNotifyMapper merchantNotifyMapper;
	
	@Autowired
	IMerchantOrderCheckMapper merchantOrderCheckMapper;
	
	/**
	 * 新增退款异步通知
	 * @param orderId
	 * @param refundOrderId
	 * @param refundId
	 * @param clientId
	 */
	public void addRefundNotify(InterfaceMessage msg,String orderId,String refundOrderId,String refundId,String clientId,Integer totalPrice,Integer refundPrice,String configKey){
		// 如果需要回调，则将回调信息写入P_MERCHANT_NOTIFY表 
		MerchantNotify merchantNotify = new MerchantNotify();
		merchantNotify.setOrderId(orderId);
		merchantNotify.setOrderType(2);
		merchantNotify.setClientId(clientId);
		merchantNotify.setCreateTime(DateOper.getNowDateTime());
		merchantNotify.setRefundNo(refundId);
		merchantNotify.setRefundOrderId(refundOrderId);
		merchantNotify.setRetryNum(0);
		merchantNotify.setState(-1);
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotify.setConfigKey(configKey);
		merchantNotify.setPrice(totalPrice);
		merchantNotify.setRefundPrice(refundPrice);
		int refundRet = merchantNotifyMapper.insertIgnore(merchantNotify);
		if( refundRet==1) {
			MerchantNotify queryParam = new MerchantNotify();
			queryParam.setOrderId(orderId);
			queryParam.setOrderType(2);
			queryParam.setRefundOrderId(refundOrderId);
			merchantNotify = merchantNotifyMapper.selectOne(queryParam);
			if( refundRet== 0 && merchantNotify!=null ) {
				refundRet = 1;
			}
			LogUtil.info(log, new LogBody(KasiteConfig.getAuthInfo(msg)).set("msg", "退费成功-写入notify表")
					.set("orderId", orderId).set("result", refundRet));
			MerchantNotifyExcutor merchantNotifyExcutor = new MerchantNotifyExcutor(msg,merchantNotify,merchantNotifyMapper);
			merchantNotifyExcutor.setName("pay-notify-thread-orderId="+merchantNotify.getOrderId());
			KstHosConstant.cachedThreadPool.execute(merchantNotifyExcutor);
		}else {
			LogUtil.info(log, new LogBody(KasiteConfig.getAuthInfo(msg)).set("msg", "退费成功-写入notify表")
					.set("orderId", orderId).set("result", refundRet));
		}
	}
	
	/**
	 * 新增支付异步通知
	 * @param orderId
	 * @param transactionId
	 * @param clientId
	 * @throws Exception
	 */
	public void addPayNotify(InterfaceMessage msg,String orderId,String transactionId,String clientId,Integer price,String configKey) throws Exception{
		MerchantNotify merchantNotify = new MerchantNotify();
		merchantNotify.setOrderId(orderId);
		merchantNotify.setOrderType(1);
		merchantNotify.setClientId(clientId);
		merchantNotify.setConfigKey(configKey);
		merchantNotify.setCreateTime(DateOper.getNowDateTime());
		merchantNotify.setRetryNum(0);
		merchantNotify.setState(-1);
		merchantNotify.setTransactionNo(transactionId);
		merchantNotify.setPrice(price);
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotify.setRefundOrderId(KstHosConstant.STRING_0);
		int addRet = merchantNotifyMapper.insertIgnore(merchantNotify);
		AuthInfoVo vo = KasiteConfig.getAuthInfo(msg);
		if( addRet == 1 ) {
			//第一次通知，启动线程执行执行商户通知
			MerchantNotify queryParam = new MerchantNotify();
			queryParam.setOrderId(orderId);
			queryParam.setOrderType(1);
			queryParam.setRefundOrderId(KstHosConstant.STRING_0);
			merchantNotify = merchantNotifyMapper.selectOne(queryParam);
			LogUtil.info(log, new LogBody(vo).set("msg", "支付成功-写入notify表").set("orderId", orderId).set("result", addRet)
					.set("clientId", clientId));
			MerchantNotifyExcutor merchantNotifyExcutor = new MerchantNotifyExcutor(msg,merchantNotify,merchantNotifyMapper);
			merchantNotifyExcutor.setName("refund-notify-thread-refundOrderId="+merchantNotify.getRefundOrderId());
			KstHosConstant.cachedThreadPool.execute(merchantNotifyExcutor);
		}else {
			//第二次通知，不做任何操作，第一次执行商户通知，有失败重试机制
			LogUtil.info(log, new LogBody(vo).set("msg", "支付成功-写入notify表").set("orderId", orderId).set("result", addRet)
					.set("clientId", clientId));
		}
	}
	

	/**
	 *  新增支付异步通知
	 * @param msg
	 * @param orderId
	 * @param transactionId
	 * @param clientId
	 * @param price
	 * @param configKey
	 * @param accountNo
	 * @return
	 * @throws Exception
	 */
	public void addPayNotify(InterfaceMessage msg,String orderId,String transactionId,String clientId,Integer price,String configKey,String accNo) throws Exception{ //判断是否已经通知过
		MerchantNotify merchantNotify = new MerchantNotify();
		merchantNotify.setOrderId(orderId);
		merchantNotify.setOrderType(1);
		merchantNotify.setClientId(clientId);
		merchantNotify.setConfigKey(configKey);
		merchantNotify.setCreateTime(DateOper.getNowDateTime());
		merchantNotify.setRetryNum(0);
		merchantNotify.setState(-1);
		merchantNotify.setTransactionNo(transactionId);
		merchantNotify.setPrice(price);
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotify.setAccNo(accNo);
		merchantNotify.setRefundOrderId(KstHosConstant.STRING_0);
		int addRet = merchantNotifyMapper.insertIgnore(merchantNotify);
		AuthInfoVo vo = KasiteConfig.getAuthInfo(msg);
		if( addRet == 1 ) {
			//第一次通知，启动线程执行执行商户通知
			MerchantNotify queryParam = new MerchantNotify();
			queryParam.setOrderId(orderId);
			queryParam.setOrderType(1);
			queryParam.setRefundOrderId(KstHosConstant.STRING_0);
			merchantNotify = merchantNotifyMapper.selectOne(queryParam);
			LogUtil.info(log, new LogBody(vo).set("msg", "支付成功-写入notify表").set("orderId", orderId).set("result", addRet));
			MerchantNotifyExcutor merchantNotifyExcutor = new MerchantNotifyExcutor(msg,merchantNotify,merchantNotifyMapper);
			merchantNotifyExcutor.setName("refund-notify-thread-refundOrderId="+merchantNotify.getRefundOrderId());
			KstHosConstant.cachedThreadPool.execute(merchantNotifyExcutor);
		}else {
			//第二次通知，不做任何操作，第一次执行商户通知，有失败重试机制
			LogUtil.info(log, new LogBody(vo).set("msg", "支付成功-写入notify表").set("orderId", orderId).set("result", addRet));
		}
	}
	
	public void payNotifyForceRetry(InterfaceMessage msg,String orderId) throws Exception{
		//判断是否已经通知过
		MerchantNotify queryParam = new MerchantNotify();
		queryParam.setOrderId(orderId);
		queryParam.setOrderType(1);
		queryParam.setRefundOrderId(KstHosConstant.STRING_0);
		List<MerchantNotify> list = merchantNotifyMapper.select(queryParam);
		if( CollectionUtils.isEmpty(list)) {
			//如果不存在记录，则直接返回
			throw new RRException(RetCode.Pay.ERROR_MERCHANTORDER);
		}
		MerchantNotify merchantNotify = list.get(0);
//		if( merchantNotify.getState().intValue() != -1 && merchantNotify.getRetryNum().intValue()<3) {
//			//只有重试过3次，还是执行HIS业务失败的，才能进行重试,,
//			throw new RRException(RetCode.Pay.ERROR_TRADESTATE);
//		}
		//重置重试次数
		merchantNotify.setRetryNum(0);
		merchantNotify.setState(-1);
		merchantNotify.setUpdateTime(DateOper.getNowDateTime());
		merchantNotifyMapper.updateByPrimaryKey(merchantNotify);
		MerchantNotifyExcutor merchantNotifyExcutor = new MerchantNotifyExcutor(msg,merchantNotify,merchantNotifyMapper);
		merchantNotifyExcutor.setName("pay-notify-forceRetry-thread-orderId="+merchantNotify.getOrderId());
		KstHosConstant.cachedThreadPool.execute(merchantNotifyExcutor);
	}
	
	/**
	 * 新增商户支付订单确认记录，由pay的job循环此记录，核实商户的支付情况
	 * @param orderId
	 * @param transactionNo
	 * @param times
	 * @param configKey
	 */
	public void addMerchantOrderCheck(AuthInfoVo vo,String orderId,String transactionNo,
			int times,String configKey,String clientId) {
		MerchantNotify queryParam = new MerchantNotify();
		queryParam.setOrderId(orderId);
		queryParam.setOrderType(1);
		queryParam.setRefundOrderId(KstHosConstant.STRING_0);
		int count = merchantNotifyMapper.selectCount(queryParam);
		if( count == 0 ) {//已经明确收到商户的支付回调的，无需再做确认。未收到商户的支付回调的，做确认处理。
			MerchantOrderCheck merchantOrderCheck = new MerchantOrderCheck();
			merchantOrderCheck.setConfigKey(configKey);
			merchantOrderCheck.setOrderId(orderId);
			merchantOrderCheck.setTimes(times);
			merchantOrderCheck.setClientId(clientId);
			merchantOrderCheck.setTransactionNo(transactionNo);
			int addRet = merchantOrderCheckMapper.insertSelective(merchantOrderCheck);
			LogUtil.info(log, new LogBody(vo).set("msg", "支付状态未知-写入商户订单确认表")
					.set("orderId", orderId).set("times", times).set("result", addRet));
		}
	}

}
