package com.kasite.client.core.msg.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.client.core.msg.bean.dbo.MsgPush;
import com.kasite.client.core.msg.dao.IMsgPushMapper;
import com.kasite.client.core.msg.dao.IMsgTempMapper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.MsgTempTypeEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.serviceinterface.module.alipay.IAliPayService;
import com.kasite.core.serviceinterface.module.msg.MsgCommonService;
import com.kasite.core.serviceinterface.module.msg.req.ReqMaintenanceMsg;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.wechat.IWeiXinService;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendCustomMessage;
import com.kasite.core.serviceinterface.module.wechat.req.ReqSendTemplateMessage;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
import com.yihu.wsgw.api.UTF8PostMethod;

/**
 * 消息服务抽象类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:10:50
 */
public abstract class AbstractMsgCommonService implements MsgCommonService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MSG);

	@Autowired
	IMsgPushMapper msgPushMapper;
	@Autowired
	IMsgTempMapper msgTempMapper;
	@Autowired
	IWeiXinService weiXinService;
	@Autowired
	IAliPayService aliPayService;

	/**
	 * 消息推送 通过该接口推送各类消息
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String sendMsg(InterfaceMessage msg) throws Exception {
		ReqSendMsg reqSendMsg = new ReqSendMsg(msg);
		CommonReq<ReqSendMsg> req = new CommonReq<ReqSendMsg>(reqSendMsg);
		CommonResp<RespMap> resp = this.sendMsg(req);
		return CommonUtil.getRetVal(msg, reqSendMsg.getTransactionCode(), RetCode.Success.RET_10000, resp);
	}

	/**
	 * 运维告警推送
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public CommonResp<RespMap> sendMaintenancenMsg(CommonReq<ReqMaintenanceMsg> reqParam) throws Exception {
		ReqMaintenanceMsg maintenanceMsg = reqParam.getParam();
		final String licenseKey = KasiteConfig.getLicenseKey();
		final String serverIp = "";
		final String appId = maintenanceMsg.getAppId();
		final String title = maintenanceMsg.getTitle();
		final String color = maintenanceMsg.getColor();
		final String level = maintenanceMsg.getLevel();
		
		final String remark = "医院ID:" + maintenanceMsg.getHosId() + "." + maintenanceMsg.getRemark();
		final String ip = maintenanceMsg.getIp();
		final String appInfo = maintenanceMsg.getIp();
		final String url = maintenanceMsg.getIp();
		final String param = maintenanceMsg.getMsg().getParam();
		final AuthInfoVo info = maintenanceMsg.getAuthInfo();
		//LogUtil.info(log,"发送告警消息："+JSON.toJSONString(maintenanceMsg));
		
		
//		KstHosConstant.cachedThreadPool.execute(new Runnable() {
//			@Override
//			public void run() {
//				PostMethod postMethod = null;
//				HttpClient httpClient = null;
//				postMethod = new UTF8PostMethod(serverIp);
//				postMethod.addParameter("appId", appId);
//				postMethod.addParameter("title", title);
//				postMethod.addParameter("color", color);
//				postMethod.addParameter("level", level);
//				postMethod.addParameter("remark", remark);
//				postMethod.addParameter("ip", ip);
//				postMethod.addParameter("licenseKey", licenseKey);
//				if (!StringUtil.isBlank(appInfo)) {
//					postMethod.addParameter("appInfo", appInfo);
//				}
//				if (!StringUtil.isBlank(url)) {
//					postMethod.addParameter("url", url);
//				}
//				httpClient = new HttpClient();
//				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(KstHosConstant.CONNECTION_TIMEOUT);
//				httpClient.getHttpConnectionManager().getParams().setSoTimeout(KstHosConstant.SO_TIMEOUT);
//				int statusCode;
//				try {
//					statusCode = httpClient.executeMethod(postMethod);
//					if (statusCode == KstHosConstant.STATUS_CODE) {
//						String response = postMethod.getResponseBodyAsString();
//						JSONObject retJson = JSONObject.parseObject(response);
//						if (retJson.containsKey("Code") && retJson.getIntValue("Code")==KstHosConstant.SUCCESS_CODE) {
//							LogUtil.warn(log, new LogBody(info).set("info", "推送运维告警成功！").set("response", response).set("param", param));
//						} else {
//							LogUtil.warn(log, new LogBody(info).set("errorInfo", "推送运维告警失败！").set("response", response).set("param", param));
//						}
//					} else {
//						LogUtil.warn(log, new LogBody(info).set("errorInfo", "推送运维告警失败！").set("httpCode", statusCode).set("param", param));
//					}
//				} catch (HttpException e1) {
//					e1.printStackTrace();
//					LogUtil.error(log, e1, info);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//					LogUtil.error(log, e1, info);
//				}
//			}
//		});
		return new CommonResp<RespMap>(reqParam, "", RetCode.Success.RET_10000, RetCode.Success.RET_10000.getMessage());
	}

	/**
	 * 运维告警推送
	 * 
	 * @param msg
	 * @return
	 * @throws AbsHosException
	 * @throws BusinessException
	 */
	@Override
	public String SendMaintenancenMsg(InterfaceMessage msg) throws Exception {
		return this.sendMaintenancenMsg(new CommonReq<ReqMaintenanceMsg>(new ReqMaintenanceMsg(msg))).toResult();
	}

	
	/**
	 * 发送第三方消息，微信、支付宝等
	 * @Description: 
	 * @param msgPush
	 * @throws Exception 
	 */
	protected void sendMsg(CommonReq<ReqSendMsg> reqParam,MsgPush msgPush,ChannelTypeEnum channelType) throws Exception {
		String msgContent = msgPush.getMsgContent();
		if (StringUtils.isNotBlank(msgContent) 
				&& (msgContent.contains("\"\\{") 
						|| msgContent.contains("}\""))) {
			msgContent = msgContent.replaceAll("\"\\{", "{");
			msgContent = msgContent.replaceAll("}\"", "}");
		}
		// 消息类型。 1：客服消息推送；2：群发消息推送；3：模板消息推送；
		String msgType = msgPush.getMsgType();
		// 微信消息接口的全JSON参数
		String parmContent = msgContent;
		// 渠道类型。 1：微信；2：支付宝
		if (StringUtils.isNotBlank(msgType) && StringUtils.isNotBlank(parmContent) && channelType != null) {
			// 发送微信消息
			if (ChannelTypeEnum.wechat.equals(channelType)) {
				CommonResp<RespMap> commResp = null;
				if (MsgTempTypeEnum.CustomMessage.getMsgType().equals(msgType)) {
					// 1：客服消息推送
					ReqSendCustomMessage req = new ReqSendCustomMessage(reqParam.getMsg(), reqParam.getParam().getConfigKey(), parmContent);
					commResp = weiXinService.sendCustomMessage(new CommonReq<ReqSendCustomMessage>(req));
				} else if (MsgTempTypeEnum.TemplateMessage.getMsgType().equals(msgType)) {
					// 3：模板消息推送
					ReqSendTemplateMessage req = new ReqSendTemplateMessage(reqParam.getMsg(), reqParam.getParam().getConfigKey(), parmContent);
					commResp = weiXinService.sendTemplateMessage(new CommonReq<ReqSendTemplateMessage>(req));
				}
				KasiteConfig.print("----reusltObj-->" + commResp.toResult());
				if (commResp == null || !KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
					LogUtil.info(log, "发送微信消息异常："+commResp.toResult(), reqParam.getParam().getAuthInfo());
					throw new RRException("微信消息发送异常："+commResp.toResult());
				}
			} else if (ChannelTypeEnum.zfb.equals(channelType)) {
				CommonResp<RespMap> commResp = null;
				if (MsgTempTypeEnum.CustomMessage.getMsgType().equals(msgType)) {
					// 1：客服消息推送
					com.kasite.core.serviceinterface.module.alipay.req.ReqSendCustomMessage req = new com.kasite.core.serviceinterface.module.alipay.req.ReqSendCustomMessage(reqParam.getMsg(), reqParam.getParam().getConfigKey(), parmContent);
					commResp = aliPayService.sendCustomMessage(new CommonReq<com.kasite.core.serviceinterface.module.alipay.req.ReqSendCustomMessage>(req));
				} else if (MsgTempTypeEnum.TemplateMessage.getMsgType().equals(msgType)) {
					// 3：模板消息推送
					com.kasite.core.serviceinterface.module.alipay.req.ReqSendTemplateMessage req = new com.kasite.core.serviceinterface.module.alipay.req.ReqSendTemplateMessage(reqParam.getMsg(), reqParam.getParam().getConfigKey(), parmContent);
					commResp = aliPayService.sendTemplateMessage(new CommonReq<com.kasite.core.serviceinterface.module.alipay.req.ReqSendTemplateMessage>(req));
				}
				KasiteConfig.print("----reusltObj-->" + commResp.toResult());
				if (commResp == null || !KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
					LogUtil.info(log, "发送支付宝消息异常："+commResp.toResult(), reqParam.getParam().getAuthInfo());
					throw new RRException("支付宝消息发送异常："+commResp.toResult());
				}
			}
		}
	}
	/**
	 * 发送短信消息
	 * @Description: 
	 * @param msgPush
	 * @throws Exception
	 */
