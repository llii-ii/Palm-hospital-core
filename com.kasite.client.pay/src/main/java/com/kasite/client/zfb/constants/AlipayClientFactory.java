package com.kasite.client.zfb.constants;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.ZFBConfigEnum;

public class AlipayClientFactory {

	public static AlipayClient getAlipayClientInstance2(String conigKey) {
		return new DefaultAlipayClient(
				AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, conigKey),
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appPrivateKey, conigKey),
				"json",
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_charset, conigKey),
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, conigKey),
				AlipayServiceEnvConstants.SIGNTYPE_RSA2
				);
	}
	
	public static AlipayClient getAlipayClientInstance(String conigKey) {
		return new DefaultAlipayClient(
				AlipayServiceEnvConstants.ALIPAY_GATEWAY, 
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, conigKey),
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appPrivateKey, conigKey),
				"json",
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_charset, conigKey),
				KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_alipayPublicKey, conigKey),
				AlipayServiceEnvConstants.SIGNTYPE_RSA
				);
	}
	
}
