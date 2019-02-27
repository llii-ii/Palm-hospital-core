 package com.kasite.client.core.msg.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.coreframework.util.DateOper;
import com.coreframework.util.Escape;
import com.coreframework.util.StringUtil;
import com.kasite.client.core.msg.bean.bo.Sms;
import com.kasite.client.core.msg.bean.dbo.AutoReplay;
import com.kasite.client.core.msg.bean.dbo.MsgOpenIdScene;
import com.kasite.client.core.msg.bean.dbo.MsgPush;
import com.kasite.client.core.msg.bean.dbo.MsgQueue;
import com.kasite.client.core.msg.bean.dbo.MsgQueueRecord;
import com.kasite.client.core.msg.bean.dbo.MsgScene;
import com.kasite.client.core.msg.bean.dbo.MsgSource;
import com.kasite.client.core.msg.bean.dbo.MsgTemp;
import com.kasite.client.core.msg.bean.dbo.MsgUserOpenId;
import com.kasite.client.core.msg.dao.IAutoReplayMapper;
import com.kasite.client.core.msg.dao.IMsgCenterMainCountMapper;
import com.kasite.client.core.msg.dao.IMsgOpenIdSceneMapper;
import com.kasite.client.core.msg.dao.IMsgPushMapper;
import com.kasite.client.core.msg.dao.IMsgQueueMapper;
import com.kasite.client.core.msg.dao.IMsgQueueRecordMapper;
import com.kasite.client.core.msg.dao.IMsgSceneMapper;
import com.kasite.client.core.msg.dao.IMsgSourceMapper;
import com.kasite.client.core.msg.dao.IMsgUserOpenIdMapper;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.AliYunSmsConfig;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.MsgCenterConfig;
import com.kasite.core.common.config.MsgCenterConfigVo;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.log.Logger;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.his.resp.HisRecipeClinicList;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgScene;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgSource;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqAddMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqDeleteMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgScene;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgSource;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgTemp;
import com.kasite.core.serviceinterface.module.msg.req.ReqEditMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgCenterMainCount;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgOpenIdSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgQueue;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgQueueList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSceneList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgSourceList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgTempList;
import com.kasite.core.serviceinterface.module.msg.req.ReqMsgUserOpenIdList;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.msg.req.ReqQueryOpenId;
import com.kasite.core.serviceinterface.module.msg.req.ReqReadMsg;
import com.kasite.core.serviceinterface.module.msg.req.ReqReplayMgr;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendSms;
import com.kasite.core.serviceinterface.module.msg.resp.MsgCenterPageHelper;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgQueueList;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgSource;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgTemp;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgUserOpenId;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryAutoReplayArbitrarily;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryAutoReplayByFollow;
import com.kasite.core.serviceinterface.module.msg.resp.RespQueryOpenId;
import com.kasite.core.serviceinterface.module.wechat.req.ReqQueryAllTemplateList;
import com.kasite.core.serviceinterface.module.yy.req.ReqPushRecipe;
import com.kasite.core.serviceinterface.module.yy.req.ReqPushRecipeIn;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;


/**
 * @author linjf 2017年11月14日 17:53:28 TODO 消息实现类
 */
@Service("msg.msgApi")
public class IMsgServiceImpl extends AbstractMsgCommonService implements IMsgService {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MSG);

	@Autowired
	private IAutoReplayMapper autoReplayMapper;
	
	@Autowired
	IMsgPushMapper msgPushMapper;
	@Autowired
	protected IMsgQueueMapper msgQueueMapper;
	@Autowired
	protected IMsgSceneMapper msgSceneMapper;
	@Autowired
	protected IMsgSourceMapper msgSourceMapper;
	@Autowired
	protected IMsgUserOpenIdMapper msgUserOpenIdMapper;
	@Autowired
	protected IMsgCenterMainCountMapper msgCenterMainCountMapper;
	@Autowired
	IMsgOpenIdSceneMapper msgOpenIdSceneMapper;
	@Autowired
	IBasicService basicService;
	@Autowired
	private IMsgService msgService;
	@Autowired
	IMsgQueueRecordMapper msgQueueRecordMapper;
	@Autowired
	MsgCenterConfig msgConfig;
	/**
	 * 关注消息自动回复
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryAutoReplayByFollow(InterfaceMessage msg) throws Exception{
		return this.queryAutoReplayByFollow(new CommonReq<ReqQueryAutoReplayByFollow>(new ReqQueryAutoReplayByFollow(msg))).toResult();
	}

	/**
	 * 任意或关键字消息自动回复
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryAutoReplayArbitrarily(InterfaceMessage msg) throws Exception{
		return this.queryAutoReplayArbitrarily(new CommonReq<ReqReplayMgr>(new ReqReplayMgr(msg))).toResult();
	}

	/**
	 * 短信推送
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String SendSms(InterfaceMessage msg) throws Exception{
		return this.sendSms(new CommonReq<ReqSendSms>(new ReqSendSms(msg))).toResult();
	}
	
	/*
	 * zwl
	 * 发送消息内部调用方法
	 */
	public CommonResp<RespMap> sendMsg(CommonReq<ReqSendMsg> reqParam) throws Exception {
		ReqSendMsg reqMsgQueue = reqParam.getParam();
		String cardNo = reqMsgQueue.getCardNo();
		int cardType = 0;
		if(reqMsgQueue.getCardType()!=null) {
			cardType = reqMsgQueue.getCardType();
		}
		String channelId = reqMsgQueue.getChannelId();
		String channelName = reqMsgQueue.getChannelName();
		String mobile = reqMsgQueue.getMobile();
		String modelId = reqMsgQueue.getModeId();
		String modeType = reqMsgQueue.getModeType();
		String msgContent = reqMsgQueue.getMsgContent();
		String openId = reqMsgQueue.getOpenId();
		int msgType = 0;
		if(reqMsgQueue.getMsgType()!=null) {
			msgType = reqMsgQueue.getMsgType();
		}
		String operatorId = reqMsgQueue.getOperatorId();
		String operatorName = reqMsgQueue.getOperatorName();
		String orderId = reqMsgQueue.getOrderId();
		String preSendTime = reqMsgQueue.getPreSendTime();
		String pushMemberId = reqMsgQueue.getPushMemberId();
		String sceneName = reqMsgQueue.getSceneName();
		String hosId = reqMsgQueue.getOrgId();
		if(StringUtil.isBlank(hosId)){
			hosId = reqMsgQueue.getHosId();
		}
		MsgQueue msgQueue = new MsgQueue();
		msgQueue.setHosId(hosId);
		msgQueue.setCardNo(cardNo);
		msgQueue.setCardType(cardType);
		msgQueue.setChannelId(channelId);
		msgQueue.setChannelName(channelName);
		msgQueue.setCreateTime(DateOper.getNowDateTime());
		msgQueue.setMobile(mobile);
		msgQueue.setModeId(modelId);
		msgQueue.setModelType(modeType);
		List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByModeType(modeType,1);
		if(queryMsgScene!=null&&queryMsgScene.size()>0){
			msgQueue.setSceneName(queryMsgScene.get(0).getSceneName());
		}
		else{
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_QUERYMSGTEMP, "消息发送失败：无效场景");
		}
		msgQueue.setpId("-1");
		msgQueue.setMsgContent(msgContent);
		msgQueue.setMsgId(CommonUtil.getGUID());
		msgQueue.setMsgType(msgType);
		msgQueue.setOpenId(openId);
		msgQueue.setOperatorId(operatorId);
		msgQueue.setOperatorName(operatorName);
		msgQueue.setOrderId(orderId);
		if(StringUtil.isBlank(preSendTime)){
			msgQueue.setPreSendTime(DateOper.getNowDateTime());
		}
		else{
			msgQueue.setPreSendTime(DateOper.parse2Timestamp(preSendTime));
		}
		msgQueue.setPushMemberId(pushMemberId);
