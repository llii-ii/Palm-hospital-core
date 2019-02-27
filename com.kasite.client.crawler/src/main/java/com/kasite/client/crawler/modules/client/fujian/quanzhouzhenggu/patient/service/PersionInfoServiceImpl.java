package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.patient.service;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.kasite.client.crawler.config.ESConfig;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.HosParse;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.HisSoapResultFormat;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.QZZGHisMetodEnum;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.patient.service.AbsPatientInfoService;
import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;
import com.kasite.core.httpclient.http.SoapResponseVo;
@SuppressWarnings("unchecked")
@HosParse(hospitalName="泉州正骨医院",key="1231",isDebug=false)
public class PersionInfoServiceImpl extends AbsPatientInfoService{

	public static String getReqParam(ReqQueryPatientListVo reqVo) {
		String idcardid = getReqParamString(reqVo.getIdCardId());
		String patientid = getReqParamString(reqVo.getPatientId());
		String clinicCard = getReqParamString(reqVo.getClinicCard());
		int pageIndex = reqVo.getPageIndex();
		int pageSize = reqVo.getPageSize();
		String reqXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice/\">" + 
				"   <soapenv:Header/>" + 
				"   <soapenv:Body>" + 
				"      <web:QueryPersion>" + 
				"         <!--Optional:-->" + 
				"         <arg0><![CDATA["
				+ "<Request>" + 
				"<idCardId>"+ idcardid +"</idCardId>" + 
				"<patientId>"+ patientid +"</patientId>" + 
				"<clinicCard>"+ clinicCard +"</clinicCard>" + 
				"<pageIndex>"+ pageIndex +"</pageIndex>" + 
				"<pageSize>"+ pageSize +"</pageSize>" + 
				"</Request>" 
				+ "]]></arg0>" + 
				"      </web:QueryPersion>" + 
				"   </soapenv:Body>" + 
				"</soapenv:Envelope>";
				;
		return reqXml;
	}

//	public static void main(String[] args) throws Exception {
//		ReqQueryPatientListVo vo = new ReqQueryPatientListVo();
//		vo.setPageIndex(1);
//		vo.setPageSize(10);
//		vo.setIdCardId("350500550419151");
//		List<PatientEntity> list = new PersionInfoServiceImpl(new QZZGHosClientConfig()).queryPatientEntityList(vo);
//		for (PatientEntity entity : list) {
//			System.out.println(entity.getName());
//			ValidatorUtils.validateEntity(entity,AddGroup.class);
//			EntityUtils.save2EsDB(ElasticsearchClientConfig.ElasticIndex.persion, entity);
//		}
//	}
	
	@Override
	public List<PatientEntity> queryPatientEntityList(ReqQueryPatientListVo reqVo) throws Exception {
		SoapResponseVo respVo = call(QZZGHisMetodEnum.QueryPersion,getReqParam(reqVo),reqVo.getPatientId());
		Document doc = DocumentHelper.parseText(HisSoapResultFormat.formateSoapResp(respVo.getResult()));
		List<Element> elements = doc.getRootElement().elements("Data");
		List<PatientEntity> entitys = new ArrayList<>();
		for (Element elemen : elements) {
			PatientEntity o = new PatientEntity();
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
	public ESConfig getESConfig() {
		return  SpringContextUtils.getBean("eSConfig",ESConfig.class);
	}
	
	
}
