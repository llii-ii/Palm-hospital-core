package com.kasite.client.business.module.backstage.order.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.basic.cache.DictLocalCache;
import com.kasite.client.basic.dao.IMemberMapper;
import com.kasite.client.basic.dao.IPatientMapper;
import com.kasite.client.business.module.backstage.UploadFileController;
import com.kasite.client.business.module.backstage.bill.dao.BillCheckDao;
import com.kasite.client.business.module.backstage.order.export.ExportOrderReportVo;
import com.kasite.client.business.module.backstage.order.export.ExportOrderVo;
import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.PayRule;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.ExportExcel;
import com.kasite.core.common.util.FileUtils;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.order.IOrderRFService;
import com.kasite.core.serviceinterface.module.order.dto.OrderCountMoneyVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderQuery;
import com.kasite.core.serviceinterface.module.order.dto.OrderReportVo;
import com.kasite.core.serviceinterface.module.order.dto.OrderVo;
import com.kasite.core.serviceinterface.module.order.dto.PayOrderDetailVo;
import com.kasite.core.serviceinterface.module.order.dto.RefundOrderDetailVo;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderRFList;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderReport;
import com.kasite.core.serviceinterface.module.order.resp.RespPayOrderDetail;
import com.kasite.core.serviceinterface.module.order.resp.RespRefundOrderDetail;

@Service("orderRF.orderRFWs")
public class OrderRFServiceImpl implements IOrderRFService{

	@Autowired
	protected IOrderMapper orderMapper;
	
	@Autowired
	protected IMemberMapper memberMapper;
	
	@Autowired
	protected IPatientMapper patientMapper;
	
	@Autowired
	protected DictLocalCache dictLocalCache;
	
	@Autowired
	protected BillCheckDao billCheckDao;
	
	@Override
	public CommonResp<RespOrderRFList> queryOrderListLocal(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String nickName = req.getNickName();
		String nickMobile = req.getNickMobile();
		String cardNo = req.getCardNo();
		
		OrderQuery query = new OrderQuery();
		query.setStartDate(req.getStartDate());
		query.setEndDate(req.getEndDate());
		
		if(StringUtil.isNotBlank(nickName)) {
			query.setNickName("%" + nickName + "%");
		}
		if(StringUtil.isNotBlank(nickMobile)) {
			query.setNickMobile(nickMobile + "%");
		}
		if(StringUtil.isNotBlank(cardNo)) {
			query.setCardNo(cardNo + "%");
		}
		List<String> channelIdList = new ArrayList<>();
		if(StringUtil.isNotBlank(req.getChannelId())) {
			String channelIdStr = req.getChannelId();
			String[] arr = channelIdStr.split(",");
			if(StringUtil.isNotEmpty(arr[0])) {
				for (String channelId : arr) {
					channelIdList.add(channelId);
				}
			}
		}
		query.setChannelIdList(channelIdList);
		query.setChannelSerialNo(req.getChannelSerialNo());
		query.setHisSerialNo(req.getHisSerialNo());
		query.setOrderId(req.getOrderId());
		query.setOrderState(req.getOrderState());
		query.setConfigKey(req.getConfigKey());
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			query.setServiceIdList(serviceIdList);
		}
		
