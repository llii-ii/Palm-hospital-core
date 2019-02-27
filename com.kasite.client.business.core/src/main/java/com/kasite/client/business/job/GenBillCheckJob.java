package com.kasite.client.business.job;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kasite.client.business.module.backstage.bill.dao.BillChannelRFDao;
import com.kasite.client.business.module.backstage.bill.dao.BillCheckDao;
import com.kasite.client.business.module.backstage.bill.dao.BillMerchRFDao;
import com.kasite.client.order.bean.dbo.PayOrder;
import com.kasite.client.order.dao.IOrderMapper;
import com.kasite.client.order.dao.IPayOrderMapper;
import com.kasite.client.pay.bean.dbo.Bill;
import com.kasite.client.pay.dao.IBillMapper;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.CommonCode;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteConfigMap;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DateUtils;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.channel.dto.ChannelVo;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IDownloadOutQLCOrder;
import com.kasite.core.serviceinterface.module.his.resp.HisBill;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryHisBill;
import com.kasite.core.serviceinterface.module.his.resp.RespQueryOrderCheck;
import com.kasite.core.serviceinterface.module.order.dto.OrderVo;
import com.kasite.core.serviceinterface.module.order.dto.RefundOrderDetailVo;
import com.kasite.core.serviceinterface.module.pay.IBillService;
import com.kasite.core.serviceinterface.module.pay.dbo.BillChannelRF;
import com.kasite.core.serviceinterface.module.pay.dbo.BillCheck;
import com.kasite.core.serviceinterface.module.pay.dbo.BillMerchRF;
import com.kasite.core.serviceinterface.module.pay.dto.BillCheckVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillDetailVo;
import com.kasite.core.serviceinterface.module.pay.dto.BillVo;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetBill;
import com.yihu.wsgw.api.InterfaceMessage;

import tk.mybatis.mapper.entity.Example;

/**
 * 商户账单、His账单、全流程订单对账Job
 * 
 * @author zhaoy
 *
 */
@Component
public class GenBillCheckJob {

	private final Log log = LogFactory.getLog(KstHosConstant.LOG4J_JOB);

	private boolean flag = true;
	
	@Autowired
	KasiteConfigMap config;
	
	@Autowired
	BillCheckDao billCheckDao;
	
	@Autowired
	BillMerchRFDao billMerchRFDao;
	
	@Autowired
	BillChannelRFDao billChannelRFDao;
	
	@Autowired
	IBillService billService;
	
	@Autowired
	IBillMapper billMapper;
	
	@Autowired
	IOrderMapper orderMapper;
	
	@Autowired
	IPayOrderMapper payOrderMapper;

