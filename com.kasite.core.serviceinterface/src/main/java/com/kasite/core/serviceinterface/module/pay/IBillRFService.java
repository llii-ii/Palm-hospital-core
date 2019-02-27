package com.kasite.core.serviceinterface.module.pay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.order.req.ReqSynchroBill;
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

public interface IBillRFService{

	/**
	 * 对账明细日账单明细列表数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillForDate> queryBillList(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 异常账单明细列表数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillForDate> queryBillListForException(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 对账月统计报表
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillForMonth> queryBillRFListForMonth(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 统计账单笔数
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespBillCheckCount> queryBillCheckCount(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 统计异常账单笔数
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespExceptionBillCount> queryExceptionBillCount(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 对账统计报表(日报表)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillRFForDate> queryBillRFListForDate(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 获取交易场景-对账报表
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillChannelRF> queryBillChannelRF(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 获取支付方式-资金对账报表
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillMerchRF> queryBillMerchRF(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 获取最新报表日期
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryCurrentDate(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 银行勾兑列表
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBankMoneyCheck> queryBankMoneyCheckList(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 银行勾兑统计数据
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBankCheckCount> queryBankCheckCount(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 账单详情(未处理)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillDetail> queryBillDetail(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 账单详情(已处理)
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryBillDetail> queryDealBillDetail(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-对账明细账单下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadBillDetailListData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-异常账单明细下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadExceptionBillData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-银行勾兑账单下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadBankBillListData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-对账报表账单打包下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadBillReportListData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-对账统计账单下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadBillRFListData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-对账账单打包下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadAllBillFile(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 更新银行勾兑信息
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> saveBankCheckInfo(CommonReq<ReqUpdateBankCheckInfo> commReq) throws Exception;
	
	/**
	 * 异常账单-同步处理
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> checkBillSynchro(CommonReq<ReqSynchroBill> commReq) throws Exception;
	
	/**
	 * 商户单边账异常账单-冲正处理
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> checkBillReverse(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 长款账单-退款处理
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> checkBillRefund(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 短款账单-登账处理
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> billCheckIn(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 渠道下银行账号金额统计
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryChannelBankCheck> queryChannelBankCheckList(CommonReq<ReqQueryBill> commReq) throws Exception;
	
	/**
	 * 智付后台-渠道关联银行金额统计报表下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadChannelBankReportListData(CommonReq<ReqQueryBill> commReq) throws Exception;
	
}
