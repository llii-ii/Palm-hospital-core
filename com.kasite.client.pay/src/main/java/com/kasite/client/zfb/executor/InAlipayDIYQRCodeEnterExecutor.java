/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageCustomSendResponse;
import com.kasite.client.zfb.factory.AlipayAPIClientFactory;
import com.kasite.client.zfb.util.AlipayMsgBuildUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.SpringContextUtil;

import net.sf.json.JSONObject;

/**
 * 自定义二维码进入服务窗事件处理器
 * 
 * @author taixu.zqq
 * @version $Id: InAlipayDIYQRCodeEnterExecutor.java, v 0.1 2014年7月24日 下午9:22:02 taixu.zqq Exp $
 */
public class InAlipayDIYQRCodeEnterExecutor implements ActionExecutor {
	
	private static final String SCENEID_1 = "1";
	private static final String SCENEID_2 = "2";

    /** 业务参数 */
    private JSONObject             bizContent;

    public InAlipayDIYQRCodeEnterExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayDIYQRCodeEnterExecutor() {
        super();
    }

    /** 
     * @see com.alipay.executor.ActionExecutor#executor(java.util.Map)
     */
    @Override
    public String execute(AuthInfoVo authInfo) throws Exception {
        //自身业务处理
        //理论上，自定义二维码会有sceneId设置，通过该id，开发者开始知道是哪个自定义二维码进入
    	String zfbKey = authInfo.getConfigKey();
   	 	//从spring 配置获取线程池类
        String syncResponseMsg = "";
        try {
            JSONObject actionParam = JSONObject.fromObject(bizContent.getString("ActionParam"));
            JSONObject scene = JSONObject.fromObject(actionParam.get("scene"));
            String sceneId = scene.getString("sceneId");
            KasiteConfig.print("sceneId:" + sceneId);

            //取得发起请求的支付宝账号id
            final String fromUserId = bizContent.getString("FromUserId");

            //1. 首先同步构建ACK响应
            syncResponseMsg = AlipayMsgBuildUtil.buildBaseAckMsg(zfbKey,fromUserId);
            
            //2. 异步发送消息，根据不同的sceneId推送不同的消息（这里的sceneId的意义由商户自己定义）
            if(SCENEID_1.equals(sceneId)){
            	 KstHosConstant.cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // 2.1 构建一个业务响应消息，开发者根据自行业务构建，这里只是一个简单的样例
                            String requestMsg = "{'msg_type':'text','text':{'content':'通过sceneId为1的二维码关注服务窗'}, 'to_user_id':'" + fromUserId + "'}";

                            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
                            AlipayOpenPublicMessageCustomSendRequest request = new AlipayOpenPublicMessageCustomSendRequest();
                            request.setBizContent(requestMsg);

                            // 2.2 使用SDK接口类发送响应
                            AlipayOpenPublicMessageCustomSendResponse response = alipayClient
                                .execute(request);

                            // 2.3 开发者根据响应结果处理结果
                            //这里只是简单的打印，请开发者根据实际情况自行进行处理
                            if (null != response && response.isSuccess()) {
                            	KasiteConfig.print("异步发送成功，结果为：" + response.getBody());
                            } else {
                            	KasiteConfig.print("异步发送失败 code=" + response.getCode() + "msg：" + response.getMsg());
                            }
                        } catch (Exception e) {
                        	KasiteConfig.print("异步发送失败");
                        }
                    }
                });
            }else if (SCENEID_2.equals(sceneId)) {
            	
            	KstHosConstant.cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // 2.1 构建一个业务响应消息，开发者根据自行业务构建，这里只是一个简单的样例
                            String requestMsg = "{'msg_type':'text','text':{'content':'通过sceneId为2的二维码关注服务窗'}, 'to_user_id':'" + fromUserId + "'}";

                            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(zfbKey);
                            AlipayOpenPublicMessageCustomSendRequest request = new AlipayOpenPublicMessageCustomSendRequest();
                            request.setBizContent(requestMsg);

                            // 2.2 使用SDK接口类发送响应
                            AlipayOpenPublicMessageCustomSendResponse response = alipayClient
                                .execute(request);

                            // 2.3 开发者根据响应结果处理结果
                            //这里只是简单的打印，请开发者根据实际情况自行进行处理
                            if (null != response && response.isSuccess()) {
                            	KasiteConfig.print("异步发送成功，结果为：" + response.getBody());
                            } else {
                            	KasiteConfig.print("异步发送失败 code=" + response.getCode() + "msg：" + response.getMsg());
                            }
                        } catch (Exception e) {
                        	KasiteConfig.print("异步发送失败");
                        }
                    }
                });
			}
        } catch (Exception exception) {
            throw new Exception("转换json错误，检查数据格式");
        }

        // 同步返回ACK响应
        return syncResponseMsg;
    }
}
