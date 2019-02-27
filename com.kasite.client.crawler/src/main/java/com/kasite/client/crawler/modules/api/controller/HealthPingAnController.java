package com.kasite.client.crawler.modules.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kasite.client.crawler.common.controller.AbstractController;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;
import com.kasite.client.crawler.modules.api.service.IPingAnService;
import com.kasite.client.crawler.modules.api.vo.PingAnReqAndRespVo;
import com.kasite.client.crawler.modules.api.vo.PingAnReqVo;
import com.kasite.client.crawler.modules.api.vo.PingAnRespVo;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.util.HealthPingAnUtil;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.IXMLCallBack;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.XMLUtil;


@RestController("/api/health/pingan")
public class HealthPingAnController extends AbstractController{
	@Autowired
	private EntityUtils entityUtils;
	
	@Autowired
	private IPingAnService pingAnService;
	
	@Autowired
	private HealthPingAnUtil pingAnUtil;
	
	@PostMapping("/C100")
	R C100(String inputData) throws Exception {
		JSONObject reqJson = (JSONObject) JSON.parse(inputData);
		PingAnReqVo req = new PingAnReqVo();
		req.parseReq(PingAnBussId.C100,reqJson);
		PingAnRespVo resp = new PingAnRespVo();
		resp.setBody(new JSONArray());
		PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
		vo.parseReq(PingAnBussId.C100, req);
		vo.parseResp(PingAnBussId.C100, resp);
		String sendTradeNum = req.getHead().getSendTradeNum();
		entityUtils.save2EsDB(ElasticIndex.pingan, sendTradeNum, vo);
		return R.ok().put("package", resp);
	}
	@AuthIgnore
	@PostMapping("/C210")
	R C210(String inputData) throws Exception {
		JSONObject reqJson = JSONObject.parseObject(inputData);
		PingAnReqVo req = new PingAnReqVo();
		req.parseReq(PingAnBussId.C210,reqJson);
		PingAnRespVo resp = new PingAnRespVo();
		resp.setBody(new JSONArray());
		PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
		vo.parseReq(PingAnBussId.C210, req);
		vo.parseResp(PingAnBussId.C210, pingAnService.doC210(req));
		String sendTradeNum = req.getHead().getSendTradeNum();
		entityUtils.save2EsDB(ElasticIndex.pingan, sendTradeNum, vo);
		return R.ok().put("package", resp);
	}
	
	@PostMapping("/C220")
	R C220(String inputData) throws Exception {
		JSONObject reqJson = (JSONObject) JSON.parse(inputData);
		PingAnReqVo req = new PingAnReqVo();
		req.parseReq(PingAnBussId.C220,reqJson);
		PingAnRespVo resp = new PingAnRespVo();
		resp.setBody(new JSONArray());
		PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
		vo.parseReq(PingAnBussId.C220, req);
		vo.parseResp(PingAnBussId.C220, resp);
		String sendTradeNum = req.getHead().getSendTradeNum();
		entityUtils.save2EsDB(ElasticIndex.pingan, sendTradeNum, vo);
		return R.ok().put("package", resp);
	}
	
	/****
	 * 由医院端发起费用结算
	 * @Description: 
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/InstantSettlement")
	String S291(String inputXml) throws Exception {
		try {
			PingAnReqAndRespVo vo = pingAnService.doS291(inputXml);
			if(vo==null) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "费用结算异常");
			}
			if(vo.getRespCode()!=0) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "费用结算异常:"+vo.getRespMessage());
			}
			JSONArray array = JSONArray.parseArray(vo.getRespBody());
			
			return XMLUtil.parseJSONArray2Xml(array,"composite=Data_1",new IXMLCallBack() {
				@Override
				public String parseXmlValue(String key, String value) {
					if("medicalType".equals(key)) {
						return pingAnUtil.parsePingAnMedicalTypeToHos(value);
					}
					return value;
				}
			}).asXML();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("费用结算异常:inputXml="+inputXml,e);
			return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "费用结算异常:"+e.getLocalizedMessage());
		}
	}
	
	@PostMapping("/ClinicInstantSettlement")
	String S299(String inputXml) throws Exception {
		try {
			PingAnReqAndRespVo vo = pingAnService.doS299(inputXml);
			if(vo==null) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "门诊即时结算异常");
			}
			if(vo.getRespCode()!=0) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "门诊即时结算异常:"+vo.getRespMessage());
			}
			JSONArray array = JSONArray.parseArray(vo.getRespBody());
			return XMLUtil.parseJSONArray2Xml(array,"composite=Data_1,recipeList=Data_2",new IXMLCallBack() {
				@Override
				public String parseXmlValue(String key, String value) {
					if("medicalType".equals(key)) {
						return pingAnUtil.parsePingAnMedicalTypeToHos(value);
					}
					return value;
				}
			}).asXML();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("S299门诊即时结算异常:inputXml="+inputXml,e);
			return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "门诊即时结算异常:"+e.getLocalizedMessage());
		}
	}
	
	@PostMapping("/CancelSettlement")
	String S270(String inputXml) throws Exception {
		try {
			PingAnReqAndRespVo vo = pingAnService.doS270(inputXml);
			if(vo==null) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "取消结算异常");
			}
			if(vo.getRespCode()!=0) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "取消结算异常:"+vo.getRespMessage());
			}
			JSONArray array = JSONArray.parseArray(vo.getRespBody());
			return XMLUtil.parseJSONArray2Xml(array,null,null).asXML();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("S270取消结算异常:inputXml="+inputXml,e);
			return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "取消结算异常:"+e.getLocalizedMessage());
		}
	}
	
	@PostMapping("/CancelPrescription")
	String S260(String inputXml) throws Exception {
		try {
			PingAnReqAndRespVo vo = pingAnService.doS260(inputXml);
			if(vo==null) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "处方明细撤销异常");
			}
			if(vo.getRespCode()!=0) {
				return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "处方明细撤销异常:"+vo.getRespMessage());
			}
			JSONArray array = JSONArray.parseArray(vo.getRespBody());
			return XMLUtil.parseJSONArray2Xml(array,null,null).asXML();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("S260处方明细撤销异常:inputXml="+inputXml,e);
			return CommonUtil.getRetVal(1, RetCode.Common.Call.getCode(), "处方明细撤销异常:"+e.getLocalizedMessage());
		}
	}
	
}


//@RestController
//@PostMapping("/api/init")
//public class TestInit {
//	@PostMapping("/test")
//    @ResponseBody
//    R init() {
//		R r = new R();
//		r.put("msg", "测试成功。");
//		return r; 
//    }
//}
