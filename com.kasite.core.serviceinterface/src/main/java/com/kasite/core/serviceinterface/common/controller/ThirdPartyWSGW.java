package com.kasite.core.serviceinterface.common.controller;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 第三方调用的接口网关入口
 * @author daiyanshui
 *
 */
//@Api(value = "第三方调用的接口网关入口 /wsgw/{module}/{name}/{method}/call.do")
@RestController
@RequestMapping("/thirdparty/{clientId}/{orgCode}/{appId}/{module}/{name}/{method}") 
public class ThirdPartyWSGW extends WSGW{

	@RequestMapping("/getExecute.do")
//	@ApiOperation(value = "第三方直接get调用接口", notes = "第三方直接get调用接口 只能在医院内部使用")
	public String getExecute(
			@PathVariable("module")
//    		@ApiParam(value = "需要请求的 Api 的模块", required = true) 
			String module,
    		@PathVariable("name")
//    		@ApiParam(value = "需要请求的 Api 的接口", required = true) 
			String name,
    		@PathVariable("method")
//    		@ApiParam(value = "需要请求的 Api 方法名", required = true) 
			String method,
			HttpServletRequest request, HttpServletResponse response) {
		return execute(module, name, method, request, response);
	}
//	/**
//	 * 解密内容
//	 * @param reqParam
//	 * @return
//	 * @throws Exception
//	 */
//	private String getRequestParam(String reqParam) throws Exception {
//		try {
//			reqParam = reqParam.replace(" ","+");
//			String decryContent = KasiteRSAUtil.rsaDecrypt(reqParam, KasiteConfig.getUpdatePrivateKey(), "utf-8");
//			return decryContent;
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			throw new RRException("请求的内容解密失败请确认 RSA 公钥是否正确。");
//		}
//	}
	/**
	 * 加签结果集
	 * @param reqParam
	 * @return
	 * @throws Exception
	 */
	private R  getResult(R r,String appId) throws Exception {
		try {
			Object result = r.get("result");
			if(null != r) {
				String resultStr = result.toString();
				String encryContent = KasiteRSAUtil.rsaEncrypt(resultStr, KasiteConfig.getAppPublicKey(appId), "utf-8");
				r.put("result", encryContent);
			}
			return r;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RRException("请求的内容解密失败请确认 RSA 公钥是否正确。");
		}
	}
	@PostMapping("/call.do")
	public R executeRSA(	
			@PathVariable("appId")
			String appId,
			@PathVariable("module")
			String module,
    		@PathVariable("name")
			String name,
    		@PathVariable("method")
			String method,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String data = request.getParameter("data");
		String decryContent = (data);
    	R r = R.error(-14444, "No Call Service");
		String uuid = IDSeed.next();
		InterfaceMessage message = new InterfaceMessage();
    	AuthInfoVo vo = createAuthInfoVo(uuid,request);
    	try {
			String api = getApi(module, method);
			message.setApiName(api);
			message.setAuthInfo(vo.toString());
			message.setOutType(1);
			message.setParam(decryContent);
			message.setParamType(1);
			message.setSeq(Long.toString(System.currentTimeMillis()));
			message.setClientIp(com.kasite.core.common.log.NetworkUtil.getLocalIP());
			r = invoke(vo, api,1,1, message, request);
		}catch (Exception ex) {
			r = parseException(ex);
		}
    	//将返回结果集加签名
    	return getResult(r,appId);
	}
	@PostMapping("/execute.do")
	public String execute(
			@PathVariable("module")
			String module,
    		@PathVariable("name")
			String name,
    		@PathVariable("method")
			String method,
			HttpServletRequest request, HttpServletResponse response) {
		String format = request.getParameter("format"); //出参格式     json 或xml 默认是json   
		Integer outType = 1;
		if("json".equals(format)) {
			outType = 0;
		}
		//请求的所有参数都是放在Data节点中 
//		Map<String,String> paramMap = changeMap(request.getParameterMap());
		Map<String,String[]> paraMap =request.getParameterMap();
		Document doc = DocumentHelper.createDocument();
		Element req = doc.addElement("Req");
		XMLUtil.addElement(req, "TransactionCode", "90001");
		Element data = req.addElement("Data");
		if(null != paraMap && paraMap.size() > 0) {
			if(paraMap!=null && !paraMap.isEmpty()){
				Set<String> keySet = paraMap.keySet();
				for(String key:keySet){
					Object value = paraMap.get(key);
					if("configKey".equals(key) || "appId".equals(key) || "cargeType".equals(key) || "sign".equals(key)) {
						continue;
					}
					
					if(value==null){//空
						XMLUtil.addElement(data, key, "");
					}else if(value.getClass().isArray()){//数组
						int length = Array.getLength(value);
						if(length==0){
							XMLUtil.addElement(data, key, "");
						}else if(length==1){
							XMLUtil.addElement(data, key, Array.get(value, 0).toString());
						}else{
							//目前不支持数组形式入参
							StringBuffer buffer = new StringBuffer("");
							for(int i=0;i<length;i++){
								buffer.append(Array.get(value, i).toString()).append("#");//多个用井号隔开
							}
							XMLUtil.addElement(data, key, buffer.toString());
						}
					}else{//非数组
						XMLUtil.addElement(data, key, value.toString());
					}
				}
			}
		}
		
    	R r = R.error(-14444, "No Call Service");
		String uuid = IDSeed.next();
		InterfaceMessage message = new InterfaceMessage();
    	AuthInfoVo vo = createAuthInfoVo(uuid,request);
    	try {
			String api = getApi(module, method);
			message.setApiName(api);
			message.setAuthInfo(vo.toString());
			message.setOutType(outType);
			message.setParam(doc.asXML());
			message.setParamType(1);
			message.setSeq(Long.toString(System.currentTimeMillis()));
			message.setClientIp(com.kasite.core.common.log.NetworkUtil.getLocalIP());
			r = invoke(vo, api,1,outType, message, request);
		}catch (Exception ex) {
			r = parseException(ex);
		}
    	int code = (int) r.get("code");
    	if(code == 0) {
    		Object o = r.get("result");
    		if(null != o) {
    			return o.toString();
    		}else {
    			StringBuffer buffer = new StringBuffer("<Resp>");
        		buffer.append("<RespCode>").append(code).append("</RespCode>");
        		buffer.append("<RespMessage>").append(r.get("msg")).append("</RespMessage>");
        		buffer.append("</Resp>");
        		return buffer.toString();
    		}
    	}else {
    		StringBuffer buffer = new StringBuffer("<Resp>");
    		buffer.append("<RespCode>").append(code).append("</RespCode>");
    		buffer.append("<RespMessage>").append(r.get("msg")).append("</RespMessage>");
    		buffer.append("</Resp>");
    		return buffer.toString();
    	}
	}
	
	
}
