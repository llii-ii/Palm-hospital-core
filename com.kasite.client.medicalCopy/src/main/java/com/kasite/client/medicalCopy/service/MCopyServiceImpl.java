package com.kasite.client.medicalCopy.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kasite.client.medicalCopy.api.FengQiaoApi;
import com.kasite.client.medicalCopy.api.OCRIDCardApi;
import com.kasite.client.medicalCopy.dao.ConsigneeAddressMapper;
import com.kasite.client.medicalCopy.dao.CopyContentMapper;
import com.kasite.client.medicalCopy.dao.CopyPurposeMapper;
import com.kasite.client.medicalCopy.dao.CopyQuestionMapper;
import com.kasite.client.medicalCopy.dao.ExpressOrderMapper;
import com.kasite.client.medicalCopy.dao.MCopyUserMapper;
import com.kasite.client.medicalCopy.dao.McopyCaseMapper;
import com.kasite.client.medicalCopy.dao.OrderCaseMapper;
import com.kasite.client.medicalCopy.dao.PriceManageMapper;
import com.kasite.client.medicalCopy.dao.SettingMapper;
import com.kasite.client.medicalCopy.dao.TransactionRecordMapper;
import com.kasite.client.medicalCopy.weixin.Sign;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.ExportExcel;
import com.kasite.core.common.util.FileUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.util.wechat.HttpClientService;
import com.kasite.core.common.util.wechat.Ticket;
import com.kasite.core.common.util.wechat.constants.WeiXinConstant;
import com.kasite.core.serviceinterface.module.basic.BasicCommonService;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.IPorvingCodeService;
import com.kasite.core.serviceinterface.module.basic.dbo.Hospital;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckPorvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.ICallHisMedicalCopy;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOperationInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryPatientInfoByNo;
import com.kasite.core.serviceinterface.module.medicalCopy.MCopyService;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.ConsigneeAddress;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.ExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.MCopyUser;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.McopyCase;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.OrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.PriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.Setting;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.TransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.dbo.CopyPurpose;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.CopyCaseVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.ExportExpressOrderVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.ExportTransactionRecordVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.ExpressOrderVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.FengQiaoLogisticsVo;
import com.kasite.core.serviceinterface.module.medicalCopy.dto.FengQiaoOrderVo;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqConsigneeAddress;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyPurpose;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqMCopyUser;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqOrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqPriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqQueryPatientInfoByNos;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqCopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqSetting;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqTransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.req.ReqVerification;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespConsigneeAddress;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyContent;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyPurpose;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespExpressOrder;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespMCopyUser;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespOrderCase;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespPriceManage;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespQueryPatientInfoByNos;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespCopyQuestion;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespSetting;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespTransactionRecord;
import com.kasite.core.serviceinterface.module.medicalCopy.resp.RespVerification;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.wechat.IMcopyWechatService;
import com.kasite.core.serviceinterface.module.wechat.req.ReqMcopyWechat;
import com.yihu.hos.util.JSONUtil;
import com.yihu.wsgw.api.InterfaceMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("medicalCopy.MedicalCopyApi")
public class MCopyServiceImpl implements MCopyService{
	
