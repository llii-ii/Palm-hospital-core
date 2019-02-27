package test.com.kasite.client.crawler.micro;

import com.alibaba.fastjson.JSONObject;

public class WsgwLogBus {
	
	public static WsgwLogBus me() {
		return new WsgwLogBus();
	}
	private WsgwLogVo vo;
	private SaveCallLogBuser buser;
	private WsgwLogBus() {
		vo = new WsgwLogVo();
		buser = SaveCallLogBuser.getInstall();
		buser.setUrl("http://127.0.0.1:11111/guard/save/wsgwlog.do");
	}
	
	public void send() throws Exception {
		buser.saveWsgwLog(vo);
	}
  	public WsgwLogBus setAuthInfoVo(String sessionKey,String clientId,String sign,String clientVersion,String configKey) {
  		JSONObject json = new JSONObject();
  		json.put("clientId", clientId); 
 		json.put("clientVersion", clientVersion);  
 		json.put("sign", sign); 
 		json.put("sessionKey", sessionKey); 
 		json.put("configKey", configKey);
 		vo.setAuthInfo(json.toJSONString());
  		return this;
  	}
	public WsgwLogBus setAuthInfoVo(String authInfoVo,String configKey) {
  		JSONObject json = JSONObject.parseObject(authInfoVo);
 		json.put("configKey", configKey);
 		vo.setAuthInfo(json.toJSONString());
  		return this;
  	}
	public WsgwLogBus setApi(String api) {
		vo.setApi(api);
		return this;
	}
	public WsgwLogBus setCallType(String callType) {
		vo.setCallType(callType) ;
		return this;
	}
	public WsgwLogBus setClassName(String className) {
		vo.setClassName(className);
		return this;
	}
	public WsgwLogBus setClientIp(String clientIp) {
		vo.setClientIp(clientIp); 
		return this;
	}
	public WsgwLogBus setIsSuccess(Boolean isSuccess) {
		vo.setIsSuccess(isSuccess); 
		return this;
	}
	public WsgwLogBus setMethodName(String methodName) {
		vo.setMethodName(methodName); 
		return this;
	}
	public WsgwLogBus setMills(Integer mills) {
		vo.setMills(mills);
		return this;
	}
	public WsgwLogBus setOrderId(String orderId) {
		vo.setOrderId(orderId);
		return this;
	}
	public WsgwLogBus setOutType(Integer outType) {
		vo.setOutType(outType); 
		return this;
	}
	public WsgwLogBus setParam(String param) {
		vo.setParam(param);
		return this;
	}
	public WsgwLogBus setParamType(Integer paramType) {
		vo.setParamType(paramType);
		return this;
	}
	public WsgwLogBus setReferer(String referer) {
		vo.setReferer(referer); 
		return this;
	}
	public WsgwLogBus setResult(String result) {
		vo.setResult(result); 
		return this;
	}
	public WsgwLogBus setUniqueReqId(String uniqueReqId) {
		vo.setUniqueReqId(uniqueReqId); 
		return this;
	}
	public WsgwLogBus setUserAgent(String userAgent) {
		vo.setUserAgent(userAgent); 
		return this;
	}
	public WsgwLogBus setV(String v) {
		vo.setV(v);
		return this;
	}
	
	
}
