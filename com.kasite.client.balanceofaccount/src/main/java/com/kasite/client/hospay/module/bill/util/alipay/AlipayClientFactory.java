package com.kasite.client.hospay.module.bill.util.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author cc
 */
@Component
public class AlipayClientFactory {

	@Autowired
    RequestHandlerParam requestHandlerParam;
	
	/**
	 *  SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
	 */
	@PostConstruct
	public AlipayClient init(){
		AlipayClient client = new DefaultAlipayClient(
			requestHandlerParam.alipayGateWay,
			requestHandlerParam.zfbAppId,
			requestHandlerParam.privateKey,
			"json",
			Constant.DEF_ENCODING,
			requestHandlerParam.alipayPublicKey,
			requestHandlerParam.signType
		);
		return client;
	}


}