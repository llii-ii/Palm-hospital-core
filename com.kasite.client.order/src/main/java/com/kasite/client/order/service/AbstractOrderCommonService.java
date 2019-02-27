package com.kasite.client.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.coreframework.util.DateOper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kasite.client.order.bean.dbo.BizOrder;
import com.kasite.client.order.bean.dbo.Order;
import com.kasite.client.order.bean.dbo.OrderItem;
import com.kasite.client.order.bean.dbo.OrderMember;
import com.kasite.client.order.bean.dbo.OrderSub;
import com.kasite.client.order.bean.dbo.OverOrder;
import com.kasite.client.order.bean.dbo.PayOrder;
import com.kasite.client.order.bean.dbo.RefundOrder;
import com.kasite.client.order.bean.dto.QueryOrderViewParam;
import com.kasite.client.order.circuitbreaker.KasiteHystrixCommandKey;
import com.kasite.client.order.circuitbreaker.KasiteHystrixConfig;
import com.kasite.client.order.dao.IBizOrderMapper;
import com.kasite.client.order.dao.IOrderHisInfoMapper;
import com.kasite.client.order.dao.IOrderItemMapper;
import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.client.order.dao.IOrderMemberMapper;
import com.kasite.client.order.dao.IOrderOutBizMapper;
import com.kasite.client.order.dao.IOrderSubMapper;
import com.kasite.client.order.dao.IOverOrderMapper;
import com.kasite.client.order.dao.IPayOrderMapper;
import com.kasite.client.order.dao.IRefundOrderMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.DefaultClientEnum;
import com.kasite.core.common.config.KasiteConfig;
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
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.dbo.Member;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.HisInfStatus;
import com.kasite.core.serviceinterface.module.his.ICallHisService;
import com.kasite.core.serviceinterface.module.his.handler.IHisOrderValidator;
import com.kasite.core.serviceinterface.module.his.resp.HisRegFlag;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.dbo.OrderHisInfo;
import com.kasite.core.serviceinterface.module.order.dbo.OrderView;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderItem;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderSub;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqBizForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqCancelOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqForceCorrectOrderBiz;
import com.kasite.core.serviceinterface.module.order.req.ReqGetOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsCancel;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderIsPayment;
import com.kasite.core.serviceinterface.module.order.req.ReqOrderListLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqPayForCompletion;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPayState;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderProcess;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSubList;
import com.kasite.core.serviceinterface.module.order.req.ReqRevokeOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqSyncLocalOrderState;
import com.kasite.core.serviceinterface.module.order.resp.CommonPrescriptionItem;
import com.kasite.core.serviceinterface.module.order.resp.RespGetOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespGetPayOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderProcess;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderSubList;
import com.kasite.core.serviceinterface.module.pay.IBizPayStartOrderCheckHandler;
import com.kasite.core.serviceinterface.module.pay.IPayMerchantService;
import com.kasite.core.serviceinterface.module.pay.IPayService;
import com.kasite.core.serviceinterface.module.pay.req.ReqMerchantNotifyForceRetry;
import com.kasite.core.serviceinterface.module.pay.req.ReqPayStartBizOrderExecute;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.RespRefund;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 订单通用服务抽象类
 *
 * @author 無
 * @version V1.0
 * @date 2018年4月24日 下午3:15:21
 */
public abstract class AbstractOrderCommonService implements IOrderService,IBizPayStartOrderCheckHandler {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_ORDER);
	
	@Autowired
	protected IOrderMapper orderMapper;
	@Autowired
	protected IOrderItemMapper orderItemMapper;
	
	@Autowired
	protected IOrderMemberMapper orderMemberMapper;
	
//	@Autowired
//	protected IOrderExtensionMapper orderExtensionMapper;
	
	@Autowired
	protected IPayOrderMapper payOrderMapper;
	
	@Autowired
	protected IBizOrderMapper bizOrderMapper;
	
	@Autowired
	protected IRefundOrderMapper refundOrderMapper;
	
	@Autowired
	protected IOverOrderMapper overOrderMapper;
	
	@Autowired
	protected IReportFormsService reportFormsUtil;

	@Autowired
	protected IPayService payService;
	
	@Autowired
	protected IPayMerchantService payMerchantService;
	
	@Autowired
	protected IBasicService basicService;

	@Autowired
	protected IOrderHisInfoMapper orderHisInfoMapper;
	
	@Autowired
	protected IOrderSubMapper orderSubMapper;
	
	/**
	 * mini付统一下单订单号校验Map缓存，30分钟清空
	 * <orderId,configKey>
	 */
	private ExpiryMap<String, String> miniPayUniteCheckOrderMap = new ExpiryMap<String, String>(16,30*60*1000);
	
	@Override
	public void saveOrderHisInfo(OrderHisInfo vo) throws Exception{
		try {
			//为了不影响主业务流程新增逻辑 这里只是保存与his相关的调用结果方便后续追溯问题 或者通过该结果集进行业务逻辑处理
			orderHisInfoMapper.insert(vo);
		}catch (Exception e) {
			LogUtil.error(log, e);
		}
	}
	
	@Autowired
	protected IOrderOutBizMapper orderOutBizMapper;
	
	@Override
	public CommonResp<RespGetPayOrder> getPayOrder(CommonReq<ReqGetOrder> req) throws Exception {
		ReqGetOrder reqParam = req.getParam();
		String orderId = reqParam.getOrderId();
		PayOrder order = payOrderMapper.selectByPrimaryKey(orderId);
		if(null != order) {
			RespGetPayOrder resp = BeanCopyUtils.copyProperties(order, new RespGetPayOrder(), null);
			return new CommonResp<RespGetPayOrder>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, resp);
		}else {
			return new CommonResp<RespGetPayOrder>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
		}
	}
	@Override
	public CommonResp<RespMap> addOrderLocal(CommonReq<ReqAddOrderLocal> req) throws Exception {
		
		ReqAddOrderLocal reqAddOrderLocal = req.getParam();
		Order order = new Order();
		OrderMember orderMember = null;
		String orderId = null;
		if (StringUtil.isEmpty(reqAddOrderLocal.getOrderId())) {
			orderId = CommonUtil.getGUID();
		} else {
			orderId = reqAddOrderLocal.getOrderId();
		}
		if(StringUtil.isNotBlank(reqAddOrderLocal.getMemberId())) {
			String openId = reqAddOrderLocal.getOpenId();
			//没有传入用户信息，有传入memberId时，查询用户信息，并保存
			ReqQueryMemberList memberReq = new ReqQueryMemberList(reqAddOrderLocal.getMsg(), 
					reqAddOrderLocal.getMemberId(), 
					null, reqAddOrderLocal.getCardType(), openId, null, null, null, null,true);
			memberReq.setHosId(reqAddOrderLocal.getHosId());
			CommonResp<RespQueryMemberList> memberResp = basicService.queryMemberList(new CommonReq<ReqQueryMemberList>(memberReq));
			if(memberResp==null || !KstHosConstant.SUCCESSCODE.equals(memberResp.getCode()) || memberResp.getData()==null || memberResp.getData().size()<=0) {
				return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Basic.ERROR_CANNOTEXIST,"没有找到用户信息，创建订单失败");
			}
			RespQueryMemberList member = memberResp.getData().get(0);
			orderMember =  new OrderMember();
			orderMember.setMemberId(reqAddOrderLocal.getMemberId());
			orderMember.setOrderId(orderId);
			orderMember.setAddress(member.getAddress());
			orderMember.setBirthdate(member.getBirthDate());
			orderMember.setIdCardNo(member.getIdCardNo());
			orderMember.setIsChildren(member.getIsChildren());
			orderMember.setMemberName(member.getMemberName());
			orderMember.setMobile(member.getMobile());
			orderMember.setOpenId(member.getOpId());
			orderMember.setSex(member.getSex());
			orderMember.setOperatorId(reqAddOrderLocal.getOperatorId());
			orderMember.setOperatorName(reqAddOrderLocal.getOperatorName());
			orderMember.setHisMemberId(member.getHisMemberId());
			order.setMemberId(member.getMemberId());
			order.setOpenId(member.getOpId());
			//卡号信息
			order.setCardNo(member.getCardNo());
			order.setCardType(member.getCardType());
		}else {
			//没有传memberId 直接保存用户信息
			orderMember =  new OrderMember();
			orderMember.setOrderId(orderId);
			orderMember.setMemberId(reqAddOrderLocal.getMemberId());
			orderMember.setAddress(reqAddOrderLocal.getAddress());
			orderMember.setBirthdate(reqAddOrderLocal.getBirthdate());
			orderMember.setIdCardNo(reqAddOrderLocal.getIdCardNo());
			orderMember.setIsChildren(reqAddOrderLocal.getIsChildren());
			orderMember.setMemberName(reqAddOrderLocal.getMemberName());
			orderMember.setMobile(reqAddOrderLocal.getMobile());
			orderMember.setOpenId(reqAddOrderLocal.getOpenId());
			orderMember.setSex(reqAddOrderLocal.getSex());
			orderMember.setOperatorId(reqAddOrderLocal.getOperatorId());
			orderMember.setOperatorName(reqAddOrderLocal.getOperatorName());
			orderMember.setHisMemberId(reqAddOrderLocal.getHisMemberId());
			order.setMemberId(reqAddOrderLocal.getMemberId());
			order.setOpenId(reqAddOrderLocal.getOpenId());
		}
		
		order.setOrderId(orderId);
		order.setPrice(reqAddOrderLocal.getPayMoney());
		order.setTotalPrice(reqAddOrderLocal.getTotalMoney());
		order.setPriceName(reqAddOrderLocal.getPriceName());
		order.setIsOnLinePay(reqAddOrderLocal.getIsOnlinePay());
		order.setBeginDate(DateOper.getNowDateTime());
		order.setPrescNo(reqAddOrderLocal.getPrescNo());
		order.setOperatorId(reqAddOrderLocal.getOperatorId());
		order.setOperatorName(reqAddOrderLocal.getOperatorName());
		if(StringUtil.isNotBlank(reqAddOrderLocal.getCardNo())){
			order.setCardNo(reqAddOrderLocal.getCardNo());
		}
		if(StringUtil.isNotBlank(reqAddOrderLocal.getCardType())){
			order.setCardType(reqAddOrderLocal.getCardType());
		}
		order.setServiceId(reqAddOrderLocal.getServiceId());
		String clientId = reqAddOrderLocal.getAuthInfo().getClientId();
		/*
		 * 如果有第三方的渠道ID创建的订单 则保存第三方的渠道id  如MiniPay
		 * 如果不传则默认是当前操作人所在的渠道
		 */
		if(StringUtil.isNotBlank(reqAddOrderLocal.getChannelId())) {
			clientId = reqAddOrderLocal.getChannelId();
		}
		order.setChannelId(clientId);
		order.setChannelName(KasiteConfig.getClientConfig(ClientConfigEnum.clientName, clientId));
		order.setEqptType(reqAddOrderLocal.getEqptType());
		order.setHosId(reqAddOrderLocal.getHosId());
		//处方单号
		//需要在新增全流程订单前，做一次订单校验的请实现该方法
		//新增本地订单前调用这个接口可以进行订单校验，也可以进行订单预下单。
		IHisOrderValidator hisOrderValidator = HandlerBuilder.get().getCallHisService(req.getParam().getAuthInfo(),IHisOrderValidator.class);
		if( hisOrderValidator!=null ) {
			reqAddOrderLocal.setMemberName(orderMember.getMemberName());
			reqAddOrderLocal.setHisMemberId(orderMember.getHisMemberId());
			reqAddOrderLocal.setCardNo(order.getCardNo());
			reqAddOrderLocal.setCardType(order.getCardType());
			reqAddOrderLocal.setIdCardNo(orderMember.getIdCardNo());
			req.setParam(reqAddOrderLocal);
			hisOrderValidator.addLocalOrderValidateBefore(req).getDataCaseRetCode();
		}
		if( StringUtil.isEmpty(reqAddOrderLocal.getEndDate())) {
			order.setEndDate(com.kasite.core.common.util.DateOper.getNowDateTimeAndAddMinute(10));
		}else {
			order.setEndDate(DateOper.parse2Timestamp(reqAddOrderLocal.getEndDate()));
		}
		String memo = reqAddOrderLocal.getOrderMemo();
		if(StringUtil.isNotBlank(memo) && memo.length() > 400) {
			throw new RRException(RetCode.Common.ERROR_PARAM,"扩展字段长度不能超过400 请核对字段长度");
		}
		order.setOrderMemo(memo);
		order.setOrderNum(CommonUtil.getOrderNum());
		orderMapper.insertSelective(order);
		if(orderMember!=null) {
			orderMemberMapper.insertSelective(orderMember);
		}
		// 判断订单是否有要存扩展信息如果有扩展信息则把扩展信息存到扩展信息表里:现在扩展信息表里有存
