package com.kasite.client.basic.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.basic.dao.IDoctorMapper;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.cache.IDoctorLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @className: DoctorLocalCache
 * @author: lcz
 * @date: 2018年7月30日 下午8:44:27
 */
@Component
public class DoctorLocalCache implements IDoctorLocalCache{
	
	@Autowired
	IDoctorMapper doctorMapper;
	
	private Map<String, Doctor> cacheMap;
	
	@PostConstruct
	public void init() {
		load();
	}
	@Override
	public void load(String hosId,String deptCode, String doctorCode) {
		Example example = new Example(Doctor.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(deptCode)) {
			criteria.andEqualTo("deptCode", deptCode);
		}
		criteria.andEqualTo("doctorCode", doctorCode);
		//先查询原有信息
		List<Doctor> list = doctorMapper.selectByExample(example);
		for (Doctor doctor : list) {
			set(doctor);
		}
	}
	
	@Override
	public void load(List<Doctor> docList) {
		for (Doctor query : docList) {
			Example example = new Example(Doctor.class);
			Criteria criteria = example.createCriteria();
			if(StringUtil.isNotBlank(query.getDeptCode())) {
				criteria.andEqualTo("deptCode", query.getDeptCode());
			}
			criteria.andEqualTo("doctorCode", query.getDoctorCode());
			//先查询原有信息
			List<Doctor> list = doctorMapper.selectByExample(example);
			for (Doctor doctor : list) {
				set(doctor);
			}
		}
	}
	@Override
	public void load() {
		synchronized (this) {
			if(cacheMap==null) {
				cacheMap = new ConcurrentHashMap<String, Doctor>();
			}else {
				cacheMap.clear();
			}
			List<Doctor> list = doctorMapper.selectAll();
			for (Doctor doctor : list) {
				set(doctor);
			}
		}
	}
	
	@Override
	public Doctor get(String key) {
		return cacheMap.get(key);
	}
	@Override
	public Doctor get(String deptCode,String doctorCode) {
		return cacheMap.get(deptCode+"_"+doctorCode);
	}
	@Override
	public Doctor set(Doctor doctor) {
		if(null != doctor) {
			return cacheMap.put(doctor.getDeptCode()+"_"+doctor.getDoctorCode(), doctor);
		}
		return null;
	}
}
