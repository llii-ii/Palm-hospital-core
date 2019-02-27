package com.kasite.core.serviceinterface.module.order;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderRFList;
import com.kasite.core.serviceinterface.module.order.resp.RespPayOrderDetail;
import com.kasite.core.serviceinterface.module.order.resp.RespRefundOrderDetail;

/**
 * 智付后台的交易管理接口
 * 
 * @author zhaoy
 *
 */
public interface IOrderRFService {

	/**
	 * 查询交易明细列表数据(智付后台)
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespOrderRFList> queryOrderListLocal(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 统计交易金额和交易笔数
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryTotalCountMoney(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 查询交易汇总日报列表
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryTransCountList(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 支付的订单详情
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespPayOrderDetail> queryPayOrderDetail(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 退款的订单详情
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespRefundOrderDetail> queryRefundOrderDetail(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 订单列表数据下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadOrderListData(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 交易报表文件下载
	 * 
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> downloadOrderReportData(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
}