//		msgQueue.setSceneName(sceneName);
		msgQueue.setSendCount(0);
		msgQueue.setState(0);
		int i = msgQueueMapper.insert(msgQueue);
		MsgQueueRecord msgQueueRecord = null;
		try {
			String errcode = "";
			String errmsg = "";
			String modeId = msgQueue.getModeId();
			List<MsgTemp> list = new ArrayList<MsgTemp>();
			if(modeId!=null&&!"".equals(modeId)){
				Example example = new Example(MsgTemp.class);
				example.createCriteria().andEqualTo("modeId", modeId).andEqualTo("state",1);
				list = msgTempMapper.selectByExample(example);
			}
			else{
				Example example = new Example(MsgTemp.class);
				example.createCriteria().andEqualTo("useChannel", msgQueue.getChannelId()).andEqualTo("modeType", msgQueue.getModelType()).andEqualTo("state",1);
				list = msgTempMapper.selectByExample(example);
			}
			if (null == list || list.size() <= 0) {
				String msg = "未找到模板";
				LogUtil.info(log, msg);
				msgQueue.setPushResult(msg);
				msgQueue.setSendCount(msgQueue.getSendCount()+1);
				msgQueue.setState(2);
				msgQueue.setUpdateTime(DateOper.getNowDateTime());
				msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
				msgQueueRecord = new MsgQueueRecord(msgQueue);
				msgQueueRecordMapper.insert(msgQueueRecord);
				return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_QUERYMSGTEMP, "消息发送失败：" +msg);
			}
			for (MsgTemp msgTemp : list) {
				try {
					msgContent = CommonUtil.getMsgContent(msgTemp.getPushMode(), DocumentHelper.parseText(msgQueue.getMsgContent()).getRootElement());
					if(StringUtil.isNotBlank(msgQueue.getOpenId())){
						msgContent = msgContent.replace("<openId>", msgQueue.getOpenId());
					}
					else{
						int openType = 0;
						if("100123".equals(msgQueue.getChannelId())){
							openType = 1;
						}
						if("100125".equals(msgQueue.getChannelId())){
							openType = 2;
						}
						List<String> openIds = msgUserOpenIdMapper.queryOpenId(msgQueue.getCardNo(),msgQueue.getCardType(),1,openType,msgQueue.getHosId());
						if(openIds==null||openIds.size()<1){
							String msg = "未找到openid";
							LogUtil.info(log, msg);
							msgQueue.setPushResult(msg);
							msgQueue.setSendCount(msgQueue.getSendCount()+1);
							msgQueue.setState(2);
							msgQueue.setUpdateTime(DateOper.getNowDateTime());
							msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
							msgQueueRecord = new MsgQueueRecord(msgQueue);
							msgQueueRecordMapper.insert(msgQueueRecord);
							continue;
						}
						msgContent = msgContent.replace("<openId>", openIds.get(0));
					}
					net.sf.json.JSONObject resJs = null;
					MsgCenterConfigVo auth = msgConfig.getAuth();
					String sendMsgUrl = "";
					if(auth!=null&&"1".equals(auth.getState())){
						sendMsgUrl = auth.getSendMsgUrl();
					}
					if (KstHosConstant.WX_CHANNEL_ID.equals(msgQueue.getChannelId())) {
						String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, msgTemp.getConfigKey());
						
						if ("1".equals(msgTemp.getMsgType())) {// 1：模板
							msgContent = msgContent.replace("<templateId>", msgTemp.getTemplateId());
							//走云端
							if(auth!=null&&"1".equals(auth.getState())){//走云端
								sendMsgUrl = sendMsgUrl.replace("{channel}", "wx");
								sendMsgUrl = sendMsgUrl.replace("{appId}", appid);
								sendMsgUrl = sendMsgUrl.replace("{msgType}", "1");
								HttpRequstBusSender sender = HttpRequestBus.create(sendMsgUrl, RequestType.post);
								sender.addHttpParam("msgContent", DesUtil.encrypt(msgContent, "UTF-8"));
								SoapResponseVo response = sender.send();
								int httpStausCode = response.getCode();
								if(response==null || httpStausCode!=200) {
									//HTTP 请求失败
									errcode = "-1";
									errmsg = "发送微信消息失败：相应code"+httpStausCode;
								}
								else{
									resJs = net.sf.json.JSONObject.fromObject(response.getResult());
								}
								
							}
							else{//直接调用微信
								resJs = WeiXinService.sendTemplateMessage(msgTemp.getConfigKey(), msgContent);
							}
							
						} else if ("2".equals(msgTemp.getMsgType())) {// 2：文本
							if(auth!=null&&"1".equals(auth.getState())){//走云端
								sendMsgUrl = sendMsgUrl.replace("{channel}", "wx");
								sendMsgUrl = sendMsgUrl.replace("{appId}", appid);
								sendMsgUrl = sendMsgUrl.replace("{msgType}", "2");
								HttpRequstBusSender sender = HttpRequestBus.create(sendMsgUrl, RequestType.post);
								sender.addHttpParam("msgContent", DesUtil.encrypt(msgContent, "UTF-8"));
								SoapResponseVo response = sender.send();
								int httpStausCode = response.getCode();
								if(response==null || httpStausCode!=200) {
									//HTTP 请求失败
									errcode = "-1";
									errmsg = "发送微信消息失败：相应code"+httpStausCode;
								}
								else{
									resJs = net.sf.json.JSONObject.fromObject(response.getResult());
								}
								
							}
							else{
								resJs = WeiXinService.sendCustomMessage(msgTemp.getConfigKey(), msgContent);
							}
							
						}
						if(resJs!=null && resJs.has("errcode") && "0".equals(resJs.getString("errcode"))) {//成功
							errcode = "0";
						}
						else{//失败
							errcode = "-1";
							errmsg = "发送微信消息失败："+(resJs!=null?resJs.toString():"");
						}
						
					} else if (KstHosConstant.ZFB_CHANNEL_ID.equals(msgQueue.getChannelId())) {
						String appid = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, msgTemp.getConfigKey());
						if ("1".equals(msgTemp.getMsgType())) {// 1：模板
							msgContent = msgContent.replace("<templateId>", msgTemp.getTemplateId());
							//走云端
							if(auth!=null&&"1".equals(auth.getState())){//走云端
								sendMsgUrl = sendMsgUrl.replace("{channel}", "zfb");
								sendMsgUrl = sendMsgUrl.replace("{appId}", appid);
								sendMsgUrl = sendMsgUrl.replace("{msgType}", "1");
								HttpRequstBusSender sender = HttpRequestBus.create(sendMsgUrl, RequestType.post);
								sender.addHttpParam("msgContent", DesUtil.encrypt(msgContent, "UTF-8"));
								SoapResponseVo response = sender.send();
								int httpStausCode = response.getCode();
								if(response==null || httpStausCode!=200) {
									//HTTP 请求失败
									errcode = "-1";
									errmsg = "发送支付宝消息失败：相应code"+httpStausCode;
								}
								else{
									resJs = net.sf.json.JSONObject.fromObject(response.getResult());
								}
								
							}
							else{
								resJs = AlipayService.sendSingleMessage(msgTemp.getConfigKey(), msgContent);
							}
							
						}else if ("2".equals(msgTemp.getMsgType())) {// 2:文本
							if(auth!=null&&"1".equals(auth.getState())){//走云端
								sendMsgUrl = sendMsgUrl.replace("{channel}", "wx");
								sendMsgUrl = sendMsgUrl.replace("{appId}", appid);
								sendMsgUrl = sendMsgUrl.replace("{msgType}", "2");
								HttpRequstBusSender sender = HttpRequestBus.create(sendMsgUrl, RequestType.post);
								sender.addHttpParam("msgContent", DesUtil.encrypt(msgContent, "UTF-8"));
								SoapResponseVo response = sender.send();
								int httpStausCode = response.getCode();
								if(response==null || httpStausCode!=200) {
									//HTTP 请求失败
									errcode = "-1";
									errmsg = "发送支付宝消息失败：相应code"+httpStausCode;
								}
								else{
									resJs = net.sf.json.JSONObject.fromObject(response.getResult());
								}
								
							}
							else{
								resJs = AlipayService.sendPublicCustomMessage(msgTemp.getConfigKey(), msgContent);
							}
							
						}
						if(resJs!=null && resJs.has("code") && "10000".equals(resJs.getString("code"))) {
							errcode = "0";
						}
						else{//失败
							errcode = "-1";
							errmsg = "发送支付宝消息失败："+(resJs!=null?resJs.toString():"");
						}
					}
					else if ("sms".equals(msgQueue.getChannelId())) {//短信
						
					}
					if("0".equals(errcode)){//成功
						msgQueue.setSendCount(msgQueue.getSendCount()+1);
						msgQueue.setSendTime(DateOper.getNowDateTime());
						msgQueue.setUpdateTime(DateOper.getNowDateTime());
						int deleteByPrimaryKey = msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
						if(deleteByPrimaryKey==1){
							msgQueueRecord = new MsgQueueRecord(msgQueue);
							msgQueueRecord.setState(1);//失败
							msgQueueRecordMapper.insert(msgQueueRecord);
						}
					}
					else{//失败
						msgQueue.setPushResult(errmsg);
						msgQueue.setSendTime(DateOper.getNowDateTime());
						msgQueue.setSendCount(msgQueue.getSendCount()+1);
						msgQueue.setUpdateTime(DateOper.getNowDateTime());
						if(5<msgQueue.getSendCount()){
							msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
							msgQueueRecord = new MsgQueueRecord(msgQueue);
							msgQueueRecord.setState(2);//失败
							msgQueueRecordMapper.insert(msgQueueRecord);
						}
						else{
							msgQueueMapper.updateByPrimaryKeySelective(msgQueue);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgQueue.setPushResult(e.getMessage());
					msgQueue.setSendTime(DateOper.getNowDateTime());
					msgQueue.setSendCount(msgQueue.getSendCount()+1);
					msgQueue.setUpdateTime(DateOper.getNowDateTime());
					if(5<msgQueue.getSendCount()){
						msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
						msgQueueRecord = new MsgQueueRecord(msgQueue);
						msgQueueRecord.setState(2);//失败
						msgQueueRecordMapper.insert(msgQueueRecord);
					}
					else{
						msgQueueMapper.updateByPrimaryKeySelective(msgQueue);
					}
				}
			}
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		} catch (Exception e) {
			msgQueue.setPushResult("发送消息报错："+e.getMessage());
			msgQueue.setSendCount(msgQueue.getSendCount()+1);
			msgQueue.setUpdateTime(DateOper.getNowDateTime());
			if(5<msgQueue.getSendCount()){
				msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
				msgQueueRecord = new MsgQueueRecord(msgQueue);
				msgQueueRecord.setState(2);//失败
				msgQueueRecordMapper.insert(msgQueueRecord);
			}
			else{
				msgQueueMapper.updateByPrimaryKeySelective(msgQueue);
			}
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_SENDMSG, "消息发送失败：" +e.getMessage());
			
		}
	}
	
	@Override
	public CommonResp<RespQueryAutoReplayByFollow> queryAutoReplayByFollow(CommonReq<ReqQueryAutoReplayByFollow> commReq) throws Exception {
		
		ReqQueryAutoReplayByFollow vo = commReq.getParam();
		//微信配置 id 即微信配置表的 orginid
		commReq.getParam().getAuthInfo().getConfigKey();
		
		
		AutoReplay query = new AutoReplay();
		query.setState(vo.getState());
		query.setReplayType(vo.getReplayType());
		List<AutoReplay> replayList = autoReplayMapper.select(query);
		if (replayList == null || replayList.size() == 0) {
			return new CommonResp<RespQueryAutoReplayByFollow>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Common.ERROR_SQLEXECERROR, "查询公众号回复设置为空!");
		}
		AutoReplay autoReplay = replayList.get(0);
		RespQueryAutoReplayByFollow resp = new RespQueryAutoReplayByFollow();
		resp.setCreateDate(DateOper.formatDate(autoReplay.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		resp.setId(autoReplay.getId());
		resp.setKeyWord(autoReplay.getKeyWord());
		resp.setMatchRule(autoReplay.getMatchRule());
		resp.setReplayMsg(autoReplay.getReplayMsg());
		resp.setReplayType(autoReplay.getReplayType());
		resp.setState(autoReplay.getState());
		resp.setUpdateDate(DateOper.formatDate(autoReplay.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		
		return new CommonResp<RespQueryAutoReplayByFollow>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespQueryAutoReplayArbitrarily> queryAutoReplayArbitrarily(CommonReq<ReqReplayMgr> commReq) throws Exception {
		
		ReqReplayMgr req = commReq.getParam();
		String inputkeyWord = req.getKeyWord().trim();
		AutoReplay query = new AutoReplay();
		query.setReplayType(2);
		query.setState(req.getState());
//		query.setKeyWord(inputkeyWord);
		List<AutoReplay> replayList = autoReplayMapper.select(query);
		AutoReplay result = null;
		// 关键字匹配
		if (null != replayList && replayList.size() > 0) {
			String replayMsg = "";
			for (AutoReplay auto : replayList) {
				result = auto;
				int matchrule = auto.getMatchRule();
				String keyword = auto.getKeyWord().trim();
				if (matchrule == 2) {
					if (null != keyword && keyword.length() > 0) {
						if (keyword.length() > 0) {
							String reg = ".*" + keyword + ".*";
							// 匹配成功
							if (inputkeyWord.matches(reg)) {
								replayMsg += auto.getReplayMsg() + "\n";
								break;
							}
						}
					}
				} else if (matchrule == 1) {
					if (null != keyword && keyword.equals(inputkeyWord)) {
						replayMsg += auto.getReplayMsg() + "\n";
						break;
						// 匹配成功
					}
				}
			}
			if (replayMsg.length() > 0) {
				RespQueryAutoReplayArbitrarily resp = new RespQueryAutoReplayArbitrarily();
				resp.setCreateDate(DateOper.formatDate(result.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				resp.setId(result.getId());
				resp.setKeyWord(result.getKeyWord());
				resp.setMatchRule(result.getMatchRule());
				resp.setReplayMsg(replayMsg);
				resp.setReplayType(result.getReplayType());
				resp.setState(result.getState());
				resp.setUpdateDate(DateOper.formatDate(result.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				return new CommonResp<RespQueryAutoReplayArbitrarily>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
			}
		}
		query.setReplayType(3);
		replayList = autoReplayMapper.select(query);
		if(replayList==null || replayList.size()<=0) {
			return new CommonResp<RespQueryAutoReplayArbitrarily>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_SQLEXECERROR, "查询不到自动回复信息");
		}
		result = replayList.get(0);
		RespQueryAutoReplayArbitrarily resp = new RespQueryAutoReplayArbitrarily();
		resp.setCreateDate(DateOper.formatDate(result.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		resp.setId(result.getId());
		resp.setKeyWord(result.getKeyWord());
		resp.setMatchRule(result.getMatchRule());
		resp.setReplayMsg(result.getReplayMsg());
		resp.setReplayType(result.getReplayType());
		resp.setState(result.getState());
		resp.setUpdateDate(DateOper.formatDate(result.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
		return new CommonResp<RespQueryAutoReplayArbitrarily>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
		
//		ReqReplayMgr vo = null;
//		AutoReplay result = null;
//		List<AutoReplay> replayList = new ArrayList<AutoReplay>();
//		AutoReplay queryAutoReplay = new AutoReplay();
//		try {
//			vo = new ReqReplayMgr(msg);
//			String inputkeyWord = vo.getKeyWord().trim();
//			queryAutoReplay.setReplayType(2);
//			queryAutoReplay.setState(vo.getState());
//			queryAutoReplay.setKeyWord(inputkeyWord);
////			replayList = MsgDao.queryReplay(queryAutoReplay);
//			replayList = autoReplayMapper.select(queryAutoReplay);
//			// 关键字匹配
//			if (null != replayList && replayList.size() > 0) {
//				String replayMsg = "";
//				for (AutoReplay auto : replayList) {
//					int matchrule = auto.getMatchRule();
//					String keyword = auto.getKeyWord().trim();
//					if (matchrule == 2) {
//						if (null != keyword && keyword.length() > 0) {
//							if (keyword.length() > 0) {
//								String reg = ".*" + keyword + ".*";
//								// 匹配成功
//								if (inputkeyWord.matches(reg)) {
//									replayMsg += auto.getReplayMsg() + "\n";
//									break;
//								}
//							}
//						}
//					} else if (matchrule == 1) {
//						if (null != keyword && keyword.equals(inputkeyWord)) {
//							replayMsg += auto.getReplayMsg() + "\n";
//							break;
//							// 匹配成功
//						}
//					}
//				}
//				if (replayMsg.length() > 0) {
//					result = new AutoReplay();
//					result.setReplayMsg(replayMsg);
//					return CommonUtil.getRetVal(msg,KstHosConstant.DEFAULTTRAN,
//							"Id,ReplayType,KeyWord,ReplayMsg,MatchRule,CreateDate,UpdateDate,State", result);
//				}
//			}
//
//			queryAutoReplay.setReplayType(3);
//			replayList = autoReplayMapper.select(queryAutoReplay);
////			replayList = MsgDao.queryReplay(queryAutoReplay);
//			if (null != replayList && replayList.size() > 0) {
//				result = replayList.get(0);
//				if (null != result) {
//					return CommonUtil.getRetVal(msg,KstHosConstant.DEFAULTTRAN,
//							"Id,ReplayType,KeyWord,ReplayMsg,MatchRule,CreateDate,UpdateDate,State", result);
//				}
//				return CommonUtil.getRetVal(msg,KstHosConstant.QUERYUSER, RetCode.Common.ERROR_SQLEXECERROR.getCode(),
//						"查询不到用户输入相关信息");
//			} else {
//				return CommonUtil.getRetVal(msg,KstHosConstant.QUERYUSER, RetCode.Common.ERROR_SQLEXECERROR.getCode(),
//						"查询不到用户输入相关信息");
//			}
//		} catch (Exception e) {
//			LogUtil.error(log, e);
//			e.printStackTrace();
//			return CommonUtil.getRetVal(msg,KstHosConstant.QUERYUSER, RetCode.Common.ERROR_UNKNOWN.getCode(),
//					"系统异常：" + StringUtil.getExceptionStack(e));
//		}
	}

	@Override
	public CommonResp<RespMap> sendSms(CommonReq<ReqSendSms> commReq) throws Exception {
		ReqSendSms reqSendSms = commReq.getParam();
		// 保存短信
		MsgPush msgPush = new MsgPush();
		msgPush.setMsgPushId(CommonUtil.getGUID());
		msgPush.setMobile(reqSendSms.getMoblie());
		msgPush.setPushChannelId(reqSendSms.getChannelId());
		msgPush.setOperator(reqSendSms.getOperatorName());
		msgPush.setOperatorName(reqSendSms.getOperatorId());
		msgPush.setState(1);
		msgPush.setNum(0);
		if(StringUtil.isBlank(reqSendSms.getTemplateCode())) {
			msgPush.setModeId(aliYunSmsConfig.getCodeTemplate());
		}else {
			msgPush.setModeId(reqSendSms.getTemplateCode());
		}
		if(StringUtil.isNotBlank(reqSendSms.getTemplateParam())) {
			msgPush.setMsgContent(reqSendSms.getTemplateParam());
		}else {
			msgPush.setMsgContent(reqSendSms.getContent());
		}
		try {
			msgPush.setBegin(DateOper.getNowDateTime());
			msgPush.setEnd(DateOper.parse2Timestamp("9999-12-31 23:59:59"));
		} catch (ParseException e) {
			e.printStackTrace();
			return new CommonResp<RespMap>(commReq, KstHosConstant.SENDMSGCODE, RetCode.Msg.ERROR_DATEFORMAT,"日期格式化异常：" + e.getMessage());
		}
		msgPushMapper.insert(msgPush);
		
		//直接发送短信消息,失败时间隔1000ms重试2次
		for(int i=0;i<KstHosConstant.NUMBER_3;i++) {
			SendSmsResponse resp = this.sendSms(reqSendSms.getAuthInfo(), msgPush.getMobile(), null, msgPush.getModeId(), msgPush.getMsgContent());
			if(resp.getCode() != null && resp.getCode().equals("OK")) {
				//发送成功，结束循环
				break;
			}else {
				//发送短信失败，间隔1000ms重试
				Thread.sleep(1000);
			}
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.SENDMSGCODE, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> sendAliCodeSms(CommonReq<ReqSendSms> commReq) throws Exception {
		Sms sms = makeCode(commReq.getParam().getMoblie());      //制作验证码，6位随机数字
		JSONObject smsJson=new JSONObject();
        smsJson.put("code",sms.getCode());
        if(StringUtil.isBlank(commReq.getParam().getTemplateParam())) {
        	commReq.getParam().setTemplateParam(smsJson.toJSONString());
        }
		boolean issend = sendSmsCode(commReq,sms);
		if(issend) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.SENDMSGCODE, RetCode.Success.RET_10000,
					RespMap.get().add(ApiKey.sendAliCodeSms.code, sms.getCode()));
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.SENDMSGCODE, RetCode.Order.ERROR_SENDSMS);
		}
	}
	/**
	 * 静态的Redis模拟 map缓存 如果同一个手机号 10秒 内重复发送超过2条 
	 */
	public static ExpiryMap<String, Sms> RedisMap = new ExpiryMap<>();
	 /**
     * 根据用户输入的phone发送验证码
     * @param phone 电话号码
     */
    public boolean sendSmsCode(CommonReq<ReqSendSms> commReq,Sms sms){
    	ReqSendSms req = commReq.getParam();
    	AuthInfoVo vo = req.getAuthInfo();
    	String phone = req.getMoblie();
    	String signName = req.getSignName();
    	String templateCode = req.getTemplateCode();
    	String templateParam = req.getTemplateParam();
    	LogUtil.info(log, "短信发送:"+phone+"|templateParam="+templateParam);
    	 
//        if(!phone.matches("^1[3|4|5|7|8][0-9]{9}$")){
//            KasiteConfig.print("手机号码格式不正确");
//            return false;
//        }
        //判断用户输入的电话号码是否频繁发送
        if(isSendOfen(phone)){
        	 LogUtil.info(log, "发送短信频繁，请稍后再试:"+phone);
            return false;
        }
        
        SendSmsResponse sendSmsResponse=null;
        try {
            sendSmsResponse = sendSms(vo, phone, signName, templateCode, templateParam);//(phone,signName,codeTemplate,smsJson);
        } catch (ClientException e) {
            e.printStackTrace();
            Logger.get().error("SendAliCodeSms", new LogBody(vo)
            		.set(e)
            		.set("mobile", phone).set("signName", signName)
            		.set("templateCode", templateCode).set("templateParam", templateParam)
            		);
            return false;
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //短信发送成功，将短信记录到redis中 5秒缓存
        	RedisMap.put(phone, sms, 5000);
        }else {
        	return false;
        }
       
        return true;
    }
    
    //判断验证功发送时候频繁
    private boolean isSendOfen(String phone) {
        if(RedisMap.get(phone)==null) {
            return false;
        }else{
            //判断上一次记录的时间和当前时间进行对比，如果两次相隔时间小于120s，视为短信发送频繁
            Sms sms=RedisMap.get(phone);
            //两次发送短信中间至少有2秒的间隔时间
            if(sms.getTime()+2000>=System.currentTimeMillis()) {
                return true;
            }
            return false;
        }
    }
    
    //随机生成6位数的短信码
    private Sms makeCode(String phone) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for(int i=0;i<6;i++){
            int next =random.nextInt(10);
            code.append(next);
        }
        return new Sms(phone,code.toString(),System.currentTimeMillis());
    }
    @Autowired
    private AliYunSmsConfig aliYunSmsConfig;
    public SendSmsResponse sendSms(AuthInfoVo vo,String mobile,String signName,String templateCode,String templateParam) throws ClientException {
    	if(!mobile.matches("^1[3|4|5|7|8][0-9]{9}$")){
            KasiteConfig.print("手机号码格式不正确");
            SendSmsResponse resp = new SendSmsResponse();
            resp.setCode("-1");
            resp.setMessage("手机号码格式不正确");
            return resp;
        }
    	//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliYunSmsConfig.getAccessId(), aliYunSmsConfig.getAccessKey());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", aliYunSmsConfig.getProduct(), aliYunSmsConfig.getDomain());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        if(StringUtil.isBlank(signName)) {
        	signName = aliYunSmsConfig.getSignName();
        }
        request.setSignName(signName);
        if(StringUtil.isBlank(templateCode)) {
        	signName = aliYunSmsConfig.getCodeTemplate();
        }
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(templateParam);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        String uuid = UUID.randomUUID().toString();
        request.setOutId(uuid);
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
     
        Logger.get().info("SendAliCodeSms", new LogBody(vo)
        		.set("mobile", mobile).set("signName", signName)
        		.set("templateCode", templateCode).set("templateParam", templateParam).set("outId", uuid)
        		.set("ResponseCode", sendSmsResponse.getCode()).set("ResponseMessage", sendSmsResponse.getMessage())
        		.set("ResponseRequestId", sendSmsResponse.getRequestId()).set("ResponseBizId", sendSmsResponse.getBizId())
        		);
        
        return sendSmsResponse;
    }

	@Override
	public Type accept() {
		// TODO 判断是否是我处理的，如果是的话消息都玩我这里发送 目前这个实现类是默认的。当其它实现类都没有的时候以这个为主默认继承这个类即可
		//目前这个是默认的DEFAULT 如果需要加载其它的需要自行定义
		return Type.DEFAULT;
	}

	@Override
	public String response(AuthInfoVo vo,String wechatConfigKey,String openId,String gId) throws Exception {
		//返回一个图文消息内容
		return null;
	}
/*
 * 消息队列--添加消息队列，供第三方调用的，内部调用走sendMsg
 * ZWL
 */
	@Override
	public CommonResp<RespMap> AddMsgQueue(CommonReq<ReqMsgQueue> reqParam) throws Exception {
		String msg = "";
		try{
			msg += "消息队列添加入参：" +reqParam.getParam()+"===";
			LogUtil.info(log, msg);
			ReqMsgQueue reqMsgQueue = reqParam.getParam();
			String cardNo = reqMsgQueue.getCardNo();
			int cardType = reqMsgQueue.getCardType();
			String channelId = reqMsgQueue.getChannelId();
			String channelName = reqMsgQueue.getChannelName();
			String mobile = reqMsgQueue.getMobile();
			String modeId = reqMsgQueue.getModeId();
			String modeType = reqMsgQueue.getModeType();
			String msgContent = reqMsgQueue.getMsgContent();
			String openId = reqMsgQueue.getOpenId();
			int msgType = reqMsgQueue.getMsgType();
			String operatorId = reqMsgQueue.getOperatorId();
			String operatorName = reqMsgQueue.getOperatorName();
			String orderId = reqMsgQueue.getOrderId();
			String preSendTime = reqMsgQueue.getPreSendTime();
			String pushMemberId = reqMsgQueue.getPushMemberId();
			String hosId = reqMsgQueue.getOrgId();
			if(StringUtil.isNotBlank(modeId)&&StringUtil.isBlank(modeType)){
				MsgTemp selectByPrimaryKey = msgTempMapper.selectByPrimaryKey(modeId);
				if(selectByPrimaryKey!=null){
					modeType = selectByPrimaryKey.getModeType();
				}
			}
			if(StringUtil.isBlank(openId)){//如果openId为空
				String[] cardNos = cardNo.split(",");
				String pId = "-1";
				if(cardNos.length>1){//大于1代表批量推送
					MsgQueue msgQueue = new MsgQueue();
					msgQueue.setHosId(hosId);
					msgQueue.setCardNo("");
					msgQueue.setCardType(cardType);
					msgQueue.setChannelId(channelId);
					msgQueue.setChannelName(channelName);
					msgQueue.setCreateTime(DateOper.getNowDateTime());
					msgQueue.setMobile(mobile);
					msgQueue.setModeId(modeId);
					msgQueue.setModelType(modeType);
					List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByModeType(modeType,1);
					if(queryMsgScene!=null&&queryMsgScene.size()>0){
						msgQueue.setSceneName(queryMsgScene.get(0).getSceneName());
					}
					else{
						return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_QUERYMSGTEMP, "消息发送失败：无效场景");
					}
					msgQueue.setMsgContent(msgContent);
					pId = CommonUtil.getGUID();
					msgQueue.setMsgId(pId);
					msgQueue.setpId("-1");
					msgQueue.setMsgType(msgType);
					msgQueue.setOpenId("");
					msgQueue.setOperatorId(operatorId);
					msgQueue.setOperatorName(operatorName);
					msgQueue.setOrderId("");
					if(StringUtil.isBlank(preSendTime)){
						msgQueue.setPreSendTime(DateOper.getNowDateTime());
					}
					else{
						msgQueue.setPreSendTime(DateOper.parse2Timestamp(preSendTime));
					}
					msgQueue.setPushMemberId(pushMemberId);
					msgQueue.setSendCount(0);
					msgQueue.setState(3);
					MsgQueueRecord msgQueueRecord = new MsgQueueRecord(msgQueue);
					msgQueueRecordMapper.insert(msgQueueRecord);
					
				}
				for (int i = 0; i < cardNos.length; i++) {
					cardNo = cardNos[i];
					MsgQueue msgQueue = new MsgQueue();
					msgQueue.setHosId(hosId);
					msgQueue.setpId(pId);
					msgQueue.setCardNo(cardNo);
					msgQueue.setCardType(cardType);
					msgQueue.setChannelId(channelId);
					msgQueue.setChannelName(channelName);
					msgQueue.setCreateTime(DateOper.getNowDateTime());
					msgQueue.setMobile(mobile);
					msgQueue.setModeId(modeId);
					msgQueue.setModelType(modeType);
					List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByModeType(modeType,1);
					if(queryMsgScene!=null&&queryMsgScene.size()>0){
						msgQueue.setSceneName(queryMsgScene.get(0).getSceneName());
					}
					else{
						return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_QUERYMSGTEMP, "消息发送失败：无效场景");
					}
					msgQueue.setMsgType(msgType);
					msgQueue.setOpenId(openId);
					msgQueue.setOperatorId(operatorId);
					msgQueue.setOperatorName(operatorName);
					msgQueue.setOrderId(orderId);
					if(StringUtil.isBlank(preSendTime)){
						msgQueue.setPreSendTime(DateOper.getNowDateTime());
					}
					else{
						msgQueue.setPreSendTime(DateOper.parse2Timestamp(preSendTime));
					}
					msgQueue.setPushMemberId(pushMemberId);
					msgQueue.setSendCount(0);
					msgQueue.setState(0);
					int openType = 0;
					if("100123".equals(channelId)){
						openType = 1;
					}
					if("100125".equals(channelId)){
						openType = 2;
					}
					List<String> openIds = msgUserOpenIdMapper.queryOpenId(cardNo,cardType,1,openType,msgQueue.getHosId());
					if(openIds==null||openIds.size()<1){
						LogUtil.info(log, msg);
						msgQueue.setMsgId(CommonUtil.getGUID());
						msgQueue.setMsgContent(msgContent);
						msgQueue.setPushResult("未找到对应的openId");
						msgQueue.setSendCount(msgQueue.getSendCount()+1);
						msgQueue.setState(2);
						msgQueue.setUpdateTime(DateOper.getNowDateTime());
//						msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
						MsgQueueRecord msgQueueRecord = new MsgQueueRecord(msgQueue);
						msgQueueRecordMapper.insert(msgQueueRecord);
						continue;
					}
					for (Iterator<String> iterator = openIds.iterator(); iterator.hasNext();) {
						openId = iterator.next();
						msgQueue.setOpenId(openId);
						String guid = CommonUtil.getGUID();
						msgQueue.setMsgId(guid);
						msgContent = msgContent.replace("<msgId>", guid);
						msgQueue.setMsgContent(msgContent);
						msgQueueMapper.insert(msgQueue);
					}
					
				}
				
			}
			else{
				MsgQueue msgQueue = new MsgQueue();
				msgQueue.setpId("-1");
				msgQueue.setHosId(hosId);
				msgQueue.setCardNo(cardNo);
				msgQueue.setCardType(cardType);
				msgQueue.setChannelId(channelId);
				msgQueue.setChannelName(channelName);
				msgQueue.setCreateTime(DateOper.getNowDateTime());
				msgQueue.setMobile(mobile);
				msgQueue.setModeId(modeId);
				msgQueue.setModelType(modeType);
				List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByModeType(modeType,1);
				if(queryMsgScene!=null&&queryMsgScene.size()>0){
					msgQueue.setSceneName(queryMsgScene.get(0).getSceneName());
				}
				else{
					return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_QUERYMSGTEMP, "消息发送失败：无效场景");
				}
				String guid = CommonUtil.getGUID();
				msgQueue.setMsgId(guid);
				msgContent = msgContent.replace("<msgId>", guid);
				msgQueue.setMsgContent(msgContent);
				msgQueue.setMsgType(msgType);
				msgQueue.setOpenId(openId);
				msgQueue.setOperatorId(operatorId);
				msgQueue.setOperatorName(operatorName);
				msgQueue.setOrderId(orderId);
				if(StringUtil.isBlank(preSendTime)){
					msgQueue.setPreSendTime(DateOper.getNowDateTime());
				}
				else{
					msgQueue.setPreSendTime(DateOper.parse2Timestamp(preSendTime));
				}
				msgQueue.setPushMemberId(pushMemberId);
				msgQueue.setSendCount(0);
				msgQueue.setState(0);
				msgQueueMapper.insert(msgQueue);
			}
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		} catch (Exception e) {
			e.printStackTrace();
			msg += "消息队列添加失败：" + e.getMessage();
			return new CommonResp<RespMap>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_CALLINTERFACE, "消息队列添加失败：" + e.getMessage());
		}
		finally {
			LogUtil.info(log, msg);
		}
	}
	@Override
	public String AddMsgQueue(InterfaceMessage msg) throws Exception {
		return this.AddMsgQueue(new CommonReq<ReqMsgQueue>(new ReqMsgQueue(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> QueryWxTemplateList(CommonReq<ReqQueryAllTemplateList> commReq) throws Exception {
		String wxkey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, KstHosConstant.WX_CHANNEL_ID);
		ReqQueryAllTemplateList req = new ReqQueryAllTemplateList(commReq.getMsg(), wxkey);
		return weiXinService.QueryWxTemplateList(new CommonReq<ReqQueryAllTemplateList>(req));
	}

	@Override
	public String QueryWxTemplateList(InterfaceMessage msg) throws Exception {
		return this.QueryWxTemplateList(new CommonReq<ReqQueryAllTemplateList>(new ReqQueryAllTemplateList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMsgScene> MsgSceneList(CommonReq<ReqMsgSceneList> commReq) throws Exception {
		ReqMsgSceneList req = commReq.getParam();
		List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgScene(req);
		return new CommonResp<RespMsgScene>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,queryMsgScene);
	}

	@Override
	public String MsgSceneList(InterfaceMessage msg) throws Exception {
		return this.MsgSceneList(new CommonReq<ReqMsgSceneList>(new ReqMsgSceneList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> AddMsgScene(CommonReq<ReqAddMsgScene> commReq) throws Exception {
		ReqAddMsgScene req = commReq.getParam();
		String sceneName = req.getSceneName();
		String modeType = req.getModeType();
		MsgScene msgScene = new MsgScene(); 
		msgScene.setModeType(modeType);
		int selectCount = msgSceneMapper.selectCount(msgScene);
		if(selectCount>0){//场景编码已存在
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Msg.ERROR_CALLINTERFACE, "场景编码已存在");
		}
		msgScene.setSceneName(sceneName);
		msgScene.setSceneId(CommonUtil.getGUID());
		msgScene.setCreateTime(DateOper.getNowDateTime());
		msgScene.setState(1);
		msgScene.setUpdateTime(DateOper.getNowDateTime());
		msgScene.setHosId(req.getOrgId());
		msgSceneMapper.insert(msgScene);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String AddMsgScene(InterfaceMessage msg) throws Exception {
		return this.AddMsgScene(new CommonReq<ReqAddMsgScene>(new ReqAddMsgScene(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> EditMsgScene(CommonReq<ReqEditMsgScene> commReq) throws Exception {
		ReqEditMsgScene req = commReq.getParam();
		MsgScene msgScene = new MsgScene(); 
		msgScene.setSceneId(req.getSceneId());
		if(StringUtil.isNotBlank(req.getSceneName())){
			msgScene.setSceneName(req.getSceneName());
		}
		if(StringUtil.isNotBlank(req.getModeType())){
			msgScene.setModeType(req.getModeType());
		}
		if(StringUtil.isNotBlank(req.getState())){
			msgScene.setState(req.getState());
		}
		msgScene.setUpdateTime(DateOper.getNowDateTime());
		msgSceneMapper.updateByPrimaryKeySelective(msgScene);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String EditMsgScene(InterfaceMessage msg) throws Exception {
		return this.EditMsgScene(new CommonReq<ReqEditMsgScene>(new ReqEditMsgScene(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> AddMsgTemp(CommonReq<ReqAddMsgTemp> commReq) throws Exception {
		ReqAddMsgTemp req = commReq.getParam();
		String configkey = "";
		if(StringUtil.isBlank(req.getConfigKey())){//如果页面没传就默认取第一个
			if(KstHosConstant.WX_CHANNEL_ID.equals(req.getUseChannel())){
				configkey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, KstHosConstant.WX_CHANNEL_ID);
			}
			if(KstHosConstant.ZFB_CHANNEL_ID.equals(req.getUseChannel())){
				configkey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, KstHosConstant.ZFB_CHANNEL_ID);
			}
			if(StringUtil.isBlank(configkey)){//
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_CHANNELID);
			}
			else{
				configkey = configkey.split(",")[0];
			}
		}
		else{
			configkey = req.getConfigKey();
		}
		MsgTemp msgTemp = new MsgTemp();
		msgTemp.setBegin(DateOper.getNowDateTime());
		msgTemp.setUserHos(req.getUserHos());
		msgTemp.setCreateTime(DateOper.getNowDateTime());
		msgTemp.setEnd(DateOper.getNowDateTime());
		msgTemp.setModeContent(Escape.unescape(req.getModeContent()));
		msgTemp.setModeId(CommonUtil.getGUID());
		msgTemp.setModeType(req.getModeType());
		msgTemp.setModeUrl(req.getModeUrl());
		msgTemp.setMsgTempName(req.getMsgTempName());
		msgTemp.setMsgType(req.getMsgType());
		msgTemp.setOperatorId(req.getOperatorId());
		msgTemp.setOperatorName(req.getOperatorName());
		msgTemp.setPushMode(Escape.unescape(req.getPushMode()));
		msgTemp.setState("1");
		msgTemp.setTemplateId(req.getTemplateId());
		msgTemp.setUpdateTime(DateOper.getNowDateTime());
		msgTemp.setUseChannel(req.getUseChannel());
		msgTemp.setMsgTempDemo((req.getMsgTempDemo()));
		msgTemp.setConfigKey(configkey);
		msgTempMapper.insert(msgTemp);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String AddMsgTemp(InterfaceMessage msg) throws Exception {
		return this.AddMsgTemp(new CommonReq<ReqAddMsgTemp>(new ReqAddMsgTemp(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> DeleteMsgTemp(CommonReq<ReqDeleteMsgTemp> commReq) throws Exception {
		ReqDeleteMsgTemp req = commReq.getParam();
		msgTempMapper.deleteByPrimaryKey(req.getModeId());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String DeleteMsgTemp(InterfaceMessage msg) throws Exception {
		return this.DeleteMsgTemp(new CommonReq<ReqDeleteMsgTemp>(new ReqDeleteMsgTemp(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> EditMsgTemp(CommonReq<ReqEditMsgTemp> commReq) throws Exception {
		ReqEditMsgTemp req = commReq.getParam();
		MsgTemp msgTemp = new MsgTemp(); 
		msgTemp.setModeId(req.getModeId());
		if(StringUtil.isNotBlank(req.getState())){
			msgTemp.setState(req.getState());
		}
		if(StringUtil.isNotBlank(req.getPushMode())){
			msgTemp.setPushMode(Escape.unescape(req.getPushMode()));
		}
		msgTemp.setUpdateTime(DateOper.getNowDateTime());
		msgTempMapper.updateByPrimaryKeySelective(msgTemp);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String EditMsgTemp(InterfaceMessage msg) throws Exception {
		return this.EditMsgTemp(new CommonReq<ReqEditMsgTemp>(new ReqEditMsgTemp(msg))).toResult();
	}

	@Override
	public CommonResp<RespMsgTemp> MsgTempList(CommonReq<ReqMsgTempList> commReq) throws Exception {
		ReqMsgTempList req = commReq.getParam();
		List<RespMsgTemp> list = msgTempMapper.queryMsgTemp(req);
		return new CommonResp<RespMsgTemp>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
	}

	@Override
	public String MsgTempList(InterfaceMessage msg) throws Exception {
		return this.MsgTempList(new CommonReq<ReqMsgTempList>(new ReqMsgTempList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> AddMsgSource(CommonReq<ReqAddMsgSource> commReq) throws Exception {
		ReqAddMsgSource req = commReq.getParam();
		MsgSource msgSource = new MsgSource();
		msgSource.setCreateTime(DateOper.getNowDateTime());
		msgSource.setOperatorId(req.getOperatorId());
		msgSource.setOperatorName(req.getOperatorName());
		msgSource.setSourceId(CommonUtil.getGUID());
		msgSource.setSourceName(req.getSourceName());
		msgSource.setState(1);
		msgSource.setUpdateTime(DateOper.getNowDateTime());
		msgSource.setHosId(req.getOrgId());
		msgSourceMapper.insert(msgSource);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String AddMsgSource(InterfaceMessage msg) throws Exception {
		return this.AddMsgSource(new CommonReq<ReqAddMsgSource>(new ReqAddMsgSource(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> EditMsgSource(CommonReq<ReqEditMsgSource> commReq) throws Exception {
		ReqEditMsgSource req = commReq.getParam();
		MsgSource msgSource = new MsgSource();
		if(StringUtil.isNotBlank(req.getState())){
			msgSource.setState(req.getState());
		}
		if(StringUtil.isNotBlank(req.getSourceName())){
			msgSource.setSourceName(req.getSourceName());
		}
		msgSource.setUpdateTime(DateOper.getNowDateTime());
		msgSource.setSourceId(req.getSourceId());
		msgSourceMapper.updateByPrimaryKeySelective(msgSource);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String EditMsgSource(InterfaceMessage msg) throws Exception {
		return this.EditMsgSource(new CommonReq<ReqEditMsgSource>(new ReqEditMsgSource(msg))).toResult();
	}

	@Override
	public CommonResp<RespMsgSource> MsgSourceList(CommonReq<ReqMsgSourceList> commReq) throws Exception {
		ReqMsgSourceList req = commReq.getParam();
		List<RespMsgSource> list = msgSourceMapper.queryMsgScene(req);
		return new CommonResp<RespMsgSource>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
	}

	@Override
	public String MsgSourceList(InterfaceMessage msg) throws Exception {
		return this.MsgSourceList(new CommonReq<ReqMsgSourceList>(new ReqMsgSourceList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> AddMsgUserOpenId(CommonReq<ReqAddMsgUserOpenId> commReq) throws Exception {
		ReqAddMsgUserOpenId req = commReq.getParam();
		MsgUserOpenId msgUserOpenId = new MsgUserOpenId();
		msgUserOpenId.setCardNo(req.getCardNo());
		msgUserOpenId.setCardType(req.getCardType());
		msgUserOpenId.setOpenId(req.getOpenId());
		msgUserOpenId.setOpenType(req.getOpenType());
		msgUserOpenId.setId(CommonUtil.getGUID());
		msgUserOpenId.setCreateTime(DateOper.getNowDateTime());
		msgUserOpenId.setUpdateTime(DateOper.getNowDateTime());
		msgUserOpenId.setOperatorId(req.getOperatorId());
		msgUserOpenId.setOperatorName(req.getOperatorName());
		msgUserOpenId.setState(req.getState());
		msgUserOpenId.setHosId(req.getOrgId());
		msgUserOpenIdMapper.insert(msgUserOpenId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String AddMsgUserOpenId(InterfaceMessage msg) throws Exception {
		return this.AddMsgUserOpenId(new CommonReq<ReqAddMsgUserOpenId>(new ReqAddMsgUserOpenId(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> EditMsgUserOpenId(CommonReq<ReqEditMsgUserOpenId> commReq) throws Exception {
		ReqEditMsgUserOpenId req = commReq.getParam();
		MsgUserOpenId msgUserOpenId = new MsgUserOpenId();
		if(StringUtil.isNotBlank(req.getState())){
			msgUserOpenId.setState(req.getState());
		}
		msgUserOpenId.setUpdateTime(DateOper.getNowDateTime());
		msgUserOpenId.setId(req.getId());
		msgUserOpenIdMapper.updateByPrimaryKeySelective(msgUserOpenId);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String EditMsgUserOpenId(InterfaceMessage msg) throws Exception {
		return this.EditMsgUserOpenId(new CommonReq<ReqEditMsgUserOpenId>(new ReqEditMsgUserOpenId(msg))).toResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CommonResp<MsgCenterPageHelper> MsgUserOpenIdList(CommonReq<ReqMsgUserOpenIdList> commReq) throws Exception {
		ReqMsgUserOpenIdList req = commReq.getParam();
		Integer pSize = req.getPage().getPSize();
		Integer pIndex = (req.getPage().getPIndex()-1)*pSize;
		int queryMsgUserOpenIdCount = msgUserOpenIdMapper.queryMsgUserOpenIdCount(req);
		List<RespMsgUserOpenId> queryMsgUserOpenIdPage = msgUserOpenIdMapper.queryMsgUserOpenIdPage(req,pIndex,pSize);
		MsgCenterPageHelper msgCenterPageHelper = new MsgCenterPageHelper();
		msgCenterPageHelper.setRows(queryMsgUserOpenIdPage);
		msgCenterPageHelper.setTotal(queryMsgUserOpenIdCount);
		return new CommonResp<MsgCenterPageHelper>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,msgCenterPageHelper);
	}

	@Override
	public String MsgUserOpenIdList(InterfaceMessage msg) throws Exception {
		return this.MsgUserOpenIdList(new CommonReq<ReqMsgUserOpenIdList>(new ReqMsgUserOpenIdList(msg))).toResult();
	}
	@Override
	public CommonResp<RespMsgQueueList> MsgQueueList(CommonReq<ReqMsgQueueList> commReq) throws Exception {
		ReqMsgQueueList req = commReq.getParam();
		List<RespMsgQueueList> list = msgQueueMapper.queryMsgList(req);
		List<RespMsgQueueList> list2 = msgQueueMapper.queryMsgRecordList(req);
		list.addAll(list2);
		ReqMsgSceneList r = new ReqMsgSceneList(commReq.getMsg(), req.getConfigKey());
		r.setState(1);
		r.setOrgId(req.getOrgId());
		List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgScene(r);		
		Map<String, String> map = new HashMap<>(queryMsgScene.size());
		for (RespMsgScene rm : queryMsgScene) {
			map.put(rm.getModeType(), rm.getSceneName());
		}
		for (RespMsgQueueList msg : list) {
			msg.setSceneName(map.get(msg.getModeType()));
			String sendTime = msg.getSendTime();
			if(StringUtil.isNotBlank(sendTime)) {
				sendTime = sendTime.substring(0, sendTime.length()-2);
			}
			msg.setSendTime(sendTime);
			msg.setMobile(com.kasite.core.common.util.StringUtil.mobileDesensitization(msg.getMobile()));
			msg.setChannelName(KasiteConfig.getClientConfig(ClientConfigEnum.clientName, msg.getChannelId()));
		}
		return new CommonResp<RespMsgQueueList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public CommonResp<MsgCenterPageHelper> MsgQueueListPage(CommonReq<ReqMsgQueueList> commReq) throws Exception {
		ReqMsgQueueList req = commReq.getParam();
		Integer pSize = req.getPage().getPSize();
		Integer pIndex = (req.getPage().getPIndex()-1)*pSize;
		List<RespMsgQueueList> queryMsgListUnion = msgQueueMapper.queryMsgListUnion(req, pIndex, pSize);
		List<RespMsgQueueList> queryMsgList  = new ArrayList<RespMsgQueueList>(); 
		ReqMsgSceneList r = new ReqMsgSceneList(commReq.getMsg(), req.getConfigKey());
		r.setState(1);
		r.setOrgId(req.getOrgId());
		List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgScene(r);		
		Map<String, String> map = new HashMap<>(queryMsgScene.size());
		for (RespMsgScene rm : queryMsgScene) {
			map.put(rm.getModeType(), rm.getSceneName());
		}
		int count = msgQueueMapper.queryMsgQueueCount(req);
		for (RespMsgQueueList msg : queryMsgListUnion) {
			msg.setSceneName(map.get(msg.getModeType()));
			String sendTime = msg.getSendTime();
			if(StringUtil.isNotBlank(sendTime)) {
				sendTime = sendTime.substring(0, sendTime.length()-2);
			}
			msg.setSendTime(sendTime);
			msg.setMobile(com.kasite.core.common.util.StringUtil.mobileDesensitization(msg.getMobile()));
			msg.setChannelName(KasiteConfig.getClientConfig(ClientConfigEnum.clientName, msg.getChannelId()));
			
			if("3".equals(req.getState())){//批量发送查询
				req.setState("0");
				req.setpId(msg.getMsgId());
				int unSendNum = msgQueueMapper.queryMsgQueueCount(req);
				req.setState("1");
				int successNum = msgQueueMapper.queryMsgQueueCount(req);
				req.setState("2");
				int failNum = msgQueueMapper.queryMsgQueueCount(req);
				msg.setUnSendNum(unSendNum);
				msg.setSuccessNum(successNum);
				msg.setFailNum(failNum);
				req.setState("");
				int totalNum = msgQueueMapper.queryMsgQueueCount(req);
				msg.setTotalNum(totalNum);
				queryMsgList.add(msg);
				req.setState("3");
			}
			else{
				if(msg.getState()!=3){
					queryMsgList.add(msg);
				}
				else{
					count--;
				}
			}
		}
		MsgCenterPageHelper<RespMsgQueueList> msgCenterPageHelper = new MsgCenterPageHelper<RespMsgQueueList>();
		msgCenterPageHelper.setRows(queryMsgList);
		msgCenterPageHelper.setTotal(count);
		return new CommonResp<MsgCenterPageHelper>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,msgCenterPageHelper);
	}

	@Override
	public String MsgQueueList(InterfaceMessage msg) throws Exception {
		return this.MsgQueueList(new CommonReq<ReqMsgQueueList>(new ReqMsgQueueList(msg))).toResult();
	}
	@Override
	public String MsgQueueListPage(InterfaceMessage msg) throws Exception {
		return this.MsgQueueListPage(new CommonReq<ReqMsgQueueList>(new ReqMsgQueueList(msg))).toResult();
	}
	@Override
	public CommonResp<Map<String, Object>> MsgCenterMainCount(CommonReq<ReqMsgCenterMainCount> commReq) throws Exception {
		ReqMsgCenterMainCount req = commReq.getParam();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if("1".equals(req.getType())){//1根据渠道统计发送数量
			list = msgCenterMainCountMapper.querySendCountByChn(req.getStartTime(), req.getEndTime(),req.getOrgId());
		}
		if("2".equals(req.getType())){//1根据来源统计发送数量
			list = msgCenterMainCountMapper.querySendCountByOperator(req.getStartTime(), req.getEndTime(),req.getOrgId());
		}
		if("3".equals(req.getType())){//1根据场景统计发送数量
			list = msgCenterMainCountMapper.querySendCountByModeType(req.getStartTime(), req.getEndTime(),req.getOrgId());
		}
		return new CommonResp<Map<String, Object>>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
	}

	@Override
	public String MsgCenterMainCount(InterfaceMessage msg) throws Exception {
		return this.MsgCenterMainCount(new CommonReq<ReqMsgCenterMainCount>(new ReqMsgCenterMainCount(msg))).toResult();
	}
	@Override
	public CommonResp<RespMsgScene> MsgOpenIdSceneList(CommonReq<ReqMsgOpenIdSceneList> commReq) throws Exception {
		ReqMsgOpenIdSceneList req = commReq.getParam();
		List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByMsgOpenIdSceneList(req);
		for (RespMsgScene respMsgScene : queryMsgScene) {
			int queryMsgOpenIdScene = msgOpenIdSceneMapper.queryMsgOpenIdScene(req.getOpenId(),respMsgScene.getModeType(),req.getOrgId());
			if(queryMsgOpenIdScene>0){
				respMsgScene.setState(1);//取消订阅
			}
			else{
				respMsgScene.setState(0);//订阅
			}
		}
		return new CommonResp<RespMsgScene>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,queryMsgScene);
	}

	@Override
	public String MsgOpenIdSceneList(InterfaceMessage msg) throws Exception {
		return this.MsgOpenIdSceneList(new CommonReq<ReqMsgOpenIdSceneList>(new ReqMsgOpenIdSceneList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> AddMsgOpenIdScene(CommonReq<ReqMsgOpenIdSceneList> commReq) throws Exception {
		ReqMsgOpenIdSceneList req = commReq.getParam();
		MsgOpenIdScene record = new MsgOpenIdScene();
		record.setId(CommonUtil.getGUID());
		record.setCreateTime(DateOper.getNowDateTime());
		record.setUpdateTime(DateOper.getNowDateTime());
		record.setModeType(req.getModeType());
		record.setOpenId(req.getOpenId());
		record.setSceneName(req.getSceneName());
		record.setState(req.getState());
		record.setHosId(req.getOrgId());
		msgOpenIdSceneMapper.insert(record);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String AddMsgOpenIdScene(InterfaceMessage msg) throws Exception {
		return this.AddMsgOpenIdScene(new CommonReq<ReqMsgOpenIdSceneList>(new ReqMsgOpenIdSceneList(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> DeleteMsgOpenIdScene(CommonReq<ReqMsgOpenIdSceneList> commReq) throws Exception {
		ReqMsgOpenIdSceneList req = commReq.getParam();
		MsgOpenIdScene record = new MsgOpenIdScene();
		record.setModeType(req.getModeType());
		record.setOpenId(req.getOpenId());
		msgOpenIdSceneMapper.delete(record);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String DeleteMsgOpenIdScene(InterfaceMessage msg) throws Exception {
		return this.DeleteMsgOpenIdScene(new CommonReq<ReqMsgOpenIdSceneList>(new ReqMsgOpenIdSceneList(msg))).toResult();
	}
	
	@Override
	public String PushRecipe(InterfaceMessage msg) throws Exception {
		//推送处方
		ReqPushRecipe req = new ReqPushRecipe(msg);
		if(null != req && null != req.getPushRecipe()) {
			CommonResp<HisRecipeClinicList> resp = req.getPushRecipe();
			ReqPushRecipeIn recipe = new ReqPushRecipeIn(msg,resp.getData());
			return this.pushRecipe(new CommonReq<ReqPushRecipeIn>(recipe)).toResult();
		}
		throw new RRException("推送的内容无法解析："+ msg.getParam());
	}

	@Override
	public CommonResp<RespMap> pushRecipe(CommonReq<ReqPushRecipeIn> req) throws Exception {
		InterfaceMessage msg = req.getMsg();
		List<HisRecipeClinicList> list = req.getParam().getList();
		String idCard = "";
		String doctorName = "";
		String deptName = "";
		String hisMemberId = "";
		String cardNo = "";
		String receiptName = "";
		String receiptNo = "";
		String receiptTime = "";
		String remark = "";
		String serviceId = "";
		Integer price = 0;
		for (HisRecipeClinicList rec : list) {
			serviceId = rec.getServiceId();
			deptName = rec.getReceiptDeptName();
			doctorName = rec.getReceiptDoctorName();
			idCard = rec.getIdCard();
			hisMemberId = rec.getHisMemberId();
			cardNo = rec.getCardNo();
			receiptName = rec.getReceiptName();
			receiptNo = rec.getReceiptNo();
			price = rec.getTotalPrice();
			receiptTime = rec.getReceiptTime();
			remark = rec.getRemark();
			if(com.kasite.core.common.util.StringUtil.isEmpty(remark)) {
				remark = "您有新的处方单据未缴费,请及时缴费。";
			}
			ReqQueryMemberList memberReq = new ReqQueryMemberList(msg, null, cardNo, "1", null, null, null, null, null, idCard, null, false, null, hisMemberId);
			CommonResp<RespQueryMemberList> memberResp = basicService.queryMemberList(new CommonReq<ReqQueryMemberList>(memberReq));
			List<RespQueryMemberList> memberList = memberResp.getData();
			if(memberList !=null && memberList.size() > 0) {
				for (RespQueryMemberList respQueryMember : memberList) {
					//下发消息
					Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
					XMLUtil.addElement(data1, "OpenId", respQueryMember.getOpId());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.UserName, respQueryMember.getMemberName());
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.DeptName, deptName);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.DoctorName, doctorName);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.Time, receiptTime);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.ReceiptNo, receiptNo);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.Price, price);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.ReceiptName, receiptName);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.Remark, remark);
					XMLUtil.addElement(data1, ApiKey.MODETYPE_10101125.URL, KasiteConfig.getServiceSuccessMessageUrl(BusinessTypeEnum.valuesOfCode(serviceId),
							req.getParam().getClientId(), req.getParam().getConfigKey(), null));
					
					//消息推送走消息中心
					ReqSendMsg queue = new ReqSendMsg(msg, respQueryMember.getCardNo(), 1, respQueryMember.getOpId(), "", respQueryMember.getMobile(),
							"", KstHosConstant.MODETYPE_10101125, data1.asXML(), respQueryMember.getOpId(), 1, respQueryMember.getOpId(), respQueryMember.getMemberName(), respQueryMember.getOpId(), "", respQueryMember.getMemberId(), "");
					CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
					CommonResp<RespMap> addMsgQueue = msgService.sendMsg(reqSendMsg);
					LogUtil.info(log, "发送处方开药消息：OpId="+respQueryMember.getOpId()+"|||Result="+addMsgQueue.getMessage(),msg.getAuthInfo());
					if (!"10000".equals(addMsgQueue.getCode())) {
						LogUtil.info(log, "发送处方开药消息异常：OpId="+respQueryMember.getOpId()+"|||Result="+addMsgQueue.getMessage(),msg.getAuthInfo());
					}
				}
			}
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQueryOpenId> QueryOpenId(CommonReq<ReqQueryOpenId> commReq) throws Exception {
		ReqQueryOpenId req = commReq.getParam();
		List<RespQueryOpenId> respQueryOpenId = msgUserOpenIdMapper.queryOpenIds(req);
		return new CommonResp<RespQueryOpenId>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respQueryOpenId);
	}

	@Override
	public String QueryOpenId(InterfaceMessage msg) throws Exception {
		return this.QueryOpenId(new CommonReq<ReqQueryOpenId>(new ReqQueryOpenId(msg))).toResult();
	}

	@Override
	public CommonResp<RespMap> ReadMsg(CommonReq<ReqReadMsg> commReq) throws Exception {
		ReqReadMsg req = commReq.getParam();
		MsgQueueRecord record = new MsgQueueRecord();
		record = msgQueueRecordMapper.selectByPrimaryKey(req.getMsgId());
		if(record!=null){
			record.setIsRead(1);
			record.setUpdateTime(DateOper.getNowDateTime());
		}
		msgQueueRecordMapper.updateByPrimaryKey(record);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String ReadMsg(InterfaceMessage msg) throws Exception {
		return this.ReadMsg(new CommonReq<ReqReadMsg>(new ReqReadMsg(msg))).toResult();
	}
	
		
}
