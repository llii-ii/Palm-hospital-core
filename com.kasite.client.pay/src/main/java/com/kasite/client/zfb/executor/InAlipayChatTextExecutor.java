/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageCustomSendResponse;
import com.coreframework.util.DateOper;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.client.zfb.util.AlipayMsgBuildUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DataUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * 聊天执行器(纯文本消息)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayChatExecutor.java, v 0.1 Jul 28, 2014 5:17:04 PM baoxing.gbx Exp $
 */
public class InAlipayChatTextExecutor implements ActionExecutor {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MSG);
	private IMsgService msgService = SpringContextUtil.getBean(IMsgService.class);
	
    /** 业务参数 */
    private JSONObject  bizContent;

    public InAlipayChatTextExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayChatTextExecutor() {
        super();
    }

    /**
     * 
     * @see com.alipay.executor.ActionExecutor#execute()
     */
    @Override
    public String execute(AuthInfoVo authInfo) throws Exception {

        //取得发起请求的支付宝账号id
        final String fromUserId = bizContent.getString("FromUserId");

        //1. 首先同步构建ACK响应
        String syncResponseMsg = AlipayMsgBuildUtil.buildBaseAckMsg(authInfo.getConfigKey(),fromUserId);
        //从spring 配置获取线程池类
//        ThreadPoolTaskExecutor executors = KstHosConstant.cachedThreadPool;//(ThreadPoolTaskExecutor) SpringContextUtil.getBean(ThreadPoolTaskExecutor.class);
        //2. 异步发送消息
        KstHosConstant.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                	JSONObject textObj = (JSONObject) bizContent.get("Text");
                	String inputText =textObj.getString("Content") .toString();
                	String api = ApiModule.Msg.QueryAutoReplayArbitrarily.getName();
    				Document document = DocumentHelper.createDocument();
    				Element req = document.addElement(KstHosConstant.REQ);
    				XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "15102");
    				Element data = req.addElement(KstHosConstant.DATA);
    				XMLUtil.addElement(data, "KeyWord",inputText );
    		        XMLUtil.addElement(data, "State", "1");
    	            
    	            InterfaceMessage msg = new InterfaceMessage();
    				msg.setApiName(api);
    				msg.setParam(document.asXML());
    				msg.setParamType(KstHosConstant.INTYPE);
    				msg.setOutType(KstHosConstant.OUTTYPE);
    				msg.setAuthInfo(authInfo.toString());
    				msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
    				msg.setVersion(KstHosConstant.V);
    				if(null != msgService) {
    					String result = msgService.QueryAutoReplayArbitrarily(msg);
        				result = DataUtils.xml2JSON(result);
        				
        				JSONObject resultJson=JSONObject.fromObject(result);
        				int respCode=resultJson.getInt("RespCode");
        				String replayMsg=null;
        				if(respCode==Integer.valueOf(KstHosConstant.SUCCESSCODE)){
        					JSONObject dataObj =  resultJson.getJSONObject("Data");
        					replayMsg=dataObj.getString("ReplayMsg");
        					if(StringUtil.isEmpty(replayMsg)){
        						replayMsg="抱歉,没查询到相关信息!";
        					 }
        				    }else{
        				    	replayMsg="抱歉,没查询到相关信息!";
        				    }
                    	 // 2.1 构建一个业务响应消息，商户根据自行业务构建，这里只是一个简单的样例
                    	StringBuilder sb = new StringBuilder();
                        sb.append("{'msg_type':'text','text':{'content':'"+replayMsg+"'}, 'to_user_id':'" + fromUserId+ "'}");

//                        if(null != replayMsg && replayMsg.equals("initALLMenu_dys3.2603")) {
                        //	AlipayService.createMenu(authInfo.getConfigKey());
//                        }
                        
                        
                        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(authInfo.getConfigKey());
                        AlipayOpenPublicMessageCustomSendRequest request = new AlipayOpenPublicMessageCustomSendRequest();
                        request.setBizContent(sb.toString());

                        // 2.2 使用SDK接口类发送响应
                        AlipayOpenPublicMessageCustomSendResponse response = alipayClient.execute(request);

                        // 2.3 商户根据响应结果处理结果
                        //这里只是简单的打印，请商户根据实际情况自行进行处理
                        if (null != response && response.isSuccess()) {
                        	LogUtil.info(log, "【"+result+ "】消息发送成功.",authInfo);
                        	KasiteConfig.print("异步发送成功，结果为：" + response.getBody());
                        } else {
                        	LogUtil.info(log, "消息发送异常",authInfo);
                        	KasiteConfig.print("异步发送失败 code=" + response.getCode() + "msg："
                                               + response.getMsg());
                        }

    				}else {
    					LogUtil.info(log, "未实现消息通知接口 IMsgService ",authInfo);
    				}
                } catch (Exception e) {
                	e.printStackTrace();
                	LogUtil.error(log,e,authInfo);
                	KasiteConfig.print("异步发送失败");
                }
            }
        });

        // 3.返回同步的ACK响应
        return syncResponseMsg;
    }

}
