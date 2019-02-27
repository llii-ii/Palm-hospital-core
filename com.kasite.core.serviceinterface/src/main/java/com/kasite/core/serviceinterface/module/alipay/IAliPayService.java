package com.kasite.core.serviceinterface.module.alipay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.alipay.req.ReqSendCustomMessage;
import com.kasite.core.serviceinterface.module.alipay.req.ReqSendTemplateMessage;

/**
 * 
 * @className: IAliPayService
 * @author: lcz
 * @date: 2018年8月3日 上午11:29:49
 */
public interface IAliPayService {
	/**
	 * 发送客服消息
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sendCustomMessage(CommonReq<ReqSendCustomMessage> commReq) throws Exception;
	/**
	 * 发送模板消息
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sendTemplateMessage(CommonReq<ReqSendTemplateMessage> commReq) throws Exception;
}
