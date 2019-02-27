/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;

import com.kasite.client.zfb.util.AlipayMsgBuildUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;

import net.sf.json.JSONObject;


/**
 * 普通进入服务窗事件处理器
 * 用户进入服务窗，可在此处理器处理开发者需要的业务逻辑
 * 
 * @author taixu.zqq
 * @version $Id: InAlipayEnterExecutor.java, v 0.1 2014年7月24日 下午7:58:25 taixu.zqq Exp $
 */
public class InAlipayEnterExecutor implements ActionExecutor {

    /** 业务参数 */
    private JSONObject bizContent;

    public InAlipayEnterExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayEnterExecutor() {
        super();
    }

    /** 
     * @see com.alipay.executor.ActionExecutor#executor(java.util.Map)
     */
    @Override
    public String execute(AuthInfoVo authInfo) throws Exception {
        //自身业务处理,这里只是简单打印下
        //建议开发者自行处理采用异步方式，参见InAlipayChatTextExecutor
    	KasiteConfig.print("欢迎光临！");

        // 同步返回ack响应
        return this.setResponse(authInfo.getConfigKey());
    }

    /**
     * 设置response返回数据
     * 
     * @return
     */
    private String setResponse(String zfbKey) throws Exception {

        //取得发起请求的支付宝账号id
        String fromUserId = bizContent.getString("FromUserId");

        return AlipayMsgBuildUtil.buildBaseAckMsg(zfbKey,fromUserId);
    }
}
