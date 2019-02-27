package com.kasite.client.core.msg.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMobilePublicMessageCustomSendRequest;
import com.alipay.api.request.AlipayMobilePublicMessageSingleSendRequest;
import com.alipay.api.request.AlipayOpenPublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayMobilePublicMessageCustomSendResponse;
import com.alipay.api.response.AlipayOpenPublicMessageCustomSendResponse;
import com.coreframework.util.DateOper;
import com.kasite.client.core.msg.bean.dbo.MsgQueue;
import com.kasite.client.core.msg.bean.dbo.MsgQueueRecord;
import com.kasite.client.core.msg.bean.dbo.MsgTemp;
import com.kasite.client.core.msg.dao.IMsgOpenIdSceneMapper;
import com.kasite.client.core.msg.dao.IMsgQueueMapper;
import com.kasite.client.core.msg.dao.IMsgQueueRecordMapper;
import com.kasite.client.core.msg.dao.IMsgSceneMapper;
import com.kasite.client.core.msg.dao.IMsgTempMapper;
import com.kasite.client.core.msg.dao.IMsgUserOpenIdMapper;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.config.MsgCenterConfig;
import com.kasite.core.common.config.MsgCenterConfigVo;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.HttpsClientUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.msg.resp.RespMsgScene;
import com.kasite.core.serviceinterface.module.wechat.IWeiXinService;

import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;

/**
 * 消息队列
 * 
 * @author zwl
 * @version 1.0 2018-11-14 上午10:58:08
 */
@Component
public class MsgQueueJob {

	private boolean flag = true;
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	private final static int MaxSendCount = 5;

