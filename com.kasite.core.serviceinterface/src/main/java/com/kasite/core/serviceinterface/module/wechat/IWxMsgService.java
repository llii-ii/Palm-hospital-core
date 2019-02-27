package com.kasite.core.serviceinterface.module.wechat;

import com.kasite.core.serviceinterface.module.wechat.req.WxMsg;
import com.kasite.core.serviceinterface.module.wechat.req.WxPayMsg;
import com.kasite.core.serviceinterface.module.wechat.req.ZfbMsg;

/**
 * 微信消息处理
 * @author daiyanshui
 *
 */
public interface IWxMsgService {

	void save(String appId,String wxMsg) throws Exception;

	void parseWxMsg(String wxAppId, int wx_event_type, WxMsg wxMsg) throws Exception;
	
	void parseWxPayMsg(String wxAppId, WxPayMsg wxPayMsg) throws Exception;
	
	void parseZfbMsg(String zfbAppId, int zfb_event_type, ZfbMsg zfbMsg) throws Exception;
	
	
}
