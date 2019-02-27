package com.kasite.client.crawler.modules.api.client;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;
import com.kasite.client.crawler.modules.api.vo.PingAnRespVo;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

import util.DESedeUtil;

/**
 * 
 * @className: PingAnClient
 * @author: lcz
 * @date: 2018年6月12日 下午6:08:32
 */
@Component
public class PingAnClient {
	//平安接口地址
	private static final String PA_URL = "https://mhis-yedi-stg1.pingan.com.cn/yedi-platform/biz/inbound/h";
	private static final String CHANNEL_CODE = "";//渠道编码
	private static final boolean NEEDDECRYPT = true;//是否加密  true是
	private static final String KEY = "1234567891111111";//加密key
	private static DESedeUtil desUtil= new DESedeUtil(KEY);//平安加密解密帮助类
	
	
	public PingAnRespVo call(String senderCode,String param) throws Exception {
//		senderCode=医院编码&channelCode=渠道编 码&needDecrypt=true|false
		String url = PA_URL + "?senderCode="+senderCode+"&channelCode="+CHANNEL_CODE+"&needDecrypt="+NEEDDECRYPT;
		HttpRequstBusSender sender = HttpRequestBus.create(url, RequestType.post);
		//加密入参
		sender.setParam(desUtil.encrypt(param));
		SoapResponseVo resp = sender.send();
		if(resp==null || resp.getCode()!=200) {
			throw new Exception("请求异常：Status="+(resp!=null?resp.getCode():""));
		}
		//解密返参
		String result = desUtil.decrypt(resp.getResult());
		JSONObject json = JSONObject.parseObject(result);
		JSONObject pkJs = json.getJSONObject("package");
		PingAnRespVo respVo = new PingAnRespVo();
		respVo.setAdditionInfo(pkJs.getJSONObject("additionInfo"));
		respVo.setBody(pkJs.getJSONArray("body"));
		respVo.setHead(pkJs.getJSONObject("head"));
		return respVo;
	}
	
	/**
	 * recordCount 传 body 中的数据的条数
	 * S291、S299、S250 这三个接口，第三方编码 intermediaryCode 和第三方名称 intermediaryName 必须
	 * @param
	 * 1 市医保、2 省医保 、3 新农合 、4 自费、5 公费医疗
	 * @return
	 */
	public JSONObject getHeadParam(PingAnBussId busseId,String senderCode,int recordCount,String intermediaryCode,String intermediaryName) {
		JSONObject head = new JSONObject();
		head.put("busseID", busseId.name());
		head.put("senderCode", senderCode);
		head.put("senderName", "");
		head.put("receiverCode", "");
		head.put("receiverName", "");
		head.put("intermediaryCode", intermediaryCode);//医保编码
		head.put("intermediaryName", intermediaryName);//医保名称
		head.put("hosorgNum", "操作员编号");
		head.put("hosorgName", "操作员姓名");
		head.put("systemType", "1");
		head.put("busenissType", "2");
		head.put("sendTradeNum", "");
		head.put("standardVersionCode", "version:1.0.0");
		head.put("clientmacAddress", "00:00:00:00:00:00");
		head.put("recordCount", recordCount);
		return head;
	}
	
	/**
	 * 如果处理成功，errorCode 返回 0，errorMsg 建议传“处理成功！”，也可以不传；  
	 * 如果未处理成功，系统级别错误 errorCode 返回-1，业务处理错误返回-2，且必须在 errorMsg 中返回 具体的错误信息
	 * @Description: 
	 * @param code
	 * @param message
	 * @return
	 */
	public JSONObject getAdditionInfoParam(int code,String message) {
		JSONObject additionInfo = new JSONObject();
		additionInfo.put("asyncAsk", "0");
		additionInfo.put("callback", "");
		additionInfo.put("correlationId", "");
		additionInfo.put("curDllAddr", "");
		additionInfo.put("errorCode", code);
		additionInfo.put("errorMsg", message);
		additionInfo.put("receiverTradeNum", "");
		return additionInfo;
	}
}
