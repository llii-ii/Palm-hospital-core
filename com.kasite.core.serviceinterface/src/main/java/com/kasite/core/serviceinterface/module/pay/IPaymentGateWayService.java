package com.kasite.core.serviceinterface.module.pay;

import java.util.Date;

import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqClose;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRefund;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqRevoke;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqUniteOrder;
import com.kasite.core.serviceinterface.module.pay.req.PgwReqWapUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespClose;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespDownloadBill;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespQueryRefundOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRefund;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespRevoke;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespUniteOrder;
import com.kasite.core.serviceinterface.module.pay.resp.PgwRespWapUniteOrder;

/**
 * @author linjf
 * 支付网关接口，负责实现对支付的渠道的封装
 */
public interface IPaymentGateWayService {

	/**
	 * 商户类型：默认取ChannelTypeEnum的值
	 * 注：威富通使用ChannelTypeEnum+swiftpass_mch_type
	 * @return
	 */
	String mchType();
	
	/**
	 * 商户统一下单接口
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	PgwRespUniteOrder uniteOrder(AuthInfoVo authInfoVo,PgwReqUniteOrder pgwReqUniteOrder) throws Exception;

	/**
	 * 商户退费接口
	 * @param authInfoVo
	 * @param pgwReqRefund
	 * @return
	 * @throws Exception
	 */
	PgwRespRefund refund(AuthInfoVo authInfoVo,PgwReqRefund pgwReqRefund)throws Exception;
	
	/**
	 * 商户扫码付接口
	 * @param authInfoVo
	 * @param pgwReqSweepCodePay
	 * @return
	 * @throws Exception
	 */
	PgwRespSweepCodePay sweepCodePay(AuthInfoVo authInfoVo,PgwReqSweepCodePay pgwReqSweepCodePay) throws Exception;
	
	/**
	 * 商户订单撤销接口
	 * @param authInfoVo
	 * @param pgwReqRevoke
	 * @return
	 * @throws Exception
	 */
	PgwRespRevoke revoke(AuthInfoVo authInfoVo,PgwReqRevoke pgwReqRevoke) throws Exception;
	
	/**
	 * 商户订单关闭接口
	 * @param authInfoVo
	 * @param pgwReqClose
	 * @return
	 * @throws Exception
	 */
	PgwRespClose close(AuthInfoVo authInfoVo,PgwReqClose pgwReqClose) throws Exception;
	
	/**
	 * WAP/H5商户查询支付订单接口
	 * @param authInfoVo
	 * @param pgwReqQueryOrder
	 * @return
	 * @throws Exception
	 */
	PgwRespQueryOrder queryOrder(AuthInfoVo authInfoVo,PgwReqQueryOrder pgwReqQueryOrder) throws Exception;
	
	/**
	 * 商户查询退费订单接口
	 * @param authInfoVo
	 * @param pgwReqQueryOrder
	 * @return
	 * @throws Exception
	 */
	PgwRespQueryRefundOrder queryRefundOrder(AuthInfoVo authInfoVo,PgwReqQueryRefundOrder pgwReqQueryRefundOrder) throws Exception;
	
	
	/**
	 * 商户H5/WAP统一下单接口
	 * @param authInfoVo
	 * @param pgwReqUniteOrder
	 * @return
	 * @throws Exception
	 */
	PgwRespWapUniteOrder wapUniteOrder(AuthInfoVo authInfoVo,PgwReqWapUniteOrder pgwReqUniteOrder) throws Exception;

//	PgwRespPayQRCode payQRCode(AuthInfoVo authInfoVo,PgwReqGetPayQRCode pgwReqGetPayQRCode) throws Exception;
	
	/**
	 * 商户下载账单
	 * @param configKey
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	PgwRespDownloadBill downloadBill(AuthInfoVo authInfoVo,Date billDate) throws Exception;
}
