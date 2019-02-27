package com.kasite.core.serviceinterface.module.order;

import com.kasite.core.common.constant.RetCode.BizDealState;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.resp.RespPayEndBizOrderExecute;
import com.kasite.core.serviceinterface.module.pay.dbo.MerchantNotify;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 订单回调接口类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:18:34
 */
public interface OrderCallbackService {

	/**
	 * 订单的回调处理标准逻辑
	 * @param msg
	 * @param orderId
	 * @param payNotifyJson
	 */
	public void orderCallback(InterfaceMessage msg,MerchantNotify merchantNotify) throws Exception;
	
	
	/**
	 * 重试失败告警接方法，已实现APM告警。 可自己重写接口实现（短信，邮箱，或者微信公众号推送消息）
	 * 
	 * @param orderCallback
	 */
	public void failWarn(InterfaceMessage msg,MerchantNotify merchantNotify);

	/**
	 * 新增:诊间支付时-需校验HIS订单金额与支付交易金额是否一致
	 * 
	 * @param payContent
	 * @param orderCallback
	 * @param orderView
	 * @param orderExtension
	 * @return
	 * @throws Exception 
	 */
	public RespPayEndBizOrderExecute dealBiz(InterfaceMessage msg,MerchantNotify merchantNotify, OrderView orderView) throws Exception;

	/**
	 * 订单校验，充值失败重试前需要先验证是否真的失败
	 * 
	 * @param payContent
	 * @param orderView
	 * @return
	 * @throws Exception 
	 */
	public BizDealState checkHisOrderStatus(InterfaceMessage msg,MerchantNotify merchantNotify,OrderView orderView);
	
	
}