	@Autowired
	IMsgQueueMapper msgQueueMapper;
	@Autowired
	IMsgQueueRecordMapper msgQueueRecordMapper;
	@Autowired
	IMsgTempMapper msgTempMapper;
	@Autowired
	protected IMsgSceneMapper msgSceneMapper;
	@Autowired
	KasiteConfigMap config;
	@Autowired
	IWeiXinService weiXinService;
	@Autowired
	protected IMsgUserOpenIdMapper msgUserOpenIdMapper;
	@Autowired
	MsgCenterConfig msgConfig;
	@Autowired
	IMsgOpenIdSceneMapper msgOpenIdSceneMapper;
	public void execute(){
		MsgCenterQueue();
	}
	public static void main(String[] args) {
		AlipayOpenPublicMessageCustomSendRequest  request = new AlipayOpenPublicMessageCustomSendRequest ();
		request.setBizContent("{\"toUserId\":\"20880079365201500359533180211598\",\"msgType\":\"text\",\"text\":{\"content\":\"文本消息\"}}");
    	
		try {
			AlipayOpenPublicMessageCustomSendResponse response = new DefaultAlipayClient(AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
					"2016022501162092", //2016022501162092
			        "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALZ6+ev7oBTWKNSdRBdePYIIWV2KXdyfITaidGmWXD/Ohz1/xHXWp0FXp1acgar0Ix8+P78G/wbP/SRyAbnQF2q7MPezfXewYFkEywUBP5f4WRn8N3fsO6Iwb/g/MuUx/lEhPKPcTS5Fgc8ZtLVTYE6uY3wpnBEaUBvCwHhW1alZAgMBAAECgYAZInk4MvGKzTDUkFDnsxhfx8yfYMaq89Q8VUYZoRdVxpnEM8wYuxdQncUz3dzSckxAKm/XXRxVkOJ6WaW5NVt0IStkMSKV5G2ZHJCmQU13xPA+ghQnF+CteU/ciivxdk48IpStl4quinfrPeMmFBR6YfGIA5gyCgRcMhNh2jUanQJBAPFK11R5cYAMMSO3O4nUHViHp/rO+jxOx1dw6cblzSA/3xdP30JN7k0BXnYoBvDPHbCkFYOw6KbBOkSKwA65WM8CQQDBmmzQJcVjIoL3AYAS5MxfcScSJlofqujM/aSL+Aa8FDESm68DivlhRssq3o4/VDvlyVP0qqadvtvdkOWyWJVXAkEAu26Dbd8YR3sxAKlo2lumoApgdfcpcodPWaEgN5xyhsaWqSkmJ/8ZST/y/J2DJrP8QCZ1f+KAFmiqtmuBXO6lCQJBAIpF18eF4odeQ7lUyfs0jD7yWtxcpeuOLn0R/u6082Jq7W2D4aifHmN6o2p3z5Ktf6Yrd5n8M8ngYbnKXjNVjzsCQQDqbl3LX/i9yRk/yBQu1JTfEbLFj0C5hJJsZRhSOSu7W9YubN4CxjcanYTSdzwwVT1yILwhsJwQz+gigYzo8Ei/", 
			        "json", 
			        "utf-8",
			        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB", 
			       "RSA").execute(request);
			System.out.println(response.getMsg());
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	/**
	 * 消息队列
	 * 每晚30秒执行一次
	 */
	@Transactional
	public void MsgCenterQueue() {
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				List<MsgQueue> msgQueues = msgQueueMapper.querySendMsg();
				for (MsgQueue msgQueue : msgQueues) {
					SendMsg(msgQueue);
				}
			}
		} catch (Exception e) {
			LogUtil.error(log, e);
			log.error(e);
			e.printStackTrace();
		} finally {
			flag = true;
		}
	}
	public void SendMsg(MsgQueue msgQueue){
		MsgQueueRecord msgQueueRecord = null;
		try {
			String errcode = "";
			String errmsg = "";
			String modeId = msgQueue.getModeId();
			List<MsgTemp> list = new ArrayList<MsgTemp>();
			List<RespMsgScene> queryMsgScene = msgSceneMapper.queryMsgSceneByModeType( msgQueue.getModelType(),1);
			if(queryMsgScene!=null&&queryMsgScene.size()>0){
				msgQueue.setSceneName(queryMsgScene.get(0).getSceneName());
			}
			else{//场景无效的直接视为失败
				String msg = "无效的消息场景";
				LogUtil.info(log, msg);
				msgQueue.setPushResult(msg);
				msgQueue.setSendCount(msgQueue.getSendCount()+1);
				msgQueue.setState(2);
				msgQueue.setUpdateTime(DateOper.getNowDateTime());
				msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
				msgQueueRecord = new MsgQueueRecord(msgQueue);
				msgQueueRecordMapper.insert(msgQueueRecord);
				return;
			}
			if(modeId!=null&&!"".equals(modeId)){
				Example example = new Example(MsgTemp.class);
				example.createCriteria().andEqualTo("modeId", modeId).andEqualTo("state",1);
				list = msgTempMapper.selectByExample(example);
			}
			else{
				Example example = new Example(MsgTemp.class);
				example.createCriteria().andEqualTo("useChannel", msgQueue.getChannelId()).
				andEqualTo("modeType", msgQueue.getModelType()).andEqualTo("state",1).andEqualTo("userHos",msgQueue.getHosId());
				list = msgTempMapper.selectByExample(example);
			}
			if (null == list || list.size() <= 0) {
				String msg = "未找到模板Id为" + modeId + "或场景为:"+msgQueue.getModelType()+"的模板";
				LogUtil.info(log, msg);
				msgQueue.setPushResult(msg);
				msgQueue.setSendCount(msgQueue.getSendCount()+1);
				msgQueue.setState(2);
				msgQueue.setUpdateTime(DateOper.getNowDateTime());
				msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
				msgQueueRecord = new MsgQueueRecord(msgQueue);
				msgQueueRecordMapper.insert(msgQueueRecord);
				return;
			}
			for (MsgTemp msgTemp : list) {
				try {
					String msgContent = CommonUtil.getMsgContent(msgTemp.getPushMode(), DocumentHelper.parseText(msgQueue.getMsgContent()).getRootElement());
					if(StringUtil.isNotBlank(msgQueue.getOpenId())){
						if(StringUtil.isNotBlank(msgTemp.getModeType())){
							int queryMsgOpenIdScene = msgOpenIdSceneMapper.queryMsgOpenIdScene(msgQueue.getOpenId(),msgQueue.getModeType(),msgQueue.getHosId());
							if(queryMsgOpenIdScene>0){
								String msg = "用户取消订阅了模板Id为" + modeId + "或场景为:"+msgQueue.getModelType()+"的模板";
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
						}
						msgContent = msgContent.replace("<openId>", msgQueue.getOpenId());
						sendMsg(msgTemp, msgQueue, msgContent, msgQueueRecord);
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
						for (Iterator<String> iterator = openIds.iterator(); iterator.hasNext();) {
							String openId = iterator.next();
							if(StringUtil.isNotBlank(msgTemp.getModeType())){
								int queryMsgOpenIdScene = msgOpenIdSceneMapper.queryMsgOpenIdScene(msgQueue.getOpenId(),msgQueue.getModeType(),msgQueue.getHosId());
								if(queryMsgOpenIdScene>0){
									String msg = "用户取消订阅了模板Id为" + modeId + "或场景为:"+msgQueue.getModelType()+"的模板";
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
							}
							msgContent = msgContent.replace("<openId>", openId);
							sendMsg(msgTemp, msgQueue, msgContent, msgQueueRecord);
						}
					}
					
				} catch (Exception e) {
					try {
						String message = e.getMessage();
						if(message.length()>299){
							message = message.substring(0, 298);
						}
						msgQueue.setPushResult(message);
						msgQueue.setSendTime(DateOper.getNowDateTime());
						msgQueue.setSendCount(msgQueue.getSendCount()+1);
						msgQueue.setUpdateTime(DateOper.getNowDateTime());
						if(MaxSendCount<msgQueue.getSendCount()){
							msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
							msgQueueRecord = new MsgQueueRecord(msgQueue);
							msgQueueRecord.setState(2);//失败
							msgQueueRecordMapper.insert(msgQueueRecord);
						}
						else{
							msgQueueMapper.updateByPrimaryKeySelective(msgQueue);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			try {
				String message = e.getMessage();
				if(message.length()>299){
					message = message.substring(0, 298);
				}
				msgQueue.setPushResult(message);
				msgQueue.setSendCount(msgQueue.getSendCount()+1);
				msgQueue.setUpdateTime(DateOper.getNowDateTime());
				if(MaxSendCount<msgQueue.getSendCount()){
					msgQueueMapper.deleteByPrimaryKey(msgQueue.getMsgId());
					msgQueueRecord = new MsgQueueRecord(msgQueue);
					msgQueueRecord.setState(2);//失败
					msgQueueRecordMapper.insert(msgQueueRecord);
				}
				else{
					msgQueueMapper.updateByPrimaryKeySelective(msgQueue);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public void sendMsg(MsgTemp msgTemp,MsgQueue msgQueue,String msgContent,MsgQueueRecord msgQueueRecord) throws Exception{
		String errcode="";
		String errmsg="";
		JSONObject resJs = null;
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
						resJs = JSONObject.fromObject(response.getResult());
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
						resJs = JSONObject.fromObject(response.getResult());
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
						resJs = JSONObject.fromObject(response.getResult());
					}
					
				}
				else{
					resJs = AlipayService.sendSingleMessage(msgTemp.getConfigKey(), msgContent);
				}
				
			}  else if ("2".equals(msgTemp.getMsgType())) {// 1:文本
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
						resJs = JSONObject.fromObject(response.getResult());
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
			if(MaxSendCount<msgQueue.getSendCount()){
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
}
