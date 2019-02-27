package com.kasite.client.crawler.modules.api.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.remoting.standard.DateOper;
import com.coreframework.util.ArithUtil;
import com.kasite.client.crawler.config.ESConfig;
import com.kasite.client.crawler.config.ElasticsearchClientConfig.ElasticIndex;
import com.kasite.client.crawler.config.HosClientConfig;
import com.kasite.client.crawler.modules.ModuleManage;
import com.kasite.client.crawler.modules.api.client.PingAnClient;
import com.kasite.client.crawler.modules.api.em.PingAnBussId;
import com.kasite.client.crawler.modules.api.service.IPingAnService;
import com.kasite.client.crawler.modules.api.vo.PingAnC100;
import com.kasite.client.crawler.modules.api.vo.PingAnC220;
import com.kasite.client.crawler.modules.api.vo.PingAnReqAndRespVo;
import com.kasite.client.crawler.modules.api.vo.PingAnReqVo;
import com.kasite.client.crawler.modules.api.vo.PingAnRespVo;
import com.kasite.client.crawler.modules.cases.entity.QueryDiseaseEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryHospitalRecordEntity;
import com.kasite.client.crawler.modules.cases.entity.QueryOperationEntity;
import com.kasite.client.crawler.modules.cases.service.ICasesService;
import com.kasite.client.crawler.modules.cases.vo.ReqQueryCasesVo;
import com.kasite.client.crawler.modules.client.fujian.quanzhouzhenggu.util.HealthPingAnUtil;
import com.kasite.client.crawler.modules.clinic.entity.ClinicBillEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicCaseEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicDiagnosisWaterEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicFreeSummaryEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicPrescriptionDetailEntity;
import com.kasite.client.crawler.modules.clinic.entity.ClinicRegWaterEntity;
import com.kasite.client.crawler.modules.clinic.service.IClinicService;
import com.kasite.client.crawler.modules.clinic.vo.AbsQueryClinicReqVo;
import com.kasite.client.crawler.modules.clinic.vo.AllClinicDataVo;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationBillEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationDiagnosisEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationFreeSummaryEntity;
import com.kasite.client.crawler.modules.hospitalization.entity.HospitalizationPerscriptionDetailEntity;
import com.kasite.client.crawler.modules.hospitalization.service.IHospitalizationService;
import com.kasite.client.crawler.modules.hospitalization.vo.AllHospitalizationDataVo;
import com.kasite.client.crawler.modules.hospitalization.vo.QueryHospitalizationReqVo;
import com.kasite.client.crawler.modules.patient.entity.PatientEntity;
import com.kasite.client.crawler.modules.patient.service.IPersionInfoService;
import com.kasite.client.crawler.modules.patient.vo.ReqQueryPatientListVo;
import com.kasite.client.crawler.modules.utils.EntityUtils;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.elastic.ElasticBus;
import com.kasite.core.elastic.ElasticQueryResponse;
import com.kasite.core.elastic.ElasticQueryResponseHits;
import com.kasite.core.elastic.ElasticQueryResponseHits_Hits;
import com.kasite.core.elastic.ElasticQueryResponseJsonVo;
import com.kasite.core.elastic.ElasticRangeEnum;
import com.kasite.core.elastic.ElasticRestClientBus;
import com.kasite.core.elastic.ElasticRestClientBusSender;
import com.kasite.core.elastic.ElasticRestResponse;
import com.kasite.core.elastic.ElasticRestTypeEnum;
import com.kasite.core.elastic.IElasticQueryResponseScrollParse;
import com.kasite.core.httpclient.http.StringUtils;
@Service("pingAnServiceImpl")
public class PingAnServiceImpl implements IPingAnService{
	private static final Logger logger = LoggerFactory.getLogger(PingAnServiceImpl.class);
	
	@Autowired
	private HosClientConfig hosClientConfig;
	@Autowired
	private HealthPingAnUtil pingAnUtil;
	@Autowired
	private PingAnClient client;
	@Autowired
	private EntityUtils entityUtils;
	
	public IHospitalizationService getHospitalizationService() {
		return ModuleManage.getInstance().getHospitalizationService(hosClientConfig);
	}
	
	public IClinicService getClinicService() {
		return ModuleManage.getInstance().getClinicService(hosClientConfig);
	}
	
	public IPersionInfoService getPersionService() {
		return ModuleManage.getInstance().getPersionInfoService(hosClientConfig);
	}
	public ICasesService getCasesService() {
		return ModuleManage.getInstance().getCasesService(hosClientConfig);
	}
	@Autowired
	private ESConfig eSConfig;
	
	public static void main(String[] args) throws Exception {
		PingAnServiceImpl service = new PingAnServiceImpl();
		service.doC100(DateOper.parse("2018-05-23 17:47:58"));
		System.exit(-1);
	}
	