//		orderExtensionMapper.insertSelective(toOrderExtension(vo,orderId));
		RespMap respMap = new RespMap();
		respMap.put(ApiKey.AddOrderLocal.OrderId, orderId);
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
	}
	
	@Override
	public CommonResp<RespGetOrder> getOrder(CommonReq<ReqGetOrder> req) throws Exception {
		String orderId = req.getParam().getOrderId();
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if(null != order) {
			RespGetOrder resp = BeanCopyUtils.copyProperties(order, new RespGetOrder(), null);
			return new CommonResp<RespGetOrder>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000, resp);
		}else {
			return new CommonResp<RespGetOrder>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
		}
	}
	@Override
	public CommonResp<RespOrderLocalList> orderListLocal(CommonReq<ReqOrderListLocal> req)throws Exception {
		ReqOrderListLocal reqOrderListLocal = req.getParam();
		//拼接查询对象入参
		QueryOrderViewParam queryOrderViewParam = new QueryOrderViewParam();
		queryOrderViewParam.setBeginDate(reqOrderListLocal.getBeginDate());
		queryOrderViewParam.setBizState(reqOrderListLocal.getBizState());
		queryOrderViewParam.setChannelId(reqOrderListLocal.getChannelId());
		queryOrderViewParam.setEndDate(reqOrderListLocal.getEndDate());
		queryOrderViewParam.setOpenId(reqOrderListLocal.getOpenId());
		queryOrderViewParam.setOrderId(reqOrderListLocal.getOrderId());
		queryOrderViewParam.setOverState(reqOrderListLocal.getOverState());
		queryOrderViewParam.setPayState(reqOrderListLocal.getPayState());
		queryOrderViewParam.setPrescNo(reqOrderListLocal.getPrescNo());
		queryOrderViewParam.setServiceId(reqOrderListLocal.getServiceId());
		queryOrderViewParam.setMemberId(reqOrderListLocal.getMemberId());
		//多个业务类型查询
		if(null != queryOrderViewParam.getServiceId() && queryOrderViewParam.getServiceId().contains(",")) {
			String[] serviceIds = queryOrderViewParam.getServiceId().split(",");
			List<String> ids = Arrays.asList(serviceIds);
			queryOrderViewParam.setServiceIds(ids);
			queryOrderViewParam.setServiceId(null);
		}
		//多个就诊卡号查询
		if( reqOrderListLocal.getCardNo()!=null && reqOrderListLocal.getCardNo().contains(",")) {
			String[] cardNos = reqOrderListLocal.getCardNo().split(",");
			String[] cardTypes = reqOrderListLocal.getCardType().split(",");
			List<Member> memberList = new ArrayList<Member>();
			for(int i = 0;i<cardNos.length;i++) {
				Member me = new Member();
				me.setCardNo(cardNos[i]);
				me.setCardType(cardTypes[i]);
				memberList.add(me);
			}
			queryOrderViewParam.setMemberList(memberList);
		} else {
			if(StringUtil.isNotBlank(reqOrderListLocal.getCardNo())) {
				queryOrderViewParam.setCardNo(reqOrderListLocal.getCardNo());
			}
			if(reqOrderListLocal.getCardType()!=null) {
				queryOrderViewParam.setCardType(new Integer(reqOrderListLocal.getCardType()));
			}
		}
		
		if( reqOrderListLocal.getMemberId()!=null) {
			String[] memberIds = reqOrderListLocal.getMemberId().split(",");
			List<Member> memberList = new ArrayList<Member>();
			for(int i = 0;i<memberIds.length;i++) {
				Member me = new Member();
				me.setMemberId(memberIds[i]);
				memberList.add(me);
			}
			queryOrderViewParam.setMemberList(memberList);
		}
		
		PageVo pageVo = null;
		List<OrderView> orderList = null;
		if(reqOrderListLocal.getPage()!=null) {
			pageVo = new PageVo();
			pageVo.setPIndex(reqOrderListLocal.getPage().getPIndex());
			pageVo.setPSize(reqOrderListLocal.getPage().getPSize());
			//设置分页
			PageHelper.startPage(reqOrderListLocal.getPage().getPIndex()+1, reqOrderListLocal.getPage().getPSize());
			//分页查询
			orderList = orderMapper.queryOrderView(queryOrderViewParam);
			PageInfo<OrderView> page = new PageInfo<OrderView>(orderList);
			//获取总记录数
			pageVo.setPCount(Integer.parseInt(page.getTotal()+""));
		}else {
			pageVo = new PageVo();
			pageVo.setPIndex(0);
			pageVo.setPSize(50);
			//设置分页
			PageHelper.startPage(pageVo.getPIndex()+1, pageVo.getPSize());
			orderList = orderMapper.queryOrderView(queryOrderViewParam);
			PageInfo<OrderView> page = new PageInfo<OrderView>(orderList);
			//获取总记录数
			pageVo.setPCount(Integer.parseInt(page.getTotal()+""));
		}
		List<RespOrderLocalList> list = new ArrayList<RespOrderLocalList>();
		if( !CollectionUtils.isEmpty(orderList)) {
			for( OrderView orderView : orderList) {
				RespOrderLocalList respOrderLocalList = new RespOrderLocalList();
				respOrderLocalList.setBeginDate(DateOper.formatDate(orderView.getBeginDate(),"yyyy-MM-dd HH:mm:ss"));
				respOrderLocalList.setBizState(orderView.getBizState());
				respOrderLocalList.setCardNo(orderView.getCardNo());
				respOrderLocalList.setCardType(orderView.getCardType());
				respOrderLocalList.setChannelId(orderView.getChannelId());
				respOrderLocalList.setIsOnlinePay(orderView.getIsOnlinePay());
				respOrderLocalList.setOperatorId(orderView.getOperatorId());
				respOrderLocalList.setOperatorName(orderView.getOperatorName());
				respOrderLocalList.setOrderNum(orderView.getOrderNum());
				respOrderLocalList.setOrderId(orderView.getOrderId());
				respOrderLocalList.setOrderMemo(orderView.getOrderMemo());
				respOrderLocalList.setOverState(orderView.getOverState());
				respOrderLocalList.setPayMoney(orderView.getPrice());
				respOrderLocalList.setPayState(orderView.getPayState());
				respOrderLocalList.setPrescNo(orderView.getPrescNo());
				respOrderLocalList.setPriceName(orderView.getPriceName());
				respOrderLocalList.setServiceId(orderView.getServiceId());
				respOrderLocalList.setTotalMoney(orderView.getTotalPrice());
				respOrderLocalList.setMemberName(orderView.getMemberName());
				respOrderLocalList.setHosId(orderView.getHosid());
				respOrderLocalList.setOpenId(orderView.getOpenId());
				respOrderLocalList.setMemberId(orderView.getMemberId());
				respOrderLocalList.setTransactionNo(orderView.getTransactionNo());
				list.add(respOrderLocalList);
			}
		}
		return new CommonResp<RespOrderLocalList>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,list,pageVo);
	}
	
	@Override
	public CommonResp<RespOrderDetailLocal> orderDetailLocal(CommonReq<ReqOrderDetailLocal> req) throws Exception{
		ReqOrderDetailLocal reqOrderDetailLocal = req.getParam();
		//这个代码好像无用
//		Order queryOrderParam = new Order();
//		queryOrderParam.setOrderId(reqOrderDetailLocal.getOrderId());
//		queryOrderParam.setPrescNo(reqOrderDetailLocal.getPrescNo());
		//拼接查询对象入参
		QueryOrderViewParam queryOrderViewParam = new QueryOrderViewParam();
		queryOrderViewParam.setOrderId(reqOrderDetailLocal.getOrderId());
		queryOrderViewParam.setPrescNo(reqOrderDetailLocal.getPrescNo());
		queryOrderViewParam.setOverState(reqOrderDetailLocal.getOverState());
		queryOrderViewParam.setTransactionNo(reqOrderDetailLocal.getTransactionId());
		PageHelper.startPage(0, 100);
		List<OrderView> orderList = orderMapper.queryOrderView(queryOrderViewParam);
		
		if( CollectionUtils.isEmpty(orderList)) {
			return new CommonResp<RespOrderDetailLocal>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CANNOTEXIST);
		}else if(orderList.size()>1){
			return new CommonResp<RespOrderDetailLocal>(req,KstHosConstant.DEFAULTTRAN,RetCode.Common.ERROR_PARAM,"入参有误，存在"+orderList.size()+"条订单！");
		}
		OrderView order =orderList.get(0);
		RespOrderDetailLocal respOrderDetailLocal = new RespOrderDetailLocal();
		//TODO 测试用	
		if(req.getParam().getAuthInfo().getClientId().equals("minipay")) {
			order.setBizState(1);
			order.setOverState(0);
		};
		respOrderDetailLocal.setOrderNum(order.getOrderNum());
		respOrderDetailLocal.setBizState(order.getBizState());
		respOrderDetailLocal.setCardNo(order.getCardNo());
		respOrderDetailLocal.setHosId(order.getHosid());
		respOrderDetailLocal.setCardType(order.getCardType());
		respOrderDetailLocal.setEqptType(order.getEqptType());
		respOrderDetailLocal.setIdCardNo(order.getIdCardNo());
		respOrderDetailLocal.setIsOnlinePay(order.getIsOnlinePay());
		respOrderDetailLocal.setOperatorId(order.getOperatorId());
		respOrderDetailLocal.setOperatorName(order.getOperatorName());
		respOrderDetailLocal.setOrderId(order.getOrderId());
		respOrderDetailLocal.setOrderMemo(order.getOrderMemo());
		respOrderDetailLocal.setOverState(order.getOverState());
		respOrderDetailLocal.setPayChannelId(order.getPayChannelid());
		respOrderDetailLocal.setPayMoney(order.getPrice());
		respOrderDetailLocal.setPayState(order.getPayState());
		respOrderDetailLocal.setPrescNo(order.getPrescNo());
		respOrderDetailLocal.setPriceName(order.getPriceName());
		respOrderDetailLocal.setServiceId(order.getServiceId());
		respOrderDetailLocal.setTotalMoney(order.getTotalPrice());
		respOrderDetailLocal.setTransactionNo(order.getTransactionNo());
		respOrderDetailLocal.setMemberName(order.getMemberName());
		respOrderDetailLocal.setAddress(order.getAddress());
		respOrderDetailLocal.setSex(order.getSex());
		respOrderDetailLocal.setMobile(order.getMobile());
		respOrderDetailLocal.setChannelId(order.getChannelId());
		respOrderDetailLocal.setMemberId(order.getMemberId());
		respOrderDetailLocal.setOrderMemo(order.getOrderMemo());//订单扩展信息保存到 OrderMemo 中 JSON保存
		respOrderDetailLocal.setHisMemberId(order.getHisMemberId());
		if(order.getBeginDate() !=null ) {
			respOrderDetailLocal.setBeginDate(DateOper.formatDate(order.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
		}
//		OrderExtension orderExtension = orderExtensionMapper.selectByPrimaryKey(order.getOrderId());
//		if( orderExtension!=null ) {
//			RespOrderExtension respOrderExtension= new RespOrderExtension();
//			respOrderExtension.setDeptCode(orderExtension.getDeptCode());
//			respOrderExtension.setDeptName(orderExtension.getDeptName());
//			respOrderExtension.setDoctorCode(orderExtension.getDoctorCode());
//			respOrderExtension.setDoctorName(orderExtension.getDoctorName());
//			respOrderExtension.setExtendField1(orderExtension.getExtendField1());
//			respOrderExtension.setExtendField2(orderExtension.getExtendField2());
//			respOrderExtension.setExtendField3(orderExtension.getExtendField3());
//			respOrderDetailLocal.setData_1(respOrderExtension);
//		}
		
		return new CommonResp<RespOrderDetailLocal>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respOrderDetailLocal);
	}

	@Override
	public CommonResp<RespMap> orderIsPayment(CommonReq<ReqOrderIsPayment> req)throws Exception{
		ReqOrderIsPayment reqOrderIsPayment = req.getParam();
		String orderId = reqOrderIsPayment.getOrderId();
		String operator = reqOrderIsPayment.getOperatorId();
		String operatorName = reqOrderIsPayment.getOperatorName();
		String channelId = reqOrderIsPayment.getChannelId();
		String remark = reqOrderIsPayment.getRemark();
		OrderView orderView = null;
		orderView = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
		if (orderView == null) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERNOTFIND,"未查询到订单信息:orderId="+orderId);
		}
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getOverState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERSTATE,"订单：" + orderId + "已取消或者已撤销无法支付！");
		}
		Integer price = orderView.getPrice();
		PayOrder payOrder = new PayOrder();
		payOrder.setOperatorId(operator);
		payOrder.setOperatorName(operatorName);
		payOrder.setBeginDate(DateOper.getNowDateTime());
		payOrder.setChannelId(channelId);
		payOrder.setRemark(remark);
		payOrder.setPrice(price);
		payOrder.setConfigKey(reqOrderIsPayment.getConfigKey());
		payOrder.setOrderId(orderId);
		payOrder.setEndDate(DateOper.getNowDateTime());
		payOrder.setPayState(KstHosConstant.ORDERPAY_1);
		//如果是订单待支付状态 新增支付中订单  状态=1 支付中
		//防止并发,再查询一遍
