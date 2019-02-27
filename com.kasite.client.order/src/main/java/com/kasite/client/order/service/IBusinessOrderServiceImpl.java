package com.kasite.client.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.ArithUtil;
import com.coreframework.util.DateOper;
import com.kasite.client.order.bean.dbo.OrderItem;
import com.kasite.client.order.dao.IOrderItemMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.common.validator.ValidatorUtils;
import com.kasite.core.common.validator.group.AddGroup;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.cache.IDictLocalCache;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IMergeSettledPayReceiptService;
import com.kasite.core.serviceinterface.module.his.handler.IOrderPrescriptionPaymentService;
import com.kasite.core.serviceinterface.module.his.handler.IOrderSettlementService;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryInHospitalRechargeList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderPrescriptionList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderReceiptList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementInfo;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOrderSettlementList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostList;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientCostType;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryOutpatientRechargeList;
import com.kasite.core.serviceinterface.module.msg.IMsgService;
import com.kasite.core.serviceinterface.module.msg.req.ReqSendMsg;
import com.kasite.core.serviceinterface.module.order.IBusinessOrderService;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.req.EqptTypeEnum;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderItem;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderPrescription;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderSub;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqMergeSettledPayReceipt;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderExtension;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderExtension.PrescriptionOrderMemoEnum;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderExtension.SettleOrderSettlementEnum;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryInHospitalCostListByDate;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryInHospitalCostType;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryInHospitalCostTypeItem;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryInHospitalRechargeList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPrescriptionList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSettlementList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSettlementPayList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOutpatientCostListByDate;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOutpatientCostType;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOutpatientCostTypeItem;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOutpatientRechargeList;
import com.kasite.core.serviceinterface.module.order.req.ReqSettleOrderSettlement;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryInHospitalCostList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryInHospitalCostType;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryInHospitalRechargeList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderPrescriptionInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderPrescriptionList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderSettlementInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderSettlementList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOutpatientCostList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOutpatientCostType;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOutpatientRechargeList;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
@Service("order.businessOrder")
public class IBusinessOrderServiceImpl implements IBusinessOrderService{
	public final static Log log = LogFactory.getLog(IBusinessOrderServiceImpl.class);
	@Autowired
	private IOrderService orderService;

	@Autowired
	IBasicService basicService;
	
	@Autowired
	private IDictLocalCache dictLocalCache;
	
	@Autowired
	IOrderItemMapper orderItemMapper;
	
