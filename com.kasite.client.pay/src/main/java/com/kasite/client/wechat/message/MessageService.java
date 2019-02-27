package com.kasite.client.wechat.message;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.DateOper;
import com.coreframework.util.JsonUtil;
import com.kasite.client.wechat.message.req.BaseMessage;
import com.kasite.client.wechat.message.req.EventScanMessage;
import com.kasite.client.wechat.message.req.LinkMessage;
import com.kasite.client.wechat.message.req.MapMessage;
import com.kasite.client.wechat.message.req.MassResultMessage;
import com.kasite.client.wechat.message.req.NewsMessage;
import com.kasite.client.wechat.message.req.SubscribeMessage;
import com.kasite.client.wechat.message.req.TextMessage;
import com.kasite.client.wechat.service.AbstractEventService;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.MsgListenerException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.serviceinterface.module.basic.IBatService;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

import static com.kasite.core.log.LogKey.configKey;

/**
 * 消息服务
 * 
 * @author Administrator
 * @param <T>
 */
public class MessageService extends AbstractEventService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MSG);
	private MessageContent mgsContent;
	private static String SEND_SUCCESS = "sendsuccess";
	private static String RESP_CODE = "-14014";
	private static String TYPE = "GZYS";
	private static String USE_TYOE = "WLZX";
	private static String NICK_NAME = "nickname";

	public MessageService() {
	}

	/**
	 * 微信事件消息处理路口
	 */
	@Override
	public String onMessage(MessageContent mgsContent) {
		this.mgsContent = mgsContent;
		try {
			return super.onMessage(mgsContent);
		} catch (MsgListenerException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
		// String api = ApiModule.msg.QueryAutoReplayArbitrarily.getName();
		// Document document = DocumentHelper.createDocument();
		// Element req = document.addElement(SysConstant.REQ);
		// XMLUtil.addElement(req, SysConstant.TRANSACTIONCODE, "15102");
		// Element data = req.addElement(SysConstant.DATA);
		// XMLUtil.addElement(data, "KeyWord", "55");
		// XMLUtil.addElement(data, "State", "1");
		// String params = document.asXML();
		// ResponseMessage resp = WGInterface.doPost(api,params);
		// String result = resp.getResult().toString();
		// ConfigUtil.print("result:"+result);
		// JSONObject resultJson=JSONObject.fromObject(result);
		// int respCode=resultJson.getInt("RespCode");
		// String replayMsg=null;
		// if(respCode==10000){
		// JSONObject dataObj = resultJson.getJSONObject("Data");
		// replayMsg=dataObj.getString("ReplayMsg");
		// if(StringUtil.isEmpty(replayMsg)){
		// replayMsg="抱歉,没查询到相关信息!";
		// }
		// }else{
		// replayMsg="抱歉,没查询到相关信息!";
		// }
		// ConfigUtil.print("replayMsg:"+replayMsg);

		// 调用云数据收集接口
//		String api = ApiModule.ReportForms.DataCloudCollection.getName();
//
//		Document document2 = DocumentHelper.createDocument();
//		Element req2 = document2.addElement(SysConstant.REQ);
//		XMLUtil.addElement(req2, SysConstant.TRANSACTIONCODE, "7006");
//		Element service2 = req2.addElement(SysConstant.DATA);
//		XMLUtil.addElement(service2, "ChannelId", SysConstant.WX_CHANNEL_ID);
//		XMLUtil.addElement(service2, "DataType", 101);
//		XMLUtil.addElement(service2, "DataCount", 1);
//		XMLUtil.addElement(service2, "TargetType", "1");
//
//		String params2 = document2.asXML();

//		ResponseMessage resp2 = WGInterface.doPost(api, params2);
//		String result2 = resp2.getResult().toString();
//		String result2 = CallApiUtils.callApi(api, params2);
	}

	/**
	 * 文本消息
	 * @param reqMsg
	 * @return
	 */
	@Override
	public String onMessage(TextMessage reqMsg) {
		com.kasite.client.wechat.message.resp.TextMessage textMessage = new com.kasite.client.wechat.message.resp.TextMessage();
		String fromUser = reqMsg.getFromUserName();
		String toUser = reqMsg.getToUserName();
		try {
			String configKey = reqMsg.getAuthInfo().getConfigKey();
			// 回复文本消息
			String api = ApiModule.Msg.QueryAutoReplayArbitrarily.getName();
			InterfaceMessage msg = new InterfaceMessage();
			msg.setApiName(api);
//			msg.setParam(document.asXML());
			msg.setParamType(KstHosConstant.INTYPE);
			msg.setOutType(KstHosConstant.OUTTYPE);
			msg.setAuthInfo(reqMsg.getAuthInfo().toString());
			msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
			msg.setVersion(KstHosConstant.V);
			IMsgService msgService = HandlerBuilder.get().getMsgService(reqMsg.getAuthInfo());
			if(null != msgService) {
				ReqQueryAutoReplayByFollow t = new ReqQueryAutoReplayByFollow(msg);
				String appId = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, configKey);
				t.setAppId(appId);
				t.setKeyWord(reqMsg.getContent());
				t.setReplayType(2);
				t.setState(1);
				CommonReq<ReqQueryAutoReplayByFollow> commReq = new CommonReq<ReqQueryAutoReplayByFollow>(t);
				CommonResp<RespQueryAutoReplayByFollow> result = msgService.queryAutoReplayByFollow(commReq);
				return messageHandle(result,fromUser,toUser,mgsContent);
			}else {
				LogUtil.info(log, "未定义微信消息关键字回复消息处理类，请实现接口："+IMsgService.class.getName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, "微信消息关键字回复异常",e);
		}
		return "success";
	}

	@Override
	public String onMessage(NewsMessage reqMsg) {

		KasiteConfig.print("this is NewsMessage...");
		com.kasite.client.wechat.message.resp.NewsMessage newsMessage = new com.kasite.client.wechat.message.resp.NewsMessage();
		try {
			// 图文信息
			newsMessage.setToUserName(reqMsg.getFromUserName());
			newsMessage.setFromUserName(reqMsg.getToUserName());
			newsMessage.setCreateTime(System.currentTimeMillis() + "");
			newsMessage.setMsgType(WeiXinConstant.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag("0");
			List<com.kasite.client.wechat.message.resp.Article> articleList = new ArrayList<com.kasite.client.wechat.message.resp.Article>();
			// 单图文消息
			com.kasite.client.wechat.message.resp.Article article = new com.kasite.client.wechat.message.resp.Article();
			article.setTitle("TEST健康之路全流程TEST");
			article.setDescription("帮助实现全渠道、全流程一站式服务。");
			article.setPicUrl("http://qlctest.yihu.com.cn/HosWiKi/business/common/image/aa.JPG");
			// article.setUrl("http://qlctest.yihu.com.cn/business/wxpay/pay.htm?showwxpaytitle=1");
			article.setUrl(
					"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxab004d908609ea0b&redirect_uri=http%3A%2F%2Fqlctest.yihu.com.cn%2FHosWiKi%2Fweixin_getCodeForPay.do?params%3D%7B%22productName%22%3A%22apple%22%2C%22productPrice%22%3A0.01%2C%22productRemark%22%3A%22remark%22%2C%22productID%22%3A1102221%2C%22productNO%22%3A2015143089223423%7D&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
			articleList.add(article);

			// 设置图文消息个数
			newsMessage.setArticleCount(articleList.size() + "");
			// 设置图文消息包含的图文集合
			newsMessage.setArticles(articleList);
			return mgsContent.make(newsMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String onMessage(MapMessage reqMsg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String onMessage(LinkMessage reqMsg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 订阅/取消订阅
	 * @param reqMsg
	 * @return
	 */
	@Override
	public String onMessage(SubscribeMessage reqMsg) {
		// TODO Auto-generated method stub
		int dateValue = 0;
		String remark = "";
		String fromUser = reqMsg.getFromUserName();
		String toUser = reqMsg.getToUserName();
		String event = reqMsg.getEvent();
		if (StringUtils.isNotBlank(event)) {
			if (WeiXinConstant.EVENT_TYPE_SUBSCRIBE.equals(event)) {
				// 关注
				dateValue = 1;
				remark = "关注";

				// 判断是否为扫描关注
				// 事件KEY值，qrscene_为前缀，后面为二维码的参数值
				String eventKey = reqMsg.getEventKey();
				// 如果是第一次扫码关注，此值不为空
				if (StringUtils.isNotBlank(eventKey)) {

					String eventKeyValue = eventKey.substring(eventKey.indexOf("_") + 1, eventKey.length());
					if (StringUtils.isNotBlank(eventKeyValue)) {
						// 如果存在二维码的参数值则进行参数值处理
						this.dealEventKeyValue(reqMsg, eventKeyValue);
					}
				}

			} else if (WeiXinConstant.EVENT_TYPE_UNSUBSCRIBE.equals(event)) {
				// 取消关注
				dateValue = -1;
				remark = "取消关注";
			}

			if (dateValue != 0) {
				try {
					// 调用数据收集接口
					String api = ApiModule.ReportForms.DataCollection.getName();

					Document document = DocumentHelper.createDocument();
					Element req = document.addElement(KstHosConstant.REQ);
					XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "7006");
					Element service = req.addElement(KstHosConstant.DATA);
					XMLUtil.addElement(service, "ChannelId", KstHosConstant.WX_CHANNEL_ID);
					XMLUtil.addElement(service, "ChannelName", KstHosConstant.WX_CHANNEL_NAME);
					XMLUtil.addElement(service, "Api", "");
					XMLUtil.addElement(service, "DataType", "1");
					XMLUtil.addElement(service, "DataValue", dateValue);
					XMLUtil.addElement(service, "Remark", remark);
					
					InterfaceMessage msg = new InterfaceMessage();
					msg.setApiName(api);
					msg.setParam(document.asXML());
					msg.setParamType(KstHosConstant.INTYPE);
					msg.setOutType(KstHosConstant.OUTTYPE);
					msg.setAuthInfo(reqMsg.getAuthInfo().toString());
					msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
					msg.setVersion(KstHosConstant.V);
					IReportFormsService reportFormService = SpringContextUtil.getBean(IReportFormsService.class);
					if(null != reportFormService) {
						String result = reportFormService.DataCollection(msg);
						KasiteConfig.print("--result-->" + result);
					}
					
					// 调用云数据收集接口
					api = ApiModule.ReportForms.DataCloudCollection.getName();

					Document document2 = DocumentHelper.createDocument();
					Element req2 = document2.addElement(KstHosConstant.REQ);
					XMLUtil.addElement(req2, KstHosConstant.TRANSACTIONCODE, "7006");
					Element service2 = req2.addElement(KstHosConstant.DATA);
					XMLUtil.addElement(service2, "ChannelId", KstHosConstant.WX_CHANNEL_ID);
					XMLUtil.addElement(service2, "DataType", 101);
					XMLUtil.addElement(service2, "DataCount", dateValue);
					XMLUtil.addElement(service2, "TargetType", "1");

					msg = new InterfaceMessage();
					msg.setApiName(api);
					msg.setParam(document2.asXML());
					msg.setParamType(KstHosConstant.INTYPE);
					msg.setOutType(KstHosConstant.OUTTYPE);
					msg.setAuthInfo(reqMsg.getAuthInfo().toString());
					msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
					msg.setVersion(KstHosConstant.V);
					if(null != reportFormService) {
						String result = reportFormService.DataCloudCollection(msg);
						KasiteConfig.print("--result-->" + result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 保存关注用户信息
				if (WeiXinConstant.EVENT_TYPE_SUBSCRIBE.equals(event)) {
					try {
						JSONObject json = WeiXinService.getUserInfo(reqMsg.getFromUserName(), reqMsg.getAuthInfo().getConfigKey());
						String api = ApiModule.Bat.AddBatUser.getName();
						Document document = DocumentHelper.createDocument();
						Element req = document.addElement(KstHosConstant.REQ);
						XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15102");
						Element data = req.addElement(KstHosConstant.DATA);
						XMLUtil.addElement(data, "OpenId", JsonUtil.getJsonString(json, "openid"));
						XMLUtil.addElement(data, "NickName",java.net.URLEncoder.encode(JsonUtil.getJsonString(json,"nickname"), "utf-8"));
						XMLUtil.addElement(data, "AccountId",KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id,reqMsg.getAuthInfo().getConfigKey()));
						XMLUtil.addElement(data, "Sex", JsonUtil.getJsonString(json,"sex"));
						XMLUtil.addElement(data, "City", JsonUtil.getJsonString(json,"city"));
						XMLUtil.addElement(data, "Country", JsonUtil.getJsonString(json,"country"));
						XMLUtil.addElement(data, "Province", JsonUtil.getJsonString(json,"province"));
						XMLUtil.addElement(data, "Language", JsonUtil.getJsonString(json,"language"));
						XMLUtil.addElement(data, "HeadImgUrl", JsonUtil.getJsonString(json,"headimgurl"));
						XMLUtil.addElement(data, "Subscribe", JsonUtil.getJsonString(json,"subscribe"));
						XMLUtil.addElement(data, "SubscribeTime", JsonUtil.getJsonString(json,"subscribe_time"));
						XMLUtil.addElement(data, "UnionId", "");
						XMLUtil.addElement(data, "Remark", JsonUtil.getJsonString(json,"remark"));
						XMLUtil.addElement(data, "GroupId", JsonUtil.getJsonString(json,"groupid"));
						XMLUtil.addElement(data, "SyncTime", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
						XMLUtil.addElement(data, "SysId", "");
						XMLUtil.addElement(data, "State", "1");

						InterfaceMessage msg = new InterfaceMessage();
						msg.setApiName(api);
						msg.setParam(document.asXML());
						msg.setParamType(KstHosConstant.INTYPE);
						msg.setOutType(KstHosConstant.OUTTYPE);
						msg.setAuthInfo(reqMsg.getAuthInfo().toString());
						msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
						msg.setVersion(KstHosConstant.V);
						IBatService batService = SpringContextUtil.getBean(IBatService.class);
						if(null != batService) {
							String result = batService.AddBatUser(msg);
							KasiteConfig.print("--result-->" + result);
						}
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.error(log, "保存关注用户失败",e);
					}
				}
				try {
					// 关注时返回提示语
					if (WeiXinConstant.EVENT_TYPE_SUBSCRIBE.equals(event)) {
						com.kasite.client.wechat.message.resp.TextMessage textMessage = new com.kasite.client.wechat.message.resp.TextMessage();
						// 回复文本消息
						String configKey = reqMsg.getAuthInfo().getConfigKey();
						// 回复文本消息
						String api = ApiModule.Msg.QueryAutoReplayArbitrarily.getName();
						InterfaceMessage msg = new InterfaceMessage();
						msg.setApiName(api);
						msg.setParamType(KstHosConstant.INTYPE);
						msg.setOutType(KstHosConstant.OUTTYPE);
						msg.setAuthInfo(reqMsg.getAuthInfo().toString());
						msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
						msg.setVersion(KstHosConstant.V);
						ReqQueryAutoReplayByFollow t = new ReqQueryAutoReplayByFollow(msg);
						String appId = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, configKey);
						t.setAppId(appId);
						//1 关注时回复
						t.setReplayType(1);
						t.setState(1);
						CommonReq<ReqQueryAutoReplayByFollow> commReq = new CommonReq<ReqQueryAutoReplayByFollow>(t);
						IMsgService msgService = HandlerBuilder.get().getMsgService(reqMsg.getAuthInfo());
						if(null != msgService) {
							CommonResp<RespQueryAutoReplayByFollow> result = msgService.queryAutoReplayByFollow(commReq);
							return messageHandle(result,fromUser,toUser,mgsContent);
						}
					}else {
						LogUtil.info(log, "未定义微信消息关注时自动恢复消息处理类，请实现接口："+IMsgService.class.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.error(log, "微信消息关注时自动恢复异常",e);
				}
			}
		}

		return "success";
	}

	@Override
	public String onMessage(MassResultMessage reqMsg) {
		// TODO Auto-generated method stub
		try {
			String status = reqMsg.getStatus();
			if (status != null && SEND_SUCCESS.equals(status)) {
				// 1成功0失败
				status = "1";
			} else {
				status = "0";
			}

//			// 群发消息发送任务完成通知
//			String api = ApiModule.Bat.msgSendJobFinishNotice.getName();
//
//			Document document = DocumentHelper.createDocument();
//			Element req = document.addElement(KstHosConstant.REQ);
//			XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15019");
//			Element service = req.addElement(KstHosConstant.DATA);
//			XMLUtil.addElement(service, "MsgId", reqMsg.getMsgId());
//			XMLUtil.addElement(service, "Status", status);
//			XMLUtil.addElement(service, "TotalCount", reqMsg.getTotalCount());
//			XMLUtil.addElement(service, "FilterCount", reqMsg.getFilterCount());
//			XMLUtil.addElement(service, "SentCount", reqMsg.getSentCount());
//			XMLUtil.addElement(service, "ErrorCount", reqMsg.getErrorCount());
//
//			InterfaceMessage msg = new InterfaceMessage();
//			msg.setApiName(api);
//			msg.setParam(document.asXML());
//			msg.setParamType(KstHosConstant.INTYPE);
//			msg.setOutType(KstHosConstant.OUTTYPE);
//			msg.setAuthInfo(reqMsg.getAuthInfo().toString());
//			msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
//			msg.setVersion(KstHosConstant.V);
//			String result = batService.msgSendJobFinishNotice(msg);
//			KasiteConfig.print("--result-->" + result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * 扫描带参数二维码事件
	 * @param reqMsg
	 * @return
	 */
	@Override
	public String onMessage(EventScanMessage reqMsg) {
		//获取事件的类型
		String event = reqMsg.getEvent();
		String fromUser = reqMsg.getFromUserName();
		String toUser = reqMsg.getToUserName();
		// 回复文本消息
		String configKey = reqMsg.getAuthInfo().getConfigKey();
		try {
			// 已关注时扫码返回提示语
			if (WeiXinConstant.EVENT_SCAN.equals(event)) {
				com.kasite.client.wechat.message.resp.TextMessage textMessage = new com.kasite.client.wechat.message.resp.TextMessage();
				// 回复文本消息
				String api = ApiModule.Msg.QueryAutoReplayByFollow.getName();
				Document document = DocumentHelper.createDocument();
				Element req = document.addElement(KstHosConstant.REQ);
				XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15102");
				Element data = req.addElement(KstHosConstant.DATA);
				XMLUtil.addElement(data, "ReplayType", "1");
				XMLUtil.addElement(data, "State", "1");

				InterfaceMessage msg = new InterfaceMessage();
				msg.setApiName(api);
				msg.setParam(document.asXML());
				msg.setParamType(KstHosConstant.INTYPE);
				msg.setOutType(KstHosConstant.OUTTYPE);
				msg.setAuthInfo(reqMsg.getAuthInfo().toString());
				msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
				msg.setVersion(KstHosConstant.V);
				ReqQueryAutoReplayByFollow t = new ReqQueryAutoReplayByFollow(msg);
				String appId = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, configKey);
				t.setAppId(appId);
				//1 关注时回复
				t.setReplayType(1);
				t.setState(1);
				CommonReq<ReqQueryAutoReplayByFollow> commReq = new CommonReq<ReqQueryAutoReplayByFollow>(t);
				IMsgService msgService = HandlerBuilder.get().getMsgService(reqMsg.getAuthInfo());
				if(null != msgService) {
					CommonResp<RespQueryAutoReplayByFollow> result = msgService.queryAutoReplayByFollow(commReq);
					return messageHandle(result,fromUser,toUser,mgsContent);
				}else {
					LogUtil.info(log, "未定义微信消息关注时自动恢复消息处理类，请实现接口："+IMsgService.class.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, "微信消息关注时自动恢复异常",e);
		}

		return "success";
	}

	/**
	 * 处理EventKey对应的参数值
	 * 
	 * @param content
	 * @return
	 */
	public String dealEventKeyValue(BaseMessage reqMsg, String content) {
		KasiteConfig.print("扫码关注调用：" + content);
		String result = "";
		try {
			IMsgService msgService =  HandlerBuilder.get().getMsgService(reqMsg.getAuthInfo());
			if(null != msgService) {
				
				
			}
			
			
			if (StringUtils.isNotBlank(content)) {
				/*
				 * 二维码类型|用途类型#二维码id GZYS|WLZX#34796877A17C4795B194C5C6C8A7F07D
				 */
				String[] eventKeys = content.split("\\|");
				if (eventKeys != null && eventKeys.length == 2) {
					String type = eventKeys[0];
					String idStr = eventKeys[1];
					if (type != null && TYPE.equals(type)) {
						// 扫描二维码关注医生
						String[] idStrs = idStr.split("\\#");
						if (idStrs != null && idStrs.length == 2) {
							/*
							 * String materialType = idStrs[0];//物料类型 String
							 * hosId = idStrs[1];//医院id String doctorUid =
							 * idStrs[2];//医生uid
							 */
							// 用途类型
							String useTyoe = idStrs[0];
							// 二维码主键id
							String qrcodeId = idStrs[1];
							// 扫码用户微信昵称
							String nickname = "";
							// 扫码用户微信头像
							String headimgurl = "";
							// 扫码用户微信性别
							String sex = "";

							if (StringUtils.isNotBlank(useTyoe) && USE_TYOE.equals(useTyoe)
									&& StringUtils.isNotBlank(qrcodeId)) {
								// 调用添加关注医生流水接口

								// 获取扫码用户对应信息
								try {
									// 获取用户信息
									JSONObject userInfo = WeiXinService.getUserInfo(reqMsg.getFromUserName(),reqMsg.getAuthInfo().getConfigKey());
									if (userInfo != null) {
										KasiteConfig.print("---userInfo-->" + userInfo.toString());
										if (userInfo.get(NICK_NAME) != null) {
											nickname = userInfo.getString("nickname");
											headimgurl = userInfo.getString("headimgurl");
											sex = userInfo.getString("sex");
										}
									}
								} catch (Exception e) {

								}

								JSONObject json = new JSONObject();
								// OpenId
								json.put("openId", reqMsg.getFromUserName());
								// 渠道id
								json.put("channelId", KstHosConstant.WX_CHANNEL_ID);
								// 二维码主键id
								json.put("qrcodeId", qrcodeId);
								// 扫码用户微信昵称
								json.put("nickName", nickname);
								// 扫码用户微信头像
								json.put("headImgurl", headimgurl);
								// 扫码用户微信性别
								json.put("sex", sex);

								KasiteConfig.print("---json-->" + json);
								// ApiModule api =
								// ApiModule.Zeus.SaveAttenDocWater;
								// result =
								// WsClient.getWsClient(SysConstant.ZEUS_CLIEND_ID,ConfigUtil.getInstance().getHttpUrl()).call(api.getName(),
								// json.toString());
								KasiteConfig.print("--respResult-->" + result);
							}

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public String messageHandle(CommonResp<RespQueryAutoReplayByFollow> result, String fromUser,String toUser, MessageContent mgsContent) throws Exception {
		com.kasite.client.wechat.message.resp.TextMessage textMessage = new com.kasite.client.wechat.message.resp.TextMessage();
		if (null != result && KstHosConstant.SUCCESSCODE.equals(result.getCode())) {

			List<RespQueryAutoReplayByFollow> resp = result.getListCaseRetCode();
			String replayMsg;
			if (null == resp || resp.size() <= 0) {
				replayMsg = "抱歉,没查询到相关信息!";
			} else {
				//如果匹配多条则默认第一条
				//
				RespQueryAutoReplayByFollow respVo = resp.get(0);
				Integer msgType = respVo.getMsgType();
				//判断文章类型／1文本消息 2图文消息
				if (null != msgType && msgType.equals(2)) {
					String responseMsg = respVo.getReplayMsg();
					responseMsg = MessageFormat.format(responseMsg, fromUser, toUser);
					return responseMsg;
				} else {
					replayMsg = respVo.getReplayMsg();
				}
				//不是图文消息则replayMsg不为空
				textMessage.setToUserName(fromUser);
				textMessage.setFromUserName(toUser);
				textMessage.setCreateTime(System.currentTimeMillis() + "");
				textMessage.setMsgType(WeiXinConstant.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag("0");
				textMessage.setContent(replayMsg);
				return mgsContent.make(textMessage);
			}
		}
		return "success";
	}
}
