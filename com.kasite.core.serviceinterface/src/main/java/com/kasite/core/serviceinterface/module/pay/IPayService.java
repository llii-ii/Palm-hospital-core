package com.kasite.core.serviceinterface.module.pay;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.pay.req.ReqClose;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetPayQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqQueryMerchantRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.ReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.ReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.ReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.ReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.RespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.RespWapUniteOrder;
import com.yihu.hos.service.ICommonService;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * TODO
 */
public interface IPayService extends ICommonService{

	/**
	 * API:商户退款,支持部分退费，请慎重使用
	 * @Description 支持部分退费，请慎重使用
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String Refund(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:商户退款,支持部分退费，请慎重使用
	 * @Description 支持部分退费，请慎重使用
	 * @param reqRefund
	 * @return
	 * @throws Exception
	 */
	public CommonResp<RespRefund> refund(CommonReq<ReqRefund> reqRefund)throws Exception;
	
	/**
	 * API:商户统一下单
	 * @Description 商户统一下单
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String UniteOrder(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:商户统一下单
	 * @Description 商户统一下单
	 * @param commReq
	 * @return 
	 * @throws Exception
	 */
	CommonResp<RespUniteOrder> uniteOrder(CommonReq<ReqUniteOrder> commReq) throws Exception;
	
	/**
	 * API:当面付接口
	 * @Description 当面付接口
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String SweepCodePay(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:当面付接口
	 * @Description 当面付接口
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> sweepCodePay(CommonReq<ReqSweepCodePay> commReq) throws Exception;
	
	/**
	 * API:获取原生支付二维码
	 * @Description 获取原生支付二维码
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String GetPayQRCode(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:获取原生支付二维码
	 * @Description 获取原生支付二维码
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> getPayQRCode(CommonReq<ReqGetPayQRCode> commReq) throws Exception;
	
	/**
	 * API:撤销商户订单,如果订单已支付，则会调用退款接口
	 * @Description 撤销商户订单，如果订单已支付，则会调用退款接口
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String Revoke(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:撤销商户订单,如果订单已支付，则会调用退款接口
	 * @Description 撤销商户订单，如果订单已支付，则会调用退款接口
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> revoke(CommonReq<ReqRevoke> commReq) throws Exception;
	
	/**
	 * API:关闭商户订单,只允许关闭未支付的订单。关闭的商户订单，不允许再支付。已支付的订单关闭失败！
	 * @Description 只允许关闭未支付的订单。关闭的商户订单，不允许再支付。已支付的订单关闭失败！
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String Close(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:关闭商户订单,只允许关闭未支付的订单。关闭的商户订单，不允许再支付。已支付的订单关闭失败！
	 * @Description 只允许关闭未支付的订单。关闭的商户订单，不允许再支付。已支付的订单关闭失败！
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> close(CommonReq<ReqClose> commReq) throws Exception;
	
	/**
	 * Common:查询商户订单信息
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryMerchantOrder(CommonReq<ReqQueryMerchantOrder> commReq) throws Exception;
	
	/**
	 * Common:查询商户退款订单
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> queryMerchantRefund(CommonReq<ReqQueryMerchantRefund> commReq) throws Exception;
	
	/**
	 * API:H5/WAP支付-统一下单
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	String WapUniteOrder(InterfaceMessage msg) throws Exception;
	
	/**
	 * Common:H5/WAP支付-统一下单
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespWapUniteOrder> wapUniteOrder(CommonReq<ReqWapUniteOrder> commReq) throws Exception;
	
//	/**
//	 * 根据主键获取商户通知记录
//	 * @param commReq
//	 * @return
//	 * @throws Exception
//	 */
//	CommonResp<RespGetMerchantNotifyById> getMerchantNotifyById(CommonReq<ReqGetMerchantNotifyById> commReq) throws Exception;
//	
//	/**
//	 * 根据主键更新商户通知记录
//	 * @param commReq
//	 * @return
//	 * @throws Exception
//	 */
//	CommonResp<RespMap> updateMerchantNotifyById(CommonReq<ReqUpdateMerchantNotifyById> commReq) throws Exception;
//	
//	/**
//	 * 新增商户通知失败的记录
//	 * @param commReq
//	 * @return
//	 * @throws Exception
//	 */
//	CommonResp<RespMap> addMerchantNotifyFailure(CommonReq<ReqAddMerchantNotifyFailure> commReq) throws Exception;
	
}