		PageInfo<OrderVo> page = null;
		if(req.getPage() != null && req.getPage().getPSize() > 0) {
			PageHelper.startPage(req.getPage().getPIndex(), req.getPage().getPSize());
		}
		List<OrderVo> retList = orderMapper.findAllOrderList(query);
		List<RespOrderRFList> respList = new ArrayList<>();
		if(retList != null) {
			page = new PageInfo<>(retList);
			Long total = page.getTotal();
			if(req.getPage() != null) {
				req.getPage().setPCount(total.intValue());
			}
			for( int i=0;i<page.getList().size();i++) {
				OrderVo vo = page.getList().get(i);
				RespOrderRFList resp = new RespOrderRFList();
				BeanCopyUtils.copyProperties(vo, resp, null);
				String serviceId = vo.getServiceId();
				String serviceName = "";
				if(StringUtil.isNotBlank(serviceId)) {
					serviceName = dictLocalCache.getValue("serviceid", serviceId);
				}
				resp.setTransType(serviceName);
				String configKey = vo.getConfigKey();
				String bankName = "";
				String bankShortName = "";
				ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configKey);
				if(payInfo != null) {
					if(ChannelTypeEnum.zfb.equals(payInfo)) {
						bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configKey);
						bankShortName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configKey);
					}else if(ChannelTypeEnum.wechat.equals(payInfo)) {
						bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configKey);
						bankShortName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configKey);
					}
				}
				resp.setBankName(bankName);
				resp.setBankShortName(bankShortName);
				resp.setTransTime(DateUtils.getTimestampToStr(vo.getTransTime()));
				Integer orderState = vo.getOrderState();
				//判断是否业务已完成
				if((StringUtil.isNotBlank(vo.getBizState()) && KstHosConstant.ORDERBIZSTATE_1.equals(vo.getBizState()))) {
					orderState = 2;
				}else {
					orderState = 1;
				}
				respList.add(resp);
				if(StringUtil.isNotBlank(vo.getRefundOrderId())) {
					String refundOrderId = vo.getRefundOrderId();
					OrderVo refundOrderVo = orderMapper.findRefundOrderByRefundOrderId(refundOrderId);
					resp.setChannelSerialNo(refundOrderVo.getChannelSerialNo());
					resp.setHisSerialNo(refundOrderVo.getHisSerialNo());
					resp.setOrderMoney(refundOrderVo.getOrderMoney());
					resp.setTransMoney(refundOrderVo.getTransMoney());
					resp.setOrderState(refundOrderVo.getOrderState());
					resp.setTransTime(DateUtils.getTimestampToStr(refundOrderVo.getTransTime()));
					respList.set(i, resp);
				}else {
					resp.setOrderState(orderState);
				}
			}
			return new CommonResp<RespOrderRFList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList, req.getPage());
		}else {
			return new CommonResp<RespOrderRFList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
	}

	@Override
	public CommonResp<RespMap> queryTotalCountMoney(CommonReq<ReqQueryLocalOrderInfo> commReq)
			throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String nickName = req.getNickName();
		String nickMobile = req.getNickMobile();
		String cardNo = req.getCardNo();
		
		OrderQuery query = new OrderQuery();
		query.setStartDate(req.getStartDate());
		query.setEndDate(req.getEndDate());
		
		if(StringUtil.isNotBlank(nickName)) {
			query.setNickName("%" + nickName + "%");
		}
		if(StringUtil.isNotBlank(nickMobile)) {
			query.setNickMobile(nickMobile + "%");
		}
		if(StringUtil.isNotBlank(cardNo)) {
			query.setCardNo(cardNo + "%");
		}
		List<String> channelIdList = new ArrayList<>();
		if(StringUtil.isNotBlank(req.getChannelId())) {
			String channelIdStr = req.getChannelId();
			String[] arr = channelIdStr.split(",");
			if(StringUtil.isNotEmpty(arr[0])) {
				for (String channelId : arr) {
					channelIdList.add(channelId);
				}
			}
		}
		query.setChannelIdList(channelIdList);
		query.setOrderId(req.getOrderId());
		query.setChannelSerialNo(req.getChannelSerialNo());
		query.setHisSerialNo(req.getHisSerialNo());
		query.setOrderState(req.getOrderState());
		query.setConfigKey(req.getConfigKey());
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			query.setServiceIdList(serviceIdList);
		}
		
		RespMap resp = new RespMap();
		Long totalMoney = 0L;
		//1,已支付 2,已完成
		if(KstHosConstant.ORDERSTATE_1.equals(req.getOrderState()) || KstHosConstant.ORDERSTATE_2.equals(req.getOrderState())) {
			OrderCountMoneyVo payVo = orderMapper.findTotalPayMoney(query);
			if(payVo != null) {
				totalMoney = payVo.getTotalPayMoney();
			}
		}else if(KstHosConstant.ORDERSTATE_3.equals(req.getOrderState()) || KstHosConstant.ORDERSTATE_4.equals(req.getOrderState())
				|| KstHosConstant.ORDERSTATE_7.equals(req.getOrderState())) {  //3,退款中  4,已退款  7,退款失败
			OrderCountMoneyVo refundVo = orderMapper.findTotalRefundMoney(query);
			if(refundVo != null) {
				totalMoney = 0 - refundVo.getTotalRefundMoney();
			}
		}else {
			OrderCountMoneyVo payVo = orderMapper.findTotalPayMoney(query);
			if(payVo != null) {
				totalMoney = payVo.getTotalPayMoney();
			}
			OrderCountMoneyVo refundVo = orderMapper.findTotalRefundMoney(query);
			if(refundVo != null && refundVo.getTotalRefundMoney() != null) {
				totalMoney = totalMoney - refundVo.getTotalRefundMoney();
			}
		}
		resp.add(ApiKey.OrderListLocal.TotalMoney, totalMoney);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}
	
	@Override
	public CommonResp<RespMap> queryTransCountList(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String payType = req.getPayType();
		
		JSONObject respJson = new JSONObject();
		RespMap respMap = new RespMap();
		//1.先获取所有的serviceId集合
		List<Dictionary> serviceIdList = dictLocalCache.get("serviceid");
		//门诊的服务ID集合
		Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
		List<String> outPatientServiceIdList = new ArrayList<>();
		//住院的服务ID集合
		Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
		List<String> hospitalizationServiceIdList = new ArrayList<>();
		for (Dictionary dictionary : serviceIdList) {
			if(pOutPatient.getId().equals(dictionary.getUpId())) {
				outPatientServiceIdList.add(dictionary.getKeyword());
			}else if(pHospitalization.getId().equals(dictionary.getUpId())) {
				hospitalizationServiceIdList.add(dictionary.getKeyword());
			}
		}
		//先获取所有的渠道
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		if(channelSet != null) {
			for (String[] channel : channelSet) {
				String channelId = channel[0];
				String channelName = channel[1];
				String isOpen = channel[2];
				if("false".equals(isOpen)){
					continue;
				}
				String configKey = "";
				if(ChannelTypeEnum.wechat.name().equals(payType)) {
					configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId);
				}else if(ChannelTypeEnum.zfb.name().equals(payType)) {
					configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
				}
				String[] configKeyArr = null;
				List<String> configKeyList = new ArrayList<>();
				if(StringUtil.isNotBlank(configKey)) {
					configKeyArr = configKey.split(",");
					for (String key : configKeyArr) {
						configKeyList.add(key);
					}
				}
				if(configKeyList == null || configKeyList.size() == 0) {
					continue;
				}
				List<OrderReportVo> payList = orderMapper.findPayOrderReport(startDate, endDate, configKeyList, channelId);
				List<OrderReportVo> refundList = orderMapper.findRefundOrderReport(startDate, endDate, configKey, channelId);
				if(payList.size() == 0 && refundList.size() == 0) {
					continue;
				}
				List<RespOrderReport> respList = new ArrayList<>();
				RespOrderReport or1 = new RespOrderReport();
				List<RespOrderReport> or1List = new ArrayList<>();
				or1.setServiceId("门诊充值");
				RespOrderReport or2 = new RespOrderReport();
				List<RespOrderReport> or2List = new ArrayList<>();
				or2.setServiceId("住院充值");
				RespOrderReport or3 = new RespOrderReport();
				List<RespOrderReport> or3List = new ArrayList<>();
				or3.setServiceId("门诊原路退款");
				RespOrderReport or4 = new RespOrderReport();
				List<RespOrderReport> or4List = new ArrayList<>();
				or4.setServiceId("住院原路退款");
				if(payList.size() > 0) {
					int or1TransCount = 0;
					Long or1TransMoney = 0L;
					int or1ReverseCount = 0;
					Long or1ReverseMoney = 0L;
					
					int or2TransCount = 0;
					Long or2TransMoney = 0L;
					int or2ReverseCount = 0;
					Long or2ReverseMoney = 0L;
					for (OrderReportVo payVo : payList) {
						String serviceId = payVo.getServiceId();
						String serviceValue = "";
						if(StringUtil.isNotBlank(serviceId)) {
							serviceValue = dictLocalCache.getValue("serviceid", serviceId);
						}
						OrderReportVo obj = billCheckDao.findReverseCount(startDate, endDate, payType, channelId, serviceId);
						if(outPatientServiceIdList.contains(serviceId)) {
							RespOrderReport report = new RespOrderReport();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(payVo.getTransCount());
							report.setTransMoney(payVo.getTransMoney()+"");
							report.setReverseCount(obj.getReverseCount());
							report.setReverseMoney(obj.getReverseMoney()+"");
							
							or1TransCount += payVo.getTransCount();
							or1TransMoney += payVo.getTransMoney();
							or1ReverseCount += payVo.getReverseCount();
							or1ReverseMoney += payVo.getReverseMoney();
							or1List.add(report);
						}else if(hospitalizationServiceIdList.contains(serviceId)) {
							RespOrderReport report = new RespOrderReport();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(payVo.getTransCount());
							report.setTransMoney(payVo.getTransMoney()+"");
							report.setReverseCount(obj.getReverseCount());
							report.setReverseMoney(obj.getReverseMoney()+"");
							
							or2TransCount += payVo.getTransCount();
							or2TransMoney += payVo.getTransMoney();
							or2ReverseCount += payVo.getReverseCount();
							or2ReverseMoney += payVo.getReverseMoney();
							or2List.add(report);
						}
					}
					or1.setTransCount(or1TransCount);
					or1.setTransMoney(or1TransMoney + "");
					or1.setReverseCount(or1ReverseCount);
					or1.setReverseMoney(or1ReverseMoney + "");
					or1.setRespOrderReportList(or1List);
					
					or2.setTransCount(or2TransCount);
					or2.setTransMoney(or2TransMoney + "");
					or2.setReverseCount(or2ReverseCount);
					or2.setReverseMoney(or2ReverseMoney + "");
					or2.setRespOrderReportList(or2List);
				}
				if(refundList.size() > 0) {
					int or3TransCount = 0;
					Long or3TransMoney = 0L;
					int or4TransCount = 0;
					Long or4TransMoney = 0L;
					for (OrderReportVo refundVo : refundList) {
						String serviceId = refundVo.getServiceId();
						String serviceValue = "";
						if(StringUtil.isNotBlank(serviceId)) {
							serviceValue = dictLocalCache.getValue("serviceid", serviceId);
						}
						Long rm = refundVo.getTransMoney();
						if(outPatientServiceIdList.contains(serviceId)) {
							RespOrderReport report = new RespOrderReport();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(refundVo.getTransCount());
							report.setTransMoney(rm == null?"0":"-" + rm);
							report.setReverseCount(0);
							report.setReverseMoney("0");
							
							or3TransCount += refundVo.getTransCount();
							or3TransMoney += rm;
							or3List.add(report);
						}else if(hospitalizationServiceIdList.contains(serviceId)) {
							RespOrderReport report = new RespOrderReport();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(refundVo.getTransCount());
							report.setTransMoney(rm == null?"0":"-" + rm);
							report.setReverseCount(0);
							report.setReverseMoney("0");
							
							or4TransCount += refundVo.getTransCount();
							or4TransMoney += rm;
							or4List.add(report);
						}
					}
					
					or3.setTransCount(or3TransCount);
					or3.setTransMoney(or3TransMoney==null?"0":"-" + or3TransMoney);
					or3.setReverseCount(0);
					or3.setReverseMoney("0");
					or3.setRespOrderReportList(or3List);
					
					or4.setTransCount(or4TransCount);
					or4.setTransMoney(or4TransMoney==null?"0":"-" + or4TransMoney);
					or4.setReverseCount(0);
					or4.setReverseMoney("0");
					or4.setRespOrderReportList(or4List);
				}
				respList.add(or1);
				respList.add(or2);
				respList.add(or3);
				respList.add(or4);
				respJson.put(channelName, respList);
			}
			respMap.put(ApiKey.OrderListLocal.OrderReport, respJson);
			respMap.put(ApiKey.OrderListLocal.PayTimeLimit, getPayLimit(req.getPayType()));
		}
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}

	@Override
	public CommonResp<RespPayOrderDetail> queryPayOrderDetail(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String orderId = req.getOrderId();
		Integer orderState = req.getOrderState();
		PayOrderDetailVo orderVo = null;
		if(KstHosConstant.ORDERSTATE_1.equals(orderState)) {
			orderVo = orderMapper.findPayOrder(orderId);
		}else if(KstHosConstant.ORDERSTATE_2.equals(orderState)) {
			orderVo = orderMapper.findBizOrder(orderId);
		}
		if(orderVo == null) {
			return new CommonResp<RespPayOrderDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		RespPayOrderDetail resp = new RespPayOrderDetail();
		BeanCopyUtils.copyProperties(orderVo, resp, null);
		String serviceId = orderVo.getServiceId();
		String serviceValue = "";
		if(StringUtil.isNotBlank(serviceId)) {
			serviceValue = dictLocalCache.getValue("serviceid", serviceId);
		}
		resp.setOrderType(serviceValue);
		String orderDate = DateUtils.getTimestampToStr(orderVo.getOrderDate());
		String payDate = DateUtils.getTimestampToStr(orderVo.getPayDate());
		String bizDate = DateUtils.getTimestampToStr(orderVo.getBizDate());
		resp.setOrderDate(orderDate);
		resp.setPayDate(payDate);
		resp.setBizDate(bizDate);
		String channelId = orderVo.getChannelId();
		String channelName = KasiteConfig.getChannelById(channelId);
		resp.setChannelName(channelName);
		//获取支付方式
		String configkey = orderVo.getConfigKey();
		ChannelTypeEnum payEnum = KasiteConfig.getPayTypeByConfigKey(configkey);
		if(payEnum != null) {
			resp.setPayMethod(payEnum.name());
			resp.setPayMethodName(payEnum.getTitle());
		}
		return new CommonResp<RespPayOrderDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, resp);
	}

	@Override
	public CommonResp<RespRefundOrderDetail> queryRefundOrderDetail(CommonReq<ReqQueryLocalOrderInfo> commReq)
			throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String refundOrderId = req.getRefundOrderId();  //退款的订单根据退款单号查询
		RefundOrderDetailVo refundOrderDetailVo = orderMapper.findRefundOrder(refundOrderId);
		if(refundOrderDetailVo == null) {
			return new CommonResp<RespRefundOrderDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_NOTRESULT);
		}
		RespRefundOrderDetail resp = new RespRefundOrderDetail();
		BeanCopyUtils.copyProperties(refundOrderDetailVo, resp, null);
		String serviceId = refundOrderDetailVo.getServiceId();
		String serviceValue = "";
		if(StringUtil.isNotBlank(serviceId)) {
			serviceValue = dictLocalCache.getValue("serviceid", serviceId);
		}
		resp.setOrderType(serviceValue);
		String orderDate = DateUtils.getTimestampToStr(refundOrderDetailVo.getOrderDate());
		String payDate = DateUtils.getTimestampToStr(refundOrderDetailVo.getPayDate());
		String bizDate = DateUtils.getTimestampToStr(refundOrderDetailVo.getBizDate());
		String refundBeginDate =  DateUtils.getTimestampToStr(refundOrderDetailVo.getRefundBeginDate());
		String refundEndDate = DateUtils.getTimestampToStr(refundOrderDetailVo.getRefundEndDate()); 
		resp.setOrderDate(orderDate);
		resp.setPayDate(payDate);
		resp.setBizDate(bizDate);
		resp.setRefundBeginDate(refundBeginDate);
		resp.setRefundEndDate(refundEndDate);
		
		String channelId = refundOrderDetailVo.getChannelId();
		String channelName = KasiteConfig.getChannelById(channelId);
		resp.setChannelName(channelName);
		
		if(StringUtil.isNotBlank(refundOrderDetailVo.getRefundChannelId())) {
			String refundChannelId = refundOrderDetailVo.getRefundChannelId();
			resp.setRefundChannelName(KasiteConfig.getChannelById(refundChannelId));
		}
		
		//获取支付方式
		String configkey = refundOrderDetailVo.getConfigkey();
		ChannelTypeEnum payEnum = KasiteConfig.getPayTypeByConfigKey(configkey);
		if(payEnum != null) {
			resp.setPayMethod(payEnum.name());
			resp.setPayMethodName(payEnum.getTitle());
		}
		List<RespRefundOrderDetail> respList = new ArrayList<>();
		respList.add(resp);
		return new CommonResp<RespRefundOrderDetail>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList);
	}
	
	
	@Override
	public CommonResp<RespMap> downloadOrderListData(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		String nickName = req.getNickName();
		String nickMobile = req.getNickMobile();
		String cardNo = req.getCardNo();
		
		OrderQuery query = new OrderQuery();
		query.setStartDate(req.getStartDate());
		query.setEndDate(req.getEndDate());
		
		if(StringUtil.isNotBlank(nickName)) {
			query.setNickName("%" + nickName + "%");
		}
		if(StringUtil.isNotBlank(nickMobile)) {
			query.setNickMobile(nickMobile + "%");
		}
		if(StringUtil.isNotBlank(cardNo)) {
			query.setCardNo(cardNo + "%");
		}
		List<String> channelIdList = new ArrayList<>();
		if(StringUtil.isNotBlank(req.getChannelId())) {
			String channelIdStr = req.getChannelId();
			String[] arr = channelIdStr.split(",");
			for (String channelId : arr) {
				channelIdList.add(channelId);
			}
		}
		query.setChannelIdList(channelIdList);
		query.setOrderId(req.getOrderId());
		query.setChannelSerialNo(req.getChannelSerialNo());
		query.setHisSerialNo(req.getHisSerialNo());
		query.setOrderState(req.getOrderState());
		query.setConfigKey(req.getConfigKey());
		String transType = req.getTransType();
		List<String> serviceIdList = getServiceIdList(transType);
		if(serviceIdList != null && serviceIdList.size() > 0) {
			query.setServiceIdList(serviceIdList);
		}
		
		List<ExportOrderVo> dataset = buildOrderExportData(query);
		ExportExcel<ExportOrderVo> ex = new ExportExcel<ExportOrderVo>();
		String[] headers = { "交易时间", "渠道流水号", "医院流水号", "就诊卡号/住院号","患者姓名","患者手机号",
				"订单类型","订单金额（元）", "实付金额（元）","订单状态"};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "order_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "orderList";

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
		String[] sheetNames = { "订单明细" };
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
	public CommonResp<RespMap> downloadOrderReportData(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception {
		ReqQueryLocalOrderInfo req = commReq.getParam();
		
		List<ExportOrderReportVo> dataset = buildOrderReportData(req);
		ExportExcel<ExportOrderReportVo> ex = new ExportExcel<ExportOrderReportVo>();
		String[] headers = { "交易渠道", "交易类型", "交易笔数", "交易金额","待冲正笔数","待冲正金额"};
		String[] thContents = { "交易日期 : " + req.getStartDate() + "~" + req.getEndDate(),"","","交易时间: " + getPayLimit(req.getPayType()),"",""};
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tempFilename = "orderReport_" + format.format(new Date()) + ".xls";
		String rootUrl = KasiteConfig.getTempfilePath();
		String path = UploadFileController.fileDir + File.separator + rootUrl + "orderReport";

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
		String[] sheetNames = { "交易日报" };
		for (int i = 0; i < sheetNames.length; i++) {
			workbook.createSheet(sheetNames[i]);
		}
		ex.exportExcel(sheetNames[0], thContents, headers, dataset, out,
				"yyyy-MM-dd HH:mm", workbook);
		workbook.write(out);
		out.close();
		
		
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.BillRFPro.FilePath, path);
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respMap);
	}
	
	/**
	 * 构建交易订单导出列表数据集合
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<ExportOrderVo> buildOrderExportData(OrderQuery query) throws Exception{
		List<OrderVo> orderList = orderMapper.findAllOrderList(query);
		List<ExportOrderVo> dataset = new ArrayList<ExportOrderVo>();
		if(orderList != null && orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				OrderVo order = orderList.get(i);
				ExportOrderVo vo = new ExportOrderVo();
				Integer orderState = order.getOrderState();
				//判断是否业务已完成
				if(StringUtil.isNotBlank(order.getBizState()) && KstHosConstant.ORDERBIZSTATE_1.equals(order.getBizState())) {
					orderState = 2;
				}else {
					orderState = 1;
				}
				if(StringUtil.isNotBlank(order.getRefundOrderId())) {
					String refundOrderId = order.getRefundOrderId();
					OrderVo refundOrderVo = orderMapper.findRefundOrderByRefundOrderId(refundOrderId);
					if(refundOrderVo != null) {
						orderState = refundOrderVo.getOrderState();
						vo.setTransTime(DateUtils.getTimestampToStr(refundOrderVo.getTransTime()));
						vo.setHisSerialNo(refundOrderVo.getHisSerialNo());
						vo.setChannelSerialNo(refundOrderVo.getChannelSerialNo());
						vo.setCardNo(refundOrderVo.getCardNo());
						String serviceId = refundOrderVo.getServiceId();
						String serviceValue = "";
						if(StringUtil.isNotBlank(serviceId)) {
							serviceValue = dictLocalCache.getValue("serviceid", serviceId);
						}
						vo.setTransType(serviceValue);
						String orderMoney = StringUtil.fenChangeYuan(refundOrderVo.getOrderMoney());
						vo.setOrderMoney(StringUtil.isBlank(orderMoney)?"0":orderMoney);
						String transMoney = StringUtil.fenChangeYuan(refundOrderVo.getTransMoney());
						vo.setTransMoney(StringUtil.isBlank(transMoney)?"0":transMoney);
					}
				}else {
					vo.setTransTime(DateUtils.getTimestampToStr(order.getTransTime()));
					vo.setHisSerialNo(order.getHisSerialNo());
					vo.setChannelSerialNo(order.getChannelSerialNo());
					vo.setCardNo(order.getCardNo());
					String serviceId = order.getServiceId();
					String serviceValue = "";
					if(StringUtil.isNotBlank(serviceId)) {
						serviceValue = dictLocalCache.getValue("serviceid", serviceId);
					}
					vo.setTransType(serviceValue);
					String orderMoney = StringUtil.fenChangeYuan(order.getOrderMoney());
					vo.setOrderMoney(StringUtil.isBlank(orderMoney)?"0":orderMoney);
					String transMoney = StringUtil.fenChangeYuan(order.getTransMoney());
					vo.setTransMoney(StringUtil.isBlank(transMoney)?"0":transMoney);
				}
				
				vo.setOrderState(KstHosConstant.ORDERSTATE_1.equals(orderState)?"已支付":(KstHosConstant.ORDERSTATE_2.equals(orderState)?"已完成":
					(KstHosConstant.ORDERSTATE_3.equals(orderState)?"退款中":(KstHosConstant.ORDERSTATE_4.equals(orderState)?"已退款":
						(KstHosConstant.ORDERSTATE_7.equals(orderState)?"退款失败":"退款中")))));
				dataset.add(vo);
			}
		}
		return dataset;
	}
	
	/**
	 * 构建交易报表导出数据集合
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	private List<ExportOrderReportVo> buildOrderReportData(ReqQueryLocalOrderInfo req)throws Exception {
		String startDate = req.getStartDate();
		String endDate = req.getEndDate();
		String payType = req.getPayType();
		List<ExportOrderReportVo> respList = new ArrayList<>();
		//1.先获取所有的serviceId集合
		List<Dictionary> serviceIdList = dictLocalCache.get("serviceid");
		//门诊的服务ID集合
		Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
		List<String> outPatientServiceIdList = new ArrayList<>();
		//住院的服务ID集合
		Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
		List<String> hospitalizationServiceIdList = new ArrayList<>();
		for (Dictionary dictionary : serviceIdList) {
			if(pOutPatient.getId().equals(dictionary.getUpId())) {
				outPatientServiceIdList.add(dictionary.getKeyword());
			}else if(pHospitalization.getId().equals(dictionary.getUpId())) {
				hospitalizationServiceIdList.add(dictionary.getKeyword());
			}
		}
		//2.获取所有的渠道
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		if(channelSet != null) {
			for (String[] channel : channelSet) {
				String channelId = channel[0];
				String channelName = channel[1];
				String isOpen = channel[2];
				if("false".equals(isOpen)) {
					continue;
				}
				String configKey = "";
				if(ChannelTypeEnum.wechat.name().equals(payType)) {
					configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId);
				}else if(ChannelTypeEnum.zfb.name().equals(payType)) {
					configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
				}
				String[] configKeyArr = null;
				List<String> configKeyList = new ArrayList<>();
				if(StringUtil.isNotBlank(configKey)) {
					configKeyArr = configKey.split(",");
					for (String key : configKeyArr) {
						configKeyList.add(key);
					}
				}
				List<OrderReportVo> payList = orderMapper.findPayOrderReport(startDate, endDate, configKeyList, channelId);
				List<OrderReportVo> refundList = orderMapper.findRefundOrderReport(startDate, endDate, configKey, channelId);
				if(payList.size() == 0 && refundList.size() == 0) {
					continue;
				}
				ExportOrderReportVo or1 = new ExportOrderReportVo();
				List<ExportOrderReportVo> or1List = new ArrayList<>();
				or1.setServiceId("门诊充值");
				or1.setChannelName(channelName);
				ExportOrderReportVo or2 = new ExportOrderReportVo();
				List<ExportOrderReportVo> or2List = new ArrayList<>();
				or2.setServiceId("住院充值");
				or2.setChannelName(channelName);
				ExportOrderReportVo or3 = new ExportOrderReportVo();
				List<ExportOrderReportVo> or3List = new ArrayList<>();
				or3.setServiceId("门诊原路退款");
				or3.setChannelName(channelName);
				ExportOrderReportVo or4 = new ExportOrderReportVo();
				List<ExportOrderReportVo> or4List = new ArrayList<>();
				or4.setServiceId("住院原路退款");
				or4.setChannelName(channelName);
				if(payList != null && payList.size() > 0) {
					int or1TransCount = 0;
					Long or1TransMoney = 0L;
					int or1ReverseCount = 0;
					Long or1ReverseMoney = 0L;
					
					int or2TransCount = 0;
					Long or2TransMoney = 0L;
					int or2ReverseCount = 0;
					Long or2ReverseMoney = 0L;
					for (OrderReportVo payVo : payList) {
						String serviceId = payVo.getServiceId();
						String serviceValue = "";
						if(StringUtil.isNotBlank(serviceId)) {
							serviceValue = dictLocalCache.getValue("serviceid", serviceId);
						}
						OrderReportVo obj = billCheckDao.findReverseCount(startDate, endDate, payType, channelId, serviceId);
						if(KstHosConstant.ORDERTYPE_006.equals(serviceId)) {
							ExportOrderReportVo report = new ExportOrderReportVo();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(payVo.getTransCount());
							report.setTransMoney(StringUtil.fenChangeYuan(payVo.getTransMoney()));
							report.setReverseCount(obj.getReverseCount());
							report.setReverseMoney(StringUtil.fenChangeYuan(obj.getReverseMoney()));
							
							or1TransCount += payVo.getTransCount();
							or1TransMoney += payVo.getTransMoney();
							or1ReverseCount += obj.getReverseCount();
							or1ReverseMoney += obj.getReverseMoney();
							or1List.add(report);
						}else if(KstHosConstant.ORDERTYPE_007.equals(serviceId)) {
							ExportOrderReportVo report = new ExportOrderReportVo();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(payVo.getTransCount());
							report.setTransMoney(StringUtil.fenChangeYuan(payVo.getTransMoney()));
							report.setReverseCount(obj.getReverseCount());
							report.setReverseMoney(StringUtil.fenChangeYuan(obj.getReverseMoney()));
							
							or2TransCount += payVo.getTransCount();
							or2TransMoney += payVo.getTransMoney();
							or2ReverseCount += obj.getReverseCount();
							or2ReverseMoney += obj.getReverseMoney();
							or2List.add(report);
						}
					}
					or1.setTransCount(or1TransCount);
					or1.setTransMoney(StringUtil.fenChangeYuan(or1TransMoney));
					or1.setReverseCount(or1ReverseCount);
					or1.setReverseMoney(StringUtil.fenChangeYuan(or1ReverseMoney));
					or1.setVoList(or1List);
					
					or2.setTransCount(or2TransCount);
					or2.setTransMoney(StringUtil.fenChangeYuan(or2TransMoney));
					or2.setReverseCount(or2ReverseCount);
					or2.setReverseMoney(StringUtil.fenChangeYuan(or2ReverseMoney));
					or2.setVoList(or2List);
				}
				if(refundList != null && refundList.size() > 0) {
					int or3TransCount = 0;
					Long or3TransMoney = 0L;
					int or4TransCount = 0;
					Long or4TransMoney = 0L;
					for (OrderReportVo refundVo : refundList) {
						String serviceId = refundVo.getServiceId();
						String serviceValue = "";
						if(StringUtil.isNotBlank(serviceId)) {
							serviceValue = dictLocalCache.getValue("serviceid", serviceId);
						}
						String rm = StringUtil.fenChangeYuan(refundVo.getTransMoney());
						if(KstHosConstant.ORDERTYPE_006.equals(serviceId)) {
							ExportOrderReportVo report = new ExportOrderReportVo();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(refundVo.getTransCount());
							report.setTransMoney(rm);
							report.setReverseCount(0);
							report.setReverseMoney("0");
							
							or3TransCount += refundVo.getTransCount();
							or3TransMoney += refundVo.getTransMoney();
							or3List.add(report);
						}else if(KstHosConstant.ORDERTYPE_007.equals(serviceId)) {
							ExportOrderReportVo report = new ExportOrderReportVo();
							report.setServiceId(serviceId);
							report.setServiceValue(serviceValue);
							report.setTransCount(refundVo.getTransCount());
							report.setTransMoney(rm);
							report.setReverseCount(0);
							report.setReverseMoney("0");
							
							or4TransCount += refundVo.getTransCount();
							or4TransMoney += refundVo.getTransMoney();
							or4List.add(report);
						}
					}
					or3.setTransCount(or3TransCount);
					String or3TransMoneyStr = StringUtil.fenChangeYuan(or3TransMoney);
					or3.setTransMoney(or3TransMoneyStr);
					or3.setReverseCount(0);
					or3.setReverseMoney("0");
					or3.setVoList(or3List);
					
					or4.setTransCount(or4TransCount);
					String or4TransMoneyStr = StringUtil.fenChangeYuan(or4TransMoney);
					or4.setTransMoney(or4TransMoneyStr);
					or4.setReverseCount(0);
					or4.setReverseMoney("0");
					or4.setVoList(or4List);
				}
				respList.add(or1);
				respList.add(or2);
				respList.add(or3);
				respList.add(or4);
			}
		}
		return respList;
	}

	private String getPayLimit(String payType) {
		String configKey = "";
		Set<String[]> channelSet = KasiteConfig.getClientIds();
		for (String[] channel : channelSet) {
			String channelId = channel[0];
			if(ChannelTypeEnum.wechat.name().equals(payType)) {
				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WxPayConfigKey, channelId);
				PayRule payRule = KasiteConfig.getPayRule(configKey);
				if(payRule != null) {
					return payRule.getPayTimeStart() + "~" + payRule.getPayTimeEnd();
				}
			}else if(ChannelTypeEnum.zfb.name().equals(payType)) {
				configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
				PayRule payRule = KasiteConfig.getPayRule(configKey);
				if(payRule != null) {
					return payRule.getPayTimeStart() + "~" + payRule.getPayTimeEnd();
				}
			}
		}
		return null;
	}
	
	/**
	 * 封装serviceId(门诊、住院)
	 * 
	 * @return
	 */
	private List<String> getServiceIdList(String transType){
		Map<String, List<String>> map = new HashMap<>();
		//1.先获取所有的serviceId集合
		List<Dictionary> serviceIdList = dictLocalCache.get("serviceid");
		//门诊的服务ID集合
		Dictionary pOutPatient = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.OUTPATIENT);
		List<String> outPatientServiceIdList = new ArrayList<>();
		//住院的服务ID集合
		Dictionary pHospitalization = dictLocalCache.get(KstHosConstant.HOSBIZTYPE, KstHosConstant.HOSPITALIZATION);
		List<String> hospitalizationServiceIdList = new ArrayList<>();
		
		for (Dictionary dictionary : serviceIdList) {
			if(pOutPatient.getId().equals(dictionary.getUpId())) {
				outPatientServiceIdList.add(dictionary.getKeyword());
			}else if(pHospitalization.getId().equals(dictionary.getUpId())) {
				hospitalizationServiceIdList.add(dictionary.getKeyword());
			}
		}
		map.put(KstHosConstant.OUTPATIENT, outPatientServiceIdList);
		map.put(KstHosConstant.HOSPITALIZATION, hospitalizationServiceIdList);
		return map.get(transType);
	}
}
