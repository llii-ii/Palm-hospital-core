/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageCustomSendResponse;
import com.coreframework.util.DateOper;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.client.zfb.util.AlipayMsgBuildUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.DataUtils;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;


/**
 * 关注服务窗执行器
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayFollowExecutor.java, v 0.1 Jul 24, 2014 4:29:04 PM baoxing.gbx Exp $
 */
public class InAlipayFollowExecutor implements ActionExecutor {
	
	/** 业务参数 */
    private JSONObject bizContent;
    
    private IReportFormsService reportFormService = SpringContextUtil.getBean(IReportFormsService.class);
    private IMsgService msgService = SpringContextUtil.getBean(IMsgService.class);

    public InAlipayFollowExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayFollowExecutor() {
        super();
    }

    @Override
    public String execute(AuthInfoVo authInfo) {

    	String zfbKey = authInfo.getConfigKey();
   	 	//从spring 配置获取线程池类
        //取得发起请求的支付宝账号id
        final String fromUserId = bizContent.getString("FromUserId");

        //1. 首先同步构建ACK响应
        String syncResponseMsg = AlipayMsgBuildUtil.buildBaseAckMsg(zfbKey,fromUserId);

        //2. 异步发送消息
        KstHosConstant.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                	//关注数据统计
                	String dcApi = ApiModule.ReportForms.DataCollection.getName();;
					Document document = DocumentHelper.createDocument();
					Element req = document.addElement(KstHosConstant.REQ);
					XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "7006");
					Element service = req.addElement(KstHosConstant.DATA);
					XMLUtil.addElement(service, "ChannelId", KstHosConstant.ZFB_CHANNEL_ID);
					XMLUtil.addElement(service, "ChannelName", KstHosConstant.ZFB_CHANNEL_NAME);
					XMLUtil.addElement(service, "Api", "");
					XMLUtil.addElement(service, "DataType", "1");
					XMLUtil.addElement(service, "DataValue", 1);
					XMLUtil.addElement(service, "Remark", null);
					
					
    	            InterfaceMessage msg = new InterfaceMessage();
    				msg.setApiName(dcApi);
    				msg.setParam(document.asXML());
    				msg.setParamType(KstHosConstant.INTYPE);
    				msg.setOutType(KstHosConstant.OUTTYPE);
    				msg.setAuthInfo(authInfo.toString());
    				msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
    				msg.setVersion(KstHosConstant.V);
    				String result = reportFormService.DataCollection(msg);
					
					KasiteConfig.print("InAlipayFollowExecutor-DataCollection:"+result);
					
					//云报表数据收集
					dcApi = ApiModule.ReportForms.DataCloudCollection.getName();
					
					Document document2 = DocumentHelper.createDocument();
					Element req2 = document2.addElement(KstHosConstant.REQ);
					XMLUtil.addElement(req2, KstHosConstant.TRANSACTIONCODE, "7006");
					Element service2 = req2.addElement(KstHosConstant.DATA);
					XMLUtil.addElement(service2, "ChannelId", KstHosConstant.ZFB_CHANNEL_ID);
					XMLUtil.addElement(service2, "DataType", 101);
					XMLUtil.addElement(service2, "DataCount", 1);
					XMLUtil.addElement(service2, "TargetType", "1");
					
					msg = new InterfaceMessage();
    				msg.setApiName(dcApi);
    				msg.setParam(document2.asXML());
    				msg.setParamType(KstHosConstant.INTYPE);
    				msg.setOutType(KstHosConstant.OUTTYPE);
    				msg.setAuthInfo(authInfo.toString());
    				msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
    				msg.setVersion(KstHosConstant.V);
    				String result2 = reportFormService.DataCloudCollection(msg);
					
					
					KasiteConfig.print("--result2-->"+result2);
					
					
					
					KasiteConfig.print("InAlipayChatTextExecutor"+bizContent.toString());
                	// 回复文本消息  
				    String api = ApiModule.Msg.QueryAutoReplayByFollow.getName();
					Document replayDoc = DocumentHelper.createDocument();
					Element replayReqEl = replayDoc.addElement(KstHosConstant.REQ);
					XMLUtil.addElement(replayReqEl, KstHosConstant.TRANSACTIONCODE, "15102");
					Element replayDataEl = replayReqEl.addElement(KstHosConstant.DATA);
					XMLUtil.addElement(replayDataEl, "ReplayType", "1");
			        XMLUtil.addElement(replayDataEl, "State", "1");
			        
			        
			        msg = new InterfaceMessage();
    				msg.setApiName(api);
    				msg.setParam(replayDoc.asXML());
    				msg.setParamType(KstHosConstant.INTYPE);
    				msg.setOutType(KstHosConstant.OUTTYPE);
    				msg.setAuthInfo(authInfo.toString());
    				msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
    				msg.setVersion(KstHosConstant.V);
    				String res = msgService.QueryAutoReplayByFollow(msg);
    				res = DataUtils.xml2JSON(res);
			        JSONObject resultJson=JSONObject.fromObject(res);
			        
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

                    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
                    AlipayOpenPublicMessageCustomSendRequest request = new AlipayOpenPublicMessageCustomSendRequest();
                    request.setBizContent(sb.toString());

                    // 2.2 使用SDK接口类发送响应
                    AlipayOpenPublicMessageCustomSendResponse response = alipayClient
                        .execute(request);

                    // 2.3 商户根据响应结果处理结果
                    //这里只是简单的打印，请商户根据实际情况自行进行处理
                    if (null != response && response.isSuccess()) {
                    	KasiteConfig.print("异步发送成功，结果为：" + response.getBody());
                    } else {
                    	KasiteConfig.print("异步发送失败 code=" + response.getCode() + "msg："
                                           + response.getMsg());
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                	KasiteConfig.print("异步发送失败");
                }
            }
        });

        // 3.返回同步的ACK响应
        return syncResponseMsg;
    }
}
