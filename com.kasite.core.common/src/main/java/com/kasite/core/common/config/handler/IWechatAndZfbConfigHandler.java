package com.kasite.core.common.config.handler;

import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.DragonPayEnum;
import com.kasite.core.common.config.NetPayEnum;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.config.UnionPayEnum;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;

public interface IWechatAndZfbConfigHandler {
	String getWeChatConfig(WXConfigEnum key,String id);
	String getWeChatPay(WXPayEnum key,String id);
	String getZfbConfig(ZFBConfigEnum key,String id);
	String getUnionPayConfig(UnionPayEnum key,String id);
	String getSwiftpassConfig(SwiftpassEnum key,String id);
	String getNetPayConfig(NetPayEnum key,String id);
	String getDragonPayConfig(DragonPayEnum key,String id);
	/**
	 * 获取支付回调地址
	 * @param channelType
	 * @param configKey
	 * @param clientId
	 * @param openId
	 * @param token
	 * @return
	 */
	String getPayCallBackUrl(ChannelTypeEnum channelType,String configKey,String clientId,String openId,String token,String orderId) ;

}