	@Autowired
	IMsgService msgService;
	
	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryInHospitalCostList(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryInHospitalCostListByDate> commReq = new CommonReq<ReqQueryInHospitalCostListByDate>(new ReqQueryInHospitalCostListByDate(msg));
		ReqQueryInHospitalCostListByDate req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(cardNo)) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put("cardType", req.getCardType() + "");
		}
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pIndex", req.getPage().getPIndex() + "");
		map.put("pSize", req.getPage().getPSize() + "");
		CommonResp<HisQueryInHospitalCostList> hisResp = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo())
				.queryInHospitalCostList(msg,map);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<RespQueryInHospitalCostList> respList = new ArrayList<RespQueryInHospitalCostList>();
			if( !CollectionUtils.isEmpty(hisResp.getData())) {
				for(HisQueryInHospitalCostList hisRespVo : hisResp.getData()) {
					ValidatorUtils.validateEntity(hisRespVo,AddGroup.class);
					RespQueryInHospitalCostList resp = new RespQueryInHospitalCostList();
					resp.setFee(hisRespVo.getFee());
					resp.setDate(hisRespVo.getDate());
					resp.setDept(hisRespVo.getDept());
					resp.setDeptStation(hisRespVo.getDeptStation());
					resp.setDoctor(hisRespVo.getDoctor());
					respList.add(resp);
				}
			}
			req.getPage().setPCount(hisResp.getPCount().intValue());
			return new CommonResp<RespQueryInHospitalCostList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
		}else{
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,hisResp.getRetCode(),hisResp.getMessage()).toResult();
		}
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryInHospitalCostType(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryInHospitalCostType> commReq = new CommonReq<ReqQueryInHospitalCostType>(new ReqQueryInHospitalCostType(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryInHospitalCostType req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getCardType())) {
			map.put("cardType", req.getCardType());
		}
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pIndex", req.getPage().getPIndex() + "");
		map.put("pSize", req.getPage().getPSize() + "");

		CommonResp<HisQueryInHospitalCostType> hisResp = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo()).queryInHospitalCostType(msg,map);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<RespQueryInHospitalCostType> respList = new ArrayList<RespQueryInHospitalCostType>();
			if( !CollectionUtils.isEmpty(hisResp.getData())) {
				for( HisQueryInHospitalCostType hisRespVo : hisResp.getData()) {
					ValidatorUtils.validateEntity(hisRespVo,AddGroup.class);
					RespQueryInHospitalCostType respQueryInHospitalCostType = new RespQueryInHospitalCostType();
					respQueryInHospitalCostType.setDate(hisRespVo.getDate());
					respQueryInHospitalCostType.setDept(hisRespVo.getDept());
					respQueryInHospitalCostType.setDeptStation(hisRespVo.getDept());
					respQueryInHospitalCostType.setDoctor(hisRespVo.getDoctor());
					respQueryInHospitalCostType.setExpenseTypeCode(hisRespVo.getExpenseTypeCode());
					respQueryInHospitalCostType.setExpenseTypeName(hisRespVo.getExpenseTypeName());
					respQueryInHospitalCostType.setFee(hisRespVo.getFee());
					respList.add(respQueryInHospitalCostType);
				}
			}
			req.getPage().setPCount(hisResp.getPCount().intValue());
			return new CommonResp<RespQueryInHospitalCostType>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
		}else {
			return new CommonResp<RespQueryInHospitalCostType>(commReq, KstHosConstant.DEFAULTTRAN,hisResp.getRetCode(),hisResp.getMessage()).toResult();
		}
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryInHospitalCostTypeItem(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryInHospitalCostTypeItem> commReq = new CommonReq<ReqQueryInHospitalCostTypeItem>(new ReqQueryInHospitalCostTypeItem(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryInHospitalCostTypeItem req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		if(StringUtil.isNotBlank(cardNo)) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put("cardType", req.getCardType());
		}
		map.put("expenseTypeCode", req.getExpenseTypeCode());
		map.put("date", req.getDate());
		CommonResp<CommonPrescriptionItem> hisResp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryInHospitalCostTypeItem(msg,map);
		//该接口比较简单，直接返回结果。
		return hisResp.toResult();
	}
	
	
	@Override
	public String QueryOutpatientCostList(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryOutpatientCostListByDate> commReq = new CommonReq<ReqQueryOutpatientCostListByDate>(new ReqQueryOutpatientCostListByDate(msg));
		ReqQueryOutpatientCostListByDate req = commReq.getParam();
		Map<String, String> map = new HashMap<String, String>(16);
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(cardNo)) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put("cardType", req.getCardType() + "");
		}
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pIndex", req.getPage().getPIndex() + "");
		map.put("pSize", req.getPage().getPSize() + "");
		CommonResp<HisQueryOutpatientCostList> hisResp =
				 HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryOutpatientCostList(msg, map);
				
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<RespQueryOutpatientCostList> respList = new ArrayList<RespQueryOutpatientCostList>();
			if( !CollectionUtils.isEmpty(hisResp.getData())) {
				for(HisQueryOutpatientCostList hisRespVo : hisResp.getData()) {
					ValidatorUtils.validateEntity(hisRespVo,AddGroup.class);
					RespQueryOutpatientCostList resp = new RespQueryOutpatientCostList();
					resp.setFee(hisRespVo.getFee());
					resp.setDate(hisRespVo.getDate());
					resp.setDept(hisRespVo.getDept());
					resp.setDeptStation(hisRespVo.getDeptStation());
					resp.setDoctor(hisRespVo.getDoctor());
					respList.add(resp);
				}
			}
			req.getPage().setPCount(hisResp.getPCount().intValue());
			return new CommonResp<RespQueryOutpatientCostList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
		}else{
			return new CommonResp<RespQueryOutpatientCostList>(commReq, KstHosConstant.DEFAULTTRAN,hisResp.getRetCode(),hisResp.getMessage()).toResult();
		}
	}

	@Override
	public String QueryOutpatientCostType(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryOutpatientCostType> commReq = new CommonReq<ReqQueryOutpatientCostType>(new ReqQueryOutpatientCostType(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOutpatientCostType req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getCardType())) {
			map.put("cardType", req.getCardType());
		}
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pIndex", req.getPage().getPIndex() + "");
		map.put("pSize", req.getPage().getPSize() + "");

		CommonResp<HisQueryOutpatientCostType> hisResp = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo()).queryOutpatientCostType(msg,map);
		if(KstHosConstant.SUCCESSCODE.equals(hisResp.getCode())) {
			List<RespQueryOutpatientCostType> respList = new ArrayList<RespQueryOutpatientCostType>();
			if( !CollectionUtils.isEmpty(hisResp.getData())) {
				for( HisQueryOutpatientCostType hisRespVo : hisResp.getData()) {
					ValidatorUtils.validateEntity(hisRespVo,AddGroup.class);
					RespQueryOutpatientCostType respQueryOutpatientCostType = new RespQueryOutpatientCostType();
					respQueryOutpatientCostType.setDate(hisRespVo.getDate());
					respQueryOutpatientCostType.setDept(hisRespVo.getDept());
					respQueryOutpatientCostType.setDeptStation(hisRespVo.getDept());
					respQueryOutpatientCostType.setDoctor(hisRespVo.getDoctor());
					respQueryOutpatientCostType.setExpenseTypeCode(hisRespVo.getExpenseTypeCode());
					respQueryOutpatientCostType.setExpenseTypeName(hisRespVo.getExpenseTypeName());
					respQueryOutpatientCostType.setFee(hisRespVo.getFee());
					respQueryOutpatientCostType.setUnit(hisRespVo.getUnit());
					respQueryOutpatientCostType.setUnitPrice(hisRespVo.getUnitPrice());
					respQueryOutpatientCostType.setObjNumber(hisRespVo.getObjNumber());
					respQueryOutpatientCostType.setInvoiceItemName(hisRespVo.getInvoiceItemName());
					respQueryOutpatientCostType.setSpecifications(hisRespVo.getSpecifications());
					respList.add(respQueryOutpatientCostType);
				}
			}
			req.getPage().setPCount(hisResp.getPCount().intValue());
			return new CommonResp<RespQueryOutpatientCostType>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
		}else {
			return new CommonResp<RespQueryOutpatientCostType>(commReq, KstHosConstant.DEFAULTTRAN,hisResp.getRetCode(),hisResp.getMessage()).toResult();
		}
	}

	@Override
	public String QueryOutpatientCostTypeItem(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryOutpatientCostTypeItem> commReq = new CommonReq<ReqQueryOutpatientCostTypeItem>(new ReqQueryOutpatientCostTypeItem(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOutpatientCostTypeItem req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		if(StringUtil.isNotBlank(cardNo)) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put("cardType", req.getCardType());
		}
		map.put("expenseTypeCode", req.getExpenseTypeCode());
		map.put("date", req.getDate());
		CommonResp<CommonPrescriptionItem> hisResp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryOutpatientCostTypeItem(msg,map);
		//该接口比较简单，直接返回结果。
		return hisResp.toResult();
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryInHospitalRechargeList(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryInHospitalRechargeList> commReq = new CommonReq<ReqQueryInHospitalRechargeList>(new ReqQueryInHospitalRechargeList(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryInHospitalRechargeList req = commReq.getParam();
		req = new ReqQueryInHospitalRechargeList(msg);
		PageVo pageVo = req.getPage();
		
		map.put("hospitalNo", req.getHospitalNo());
		map.put("pSize", pageVo.getPSize() + "");
		map.put("pIndex", pageVo.getPIndex() + "");
		map.put("chargeType", req.getChargeType());
		map.put("orderType", req.getOrderType());
		map.put("beginTime", req.getBeginDate());
		map.put("endTime", req.getEndDate());
		basicService.addMemberInfo2Map(commReq, req.getMemberId(), req.getHospitalNo(), KstHosConstant.CARDTYPE_14, req.getOpenId(), map);
		CommonResp<HisQueryInHospitalRechargeList> hisCommonResp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryInHospitalRechargeList(msg,map);
		List<HisQueryInHospitalRechargeList> hisRespList = hisCommonResp.getListCaseRetCode();
		req.getPage().setPCount(hisCommonResp.getPCount().intValue());
		List<RespQueryInHospitalRechargeList> respList = new ArrayList<RespQueryInHospitalRechargeList>();
		if( !CollectionUtils.isEmpty(hisRespList)) {
			for(HisQueryInHospitalRechargeList hisResp : hisRespList) {
				RespQueryInHospitalRechargeList respVo = BeanCopyUtils.copyProperties(hisResp, new RespQueryInHospitalRechargeList(), null);
				respList.add(respVo);
			}
		}
		return new CommonResp<RespQueryInHospitalRechargeList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOutpatientRechargeList(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryOutpatientRechargeList> commReq = new CommonReq<ReqQueryOutpatientRechargeList>(new ReqQueryOutpatientRechargeList(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOutpatientRechargeList req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getCardType())) {
			map.put("cardType", req.getCardType());
		}
		map.put("pSize", req.getPage().getPSize() + "");
		map.put("pIndex", req.getPage().getPIndex() + "");
		map.put("chargeType", req.getChargeType());
		map.put("orderType", req.getOrderType());
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());

		CommonResp<HisQueryOutpatientRechargeList> hisCommonResp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryHosOutpatientRecords(msg,map);
		List<HisQueryOutpatientRechargeList>  hisRespList = hisCommonResp.getListCaseRetCode(); 
		req.getPage().setPCount(hisCommonResp.getPCount().intValue());
		List<RespQueryOutpatientRechargeList> respList = new ArrayList<RespQueryOutpatientRechargeList>();
		if( !CollectionUtils.isEmpty(hisRespList)) {
			for(HisQueryOutpatientRechargeList hisResp : hisRespList) {
				RespQueryOutpatientRechargeList respVo = BeanCopyUtils.copyProperties(hisResp, new RespQueryOutpatientRechargeList(), null);
				respList.add(respVo);
			}
		}
		return new CommonResp<RespQueryOutpatientRechargeList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();		
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryOrderPrescriptionList(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryOrderPrescriptionList> commReq = new CommonReq<ReqQueryOrderPrescriptionList>(new ReqQueryOrderPrescriptionList(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOrderPrescriptionList req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
	
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getCardType())) {
			map.put("cardType", req.getCardType());
		}
		map.put("orderState", req.getOrderState());
		map.put("serviceId", req.getServiceId());
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pSize", req.getPage().getPSize().toString());
		map.put("pIndex", req.getPage().getPIndex().toString());
		
		IOrderPrescriptionPaymentService orderPrescriptionPaymentService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderPrescriptionPaymentService.class);
		if( orderPrescriptionPaymentService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderPrescriptionPaymentService ");
		}else {
			CommonResp<HisQueryOrderPrescriptionList> hisResp = orderPrescriptionPaymentService.queryOrderPrescriptionList(msg,map);
			if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode() )) {
				List<RespQueryOrderPrescriptionList> respList = new ArrayList<RespQueryOrderPrescriptionList>();
				if( !CollectionUtils.isEmpty(hisResp.getData())) {
					for(HisQueryOrderPrescriptionList hisRespVo : hisResp.getData()) {
						ValidatorUtils.validateEntity(hisRespVo,AddGroup.class);
						RespQueryOrderPrescriptionList respVo = new RespQueryOrderPrescriptionList();
						respVo.setPrescTime(hisRespVo.getOrderTime());
						respVo.setCardNo(hisRespVo.getCardNo());
						respVo.setCardType(hisRespVo.getCardType());
						respVo.setIfOnlinePay(hisRespVo.getIfOnlinePay());
						respVo.setOrderState(hisRespVo.getOrderState());
						respVo.setPayMoney(hisRespVo.getPayMoney());
						respVo.setPrescNo(hisRespVo.getPrescNo());
						respVo.setServiceId(hisRespVo.getServiceId());
						respVo.setTotalMoney(hisRespVo.getTotalMoney());
						respVo.setHisOrderId(hisRespVo.getHisOrderId());
						respVo.setDoctorName(hisRespVo.getDoctorName());
						respVo.setDeptName(hisRespVo.getDeptName());
						Dictionary dic = dictLocalCache.get("serviceid", hisRespVo.getServiceId());
						//默认诊间处方
						Dictionary defaultDic = dictLocalCache.get("serviceid", KstHosConstant.ORDERTYPE_008);
						String serviceName = defaultDic.getValue();
						if( dic != null ) {
							serviceName = dic.getValue();
						}
						respVo.setServiceName(serviceName);
						respList.add(respVo);
					}
				}
				req.getPage().setPCount(hisResp.getPCount().intValue());
				return new CommonResp<RespQueryOrderPrescriptionList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
			}else {
				//如果返回异常结果，则直接返回前端
				return hisResp.toResult();
			}
		}
	}

	/**
	 * @param msg
	 * @return
	 */
	@Override
	public String QueryOrderPrescriptionInfo(InterfaceMessage msg) throws Exception{
		CommonReq<ReqQueryOrderPrescriptionInfo> commReq = new CommonReq<ReqQueryOrderPrescriptionInfo>(new ReqQueryOrderPrescriptionInfo(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOrderPrescriptionInfo req = commReq.getParam();		
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		try {
			basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.hisOrderId.name(), req.getHisOrderId());
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.prescNo.name(), req.getPrescNo());
		// 入参xml转map格式
		if(StringUtil.isNotBlank(cardNo)) {
			map.put(ApiKey.HisQueryOrderPrescriptionInfo.cardNo.name(), req.getCardNo());
		}
		if(StringUtil.isNotBlank(cardType)) {
			map.put(ApiKey.HisQueryOrderPrescriptionInfo.cardType.name(), req.getCardType());
		}
		if(null != req && null != req.getData_1()) {
			map.put("orderMemo", req.getData_1().toJSONString());
		}
		IOrderPrescriptionPaymentService orderPrescriptionPaymentService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderPrescriptionPaymentService.class);
		if(orderPrescriptionPaymentService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderPrescriptionPaymentService ");
		}else {
			CommonResp<HisQueryOrderPrescriptionInfo> hisResp = orderPrescriptionPaymentService.queryOrderPrescriptionInfo(msg,map);
			RespQueryOrderPrescriptionInfo prescInfo = new RespQueryOrderPrescriptionInfo();
			if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode() )) {
				if( !CollectionUtils.isEmpty(hisResp.getData())) {
					HisQueryOrderPrescriptionInfo hisprescInfo = hisResp.getData().get(0);
					ValidatorUtils.validateEntity(hisprescInfo,AddGroup.class);
					prescInfo.setCardNo(hisprescInfo.getCardNo());
					prescInfo.setCardType(hisprescInfo.getCardType().toString());
					prescInfo.setData_1(hisprescInfo.getData_1());
					prescInfo.setDeptCode(hisprescInfo.getDeptCode());
					prescInfo.setDeptName(hisprescInfo.getDeptName());
					prescInfo.setDoctorCode(hisprescInfo.getDoctorCode());
					prescInfo.setDoctorName(hisprescInfo.getDoctorName());
					prescInfo.setHisOrderId(hisprescInfo.getHisOrderId());
					prescInfo.setIfOnlinePay(hisprescInfo.getIfOnlinePay());
					prescInfo.setMemberName(hisprescInfo.getMemberName());
					prescInfo.setOrderMemo(hisprescInfo.getOrderMemo());
					prescInfo.setOrderState(hisprescInfo.getOrderState());
					prescInfo.setOrderTime(hisprescInfo.getOrderTime());
					prescInfo.setPayMoney(hisprescInfo.getPayMoney());
					prescInfo.setPrescNo(hisprescInfo.getPrescNo());
					prescInfo.setPriceName(hisprescInfo.getPriceName());
					prescInfo.setServiceId(hisprescInfo.getServiceId());
					prescInfo.setTotalMoney(hisprescInfo.getTotalMoney());
					String serviceId = hisprescInfo.getServiceId();
					Dictionary dic = dictLocalCache.get("serviceid", serviceId);
					//默认诊间处方
					Dictionary defaultDic = dictLocalCache.get("serviceid", KstHosConstant.ORDERTYPE_008);
					String serviceName = defaultDic.getValue();
					if( dic != null ) {
						serviceName = dic.getValue();
					}
					prescInfo.setServiceName(serviceName);
				}else {
					//调用HIS返回成功，并且无数据时，查询数据库
					ReqOrderDetailLocal reqOrderDetailLocal = new ReqOrderDetailLocal(msg, null, req.getHisOrderId(),KstHosConstant.ORDEROVER_0_STR);
					CommonResp<RespOrderDetailLocal>  orderResp = orderService.orderDetailLocal(new CommonReq<ReqOrderDetailLocal> (reqOrderDetailLocal));
					RespOrderDetailLocal orderDetailLocal = orderResp.getDataCaseRetCode();
					prescInfo.setCardNo(orderDetailLocal.getCardNo());
					prescInfo.setCardType(orderDetailLocal.getCardType());
					String orderMemo = orderDetailLocal.getOrderMemo();
					JSONObject orderMemoJosn = new JSONObject();
					if( !StringUtil.isEmpty(orderMemo)) {
						orderMemoJosn = JSONObject.parseObject(orderMemo);
					}
					prescInfo.setDeptCode(orderMemoJosn.getString(PrescriptionOrderMemoEnum.DeptCode.toString()));
					prescInfo.setDeptName(orderMemoJosn.getString(PrescriptionOrderMemoEnum.DeptName.toString()));
					prescInfo.setDoctorCode(orderMemoJosn.getString(PrescriptionOrderMemoEnum.DoctorCode.toString()));
					prescInfo.setDoctorName(orderMemoJosn.getString(PrescriptionOrderMemoEnum.DoctorName.toString()));
					prescInfo.setHisOrderId(orderDetailLocal.getPrescNo());
					prescInfo.setIfOnlinePay(orderDetailLocal.getIsOnlinePay());
					prescInfo.setMemberName(orderDetailLocal.getMemberName());
					prescInfo.setOrderMemo(orderDetailLocal.getOrderMemo());
					prescInfo.setOrderState(orderDetailLocal.getPayState());
					prescInfo.setOrderTime(orderDetailLocal.getBeginDate());
					prescInfo.setPayMoney(orderDetailLocal.getPayMoney());
					prescInfo.setPrescNo(orderDetailLocal.getPrescNo());
					prescInfo.setPriceName(orderDetailLocal.getPriceName());
					prescInfo.setServiceId(orderDetailLocal.getServiceId());
					prescInfo.setTotalMoney(orderDetailLocal.getTotalMoney());
					OrderItem queryItem = new OrderItem();
					queryItem.setHisOrderId(req.getHisOrderId());
					List<OrderItem> itemList = orderItemMapper.select(queryItem);
					List<CommonPrescriptionItem> commonItemList = new ArrayList<CommonPrescriptionItem>();
					if( itemList!=null && itemList.size()>0 ) {
						for(OrderItem item : itemList) {
							CommonPrescriptionItem prescriptionItem = new CommonPrescriptionItem();
							prescriptionItem.setNumber(item.getNumber());
							prescriptionItem.setProject(item.getProject());
							prescriptionItem.setSpecifications(item.getSpecifications());
							prescriptionItem.setSumOfMoney(item.getSumOfMoney());
							prescriptionItem.setUnitPrice(item.getUnitPrice());
							commonItemList.add(prescriptionItem);
						}
					}
					prescInfo.setData_1(commonItemList);
				}
				return new CommonResp<RespQueryOrderPrescriptionInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,prescInfo).toResult();
			}else {
				//如果返回异常结果，则直接返回前端
				return hisResp.toResult();
			}
		}
		
		
	}

	
	@Override
	public String AddOrderPrescription(InterfaceMessage msg)throws Exception{
		CommonReq<ReqAddOrderPrescription> commReq = new CommonReq<ReqAddOrderPrescription>(new ReqAddOrderPrescription(msg));		
		ReqAddOrderPrescription reqAddOrderPrescription = commReq.getParam();
		//如果存在处方订单金额变动前的订单，则取消订单
		if(!StringUtil.isEmpty(reqAddOrderPrescription.getOldOrderId())) {
			ReqCancelOrder reqCancelOrder = new ReqCancelOrder(msg, reqAddOrderPrescription.getOldOrderId(), 
					reqAddOrderPrescription.getOperatorId(), reqAddOrderPrescription.getOperatorName());
			CommonResp<RespMap> cancelOrderResp = orderService.cancelOrder(new CommonReq<ReqCancelOrder>(reqCancelOrder));
			if( !KstHosConstant.SUCCESSCODE.equals(cancelOrderResp.getCode())) {
				return new CommonResp<ReqAddOrderPrescription>(commReq,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERPRICE,"处方订单金额改变，取消旧订单失败！重新支付，或者联系管理员！").toResult();
			}
		}
		Map<String, String> map = new HashMap<String, String>(16);
		
		String openId = reqAddOrderPrescription.getOpenId();
		String memberId = reqAddOrderPrescription.getMemberId();
		String cardNo = reqAddOrderPrescription.getCardNo();
		String cardType = reqAddOrderPrescription.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);	
				
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.hisOrderId.name(), reqAddOrderPrescription.getHisOrderId());
		map.put(ApiKey.HisQueryOrderPrescriptionInfo.prescNo.name(), reqAddOrderPrescription.getPrescNo());
		if(null != reqAddOrderPrescription && null != reqAddOrderPrescription.getData_1()) {
			map.put("orderMemo", reqAddOrderPrescription.getData_1().toJSONString());
		}
		IOrderPrescriptionPaymentService orderPrescriptionPaymentService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderPrescriptionPaymentService.class);
		if(orderPrescriptionPaymentService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderPrescriptionPaymentService ");
		}else {
			CommonResp<HisQueryOrderPrescriptionInfo> hisResp = orderPrescriptionPaymentService.queryOrderPrescriptionInfo(msg,map);
			if( KstHosConstant.SUCCESSCODE.equals(hisResp.getCode()) && !CollectionUtils.isEmpty(hisResp.getData())) {
				HisQueryOrderPrescriptionInfo hisprescInfo = hisResp.getData().get(0);
				if(!reqAddOrderPrescription.getPayMoney().equals(hisprescInfo.getPayMoney())) {
					//前端传入金额，与查询HIS的处方金额不一致，则直接返回错误，要求重新支付。
					//防止前端修改金额
					return new CommonResp<ReqAddOrderPrescription>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERPRICE,"该金额与医院处方金额不一致，请重新支付！").toResult();
				}
				ReqOrderExtension reqOrderExtension = new ReqOrderExtension();
				reqOrderExtension.addPrescriptionOrderMemo(PrescriptionOrderMemoEnum.DeptCode,hisprescInfo.getDeptCode())
				.addPrescriptionOrderMemo(PrescriptionOrderMemoEnum.DeptName,hisprescInfo.getDeptName())
				.addPrescriptionOrderMemo(PrescriptionOrderMemoEnum.DoctorCode,hisprescInfo.getDoctorCode())
				.addPrescriptionOrderMemo(PrescriptionOrderMemoEnum.DoctorName,hisprescInfo.getDoctorName());
				if(null != reqAddOrderPrescription && null != reqAddOrderPrescription.getData_1()) {
					reqOrderExtension.putAll(reqAddOrderPrescription.getData_1());
				}
				
				ReqAddOrderLocal addOrderReq = new ReqAddOrderLocal(msg, null, hisprescInfo.getHisOrderId(), hisprescInfo.getPayMoney()
						, hisprescInfo.getTotalMoney(), hisprescInfo.getPriceName(), hisprescInfo.getCardNo(),
						hisprescInfo.getCardType().toString(), reqAddOrderPrescription.getOperatorId(), reqAddOrderPrescription.getOperatorName(), 
						hisprescInfo.getServiceId(), 1, reqAddOrderPrescription.getEqptType(), reqOrderExtension);
				
				CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderReq));
				//是否存在子项目
				if( hisprescInfo.getData_1()!=null && hisprescInfo.getData_1().size()>0) {
					OrderItem queryItem = new OrderItem(); 
					queryItem.setHisOrderId(reqAddOrderPrescription.getHisOrderId());
					int count = orderItemMapper.selectCount(queryItem);
					if( count<1) {
						for(CommonPrescriptionItem item :  hisprescInfo.getData_1()) {
							OrderItem orderItem = new OrderItem();
							orderItem.setHisOrderId(reqAddOrderPrescription.getHisOrderId());
							orderItem.setNumber(item.getNumber());
							orderItem.setProject(item.getProject());
							orderItem.setSpecifications(item.getSpecifications());
							orderItem.setSumOfMoney(item.getSumOfMoney());
							orderItem.setUnitPrice(item.getUnitPrice());
							orderItemMapper.insert(orderItem);
						}
					}
				}
				
				if( KstHosConstant.SUCCESSCODE.equals(addOrderResp.getCode()) && !CollectionUtils.isEmpty(addOrderResp.getData())) {
					return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,addOrderResp.getData().get(0)).toResult();
				}else {
					return hisResp.toResult();
				}
			}else {
				return hisResp.toResult();
			}
		}
	}
	
	/**
	 * @param msg
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	@Override
	public String QueryOrderSettlementList(InterfaceMessage msg) throws Exception  {
		CommonReq<ReqQueryOrderSettlementList> commReq = 
				new CommonReq<ReqQueryOrderSettlementList>(new ReqQueryOrderSettlementList(msg));
		Map<String, String> map = new HashMap<String, String>(16);

		ReqQueryOrderSettlementList req = commReq.getParam();
		
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		// 入参xml转map格式
		if(StringUtil.isNotBlank(req.getCardNo())) {
			map.put("cardNo", req.getCardNo());
		}
		if(StringUtil.isNotBlank(req.getCardType())) {
			map.put("cardType", req.getCardType());
		}
		map.put("isSettlement", req.getIsSettlement());
		map.put("beginDate", req.getBeginDate());
		map.put("endDate", req.getEndDate());
		map.put("pSize", req.getPage().getPSize() + "");
		map.put("pIndex", req.getPage().getPIndex() + "");
		IOrderSettlementService orderSettlementService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderSettlementService.class);
		if(orderSettlementService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderSettlementService ");
		}else {
			CommonResp<HisQueryOrderSettlementList> hisCommonResp = orderSettlementService.queryOrderSettlementList(msg,map);
			List<RespQueryOrderSettlementList> respList = new ArrayList<RespQueryOrderSettlementList>();
			if( KstHosConstant.SUCCESSCODE.equals(hisCommonResp.getCode()) ) {
				if( !CollectionUtils.isEmpty(hisCommonResp.getData())) {
					for(HisQueryOrderSettlementList hisResp : hisCommonResp.getData()) {
						ValidatorUtils.validateEntity(hisResp,AddGroup.class);
						RespQueryOrderSettlementList respVo = new RespQueryOrderSettlementList();
						respVo.setHisOrderId(hisResp.getHisOrderId());
						respVo.setDeptName(hisResp.getDeptName());
						respVo.setDoctorName(hisResp.getDoctorName());
						respVo.setIsSettlement(hisResp.getIsSettlement());
						respVo.setPrescNo(hisResp.getPrescNo());
						respVo.setPrescTime(hisResp.getPrescTime());
						respVo.setPrescType(hisResp.getPrescType());
						respVo.setPrice(hisResp.getPrice());
						respList.add(respVo);
					}
				}
				req.getPage().setPCount(hisCommonResp.getPCount().intValue());
				return new CommonResp<RespQueryOrderSettlementList>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respList,req.getPage()).toResult();
			}else {
				return hisCommonResp.toResult();
			}
		}
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryOrderSettlementInfo(InterfaceMessage msg) throws Exception {
		CommonReq<ReqQueryOrderSettlementList> commReq = 
				new CommonReq<ReqQueryOrderSettlementList>(new ReqQueryOrderSettlementList(msg));		
		Map<String, String> map = new HashMap<String, String>(16);
		ReqQueryOrderSettlementList req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		map.put("orderId", req.getHisOrderId());
		map.put("prescNo", req.getPrescNo());
		map.put("isSettlement", req.getIsSettlement());
		IOrderSettlementService orderSettlementService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderSettlementService.class);
		if(orderSettlementService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderSettlementService ");
		}else {
			CommonResp<HisQueryOrderSettlementInfo> hisCommonResp = orderSettlementService.queryOrderSettlementInfo(msg,map);
			RespQueryOrderSettlementInfo respVo = new RespQueryOrderSettlementInfo();
			if( KstHosConstant.SUCCESSCODE.equals(hisCommonResp.getCode()) ) {
				if( !CollectionUtils.isEmpty(hisCommonResp.getData())) {
					HisQueryOrderSettlementInfo hisResp = hisCommonResp.getData().get(0);
					ValidatorUtils.validateEntity(hisResp,AddGroup.class);
					respVo.setCardNo(hisResp.getCardNo());
					respVo.setCardType(hisResp.getCardType());
					respVo.setData_1(hisResp.getData_1());
					respVo.setDeptName(hisResp.getDeptName());
					respVo.setDoctorName(hisResp.getDoctorName());
					respVo.setHisOrderId(hisResp.getHisOrderId());
					respVo.setIsSettlement(hisResp.getIsSettlement());
					respVo.setMemberName(hisResp.getMemberName());
					respVo.setPrescNo(hisResp.getPrescNo());
					respVo.setPrescTime(hisResp.getPrescTime());
					respVo.setPrescType(hisResp.getPrescType());
					respVo.setPrice(hisResp.getPrice());
				}
				req.getPage().setPCount(hisCommonResp.getPCount().intValue());
				return new CommonResp<RespQueryOrderSettlementInfo>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respVo).toResult();
			}else {
				return hisCommonResp.toResult();
			}
		}
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String SettleOrderSettlement(InterfaceMessage msg) throws Exception {
		CommonReq<ReqSettleOrderSettlement> commReq = 
				new CommonReq<ReqSettleOrderSettlement>(new ReqSettleOrderSettlement(msg));
		Map<String, String> map = new HashMap<String, String>(16);
		ReqSettleOrderSettlement req = commReq.getParam();
		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		if(StringUtil.isBlank(memberId)) {
			memberId = map.getOrDefault("memberId", null);
		}
		
		map.put("cardNo", req.getCardNo());
		map.put("cardType", req.getCardType());
		
		//先查询HIS结算单明细
		map.put("orderId", req.getHisOrderIds());
		map.put("prescNo", req.getPrescNos());
		IOrderSettlementService orderSettlementService = HandlerBuilder.get()
				.getCallHisService(commReq.getParam().getAuthInfo(),IOrderSettlementService.class);
		if(orderSettlementService == null ) {
			throw new RRException("该医院未实现处方支付的接口： IOrderSettlementService ");
		}else {
			CommonResp<HisQueryOrderSettlementInfo> infoResp = orderSettlementService.queryOrderSettlementInfo(msg, map);
			if(!KstHosConstant.SUCCESSCODE.equals(infoResp.getCode())) {
				return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CALLHIS,infoResp.getMessage()).toResult();
			}
			if(infoResp.getData()==null || infoResp.getData().size()<=0) {
				return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_DATAEXIST,"没有查询到处方信息。").toResult();
			}
			HisQueryOrderSettlementInfo info = infoResp.getResultData();
			// 查询就诊卡余额
			CommonResp<RespMap> balanceResp = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo()).queryCardBalance(msg,map);
			//如果返回失败，或者查不到余额
			if( KstHosConstant.SUCCESSCODE.equals(balanceResp.getCode()) && !StringUtil.isEmpty(balanceResp.getData())) {
				String balance = balanceResp.getData().get(0).getString(ApiKey.HisQueryCardBalance.Balance);
				if (StringUtil.isBlank(balance)) {
					return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CALLHIS,"验证就诊卡余额失败！无法结算！").toResult();
				}
				if (new Integer(balance) < req.getTotalPrice().intValue()) {
					return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_BALANCENotENOUGH).toResult();
				}
				//新增全流程订单
				String orderId = null;
				ReqOrderExtension reqOrderExtension = new ReqOrderExtension();
		//			reqOrderExtension.setExtension(req.getHisOrderIds()+"|"+req.getPrescNos());
				reqOrderExtension.addSettleOrderSettlementMemo(SettleOrderSettlementEnum.HisOrderId, req.getHisOrderIds());
				reqOrderExtension.addSettleOrderSettlementMemo(SettleOrderSettlementEnum.PrescNos, req.getPrescNos());
				reqOrderExtension.addSettleOrderSettlementMemo(SettleOrderSettlementEnum.DoctorName, info.getDoctorName());
				reqOrderExtension.addSettleOrderSettlementMemo(SettleOrderSettlementEnum.DeptName, info.getDeptName());
				
				ReqAddOrderLocal addOrderReq = new ReqAddOrderLocal(msg, null, req.getPrescNos(),req.getTotalPrice(), req.getTotalPrice(),
						 "诊间订单结算", req.getCardNo(), req.getCardType(), req.getOpenId(), 
						req.getOperatorName(), KstHosConstant.ORDERTYPE_008, 2, 1,memberId,map.get("hisMemberId"), reqOrderExtension);
				
				CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderReq));
				if(KstHosConstant.SUCCESSCODE.equals(addOrderResp.getCode()) && !StringUtil.isEmpty(addOrderResp.getData())) {
					orderId = addOrderResp.getData().get(0).getString(ApiKey.AddOrderLocal.OrderId);
					//保存处方明细信息
					int no = 1;
					StringBuffer nameSbuff = new StringBuffer();
					List<OrderItem> addReqList = new ArrayList<>();
					for (CommonPrescriptionItem res : info.getData_1()) {
						nameSbuff.append(no).append(".").append(res.getProject()).append("\\n");
						no++;
						OrderItem item = new OrderItem();
						item.setOrderId(orderId);
						item.setHisOrderId(info.getHisOrderId());
						item.setNumber(res.getNumber());
						item.setProject(res.getProject());
						item.setSpecifications(res.getSpecifications());
						item.setSumOfMoney(res.getSumOfMoney());
						item.setUnitPrice(res.getUnitPrice());
						item.setUnit(res.getUnit());
						addReqList.add(item);
					}
					ReqAddOrderItem addOrderItemReq = new ReqAddOrderItem(msg, null, 
							null, null,null, null, null, null, addReqList);
					orderService.addOrderItem(new CommonReq<ReqAddOrderItem>(addOrderItemReq));
					map.put("orderId", orderId);
					map.put("price", req.getTotalPrice().toString());
					map.put("prescNos", req.getPrescNos());
					map.put("hisOrderIds", req.getHisOrderIds());
					CommonResp<RespMap> hisCommonResp = orderSettlementService.settleOrderSettlement(msg,map);
					if(KstHosConstant.SUCCESSCODE.equals(hisCommonResp.getCode())) {
						//结算成功，回调订单业务完成
						ReqBizForCompletion reqBizForCompletion = new ReqBizForCompletion(msg, orderId, req.getOpenId(), req.getOperatorName());
						orderService.bizForCompletion(new CommonReq<ReqBizForCompletion>(reqBizForCompletion));
						
						//推送结算成功消息
						Element data1 = DocumentHelper.createElement(KstHosConstant.DATA_1);
						XMLUtil.addElement(data1, "NowDate", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
						XMLUtil.addElement(data1, "DeptName", info.getDeptName());
						XMLUtil.addElement(data1, "DoctorName", info.getDoctorName());
						XMLUtil.addElement(data1, "ProjectName", nameSbuff.toString());
						XMLUtil.addElement(data1, "Price", ArithUtil.div(info.getPrice(), 100, 2));
						XMLUtil.addElement(data1, "OrderId", orderId);
						XMLUtil.addElement(data1, "operId", req.getOpenId());
						ReqSendMsg queue = new ReqSendMsg(msg,"", 1, req.getClientId(), "", "",
								"", KstHosConstant.MODETYPE_20101112, data1.asXML(), req.getOpenId(), 1, req.getOpenId(), req.getOperatorName(), orderId, "", "", "");
						CommonReq<ReqSendMsg> reqSendMsg = new CommonReq<ReqSendMsg>(queue);
						msgService.sendMsg(reqSendMsg);
						return new CommonResp<>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000).toResult();
					}else {
						return hisCommonResp.toResult();
					}
				}else {
					return addOrderResp.toResult();
				}
			}else {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CALLHIS,"验证就诊卡余额失败！无法结算！").toResult();
			}
		}
		
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String QueryUnsettledOrderReceiptList(InterfaceMessage msg) throws Exception {		
		CommonReq<ReqQueryOrderSettlementPayList> commReq = 
		new CommonReq<ReqQueryOrderSettlementPayList>(new ReqQueryOrderSettlementPayList(msg));
		ReqQueryOrderSettlementPayList req = commReq.getParam();
//		ReqQueryMemberInfo reqQueryMemberInfo = new ReqQueryMemberInfo(msg, req.getMemberId(),
//				req.getCardNo(), req.getCardType(), req.getOpenId(), false);
//		//查询数据库是否有hisMemberId
//		RespQueryMemberList respQueryMemberList = basicService.queryMemberInfo(new CommonReq<ReqQueryMemberInfo>(reqQueryMemberInfo)).getDataCaseRetCode();
		Map<String, String> map = new HashMap<String, String>(16);
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		//暂时不需要什么逻辑，HIS的返回结果，直接返回前端
		IMergeSettledPayReceiptService service = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo(),IMergeSettledPayReceiptService.class);
		if(null == service) {
			throw new RRException("该医院未实现合并支付的接口： IMergeSettledPayReceiptService ");
		}
		return	service.queryOrderReceiptList(msg,map).toResult();
	}

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@Override
	public String MergeSettledPayReceipt(InterfaceMessage msg) throws Exception {
		CommonReq<ReqMergeSettledPayReceipt> commReq = 
				new CommonReq<ReqMergeSettledPayReceipt>(new ReqMergeSettledPayReceipt(msg));
		ReqMergeSettledPayReceipt req = commReq.getParam();
		//这个不知道为啥new出来，注释掉，-linjf 2019年1月28日 11:27:00
		//new CommonReq<ReqQueryOrderSettlementPayList>(new ReqQueryOrderSettlementPayList(msg));
		Map<String, String> map = new HashMap<String, String>(16);
 		
		String openId = req.getOpenId();
		String memberId = req.getMemberId();
		String cardNo = req.getCardNo();
		String cardType = req.getCardType();
		basicService.addMemberInfo2Map(commReq, memberId, cardNo, cardType, openId, map);
		
		map.put("receiptNos", req.getReceiptNos());
		IMergeSettledPayReceiptService service = HandlerBuilder.get().getCallHisService(commReq.getParam().getAuthInfo(),IMergeSettledPayReceiptService.class);
		if(null == service) {
			throw new RRException("该医院未实现合并支付的接口： IMergeSettledPayReceiptService ");
		}
		List<HisQueryOrderReceiptList>	receiptList = service.queryOrderReceiptList(msg,map).getListCaseRetCode();
		//OrderItem=订单的明细项。
		//前端关于订单明细项的展示，正常流程为直接查询HIS的接口展示。
		//但是部分医院，支付完HIS的处方/医嘱/订单后，HIS的接口不提供订单明细项的查询；
		//所以在此处用户合并支付前，读取HIS的处方/医嘱/订单的明细项，存入OrderItem。用于前端展示使用
		int mergePrice =0;
		String orderId = StringUtil.getUUID();
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for( HisQueryOrderReceiptList receipt : receiptList ) {
			mergePrice += receipt.getTotalPrice().intValue();
			for(CommonPrescriptionItem commItem : receipt.getItemList()) {
				OrderItem orderItem = new OrderItem();
				orderItem.setHisOrderId(receipt.getReceiptNo());
				orderItem.setOrderId(orderId);
				orderItem.setNumber(commItem.getNumber());
				orderItem.setProject(commItem.getProject());
				orderItem.setSpecifications(commItem.getSpecifications());
				orderItem.setSumOfMoney(commItem.getSumOfMoney());
				orderItem.setUnitPrice(commItem.getUnitPrice());
				//保存orderItem
				orderItemList.add(orderItem);
			}
		}
		//根据前端传入的receiptNos查询HIS的单据号数据 
		//将HIS的总金额与剖前端传入的合并总金额对比，防止前端篡改金额，或者HIS修改金额
		if( mergePrice != req.getTotalPrice().intValue()) {
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERPRICE).toResult();
		}
		//新增本地订单
		ReqAddOrderLocal addOrderReq = new ReqAddOrderLocal(msg, orderId,null, req.getTotalPrice(), req.getTotalPrice(),
				"单据合并支付", req.getCardNo(), req.getCardType(), req.getOpenId(),
				req.getOperatorName(), BusinessTypeEnum.ORDERTYPE_011.getCode(), 1, 
				EqptTypeEnum.wechat_zfb.getCode(),memberId,map.get("hisMemberId") ,null);
		CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(new CommonReq<ReqAddOrderLocal>(addOrderReq));
		String[] receiptNames = req.getReceiptNames().split(",");
		String[] receiptPrices = req.getReceiptPrices().split(",");
		String[] receiptNos = req.getReceiptNos().split(",");
		String[] hisRegIds = null;
		if( !StringUtil.isEmpty(req.getReceiptHisRegIds())) {
			hisRegIds = req.getReceiptHisRegIds().split(",");
		}
		//全流程订单的子订单。用于合并支付，子订单的信息的保存。
		for(int i=0;i<receiptNos.length;i++) {
			ReqAddOrderSub reqAddOrderSubLocal = new  ReqAddOrderSub(msg,orderId, receiptNos[i], new Integer(receiptPrices[i]),
					receiptNames[i],hisRegIds!=null?hisRegIds[i]:null);
			CommonReq<ReqAddOrderSub> reqAddOrderSub = new CommonReq<ReqAddOrderSub>(reqAddOrderSubLocal);
			//新增子订单
			orderService.addOrderSub(reqAddOrderSub);
		}
		ReqAddOrderItem addOrderItemReq = new ReqAddOrderItem(msg, null, 
				null, null,null, null, null, null, orderItemList);
		orderService.addOrderItem(new CommonReq<ReqAddOrderItem>(addOrderItemReq));
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,addOrderResp.getData().get(0)).toResult();
	}

}
