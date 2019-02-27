package com.kasite.client.business.module.backstage.bill.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.basic.cache.DictLocalCache;
import com.kasite.client.business.job.BillCheckSybchroJob;
import com.kasite.client.business.module.backstage.UploadFileController;
import com.kasite.client.business.module.backstage.bill.dao.BankCheckDao;
import com.kasite.client.business.module.backstage.bill.dao.BillChannelRFDao;
import com.kasite.client.business.module.backstage.bill.dao.BillCheckDao;
import com.kasite.client.business.module.backstage.bill.dao.BillMerchRFDao;
import com.kasite.client.business.module.backstage.bill.dao.ChannelBankCheckDao;
import com.kasite.client.business.module.backstage.bill.export.ExportBankBillCheckVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBillChannelRfVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBillCheckVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBillMerchRFVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBillRFVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBillVo;
import com.kasite.client.business.module.backstage.bill.export.ExportChannelReportVo;
import com.kasite.client.business.module.backstage.bill.export.ExportBankReportVo;
import com.kasite.client.business.module.backstage.bill.export.ExportHisBillVo;
import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.client.pay.bean.dbo.Bill;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.client.pay.dao.IHisBillMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.CommonCode;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.ExportExcel;
import com.kasite.core.common.util.FileUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.ZipUtils;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.his.resp.HisBill;
import com.kasite.core.serviceinterface.module.order.dto.OrderVo;
import com.kasite.core.serviceinterface.module.order.dto.PayOrderDetailVo;
import com.kasite.core.serviceinterface.module.order.req.ReqSynchroBill;
import com.kasite.core.serviceinterface.module.pay.IBillRFService;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.dbo.BankMoneyCheck;
import com.kasite.core.serviceinterface.module.pay.dbo.BillChannelRF;
import com.kasite.core.serviceinterface.module.pay.dbo.BillCheck;
import com.kasite.core.serviceinterface.module.pay.dbo.BillMerchRF;
import com.kasite.core.serviceinterface.module.pay.dbo.ChannelBankCheck;
import com.kasite.core.serviceinterface.module.pay.dto.BankCheckCountVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillByMonthVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckCountVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillDetailVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillRFVo;
import com.kasite.core.serviceinterface.module.pay.dto.ExceptionBillCountVo;
import com.kasite.core.serviceinterface.module.pay.dto.QueryBillCheck;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryBill;
import com.kasite.core.serviceinterface.module.pay.req.ReqUpdateBankCheckInfo;
import com.kasite.core.serviceinterface.module.pay.resp.RespBillCheckCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespExceptionBillCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBankCheckCount;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBankMoneyCheck;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillChannelRF;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillDetail;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillForDate;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillForMonth;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillMerchRF;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryBillRFForDate;
import com.kasite.core.serviceinterface.module.pay.resp.RespQueryChannelBankCheck;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("billRF.BillRFWS")
public class BillRFServiceImpl implements IBillRFService {

	@Autowired
	private BillChannelRFDao billChannelRFDao;
	
	@Autowired
	private BillMerchRFDao billMerchRFDao;
	
	@Autowired
	private BankCheckDao bankCheckDao;
	
	@Autowired
	private BillCheckDao billCheckDao;
	
	@Autowired
	private IBillMapper billMapper;
	
	@Autowired
	private ChannelBankCheckDao channelBankCheckDao;
	
	@Autowired
	IPayService payService;
	
	@Autowired
	IHisBillMapper hisBillMapper;
	
	@Autowired
	IOrderMapper orderMapper;
	
	@Autowired
	BillCheckSybchroJob billCheckSybchroJob;
	
	@Autowired
	protected DictLocalCache dictLocalCache;
	
	@Override
	public CommonResp<RespQueryBillForDate> queryBillList(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		QueryBillCheck queryBean = new QueryBillCheck();
		Integer initCheckState = req.getInitCheckState();
		queryBean.setCheckState(req.getCheckState());
		queryBean.setChannelId(req.getChannelId());
		queryBean.setStartDate(req.getStartDate());
		queryBean.setEndDate(req.getEndDate());
		queryBean.setOrderId(req.getOrderId());
		queryBean.setHisOrderNo(req.getHisOrderNo());
		queryBean.setMerchNo(req.getMerchNo());
		queryBean.setPayMethod(req.getPayType());
		queryBean.setOrderRule(req.getOrderRule());
		queryBean.setConfigKey(req.getConfigKey());
		
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			queryBean.setServiceIdList(serviceIdList);
		}
		
		PageInfo<BillCheckVo> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<BillCheckVo> retList = billCheckDao.findBillList(queryBean, initCheckState);
		List<RespQueryBillForDate> respList = new ArrayList<>();
		if(retList != null) {
			page = new PageInfo<>(retList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (BillCheckVo vo : page.getList()) {
				RespQueryBillForDate resp = new RespQueryBillForDate();
				resp.setId(vo.getId());
				resp.setOrderNo(vo.getOrderNo());
				String createTime = DateUtils.getTimestampToStr(vo.getCreateDate());
				String transTime = DateUtils.getTimestampToStr(vo.getTransDate());
				resp.setCreateDate(createTime);
				resp.setTransDate(transTime);
				resp.setBillType(vo.getBillType());
				resp.setPayMethod(vo.getPayMethod());
				resp.setPayMethodName(vo.getPayMethodName());
				resp.setChannelId(vo.getChannelId());
				resp.setChannelName(vo.getChannelName());
				resp.setCheckState(vo.getCheckState());
				resp.setHisOrderNo(vo.getHisOrderNo());
				resp.setHisPrice(vo.getHisPrice());
				resp.setMerchNo(vo.getMerchNo());
				resp.setMerchPrice(vo.getMerchPrice());
				resp.setDealState(vo.getDealState());
				resp.setDealWay(vo.getDealWay());
				resp.setNickName(vo.getNickName());
				resp.setCaseHistory(vo.getCaseHistory());
				String serviceId = vo.getServiceId();
				String serviceValue = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceValue = dictLocalCache.getValue("serviceid", serviceId);
				}
				resp.setService(StringUtil.isNotBlank(serviceValue)?serviceValue:serviceId);
				respList.add(resp);
			}
			return new CommonResp<RespQueryBillForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
		}else {
			return new CommonResp<RespQueryBillForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}
	
	@Override
	public CommonResp<RespQueryBillForDate> queryBillListForException(CommonReq<ReqQueryBill> commReq)
			throws Exception {
		ReqQueryBill req = commReq.getParam();
		QueryBillCheck queryBean = new QueryBillCheck();
		queryBean.setStartDate(req.getStartDate());
		queryBean.setEndDate(req.getEndDate());
		queryBean.setPayMethod(req.getPayType());
		queryBean.setChannelId(req.getChannelId());
		queryBean.setOrderId(req.getOrderId());
		queryBean.setHisOrderNo(req.getHisOrderNo());
		queryBean.setMerchNo(req.getMerchNo());
		queryBean.setDealWay(req.getDealWay());
		queryBean.setDealState(req.getDealState());
		queryBean.setOrderRule(req.getOrderRule());
		queryBean.setConfigKey(req.getConfigKey());
		
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			queryBean.setServiceIdList(serviceIdList);
		}
		
