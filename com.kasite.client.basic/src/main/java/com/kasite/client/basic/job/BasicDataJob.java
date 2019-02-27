package com.kasite.client.basic.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coreframework.util.StringUtil;
import com.kasite.client.basic.dao.IDeptMapper;
import com.kasite.client.basic.dao.IDoctorMapper;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.serviceinterface.module.basic.dbo.Dept;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDept;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryBaseDoctor;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author linjianfa
 * @Description: TODO 从HIS那边同步基础科室医生！
 * @version: V1.0 2017年10月30日 下午3:34:12
 */
@Component
public class BasicDataJob{
	private boolean flag = true;
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);
	@Autowired
	IDoctorMapper doctorMapper;
	@Autowired
	IDeptMapper deptMapper;
	@Autowired
	KasiteConfigMap config;
	
	public void syncBasicDataJob() {
		// 这里捕获异常这么写，为了某些数据引起异常不影响其他数据继续新增或更新
		try {
			if (flag && config.isStartJob(BasicDataJob.class)) {
				flag = false;
				this.syncBasicDeptAndDoctor();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			LogUtil.error(log, e);
			log.error(e);
		} finally {
			flag = true;
		}
	}

	private Dept parse2Dept(HisQueryBaseDept baseDept) {
		Dept dept = new Dept();
		dept.setAttachBuilding(baseDept.getAttachBuilding());
		dept.setCoordinateX(baseDept.getCoordinateX());
		dept.setCoordinateY(baseDept.getCoordinateY());
		dept.setDeptAddr(baseDept.getDeptAddr());
		dept.setDeptBrief(baseDept.getDeptBrief());
		dept.setDeptCode(baseDept.getDeptCode());
		dept.setDeptId(baseDept.getDeptId());
		dept.setDeptName(baseDept.getDeptName());
		dept.setDeptTel(baseDept.getDeptTel());
		dept.setDeptType(baseDept.getDeptType());
		dept.setDeptUid(baseDept.getDeptUid());
		dept.setFloorNum(baseDept.getFloorNum());
		dept.setHosId(baseDept.getHosId());
		dept.setIsShow(baseDept.getIsShow());
		dept.setOrderCol(baseDept.getOrderCol());
		dept.setParentDeptCode(baseDept.getParentDeptCode());
		dept.setPhotoUrl(baseDept.getPhotoUrl());
		dept.setRemark(baseDept.getRemark());
		return dept;
	}
	
	public Doctor parse2Doctor(HisQueryBaseDoctor baseDoctor) {
		Doctor doc = new Doctor();
		doc.setDeptCode(baseDoctor.getDeptCode());
		doc.setDeptName(baseDoctor.getDeptName());
		doc.setDoctorCode(baseDoctor.getDoctorCode());
		doc.setDoctorName(baseDoctor.getDoctorName());
		doc.setDoctorSex(baseDoctor.getDoctorSex());
		doc.setDoctorType(baseDoctor.getDoctorType());
		doc.setDoctoruId(baseDoctor.getDoctoruId());
		doc.setHosId(baseDoctor.getHosId());
		doc.setIntroduction(baseDoctor.getIntroduction());
		doc.setIsShow(baseDoctor.getIsShow());
		doc.setLevelId(baseDoctor.getLevelId());
		doc.setLevelName(baseDoctor.getLevelName());
		doc.setOrderCol(baseDoctor.getOrderCol());
		doc.setPhotoUrl(baseDoctor.getPhotoUrl());
		doc.setPrice(baseDoctor.getPrice());
		doc.setRemark(baseDoctor.getRemark());
		doc.setSpec(baseDoctor.getSpec());
		doc.setTel(baseDoctor.getTel());
		doc.setTitle(baseDoctor.getTitle());
		doc.setTitleCode(baseDoctor.getTitleCode());
		return doc;
	}
	private void syncBasicDeptAndDoctor() {
		try {
			String orderId = "BasicDataJob_"+System.currentTimeMillis();
			InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(BasicDataJob.class,"queryBaseDept", 
					KasiteConfig.getOrgCode(), orderId, "syncBasicDeptAndDoctor",
					null, null,null);
					
			String hosid = KasiteConfig.getOrgCode();
			Map<String, String> paramMap = new HashMap<String, String>(16);
			paramMap.put("hosId", hosid);
			List<HisQueryBaseDept> deptList = HandlerBuilder.get().getCallHisService(hosid)
					.queryBaseDept(msg,paramMap).getListCaseRetCode();
			if (deptList == null || deptList.size() == 0) {
				log.error("获取科室的列表为空");
				return;
			}
			// 获取所有的科室
			for (int i = 0; i < deptList.size(); i++) {
				HisQueryBaseDept baseDept = deptList.get(i);
				Dept dept = parse2Dept(baseDept);
				// 存在更新
				Dept exists = deptMapper.selectByPrimaryKey(dept.getDeptCode());
				if (exists!=null) {
					deptMapper.updateByPrimaryKeySelective(dept);
				} else {
					dept.setHosId(hosid);
					deptMapper.insertSelective(dept);
				}
				paramMap.put("deptCode", dept.getDeptCode());
				paramMap.put("deptName", dept.getDeptName());
				// 获取医生
				List<HisQueryBaseDoctor> baseDoctorList = HandlerBuilder.get().getCallHisService(hosid)
						.queryBaseDoctor(msg,paramMap).getListCaseRetCode();
				if (baseDoctorList == null || baseDoctorList.size() == 0) {
					log.error( "科室编码为" + dept.getDeptCode() + "的医生列表为空");
					continue;
				}
				for (int j = 0; j < baseDoctorList.size(); j++) {
					HisQueryBaseDoctor baseDoctor = baseDoctorList.get(j);
					Doctor doctor = parse2Doctor(baseDoctor);
					// 存在更新
					Example example = new Example(Doctor.class);
					Criteria criteria = example.createCriteria();
					criteria.andEqualTo("doctorCode", doctor.getDoctorCode());
					if(StringUtil.isNotBlank(doctor.getDeptCode())) {
						criteria.andEqualTo("deptCode", doctor.getDeptCode());
					}
					int num = doctorMapper.selectCountByExample(example);
					if (num>0) {
						doctorMapper.updateByExampleSelective(doctor, example);
					} else {
						doctor.setHosId(KasiteConfig.getOrgCode());
						doctorMapper.insertSelective(doctor);
					}
				}
			}
		} catch (Exception e) {
			log.error("同步基础数据异常", e);
			e.printStackTrace();
		}
	}

}