	@Override
	public void doC100(Date startTime) {
		//执行C100 后续的动作。
		try {
			//触发所有查询条件，查询门诊信息和住院信息
//			getService().queryAll(pid, orgCode);
			
			//读取ES中的数据
			ElasticIndex index = ElasticIndex.pingan;
			Map<String, Object> _search = ElasticBus.create()
					.setSize(20)
					.createSimpleQuery()
			.createElasticQuerySimpleBool()
			.addMustTerm("callType", "C100")
			.addMustRange(ElasticRangeEnum.gte, "inserttime", DateOper.formatDate(startTime, "yyyy-MM-dd HH:mm:ss"))
			.getSearch();
			try{
				String ips = null == eSConfig? "127.0.0.1:9200":eSConfig.getUrl();
				ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
				.set_search(_search);
				ElasticRestResponse resp = sender.simpleQuery(index.name());
				resp.setParse(new IElasticQueryResponseScrollParse() {
					@Override
					public void parse(ElasticQueryResponse response,
							ElasticQueryResponseHits_Hits hist, String _source,int i) {
						String id = hist.get_id();
						System.out.println("_id="+id);
						ElasticQueryResponseHits hits = response.getHits();
						if(null != hits) {
//							System.out.println("total="+ response.getHits().getTotal() +"id="+id +" data = [ "+_source +" ]");
							JSONObject json = JSON.parseObject(_source);
							JSONArray reqBody = json.getJSONArray("reqBody");
							for (Object o : reqBody) {
								JSONObject obj = (JSONObject) o;
								PingAnC100 c100 = new PingAnC100();
								c100.setJson(obj);
								String cardNo = c100.getCredentialNum();//证件号
//								String cardType = c100.getCredentialType();//证件类型
								ReqQueryPatientListVo reqVo = new ReqQueryPatientListVo();
								reqVo.setIdCardId(cardNo);
								try {
									List<PatientEntity> list = getPersionService().queryPatientList2EsDB(reqVo);
									for (PatientEntity entity : list) {
										String pid = entity.getPatientId();
										AllClinicDataVo allClinicVo = null;
										try {
											allClinicVo = getClinicService().queryAll(pid);
										}catch (Exception e) {
											e.printStackTrace();
											logger.error("同步门诊信息异常",e);
										}
										AllHospitalizationDataVo allHospitalizationVo = null;
										try {
											allHospitalizationVo = getHospitalizationService().queryAll2EsDB(pid);
										}catch (Exception e) {
											e.printStackTrace();
											logger.error("同步住院信息异常",e);
										}
										List<ClinicRegWaterEntity> clinicRegWaterlist = allClinicVo.getClinicRegWaterList();
										for (ClinicRegWaterEntity e : clinicRegWaterlist) {
											String clinicNum = e.getClinicNum();
											if(StringUtils.isNotBlank(clinicNum)) {
												List<ClinicBillEntity> billList = allClinicVo.getClinicBillEntityListByClinicNum(clinicNum);
												List<ClinicCaseEntity> caseList = allClinicVo.getClinicCaseEntityListByClinicNum(clinicNum);
												List<ClinicDiagnosisWaterEntity> diagnosisList = allClinicVo.getClinicDiagnosisWaterEntityListByClinicNum(clinicNum);
												List<ClinicFreeSummaryEntity> freeSummaryList = allClinicVo.getClinicFreeSummaryEntityListByClinicNum(clinicNum);
												List<ClinicPrescriptionDetailEntity> prescriptionList = allClinicVo.getClinicPrescriptionDetailEntityListByClinicNum(clinicNum);
												
												
												
												
												
												
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									logger.error("查询用户信息异常：",e);
								}
							}
						}
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行doC100错误",e);
		}
	}

	
	private PingAnRespVo queryClinicC210(String pageNum,String medicalNum,String treatBeginDate,String treatEndDate,PatientEntity patient) throws Exception {
		PingAnRespVo resp = new PingAnRespVo();
		AbsQueryClinicReqVo reqVo = new AbsQueryClinicReqVo(patient.getPatientId());
		reqVo.setPageIndex(Integer.parseInt(pageNum));
		reqVo.setPageSize(100);
		String startDate = DateOper.formatDate(treatBeginDate, "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
		String endDate = DateOper.formatDate(treatEndDate, "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
		reqVo.setStartDate(startDate);
		reqVo.setEndDate(endDate);
		reqVo.setMedicalNum(medicalNum);
		List<ClinicRegWaterEntity> regWaterList = getClinicService().queryClinicRegisetrInfo(reqVo);
		List<ClinicCaseEntity> caseList = getClinicService().queryClinicCase(reqVo);
		List<ClinicFreeSummaryEntity> freeList = getClinicService().queryClinicFreeSummary(reqVo);
		String conditionDescription = "";
		if(caseList!=null && caseList.size()>0) {
			conditionDescription = caseList.get(0).getDescription();
		}
		String currMedicalCost = "";
		String updateBy = "";
		if(freeList!=null && freeList.size()>0) {
			if(freeList.get(0).getFee()!=null && freeList.get(0).getFee().length()>0) {
				//金额单位转换  分 转元，保留2位小数
				currMedicalCost = ArithUtil.div(freeList.get(0).getFee(), "100", 2);
			}
			updateBy  = freeList.get(0).getUpdateBy();
		}
		JSONArray bodyArray = new JSONArray();
		for (ClinicRegWaterEntity reg : regWaterList) {
			JSONObject bodyJs = new JSONObject();
			bodyJs.put("medicalNum", reg.getMedicalNum());//c_  表示是门诊流水号
			bodyJs.put("medicalType", pingAnUtil.parseHosMedicalTypeToPa(reg.getMedicalType()));
			bodyJs.put("treatDate", DateOper.formatDate(reg.getClinicDate(), "yyyy-MM-dd","yyyyMMddHHmmss"));
			bodyJs.put("conditionDescription", conditionDescription);
			bodyJs.put("inHosDoctorCode", reg.getDoctorId());
			bodyJs.put("inHosDoctorName", reg.getDoctorName());
			bodyJs.put("endemicArea", "");
			bodyJs.put("treatDeptCode", reg.getDepartmentId());
			bodyJs.put("treatDeptName", reg.getDepartmentName());
			bodyJs.put("bunkId", "");
			bodyJs.put("inHospitalNum", "");
			bodyJs.put("credentialType", pingAnUtil.parseHosCertTypeToPa(patient.getCertType()));
			bodyJs.put("credentialNum", patient.getCertNum());
			bodyJs.put("name", patient.getName());
			bodyJs.put("gender", patient.getGender());
			bodyJs.put("birthday", DateOper.formatDate(patient.getBirthday(), "yyyy-MM-dd", "yyyyMMdd"));
			bodyJs.put("race", pingAnUtil.parseHosRaceToPa(patient.getRace()));
			bodyJs.put("homeAddress", patient.getHomeAddress());//家庭地址
			bodyJs.put("companyName", patient.getCompanyName());//单位名称
			bodyJs.put("clientStatus", "");//患者现状
			bodyJs.put("socialNumber", patient.getSsNum());//社保编码
			bodyJs.put("isInSocialSecurityFlg", patient.getIsInSocialSecurityFlg());//是否参加社保
			bodyJs.put("socialSecurityNm", patient.getSocialSecurityNm());//参加何种社保
			bodyJs.put("currMedicalCost", currMedicalCost);//目前医疗费
			bodyJs.put("linkmanName", patient.getGuardianName());//联系人姓名
			bodyJs.put("linkmanMobile", patient.getMobilePhone());//联系人电话
			bodyJs.put("updateBy", updateBy);//经办人
			bodyJs.put("remark", "");//备注
			//查询诊断列表
			JSONArray diagArray = new JSONArray();
			List<ClinicDiagnosisWaterEntity> diagList = getClinicService().queryClinicDiagnosis(reqVo);
			for (ClinicDiagnosisWaterEntity diag : diagList) {
				JSONObject hosDiag = new JSONObject();
				hosDiag.put("diagnosisCode", diag.getDiagnosisCode());
				hosDiag.put("diagnosisName", diag.getDiagnosisName());
				hosDiag.put("clinicalDiagnosis", "");
				hosDiag.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(diag.getDiagnosisType()));
				hosDiag.put("diagSort", diag.getDiagnosisType());
				diagArray.add(hosDiag);
			}
			bodyJs.put("additionalDiagnosisList", diagArray);//诊断列表
			//查询发票
			JSONArray billArray = new JSONArray();
			List<ClinicBillEntity>  billList = getClinicService().queryClinicBill(reqVo);
			for (ClinicBillEntity billEntity : billList) {
				JSONObject bill = new JSONObject();
				bill.put("invoiceNO", billEntity.getInvoiceNum());
				bill.put("sumMoney", billEntity.getInvoiceAmount());
				billArray.add(bill);
			}
			bodyJs.put("invoiceList", billArray);//发票
			bodyArray.add(bodyJs);
		}
		resp.setBody(bodyArray);
		return resp;
	}
	
	/**
	 * C210  查询住院信息
	 * @Description: 
	 * @param pageNum
	 * @param treatBeginDate
	 * @param treatEndDate
	 * @param patient
	 * @return
	 * @throws Exception 
	 */
	private PingAnRespVo queryHospitalizationC210(String pageNum,String medicalNum,String treatBeginDate,String treatEndDate,PatientEntity patient) throws Exception {
		//默认住院,查询住院记录
		PingAnRespVo resp = new PingAnRespVo();
		QueryHospitalizationReqVo queryHospitalization = new QueryHospitalizationReqVo(patient.getPatientId());
		queryHospitalization.setPageIndex(Integer.parseInt(pageNum));
		queryHospitalization.setPageSize(100);
		String startDate = DateOper.formatDate(treatBeginDate, "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
		String endDate = DateOper.formatDate(treatEndDate, "yyyyMMdd", "yyyy-MM-dd HH:mm:ss");
		queryHospitalization.setStartDate(startDate);
		queryHospitalization.setEndDate(endDate);
		queryHospitalization.setInHospitalNum(medicalNum);
		List<HospitalizationEntity> inHosList = getHospitalizationService().queryHospitalization(queryHospitalization);
		
		String updateBy = "";
		String currMedicalCost = "";
		List<HospitalizationFreeSummaryEntity> freeList = getHospitalizationService().queryHospitalizationFreeSummary(queryHospitalization);
		if(freeList!=null && freeList.size()>0) {
			if(freeList.get(0).getFee()!=null) {
				currMedicalCost = ArithUtil.div(freeList.get(0).getFee().toString(), "100",2);
			}
			updateBy = freeList.get(0).getUpdateBy();
		}
		JSONArray bodyArray = new JSONArray();
		for (HospitalizationEntity hosEntity : inHosList) {
			JSONObject bodyJs = new JSONObject();
			bodyJs.put("medicalNum", hosEntity.getInHospitalNum());//h_ 表示是住院流水号
			bodyJs.put("medicalType", pingAnUtil.parseHosMedicalTypeToPa(hosEntity.getMedicalType()));
			bodyJs.put("treatDate", hosEntity.getInHosDate());
			bodyJs.put("conditionDescription", hosEntity.getDescription());
			bodyJs.put("inHosDoctorCode", hosEntity.getInHosDoctorCode());
			bodyJs.put("inHosDoctorName", hosEntity.getInHosDoctorName());
			bodyJs.put("endemicArea", hosEntity.getLesionName());
			bodyJs.put("treatDeptCode", hosEntity.getDeptCod());
			bodyJs.put("treatDeptName", hosEntity.getDeptName());
			bodyJs.put("bunkId", hosEntity.getBunkId());
			bodyJs.put("inHospitalNum", hosEntity.getInHospitalNum());
			bodyJs.put("credentialType", pingAnUtil.parseHosCertTypeToPa(hosEntity.getCertType()));
			bodyJs.put("credentialNum", hosEntity.getCertNum());
			bodyJs.put("name", patient.getName());
			bodyJs.put("gender", patient.getGender());
			bodyJs.put("birthday", DateOper.formatDate(patient.getBirthday(), "yyyy-MM-dd", "yyyyMMdd"));
			bodyJs.put("race", pingAnUtil.parseHosRaceToPa(patient.getRace()));
			bodyJs.put("homeAddress", patient.getHomeAddress());//家庭地址
			bodyJs.put("companyName", patient.getCompanyName());//单位名称
			bodyJs.put("clientStatus", hosEntity.getClientStatus());//患者现状
			bodyJs.put("socialNumber", patient.getSsNum());//社保编码
			bodyJs.put("isInSocialSecurityFlg", patient.getIsInSocialSecurityFlg());//是否参加社保
			bodyJs.put("socialSecurityNm", patient.getSocialSecurityNm());//参加何种社保
			bodyJs.put("currMedicalCost", currMedicalCost);//目前医疗费
			bodyJs.put("linkmanName", patient.getGuardianName());//联系人姓名
			bodyJs.put("linkmanMobile", patient.getMobilePhone());//联系人电话
			bodyJs.put("updateBy", updateBy);//经办人
			bodyJs.put("remark", hosEntity.getDoctorRemark());//备注
			//查询诊断列表
			JSONArray diagArray = new JSONArray();
			QueryHospitalizationReqVo queryHosDiag = new QueryHospitalizationReqVo(patient.getPatientId());
			queryHosDiag.setInHospitalNum(hosEntity.getInHospitalNum());
			List<HospitalizationDiagnosisEntity>  hosDiagList = getHospitalizationService().queryHospitalizationDiagnosis(queryHosDiag);
			for (HospitalizationDiagnosisEntity hosDiagEntity : hosDiagList) {
				JSONObject hosDiag = new JSONObject();
				hosDiag.put("diagnosisCode", hosDiagEntity.getDiagnosisCode());
				hosDiag.put("diagnosisName", hosDiagEntity.getDiagnosisName());
				hosDiag.put("clinicalDiagnosis", hosDiagEntity.getResult());
				hosDiag.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(hosDiagEntity.getDiagnosisType()));
				hosDiag.put("diagSort", hosDiagEntity.getDiagnosisType());
				diagArray.add(hosDiag);
			}
			bodyJs.put("additionalDiagnosisList", diagArray);//诊断列表
			//查询发票
			JSONArray billArray = new JSONArray();
			List<HospitalizationBillEntity>  billList = getHospitalizationService().queryHospitalizationBill(queryHosDiag);
			for (HospitalizationBillEntity billEntity : billList) {
				JSONObject bill = new JSONObject();
				bill.put("invoiceNO", billEntity.getInvoiceNum());
				bill.put("sumMoney", billEntity.getInvoiceAmount());
				billArray.add(bill);
			}
			bodyJs.put("invoiceList", billArray);//发票
			bodyArray.add(bodyJs);
		}
		resp.setBody(bodyArray);
		
		return resp;
	}
	
	@Override
	public PingAnRespVo doC210(PingAnReqVo vo) {
		PingAnRespVo resp = null;
		try {
			//请求入参
			JSONObject reqJs = vo.getReqBody().getJSONObject(0);
			//住院号
			String inHospitalNum = reqJs.getString("inHospitalNum");
			//证件类型
			String credentialType = reqJs.getString("credentialType");
			//证件号码
			String credentialNum = reqJs.getString("credentialNum");
			//姓名
			String name = reqJs.getString("name");
			//字典映射 1-门急诊 2-住院, 0-“门诊+住院”，如果为空默认为住院
			String treatType = reqJs.getString("treatType");
			//就诊开始日期
			String treatBeginDate = reqJs.getString("treatBeginDate");
			//就诊结束日期
			String treatEndDate = reqJs.getString("treatEndDate");
			//授权号
//			String authorizationNum = reqJs.getString("authorizationNum");
			//当前页
			String pageNum = reqJs.getString("pageNum");
			
			//分页查询患者信息
			ReqQueryPatientListVo queryPatVo = new ReqQueryPatientListVo();
			queryPatVo.setCertType(pingAnUtil.parsePingAnCertTypeToHos(credentialType));
			queryPatVo.setCertNum(credentialNum);
			queryPatVo.setName(name);
			queryPatVo.setInHospitalNum(inHospitalNum);
			List<PatientEntity> patList = getPersionService().queryPatientEntityList(queryPatVo);
			if(patList==null || patList.size()<=0) {
				//没有找到用户信息
				logger.error("没有找到对应的用户信息：Param="+JSON.toJSONString(queryPatVo));
				return resp;
			}
			PatientEntity patient = patList.get(0);
			if(StringUtil.isBlank(treatType) || "2".equals(treatType)) {
				resp = queryHospitalizationC210(pageNum,null, treatBeginDate, treatEndDate, patient);
			}else if("0".equals(treatType)) {
				//门诊+住院
				PingAnRespVo clinicResp = queryClinicC210(pageNum, null,treatBeginDate, treatEndDate, patient);
				PingAnRespVo hosResp = queryHospitalizationC210(pageNum,null, treatBeginDate, treatEndDate, patient);
				//合并
				JSONArray clinicArray = clinicResp.getBody();
				JSONArray hosArray = hosResp.getBody();
				for(int i=0;i<clinicArray.size();i++) {
					hosArray.add(clinicArray.getJSONObject(i));
				}
				resp = new PingAnRespVo();
				resp.setBody(hosArray);
			}else if("1".equals(treatType)) {
				//门急诊
				resp = queryClinicC210(pageNum,null, treatBeginDate, treatEndDate, patient);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行doC210异常",e);
		}
		return resp;
	}

	@Override
	public void doC220(Date startTime) {
		try {
			//读取ES中的数据
			ElasticIndex index = ElasticIndex.pingan;
			Map<String, Object> _search = ElasticBus.create().setSize(20).createSimpleQuery()
			.createElasticQuerySimpleBool()
			.addMustTerm("callType", "C220")
			.addMustRange(ElasticRangeEnum.gte, "inserttime", DateOper.formatDate(startTime, "yyyy-MM-dd HH:mm:ss"))
			.getSearch();
			
			String ips = null == eSConfig? "127.0.0.1:9200":eSConfig.getUrl();
			ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
			.set_search(_search);
			ElasticRestResponse resp = sender.simpleQuery(index.name());
			resp.setParse(new IElasticQueryResponseScrollParse() {
				@Override
				public void parse(ElasticQueryResponse response, ElasticQueryResponseHits_Hits hist, String _source, int index) {
					String id = hist.get_id();
					System.out.println("_id="+id);
					ElasticQueryResponseHits hits = response.getHits();
					if(null != hits) {
						JSONObject json = JSON.parseObject(_source);
						JSONArray reqBody = json.getJSONArray("reqBody");
						for (Object o : reqBody) {
							JSONObject obj = (JSONObject) o;
							PingAnC220 c220 = new PingAnC220();
							c220.setJson(obj);
							String medicalNum = c220.getMedicalNum();
							doS230(medicalNum);
							doS250(medicalNum);
							doS340(medicalNum);
						}
					}
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据就诊流水号或住院号查询患者信息
	 * @Description: 
	 * @param medicalNum
	 * @return
	 * @throws Exception
	 */
	private PatientEntity getPatientByMedicalNum(String medicalNum) throws Exception {
		//查询住院患者信息
		PatientEntity patient = null;
		ElasticIndex index = ElasticIndex.hospitalization;
		Map<String, Object> _search = ElasticBus.create().setSize(20).createSimpleQuery()
		.createElasticQuerySimpleBool().addMustTerm("medicalNum", medicalNum).getSearch();
		String ips = null == eSConfig? "127.0.0.1:9200":eSConfig.getUrl();
		ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST).set_search(_search);
		String patientId = null;
		ElasticRestResponse resp = sender.simpleQuery(index.name());
		ElasticQueryResponseJsonVo vo = resp.setParseToJson();
		List<JSONObject> list = vo.get_sources();
		if(list!=null && list.size()>0) {
			patientId = list.get(0).getString("patientId");
		}
		if(StringUtil.isNotBlank(patientId)) {
			//分页查询患者信息
			ReqQueryPatientListVo queryPatVo = new ReqQueryPatientListVo();
			queryPatVo.setPatientId(patientId);
			List<PatientEntity> patList = getPersionService().queryPatientEntityList(queryPatVo);
			if(patList!=null && patList.size()>0) {
				patient = patList.get(0);
			}
		}
		
		if(patient == null) {
			//查询门诊患者信息
			index = ElasticIndex.clinicregisetrinfo;
			_search = ElasticBus.create().setSize(20).createSimpleQuery()
					.createElasticQuerySimpleBool()
					.addMustTerm("medicalNum", medicalNum)
					.getSearch();
			ips = null == eSConfig? "127.0.0.1:9200":eSConfig.getUrl();
			sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST).set_search(_search);
			resp = sender.simpleQuery(index.name());
			vo = resp.setParseToJson();
			list = vo.get_sources();
			if(list!=null && list.size()>0) {
				patientId = list.get(0).getString("patientId");
			}
			if(StringUtil.isNotBlank(patientId)) {
				//分页查询患者信息
				ReqQueryPatientListVo queryPatVo = new ReqQueryPatientListVo();
				queryPatVo.setPatientId(patientId);
				List<PatientEntity> patList = getPersionService().queryPatientEntityList(queryPatVo);
				if(patList!=null && patList.size()>0) {
					patient = patList.get(0);
				}
			}
		}
		if(patient!=null) {
			return patient;
		}else {
			logger.error("没有找到对应的用户信息：medicalNum="+medicalNum);
			throw new Exception("没有找到对应的用户信息：medicalNum="+medicalNum);
		}
	}
	
	@Override
	public void doS230(String medicalNum) {
		try {
			PingAnReqVo s230Req = new PingAnReqVo();
			PatientEntity patient = getPatientByMedicalNum(medicalNum);
			PingAnReqVo s230ReqClinic = queryClinicS230(medicalNum, patient);
			PingAnReqVo s230ReqHos = queryHospitalizationS230(medicalNum, patient);
			//合并
			JSONArray clinicArray = s230ReqClinic.getReqBody();
			JSONArray hosArray = s230ReqHos.getReqBody();
			for(int i=0;i<clinicArray.size();i++) {
				hosArray.add(clinicArray.getJSONObject(i));
			}
			
			//调用平安 S230
			s230Req.setReqHead(client.getHeadParam(PingAnBussId.S230, hosClientConfig.getSenderCode(),hosArray.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			s230Req.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			
			if(hosArray.size()>100) {
				//分页传输
				int total = hosArray.size();//总记录数
				int pageSize = 100;	//每页条数
				int pageCount = (int) Math.ceil((double)  total/ pageSize);//总页数
				int ii = 0;//下标，用于获取对应的记录
				for(int pageIndex=1;pageIndex<=pageCount;pageIndex++) {//当前页码
					int pCount = 0;//当前页最大记录数
					if(pageIndex==pageCount) {
						//最后一页
						pCount = total;
					}else {
						//计算当前页需要获取的记录数
						pCount = pageIndex*pageSize;
					}
					JSONArray pageArray = new JSONArray();
					while(ii<pCount) {
						pageArray.add(hosArray.getJSONObject(ii));
						ii++;
					}
					//发送
					s230Req.setReqBody(pageArray);
					
					JSONObject packageJs = new JSONObject();
					packageJs.put("body", s230Req.getReqBody());
					packageJs.put("head", s230Req.getReqHead());
					packageJs.put("additionInfo", s230Req.getReqAdditionInfo());
					PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
					
					PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
					vo.parseResp(PingAnBussId.S230,s230Req,resp);
					
					ElasticIndex index = ElasticIndex.pingan;
					entityUtils.save2EsDB(index, vo);
				}
			}else {
				//直接传输
				s230Req.setReqBody(hosArray);
				
				JSONObject packageJs = new JSONObject();
				packageJs.put("body", s230Req.getReqBody());
				packageJs.put("head", s230Req.getReqHead());
				packageJs.put("additionInfo", s230Req.getReqAdditionInfo());
				PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
				
				PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
				vo.parseResp(PingAnBussId.S230,s230Req,resp);
				
				ElasticIndex index = ElasticIndex.pingan;
				entityUtils.save2EsDB(index, vo);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S230异常：medicalNum="+medicalNum,e);
		}
	}
	
	/**
	 * S250 上传处方明细,针对住院
	 */
	@Override
	public void doS250(String medicalNum) {
		try {
			JSONArray body = new JSONArray();
			PatientEntity patient = getPatientByMedicalNum(medicalNum);
			if(patient!=null) {
				//查询住院
				QueryHospitalizationReqVo hosReqVo = new QueryHospitalizationReqVo(patient.getPatientId());
				hosReqVo.setMedicalNum(medicalNum);
				List<HospitalizationPerscriptionDetailEntity> hosList = getHospitalizationService().queryHospitalizationPerscriptionDetail(hosReqVo);
				for (HospitalizationPerscriptionDetailEntity en : hosList) {
					JSONObject row = new JSONObject();
					row.put("medicalNum", en.getMedicalNum());//就诊流水号
					row.put("listCat", pingAnUtil.parseHosCatToPa(en.getMedItemCtg()));//目录类别 
					row.put("medicalItemCat", pingAnUtil.parseHosItemCodeToPa(en.getItemCode()));//医疗项目类别
					row.put("recipeNum", en.getReceiptNum());//处方号
					row.put("recipeSerialNum", en.getRecipeSerialNum());//处方明细流水号
					row.put("recipeDate", pingAnUtil.formatDate(en.getRecipeDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));//处方日期YYYYMMDDHH24MISS
					row.put("hospitalChargeCode", en.getHospitalChargeCode());//医院收费项目编码
					row.put("hospitalChargeName", en.getHospitalChargeName());//医院收费项目名称
					row.put("centreChargeCode", en.getSsItemCode());//医保收费项目编码
					row.put("medicareFeeitemName", en.getSsItemName());//医保收费项目名称
					row.put("price", en.getPrice());//单价
					row.put("quantity", en.getSize());//数量
					row.put("money", en.getAmount());//金额
					row.put("hosBearMoney", en.getHosBearMoney());//医院负担费用
					row.put("formulation", "");//剂型
					row.put("spec", en.getSpec());//规格
					row.put("herbFuFangSign", en.getIsSingleRemedy());//草药单复方标志
					row.put("totalSelfFundFlg", "");//全额自费标志
					row.put("extraRecipeFlg", en.getExtraRecipeFlg());//外带处方标志0 非外带处方；1 外带处方
					row.put("usage", en.getDrugDeliverType());//用法
					row.put("perQuantity", en.getSingleUseSize());//每次用量
					row.put("frequency", en.getEveryDaySize());//使用频次
					row.put("exeDays", en.getUseDaySize());//执行天数
					row.put("packetNum", en.getHerbalSize());//帖数
					row.put("isRestricted", en.getIsSeriousDrugs());//限制用药标识是或否；1 是 0 否
					row.put("restrictions", en.getSeriousDrugs());//限制用药限制范围说明
					row.put("isBasicMedicine", en.getDrugFlag());//基药标识
					row.put("deptNum", en.getDepartmentName());//科室编码
					row.put("deptName", en.getDepartmentName());//科室名称
					row.put("doctorCode", en.getDoctorId());//处方医生编码
					row.put("doctorName", en.getDoctorName());//处方医生姓名
					row.put("updateBy", en.getUpdateBy());//经办人
					row.put("selfPayRatio", en.getSelfPayRatio());//自付比例
					row.put("medlimitedPrice", en.getMedlimitedPrice());//医保限价
					body.add(row);
				}
			}
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S250, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			
			if(body.size()>100) {
				//分页传输
				int total = body.size();//总记录数
				int pageSize = 100;	//每页条数
				int pageCount = (int) Math.ceil((double)  total/ pageSize);//总页数
				int ii = 0;//下标，用于获取对应的记录
				for(int pageIndex=1;pageIndex<=pageCount;pageIndex++) {//当前页码
					int pCount = 0;//当前页最大记录数
					if(pageIndex==pageCount) {
						//最后一页
						pCount = total;
					}else {
						//计算当前页需要获取的记录数
						pCount = pageIndex*pageSize;
					}
					JSONArray pageArray = new JSONArray();
					while(ii<pCount) {
						pageArray.add(body.getJSONObject(ii));
						ii++;
					}
					//发送
					reqVo.setReqBody(pageArray);
					
					//调用S250接口
					JSONObject packageJs = new JSONObject();
					packageJs.put("body", reqVo.getReqBody());
					PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
					
					PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
					vo.parseResp(PingAnBussId.S250,reqVo,resp);
					
					ElasticIndex index = ElasticIndex.pingan;
					entityUtils.save2EsDB(index, vo);
				}
			}else {
				//直接传输
				reqVo.setReqBody(body);
				
				//调用S250接口
				JSONObject packageJs = new JSONObject();
				packageJs.put("body", reqVo.getReqBody());
				PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
				
				PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
				vo.parseResp(PingAnBussId.S250,reqVo,resp);
				
				ElasticIndex index = ElasticIndex.pingan;
				entityUtils.save2EsDB(index, vo);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S250异常：medicalNum="+medicalNum,e);
		}
		
	}

	private PingAnReqVo queryClinicS230(String medicalNum,PatientEntity patient) throws Exception {
		PingAnReqVo req = new PingAnReqVo();
		AbsQueryClinicReqVo reqVo = new AbsQueryClinicReqVo(patient.getPatientId());
		reqVo.setMedicalNum(medicalNum);
		List<ClinicRegWaterEntity> regWaterList = getClinicService().queryClinicRegisetrInfo(reqVo);
		JSONArray bodyArray = new JSONArray();
		for (ClinicRegWaterEntity reg : regWaterList) {
			JSONObject bodyJs = new JSONObject();
			bodyJs.put("medicalNum", reg.getMedicalNum());//C_  表示是门诊流水号
			bodyJs.put("medicalType", pingAnUtil.parseHosMedicalTypeToPa(reg.getMedicalType()));
			bodyJs.put("inHospitalNum", "");
			bodyJs.put("treatDeptCode", reg.getDepartmentId());
			bodyJs.put("treatDeptName", reg.getDepartmentName());
			bodyJs.put("bunkId", "");
			bodyJs.put("clientStatus", "");//患者现状
			bodyJs.put("inHosDoctorCode", reg.getDoctorId());
			bodyJs.put("inHosDoctorName", reg.getDoctorName());
			bodyJs.put("conditionDescription", "");
			bodyJs.put("linkmanName", patient.getGuardianName());//联系人姓名
			bodyJs.put("linkmanMobile", patient.getMobilePhone());//联系人电话
			bodyJs.put("email", "");
			bodyJs.put("updateBy", reg.getUpdateBy());//经办人
			bodyJs.put("updateDate", pingAnUtil.formatDate(reg.getUpdateDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));//经办时间YYYYMMDDHH24MISS
			bodyJs.put("guardianIdType", pingAnUtil.parseHosDiagnosisTypeToPa(patient.getGuardianIdType()));
			bodyJs.put("guardianIdNo", patient.getGuardianIdNo());
			bodyJs.put("guardianName", patient.getGuardianName());
			bodyJs.put("guardianGender", patient.getGuardianGender());
			bodyJs.put("guardianBirthday", DateOper.formatDate(patient.getGuardianBirthday(), "yyyy-MM-dd", "yyyyMMdd"));
			bodyJs.put("guardianIDDueDay", "");//YYYYMMDD
			
			
			//查询诊断列表
			JSONArray diagArray = new JSONArray();
			List<ClinicDiagnosisWaterEntity> diagList = getClinicService().queryClinicDiagnosis(reqVo);
			for (ClinicDiagnosisWaterEntity diag : diagList) {
				JSONObject hosDiag = new JSONObject();
				hosDiag.put("diagnosisCode", diag.getDiagnosisCode());
				hosDiag.put("diagnosisName", diag.getDiagnosisName());
				hosDiag.put("clinicalDiagnosis", "");
				hosDiag.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(diag.getDiagnosisType()));
				hosDiag.put("diagSort", diag.getDiagnosisType());
				diagArray.add(hosDiag);
			}
			bodyJs.put("additionalDiagnosisList", diagArray);//诊断列表
			bodyArray.add(bodyJs);
		}
		req.setReqBody(bodyArray);
		return req;
	}
	private PingAnReqVo queryHospitalizationS230(String medicalNum,PatientEntity patient) throws Exception {
		//默认住院,查询住院记录
		PingAnReqVo resp = new PingAnReqVo();
		QueryHospitalizationReqVo queryHospitalization = new QueryHospitalizationReqVo(patient.getPatientId());
		queryHospitalization.setMedicalNum(medicalNum);
		List<HospitalizationEntity> inHosList = getHospitalizationService().queryHospitalization(queryHospitalization);
		JSONArray bodyArray = new JSONArray();
		for (HospitalizationEntity hosEntity : inHosList) {
			JSONObject bodyJs = new JSONObject();
			bodyJs.put("medicalNum", hosEntity.getMedicalNum());
			bodyJs.put("medicalType", pingAnUtil.parseHosMedicalTypeToPa(hosEntity.getMedicalType()));
			bodyJs.put("inHospitalNum", hosEntity.getInHospitalNum());
			bodyJs.put("treatDeptCode", hosEntity.getDeptCod());
			bodyJs.put("treatDeptName", hosEntity.getDeptName());
			bodyJs.put("bunkId", hosEntity.getBunkId());
			bodyJs.put("clientStatus", hosEntity.getClientStatus());//患者现状
			bodyJs.put("inHosDoctorCode", hosEntity.getInHosDoctorCode());
			bodyJs.put("inHosDoctorName", hosEntity.getInHosDoctorName());
			bodyJs.put("conditionDescription", hosEntity.getDescription());
			bodyJs.put("linkmanName", patient.getGuardianName());//联系人姓名
			bodyJs.put("linkmanMobile", patient.getMobilePhone());//联系人电话
			bodyJs.put("email", "");
			bodyJs.put("updateBy", hosEntity.getUpdateBy());//经办人
			bodyJs.put("updateDate", pingAnUtil.formatDate(hosEntity.getUpdateDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));//经办时间YYYYMMDDHH24MISS
			bodyJs.put("guardianIdType", pingAnUtil.parseHosDiagnosisTypeToPa(patient.getGuardianIdType()));
			bodyJs.put("guardianIdNo", patient.getGuardianIdNo());
			bodyJs.put("guardianName", patient.getGuardianName());
			bodyJs.put("guardianGender", pingAnUtil.parseHosGenderToPa(patient.getGuardianGender()));
			bodyJs.put("guardianBirthday", DateOper.formatDate(patient.getGuardianBirthday(), "yyyy-MM-dd", "yyyyMMdd"));
			bodyJs.put("guardianIDDueDay", "");//YYYYMMDD
			
			//查询诊断列表
			JSONArray diagArray = new JSONArray();
			QueryHospitalizationReqVo queryHosDiag = new QueryHospitalizationReqVo(patient.getPatientId());
			queryHosDiag.setInHospitalNum(hosEntity.getInHospitalNum());
			List<HospitalizationDiagnosisEntity>  hosDiagList = getHospitalizationService().queryHospitalizationDiagnosis(queryHosDiag);
			for (HospitalizationDiagnosisEntity hosDiagEntity : hosDiagList) {
				JSONObject hosDiag = new JSONObject();
				hosDiag.put("diagnosisCode", hosDiagEntity.getDiagnosisCode());
				hosDiag.put("diagnosisName", hosDiagEntity.getDiagnosisName());
				hosDiag.put("clinicalDiagnosis", hosDiagEntity.getResult());
				hosDiag.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(hosDiagEntity.getDiagnosisType()));
				hosDiag.put("diagSort", hosDiagEntity.getDiagnosisType());
				diagArray.add(hosDiag);
			}
			bodyJs.put("additionalDiagnosisList", diagArray);//诊断列表
			bodyArray.add(bodyJs);
		}
		resp.setReqBody(bodyArray);
		
		return resp;
	}

	@Override
	public void doS340(String medicalNum) {
		try {
			JSONArray body = new JSONArray();
			PatientEntity patient = getPatientByMedicalNum(medicalNum);
			if(patient!=null) {
				ReqQueryCasesVo reqVo = new ReqQueryCasesVo(patient.getPatientId());
				reqVo.setEventNo(medicalNum);
				List<QueryHospitalRecordEntity> list = getCasesService().queryHospitalRecordList(reqVo);
				for (QueryHospitalRecordEntity en : list) {
					JSONObject row = new JSONObject();
					row.put("medicalNum", medicalNum);//就诊流水号
					row.put("hospitalRecordId", en.getClinicCaseId());//医院病历 IDv
					row.put("inHospitalNum", en.getEventNo());//住院号
					row.put("cheifComplaint", en.getComplained());//主诉
					row.put("historyPresentIllness", en.getHistoryPresentIllness());//现病史
					row.put("pastDiseaseHistory", en.getPastDiseaseHistory());//既往史
					row.put("personalHistory", en.getSocialhistory());//个人史
					row.put("obstetricalHistory", en.getObstetricalHistory());//婚育史
					row.put("menstruationHistory", en.getMenstruationHistory());//月经史
					row.put("familyHistory", en.getFamilyHistory());//家族史
					
					List<QueryDiseaseEntity> diseaseList = getCasesService().queryDiseaseList(reqVo);
					JSONArray disArray = new JSONArray();
					for (QueryDiseaseEntity disEntity : diseaseList) {
						JSONObject disJs = new JSONObject();
						disJs.put("diseaseCode", disEntity.getDiseaseCode());//疾病代码 
						disJs.put("diseaseName", disEntity.getDiseaseName());//疾病名称
						disArray.add(disJs);
					}
					row.put("diseaseList", disArray);//疾病信息列表
					
					List<QueryOperationEntity> operList = getCasesService().queryOperationList(reqVo);
					JSONArray opArray = new JSONArray();
					for (QueryOperationEntity opEntity : operList) {
						JSONObject opJs = new JSONObject();
						opJs.put("operationCode", opEntity.getOperationCode());//手术编码 
						opJs.put("operationName", opEntity.getOperationName());//手术名称 
						opArray.add(opJs);
					}
					row.put("operationList", opArray);//手术信息列表
					
					row.put("diagnosisTreatment", en.getDiagnosisTreatment());//诊治经过 
					row.put("attendingPhysician", en.getAttendingPhysician());//主治医生 
					row.put("dischargeStatus", en.getDischargeStatus());//出院情况
					row.put("dischargeOrder", en.getDischargeOrder());//出院医嘱 
					row.put("medicalAbstract", en.getMedicalAbstract());//事件摘要
					row.put("physicalExamination", en.getPhysicalExamination());//体格检查
					row.put("juniorCollege", en.getJuniorCollege());//专科情况
					row.put("auxiliaryExamination", en.getAuxiliaryExamination());//辅助检查 
					row.put("totalRecordInfo", en.getTotalRecordInfo());//全量病历信息 
					body.add(row);
				}
			}
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S340, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			if(body.size()>100) {
				//分页传输
				int total = body.size();//总记录数
				int pageSize = 100;	//每页条数
				int pageCount = (int) Math.ceil((double)  total/ pageSize);//总页数
				int ii = 0;//下标，用于获取对应的记录
				for(int pageIndex=1;pageIndex<=pageCount;pageIndex++) {//当前页码
					int pCount = 0;//当前页最大记录数
					if(pageIndex==pageCount) {
						//最后一页
						pCount = total;
					}else {
						//计算当前页需要获取的记录数
						pCount = pageIndex*pageSize;
					}
					JSONArray pageArray = new JSONArray();
					while(ii<pCount) {
						pageArray.add(body.getJSONObject(ii));
						ii++;
					}
					
					//调用S340接口
					reqVo.setReqBody(pageArray);
					
					JSONObject packageJs = new JSONObject();
					packageJs.put("body", reqVo.getReqBody());
					packageJs.put("head", reqVo.getReqHead());
					packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
					PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
					
					PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
					vo.parseResp(PingAnBussId.S340,reqVo,resp);
					
					ElasticIndex index = ElasticIndex.pingan;
					entityUtils.save2EsDB(index, vo);
				}
			}else {
				//直接传输
				reqVo.setReqBody(body);
				
				JSONObject packageJs = new JSONObject();
				packageJs.put("body", reqVo.getReqBody());
				packageJs.put("head", reqVo.getReqHead());
				packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
				PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
				
				PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
				vo.parseResp(PingAnBussId.S340,reqVo,resp);
				
				ElasticIndex index = ElasticIndex.pingan;
				entityUtils.save2EsDB(index, vo);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S340异常：medicalNum="+medicalNum,e);
		}
	}
	
	/**
	 * 住院费用结算
	 */
	@Override
	public PingAnReqAndRespVo doS291(String inputXml) {
		try {
			JSONArray body = new JSONArray();
			JSONObject json = new JSONObject();
			Document doc = DocumentHelper.parseText(inputXml);
			Element root = doc.getRootElement();
			Element data = root.element(KstHosConstant.DATA);
			json.put("medicalNum", XMLUtil.getString(data, "medicalNum", true));
			json.put("billNum", XMLUtil.getString(data, "receiptNum", true));
			json.put("invoiceNO", XMLUtil.getString(data, "invoiceNum", false));
			json.put("medicalType", pingAnUtil.parseHosMedicalTypeToPa(XMLUtil.getString(data, "medicalType", false)));
			json.put("settleDate", pingAnUtil.formatDate(XMLUtil.getString(data, "settleDate", false), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			json.put("dischDate", pingAnUtil.formatDate(XMLUtil.getString(data, "dischDate", false), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			json.put("dischCause", XMLUtil.getString(data, "dischCause", false));
			json.put("complication", XMLUtil.getString(data, "complication", false));
			json.put("wardshipStartDate", pingAnUtil.formatDate(XMLUtil.getString(data, "wardshipStartDate", false), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			json.put("billType", XMLUtil.getString(data, "materialType", false));
			json.put("hospitalDay", XMLUtil.getString(data, "hospitalDay", false));
			json.put("sumMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(data, "fee", false)));
			json.put("updateBy", XMLUtil.getString(data, "updateBy", false));
			JSONArray comArray = new JSONArray();
			Iterator<?> it = data.elementIterator(KstHosConstant.DATA_1);
			while(it.hasNext()) {
				Element el = (Element) it.next();
				JSONObject row = new JSONObject();
				row.put("selfCareAmount", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "selfCareAmount", false)));
				row.put("selfAmount", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "selfAmount", false)));
				row.put("inInsureMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "inInsureMoney", false)));
				row.put("medicareFundCost", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "medicareFundCost", false)));
				row.put("medicarePayLine", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "medicarePayLine", false)));
				row.put("perBearMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "perBearMoney", false)));
				row.put("hosBearMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "hosBearMoney", false)));
				row.put("priorBurdenMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "priorBurdenMoney", false)));
				row.put("sectionCoordinatePayMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "sectionCoordinatePayMoney", false)));
				row.put("overCappingPayMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "overCappingPayMoney", false)));
				row.put("fundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "fundMoney", false)));
				row.put("civilServantFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "civilServantFundMoney", false)));
				row.put("seriousFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "seriousFundMoney", false)));
				row.put("accountFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "accountFundMoney", false)));
				row.put("civilSubsidy", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "civilSubsidy", false)));
				row.put("otherFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "otherFundMoney", false)));
				row.put("cashMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "cashMoney", false)));
				comArray.add(row);
			}
			json.put("composite", comArray);
			json.put("isPrintPolicy", XMLUtil.getString(data, "isPrintPolicy", false));
			json.put("directServiceMark", XMLUtil.getString(data, "directServiceMark", false));
			JSONArray diaArray = new JSONArray();
			it = data.elementIterator(KstHosConstant.DATA_2);
			while(it.hasNext()) {
				Element el = (Element) it.next();
				JSONObject row = new JSONObject();
				row.put("diagnosisCode", XMLUtil.getString(el, "diagnosisCode", false));
				row.put("diagnosisName", XMLUtil.getString(el, "diagnosisName", false));
				row.put("clinicalDiagnosis", XMLUtil.getString(el, "clinicalDiagnosis", false));
				row.put("diagnosisType", XMLUtil.getString(el, "diagnosisType", false));
				row.put("diagSort", XMLUtil.getString(el, "diagSort", false));
				diaArray.add(row);
			}
			json.put("additionalDiagnosisList", diaArray);
			body.add(json);
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S291, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			reqVo.setReqBody(body);
			
			JSONObject packageJs = new JSONObject();
			packageJs.put("body", reqVo.getReqBody());
			packageJs.put("head", reqVo.getReqHead());
			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
			PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
			
			PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
			vo.parseResp(PingAnBussId.S291,reqVo,resp);
			
			ElasticIndex index = ElasticIndex.pingan;
			entityUtils.save2EsDB(index, vo);
			
			return vo;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S291异常：inputXml="+inputXml,e);
			return null;
		}
	}
//	@Override
//	public void doS291(String medicalNum) {
//		String realMedicalNum = null;
//		PatientEntity patient = null;
//		JSONArray body = new JSONArray();
//		try {
//			if(medicalNum.startsWith(C_PREFIX)) {
//				//门诊
//				realMedicalNum = medicalNum.replace(C_PREFIX, "");
//				patient = getPatientByMedicalNum(medicalNum);
//				AbsQueryClinicReqVo reqVo = new AbsQueryClinicReqVo(patient);
//				reqVo.setPatientId(patient.getPatientId());
//				reqVo.setMedicalNum(realMedicalNum);
//				List<ClinicBillEntity> list = getClinicService().queryClinicBill(reqVo);
////				List<ClinicFreeSummaryEntity> list = getClinicService().queryClinicFreeSummary(reqVo);
//				for (ClinicBillEntity en : list) {
//					JSONObject row = new JSONObject();
//					row.put("medicalNum", C_PREFIX+en.getMedicalNum());//就诊流水号
//					row.put("billNum", en.getReceiptNum());//单据号
//					row.put("invoiceNO", en.getInvoiceNum());//发票号
//					row.put("medicalType", "");//医疗类别
//					row.put("settleDate", "");//结算日期
//					row.put("dischDate", "");//出院日期
//					row.put("dischCause", "");//出院原因
//					row.put("complication", "");//并发症
//					row.put("wardshipStartDate", "");//重监病房入住日期
//					row.put("wardshipEndDate", "");//重监病房离开日期
//					row.put("billType", "");//材料类型
//					row.put("hospitalDay", "");//住院天数
//					row.put("sumMoney", "");//费用总额
//					row.put("updateBy", "");//经办人
//					row.put("composite", "");//医保已支付费用列表
//					row.put("isPrintPolicy", "");//是否打印理赔单
//					row.put("directServiceMark", "");//是否直结
//					
//					row.put("selfCareAmount", "");//自理金额
//					row.put("selfAmount", "");//自费金额
//					row.put("inInsureMoney", "");//符合医保费用
//					row.put("medicareFundCost", "");//医保基金
//					row.put("medicarePayLine", "");//医保起付线
//					row.put("perBearMoney", "");//个人自付
//					row.put("hosBearMoney", "");//医院负担
//					row.put("priorBurdenMoney", "");//转诊先自付
//					row.put("sectionCoordinatePayMoney", "");//统筹分段自付
//					row.put("overCappingPayMoney", "");//超封顶线自付
//					row.put("fundMoney", "");//统筹基金支付
//					row.put("civilServantFundMoney", "");//公务员基金支付
//					row.put("seriousFundMoney", "");//大病基金支付
//					row.put("accountFundMoney", "");//账户支付
//					row.put("civilSubsidy", "");//民政救助支付
//					row.put("otherFundMoney", "");//其他基金支付
//					row.put("cashMoney", "");//本次现金支付
//					//查询诊断列表
//					JSONArray diaArray = new JSONArray();
//					List<ClinicDiagnosisWaterEntity> diaList = getClinicService().queryClinicDiagnosis(reqVo);
//					for (ClinicDiagnosisWaterEntity diag : diaList) {
//						JSONObject diaJs = new JSONObject();
//						diaJs.put("diagnosisCode", diag.getDiagnosisCode());//诊断编码
//						diaJs.put("diagnosisName", diag.getDiagnosisName());//诊断名称
//						diaJs.put("clinicalDiagnosis", "");//临床诊断
//						diaJs.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(diag.getDiagnosisType()));//诊断类型
//						diaJs.put("diagSort", diag.getDiagnosisType());//诊断排序
//						diaArray.add(diaJs);
//					}
//					row.put("additionalDiagnosisList", diaArray);//诊断列表
//					body.add(row);
//				}
//			}else if(medicalNum.startsWith(H_PREFIX)) {
//				//住院
//				realMedicalNum = medicalNum.replace(H_PREFIX, "");
//				patient = getPatientByMedicalNum(realMedicalNum);
//				
//				QueryHospitalizationReqVo reqVo = new QueryHospitalizationReqVo(patient);
//				reqVo.setPatientId(patient.getPatientId());
//				reqVo.setInHospitalNum(realMedicalNum);
//				List<HospitalizationBillEntity> list = getHospitalizationService().queryHospitalizationBill(reqVo);
//				for (HospitalizationBillEntity en : list) {
//					JSONObject row = new JSONObject();
//					row.put("medicalNum", H_PREFIX+en.getInHospitalNum());//就诊流水号
//					row.put("billNum", en.getReceiptNum());//单据号
//					row.put("invoiceNO", en.getInvoiceNum());//发票号
//					row.put("medicalType", "");//医疗类别
//					row.put("settleDate", "");//结算日期
//					row.put("dischDate", "");//出院日期
//					row.put("dischCause", "");//出院原因
//					row.put("complication", "");//并发症
//					row.put("wardshipStartDate", "");//重监病房入住日期
//					row.put("wardshipEndDate", "");//重监病房离开日期
//					row.put("billType", "");//材料类型
//					row.put("hospitalDay", "");//住院天数
//					row.put("sumMoney", "");//费用总额
//					row.put("updateBy", "");//经办人
//					row.put("composite", "");//医保已支付费用列表
//					row.put("isPrintPolicy", "");//是否打印理赔单
//					row.put("directServiceMark", "");//是否直结
//					
//					row.put("selfCareAmount", "");//自理金额
//					row.put("selfAmount", "");//自费金额
//					row.put("inInsureMoney", "");//符合医保费用
//					row.put("medicareFundCost", "");//医保基金
//					row.put("medicarePayLine", "");//医保起付线
//					row.put("perBearMoney", "");//个人自付
//					row.put("hosBearMoney", "");//医院负担
//					row.put("priorBurdenMoney", "");//转诊先自付
//					row.put("sectionCoordinatePayMoney", "");//统筹分段自付
//					row.put("overCappingPayMoney", "");//超封顶线自付
//					row.put("fundMoney", "");//统筹基金支付
//					row.put("civilServantFundMoney", "");//公务员基金支付
//					row.put("seriousFundMoney", "");//大病基金支付
//					row.put("accountFundMoney", "");//账户支付
//					row.put("civilSubsidy", "");//民政救助支付
//					row.put("otherFundMoney", "");//其他基金支付
//					row.put("cashMoney", "");//本次现金支付
//					//查询诊断列表
//					JSONArray diaArray = new JSONArray();
//					List<HospitalizationDiagnosisEntity> diaList = getHospitalizationService().queryHospitalizationDiagnosis(reqVo);
//					for (HospitalizationDiagnosisEntity diag : diaList) {
//						JSONObject diaJs = new JSONObject();
//						diaJs.put("diagnosisCode", diag.getDiagnosisCode());//诊断编码
//						diaJs.put("diagnosisName", diag.getDiagnosisName());//诊断名称
//						diaJs.put("clinicalDiagnosis", "");//临床诊断
//						diaJs.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(diag.getDiagnosisType()));//诊断类型
//						diaJs.put("diagSort", diag.getDiagnosisType());//诊断排序
//						diaArray.add(diaJs);
//					}
//					row.put("additionalDiagnosisList", diaArray);//诊断列表
//					body.add(row);
//				}
//			}
//			//调用平安S291
//			PingAnReqVo reqVo = new PingAnReqVo();
//			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
//			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S291, hosClientConfig.getSenderCode()));
//			reqVo.setReqBody(body);
//			
//			JSONObject packageJs = new JSONObject();
//			packageJs.put("body", reqVo.getReqBody());
//			packageJs.put("head", reqVo.getReqHead());
//			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
//			PingAnRespVo resp = client.call(hosCode, packageJs.toString());
//			
//			PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
//			vo.parseResp(PingAnBussId.S291,reqVo,resp);
//			
//			ElasticIndex index = ElasticIndex.pingan;
//			entityUtils.save2EsDB(index, vo);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("执行S291异常：medicalNum="+medicalNum,e);
//		}
//	}

	/**
	 * 门诊即时结算
	 */
	@Override
	public PingAnReqAndRespVo doS299(String inputXml) {
		try {
			JSONArray body = new JSONArray();
			JSONObject json = new JSONObject();
			Document doc = DocumentHelper.parseText(inputXml);
			Element root = doc.getRootElement();
			Element data = root.element(KstHosConstant.DATA);
			
			String medicalNum = XMLUtil.getString(data, "medicalNum", true);
			
			ElasticIndex index = ElasticIndex.pingan;
			Map<String, Object> _search = ElasticBus.create().setSize(20).createSimpleQuery()
			.createElasticQuerySimpleBool().addMustTerm("callType", "C100")
			.addMustTerm("medicalNum", medicalNum)
			.getSearch();
			String ips = null == eSConfig? "127.0.0.1:9200":eSConfig.getUrl();
			ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
			.set_search(_search);
			ElasticRestResponse resp = sender.simpleQuery(index.name());
			ElasticQueryResponseJsonVo vo = resp.setParseToJson();
			List<JSONObject> list = vo.get_sources();
			String caseNum = "";
			if(list!=null && list.size()>0) {
				caseNum = list.get(0).getString("caseNum");
			}
			json.put("caseNum", caseNum);
			json.put("medicalNum", medicalNum);
			json.put("billNum", XMLUtil.getString(data, "receiptNum", true));
			json.put("medicalType", XMLUtil.getString(data, "medicalType", false));
			json.put("treatDate", DateOper.formatDate(XMLUtil.getString(data, "clinicDate", false), "yyyy-MM-dd","yyyyMMddHHmmss"));
			json.put("diagnosisName", XMLUtil.getString(data, "diagnosisName", false));
			json.put("endemicArea", XMLUtil.getString(data, "endemicArea", false));
			json.put("deptNum", XMLUtil.getString(data, "departmentId", false));
			json.put("deptName", XMLUtil.getString(data, "departmentName", false));
			json.put("doctorCode", XMLUtil.getString(data, "doctorId", false));
			json.put("doctorName", XMLUtil.getString(data, "doctorName", false));
			json.put("credentialType", pingAnUtil.parseHosCertTypeToPa(XMLUtil.getString(data, "certType", false)));
			json.put("credentialNum", XMLUtil.getString(data, "certNum", false));
			json.put("name", XMLUtil.getString(data, "name", false));
			json.put("gender", XMLUtil.getString(data, "gender", false));
			json.put("birthday", XMLUtil.getString(data, "birthday", false));
			json.put("idDueDay", XMLUtil.getString(data, "idDueDay", false));
			json.put("race", pingAnUtil.parseHosRaceToPa(XMLUtil.getString(data, "race", false)));
			json.put("homeAddress", XMLUtil.getString(data, "homeAddress", false));
			json.put("companyName", XMLUtil.getString(data, "companyName", false));
			json.put("sumMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(data, "fee", false)));
			json.put("updateBy", XMLUtil.getString(data, "updateBy", false));
			
			JSONArray comArray = new JSONArray();
			Iterator<?> it = data.elementIterator("Data_1");
			while(it.hasNext()) {
				Element el = (Element) it.next();
				JSONObject row = new JSONObject();
				row.put("selfCareAmount", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "selfCareAmount", false)));
				row.put("selfAmount", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "selfAmount", false)));
				row.put("inInsureMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "inInsureMoney", false)));
				row.put("medicareFundCost", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "medicareFundCost", false)));
				row.put("medicarePayLine", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "medicarePayLine", false)));
				row.put("perBearMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "perBearMoney", false)));
				row.put("hosBearMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "hosBearMoney", false)));
				row.put("priorBurdenMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "priorBurdenMoney", false)));
				row.put("sectionCoordinatePayMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "sectionCoordinatePayMoney", false)));
				row.put("overCappingPayMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "overCappingPayMoney", false)));
				row.put("fundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "fundMoney", false)));
				row.put("civilServantFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "civilServantFundMoney", false)));
				row.put("seriousFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "seriousFundMoney", false)));
				row.put("accountFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "accountFundMoney", false)));
				row.put("civilSubsidy", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "civilSubsidy", false)));
				row.put("otherFundMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "otherFundMoney", false)));
				row.put("cashMoney", pingAnUtil.parseFenToYuan(XMLUtil.getString(el, "cashMoney", false)));
				comArray.add(row);
			}
			json.put("composite", comArray);
			json.put("isPrintPolicy", XMLUtil.getString(data, "isPrintPolicy", false));
			json.put("invoiceNO", XMLUtil.getString(data, "invoiceNum", false));
			json.put("settleDate", XMLUtil.getString(data, "settleDate", false));
			json.put("directServiceMark", XMLUtil.getString(data, "directServiceMark", false));
			json.put("linkmanName", XMLUtil.getString(data, "linkmanName", false));
			json.put("linkmanMobile", XMLUtil.getString(data, "linkmanMobile", false));
			json.put("email", XMLUtil.getString(data, "email", false));
			json.put("guardianIdType", pingAnUtil.parseHosCertTypeToPa(XMLUtil.getString(data, "guardianIdType", false)));
			json.put("guardianIdNo", XMLUtil.getString(data, "guardianIdNo", false));
			json.put("guardianName", XMLUtil.getString(data, "guardianName", false));
			json.put("guardianGender", XMLUtil.getString(data, "guardianGender", false));
			json.put("guardianBirthday", DateOper.formatDate(XMLUtil.getString(data, "guardianBirthday", false), "yyyy-MM-dd", "yyyyMMdd"));
			json.put("guardianIDDueDay", DateOper.formatDate(XMLUtil.getString(data, "guardianIDDueDay", false), "yyyy-MM-dd", "yyyyMMdd"));
			JSONArray diaArray = new JSONArray();
			it = data.elementIterator("Data_2");
			while(it.hasNext()) {
				Element el = (Element) it.next();
				JSONObject row = new JSONObject();
				row.put("diagnosisCode", XMLUtil.getString(el, "diagnosisCode", false));
				row.put("diagnosisName", XMLUtil.getString(el, "diagnosisName", false));
				row.put("clinicalDiagnosis", XMLUtil.getString(el, "clinicalDiagnosis", false));
				row.put("diagnosisType", pingAnUtil.parseHosDiagnosisTypeToPa(XMLUtil.getString(el, "diagnosisType", false)));
				row.put("diagSort", XMLUtil.getString(el, "diagSort", false));
				diaArray.add(row);
			}
			json.put("additionalDiagnosisList", diaArray);
			
			
			JSONArray recArray = new JSONArray();
			it = data.elementIterator("Data_3");
			while(it.hasNext()) {
				Element el = (Element) it.next();
				JSONObject row = new JSONObject();
				row.put("listCat", XMLUtil.getString(el, "listCat", false));
				row.put("medicalItemCat", XMLUtil.getString(el, "medicalItemCat", false));
				row.put("recipeNum", XMLUtil.getString(el, "recipeNum", false));
				row.put("recipeSerialNum", XMLUtil.getString(el, "recipeSerialNum", false));
				row.put("recipeDate", pingAnUtil.formatDate(XMLUtil.getString(el, "recipeDate", false), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
				row.put("hospitalChargeCode", XMLUtil.getString(el, "hospitalChargeCode", false));
				row.put("hospitalChargeName", XMLUtil.getString(el, "hospitalChargeName", false));
				row.put("centreChargeCode", XMLUtil.getString(el, "centreChargeCode", false));
				row.put("medicareFeeitemName", XMLUtil.getString(el, "medicareFeeitemName", false));
				row.put("price", XMLUtil.getString(el, "price", false));
				row.put("quantity", XMLUtil.getString(el, "quantity", false));
				row.put("money", XMLUtil.getString(el, "money", false));
				row.put("hosBearMoney", XMLUtil.getString(el, "hosBearMoney", false));
				row.put("formulation", XMLUtil.getString(el, "formulation", false));
				row.put("spec", XMLUtil.getString(el, "spec", false));
				row.put("herbFuFangSign", XMLUtil.getString(el, "herbFuFangSign", false));
				row.put("totalSelfFundFlg", XMLUtil.getString(el, "totalSelfFundFlg", false));
				row.put("extraRecipeFlg", XMLUtil.getString(el, "extraRecipeFlg", false));
				row.put("usage", XMLUtil.getString(el, "usage", false));
				row.put("perQuantity", XMLUtil.getString(el, "perQuantity", false));
				row.put("frequency", XMLUtil.getString(el, "frequency", false));
				row.put("days", XMLUtil.getString(el, "days", false));
				row.put("packetNum", XMLUtil.getString(el, "packetNum", false));
				row.put("isRestricted", XMLUtil.getString(el, "isRestricted", false));
				row.put("restrictions", XMLUtil.getString(el, "restrictions", false));
				row.put("isBasicMedicine", XMLUtil.getString(el, "isBasicMedicine", false));
				row.put("deptNum", XMLUtil.getString(el, "deptNum", false));
				row.put("deptName", XMLUtil.getString(el, "deptName", false));
				row.put("doctorCode", XMLUtil.getString(el, "doctorCode", false));
				row.put("doctorName", XMLUtil.getString(el, "doctorName", false));
				row.put("selfPayRatio", XMLUtil.getString(el, "selfPayRatio", false));
				row.put("medlimitedPrice", XMLUtil.getString(el, "medlimitedPrice", false));
				recArray.add(row);
			}
			json.put("recipeList", recArray);
			
			body.add(json);
			
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S299, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			reqVo.setReqBody(body);
			
			JSONObject packageJs = new JSONObject();
			packageJs.put("body", reqVo.getReqBody());
			packageJs.put("head", reqVo.getReqHead());
			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
			PingAnRespVo respVo = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
			
			PingAnReqAndRespVo reqAndRespVo = new PingAnReqAndRespVo();
			reqAndRespVo.parseResp(PingAnBussId.S299,reqVo,respVo);
			
			index = ElasticIndex.pingan;
			entityUtils.save2EsDB(index, reqAndRespVo);
			return reqAndRespVo;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S299异常：inputXml="+inputXml,e);
			return null;
		}
//		try {
//			String realMedicalNum = null;
//			PatientEntity patient = null;
//			if(medicalNum.startsWith(C_PREFIX)) {
//				//门诊
//				realMedicalNum = medicalNum.replace(C_PREFIX, "");
//				patient = getPatientByMedicalNum(realMedicalNum);
//				
//			}else if(medicalNum.startsWith(H_PREFIX)) {
//				//住院
//				realMedicalNum = medicalNum.replace(H_PREFIX, "");
//				patient = getPatientByMedicalNum(realMedicalNum);
//				
//			}
//			
//			PingAnReqVo reqVo = new PingAnReqVo();
//			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
//			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S299, hosClientConfig.getSenderCode()));
//			reqVo.setReqBody(body);
//			
//			JSONObject packageJs = new JSONObject();
//			packageJs.put("body", reqVo.getReqBody());
//			packageJs.put("head", reqVo.getReqHead());
//			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
//			PingAnRespVo resp = client.call(hosCode, packageJs.toString());
//			
//			PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
//			vo.parseResp(PingAnBussId.S299,reqVo,resp);
//			
//			ElasticIndex index = ElasticIndex.pingan;
//			entityUtils.save2EsDB(index, vo);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("执行S299异常：caseNum="+caseNum+"||medicalNum="+medicalNum,e);
//		}
	}

	@Override
	public PingAnReqAndRespVo doS260(String inputXml) {
		try {
			JSONArray body = new JSONArray();
			JSONObject json = new JSONObject();
			Document doc = DocumentHelper.parseText(inputXml);
			Element root = doc.getRootElement();
			json.put("medicalNum", XMLUtil.getString(root, "medicalNum", false));
			json.put("cancelledTradeNum", XMLUtil.getString(root, "cancelledTradeNum", false));
			json.put("recipeSerialNum", XMLUtil.getString(root, "recipeSerialNum", false));
			json.put("recipeNum", XMLUtil.getString(root, "recipeNum", false));
			json.put("updateDate", XMLUtil.getString(root, "updateDate", false));
			json.put("updateBy", XMLUtil.getString(root, "updateBy", false));
			json.put("recipeBeginDate", XMLUtil.getString(root, "recipeBeginDate", false));
			json.put("recipeEndDate", XMLUtil.getString(root, "recipeEndDate", false));
			body.add(json);
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S260, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			reqVo.setReqBody(body);
			
			JSONObject packageJs = new JSONObject();
			packageJs.put("body", reqVo.getReqBody());
			packageJs.put("head", reqVo.getReqHead());
			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
			PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
			
			PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
			vo.parseResp(PingAnBussId.S260,reqVo,resp);
			
			ElasticIndex index = ElasticIndex.pingan;
			entityUtils.save2EsDB(index, vo);
			return vo;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S260异常：inputXml="+inputXml,e);
			return null;
		}
		
	}

	@Override
	public PingAnReqAndRespVo doS270(String inputXml) {
		try {
			JSONArray body = new JSONArray();
			JSONObject json = new JSONObject();
			Document doc = DocumentHelper.parseText(inputXml);
			Element root = doc.getRootElement();
			json.put("settleSerialNum", XMLUtil.getString(root, "settleSerialNum", false));
			json.put("revokeDate", XMLUtil.getString(root, "revokeDate", false));
			json.put("updateBy", XMLUtil.getString(root, "updateBy", false));
			json.put("isRetainedFlg", XMLUtil.getString(root, "isRetainedFlg", false));
			
			PingAnReqVo reqVo = new PingAnReqVo();
			reqVo.setReqAdditionInfo(client.getAdditionInfoParam(0, "成功"));
			reqVo.setReqHead(client.getHeadParam(PingAnBussId.S270, hosClientConfig.getSenderCode(),body.size(),hosClientConfig.getIntermediaryCode(),hosClientConfig.getIntermediaryName()));
			reqVo.setReqBody(body);
			
			JSONObject packageJs = new JSONObject();
			packageJs.put("body", reqVo.getReqBody());
			packageJs.put("head", reqVo.getReqHead());
			packageJs.put("additionInfo", reqVo.getReqAdditionInfo());
			PingAnRespVo resp = client.call(hosClientConfig.getSenderCode(), packageJs.toString());
			
			PingAnReqAndRespVo vo = new PingAnReqAndRespVo();
			vo.parseResp(PingAnBussId.S270,reqVo,resp);
			
			ElasticIndex index = ElasticIndex.pingan;
			entityUtils.save2EsDB(index, vo);
			
			return vo;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("执行S270异常：inputXml="+inputXml,e);
			return null;
		}
	}
	
	
	
}
