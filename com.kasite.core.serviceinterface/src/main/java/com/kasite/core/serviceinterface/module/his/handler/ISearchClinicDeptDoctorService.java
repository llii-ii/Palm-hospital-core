package com.kasite.core.serviceinterface.module.his.handler;

import java.util.List;
import java.util.Map;

import com.kasite.core.common.service.ICallHis;
import com.kasite.core.serviceinterface.module.his.resp.HisSearchClinicDeptAndDoctor;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 搜索门诊科室、医生
 * @className: ISearchClinicDeptDoctorService
 * @author: lcz
 * @date: 2018年9月18日 下午4:40:03
 */
public interface ISearchClinicDeptDoctorService extends ICallHis{
	/**
	 * 搜索门诊科室、医生
	 * @Description: 
	 * @param msg
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<HisSearchClinicDeptAndDoctor> searchClinicDeptAndDoctor(InterfaceMessage msg,Map<String, String> map)throws Exception;
}
