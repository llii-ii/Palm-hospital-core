package com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.cases.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.HosParse;
import com.kasite.client.crawler.modules.cases.entity.QueryCheckEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryCheckItemEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryDiseaseEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryHospitalRecordEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryOperationEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryReportEntity;
import com.kasite.client.crawler.modules.cases.service.AbsCasesService;
import com.kasite.client.crawler.modules.cases.vo.ReqQueryCasesVo;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.HisSoapResultFormat;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.QZZGHisMetodEnum;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.client.crawler.modules.utils.SpringContextUtils;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.httpclient.http.SoapResponseVo;

/**
 * 
 * @className: CasesServiceImpl
 * @author: lcz
 * @date: 2018年6月12日 上午9:24:04
 */
@HosParse(hospitalName="泉州正骨医院",key="1231",isDebug=false)
public class CasesServiceImpl extends AbsCasesService{
	
	private static Logger logger = LoggerFactory.getLogger(CasesServiceImpl.class);
	
	public static String getReqXml(String method,ReqQueryCasesVo vo) {
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
	
	public String call(QZZGHisMetodEnum method,ReqQueryCasesVo vo) throws Exception{
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
	public List<QueryHospitalRecordEntity> queryHospitalRecordList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryHospitalRecordList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryHospitalRecordEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryHospitalRecordEntity o = new QueryHospitalRecordEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<QueryDiseaseEntity> queryDiseaseList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryDiseaseList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryDiseaseEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryDiseaseEntity o = new QueryDiseaseEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<QueryOperationEntity> queryOperationList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryOperationList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryOperationEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryOperationEntity o = new QueryOperationEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<QueryReportEntity> queryReportList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryReportList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryReportEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryReportEntity o = new QueryReportEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<QueryCheckEntity> queryCheckList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryCheckList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryCheckEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryCheckEntity o = new QueryCheckEntity();
			parseVo2Obj(o, elemen);
			entitys.add(o);
		}
		return entitys;
	}

	@Override
	public List<QueryCheckItemEntity> queryCheckItemList(ReqQueryCasesVo reqVo) throws Exception {
		String result = call(QZZGHisMetodEnum.QueryCheckItemList,reqVo);
		Document doc = DocumentHelper.parseText(result);
		Iterator<?> it = doc.getRootElement().elementIterator("Data");
		List<QueryCheckItemEntity> entitys = new ArrayList<>();
		while(it.hasNext()) {
			Element elemen = (Element) it.next();
			QueryCheckItemEntity o = new QueryCheckItemEntity();
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
