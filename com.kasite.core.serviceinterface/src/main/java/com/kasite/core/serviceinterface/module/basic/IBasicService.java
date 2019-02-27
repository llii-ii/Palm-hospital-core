package com.kasite.core.serviceinterface.module.basic;

import java.util.Map;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqBindClinicCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqBindHospitalNo;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelMemberCardInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelMemberInfo;
import com.kasite.core.serviceinterface.module.basic.req.ReqGetProvingCode;
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
import com.kasite.core.serviceinterface.module.basic.req.ReqSetDefaultCard;
import com.kasite.core.serviceinterface.module.basic.req.ReqSetDefaultMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateArticle;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDeptLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateHospitalLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqValidateCardInfo;
import com.kasite.core.serviceinterface.module.basic.resp.RespAddMemberWithValidate;
import com.kasite.core.serviceinterface.module.basic.resp.RespGetprovingCode;
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
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf 2017年11月14日 17:50:19
 * TODO 基础模块接口类
 */
public interface IBasicService extends BasicCommonService{
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
	 void addMemberInfo2Map(CommonReq<?> req,String memberId,String cardNo,String cardType,String openId,Map<String, String> map) throws Exception;
	
	/**
	 * 查询HIS基础科室
	 * @param msg
	 * @return
	 */
	String QueryBaseDept(InterfaceMessage msg) throws Exception;
	/**
	 * 查询HIS基础科室
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryBaseDept> queryBaseDept(CommonReq<ReqQueryBaseDept> reqComm) throws Exception;
	/**
	 * 查询HIS基础医生
	 * @param msg
	 * @return
	 */
	String QueryBaseDoctor(InterfaceMessage msg) throws Exception;
	/**
	 * 查询HIS基础医生
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryBaseDoctor> queryBaseDoctor(CommonReq<ReqQueryBaseDoctor> reqComm) throws Exception;
	
	CommonResp<RespMap> addDoctor(CommonReq<ReqAddDoctor> reqComm) throws Exception;
	
	/**
	 * 查询就诊人列表
	 * @param msg
	 * @return
	 */
	String QueryMemberList(InterfaceMessage msg) throws Exception;
	/**
	 * 查询就诊人卡列表（多张卡返回多条数据）
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryMemberCardList(InterfaceMessage msg) throws Exception;
	
	/**
	 * 设置默认就诊卡
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String SetDefaultCard(InterfaceMessage msg) throws Exception;
	
	/**
	 * 设置默认就诊人
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> setDefaultCard(CommonReq<ReqSetDefaultCard> reqComm) throws Exception;
	
	/**
	 * 查询就诊人卡列表（多张卡返回多条数据）
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryMemberList> queryMemberCardList(CommonReq<ReqQueryMemberList> reqComm) throws Exception;
	/**
	 * 查询成员列表
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryMemberList> queryMemberList(CommonReq<ReqQueryMemberList> reqComm) throws Exception;
	/**
	 * 根据opId、卡类型、成员ID或卡号查询成员及卡信息
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryMemberList> queryMemberInfo(CommonReq<ReqQueryMemberInfo> reqComm) throws Exception;
	
	/**
	 * 从缓存中查询用户信息，缓存中没有则从数据库中查找后写入缓存
	 * 缓存有效时间，继续使用缓存则直接清楚
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryMemberList> queryMemberListToCache(CommonReq<ReqQueryMemberList> reqComm) throws Exception;
	
	
	
	
	/**
	 * 新增就诊人
	 * @param msg
	 * @return
	 */
	String AddMember(InterfaceMessage msg) throws Exception;
	/**
	 * 新增就诊人
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> addMember(CommonReq<ReqAddMember> reqComm) throws Exception;
	
	/**
	 * 添加就诊人及卡信息
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addMemberAndCardInfo(CommonReq<ReqAddMember> reqComm) throws Exception;
	
	/**
	 * 验证卡信息是否有误（目前仅有就诊卡和住院号）
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> validateCardInfo(CommonReq<ReqValidateCardInfo> reqComm) throws Exception;
	
//	/**
//	 * 
//	 * @Description: 
//	 * @param reqComm
//	 * @return
//	 * @throws Exception
//	 */
//	CommonResp<RespMap> addMemberNotCode(CommonReq<ReqAddMember> reqComm) throws Exception;
	