		PageInfo<BillCheckVo> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<BillCheckVo> retList = billCheckDao.findBillListForException(queryBean);
		List<RespQueryBillForDate> respList = new ArrayList<>();
		if(retList != null) {
			page = new PageInfo<>(retList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (BillCheckVo vo : page.getList()) {
				RespQueryBillForDate resp = new RespQueryBillForDate();
				resp.setId(vo.getId());
				resp.setOrderNo(vo.getOrderNo());
				String transTime = DateUtils.getTimestampToStr(vo.getTransDate());
				String createTime = DateUtils.getTimestampToStr(vo.getCreateDate());
				String updateTime = DateUtils.getTimestampToStr(vo.getUpdateDate());
				String dealTime = DateUtils.getTimestampToStr(vo.getDealDate());
				
				resp.setTransDate(transTime);
				resp.setCreateDate(createTime);
				resp.setBillType(vo.getBillType());
				resp.setPayMethod(vo.getPayMethod());
				resp.setPayMethodName(vo.getPayMethodName());
				resp.setChannelId(vo.getChannelId());
				resp.setChannelName(vo.getChannelName());
				resp.setCheckState(vo.getCheckState());
				resp.setHisOrderNo(vo.getHisOrderNo());
				resp.setHisPrice(vo.getHisPrice());
				resp.setMerchNo(vo.getMerchNo());
				resp.setMerchPrice(vo.getMerchPrice());
				resp.setDealState(vo.getDealState());
				resp.setDealWay(vo.getDealWay());
				resp.setUpdateDate(updateTime);
				resp.setDealDate(dealTime);
				resp.setNickName(vo.getNickName());
				resp.setCaseHistory(vo.getCaseHistory());
				String serviceId = vo.getServiceId();
				String serviceValue = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceValue = dictLocalCache.getValue("serviceid", serviceId);
				}
				resp.setService(StringUtil.isNotBlank(serviceValue)?serviceValue:serviceId);
				respList.add(resp);
			}
			
			return new CommonResp<RespQueryBillForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
		}else {
			return new CommonResp<RespQueryBillForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	@Override
	public CommonResp<RespQueryBillForMonth> queryBillRFListForMonth(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String queryYear = req.getQueryYear();
		List<BillByMonthVo> retList = billCheckDao.findBillListForMonth(queryYear);
		List<RespQueryBillForMonth> respList = new ArrayList<>();
		if(retList != null) {
			for (BillByMonthVo vo : retList) {
				RespQueryBillForMonth resp = new RespQueryBillForMonth();
				BeanCopyUtils.copyProperties(vo, resp, null);
				String payMethod = vo.getPayMethod();
				ChannelTypeEnum payInfo = KasiteConfig.getPayInfoByType(payMethod);
				if(payInfo != null) {
					resp.setPayMethodName(payInfo.getTitle());
				}
				String queryMonth = vo.getTransMonth();
				BillByMonthVo payVo = billCheckDao.findSumMoneyForMonth(queryYear, queryMonth, 1);
				Long hisMoney = 0L;
				Long merchMoney = 0L;
				if(payVo != null) {
					hisMoney = payVo.getHisMoney();
					merchMoney = payVo.getMerchMoney();
				}
				BillByMonthVo refundVo = billCheckDao.findSumMoneyForMonth(queryYear, queryMonth, 2);
				if(refundVo != null) {
					hisMoney = hisMoney - refundVo.getHisMoney();
					merchMoney = merchMoney - refundVo.getMerchMoney();
				}
				resp.setHisMoney(hisMoney);
				resp.setMerchMoney(merchMoney);
				respList.add(resp);
			}
			return new CommonResp<RespQueryBillForMonth>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}else {
			return new CommonResp<RespQueryBillForMonth>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	@Override
	public CommonResp<RespBillCheckCount> queryBillCheckCount(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String channelId = req.getChannelId();
		String payType = req.getPayType();
		String transType = req.getTransType();
		String configKey = req.getConfigKey();
		
		List<String> serviceIdList = getServiceIdList(transType);
		
		List<BillCheckCountVo> retList = billCheckDao.findBillCheckCount(startDate, endDate, channelId, payType, serviceIdList, configKey);
		if(retList != null) {
			int totalCount = 0;
			RespBillCheckCount resp = new RespBillCheckCount();
			for (BillCheckCountVo vo : retList) {
				Integer checkState = vo.getCheckState();
				int count = vo.getCount();
				totalCount += count;
				if(KstHosConstant.BILL_CHECK_STATE_0.equals(checkState)) {
					resp.setCheckState0Count(count);
				}else if(KstHosConstant.BILL_CHECK_STATE_1.equals(checkState)) {
					resp.setCheckState1Count(count);
				}else if(KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE.equals(checkState)) {
					resp.setCheckStateT1Count(count);
				}else if(KstHosConstant.BILL_CHECK_STATE_2.equals(checkState)){
					resp.setCheckState2Count(count);
				}else {
					continue;
				}
			}
			resp.setTotalCount(totalCount);
			return new CommonResp<RespBillCheckCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
		}
		return new CommonResp<RespBillCheckCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespExceptionBillCount> queryExceptionBillCount(CommonReq<ReqQueryBill> commReq)
			throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String payType = req.getPayType();
		String channelId = req.getChannelId();
		String transType = req.getTransType();
		String configKey = req.getConfigKey();
		List<String> serviceIdList = getServiceIdList(transType);
		
		List<ExceptionBillCountVo> retList = billCheckDao.findExceptionBillCount(startDate, endDate, payType, channelId, serviceIdList, configKey);
		RespExceptionBillCount resp = new RespExceptionBillCount();
		int totalCount = 0;
		if(retList != null) {
			for (ExceptionBillCountVo vo : retList) {
				Integer dealState = vo.getDealState();
				int count = vo.getCount();
				totalCount += count ;
				if(KstHosConstant.BILL_IS_DEAL_0.equals(dealState)) {
					resp.setNoDealCount(count);
				}else if(KstHosConstant.BILL_IS_DEAL_1.equals(dealState)) {
					resp.setIsDealCount(count);
				}else {
					continue;
				}
			}
			resp.setTotalCount(totalCount);
			return new CommonResp<RespExceptionBillCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
		}
		return new CommonResp<RespExceptionBillCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	@Override
	public CommonResp<RespQueryBillRFForDate> queryBillRFListForDate(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String checkState = req.getCheckState();  //对账结果 1账平 0账不平
		Integer dateType = req.getDateType();
		List<BillRFVo> retList = null;
		PageInfo<BillRFVo> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		if(dateType.equals(1)) {
			retList = billChannelRFDao.findBillRFListForDate(startDate, endDate);
		}else if(dateType.equals(2)) {
			retList = billChannelRFDao.findBillRFListForMonth(startDate, endDate);
		}
		List<RespQueryBillRFForDate> respList = new ArrayList<>();
		if(retList != null) {
			page = new PageInfo<>(retList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (BillRFVo vo : page.getList()) {
				RespQueryBillRFForDate resp = new RespQueryBillRFForDate();
				BeanCopyUtils.copyProperties(vo, resp, null);
				Integer dealState = null;
				String queryDate = vo.getBillDate();
				if(dateType.equals(1)) {
					int billIsOk = billChannelRFDao.findBillCountIsOkForDate(queryDate);
					if(billIsOk > 0) {
						dealState = 0; //账不平
					}else {
						int IsDealOk = billChannelRFDao.findBillCountIsDealOkForDate(queryDate);
						if(IsDealOk > 0) {
							dealState = 2;  //处理后的账平
						}else {
							dealState = 1; //账平
						}
					}
				}else if(dateType.equals(2)) {
					int IsDealOk = billChannelRFDao.findBillCountIsDealOkForMonth(queryDate);
					if(IsDealOk > 0) {
						dealState = 2;  //处理后的账平
					}else {
						int billIsOk = billChannelRFDao.findBillCountIsOkForMonth(queryDate);
						if(billIsOk > 0) {
							dealState = 0; //账不平
						}else {
							dealState = 1; //账平
						}
					}
				}
				if(StringUtil.isNotBlank(checkState)) {
					//账不平
					if("0".equals(checkState)) {
						if(dealState == 1 || dealState == 2) {continue;}
					}else if("1".equals(checkState)) { //账平
						if(dealState == 0) {continue;}
					}
				}
				resp.setDealState(dealState);
				respList.add(resp);
			}
			return new CommonResp<RespQueryBillRFForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,req.getPage());
		}else {
			return new CommonResp<RespQueryBillRFForDate>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	@Override
	public CommonResp<RespQueryBillChannelRF> queryBillChannelRF(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		Example example = new Example(BillChannelRF.class);
		example.createCriteria().andEqualTo("date", req.getQueryDate());
		example.setOrderByClause("CHANNELID DESC");
		List<BillChannelRF> retList = billChannelRFDao.selectByExample(example);
		List<RespQueryBillChannelRF> respList = new ArrayList<>();
		if(retList != null) {
			for (BillChannelRF vo : retList) {
				RespQueryBillChannelRF resp = new RespQueryBillChannelRF();
				resp.setBillDate(vo.getDate());	
				resp.setHisBillCount(vo.getHisBillCount());
				resp.setHisSingleBillCount(vo.getHisSingleBillCount());
				resp.setHisBillSum(vo.getHisBillSum());
				resp.setChannelBillCount(vo.getChannelBillCount());
				resp.setMerchBillSum(vo.getMerchBillSum());
				resp.setChannelId(vo.getChannelId());
				resp.setChannelName(vo.getChannelName());
				resp.setChannelSingleBillCount(vo.getChannelSingleBillCount());
				resp.setCheckCount(vo.getCheckCount());
				resp.setDifferPirceCount(vo.getDifferPirceCount());
				resp.setDealState(vo.getDealState());
				resp.setCheckState(vo.getCheckState());
				respList.add(resp);
			}
			return new CommonResp<RespQueryBillChannelRF>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}else {
			return new CommonResp<RespQueryBillChannelRF>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	@Override
	public CommonResp<RespQueryBillMerchRF> queryBillMerchRF(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		Example example = new Example(BillMerchRF.class);
		example.createCriteria().andEqualTo("date", req.getQueryDate());
		example.setOrderByClause("PAYMETHOD DESC");
		List<BillMerchRF> retList = billMerchRFDao.selectByExample(example);
		List<RespQueryBillMerchRF> respList = new ArrayList<>();
		if(retList != null) {
			for (BillMerchRF vo : retList) {
				RespQueryBillMerchRF resp = new RespQueryBillMerchRF();
				BeanCopyUtils.copyProperties(vo, resp, null);
				respList.add(resp);
			}
			return new CommonResp<RespQueryBillMerchRF>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
		}else {
			return new CommonResp<RespQueryBillMerchRF>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		
	}

	@Override
	public CommonResp<RespMap> queryCurrentDate(CommonReq<ReqQueryBill> commReq) throws Exception {
		String currentDate = billChannelRFDao.findCurrentDate();
		RespMap resp = new RespMap();
		resp.put(ApiKey.BillRFPro.CurrentDate, currentDate);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespQueryBankMoneyCheck> queryBankMoneyCheckList(CommonReq<ReqQueryBill> commReq)
			throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String isCheckOut = req.getIsCheckOut();  // 勾兑状态  0未勾兑，1已勾兑，""全部勾兑状态
		String checkState = req.getCheckState();  // 勾兑结果  0账平，1长款，-1短款，""全部勾兑结果
		String bankNo = req.getBankNo();   //银行账号
		Example example = new Example(BankMoneyCheck.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			criteria.andBetween("date", startDate, endDate);
		}
		if(StringUtil.isNotBlank(isCheckOut)) {
			criteria.andEqualTo("isCheck", isCheckOut);
		}
		if(StringUtil.isNotBlank(checkState)) {
			criteria.andEqualTo("checkState", checkState);
		}
		if(StringUtil.isNotBlank(bankNo)) {
			criteria.andEqualTo("bankNo", bankNo);
		}
		example.setOrderByClause(" DATE DESC");
		PageInfo<BankMoneyCheck> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<BankMoneyCheck> retList = bankCheckDao.selectByExample(example);
		List<RespQueryBankMoneyCheck> respList = new ArrayList<>();
		if(retList != null) {
			page = new PageInfo<>(retList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for (BankMoneyCheck vo : page.getList()) {
				RespQueryBankMoneyCheck resp = new RespQueryBankMoneyCheck();
				BeanCopyUtils.copyProperties(vo, resp, null);
				respList.add(resp);
			}
			return new CommonResp<RespQueryBankMoneyCheck>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList, req.getPage());
		}else {
			return new CommonResp<RespQueryBankMoneyCheck>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}
	
	@Override
	public CommonResp<RespQueryBankCheckCount> queryBankCheckCount(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		List<RespQueryBankCheckCount> respList = new ArrayList<>();
		List<BankCheckCountVo> retList = bankCheckDao.findBankCheckListForDate(startDate, endDate);
		if(retList == null || retList.size() == 0) {
			return new CommonResp<RespQueryBankCheckCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		RespQueryBankCheckCount resp = new RespQueryBankCheckCount();
		int isCheck0Count = 0;
		int isCheck1Count = 0;
		for (BankCheckCountVo obj : retList) {
			int isCheck = obj.getIsCheck();
			int count = obj.getCount();
			if(isCheck == 1) {
				isCheck1Count = isCheck1Count + count;
				Integer checkState = obj.getCheckState();
				if(KstHosConstant.BANK_CHECK_STATE_0.equals(checkState)) {
					resp.setCheckState0Count(count);
				}else if(KstHosConstant.BANK_CHECK_STATE_1.equals(checkState)) {
					resp.setCheckState1Count(count);
				}else if(KstHosConstant.BANK_CHECK_STATE_T1.equals(checkState)) {
					resp.setCheckStateT1Count(count);
				}else {
					resp.setCheckState0Count(0);
				}
			}else {
				isCheck0Count = isCheck0Count + count;
			}
		}
		resp.setIsCheck0Count(isCheck0Count);
		resp.setIsCheck1Count(isCheck1Count);
		int totalCount = isCheck0Count + isCheck1Count;
		resp.setTotalCount(totalCount);
		respList.add(resp);
		return new CommonResp<RespQueryBankCheckCount>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}

	@Override
	public CommonResp<RespQueryBillDetail> queryBillDetail(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String billCheckId = req.getBillCheckId();
		BillDetailVo billVo = billCheckDao.findBillDetailById(billCheckId);
		if(billVo == null) {
			return new CommonResp<RespQueryBillDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		RespQueryBillDetail resp = new RespQueryBillDetail();
		BeanCopyUtils.copyProperties(billVo, resp, null);
		String orderId = billVo.getOrderId();
		
		OrderVo order = orderMapper.findCardNoForOrder(orderId);
		if(order == null) {
			order = orderMapper.findCardNoForOrderCheck(orderId);
		}
		if(order != null) {
			resp.setPayMoney(order.getPayMoney());
		}
		
		String hisbizDate = DateUtils.getTimestampToStr(billVo.getHisbizDate());
		String payDate = DateUtils.getTimestampToStr(billVo.getPayDate());
		resp.setHisbizDate(hisbizDate);
		resp.setPayDate(payDate);
		String billDate = DateUtils.getTimestampToStr(billVo.getBillDate());
		resp.setBillDate(billDate);
		String diffReason = "";
		String diffDesc = "";
		Integer billType = resp.getBillType();
		String hisOrderNo = resp.getHisOrderNo();
		String merchNo = resp.getMerchNo();
		Integer hisPrice = resp.getHisPrice();
		Integer merchPrice = resp.getMerchPrice();
		Integer checkState = resp.getCheckState();
		Integer diffPrice = getDiffPrice(hisPrice, merchPrice);
		resp.setDiffPrice(diffPrice);
		
		//长款-his单边账
		if(StringUtil.isEmpty(merchNo)) {
			diffReason = CommonCode.Descr.DIFF_REASON_1.getMessage();
			diffDesc = CommonCode.Descr.DIFF_REASON_1_DECR.getMessage();
		}
		//长款-渠道单边账
		if(StringUtil.isEmpty(hisOrderNo)) {
			diffReason = CommonCode.Descr.DIFF_REASON_2.getMessage();
			diffDesc = CommonCode.Descr.DIFF_REASON_2_DECR.getMessage();
		}
		
		if(KstHosConstant.BILL_CHECK_STATE_1.equals(checkState)) {     //对账长款的状态
			//长款-(应收/应退)金额不一致
			if(hisPrice != null && merchPrice!= null && !hisPrice.equals(merchPrice)) {
				diffReason = CommonCode.Descr.DIFF_REASON_3.getMessage();
				diffDesc = KstHosConstant.BILL_ORDER_TYPE_1.equals(billType)
						?CommonCode.Descr.DIFF_REASON_3_DECR_PAY_G.getMessage():CommonCode.Descr.DIFF_REASON_3_DECR_REFUND_T.getMessage();
			}
		}else if(KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE.equals(checkState)) {  //对账短款的状态
			//短款-(应收/应退)金额不一致
			if(hisPrice != null && merchPrice!= null && !hisPrice.equals(merchPrice)) {
				diffReason = CommonCode.Descr.DIFF_REASON_3.getMessage();
				diffDesc = KstHosConstant.BILL_ORDER_TYPE_1.equals(billType)
						?CommonCode.Descr.DIFF_REASON_3_DECR_PAY_T.getMessage():CommonCode.Descr.DIFF_REASON_3_DECR_REFUND_G.getMessage();
			}
		}
		resp.setDiffReason(diffReason);
		resp.setDiffDesc(diffDesc);
		return new CommonResp<RespQueryBillDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespQueryBillDetail> queryDealBillDetail(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String billCheckId = req.getBillCheckId();
		BillDetailVo billVo = billCheckDao.findBillDetailById(billCheckId);
		if(billVo == null) {
			return new CommonResp<RespQueryBillDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		RespQueryBillDetail resp = new RespQueryBillDetail();
		BeanCopyUtils.copyProperties(billVo, resp, null);
		String hisbizDate = DateUtils.getTimestampToStr(billVo.getHisbizDate());
		String payDate = DateUtils.getTimestampToStr(billVo.getPayDate());
		resp.setHisbizDate(hisbizDate);
		resp.setPayDate(payDate);
		String billDate = DateUtils.getTimestampToStr(billVo.getBillDate());
		resp.setBillDate(billDate);
		Integer hisPrice = resp.getHisPrice();
		Integer merchPrice = resp.getMerchPrice();
		Integer diffPrice = getDiffPrice(hisPrice, merchPrice);
		String diffReason = "";
		String diffDesc = "";
		String dealRemark = resp.getDealRemark();
		if(StringUtil.isNotBlank(dealRemark)) {
			String[] arr = dealRemark.split("-");
			diffReason = arr[0];
			diffDesc = arr[1];
			dealRemark = arr[2];
			if(arr.length == 4) {
				resp.setOrderNo(arr[3]);
			}
		}
		String updateDate = DateUtils.getTimestampToStr(billVo.getUpdateDate());
		resp.setUpdateDate(updateDate);
		String dealDate = DateUtils.getTimestampToStr(billVo.getDealDate());
		resp.setDealDate(dealDate);
		resp.setDiffPrice(diffPrice);
		resp.setDiffReason(diffReason);
		resp.setDiffDesc(diffDesc);
		resp.setDealRemark(dealRemark);
		return new CommonResp<RespQueryBillDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespMap> downloadBillDetailListData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		QueryBillCheck queryBean = new QueryBillCheck();
		Integer initCheckState = req.getInitCheckState();
		queryBean.setCheckState(req.getCheckState());
		queryBean.setChannelId(req.getChannelId());
		queryBean.setDealWay(req.getDealWay());
		queryBean.setStartDate(req.getStartDate());
		queryBean.setEndDate(req.getEndDate());
		queryBean.setOrderId(req.getOrderId());
		queryBean.setHisOrderNo(req.getHisOrderNo());
		queryBean.setMerchNo(req.getMerchNo());
		queryBean.setPayMethod(req.getPayType());
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			queryBean.setServiceIdList(serviceIdList);
		}
		//查询出所有导出的数据
		List<ExportBillCheckVo> dataset = buildBillCheckExportData(queryBean, initCheckState);
		ExportExcel<ExportBillCheckVo> ex = new ExportExcel<ExportBillCheckVo>();
		String[] headers = { "订单交易时间", "订单类型", "订单号", "病人姓名", "病历号/住院号", "医院流水号", "渠道流水号", "应收/退", "应收/退金额（元）","支付方式","实收/退",
				"实收/退金额（元）", "对账结果", "差错金额（元）", "对账时间"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "billDetail";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "账单明细" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	@Override
	public CommonResp<RespMap> downloadExceptionBillData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		QueryBillCheck queryBean = new QueryBillCheck();
		queryBean.setStartDate(req.getStartDate());
		queryBean.setEndDate(req.getEndDate());
		queryBean.setPayMethod(req.getPayType());
		queryBean.setDealWay(req.getDealWay());
		queryBean.setDealState(req.getDealState());
		queryBean.setOrderId(req.getOrderId());
		queryBean.setHisOrderNo(req.getHisOrderNo());
		queryBean.setMerchNo(req.getMerchNo());
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			queryBean.setServiceIdList(serviceIdList);
		}
		
		//查询出所有导出的数据
		List<ExportBillCheckVo> dataset = buildExceptionBillExportData(queryBean);
		ExportExcel<ExportBillCheckVo> ex = new ExportExcel<ExportBillCheckVo>();
		String[] headers = { "订单交易时间", "订单类型", "订单号", "病人姓名", "病历号/住院号", "医院流水号", "渠道流水号", "应收/退", "应收/退金额（元）","支付方式","实收/退",
				"实收/退金额（元）", "对账结果", "差错金额（元）", "对账时间"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "billDetail";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "账单明细" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	@Override
	public CommonResp<RespMap> downloadBankBillListData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String isCheckOut = req.getIsCheckOut();  // 勾兑状态  0未勾兑，1已勾兑
		String checkState = req.getCheckState();  // 勾兑结果  1账平，2长款，3短款
		String bankNo = req.getBankNo();   //银行账号
		Example example = new Example(BankMoneyCheck.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			criteria.andBetween("date", startDate, endDate);
		}
		if(StringUtil.isNotBlank(isCheckOut) && !"-1".equals(isCheckOut)) {
			criteria.andEqualTo("isCheck", isCheckOut);
		}
		if(StringUtil.isNotBlank(checkState) && !"-1".equals(checkState)) {
			criteria.andEqualTo("checkState", checkState);
		}
		if(StringUtil.isNotBlank(bankNo)) {
			criteria.andEqualTo("bankNo", bankNo);
		}
		List<ExportBankBillCheckVo> dataSet = buildBankCheckExportData(example);
		ExportExcel<ExportBankBillCheckVo> ex = new ExportExcel<ExportBankBillCheckVo>();
		String[] headers = { "到账时间", "开户银行", "银行账号", "应到款（元）","实际到款（元）", "勾兑状态", "勾兑人", "勾兑结果", "差错金额（元）"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "bankCheck";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "银行到账勾兑汇总" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataSet, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespMap> downloadBillReportListData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		Date date = DateUtils.formatStringtoDate(req.getQueryDate(), "yyyy-MM-dd");
		String titlename = DateUtils.formatDateToString(date,"yyyy年MM月dd日");
		//交易渠道查询条件
		Example bcfExample = new Example(BillChannelRF.class);
		bcfExample.createCriteria().andEqualTo("date", req.getQueryDate());
		//支付方式报表查询条件
		Example bmfExample = new Example(BillMerchRF.class);
		bmfExample.createCriteria().andEqualTo("date", req.getQueryDate());
		
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "billReport";
		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		//先创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { titlename + " 交易场景-订单对账报表", titlename + " 支付方式-资金对账报表" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		String tempFilename = "data_" + System.currentTimeMillis() + ".xls";
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath);
		OutputStream out = new FileOutputStream(tempFile);
		//1.交易渠道的报表Excel写入
		List<ExportBillChannelRfVo> bcfDataset = buildBillChannelRFExportData(bcfExample);
		ExportExcel<ExportBillChannelRfVo> bcfExport = new ExportExcel<ExportBillChannelRfVo>();
		String[] bcfHeaders = { "交易场景", "医院流水（笔）", "渠道流水（笔）", "已勾兑（笔）","医院单边账（笔）","渠道单边账（笔）",
				"金额不一致（笔）","医院应净收（元）", "渠道实净收（元）","差额","对账结果"};
		bcfExport.exportExcel(sheetNames[0], bcfHeaders, bcfDataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		//2.支付方式的报表Excel写入
		List<ExportBillMerchRFVo> bMfDataset = buildBillMerchRFExportData(bmfExample);
		ExportExcel<ExportBillMerchRFVo> bMfExport = new ExportExcel<ExportBillMerchRFVo>();
		String[] bMfHeaders = { "支付方式", "医院商户号", "所属银行", "银行账号", "医院应净收（元）", "渠道实净收（元）","对账结果"};
		bMfExport.exportExcel(sheetNames[1], bMfHeaders, bMfDataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespMap> downloadBillRFListData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		Integer dateType = req.getDateType(); 
		String checkState = req.getCheckState();
		List<ExportBillRFVo> dataset = buildBillRFExportData(startDate, endDate, dateType, checkState);
		ExportExcel<ExportBillRFVo> ex = new ExportExcel<ExportBillRFVo>();
		String[] headers = { "交易日期", "医院流水（笔）", "渠道流水（笔）", "已勾兑（笔）","医院单边账（笔）","渠道单边账（笔）",
				"金额不一致（笔）","医院应净收（元）", "渠道实净收（元）","对账结果"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "billCount";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separatorChar + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separatorChar + path;
		File tempFile = new File(tempPath);
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { "日账单汇总" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();

		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespMap> downloadAllBillFile(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String channelId = req.getChannelId();
		String fileType = req.getFileType();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "billFile";
		String tempfilePath = KasiteConfig.localConfigPath() + File.separator + path;
		File saveDir = new File(tempfilePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs(); // 如果文件不存在则创建文件夹
		}else if(saveDir.exists()) {
			FileUtils.deleteDir(saveDir);
			saveDir.mkdirs(); // 如果文件存在则先删除再重新创建文件夹
		}
		
		List<ExportBillVo> billDataset = null;
		List<ExportHisBillVo> hisBillDataset = null;
		List<ExportBillCheckVo> billCheckDataset = null;
		List<ExportBillRFVo> billRFDataset = null;
		
		Date date = DateUtils.formatStringtoDate(startDate, "yyyy-MM-dd");
		Date startDateN = DateOper.getNextDay(date, -1);
		date = DateUtils.formatStringtoDate(endDate, "yyyy-MM-dd");
		Date endDateN = DateOper.getNextDay(date, 0);
		
		String startDateNStr = DateUtils.formatDateToString(startDateN, "yyyy-MM-dd");
		String endDateNStr = DateUtils.formatDateToString(endDateN, "yyyy-MM-dd");
		
		//封装数据
		if("ZF".equals(fileType)) {  //智付系统对账文件
			QueryBillCheck billCheckQuery = new QueryBillCheck();
			billCheckQuery.setStartDate(startDate);
			billCheckQuery.setEndDate(endDate);
			billCheckQuery.setChannelId(channelId);
			billCheckQuery.setCheckState(KstHosConstant.ORDERTYPE_999);
			billCheckQuery.setDealWay(0);
			billCheckDataset = buildBillCheckExportData(billCheckQuery,null);
			
			if(StringUtil.isEmpty(channelId)) {
				billRFDataset = buildBillRFExportData(startDateNStr, endDateNStr, 1, null);
				
			}else {
				billRFDataset = buildBillChannelRFExportData(startDateNStr, endDateNStr, channelId);
			}
			//写EXCEL
			if(billCheckDataset != null && billCheckDataset.size() > 0) {
				writeBillCheckExcel(tempfilePath, billCheckDataset);
			}
			if(billRFDataset != null && billRFDataset.size() > 0) {
				writeBillRFExcel(tempfilePath, billRFDataset);
			}
		}else if("MERCH".equals(fileType)) {                    //支付渠道对账文件
			billDataset = buildBillExportData(startDateNStr, endDateNStr, channelId);
			//写EXCEL
			if(billDataset != null && billDataset.size() > 0) {
				writeBillExcel(tempfilePath, billDataset);
			}
		}else if("HIS".equals(fileType)) {                      //医院系统对账文件
			hisBillDataset = buildHisBillExportData(startDateNStr, endDateNStr, channelId);
			//写EXCEL
			if(hisBillDataset != null && hisBillDataset.size() > 0) {
				writeHisBillExcel(tempfilePath, hisBillDataset);
			}
		}else {	//所有对账文件
			QueryBillCheck billCheckQuery = new QueryBillCheck();
			billCheckQuery.setStartDate(startDate);
			billCheckQuery.setCheckState(KstHosConstant.ORDERTYPE_999);
			billCheckQuery.setEndDate(endDate);
			billCheckQuery.setChannelId(channelId);
			billCheckQuery.setDealWay(0);
			
			billCheckDataset = buildBillCheckExportData(billCheckQuery,null);
			if(StringUtil.isEmpty(channelId)) {
				billRFDataset = buildBillRFExportData(startDateNStr, endDateNStr, 1, null);
			}else {
				billRFDataset = buildBillChannelRFExportData(startDateNStr, endDateNStr, channelId);
			}
			billDataset = buildBillExportData(startDateNStr, endDateNStr, channelId);
			hisBillDataset = buildHisBillExportData(startDateNStr, endDateNStr, channelId);
			//写EXCEL
			if(billCheckDataset != null && billCheckDataset.size() > 0) {
				writeBillCheckExcel(tempfilePath, billCheckDataset);
			}
			if(billRFDataset != null && billRFDataset.size() > 0) {
				writeBillRFExcel(tempfilePath, billRFDataset);
			}
			if(billDataset != null && billDataset.size() > 0) {
				writeBillExcel(tempfilePath, billDataset);
			}
			if(hisBillDataset != null && hisBillDataset.size() > 0) {
				writeHisBillExcel(tempfilePath, hisBillDataset);
			}
		}
		String zipPath = tempfilePath + ".zip";
		FileOutputStream fos = new FileOutputStream(new File(zipPath));
		ZipUtils.toZip(tempfilePath, fos,true);
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path + ".zip");
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	@Override
	public CommonResp<RespMap> checkBillSynchro(CommonReq<ReqSynchroBill> commReq) throws Exception {
		ReqSynchroBill req = commReq.getParam();
		String billDate = req.getBillDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(billDate);
		Date yestDate = DateOper.getNextDay(date, -1);
		String startDate = DateOper.formatDate(yestDate, "yyyy-MM-dd");
		String endDate = DateOper.formatDate(date, "yyyy-MM-dd");
		
		billCheckSybchroJob.deal(startDate, endDate, startDate, new Timestamp(date.getTime()));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> checkBillReverse(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String billCheckId = req.getBillCheckId();
		String orderId = req.getOrderNo();  //只有支付订单可以冲正
		String userId = req.getOpenId();
		
		BillCheck billCheck = billCheckDao.selectByPrimaryKey(billCheckId);
		PayOrderDetailVo payOrder = billCheckDao.findHisOrderByOrderId(orderId);
		String diffReason = CommonCode.Descr.DIFF_REASON_2.getMessage();
		String diffDesc = CommonCode.Descr.DIFF_REASON_2_DECR.getMessage();
		String dealRemark = "冲正成功";
		billCheck = buildBillCheck(billCheck, userId, diffReason+"-"+diffDesc+"-"+dealRemark, KstHosConstant.BILL_DEAL_WAY_2);
		if(payOrder != null) {
			billCheck.setHisOrderNo(StringUtil.isBlank(payOrder.getHisSerialNo())?"":payOrder.getHisSerialNo());
			billCheck.setHisPrice(payOrder.getPayMoney()==null?0:payOrder.getPayMoney());
		}
		billCheckDao.updateByPrimaryKeySelective(billCheck);
		updateBillRFInfo(billCheck);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> checkBillRefund(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String billCheckId = req.getBillCheckId();
		String userId = req.getOpenId();
		String refundOrderId = req.getRefundOrderId();
		
		BillCheck billCheck = billCheckDao.selectByPrimaryKey(billCheckId);
		String hisOrderNo = billCheck.getHisOrderNo();
		String merchNo = billCheck.getMerchNo();
		Integer hisPrice = billCheck.getHisPrice();
		Integer merchPrice = billCheck.getMerchPrice();
		Integer billType = billCheck.getBillType();
		String diffReason = "";
		String diffDesc = "";
		//长款-his单边账(退款订单)
		if(StringUtil.isEmpty(merchNo)) {
			diffReason = CommonCode.Descr.DIFF_REASON_1.getMessage();
			diffDesc = CommonCode.Descr.DIFF_REASON_1_DECR.getMessage();
		}
		//长款-渠道单边账(支付订单)
		if(StringUtil.isEmpty(hisOrderNo)) {
			diffReason = CommonCode.Descr.DIFF_REASON_2.getMessage();
			diffDesc = CommonCode.Descr.DIFF_REASON_2_DECR.getMessage();
		}
		//长款-(应收/应退)金额不一致
		if(hisPrice != null && merchPrice!= null && !hisPrice.equals(merchPrice)) {
			diffReason = CommonCode.Descr.DIFF_REASON_3.getMessage();
			diffDesc = KstHosConstant.BILL_ORDER_TYPE_1.equals(billType)
					?CommonCode.Descr.DIFF_REASON_3_DECR_PAY_G.getMessage():CommonCode.Descr.DIFF_REASON_3_DECR_REFUND_T.getMessage();
		}
		String dealRemark = "退费成功";
		billCheck = buildBillCheck(billCheck, userId, diffReason+"-"+diffDesc+"-"+dealRemark+"-"+refundOrderId, KstHosConstant.BILL_DEAL_WAY_1);
		RespMap resp = new RespMap();
		billCheckDao.updateByPrimaryKeySelective(billCheck);
		resp.put(ApiKey.BillRFPro.BillMap, billCheck);
		updateBillRFInfo(billCheck);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespMap> billCheckIn(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		String billCheckId = req.getBillCheckId();
		Integer billSingleType = req.getBillSingleType();
		String userId = req.getOpenId();
		BillCheck billCheck = billCheckDao.selectByPrimaryKey(billCheckId);
		if( !KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE.equals(billCheck.getCheckState()) ){
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_PARAM);
		}
		Integer billType = billCheck.getBillType();
		String diffReason = new Integer(1).equals(billSingleType)?CommonCode.Descr.DIFF_REASON_1.getMessage():CommonCode.Descr.DIFF_REASON_3.getMessage();
		String diffDesc = new Integer(1).equals(billSingleType)?CommonCode.Descr.DIFF_REASON_1_DECR.getMessage():
			(KstHosConstant.BILL_ORDER_TYPE_1.equals(billType)?
					CommonCode.Descr.DIFF_REASON_3_DECR_PAY_T.getMessage():
						CommonCode.Descr.DIFF_REASON_3_DECR_REFUND_G.getMessage());
		String dealRemark = "登账成功";
		billCheck = this.buildBillCheck(billCheck, userId, (diffReason+"-"+diffDesc+"-"+dealRemark), KstHosConstant.BILL_DEAL_WAY_3);
		billCheckDao.updateByPrimaryKeySelective(billCheck);
		updateBillRFInfo(billCheck);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespQueryChannelBankCheck> queryChannelBankCheckList(CommonReq<ReqQueryBill> commReq)
			throws Exception {
		List<RespQueryChannelBankCheck> respList = new ArrayList<>();
		ReqQueryBill req = commReq.getParam();
		String channelId = req.getChannelId();
		String payType = req.getPayType();
		String queryDate = req.getQueryDate();
		String transType = req.getTransType();
		Integer orderRule = req.getOrderRule();
		
		Example example = new Example(ChannelBankCheck.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("date", queryDate);
		if(StringUtil.isNotBlank(channelId)) {
			criteria.andEqualTo("channelId", channelId);	
		}
		if(StringUtil.isNotBlank(payType)) {
			criteria.andEqualTo("payMethod", payType);	
		}
		if(StringUtil.isNotBlank(transType)) {
			criteria.andEqualTo("serviceId", transType);
		}
		if(new Integer(1).equals(orderRule)) {
			example.setOrderByClause(" bankNo DESC");
		}else {
			example.setOrderByClause(" channelId DESC");
		}
		List<ChannelBankCheck> thisList = channelBankCheckDao.selectByExample(example);
		if(thisList != null) {
			for (ChannelBankCheck channelBankCheck : thisList) {
				RespQueryChannelBankCheck resp = new RespQueryChannelBankCheck();
				BeanCopyUtils.copyProperties(channelBankCheck, resp, null);
				String serviceName = "";
				if(KstHosConstant.OUTPATIENT.equals(channelBankCheck.getServiceId())) {
					serviceName = dictLocalCache.getValue(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
				}else if(KstHosConstant.HOSPITALIZATION.equals(channelBankCheck.getServiceId())) {
					serviceName = dictLocalCache.getValue(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
				}
				resp.setService(channelBankCheck.getServiceId());
				resp.setTransType(serviceName);
				respList.add(resp);
			}
		}
		return new CommonResp<RespQueryChannelBankCheck>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	@Override
	public CommonResp<RespMap> downloadChannelBankReportListData(CommonReq<ReqQueryBill> commReq) throws Exception {
		ReqQueryBill req = commReq.getParam();
		Date queryDate = DateUtils.formatStringtoDate(req.getQueryDate(), "yyyy-MM-dd");
		String titlename = DateUtils.formatDateToString(queryDate,"yyyy年MM月dd日");
		String channelId = req.getChannelId();
		String payType = req.getPayType();
		String transType = req.getTransType();
		Integer orderRule = req.getOrderRule();
		
		List<String> serviceIdList = getServiceIdList(transType);
		
		Example example = new Example(ChannelBankCheck.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("date", queryDate);
		if(StringUtil.isNotBlank(channelId)) {
			criteria.andEqualTo("channelId", channelId);	
		}
		if(StringUtil.isNotBlank(payType)) {
			criteria.andEqualTo("payMethod", payType);	
		}
		if(serviceIdList != null && serviceIdList.size() > 0) {
			criteria.andIn("serviceId", serviceIdList);
		}
		String sheetName = "";
		String[] headers = null;
		List<ExportBankReportVo> bankDataSet = new ArrayList<>();
		List<ExportChannelReportVo> channelDataSet = new ArrayList<>();
		if(new Integer(1).equals(orderRule)) {
			headers = new String[]{"日期", "所属银行", "银行账号", "交易渠道", "服务类型", "支付方式", "医院应到款（元）","渠道实到款（元）", "差额(实到款-应到款)", "对账结果"};
			sheetName = titlename + "-关联银行账号金额统计报表";
			bankDataSet = buildBankReportExportData(example);
			example.setOrderByClause(" bankNo DESC");
		}else {
			headers = new String[]{"日期", "交易渠道", "服务类型", "所属银行", "银行账号", "支付方式", "医院应到款（元）","渠道实到款（元）", "差额(实到款-应到款)", "对账结果"};
			sheetName = titlename + "-渠道金额统计报表";
			channelDataSet = buildChannelReportExportData(example);
			example.setOrderByClause(" channelId DESC");
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "data_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "channelBankCheck";

		File saveDir = new File(KasiteConfig.localConfigPath() + File.separator + path);
		if (saveDir.exists()) {
			FileUtils.deleteDir(saveDir); //如果文件存在则删除再重新创建文件夹
		}
		saveDir.mkdirs();
		path = path + File.separator + tempFilename;
		String tempPath = KasiteConfig.localConfigPath() + File.separator + path;
		File tempFile = new File(tempPath); // 临时文件目录
		
		OutputStream out = new FileOutputStream(tempFile);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String[] sheetNames = { sheetName };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		if(new Integer(1).equals(orderRule)) {
			ExportExcel<ExportBankReportVo> ex = new ExportExcel<ExportBankReportVo>();
			ex.exportExcel(sheetNames[0], headers, bankDataSet, out,
					"yyyy-MM-dd HH:mm", workbook);
		}else{
			ExportExcel<ExportChannelReportVo> ex = new ExportExcel<ExportChannelReportVo>();
			ex.exportExcel(sheetNames[0], headers, channelDataSet, out,
					"yyyy-MM-dd HH:mm", workbook);
		}
		workbook.write(out);
		out.close();
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	/**
	 * 封装渠道统计报表数据
	 * 
	 * @param example
	 * @return
	 */
	private List<ExportChannelReportVo> buildChannelReportExportData(Example example){
		List<ChannelBankCheck> thisList = channelBankCheckDao.selectByExample(example);
		List<ExportChannelReportVo> dataset = new ArrayList<ExportChannelReportVo>();
		if( thisList!=null && thisList.size()>0 ){
			for(ChannelBankCheck channelBankCheckVo : thisList ){
				ExportChannelReportVo exportVo = new ExportChannelReportVo();
				exportVo.setTransdate(channelBankCheckVo.getDate());
				exportVo.setChannel(channelBankCheckVo.getChannelName());
				String serviceId = channelBankCheckVo.getServiceId();
				String serviceName = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceName = dictLocalCache.getValue("serviceid", serviceId);
				}
				exportVo.setTransType(serviceName);
				exportVo.setBankName(channelBankCheckVo.getBankName());
				exportVo.setBankNo(channelBankCheckVo.getBankNo());
				exportVo.setPayMethod(channelBankCheckVo.getPayMethodName());
				exportVo.setPayAbleMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPayAbleMoney()));
				exportVo.setPaideMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPaideMoney()));
				exportVo.setDiffMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPaideMoney() - channelBankCheckVo.getPayAbleMoney()));
				String checkState = KstHosConstant.BILL_CHECK_RF_STATE_1.equals(channelBankCheckVo.getCheckState())?"账不平":"账平";
				exportVo.setCheckState(checkState);
				dataset.add(exportVo);
			}
		}
		return dataset;
	}
	
	/**
	 * 封装关联银行账号金额统计报表数据
	 * 
	 * @param example
	 * @return
	 */
	private List<ExportBankReportVo> buildBankReportExportData(Example example){
		List<ChannelBankCheck> thisList = channelBankCheckDao.selectByExample(example);
		List<ExportBankReportVo> dataset = new ArrayList<ExportBankReportVo>();
		if( thisList!=null && thisList.size()>0 ){
			for(ChannelBankCheck channelBankCheckVo : thisList ){
				ExportBankReportVo exportVo = new ExportBankReportVo();
				exportVo.setTransdate(channelBankCheckVo.getDate());
				exportVo.setBankName(channelBankCheckVo.getBankName());
				exportVo.setBankNo(channelBankCheckVo.getBankNo());
				String serviceId = channelBankCheckVo.getServiceId();
				String serviceName = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceName = dictLocalCache.getValue("serviceid", serviceId);
				}
				exportVo.setTransType(serviceName);
				exportVo.setPayMethod(channelBankCheckVo.getPayMethodName());
				exportVo.setChannel(channelBankCheckVo.getChannelName());
				exportVo.setPayAbleMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPayAbleMoney()));
				exportVo.setPaideMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPaideMoney()));
				exportVo.setDiffMoney(StringUtil.fenChangeYuan(channelBankCheckVo.getPaideMoney() - channelBankCheckVo.getPayAbleMoney()));
				String checkState = KstHosConstant.BILL_CHECK_RF_STATE_1.equals(channelBankCheckVo.getCheckState())?"账不平":"账平";
				exportVo.setCheckState(checkState);
				dataset.add(exportVo);
			}
		}
		return dataset;
	}
	
	/**
	 * <p>Description: 封装更新的对账账单信息</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年6月1日
	 * @author zhaoy  
	 * @param @param billCheck
	 * @param @param remark
	 * @param @param dealWay
	 * @param @return
	 * @return BillCheck 
	 * @throws
	 */
	private BillCheck buildBillCheck(BillCheck billCheck, String userId, String remark, Integer dealWay) {
		billCheck.setDealway(dealWay);
		billCheck.setDealby(userId);
		billCheck.setDealDate(DateOper.getNowDateTime());
		billCheck.setDealState(KstHosConstant.BILL_IS_DEAL_1);
		billCheck.setDealRemark(remark);
		billCheck.setUpdateBy(userId);
		billCheck.setUpdateDate(DateOper.getNowDateTime());
		//设置核对状态为已处理账平
		billCheck.setCheckState(KstHosConstant.BILL_CHECK_STATE_0);
		return billCheck;
	}
	
	/**
	 * 更新对账报表信息
	 */
	private void updateBillRFInfo(BillCheck billCheck) {
		Timestamp checkDate = billCheck.getTransDate();
		Date date = DateUtils.formatStringtoDate(DateUtils.getTimestampToStr(checkDate), "yyyy-MM-dd HH:mm:ss");
		String startDate = DateUtils.getStartForDayString(date);
		String endDate = DateUtils.getEndForDayString(date);
		String thisDate = DateUtils.getTimestampToStr(checkDate, "yyyy-MM-dd");
		
		Example bcExample = new Example(BillCheck.class);
		bcExample.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("channelId", billCheck.getChannelId()).andNotEqualTo("checkState", 0);
		int channelCount = billCheckDao.selectCountByExample(bcExample);
		if(channelCount == 0) {
			//交易渠道报表更新
			Example bcfExample = new Example(BillChannelRF.class);
			bcfExample.createCriteria().andEqualTo("date", thisDate).andEqualTo("channelId", billCheck.getChannelId());
			BillChannelRF bcfBean = new BillChannelRF();
			bcfBean.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);
			bcfBean.setDealState(KstHosConstant.BILL_IS_DEAL_1);
			billChannelRFDao.updateByExampleSelective(bcfBean, bcfExample);
			
		}
		bcExample = new Example(BillCheck.class);
		bcExample.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("configkey", billCheck.getConfigkey()).andNotEqualTo("checkState", 0);
		int merchCount = billCheckDao.selectCountByExample(bcExample);
		if(merchCount == 0) {
			//支付方式报表更新
			Example bmfExample = new Example(BillMerchRF.class);
			bmfExample.createCriteria().andEqualTo("date", thisDate).andEqualTo("configkey", billCheck.getConfigkey());
			BillMerchRF bmfBean = new BillMerchRF();
			bmfBean.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);
			bmfBean.setDealState(KstHosConstant.BILL_IS_DEAL_1);
			billMerchRFDao.updateByExampleSelective(bmfBean, bmfExample);
		}
		bcExample = new Example(BillCheck.class);
		bcExample.createCriteria().andBetween("transDate", startDate, endDate).andEqualTo("channelId", billCheck.getChannelId()).andEqualTo("configkey", billCheck.getConfigkey()).andNotEqualTo("checkState", 0);
		int count = billCheckDao.selectCountByExample(bcExample);
		if(count == 0) {
			//渠道商户统计报表
			Example cbcExample = new Example(ChannelBankCheck.class);
			//门诊的服务ID集合
			Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
			//住院的服务ID集合
			Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
			Dictionary dictionary = dictLocalCache.get("serviceid", billCheck.getServiceId());
			String service = "";
			if(dictionary.getUpId().equals(pOutPatient.getId())) {
				service = pOutPatient.getKeyword();
			}else if(dictionary.getUpId().equals(pHospitalization.getId())) {
				service = pHospitalization.getKeyword();
			}
			cbcExample.createCriteria().andEqualTo("date", thisDate).andEqualTo("serviceId", service).andEqualTo("channelId", billCheck.getChannelId()).andEqualTo("configKey", billCheck.getConfigkey());
			ChannelBankCheck cbcBean = new ChannelBankCheck();
			cbcBean.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_2);
			channelBankCheckDao.updateByExampleSelective(cbcBean, cbcExample);
		}
	}
	
	/**
	 * 封装对账明细列表导出的数据集合
	 * 
	 * @param queryBean
	 * @param initCheckState
	 * @return
	 * @throws Exception
	 */
	public List<ExportBillCheckVo> buildBillCheckExportData(QueryBillCheck queryBean, Integer initCheckState) throws Exception{
		//查询出所有导出的数据
		List<BillCheckVo> list = billCheckDao.findBillList(queryBean, initCheckState);
		List<ExportBillCheckVo> dataset = new ArrayList<ExportBillCheckVo>();
		if( list!=null && list.size()>0 ){
			for(BillCheckVo billCheckVo : list ){
				ExportBillCheckVo exportVo = new ExportBillCheckVo();
				String createTime = DateUtils.getTimestampToStr(billCheckVo.getCreateDate());
				String transDate = DateUtils.getTimestampToStr(billCheckVo.getTransDate());
				String serviceId = billCheckVo.getServiceId();
				String serviceValue = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceValue = dictLocalCache.getValue("serviceid", serviceId);
				}
				
				exportVo.setTransDate(transDate);
				exportVo.setTransType(serviceValue);
				exportVo.setOrderId(billCheckVo.getOrderId());
				exportVo.setNickName(billCheckVo.getNickName());
				exportVo.setCaseHistory(billCheckVo.getCaseHistory());
				exportVo.setHisOrderNo(billCheckVo.getHisOrderNo());
				exportVo.setMerchNo(billCheckVo.getMerchNo());
				exportVo.setBillType1(KstHosConstant.BILL_ORDER_TYPE_1.equals(billCheckVo.getBillType())?"应收":"应退");
				exportVo.setHisPrice(StringUtil.fenChangeYuan(billCheckVo.getHisPrice()));
				String payMethod = billCheckVo.getPayMethodName() == null ? "": billCheckVo.getPayMethodName();
				exportVo.setPayMethod(payMethod);
				exportVo.setBillType2(KstHosConstant.BILL_ORDER_TYPE_1.equals(billCheckVo.getBillType())?"实收":"实退");
				exportVo.setMerchPrice(StringUtil.fenChangeYuan(billCheckVo.getMerchPrice()));
				exportVo.setCheckState(KstHosConstant.BILL_CHECK_STATE_1.equals(billCheckVo.getCheckState())
						?"长款":(KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE.equals(billCheckVo.getCheckState())
							?"短款":"账平"));
				if(KstHosConstant.BILL_IS_DEAL_1.equals(billCheckVo.getDealState())) {
					exportVo.setCheckState("账平(处置后)");
				}
				Integer diffPrice = getDiffPrice(billCheckVo.getHisPrice(), billCheckVo.getMerchPrice());
				exportVo.setDiffPrice(StringUtil.fenChangeYuan(diffPrice));
				exportVo.setCheckDate(createTime);
				dataset.add(exportVo);
			}
		}
		return dataset;
	}
	
	/**
	 * 构建异常账单导出数据
	 * 
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	public List<ExportBillCheckVo> buildExceptionBillExportData(QueryBillCheck queryBean) throws Exception{
		//查询出所有导出的数据
		List<BillCheckVo> list = billCheckDao.findBillListForException(queryBean);
		List<ExportBillCheckVo> dataset = new ArrayList<ExportBillCheckVo>();
		if( list!=null && list.size()>0 ){
			for(BillCheckVo billCheckVo : list ){
				ExportBillCheckVo exportVo = new ExportBillCheckVo();
				String createTime = DateUtils.getTimestampToStr(billCheckVo.getCreateDate());
				String transDate = DateUtils.getTimestampToStr(billCheckVo.getTransDate());
				String serviceId = billCheckVo.getServiceId();
				String serviceValue = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceValue = dictLocalCache.getValue("serviceid", serviceId);
				}
				
				exportVo.setTransDate(transDate);
				exportVo.setTransType(serviceValue);
				exportVo.setOrderId(billCheckVo.getOrderId());
				exportVo.setNickName(billCheckVo.getNickName());
				exportVo.setCaseHistory(billCheckVo.getCaseHistory());
				exportVo.setHisOrderNo(billCheckVo.getHisOrderNo());
				exportVo.setMerchNo(billCheckVo.getMerchNo());
				exportVo.setBillType1(KstHosConstant.BILL_ORDER_TYPE_1.equals(billCheckVo.getBillType())?"应收":"应退");
				exportVo.setHisPrice(StringUtil.fenChangeYuan(billCheckVo.getHisPrice()));
				String payMethod = billCheckVo.getPayMethodName() == null ? "": billCheckVo.getPayMethodName();
				exportVo.setPayMethod(payMethod);
				exportVo.setBillType2(KstHosConstant.BILL_ORDER_TYPE_1.equals(billCheckVo.getBillType())?"实收":"实退");
				exportVo.setMerchPrice(StringUtil.fenChangeYuan(billCheckVo.getMerchPrice()));
				exportVo.setCheckState(KstHosConstant.BILL_CHECK_STATE_1.equals(billCheckVo.getCheckState())
						?"长款":(KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE.equals(billCheckVo.getCheckState())
							?"短款":"账平"));
				if(KstHosConstant.BILL_IS_DEAL_1.equals(billCheckVo.getDealState())) {
					exportVo.setCheckState("账平(处置后)");
				}
				Integer diffPrice = getDiffPrice(billCheckVo.getHisPrice(), billCheckVo.getMerchPrice());
				exportVo.setDiffPrice(StringUtil.fenChangeYuan(diffPrice));
				exportVo.setCheckDate(createTime);
				dataset.add(exportVo);
			}
		}
		return dataset;
	}
	
	/***
	 * 封装银行到账勾兑列表导出的数据集合
	 * 
	 * @param startDate
	 * @param endDate
	 * @param isCheckOut
	 * @param checkState
	 * @param bankNo
	 * @return
	 * @throws Exception
	 */
	public List<ExportBankBillCheckVo> buildBankCheckExportData(Example example) throws Exception{
		List<BankMoneyCheck> bankMoneyCheckList = bankCheckDao.selectByExample(example);
		//封装导出数据
		List<ExportBankBillCheckVo> dataSet = new ArrayList<ExportBankBillCheckVo>();
		if(bankMoneyCheckList != null && bankMoneyCheckList.size() > 0) {
			bankMoneyCheckList.forEach((BankMoneyCheck bankCheck) -> {
				ExportBankBillCheckVo vo = new ExportBankBillCheckVo();
				vo.setCheckDate(bankCheck.getDate());
				vo.setBankName(bankCheck.getBankName());
				vo.setBankNo(bankCheck.getBankNo());
				vo.setIsCheck(KstHosConstant.BANK_IS_CHECK_1.equals(bankCheck.getIsCheck())?"已勾兑":"未勾兑");
				vo.setPayAbleMoney(StringUtil.fenChangeYuan(bankCheck.getPayAbleMoney()));
				if(KstHosConstant.BANK_IS_CHECK_0.equals(bankCheck.getIsCheck())) {
					vo.setCheckState("--");
					vo.setOperUserName("--");
					vo.setDiffMoney("--");
					vo.setPaideMoney("--");
				}else if(KstHosConstant.BANK_IS_CHECK_1.equals(bankCheck.getIsCheck())) {
					vo.setPaideMoney(StringUtil.fenChangeYuan(bankCheck.getPaideMoney()));
					vo.setCheckState(KstHosConstant.BANK_CHECK_STATE_1.equals(bankCheck.getCheckState())
							?"长款":(KstHosConstant.BANK_CHECK_STATE_T1.equals(bankCheck.getCheckState())
									?"短款":"账平"));
					vo.setOperUserName(bankCheck.getUpdateBy());
					Long diffMoney = getDiffPrice(bankCheck.getPayAbleMoney(), bankCheck.getPaideMoney());
					vo.setDiffMoney(StringUtil.fenChangeYuan(diffMoney));
				}
				dataSet.add(vo);
			});
		}
		return dataSet;
	}
	
	/**
	 * 
	 * <p>Description: 封装支付方式(商户:微信、支付宝、银联等)的账单导出对象数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年9月13日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param channelId
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportBillVo> 
	 * @throws
	 */
	public List<ExportBillVo> buildBillExportData(String startDate, String endDate, String channelId) throws Exception {
		Example example = new Example(Bill.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			criteria.andBetween("transDate", startDate, endDate);
		}
		if(StringUtil.isNotBlank(channelId)) {
			criteria.andEqualTo("channelId", channelId);
		}
		List<Bill> billList = billMapper.selectByExample(example);
		List<ExportBillVo> dataset = new ArrayList<ExportBillVo>();
		billList.forEach((Bill bill) -> {
			ExportBillVo vo = new ExportBillVo();
			Integer orderType = new Integer(bill.getOrderType());
			vo.setTransDate(DateUtils.getTimestampToStr(bill.getTransDate()));
			String channelId_ = bill.getChannelId();
			String channelName = "";
			if(StringUtils.isNotBlank(channelId_)) {
				channelName = KasiteConfig.getChannelById(channelId_);
			}
			vo.setChannelName(channelName);
			vo.setHosName(KasiteConfig.getOrgName());
			vo.setMerchNo(bill.getMerchNo());
			vo.setOrderId(bill.getOrderId());
			vo.setOrderType(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)?"支付订单":"退款订单");
			vo.setRefundMerchNo(bill.getRefundMerchNo()==null?"":bill.getRefundMerchNo());
			vo.setRefundOrderId(bill.getRefundOrderId()==null?"":bill.getRefundOrderId());
			vo.setRefundPrice(StringUtil.fenChangeYuan(bill.getRefundPrice()));
			vo.setTransactions(StringUtil.fenChangeYuan(bill.getTransactions()));
			if(StringUtil.isNotBlank(bill.getRefundOrderId())) {
				Example example_ = new Example(Bill.class);
				example_.createCriteria().andEqualTo("merchNo", bill.getMerchNo()).andEqualTo("orderType", KstHosConstant.BILL_ORDER_TYPE_1);
				Bill payBill = billMapper.selectOneByExample(example_);
				if(payBill != null) {
					vo.setTransactions(StringUtil.fenChangeYuan(payBill.getTransactions()));
				}
			}
			vo.setTradeType(bill.getTradeType());
			
			dataset.add(vo);
		});
		return dataset;
	}
	
	/**
	 * 
	 * <p>Description: 封装his的账单导出对象数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年9月13日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param channelId
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportHisBillVo> 
	 * @throws
	 */
	public List<ExportHisBillVo> buildHisBillExportData(String startDate, String endDate, String channelId) throws Exception {
		List<HisBill> hisBillList = billMapper.queryHisBillList(startDate, endDate, channelId);
		List<ExportHisBillVo> dataset = new ArrayList<ExportHisBillVo>();
		hisBillList.forEach((HisBill hisBill) -> {
			ExportHisBillVo vo = new ExportHisBillVo();
			String channelId_ = hisBill.getChannelId();
			String channelName = "";
			if(StringUtils.isNotBlank(channelId_)) {
				channelName = KasiteConfig.getChannelById(channelId_);
			}
			vo.setChannelName(channelName);
			Timestamp date = hisBill.getCreateDate();
			vo.setCreateDate(String.valueOf(date));
			vo.setHisBizState(String.valueOf(hisBill.getHisBizState()));
			vo.setHisOrderId(hisBill.getHisOrderId());
			vo.setHisOrderType(KstHosConstant.BILL_ORDER_TYPE_1.equals(hisBill.getHisOrderType())?"支付订单":"退款订单");
			vo.setMerchOrderNo(hisBill.getMerchOrderNo());
			vo.setOrderId(hisBill.getOrderId());
			vo.setOrderMemo(hisBill.getOrderMemo());
			vo.setPayMoney(StringUtil.fenChangeYuan(hisBill.getPayMoney()));
			vo.setPriceName(hisBill.getPriceName());
			vo.setRefundMoney(StringUtil.fenChangeYuan(hisBill.getRefundMoney()));
			vo.setRefundOrderId(hisBill.getRefundOrderId());
			vo.setTotalMoney(StringUtil.fenChangeYuan(hisBill.getTotalMoney()));
			vo.setTransDate(DateUtils.getTimestampToStr(hisBill.getTransDate()));
			dataset.add(vo);
		});
		return dataset;
	}
	
	/**
	 * <p>Description: 封装对账报表(支付场景)列表导出的数据集合</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param checkState
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportBillRFVo> 
	 * @throws
	 */
	public List<ExportBillChannelRfVo> buildBillChannelRFExportData(Example example) throws Exception {
		List<BillChannelRF> bcrfList = billChannelRFDao.selectByExample(example);
		List<ExportBillChannelRfVo> dataset = new ArrayList<ExportBillChannelRfVo>();
		if(bcrfList != null) {
			int hisBillCount = 0;
			int channelBillCount = 0;
			int checkCount = 0;
			int hisSingleBillCount = 0;
			int channelSingleBillCount = 0;
			int differPirceCount = 0;
			Long hisBillSum = 0L;
			Long merchBillSum = 0L;
			String checkState = "账平";
			for (BillChannelRF bcrf : bcrfList) {
				ExportBillChannelRfVo vo = new ExportBillChannelRfVo();
				//合计数据
				hisBillCount += bcrf.getHisBillCount(); 
				channelBillCount += bcrf.getChannelBillCount();
				checkCount += bcrf.getCheckCount();
				hisSingleBillCount += bcrf.getHisSingleBillCount();
				channelSingleBillCount += bcrf.getChannelSingleBillCount();
				differPirceCount += bcrf.getDifferPirceCount();
				if(KstHosConstant.BILL_CHECK_RF_STATE_0.equals(bcrf.getCheckState())) { 
					checkState = "账不平";
				}
				String channelId = bcrf.getChannelId();
				String channelName = KasiteConfig.getChannelById(channelId);
				vo.setChannelName(channelName);
				vo.setHisBillCount(bcrf.getHisBillCount());
				vo.setChannelBillCount(bcrf.getChannelBillCount());
				vo.setCheckCount(bcrf.getCheckCount());
				vo.setHisSingleBillCount(bcrf.getHisSingleBillCount());
				vo.setChannelSingleBillCount(bcrf.getChannelSingleBillCount());
				vo.setDifferPirceCount(bcrf.getDifferPirceCount());
				hisBillSum = hisBillSum + bcrf.getHisBillSum();
				merchBillSum = merchBillSum + bcrf.getMerchBillSum();
				Long diffMoney = getDiffPrice(bcrf.getHisBillSum(), bcrf.getMerchBillSum());
				vo.setHisBillSum(StringUtil.fenChangeYuan(bcrf.getHisBillSum()));
				vo.setMerchBillSum(StringUtil.fenChangeYuan(bcrf.getMerchBillSum()));
				vo.setDiffMoney(StringUtil.fenChangeYuan(diffMoney));
				vo.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_0.equals(bcrf.getCheckState())?"账不平":"账平");
				if(bcrf.getDealState() == 1) {
					vo.setCheckState("账平(处置后)");
				}
				dataset.add(vo);
			}
			ExportBillChannelRfVo vo = new ExportBillChannelRfVo();
			vo.setChannelName("合计");
			vo.setHisBillCount(hisBillCount);
			vo.setChannelBillCount(channelBillCount);
			vo.setCheckCount(checkCount);
			vo.setHisSingleBillCount(hisSingleBillCount);
			vo.setChannelSingleBillCount(channelSingleBillCount);
			vo.setDifferPirceCount(differPirceCount);
			vo.setHisBillSum(StringUtil.fenChangeYuan(hisBillSum));
			vo.setMerchBillSum(StringUtil.fenChangeYuan(merchBillSum));
			Long diffMoneySum = getDiffPrice(hisBillSum, merchBillSum);
			vo.setDiffMoney(StringUtil.fenChangeYuan(diffMoneySum));
			vo.setCheckState(checkState);
			dataset.add(vo);
		}
		return dataset;
	}
	
	/**
	 * <p>Description: 封装对账报表(支付方式)列表导出的数据集合</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年9月13日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param checkState
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportBillRFVo> 
	 * @throws
	 */
	public List<ExportBillMerchRFVo> buildBillMerchRFExportData(Example example) throws Exception {
		List<BillMerchRF> retList = billMerchRFDao.selectByExample(example);
		List<ExportBillMerchRFVo> dataset = new ArrayList<ExportBillMerchRFVo>();
		if(retList != null) {
			int hisBillSum = 0;
			int merchBillSum = 0;
			String checkState = "账平";
			for (BillMerchRF bmf : retList) {
				ExportBillMerchRFVo vo = new ExportBillMerchRFVo();
				hisBillSum += bmf.getHisBillSum();
				merchBillSum += bmf.getMerchBillSum();
				if(KstHosConstant.BILL_CHECK_RF_STATE_0.equals(bmf.getCheckState())) {
					checkState = "账不平";
				}
				vo.setPayMethod(bmf.getPayMethodName());
				vo.setBank(bmf.getBankName() + "(" + bmf.getBankShortName() + ")");
				vo.setBankNo(bmf.getBankNo());
				vo.setConfigkey(bmf.getConfigkey());
				vo.setHisBillSum(StringUtil.fenChangeYuan(bmf.getHisBillSum()));
				vo.setMerchBillSum(StringUtil.fenChangeYuan(bmf.getMerchBillSum()));
				vo.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_0.equals(bmf.getCheckState())?"账不平":"账平");
				if(bmf.getDealState() == 1) {
					vo.setCheckState("账平(处置后)");
				}
				dataset.add(vo);
			}
			ExportBillMerchRFVo vo = new ExportBillMerchRFVo();
			vo.setPayMethod("合计");
			vo.setBank("");
			vo.setBankNo("");
			vo.setConfigkey("");
			vo.setHisBillSum(StringUtil.fenChangeYuan(hisBillSum));
			vo.setMerchBillSum(StringUtil.fenChangeYuan(merchBillSum));
			vo.setCheckState(checkState);
			dataset.add(vo);
		}	
		return dataset;
	}
	
	/**
	 * <p>Description: 封装日账单统计列表导出的数据集合</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param checkState
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportBillRFVo> 
	 * @throws
	 */
	public List<ExportBillRFVo> buildBillRFExportData(String startDate, String endDate, Integer dateType, String checkState) throws Exception {
		List<BillRFVo> billList = null;
		if(dateType.equals(1)) {
			billList = billChannelRFDao.findBillRFListForDate(startDate, endDate);
		}else if(dateType.equals(2)) {
			billList = billChannelRFDao.findBillRFListForMonth(startDate, endDate);
		}
		List<ExportBillRFVo> dataset = new ArrayList<ExportBillRFVo>();
		if(billList != null && billList.size() > 0) {
			for (BillRFVo billRF : billList) {
				ExportBillRFVo vo = new ExportBillRFVo();
				String billDate = billRF.getBillDate();
				Integer dealState = null;
				String queryDate = billRF.getBillDate();
				if(dateType.equals(1)) {
					int IsDealOk = billChannelRFDao.findBillCountIsDealOkForDate(queryDate);
					if(IsDealOk > 0) {
						dealState = 2;  //处理后的账平
					}else {
						int billIsOk = billChannelRFDao.findBillCountIsOkForDate(queryDate);
						if(billIsOk > 0) {
							dealState = 0; //账不平
						}else {
							dealState = 1; //账平
						}
					}
				}else if(dateType.equals(2)) {
					int IsDealOk = billChannelRFDao.findBillCountIsDealOkForMonth(queryDate);
					if(IsDealOk > 0) {
						dealState = 2;  //处理后的账平
					}else {
						int billIsOk = billChannelRFDao.findBillCountIsOkForMonth(queryDate);
						if(billIsOk > 0) {
							dealState = 0; //账不平
						}else {
							dealState = 1; //账平
						}
					}
				}
				if(StringUtil.isNotBlank(checkState)) {
					//账不平
					if("0".equals(checkState)) {
						if(dealState == 1 || dealState == 2) {continue;}
					}else if("1".equals(checkState)) { //账平
						if(dealState == 0) {continue;}
					}
				}
				vo.setTradeTime(billDate);
				vo.setHisBillCount(billRF.getHisBillCount());
				vo.setChannelBillCount(billRF.getChannelBillCount());
				vo.setCheckCount(billRF.getCheckCount());
				vo.setHisSingleBillCount(billRF.getHisSingleBillCount());
				vo.setChannelSingleBillCount(billRF.getChannelSingleBillCount());
				vo.setDifferPirceCount(billRF.getDifferPirceCount());
				
				Long hisBillMoneySum = billRF.getHisBillMoneySum();
				Long merchBillMoneySum = billRF.getMerchBillMoneySum();
				vo.setHisBillMoneySum(StringUtil.fenChangeYuan(hisBillMoneySum));
				vo.setMerchBillMoneySum(StringUtil.fenChangeYuan(merchBillMoneySum));
				if(dealState == 2) {
					vo.setCheckState("账平(处置后)");
				}else if(dealState == 0){
					vo.setCheckState("账不平");
				}else{
					vo.setCheckState("账平");
				}
				dataset.add(vo);
			}
		}
		return dataset;
	}
	
	/**
	 * 
	 * <p>Description: 封装交易场景下的日账单汇总数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月30日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param channelId
	 * @param @return
	 * @param @throws SQLException
	 * @return List<ExportBillRFVo> 
	 * @throws
	 */
	public List<ExportBillRFVo> buildBillChannelRFExportData(String startDate, String endDate, String channelId) throws Exception {
		//交易渠道查询条件
		Example example = new Example(BillChannelRF.class);
		Criteria criteria = example.createCriteria();
		if(StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			criteria.andBetween("date", startDate, endDate);
		}
		if(StringUtil.isNotBlank(channelId)) {
			criteria.andEqualTo("channelId", channelId);
		}
		List<BillChannelRF> bcrfList = billChannelRFDao.selectByExample(example);
		List<ExportBillRFVo> dataset = new ArrayList<ExportBillRFVo>();
		if(bcrfList != null && bcrfList.size() > 0) {
			for (BillChannelRF bcf : bcrfList) {
				ExportBillRFVo vo = new ExportBillRFVo();
				vo.setTradeTime(bcf.getDate());
				vo.setHisBillCount(bcf.getHisBillCount());
				vo.setChannelBillCount(bcf.getChannelBillCount());
				vo.setCheckCount(bcf.getCheckCount());
				vo.setHisSingleBillCount(bcf.getHisSingleBillCount());
				vo.setChannelSingleBillCount(bcf.getChannelSingleBillCount());
				vo.setDifferPirceCount(bcf.getDifferPirceCount());
				
				Long hisBillMoneySum = bcf.getHisBillSum();
				Long merchBillMoneySum = bcf.getMerchBillSum();
				vo.setHisBillMoneySum(StringUtil.fenChangeYuan(hisBillMoneySum));
				vo.setMerchBillMoneySum(StringUtil.fenChangeYuan(merchBillMoneySum));
				if(hisBillMoneySum != null && hisBillMoneySum.equals(merchBillMoneySum)) { 
					vo.setCheckState("账不平");
				}else {
					vo.setCheckState("账平");
				}
				dataset.add(vo);
			}
		}
		return dataset;
	}
	
	/**
	 * 
	 * <p>Description: 支付商户账单信息写入Excel表格</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param savePath
	 * @param @param sheetName
	 * @return void 
	 * @throws
	 */
	private void writeBillExcel(String savePath, List<ExportBillVo> dataset) throws Exception{
		//先创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		String sheetName = "支付商户的账单信息"; 
		workbook.createSheet(sheetName);
		String tempFilename = "bill_" + System.currentTimeMillis() + ".xls";
		savePath = savePath + File.separator + tempFilename;
		File tempFile = new File(savePath);
		OutputStream out = new FileOutputStream(tempFile);
		ExportExcel<ExportBillVo> ex = new ExportExcel<ExportBillVo>();
		String[] headers = { "商户订单号", "全流程订单号", "商户退款单号", "全流程退款订单号","交易场景","订单类型",
				"交易日期","退费金额","支付金额（元）","医院名称","调用口提交的交易类型"};
		ex.exportExcel(sheetName, headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
	}
	
	/**
	 * 
	 * <p>Description: 支付商户账单信息写入Excel表格</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param savePath
	 * @param @param sheetName
	 * @return void 
	 * @throws
	 */
	private void writeHisBillExcel(String savePath, List<ExportHisBillVo> dataset) throws Exception{
		//先创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		String sheetName = "HIS的账单信息"; 
		workbook.createSheet(sheetName);
		String tempFilename = "his_bill_" + System.currentTimeMillis() + ".xls";
		savePath = savePath + File.separator + tempFilename;
		File tempFile = new File(savePath);
		OutputStream out = new FileOutputStream(tempFile);
		ExportExcel<ExportHisBillVo> ex = new ExportExcel<ExportHisBillVo>();
		String[] headers = { "全流程订单号", "全流程退款订单号", "商户订单号", "HIS订单号","支付金额/退费金额","全部金额",
				"退款金额","订单时间","交易场景","价格名称","订单说明","HIS订单状态","HIS业务状态","插入时间"};
		ex.exportExcel(sheetName, headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
	}
	
	/**
	 * 
	 * <p>Description: 支付商户账单信息写入Excel表格</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param savePath
	 * @param @param sheetName
	 * @return void 
	 * @throws
	 */
	private void writeBillCheckExcel(String savePath, List<ExportBillCheckVo> dataset) throws Exception{
		//先创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		String sheetName = "智付对账的账单信息表"; 
		workbook.createSheet(sheetName);
		String tempFilename = "bill_check_" + System.currentTimeMillis() + ".xls";
		savePath = savePath + File.separator + tempFilename;
		File tempFile = new File(savePath);
		OutputStream out = new FileOutputStream(tempFile);
		ExportExcel<ExportBillCheckVo> ex = new ExportExcel<ExportBillCheckVo>();
		String[] headers = { "对账时间", "订单号","医院流水号","渠道流水号", "应收/退", "应收/退金额（元）","支付方式","实收/退",
				"实收/退金额（元）", "对账结果", "差错金额（元）"};
		ex.exportExcel(sheetName, headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
	}
	
	/**
	 * 
	 * <p>Description: 支付商户账单信息写入Excel表格</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月29日
	 * @author zhaoy  
	 * @param @param savePath
	 * @param @param sheetName
	 * @return void 
	 * @throws
	 */
	private void writeBillRFExcel(String savePath, List<ExportBillRFVo> dataset) throws Exception{
		//先创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		String sheetName = "智付对账的账单信息汇总表"; 
		workbook.createSheet(sheetName);
		String tempFilename = "bill_check_count_" + System.currentTimeMillis() + ".xls";
		savePath = savePath + File.separator + tempFilename;
		File tempFile = new File(savePath);
		try {
			OutputStream out = new FileOutputStream(tempFile);
			ExportExcel<ExportBillRFVo> ex = new ExportExcel<ExportBillRFVo>();
			String[] headers = { "交易日期", "医院流水（笔）", "渠道流水（笔）", "已勾兑（笔）","医院单边账（笔）","渠道单边账（笔）",
					"金额不一致（笔）","医院应净收（元）", "渠道实净收（元）","对账结果"};
			ex.exportExcel(sheetName, headers, dataset, out,
					"yyyy-MM-dd HH:mm", workbook);
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public CommonResp<RespMap> saveBankCheckInfo(CommonReq<ReqUpdateBankCheckInfo> commReq) throws Exception {
		ReqUpdateBankCheckInfo req = commReq.getParam();
		String date = req.getDate();
		String bankNo = req.getBankNo();
		Example example = new Example(BankMoneyCheck.class);
		example.createCriteria().andEqualTo("date", date).andEqualTo("bankNo", bankNo);
		
		BankMoneyCheck bankCheckInfo = bankCheckDao.selectOneByExample(example);
		Long paideMoney = Long.parseLong(StringUtil.yuanChangeFen(req.getPaideMoney()));   //实到金额
		Long payAbleMoney = bankCheckInfo.getPayAbleMoney();   //应到金额
		Integer checkState = null;
		if(paideMoney.equals(payAbleMoney)) {
			checkState = KstHosConstant.BANK_CHECK_STATE_0;  // 账平
		}else if(paideMoney > payAbleMoney) {
			checkState = KstHosConstant.BANK_CHECK_STATE_1;  // 长款
		}else if(paideMoney < payAbleMoney) {
			checkState = KstHosConstant.BANK_CHECK_STATE_T1;  // 短款
		}
		bankCheckInfo.setIsCheck(KstHosConstant.BANK_IS_CHECK_1);  // 已勾兑
		bankCheckInfo.setCheckState(checkState);
		bankCheckInfo.setPaideMoney(paideMoney);
		bankCheckInfo.setBankFlowNo(req.getBankFlowNo());
		bankCheckInfo.setUpdateBy(req.getUpdateBy());
		bankCheckInfo.setUpdateDate(DateOper.getNowDateTime());
		bankCheckDao.updateByExample(bankCheckInfo, example);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}
	
	/**
	 * 
	 * <p>Description: 计算差错金额</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年9月12日
	 * @author zhaoy  
	 * @param @param hisPrice
	 * @param @param merchPrice
	 * @param @return
	 * @return Integer 
	 * @throws
	 */
	private Integer getDiffPrice(Integer hisPrice, Integer merchPrice) {
		if(hisPrice == null) {
			return merchPrice;
		}
		if(merchPrice == null) {
			return hisPrice;
		}
		if(hisPrice > merchPrice) {
			return hisPrice - merchPrice;
		}else {
			return merchPrice - hisPrice;
		}
	}
	
	private Long getDiffPrice(Long hisPrice, Long merchPrice) {
		if(hisPrice == null) {
			return merchPrice;
		}
		if(merchPrice == null) {
			return hisPrice;
		}
		if(hisPrice > merchPrice) {
			return hisPrice - merchPrice;
		}else {
			return merchPrice - hisPrice;
		}
	}
	
	/**
	 * 封装serviceId(门诊、住院)
	 * 
	 * @return
	 */
	private List<String> getServiceIdList(String transType){
		if(StringUtil.isBlank(transType)) {
			return null;
		}
		Map<String, List<String>> map = new HashMap<>();
		//1.先获取所有的serviceId集合
		List<Dictionary> serviceIdList = dictLocalCache.get("serviceid");
		//门诊的服务ID集合
		Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
		List<String> outPatientServiceIdList = new ArrayList<>();
		//住院的服务ID集合
		Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
		List<String> hospitalizationServiceIdList = new ArrayList<>();
		if(serviceIdList != null && serviceIdList.size() > 0) {
			for (Dictionary dictionary : serviceIdList) {
				if(pOutPatient.getId().equals(dictionary.getUpId())) {
					outPatientServiceIdList.add(dictionary.getKeyword());
				}else if(pHospitalization.getId().equals(dictionary.getUpId())) {
					hospitalizationServiceIdList.add(dictionary.getKeyword());
				}
			}
		}
		map.put(KstHosConstant.OUTPATIENT, outPatientServiceIdList);
		map.put(KstHosConstant.HOSPITALIZATION, hospitalizationServiceIdList);
		return map.get(transType);
	}

}
