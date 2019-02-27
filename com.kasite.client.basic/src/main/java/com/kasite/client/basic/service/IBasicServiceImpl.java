package com.kasite.client.basic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.basic.cache.MemberAutoUnbindCache;
import com.kasite.client.basic.cache.MemberListCache;
import com.kasite.client.basic.dao.IBatUserMapper;
import com.kasite.client.basic.dao.ICardBagMapper;
import com.kasite.client.basic.dao.IDeptMapper;
import com.kasite.client.basic.dao.IDoctorMapper;
import com.kasite.client.basic.dao.IMemberBaseMapper;
import com.kasite.client.basic.dao.IMemberMapper;
import com.kasite.client.basic.dao.IPatientMapper;
import com.kasite.client.basic.dao.IUserMemberCardMapper;
import com.kasite.client.basic.dao.IUserMemberMapper;
import com.kasite.client.basic.thread.MemberAutoUnbindThread;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.FormatUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.io.ByteArrayOutputStream;
import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.IPorvingCodeService;
import com.kasite.core.serviceinterface.module.basic.cache.IDoctorLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Article;
import com.kasite.core.serviceinterface.module.basic.dbo.BatUser;
import com.kasite.core.serviceinterface.module.basic.dbo.CardBag;
import com.kasite.core.serviceinterface.module.basic.dbo.Dept;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;
import com.kasite.core.serviceinterface.module.basic.dbo.Hospital;
import com.kasite.core.serviceinterface.module.basic.dbo.Member;
import com.kasite.core.serviceinterface.module.basic.dbo.MemberBase;
import com.kasite.core.serviceinterface.module.basic.dbo.Patient;
import com.kasite.core.serviceinterface.module.basic.dbo.UserMember;
import com.kasite.core.serviceinterface.module.basic.dbo.UserMemberCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqBindClinicCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqBindHospitalNo;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckEntityCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqCheckPorvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelMemberCardInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelMemberInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqGetProvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryArticleList;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDept;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryCardBalance;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryHospitalUserInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryPatientInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryTitleInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqSaveProvingCode;
import com.kasite.core.serviceinterface.module.basic.req.ReqSetDefaultCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqSetDefaultMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateArticle;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqValidateCardInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespAddMemberWithValidate;
import com.kasite.core.serviceinterface.module.basic.resp.RespCardPackage;
import com.kasite.core.serviceinterface.module.basic.resp.RespGetprovingCode;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryArticleList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDept;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDeptLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDeptTreeInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryCardBalance;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryHospitalUserInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryPatientInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryTitleInfo;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IAddMemberService;
import com.kasite.core.serviceinterface.module.his.handler.IMemberCardService;
import com.kasite.core.serviceinterface.module.his.handler.IMiniPayService;
import com.kasite.core.serviceinterface.module.his.resp.HisAddMember;
import com.kasite.core.serviceinterface.module.his.resp.HisCheckEntityCard;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicCard;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryHospitalUserInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryUserInfo;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendSms;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.hos.util.EscapeCodeUtil;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
/**
 * @author linjf 2017年11月14日 17:50:53 TODO
 */
@Service("basic.BasicApi")
public class IBasicServiceImpl extends AbstractBasicCommonService implements IBasicService {

	public final static String fileDir = "uploadFile";

	@Autowired
	private IReportFormsService reportFormsUtil;
	