	/**
	 * 修改就诊人信息
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> updateMember(CommonReq<ReqUpdateMember> reqComm) throws Exception;
	
	/**
	 * 删除就诊人
	 * @param msg
	 * @return
	 */
	String DelMemberInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 删除就诊人
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> delMemberInfo(CommonReq<ReqDelMemberInfo> reqComm) throws Exception;
	/**
	 * 删除就诊卡
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String DelMemberCardInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 删除就诊卡
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> delMemberCardInfo(CommonReq<ReqDelMemberCardInfo> reqComm) throws Exception;
	/**
	 * 查询字典
	 * @param msg
	 * @return
	 */
	String QueryDictionaryInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 新增字典
	 * @param msg
	 * @return
	 */
	String AddDictionary(InterfaceMessage msg) throws Exception;
	/**
	 * 删除字典
	 * @param msg
	 * @return
	 */
	String DelDictionary(InterfaceMessage msg) throws Exception;
	/**
	 * 更新字典
	 * @param msg
	 * @return
	 */
	String UpdateDictionary(InterfaceMessage msg) throws Exception;
	/**
	 * 查询文章列表
	 * @param msg
	 * @return
	 */
	String QueryArticleInfoForList(InterfaceMessage msg) throws Exception;
	/**
	 * 更新医院
	 * @param msg
	 * @return
	 */
	String UpdateHospital(InterfaceMessage msg) throws Exception;
	/**
	 * 更新医生信息
	 * @param msg
	 * @return
	 */
	String UpdateDoctor(InterfaceMessage msg) throws Exception;
	/**
	 * 更新科室信息
	 * @param msg
	 * @return
	 */
	String UpdateDept(InterfaceMessage msg) throws Exception;
	/**
	 * 获取验证码
	 * @param msg
	 * @return
	 */
	String GetProvingCode(InterfaceMessage msg) throws Exception;
	
	CommonResp<RespGetprovingCode> getProvingCode(CommonReq<ReqGetProvingCode> reqComm) throws Exception;
	
	/**
	 * 绑定就诊卡
	 * @param msg
	 * @return
	 */
	String BindClinicCard(InterfaceMessage msg) throws Exception;
	/**
	 * 绑定就诊卡
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> bindClinicCard(CommonReq<ReqBindClinicCard> reqComm) throws Exception;
	
	/**
	 * 设置默认就诊人
	 * @param msg
	 * @return
	 */
	String SetDefaultMember(InterfaceMessage msg) throws Exception;
	/**
	 * 设置默认就诊人
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> setDefaultMember(CommonReq<ReqSetDefaultMember> reqComm) throws Exception;
	/**
	 * 绑定住院号
	 * @param msg
	 * @return
	 */
	String BindHospitalNo(InterfaceMessage msg) throws Exception;
	/**
	 * 绑定住院号
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespMap> bindHospitalNo(CommonReq<ReqBindHospitalNo> reqComm) throws Exception;
	/**
	 * 查询医生职称
	 * @param msg
	 * @return
	 */
	String QueryTitleInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 查询医生职称
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryTitleInfo> queryTitleInfo(CommonReq<ReqQueryTitleInfo> reqComm) throws Exception;
	
	/**
	 * 查询卡余额
	 * @param msg
	 * @return
	 */
	String QueryCardBalance(InterfaceMessage msg) throws Exception;
	/**
	 * 查询卡余额
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryCardBalance> queryCardBalance(CommonReq<ReqQueryCardBalance> reqComm) throws Exception;
	/**
	 * 住院用户信息查询
	 * @param msg
	 * @return
	 */
	String QueryHospitalUserInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 住院用户信息查询
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryHospitalUserInfo> queryHospitalUserInfo(CommonReq<ReqQueryHospitalUserInfo> reqComm) throws Exception;
	
	

