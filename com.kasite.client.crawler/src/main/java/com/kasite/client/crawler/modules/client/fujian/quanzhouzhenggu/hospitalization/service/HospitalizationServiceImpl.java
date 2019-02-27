package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.hospitalization.service;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.HosParse;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.HisSoapResultFormat;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.QZZGHisMetodEnum;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.service.AbsHospitalizationService;
import com.kasite.client.crawler.modules.hospitalization.vo.QueryHospitalizationReqVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.httpclient.http.SoapResponseVo;
@SuppressWarnings("unchecked")
@HosParse(hospitalName="泉州正骨医院",key="1231",isDebug=false)
public class HospitalizationServiceImpl extends AbsHospitalizationService {

	public static String getReqXml(String method,QueryHospitalizationReqVo vo) {
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
	public String call(QZZGHisMetodEnum method,QueryHospitalizationReqVo vo) throws Exception{
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
	public List<HospitalizationEntity> queryHospitalization(QueryHospitalizationReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalization,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationEntity o = new HospitalizationEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}
	@Override
	public List<HospitalizationDiagnosisEntity> queryHospitalizationDiagnosis(QueryHospitalizationReqVo reqVo)
			throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalizationDiagnosis,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationDiagnosisEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationDiagnosisEntity o = new HospitalizationDiagnosisEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}
	@Override
	public List<HospitalizationFreeSummaryEntity> queryHospitalizationFreeSummary(QueryHospitalizationReqVo reqVo)
			throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalizationFreeSummary,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationFreeSummaryEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationFreeSummaryEntity o = new HospitalizationFreeSummaryEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}
	@Override
	public List<HospitalizationPerscriptionDetailEntity> queryHospitalizationPerscriptionDetail(
			QueryHospitalizationReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalizationPerscriptionDetail,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationPerscriptionDetailEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationPerscriptionDetailEntity o = new HospitalizationPerscriptionDetailEntity();
			parseVo2Obj(o, elemen);
			String eventTime = o.getEventTime();
			if(StringUtil.isNotBlank(eventTime)) {
				o.setEventTime(eventTime.substring(0, 19));
			}
			if(o.getItemName().equals("挂号费")) {
				o.setHospitalChargeCode("98");
			}
			entitys.add(o);
		}
		return entitys;
	}
	@Override
	public List<HospitalizationBillEntity> queryHospitalizationBill(QueryHospitalizationReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalizationBill,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationBillEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationBillEntity o = new HospitalizationBillEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}
	@Override
	public List<HospitalizationSummaryEntity> queryHospitalizationSummary(QueryHospitalizationReqVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalizationSummary,reqVo);
		Document doc = DocumentHelper.parseText(result);
		List<Element> elements = doc.getRootElement().elements("Data");
		List<HospitalizationSummaryEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			HospitalizationSummaryEntity o = new HospitalizationSummaryEntity();
			parseVo2Obj(o, elemen);
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