//		PayOrder exsitPayOrder = payOrderMapper.selectByPrimaryKey(orderId);
//		if (KstHosConstant.ORDERPAY_0.equals(orderView.getPayState()) || exsitPayOrder ==null) {
//			/** 新增订单到待支付表 */
//			payOrder.setPayState(KstHosConstant.ORDERPAY_1);
//			payOrderMapper.insertSelective(payOrder);
//		} else {
//			//如果是微信那边支付回调比前端页面支付快 则更新其它业务数据
//			/** 更新待支付表数据 根据订单号更新业务数据 */
//			payOrderMapper.updateByPrimaryKeySelective(payOrder);
//		}
		payOrderMapper.insertByOrderIsPayment(payOrder);
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}

	@Override
	public CommonResp<RespMap> payForCompletion(CommonReq<ReqPayForCompletion> req)throws Exception{
		ReqPayForCompletion reqPayForCompletion =req.getParam();
		String orderId = reqPayForCompletion.getOrderId();
		String transActionNo = reqPayForCompletion.getTransActionNo();
		OrderView orderView = null;
		orderView = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
		if (orderView == null) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERNOTFIND,"未查询到订单信息:orderId="+orderId);
		}
		
		//如果已经回调过了，则直接返回成功
		if (KstHosConstant.ORDERPAY_2.equals(orderView.getPayState())) {
			return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
		}

		// 订单状态不是正在支付或待支付的时候不允许更新成付费完成
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERPAY_1.equals(orderView.getPayState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERSTATE,"订单：" + orderId + "状态不是正在付费或待支付订单");
		}
		PayOrder payOrder = new PayOrder();
		payOrder.setTransactionNo(transActionNo);
		payOrder.setPayState(KstHosConstant.ORDERPAY_2);
		payOrder.setOrderId(orderId);
		payOrder.setConfigKey(reqPayForCompletion.getConfigKey());
		payOrder.setEndDate(DateOper.getNowDateTime());
		//防止并发,再查询一遍，查询-插入/更新之间代码越少越好，减少执行时间，可以有效防止