	//final static String DEPLOY_URL = "/usr/local/Client/apache-tomcat-8.5.32-BAFY/conf/uploadFile/mcopyIdcards/";
	public final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_MCOPY);
	@Autowired
	private ExpressOrderMapper expressOrderMapper;
	
	@Autowired
	private MCopyUserMapper mCopyUserMapper;
	
	@Autowired
	private OrderCaseMapper orderCaseMapper;
	
	@Autowired
	private McopyCaseMapper mcopyCaseMapper;
	
	@Autowired
	private PriceManageMapper priceManageMapper;
	
	@Autowired
	private TransactionRecordMapper transactionRecordMapper;
	
	@Autowired
	private IPorvingCodeService porvingCodeService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IMcopyWechatService mcopyWechatService;
	
	@Autowired
	private OCRIDCardApi ocridCardApi;
	
	@Autowired
	private FengQiaoApi fengQiaoApi;
	
	@Autowired
	private ConsigneeAddressMapper consigneeAddressMapper;
	
	@Autowired
	private CopyContentMapper copyContentMapper;
	
	@Autowired
	private CopyPurposeMapper copyPurposeMapper;
	
	@Autowired
	private SettingMapper settingMapper;
	
	@Autowired
	private CopyQuestionMapper copyQuestionMapper;
	
	@Autowired
	private BasicCommonService basicCommonService;
	/**
	 * 通过病案号获得病人信息(his)
	 * */
	@Override
	public String QueryPatientInfoByNos(InterfaceMessage msg) throws Exception {
		return queryPatientInfoByNos(new CommonReq<ReqQueryPatientInfoByNos>(new ReqQueryPatientInfoByNos(msg))).toResult();
	}
	@Override
	public CommonResp<RespQueryPatientInfoByNos> queryPatientInfoByNos(CommonReq<ReqQueryPatientInfoByNos> req) throws Exception {
		String mcopyUserId = "";
		ReqQueryPatientInfoByNos reqVo = req.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("CardNo", reqVo.getCardNo());
		paramMap.put("CardType", reqVo.getCardType());
		paramMap.put("OrgCode", reqVo.getOrgCode());
		
		//his获取病人信息
		List<HisQueryPatientInfoByNo> list = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(),ICallHisMedicalCopy.class).queryPatientInfoByNos(reqVo.getMsg(), paramMap).getListCaseRetCode();

		//查询不到病人信息
		if(list == null || list.isEmpty()) {
			return new CommonResp<RespQueryPatientInfoByNos>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			List<RespQueryPatientInfoByNos> respList = new ArrayList<RespQueryPatientInfoByNos>();
			for (HisQueryPatientInfoByNo item : list) {
				if (!reqVo.getName().equals(item.getName())) {
					mcopyUserId = "noUser";
				}else {
					//保存病人信息
					MCopyUser mCopyUser = new MCopyUser();
					mCopyUser.setPatientId(item.getPatientId());
					mCopyUser.setName(item.getName());
					List<MCopyUser> mCopyUserList = mCopyUserMapper.select(mCopyUser);
					
					mCopyUser.setCardNo(item.getCardNo());
					mCopyUser.setGender(item.getGender());
					mCopyUser.setIdType(item.getIdType());
					mCopyUser.setIdCode(item.getIdCode());
					mCopyUser.setBirthday(item.getBirthday());
					mCopyUser.setChargeType(item.getChargeType());
					mCopyUser.setIdentity(item.getIdentity());
					mCopyUser.setIdCard(item.getOutPara2());
					mCopyUser.setPhone(item.getPhone());
					mCopyUser.setBalance(item.getBalance());
					mCopyUser.setOutPara1(item.getOutPara1());
					mCopyUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					if (mCopyUserList == null || mCopyUserList.isEmpty()) {
						mcopyUserId = item.getPatientId();
						mCopyUser.setId(mcopyUserId);
						mCopyUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
						mCopyUser.setOperatorId("addMCopyUser");
						mCopyUser.setOperatorName("新增病历复印用户");
						mCopyUserMapper.insert(mCopyUser);					
					}else {
						mcopyUserId = mCopyUserList.get(0).getId();
						mCopyUser.setId(mcopyUserId);
						mCopyUserMapper.updateByPrimaryKeySelective(mCopyUser);
					}
				}
				//将病人id和订单id发回前端
				RespQueryPatientInfoByNos resp = new RespQueryPatientInfoByNos();
				resp.setMcopyUserId(mcopyUserId);
				respList.add(resp);
			}
			return new CommonResp<RespQueryPatientInfoByNos>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}
	}

	/**
	 * 通过病案号获得病人病历(his)
	 * */
	@Override
	public String QueryMedicalRecords(InterfaceMessage msg) throws Exception {
		return queryMedicalRecords(new CommonReq<ReqQueryMedicalRecords>(new ReqQueryMedicalRecords(msg))).toResult();
	}
	@Override
	public CommonResp<RespQueryMedicalRecords> queryMedicalRecords(CommonReq<ReqQueryMedicalRecords> req)
			throws Exception {
		
		ReqQueryMedicalRecords reqVo = req.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("PatientId", reqVo.getPatientId());
		paramMap.put("OrgCode", reqVo.getOrgCode());

		List<HisQueryMedicalRecords> list = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(),ICallHisMedicalCopy.class).queryMedicalRecords(reqVo.getMsg(), paramMap).getListCaseRetCode();

		if(list == null || list.isEmpty()) {
			return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			List<RespQueryMedicalRecords> respList = new ArrayList<RespQueryMedicalRecords>();
			for (HisQueryMedicalRecords item : list) {
				if(!"门诊".equals(item.getDeptName()) && !"急诊".equals(item.getDeptName())) {
					int outHosDate = DateOper.diffDay(DateOper.parse2Timestamp(item.getOutHosDate()).toString(),new Timestamp(System.currentTimeMillis()).toString(),DateOper.format19chars);
					if(outHosDate >=14) {
						//将获得的病历添加到数据库
						McopyCase mcopyCase = new McopyCase();
					
						//返回
						RespQueryMedicalRecords resp = new RespQueryMedicalRecords();
						resp.setHospitalId(item.getHospitalId());
						
						List<ExpressOrder> expressOrders = expressOrderMapper.selectExpressOrderByCaseId(item.getHospitalId());
						if (expressOrders == null || expressOrders.isEmpty()) {
							resp.setExistOrder(false);
							resp.setApplyTime("");
						}else {
							resp.setExistOrder(true);
							resp.setApplyTime(expressOrders.get(0).getApplyTime());
						}
						resp.setErdat(item.getErdat());
						resp.setFalar(item.getFalar());
						resp.setInHosDate(item.getInHosDate());
						resp.setDeptCode(item.getDeptCode());
						resp.setDeptName(item.getDeptName());
						resp.setErtim(item.getErtim());
						resp.setOutDeptCode(item.getOutDeptCode());
						if (StringUtil.isBlank(item.getOutDeptName())) {
							resp.setOutDeptName("暂无");	
						}else {
							resp.setOutDeptName(item.getOutDeptName());	
						}
						resp.setIsoperation("1");
						mcopyCase.setIsoperation("1");
						paramMap.put("HospitalId", item.getHospitalId());
						List<HisQueryOperationInfo> operlist = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(),ICallHisMedicalCopy.class).queryOperationInfo(reqVo.getMsg(), paramMap).getListCaseRetCode();	
						
						if (operlist != null && !operlist.isEmpty()) {
							String operName = "";
							String operIds = "";
							for (HisQueryOperationInfo operitem : operlist) {
								if("外科".equals(operitem.getIsOutOperation())) {
									resp.setIsoperation("0");
									mcopyCase.setIsoperation("0");
								}
								operName += operitem.getOperationName() +",";
								operIds += operitem.getOperatorId() +",";
							}
							resp.setOperationName(operName.substring(0, operName.length()-1));
							mcopyCase.setOperationName(operName.substring(0, operName.length()-1));
							mcopyCase.setOperationIds(operIds.substring(0, operIds.length()-1));
						}	
						
						if (StringUtil.isNotBlank(item.getInHosDate()) && StringUtil.isNotBlank(item.getOutHosDate())) {
							int diffDay = DateOper.diffDay(item.getInHosDate(),item.getOutHosDate(),DateOper.LONG_DATE_TIME_FORMAT_STRING);
							resp.setHospitalization(Integer.toString(diffDay));
						}else {
							resp.setHospitalization("暂无");
						}
						if(StringUtil.isNotBlank(item.getOutHosDate())) {
							resp.setOutHosDate(DateOper.formatDate(item.getOutHosDate(), DateOper.LONG_DATE_TIME_FORMAT_STRING, DateOper.format16chars));
						}else {
							resp.setOutHosDate("暂无");
						}
						respList.add(resp);
						if(mcopyCaseMapper.selectByPrimaryKey(item.getHospitalId()) == null) {
							mcopyCase.setId(item.getHospitalId()); 
							mcopyCase.setErdat(item.getErdat());
							mcopyCase.setFalar(item.getFalar());
							mcopyCase.setInHosDate(item.getInHosDate());
							mcopyCase.setDeptCode(item.getDeptCode());
							mcopyCase.setDeptName(item.getDeptName());
							mcopyCase.setErtim(item.getErtim());
							mcopyCase.setOutHosDate(item.getOutHosDate());
							mcopyCase.setOutDeptCode(item.getOutDeptCode());
							mcopyCase.setOutDeptName(item.getOutDeptName());	
							mcopyCase.setCreateTime(new Timestamp(System.currentTimeMillis()));
							mcopyCase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
							mcopyCase.setOperatorId("addMcopyCase");
							mcopyCase.setOperatorName("新增病历");
							mcopyCaseMapper.insert(mcopyCase);				
						}
					}
				}	
			}
			return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,reqVo.getPage());
		}
	}
	
	/**
	 * 通过id获得病人信息(kst)
	 * */
	@Override
	public String QueryPatientInfoById(InterfaceMessage msg) throws Exception {
		return queryPatientInfoById(new CommonReq<ReqMCopyUser>(new ReqMCopyUser(msg))).toResult();
	}
	@Override
	public CommonResp<RespMCopyUser> queryPatientInfoById(CommonReq<ReqMCopyUser> req) throws Exception {
		ReqMCopyUser reqVo = req.getParam();
		String id = reqVo.getId();
		MCopyUser mCopyUser = mCopyUserMapper.selectByPrimaryKey(id);
		
		List<RespMCopyUser> respList = new ArrayList<RespMCopyUser>();
		if (mCopyUser == null) {
			return new CommonResp<RespMCopyUser>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			RespMCopyUser resp = new RespMCopyUser();
			resp.setId(id);
			resp.setCardNo(mCopyUser.getCardNo());
			resp.setName(mCopyUser.getName());
			resp.setGender(mCopyUser.getGender());
			resp.setIdType(mCopyUser.getIdType());
			resp.setIdCode(mCopyUser.getIdCode());
			resp.setBirthday(mCopyUser.getBirthday());
			resp.setPatientId(mCopyUser.getPatientId());
			resp.setChargeType(mCopyUser.getChargeType());
			if (StringUtil.isNotBlank(mCopyUser.getPhone())) {
				resp.setMobileFormat(StringUtil.mobileFormat(mCopyUser.getPhone()));
				resp.setPhone(mCopyUser.getPhone());
			}else {
				resp.setMobileFormat("暂无");
				resp.setPhone("暂无");
			}
			if (StringUtil.isNotBlank(mCopyUser.getIdCard())) {
				resp.setIdCardFormat(StringUtil.idCardFormat(mCopyUser.getIdCard()));
				resp.setIdCard(mCopyUser.getIdCard());
			}else {
				resp.setIdCardFormat("暂无");
				resp.setIdCard("暂无");
			}	
			resp.setBalance(mCopyUser.getBalance());
			resp.setOutPara1(mCopyUser.getOutPara1());
			respList.add(resp);
		}
		return new CommonResp<RespMCopyUser>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	
	/**
	 * 通过病历id获得病人病历信息(kst)
	 * */
	@Override
	public String GetMedicalRecordsById(InterfaceMessage msg) throws Exception {
		return getMedicalRecordsById(new CommonReq<ReqQueryMedicalRecords>(new ReqQueryMedicalRecords(msg))).toResult();
	}
	@Override
	public CommonResp<RespQueryMedicalRecords> getMedicalRecordsById(CommonReq<ReqQueryMedicalRecords> req)
			throws Exception {
		ReqQueryMedicalRecords reqVo = req.getParam();
		
		if(StringUtil.isBlank(reqVo.getCaseId())) {
		 	return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		
		String[] caseIds = reqVo.getCaseId().split(",");
		List<RespQueryMedicalRecords> respList = new ArrayList<RespQueryMedicalRecords>();
		for (int i = 0; i < caseIds.length; i++) {
			McopyCase item = mcopyCaseMapper.selectByPrimaryKey(caseIds[i]);
			RespQueryMedicalRecords resp = new RespQueryMedicalRecords();
			resp.setHospitalId(item.getId());
			resp.setErdat(item.getErdat());
			resp.setFalar(item.getFalar());
			resp.setDeptCode(item.getDeptCode());
			resp.setDeptName(item.getDeptName());
			resp.setErtim(item.getErtim());
			resp.setOutDeptCode(item.getOutDeptCode());
			if(StringUtil.isNotBlank(item.getOutDeptName())) {
				resp.setOutDeptName(item.getOutDeptName());	
			}else {
				resp.setOutDeptName("暂无");
			}
			resp.setIsoperation(item.getIsoperation());
			resp.setOperationName(item.getOperationName());
			if (StringUtil.isNotBlank(item.getInHosDate()) && StringUtil.isNotBlank(item.getOutHosDate())) {
				int diffDay = DateOper.diffDay(item.getInHosDate(),item.getOutHosDate(),DateOper.format19chars);
				resp.setHospitalization(Integer.toString(diffDay));
			}else {
				resp.setHospitalization("暂无");
			}
			if(StringUtil.isNotBlank(item.getInHosDate())) {
				resp.setInHosDate(DateOper.formatDate(item.getInHosDate(), DateOper.format19chars, DateOper.format16chars));
			}else {
				resp.setInHosDate("暂无");
			}
			if(StringUtil.isNotBlank(item.getOutHosDate())) {
				resp.setOutHosDate(DateOper.formatDate(item.getOutHosDate(), DateOper.format19chars, DateOper.format16chars));
			}else {
				resp.setOutHosDate("暂无");
			}
			respList.add(resp);
		}
		return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	
	/**
	 * 修改快递单(kst)
	 * */
	@Override
	public String UpdateOrder(InterfaceMessage msg) throws Exception {
		return updateOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespExpressOrder> updateOrder(CommonReq<ReqExpressOrder> req) throws Exception {
		ReqExpressOrder upReq = req.getParam();
		if(upReq==null || StringUtil.isBlank(upReq.getId())) {
			return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		
		if ("2".equals(upReq.getOrderState())) {
			ExpressOrder param = expressOrderMapper.selectByPrimaryKey(upReq.getId());
			String[] caseIdList = param.getCaseIdAll().split(",");
			String[] caseMoneyList = param.getCaseMoneyAll().split(",");
			String[] caseNumList = param.getCaseNumberAll().split(",");
			for (int i = 0; i < caseIdList.length; i++) {	
				Map<String, String> paramMap = new HashMap<String, String>(16);
				paramMap.put("OrderIds", param.getWxOrderId()+"_"+caseIdList[i]);
				paramMap.put("OrgCode", "AH01");
				paramMap.put("Num", caseNumList[i]);
				double cash = Integer.parseInt(caseNumList[i])*Double.parseDouble(caseMoneyList[i]);
				paramMap.put("Cash",Double.toString(cash));
				paramMap.put("HospitalId", caseIdList[i]);
				paramMap.put("OrderTime", DateOper.formatDate(param.getApplyTime(),DateOper.format19chars,DateOper.format14chars));
				//DateOper.formatDate(param.getApplyTime(),DateOper.format23chars,DateOper.format14chars);
				McopyCase mcopyCase = mcopyCaseMapper.selectByPrimaryKey(caseIdList[i]);
				if(mcopyCase == null) {
					paramMap.put("OperatorId", null);
				}else {
					paramMap.put("OperatorId", mcopyCase.getOperationIds());
				}
				String map = HandlerBuilder.get().getCallHisService(upReq.getAuthInfo(),ICallHisMedicalCopy.class).backInfoToHis(upReq.getMsg(), paramMap).getMessage();	
				JSONObject jsonObject = JSONObject.fromObject(map);
				if ("0".equals(jsonObject.get("rspcode"))) {
				}else {
					return new CommonResp<RespExpressOrder>(req, RetCode.Common.ERROR_CALLHIS, "病例复印请求信息回写HIS有误，订单号："+param.getWxOrderId());
				}
			}
		}
		
		ExpressOrder expressOrder = new ExpressOrder();
		expressOrder.setId(upReq.getId());
		expressOrder.setOrderState(upReq.getOrderState());
		//expressOrder.setPayState(upReq.getPayState());
		expressOrder.setWxOrderId(upReq.getWxOrderId());
		expressOrder.setCourierNumber(upReq.getCourierNumber());
		expressOrder.setCourierCompanyCode(upReq.getCourierCompanyCode());
		expressOrder.setCourierCompany(upReq.getCourierCompany());
		expressOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		int update = expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
		if(update > 0) {
			return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
	}

	
	/**
	 * 新增快递单(kst)
	 * */
	@Override
	public String AddExpressOrder(InterfaceMessage msg) throws Exception {
		return addExpressOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}

	@Override
	public CommonResp<RespExpressOrder> addExpressOrder(CommonReq<ReqExpressOrder> req) throws Exception {

		ReqExpressOrder upReq = req.getParam();
		if(StringUtil.isBlank(upReq.getCaseIdAll())) {
		 	return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		ExpressOrder expressOrder = new ExpressOrder();
		expressOrder.setId(id);
		expressOrder.setTotalMoney(upReq.getTotalMoney());
		expressOrder.setCaseIdAll(upReq.getCaseIdAll());
		expressOrder.setCaseNumberAll(upReq.getCaseNumberAll());
		expressOrder.setCaseMoneyAll(upReq.getCaseMoneyAll());
		expressOrder.setPatientId(upReq.getPatientId());
		expressOrder.setAuthentication(upReq.getAuthentication());
		expressOrder.setAddressee(upReq.getAddressee());
		expressOrder.setAddress(upReq.getAddress());
		expressOrder.setTelephone(upReq.getTelephone());
		expressOrder.setApplyOpenId(upReq.getApplyOpenId());
		expressOrder.setApplyName(upReq.getApplyName());
		expressOrder.setWxOrderId(upReq.getWxOrderId());
		expressOrder.setProvince(upReq.getProvince());
		expressOrder.setIdcardImgName(upReq.getIdcardImgName());
		expressOrder.setOrderState("1");
		//expressOrder.setPayState("0");
		expressOrder.setApplyTime(DateUtils.formatDateToString(new Date(), DateUtils.DATE_TIME_PATTERN));
		expressOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		expressOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		expressOrder.setOperatorId("addExpressOrder");
		expressOrder.setOperatorName("新增病历复印订单");
		expressOrderMapper.insertSelective(expressOrder);
		
		String[] caseIdList = upReq.getCaseIdAll().split(",");
		String[] caseMoneyList = upReq.getCaseMoneyAll().split(",");
		String[] caseNumList = upReq.getCaseNumberAll().split(",");
		for (int i = 0; i < caseIdList.length; i++) {
			Map<String, String> paramMap = new HashMap<String, String>(16);
			paramMap.put("OrderIds", id);
			paramMap.put("Num", caseNumList[i]);
			paramMap.put("OrgCode", upReq.getOrgCode());
			paramMap.put("Cash", caseMoneyList[i]);
			paramMap.put("OrderTime", new Timestamp(System.currentTimeMillis()).toString());
			paramMap.put("HospitalId", caseIdList[i]);
			
			McopyCase mcopyCase = mcopyCaseMapper.selectByPrimaryKey(caseIdList[i]);
			if(mcopyCase == null) {
				paramMap.put("OperatorId", null);
			}else {
				paramMap.put("OperatorId", mcopyCase.getOperationIds());
			}
			
			//String map = HandlerBuilder.get().getCallHisService(upReq.getAuthInfo(),ICallHisMedicalCopy.class).backInfoToHis(upReq.getMsg(), paramMap).getMessage();	
			//JSONObject jsonObject = JSONObject.fromObject(map);
			//if ("0".equals(jsonObject.get("rspcode"))) {
			OrderCase orderCase = new OrderCase();
			orderCase.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			orderCase.setOrderId(id);			
			orderCase.setCaseId(caseIdList[i]);
			orderCase.setCaseNumber(caseNumList[i]);
			orderCase.setMoney(caseMoneyList[i]);
			orderCase.setCreateTime(new Timestamp(System.currentTimeMillis()));
			orderCase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			orderCase.setOperatorId("addOrderCase");
			orderCase.setOperatorName("新增复印订单与病历的关系");
			orderCaseMapper.insertSelective(orderCase);
			//}else {
			//	return new CommonResp<RespExpressOrder>(req, RetCode.Common.ERROR_CALLHIS, "病例复印请求信息回写有误");
			//}
		}

		return new CommonResp<RespExpressOrder>(req, RetCode.Success.RET_10000, id);
	}
	
	/**
	 * 根据id获取快递订单信息
	 * */
	@Override
	public String QueryExpressOrderById(InterfaceMessage msg) throws Exception {
		return queryExpressOrderById(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespExpressOrder> queryExpressOrderById(CommonReq<ReqExpressOrder> req) throws Exception {
		ReqExpressOrder upReq = req.getParam();
		String orderId = upReq.getId();
		if (StringUtil.isBlank(orderId)) {
			new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(orderId);
		RespExpressOrder resp = new RespExpressOrder();
		resp.setCourierNumber(expressOrder.getCourierNumber());
		resp.setCourierCompanyCode(expressOrder.getCourierCompanyCode());
		resp.setTotalMoney(expressOrder.getTotalMoney());
		//resp.setPayState(expressOrder.getPayState());
		
		//获取订单状态
		OrderView view = expressOrderMapper.getPayState(expressOrder.getWxOrderId());
		if(view == null || StringUtil.isBlank(view.getPayState())) {
			resp.setPayState("0");
		}else {
			resp.setPayState(Integer.toString(view.getPayState()));
		}		
		resp.setWxOrderId(expressOrder.getWxOrderId());
		resp.setOrderState(expressOrder.getOrderState());
		resp.setAuthentication(expressOrder.getAuthentication());
		resp.setCaseIdAll(expressOrder.getCaseIdAll());
		resp.setAddress(expressOrder.getAddress());
		resp.setAddressee(expressOrder.getAddressee());
		resp.setProvince(expressOrder.getProvince());
		resp.setTelephone(expressOrder.getTelephone());
		resp.setPreMoney(expressOrder.getPreMoney());
		resp.setApplyRelationType(expressOrder.getApplyRelationType());
		resp.setProveImgs(expressOrder.getProveImgs());
		resp.setState(getState(expressOrder.getOrderState(), expressOrder.getPayState(), expressOrder.getApplyTime(),1));
		//复印病例详情
		MCopyUser mCopyUser = mCopyUserMapper.selectByPrimaryKey(expressOrder.getPatientId());
		if(mCopyUser!=null) {
			resp.setName(mCopyUser.getName());
			resp.setIdCard(mCopyUser.getIdentity());
		}
		OrderCase orderCase = new OrderCase();
		orderCase.setOrderId(expressOrder.getId());
		List<CopyCaseVo> copyCaseVos = new ArrayList<CopyCaseVo>();
		List<OrderCase> orderCases = orderCaseMapper.select(orderCase);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (OrderCase OCase : orderCases) {
			CopyCaseVo copyCaseVo = new CopyCaseVo();
			copyCaseVo.setCaseId(OCase.getCaseId());
			copyCaseVo.setCaseNumber(OCase.getCaseNumber());
			copyCaseVo.setCopyUtility(OCase.getCopyUtility());
			copyCaseVo.setCopyContentNames(OCase.getCopyContentNames());
			McopyCase mcopyCase = mcopyCaseMapper.selectByPrimaryKey(OCase.getCaseId());
			long inHosDay = 0;
			if(StringUtil.isNotBlank(mcopyCase.getOutHosDate())) {
				inHosDay = format.parse(mcopyCase.getOutHosDate()).getTime()-format.parse(mcopyCase.getInHosDate()).getTime();
			}else {
				inHosDay = System.currentTimeMillis()-format.parse(mcopyCase.getInHosDate()).getTime();
			}
			copyCaseVo.setInHosDay(String.valueOf(inHosDay/(24*60*60*1000)));
			copyCaseVo.setOutHosDate(mcopyCase.getOutHosDate());
			copyCaseVos.add(copyCaseVo);
		}
		resp.setOrderCases(copyCaseVos);
		//配送信息
		resp.setReceiveType(expressOrder.getReceiveType());
		if(expressOrder.getReceiveType().equals("1")) {
			ConsigneeAddress consigneeAddress = consigneeAddressMapper.selectByPrimaryKey(expressOrder.getAddressee());
			resp.setAddress(consigneeAddress.getAddress()+","+consigneeAddress.getAddressee()+","+consigneeAddress.getTelephone());
		}
		//付款信息
		PriceManage priceManage = priceManageMapper.selectByPrimaryKey(orderCases.get(0).getPriceManageId());
		resp.setPriceManage(priceManage);
		return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	/**
	 * 后台获得快递单列表(kst)
	 * */
	@Override
	public String GetExpressOrderList(InterfaceMessage msg) throws Exception {
		return getExpressOrderList(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespExpressOrder> getExpressOrderList(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		ReqExpressOrder upReq = commonReq.getParam();	
		List<ExpressOrderVo> orders = expressOrderMapper.selectExpressOrder(upReq);	
		if(orders.isEmpty()) {
			new CommonResp<RespExpressOrder>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
		List<RespExpressOrder> resps = new ArrayList<RespExpressOrder>();
		for (ExpressOrderVo item : orders) {
			RespExpressOrder r = new RespExpressOrder();

			r.setId(item.getId());
			r.setName(item.getName());
			r.setPatientId(item.getPatientId());
			r.setOperationName(item.getOperationName());
			r.setCaseNumber(item.getCaseNumber());
			r.setCaseNumberAll(item.getCaseNumberAll());

			
			if(StringUtil.isBlank(item.getOutDeptName())) {
				r.setOutDeptName("暂无");
			}else {
				if(item.getOutDeptName().indexOf("楼") > 0) {
					int start = item.getOutDeptName().indexOf("科")+1;
					int end = item.getOutDeptName().indexOf("楼")+1;
					r.setOutDeptName(item.getOutDeptName().substring(start, end));
				}else {
					r.setOutDeptName(item.getOutDeptName());
				}
			}
			
			r.setIsoperation(item.getIsoperation());
			
			//获取订单状态
			OrderView view = expressOrderMapper.getPayState(item.getWxOrderId());
			
			if(view == null || StringUtil.isBlank(view.getPayState())) {
				r.setPayState("0");
			}else {
				r.setPayState(Integer.toString(view.getPayState()));
			}
			//r.setPayState(item.getPayState());
			
			r.setOrderState(item.getOrderState());
			r.setApplyTime(item.getApplyTime());
			r.setOutHosDate(item.getOutHosDate());
			r.setMoney(item.getMoney());
			r.setMcId(item.getMcId());
			r.setTotalMoney(item.getTotalMoney());
			r.setCourierNumber(item.getCourierNumber());
			r.setCourierCompany(item.getCourierCompany());
			r.setCourierCompanyCode(item.getCourierCompanyCode());
			r.setTelephone(item.getTelephone());
			r.setProvince(item.getProvince());
			r.setAddress(item.getAddress());
			r.setAddressee(item.getAddressee());
			r.setIdcardImgName(item.getIdcardImgName());
			r.setState(getState(item.getOrderState(), item.getPayState(), item.getApplyTime(),1));
			String[] orderIds = StringUtil.isNotBlank(item.getWxOrderId())?item.getWxOrderId().split(","):null;
			if(orderIds!=null&&orderIds.length>0) {
				Map<String, Object> map = expressOrderMapper.countPriceByOrderIds(orderIds);
				r.setPreMoney(item.getPreMoney());
				Object refundPrice = map.get("REFUNDPRICE");
				Object payPrice = map.get("PAYPRICE");
				r.setRefundMoney(refundPrice.toString());
				r.setTotalMoney(payPrice.toString());				
			}
			int caseNumber = 0;
			for (String string : item.getCaseNumberAll().split(",")) {
				caseNumber+=StringUtil.strToInt(string);
			}
			r.setCaseNumber(caseNumber);
			if (StringUtil.isNotBlank(item.getInHosDate()) && StringUtil.isNotBlank(item.getOutHosDate())) {
				int diffDay = DateOper.diffDay(item.getInHosDate(),item.getOutHosDate(),"yyyy-MM-dd");
				r.setHospitalization(Integer.toString(diffDay));
			}	
			if (StringUtil.isNotBlank(upReq.getPayState())) {
				if (r.getPayState().equals(upReq.getPayState())) {
					resps.add(r);
				}
			}else {
				resps.add(r);
			}
		}
		
		return new CommonResp<RespExpressOrder>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resps);
	}
	
	
	/**
	 * 微信端获得快递单列表(kst)
	 * */
	@Override
	public String GetOrderList(InterfaceMessage msg) throws Exception {
		return getOrderList(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespExpressOrder> getOrderList(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		ReqExpressOrder upReq = commonReq.getParam();
		List<ExpressOrderVo> orders = expressOrderMapper.selectOrderByWX(upReq);
		if (orders == null || orders.isEmpty()) {
			return new CommonResp<RespExpressOrder>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
		List<RespExpressOrder> resps = new ArrayList<RespExpressOrder>();
		for (ExpressOrderVo item : orders) {
			RespExpressOrder r = new RespExpressOrder();
			r.setId(item.getId());
			r.setName(item.getName());
			r.setPatientId(item.getPatientId());
			r.setOperationName(item.getOperationName());
			r.setCaseNumber(item.getCaseNumber());
			r.setOutDeptName(item.getOutDeptName());
			r.setIsoperation(item.getIsoperation());
			
			OrderView view = expressOrderMapper.getPayState(item.getWxOrderId());
			if(view == null || StringUtil.isBlank(view.getPayState())) {
				r.setPayState("0");
			}else {
				r.setPayState(Integer.toString(view.getPayState()));
			}
			
			//r.setPayState(item.getPayState());
			
			r.setOrderState(item.getOrderState());
			r.setOutHosDate(item.getOutHosDate());
			r.setTotalMoney(item.getTotalMoney());
			r.setCourierNumber(item.getCourierNumber());
			r.setCourierCompany(item.getCourierCompany());
			r.setCourierCompanyCode(item.getCourierCompanyCode());
			r.setTelephone(item.getTelephone());
			r.setProvince(item.getProvince());
			r.setAddress(item.getAddress());
			r.setAddressee(item.getAddressee());
			r.setWxOrderId(item.getWxOrderId());
			r.setCaseMoneyAll(item.getCaseMoneyAll());
			r.setCaseNumberAll(item.getCaseNumberAll());
			r.setIdcardImgName(item.getIdcardImgName());
			r.setApplyTime(DateOper.formatDate(item.getApplyTime(), DateOper.format19chars, DateOper.format16chars));
			r.setPreMoney(item.getPreMoney());
			//设置状态
			r.setState(getState(item.getOrderState(),item.getPayState(),item.getApplyTime(),1));
			if (StringUtil.isNotBlank(upReq.getPayState())) {
				if (r.getPayState().equals(upReq.getPayState())) {
					resps.add(r);
				}
			}else {
				resps.add(r);
			}
		}
		return new CommonResp<RespExpressOrder>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resps);
	}
	
	private String getState(String orderState,String payState,String applyTime,int type) throws ParseException {
		String state = "";
		switch (orderState) {
		case "1":
			if(payState.equals("0")) {
				//判断是否超时
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 if(System.currentTimeMillis()-sdf.parse(applyTime).getTime()>10*60*1000) {
					 //超过10分钟为待支付
					 state = type==1?"7":"已失效";
				 }else {
					 state = type==1?"1":"待支付";
				 }
				
			}else {
				state = type==1?"2":"待审核";
			}
			break;
		case "2":
			state = type==1?"3":"待发件";
			break;
		case "3":
			state = type==1?"4":"待收件";
			break;
		case "4":
			state = type==1?"8":"已取消";
			break;
		case "5":
			state = type==1?"5":"审核不通过";
			break;
		case "6":
			
			break;
		case "7":
			state = type==1?"6":"待补缴";
			break;
		case "8":
			state = type==1?"2":"待审核";
			break;
		case "9":
			state = type==1?"9":"已签收";
			break;

		default:
			break;
		}
		return state;
	}
	
	
	/**
	 * 获取费用管理信息(kst)
	 * */
	@Override
	public String GetPriceManageInfo(InterfaceMessage msg) throws Exception {
		return getPriceManageInfo(new CommonReq<ReqPriceManage>(new ReqPriceManage(msg))).toResult();
	}
	@Override
	public CommonResp<RespPriceManage> getPriceManageInfo(CommonReq<ReqPriceManage> commonReq) throws Exception {
		ReqPriceManage upReq = commonReq.getParam();
		List<PriceManage> resp = priceManageMapper.selectPriceManage(upReq);
		List<RespPriceManage> list = new ArrayList<>();
		for (PriceManage item : resp) {
			RespPriceManage respPriceManage = new RespPriceManage();
			respPriceManage.setId(item.getId());
			respPriceManage.setMoney(item.getMoney());
			respPriceManage.setName(item.getName());
			respPriceManage.setPriceType(item.getPriceType());
			respPriceManage.setUpdateTime(item.getUpdateTime());
			list.add(respPriceManage);
		}
		return new CommonResp<RespPriceManage>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list);
	}
	
	/**
	 * 修改费用管理信息(kst)
	 * */
	@Override
	public String UpdatePriceManage(InterfaceMessage msg) throws Exception {
		return updatePriceManage(new CommonReq<ReqPriceManage>(new ReqPriceManage(msg))).toResult();
	}
	@Override
	public CommonResp<RespPriceManage> updatePriceManage(CommonReq<ReqPriceManage> commonReq) throws Exception {
		ReqPriceManage upReq = commonReq.getParam();
		if(upReq==null || StringUtil.isBlank(upReq.getId())) {
			return new CommonResp<RespPriceManage>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		PriceManage priceManage = new PriceManage();
		priceManage.setId(upReq.getId());
		priceManage.setMoney(upReq.getMoney());
		priceManage.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		int update = priceManageMapper.updateByPrimaryKeySelective(priceManage);
		return new CommonResp<RespPriceManage>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	/**
	 * 获得交易记录(kst)
	 * */
	@Override
	public String GetTransactionRecord(InterfaceMessage msg) throws Exception {
		return getTransactionRecord(new CommonReq<ReqTransactionRecord>(new ReqTransactionRecord(msg))).toResult();
	}
	@Override
	public CommonResp<RespTransactionRecord> getTransactionRecord(CommonReq<ReqTransactionRecord> commonReq)
			throws Exception {
		ReqTransactionRecord upReq = commonReq.getParam();
		List<RespTransactionRecord> resp = transactionRecordMapper.selectTransactionRecord(upReq);
		if (resp.isEmpty()) {
			new CommonResp<RespTransactionRecord>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		return new CommonResp<RespTransactionRecord>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,resp);
	}
	
	/**
	 * 身份证和手机验证码验证判断(kst)
	 * */
	@Override
	public String Verification(InterfaceMessage msg) throws Exception {
		return verification(new CommonReq<ReqVerification>(new ReqVerification(msg))).toResult();
	}
	@Override
	public CommonResp<RespVerification> verification(CommonReq<ReqVerification> commonReq) throws Exception {
		ReqVerification req = commonReq.getParam();
		String url1 = KasiteConfig.localConfigPath()+File.separator+req.getPicId()+"1.png";//身份证正面	
		String url2 = KasiteConfig.localConfigPath()+File.separator+req.getPicId()+"2.png";//身份证反面
		//获得身份证图片
		File file = new File(url1);
		try {
			String idCardInfo = ocridCardApi.ocrIDCardAPI(file);
			JSONObject json = JSONObject.fromObject(idCardInfo);
			JSONArray jsonArray = JSONArray.fromObject(json.getString("cards"));
			JSONObject ob = (JSONObject) jsonArray.get(0);
			
			if(StringUtil.isBlank(ob.getString("legality"))) {
				return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
			}else {
				JSONObject legality = JSONObject.fromObject(ob.getString("legality"));
				Double le = legality.getDouble("ID Photo");//识别后是否正式身份证照片的概率值
				if (le < 0.98) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
				}
			}
			//验证身份证号
			if(StringUtil.isBlank(req.getIdCard())) {
				if(StringUtil.isBlank(ob.getString("id_card_number"))) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
				}
			}else {
				if(!req.getIdCard().equals(ob.getString("id_card_number"))) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "身份号验证与患者身份不一致，请重新上传");
				}
			}
			//验证姓名
			if(StringUtil.isBlank(req.getPatientName())) {
				if(StringUtil.isBlank(ob.getString("name"))) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
				}
			}else {
				if(!req.getPatientName().equals(ob.getString("name"))) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "姓名验证与患者身份不一致，请重新上传");
				}
			}
		} catch (Exception e) {
			return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
		}

		// 验证验证码
		ReqCheckPorvingCode checkReq = new ReqCheckPorvingCode(req.getMsg(),req.getPcId(),req.getMobile(),req.getCode());
		CommonReq<ReqCheckPorvingCode> checkReqComm = new CommonReq<ReqCheckPorvingCode>(checkReq);
		CommonResp<RespMap> checkResp = porvingCodeService.checkPorvingCode(checkReqComm);
		if (!KstHosConstant.SUCCESSCODE.equals(checkResp.getCode())) {
			// 验证码错误
			return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "手机验证码错误");
		}
		return new CommonResp<RespVerification>(commonReq, RetCode.Success.RET_10000, "验证成功");
	}
	
	/**
	 * 判断病历是否在30内已经有复印(kst)
	 * */
	@Override
	public String IsCopyBy30Day(InterfaceMessage msg) throws Exception {
		return isCopyBy30Day(new CommonReq<ReqOrderCase>(new ReqOrderCase(msg))).toResult();
	}
	@Override
	public CommonResp<RespOrderCase> isCopyBy30Day(CommonReq<ReqOrderCase> commonReq) throws Exception {
		boolean flag = true;
		ReqOrderCase req = commonReq.getParam();
		String[] caseIdList = req.getCase_id().split(",");
		for (int i = 0; i < caseIdList.length; i++) {
			List<OrderCase> list = orderCaseMapper.isCopyBy30Day(caseIdList[i]);
			int diffDay = DateOper.diffDay(list.get(0).getCreateTime().toString(),new Timestamp(System.currentTimeMillis()).toString(),"yyyy-MM-dd")+1;
			if (diffDay < 30) {
				flag = false;
				break;
			}
		}
		if(flag) {
			return new CommonResp<RespOrderCase>(commonReq, RetCode.Success.RET_10000, "允许复印");
		}else {
			return new CommonResp<RespOrderCase>(commonReq, RetCode.Common.ERROR_PARAM, "非30天内");
		}
		
	}
	
	/**
	 * 退款(kst)
	 * */
	@Override
	public String Refund(InterfaceMessage msg) throws Exception {
		return refund(new CommonReq<ReqOrderIsCancel>(new ReqOrderIsCancel(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> refund(CommonReq<ReqOrderIsCancel> req) throws Exception {
		ReqOrderIsCancel reqOrderIsCancel = req.getParam();

		CommonResp<RespMap> orderIsCancel = orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(reqOrderIsCancel));
		return orderIsCancel;
	}
	
	/**
	 * 新增交易记录(kst)
	 * */
	@Override
	public String AddTransactionRecord(InterfaceMessage msg) throws Exception {
		return addTransactionRecord(new CommonReq<ReqTransactionRecord>(new ReqTransactionRecord(msg))).toResult();
	}
	@Override
	public CommonResp<RespTransactionRecord> addTransactionRecord(CommonReq<ReqTransactionRecord> req) throws Exception {

		ReqTransactionRecord reqVo = req.getParam();
		TransactionRecord transactionRecord = new TransactionRecord();
		transactionRecord.setId(CommonUtil.getGUID());
		transactionRecord.setOrderType("1");
		transactionRecord.setOrderId(reqVo.getOrderId());
		transactionRecord.setPayChannelId(reqVo.getPayChannelId());
		transactionRecord.setPayChannel(reqVo.getPayChannel());
		transactionRecord.setServiceContent(reqVo.getServiceContent());
		transactionRecord.setActualReceipts(reqVo.getActualReceipts());
		transactionRecord.setShouldRefunds(reqVo.getShouldRefunds());
		transactionRecord.setActualRefunds(reqVo.getActualRefunds());
		transactionRecord.setReceiptsType(reqVo.getReceiptsType());
		transactionRecord.setAccountResult(reqVo.getAccountResult());
		transactionRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
		transactionRecord.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		transactionRecord.setOperatorId("addTransactionRecord");
		transactionRecord.setOperatorName("新增交易记录");
		int insert = transactionRecordMapper.insert(transactionRecord);
		if (insert == 1) {
			return new CommonResp<RespTransactionRecord>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespTransactionRecord>(req,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	
	
	/**
	 * 修改交易记录(kst)
	 * */
	@Override
	public String UpdateTransactionRecord(InterfaceMessage msg) throws Exception {
		return updateTransactionRecord(new CommonReq<ReqTransactionRecord>(new ReqTransactionRecord(msg))).toResult();
	}
	@Override
	public CommonResp<RespTransactionRecord> updateTransactionRecord(CommonReq<ReqTransactionRecord> commonReq)
			throws Exception {
		ReqTransactionRecord upReq = commonReq.getParam();
		if(upReq==null || StringUtil.isBlank(upReq.getOrderId())) {
			return new CommonResp<RespTransactionRecord>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		int update = transactionRecordMapper.updateByOrderId(upReq);
		if(update == 1) {
			return new CommonResp<RespTransactionRecord>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<RespTransactionRecord>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}
	
	
	/**
	 * 获得wxconfig配置签名
	 * */
	@Override
	public String GetWxConfigInfo(InterfaceMessage msg) throws Exception{
		return getWxConfigInfo(new CommonReq<ReqMcopyWechat>(new ReqMcopyWechat(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> getWxConfigInfo(CommonReq<ReqMcopyWechat> commReq) throws Exception{
		ReqMcopyWechat upReq = commReq.getParam();
		CommonResp<RespMap> ticket = mcopyWechatService.getWechatTicket(commReq);
		
		if ("10000".equals((String) ticket.toJSONResult().get("RespCode"))) {
			String jsapi_ticket = (String) ticket.toJSONResult().get("RespMessage");
			JSONObject jsonObject = JSONObject.fromObject(Sign.sign(jsapi_ticket,upReq.getConfigKey(),upReq.getUrl()));
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,jsonObject.toString());
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
	}
	
	/**
	 * 微信图文消息推送
	 * */
	@Override
	public String WxSendMsg(InterfaceMessage msg) throws Exception {
		return wxSendMsg(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> wxSendMsg(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		ReqExpressOrder req = commonReq.getParam();
		ReqExpressOrder sendMsg = new ReqExpressOrder(req.getMsg());
		if (StringUtil.isNotBlank(req.getId())) {
			sendMsg.setId(req.getId());
		}else if (StringUtil.isNotBlank(req.getWxOrderId())) {
			sendMsg.setWxOrderId(req.getWxOrderId());
		}else {
			return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		List<ExpressOrderVo> list = expressOrderMapper.selectExpressOrder(sendMsg);
		commonReq.getParam().setName(list.get(0).getName());
		commonReq.getParam().setPatientId(list.get(0).getPatientId());
		commonReq.getParam().setTotalMoney(list.get(0).getTotalMoney());
		commonReq.getParam().setApplyTime(list.get(0).getApplyTime());
		commonReq.getParam().setUpdateTime(new Timestamp(System.currentTimeMillis()));
		commonReq.getParam().setOrderState(list.get(0).getOrderState());
		commonReq.getParam().setMsgUrl(req.getMsgUrl());	
		OrderView view = expressOrderMapper.getPayState(list.get(0).getWxOrderId());
		if(view == null || StringUtil.isBlank(view.getPayState())) {
			commonReq.getParam().setPayState("0");
		}else {
			commonReq.getParam().setPayState(Integer.toString(view.getPayState()));
		}
		CommonResp<RespMap> respMap = mcopyWechatService.sendMessage(commonReq);
		
		return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	@Override
	public String AddConsigneeAddress(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return addConsigneeAddress(new CommonReq<ReqConsigneeAddress>(new ReqConsigneeAddress(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> addConsigneeAddress(CommonReq<ReqConsigneeAddress> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqConsigneeAddress reqConsigneeAddress = commonReq.getParam();
		ConsigneeAddress consigneeAddress = new ConsigneeAddress();
		BeanUtils.copyProperties(consigneeAddress, reqConsigneeAddress);
		consigneeAddress.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		consigneeAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
		consigneeAddress.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		consigneeAddress.setOperatorId("addConsigneeAddress");
		consigneeAddress.setOperatorName("新增收件人信息");
		int result = consigneeAddressMapper.insertSelective(consigneeAddress);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String UpdateConsigneeAddress(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return updateConsigneeAddress(new CommonReq<ReqConsigneeAddress>(new ReqConsigneeAddress(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> updateConsigneeAddress(CommonReq<ReqConsigneeAddress> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqConsigneeAddress reqConsigneeAddress = commonReq.getParam();
		ConsigneeAddress consigneeAddress = consigneeAddressMapper.selectByPrimaryKey(reqConsigneeAddress.getId());
		if(consigneeAddress!=null) {
			if(StringUtil.isNotBlank(reqConsigneeAddress.getAddress())) {
				consigneeAddress.setAddress(reqConsigneeAddress.getAddress());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getAddressee())) {
				consigneeAddress.setAddressee(reqConsigneeAddress.getAddressee());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getCounty())) {
				consigneeAddress.setCounty(reqConsigneeAddress.getCounty());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getOpenId())) {
				consigneeAddress.setOpenId(reqConsigneeAddress.getOpenId());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getProvince())) {
				consigneeAddress.setProvince(reqConsigneeAddress.getProvince());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getState())) {
				consigneeAddress.setState(reqConsigneeAddress.getState());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getTelephone())) {
				consigneeAddress.setTelephone(reqConsigneeAddress.getTelephone());
			}if(StringUtil.isNotBlank(reqConsigneeAddress.getMunicipal())) {
				consigneeAddress.setMunicipal(reqConsigneeAddress.getMunicipal());
			}
			consigneeAddress.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		}
		int result = consigneeAddressMapper.updateByPrimaryKeySelective(consigneeAddress);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String GetConsigneeAddressList(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getConsigneeAddressList(new CommonReq<ReqConsigneeAddress>(new ReqConsigneeAddress(msg))).toResult();
	}
	@Override
	public CommonResp<RespConsigneeAddress> getConsigneeAddressList(CommonReq<ReqConsigneeAddress> commonReq)
			throws Exception {
		// TODO Auto-generated method stub
		List<RespConsigneeAddress> list = new ArrayList<RespConsigneeAddress>();
		ReqConsigneeAddress reqConsigneeAddress = commonReq.getParam();
		ConsigneeAddress consigneeAddress = new ConsigneeAddress();
		consigneeAddress.setOpenId(reqConsigneeAddress.getOpenId());
		List<ConsigneeAddress> consigneeAddresses = consigneeAddressMapper.select(consigneeAddress);
		for (ConsigneeAddress consigneeAddress2 : consigneeAddresses) {
			RespConsigneeAddress respConsigneeAddress = new RespConsigneeAddress();
			BeanUtils.copyProperties(respConsigneeAddress, consigneeAddress2);
			list.add(respConsigneeAddress);
		}
		return new CommonResp<RespConsigneeAddress>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list); 
	}
	@Override
	public String IdCardVerification(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return idCardVerification(new CommonReq<ReqVerification>(new ReqVerification(msg))).toResult();
	}
	@Override
	public CommonResp<RespVerification> idCardVerification(CommonReq<ReqVerification> commonReq) throws Exception {
		// TODO Auto-generated method stub
		RespVerification respVerification = new RespVerification();
		ReqVerification reqVerification = commonReq.getParam();
		String filepath = reqVerification.getFilePath();
		//filepath.replaceAll("\\", "/");
		//获得身份证图片
		File file = new File(filepath);
		try {
			if(StringUtil.isNotBlank(reqVerification.getType())&&reqVerification.getType().equals("2")) {
				//户口本获取信息
				String ocrTemplate = ocridCardApi.ocrTemplateAPI(file);
				JSONObject json = JSONObject.fromObject(ocrTemplate);
				//{"time_used":5597,"result":[{"value":{"text":["929年6月19日"]},"key":"birthday"},{"value":{"text":["360103192906191727"]},"key":"idCard"},{"value":{"text":["余润娇"]},"key":"name"},{"value":{"text":["女"]},"key":"sex"}],"request_id":"1550214622,e7ecfef7-c7e0-4443-a732-fe23a22e5331"}
				JSONArray jsonArray = JSONArray.fromObject(json.getString("result"));
				if(jsonArray.size()>0){
					  for(int i=0;i<jsonArray.size();i++){
					    JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					    String key = (String) job.get("key"); // 得到 每个对象中的属性值
					    JSONObject value = (JSONObject) job.get("value");
					    JSONArray texts = (JSONArray) value.get("text");
					    String text = texts.getString(texts.size()-1);
					    if(key.equals("idCard")) {
					    	respVerification.setIdCards(text);
					    }if(key.equals("name")) {
					    	respVerification.setNames(text);
					    }
					  }
				}
			}else {
				String idCardInfo = ocridCardApi.ocrIDCardAPI(file);
				JSONObject json = JSONObject.fromObject(idCardInfo);
				JSONArray jsonArray = JSONArray.fromObject(json.getString("cards"));
				JSONObject ob = (JSONObject) jsonArray.get(0);
				if(StringUtil.isBlank(ob.getString("side"))) {
					return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
				}else if(ob.getString("side").equals("front")) {
					if(StringUtil.isBlank(ob.getString("name"))) {
						return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
					}if(StringUtil.isBlank(ob.getString("id_card_number"))) {
						return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
					}
					respVerification.setIdCards(ob.getString("id_card_number"));
					respVerification.setNames(ob.getString("name"));
				}				
			}		
		} catch (Exception e) {
			return new CommonResp<RespVerification>(commonReq, RetCode.Common.ERROR_PARAM, "上传的不是合法身份证照片，请重新上传");
		}
		
		return new CommonResp<RespVerification>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respVerification); 
	}
	@Override
	public String AddCopyContent(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return addCopyContent(new CommonReq<ReqCopyContent>(new ReqCopyContent(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> addCopyContent(CommonReq<ReqCopyContent> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyContent reqCopyContent = commonReq.getParam();
		CopyContent copyContent = new CopyContent();
		BeanUtils.copyProperties(copyContent, reqCopyContent);
		copyContent.setCreateTime(new Timestamp(System.currentTimeMillis()));
		copyContent.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		copyContent.setOperatorId("AddCopyContent");
		copyContent.setOperatorName("添加复印内容");
		int result = copyContentMapper.insertSelective(copyContent);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String UpdateCopyContent(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return updateCopyContent(new CommonReq<ReqCopyContent>(new ReqCopyContent(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> updateCopyContent(CommonReq<ReqCopyContent> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyContent reqCopyContent = commonReq.getParam();
		CopyContent copyContent = copyContentMapper.selectByPrimaryKey(reqCopyContent.getId());
		if(StringUtil.isNotBlank(reqCopyContent.getName())) {
			copyContent.setName(reqCopyContent.getName());
		}if(StringUtil.isNotBlank(reqCopyContent.getEnableShow())) {
			copyContent.setEnableShow(reqCopyContent.getEnableShow());
		}if(StringUtil.isNotBlank(reqCopyContent.getEnableDel())) {
			copyContent.setEnableDel(reqCopyContent.getEnableDel());
		}if(StringUtil.isNotBlank(reqCopyContent.getSort())) {
			copyContent.setSort(reqCopyContent.getSort());
		}
		int result = copyContentMapper.updateByPrimaryKeySelective(copyContent);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String GetCopyContentList(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getCopyContentList(new CommonReq<ReqCopyContent>(new ReqCopyContent(msg))).toResult();
	}
	@Override
	public CommonResp<RespCopyContent> getCopyContentList(CommonReq<ReqCopyContent> commonReq) throws Exception {
		// TODO Auto-generated method stub
		CopyPurpose copyPurpose = null;
		ReqCopyContent reqCopyContent = commonReq.getParam();
		CopyContent copyContent = new CopyContent();
		if(StringUtil.isNotBlank(reqCopyContent.getEnableShow())) {
			copyContent.setEnableShow(reqCopyContent.getEnableShow());
		}
		if (StringUtil.isNotBlank(reqCopyContent.getPurposeId())) {
			copyPurpose = copyPurposeMapper.selectByPrimaryKey(reqCopyContent.getPurposeId());
		}
		List<CopyContent> copyContents = copyContentMapper.select(copyContent);
		List<RespCopyContent> respCopyContents = new ArrayList<RespCopyContent>();
		for (CopyContent content : copyContents) {
			RespCopyContent respCopyContent = new RespCopyContent();
			BeanUtils.copyProperties(respCopyContent, content);
			if(copyPurpose!=null&&copyPurpose.getContentIds().contains(respCopyContent.getId())) {
				respCopyContent.setIsDefault("1");
			}else {
				respCopyContent.setIsDefault("0");
			}
			respCopyContents.add(respCopyContent);
		}
		return new CommonResp<RespCopyContent>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respCopyContents);
	}
	@Override
	public String AddCopyPurpose(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return addCopyPurpose(new CommonReq<ReqCopyPurpose>(new ReqCopyPurpose(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> addCopyPurpose(CommonReq<ReqCopyPurpose> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyPurpose reqCopyPurpose = commonReq.getParam();
		CopyPurpose copyPurpose = new CopyPurpose();
		BeanUtils.copyProperties(copyPurpose, reqCopyPurpose);
		copyPurpose.setCreateTime(new Timestamp(System.currentTimeMillis()));
		copyPurpose.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		copyPurpose.setOperatorId("AddCopyPurpose");
		copyPurpose.setOperatorName("添加复印用途");
		int result = copyPurposeMapper.insertSelective(copyPurpose);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String UpdateCopyPurpose(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return updateCopyPurpose(new CommonReq<ReqCopyPurpose>(new ReqCopyPurpose(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> updateCopyPurpose(CommonReq<ReqCopyPurpose> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyPurpose reqCopyPurpose = commonReq.getParam();
		CopyPurpose copyPurpose = copyPurposeMapper.selectByPrimaryKey(reqCopyPurpose.getId());
		if(copyPurpose!=null) {
			if(StringUtil.isNotBlank(reqCopyPurpose.getName())) {
				copyPurpose.setName(reqCopyPurpose.getName());
			}if(StringUtil.isNotBlank(reqCopyPurpose.getEnableShow())) {
				copyPurpose.setEnableShow(reqCopyPurpose.getEnableShow());
			}if(StringUtil.isNotBlank(reqCopyPurpose.getEnableDel())) {
				copyPurpose.setEnableDel(reqCopyPurpose.getEnableDel());
			}if(StringUtil.isNotBlank(reqCopyPurpose.getSort())) {
				copyPurpose.setSort(reqCopyPurpose.getSort());;
			}if(StringUtil.isNotBlank(reqCopyPurpose.getState())) {
				copyPurpose.setState(reqCopyPurpose.getState());
			}if(StringUtil.isNotBlank(reqCopyPurpose.getContentIds())) {
				copyPurpose.setContentIds(reqCopyPurpose.getContentIds());
			}
		}
		int result = copyPurposeMapper.updateByPrimaryKeySelective(copyPurpose);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String GetCopyPurposeList(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getCopyPurposeList(new CommonReq<ReqCopyPurpose>(new ReqCopyPurpose(msg))).toResult();
	}
	@Override
	public CommonResp<RespCopyPurpose> getCopyPurposeList(CommonReq<ReqCopyPurpose> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyPurpose reqCopyPurpose = commonReq.getParam();
		CopyPurpose copyPurpose = new CopyPurpose();
		if(StringUtil.isNotBlank(reqCopyPurpose.getState())) {
			copyPurpose.setState(reqCopyPurpose.getState());
		}if(StringUtil.isNotBlank(reqCopyPurpose.getEnableShow())) {
			copyPurpose.setEnableShow(reqCopyPurpose.getEnableShow());
		}
		List<RespCopyPurpose> respCopyPurposes = new ArrayList<RespCopyPurpose>();
		List<CopyPurpose> copyPurposes = copyPurposeMapper.select(copyPurpose);
		for (CopyPurpose purpose : copyPurposes) {
			RespCopyPurpose respCopyPurpose = new RespCopyPurpose();
			BeanUtils.copyProperties(respCopyPurpose, purpose);
			String[] contentIds = purpose.getContentIds().split(",");
			StringBuffer stringBuffer = new StringBuffer();
			int i = 0;
			List<CopyContent> copyContents = copyContentMapper.getCopyContentByCopyContentIds(contentIds);
			for (CopyContent copyContent : copyContents) {
				i++;
				stringBuffer.append(copyContent.getName());
				if(i<copyContents.size()) {
					stringBuffer.append(",");
				}
				
			}
			respCopyPurpose.setContentNames(stringBuffer.toString());
			respCopyPurposes.add(respCopyPurpose);
		}
		return new CommonResp<RespCopyPurpose>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respCopyPurposes); 
	}
	@Override
	public String FrontGetPriceManage(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return frontGetPriceManage(new CommonReq<ReqPriceManage>(new ReqPriceManage(msg))).toResult();
	}
	@Override
	public CommonResp<RespPriceManage> frontGetPriceManage(CommonReq<ReqPriceManage> commonReq) throws Exception {
		// TODO Auto-generated method stub
		//获取基本设置收费设置类型
		Setting setting = new Setting();
		setting.setName("收费设置");
		setting = settingMapper.selectOne(setting);
		String priceType = setting.getDefaultType();
		PriceManage priceManage = new PriceManage();
		priceManage.setState("2");
		priceManage.setPriceType(priceType);
		List<RespPriceManage> respPriceManages = new ArrayList<>();
		List<PriceManage> priceManages = priceManageMapper.select(priceManage);
		for (PriceManage manage : priceManages) {
			RespPriceManage respPriceManage = new RespPriceManage();
			if(priceType.equals("1")) {
				BeanUtils.copyProperties(respPriceManage, manage);
				respPriceManages.add(respPriceManage);
			}else {
				//根据套餐收费（后面补上）
			}
		}
		
		return new CommonResp<RespPriceManage>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respPriceManages);
	}
	@Override
	public String GetSettingById(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getSettingById(new CommonReq<ReqSetting>(new ReqSetting(msg))).toResult();
	}
	@Override
	public CommonResp<RespSetting> getSettingById(CommonReq<ReqSetting> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqSetting reqSetting = commonReq.getParam();
		Setting setting = settingMapper.selectByPrimaryKey(reqSetting.getId());
		RespSetting respSetting = new RespSetting();
		BeanUtils.copyProperties(reqSetting, setting);
		return new CommonResp<RespSetting>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respSetting);
	}
	@Override
	public String UpdateSetting(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return updateSetting(new CommonReq<ReqSetting>(new ReqSetting(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> updateSetting(CommonReq<ReqSetting> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqSetting reqSetting = commonReq.getParam();
		Setting setting = new Setting();
		setting.setId(reqSetting.getId());
		if(StringUtil.isNotBlank(reqSetting.getDefaultType())) {
			setting.setDefaultType(reqSetting.getDefaultType());
		}if(StringUtil.isNotBlank(reqSetting.getDescription())) {
			setting.setDescription(reqSetting.getDescription());
		}if(StringUtil.isNotBlank(reqSetting.getTypes())) {
			setting.setTypes(reqSetting.getTypes());
		}if(StringUtil.isNotBlank(reqSetting.getSettingSwitch())) {
			setting.setSettingSwitch(reqSetting.getSettingSwitch());
		}if(StringUtil.isNotBlank(reqSetting.getName())) {
			setting.setRemark(reqSetting.getName());
		}if(StringUtil.isNotBlank(reqSetting.getRemark())) {
			setting.setRemark(reqSetting.getRemark());
		}
		int result = settingMapper.updateByPrimaryKeySelective(setting);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String AddQuestion(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return addQuestion(new CommonReq<ReqCopyQuestion>(new ReqCopyQuestion(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> addQuestion(CommonReq<ReqCopyQuestion> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyQuestion reqQuestion = commonReq.getParam();
		CopyQuestion question = new CopyQuestion();
		BeanUtils.copyProperties(question, reqQuestion);
		question.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		question.setCreateTime(new Timestamp(System.currentTimeMillis()));
		question.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		question.setOperatorId("AddQuestion");
		question.setOperatorName("添加常用问题");
		int result = copyQuestionMapper.insertSelective(question);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String UpdateQuestion(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return updateQuestion(new CommonReq<ReqCopyQuestion>(new ReqCopyQuestion(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> updateQuestion(CommonReq<ReqCopyQuestion> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqCopyQuestion reqCopyQuestion = commonReq.getParam();
		CopyQuestion copyQuestion = new CopyQuestion();
		copyQuestion.setId(reqCopyQuestion.getId());
		copyQuestion.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		if(StringUtil.isNotBlank(reqCopyQuestion.getAnswer())) {
			copyQuestion.setAnswer(reqCopyQuestion.getAnswer());
		}if(StringUtil.isNotBlank(reqCopyQuestion.getQuestion())) {
			copyQuestion.setQuestion(reqCopyQuestion.getQuestion());
		}if(StringUtil.isNotBlank(reqCopyQuestion.getState())) {
			copyQuestion.setState(reqCopyQuestion.getState());
			if(reqCopyQuestion.getState().equals("2")) {
				copyQuestion.setReleaseTime(new Timestamp(System.currentTimeMillis()));
			}
		}
		int result = copyQuestionMapper.updateByPrimaryKeySelective(copyQuestion);
		if (result == 1) {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
		}else {
			return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT); 
		}
	}
	@Override
	public String GetQuestionList(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getQuestionList(new CommonReq<ReqCopyQuestion>(new ReqCopyQuestion(msg))).toResult();
	}
	@Override
	public CommonResp<RespCopyQuestion> getQuestionList(CommonReq<ReqCopyQuestion> commonReq) throws Exception {
		// TODO Auto-generated method stub
		List<RespCopyQuestion> respCopyQuestions = new ArrayList<RespCopyQuestion>(); 
		List<CopyQuestion> questions = copyQuestionMapper.selectCopyQuestion(commonReq.getParam());
		for (CopyQuestion copyQuestion : questions) {
			RespCopyQuestion respCopyQuestion = new RespCopyQuestion();
			BeanUtils.copyProperties(respCopyQuestion, copyQuestion);
			respCopyQuestions.add(respCopyQuestion);
		}
		return new CommonResp<RespCopyQuestion>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respCopyQuestions); 
	}
	@Override
	public String GetSignature(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getSignature(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> getSignature(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		ReqExpressOrder req = commonReq.getParam();
		String wxKey = req.getConfigKey();
		String type = "jsapi";
		String appid = KasiteConfig.getWxConfig(WXConfigEnum.wx_app_id, wxKey);
		String secret = KasiteConfig.getWxConfig(WXConfigEnum.wx_secret, wxKey);
		String key = appid + secret + type;
		Ticket ticket = new Ticket();
		String url = WeiXinConstant.GETTICKET_URL + "&type=" + type;
		JSONObject json = HttpClientService.doHttpsGet(ApiModule.WeChat.ticket_getticket,url,wxKey);
		if (json != null) {
			String t = JSONUtil.getJsonString(json, "ticket");
		}
		return null;
	}
	@Override
	public String GetFengQiaoLogistics(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getFengQiaoLogistics(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<FengQiaoLogisticsVo> getFengQiaoLogistics(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		List<FengQiaoLogisticsVo> fengQiaoLogisticsVos = new ArrayList<FengQiaoLogisticsVo>();
		ReqExpressOrder reqExpressOrder = commonReq.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		if(expressOrder!=null) {
			String result = fengQiaoApi.fengqiaoApiSearch(expressOrder.getReceiveCode());
			Document doc = XMLUtil.parseXml(result);
			Element root = doc.getRootElement();
			String head = XMLUtil.getString(root, "Head", false);
			if(StringUtil.isNotBlank(head)&&head.equalsIgnoreCase("ok")) {
				Element routeResponse = root.element("Body").element("RouteResponse ");
				List<Element> elements = routeResponse.elements();
				for (Element element : elements) {
					FengQiaoLogisticsVo fengQiaoLogisticsVo = new FengQiaoLogisticsVo();
					fengQiaoLogisticsVo.setAcceptAddress(element.attributeValue("accept_address"));
					fengQiaoLogisticsVo.setAcceptTime(element.attributeValue("accept_time"));
					fengQiaoLogisticsVo.setRemark(element.attributeValue("remark"));
					fengQiaoLogisticsVos.add(fengQiaoLogisticsVo);
				}
			}
		}else {
			return new CommonResp<FengQiaoLogisticsVo>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM); 
		}
		return new CommonResp<FengQiaoLogisticsVo>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,fengQiaoLogisticsVos); 
	}
	@Override
	public String QueryPatientInfo(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return queryPatientInfo(new CommonReq<ReqQueryPatientInfoByNos>(new ReqQueryPatientInfoByNos(msg))).toResult();
	}
	@Override
	public CommonResp<RespQueryPatientInfoByNos> queryPatientInfo(CommonReq<ReqQueryPatientInfoByNos> req)
			throws Exception {
		// TODO Auto-generated method stub
		String mcopyUserId = "";
		ReqQueryPatientInfoByNos reqVo = req.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		//5：身份证号、6：病案号
		if(reqVo.getCardType().equals("5")) {
			paramMap.put("patientId", "");
			paramMap.put("idCard", reqVo.getCardNo());
		}else {
			paramMap.put("patientId", reqVo.getCardNo());
			paramMap.put("idCard", "");
		}
		paramMap.put("name", reqVo.getName());
		
		//his获取病人信息
		List<HisQueryPatientInfoByNo> list = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(),ICallHisMedicalCopy.class).queryPatientInfoByNos(reqVo.getMsg(), paramMap).getListCaseRetCode();

		//查询不到病人信息
		if(list == null || list.isEmpty()) {
			return new CommonResp<RespQueryPatientInfoByNos>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			List<RespQueryPatientInfoByNos> respList = new ArrayList<RespQueryPatientInfoByNos>();
			HisQueryPatientInfoByNo item = list.get(0);			
			//保存病人信息
			MCopyUser mCopyUser = new MCopyUser();
			mCopyUser.setPatientId(item.getPatientId());
			mCopyUser.setName(item.getName());
			List<MCopyUser> mCopyUserList = mCopyUserMapper.select(mCopyUser);			
			mCopyUser.setCardNo(item.getCardNo());
			mCopyUser.setGender(item.getGender());
			mCopyUser.setIdType(item.getIdType());
			mCopyUser.setIdCode(item.getIdCode());
			mCopyUser.setBirthday(item.getBirthday());
			mCopyUser.setChargeType(item.getChargeType());
			mCopyUser.setIdentity(item.getIdentity());
			mCopyUser.setIdCard(item.getOutPara2());
			mCopyUser.setPhone(item.getPhone());
			mCopyUser.setBalance(item.getBalance());
			mCopyUser.setOutPara1(item.getOutPara1());
			mCopyUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			if (mCopyUserList == null || mCopyUserList.isEmpty()) {
				mcopyUserId = item.getPatientId();
				mCopyUser.setId(mcopyUserId);
				mCopyUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
				mCopyUser.setOperatorId("addMCopyUser");
				mCopyUser.setOperatorName("新增病历复印用户");
				mCopyUserMapper.insert(mCopyUser);					
			}else {
				mcopyUserId = mCopyUserList.get(0).getId();
				mCopyUser.setId(mcopyUserId);
				mCopyUserMapper.updateByPrimaryKeySelective(mCopyUser);
			}			
			//将病人id(病案号)发回前端
			RespQueryPatientInfoByNos resp = new RespQueryPatientInfoByNos();
			resp.setMcopyUserId(mcopyUserId);
			respList.add(resp);
			return new CommonResp<RespQueryPatientInfoByNos>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}

	}
	@Override
	public String GetMedicalRecords(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return getMedicalRecords(new CommonReq<ReqQueryMedicalRecords>(new ReqQueryMedicalRecords(msg))).toResult();
	}
	@Override
	public CommonResp<RespQueryMedicalRecords> getMedicalRecords(CommonReq<ReqQueryMedicalRecords> req)
			throws Exception {
		// TODO Auto-generated method stub		
		ReqQueryMedicalRecords reqVo = req.getParam();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put("patientId", reqVo.getPatientId());
		paramMap.put("name", reqVo.getName());
		paramMap.put("idCard", reqVo.getIdCard());
		List<HisQueryMedicalRecords> list = HandlerBuilder.get().getCallHisService(reqVo.getAuthInfo(),ICallHisMedicalCopy.class).queryMedicalRecords(reqVo.getMsg(), paramMap).getListCaseRetCode();

		if(list == null || list.isEmpty()) {
			return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}else {
			List<RespQueryMedicalRecords> respList = new ArrayList<RespQueryMedicalRecords>();
			for (HisQueryMedicalRecords item : list) {					
				//将获得的病历添加到数据库
				McopyCase mcopyCase = new McopyCase();				
				//返回
				RespQueryMedicalRecords resp = new RespQueryMedicalRecords();
				resp.setHospitalId(item.getHospitalId());
				
				List<ExpressOrder> expressOrders = expressOrderMapper.selectExpressOrderByCaseId(item.getHospitalId());
				if (expressOrders == null || expressOrders.isEmpty()) {
					resp.setExistOrder(false);
					resp.setApplyTime("");
				}else {
					resp.setExistOrder(true);
					resp.setApplyTime(expressOrders.get(0).getApplyTime());
				}
				resp.setErdat(item.getErdat());
				resp.setFalar(item.getFalar());
				resp.setInHosDate(item.getInHosDate());
				resp.setDeptCode(item.getDeptCode());
				resp.setDeptName(item.getDeptName());
				resp.setErtim(item.getErtim());
				resp.setOutDeptCode(item.getOutDeptCode());
				if (StringUtil.isBlank(item.getOutDeptName())) {
					resp.setOutDeptName("暂无");	
				}else {
					resp.setOutDeptName(item.getOutDeptName());	
				}
				resp.setIsoperation(item.getIsoperation());
				mcopyCase.setIsoperation(item.getIsoperation());
				paramMap.put("HospitalId", item.getHospitalId());								
				if (StringUtil.isNotBlank(item.getInHosDate()) && StringUtil.isNotBlank(item.getOutHosDate())) {
					int diffDay = DateOper.diffDay(item.getInHosDate(),item.getOutHosDate(),DateOper.LONG_DATE_TIME_FORMAT_STRING);
					resp.setHospitalization(Integer.toString(diffDay));
				}else {
					resp.setHospitalization("暂无");
				}
				if(StringUtil.isNotBlank(item.getOutHosDate())) {
					resp.setOutHosDate(DateOper.formatDate(item.getOutHosDate(), DateOper.LONG_DATE_TIME_FORMAT_STRING, DateOper.format16chars));
				}else {
					resp.setOutHosDate("暂无");
				}
				respList.add(resp);
				if(mcopyCaseMapper.selectByPrimaryKey(item.getHospitalId()) == null) {
					mcopyCase.setId(item.getHospitalId()); 
					mcopyCase.setErdat(item.getErdat());
					mcopyCase.setFalar(item.getFalar());
					mcopyCase.setInHosDate(item.getInHosDate());
					mcopyCase.setDeptCode(item.getDeptCode());
					mcopyCase.setDeptName(item.getDeptName());
					mcopyCase.setErtim(item.getErtim());
					mcopyCase.setOutHosDate(item.getOutHosDate());
					mcopyCase.setOutDeptCode(item.getOutDeptCode());
					mcopyCase.setOutDeptName(item.getOutDeptName());	
					mcopyCase.setCreateTime(new Timestamp(System.currentTimeMillis()));
					mcopyCase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					mcopyCase.setOperatorId("addMcopyCase");
					mcopyCase.setOperatorName("新增病历");
					mcopyCaseMapper.insert(mcopyCase);				
				}					
			}
			return new CommonResp<RespQueryMedicalRecords>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,reqVo.getPage());
		}
	}
	@Override
	public String CheckExpressOrder(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return checkExpressOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> checkExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder reqExpressOrder = commonReq.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		if(expressOrder!=null) {
			expressOrder.setReason(reqExpressOrder.getReason());
			expressOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			if(reqExpressOrder.getApplyState().equals("2")) {
				expressOrder.setOrderState("6");
			}else {
				expressOrder.setOrderState("5");
			}
			expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
			return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}else {
			return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		
	}
	@Override
	public String CreateExpressOrder(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return createExpressOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespExpressOrder> createExpressOrder(CommonReq<ReqExpressOrder> req) throws Exception {

		ReqExpressOrder upReq = req.getParam();
		if(StringUtil.isBlank(upReq.getCaseIdAll())) {
		 	return new CommonResp<RespExpressOrder>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.NULLERROR);
		}
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		ExpressOrder expressOrder = new ExpressOrder();
		expressOrder.setId(id);
		expressOrder.setTotalMoney(upReq.getTotalMoney());
		expressOrder.setCaseIdAll(upReq.getCaseIdAll());
		expressOrder.setCaseNumberAll(upReq.getCaseNumberAll());
		expressOrder.setPatientId(upReq.getPatientId());
		expressOrder.setAuthentication(upReq.getAuthentication());
		expressOrder.setAddressee(upReq.getAddressee());
		expressOrder.setAddress(upReq.getAddress());
		expressOrder.setTelephone(upReq.getTelephone());
		expressOrder.setApplyOpenId(upReq.getApplyOpenId());
		expressOrder.setApplyName(upReq.getApplyName());
		expressOrder.setWxOrderId(upReq.getWxOrderId());
		expressOrder.setProvince(upReq.getProvince());
		expressOrder.setIdcardImgName(upReq.getIdcardImgName());
		expressOrder.setOrderState("1");
		expressOrder.setPayState("0");
		expressOrder.setApplyRelationType(upReq.getApplyRelationType());
		expressOrder.setReceiveType(upReq.getReceiveType());
		expressOrder.setProveImgs(upReq.getProveImgs());
		expressOrder.setPreMoney(upReq.getPreMoney());
		expressOrder.setApplyTime(DateUtils.formatDateToString(new Date(), DateUtils.DATE_TIME_PATTERN));
		expressOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		expressOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		expressOrder.setOperatorId("addExpressOrder");
		expressOrder.setOperatorName("新增病历复印订单");
		expressOrder.setHosId(upReq.getHosId());
		expressOrderMapper.insertSelective(expressOrder);
		
		String[] caseIdList = upReq.getCaseIdAll().split(",");
		String[] caseNumList = upReq.getCaseNumberAll().split(",");
		String[] copyContentNameList = upReq.getCopyContentNames().split(";");
		String[] copyUtilityList = upReq.getCopyUtilitys().split(",");
		for (int i = 0; i < caseIdList.length; i++) {
			OrderCase orderCase = new OrderCase();
			orderCase.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			orderCase.setOrderId(id);			
			orderCase.setCaseId(caseIdList[i]);
			orderCase.setCaseNumber(caseNumList[i]);
			orderCase.setCopyContentNames(copyContentNameList[i]);
			orderCase.setCopyUtility(copyUtilityList[i]);
			orderCase.setPriceManageId(upReq.getPriceManageId());
			orderCase.setCreateTime(new Timestamp(System.currentTimeMillis()));
			orderCase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			orderCase.setOperatorId("addOrderCase");
			orderCase.setOperatorName("新增复印订单与病历的关系");
			orderCaseMapper.insertSelective(orderCase);
			//}else {
			//	return new CommonResp<RespExpressOrder>(req, RetCode.Common.ERROR_CALLHIS, "病例复印请求信息回写有误");
			//}
		}
		//添加交易记录
		TransactionRecord transactionRecord = new TransactionRecord();
		transactionRecord.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		transactionRecord.setOrderId(id);
		transactionRecord.setOrderType("1");
		transactionRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
		transactionRecord.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		transactionRecord.setOperatorId("addTransactionRecord");
		transactionRecord.setOperatorName("新增交易记录");
		transactionRecord.setState("0");
		transactionRecordMapper.insertSelective(transactionRecord);
		//添加全流程订单
		ReqAddOrderLocal addOrderLocal = new ReqAddOrderLocal(upReq.getMsg(), UUID.randomUUID().toString().replaceAll("-", ""), null, StringUtil.yuanChangeFenInt(upReq.getTotalMoney()), StringUtil.yuanChangeFenInt(upReq.getTotalMoney()),
				"病历复印费用", null, null, upReq.getOpenId(), "微信", "010", 1, 1, null);
		CommonResp<RespMap> resp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderLocal));
		String code = resp.getCode();
		if(code.equals("10000")) {
			RespMap respMap = resp.getData().get(0);
			String wxOrderId = respMap.getString(ApiKey.AddOrderLocal.OrderId);
			expressOrder = new ExpressOrder();
			expressOrder.setId(id);
			expressOrder.setWxOrderId(wxOrderId);
			expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
		}
		return new CommonResp<RespExpressOrder>(req, RetCode.Success.RET_10000, id);
	}
	@Override
	public String AfterUnderOrder(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return afterUnderOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> afterUnderOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder reqExpressOrder = commonReq.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		if(expressOrder!=null) {
			expressOrder.setPayState("3");
			OrderView view = expressOrderMapper.getPayState(expressOrder.getWxOrderId());
			TransactionRecord transactionRecord = new TransactionRecord();
			transactionRecord.setOrderId(expressOrder.getId());
			transactionRecord = transactionRecordMapper.selectOne(transactionRecord);
			transactionRecord.setState("1");
			transactionRecord.setActualReceipts(StringUtil.yuanChangeFen(StringUtil.intToStr(view.getTotalPrice())));
			transactionRecordMapper.updateByPrimaryKeySelective(transactionRecord);
			expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
		}
		return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000); 
	}
	@Override
	public String CheckExpressOrderFee(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return checkExpressOrderFee(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> checkExpressOrderFee(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder reqExpressOrder = commonReq.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		TransactionRecord transactionRecord = new TransactionRecord();
		if(expressOrder==null) {
			return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		OrderView view = expressOrderMapper.getPayState(expressOrder.getWxOrderId());
		transactionRecord.setOrderId(expressOrder.getId());
		transactionRecord = transactionRecordMapper.selectOne(transactionRecord);
		BigDecimal totalMoney = new BigDecimal(reqExpressOrder.getTotalMoney());
		BigDecimal preMoney = new BigDecimal(expressOrder.getPreMoney());
		BigDecimal balance = preMoney.subtract(totalMoney);
		if(balance.doubleValue()>=0) {
			//自动退款
			ReqOrderIsCancel reqOrderIsCancel = new ReqOrderIsCancel(reqExpressOrder.getMsg(), view.getOrderId(), view.getPrice(), StringUtil.yuanChangeFenInt(balance.toString()), view.getOperatorId(), view.getOperatorName(), view.getChannelId(), "预付款大于实际金额");
			orderService.orderIsCancel(new CommonReq<ReqOrderIsCancel>(reqOrderIsCancel));			
			expressOrder.setOrderState("2");
			transactionRecord.setShouldRefunds(balance.abs().toPlainString());
			transactionRecord.setActualRefunds(balance.abs().toPlainString());

		}else {
			//提醒用户补缴
			expressOrder.setOrderState("7");
			transactionRecord.setShouldRefunds("0");
			transactionRecord.setActualRefunds("0");
		}
		if(transactionRecord.getState().equals("0")) {
			transactionRecord.setState("1");
		}				
		expressOrder.setTotalMoney(totalMoney.toString());
		expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
		transactionRecordMapper.updateByPrimaryKeySelective(transactionRecord);
		return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	@Override
	public String SendExpressOrder(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return sendExpressOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> sendExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder reqExpressOrder = commonReq.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		if(expressOrder==null) {
			return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		if(expressOrder.getReceiveType().equals("1")) {
			//快递下单
			FengQiaoOrderVo fengQiaoOrderVo = new FengQiaoOrderVo();
			fengQiaoOrderVo.setOrderId(expressOrder.getId());
			fengQiaoOrderVo.setPayMethod("2");
			fengQiaoOrderVo.setExpressType("2");
			fengQiaoOrderVo.setParcelQuantity("1");
			ReqQueryHospitalLocal hospitalLocal = new ReqQueryHospitalLocal(reqExpressOrder.getMsg(), expressOrder.getHosId());
			CommonResp<RespQueryHospitalLocal> resp = basicCommonService.queryHospitalLocal(new CommonReq<ReqQueryHospitalLocal>(hospitalLocal));
			List<RespQueryHospitalLocal> list = resp.getData();
			if(list!=null&&list.size()>0) {
				RespQueryHospitalLocal queryHospitalLocal = list.get(0);
				fengQiaoOrderVo.setjAddress(queryHospitalLocal.getAddress());
				fengQiaoOrderVo.setjCity(queryHospitalLocal.getCity());
				fengQiaoOrderVo.setjContact(queryHospitalLocal.getHosName());
				fengQiaoOrderVo.setjProvince(queryHospitalLocal.getProvince());
				fengQiaoOrderVo.setjTel(queryHospitalLocal.getTel());
			}
			ConsigneeAddress consigneeAddress = consigneeAddressMapper.selectByPrimaryKey(expressOrder.getAddressee());
			if(consigneeAddress!=null) {
				fengQiaoOrderVo.setdAddress(consigneeAddress.getAddress());
				fengQiaoOrderVo.setdCity(consigneeAddress.getMunicipal());
				fengQiaoOrderVo.setdContact(expressOrder.getApplyName());
				fengQiaoOrderVo.setdProvince(consigneeAddress.getProvince());
				fengQiaoOrderVo.setdTel(consigneeAddress.getTelephone());
			}
			String result = fengQiaoApi.downloadOrder(fengQiaoOrderVo);
			Document document = XMLUtil.parseXml(result);
			Element root = document.getRootElement();
			String head = XMLUtil.getString(root, "XMLUtil", false);
			if(head.equalsIgnoreCase("ok")) {
				Element orderResponse = root.element("Body").element("OrderResponse");
				expressOrder.setReceiveCode(orderResponse.attributeValue("mailno"));
			}
		}else {
			//来院自取
			expressOrder.setReceiveCode(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		expressOrder.setOrderState("3");
		expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
		return new CommonResp<RespMap>(commonReq,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	@Override
	public String ExportExpressOrder(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return exportExpressOrder(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> exportExpressOrder(CommonReq<ReqExpressOrder> commonReq) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder expressOrder = commonReq.getParam();
		List<ExpressOrderVo> orders = expressOrderMapper.selectExpressOrder(expressOrder);	
		if(orders.isEmpty()) {
			new CommonResp<RespExpressOrder>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
		List<ExportExpressOrderVo> dataSet = new ArrayList<ExportExpressOrderVo>();
		for (ExpressOrderVo item : orders) {
			ExportExpressOrderVo r = new ExportExpressOrderVo();
			r.setId(item.getId());
			r.setName(item.getName());
			r.setPatientId(item.getPatientId());			
			r.setApplyTime(item.getApplyTime());
			r.setState(getState(item.getOrderState(), item.getPayState(), item.getApplyTime(),2));
			String[] orderIds = StringUtil.isNotBlank(item.getWxOrderId())?item.getWxOrderId().split(","):null;
			if(orderIds!=null&&orderIds.length>0) {
				Map<String, Object> map = expressOrderMapper.countPriceByOrderIds(orderIds);
				r.setPreMoney(item.getPreMoney());
				Object refundPrice = map.get("REFUNDPRICE");
				Object payPrice = map.get("PAYPRICE");
				r.setRefundMoney(refundPrice.toString());
				r.setTotalMoney(payPrice.toString());				
			}
			int caseNumber = 0;
			for (String string : item.getCaseNumberAll().split(",")) {
				caseNumber+=StringUtil.strToInt(string);
			}
			r.setCopyNumber(StringUtil.intToStr(caseNumber)+"份");
			r.setChannelName("微信公众号");
			dataSet.add(r);
		}
		ExportExcel<ExportExpressOrderVo> ex = new ExportExcel<ExportExpressOrderVo>();
		String[] headers = { "申请时间", "申请渠道", "申请订单号", "患者病案号","复印份数", "预收金额", "实收金额", "原路退回", "订单状态"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = "uploadFile" + File.separator + rootUrl + "exportExpressOrder";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "订单信息表" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataSet, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commonReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	@Override
	public String CountTransactionRecord(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return countTransactionRecord(new CommonReq<ReqTransactionRecord>(new ReqTransactionRecord(msg))).toResult();
	}
	@Override
	public CommonResp<RespTransactionRecord> countTransactionRecord(CommonReq<ReqTransactionRecord> req)
			throws Exception {
		// TODO Auto-generated method stub
		ReqTransactionRecord reqTransactionRecord = req.getParam();
		List<RespTransactionRecord> list = transactionRecordMapper.countTransactionRecord(reqTransactionRecord);
		RespTransactionRecord respTransactionRecord = new RespTransactionRecord();
		int orderNum = 0;
		BigDecimal totalInnerMoney = new BigDecimal(0);
		for (RespTransactionRecord record : list) {
			BigDecimal innerMoney = new BigDecimal(record.getTotalMoney()).subtract(new BigDecimal(record.getTotalRefundMoney()));
			record.setTotalInnerMoney(innerMoney.toPlainString());
			totalInnerMoney.add(innerMoney);
			orderNum+=record.getOrderNum();
		}
		if(reqTransactionRecord.getType().equals("2")) {
			respTransactionRecord.setPayChannel("全渠道");
			respTransactionRecord.setTotalInnerMoney(totalInnerMoney.toPlainString());
			respTransactionRecord.setOrderNum(orderNum);
			list.add(respTransactionRecord);
		}
		return new CommonResp<RespTransactionRecord>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, list);
	}
	@Override
	public String ExportTransactionRecord(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return exportTransactionRecord(new CommonReq<ReqTransactionRecord>(new ReqTransactionRecord(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> exportTransactionRecord(CommonReq<ReqTransactionRecord> req) throws Exception {
		// TODO Auto-generated method stub
		ReqTransactionRecord reqTransactionRecord = req.getParam();
		List<ExportTransactionRecordVo> dataSet = new ArrayList<ExportTransactionRecordVo>();
		List<RespTransactionRecord> list = transactionRecordMapper.countTransactionRecord(reqTransactionRecord);
		for (RespTransactionRecord respTransactionRecord : list) {
			ExportTransactionRecordVo recordVo = new ExportTransactionRecordVo();
			BigDecimal innerMoney = new BigDecimal(respTransactionRecord.getTotalMoney()).subtract(new BigDecimal(respTransactionRecord.getTotalRefundMoney()));
			recordVo.setTotalInnerMoney(innerMoney.toPlainString());
			recordVo.setEoTime(respTransactionRecord.getEoTime());
			recordVo.setOrderNum(respTransactionRecord.getOrderNum());
			recordVo.setTotalMoney(respTransactionRecord.getTotalMoney());
			recordVo.setTotalRefundMoney(respTransactionRecord.getTotalRefundMoney());
			dataSet.add(recordVo);
		}
		ExportExcel<ExportTransactionRecordVo> ex = new ExportExcel<ExportTransactionRecordVo>();
		String[] headers = { "日期", "订单数", "收入金额", "实退金额","净收入金额"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = "uploadFile" + File.separator + rootUrl + "exportTransactionRecord";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "统计报表" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataSet, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	@Override
	public String SupplementaryPay(InterfaceMessage msg) throws Exception {
		// TODO Auto-generated method stub
		return supplementaryPay(new CommonReq<ReqExpressOrder>(new ReqExpressOrder(msg))).toResult();
	}
	@Override
	public CommonResp<RespMap> supplementaryPay(CommonReq<ReqExpressOrder> req) throws Exception {
		// TODO Auto-generated method stub
		ReqExpressOrder reqExpressOrder = req.getParam();
		ExpressOrder expressOrder = expressOrderMapper.selectByPrimaryKey(reqExpressOrder.getId());
		if(expressOrder==null) {
			return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		String[] wxOrderIds = expressOrder.getWxOrderId().split(",");
		Timestamp beginDate = null;
		//是否需要添加全流程订单
		boolean enable = false;
		//判断是否已存在待补缴订单
		if(wxOrderIds.length>1) {
			OrderView view = expressOrderMapper.getPayState(wxOrderIds[wxOrderIds.length-1]);
			Long endDate = view.getBeginDate().getTime()+10*60*1000;
			//判断待补缴订单是否超时
			if(endDate<System.currentTimeMillis()) {
				enable = true;
			}else {
				beginDate = view.getBeginDate();
			}
		}else {
			enable = true;
		}
		if(enable) {
			//添加全流程订单
			ReqAddOrderLocal addOrderLocal = new ReqAddOrderLocal(reqExpressOrder.getMsg(), UUID.randomUUID().toString().replaceAll("-", ""), null, StringUtil.yuanChangeFenInt(reqExpressOrder.getTotalMoney()), StringUtil.yuanChangeFenInt(reqExpressOrder.getTotalMoney()),
					"病历复印费用", null, null, reqExpressOrder.getOpenId(), "微信", "010", 1, 1, null);
			CommonResp<RespMap> resp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderLocal));
			String code = resp.getCode();
			if(code.equals("10000")) {
				RespMap respMap = resp.getData().get(0);
				String wxOrderId = respMap.getString(ApiKey.AddOrderLocal.OrderId);
				expressOrder = new ExpressOrder();
				expressOrder.setId(expressOrder.getId());
				expressOrder.setWxOrderId(expressOrder.getWxOrderId()+","+wxOrderId);
				expressOrderMapper.updateByPrimaryKeySelective(expressOrder);
			}
			beginDate = new Timestamp(System.currentTimeMillis());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateOper.format19chars);
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, dateFormat.format(beginDate.getDate()));
	}
	
//	/**
//	 * 微信临时素材下载
//	 * */
//	@Override
//	public String GetWechatMedia(InterfaceMessage msg) throws Exception{
//		return getWechatMedia(new CommonReq<ReqMcopyWechat>(new ReqMcopyWechat(msg))).toResult();
//	}
//	@Override
//	public CommonResp<RespMap> getWechatMedia(CommonReq<ReqMcopyWechat> commReq) throws Exception{
//		ReqMcopyWechat req = commReq.getParam();
//		//String url = DEPLOY_URL+req.getMediaName()+".png";	
//		CommonResp<RespMap> media = mcopyWechatService.getWechatMedia(commReq,url);
//		return new CommonResp<RespMap>(commReq, RetCode.Success.RET_10000,url);
//	}
	

}
