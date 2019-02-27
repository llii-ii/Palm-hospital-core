package com.kasite.core.serviceinterface.module.order;

import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.serviceinterface.module.order.req.ReqSweepCodePay;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePatientQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePayQRCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqCreatePrescriptionQrCode;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetGuide;
import com.kasite.core.serviceinterface.module.pay.req.ReqQuickPaymentQR;
import com.kasite.core.serviceinterface.module.pay.resp.RespQuickPaymentQR;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 智付接口
 * 
 * @author 無
 *
 */
public interface ISmartPayService {

	/**
	 * API:快速支付二维码。 新增全流程订单，然后调用pay模块的原生支付二维码<br/>
	 * 该二维码为商户原生的支付二维码，有时间限制，请在实时过程调研清楚需求。
	 * @param msg
	 * @return
	 */
	String QuickPaymentQR(InterfaceMessage msg) throws Exception;
	CommonResp<RespQuickPaymentQR> quickPaymentQR(CommonReq<ReqQuickPaymentQR> commReq) throws Exception;

	/**
	 * API:当面付扫码。新增全流程订单，然后调用pay模块的当面付接口
	 * @param msg
	 * @return
	 */
	String SweepCodePay(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> sweepCodePay(CommonReq<ReqSweepCodePay> commReq) throws Exception;
	/**
	 * API:当面付扫码。新增全流程订单，然后调用pay模块的当面付接口
	 * 针对福建医科大学兼容旧版
	 * @param msg
	 * @return
	 */
	String SweepCodePay_V1(InterfaceMessage msg) throws Exception;
	
	/**
	 * 生成患者二维码。 新增二维码信息，生成二维字符串。
	 * 
	 * @param msg
	 * @return
	 */
	String CreatePatientQRCode(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> createPatientQRCode(CommonReq<ReqCreatePatientQRCode> commReq) throws Exception;
	/**
	 * API:生成支付二维码。 新增本地订单，生成二维码。<br/>
	 * 该二维码为收银台二维码。金额已经预先生成好。无法修改。
	 * @param msg
	 * @return
	 */
	String CreatePayQRCode(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> createPayQRCode(CommonReq<ReqCreatePayQRCode> commReq)throws Exception;
	
	/**
	 * 获取二维码信息点信息。 根据二维码信息唯一ID，获取二维码信息数据
	 * 
	 * @param msg
	 * @return
	 */
	String GetGuide(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> getGuide(CommonReq<ReqGetGuide> commReq)throws Exception;
	
	/**
	 * 
	 * 创建处方付二维码
	 * @param msg
	 * @return
	 */
	String CreatePrescriptionQrCode(InterfaceMessage msg) throws Exception;
	CommonResp<RespMap> createPrescriptionQrCode(CommonReq<ReqCreatePrescriptionQrCode> commReq) throws Exception;
	
	/**
	 * 处方付场景前校验
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String PrescQrValidateBefore(InterfaceMessage msg) throws Exception;
	
	/**
	 * 查询用户可退余额(门诊)
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QueryMemberRefundableMoney(InterfaceMessage msg) throws Exception;
	
	/**
	 * 用户申请自助退款（门诊）
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String ApplySelfServiceRefund(InterfaceMessage msg) throws Exception; 
	
	/**
	 *  用户申请自助退款记录列表
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QuerySelfRefundRecordList(InterfaceMessage msg) throws Exception;
	
	/**
	 *   用户申请自助退款记录详情
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	String QuerySelfRefundRecordInfo(InterfaceMessage msg) throws Exception;
	/**
	 * 腕带付二维码生成
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	CommonResp<RespMap> createWristBandCodePay(CommonReq<ReqCreatePayQRCode> commReq) throws Exception;
	/**
	 * 腕带付二维码生成
	 * @param commReq
	 * @return
	 * @throws Exception
	 */
	String CreateWristBandCodePay(InterfaceMessage msg) throws Exception;
	
}
