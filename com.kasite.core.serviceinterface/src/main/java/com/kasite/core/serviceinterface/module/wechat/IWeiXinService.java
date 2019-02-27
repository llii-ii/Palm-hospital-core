package com.kasite.core.serviceinterface.module.wechat;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.wechat.req.ReqGetShortUrl;
import com.kasite.core.serviceinterface.module.wechat.req.ReqQueryAllTemplateList;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendCustomMessage;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendTemplateMessage;

import net.sf.json.JSONObject;

/**
 * @author cyh
 * @version 1.0
 * 2017-11-17 下午2:29:38
 * */
public interface IWeiXinService  {

//	/**
//	 * 获取微信用户分组
//	 * @param msg
//	 * @return String
//	 */
//	public String getGroups(InterfaceMessage msg);
//	/**
//	 * 获取微信用户列表
//	 * @param msg
//	 * @return String
//	 */
//	public String getUserList(InterfaceMessage msg);
//	/**
//	 * 批量获取微信用户列表
//	 * @param msg
//	 * @return String
//	 */
//	public String batchGetUserInfo(InterfaceMessage msg);
	
	CommonResp<RespMap> getWechatShorturl(CommonReq<ReqGetShortUrl> commReq) throws Exception;
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
	/**
	 * 获取模板消息列表
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> QueryWxTemplateList(CommonReq<ReqQueryAllTemplateList> commReq) throws Exception;
	
	
}
