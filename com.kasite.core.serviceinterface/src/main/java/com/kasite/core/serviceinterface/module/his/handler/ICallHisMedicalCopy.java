package com.kasite.core.serviceinterface.module.his.handler;

import java.util.List;
import java.util.Map;

import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryMedicalRecords;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOperationInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryPatientInfoByNo;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * 病历复印相关接口
 * @author daiyanshui
 *
 */
public interface ICallHisMedicalCopy extends ICallHis{
	
	/**
	 * 根据病案号查询病人信息
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryPatientInfoByNo> queryPatientInfoByNos(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;
	
	/**
	 * 获取病历清单
	 * @param msg
	 * @param map
	 * @return
	 */
	CommonResp<HisQueryMedicalRecords> queryMedicalRecords(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;
	
	/**
	 * 获取手术信息
	 * @param msg
	 * @param map
	 * @return
	 * */
	CommonResp<HisQueryOperationInfo> queryOperationInfo(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;
	
	/**
	 * 病例复印请求信息回写
	 * @param msg
	 * @param map
	 * @return
	 * */
	CommonResp<RespMap> backInfoToHis(InterfaceMessage msg, Map<String, String> paramMap) throws Exception;
}
