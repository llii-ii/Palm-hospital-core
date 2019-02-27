/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.factory;

import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;


/**
 * API调用客户端工厂
 * 
 * @author taixu.zqq
 * @version $Id: AlipayAPIClientFactory.java, v 0.1 2014年7月23日 下午5:07:45 taixu.zqq Exp $
 */
public class AlipayAPIClientFactory {

    /** API调用客户端 */
    private static Map<String, AlipayClient> alipayClientMap = new HashMap<>();
    
    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public static AlipayClient getAlipayClient(String zfbKey){
        //AlipayClient alipayClient = new DefaultAlipayClient(gateway,app_id,private_key,"json",charset,alipay_public_key,sign_type);
    	AlipayClient client = alipayClientMap.get(zfbKey);
    	if(null == client) {
    		client = new DefaultAlipayClient(AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
    				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, zfbKey), 
    		        KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appPrivateKey, zfbKey), 
    		        "json", 
    		        KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_charset, zfbKey),
    		        KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, zfbKey), 
    		        KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_signType, zfbKey)
    		        );
    		alipayClientMap.put(zfbKey, client);
    	}
        return client;
    }
}
