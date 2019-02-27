package com.kasite.client.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItemDetails;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItemInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisReportItems;
import com.kasite.core.serviceinterface.module.report.IReportService;
import com.kasite.core.serviceinterface.module.report.req.ReqGetReportInfo;
import com.kasite.core.serviceinterface.module.report.req.ReqGetReportList;
import com.kasite.core.serviceinterface.module.report.req.ReqGetTjReportInfo;
import com.kasite.core.serviceinterface.module.report.resp.RespGetReportInfo;
import com.kasite.core.serviceinterface.module.report.resp.RespGetReportList;
import com.kasite.core.serviceinterface.module.report.resp.RespGetTjReportInfo;
import com.kasite.core.serviceinterface.module.report.resp.RespReportItemDetail;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf TODO
 */
@Service("report.ReportWs")
public class IReportServiceImpl implements IReportService {

	@Autowired
	IBasicService basicService;
	
	/**
	 * 查询报告单列表
	 * 
	 * @see com.yihu.hos.service.report.IReportService#GetReportList(com.yihu.wsgw.api.InterfaceMessage)
	 */
	@Override
	public String GetReportList(InterfaceMessage msg) throws Exception{
		return this.getReportList(new CommonReq<ReqGetReportList>(new ReqGetReportList(msg))).toResult();
	}


	/**
	 * 获得报告单信息
	 * 
	 * @see com.yihu.hos.service.report.IReportService#GetReportInfo(com.yihu.wsgw.api.InterfaceMessage)
	 */
	@Override
	public String GetReportInfo(InterfaceMessage msg) throws Exception{
		return this.getReportInfo(new CommonReq<ReqGetReportInfo>(new ReqGetReportInfo(msg))).toResult();
	}