//		PayOrder exsitPayOrder = payOrderMapper.selectByPrimaryKey(orderId);
//		/** 订单为待支付时需要写到支付表中 */
//		if (exsitPayOrder ==null) {
			payOrder.setOperatorId(req.getParam().getOpenId());
			payOrder.setOperatorName(req.getParam().getOperatorName());
			payOrder.setBeginDate(DateOper.getNowDateTime());
			payOrder.setChannelId(reqPayForCompletion.getClientId());
			payOrder.setRemark("微信回调的订单支付完成。");
			payOrder.setPrice(reqPayForCompletion.getPrice());
			payOrder.setAccNo(reqPayForCompletion.getAccNo());
//			payOrderMapper.insertSelective(payOrder);
//		} else {
//			/** 更新订单状态为支付完成 */
//			payOrderMapper.updateByPrimaryKeySelective(payOrder);
//		}
		payOrderMapper.insertByPayForCompletion(payOrder);
		// 支付完成后调用数据收集接口 付费 及 成交量,后期优化
		reportFormsUtil.dataCollection(req.getMsg(),orderView.getChannelId(), "", "order.orderApi.PayForCompletion", 5, 1, null);
		reportFormsUtil.dataCollection(req.getMsg(),orderView.getChannelId(), "", "order.orderApi.PayForCompletion", 6,
				orderView.getPrice(), null);
		if (KstHosConstant.ORDERTYPE_006.equals(orderView.getServiceId())) {
			// 门诊
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 103, 1, "1");
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 104, orderView.getPrice(), "1");
		} else if (KstHosConstant.ORDERTYPE_007.equals(orderView.getServiceId())) {
			// 住院
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 111, 1, "1");
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 112, orderView.getPrice(), "1");
		} else if (KstHosConstant.ORDERTYPE_0.equals(orderView.getServiceId())) {
			/** 线上支付 */
			if (KstHosConstant.ONLINEPAY.equals(orderView.getIsOnlinePay())) {
				reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 106, orderView.getPrice(), "1");
			}
		} else if (KstHosConstant.ORDERTYPE_008.equals(orderView.getServiceId())) {
			// 诊间支付
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 108, orderView.getPrice(), "1");
		}
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}
	
	@Override
	public CommonResp<RespMap> bizForCompletion(CommonReq<ReqBizForCompletion> req)throws Exception{
		ReqBizForCompletion reqBizForCompletion = req.getParam();
		BizOrder bizOrder = bizOrderMapper.selectByPrimaryKey(reqBizForCompletion.getOrderId());
		if( bizOrder!=null ) {
			if(KstHosConstant.ORDERBIZSTATE_1.equals(bizOrder.getBizState())) {
				//如果业务成功已经回到过，则直接返回成功
				return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, RetCode.Success.RET_10000.getMessage());
			}else {
				//否则返回错误
				return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,"该订单业务状态已取消！无法执行完成操作！");
			}
		}
		// 新增订单ORDEROVER
		BizOrder vo = new BizOrder();
		vo.setOrderId(reqBizForCompletion.getOrderId());
		vo.setOperatorId(reqBizForCompletion.getOperatorId());
		vo.setOperatorName(reqBizForCompletion.getOperatorName());
		vo.setBeginDate(DateOper.getNowDateTime());
		vo.setBizState(KstHosConstant.ORDERBIZSTATE_1);
		vo.setEndDate(DateOper.parse2Timestamp("9999-12-31 23:59:59"));
		vo.setOutBizOrderId(reqBizForCompletion.getOutBizOrderId());
		bizOrderMapper.insertSelective(vo);
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, RetCode.Success.RET_10000.getMessage());
	}
	
	
	@Override
	public CommonResp<RespMap> syncLocalOrderState(CommonReq<ReqSyncLocalOrderState> req) throws Exception {
		ReqSyncLocalOrderState reqParam = req.getParam();
		String orderId = reqParam.getOrderId();
		int bizState = 0;
		//查询本地订单。 
		if(null != reqParam.getOrderLocal()) {
			bizState = reqParam.getOrderLocal().getBizState();
		}else {
			OrderView orderView = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
			bizState = orderView.getBizState();
		}
		int regFlag = reqParam.getHisBizState();
		BusinessTypeEnum busType = reqParam.getBusType();
		switch (busType) {
		case ORDERTYPE_0:{
			//如果是预约挂号订单  HISState = RegFlag
			HisRegFlag hisFlag = HisRegFlag.valuesOf(regFlag);
			//如果HIS订单状态 = 正常  而本地订单状态为 已经取消 则发出告警信息
			if(null != hisFlag) {
				if(hisFlag.equals(HisRegFlag.state_1) && bizState == 2) {
					//TODO 发送告警消息
					
				//HIS订单状态为已退号  本地订单为正常  更新本地bizState = 2 已退号
				}else if(hisFlag.equals(HisRegFlag.state_2) && bizState == KstHosConstant.ORDERBIZSTATE_1) {
					LogUtil.info(log, "因本地订单状态与HIS订单状态不一致 更新本地BizState状态为 2 订单号："+ orderId);
					BizOrder record = new BizOrder();
					record.setBizState(KstHosConstant.ORDERBIZSTATE_2);
					record.setOrderId(orderId);
					bizOrderMapper.updateByPrimaryKeySelective(record);
					//预约记录已经失效，订单还未支付 将订单更新成取消
				}else if(hisFlag.equals(HisRegFlag.state_2) && bizState == KstHosConstant.ORDERBIZSTATE_0) {
					OverOrder o = overOrderMapper.selectByPrimaryKey(orderId);
					if(null == o) {
						LogUtil.info(log, "因本地订单状态与HIS订单状态不一致 更新本地OverState状态为 5 订单号："+ orderId);
						OverOrder record = new OverOrder();
						record.setOverState(KstHosConstant.ORDEROVER_5);
						record.setOrderId(orderId);
						record.setOperatorId(reqParam.getOpenId());
						record.setOperatorName(reqParam.getOperatorName());
						record.setEndDate(DateOper.getNowDate());
						overOrderMapper.insert(record);
					}else {
						LogUtil.info(log, "改订单已经是最终完结状态："+ orderId +" overState = "+ o.getOverState());
					}
				}
			}
			break;
		}
		default:
			break;
		}
		
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000);
	}
	
	
	@Override
	public CommonResp<RespMap> bizForCancel(CommonReq<ReqBizForCancel> req) throws Exception {
		ReqBizForCancel reqBizForCancel = req.getParam();
		OrderView orderView = orderMapper.getOrderViewByOrderId(reqBizForCancel.getOrderId(),KasiteConfig.getHistoryOrderListDays());		
		if (orderView == null) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERNOTFIND,"未查询到订单信息:orderId="+reqBizForCancel.getOrderId());
		}
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERPAY_3.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERPAY_4.equals(orderView.getPayState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_ORDERSTATE,"订单状态异常,不能取消业务状态。");
		}
		BizOrder bizOrder = new BizOrder();
		bizOrder.setOrderId(reqBizForCancel.getOrderId());
		bizOrder.setBizState(KstHosConstant.ORDERBIZSTATE_2);
		Example example = new Example(BizOrder.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("orderId", reqBizForCancel.getOrderId());
		criteria.andEqualTo("bizState", KstHosConstant.ORDERBIZSTATE_1);
		int result = bizOrderMapper.updateByExampleSelective(bizOrder, example);
		if (result == 0) {
			//TODO 要怎么做处理
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_UPDATESTATE,"修改业务状态为已取消失败");
		}
		return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, RetCode.Success.RET_10000.getMessage());
	}
	
	@Override
	public CommonResp<RespMap> orderIsCancel(CommonReq<ReqOrderIsCancel> req) throws Exception {
		ReqOrderIsCancel reqOrderIsCancel =  req.getParam();
		String orderId = reqOrderIsCancel.getOrderId();
		String operatorId = reqOrderIsCancel.getOperatorId();
		String operatorName = reqOrderIsCancel.getOperatorName();
		String channelId = reqOrderIsCancel.getChannelId();
		String remark = reqOrderIsCancel.getReason();
		Integer price = reqOrderIsCancel.getPrice();
		Integer refundPrice = reqOrderIsCancel.getRefundPrice();
		
		if( StringUtil.isEmpty(orderId)) {
			PayOrder payOrder = new PayOrder();
			payOrder.setTransactionNo(reqOrderIsCancel.getTransactionNo());
			payOrder = payOrderMapper.selectOne(payOrder);
			if( payOrder== null ) {
				return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
						"订单未找到TransactionNo：" + reqOrderIsCancel.getTransactionNo());
			}else {
				orderId =payOrder.getOrderId();
			}
		}
		if( StringUtil.isEmpty(channelId)) {
			channelId = reqOrderIsCancel.getClientId();
		}
		OrderView orderView = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
		if (null == orderView) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单未找到orderId：" + orderId);
		}
		if( price == null || price.intValue()<=0) {
			price = orderView.getPrice();
		}
		RespMap respMap = new RespMap();
		// 退款全流程ID
		String refundOrderId = CommonUtil.getUUID();
		// 退费金额不能大于支付总金额
		if (refundPrice.intValue() > price.intValue()) {
			return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_REFUNDPRICEERROR);
		}
		//超过90天的订单不允许退款
		if (DateUtils.getAfterDaysDate(orderView.getBeginDate(), 90).before(new Date())) {
			//订单开始日期+90，如果小于当前日期，即订单超过90天
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"超过90天的订单，禁止退款！" + orderId);
		}
		if (!KstHosConstant.ORDEROVER_0.equals(orderView.getOverState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"订单：" + orderId + "已取消或者已撤销无法再退费！");
		}
		// 订单为支付完成时验证订单表中的金额和所传的金额是否匹配
		if (!orderView.getPrice().equals(price)) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERPRICE,
					RetCode.Order.ERROR_ORDERPRICE.getMessage());
		}
		//已支付，已退费（未退完）的订单才允许继续退费。
