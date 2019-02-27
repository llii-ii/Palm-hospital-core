package com.kasite.client.zfb.service;

import org.springframework.stereotype.Service;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.alipay.IAliPayService;
import com.kasite.core.serviceinterface.module.alipay.req.ReqSendCustomMessage;
import com.kasite.core.serviceinterface.module.alipay.req.ReqSendTemplateMessage;

import net.sf.json.JSONObject;


/**
 * 
 * @className: AliPayServiceImpl
 * @author: lcz
 * @date: 2018年8月3日 上午11:37:38
 */
@Service
public class AliPayServiceImpl implements IAliPayService{

	@Override
	public CommonResp<RespMap> sendCustomMessage(CommonReq<ReqSendCustomMessage> commReq) throws Exception {
		ReqSendCustomMessage req = commReq.getParam();
		JSONObject resJs = AlipayService.sendPublicCustomMessage(req.getZfbKey(), req.getContent());
		if(resJs!=null && resJs.containsKey("code") && "10000".equals(resJs.getString("code"))) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN,"发送客服消息失败："+(resJs!=null?resJs.toString():""));
	}

	@Override
	public CommonResp<RespMap> sendTemplateMessage(CommonReq<ReqSendTemplateMessage> commReq) throws Exception {
		ReqSendTemplateMessage req = commReq.getParam();
		JSONObject resJs = AlipayService.sendSingleMessage(req.getZfbKey(), req.getContent());
		if(resJs!=null && resJs.containsKey("code") && "10000".equals(resJs.getString("code"))) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_UNKNOWN,"发送客服消息失败："+(resJs!=null?resJs.toString():""));
	}

}
