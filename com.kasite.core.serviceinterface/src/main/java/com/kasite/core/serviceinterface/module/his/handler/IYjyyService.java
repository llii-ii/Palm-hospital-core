package com.kasite.core.serviceinterface.module.his.handler;

import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisGetAppointReceiptInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryExamItemList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryScheduleDate;
import com.kasite.core.serviceinterface.module.his.resp.HisQuerySignalSourceList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 医技预约接口
 * @author daiyanshui
 *
 */
public interface IYjyyService extends ICallHis{

	/**
	 * 8.2.1	获取开单项目列表
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<HisQueryExamItemList> QueryExamItemList(InterfaceMessage msg,Map<String, String> paramMap) throws Exception;
	/**
	 * 	获取医技预约号源列表
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<HisQuerySignalSourceList> QuerySignalSourceList(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	/**
	 * 	医技预约
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<RespMap> MedicalAppoint(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	/**
	 * 	医技取消
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<RespMap> CancelAppoint(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	/**
	 * 	医技回执单
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<HisGetAppointReceiptInfo> GetAppointReceiptInfo(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	/**
	 * 	医技获得可预约的日期
	 * @param msg
	 * @param paramList
	 * @return
	 */
	CommonResp<HisQueryScheduleDate> QueryScheduleDate(InterfaceMessage msg,Map<String, String> paramMap)throws Exception;
	
}
