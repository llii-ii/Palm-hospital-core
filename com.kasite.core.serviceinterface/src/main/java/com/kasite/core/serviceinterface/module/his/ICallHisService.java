package com.kasite.core.serviceinterface.module.his;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.service.CallHis;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisBookService;
import com.kasite.core.serviceinterface.module.his.resp.HisGetQueueInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisHosNoRecharge;
import com.kasite.core.serviceinterface.module.his.resp.HisOPDRecharge;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicCard;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryHospitalUserInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalRechargeList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryNumbers;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientRechargeList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryRegInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryUserInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItemInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItems;
import com.kasite.core.serviceinterface.module.his.resp.HisStopClinicList;
import com.kasite.core.serviceinterface.module.his.resp.HisYYCancel;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;
import com.kasite.core.serviceinterface.module.report.resp.RespGetTjReportInfo;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 这里定义的都是HIS必须提供的接口方法定义
 * 
 * ！！！开发人员在评估his接口是否可以对接掌上智慧医院的时候这些接口都是必须实现的。
 * 请与HIS开发人员沟通后进行实现类的编写
 * @className: ICallHisService
 * @author: lcz
 * @date: 2018年5月30日 下午8:10:31
 */
public interface ICallHisService extends ICallHis{
	
	/**
	 * 返回是哪一家医院的接口实现类（系统支持多家医院的接口实现）
	 */
	CallHis accept();
	/**
	 * 校验HIS接口是否正常 返回2个状态，正常／异常
	 * 当有业务需要调用his 并需要提前判断his接口是否通讯正常时 会调用这个接口进行判断
	 * @param his
	 * @return
	 */
	HisInfStatus infStatus(InterfaceMessage msg,BusinessTypeEnum bt);
	
	
	/**验证就诊卡**/
	CommonResp<HisQueryClinicCard> hisQueryClinicCard(InterfaceMessage msg,Map<String, String> map) throws Exception;
//	/**验证住院号***/
//	CommonResp<HisQueryHospitalNo> hisQueryHospitalNo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询用户信息**/
	CommonResp<HisQueryUserInfo> hisQueryUserinfo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 根据住院号查询住院患者信息
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryHospitalUserInfo> queryHospitalUserInfo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询卡余额
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<RespMap> queryCardBalance(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
//	/**检验住院号是否已解绑**/
//	int hisCheckHospitalNo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询科室信息**/
	CommonResp<HisQueryBaseDept> queryBaseDept(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询医生信息**/
	CommonResp<HisQueryBaseDoctor> queryBaseDoctor(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询门诊科室
	 * @throws Exception **/
	CommonResp<HisQueryClinicDept> queryClinicDept(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询门诊医生**/
	CommonResp<HisQueryClinicDoctor> queryClinicDoctor(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询门诊排班**/
	CommonResp<HisQueryClinicSchedule> queryClinicSchedule(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询号源**/
	CommonResp<HisQueryNumbers> queryNumbers(InterfaceMessage msg,HisQueryClinicSchedule sch,Map<String, String> map) throws Exception;
	/**挂号**/
	CommonResp<HisBookService> bookService(InterfaceMessage msg,String orderId,String scheduleStor,String numberStore,String lockStore,Map<String, String> map) throws Exception;
	/**退号**/
	CommonResp<HisYYCancel> yyCancel(InterfaceMessage msg,String orderId,Map<String, String> map,String bookServiceStroe) throws Exception;
	/**查询停诊数据**/
	CommonResp<HisStopClinicList> queryStopClinicList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询预约记录**/
	CommonResp<HisQueryRegInfo> queryRegInfo(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**住院充值**/
	CommonResp<HisHosNoRecharge> hosNoRecharge(InterfaceMessage msg,String orderId,Map<String, String> map) throws Exception;
	/**就诊卡充值**/
	CommonResp<HisOPDRecharge> oPDRecharge(InterfaceMessage msg,String orderId,Map<String, String> map) throws Exception;
	
	/**
	 * 查询门诊费用日清单列表
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOutpatientCostList> queryOutpatientCostList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询门诊日清单分类
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryOutpatientCostType> queryOutpatientCostType(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询门诊日清单分类下的项目明细
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<CommonPrescriptionItem> queryOutpatientCostTypeItem(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	
	
	/**
	 * 查询住院费用日清单列表
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryInHospitalCostList> queryInHospitalCostList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询住院日清单分类
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryInHospitalCostType> queryInHospitalCostType(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/**
	 * 查询住院日清单分类下的项目明细
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<CommonPrescriptionItem> queryInHospitalCostTypeItem(InterfaceMessage msg,Map<String, String> map) throws Exception;	
	
	/**查询排队信息**/
	CommonResp<HisGetQueueInfo> queryQueue(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询报告数**/
	int queryReportCount(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询检查检验报告单**/
	CommonResp<HisReportItems>  queryReportList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询报告但详情**/
	CommonResp<HisReportItemInfo> queryReportDetail(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**查询体检报告但详情**/
	CommonResp<RespGetTjReportInfo> queryTjReportDetail(InterfaceMessage msg,Map<String, String> map) throws Exception;

	/**预交金充值记录查询**/
	CommonResp<HisQueryInHospitalRechargeList> queryInHospitalRechargeList(InterfaceMessage msg,Map<String, String> map) throws Exception;
	/**门诊充值记录查询**/
	CommonResp<HisQueryOutpatientRechargeList> queryHosOutpatientRecords(InterfaceMessage msg,Map<String, String> map) throws Exception;
	
	/** 查询某日HIS账单列表(下载His账单) ***/
	CommonResp<RespQueryHisBill> queryHisOrderBillList(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;

	/** 查询HIS账单,传入支付订单号目前查询单条信息，后续有做退款操作看是否需要修改***/
	CommonResp<RespQueryHisBill> queryHisOrderBillByPayNo(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;
	
	/**
	 * 3.8.5.	门诊预交金退费冻结
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<RespMap> oPDRechargeRefundFreeze(InterfaceMessage msg,Map<String, String> paramMap);
	
	/**
	 * 3.8.6.	门诊预交金退费核销
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<RespMap> oPDRechargeRefundWriteOff(InterfaceMessage msg,Map<String, String> paramMap);
	
	
			
}
