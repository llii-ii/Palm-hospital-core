/**
 * 
 */
package com.kasite.core.serviceinterface.module.yy;

import java.util.List;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.yy.req.ReqBookService;
import com.kasite.core.serviceinterface.module.yy.req.ReqCancelAppoint;
import com.kasite.core.serviceinterface.module.yy.req.ReqGetAppointReceiptInfo;
import com.kasite.core.serviceinterface.module.yy.req.ReqLockOrder;
import com.kasite.core.serviceinterface.module.yy.req.ReqMedicalAppoint;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDept;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryExamItemList;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryHistoryDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryNumbers;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryScheduleDate;
import com.kasite.core.serviceinterface.module.yy.req.ReqQuerySignalSourceList;
import com.kasite.core.serviceinterface.module.yy.req.ReqSearchClinicDeptAndDoctor;
import com.kasite.core.serviceinterface.module.yy.req.ReqStop;
import com.kasite.core.serviceinterface.module.yy.req.ReqUnlock;
import com.kasite.core.serviceinterface.module.yy.req.ReqUpdateYyWater;
import com.kasite.core.serviceinterface.module.yy.req.ReqYYCancel;
import com.kasite.core.serviceinterface.module.yy.req.ReqYyLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicSchedule;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryRegInfo;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryYYLimit;
import com.kasite.core.serviceinterface.module.yy.resp.RespSearchClinicDeptAndDoctor;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author lsq
 * version 1.0
 * 2017-6-20下午2:58:35
 */
public interface IYYService{
	
	/**
	 * 推送停诊
	 * @param msg
	 * @return
	 */
	String PushStop(InterfaceMessage msg) throws Exception;

	/**
	 * 推送停诊消息
	 * @param msg
	 * @param list
	 * @return
	 */
	CommonResp<RespMap> pushStop(CommonReq<ReqStop> req) throws Exception;

	/**
	 * 查询有排班的科室
	 * @param msg
	 * @return
	 */
	String QueryClinicDept(InterfaceMessage msg) throws Exception;
	/**
	 * 查询有排班的科室
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryClinicDept(CommonReq<ReqQueryClinicDept> commReq) throws Exception;
	/**
	 * 查询有排班的医生
	 * @param msg
	 * @return
	 */
	public String QueryClinicDoctor(InterfaceMessage msg) throws Exception;
	/**
	 * 查询有排班的医生
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryClinicDoctor> queryClinicDoctor(CommonReq<ReqQueryClinicDoctor> commReq) throws Exception;
	
	/**
	 * 搜索门诊科室、医生
	 * @Description: 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String SearchClinicDeptAndDoctor(InterfaceMessage msg) throws Exception;
	/**
	 * 搜索门诊科室、医生
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespSearchClinicDeptAndDoctor> searchClinicDeptAndDoctor(CommonReq<ReqSearchClinicDeptAndDoctor> commReq) throws Exception;
	
	/**
	 * 查询号源
	 * @param msg
	 * @return
	 */
	public String QueryNumbers(InterfaceMessage msg) throws Exception;
	/**
	 * 查询号源
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryNumbers(CommonReq<ReqQueryNumbers> commReq) throws Exception;
	/**
	 * 查询门诊排班
	 * @param msg
	 * @return
	 */
	public String QueryClinicSchedule(InterfaceMessage msg) throws Exception;
	/**
	 * 查询门诊排班
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryClinicSchedule> queryClinicSchedule(CommonReq<ReqQueryClinicSchedule> commReq) throws Exception;
	/**
	 * 查询历史医生
	 * @param msg
	 * @return
	 */
	public String QueryHistoryDoctor(InterfaceMessage msg) throws Exception;
	/**
	 * 查询历史医生
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryClinicDoctor> queryHistoryDoctor(CommonReq<ReqQueryHistoryDoctor> commReq) throws Exception;
	/**
	 * 锁号
	 * @param msg
	 * @return
	 */
	public String LockOrder(InterfaceMessage msg) throws Exception;
	/**
	 * 锁号
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> lockOrder(CommonReq<ReqLockOrder> commReq) throws Exception;
	/**
	 * 释号
	 * @param msg
	 * @return
	 */
	public String Unlock(InterfaceMessage msg) throws Exception;
	/**
	 * 释号
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> unlock(CommonReq<ReqUnlock> commReq) throws Exception;
	
	/**
	 * 预约信息查询
	 * @param msg
	 * @return
	 */
	public String QueryRegInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 预约信息查询
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryRegInfo> queryRegInfo(CommonReq<ReqQueryRegInfo> commReq) throws Exception;
	CommonResp<RespQueryRegInfo> queryLocalRegInfo(CommonReq<ReqQueryRegInfo> commReq) throws Exception;
	
	/**
	 * 查询预约规则
	 * @param msg
	 * @return
	 */
	public String QueryYYRule(InterfaceMessage msg) throws Exception;
	
	/**
	 * 挂号
	 * @param msg
	 * @return
	 */
	public String BookService(InterfaceMessage msg) throws Exception;
	/**
	 *  挂号
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> bookService(CommonReq<ReqBookService> commReq) throws Exception;
	
	
	/**
	 * 新增订单并挂号
	 * @param msg
	 * @return
	 */
	public String AddOrderAndBookService(InterfaceMessage msg) throws Exception;
	/**
	 * 退号
	 * @param msg
	 * @return
	 */
	public String YYCancel(InterfaceMessage msg) throws Exception;
	/**
	 * 退号
	 * @Description: 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> yYCancel(CommonReq<ReqYYCancel> commReq) throws Exception;

	
	/**
	 * 查询预约限号规则(管理后台)
	 * 
	 * @param ruleId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public List<RespQueryYYLimit> queryYYLimit(String ruleId, Integer state) throws Exception;
	
	/**
	 * 更新预约限号规则(管理后台)
	 * 
	 * @param reqYyLimit
	 * @return
	 * @throws Exception
	 */
	public int updateYYLimit(ReqYyLimit reqYyLimit) throws Exception;
	
	/**
	 * 更新预约记录
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespMap> updateYyWater(CommonReq<ReqUpdateYyWater> commReq) throws Exception;
	/*
	 * 以下为医技预约方法
	 */
	public String QueryExamItemList(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> queryExamItemList(CommonReq<ReqQueryExamItemList> commReq) throws Exception;
	
	public String QuerySignalSourceList(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> querySignalSourceList(CommonReq<ReqQuerySignalSourceList> commReq) throws Exception;
	
	public String MedicalAppoint(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> medicalAppoint(CommonReq<ReqMedicalAppoint> commReq) throws Exception;
	
	public String CancelAppoint(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> cancelAppoint(CommonReq<ReqCancelAppoint> commReq) throws Exception;
	
	public String GetAppointReceiptInfo(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> getAppointReceiptInfo(CommonReq<ReqGetAppointReceiptInfo> commReq) throws Exception;
	
	public String QueryScheduleDate(InterfaceMessage msg)throws Exception;
	public CommonResp<RespMap> queryScheduleDate(CommonReq<ReqQueryScheduleDate> commReq) throws Exception;
	
	
}
