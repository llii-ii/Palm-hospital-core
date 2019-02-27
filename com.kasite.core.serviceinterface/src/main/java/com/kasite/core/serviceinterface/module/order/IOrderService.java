package com.kasite.core.serviceinterface.module.order;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.order.dbo.OrderHisInfo;
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
import com.kasite.core.serviceinterface.module.order.req.ReqQueryLocalOrderInfo;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderItemList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderPayState;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderProcess;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryOrderSubList;
import com.kasite.core.serviceinterface.module.order.req.ReqQueryRefundableOrderList;
import com.kasite.core.serviceinterface.module.order.req.ReqRevokeOrder;
import com.kasite.core.serviceinterface.module.order.req.ReqSyncLocalOrderState;
import com.kasite.core.serviceinterface.module.order.resp.RespGetOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespGetPayOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderDetailLocal;
import com.kasite.core.serviceinterface.module.order.resp.RespOrderLocalList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryLocalTransLogInfo;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderItemList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderProcess;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryOrderSubList;
import com.kasite.core.serviceinterface.module.order.resp.RespQueryQLCOrder;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface IOrderService {

	/**
	 * 获取单笔支付订单详情 未做任何业务判断直接根据id查询 O_PayOrder 表
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespGetPayOrder> getPayOrder(CommonReq<ReqGetOrder> req) throws Exception;;
	
	/**
	 * 根据订单号查询订单信息：直接查询O_Order 表 未做任何业务判断
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespGetOrder> getOrder(CommonReq<ReqGetOrder> req) throws Exception;
	/**
	 * Common:新增本地订单
	 * @Description 新增一条Order（订单）以及OrderExtension（订单扩展表）
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addOrderLocal(CommonReq<ReqAddOrderLocal> req) throws Exception;
	
	/**
	 * API:新增本地订单
	 * @Description 新增一条Order（订单）以及OrderExtension（订单扩展表）
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	String AddOrderLocal(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:查询本地订单详情 --查询视图 如果不是要判断业务状态／支付状态／退费状态  则不需要从这里判断 走 getOrder 就行
	 * @Description 新增本地订单
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespOrderLocalList> orderListLocal(CommonReq<ReqOrderListLocal> req) throws Exception;
	
	/**
	 * API:查询本地订单列表
	 * @Description 查询本地订单列表
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderListLocal(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:查询本地订单明细
	 * @Description 查询本地订单明细
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespOrderDetailLocal> orderDetailLocal(CommonReq<ReqOrderDetailLocal> req) throws Exception;
	
	/**
	 * API:查询本地订单明细
	 * @Description 查询本地订单明细
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderDetailLocal(InterfaceMessage msg)throws Exception;
	
	/**
	 * Common:订单正在支付;订单状态为：待付费的订单 --> 正在付费订单 ;新增O_PAYORDER并且状态为1 正在付费。
	 * @Description 只有未支付的订单才能调用此方法
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> orderIsPayment(CommonReq<ReqOrderIsPayment> req) throws Exception ;
	
	/**
	 * API:订单正在支付;订单状态为：待付费的订单 --> 正在付费订单 ;新增O_PAYORDER并且状态为1 正在付费。
	 * @Description 只有未支付的订单才能调用此API
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderIsPayment(InterfaceMessage msg) throws Exception ;
	
	/**
	 * Common:订单正在支付;订单状态为：待付费的订单 --> 正在付费订单 ;新增O_PAYORDER并且状态为1 正在付费。
	 * @Description 只有未支付的订单才能调用此方法
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> payForCompletion(CommonReq<ReqPayForCompletion> req) throws Exception ;

	/**
	 * API:订单支付完成回调 由Pay模块发起调用。 修改O_PAYORDER订单状态为2. 并更新交易流水号TransactionNo。
	 * @Description 只有未支付订单，或者正在支付的订单才能调用此API
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String PayForCompletion(InterfaceMessage msg)throws Exception;
	
	/**
	 * Common:业务完成之后，调用此接口新增O_BIZORDER，业务状态为1
	 * @Description 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> bizForCompletion(CommonReq<ReqBizForCompletion> req) throws Exception;
	
	/**
	 * API:业务完成之后，调用此接口新增O_BIZORDER，业务状态为1
	 * @Description 只有未支付订单，或者正在支付的订单才能调用此API
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String BizForCompletion(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:业务取消之后，调用此接口回写O_BIZORDER业务状态为2
	 * @Description 只有业务执行完成的订单，才能调用此接口
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> bizForCancel(CommonReq<ReqBizForCancel> req) throws Exception;
	
	/**
	 * API:业务取消之后，调用此接口回写O_BIZORDER业务状态为2
	 * @Description 只有业务执行完成的订单，才能调用此接口
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String BizForCancel(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:对已经支付的订单，进行退费。支持部分退费。
	 * @Description 1.判断退费金额是否满足条件（退费金额不能大于剩余金额） 
	 * 				2.判断支付状态是否满足条件（只有2、4才允许退费）
	 * 				3.新增O_REFUNDORDER 支付状态为3 
	 * 				4.调用pay的Refund退费接口
	 * 				5.成功，更新O_REFUNDORDER 支付状态为4
	 * 				6.失败，更新O_REFUNDORDER 支付状态为7
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> orderIsCancel(CommonReq<ReqOrderIsCancel> req) throws Exception;
	
	/**
	 * Common:对已经支付的订单，进行退费。支持部分退费。
	 * @Description 1.判断退费金额是否满足条件（退费金额不能大于剩余金额） 
	 * 				2.判断支付状态是否满足条件（只有2、4才允许退费）
	 * 				3.新增O_REFUNDORDER 支付状态为3 
	 * 				4.调用pay的Refund退费接口
	 * 				5.成功，更新O_REFUNDORDER 支付状态为4
	 * 				6.失败，更新O_REFUNDORDER 支付状态为7
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderIsCancel(InterfaceMessage msg) throws Exception;
	/**
	 * 福建医科大学附属第二医院当面付版本兼容接口。
	 * 如果没有使用 诗雷的当面付提供给医院调用的接口不要使用这个兼容版本接口 
	 * 统一使用 OrderIsCancel 接口
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderIsCancel_V1(InterfaceMessage msg) throws Exception;
	/**
	 * Common:退费成功回调
	 * @Description 更新O_REFUNDORDER 支付状态为4
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> cancelForCompletion(CommonReq<ReqCancelForCompletion> req)throws Exception;
	
	/**
	 * API:退费成功回调
	 * @Description 更新O_REFUNDORDER 支付状态为4
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String CancelForCompletion(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:取消未支付的订单，之后该订单不能进行任何操作。
	 * @Description 注意与OrderIsCancel接口的区别
	 * 				OrderIsCancel为退费。
	 * 				只有未支付的订单才能取消。
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> cancelOrder(CommonReq<ReqCancelOrder> req) throws Exception;
	
	/**
	 * API:取消未支付的订单，之后该订单不能进行任何操作。
	 * @Description 注意与OrderIsCancel接口的区别
	 * 				OrderIsCancel为退费。
	 * 				只有未支付的订单才能取消。
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String CancelOrder(InterfaceMessage msg) throws Exception;

	/**
	 * Common:撤销订单 该操作只能撤销未支付、支付中并且未执行HIS业务的订单
	 * @Description 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> revokeOrder(CommonReq<ReqRevokeOrder> req) throws Exception;
	
	/**
	 * API:撤销订单 该操作只能撤销未支付、支付中并且未执行HIS业务的订单
	 * @Description 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	String RevokeOrder(InterfaceMessage msg)throws Exception;

	/**
	 * Common:查询订单状态过程
	 * @Description 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryOrderProcess> queryOrderProcess(CommonReq<ReqQueryOrderProcess> req)throws Exception;
	
	/**
	 * API:查询订单状态过程
	 * @Description 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOrderProcess(InterfaceMessage msg)throws Exception;
	
	/**
	 * API:查询订单支付状态
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOrderState(InterfaceMessage msg)throws Exception;
	/**
	 * API:查询订单支付状态 兼容旧版本
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryOrderState_V1(InterfaceMessage msg)throws Exception;
	String QueryTuiFeiOrderState_V1(InterfaceMessage msg)throws Exception;
	/**
	 * Common:查询订单支付状态
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryOrderState(CommonReq<ReqQueryOrderPayState> commReq)throws Exception;
	
	/**
	 * 退款重试
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> refundOrderAgin(CommonReq<ReqOrderIsCancel> req) throws Exception;
	
	/**
	 * API:强制冲正订单的业务，只针对调用HIS多次失败的订单
	 * 逻辑：将订单的对应P_MERCHANT_NOTIFY，状态重置，然后启动线程异步调用后续的orderCallBack操作
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String ForceCorrectOrderBiz(InterfaceMessage msg)throws Exception;
	
	/**
	 * Common:强制冲正订单的业务，只针对调用HIS多次失败的订单
	 * 逻辑：将订单的对应P_MERCHANT_NOTIFY，状态重置，然后启动线程异步调用后续的orderCallBack操作
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> forceCorrectOrderBiz(CommonReq<ReqForceCorrectOrderBiz> commReq)throws Exception;

	/**
	 * API：订单外部业务状态通知
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String OrderOutBizNotify(InterfaceMessage msg)throws Exception;
	
	
	/**
	 * 同步与本地订单的状态。用于查询订单详情的时候调用。
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> syncLocalOrderState(CommonReq<ReqSyncLocalOrderState> req) throws Exception;

	/**
	 * 保存调用HIS接口结果  所有与HIS订单相关结果  
	 * @param vo
	 * @throws Exception
	 */
	void saveOrderHisInfo(OrderHisInfo vo) throws Exception;
	
	/**
	 * API:根据卡号/his唯一标识查询用户的可退金额列表，90天内的订单，超过90天的订单不显示
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryRefundableOrderList(InterfaceMessage msg)throws Exception;
	
	/**
	 * 兼容泉州儿童专用
	 * API:根据卡号/his唯一标识查询用户的可退金额列表，90天内的订单，超过90天的订单不显示
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryRefundableOrderList_V1(InterfaceMessage msg)throws Exception;
	
	/**
	 *  Common:根据卡号/his唯一标识查询用户的可退金额列表，90天内的订单，超过90天的订单不显示
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryRefundableOrderList(CommonReq<ReqQueryRefundableOrderList> commReq)throws Exception;
	
	/**
	 * 新增本地子订单
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addOrderSub(CommonReq<ReqAddOrderSub> req) throws Exception;
	
	/**
	 * 新增订单明细
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> addOrderItem(CommonReq<ReqAddOrderItem> req) throws Exception;

	
	/**
	 * 查询本地子订单
	 * @param req
	 * @return
	 * @throws Exception
	 */
	String QueryOrderSubList(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询本地子订单
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryOrderSubList> queryOrderSubList(CommonReq<ReqQueryOrderSubList> req) throws Exception;
	
	/**
	 * 查询his处理状态 0,冲正失败 1,订单业务完成  2,订单业务取消
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	int queryBizState(CommonReq<ReqSyncLocalOrderState> req) throws Exception;
	
	/**
	 * 查询交易日志信息
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryLocalTransLogInfo> queryTransLogInfo(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 查询本地的全流程订单(下载his账单用到)
	 * 
	 * @param orderId
	 * @param transationNo
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryQLCOrder> queryLocalQLCOrder(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	/**
	 * 查询本地的全流程退款订单(下载his账单用到)
	 * 
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespQueryQLCOrder> queryLocalRefundOrder(CommonReq<ReqQueryLocalOrderInfo> commReq) throws Exception;
	
	
	CommonResp<RespQueryOrderItemList> queryOrderItemList(CommonReq<ReqQueryOrderItemList> commReq) throws Exception;
}