	@Override
	public CommonResp<RespGetReportList> getReportList(CommonReq<ReqGetReportList> commReq) throws Exception {
		ReqGetReportList reqVo = commReq.getParam();
		int pIndex = 0;
		int pSize = 0;
		int pCount = 0;
		List<HisReportItems> list = null;
		PageVo page = reqVo.getPage();
		if (page != null) {
			pIndex = page.getPIndex();
			pSize = page.getPSize();
			pCount = page.getPCount();
		}
		
		String memberId = reqVo.getMemberId();
		String cardNo = reqVo.getCardNo();
		String cardType = reqVo.getCardType();
		String openId = reqVo.getOpenId();
		Map<String, String> paramMap = new HashMap<String, String>(16);
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, paramMap);
		paramMap.put(ApiKey.HisGetReportList.reportType.name(), reqVo.getReportType());
		paramMap.put(ApiKey.HisGetReportList.cardType.name(), reqVo.getCardType());
		paramMap.put(ApiKey.HisGetReportList.cardNo.name(), reqVo.getCardNo());
		paramMap.put(ApiKey.HisGetReportList.diyJson.name(), reqVo.getDiyJson());
		paramMap.put(ApiKey.HisGetReportList.startDate.name(), reqVo.getStartDate());
		paramMap.put(ApiKey.HisGetReportList.endDate.name(), reqVo.getEndDate());
		
		
		if (pSize > 0) {
			if (pIndex == 0) {
				pCount = 0;
				pCount = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryReportCount(reqVo.getMsg(),paramMap);
				page.setPCount(pCount);
			}
			paramMap.put(ApiKey.HisGetReportList.pIndex.name(), pIndex + "");
			paramMap.put(ApiKey.HisGetReportList.pSize.name(), pSize + "");
			list = HandlerBuilder.get()
					.getCallHisService(commReq.getParam().getAuthInfo())
					.queryReportList(reqVo.getMsg(),paramMap)
					.getListCaseRetCode();
		} else {
			list = HandlerBuilder.get()
					.getCallHisService(commReq.getParam().getAuthInfo())
					.queryReportList(reqVo.getMsg(),paramMap)
					.getListCaseRetCode();
		}
		List<RespGetReportList> respList = new ArrayList<RespGetReportList>();
		if(null != list) {
			for (HisReportItems item : list) {
				RespGetReportList resp = new RespGetReportList();
				resp.setItemName(item.getItemName());
				resp.setPatientName(item.getPatientName());
				resp.setReportId(item.getReportId());
				resp.setReportType(item.getReportType());
				resp.setState(item.getState());
				resp.setSubmissionTime(item.getSubmissionTime());
				resp.setEventNo(item.getEventNo());
				resp.setSampleNo(item.getSampleNo());
				respList.add(resp);
			}
		}
		return new CommonResp<RespGetReportList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,page);
	}

	@Override
	public CommonResp<RespGetReportInfo> getReportInfo(CommonReq<ReqGetReportInfo> commReq) throws Exception {
		ReqGetReportInfo req = commReq.getParam();
		
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put(ApiKey.HisGetReportInfo.reportId.name(), req.getReportId());
		paramMap.put(ApiKey.HisGetReportInfo.reportType.name(), req.getReportType());
		paramMap.put(ApiKey.HisGetReportInfo.eventNo.name(), req.getEventNo());
		paramMap.put(ApiKey.HisGetReportInfo.sampleNo.name(), req.getSampleNo());
		paramMap.put(ApiKey.HisGetReportInfo.startDate.name(), req.getSubmissionTime());
		paramMap.put(ApiKey.HisGetReportList.diyJson.name(), req.getDiyJson());
		HisReportItemInfo info = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo())
				.queryReportDetail(req.getMsg(),paramMap)
				.getDataCaseRetCode();
		RespGetReportInfo resp = new RespGetReportInfo();
		if (info == null) {
			return new CommonResp<RespGetReportInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_XMLERROR);
		}
		resp.setReportTitle(info.getReportTitle());
		resp.setClinicCard(info.getClinicCard());
		resp.setUserType(info.getUserType());
		resp.setHosBedNo(info.getHosBedNo());
		resp.setSigningTime(info.getSigningTime());
		resp.setChecker(info.getChecker());
		resp.setBarCode(info.getBarCode());
		resp.setSex(info.getSex());
		resp.setAge(info.getAge());
		resp.setIsEmergency(info.getIsEmergency());
		resp.setPatientName(info.getPatientName());
		resp.setSampleNumber(info.getSampleNumber());
		resp.setSampleType(info.getSampleType());
		resp.setApplicationDepartment(info.getApplicationDepartment());
		resp.setCheckToSee(info.getCheckSee());
		resp.setSubmissionTime(info.getSubmissionTime());
		resp.setRemark(info.getRemark());
		resp.setReportingPhysicians(info.getReportingPhysicians());
		resp.setReportTime(info.getReportTime());
		resp.setInspector(info.getInspector());
		resp.setItemNum(info.getItemNum());
		resp.setClinicNo(info.getClinicNo());
		resp.setHosUserNo(info.getHosUserNo());
		List<RespReportItemDetail> data_1 = new ArrayList<RespReportItemDetail>();
		if (info.getReportItemDetails()!=null && info.getReportItemDetails().size() > 0) {
			List<HisReportItemDetails> listDetails = info.getReportItemDetails();
			for (int j = 0; j < listDetails.size(); j++) {
				HisReportItemDetails detail = listDetails.get(j);
				RespReportItemDetail respDetail = new RespReportItemDetail();
				respDetail.setItemDetailsName(detail.getItemDetailsName());
				respDetail.setResultValue(detail.getResultValue());
				respDetail.setUnit(detail.getUnit());
				respDetail.setReferenceValues(detail.getReferenceValues());
				respDetail.setIsNormal(detail.getIsNormal());
				respDetail.setExFlag(detail.getExFlag());
				respDetail.setRange(detail.getRange());
				respDetail.setSeFlag(detail.getSeFlag());
				respDetail.setWords(detail.getWords());
				respDetail.setGermName(detail.getGermName());
				respDetail.setGrowStatus(detail.getGrowStatus());
				data_1.add(respDetail);
			}
		}
		resp.setData_1(data_1);
		return new CommonResp<RespGetReportInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}


	@Override
	public String GetTjReportInfo(InterfaceMessage msg) throws Exception {
		return this.GetTjReportInfo(new CommonReq<ReqGetTjReportInfo>(new ReqGetTjReportInfo(msg))).toResult();
	}


	@Override
	public CommonResp<RespGetTjReportInfo> GetTjReportInfo(CommonReq<ReqGetTjReportInfo> commReq) throws Exception {
		ReqGetTjReportInfo req = commReq.getParam();
		
		Map<String, String> paramMap = new HashMap<String, String>(16);
		paramMap.put(ApiKey.HisGetReportInfo.reportId.name(), req.getReportId());
		RespGetTjReportInfo resp = new RespGetTjReportInfo();
		resp = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo())
				.queryTjReportDetail(req.getMsg(),paramMap)
				.getDataCaseRetCode();
		
		if (resp == null) {
			return new CommonResp<RespGetTjReportInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_XMLERROR);
		}
		return new CommonResp<RespGetTjReportInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	
}