//	protected void sendSms(AuthInfoVo authVo,MsgPush msgPush) throws Exception{
//		//直接发送短信消息,失败时间隔500ms重试2次
//			JSONObject paramJson = new JSONObject();
//			paramJson.put("mobile", msgPush.getMobile());
//			paramJson.put("content", msgPush.getMsgContent());
//			paramJson.put("handlerId", KstHosConstant.JKZL_SMS_HANDLERID);
//			String url = null;
//			if(KasiteConfig.getDebug()) {
//				url = KstHosConstant.JKZL_INTERFACE_URL_DEBUG;
//			}else {
//				url = KstHosConstant.JKZL_INTERFACE_URL;
//			}
//			
//			String resp = WsClient.getWsClient(KstHosConstant.JKZL_KASITE_CLIENTID, url)
//					.call(ApiModule.JKZL.sendSms.getName(), paramJson.toString());
//			KasiteConfig.print(resp);
//			com.alibaba.fastjson.JSONObject respJs = com.alibaba.fastjson.JSONObject.parseObject(resp);
//			if(respJs.getIntValue(KstHosConstant.RESPCODE)==10000) {
//				//发送成功，结束循环
//				break;
//			}else {
//				//发送短信失败，间隔500ms重试
//				Thread.sleep(500);
//			}
//		}
//	}
}