//		if (!KstHosConstant.ORDERPAY_2.equals(orderView.getPayState())
//				&& !KstHosConstant.ORDERPAY_4.equals(orderView.getPayState())) {
//			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
//					"订单：" + orderId + "状态不是已支付，或者已退费状态，无法再退费！");
//		}
		//已支付的订单才能退费。（正在退费，已经退成功过一次，都不允许退费），此判断作用：只允许一次退费。
		//如果需要允许多次退费，请注释该判断。启用上面的判断。
		if (!KstHosConstant.ORDERPAY_2.equals(orderView.getPayState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"订单：" + orderId + "状态不是已支付，无法再退费！");
		}
		
		Integer totalRefundPrice = orderMapper.getTotalRrefundPrice(orderId);
		if(totalRefundPrice ==null) {
			totalRefundPrice = 0;
		}
		// 如果余额小于退费金额
		if ((orderView.getPrice().intValue() - totalRefundPrice.intValue()) < refundPrice.intValue()) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN,
					RetCode.Order.ERROR_REFUNDPRICEERROR, "可退余额小于退费金额，无法退费！");
		}
		//判断是否有同样的，失败的退费订单,如果有，则重复利用
		RefundOrder queryRefund = new RefundOrder();
		queryRefund.setOrderId(orderId);
		queryRefund.setRefundPrice(refundPrice);
		queryRefund.setTotalPrice(price);
		queryRefund.setChannelId(channelId);
		queryRefund.setPayState(KstHosConstant.ORDERPAY_7);
		queryRefund.setOutRefundOrderId(reqOrderIsCancel.getOutRefundOrderId());
		List<RefundOrder> refundLit = refundOrderMapper.select(queryRefund);
		RefundOrder refundOrder = null;
		if( CollectionUtils.isEmpty(refundLit)) {
			// 新增退费订单表
			refundOrder = new RefundOrder();
			refundOrder.setOrderId(orderId);
			refundOrder.setRefundOrderId(refundOrderId);
			refundOrder.setChannelId(channelId);
			refundOrder.setBeginDate(DateOper.getNowDateTime());
			refundOrder.setPayState(KstHosConstant.ORDERPAY_3);
			refundOrder.setOperatorid(operatorId);
			refundOrder.setOperatorName(operatorName);
			refundOrder.setRemark(remark);
			refundOrder.setRetryNum(0);
			refundOrder.setRefundPrice(refundPrice);
			refundOrder.setTotalPrice(price);
			refundOrder.setOutRefundOrderId(reqOrderIsCancel.getOutRefundOrderId());
			refundOrderMapper.insertSelective(refundOrder);
		}else {
			refundOrder = refundLit.get(0);//取第一个就好,重复利用
			refundOrder.setPayState(KstHosConstant.ORDERPAY_3);
			refundOrder.setEndDate(DateOper.getNowDateTime());
			refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
		}

		// 调用HOS-PAY退费接口 -pay.PayWs.Refund  
		String refundNo = null;
		ReqRefund reqRefund = new ReqRefund(req.getParam().getMsg(),orderId, refundOrder.getRefundOrderId(), 
				price,refundOrder.getRefundPrice(), remark, 0,orderView.getConfigKey(),DateOper.formatDate(orderView.getBeginDate(), "yyyyMMddHHmmss"));
		//reqRefund.setChannelId(channelId);
		CommonResp<RespRefund> refundResp = payService.refund(new CommonReq<ReqRefund>(reqRefund));
		if(!KstHosConstant.SUCCESSCODE.equals(refundResp.getCode())) {
			refundOrder.setPayState(KstHosConstant.ORDERPAY_7);
			refundOrder.setEndDate(DateOper.getNowDateTime());
			refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
			respMap.put(ApiKey.OrderIsCancel.RefundOrderId,refundOrderId);
			return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN, RetCode.Pay.ERROR_REFUND,refundResp.getMessage(),respMap);
		}else if(refundResp!=null) {
			RespRefund refund = refundResp.getData().get(0);
			refundNo = refund.getRefundNo();
			refundOrder.setPayState(KstHosConstant.ORDERPAY_4);
			refundOrder.setEndDate(DateOper.getNowDateTime());
			refundOrder.setRefundNo(refundNo);
			refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
		}
		reportFormsUtil.dataCollection(req.getMsg(),orderView.getChannelId(), "", "order.orderApi.OrderIsCancel", 5, -1, null);
		reportFormsUtil.dataCollection(req.getMsg(),orderView.getChannelId(), "", "order.orderApi.OrderIsCancel", 6, -refundPrice,
				null);
		if (KstHosConstant.ORDERTYPE_006.equals(orderView.getServiceId())) {
			// 门诊
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 109, 1, "1");
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 110, -refundPrice, "1");
		} else if (KstHosConstant.ORDERTYPE_007.equals(orderView.getServiceId())) {
			// 住院
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 113, 1, "1");
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 114, -refundPrice, "1");
		} else if (KstHosConstant.ORDERTYPE_0.equals(orderView.getServiceId())) {
			/** 线上支付 */
			if (KstHosConstant.ONLINEPAY.equals(orderView.getIsOnlinePay())) {
				reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 106, -refundPrice, "1");
			}
		} else if (KstHosConstant.ORDERTYPE_008.equals(orderView.getServiceId())) {
			// 门诊退费金额
			reportFormsUtil.dataCloudCollection(req.getMsg(),orderView.getChannelId(), 114, refundPrice, "1");
		}
		respMap.put(ApiKey.OrderIsCancel.RefundNo, refundNo);
		respMap.put(ApiKey.OrderIsCancel.RefundOrderId, refundOrderId);
		respMap.put(ApiKey.OrderIsCancel.TransactionNo, orderView.getTransactionNo());
		respMap.put(ApiKey.OrderIsCancel.OrderId, orderView.getOrderId());
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,respMap);
	}
	
	@Override
	public CommonResp<RespMap> cancelForCompletion(CommonReq<ReqCancelForCompletion> req) throws Exception {
		ReqCancelForCompletion reqCancelForCompletion = req.getParam();
		RefundOrder existRefund = refundOrderMapper.selectByPrimaryKey(reqCancelForCompletion.getRefundOrderId());
		if( existRefund !=null ) {
			RefundOrder refundOrder = new RefundOrder();
			refundOrder.setPayState(KstHosConstant.ORDERPAY_4);
			refundOrder.setRefundNo(reqCancelForCompletion.getRefundNo());
			refundOrder.setEndDate(DateOper.getNowDateTime());
			refundOrder.setRefundOrderId(reqCancelForCompletion.getRefundOrderId());
			refundOrder.setOperatorid(req.getParam().getOpenId());
			refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
		}else {
			RefundOrder refundOrder = new RefundOrder();
			refundOrder.setPayState(KstHosConstant.ORDERPAY_4);
			refundOrder.setRefundNo(reqCancelForCompletion.getRefundNo());
			refundOrder.setEndDate(DateOper.getNowDateTime());
			refundOrder.setRefundOrderId(reqCancelForCompletion.getRefundOrderId());
			refundOrder.setOperatorid(reqCancelForCompletion.getOperatorId());
			refundOrder.setOperatorName(reqCancelForCompletion.getOperatorName());
			refundOrder.setBeginDate(DateOper.getNowDateTime());
			refundOrder.setChannelId(reqCancelForCompletion.getClientId());
			refundOrder.setOrderId(reqCancelForCompletion.getOrderId());
			refundOrder.setRefundPrice(reqCancelForCompletion.getRefundPrice());
			refundOrder.setTotalPrice(reqCancelForCompletion.getTotalPrice());
			refundOrderMapper.insertSelective(refundOrder);
		}
		
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
				KstHosConstant.SUCCESSMSG);
	}
	
	@Override
	public CommonResp<RespMap> cancelOrder(CommonReq<ReqCancelOrder> req) throws Exception{
		ReqCancelOrder reqCancelOrder = req.getParam();
		OrderView orderView = orderMapper.getOrderViewByOrderId(reqCancelOrder.getOrderId(),KasiteConfig.getHistoryOrderListDays());
		if (null == orderView) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单未找到orderId：" + reqCancelOrder.getOrderId());
		}
		// 判断订单支付状态，跟订单的业务执行状态
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERBIZSTATE_0.equals(orderView.getBizState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"只有未支付，并且未执行HIS业务的订单才可以取消订单！");
		}
		// 判断订单是否已经撤销或者已经取消
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getOverState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"订单已经撤销或者已取消！无法再执行取消操作！");
		}
		OverOrder overOrder = new OverOrder();
		overOrder.setOrderId(reqCancelOrder.getOrderId());
		overOrder.setOperatorId(reqCancelOrder.getOperatorId());
		overOrder.setOperatorName(reqCancelOrder.getOperatorName());
		overOrder.setOverState(KstHosConstant.ORDEROVER_5);
		overOrder.setBeginDate(DateOper.getNowDateTime());
		overOrder.setEndDate(DateOper.getNowDateTime());
		overOrderMapper.insertSelective(overOrder);
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
				KstHosConstant.SUCCESSMSG);
	}
	
	@Override
	public CommonResp<RespMap> revokeOrder(CommonReq<ReqRevokeOrder> req) throws Exception{
		ReqRevokeOrder reqRevokeOrder = req.getParam();
		OrderView orderView = orderMapper.getOrderViewByOrderId(reqRevokeOrder.getOrderId(),KasiteConfig.getHistoryOrderListDays());
		if (null == orderView) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单未找到orderId：" + reqRevokeOrder.getOrderId());
		}
		// 判断订单支付状态，跟订单的业务执行状态
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERPAY_1.equals(orderView.getPayState())
				&& !KstHosConstant.ORDERBIZSTATE_0.equals(orderView.getBizState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"只有未支付，支付中，并且未执行HIS业务的订单才可以撤销订单！");
		}
		// 判断订单是否已经撤销或者已经取消
		if (!KstHosConstant.ORDERPAY_0.equals(orderView.getOverState())) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_ORDERSTATE,
					"订单已经撤销或者已取消！无法再执行撤销操作！");
		}
		if (reqRevokeOrder.getIsRevokeMerchantOrder() == 1 && !StringUtil.isEmpty(orderView.getConfigKey())) {
			// 调用pay模块的撤销商户订单
			ReqRevoke reqRevoke = new ReqRevoke(req.getMsg(), reqRevokeOrder.getOrderId(),
					orderView.getConfigKey(),DateOper.formatDate(orderView.getBeginDate(), "yyyyMMddHHmmss"));
			payService.revoke(new CommonReq<ReqRevoke>(reqRevoke)).getDataCaseRetCode();
		}
		OverOrder overOrder = new OverOrder();
		overOrder.setOperatorId(reqRevokeOrder.getOperatorId());
		overOrder.setOperatorName(reqRevokeOrder.getOperatorName());
		overOrder.setOverState(KstHosConstant.ORDEROVER_6);
		overOrder.setBeginDate(DateOper.getNowDateTime());
		overOrder.setEndDate(DateOper.getNowDateTime());
		overOrder.setOrderId(reqRevokeOrder.getOrderId());
		overOrderMapper.insertSelective(overOrder);
		return new CommonResp<>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,
				KstHosConstant.SUCCESSMSG);
		
	}

	@Override
	public CommonResp<RespQueryOrderProcess> queryOrderProcess(CommonReq<ReqQueryOrderProcess> req) throws Exception{
		ReqQueryOrderProcess reqQueryOrderProcess = req.getParam();
		String orderId = reqQueryOrderProcess.getOrderId();
		// 先查询ORDER表确定订单类型及订单开始时间
		Order order = orderMapper.selectByPrimaryKey(orderId);
		if (null == order) {
			return new CommonResp<RespQueryOrderProcess>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单未找到orderId：" + orderId);
		}
		List<RespQueryOrderProcess> list = new ArrayList<RespQueryOrderProcess>();
		
		//查询订单的业务状态
		BizOrder bizOrder = bizOrderMapper.selectByPrimaryKey(orderId);
		if( bizOrder!=null) {
			RespQueryOrderProcess bizPorcess = new RespQueryOrderProcess();
			bizPorcess.setIsOnlinePay(order.getIsOnLinePay());
			bizPorcess.setOrderId(orderId);
			bizPorcess.setState(bizOrder.getBizState());
			bizPorcess.setType("BIZ");
			bizPorcess.setBeginDate(DateOper.formatDate(bizOrder.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
			bizPorcess.setEndDate(DateOper.formatDate(bizOrder.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			list.add(bizPorcess);
		}
		
		//查询订单的支付与退款状态
		PayOrder payOrder =payOrderMapper.selectByPrimaryKey(orderId);
		if( payOrder!=null) {
			RespQueryOrderProcess payPorcess = new RespQueryOrderProcess();
			payPorcess.setIsOnlinePay(order.getIsOnLinePay());
			payPorcess.setOrderId(orderId);
			payPorcess.setState(payOrder.getPayState());
			payPorcess.setType("PAY");
			payPorcess.setBeginDate(DateOper.formatDate(payOrder.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
			payPorcess.setEndDate(DateOper.formatDate(payOrder.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			list.add(payPorcess);
		}
		RefundOrder queryRefundOrder = new RefundOrder();
		queryRefundOrder.setOrderId(orderId);
		List<RefundOrder> refundList = refundOrderMapper.select(queryRefundOrder);
		if( !CollectionUtils.isEmpty(refundList)) {
			for(RefundOrder refundOrder : refundList) {
				RespQueryOrderProcess refundPorcess = new RespQueryOrderProcess();
				refundPorcess.setIsOnlinePay(order.getIsOnLinePay());
				refundPorcess.setOrderId(orderId);
				refundPorcess.setState(refundOrder.getPayState());
				refundPorcess.setRefundPrice(refundOrder.getRefundPrice());
				refundPorcess.setType("REFUND");
				if(null != refundOrder.getBeginDate()) {
					refundPorcess.setBeginDate(DateOper.formatDate(refundOrder.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
				}
				if(null != refundOrder.getEndDate()) {
					refundPorcess.setEndDate(DateOper.formatDate(refundOrder.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
				}
				list.add(refundPorcess);
			}
		}
		
		//查询订单的终结状态
		OverOrder overOrder = overOrderMapper.selectByPrimaryKey(orderId);
		if( overOrder!=null) {
			RespQueryOrderProcess overPorcess = new RespQueryOrderProcess();
			overPorcess.setIsOnlinePay(order.getIsOnLinePay());
			overPorcess.setOrderId(orderId);
			overPorcess.setState(overOrder.getOverState());
			overPorcess.setType("OVER");
			overPorcess.setBeginDate(DateOper.formatDate(overOrder.getBeginDate(), "yyyy-MM-dd HH:mm:ss"));
			if(null != overOrder.getEndDate()) {
				overPorcess.setEndDate(DateOper.formatDate(overOrder.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			}
			list.add(overPorcess);
		}
		
		return new CommonResp<RespQueryOrderProcess>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,list);
	}
	 
	@Override
	public CommonResp<RespMap> checkPayOrder(CommonReq<ReqPayStartBizOrderExecute> req) {
		ReqPayStartBizOrderExecute reqVo = req.getParam();
		String orderId = reqVo.getOrderId();
		//his回调业务的断路器是否开启
		boolean isCircuitBreakerOpen = KasiteHystrixConfig.getBooleanValue(KasiteHystrixCommandKey.HisBizExecuteGroup.name(),"IsCircuitBreakerOpen");
		if( isCircuitBreakerOpen ) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Common.ERROR_CALLHIS,"医院HIS相关缴费接口异常！请您稍后再试！");
		}
		
		//目前mini付的订单，有可能被微信支付同时支付
		//判断mini付的订单，是否被微信、支付宝重复调用统一下单
		if( DefaultClientEnum.minipay.getClientId().equals(reqVo.getClientId())) {
			if(miniPayUniteCheckOrderMap.containsKey(reqVo.getOrderId())) {
				String existConfigKey = miniPayUniteCheckOrderMap.get(reqVo.getOrderId());
				if(existConfigKey!=null && !existConfigKey.equals(reqVo.getConfigKey())) {
					//如果缓存的ConfigKey与入参的ConfigKey不一致
					//说明mini付的订单被重复调用统一小单接口，返回失败
					ChannelTypeEnum existChannelTypeEnum = KasiteConfig.getChannelType(reqVo.getClientId(),existConfigKey);
					ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(reqVo.getClientId(),reqVo.getConfigKey());
					return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_DATAEXIST,
							"MINI付二维码已被"+existChannelTypeEnum.getTitle()+"发起过支付，请勿使用"+channelTypeEnum.getTitle()
							+"再次发起支付！请使用"+existChannelTypeEnum.getTitle()+"支付！");
				}
			}else {
				//如果缓存不存在，则写入缓存<orderId,configKey>
				miniPayUniteCheckOrderMap.put(reqVo.getOrderId(), reqVo.getConfigKey());
			}
		}
		OrderView order = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
		if(null == order) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单未找到orderId：" + orderId);
		}
		Integer isOnLinePay = order.getIsOnlinePay();
		if(!KstHosConstant.ONLINEPAY.equals(isOnLinePay)) {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"该订单不是需要在线支付的订单，请重新申请一个在线支付订单。orderId：" + orderId);
		}
		int overState = order.getOverState()==null?0:order.getOverState().intValue();
		if(overState != KstHosConstant.ORDEROVER_0.intValue()) {
			String msg = "订单overState状态未知，无法进行支付。";
			if(overState == KstHosConstant.ORDEROVER_5.intValue()) {
				msg = "该订单已经超过支付时间，已取消。请重新下单！orderId：";
			}else if(overState == KstHosConstant.ORDEROVER_6.intValue()) {
				msg = "该订单已撤销。请重新下单！orderId：";
			}
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					msg + orderId);
		}
		//payState 总共有4个状态 0:待支付  1: 支付中  2:支付完成 3:退费中 4:退费完成 
		//bizState 总共有2个状态 0:未执行业务  1:订单业务完成  2:订单业务取消
		//overState 总共有2个状态 5:订单取消  6:订单撤销  7:已关闭
		int payState = order.getPayState()==null?0: order.getPayState().intValue();
		if( payState != KstHosConstant.ORDERPAY_0.intValue()) {
			String msg = "订单payState状态未知，无法进行支付。";
			if(payState == KstHosConstant.ORDERPAY_1.intValue()) {
				msg = "该订单正在支付，请勿重复支付！orderId：";
			}else if(payState == KstHosConstant.ORDERPAY_2.intValue()){
				msg = "该订单已支付完成，请勿重复支付！orderId：";
			}else if(payState == KstHosConstant.ORDERPAY_3.intValue()){
				msg = "该订单已退费，确认订单状态！orderId：";
			}else if(payState == KstHosConstant.ORDERPAY_4.intValue()){
				msg = "该订单已退费，确认订单状态！orderId：";
			}
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					msg + orderId);
		}
		
		int payPrice = order.getPrice()==null?0:order.getPrice().intValue();
		String priceName = order.getPriceName();
		//支付金额必须 > 0 < 99999 后续校验金额控制大小可以在这里加入限制 这个是目前金额大小限制 超过 10w 不能直接充值
		if(0 < payPrice && payPrice < 9999999) {
			//TODO 判断配置的限额
			
			//TODO 判断医院HIS接口是否正常。
			/*
			 * 如果HIS接口有根据不通接口个性化提供 则这里根据业务进行扩展 如果没有 那么默认自行 从his唯一的接口上进行判断
			 */
			String serviceId = order.getServiceId();
			HisInfStatus status = HisInfStatus.error;
			BusinessTypeEnum bt = BusinessTypeEnum.valuesOfCode(serviceId);
			ICallHisService service = HandlerBuilder.get().getCallHisService(req.getParam().getAuthInfo());
			if(null != service) {
				status = service.infStatus(req.getMsg(), bt);
				if(status == HisInfStatus.error) {
					return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CALLHIS,
							"目前HIS接口无法正常通讯，请联系管理员。订单无法完成支付，请稍后网络恢复后再进行缴费。orderNum ：" + order.getOrderNum());
				}
			}
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000,new RespMap()
					.add(ApiKey.checkPayOrder.price, payPrice)
					.add(ApiKey.checkPayOrder.priceName, priceName)
					.add(ApiKey.checkPayOrder.clientId, order.getChannelId())
					);
		}else {
			return new CommonResp<RespMap>(req,KstHosConstant.DEFAULTTRAN, RetCode.Order.ERROR_CANNOTEXIST,
					"订单的支付金额不能超过限额【0-99999】元，请核对订单金额。orderNum ：" + order.getOrderNum());
		}
	}
	
	@Override
	public CommonResp<RespMap> queryOrderState(CommonReq<ReqQueryOrderPayState> commReq)throws Exception{
		ReqQueryOrderPayState reqQueryOrderPayState = commReq.getParam();
		RespMap respMap = new RespMap();
		//如果入参为退费订单号，或者第三方退费订单号，则查询对应退费订单的退费情况
		if( !StringUtil.isEmpty(reqQueryOrderPayState.getRefundOrderId()) || !StringUtil.isEmpty(reqQueryOrderPayState.getOutRefundOrderId())) {
			RefundOrder queryRefundOrder = new RefundOrder();
			queryRefundOrder.setOutRefundOrderId(reqQueryOrderPayState.getOutRefundOrderId());
			queryRefundOrder.setRefundOrderId(reqQueryOrderPayState.getRefundOrderId());
			RefundOrder refundOrder = refundOrderMapper.selectOne(queryRefundOrder);
			if( refundOrder == null ) {
				return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CANNOTEXIST);
			}
			Integer refundState = refundOrder.getPayState();
			if(KstHosConstant.ORDERPAY_7.equals(refundState) || KstHosConstant.ORDERPAY_3.equals(refundState)) {
				//如果退款失败，或者退款中，再查询商户的状态
				ReqQueryMerchantRefund reqQueryMerchantRefund = new ReqQueryMerchantRefund(reqQueryOrderPayState.getMsg(),
						reqQueryOrderPayState.getOrderId(), reqQueryOrderPayState.getRefundOrderId(), null,DateOper.formatDate(refundOrder.getBeginDate(), "yyyyMMddHHmmss"));
				CommonResp<RespMap>  commResp = payService.queryMerchantRefund(new CommonReq<ReqQueryMerchantRefund>(reqQueryMerchantRefund));
				if( commResp.getCode().equals(RetCode.Success.RET_10000.toString())) {
					//查询成功
					refundState = commResp.getResultData().getInteger(ApiKey.QueryMerchantRefund.RefundStatus);
				} else if( !commResp.getCode().equals(RetCode.Pay.ERROR_MERCHANTORDER.toString())) {
					//其他异常直接返回。（查询不到商户订单，说明退款失败,直接返回数据库的退费状）
					return commResp;
				}
			}
			OrderView orderView = orderMapper.getOrderViewByOrderId(refundOrder.getOrderId(),KasiteConfig.getHistoryOrderListDays());
			respMap.put(ApiKey.QueryOrderState.OrderState,refundState);
			respMap.put(ApiKey.QueryOrderState.RefundOrderId,refundOrder.getRefundOrderId());
			respMap.put(ApiKey.QueryOrderState.OrderId,refundOrder.getOrderId());
			respMap.put(ApiKey.QueryOrderState.TransactionNo,orderView.getTransactionNo());
			return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
		}
		//如果入参为订单号
		if( !StringUtil.isEmpty(reqQueryOrderPayState.getOrderId()) ) {
			String orderId = reqQueryOrderPayState.getOrderId();
			OrderView orderView = orderMapper.getOrderViewByOrderId(orderId,KasiteConfig.getHistoryOrderListDays());
			if( orderView == null ) {
				return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_CANNOTEXIST);
			}
			Integer payState = orderView.getPayState();
			Integer overState = orderView.getOverState();
			if( !KstHosConstant.ORDEROVER_0.equals(overState)) {
				//如果存在终结状态，则直接返回终结状态
				respMap.put(ApiKey.QueryOrderState.OrderState,overState);
			}else {
				if( KstHosConstant.ORDERPAY_0.equals(payState) || KstHosConstant.ORDERPAY_1.equals(payState)) {
					//如果订单未支付、正在支付，则查询商户订单的支付状态
					String beginTimes = DateOper.formatDate(orderView.getBeginDate(), "yyyyMMddHHmmss");
					ReqQueryMerchantOrder reqQueryMerchantOrder = new ReqQueryMerchantOrder(reqQueryOrderPayState.getMsg(), reqQueryOrderPayState.getOrderId(),null
							,beginTimes,KstHosConstant.I1,orderView.getChannelId(),orderView.getConfigKey());
					CommonResp<RespMap> queryMerchantOrderResp = payService.queryMerchantOrder(new CommonReq<ReqQueryMerchantOrder>(reqQueryMerchantOrder));
					if(RetCode.Success.RET_10000.getCode().equals(queryMerchantOrderResp.getRetCode().getCode())
							&& !CollectionUtils.isEmpty(queryMerchantOrderResp.getData())) {
						//如果商户有订单
						respMap.put(ApiKey.QueryOrderState.OrderState,queryMerchantOrderResp.getData().get(0).getString(ApiKey.QueryMerchantOrder.OrderState));
						respMap.put(ApiKey.QueryOrderState.TransactionNo,queryMerchantOrderResp.getData().get(0).getString(ApiKey.QueryMerchantOrder.TransactionNo));
					}
				}else {
					//如果payState = 2 、3 、4 则返回2
					respMap.put(ApiKey.QueryOrderState.OrderState,KstHosConstant.ORDERPAY_2);
					respMap.put(ApiKey.QueryOrderState.TransactionNo,orderView.getTransactionNo());
				}
			}
			return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respMap);
		}else {
			return new CommonResp<RespMap>(commReq,KstHosConstant.DEFAULTTRAN,RetCode.Order.ERROR_SENDVALUE);
		}
		
	}
	
	@Override
	public CommonResp<RespMap> forceCorrectOrderBiz(CommonReq<ReqForceCorrectOrderBiz> commReq) throws Exception {		
		ReqForceCorrectOrderBiz reqForceCorrectOrderBiz = commReq.getParam();
		ReqMerchantNotifyForceRetry reqMerchantNotifyForceRetry = new ReqMerchantNotifyForceRetry(reqForceCorrectOrderBiz.getMsg(), reqForceCorrectOrderBiz.getOrderId());
		return payMerchantService.merchantNotifyForceRetry(new CommonReq<ReqMerchantNotifyForceRetry>(reqMerchantNotifyForceRetry));
	}
	
	
	@Override
	public CommonResp<RespMap> refundOrderAgin(CommonReq<ReqOrderIsCancel> commReq) throws Exception {
		
		return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}
	

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespMap> addOrderSub(CommonReq<ReqAddOrderSub> commReq) throws Exception {
		ReqAddOrderSub reqAddOrderSubLocal = commReq.getParam();
		OrderSub queryOrderSub = new OrderSub();
		queryOrderSub.setOrderId(reqAddOrderSubLocal.getOrderId());
		queryOrderSub.setSubHisOrderId(reqAddOrderSubLocal.getSubHisOrderId());
		OrderSub orderSub = orderSubMapper.selectOne(queryOrderSub);
		if( orderSub !=null) {//存在his子订单号、价格一致的，则直接返回成功
			if(  orderSub.getPrice().intValue() == queryOrderSub.getPrice().intValue() ) {
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
			}else {
				//金额不一致，则修改,处方订单金额有可能修改
				orderSub.setPrice(reqAddOrderSubLocal.getPrice());
				orderSub.setUpdateTime(com.kasite.core.common.util.DateOper.getNowDateTime());
				orderSubMapper.updateByPrimaryKeySelective(orderSub);
				return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
			}
		}else {
			orderSub = new OrderSub();
			orderSub.setOrderId(reqAddOrderSubLocal.getOrderId());
			orderSub.setHisRegId(reqAddOrderSubLocal.getHisRegId());
			orderSub.setCreateTime(com.kasite.core.common.util.DateOper.getNowDateTime());
			orderSub.setPrice(reqAddOrderSubLocal.getPrice());
			orderSub.setPriceName(reqAddOrderSubLocal.getPriceName());
			orderSub.setSubHisOrderId(reqAddOrderSubLocal.getSubHisOrderId());
			orderSub.setUpdateTime(com.kasite.core.common.util.DateOper.getNowDateTime());
			orderSubMapper.insertSelective(orderSub);
			return new CommonResp<RespMap>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
		}
	}
	

	/**
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public CommonResp<RespQueryOrderSubList> queryOrderSubList(CommonReq<ReqQueryOrderSubList> commReq) throws Exception {
		ReqQueryOrderSubList req = commReq.getParam();
		OrderSub queryOrderSub = new OrderSub();
		queryOrderSub.setSubHisOrderId(req.getSubHisOrderId());
//		queryOrderSub.setId(req.getId());
		queryOrderSub.setOrderId(req.getOrderId());
		List<OrderSub> list = orderSubMapper.select(queryOrderSub);
		List<RespQueryOrderSubList> respList = new ArrayList<RespQueryOrderSubList>();
		if( list!=null && list.size()>0 ) {
			for(OrderSub  orderSub : list) {
				RespQueryOrderSubList respQueryOrderSubList = BeanCopyUtils.copyProperties(orderSub, new RespQueryOrderSubList(), null);
				
				OrderItem queryOrderItem = new OrderItem();
				queryOrderItem.setHisOrderId(orderSub.getSubHisOrderId());
				queryOrderItem.setOrderId(req.getOrderId());
				List<OrderItem> orderItemList = orderItemMapper.select(queryOrderItem);
				List<CommonPrescriptionItem> data1List = new ArrayList<CommonPrescriptionItem>();
				if( orderItemList!=null && orderItemList.size()>0) {
					for(OrderItem orderItem : orderItemList) {
						CommonPrescriptionItem item = BeanCopyUtils.copyProperties(orderItem, new CommonPrescriptionItem(), null);
						data1List.add(item);
					}
				}
				respQueryOrderSubList.setData_1(data1List);
				respList.add(respQueryOrderSubList);
			}
		}
		return new CommonResp<RespQueryOrderSubList>(commReq, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000,respList);
	}

	@Override
	public int queryBizState(CommonReq<ReqSyncLocalOrderState> commReq) throws Exception {
		ReqSyncLocalOrderState req = commReq.getParam();
		String orderId = req.getOrderId();
		BizOrder bizOrder = bizOrderMapper.selectByPrimaryKey(orderId);
		if(bizOrder != null) {
			return bizOrder.getBizState();
		}else {
			return 0;
		}
	}
	
	@Override
	public CommonResp<RespMap> addOrderItem(CommonReq<ReqAddOrderItem> req) throws Exception {
		ReqAddOrderItem  addOrderItem = req.getParam();
		
		if(addOrderItem.getItemList()!=null && addOrderItem.getItemList().size()>0) {
			List<OrderItem> addList = new ArrayList<>();
			for (Object addItem : addOrderItem.getItemList()) {
				if(addItem instanceof OrderItem) {
					addList.add((OrderItem)addItem);
				}
			}
			if(addList.size()>0) {
				orderItemMapper.batchInsert(addList);
			}
		}
		return new CommonResp<RespMap>(req, KstHosConstant.DEFAULTTRAN,RetCode.Success.RET_10000);
	}

}
