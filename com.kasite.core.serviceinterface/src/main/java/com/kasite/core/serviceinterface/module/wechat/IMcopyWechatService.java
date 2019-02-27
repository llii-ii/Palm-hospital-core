package com.kasite.core.serviceinterface.module.wechat;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqExpressOrder;
import com.kasite.core.serviceinterface.module.wechat.req.ReqMcopyWechat;

public interface IMcopyWechatService {
	/**
	 * 获取微信票据，通过wxKey获取accessToken后发送
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> getWechatTicket(CommonReq<ReqMcopyWechat> commReq) throws Exception;
	
	
	/**
	 * 获取微信临时素材
	 * @Description: 
	 * @param commReq
	 * @param url
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> getWechatMedia(CommonReq<ReqMcopyWechat> commReq,String url) throws Exception;
	
	
	/**
	 * 微信消息回调
	 * @Description: 
	 * @param commReq
	 * @param url
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sendMessage(CommonReq<ReqExpressOrder> commReq) throws Exception;
}
