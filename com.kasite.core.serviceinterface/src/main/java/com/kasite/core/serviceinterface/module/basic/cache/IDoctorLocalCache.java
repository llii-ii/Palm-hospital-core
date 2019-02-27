package com.kasite.core.serviceinterface.module.basic.cache;

import java.util.List;

import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;

/**
 * 医生本地缓存
 * @className: DoctorLocalCache
 * @author: lcz
 * @date: 2018年7月30日 下午8:06:20
 */

public interface IDoctorLocalCache {
	/**
	 * 获取缓存医生信息
	 * @Description: 
	 * @param key	格式: deptCode+"_"+doctorCode
	 * @return
	 */
	Doctor get(String key);
	
	Doctor get(String deptCode,String doctorCode);
	Doctor set(Doctor doctor);
	/**
	 * 加载数据
	 * @Description:
	 */
	void load();
	
	void load(String hosId, String deptCode, String doctorCode);
	
	void load(List<Doctor> docList);

}
