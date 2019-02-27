package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.clinic.service;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.HosParse;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.HisSoapResultFormat;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.QZZGHisMetodEnum;
import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.service.AbsClinicInfoService;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.httpclient.http.SoapResponseVo;

@SuppressWarnings("unchecked")
@HosParse(hospitalName="泉州正骨医院",key="1231",isDebug=false)
public class ClinicServiceImpl extends AbsClinicInfoService{
	private static Logger logger = LoggerFactory.getLogger(ClinicServiceImpl.class);
	public static String getReqXml(String method,AbsQueryClinicReqVo vo) {
		String reqXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice/\">" + 
				"   <soapenv:Header/>" + 
				"   <soapenv:Body>" + 
				"      <web:"+method+">" + 
				"         <!--Optional:-->" + 
				"         <arg0><![CDATA["
				+ 	 "<Request>" + 
						"<patientId>"+ vo.getPatientId() +"</patientId>" + 
						"</Request>" 
				+ "]]></arg0>" + 
				"      </web:"+method+">" + 
				"   </soapenv:Body>" + 
				"</soapenv:Envelope>";
		return reqXml;
	}
	
	public String call(QZZGHisMetodEnum method,AbsQueryClinicReqVo vo) throws Exception{
		SoapResponseVo respVo = call(method,getReqXml(method.name(), vo),vo.getPatientId());
		if( null != respVo && respVo.getCode() == 200) {
			String result = HisSoapResultFormat.formateSoapResp(respVo.getResult());
			System.out.println("CallLog");
			System.out.println(getReqXml(method.name(), vo));
			System.out.println(result);
			return result;
		}
		throw new RRException("调用接口“"+method+"”返回的报文为空，请确认医院接口是否可以正常访问。"+getUrl());
	}
	
	
	@Override
	public List<ClinicRegWaterEntity> queryClinicRegisetrInfo(AbsQueryClinicReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicRegisetrInfo,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<ClinicRegWaterEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			ClinicRegWaterEntity o = new ClinicRegWaterEntity();
			parseVo2Obj(o, elemen);
			//个性化部分实现。
			String clinicNum = o.getMedicalNum();
			o.setClinicNum(clinicNum);
			
			entitys.add(o);
		}
		return entitys;
	}
	

	@Override
	public List<ClinicDiagnosisWaterEntity> queryClinicDiagnosis(AbsQueryClinicReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicDiagnosis,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<ClinicDiagnosisWaterEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			ClinicDiagnosisWaterEntity o = new ClinicDiagnosisWaterEntity();
			parseVo2Obj(o, elemen);
			//个性化部分
			String clinicNum = o.getMedicalNum();
			o.setClinicNum(clinicNum);
			String stort = o.getSort();
			o.setDiagnosisId(stort);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<ClinicFreeSummaryEntity> queryClinicFreeSummary(AbsQueryClinicReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicFreeSummary,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<ClinicFreeSummaryEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			ClinicFreeSummaryEntity o = new ClinicFreeSummaryEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<ClinicPrescriptionDetailEntity> queryClinicPrescriptionDetail(AbsQueryClinicReqVo reqVo)
			throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicPrescriptionDetail,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<ClinicPrescriptionDetailEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			ClinicPrescriptionDetailEntity o = new ClinicPrescriptionDetailEntity();
			parseVo2Obj(o, elemen);
			String eventTime = o.getEventTime();
			if(StringUtil.isNotBlank(eventTime)) {
				o.setEventTime(eventTime.substring(0, 19));
			}
			if(o.getItemName().equals("挂号费")) {
				o.setClinicChargeCode("98");
			}
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<ClinicBillEntity> queryClinicBill(AbsQueryClinicReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicBill,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<ClinicBillEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			ClinicBillEntity o = new ClinicBillEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<ClinicCaseEntity> queryClinicCase(AbsQueryClinicReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryClinicCase,reqVo);
		List<ClinicCaseEntity> entitys = new ArrayList<>();
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(result);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("医院的返回的报文解析异常：result = "+ result,e);
			return entitys;
		}
		List<Element> elements = doc.getRootElement().elements("Data");
		for (Element elemen : elements) {
			ClinicCaseEntity o = new ClinicCaseEntity();
			parseVo2Obj(o, elemen);
			//个性化部分
			String clinicNum = o.getMedicalNum();
			o.setClinicNum(clinicNum);
			String cid = o.getClinicCaseId();
			if(StringUtil.isBlank(cid)) {
				String pid = o.getPatientId();
				o.setClinicCaseId(pid);
			}
			o.setPatientId(reqVo.getPatientId());
			entitys.add(o);
		}
		return entitys;
	}
	
	public EntityUtils getEntityUtils() {
		return SpringContextUtils.getBean("entityUtils",EntityUtils.class);
	}
	
	public HosClientConfig getHosClientConfig() {
		return SpringContextUtils.getBean("hosClientConfig",HosClientConfig.class);
	}
	
	
}
