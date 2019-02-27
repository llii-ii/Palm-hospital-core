package com.kasite.client.yy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.DateOper;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.cache.IDoctorLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Doctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDoctor;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDoctorLocal;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryBaseDoctorLocal;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.ISyncClinicDoctorService;
import com.kasite.core.serviceinterface.module.yy.req.ReqQueryClinicDoctor;
import com.kasite.core.serviceinterface.module.yy.resp.RespQueryClinicDoctor;

/**
 * 
 * @className: ISyncClinicDoctorService
 * @author: lcz
 * @date: 2018年9月27日 下午8:35:53
 */
@Service
public class SyncClinicDoctorService implements ISyncClinicDoctorService{
	
	@Autowired
	private IBasicService basicService;
	@Autowired
	private IDoctorLocalCache doctorLocalCache;
	/**
	 * 如果机构有实现锁号，则返回锁号接口
	 * @param vo
	 * @return
	 */
	protected ICallHisService getCallHisService(AuthInfoVo vo) {
		return HandlerBuilder.get().getCallHisService(vo);
	}
	
	@Override
	public CommonResp<RespQueryClinicDoctor> queryAndSyncClinicDoctor(CommonReq<ReqQueryClinicDoctor> reqComm) throws Exception {
		ReqQueryClinicDoctor req = reqComm.getParam();
		
		//查询本地所有符合条件的医生信息
		CommonResp<RespQueryBaseDoctorLocal> localResp = basicService.queryBaseDoctorLocal(new CommonReq<ReqQueryBaseDoctorLocal>(new ReqQueryBaseDoctorLocal(req.getMsg(), req.getHosId(), req.getDeptCode(), 
				req.getDoctorCode(), null, null, null, req.getDoctorCodes(),req.getDoctorTitleCode())));
		if(!KstHosConstant.SUCCESSCODE.equals(localResp.getCode())) {
			return new CommonResp<RespQueryClinicDoctor>(reqComm, localResp.getRetCode(), localResp.getMessage()); 
		}
		List<RespQueryBaseDoctorLocal> localList = localResp.getData();
		//遍历本地医生信息
		Map<String, RespQueryClinicDoctor> docMap = new ConcurrentHashMap<String, RespQueryClinicDoctor>();
		//需要返回的医生
		Map<String, RespQueryClinicDoctor> respMap = new LinkedHashMap<String, RespQueryClinicDoctor>();
		//本地设置不显示的医生，返回回去时需要隐藏
		Set<String> hideDoc = new HashSet<>();
		
		for (RespQueryBaseDoctorLocal localDoc : localList) {
			RespQueryClinicDoctor resp = new RespQueryClinicDoctor();
			resp.setDeptCode(localDoc.getDeptCode());
			resp.setDeptName(localDoc.getDeptName());
			resp.setDoctorCode(localDoc.getDoctorCode());
			resp.setDoctorName(localDoc.getDoctorName());
			resp.setDoctorTitle(localDoc.getDoctorTitle());
			resp.setDoctorTitleCode(localDoc.getDoctorTitleCode());
			resp.setUrl(localDoc.getPhotoUrl());
			resp.setSpec(localDoc.getSpec());
			resp.setLevel(localDoc.getLevel());
			resp.setLevelId(localDoc.getLevelId());
			resp.setRemark(localDoc.getRemark());
			resp.setSex(localDoc.getSex());
			resp.setIntro(localDoc.getIntro());
			resp.setPrice(localDoc.getPrice()!=null?localDoc.getPrice().toString():null);
			resp.setDoctorIsHalt(0);
			respMap.put(localDoc.getDeptCode()+"_"+localDoc.getDoctorCode(), resp);
			if(localDoc.getIsShow()==null || localDoc.getIsShow()==0) {
				hideDoc.add(localDoc.getDeptCode()+"_"+localDoc.getDoctorCode());
			}
			docMap.put(localDoc.getDoctorCode(), resp);
		}
		//需要更新的医生信息
		List<HisQueryClinicDoctor> updateList = new ArrayList<HisQueryClinicDoctor>();
		//需要新增的医生信息
		List<Doctor> addList = new ArrayList<Doctor>();
		
		//查询his所有符合条件的医生信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("hosId", req.getHosId());
		map.put("deptCode", req.getDeptCode());
		map.put("doctorCode", req.getDoctorCode());
		map.put("workDateStart", req.getWorkDateStart());
		map.put("workDateEnd", req.getWorkDateEnd());
		map.put("doctorCodes", req.getDoctorCodes());
		map.put("doctorTitleCode", req.getDoctorTitleCode());
		ICallHisService callHisService = getCallHisService(req.getAuthInfo());
		List<HisQueryClinicDoctor> ll = callHisService.queryClinicDoctor(req.getMsg(), map).getListCaseRetCode();
		if(ll!=null && ll.size()>0) {
			for (HisQueryClinicDoctor hisDoc : ll) {
				String deptDocKey = hisDoc.getDeptCode()+"_"+hisDoc.getDoctorCode();
				if(respMap.containsKey(deptDocKey)) {
					RespQueryClinicDoctor resp = respMap.get(deptDocKey);
					//本地已经存在，且数据和his端不一致时，需要进行更新
					if(StringUtil.isNotBlank(hisDoc.getDoctorTitleCode()) && (StringUtil.isBlank(resp.getDoctorTitleCode()) || !hisDoc.getDoctorTitleCode().equals(resp.getDoctorTitleCode()))
							||	StringUtil.isNotBlank(hisDoc.getDoctorTitle()) && (StringUtil.isBlank(resp.getDoctorTitle()) || !hisDoc.getDoctorTitle().equals(resp.getDoctorTitle()))
							||	StringUtil.isNotBlank(hisDoc.getSpec()) && (StringUtil.isBlank(resp.getSpec()) || !hisDoc.getSpec().equals(resp.getSpec()))
						) {
						updateList.add(hisDoc);
					}
					//更新返回到前端的医生数据
					if(StringUtil.isNotBlank(hisDoc.getDoctorTitle())) {
						resp.setDoctorTitle(hisDoc.getDoctorTitle());
					}
					if(StringUtil.isNotBlank(hisDoc.getDoctorTitleCode())) {
						resp.setDoctorTitleCode(hisDoc.getDoctorTitleCode());
					}
					if(StringUtil.isNotBlank(hisDoc.getSpec())) {
						resp.setSpec(hisDoc.getSpec());
					}
					resp.setDoctorIsHalt(hisDoc.getDoctorIsHalt());
				}else {
					//判断本地是否已经存在坐诊其他科室的医生，存在时需要更新医生信息
					if(docMap.containsKey(hisDoc.getDoctorCode())) {
						RespQueryClinicDoctor resp = docMap.get(hisDoc.getDoctorCode());
						//本地已经存在，且数据和his端不一致时，需要进行更新
						if(StringUtil.isNotBlank(hisDoc.getDoctorTitleCode()) && (StringUtil.isBlank(resp.getDoctorTitleCode()) || !hisDoc.getDoctorTitleCode().equals(resp.getDoctorTitleCode()))
								||	StringUtil.isNotBlank(hisDoc.getDoctorTitle()) && (StringUtil.isBlank(resp.getDoctorTitle()) || !hisDoc.getDoctorTitle().equals(resp.getDoctorTitle()))
								||	StringUtil.isNotBlank(hisDoc.getSpec()) && (StringUtil.isBlank(resp.getSpec()) || !hisDoc.getSpec().equals(resp.getSpec()))
							) {
							updateList.add(hisDoc);
						}
					}
					//新增一条医生的坐诊信息
					Doctor addDoc = new Doctor();
					addDoc.setDeptCode(hisDoc.getDeptCode());
					addDoc.setDeptName(hisDoc.getDeptName());
					addDoc.setDoctorCode(hisDoc.getDoctorCode());
					addDoc.setDoctorName(hisDoc.getDoctorName());
					addDoc.setTitle(hisDoc.getDoctorTitle());
					addDoc.setTitleCode(hisDoc.getDoctorTitleCode());
					addDoc.setPhotoUrl(hisDoc.getUrl());
					addDoc.setSpec(hisDoc.getSpec());
					addDoc.setLevelName(hisDoc.getLevel());
					addDoc.setLevelId(hisDoc.getLevelId());
					addDoc.setRemark(hisDoc.getRemark());
					addDoc.setIntroduction(hisDoc.getIntro());
					addDoc.setRemark(hisDoc.getRemark());
					addDoc.setDoctorSex(hisDoc.getSex());
					addDoc.setIsShow(1);
					addDoc.setHosId(req.getHosId());
					addDoc.setCreateTime(DateOper.getNowDateTime());
					addDoc.setUpdateTime(DateOper.getNowDateTime());
					if(StringUtil.isNotBlank(hisDoc.getPrice())) {
						addDoc.setPrice(Integer.parseInt(hisDoc.getPrice()));
					}
					addList.add(addDoc);
					
					//新增返回到前端的医生信息
					RespQueryClinicDoctor resp = new RespQueryClinicDoctor();
					BeanCopyUtils.copyProperties(hisDoc, resp, null);
					respMap.put(hisDoc.getDeptCode()+"_"+hisDoc.getDoctorCode(), resp);
				}
			}
		}
		boolean refreshDoctorCache = false;
		if(updateList.size()>0) {
			refreshDoctorCache = true;
			//更新
			for (HisQueryClinicDoctor hisDoc : updateList) {
				CommonResp<RespMap> upResp = basicService.updateDoctor(new CommonReq<ReqUpdateDoctorLocal>(new ReqUpdateDoctorLocal(req.getMsg(), 
						null, hisDoc.getDoctorCode(), hisDoc.getSpec(), null, hisDoc.getDoctorTitle(), hisDoc.getDoctorTitleCode(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)));
				if(!KstHosConstant.SUCCESSCODE.equals(upResp.getCode())) {
					return new CommonResp<>(reqComm, upResp.getRetCode(), upResp.getMessage());
				}
			}
		}
		if(addList.size()>0) {
			refreshDoctorCache = true;
			//新增
			CommonResp<RespMap> addResp = basicService.addDoctor(new CommonReq<ReqAddDoctor>(new ReqAddDoctor(req.getMsg(), addList, null, null, null, null, null, null, null, null, null, null, null, null,null, null, null, null,null, null, null, null)));
			if(!KstHosConstant.SUCCESSCODE.equals(addResp.getCode())) {
				return new CommonResp<>(reqComm, addResp.getRetCode(), addResp.getMessage());
			}
		}
		if(refreshDoctorCache) {
			doctorLocalCache.load();
		}
		
		//遍历医生信息把需要隐藏的医生移除
		if(!hideDoc.isEmpty()) {
			for (String key : hideDoc) {
				respMap.remove(key);
			}
		}
		return new CommonResp<RespQueryClinicDoctor>(reqComm, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, Arrays.asList(respMap.values().toArray(new RespQueryClinicDoctor[] {})));
	}
}