	@Autowired
	private IMemberBaseMapper memberBaseMapper;
	@Autowired
	private IPatientMapper patientMapper;
	@Autowired
	private IMemberMapper memberMapper;
	@Autowired
	IUserMemberMapper userMemberMapper;
	@Autowired
	IDeptMapper deptMapper;
	@Autowired
	private IDoctorMapper doctorMapper;
	@Autowired
	private IMsgService msgService;
	@Autowired
	private IDoctorLocalCache doctorLocalCache;
	@Autowired
	IPorvingCodeService porvingCodeService;
	@Autowired
	IBatUserMapper userMapper;
	@Autowired
	IUserMemberCardMapper userMemberCardMapper;
	@Autowired
	ICardBagMapper cardBagMapper;
	
	
	@Override
	public CommonResp<RespQueryBaseDoctor> queryLocalDoctor(CommonReq<ReqQueryBaseDoctor> reqComm){
		ReqQueryBaseDoctor req = reqComm.getParam();
		Doctor record = new Doctor();
		if (StringUtil.isNotBlank(req.getHosId())) {
			record.setHosId(req.getHosId());
		}
		if (StringUtil.isNotBlank(req.getDeptCode())) {
			record.setDeptCode(req.getDeptCode());
			
		} 
		if (StringUtil.isNotBlank(req.getDoctorCode())) {
			record.setDoctorCode(req.getDoctorCode());
		}
		if (StringUtil.isNotBlank(req.getDoctorName())) {
			record.setDoctorName(req.getDoctorName());
		}
		if (StringUtil.isNotBlank(req.getDoctorType())) {
			record.setDoctorType(req.getDoctorType());
		}
		List<Doctor> localDoctorList = doctorMapper.select(record);
		List<RespQueryBaseDoctor> respList = new Vector<RespQueryBaseDoctor>();
		if (localDoctorList != null && !localDoctorList.isEmpty()) {
			for (Doctor doctor : localDoctorList) {
				RespQueryBaseDoctor resp = new RespQueryBaseDoctor();
				resp.setDeptCode(doctor.getDeptCode());
				resp.setDeptName(doctor.getDeptName());
				resp.setDoctorCode(doctor.getDoctorCode());
				resp.setDoctorName(doctor.getDoctorName());
				resp.setDoctorTitle(doctor.getTitle());
				resp.setDoctorTitleCode(doctor.getTitleCode());
				resp.setIntro(doctor.getIntroduction());
				resp.setLevel(doctor.getLevelName());
				resp.setLevelId(doctor.getLevelId());
				resp.setPhotoUrl(doctor.getPhotoUrl());
				resp.setPrice(doctor.getPrice());
				resp.setRemark(doctor.getRemark());
				resp.setSex(doctor.getDoctorSex());
				resp.setSpec(doctor.getSpec());
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryBaseDoctor>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList);
	}
	
	
	/**
	 * 支持自动关联的新增就诊人 开发中
	 * ApiModule
	 * @param msg
	 * @return
	 */
	@Override
	public String AddMemberWithValidate(InterfaceMessage msg) {
		//TODO 
		return null;
	}

	/**
	 * 绑定就诊卡
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:20:34
	 */
	@Override
	public String BindClinicCard(InterfaceMessage msg) throws Exception{
		CommonReq<ReqBindClinicCard> reqComm = new CommonReq<ReqBindClinicCard>(new ReqBindClinicCard(msg));
		CommonResp<RespMap> commResp = this.bindClinicCard(reqComm);
		return commResp.toResult();
	}

	/**
	 * 绑定住院号
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:20:59
	 */
	@Override
	public String BindHospitalNo(InterfaceMessage msg) throws Exception{
			CommonReq<ReqBindHospitalNo> reqComm = new CommonReq<ReqBindHospitalNo>(new ReqBindHospitalNo(msg));
			CommonResp<RespMap> commResp = this.bindHospitalNo(reqComm);
			return commResp.toResult();
	}

//	/**
//	 * 查询用户信息 (ok) 1.调用his接口查询用户信息 2.返回HIS返回的数据
//	 * 
//	 * @param msg
//	 * @return
//	 */
//	public String QueryUserInfo(InterfaceMessage msg) {
//		Map<String, String> map = new HashMap<String, String>(16);
//		HisQueryUserInfo hiQ = null;
//		// 调用his接口验证
//		try {
//			ReqQueryUserInfo req = new ReqQueryUserInfo(msg);
//			map.put("cardNo", req.getCardNo());
//			map.put("cardType", req.getCardType());
//			map.put("mobile", req.getMobile());
//			map.put("patientId", req.getPatientId());
//			map.put("memberName", req.getMemberName());
//			map.put("idCardNo", req.getIdCardNo());
//			hiQ = hisService.hisQueryUserinfo(map);
////			hiQ = HisClient.hisQueryUserinfo(map);
//			map.clear();
//			if (hiQ != null) {
//				return CommonUtil.getRetVal("",
//						"PatientId,ClinicCard,Name,Mobile,McardNo,Address,Sex,Fee,BirthDay,Country,Nation,BloodCode1,BloodCode2,Allergens,IdCardId",
//						hiQ);
//			} else {
//				return CommonUtil.getRetVal("", RetCode.Basic.ERROR_CANNOTEXIST);
//			}
//		} catch (AbsHosException e) {
//			LogUtil.error(log, e);
//			e.printStackTrace();
//			return CommonUtil.getRetVal(map.get("transactionCode"), RetCode.Common.ERROR_PARAM);
//		}
//
//	}

	@Override
	public String QueryHospitalUserInfo(InterfaceMessage msg) throws Exception {
			CommonReq<ReqQueryHospitalUserInfo> reqComm = new CommonReq<ReqQueryHospitalUserInfo>(new ReqQueryHospitalUserInfo(msg));
			CommonResp<RespQueryHospitalUserInfo>  commResp = this.queryHospitalUserInfo(reqComm);
			return commResp.toResult();
	}

	/**
	 * 1.根据入参调用HIS查询账户余额 2.返回查询到的余额
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryCardBalance(InterfaceMessage msg) throws Exception{
			// 调用HIS接口查询
			CommonReq<ReqQueryCardBalance> reqComm = new CommonReq<ReqQueryCardBalance>(new ReqQueryCardBalance(msg));
			CommonResp<RespQueryCardBalance> respComm =  this.queryCardBalance(reqComm);
			return respComm.toResult();
	}

//	/**
//	 * 检验住院号是否已解绑,如HIS那边已解绑,则同步本地
//	 * 
//	 * @param msg
//	 */
//	@Override
//	public String CheckHospitalNo(InterfaceMessage msg) throws Exception{
//			CommonReq<ReqCheckHospitalNo> commReq = new CommonReq<ReqCheckHospitalNo>(new ReqCheckHospitalNo(msg));
//			CommonResp<RespMap> commResp = this.checkHospitalNo(commReq);
//			return commResp.toResult();
//	}

	/**
	 * 查询HIS基础科室
	 */
	@Override
	public String QueryBaseDept(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryBaseDept> commReq = new CommonReq<ReqQueryBaseDept>(new ReqQueryBaseDept(msg));
		CommonResp<RespQueryBaseDept> commResp = this.queryBaseDept(commReq);
		return commResp.toResult();
	}

	
	
	
	/**
	 * 查询HIS基础医生
	 */
	@Override
	public String QueryBaseDoctor(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryBaseDoctor> commReq = new CommonReq<ReqQueryBaseDoctor>(new ReqQueryBaseDoctor(msg));
			CommonResp<RespQueryBaseDoctor>  commResp = this.queryBaseDoctor(commReq);
			return commResp.toResult();
	}

	/**
	 * 查询HIS患者信息、全流程平台患者信息 入参OpenId为空时，查询HIS接口 入参OpenId非空时，查询本地member记录
	 * 
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:22:03
	 */
	@Override
	public String QueryPatientInfo(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryPatientInfo> commReq = new CommonReq<ReqQueryPatientInfo>(new ReqQueryPatientInfo(msg));
			CommonResp<RespQueryPatientInfo> commResp = this.queryPatientInfo(commReq);
			return commResp.toResult();
	}
	
	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryDictionaryInfo(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String AddDictionary(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String DelDictionary(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String UpdateDictionary(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryArticleInfoForList(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String UpdateHospital(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String UpdateDoctor(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String UpdateDept(InterfaceMessage msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取验证码
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:25:08
	 */
	@Override
	public String GetProvingCode(InterfaceMessage msg) throws Exception{
		return this.getProvingCode(new CommonReq<ReqGetProvingCode>(new ReqGetProvingCode(msg))).toResult();
	}


	@Override
	public CommonResp<RespGetprovingCode> getProvingCode(CommonReq<ReqGetProvingCode> reqComm) throws Exception {
		ReqGetProvingCode req = reqComm.getParam();
		String code = StringUtil.getRandomNumber(100000, 999999) + "";
		
		JSONObject smsJson=new JSONObject();
        smsJson.put("code",code);
     
		ReqSendSms reqSendSms = new ReqSendSms(req.getMsg(),
				req.getMobile(), smsJson.toString(), req.getClientId(), null, null,
				null, null, smsJson.toString());
		CommonResp<RespMap> sendResp = msgService.sendSms(new CommonReq<ReqSendSms>(reqSendSms));
		if(!KstHosConstant.SUCCESSCODE.equals(sendResp.getCode())) {
			return new CommonResp<RespGetprovingCode>(reqComm,KstHosConstant.DEFAULTTRAN,RetCode.Basic.ERROR_SENDMSG,sendResp.getMessage());
		}
		ReqSaveProvingCode saveReq = new ReqSaveProvingCode(req.getMsg(), code, req.getMobile());
		return porvingCodeService.saveProvingCode(new CommonReq<ReqSaveProvingCode>(saveReq));
	}

	/**
	 * 设置默认就诊人 1.查询现有的有效默认诊人 2.将查到的默认就诊人设置为非默认就诊人 3.将请求的就诊人设置为默认就诊人 4.返回操作信息
	 * 设置非默认就诊人 1.将请求的就诊人设置为非默认就诊人 2.返回操作信息
	 * 
	 * @param msg
	 * @return
	 */
	@Transactional
	@Override
	public String SetDefaultMember(InterfaceMessage msg) throws Exception{
		CommonReq<ReqSetDefaultMember> commReq = new CommonReq<ReqSetDefaultMember>(new ReqSetDefaultMember(msg));
		CommonResp<RespMap> commResp = this.setDefaultMember(commReq);
		return commResp.toResult();
	}

	/**
	 * 查询医生职称
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:25:34
	 */
	@Override
	public String QueryTitleInfo(InterfaceMessage msg) throws Exception{
			CommonReq<ReqQueryTitleInfo> commReq = new CommonReq<ReqQueryTitleInfo>(new ReqQueryTitleInfo(msg));
			CommonResp<RespQueryTitleInfo> commResp = this.queryTitleInfo(commReq);
			return commResp.toResult();
	}

	
	
	
	/**
	 * 查询就诊人列表
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:26:09
	 */
	@Override
	public String QueryMemberList(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryMemberList> commReq = new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(msg));
		CommonResp<RespQueryMemberList> commResp = this.queryMemberList(commReq);
		return commResp.toResult();
	}

	/**
	 * 删除就诊人
	 *
	 * @param msg
	 * @return
	 * @author 無
	 * @date 2018年4月24日 下午5:26:27
	 */
	@Override
	public String DelMemberInfo(InterfaceMessage msg) throws Exception{
		CommonReq<ReqDelMemberInfo> commReq = new CommonReq<ReqDelMemberInfo>(new ReqDelMemberInfo(msg));
		CommonResp<RespMap> commResp = this.delMemberInfo(commReq);
		return commResp.toResult();
	}

	
	@Override
	public CommonResp<RespQueryBaseDept> queryBaseDept(CommonReq<ReqQueryBaseDept> reqComm) throws Exception{
		ReqQueryBaseDept req = reqComm.getParam();
		if (StringUtil.isBlank(req.getHosId())) {
			return new CommonResp<RespQueryBaseDept>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数hosId不能为空。");
		}
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put(ApiKey.HisQueryClinicDoctor.hosId.getName(), req.getHosId());
		List<RespQueryBaseDept> respList = new Vector<RespQueryBaseDept>();
		CommonResp<HisQueryBaseDept> hisResp = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo())
				.queryBaseDept(req.getMsg(),paramMap);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<HisQueryBaseDept> depts = hisResp.getListCaseRetCode();
			if (depts != null && !depts.isEmpty()) {
				for (HisQueryBaseDept dept : depts) {
					RespQueryBaseDept resp = new RespQueryBaseDept();
					resp.setAddress(dept.getDeptAddr());
					resp.setDeptCode(dept.getDeptCode());
					resp.setDeptName(dept.getDeptName());
					resp.setIntro(dept.getDeptBrief());
					resp.setParentId(dept.getParentDeptCode());
					resp.setRemark(dept.getRemark());
					respList.add(resp);
				}
				return new CommonResp<RespQueryBaseDept>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList);
			}else {
				return new CommonResp<RespQueryBaseDept>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
			}
		}else {
			return new CommonResp<RespQueryBaseDept>(reqComm, KstHosConstant.DEFAULTTRAN, hisResp.getRetCode(), hisResp.getMessage());
		}
	}
	@Override
	public CommonResp<RespQueryBaseDoctor> queryBaseDoctor(CommonReq<ReqQueryBaseDoctor> reqComm) throws Exception{
		ReqQueryBaseDoctor req = reqComm.getParam();
		if (StringUtil.isBlank(req.getHosId())) {
			return new CommonResp<RespQueryBaseDoctor>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数hosId不能为空。");
		}
		if (StringUtil.isBlank(req.getDeptCode())) {
			return new CommonResp<RespQueryBaseDoctor>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数dptCode不能为空。");
		}
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put(ApiKey.HisQueryClinicDoctor.hosId.getName(), req.getHosId());
		paramMap.put(ApiKey.HisQueryClinicDoctor.deptCode.getName(), req.getDeptCode());
		CommonResp<HisQueryBaseDoctor> hisResp = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo())
				.queryBaseDoctor(req.getMsg(),paramMap);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<HisQueryBaseDoctor> doctors = hisResp.getListCaseRetCode();
			List<RespQueryBaseDoctor> respList = new Vector<RespQueryBaseDoctor>();
			if (doctors != null && !doctors.isEmpty()) {
				for (HisQueryBaseDoctor doctor : doctors) {
					RespQueryBaseDoctor resp = new RespQueryBaseDoctor();
					resp.setDeptCode(doctor.getDeptCode());
					resp.setDeptName(doctor.getDeptName());
					resp.setDoctorCode(doctor.getDoctorCode());
					resp.setDoctorName(doctor.getDoctorName());
					resp.setDoctorTitle(doctor.getTitle());
					resp.setDoctorTitleCode(doctor.getTitleCode());
					resp.setIntro(doctor.getIntroduction());
					resp.setLevel(doctor.getLevelName());
					resp.setLevelId(doctor.getLevelId());
					resp.setPhotoUrl(doctor.getPhotoUrl());
					resp.setPrice(new Integer(doctor.getPrice()));
					resp.setRemark(doctor.getRemark());
					resp.setSex(doctor.getDoctorSex());
					resp.setSpec(doctor.getSpec());
					respList.add(resp);
				}
				return new CommonResp<RespQueryBaseDoctor>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList);
			}else {
				return new CommonResp<RespQueryBaseDoctor>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
			}
		}else {
			return new CommonResp<RespQueryBaseDoctor>(reqComm, KstHosConstant.DEFAULTTRAN, hisResp.getRetCode(), hisResp.getMessage());
		}
	}
	@Override
	public CommonResp<RespQueryMemberList> queryMemberInfo(CommonReq<ReqQueryMemberInfo> reqComm) throws Exception {
		ReqQueryMemberInfo req = reqComm.getParam();
		Member member = new Member();
		member.setCardType(req.getCardType());
		member.setCardNo(req.getCardNo());
		member.setOpenId(req.getOpId());
		member.setMemberId(req.getMemberId());
		member.setIsDefaultMember(req.getIsDefaultMember());
		
		Member mm = memberMapper.queryMemberInfo(member);
		if(mm==null) {
			return new CommonResp<RespQueryMemberList>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		}
		//异步验证成员卡信息是否有效
		List<Member> ll = new ArrayList<Member>();
		ll.add(mm);
		MemberAutoUnbindThread autoUnbindThread = new MemberAutoUnbindThread(req.getMsg(),ll);
		autoUnbindThread.setName("Member-auto-unbind-thread-openId="+req.getOpId());
		KstHosConstant.cachedThreadPool.execute(autoUnbindThread);
		MemberAutoUnbindCache.me().put(req.getOpId(), "1");
		
		//返回参数处理
		RespQueryMemberList resp = new RespQueryMemberList();
		resp.setAddress(mm.getAddress());
		resp.setBirthDate(mm.getBirthDate());
		resp.setCardNo(mm.getCardNo());
		resp.setCardType(mm.getCardType());
		resp.setCardTypeName(mm.getCardTypeName());
		resp.setChannelId(mm.getChannelId());
		resp.setHospitalNo(mm.getHospitalNo());
		resp.setIsChildren(mm.getIsChildren());
		resp.setIsDefaultMember(mm.getIsDefaultMember());
		resp.setIsDefault(mm.getIsDefault());
//		resp.setBlance(mm.getBalance()!=null?String.valueOf(mm.getBalance()):"");
//		resp.setBirthNumber(mm.getBirthNumber());
//		resp.setMcardNo(mm.getMcardNo());
		resp.setMemberId(mm.getMemberId());
		resp.setMemberName(mm.getMemberName());
		resp.setOpId(mm.getOpenId());
		resp.setSex(mm.getSex());
		resp.setHisMemberId(mm.getHisMemberId());
		if(req.isReturnAllInfo()) {
			resp.setIdCardNo((mm.getIdCardNo()));
			resp.setMobile((mm.getMobile()));
			resp.setCertNum(mm.getCertNum());
			resp.setGuardianCertNum(mm.getGuardianCertNum());
		}else {
			if(mm.getCertType()!=null && "01".equals(mm.getCertType()) && StringUtil.isNotBlank(mm.getCertNum())) {
				resp.setCertNum(FormatUtils.idCardFormat(mm.getCertNum()));
			}else {
				resp.setCertNum(mm.getCertNum());
			}
			if(mm.getGuardianCertType()!=null && "01".equals(mm.getGuardianCertType()) && StringUtil.isNotBlank(mm.getGuardianCertNum())) {
				resp.setGuardianCertNum(FormatUtils.idCardFormat(mm.getGuardianCertNum()));
			}else {
				resp.setGuardianCertNum(mm.getGuardianCertNum());
			}
			resp.setIdCardNo(FormatUtils.idCardFormat(mm.getIdCardNo()));
			resp.setMobile(FormatUtils.mobileFormat(mm.getMobile()));
		}
		resp.setCertType(mm.getCertType());
		resp.setGuardianName(mm.getGuardianName());
		resp.setGuardianCertType(mm.getGuardianCertType());
		resp.setGuardianSex(mm.getGuardianSex());
		return new CommonResp<>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}


	@Override
	public CommonResp<RespQueryMemberList> queryMemberList(CommonReq<ReqQueryMemberList> reqComm) throws Exception{
		ReqQueryMemberList req = reqComm.getParam();
		Member member = new Member();
		member.setCardNo(req.getCardNo());
		member.setCardType(req.getCardType());
		member.setMemberId(req.getMemberId());
		member.setOpenId(req.getOpId());
		member.setIdCardNo(req.getIdCardNo());
		if(StringUtil.isBlank(req.getOpId())) {
			member.setChannelId(req.getChannelId());
		}
		member.setMemberName(req.getMemberName());
		member.setMemberNameLike(req.getMemberNameLike());
		member.setIsDefaultMember(req.getIsDefaultMember());
		//直接从配置参数获取hosid
//		member.setHosId(req.getHosId());
		member.setHospitalNo(req.getHospitalNo());
		member.setHisMemberId(req.getHisMemberId());
		List<Member> list = null;
		//有传入openId时，异步调用HIS接口自动较验，不存在的自动解绑
		if(StringUtil.isNotBlank(req.getOpId()) && !MemberAutoUnbindCache.me().hasKey(req.getOpId())) {
			list = memberMapper.queryMemberList(member);
			if(list!=null && list.size()>0) {
				MemberAutoUnbindThread autoUnbindThread = new MemberAutoUnbindThread(req.getMsg(),list);
				autoUnbindThread.setName("Member-auto-unbind-thread-openId="+req.getOpId());
				KstHosConstant.cachedThreadPool.execute(autoUnbindThread);
				MemberAutoUnbindCache.me().put(req.getOpId(), "1");
			}
		}
		
		if(req.getPage()!=null && req.getPage().getPIndex()!=null && req.getPage().getPSize()>0) {
			PageHelper.startPage(req.getPage().getPIndex()+1, req.getPage().getPSize());
			list = memberMapper.queryUserMemberList(member);
			req.getPage().initPCount(list);
		}else {
			//默认最多查询200
			PageHelper.startPage(1, 200);
			list = memberMapper.queryUserMemberList(member);
		}
		
		List<RespQueryMemberList> respList =  new ArrayList<>();
		for (Member mm : list) {
			RespQueryMemberList resp = new RespQueryMemberList();
			resp.setAddress(mm.getAddress());
			resp.setBirthDate(mm.getBirthDate());
			resp.setCardNo(mm.getCardNo());
			resp.setCardType(mm.getCardType());
			resp.setCardTypeName(mm.getCardTypeName());
			resp.setChannelId(mm.getChannelId());
			resp.setHospitalNo(mm.getHospitalNo());
			resp.setIsChildren(mm.getIsChildren());
			resp.setIsDefaultMember(mm.getIsDefaultMember());
			resp.setIsDefault(mm.getIsDefault());
//			resp.setBlance(mm.getBalance()!=null?String.valueOf(mm.getBalance()):"");
//			resp.setBirthNumber(mm.getBirthNumber());
//			resp.setMcardNo(mm.getMcardNo());
			resp.setMemberId(mm.getMemberId());
			resp.setMemberName(mm.getMemberName());
			resp.setOpId(mm.getOpenId());
			resp.setSex(mm.getSex());
			resp.setHisMemberId(mm.getHisMemberId());
			if(req.isReturnAllInfo()) {
				resp.setIdCardNo((mm.getIdCardNo()));
				resp.setMobile((mm.getMobile()));
				resp.setCertNum(mm.getCertNum());
				resp.setGuardianCertNum(mm.getGuardianCertNum());
			}else {
				if(mm.getCertType()!=null && "01".equals(mm.getCertType()) && StringUtil.isNotBlank(mm.getCertNum())) {
					resp.setCertNum(FormatUtils.idCardFormat(mm.getCertNum()));
				}else {
					resp.setCertNum(mm.getCertNum());
				}
				if(mm.getGuardianCertType()!=null && "01".equals(mm.getGuardianCertType()) && StringUtil.isNotBlank(mm.getGuardianCertNum())) {
					resp.setGuardianCertNum(FormatUtils.idCardFormat(mm.getGuardianCertNum()));
				}else {
					resp.setGuardianCertNum(mm.getGuardianCertNum());
				}
				resp.setIdCardNo(FormatUtils.idCardFormat(mm.getIdCardNo()));
				resp.setMobile(FormatUtils.mobileFormat(mm.getMobile()));
			}
			resp.setCertType(mm.getCertType());
			resp.setGuardianName(mm.getGuardianName());
			resp.setGuardianCertType(mm.getGuardianCertType());
			resp.setGuardianSex(mm.getGuardianSex());
			
			//查询成员对应的卡信息
			Map<String, Object> query = new HashMap<>();
			query.put("openId", mm.getOpenId());
			query.put("memberId", mm.getMemberId());
			if(StringUtil.isNotBlank(req.getCardType())) {
				query.put("cardType", req.getCardType());
			}
			if(StringUtil.isNotBlank(req.getCardNo())) {
				query.put("cardNo", req.getCardNo());
			}
			List<UserMemberCard> ll = userMemberCardMapper.queryUserMemberCardList(query);
			
			List<RespCardPackage> cardList = new ArrayList<>();
			
			if(ll!=null && ll.size()>0) {
				for (int i=0;i<ll.size();i++) {
					UserMemberCard umc = ll.get(i);
					RespCardPackage cardPkg = new RespCardPackage();
					cardPkg.setCardType(umc.getCardType());
					cardPkg.setCardNo(umc.getCardNo());
					cardPkg.setCardTypeName(umc.getCardTypeName());
					cardPkg.setIsDefault(umc.getIsDefault());
					cardPkg.setHisMemberId(umc.getHisMemberId());
					cardPkg.setCreateTime(DateOper.formatDate(umc.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					cardPkg.setUpdateTime(DateOper.formatDate(umc.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
					cardList.add(cardPkg);
					
					//住院号不为空返回住院号信息
					if(KstHosConstant.CARDTYPE_14.equals(umc.getCardType())) {
						resp.setHospitalNo(umc.getCardNo());
					}
					
					if(StringUtil.isNotBlank(req.getCardNo()) && StringUtil.isNotBlank(req.getCardType())) {
						//有明确查询的卡信息
						resp.setCardNo(umc.getCardNo());
						resp.setCardType(umc.getCardType());
						resp.setIsDefault(umc.getIsDefault());
						resp.setCardTypeName(umc.getCardTypeName());
						resp.setHisMemberId(umc.getHisMemberId());
					}else {
						//有明确需要查询的卡类型时，将对应卡类型返回
						if(StringUtil.isNotBlank(req.getCardType())) {
							if(KstHosConstant.CARDTYPE_1.equals(req.getCardType()) 
									&& StringUtil.isNotBlank(umc.getCardType()) 
									&& req.getCardType().equals(umc.getCardType())) {
								//如果查询的是就诊卡，默认返回第一张卡
								if(i==0 && StringUtil.isNotBlank(umc.getCardNo())) {
									resp.setCardNo(umc.getCardNo());
									resp.setHisMemberId(umc.getHisMemberId());
									resp.setIsDefault(umc.getIsDefault());
								}
								//存在默认就诊卡时返回默认就诊卡
								if(umc.getIsDefault()!=null && umc.getIsDefault()==1) {
									resp.setCardNo(umc.getCardNo());
									resp.setHisMemberId(umc.getHisMemberId());
									resp.setIsDefault(umc.getIsDefault());
								}
								resp.setCardType(umc.getCardType());
								resp.setCardTypeName(umc.getCardTypeName());
							}else if(StringUtil.isNotBlank(umc.getCardType()) && umc.getCardType().equals(req.getCardType())){
								//其他卡类型直接返回
								resp.setCardNo(umc.getCardNo());
								resp.setCardType(umc.getCardType());
								resp.setCardTypeName(umc.getCardTypeName());
								resp.setHisMemberId(umc.getHisMemberId());
								resp.setIsDefault(umc.getIsDefault());
							}
						}else {
							//没有明确查询的卡类型时，默认返回默认就诊卡信息
							if(StringUtil.isNotBlank(umc.getCardType()) && 
									KstHosConstant.CARDTYPE_1.equals(umc.getCardType())) {
								//没有默认就诊卡时，默认取查询到的第一张
								if(i==0 && StringUtil.isNotBlank(umc.getCardNo())) {
									resp.setCardNo(umc.getCardNo());
									resp.setHisMemberId(umc.getHisMemberId());
									resp.setIsDefault(umc.getIsDefault());
								}
								//有默认就诊卡时
								if(umc.getIsDefault()!=null && umc.getIsDefault()==1) {
									resp.setCardNo(umc.getCardNo());
									resp.setHisMemberId(umc.getHisMemberId());
									resp.setIsDefault(umc.getIsDefault());
								}
								resp.setCardType(umc.getCardType());
								resp.setCardTypeName(umc.getCardTypeName());
							}
						}
					}
				}
			}
			resp.setData_1(cardList);
			respList.add(resp);
		}
		return new CommonResp<>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
	}
	@Override
	public String QueryMemberCardList(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryMemberList> commReq = new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(msg));
		CommonResp<RespQueryMemberList> commResp = this.queryMemberCardList(commReq);
		return commResp.toResult();
	}
	
	@Override
	public CommonResp<RespQueryMemberList> queryMemberCardList(CommonReq<ReqQueryMemberList> reqComm) throws Exception {
		ReqQueryMemberList req = reqComm.getParam();
		Member member = new Member();
		member.setCardNo(req.getCardNo());
		member.setCardType(req.getCardType());
		member.setMemberId(req.getMemberId());
		member.setOpenId(req.getOpId());
		member.setIdCardNo(req.getIdCardNo());
		if(StringUtil.isBlank(req.getOpId())) {
			member.setChannelId(req.getChannelId());
		}
		member.setMemberName(req.getMemberName());
		member.setMemberNameLike(req.getMemberNameLike());
		member.setIsDefaultMember(req.getIsDefaultMember());
		//直接从配置参数获取hosid
//		member.setHosId(req.getHosId());
		member.setHospitalNo(req.getHospitalNo());
		List<Member> list = null;
		
		if(StringUtil.isNotBlank(req.getCardType()) && KstHosConstant.CARDTYPE_1.equals(req.getCardType()) && StringUtil.isBlank(req.getCardNo())) {
			//查询就诊卡  且没有传入卡号时，需要返回无卡的数据，先查询就诊人，在查询就诊人对应的卡信息
			return this.queryMemberList(reqComm);
		}else {
			if(req.getPage()!=null && req.getPage().getPIndex()!=null && req.getPage().getPSize()>0) {
				PageHelper.startPage(req.getPage().getPIndex()+1, req.getPage().getPSize());
				list = memberMapper.queryMemberList(member);
				req.getPage().initPCount(list);
			}else {
				//默认最多查询200
				PageHelper.startPage(1, 200);
				list = memberMapper.queryMemberList(member);
			}
			//有传入openId时，异步调用HIS接口自动较验，不存在的自动解绑
			if(StringUtil.isNotBlank(req.getOpId()) && !MemberAutoUnbindCache.me().hasKey(req.getOpId())) {
				if(list!=null && list.size()>0) {
					MemberAutoUnbindThread autoUnbindThread = new MemberAutoUnbindThread(req.getMsg(),list);
					autoUnbindThread.setName("Member-auto-unbind-thread-openId="+req.getOpId());
					KstHosConstant.cachedThreadPool.execute(autoUnbindThread);
					MemberAutoUnbindCache.me().put(req.getOpId(), "1");
				}
			}
		}
		
		List<RespQueryMemberList> respList =  new ArrayList<>();
		for (Member mm : list) {
			RespQueryMemberList resp = new RespQueryMemberList();
			resp.setAddress(mm.getAddress());
			resp.setBirthDate(mm.getBirthDate());
			resp.setCardNo(mm.getCardNo());
			resp.setCardType(mm.getCardType());
			resp.setCardTypeName(mm.getCardTypeName());
			resp.setChannelId(mm.getChannelId());
			resp.setHospitalNo(mm.getHospitalNo());
			resp.setIsChildren(mm.getIsChildren());
			resp.setIsDefaultMember(mm.getIsDefaultMember());
			resp.setIsDefault(mm.getIsDefault());
//			resp.setBlance(mm.getBalance()!=null?String.valueOf(mm.getBalance()):"");
//			resp.setBirthNumber(mm.getBirthNumber());
//			resp.setMcardNo(mm.getMcardNo());
			resp.setMemberId(mm.getMemberId());
			resp.setMemberName(mm.getMemberName());
			resp.setOpId(mm.getOpenId());
			resp.setSex(mm.getSex());
			resp.setHisMemberId(mm.getHisMemberId());
			if(req.isReturnAllInfo()) {
				resp.setIdCardNo((mm.getIdCardNo()));
				resp.setMobile((mm.getMobile()));
				resp.setCertNum(mm.getCertNum());
				resp.setGuardianCertNum(mm.getGuardianCertNum());
			}else {
				if(mm.getCertType()!=null && "01".equals(mm.getCertType()) && StringUtil.isNotBlank(mm.getCertNum())) {
					resp.setCertNum(FormatUtils.idCardFormat(mm.getCertNum()));
				}else {
					resp.setCertNum(mm.getCertNum());
				}
				if(mm.getGuardianCertType()!=null && "01".equals(mm.getGuardianCertType()) && StringUtil.isNotBlank(mm.getGuardianCertNum())) {
					resp.setGuardianCertNum(FormatUtils.idCardFormat(mm.getGuardianCertNum()));
				}else {
					resp.setGuardianCertNum(mm.getGuardianCertNum());
				}
				resp.setIdCardNo(FormatUtils.idCardFormat(mm.getIdCardNo()));
				resp.setMobile(FormatUtils.mobileFormat(mm.getMobile()));
			}
			resp.setCertType(mm.getCertType());
			resp.setGuardianName(mm.getGuardianName());
			resp.setGuardianCertType(mm.getGuardianCertType());
			resp.setGuardianSex(mm.getGuardianSex());
			respList.add(resp);
		}
		return new CommonResp<>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
	}

	/**
	 * 普通版新增就诊人
	 */
	@Transactional
	@Override
	public String AddMember(InterfaceMessage msg) throws Exception{
			CommonReq<ReqAddMember> reqComm = new CommonReq<ReqAddMember>(new ReqAddMember(msg));
			CommonResp<RespMap> respComm = this.addMember(reqComm);
			return respComm.toResult();
	}

	@Override
	public CommonResp<RespMap> addMember(CommonReq<ReqAddMember> reqComm) throws Exception{
		
			return addMemberAndCardInfo(reqComm);
	}

	
	
	@Override
	public CommonResp<RespMap> addMemberAndCardInfo(CommonReq<ReqAddMember> reqComm) throws Exception {
		
		ReqAddMember req = reqComm.getParam();
		
		//判断成员信息是否已经存在，已经存在的直接返回成功
		//根据Openid 查询所有成员，判断成员信息是否已经存在，已经存在的直接返回成功
		CommonResp<RespQueryMemberList> commResp = this.queryMemberList(new CommonReq<ReqQueryMemberList>(
				new ReqQueryMemberList(req.getMsg(), null, null, null, req.getOpId(), 
						null, null, req.getMemberName(), req.getIdCardNo(), false)));
		if(!KstHosConstant.SUCCESSCODE.equals(commResp.getCode())) {
			return new CommonResp<>(reqComm, RetCode.Basic.ERROR_EXECUTESQL, "查询成员信息异常。");
		}
		if(commResp.getData()!=null && commResp.getData().size()>0) {
			List<RespQueryMemberList> ll = commResp.getData();
			boolean isExistCard = false;//是否存在就诊卡
			boolean isExistInHosNo = false;//是否存在住院号
			if(StringUtil.isNotBlank(req.getCardNo()) && StringUtil.isNotBlank(req.getHospitalNo())) {
				//同时绑定就诊卡和住院号时
				for (RespQueryMemberList mm : ll) {
					List<RespCardPackage>  cardList = mm.getData_1();
					if(cardList!=null && cardList.size()>0) {
						for (RespCardPackage card : cardList) {
							if(req.getCardType().equals(card.getCardType())
									&& req.getCardNo().equals(card.getCardNo())) {
								isExistCard = true;
							}
							if(KstHosConstant.CARDTYPE_14.equals(card.getCardType())
									&& req.getHospitalNo().equals(card.getCardNo())) {
								isExistInHosNo = true;
							}
						}
					}
				}
				if(isExistCard && isExistInHosNo) {
					RespQueryMemberList member = ll.get(0);
					return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
							RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, member.getMemberId())); 
				}
			}else if(StringUtil.isNotBlank(req.getCardNo())){
				//仅仅绑定就诊卡
				for (RespQueryMemberList mm : ll) {
					List<RespCardPackage>  cardList = mm.getData_1();
					if(cardList!=null && cardList.size()>0) {
						for (RespCardPackage card : cardList) {
							if(req.getCardType().equals(card.getCardType())
									&& req.getCardNo().equals(card.getCardNo())) {
								isExistCard = true;
							}
						}
					}
				}
				if(isExistCard) {
					RespQueryMemberList member = ll.get(0);
					return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
							RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, member.getMemberId())); 
				}
			}else if(StringUtil.isNotBlank(req.getHospitalNo())) {
				//仅仅绑定住院号
				for (RespQueryMemberList mm : ll) {
					List<RespCardPackage>  cardList = mm.getData_1();
					if(cardList!=null && cardList.size()>0) {
						for (RespCardPackage card : cardList) {
							if(KstHosConstant.CARDTYPE_14.equals(card.getCardType())
									&& req.getHospitalNo().equals(card.getCardNo())) {
								isExistInHosNo = true;
							}
						}
					}
				}
				if(isExistInHosNo) {
					RespQueryMemberList member = ll.get(0);
					return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
							RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, member.getMemberId())); 
				}
			}else {
				//仅新增就诊人
				RespQueryMemberList member = ll.get(0);
				return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
						RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, member.getMemberId())); 
			}
		}else {
			BatUser query = new BatUser();
			query.setOpenId(req.getOpId());
			int num = userMapper.selectCount(query);
			if(num<=0) {
				//不存在渠道用户信息时，新增渠道用户
				BatUser user = new BatUser();
				user.setOpenId(req.getOpId());
				user.setChannelId(req.getClientId());
				user.setOperatorId(req.getOpId());
				user.setOperatorName(req.getOpId());
				user.setId(CommonUtil.getGUID());
				user.setConfigKey(req.getConfigKey());
				userMapper.insertSelective(user);
			}
		}
		
		//验证是否超过绑定人数限制
		UserMember queryUserMember = new UserMember();
		queryUserMember.setOpenId(req.getOpId());
		List<UserMember> userMemberList = userMemberMapper.select(queryUserMember);
		if(userMemberList!=null && userMemberList.size()>=5) {
			return new CommonResp<>(reqComm, RetCode.Basic.ERROR_ADDUSERLIMIT,"已达到绑定限制，每个用户只能绑定5个就诊人信息。");
		}
		if (StringUtils.isNotBlank(req.getCode()) && KasiteConfig.isCheckPorvingCode()) {
			//较验验证码
			ReqCheckPorvingCode checkReq = new ReqCheckPorvingCode(req.getMsg(),req.getpCId(),req.getMobile(),req.getCode());
			CommonReq<ReqCheckPorvingCode> checkReqComm = new CommonReq<ReqCheckPorvingCode>(checkReq);
			CommonResp<RespMap> checkResp = porvingCodeService.checkPorvingCode(checkReqComm);
			if (!KstHosConstant.SUCCESSCODE.equals(checkResp.getCode())) {
				// 验证码错误
				return checkResp;
			}
		}
		
		//判断是否有实现 HIS创建用户信息的接口 如果有则调用该接口进行建档 
		boolean isAddHisUser = false;
		IAddMemberService addMemberService = HandlerBuilder.get().getCallHisService(req.getAuthInfo(), IAddMemberService.class);
		if(null != addMemberService && StringUtil.isBlank(req.getCardNo()) && StringUtil.isBlank(req.getHospitalNo())) {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put(ApiKey.HisAddMember.idCardNo.name(), req.getIdCardNo());
			paramMap.put(ApiKey.HisAddMember.address.name(), req.getAddress());
			paramMap.put(ApiKey.HisAddMember.certNum.name(), req.getCertNum());
			paramMap.put(ApiKey.HisAddMember.certType.name(), req.getCertType());
			paramMap.put(ApiKey.HisAddMember.mobile.name(), req.getMobile());
			paramMap.put(ApiKey.HisAddMember.patientName.name(), req.getMemberName());
			paramMap.put(ApiKey.HisAddMember.sex.name(), req.getSex()+"");
			paramMap.put(ApiKey.HisAddMember.isChildren.name(), req.getIsChildren()==null?"0":req.getIsChildren().toString());
			paramMap.put(ApiKey.HisAddMember.guardianName.name(), req.getGuardianName());
			CommonResp<HisAddMember> addMemberResp = addMemberService.addMemberService(req.getMsg(), paramMap);
			if(null != addMemberResp) {//如果新增成功则保存用户信息
				HisAddMember hisMember = addMemberResp.getDataCaseRetCode();
				String cardNo = hisMember.getCardNo();
				req.setCardNo(cardNo);
				req.setCardType(hisMember.getCardType());
				req.setHisMemberId(hisMember.getHisMemberId());
				isAddHisUser = true;
			}
		}
		
		//获取成员唯一编码  规则：成员名称+“_”+证件类型+“_”+证件号码（儿童无证件类型时，使用证件监护人的证件类型及号码）
		String memberCode = getMemberCode(req.getMemberName(), req.getIsChildren(), 
				req.getCertType(), req.getCertNum(), req.getGuardianCertType(),
				req.getGuardianCertNum());
		
		//根据成员唯一编码查询成员信息
		Example baseExample = new Example(MemberBase.class);
		baseExample.createCriteria()
		.andEqualTo("memberCode", memberCode);
		MemberBase memberBase = memberBaseMapper.selectOneByExample(baseExample);
		
		//是否默认就诊人
		int isDefaultMember = 1;
		
		//成员主键ID
		String memberId = null;
		if(memberBase==null) {
			//不存在成员信息时，表示是新的成员，直接新增成员及成员关系信息
			memberId = CommonUtil.getUUID();
			
			//遍历验证渠道用户是否存在默认就诊人
			for(UserMember um : userMemberList) {
				if(um.getIsDefaultMember()!=null && um.getIsDefaultMember()==1) {
					isDefaultMember = 0;
					break;
				}
			}
			
			MemberBase addMemBase = new MemberBase();
			addMemBase.setMemberId(memberId);
			addMemBase.setMemberCode(memberCode);
			addMemBase.setMemberName(req.getMemberName());
			addMemBase.setSex(req.getSex());
			addMemBase.setMobile(req.getMobile());
			addMemBase.setIdCardNo(req.getIdCardNo());
			addMemBase.setIsChildren(req.getIsChildren());
			addMemBase.setAddress(req.getAddress());
			addMemBase.setBirthDate(req.getBirthDate());
			addMemBase.setOperatorId(req.getOpId());
			addMemBase.setOperatorName(req.getOpId());
			addMemBase.setCertType(req.getCertType());
			addMemBase.setCertNum(req.getCertNum());
			addMemBase.setGuardianCertNum(req.getGuardianCertNum());
			addMemBase.setGuardianCertType(req.getGuardianCertType());
			addMemBase.setGuardianName(req.getGuardianName());
			addMemBase.setGuardianSex(req.getGuardianSex());
			memberBaseMapper.insertSelective(addMemBase);
			
		}else {
			//已经存在成员信息时，获取成员ID
			memberId = memberBase.getMemberId();
			
			//更新成员信息
			MemberBase upMemBase = new MemberBase();
			upMemBase.setMemberId(memberId);
			upMemBase.setMemberName(req.getMemberName());
			upMemBase.setMobile(req.getMobile());
			upMemBase.setSex(req.getSex());
			upMemBase.setIdCardNo(req.getIdCardNo());
			upMemBase.setIsChildren(req.getIsChildren());
			upMemBase.setAddress(req.getAddress());
			upMemBase.setBirthDate(req.getBirthDate());
			upMemBase.setCertType(req.getCertType());
			upMemBase.setCertNum(req.getCertNum());
			upMemBase.setGuardianCertNum(req.getGuardianCertNum());
			upMemBase.setGuardianCertType(req.getGuardianCertType());
			upMemBase.setGuardianName(req.getGuardianName());
			upMemBase.setGuardianSex(req.getGuardianSex());
			upMemBase.setOperatorId(req.getOpId());
			upMemBase.setOperatorName(req.getOpId());
			memberBaseMapper.updateByPrimaryKeySelective(upMemBase);
			
			//遍历验证渠道用户是否存在默认就诊人
			for(UserMember um : userMemberList) {
				if(um.getIsDefaultMember()!=null && um.getIsDefaultMember()==1) {
					isDefaultMember = 0;
					break;
				}
			}
		}
		
		//验证是否存在渠道用户和成员关系
		UserMember query = new UserMember();
		query.setOpenId(req.getOpId());
		query.setMemberId(memberId);
		int num = userMemberMapper.selectCount(query);
		if(num<=0) {
			//不存在渠道用户和成员关系时，新增
			UserMember userMember = new UserMember();
			userMember.setOpenId(req.getOpId());
			userMember.setMemberId(memberId);
			userMember.setIsDefaultMember(isDefaultMember);
			userMember.setOperatorId(req.getOpId());
			userMember.setOperatorName(req.getOpId());
			userMemberMapper.insertSelective(userMember);
		}
		
		if(isAddHisUser) {
			//查询是否存在默认就诊卡信息
			int isDefault = 0;
			UserMemberCard queryUm = new UserMemberCard();
			queryUm.setMemberId(memberId);
			queryUm.setOpenId(req.getOpId());
			queryUm.setIsDefault(1);
			int umNum = userMemberCardMapper.selectCount(queryUm);
			if(umNum<=0) {
				isDefault = 1;
			}
			
			//直接调用HIS建档的用户查询卡信息是否存在，存在时，新增关联关系，不存在时新增卡信息和关联关系
			CardBag queryCard = new CardBag();
			queryCard.setCardType(req.getCardType());
			queryCard.setCardNo(req.getCardNo());
			List<CardBag> cardList = cardBagMapper.select(queryCard);
			if(cardList==null || cardList.size()<=0) {
				//新增卡
				CardBag card = new CardBag();
				card.setCardNo(req.getCardNo());
				card.setCardType(req.getCardType());
				card.setCardTypeName(req.getCardTypeName());
				card.setHosId(req.getHosId());
				card.setOperatorId(req.getOpId());
				card.setOperatorName(req.getOperatorName());
				cardBagMapper.insertSelective(card);
				
				//新增用户成员卡信息关联关系表
				UserMemberCard userMemberCard = new UserMemberCard();
				userMemberCard.setIsDefault(isDefault);
				userMemberCard.setMemberId(memberId);
				userMemberCard.setOpenId(req.getOpId());
				userMemberCard.setHosId(req.getHosId());
				userMemberCard.setCardId(card.getCardId());
				userMemberCard.setHisMemberId(req.getHisMemberId());
				userMemberCard.setOperatorId(req.getOpId());
				userMemberCard.setOperatorName(req.getOperatorName());
				userMemberCardMapper.insertSelective(userMemberCard);
			}else {
				long cardId = cardList.get(0).getCardId();
				UserMemberCard queryUserCard = new UserMemberCard();
				queryUserCard.setOpenId(req.getOpId());
				queryUserCard.setCardId(cardId);
				queryUserCard.setMemberId(memberId);
				int cc = userMemberCardMapper.selectCount(queryUserCard);
				if(cc<=0) {
					//新增用户成员卡信息关联关系表
					UserMemberCard userMemberCard = new UserMemberCard();
					userMemberCard.setIsDefault(isDefault);
					userMemberCard.setMemberId(memberId);
					userMemberCard.setOpenId(req.getOpId());
					userMemberCard.setCardId(cardId);
					userMemberCard.setHosId(req.getHosId());
					userMemberCard.setHisMemberId(req.getHisMemberId());
					userMemberCard.setOperatorId(req.getOpId());
					userMemberCard.setOperatorName(req.getOperatorName());
					userMemberCardMapper.insertSelective(userMemberCard);
				}
			}
			try {
				//清除成员对应的缓存卡信息
				MemberListCache.me().clearMemberMemberId(memberId, req.getCardType(), req.getOpId());
				MemberListCache.me().clearMemberCardNo(req.getCardNo(), req.getCardType(), req.getOpId());
			}catch (Exception e) {
				e.printStackTrace();
			}
			//写入绑卡报表数据
			reportFormsUtil.dataCollection(req.getMsg(),req.getClientId(), "", null, 3, 1, null);
			reportFormsUtil.dataCloudCollection(req.getMsg(),req.getClientId(), 102, 1, "1");
		}else {
			CommonResp<RespMap> cardResp = null;
			CommonResp<RespMap> hosNoResp = null;
			if(StringUtil.isNotBlank(req.getCardNo())) {
				//有传入卡信息时，验证卡信息，此处不传入验证码，因为新增就诊人已经检验过
				cardResp = bindClinicCard(new CommonReq<ReqBindClinicCard>(new ReqBindClinicCard(req.getMsg(),
						null, req.getCardType(), req.getCardNo(), 
						req.getCardTypeName(), memberId, null, 
						req.getMobile(), req.getIsVirtualCard(),req.getOpId())));
			}
			
			if(StringUtil.isNotBlank(req.getHospitalNo())) {
				//有传入住院号时，验证住院号信息，此处不传入验证码，因为新增就诊人已经检验过
				ReqBindHospitalNo reqvo = new ReqBindHospitalNo(req.getMsg(), 
						null, memberId, req.getMobile(), null, req.getHospitalNo(),req.getOpId());
				reqvo.setIsValidateCard(req.getIsValidateCard());
				reqvo.setHisMemberId(req.getHisMemberId());
				hosNoResp = bindHospitalNo(new CommonReq<ReqBindHospitalNo>(reqvo));
			}
			
			if(cardResp!=null && !KstHosConstant.SUCCESSCODE.equals(cardResp.getCode()) && hosNoResp!=null && !KstHosConstant.SUCCESSCODE.equals(hosNoResp.getCode())) {
				return new CommonResp<RespMap>(reqComm, RetCode.Common.ERROR_UNKNOWN, "新增就诊人成功，"+cardResp.getMessage()+","+hosNoResp.getMessage());
			}else if(cardResp!=null && !KstHosConstant.SUCCESSCODE.equals(cardResp.getCode())) {
				return cardResp;
			}else if(hosNoResp!=null && !KstHosConstant.SUCCESSCODE.equals(hosNoResp.getCode())) {
				return hosNoResp;
			}
			
		}
		return new CommonResp<RespMap>(reqComm, RetCode.Success.RET_10000,
				RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, memberId));
	}

	@Override
	public CommonResp<RespMap> validateCardInfo(CommonReq<ReqValidateCardInfo> reqComm) throws Exception {
		ReqValidateCardInfo req = reqComm.getParam();
		if(req==null || StringUtil.isBlank(req.getCardNo()) || StringUtil.isBlank(req.getCardType())) {
			return new CommonResp<>(reqComm, RetCode.Common.ERROR_PARAM, "验证卡信息失败，传入的卡号不能为空。");
		}
		if(KstHosConstant.CARDTYPE_1.equals(req.getCardType())) {
			//验证就诊卡
			Map<String, String> map = new HashMap<String, String>(16);
			map.put("cardNo", req.getCardNo());
			map.put("cardType", req.getCardType());
			map.put("cardTypeName", req.getCardTypeName());
			map.put("memberName", req.getMemberName());
			map.put("mobile", req.getMobile());
			map.put("idCardNo", req.getIdCardNo());
			map.put("memberId", req.getMemberId());
			
			// 调用HIS接口查询验证就诊卡
			CommonResp<HisQueryClinicCard> hisResp = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo()).hisQueryClinicCard(req.getMsg(),map);
			if(!KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
				return new CommonResp<>(reqComm,hisResp.getRetCode(), hisResp.getMessage());
			}
			HisQueryClinicCard cardInfo = hisResp.getResultData();
			if(cardInfo==null || !"1".equals(cardInfo.getStatus())) {
				return new CommonResp<RespMap>(reqComm,RetCode.Basic.ERROR_ADDCARD, "未查到有效的卡信息,绑卡失败");
			}
			RespMap resp = new RespMap();
			resp.put(ApiKey.ValidateCardInfo.HisMemberId, cardInfo.getHisPatientId());
			resp.put(ApiKey.ValidateCardInfo.Sex, cardInfo.getSex());
			resp.put(ApiKey.ValidateCardInfo.Age, cardInfo.getAge());
			resp.put(ApiKey.ValidateCardInfo.MemberName, cardInfo.getPatientName());
			resp.put(ApiKey.ValidateCardInfo.CardType, KstHosConstant.CARDTYPE_1);
			resp.put(ApiKey.ValidateCardInfo.IdCardNo, cardInfo.getIdCardId());
			resp.put(ApiKey.ValidateCardInfo.Mobile, cardInfo.getMobile());
			resp.put(ApiKey.ValidateCardInfo.CardNo, cardInfo.getClinicCard());
			return new CommonResp<RespMap>(reqComm, RetCode.Success.RET_10000, resp);
		}else if(KstHosConstant.CARDTYPE_14.equals(req.getCardType())) {
			//验证住院号
			Map<String, String> map = new HashMap<String, String>(16);
			map.put(ApiKey.HisQueryHospitalUserInfo.memberName.getName(), req.getMemberName());
			map.put(ApiKey.HisQueryHospitalUserInfo.mobile.getName(), req.getMobile());
			map.put(ApiKey.HisQueryHospitalUserInfo.idCardNo.getName(), req.getIdCardNo());
			map.put(ApiKey.HisQueryHospitalUserInfo.hospitalNo.getName(), req.getCardNo());
			map.put(ApiKey.HisQueryHospitalUserInfo.memberId.getName(),  req.getMemberId());
			// 调用HIS接口查询验证就诊卡
			CommonResp<HisQueryHospitalUserInfo>  hisResp = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo()).queryHospitalUserInfo(req.getMsg(), map);
			if(!KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
				return new CommonResp<>(reqComm,hisResp.getRetCode(), hisResp.getMessage());
			}
			HisQueryHospitalUserInfo cardInfo = hisResp.getResultData();
			if(cardInfo==null) {
				return new CommonResp<RespMap>(reqComm,RetCode.Basic.ERROR_BINDHOSPITALNO,"住院号绑定失败，院内未查询到该住院号信息");
			}
			RespMap resp = new RespMap();
			resp.put(ApiKey.ValidateCardInfo.HisMemberId, cardInfo.getHisMemberId());
			resp.put(ApiKey.ValidateCardInfo.Sex, cardInfo.getSex());
			resp.put(ApiKey.ValidateCardInfo.Age, cardInfo.getAge());
			resp.put(ApiKey.ValidateCardInfo.MemberName, cardInfo.getName());
			resp.put(ApiKey.ValidateCardInfo.CardType, KstHosConstant.CARDTYPE_14);
			resp.put(ApiKey.ValidateCardInfo.IdCardNo, cardInfo.getIdCardId());
			resp.put(ApiKey.ValidateCardInfo.Mobile, cardInfo.getMobile());
			resp.put(ApiKey.ValidateCardInfo.CardNo, cardInfo.getHospitalNo());
			return new CommonResp<RespMap>(reqComm, RetCode.Success.RET_10000, resp);
		}else {
			return new CommonResp<>(reqComm, RetCode.Common.ERROR_PARAM, "验证卡信息失败，未知的卡类型。");
		}
	}


