package com.kasite.core.serviceinterface.module.pay;

import java.util.HashMap;
import java.util.Map;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.log.NetworkUtil;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IMiniPayService;
import com.kasite.core.serviceinterface.module.order.ISmartPayService;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetGuide;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 支付的时候获取支付配置 configkey
 * @author daiyanshui
 *
 */
public class GetPayConfigKeyUtil {

	
	public static RespMap get(String clientId,String guideId,String orderId,AuthInfoVo vo) throws Exception {
		RespMap configKeyResp = new RespMap();
		String wxPayConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
		String zfbConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
		String netConfigKey = KasiteConfig.getClientConfig(ClientConfigEnum.NetPayConfigKey, clientId);
		if(StringUtil.isNotBlank(wxPayConfigKey) && wxPayConfigKey.split(",").length > 1) {
			wxPayConfigKey = wxPayConfigKey.split(",")[0];
		}
		if(StringUtil.isNotBlank(zfbConfigKey) && zfbConfigKey.split(",").length > 1) {
			zfbConfigKey = zfbConfigKey.split(",")[0];
		} 
		if(StringUtil.isNotBlank(netConfigKey) && netConfigKey.split(",").length > 1) {
			netConfigKey = netConfigKey.split(",")[0];
		} 
		IMiniPayService service = null;
		if(KstHosConstant.MINIPAY_CHANNEL_ID.equalsIgnoreCase(clientId)) {
			service = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode(), IMiniPayService.class);
		}
		//如果是mini付渠道，需要通过生成的信息点进行判断是用哪个支付商户进行支付
		if(null != service && KstHosConstant.MINIPAY_CHANNEL_ID.equalsIgnoreCase(clientId)) {
			InterfaceMessage message = new InterfaceMessage();
			message.setAuthInfo(vo.toString());
			message.setOutType(0);//返回JSON
			message.setParamType(0);
			message.setSeq(Long.toString(System.currentTimeMillis()));
			message.setClientIp(NetworkUtil.getLocalIP());
			ReqGetGuide t = new ReqGetGuide(message);
			t.setOrderId(orderId);
			t.setGuideId(guideId);
			CommonReq<ReqGetGuide> commReq = new CommonReq<ReqGetGuide>(t);
			ISmartPayService smartPayService = SpringContextUtil.getBean(ISmartPayService.class);
			CommonResp<RespMap> resp = smartPayService.getGuide(commReq);
			Map<String, String> paramMap = new HashMap<>(16);
			RespMap respMap = resp.getDataCaseRetCode();
			for (Map.Entry<ApiKey, Object> entity : respMap.getMap().entrySet()) {
				if(null != entity.getValue()) {
					paramMap.put(entity.getKey().getName(), entity.getValue().toString());
				}
			}
			CommonResp<RespMap> configResp = service.GetConfigKeyByGuidInfo(message, paramMap);
			RespMap respMap2 = configResp.getDataCaseRetCode();
			String k = respMap2.getString(ApiKey.HisGetConfigKeyByGuidInfo.WxPayConfigKey);
			if(StringUtil.isNotBlank(k)) {
				wxPayConfigKey = k;
			}
			String k2 = respMap2.getString(ApiKey.HisGetConfigKeyByGuidInfo.ZfbConfigKey);
			if(StringUtil.isNotBlank(k2)) {
				zfbConfigKey = k2;
			}
		}
		configKeyResp.put(ApiKey.GetPayConfigKey.WxPayConfigKey, wxPayConfigKey);
		configKeyResp.put(ApiKey.GetPayConfigKey.ZfbConfigKey, zfbConfigKey);
		configKeyResp.put(ApiKey.GetPayConfigKey.NetPayConfigKey, netConfigKey);
		return configKeyResp;
	}
}