	/**
	 * 交易订单对账账单
	 * 		--每天12点25分执行
	 * 
	 * @Description:
	 */
	public void execute(){
		try {
			if (flag && config.isStartJob(this.getClass())) {
				flag = false;
				log.info("----------开始对账--------------");
				
				String startDate = DateUtils.getYesterdayStartString(new Date());
				String endDate = DateUtils.getYesterdayEndString(new Date());
				String date = DateUtils.getYesterdayString(new Date());
				Timestamp checkDate = DateOper.getNowDateTime();
				
				deal(startDate, endDate, date, checkDate);
				
				flag = true;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			LogUtil.error(log, e);
		} finally {
			flag = true;
		}
	}
	
	public void deal(String startDate, String endDate, String date, Timestamp checkDate) throws Exception{
		//1.删除旧有重复数据
		Example BCExample = new Example(BillCheck.class);
		BCExample.createCriteria().andBetween("transDate", startDate, endDate);
		billCheckDao.deleteByExample(BCExample);
		//用来存放当日原始账单中的支付订单号相对应的退款订单号(当作临时差错池)
		Map<String, String> orderMap = new HashMap<>();
		//2.构建当日对账数据
		List<BillCheck> billCheckList = this.buildBillCheck(startDate, endDate, orderMap, checkDate);
		if(billCheckList == null) {
			return;
		}
		for (BillCheck billCheck : billCheckList) {
			//3.当日对账数据入库
			billCheckDao.insertSelective(billCheck);
		}
		List<BillMerchRF> billMerchRFList = this.buildBillMerchRF(startDate, endDate, date, checkDate);
		if(billMerchRFList == null) {
			return;
		}
		Example BMRExample = new Example(BillMerchRF.class);
		BMRExample.createCriteria().andEqualTo("date", startDate);
		billMerchRFDao.deleteByExample(BMRExample);
		for (BillMerchRF billMerchRF : billMerchRFList) {
			billMerchRFDao.insertSelective(billMerchRF);
		}
		
		List<BillChannelRF> billChannelRFList = this.buildBillChannelRF(startDate, endDate, date, orderMap, checkDate);
		if(billChannelRFList == null || billChannelRFList.size() == 0) {
			return;
		}
		Example BCRExample = new Example(BillChannelRF.class);
		BCRExample.createCriteria().andEqualTo("date", startDate);
		billChannelRFDao.deleteByExample(BCRExample);
		for (BillChannelRF billChannelRF : billChannelRFList) {
			billChannelRFDao.insertSelective(billChannelRF);
		}
		
		log.info("对账结束:共对账明细" + billCheckList.size() + "条记录,商户对账统计报表共:" + billMerchRFList.size() 
		+ "条记录,交易渠道对账统计报表共:" + billChannelRFList.size() + "条记录!");
	}
	
	/**
	 * 
	 * <p>Description: 构建对账核对数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年9月14日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @return
	 * @return List<BillCheckBean> 
	 * @throws
	 */
	private List<BillCheck> buildBillCheck(String startDate, String endDate, Map<String, String> orderMap, Timestamp checkDate) throws Exception{
		InterfaceMessage msgQuery = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryAllBill", 
				KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
		msgQuery.setParamType(2);
		CommonResp<RespMap> queryResp = billService.queryAllBill(new CommonReq<ReqGetBill>(new ReqGetBill(msgQuery, startDate, endDate)));
		if(KstHosConstant.SUCCESSCODE.equals(queryResp.getCode())) {
			Map<ApiKey,Object> resultMap = queryResp.getResultData().getMap();
			Map<String, BillVo> map = (HashMap<String, BillVo>)resultMap.get(ApiKey.BillRFPro.BillMap);
			if(map == null || map.size() == 0) {
				return null;
			}
			Map<String, BillVo> payMap = new HashMap<>();
			List<BillCheck> billCheckList = new ArrayList<BillCheck>();
			for (String key : map.keySet()) {
				BillVo billVo = map.get(key);
				String orderId = billVo.getOrderId();
				String orderNo = null;
				String merchNo = null;
				String payMethod = "";
				String payMethodName = "";
				String channelId = billVo.getChannelId();
				//湖北省，十堰市，郧西县人民医院 与其他商家共用商户号，此处过滤非全流程渠道的订单
				if(KasiteConfig.getOrgCode().equals("22108") && KstHosConstant.UNKNOW_CHANNEL_ID.equals(channelId)) {
					continue;
				}
				String channelName = billVo.getChannelName();
				String configkey = billVo.getConfigkey();
				ChannelTypeEnum payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
				if(payInfo != null) {
					payMethod = payInfo.name();
					payMethodName = payInfo.getTitle();
				}
				Timestamp transDate = billVo.getTransDate();
				Integer hisPrice = null;
				Integer merchPrice = null;
				Integer checkState = null;
				Integer orderType = billVo.getOrderType();
				String hisOrderNo = billVo.getHisOrderId();
					
				if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
					orderNo = billVo.getOrderId();
					merchNo = billVo.getMerchNo();
				}else if(KstHosConstant.BILL_ORDER_TYPE_2.equals(orderType)) {
					orderNo = billVo.getRefundOrderId();
					merchNo = billVo.getRefundMerchNo();
				}
				if(hisOrderNo != null && merchNo != null) {     //没有单边账的情况
					if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
						hisPrice = billVo.getHisPayPrice();
						merchPrice = billVo.getMerchPayPrice();
						if(hisPrice > merchPrice) {
							checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE;
						}else if(hisPrice < merchPrice) {
							checkState = KstHosConstant.BILL_CHECK_STATE_1;
						}else if(hisPrice.equals(merchPrice)) {
							checkState = KstHosConstant.BILL_CHECK_STATE_0;
						}
					}else if(KstHosConstant.BILL_ORDER_TYPE_2.equals(orderType)) {
						hisPrice = billVo.getHisRefundMoney();
						merchPrice = billVo.getMerchRefundPrice();
						if(hisPrice > merchPrice) {
							checkState = KstHosConstant.BILL_CHECK_STATE_1;
						}else if(hisPrice < merchPrice) {
							checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE;
						}else if(hisPrice.equals(merchPrice)) {
							checkState = KstHosConstant.BILL_CHECK_STATE_0;
						}
					}
					BillCheckVo billCheckOld = billCheckDao.findBillCheckByOrderNo(orderNo, false, false, orderType);
					if(billCheckOld != null) {
						BillCheck obj = new BillCheck();
						BeanCopyUtils.copyProperties(billCheckOld, obj, null);
						obj.setCheckState(checkState);
						obj.setUpdateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
						billCheckDao.updateByPrimaryKeySelective(obj);
						//重新统计
//							revertBillCount(billCheckOld);
						continue;
					}
					if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
						if(payMap != null && payMap.containsKey(orderId)) {
							checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE;
						}
						payMap.put(orderId, billVo);
					}
				}else if(hisOrderNo == null && merchNo != null) {   //商户单边账
					//支付订单
					if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
						merchPrice = billVo.getMerchPayPrice();
						//1.暂定为商户单边账
						checkState = KstHosConstant.BILL_CHECK_STATE_1;
						//2.判断是否为当日的一正一负的订单(His执行业务失败,直接退款的订单)
						if(orderMap != null && orderMap.containsKey(orderNo)) {
							continue;
						}
						RespQueryHisBill queryHisPayOrder = null;
						OrderVo thisOrder = orderMapper.findCardNoForOrder(orderId);
						if(thisOrder == null) {
							thisOrder = orderMapper.findCardNoForOrderCheck(orderId);
						}
						if(thisOrder != null) {
							InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryHisOrderBillByPayNo", 
									KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
							Map<String,String> paramMap = new HashMap<String,String>();
							paramMap.put("orderId", orderId);
							paramMap.put("channelId", thisOrder.getChannelId());
							paramMap.put("cardNo", thisOrder.getCardNo());
							paramMap.put("serviceId", thisOrder.getServiceId());
							paramMap.put("payDate", DateUtils.getTimestampToStr(thisOrder.getPayDate()));
							paramMap.put("payNo", merchNo);
							paramMap.put("memberName", thisOrder.getNickName());
							CommonResp<RespQueryHisBill> resp = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode()).queryHisOrderBillByPayNo(msg, paramMap);
							if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
								List<RespQueryHisBill> respList = resp.getList();
								if(respList != null && respList.size() > 0) {
									for (RespQueryHisBill queryOrder : respList) {
										Integer type = queryOrder.getHisOrderType();
										if(KstHosConstant.BILL_ORDER_TYPE_1.equals(type)) {
											queryHisPayOrder = queryOrder;
											continue;
										}
									}
								}
							}
							if(queryHisPayOrder != null) {
								hisOrderNo = queryHisPayOrder.getHisOrderId();
								hisPrice = new Integer(queryHisPayOrder.getPayMoney());
								if(hisPrice > merchPrice) {
									checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE;
								}else if(hisPrice < merchPrice) {
									checkState = KstHosConstant.BILL_CHECK_STATE_1;
								}else if(hisPrice.equals(merchPrice)) {
									checkState = KstHosConstant.BILL_CHECK_STATE_0;
								}
							}
						}
					}else if(KstHosConstant.BILL_ORDER_TYPE_2.equals(orderType)) {
						merchPrice = billVo.getMerchRefundPrice();
						//1.昨天之前商户是否存在支付订单信息(判断是否跨日)
						Example billExample =  new Example(Bill.class);
						billExample.createCriteria().andLessThan("transDate", startDate)
							.andEqualTo("orderId", orderId)
								.andEqualTo("orderType", KstHosConstant.BILL_ORDER_TYPE_1);
						Bill bill = billMapper.selectOneByExample(billExample);
						//2.his是否有支付信息
						HisBill hisBill = billMapper.findHisBillByOrderId(orderId, KstHosConstant.BILL_ORDER_TYPE_1);
						//3.如果两边都是空，则此订单为当日的一正一负的订单(His执行业务失败,直接退款的订单)
						if(bill == null && hisBill == null) {
							orderMap.put(orderId, channelId + "_" + orderNo);
							continue;
						}else if(bill != null && hisBill == null){ //如果bill账单不为空，则为渠道单边账
							//1.先判断是否有未处理的支付长款（渠道单边账）
							BillCheckVo billCheckOld = billCheckDao.findBillCheckByOrderNo(orderId, true, false, KstHosConstant.BILL_ORDER_TYPE_1);
							if(billCheckOld != null && KstHosConstant.BILL_IS_DEAL_0.equals(billCheckOld.getDealState())) {
								BillCheck obj = new BillCheck();
								BeanCopyUtils.copyProperties(billCheckOld, obj, null);
								checkState = KstHosConstant.BILL_CHECK_STATE_0;
								obj.setCheckState(checkState);
								obj.setDealby(KstHosConstant.SYSOPERATORID);
								obj.setDealDate(checkDate);
								obj.setDealState(KstHosConstant.BILL_IS_DEAL_1);
								obj.setDealway(KstHosConstant.BILL_DEAL_WAY_1);
								String diffReason = CommonCode.Descr.DIFF_REASON_2.getMessage();
								String diffDesc = CommonCode.Descr.DIFF_REASON_2_DECR.getMessage();
								obj.setDealRemark(diffReason+"-"+diffDesc+"-退款成功-"+orderNo);
								obj.setUpdateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
								billCheckDao.updateByPrimaryKeySelective(obj);
								//重新统计
								revertBillCount(billCheckOld, checkDate);
							}
						}else {
							checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE;  //暂定短款
							//此处查询是为了拿到该订单的支付流水号
							billExample =  new Example(Bill.class);
							billExample.createCriteria().andEqualTo("refundMerchNo", merchNo);
							List<RespQueryHisBill> queryHisRefundOrderList = new ArrayList<>();
							OrderVo thisOrder = orderMapper.findCardNoForOrder(orderId);
							if(thisOrder == null) {
								thisOrder = orderMapper.findCardNoForOrderCheck(orderId);
							}
							if(thisOrder != null) {
								InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryHisOrderBillByPayNo", 
										KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
								Map<String,String> paramMap = new HashMap<String,String>();
								paramMap.put("orderId", orderId);
								paramMap.put("channelId", thisOrder.getChannelId());
								paramMap.put("cardNo", thisOrder.getCardNo());
								paramMap.put("serviceId", thisOrder.getServiceId());
								paramMap.put("payDate", DateUtils.getTimestampToStr(thisOrder.getPayDate()));
								paramMap.put("payNo", billVo.getMerchNo());
								paramMap.put("memberName", thisOrder.getNickName());
								CommonResp<RespQueryHisBill> resp = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode()).queryHisOrderBillByPayNo(msg, paramMap);
								if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
									List<RespQueryHisBill> respList = resp.getList();
									if(respList != null && respList.size() > 0) {
										for (RespQueryHisBill queryOrder : respList) {
											Integer type = queryOrder.getHisOrderType();
											if(KstHosConstant.BILL_ORDER_TYPE_2.equals(type)) {
												queryHisRefundOrderList.add(queryOrder);
											}
										}
									}
								}
								if(queryHisRefundOrderList != null && queryHisRefundOrderList.size() > 0) {
									for (RespQueryHisBill refundHisOrder : queryHisRefundOrderList) {
										Integer refundHisPrice = new Integer(refundHisOrder.getRefundMoney());
										if(refundHisPrice.equals(merchPrice)) {
											hisPrice = refundHisPrice;
											hisOrderNo = refundHisOrder.getHisOrderId();
											checkState = KstHosConstant.BILL_CHECK_STATE_0; 
										}
									}
								}
							}
						}
					}
				}else if(hisOrderNo != null && merchNo == null) {   //His单边账
					if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
						hisPrice = billVo.getHisPayPrice();
						//会出现支付的短款，服务器的系统时间不一致导致的His单边账
						PayOrder payOrder = payOrderMapper.selectByPrimaryKey(orderId);
						if(payOrder == null) {
							IDownloadOutQLCOrder service = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode(), IDownloadOutQLCOrder.class);
							if(service != null) {
								InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryOutOrderList", 
										KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
								Map<String,String> paramMap = new HashMap<String,String>();
								paramMap.put("orderId", orderId);
//									paramMap.put("orderType", String.valueOf(KstHosConstant.BILL_ORDER_TYPE_1));
								CommonResp<RespQueryOrderCheck> resp = service.queryOutOrderList(msg, paramMap);
								if(KstHosConstant.SUCCESSCODE.equals(resp.getCode())) {
									List<RespQueryOrderCheck> respList = resp.getList();
									if(respList != null && respList.size() > 0) {
										RespQueryOrderCheck queryOrder = respList.get(0);
										merchPrice = queryOrder.getPayPrice();
										merchNo = queryOrder.getTransactionNo();
										checkState = KstHosConstant.BILL_CHECK_STATE_0;
									}else {
										checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE; //确定为支付的His单边账(短款)
									}
								}else {
									checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE; //确定为支付的His单边账(短款)
								}
							}else {
								checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE; //确定为支付的His单边账(短款)
							}
						}else {
							if(KstHosConstant.ORDERPAY_2.equals(payOrder.getPayState())) {
								configkey = payOrder.getConfigKey();
								payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
								if(payInfo != null) {
									payMethod = payInfo.name();
									payMethodName = payInfo.getTitle();
								}
								merchPrice = payOrder.getPrice();
								merchNo = payOrder.getTransactionNo();
								checkState = KstHosConstant.BILL_CHECK_STATE_0;
							}else {
								checkState = KstHosConstant.BILL_CHECK_STATE_1_NEGATIVE; //确定为支付的His单边账(短款)
							}
						}
					}else if(KstHosConstant.BILL_ORDER_TYPE_2.equals(orderType)) {
						hisPrice = billVo.getHisRefundMoney();
						//先判断是否为跨日账
						RefundOrderDetailVo refundOrder = orderMapper.findRefundOrderByOrderNo(orderNo);
						if(refundOrder == null) {
							IDownloadOutQLCOrder service = HandlerBuilder.get().getCallHisService(KasiteConfig.getOrgCode(), IDownloadOutQLCOrder.class);
							if(service != null) {
								InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryOutOrderList", 
										KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
								Map<String,String> paramMap = new HashMap<String,String>();
								paramMap.put("refundOrderId", orderNo);
								RespQueryOrderCheck thisRefundOrder = service.queryRefundOrderInfo(msg, paramMap);
								if(thisRefundOrder != null) {
									refundOrder = new RefundOrderDetailVo();
									refundOrder.setRefundNo(thisRefundOrder.getRefundNo());
									refundOrder.setRefundPrice(thisRefundOrder.getRefundPrice());
								}
							}
						}
						if(refundOrder != null && StringUtil.isNotEmpty(refundOrder.getRefundNo())) {
							configkey = refundOrder.getConfigkey();
							payInfo = KasiteConfig.getPayTypeByConfigKey(configkey);
							if(payInfo != null) {
								payMethod = payInfo.name();
								payMethodName = payInfo.getTitle();
							}
							merchNo = refundOrder.getRefundNo();
							merchPrice = refundOrder.getRefundPrice();
							checkState = KstHosConstant.BILL_CHECK_STATE_0;
						}else {
							//确定为退款的His单边账(长款)
							checkState = KstHosConstant.BILL_CHECK_STATE_1;
						}
					}
				}else {
					continue;
				}
				
				BillCheck billCheck = new BillCheck();
				billCheck.setId(StringUtil.getUUID());  //随机数
				billCheck.setOrderNo(orderNo);
				billCheck.setOrderId(orderId);
				billCheck.setHisOrderNo(hisOrderNo);
				billCheck.setMerchNo(merchNo);
				billCheck.setConfigkey(configkey);
				billCheck.setTransDate(transDate);
				billCheck.setPayMethod(payMethod);
				billCheck.setPayMethodName(payMethodName);
				billCheck.setChannelId(channelId);
				billCheck.setChannelName(channelName);
				billCheck.setBillType(orderType);
				BillDetailVo orderVo = new BillDetailVo();
				if(KstHosConstant.BILL_ORDER_TYPE_1.equals(orderType)) {
					//支付订单
					orderVo = billCheckDao.findPayOrderById(orderNo);
				}else if(KstHosConstant.BILL_ORDER_TYPE_2.equals(orderType)){
					//退款订单
					orderVo = billCheckDao.findRefundOrderById(orderNo);
				}
				if(orderVo == null) {
					orderVo = this.getOrderDetail(orderId, orderNo);
				}
				if(orderVo != null) {
					billCheck.setServiceId(orderVo.getServiceId());
				}
				billCheck.setHisBizDate(billVo.getHisBizDate());
				billCheck.setPayDate(billVo.getPayTransDate());
				billCheck.setHisPrice(hisPrice);
				billCheck.setMerchPrice(merchPrice);
				billCheck.setCheckState(checkState);
				billCheck.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
				billCheck.setCreateDate(checkDate);
				billCheck.setUpdateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
				billCheck.setUpdateDate(checkDate);
				
				billCheckList.add(billCheck);
			}
			return billCheckList;
		}
		return null;
	}
	
	/**
	 * 重新统计对账
	 * 
	 * @param billCheckOld
	 * @throws Exception
	 */
	public void revertBillCount(BillCheckVo billCheckOld, Timestamp checkDate) throws Exception{
		//重新统计当日账单
		String thisTransDate = DateUtils.getTimestampToStr(billCheckOld.getTransDate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = sdf.parse(thisTransDate);
		String thisStartDate = DateOper.formatDate(date1, "yyyy-MM-dd");
		Date date2 = DateOper.getNextDay(date1, 1);
		String thisEndDate = DateOper.formatDate(date2, "yyyy-MM-dd");
		String thisChannelId = billCheckOld.getChannelId();
		String thisConfigkey = billCheckOld.getConfigkey();
		int exceptBillCount = billCheckDao.findExceptBillCount(thisStartDate, thisEndDate, thisChannelId, thisConfigkey);
		if(exceptBillCount == 0) {
			int dealBillCount = billCheckDao.findDealBillCount(thisStartDate, thisEndDate, thisChannelId, thisConfigkey);
			int dealState = KstHosConstant.BILL_IS_DEAL_0; //未处置
			if(dealBillCount > 0) {
				dealState = KstHosConstant.BILL_IS_DEAL_1; //已处置
			}
			Example BMRExample = new Example(BillMerchRF.class);
			BMRExample.createCriteria().andEqualTo("date", thisStartDate).andEqualTo("configkey", thisConfigkey);
			BillMerchRF billMerchRF = billMerchRFDao.selectOneByExample(BMRExample);
			billMerchRF.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);
			billMerchRF.setDealState(dealState);
			billMerchRF.setUpdateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
			billMerchRF.setUpdateDate(checkDate);
			billMerchRFDao.updateByExampleSelective(billMerchRF, BMRExample);
			
			Example BCRExample = new Example(BillChannelRF.class);
			BCRExample.createCriteria().andEqualTo("date", thisStartDate).andEqualTo("channelId", thisChannelId);
			BillChannelRF billChannelRF = billChannelRFDao.selectOneByExample(BCRExample);
			billChannelRF.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);
			billChannelRF.setDealState(dealState);
			billChannelRF.setUpdateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
			billChannelRF.setUpdateDate(checkDate);
			billChannelRFDao.updateByExampleSelective(billChannelRF, BCRExample);
		}
	}
	
	/**
	 * 
	 * <p>Description: 构建对账商户报表数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月2日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param date
	 * @param @return
	 * @param @throws Exception
	 * @return billMerchRF 
	 * @throws
	 */
	private List<BillMerchRF> buildBillMerchRF(String startDate, String endDate, String date, Timestamp checkDate) throws Exception{
		Map<String, BillMerchRF> payMoneyMap = buildBillMap(startDate, endDate, KstHosConstant.BILL_ORDER_TYPE_1);
		Map<String, BillMerchRF> refundMoneyMap = buildBillMap(startDate, endDate, KstHosConstant.BILL_ORDER_TYPE_2);
		if(payMoneyMap == null && refundMoneyMap == null) {
			return null;
		}
		List<BillMerchRF> billMerchList = new ArrayList<>();
		if(payMoneyMap != null) {
			payMoneyMap.keySet().forEach((String configkey) -> {
				BillMerchRF billMerchPayRF = payMoneyMap.get(configkey); 
				Long hisBillRefundSum = 0L;
				Long merchBillRefundSum = 0L;
				if(refundMoneyMap != null && refundMoneyMap.containsKey(configkey)) {
					BillMerchRF billMerchRefundRF = refundMoneyMap.get(configkey); 
					hisBillRefundSum = billMerchRefundRF.getHisBillSum()==null?0L:billMerchRefundRF.getHisBillSum();
					merchBillRefundSum = billMerchRefundRF.getMerchBillSum()==null?0L:billMerchRefundRF.getMerchBillSum();
				}
				billMerchPayRF.setDate(date);
				Integer checkState = null;
				Long hisBillPaySum = billMerchPayRF.getHisBillSum()==null?0L:billMerchPayRF.getHisBillSum();
				Long merchBillPaySum = billMerchPayRF.getMerchBillSum()==null?0L:billMerchPayRF.getMerchBillSum();
				
				Long hisBillSum = hisBillPaySum - hisBillRefundSum;
				Long merchBillSum = merchBillPaySum - merchBillRefundSum;
				if(hisBillSum.equals(merchBillSum)) {
					checkState = KstHosConstant.BILL_CHECK_RF_STATE_1;  //账平
				}else {
					checkState = KstHosConstant.BILL_CHECK_RF_STATE_0;  //账不平
					//判断核对状态是否账平
					Integer result = billCheckDao.findMoneyIsWrong(startDate, endDate, configkey, null);
					if(result == 0) {
						checkState = KstHosConstant.BILL_CHECK_RF_STATE_1;  //账平	
					}
				}
				String bankNo = "";
				String bankName = "";
				String bankShortName = "";
				if(StringUtil.isNotBlank(configkey) && StringUtil.isNotBlank(billMerchPayRF.getPayMethod())) {
					if(ChannelTypeEnum.zfb.name().equals(billMerchPayRF.getPayMethod())) {
						bankNo = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_num, configkey);
						bankName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_name, configkey);
						bankShortName = KasiteConfig.getZfbConfig(ZFBConfigEnum.bank_abs_name, configkey);
					}else if(ChannelTypeEnum.wechat.name().equals(billMerchPayRF.getPayMethod())) {
						bankNo = KasiteConfig.getWxPay(WXPayEnum.bank_num, configkey);
						bankName = KasiteConfig.getWxPay(WXPayEnum.bank_name, configkey);
						bankShortName = KasiteConfig.getWxPay(WXPayEnum.bank_abs_name, configkey);
					}
					billMerchPayRF.setBankNo(bankNo);
					billMerchPayRF.setBankName(bankName);
					billMerchPayRF.setBankShortName(bankShortName);
				}
				
				billMerchPayRF.setHisBillSum(hisBillSum);
				billMerchPayRF.setMerchBillSum(merchBillSum);
				billMerchPayRF.setCheckState(checkState);
				billMerchPayRF.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
				billMerchPayRF.setCreateDate(checkDate);
				billMerchPayRF.setUpdateBy(KstHosConstant.SYSOPERATORID);
				billMerchPayRF.setUpdateDate(checkDate);
				
				billMerchList.add(billMerchPayRF);
			});
		}
		if(refundMoneyMap != null) {
			refundMoneyMap.keySet().forEach((String configkey) -> {
				if(payMoneyMap != null && payMoneyMap.containsKey(configkey)) {
					return;
				}
				BillMerchRF billMerchRF = refundMoneyMap.get(configkey);
				Integer checkState = null;
				Long hisBillRefundSum = billMerchRF.getHisBillSum()==null?0L:billMerchRF.getHisBillSum();
				Long merchBillRefundSum = billMerchRF.getMerchBillSum()==null?0L:billMerchRF.getMerchBillSum();
				if(hisBillRefundSum.equals(merchBillRefundSum)) {
					checkState = KstHosConstant.BILL_CHECK_RF_STATE_1;  //账平
				}else {
					checkState = KstHosConstant.BILL_CHECK_RF_STATE_0;  //账不平
				}
				billMerchRF.setHisBillSum(0-hisBillRefundSum);
				billMerchRF.setMerchBillSum(0-merchBillRefundSum);
				billMerchRF.setCheckState(checkState);
				billMerchRF.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
				billMerchRF.setCreateDate(checkDate);
				billMerchRF.setUpdateBy(KstHosConstant.SYSOPERATORID);
				billMerchRF.setUpdateDate(checkDate);
				billMerchList.add(billMerchRF);
			});
		}
		
		return billMerchList;
	}
	
	/**
	 * 
	 * <p>Description: 构建对账渠道报表数据</p>  
	 * <p>Company: KST</p>
	 * 
	 * @date 2018年5月2日
	 * @author zhaoy  
	 * @param @param startDate
	 * @param @param endDate
	 * @param @param date
	 * @param @return
	 * @param @throws SQLException
	 * @return BillChannelRF 
	 * @throws
	 */
	private List<BillChannelRF> buildBillChannelRF(String startDate, String endDate, String date, Map<String, String> orderMap, Timestamp checkDate) throws Exception{
		
		//1.获取当日所有有账单的渠道ID
		List<ChannelVo> channelList = billCheckDao.findChannelList(startDate, endDate);
		if(channelList == null || channelList.size() == 0) {
			return null;
		}
		List<BillChannelRF> billChannelRFList = new ArrayList<>();
		for (ChannelVo channel : channelList) {
			if(StringUtil.isBlank(channel.getChannelId())) {
				continue;
			}
			String channelId = channel.getChannelId();
			String channelName = channel.getChannelName();
			//2.渠道账单笔数
			int channelBillCount = billCheckDao.findChannelBillCount(startDate, endDate, channelId);
			//遍历差错池查看是否有渠道单边账(同一笔订单的支付和支出，并且同时为渠道单边账)
			if(orderMap != null && orderMap.size() > 0) {
				int size = 0;
				Set<String> orderIdSet = orderMap.keySet();
				for (String orderId : orderIdSet) {
					String obj = orderMap.get(orderId);
					String[] strArr = obj.split("_");
					if(channelId.equals(strArr[0])) {
						size++;
					}
				}
				channelBillCount = channelBillCount - size*2;
			}
			//3.his账单笔数
			int hisBillCount = billCheckDao.findHisBillCount(startDate, endDate, channelId);
			// 当日该渠道没有订单对账数据
			if(hisBillCount == 0 && channelBillCount == 0) {
				continue;
			}
			//4.已勾兑笔数
			int billCheck0Count = billCheckDao.findBillCheck0Count(startDate, endDate, channelId);
			//5.his单边账笔数
			int hisSingleBillCount = billCheckDao.findHisSingleBillCount(startDate, endDate, channelId);
			//6.渠道单边账笔数(商户)
			int channelSingleBillCount = billCheckDao.findChannelSingleBillCount(startDate, endDate, channelId);
			//7.金额不一致笔数
			int diffPriceBillCount = billCheckDao.findDiffPriceBillCount(startDate, endDate, channelId);
			//8.根据渠道号统计金额
			BillChannelRF bcPayRF = billCheckDao.findMoneySumByChannelId(startDate, endDate, channelId, 1);
			BillChannelRF bcRefundRF = billCheckDao.findMoneySumByChannelId(startDate, endDate, channelId, 2);
			
			BillChannelRF billChannelRF = new BillChannelRF();
			billChannelRF.setDate(date);
			billChannelRF.setChannelId(channelId);
			billChannelRF.setChannelName(channelName);
			billChannelRF.setHisBillCount(hisBillCount);
			billChannelRF.setChannelBillCount(channelBillCount);
			billChannelRF.setCheckCount(billCheck0Count);
			
			Long hisBillPaySum = (bcPayRF==null||bcPayRF.getHisBillSum()==null)?0L:bcPayRF.getHisBillSum();
			Long merchBillPaySum = (bcPayRF==null||bcPayRF.getMerchBillSum()==null)?0L:bcPayRF.getMerchBillSum();
			Long hisBillRefundSum = (bcRefundRF==null||bcRefundRF.getHisBillSum()==null)?0L:bcRefundRF.getHisBillSum();
			Long merchBillRefundSum = (bcRefundRF==null||bcRefundRF.getMerchBillSum()==null)?0L:bcRefundRF.getMerchBillSum();
			Long hisBillSum = hisBillPaySum - hisBillRefundSum;
			Long merchBillSum = merchBillPaySum - merchBillRefundSum;
			
			billChannelRF.setHisBillSum(hisBillSum);
			billChannelRF.setMerchBillSum(merchBillSum);
			billChannelRF.setHisSingleBillCount(hisSingleBillCount);
			billChannelRF.setChannelSingleBillCount(channelSingleBillCount);
			billChannelRF.setDifferPirceCount(diffPriceBillCount);
			if(hisBillSum.equals(merchBillSum)) {
				billChannelRF.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);  //账平
			}else {
				billChannelRF.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_0);  //账不平
				try {
					Integer result = billCheckDao.findMoneyIsWrong(startDate, endDate, null, channelId);
					if(result == 0) {
						billChannelRF.setCheckState(KstHosConstant.BILL_CHECK_RF_STATE_1);  //账平
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			billChannelRF.setCreateBy(KstHosConstant.SYSOPERATORID);  //默认系统自建
			billChannelRF.setCreateDate(checkDate);
			billChannelRF.setUpdateBy(KstHosConstant.SYSOPERATORID);
			billChannelRF.setUpdateDate(checkDate);
			
			billChannelRFList.add(billChannelRF);
		}
		return billChannelRFList;
	}
	
	/**
	 * 构建支付和退款统计金额的map
	 * 
	 * @param startDate
	 * @param endDate
	 * @param payMoneyMap
	 * @param refundMoneyMap
	 */
	private Map<String, BillMerchRF> buildBillMap(String startDate, String endDate, Integer billType) {
		List<BillMerchRF> payMoneyList = billCheckDao.findMoneySumByOrderType(startDate, endDate, billType);
		Map<String, BillMerchRF> map = new HashMap<>();
		if(payMoneyList != null) {
			payMoneyList.forEach((BillMerchRF billMerchRF) -> {
				map.put(billMerchRF.getConfigkey(), billMerchRF);
			});
		}
		return map;
	}
	
	/**
	 * 获取远程服务订单信息
	 * 
	 * @param orderId
	 * @param transationNo
	 * @param orderType
	 * @param payMoney
	 * @return
	 */
	private BillDetailVo getOrderDetail(String orderId, String refundOrderId) throws Exception{
		if(StringUtil.isBlank(orderId)) {
			return null;
		}
		BillDetailVo orderVo = new BillDetailVo();
		List<OrderVo> volist = orderMapper.findOrderCheck(orderId, null);
		if(volist != null && volist.size() > 0){
			OrderVo vo = volist.get(0);
			orderVo.setOrderId(vo.getOrderId());
			orderVo.setServiceId(vo.getServiceId());
		}else {
			String hosid = KasiteConfig.getOrgCode();
			IDownloadOutQLCOrder service = HandlerBuilder.get().getCallHisService(hosid, IDownloadOutQLCOrder.class);
			if(service != null) {
				Map<String,String> paramMap = new HashMap<String,String>(16);
				paramMap.put("refundOrderId", refundOrderId);
				InterfaceMessage msg1 = KasiteConfig.createJobInterfaceMsg(GenBillCheckJob.class, "queryRefundOrderInfo", 
						KasiteConfig.getOrgCode(), String.valueOf(UUID.randomUUID()),null, null);
				RespQueryOrderCheck resp = service.queryRefundOrderInfo(msg1, paramMap);
				orderVo.setOrderId(resp.getOrderId());
				orderVo.setServiceId(resp.getServiceId());
			}
		}
		return orderVo;
	}
}