//	@Override
//	public CommonResp<RespMap> addMemberNotCode(CommonReq<ReqAddMember> reqComm) throws Exception {
//		
//		ReqAddMember req = reqComm.getParam();
//		//验证是否超过可绑定的限制，且判断是否需要设置为默认就诊人 
//		boolean isDefMember = false;
//		try {
//			isDefMember = isSetDefaultMember(req.getOpId(),1);
//		}catch (RRException e) {
//			return new CommonResp<>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_ADDUSERLIMIT, e.getMessage());
//		}
//		/*
//		 * 判断HIS是否有实现 创建用户信息的接口 如果有则调用该接口进行建档 
//		 */
//		boolean isAddHisUser = false;
//		IAddMemberService addMemberService = HandlerBuilder.get().getCallHisService(req.getAuthInfo(), IAddMemberService.class);
//		if(null != addMemberService && StringUtil.isBlank(req.getCardNo()) && StringUtil.isBlank(req.getHospitalNo())) {
//			Map<String, String> paramMap = new HashMap<>();
//			paramMap.put(ApiKey.HisAddMember.idCardNo.name(), req.getIdCardNo());
//			paramMap.put(ApiKey.HisAddMember.address.name(), req.getAddress());
//			paramMap.put(ApiKey.HisAddMember.certNum.name(), req.getCertNum());
//			paramMap.put(ApiKey.HisAddMember.certType.name(), req.getCertType());
//			paramMap.put(ApiKey.HisAddMember.mobile.name(), req.getMobile());
//			paramMap.put(ApiKey.HisAddMember.patientName.name(), req.getMemberName());
//			paramMap.put(ApiKey.HisAddMember.sex.name(), req.getSex()+"");
//			CommonResp<HisAddMember> addMemberResp = addMemberService.addMemberService(req.getMsg(), paramMap);
//			if(null != addMemberResp) {//如果新增成功则保存用户信息
//				HisAddMember hisMember = addMemberResp.getDataCaseRetCode();
//				String cardNo = hisMember.getCardNo();
//				req.setCardNo(cardNo);
//				req.setCardType(hisMember.getCardType());
//				req.setHisMemberId(hisMember.getHisMemberId());
//				isAddHisUser = true;
//			}
//		}
//		
//		//成员主键ID
//		String memberId = null;
//		
//		//获取成员唯一编码  规则：成员名称+“_”+证件类型+“_”+证件号码（儿童无证件类型时，使用证件监护人的证件类型及号码）
//		String memberCode = getMemberCode(req.getMemberName(), req.getIsChildren(), 
//				req.getCertType(), req.getCertNum(), req.getGuardianCertType(),
//				req.getGuardianCertNum());
//		
//		//根据成员姓名及身份证号码查询成员信息
//		Example baseExample = new Example(MemberBase.class);
//		baseExample.createCriteria()
//		.andEqualTo("memberCode", memberCode);
//		MemberBase memberBase = memberBaseMapper.selectOneByExample(baseExample);
//		
//		if(memberBase==null) {
//			//成员信息不存在，新增成员信息
//			memberId = CommonUtil.getUUID();
//			
//			
//			MemberBase addMemBase = new MemberBase();
//			addMemBase.setMemberId(memberId);
//			addMemBase.setMemberCode(memberCode);
//			addMemBase.setMemberName(req.getMemberName());
//			addMemBase.setSex(req.getSex());
//			addMemBase.setMobile(req.getMobile());
//			addMemBase.setIdCardNo(req.getIdCardNo());
//			addMemBase.setIsChildren(req.getIsChildren());
//			addMemBase.setAddress(req.getAddress());
//			addMemBase.setBirthDate(req.getBirthDate());
//			addMemBase.setOperatorId(req.getOpId());
//			addMemBase.setOperatorName(req.getOpId());
//			addMemBase.setCertType(req.getCertType());
//			addMemBase.setCertNum(req.getCertNum());
//			addMemBase.setGuardianCertNum(req.getGuardianCertNum());
//			addMemBase.setGuardianCertType(req.getGuardianCertType());
//			addMemBase.setGuardianName(req.getGuardianName());
//			addMemBase.setGuardianSex(req.getGuardianSex());
//			if(isDefMember) {
//				addMemBase.setIsDefaultMember(1);
//			}
//			memberBaseMapper.insertSelective(addMemBase);
//			
//			//新增渠道成员关系
//			UserMember userMember = new UserMember();
//			userMember.setMemberId(memberId);
//			userMember.setOpenId(req.getOpId());
//			userMemberMapper.insertSelective(userMember);
//			
//			//新增患者信息
//			addPatient(isAddHisUser, memberId, req);
//			
//		}else {
//			//成员信息已经存在
//			memberId = memberBase.getMemberId();
//			
//			//查询成员关系
//			UserMember query = new UserMember();
//			query.setMemberId(memberId);
//			query.setOpenId(req.getOpId());
//			UserMember userMember = userMemberMapper.selectOne(query);
//			
//			
//			//验证是否重复绑定
//			//根据成员ID 查询有效的患者信息，当一个成员绑定多张卡时，可能有多条
//			Example patientExample = new Example(Patient.class);
//			patientExample.createCriteria()
//			.andEqualTo("state", 1)
//			.andEqualTo("memberId", memberId);
//			List<Patient> patList = patientMapper.selectByExample(patientExample);
//			if(patList==null || patList.size()<=0) {
//				if(userMember==null) {
//					//不存在成员关系时，新增成员关系
//					userMember = new UserMember();
//					userMember.setMemberId(memberId);
//					userMember.setOpenId(req.getOpId());
//					userMemberMapper.insertSelective(userMember);
//				}
//				//不存在患者信息时，直接新增患者信息
//				addPatient(isAddHisUser, memberId, req);
//			}else {
//				//遍历验证是否重复绑卡
//				boolean isRepeatBind = false;
//				for (Patient patient : patList) {
//					//患者已存在有效卡
//					if(patient.getCardType().equals(req.getCardType())
//							&&StringUtil.isNotBlank(patient.getCardNo())
//							&&StringUtil.isNotBlank(req.getCardNo()) 
//							&& patient.getCardNo().equals(req.getCardNo())) {
//						isRepeatBind = true;
//						break;
//					}
//					//患者已经存在有效住院号
//					if(StringUtil.isNotBlank(req.getHospitalNo()) 
//							&& KstHosConstant.CARDTYPE_14.equals(patient.getCardType()) 
//							&& StringUtil.isNotBlank(patient.getCardNo())
//							&& patient.getCardNo().equals(req.getHospitalNo())) {
//						isRepeatBind = true;
//						break;
//					}
//				}
//				//重复绑定时，
//				//1、可能是不同的微信或支付宝账户进行绑定。这种情况新增成员关系，并更新成员信息，同时返回成功。
//				//2、同一微信或支付宝账户 绑定同一张卡，属于重复绑定，返回异常
//				if(isRepeatBind) {
//					if(userMember==null) {
//						//不存在成员关系时，新增成员关系
//						userMember = new UserMember();
//						userMember.setMemberId(memberId);
//						userMember.setOpenId(req.getOpId());
//						userMemberMapper.insertSelective(userMember);
//						
//						//更新成员信息
//						MemberBase upMemBase = new MemberBase();
//						upMemBase.setMemberId(memberId);
//						upMemBase.setMemberCode(memberCode);
//						upMemBase.setMemberName(req.getMemberName());
//						upMemBase.setMobile(req.getMobile());
//						upMemBase.setSex(req.getSex());
//						upMemBase.setIdCardNo(req.getIdCardNo());
//						upMemBase.setIsChildren(req.getIsChildren());
//						upMemBase.setAddress(req.getAddress());
//						upMemBase.setBirthDate(req.getBirthDate());
//						upMemBase.setCertType(req.getCertType());
//						upMemBase.setCertNum(req.getCertNum());
//						upMemBase.setGuardianCertNum(req.getGuardianCertNum());
//						upMemBase.setGuardianCertType(req.getGuardianCertType());
//						upMemBase.setGuardianName(req.getGuardianName());
//						upMemBase.setGuardianSex(req.getGuardianSex());
//						upMemBase.setOperatorId(req.getOpId());
//						upMemBase.setOperatorName(req.getOpId());
//						memberBaseMapper.updateByPrimaryKeySelective(upMemBase);
//						
//						return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
//								RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, memberId));
//					}
//					String mobile = memberBase.getMobile();
//					mobile = mobile.substring(0, 3)+"****"+mobile.substring(7, 11);
//					JSONObject retJson = new JSONObject();
//					retJson.put("msg", "该用户已经被手机号："+mobile+" 绑定，请与绑定用户联系获取最新的验证码进行操作。");
//					retJson.put("memberId", memberId);
//					retJson.put("mobile", mobile);
//					return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN,RetCode.Basic.ERROR_binding,retJson.toJSONString());
//				}
//				if(userMember==null) {
//					//不存在成员关系时，新增成员关系
//					userMember = new UserMember();
//					userMember.setMemberId(memberId);
//					userMember.setOpenId(req.getOpId());
//					userMemberMapper.insertSelective(userMember);
//				}
//				//新增患者信息,当患者对应的卡信息和住院号信息存在时，不再添加
//				addPatient(isAddHisUser, memberId, req);
//			}
//			
//			//更新成员信息
//			MemberBase upMemBase = new MemberBase();
//			upMemBase.setMemberId(memberId);
//			upMemBase.setMemberCode(memberCode);
//			upMemBase.setMemberName(req.getMemberName());
//			upMemBase.setMobile(req.getMobile());
//			upMemBase.setSex(req.getSex());
//			upMemBase.setIdCardNo(req.getIdCardNo());
//			upMemBase.setIsChildren(req.getIsChildren());
//			upMemBase.setAddress(req.getAddress());
//			upMemBase.setBirthDate(req.getBirthDate());
//			upMemBase.setCertType(req.getCertType());
//			upMemBase.setCertNum(req.getCertNum());
//			upMemBase.setGuardianCertNum(req.getGuardianCertNum());
//			upMemBase.setGuardianCertType(req.getGuardianCertType());
//			upMemBase.setGuardianName(req.getGuardianName());
//			upMemBase.setGuardianSex(req.getGuardianSex());
//			upMemBase.setOperatorId(req.getOpId());
//			upMemBase.setOperatorName(req.getOpId());
//			memberBaseMapper.updateByPrimaryKeySelective(upMemBase);
//			
//			if(StringUtil.isBlank(req.getCardNo()) && StringUtil.isBlank(req.getHospitalNo())) {
//				//卡号和住院号都是空，则只是新增成员信息
//				addPatient(isAddHisUser, memberId, req);
//				return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
//						RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, memberId));
//			}
//		}
//		
//		// 传入卡信息时，进行绑卡
//		if (!isAddHisUser && StringUtil.isNotBlank(req.getCardType()) && StringUtil.isNotBlank(req.getCardNo())) {
//			ReqBindClinicCard bindReq = new ReqBindClinicCard(req.getMsg(), 
//					req.getpCId(), req.getCardType(), req.getCardNo(), req.getCardTypeName(), 
//					memberId, req.getCode(), req.getMobile(), req.getIsVirtualCard(),req.getOpId());
//			
//			CommonReq<ReqBindClinicCard> bindReqComm = new CommonReq<ReqBindClinicCard>(bindReq);
//			CommonResp<RespMap> commonResp = this.bindClinicCard(bindReqComm);
//			if(!KstHosConstant.SUCCESSCODE.equals(commonResp.getCode())) {
//				//绑卡不成功，返回异常
//				return commonResp;
//			}
//		}
//		//传入住院号时，进行绑住院号
//		if(!isAddHisUser && StringUtil.isNotBlank(req.getHospitalNo())) {
//			CommonResp<RespMap> commonResp = this.bindHospitalNo(new CommonReq<ReqBindHospitalNo>(new ReqBindHospitalNo(req.getMsg(),
//					req.getpCId(), memberId, req.getMobile(), req.getCode(), req.getHospitalNo(),req.getOpId())));
//			if(!KstHosConstant.SUCCESSCODE.equals(commonResp.getCode())) {
//				//绑定住院号不成功，返回异常
//				return commonResp;
//			}
//		}
//		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
//				RespMap.get().put(ApiKey.AddMemberNotCode.MemberId, memberId));
//	}
	
	/**
	 * 获取成员编码 规则：成员名称+“_”+证件类型+“_”+证件号码（儿童，无证件类型及号码时，使用证件监护人的证件类型及号码）
	 * @param memberName
	 * @param isChildren
	 * @param certType
	 * @param certNum
	 * @param guardianCertType
	 * @param guardianCertNum
	 * @return
	 */
	private String getMemberCode(String memberName,Integer isChildren,String certType,String certNum,String guardianCertType,String guardianCertNum) {
		//儿童，且证件号码为空时，使用监护人的证件类型及号码
		if(isChildren!=null && isChildren==1 && StringUtil.isBlank(certNum)) {
			return memberName+"_"+guardianCertType+"_"+guardianCertNum;
		}
		return memberName+"_"+certType+"_"+certNum;
		
	}