	/**
	 * 新增就诊人(验证)
	 * @param msg
	 * @return
	 */
	String AddMemberWithValidate(InterfaceMessage msg) throws Exception;
	/**
	 * 新增就诊人(验证)
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespAddMemberWithValidate> addMemberWithValidate(CommonReq<ReqQueryHospitalUserInfo> reqComm) throws Exception;
//	/**检验住院号
//	 * @param msg
//	 * @return
//	 */
//	String CheckHospitalNo(InterfaceMessage msg) throws Exception;	
//	/**
//	 * 检验住院号
//	 * @Description: 
//	 * @param reqComm
//	 * @return
//	 */
//	CommonResp<RespMap> checkHospitalNo(CommonReq<ReqCheckHospitalNo> reqComm) throws Exception;
	
	/**
	 * 查询HIS患者信息、全流程平台患者信息
	 *	入参OpenId为空时，查询HIS接口
	 *	入参OpenId非空时，查询本地member记录
	 * @param msg
	 * @return
	 */
	String QueryPatientInfo(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询HIS患者信息、全流程平台患者信息
	 * @Description: 
	 * @param reqComm
	 * @return
	 */
	CommonResp<RespQueryPatientInfo> queryPatientInfo(CommonReq<ReqQueryPatientInfo> reqComm) throws Exception;
	
	/**
	 * 查询科室的列表数据（管理后台的树状列表）
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBaseDeptTreeInfo> queryDeptInfoByHosIdForTree(CommonReq<ReqQueryBaseDept> reqComm) throws Exception;
	
	/**
	 * 查询科室列表信息(管理后台)
	 * 
	 * @param hosId
	 * @param deptCode
	 * @param pageVo
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBaseDeptLocal> queryBaseDeptLocal(CommonReq<ReqQueryBaseDeptLocal> reqComm)  throws Exception;
	
	/**
	 * 查询医生列表信息(管理后台)
	 * 
	 * @param hosId
	 * @param deptCode
	 * @param doctorCode
	 * @param pageVo
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBaseDoctorLocal> queryBaseDoctorLocal(CommonReq<ReqQueryBaseDoctorLocal> reqComm) throws Exception;
	
	/**
	 * 更新医院
	 * @param msg
	 * @return
	 */
	CommonResp<RespMap> updateHospital(CommonReq<ReqUpdateHospitalLocal> reqComm) throws Exception;
	
	/**
	 * 更新科室信息(管理后台)
	 * 
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateDept(CommonReq<ReqUpdateDeptLocal> reqComm) throws Exception;
	
	/**
	 * 更新医生信息(管理后台)
	 * 
	 * @param doctor
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateDoctor(CommonReq<ReqUpdateDoctorLocal> reqComm) throws Exception;
	
	/**
	 * 新增文章(管理后台)
	 * 
	 * @param article
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addArticle(CommonReq<ReqUpdateArticle> reqComm) throws Exception;
	
	/**
	 * 修改文章(管理后台)
	 * 
	 * @param article
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> updateArticle(CommonReq<ReqUpdateArticle> reqComm) throws Exception;
	
	/**
	 * 删除文章(管理后台)
	 * 
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> delArticle(CommonReq<ReqUpdateArticle> reqComm) throws Exception;
	
	/**
	 * 科室医生的同步操作(管理后台)
	 * 
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> dataSynchronous(CommonReq<ReqQueryHospitalLocal> reqComm) throws Exception;
	
	
	String CheckEntityCard(InterfaceMessage msg) throws Exception;
	
	/**
	 * 发布文章(管理后台)
	 * 
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> toIsSue(CommonReq<ReqUpdateArticle> commReq) throws Exception;
	
	/**
	 * 文章置顶(管理后台)
	 * 
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> toHead(CommonReq<ReqUpdateArticle> commReq) throws Exception;

	/**
	 * 查询本地医生信息
	 * @param reqComm
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBaseDoctor> queryLocalDoctor(CommonReq<ReqQueryBaseDoctor> reqComm) throws Exception;
	
}
