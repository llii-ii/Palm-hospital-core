package com.kasite.client.yy.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.kasite.client.yy.bean.dbo.YyWater;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.serviceinterface.module.yy.IYYService;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateYyWater;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

public class PushRegWaterToJkzlThread extends Thread{
	
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_BASIC);
	//失败重试次数，默认5次
	private static int retryCount = 5;
	//健康之路接口地址
	private final static String JkzlWsUrl = "http://service.yihu.com:8080/WSGW/services/ServiceGateWay";
	private final static String Jkzl_Kasite_ClientId = "9000146";
	
	private YyWater water;
	private InterfaceMessage msg;
	private String jkzlHosId;
	
	public PushRegWaterToJkzlThread(InterfaceMessage msg,String jkzlHosId,YyWater water) {
		this.msg = msg;
		this.jkzlHosId = jkzlHosId;
		this.water = water;
	}
	
	
	
	@Override
	public void run() {
		try {
			pushYyWater();
		}finally {
			//未推送成功且 发送次数小等于5次，休眠1秒重试
			if(water.getPushState()!=1 && water.getNum()<=retryCount) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pushYyWater();
			}
		}
	}
	
	/**
	 * 推送预约记录到健康之路
	 */
	private void pushYyWater(){
		int count = water.getNum()!=null?water.getNum()+1:1;
		water.setNum(count);
		try {
			//如果是健康之路或微信端渠道预约的，不进行推送
			if(StringUtil.isNotBlank(water.getOperator()) && 
					(KstHosConstant.WX_CHANNEL_ID.equals(water.getOperator())
							|| KstHosConstant.JKZL_KASITE_CLIENTID.equals(water.getOperator()))) {
				water.setPushState(1);
				updateYyWater(1, "该渠道不进行推送",count, water.getOrderId());
				return;
			}
			JSONObject param = new JSONObject();
			param.put("ghtHospitalId", jkzlHosId);
			String OperaType ="";
			if(water.getState()==1){//保存预约流水
				OperaType = "3";
			}else{//退号
				OperaType = "4";
			}
			param.put("operaType", OperaType);
			param.put("modeId", 1);
			param.put("deptCode", water.getDeptCode());
			param.put("doctorEmpId", water.getDoctorCode());
			param.put("deptName", water.getDeptName());
			param.put("doctorName", water.getDoctorName());
			param.put("isSendSms", "0");
			param.put("userMobile", water.getUserMobile());
			param.put("orderId", water.getOrderId());
			param.put("familyAddress", water.getAddress());
			param.put("serialNo", water.getQueueNo());
			param.put("queueNo", water.getQueueNo());
			param.put("registerDate", water.getRegisterDate());
			param.put("commendTime", water.getCommendTime());
			param.put("startTime", "");
			param.put("endTime", "");
			param.put("operatorId",water.getOperatorId());
			param.put("operatorName", water.getOperatorName());
			param.put("numberSn", 0);
			param.put("wayId", 9);
			param.put("userName", water.getUserName());
			param.put("timeId", water.getTimeId());
			param.put("fee", water.getFee());
			param.put("ghFee", water.getRegistrationFee());
			param.put("treatFee", water.getClinicFee());
			param.put("userIdCard",water.getIdCardNo());
			param.put("clinicCard", water.getClinicCard());
			param.put("workPlace", water.getWorkPlace());//门诊挂号处预约专窗15号、16号或17号建卡取号
			param.put("userAge", "");
			param.put("cardId", "");
			param.put("remark", "");
			param.put("accountSn", "");
			param.put("memberSn", "");
			param.put("resType", -1);
			param.put("accountType", -1);
			param.put("userSex", water.getSex());
			param.put("birthday", water.getBirthday());
			JSONObject respJs = null;
			try {
				String resp = pushMsgToJkzl(param.toString());
				respJs = JSONObject.parseObject(resp);
			}catch (Exception e) {
				e.printStackTrace();
				water.setPushState(-1);
				updateYyWater(-1, "推送失败："+e.getLocalizedMessage(),count, water.getOrderId());
			}
			if(respJs==null || respJs.getIntValue("Code")!=10000) {
				water.setPushState(-1);
				updateYyWater(-1, water.getPushRemark()+respJs.toString(), count, water.getOrderId());
				return;
			}
			water.setPushState(1);
			updateYyWater(1, respJs.toString(),count, water.getOrderId());
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
			try {
				water.setPushState(-1);
				updateYyWater(-1, "推送失败："+e.getLocalizedMessage(),count, water.getOrderId());
			} catch (Exception e1) {
				e1.printStackTrace();
				LogUtil.error(log, e);
			}
		}
	}
	
	private void updateYyWater(int pushState,String remark,int num,String orderId) throws AbsHosException, Exception {
		IYYService yyService = SpringContextUtil.getBean(IYYService.class);
		yyService.updateYyWater(new CommonReq<ReqUpdateYyWater>(new ReqUpdateYyWater(msg, orderId, pushState, remark,num)));
	}
	/**
	 * 推送预约记录到健康之路
	 * @param param
	 * @return
	 */
	public String pushMsgToJkzl(String param) throws Exception{
		JSONObject authInfo = new JSONObject();
		authInfo.put("ClientId", Jkzl_Kasite_ClientId);
		authInfo.put("ClientVersion","1.0");
		authInfo.put("Sign", "");
		authInfo.put("SessionKey", "");
		String seq = DateOper.getNow("yyyyMMddHHmmssSSS");
		HttpRequstBusSender sender = HttpRequestBus.create(JkzlWsUrl, RequestType.soap1);
		sender.setParam(getJkzlSoapParam(authInfo.toString(), seq, "yuyueclient2.GhWs.regWaterOper", param, "0", "0"));
		SoapResponseVo respVo = sender.send();
		if(respVo.getCode()!=200) {
			throw new RRException("Http请求异常：Status="+respVo.getCode());
		}
		Document doc = DocumentHelper.parseText(respVo.getResult());
		Element root = doc.getRootElement();
		Element body = root.element("Body");
		Element respEl = body.element("serviceResponse");
		return respEl.elementTextTrim("out");
	}
	
	public String getJkzlSoapParam(String authInfo,String seq,String api,String param,String paramType,String outType) {
		StringBuffer sbuff = new StringBuffer();
		sbuff.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://com.yihu.wsgw/ServiceGateWay\">");
		sbuff.append("<soapenv:Header/>");
		sbuff.append("<soapenv:Body>");
		sbuff.append("<ser:service>");
		sbuff.append("<ser:authInfo><![CDATA[").append(authInfo).append("]]></ser:authInfo>");
		sbuff.append("<ser:sequenceNo>").append(seq).append("</ser:sequenceNo>");
		sbuff.append("<ser:api>").append(api).append("</ser:api>");
		sbuff.append("<ser:param><![CDATA[").append(param).append("]]></ser:param>");
		sbuff.append("<ser:paramType>").append(paramType).append("</ser:paramType>");
		sbuff.append("<ser:outType>").append(outType).append("</ser:outType>");
		sbuff.append("<ser:v>1.0</ser:v>");
		sbuff.append("</ser:service>");
		sbuff.append("</soapenv:Body>");
		sbuff.append("</soapenv:Envelope>");
		return sbuff.toString();
	}
	
}