//	/**
//	 * 新增就诊人时，添加患者信息
//	 * @param isAddHisUser
//	 * @param memberId
//	 * @param req
//	 */
//	private void addPatient(boolean isAddHisUser,String memberId,ReqAddMember req) {
//		//是调用HIS建档创建的患者信息时，保存患者的卡号信息
//		if(isAddHisUser) {
//			Example example = new Example(Patient.class);
//			Criteria criteria = example.createCriteria();
//			criteria.andEqualTo("state", 1);
//			criteria.andEqualTo("memberId", memberId);
//			if(StringUtil.isNotBlank(req.getCardType())) {
//				criteria.andEqualTo("cardType", req.getCardType());
//			}
//			List<Patient> existsList = patientMapper.selectByExample(example);
//			
//			Patient patient = new Patient();
//			patient.setId(CommonUtil.getUUID());
//			patient.setMemberId(memberId);
//			patient.setMcardNo(req.getMcardNo());
//			patient.setBirthNumber(req.getBirthNumber());
//			patient.setHosId(req.getHosId());
//			patient.setOperatorId(req.getOpId());
//			patient.setOperatorName(req.getOpId());
//			patient.setState(1);
//			patient.setCardNo(req.getCardNo());
//			patient.setCardType(req.getCardType());
//			patient.setHisMemberId(req.getHisMemberId());
//			if(existsList == null || existsList.size()<=0) {
//				patient.setIsDefault(1);
//			}
//			patientMapper.insertSelective(patient);
//			
//			//
//			reportFormsUtil.dataCollection(req.getMsg(),req.getClientId(), "", null, 3, 1, null);
//			reportFormsUtil.dataCloudCollection(req.getMsg(),req.getClientId(), 102, 1, "1");
//			
//		}else {
//			//默认新增一条患者信息
//			Example patientExample = new Example(Patient.class);
//			patientExample.createCriteria()
//			.andEqualTo("state", 1)
//			.andEqualTo("cardType", KstHosConstant.CARDTYPE_1)
//			.andEqualTo("memberId", memberId);
//			List<Patient> patList = patientMapper.selectByExample(patientExample);
//			if(patList==null || patList.size()<=0) {
//				Patient patient1 = new Patient();
//				patient1.setId(CommonUtil.getUUID());
//				patient1.setMemberId(memberId);
//				patient1.setMcardNo(req.getMcardNo());
//				patient1.setBirthNumber(req.getBirthNumber());
//				patient1.setHosId(req.getHosId());
//				patient1.setOperatorId(req.getOpId());
//				patient1.setOperatorName(req.getOpId());
//				patient1.setHisMemberId(req.getHisMemberId());
//				patient1.setState(1);
//				patient1.setCardType(req.getCardType());
//				patient1.setCardTypeName(req.getCardTypeName());
//				patientMapper.insertSelective(patient1);
//			}
////			//住院号不为空时，再添加患者住院号卡信息
////			if(StringUtil.isNotBlank(req.getHospitalNo()) && (StringUtil.isBlank(req.getCardNo()) || !req.getCardNo().equals(req.getHospitalNo()))) {
////				Patient patient2 = new Patient();
////				patient2.setId(CommonUtil.getUUID());
////				patient2.setMemberId(memberId);
////				patient2.setMcardNo(req.getMcardNo());
////				patient2.setBirthNumber(req.getBirthNumber());
////				patient2.setHosId(req.getHosId());
////				patient2.setOperatorId(req.getOpId());
////				patient2.setOperatorName(req.getOpId());
////				patient2.setHisMemberId(req.getHisMemberId());
////				patient2.setState(1);
////				patient2.setCardType(KstHosConstant.CARDTYPE_14);
////				patient2.setCardTypeName("住院号");
////				patientMapper.insertSelective(patient2);
////			}
//		}
//	}
	
	
	@Override
	public CommonResp<RespMap> delMemberInfo(CommonReq<ReqDelMemberInfo> reqComm) throws Exception{
		ReqDelMemberInfo req = reqComm.getParam();
		if(StringUtil.isBlank(req.getMemberId())) {
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数MemberId不能为空。");
		}
		Member query = new Member();
		query.setMemberId(req.getMemberId());
		query.setOpenId(req.getOpenId());
		List<Member> mList = memberMapper.queryMemberList(query);
		if(mList==null || mList.size()<=0) {
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST,"没有找到对应的用户信息。");
		}
		boolean isBindCard = false;
		for (Member mm : mList) {
			
			//清除缓存所有成员卡信息
			MemberListCache.me().clearMemberMemberId(mm.getMemberId(), mm.getCardType(), req.getOpenId());
			MemberListCache.me().clearMemberCardNo(mm.getCardNo(), mm.getCardType(), req.getOpenId());
			
			//判断是否有绑过卡，有绑过卡时，解绑需写入报表
			if(StringUtils.isNotBlank(mm.getCardNo()) && !isBindCard) {
				isBindCard = true;
			}
			
			IMemberCardService memberCardService = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo(),IMemberCardService.class);
			//有实现his卡类型操作时调用HIS绑卡
			if(memberCardService!=null) {
				Map<String, String> map = new HashMap<String,String>();
				map.put("hisMemberId", mm.getHisMemberId());
				map.put("openId", mm.getOpenId());
				map.put("idCardNo", mm.getIdCardNo());
				map.put("mobile", mm.getMobile());
				map.put("memberName", mm.getMemberName());
				map.put("cardType", mm.getCardType());
				map.put("cardNo", mm.getCardNo());
				CommonResp<RespMap> bindResp = memberCardService.hisUnbindMemberCard(req.getMsg(), map);
				if(!KstHosConstant.SUCCESSCODE.equals(bindResp.getCode())) {
					return bindResp;
				}
			}
		}
		
		
		
		//将渠道用户、成员、卡关联信息移除到历史表
		userMemberCardMapper.moveUserMemberCardToOld(req.getOpenId(), req.getMemberId(), null);
		userMemberCardMapper.delUserMemberCard(req.getOpenId(), req.getMemberId(), null);
		
		//将渠道用户和成员关系移除到历史表
		userMemberMapper.moveUserMemberToOld(req.getOpenId(), req.getMemberId());
		userMemberMapper.delUserMember(req.getOpenId(), req.getMemberId());
		
		
		if (isBindCard) {
			reportFormsUtil.dataCollection(req.getMsg(),req.getClientId(), "", null, 3, -1, null);
			reportFormsUtil.dataCloudCollection(req.getMsg(),req.getClientId(), 102, -1, "1");
		}
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	
	
	@Override
	public String DelMemberCardInfo(InterfaceMessage msg) throws Exception {
		CommonReq<ReqDelMemberCardInfo> commReq = new CommonReq<ReqDelMemberCardInfo>(new ReqDelMemberCardInfo(msg));
		CommonResp<RespMap> commResp = this.delMemberCardInfo(commReq);
		return commResp.toResult();
	}


	@Override
	public CommonResp<RespMap> delMemberCardInfo(CommonReq<ReqDelMemberCardInfo> reqComm) throws Exception {
		ReqDelMemberCardInfo req = reqComm.getParam();
		
		//查询用户所有卡信息
		Member query = new Member();
		query.setMemberId(req.getMemberId());
		query.setOpenId(req.getOpenId());
		query.setCardNo(req.getCardNo());
		query.setCardType(req.getCardType());
		List<Member> mList = memberMapper.queryMemberList(query);
		if(mList==null || mList.size()<=0) {
			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST,"没有找到对应的用户卡信息。");
		}
		for(Member mm : mList) {
			IMemberCardService memberCardService = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo(),IMemberCardService.class);
			//有实现his卡类型操作时调用HIS绑卡
			if(memberCardService!=null) {
				Map<String, String> map = new HashMap<String,String>();
				map.put("hisMemberId", mm.getHisMemberId());
				map.put("openId", mm.getOpenId());
				map.put("idCardNo", mm.getIdCardNo());
				map.put("mobile", mm.getMobile());
				map.put("memberName", mm.getMemberName());
				map.put("cardType", mm.getCardType());
				map.put("cardNo", mm.getCardNo());
				CommonResp<RespMap> bindResp = memberCardService.hisUnbindMemberCard(req.getMsg(), map);
				if(!KstHosConstant.SUCCESSCODE.equals(bindResp.getCode())) {
					return bindResp;
				}
			}
		}
		
		//根据卡号和卡类型查询所有满足的卡信息
		CardBag queryCard = new CardBag();
		queryCard.setCardType(req.getCardType());
		queryCard.setCardNo(req.getCardNo());
		List<CardBag> cardList = cardBagMapper.select(queryCard);
		
		//将渠道用户、成员、卡关联信息移除到历史表
		for (CardBag cardBag : cardList) {
			userMemberCardMapper.moveUserMemberCardToOld(req.getOpenId(), req.getMemberId(), cardBag.getCardId());
			userMemberCardMapper.delUserMemberCard(req.getOpenId(), req.getMemberId(), cardBag.getCardId());
		}
		
		//清除成员对应的缓存卡信息
		MemberListCache.me().clearMemberMemberId(req.getMemberId(), req.getCardType(), req.getOpenId());
		MemberListCache.me().clearMemberCardNo(req.getCardNo(), req.getCardType(), req.getOpenId());
		
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> bindClinicCard(CommonReq<ReqBindClinicCard> reqComm) throws Exception {
			ReqBindClinicCard req = reqComm.getParam();
			if(StringUtil.isBlank(req.getMemberId())) {
				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数MemberId不能为空。");
			}
			if(StringUtil.isBlank(req.getCardNo())) {
				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数CardNo不能为空。");
			}
			if(StringUtil.isBlank(req.getCardType())) {
				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数CardType不能为空。");
			}
			// 验证验证码
			if (StringUtils.isNotBlank(req.getCode()) && KasiteConfig.isCheckPorvingCode()) {
				ReqCheckPorvingCode checkReq = new ReqCheckPorvingCode(req.getMsg(),req.getpCId(),req.getMobile(),req.getCode());
				CommonReq<ReqCheckPorvingCode> checkReqComm = new CommonReq<ReqCheckPorvingCode>(checkReq);
				CommonResp<RespMap> checkResp = porvingCodeService.checkPorvingCode(checkReqComm);
				if (!KstHosConstant.SUCCESSCODE.equals(checkResp.getCode())) {
					// 验证码错误
					return checkResp;
				}
			}
			
			MemberBase member = memberBaseMapper.selectByPrimaryKey(req.getMemberId());
			if(member==null) {
				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_MEMBERINFO,"没有找到成员信息");
			}
			//查询是否存在默认卡信息，不存在时设置当前绑定的卡为默认卡
			int isDefault = 0;
			int num = userMemberCardMapper.queryUserMemberCardCount(req.getOpenId(), member.getMemberId(), req.getCardType());
			if(num<=0) {
				isDefault = 1;
			}
			//有传入卡信息时，验证卡信息
			CommonResp<RespMap> cardResp = validateCardInfo(new CommonReq<ReqValidateCardInfo>(new ReqValidateCardInfo(req.getMsg(), req.getOpenId(), req.getHosId(), 
					member.getMemberName(), req.getMobile(), member.getIdCardNo(), req.getCardNo(), req.getCardType(), req.getCardTypeName(), 
					member.getMemberId())));
			if(!KstHosConstant.SUCCESSCODE.equals(cardResp.getCode())) {
				return cardResp;
			}
			
			//验证成功，查询卡信息是否存在，存在时，新增关联关系，不存在时新增卡信息和关联关系
			RespMap respMap = cardResp.getResultData();
			
			IMemberCardService memberCardService = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo(),IMemberCardService.class);
			//有实现his卡类型操作时调用HIS绑卡
			if(memberCardService!=null) {
				Map<String, String> map = new HashMap<String,String>();
				map.put("hisMemberId", respMap.getString(ApiKey.ValidateCardInfo.HisMemberId));
				map.put("openId", req.getOpenId());
				map.put("idCardNo", member.getIdCardNo());
				map.put("mobile", req.getMobile());
				map.put("memberName", member.getMemberName());
				map.put("sex", member.getSex()!=null?member.getSex().toString():"");
				map.put("provingCode", req.getCode());
				map.put("cardType", req.getCardType());
				map.put("cardNo", req.getCardNo());
				map.put("memberId", member.getMemberId());
				map.put("certType", member.getCertType());
				map.put("certNum", member.getCertNum());
				map.put("guardianName", member.getGuardianName());
				map.put("guardianCertType", member.getGuardianCertType());
				map.put("guardianCertNum", member.getGuardianCertNum());
				map.put("isChildren", member.getIsChildren()!=null?member.getIsChildren().toString():"");
				CommonResp<RespMap> bindResp = memberCardService.hisBindMemberCard(req.getMsg(), map);
				if(!KstHosConstant.SUCCESSCODE.equals(bindResp.getCode())) {
					return bindResp;
				}
				RespMap bRespMap = bindResp.getResultData();
				if(bRespMap!=null) {
					String respCardNo = bRespMap.getString(ApiKey.HisBindMemberCardResp.cardNo);
					if(StringUtil.isNotBlank(respCardNo)) {
						req.setCardNo(respCardNo);
					}
				}
			}
			
			
			CardBag queryCard = new CardBag();
			queryCard.setCardType(req.getCardType());
			queryCard.setCardNo(req.getCardNo());
			List<CardBag> cardList = cardBagMapper.select(queryCard);
			if(cardList==null || cardList.size()<=0) {
				//新增卡
				CardBag card = new CardBag();
				card.setCardNo(req.getCardNo());
				card.setCardType(req.getCardType());
				card.setCardTypeName(req.getCardTypeName());
				card.setHosId(req.getHosId());
				card.setOperatorId(req.getOpenId());
				card.setOperatorName(req.getOperatorName());
				cardBagMapper.insertSelective(card);
				
				//新增用户成员卡信息关联关系表
				UserMemberCard userMemberCard = new UserMemberCard();
				userMemberCard.setIsDefault(isDefault);
				userMemberCard.setMemberId(member.getMemberId());
				userMemberCard.setOpenId(req.getOpenId());
				userMemberCard.setCardId(card.getCardId());
				userMemberCard.setHosId(req.getHosId());
				userMemberCard.setHisMemberId(respMap.getString(ApiKey.ValidateCardInfo.HisMemberId));
				userMemberCard.setOperatorId(req.getOpenId());
				userMemberCard.setOperatorName(req.getOperatorName());
				userMemberCardMapper.insertSelective(userMemberCard);
			}else {
				long cardId = cardList.get(0).getCardId();
				UserMemberCard queryUserCard = new UserMemberCard();
				queryUserCard.setOpenId(req.getOpenId());
				queryUserCard.setCardId(cardId);
				queryUserCard.setMemberId(member.getMemberId());
				int cc = userMemberCardMapper.selectCount(queryUserCard);
				if(cc<=0) {
					//新增用户成员卡信息关联关系表
					UserMemberCard userMemberCard = new UserMemberCard();
					userMemberCard.setIsDefault(isDefault);
					userMemberCard.setMemberId(member.getMemberId());
					userMemberCard.setOpenId(req.getOpenId());
					userMemberCard.setCardId(cardId);
					userMemberCard.setHosId(req.getHosId());
					userMemberCard.setHisMemberId(respMap.getString(ApiKey.ValidateCardInfo.HisMemberId));
					userMemberCard.setOperatorId(req.getOpenId());
					userMemberCard.setOperatorName(req.getOperatorName());
					userMemberCardMapper.insertSelective(userMemberCard);
				}else {
					return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_DATAEXIST,"绑定就诊卡失败，该卡已绑定，请勿重复操作。");
				}
			}
			//更新绑定的手机号码
			MemberBase upBase = new MemberBase();
			upBase.setMobile(req.getMobile());
			upBase.setMemberId(member.getMemberId());
			memberBaseMapper.updateByPrimaryKeySelective(upBase);
			
			try {
				//清除成员对应的缓存卡信息
				MemberListCache.me().clearMemberMemberId(req.getMemberId(), req.getCardType(), req.getOpenId());
				MemberListCache.me().clearMemberCardNo(req.getCardNo(), req.getCardType(), req.getOpenId());
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			reportFormsUtil.dataCollection(req.getMsg(),req.getClientId(), "", null, 3, 1, null);
			reportFormsUtil.dataCloudCollection(req.getMsg(),req.getClientId(), 102, 1, "1");
			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
		
	}

	@Override
	public CommonResp<RespMap> setDefaultMember(CommonReq<ReqSetDefaultMember> reqComm) throws Exception{
		ReqSetDefaultMember req = reqComm.getParam();
		if(req.getIsDefaultMember()==1) {
			//根据OpenId查询所有默认就诊人信息
			UserMember query = new UserMember();
			query.setOpenId(req.getOpId());
			query.setIsDefaultMember(1);
			List<UserMember>  list = userMemberMapper.select(query);
			
			//设置为默认就诊人时，先将所有已经是默认就诊人的成员设置为非默认
			for (UserMember member : list) {
				if(!member.getMemberId().equals(req.getMemberId())) {
					Example example = new Example(UserMember.class);
					example.createCriteria()
					.andEqualTo("openId", member.getOpenId())
					.andEqualTo("memberId", member.getMemberId());
					
					UserMember update = new UserMember();
					update.setIsDefaultMember(0);
					update.setOperatorId(req.getOpenId());
					update.setOperatorId(req.getOperatorName());
					
					userMemberMapper.updateByExampleSelective(update, example);
				}
			}
		}
		if (req.getIsDefaultMember()==1 || req.getIsDefaultMember() == 0) {
			// 设置或取消默认就诊人
			Example example = new Example(UserMember.class);
			example.createCriteria()
			.andEqualTo("openId", req.getOpenId())
			.andEqualTo("memberId", req.getMemberId());
			
			UserMember update = new UserMember();
			update.setIsDefaultMember(req.getIsDefaultMember());
			update.setOperatorId(req.getOpenId());
			update.setOperatorId(req.getOperatorName());
			
			userMemberMapper.updateByExampleSelective(update, example);
		}
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public String SetDefaultCard(InterfaceMessage msg) throws Exception {
		return setDefaultCard(new CommonReq<ReqSetDefaultCard>(new ReqSetDefaultCard(msg))).toResult();
	}


	@Override
	public CommonResp<RespMap> setDefaultCard(CommonReq<ReqSetDefaultCard> reqComm) throws Exception {
		ReqSetDefaultCard req = reqComm.getParam();
		
		if(req.getIsDefault()==1) {
			Map<String, Object> queryCard = new HashMap<>();
			queryCard.put("openId", req.getOpenId());
			queryCard.put("memberId", req.getMemberId());
			queryCard.put("cardType", req.getCardType());
			queryCard.put("notEqualCardNo", req.getCardNo());
			queryCard.put("isDefault", 1);
			List<UserMemberCard> list = userMemberCardMapper.queryUserMemberCardList(queryCard);
			
			if(list!=null && list.size()>0) {
				//存在默认卡时，设置所有默认卡为非默认
				Map<String, Object> setMap = new HashMap<String, Object>();
				//修改内容
				setMap.put("setIsDefault", 0);
				setMap.put("operatorId", req.getOpenId());
				setMap.put("operatorName", req.getOperatorName());
				//条件
				setMap.put("openId", req.getOpId());
				setMap.put("memberId", req.getMemberId());
				setMap.put("cardType", req.getCardType());
				setMap.put("isDefault", 1);
				userMemberCardMapper.setDefaultCard(setMap);
			}
		}
		Map<String, Object> setMap = new HashMap<String, Object>();
		//修改内容
		setMap.put("setIsDefault", req.getIsDefault());
		setMap.put("operatorId", req.getOpenId());
		setMap.put("operatorName", req.getOperatorName());
		//条件
		setMap.put("openId", req.getOpId());
		setMap.put("memberId", req.getMemberId());
		setMap.put("cardType", req.getCardType());
		setMap.put("cardNo", req.getCardNo());
		userMemberCardMapper.setDefaultCard(setMap);
			
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}


	@Override
	public CommonResp<RespMap> bindHospitalNo(CommonReq<ReqBindHospitalNo> reqComm)  throws Exception{
		ReqBindHospitalNo req = reqComm.getParam();
		String hisMemberId = req.getHisMemberId();
		if (StringUtil.isEmpty(req.getHospitalNo())) {
			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数HospitalNo不能为空。");
		}
		if(StringUtil.isBlank(req.getMemberId())) {
			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM,"参数MemberId不能为空。");
		}
		// 验证验证码
		if (StringUtils.isNotBlank(req.getProvingCode()) && KasiteConfig.isCheckPorvingCode()) {
			ReqCheckPorvingCode checkReq = new ReqCheckPorvingCode(req.getMsg(),req.getpCId(),req.getMobile(),req.getProvingCode());
			CommonReq<ReqCheckPorvingCode> checkReqComm = new CommonReq<ReqCheckPorvingCode>(checkReq);
			CommonResp<RespMap> checkResp = porvingCodeService.checkPorvingCode(checkReqComm);
			if (!KstHosConstant.SUCCESSCODE.equals(checkResp.getCode())) {
				// 验证码错误
				return checkResp;
			}
		}
		//判断是否已经绑定一个住院号
		int count = userMemberCardMapper.queryUserMemberCardCount(req.getOpenId(), req.getMemberId(), KstHosConstant.CARDTYPE_14);
		if(count>0) {
			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CHECKHOSPITALNO,"绑定住院号失败，该成员已经绑定了一个住院号，请先进行删除后再绑定。");
		}
		MemberBase member = memberBaseMapper.selectByPrimaryKey(req.getMemberId());
		if(member==null) {
			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN,  RetCode.Basic.ERROR_MEMBERINFO,"没有找到成员信息");
		}
		if(req.getIsValidateCard()) {
			CommonResp<RespMap> cardResp = validateCardInfo(new CommonReq<ReqValidateCardInfo>(new ReqValidateCardInfo(req.getMsg(), req.getOpenId(), req.getHosId(), 
					member.getMemberName(), req.getMobile(), member.getIdCardNo(), req.getHospitalNo(), KstHosConstant.CARDTYPE_14, "住院号", 
					member.getMemberId())));
			if(!KstHosConstant.SUCCESSCODE.equals(cardResp.getCode())) {
				return cardResp;
			}
			RespMap respMap = cardResp.getResultData();
			hisMemberId = respMap.getString(ApiKey.ValidateCardInfo.HisMemberId);
		}
		
		IMemberCardService memberCardService = HandlerBuilder.get().getCallHisService(reqComm.getParam().getAuthInfo(),IMemberCardService.class);
		//有实现his卡类型操作时调用HIS绑卡
		if(memberCardService!=null) {
			Map<String, String> map = new HashMap<String,String>();
			map.put("hisMemberId", hisMemberId);
			map.put("openId", req.getOpenId());
			map.put("idCardNo", member.getIdCardNo());
			map.put("mobile", req.getMobile());
			map.put("memberName", member.getMemberName());
			map.put("sex", member.getSex()!=null?member.getSex().toString():"");
			map.put("provingCode", req.getProvingCode());
			map.put("cardType", KstHosConstant.CARDTYPE_14);
			map.put("cardNo", req.getHospitalNo());
			map.put("memberId", member.getMemberId());
			map.put("certType", member.getCertType());
			map.put("certNum", member.getCertNum());
			map.put("guardianName", member.getGuardianName());
			map.put("guardianCertType", member.getGuardianCertType());
			map.put("guardianCertNum", member.getGuardianCertNum());
			map.put("isChildren", member.getIsChildren()!=null?member.getIsChildren().toString():"");
			CommonResp<RespMap> bindResp = memberCardService.hisBindMemberCard(req.getMsg(), map);
			if(!KstHosConstant.SUCCESSCODE.equals(bindResp.getCode())) {
				return bindResp;
			}
			RespMap bRespMap = bindResp.getResultData();
			if(bRespMap!=null) {
				String respCardNo = bRespMap.getString(ApiKey.HisBindMemberCardResp.cardNo);
				String respHisMemberId = bRespMap.getString(ApiKey.HisBindMemberCardResp.HisMemberId);
				if(StringUtil.isNotBlank(respCardNo)) {
					req.setHospitalNo(respCardNo);
				}
				if(StringUtil.isNotBlank(respHisMemberId)) {
					hisMemberId = respHisMemberId;
				}
			}
		}
		
		//验证成功，查询卡信息是否存在，并新增关联关系
		CardBag queryCard = new CardBag();
		queryCard.setCardType(KstHosConstant.CARDTYPE_14);
		queryCard.setCardNo(req.getHospitalNo());
		List<CardBag> cardList = cardBagMapper.select(queryCard);
		if(cardList==null || cardList.size()<=0) {
			//新增卡信息
			CardBag card = new CardBag();
			card.setCardNo(req.getHospitalNo());
			card.setCardType(KstHosConstant.CARDTYPE_14);
			card.setCardTypeName("住院号");
			card.setHosId(req.getHosId());
			card.setOperatorId(req.getOpenId());
			card.setOperatorName(req.getOperatorName());
			cardBagMapper.insertSelective(card);
			
			//新增渠道用户、成员、卡信息的关联关系
			UserMemberCard userMemberCard = new UserMemberCard();
			userMemberCard.setIsDefault(1);
			userMemberCard.setMemberId(member.getMemberId());
			userMemberCard.setOpenId(req.getOpenId());
			userMemberCard.setCardId(card.getCardId());
			userMemberCard.setHosId(req.getHosId());
			userMemberCard.setHisMemberId(hisMemberId);
			userMemberCard.setOperatorId(req.getOpenId());
			userMemberCard.setOperatorName(req.getOperatorName());
			userMemberCardMapper.insertSelective(userMemberCard);
			
		}else {
			//新增渠道用户、成员、卡信息的关联关系
			long cardId = cardList.get(0).getCardId();
			UserMemberCard queryUserCard = new UserMemberCard();
			queryUserCard.setOpenId(req.getOpenId());
			queryUserCard.setCardId(cardId);
			queryUserCard.setMemberId(member.getMemberId());
			int cc = userMemberCardMapper.selectCount(queryUserCard);
			if(cc<=0) {
				UserMemberCard userMemberCard = new UserMemberCard();
				userMemberCard.setIsDefault(1);
				userMemberCard.setMemberId(member.getMemberId());
				userMemberCard.setOpenId(req.getOpenId());
				userMemberCard.setCardId(cardId);
				userMemberCard.setHosId(req.getHosId());
				userMemberCard.setHisMemberId(hisMemberId);
				userMemberCard.setOperatorId(req.getOpenId());
				userMemberCard.setOperatorName(req.getOperatorName());
				userMemberCardMapper.insertSelective(userMemberCard);
			}else {
				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CHECKHOSPITALNO,"绑定住院号失败，该成员已经绑定了一个住院号，请先进行删除后再绑定。");
			}
		}
		try {
			//清除成员对应的缓存卡信息
			MemberListCache.me().clearMemberMemberId(req.getMemberId(), KstHosConstant.CARDTYPE_14, req.getOpenId());
			MemberListCache.me().clearMemberCardNo(req.getHospitalNo(), KstHosConstant.CARDTYPE_14, req.getOpenId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
//		Map<String, String> map = new ConcurrentHashMap<String, String>();
//		map.put(ApiKey.HisQueryHospitalUserInfo.memberName.getName(), member.getMemberName());
//		map.put(ApiKey.HisQueryHospitalUserInfo.mobile.getName(), req.getMobile());
//		map.put(ApiKey.HisQueryHospitalUserInfo.idCardNo.getName(), StringUtil.getString(member.getIdCardNo()));
//		map.put(ApiKey.HisQueryHospitalUserInfo.hospitalNo.getName(), req.getHospitalNo());
//		map.put(ApiKey.HisQueryHospitalUserInfo.memberId.getName(),  member.getMemberId());
//		// 调用HIS接口查询验证就诊卡
//		CommonResp<HisQueryHospitalUserInfo>  hosResp = HandlerBuilder.get()
//		.getCallHisService(reqComm.getParam().getAuthInfo()).queryHospitalUserInfo(req.getMsg(), map);
//		
//		map.clear();
//		// 查到该信息,绑定住院号
//		if (hosResp==null || !KstHosConstant.SUCCESSCODE.equals(hosResp.getCode()) || hosResp.getData()==null || hosResp.getData().size()<=0) {
//			// 未查到住院号信息
//			return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_BINDHOSPITALNO);
//		}
//		HisQueryHospitalUserInfo resHosUser = hosResp.getDataCaseRetCode();
//		Example example = new Example(Patient.class);
//		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("state", 1);
//		criteria.andEqualTo("memberId", req.getMemberId());
//		criteria.andEqualTo("cardType", KstHosConstant.CARDTYPE_14);
//		Patient exists = patientMapper.selectOneByExample(example);
//		if(exists==null) {
//			//新增患者信息
//			Patient patient = new Patient();
//			patient.setId(CommonUtil.getUUID());
//			patient.setMemberId(req.getMemberId());
//			patient.setHosId(req.getHosId());
//			patient.setOperatorId(req.getOpenId());
//			patient.setOperatorName(req.getOpenId());
//			patient.setState(1);
//			patient.setCardNo(req.getHospitalNo());
//			patient.setCardType(KstHosConstant.CARDTYPE_14);
//			patient.setCardTypeName("住院号");
//			patient.setHospitalNo(req.getHospitalNo());
//			patient.setHisMemberId(resHosUser.getHisMemberId());
//			patientMapper.insertSelective(patient);
//		}else {
//			if(StringUtil.isNotBlank(exists.getCardNo())) {
//				return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CHECKHOSPITALNO,"绑定住院号失败，该成员已经绑定了一个住院号，请先进行解绑后再绑定。");
//			}
//			Patient upPatient = new Patient();
//			upPatient.setHospitalNo(req.getHospitalNo());
//			String hisMemberId = resHosUser.getHisMemberId();
//			if(StringUtil.isNotBlank(hisMemberId)) {
//				upPatient.setHisMemberId(hisMemberId);
//			}
//			upPatient.setCardNo(req.getHospitalNo());
//			upPatient.setCardType(KstHosConstant.CARDTYPE_14);
//			upPatient.setCardTypeName("住院号");
//			Example upExample = new Example(Patient.class);
//			upExample.createCriteria()
//					.andEqualTo("memberId", member.getMemberId())
//					.andEqualTo("cardType", KstHosConstant.CARDTYPE_14)
//					.andEqualTo("state", 1);
//			patientMapper.updateByExampleSelective(upPatient, upExample);
//		}
//		//更新绑定的手机号码
//		MemberBase upBase = new MemberBase();
//		upBase.setMobile(req.getMobile());
//		upBase.setMemberId(member.getMemberId());
//		memberBaseMapper.updateByPrimaryKeySelective(upBase);
//		return new CommonResp<RespMap>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQueryTitleInfo> queryTitleInfo(CommonReq<ReqQueryTitleInfo> reqComm)  throws Exception{
		ReqQueryTitleInfo req = reqComm.getParam();
		List<Map<String, String>> list = doctorMapper.getDocTitleList(req.getHosId(), req.getDeptCode());
		List<RespQueryTitleInfo> respList = new Vector<RespQueryTitleInfo>();
		for (Map<String, String> map : list) {
			
			if(StringUtil.isBlank(MapUtils.getString(map, "DoctorTitle"))
					&& StringUtil.isBlank(MapUtils.getString(map, "DoctorTitleCode"))) {
				continue;
			}
			RespQueryTitleInfo info = new RespQueryTitleInfo();
			if(StringUtil.isNotBlank(MapUtils.getString(map, "DoctorTitle"))
					&& StringUtil.isBlank(MapUtils.getString(map, "DoctorTitleCode"))) {
				info.setDoctorTitle(MapUtils.getString(map, "DoctorTitle"));
				info.setDoctorTitleCode(MapUtils.getString(map, "DoctorTitle"));
			}else {
				info.setDoctorTitle(MapUtils.getString(map, "DoctorTitle"));
				info.setDoctorTitleCode(MapUtils.getString(map, "DoctorTitleCode"));
			}
			respList.add(info);
		}
		return new CommonResp<RespQueryTitleInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	
	@Override
	public CommonResp<RespQueryCardBalance> queryCardBalance(CommonReq<ReqQueryCardBalance> reqComm)  throws Exception{
		ReqQueryCardBalance req = reqComm.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		addMemberInfo2Map(reqComm, memberId, cardNo, cardType, openId, map);
		
		map.put("cardType", req.getCardType());
		map.put("cardNo", req.getCardNo());
		CommonResp<RespQueryMemberList> memberResp = this.queryMemberList(new CommonReq<ReqQueryMemberList>(new ReqQueryMemberList(req.getMsg(), req.getMemberId(), req.getCardNo(),req.getCardType(),req.getOpenId())));
		RespQueryMemberList member = memberResp.getDataCaseRetCode();
		map.put("memberName", member.getMemberName());
		map.put("hisMemberId", member.getHisMemberId());
		
		CommonResp<RespMap> hisResp = HandlerBuilder.get()
				.getCallHisService(reqComm.getParam().getAuthInfo()).queryCardBalance(req.getMsg(), map);
		RespQueryCardBalance resp = new RespQueryCardBalance();
		if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode()) && !StringUtil.isEmpty(hisResp.getData())) {
			resp.setBalance(hisResp.getData().get(0).getInteger(ApiKey.HisQueryCardBalance.Balance));
			return new CommonResp<RespQueryCardBalance>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
		}else {
			//调用his失败，
			return new CommonResp<RespQueryCardBalance>(reqComm,KstHosConstant.DEFAULTTRAN, hisResp.getRetCode(),hisResp.getMessage());
		}
	}

	@Override
	public CommonResp<RespQueryHospitalUserInfo> queryHospitalUserInfo(CommonReq<ReqQueryHospitalUserInfo> reqComm)  throws Exception{
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryHospitalUserInfo req = reqComm.getParam();
		// 入参xml转map格式
		map.put("hospitalNo", req.getHospitalNo());
		map.put("queryHospitalCost", req.getQueryHospitalCost());
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		//如果是mini付的场景情况下 就不查询就诊人信息直接查询医院接口
		if(KstHosConstant.MINIPAY_CHANNEL_ID.equals(req.getClientId())) {
			map.put("clientId", req.getClientId());
			IMiniPayService service = HandlerBuilder.get()
					.getCallHisService(req.getAuthInfo(),IMiniPayService.class);
			CommonResp<HisQueryHospitalUserInfo> userInfo = service.queryHospitalUserInfoByHospiatlNo(req.getMsg(),map);
			if(KstHosConstant.SUCCESSCODE.equals(userInfo.getCode()) && !StringUtil.isEmpty(userInfo.getData())) {
			    ValidatorUtils.validateEntity(userInfo.getData().get(0),AddGroup.class);
				RespQueryHospitalUserInfo resp = new RespQueryHospitalUserInfo();
				BeanCopyUtils.copyProperties(userInfo.getData().get(0), resp, null);
				resp.setMobile(StringUtil.mobileDesensitization(resp.getMobile()));
				resp.setIdCardId(StringUtil.idCardNoDesensitization(resp.getIdCardId()));
				return new CommonResp<RespQueryHospitalUserInfo>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
			}else {
				return new CommonResp<RespQueryHospitalUserInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.CallHosClientError,userInfo.getMessage());
			}
		}else {
			addMemberInfo2Map(reqComm, memberId, req.getHospitalNo(), KstHosConstant.CARDTYPE_14, openId, map);
			CommonResp<HisQueryHospitalUserInfo> userInfo = HandlerBuilder.get()
					.getCallHisService(reqComm.getParam().getAuthInfo()).queryHospitalUserInfo(req.getMsg(),map);
			if(KstHosConstant.SUCCESSCODE.equals(userInfo.getCode()) && !StringUtil.isEmpty(userInfo.getData())) {
			    ValidatorUtils.validateEntity(userInfo.getData().get(0),AddGroup.class);
				RespQueryHospitalUserInfo resp = new RespQueryHospitalUserInfo();
				BeanCopyUtils.copyProperties(userInfo.getData().get(0), resp, null);
				return new CommonResp<RespQueryHospitalUserInfo>(reqComm,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
			}else {
				return new CommonResp<RespQueryHospitalUserInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Common.CallHosClientError,userInfo.getMessage());
			}
		}
		
	}

	@Override
	public CommonResp<RespAddMemberWithValidate> addMemberWithValidate(CommonReq<ReqQueryHospitalUserInfo> reqComm)  throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public CommonResp<RespMap> checkHospitalNo(CommonReq<ReqCheckHospitalNo> reqComm)  throws Exception{
//		ReqCheckHospitalNo req = reqComm.getParam();
//		Map<String, String> map = new HashMap<String, String>(16);
//		map.put("memberName", req.getMemberName());
//		map.put("mobile", req.getMobile());
//		map.put("idCardNo", req.getIdCardNo());
//		map.put("hospitalNo", req.getHospitalNo());
//		String memberId = req.getMemberId();
//		String openId = req.getOpenId();
//		
//		// 调用HIS接口验证
//		CommonResp<HisQueryHospitalNo> hasHospitalNO = HandlerBuilder.get()
//				.getCallHisService(reqComm.getParam().getAuthInfo()).hisQueryHospitalNo(req.getMsg(),map);
//		// 未查到住院号
//		if (!hasHospitalNO.getCode().equals(KstHosConstant.SUCCESSCODE)) {
//			// 调用本地接口,删除住院号
//			patientMapper.delHospitalNo(null, req.getMemberName(), req.getIdCardNo(), req.getMobile());
//			return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
//		}else {
//			HisQueryHospitalNo result = hasHospitalNO.getResultData();
//			if(null != result && StringUtil.isNotBlank(result.getHisMemberId())) {
//				Patient patient = new Patient();
//				patient.setMemberId(memberId);
//				patient.setState(1);
//				patient.setHisMemberId(result.getHisMemberId());
//				Member member = new Member();
//				member.setMemberId(memberId);
//				member.setOpenId(openId);
//				List<Member> list = memberMapper.queryMemberList(member);
//				for (Member m : list) {
////					String pid = m.getid
//				}
//				
//				patientMapper.updateByPrimaryKey(record)
//			}
//		}
//		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CHECKHOSPITALNO);
//	}

	@Override
	public CommonResp<RespQueryPatientInfo> queryPatientInfo(CommonReq<ReqQueryPatientInfo> reqComm)  throws Exception{
		ReqQueryPatientInfo req = reqComm.getParam();
		List<RespQueryPatientInfo> respList = new Vector<RespQueryPatientInfo>();
		if (StringUtil.isBlank(req.getOpenId())) {
			// 如果OpenId为空，则查询HIS接口
			Map<String, String> hisParam = new HashMap<String, String>(16);
			hisParam.put("cardNo", req.getClinicCard());
			hisParam.put("cardType", "1");
			hisParam.put("mobile", req.getMobile());
			hisParam.put("idCardNo", req.getIdCardNo());
			HisQueryUserInfo hisUser = HandlerBuilder.get()
					.getCallHisService(reqComm.getParam().getAuthInfo())
					.hisQueryUserinfo(req.getMsg(),hisParam)
					.getDataCaseRetCode();
			hisParam.clear();
			if (hisUser == null) {
				return new CommonResp<RespQueryPatientInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST);
			}
			RespQueryPatientInfo resp = new RespQueryPatientInfo();
			resp.setMemberName(hisUser.getName());
			resp.setMobile(hisUser.getMobile());
			resp.setIdCardNo(hisUser.getIdCardId());
			resp.setClinicCard(hisUser.getClinicCard());
			resp.setSex(hisUser.getSex());
			resp.setBirthDate(null);
			resp.setAddress(hisUser.getAddress());
			resp.setMcardNo(hisUser.getMcardNo());
			resp.setBirthNumber(null);
			resp.setIsChildren(null);
			resp.setOpId(null);
			resp.setChannelId(null);
			resp.setBalance(hisUser.getFee());
			respList.add(resp);
		} else {
			// 如果OpenId存在，则查询全流程数据库
			Member queryMember = new Member();
			queryMember.setOpenId(req.getOpenId());
			queryMember.setMobile(req.getMobile());
			queryMember.setIdCardNo(req.getIdCardNo());
			queryMember.setCardNo(req.getClinicCard());
			queryMember.setCardType("1");
			
			PageHelper.startPage(0, 100);
			List<Member> memberList = memberMapper.queryMemberList(queryMember);
			
			if (memberList != null && memberList.size() > 0) {
				for (Member member : memberList) {
					RespQueryPatientInfo resp = new RespQueryPatientInfo();
					resp.setMemberName(member.getMemberName());
					resp.setMobile(member.getMobile());
					resp.setIdCardNo(member.getIdCardNo());
					if (KstHosConstant.CARDTYPE_1.equals(member.getCardType())) {
						resp.setClinicCard(member.getCardNo());
					}
					if(member.getSex()!=null) {
						resp.setSex(member.getSex().toString());
					}
					resp.setBirthDate(member.getBirthDate());
					resp.setAddress(member.getAddress());
//					resp.setMcardNo(member.getMcardNo());
//					resp.setBirthNumber(member.getBirthNumber());
					if(member.getIsChildren()!=null) {
						resp.setIsChildren(member.getIsChildren().toString());
					}
					resp.setOpId(member.getOpenId());
					resp.setChannelId(member.getChannelId());
					respList.add(resp);
				}
			}
		}
		return new CommonResp<RespQueryPatientInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespMap> updateMember(CommonReq<ReqUpdateMember> reqComm) throws Exception {
		ReqUpdateMember req = reqComm.getParam();
		MemberBase upMemBase = new MemberBase();
		upMemBase.setMemberId(req.getMemberId());
		upMemBase.setMemberName(req.getMemberName());
		upMemBase.setMobile(req.getMobile());
		upMemBase.setIdCardNo(req.getIdCardNo());
		upMemBase.setIsChildren(req.getIsChildren());
		upMemBase.setAddress(req.getAddress());
		upMemBase.setBirthDate(req.getBirthDate());
		upMemBase.setOperatorId(req.getOpId());
		upMemBase.setOperatorName(req.getOpId());
		
		Patient patient = new Patient();
		patient.setMemberId(req.getMemberId());
		patient.setCardNo(req.getCardNo());
		patient.setCardType(req.getCardType());
		patient.setCardTypeName(req.getCardTypeName());
		patient.setMcardNo(req.getMcardNo());
		patient.setBirthNumber(req.getBirthNumber());
		patient.setHosId(req.getHosId());
		patient.setOperatorId(req.getOpId());
		patient.setOperatorName(req.getOpId());
		patient.setHisMemberId(req.getHisMemberId());
		patient.setHospitalNo(req.getHospitalNo());
		if(StringUtil.isNotBlank(req.getState())) {
			patient.setState(Integer.parseInt(req.getState()));
		}
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		String openId = req.getOpenId();
		if(StringUtil.isNotBlank(memberId) && StringUtil.isNotBlank(openId)) {
			MemberListCache.me().clearMemberCardNo(cardNo, cardType, openId);
			MemberListCache.me().clearMemberMemberId(memberId, cardType, openId);
		}
		Example example = new Example(Patient.class);
		example.createCriteria()
		.andEqualTo("state", 1)
		.andEqualTo("cardType", req.getCardType())
		.andEqualTo("memberId", req.getMemberId());
		memberBaseMapper.updateByPrimaryKeySelective(upMemBase);
		patientMapper.updateByExampleSelective(patient, example);
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespQueryBaseDeptTreeInfo> queryDeptInfoByHosIdForTree(CommonReq<ReqQueryBaseDept> reqComm) throws Exception {
		ReqQueryBaseDept req = reqComm.getParam();
		Dept deptReq = new Dept();
		deptReq.setHosId(req.getHosId());
		deptReq.setParentDeptCode("-1");
		deptReq.setIsShow(req.getIsShow());
		List<Dept> deptList = deptMapper.select(deptReq);
		List<RespQueryBaseDeptTreeInfo> respList = new Vector<RespQueryBaseDeptTreeInfo>();
		for (Dept obj : deptList) {
			RespQueryBaseDeptTreeInfo parentResp = new RespQueryBaseDeptTreeInfo();
			parentResp.setId(obj.getDeptCode());
			parentResp.setText(obj.getDeptName());
			deptReq.setParentDeptCode(obj.getDeptCode());
			List<RespQueryBaseDeptTreeInfo> childrenRespList = new Vector<RespQueryBaseDeptTreeInfo>();
			List<Dept> childDeptList = deptMapper.queryDeptList(deptReq);  //子级科室列表
			for (Dept children : childDeptList) {
				RespQueryBaseDeptTreeInfo childResp = new RespQueryBaseDeptTreeInfo();
				childResp.setId(children.getDeptCode());
				childResp.setText(children.getDeptName());
				childrenRespList.add(childResp);
			}
			parentResp.setChildren(childrenRespList);
			respList.add(parentResp);
		}
		return new CommonResp<RespQueryBaseDeptTreeInfo>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public CommonResp<RespQueryBaseDeptLocal> queryBaseDeptLocal(CommonReq<ReqQueryBaseDeptLocal> reqComm) throws Exception{
		ReqQueryBaseDeptLocal req = reqComm.getParam();
		Example example = new Example(Dept.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(req.getHosId())) {
			criteria.andEqualTo("hosId", req.getHosId());
		}
		if(StringUtil.isNotBlank(req.getDeptCode())) {
			criteria.andEqualTo("deptCode", req.getDeptCode());
		}
		if(StringUtil.isNotBlank(req.getDeptName())) {
			criteria.andEqualTo("deptName", req.getDeptName());
		}
		if(StringUtil.isNotBlank(req.getDeptNameLike())) {
			criteria.andLike("deptName", "%"+req.getDeptNameLike()+"%");
		}
		if(StringUtil.isNotBlank(req.getDeptType())) {
			criteria.andEqualTo("deptType", req.getDeptType());
		}
		if(req.getIsShow()!=null) {
			criteria.andEqualTo("isShow", req.getIsShow());
		}
		example.setOrderByClause(" ORDERCOL ASC ");
		Page<Dept> page = null;
		List<RespQueryBaseDeptLocal> respList = new Vector<RespQueryBaseDeptLocal>();
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			page = PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}else {
			//默认每次最多查询200条
			page = PageHelper.startPage(1, 200);
		}
		List<Dept> deptList = deptMapper.selectByExample(example);
		if(deptList != null) {
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (Dept dept : deptList) {
				RespQueryBaseDeptLocal resp = new RespQueryBaseDeptLocal();
				resp.setAddress(dept.getDeptAddr());
				resp.setDeptCode(dept.getDeptCode());
				resp.setDeptName(dept.getDeptName());
				resp.setIntro(dept.getDeptBrief());
				resp.setParentId(dept.getParentDeptCode());
				resp.setRemark(dept.getRemark());
				resp.setOrderCol(dept.getOrderCol());
				resp.setDeptTel(dept.getDeptTel());
				resp.setDeptType(dept.getDeptType()); 
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryBaseDeptLocal>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}
	
	@Override
	public CommonResp<RespQueryBaseDoctorLocal> queryBaseDoctorLocal(CommonReq<ReqQueryBaseDoctorLocal> reqComm) throws Exception{
		ReqQueryBaseDoctorLocal req = reqComm.getParam();
		Example example = new Example(Doctor.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(req.getHosId())) {
			criteria.andEqualTo("hosId", req.getHosId());
		}
		if(StringUtil.isNotBlank(req.getDeptCode())) {
			criteria.andEqualTo("deptCode", req.getDeptCode());		
		}
		if(StringUtil.isNotBlank(req.getDoctorCode())) {
			criteria.andEqualTo("doctorCode", req.getDoctorCode());
		}
		if(StringUtil.isNotBlank(req.getDoctorCodes())) {
			criteria.andIn("doctorCode", Arrays.asList(req.getDoctorCodes().split(",")));
		}
		if(StringUtil.isNotBlank(req.getDoctorType()) && req.getDoctorType()>0) {
			criteria.andEqualTo("doctorType", req.getDoctorType());		
		}
		if(StringUtil.isNotBlank(req.getDoctorName())) {
			criteria.andEqualTo("doctorName", req.getDoctorName());		
		}
		if(StringUtil.isNotBlank(req.getDoctorNameLike())) {
			criteria.andLike("doctorName", "%"+req.getDoctorNameLike()+"%");		
		}
		if(StringUtil.isNotBlank(req.getDoctorTitleCode())) {
			criteria.andEqualTo("titleCode", req.getDoctorTitleCode());		
		}
		if(StringUtil.isNotBlank(req.getDoctorTitle())) {
			criteria.andEqualTo("title", req.getDoctorTitle());		
		}
		if(StringUtil.isNotBlank(req.getTeachTitle())) {
			criteria.andEqualTo("teachTitle", req.getTeachTitle());		
		}
		example.setOrderByClause("orderCol ASC");
		List<RespQueryBaseDoctorLocal> respList = new Vector<RespQueryBaseDoctorLocal>();
		PageInfo<Doctor> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}else {
			PageHelper.startPage(1, 200);
		}
		List<Doctor> docList = doctorMapper.selectByExample(example);
		if(docList != null) {
			page = new PageInfo<>(docList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (Doctor doc : page.getList()) {
				RespQueryBaseDoctorLocal resp = new RespQueryBaseDoctorLocal();
				resp.setDeptCode(doc.getDeptCode());
				resp.setDeptName(doc.getDeptName());
				resp.setDoctorCode(doc.getDoctorCode());
				resp.setDoctorName(doc.getDoctorName());
				resp.setDoctorTitle(doc.getTitle());
				resp.setDoctorTitleCode(doc.getTitleCode());
				resp.setPhotoUrl(doc.getPhotoUrl());
				resp.setIntro(doc.getIntroduction());
				resp.setSpec(doc.getSpec());
				resp.setRemark(doc.getRemark());
				resp.setLevel(doc.getLevelName());
				resp.setLevelId(doc.getLevelId());
				resp.setSex(doc.getDoctorSex());
				resp.setPrice(0);
				resp.setDoctorTel(doc.getTel());
				resp.setOrderCol(doc.getOrderCol());
				resp.setTeachTitle(doc.getTeachTitle());
				resp.setIsShow(doc.getIsShow());
				resp.setDoctorDegree(doc.getDoctorDegree());
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryBaseDoctorLocal>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}
	
	@Override
	public CommonResp<RespMap> updateHospital(CommonReq<ReqUpdateHospitalLocal> reqComm) throws Exception {
		ReqUpdateHospitalLocal req = reqComm.getParam();
		String requestUrl = KasiteConfig.getKasiteHosWebAppUrl();
		//先查询出医院的基本信息
		Hospital hospitalInfo = hospitalMapper.selectByPrimaryKey(req.getHosId());
		String hosBrief = req.getHosBrief() == null ? "" : req.getHosBrief();
		hospitalInfo.setHosBrief(hosBrief);
		String brief = EscapeCodeUtil.unescape(hosBrief);
		List<String> imgList1 = getImgInfo(EscapeCodeUtil.unescape(hosBrief));
		List<String> list = getImgSrcInfo(imgList1);
		String repContend = null;
		boolean isEdit =false;
		for (String str : list) {
			String temp = str.substring(5, str.length() - 1);
			String tem;
			tem = temp;
			tem = tem.replace(requestUrl, "").replace("\\", "/");
			if(!isEdit){
				File tempFile = new File(KasiteConfig.localConfigPath() + tem);
				String fileName = tempFile.getName();
				StringBuffer savePath = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
				this.uploadFile(tempFile, savePath.append("/hospital/").toString(), fileName);
				String filePath = savePath.append(fileName).toString();
				String saveUrl = requestUrl + "/" + filePath.substring(filePath.indexOf(fileDir));
				repContend = brief.replace(str, "src="+"\""+saveUrl+"\"");
			}else{
				tem = requestUrl + tem;
				repContend = brief.replace(str, "src="+"\""+tem+"\"");
			}
			hospitalInfo.setHosBrief(EscapeCodeUtil.escape(repContend.replace("\\", "/")));
		}
		String contends = req.getPhotoUrl();
		contends = contends.replace("\\", "/");
		hospitalInfo.setPhotoUrl(contends);
		
		hospitalInfo.setHosName(req.getHosName());
		hospitalInfo.setHosLevel(req.getHosLevel());
		hospitalInfo.setHosLevelName(req.getHosLevelName());
		hospitalInfo.setTel(req.getTel());
		hospitalInfo.setCoordinateX(req.getCoordinateX());
		hospitalInfo.setCoordinateY(req.getCoordinateY());
		hospitalInfo.setProvince(req.getProvince());
		hospitalInfo.setAddress(req.getAddress());
		hospitalInfo.setHosRoute(req.getHosRoute());
		// 存在更新
		Example example = new Example(Dept.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("hosId", hospitalInfo.getHosId());
		hospitalMapper.updateByExample(hospitalInfo, example);
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> updateDept(CommonReq<ReqUpdateDeptLocal> reqComm) throws Exception {
		ReqUpdateDeptLocal req = reqComm.getParam();
		//先查询原有信息
		Dept deptInfo = deptMapper.selectByPrimaryKey(req.getDeptCode());
		deptInfo.setDeptAddr(req.getDeptAddr());
		deptInfo.setDeptBrief(req.getDeptBrief());
		deptInfo.setDeptTel(req.getDeptTel());
		deptInfo.setDeptType(req.getDeptType());     
		deptInfo.setOrderCol(req.getOrderCol());
		deptInfo.setParentDeptCode(req.getParentDeptCode());
		// 存在更新
		Example example = new Example(Dept.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("deptCode", deptInfo.getDeptCode());
		deptMapper.updateByExample(deptInfo, example);
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> updateDoctor(CommonReq<ReqUpdateDoctorLocal> reqComm) throws Exception {
		
		ReqUpdateDoctorLocal req = reqComm.getParam();
		Example example = new Example(Doctor.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("doctorCode", req.getDoctorCode());
		//先查询原有信息
		List<Doctor> doctorList = doctorMapper.selectByExample(example);
		if(doctorList!=null && doctorList.size()>0) {
			//传入科室代码且有修改医生是否显示或医生排序时，先根据科室代码和医生工号修改医生的是否显示和医生排序，再根据医生工号修改医生共有的属性
			boolean isUpdateByDeptCode = false;
			Doctor upDoc = new Doctor();
			if(StringUtil.isNotBlank(req.getDeptCode())) {
				if(req.getIsShow()!=null) {
					upDoc.setIsShow(req.getIsShow());
					isUpdateByDeptCode = true;
				}
				if(req.getOrderCol()!=null) {
					upDoc.setOrderCol(req.getOrderCol());
					isUpdateByDeptCode = true;
				}
			}
			if(isUpdateByDeptCode) {
				criteria.andEqualTo("deptCode", req.getDeptCode());
				doctorMapper.updateByExampleSelective(upDoc, example);
			}
			//修改医生内容
			Doctor doctorInfo = new Doctor();
			doctorInfo.setIntroduction(req.getIntroduction());
			doctorInfo.setSpec(req.getSpec());
			doctorInfo.setPhotoUrl(req.getPhotoUrl());
			doctorInfo.setDoctorSex(req.getDoctorSex());
			doctorInfo.setTel(req.getTel());
			doctorInfo.setTitle(req.getTitle());
			doctorInfo.setTitleCode(req.getTitleCode());
			doctorInfo.setDoctorName(req.getDoctorName());
			doctorInfo.setTeachTitle(req.getTeachTitle());
			doctorInfo.setDoctorDegree(req.getDoctorDegree());
			// 存在更新
			doctorMapper.updateByExampleSelective(doctorInfo, example);
			//缓存更新
			doctorLocalCache.load(null,null,req.getDoctorCode());
		}
		return new CommonResp<RespMap>(reqComm, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespQueryArticleList> queryArticleList(CommonReq<ReqQueryArticleList> reqParam) throws Exception {
		ReqQueryArticleList req = reqParam.getParam();
		Example example = new Example(Article.class);
		Criteria criteria = example.createCriteria();
		
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String articleId = req.getId();
		String hosId = req.getHosID();
		Integer status = req.getStatus();
		/*Integer typeClass = null;
		try {
			typeClass = StringUtil.isBlank(req.getTypeClass())?null:Integer.valueOf(req.getTypeClass());
		}catch(Exception e) {
			
		}*/
		String[] typeClassArr = null;
		if(StringUtils.isNotBlank(req.getTypeClass()) && !"-1".equals(req.getTypeClass())) {
			typeClassArr = req.getTypeClass().split(",");
		}
		Integer isHead = req.getIsHead();
		String title = req.getTitle();
		
		if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			criteria.andBetween("updateTime", startDate, endDate);
		}
		if (StringUtil.isNotBlank(articleId)) {
			criteria.andEqualTo("id", articleId);
		}
		if (StringUtil.isNotBlank(hosId)) {
			criteria.andEqualTo("hosId", hosId);
		}
		if (status != null && status >= 0) {
			criteria.andEqualTo("status", status);
		}
		if (typeClassArr != null && typeClassArr.length > 0) {
//			criteria.andEqualTo("typeClass", typeClass);
			criteria.andIn("typeClass", Arrays.asList(typeClassArr));
		}
		if (isHead != null && isHead >= 0) {
			criteria.andEqualTo("isHead", isHead);
		}
		if (StringUtil.isNotBlank(title)) {
			criteria.andLike("title", "%" + title + "%");
		}
		example.setOrderByClause("ISHEAD DESC, ISSUEDATE DESC, UPDATETIME DESC");
		PageInfo<Article> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<Article> articleList = articleMapper.selectByExample(example);
		List<RespQueryArticleList> respList = new Vector<RespQueryArticleList>();
		if(articleList != null) {
			page = new PageInfo<>(articleList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (Article article : page.getList()) {
				RespQueryArticleList resp = new RespQueryArticleList();
				resp.setArticleId(article.getId().toString());
				resp.setImgUrl(article.getImgUrl());
				try {
					if(article.getIsSueDate()!=null) {
						resp.setIsSueDate(DateOper.formatDate(article.getIsSueDate(), "yyyy-MM-dd HH:mm:ss"));
					}
					resp.setCreateDate(DateOper.formatDate(article.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					resp.setLastModify(DateOper.formatDate(article.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				resp.setLinkUrl(article.getLinkUrl());
				resp.setTitle(article.getTitle());
				resp.setType(article.getType());
				resp.setTypeClass(article.getTypeClass());
				resp.setBigImgUrl(article.getBigImgUrl());
				resp.setContents(article.getContents());
				resp.setFinalDeal(article.getFinalDeal());
				resp.setStatus(article.getStatus());
				resp.setIsHead(article.getIsHead());
				resp.setHosId(article.getHosId());
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryArticleList>(reqParam, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
	}

	@Override
	public CommonResp<RespMap> addArticle(CommonReq<ReqUpdateArticle> commReq) throws Exception {
		ReqUpdateArticle req = commReq.getParam();
		Article article = new Article();
		// 文章标题
		String title = req.getTitle()==null?"":req.getTitle();
		// 文章图片
		String imgUrl = req.getImgUrl()==null?"":req.getImgUrl();
		//移除图片url的包装  原格式为<![CDATA[图片url]]
		imgUrl = imgUrl.replace("<![CDATA[", "").replace("]]>", "");
		// 文章大图
		String bigImgUrl = req.getBigImgUrl()==null?"":req.getBigImgUrl();
		//移除图片url的包装  原格式为<![CDATA[图片url]]
		bigImgUrl = bigImgUrl.replace("<![CDATA[", "").replace("]]>", "");
		// 文章链接
		String linkUrl = req.getLinkUrl();
		linkUrl = StringUtil.isBlank(linkUrl) ? "" : linkUrl;
		// 文章内容
		String contents = req.getContents()==null?"":req.getContents();
		// 文章状态
		int status = req.getStatus(); 
		// 文章栏目 
		int typeClass = req.getTypeClass(); 
		int type = req.getType();
		//转义 
		String finalContend = EscapeCodeUtil.unescape(contents);
		List<String> imgList = getImgInfo(EscapeCodeUtil.unescape(contents));
		List<String> lsit = getImgSrcInfo(imgList);
		String tem;
		String repContend = null;
		if(lsit.size() ==0 || lsit == null){
			repContend = finalContend;
		}
		for (String str : lsit) {
			String temp = str.substring(5, str.length() - 1);				
			tem = temp;
			String requestUrl = KasiteConfig.getKasiteHosWebAppUrl();
			tem = tem.replace(requestUrl, "").replace("\\", "/");
			File tempFile = new File(KasiteConfig.localConfigPath() + tem);
			String fileName = tempFile.getName();
			StringBuffer savePath = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
			this.uploadFile(tempFile, savePath.append("/article/").toString(), fileName);
			String filePath = savePath.append(fileName).toString();
			String saveUrl = requestUrl + "/" + filePath.substring(filePath.indexOf(fileDir));
			finalContend = finalContend.replace(str, "src="+"\""+ saveUrl+"\"");
		}
		if(repContend == null){
			article.setContents(EscapeCodeUtil.escape(finalContend));
		}else{
			article.setContents(EscapeCodeUtil.escape(repContend.replace("\\", "/")));
		}
		bigImgUrl = bigImgUrl.replace("\\", "/");
			
		article.setBigImgUrl(bigImgUrl);
		article.setCreateTime(DateOper.getNowDateTime());
		article.setFinalDeal(0);
		article.setId(UUID.randomUUID().toString().replaceAll("-",""));
		article.setIsHead(0);
		article.setLinkUrl(linkUrl);
		article.setTitle(title);
		article.setType(type);
		article.setTypeClass(typeClass);
		imgUrl = imgUrl.replace("\\", "/");
		article.setImgUrl(imgUrl);
		article.setStatus(status);
		article.setFinalDeal(type);
		//status(1发布 0未发布)  isSueDate发布时间
		if(status > 0){
			article.setIsSueDate(DateOper.getNowDateTime());
		}else{
			article.setIsSueDate(null);
		}
		article.setHosId(req.getHosId());
		article.setOperatorId(req.getOperatorId());
		article.setOperatorName(req.getOperatorName());
		//article.setCreateTime(DateOper.getNowDateTime());
		
		articleMapper.insertSelective(article);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> updateArticle(CommonReq<ReqUpdateArticle> commReq) throws Exception {
		ReqUpdateArticle req = commReq.getParam();
		String contents = req.getContents()==null?"":req.getContents();
		//大图
		String bigPic = req.getBigImgUrl()==null?"":req.getBigImgUrl();
		//移除大图url的包装  原格式为<![CDATA[图片url]]
		bigPic=bigPic.replace("<![CDATA[", "").replace("]]>", "");
		//小图
		String smallPic = req.getImgUrl()==null?"":req.getImgUrl();
		smallPic = smallPic.replace("<![CDATA[", "").replace("]]>", "");
		//文章类型
		Integer type = req.getType(); 
		String finalContend = EscapeCodeUtil.unescape(contents);
		List<String> imgList = getImgInfo(EscapeCodeUtil.unescape(contents));
		List<String> lsit = getImgSrcInfo(imgList);
		String repContend = null;
		if(lsit.size() ==0 || lsit == null){
			repContend = finalContend;
		}
		String tem="";
		//转义
		for (String str : lsit) {
			String temp = str.substring(5, str.length() - 1);
			tem = temp;
			finalContend = finalContend.replace(str, "src="+"\""+tem+"\"");
		}
		Article article = new Article();
		article.setId(req.getId());
		article.setTitle(req.getTitle());
		article.setType(type);
		article.setTypeClass(req.getTypeClass());
		article.setLinkUrl(req.getLinkUrl());
		if(repContend == null){
			article.setContents(EscapeCodeUtil.escape(finalContend));
		}else{
			article.setContents(EscapeCodeUtil.escape(repContend.replace("\\", "/")));
		}
		bigPic = bigPic.replace("\\", "/");
		article.setBigImgUrl(bigPic);
		smallPic = smallPic.replace("\\", "/");
		article.setImgUrl(smallPic);
		article.setFinalDeal(type);
		article.setHosId(req.getHosId());
		article.setOperatorId(req.getOperatorId());
		article.setOperatorName(req.getOperatorName());
		//article.setUpdateTime(DateOper.getNowDateTime());
		
		articleMapper.updateByPrimaryKeySelective(article);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}


	@Override
	public CommonResp<RespMap> delArticle(CommonReq<ReqUpdateArticle> commReq) throws Exception {
		ReqUpdateArticle req = commReq.getParam();
		articleMapper.deleteByPrimaryKey(req.getId());
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> dataSynchronous(CommonReq<ReqQueryHospitalLocal> commReq) throws Exception {
		ReqQueryHospitalLocal req = commReq.getParam();
		if (StringUtil.isBlank(req.getHosId())) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM, "参数hosId不能为空。");
		}
		Map<String, String> paramMap = new HashMap<String, String>(16);
		String hosId = req.getHosId();
		paramMap.put(ApiKey.HisQueryClinicDoctor.hosId.getName(), hosId);
		CommonResp<HisQueryBaseDept> hisResp = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo()).queryBaseDept(req.getMsg(), paramMap);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<HisQueryBaseDept> baseDepts = hisResp.getListCaseRetCode();
			if (baseDepts != null && !baseDepts.isEmpty()) {
				for (HisQueryBaseDept baseDept : baseDepts) {
					if(StringUtil.isBlank(baseDept.getDeptCode())) {
						continue;
					}
					Dept dept = new Dept();
					dept.setDeptCode(baseDept.getDeptCode());
					//存在更新
					dept = deptMapper.selectByPrimaryKey(dept);
					if(dept != null) {
						dept.setDeptName(baseDept.getDeptName());
						dept.setDeptBrief(baseDept.getDeptBrief());
						dept.setParentDeptCode(baseDept.getParentDeptCode());
						dept.setRemark(baseDept.getRemark());
						dept.setDeptAddr(baseDept.getDeptAddr());
						
						deptMapper.updateByPrimaryKeySelective(dept);
					}else {
						dept = new Dept();
						dept.setHosId(hosId);
						dept.setDeptCode(baseDept.getDeptCode());
						dept.setDeptName(baseDept.getDeptName());
						dept.setDeptBrief(baseDept.getDeptBrief());
						dept.setParentDeptCode(baseDept.getParentDeptCode());
						dept.setRemark(baseDept.getRemark());
						dept.setDeptAddr(baseDept.getDeptAddr());
						dept.setIsShow(1);
						
						deptMapper.insertSelective(dept);
					}
					String deptName = baseDept.getDeptName();
					String deptCode = baseDept.getDeptCode();
					paramMap.put("deptCode", deptCode);
					paramMap.put("deptName", deptName);
					CommonResp<HisQueryBaseDoctor> hisResp_ = HandlerBuilder.get()
							.getCallHisService(commReq.getParam().getAuthInfo()).queryBaseDoctor(req.getMsg(), paramMap);
					if(KstHosConstant.SUCCESSCODE.equals(hisResp_.getCode())) {
						List<HisQueryBaseDoctor> baseDoctors = hisResp_.getListCaseRetCode();
						if (baseDoctors != null && !baseDoctors.isEmpty()) {
							for (HisQueryBaseDoctor baseDoctor : baseDoctors) {
								if(StringUtil.isBlank(baseDoctor.getDoctorCode())) {
									continue;
								}
								//存在更新
								Doctor doctor = new Doctor();
								doctor.setDeptCode(baseDept.getDeptCode());
								doctor.setDoctorCode(baseDoctor.getDoctorCode());
								doctor = doctorMapper.selectByPrimaryKey(doctor);
								if(doctor != null) {
									doctor.setDoctorName(baseDoctor.getDoctorName());
									doctor.setTitle(baseDoctor.getTitle());
									doctor.setTitleCode(baseDoctor.getTitleCode());
									doctor.setIntroduction(baseDoctor.getIntroduction());
									doctor.setLevelId(baseDoctor.getLevelId());
									doctor.setLevelName(baseDoctor.getLevelName());
									doctor.setPrice(baseDoctor.getPrice());
									doctor.setRemark(baseDoctor.getRemark());
									doctor.setDoctorSex(baseDoctor.getDoctorSex());
									doctor.setSpec(baseDoctor.getSpec());
									doctor.setPhotoUrl(baseDoctor.getPhotoUrl());
									
									doctorMapper.updateByPrimaryKeySelective(doctor);
								}else {
									doctor = new Doctor();
									doctor.setDeptCode(deptCode);
									doctor.setDeptName(deptName);
									doctor.setDoctorCode(baseDoctor.getDoctorCode());
									doctor.setDoctorName(baseDoctor.getDoctorName());
									doctor.setTitle(baseDoctor.getTitle());
									doctor.setTitleCode(baseDoctor.getTitleCode());
									doctor.setIntroduction(baseDoctor.getIntroduction());
									doctor.setLevelId(baseDoctor.getLevelId());
									doctor.setLevelName(baseDoctor.getLevelName());
									doctor.setPrice(baseDoctor.getPrice());
									doctor.setRemark(baseDoctor.getRemark());
									doctor.setDoctorSex(baseDoctor.getDoctorSex());
									doctor.setSpec(baseDoctor.getSpec());
									doctor.setPhotoUrl(baseDoctor.getPhotoUrl());
									doctor.setHosId(hosId);
									doctor.setIsShow(1);
									
									doctorMapper.insertSelective(doctor);
								}
								//缓存更新
								doctorLocalCache.load(null, null, doctor.getDoctorCode());
							}
						}
					}
				}
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
			}else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
			}
		}else {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, hisResp.getRetCode(), hisResp.getMessage());
		}
	}
	
	@Override
	public CommonResp<RespMap> toIsSue(CommonReq<ReqUpdateArticle> commReq) throws Exception {
		ReqUpdateArticle req = commReq.getParam();
		String id = req.getId();
		Integer status = req.getStatus();
		Timestamp isSueDate = StringUtil.isBlank(req.getIsSueDate())?DateOper.getNowDateTime():Timestamp.valueOf(req.getIsSueDate());
		Article article = new Article();
		article.setId(id);
		article.setStatus(status);
		article.setIsSueDate(isSueDate);
		article.setUpdateTime(DateOper.getNowDateTime());
		articleMapper.updateByPrimaryKeySelective(article);
		
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> toHead(CommonReq<ReqUpdateArticle> commReq) throws Exception {
		ReqUpdateArticle req = commReq.getParam();
		String id = req.getId();
		Integer isHead = req.getIsHead();
		Article article = new Article();
		article.setId(id);
		article.setIsHead(isHead);
		article.setUpdateTime(DateOper.getNowDateTime());
		articleMapper.updateByPrimaryKeySelective(article);
		
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	/**
	 * 取得IMG标签的信息
	 * 
	 * @param content
	 * @return
	 */
	private List<String> getImgInfo(String content) {
    	String regular = "<(img)\\s*([\\w]*=(\"|\')([^\"\'<]*)(\"|\')\\s*)*(/>|>)";
		Matcher m1 = Pattern.compile(regular)
				.matcher(content);
		List<String> strL = new ArrayList<String>();
		while (m1.find()) {

			strL.add(m1.group());

		}
		return strL;
	}
	/**
	 * 获取IMG标签中SRC里的值
	 * 
	 * @param strL
	 * @return
	 */
	private List<String> getImgSrcInfo(List<String> strL) {
		List<String> strList = new ArrayList<String>();
		String regular = "(src)=(\\s)*(\"|\')([^\"\']*)(\"|\')*";
		for (String str : strL) {
			Matcher m2 = Pattern.compile(regular).matcher(str);
			while (m2.find()) {
				
				String temp = m2.group();
				strList.add(temp);
			}
		}
		return strList;
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */ 
	@Override
	public String CheckEntityCard(InterfaceMessage msg) throws Exception {
		CommonReq<ReqCheckEntityCard> reqParam = new CommonReq<ReqCheckEntityCard>(new ReqCheckEntityCard(msg));
		IMiniPayService service = HandlerBuilder.get().getCallHisService(reqParam.getParam().getAuthInfo(), IMiniPayService.class);
		if(null != service) {
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put(ApiKey.HisCheckEntityCard.memberName.name(), reqParam.getParam().getPatientName());
			paramMap.put(ApiKey.HisCheckEntityCard.cardNo.name(), reqParam.getParam().getCardNo());
			paramMap.put(ApiKey.HisCheckEntityCard.cardType.name(), reqParam.getParam().getCardType());
			CommonResp<HisCheckEntityCard> resp = service.CheckEntityCard(msg, paramMap);
			HisCheckEntityCard data = resp.getResultData();
			if(null != data) {
				data.setPhone(StringUtil.mobileFormat(data.getPhone()));
				data.setIdCardId(StringUtil.idCardFormat(data.getIdCardId()));
			}
			return resp.toResult();
		}else {
			throw new RRException("该医院未实现Mini付实体卡校验接口");
		}
	}

	@Override
	public CommonResp<RespMap> addDoctor(CommonReq<ReqAddDoctor> reqComm) throws Exception {
		ReqAddDoctor req = reqComm.getParam();
		if(req.getDoctorList()!=null && req.getDoctorList().size()>0) {
			//批量新增
			doctorMapper.batchInsert(req.getDoctorList());
			//缓存更新
			doctorLocalCache.load(req.getDoctorList());
		}else {
			Doctor doc = new Doctor();
			doc.setDeptCode(req.getDeptCode());
			doc.setDeptName(req.getDeptName());
			doc.setDoctorCode(req.getDoctorCode());
			doc.setDoctorName(req.getDoctorName());
			doc.setTitle(req.getTitle());
			doc.setTitleCode(req.getTitleCode());
			doc.setPhotoUrl(req.getPhotoUrl());
			doc.setSpec(req.getSpec());
			doc.setLevelName(req.getLevelName());
			doc.setLevelId(req.getLevelId());
			doc.setRemark(req.getRemark());
			doc.setIntroduction(req.getIntroduction());
			doc.setRemark(req.getRemark());
			doc.setDoctorSex(req.getDoctorSex());
			doc.setIsShow(1);
			doc.setHosId(req.getHosId());
			doc.setPrice(req.getPrice());
			doctorMapper.insertSelective(doc);
			
			//缓存更新
			doctorLocalCache.load(null, req.getDeptCode(), req.getDoctorCode());
		}
		
		return new CommonResp<RespMap>(reqComm, RetCode.Success.RET_10000, "成功");
	}
	
	@Override
	public CommonResp<RespQueryMemberList> queryMemberListToCache(CommonReq<ReqQueryMemberList> reqComm) throws Exception {
		String cardNo = reqComm.getParam().getCardNo();
		String cardType = reqComm.getParam().getCardType();
		String openId = reqComm.getParam().getOpenId();
		String memberId = reqComm.getParam().getMemberId();
		RespQueryMemberList memberList =  MemberListCache.me().getValue(memberId,cardNo,cardType,openId);
		if(null != memberList) {
			return new CommonResp<RespQueryMemberList>(reqComm, RetCode.Success.RET_10000, memberList);
		}
		//对单个用户做缓存其它都不做缓存
		CommonResp<RespQueryMemberList> resp = queryMemberList(reqComm);
		if(resp.getData().size() == 1) {
			RespQueryMemberList retData = resp.getDataCaseRetCode();
			MemberListCache.me().add2MemberId(retData.getMemberId(), retData.getOpId(), cardType,retData);
			MemberListCache.me().add2CardNo(retData.getCardNo(), retData.getCardType(), retData.getOpId(), retData);
		}
		return resp;
		
	}

	/**
	 * 判断是否需要查询HIS用户id 如果需要从数据库里查一次
	 * @param req
	 * @param memberId
	 * @param cardNo
	 * @param cardType
	 * @param openId
	 * @param map
	 * @throws Exception
	 */
	public void addMemberInfo2MapNotCaseRetCode(CommonReq<?> req,String memberId,String cardNo,String cardType,String openId,Map<String, String> map) throws Exception {
		ReqQueryMemberList t = null;
		RespQueryMemberList member = null;
		t = new ReqQueryMemberList(req.getMsg(), memberId,cardNo, cardType, openId);
		if(null != t) {
			CommonReq<ReqQueryMemberList> reqCommM = new CommonReq<ReqQueryMemberList>(t);
			CommonResp<RespQueryMemberList> mlist = queryMemberListToCache(reqCommM);
			member = mlist.getResultData();
			if(null != member) {
				member.toMap(map);
			}
		}
	}
	/**
	 * 判断是否需要查询HIS用户id 如果需要从数据库里查一次
	 * @param req
	 * @param memberId
	 * @param cardNo
	 * @param cardType
	 * @param openId
	 * @param map
	 * @throws Exception
	 */
	public void addMemberInfo2Map(CommonReq<?> req,String memberId,String cardNo,String cardType,String openId,Map<String, String> map) throws Exception {
		ReqQueryMemberList t = null;
		RespQueryMemberList member = null;
		t = new ReqQueryMemberList(req.getMsg(), memberId,cardNo, cardType, openId);
		if(null != t) {
			CommonReq<ReqQueryMemberList> reqCommM = new CommonReq<ReqQueryMemberList>(t);
			CommonResp<RespQueryMemberList> mlist = queryMemberListToCache(reqCommM);
			member = mlist.getDataCaseRetCode();
			if(null == member) {
				throw new RRException("未查到用户信息");
			}
			member.toMap(map);
		}
	}
	
	//上传文件
	private void uploadFile(File file, String filePath, String fileName) throws Exception {
		InputStream in = new FileInputStream(file);
	    byte[] data = toByteArray(in);
        in.close(); 
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(data);
        out.flush();
        out.close();
    }
	
	private byte[] toByteArray(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024 * 30];
	    int n = 0;
	    while ((n = in.read(buffer)) != -1) {
	        out.write(buffer, 0, n);
	    }
	    byte[] data = out.toByteArray();
	    out.flush();
        out.close();
	    return data;
	}
	
}
