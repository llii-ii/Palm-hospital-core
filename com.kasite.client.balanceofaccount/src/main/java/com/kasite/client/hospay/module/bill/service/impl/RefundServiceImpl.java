package com.kasite.client.hospay.module.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.coreframework.util.ArithUtil;
import com.kasite.client.hospay.common.constant.Constant;
import com.kasite.client.hospay.module.bill.dao.PayDao;
import com.kasite.client.hospay.module.bill.entity.bill.bo.RequestHandlerParam;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.NoticeLog;
import com.kasite.client.hospay.module.bill.entity.bill.dbo.ThreePartyBalance;
import com.kasite.client.hospay.module.bill.entity.bill.dto.QueryBillParam;
import com.kasite.client.hospay.module.bill.service.RefundService;
import com.kasite.client.hospay.module.bill.util.wxpay.ClientCustomSSL;
import com.kasite.client.hospay.module.bill.util.CommonUtils;
import com.kasite.client.hospay.module.bill.util.wxpay.TenPayUtil;
import com.kasite.client.hospay.module.bill.util.XmlParamUtils;
import com.kasite.client.hospay.module.bill.util.alipay.AlipayClientFactory;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.SortedMap;


/**
 * @author cc
 */
@Service
public class RefundServiceImpl implements RefundService {

     private static final Logger log = LoggerFactory.getLogger(RefundServiceImpl.class);

     @Autowired
     AlipayClientFactory alipayClientFactory;

     @Autowired
     RequestHandlerParam requestHandlerParam;

     @Autowired
     TenPayUtil tenPayUtil;

     @Autowired
     ClientCustomSSL clientCustomSSL;

     @Autowired
     PayDao payDao;

     public static void main(String[] args) {
          String price = "1000";
          System.out.println(ArithUtil.mul(price+"", "100"));
     }
     /*
      * 退费总入口(根据渠道ID进行区分)
      *
      * @param bills
      *
      * @return
      * @throws Exception
      */
     @Override
     public JSONObject refund(ThreePartyBalance bills) throws Exception {
          JSONObject result = null;
          if (Constant.WXCHANNELID.equals(bills.getChannelId())) {
               result = wxRefund(bills);
          } else if (Constant.ZFBCHANNELID.equals(bills.getChannelId())) {
               result = zfbRefund(bills);
          }
          return result;
     }

     /*
      * 支付宝退费接口
      * <p>
      * 参数 类型 是否必填 最大长度 描述 示例值
      * out_trade_no	String	特殊可选	64	订单支付时传入的商户订单号,不能和 trade_no同时为空。	20150320010101001
      * trade_no	String	特殊可选	64	支付宝交易号，和商户订单号不能同时为空	2014112611001004680073956707
      * refund_amount Price 必须 9 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数 200.12
      * refund_reason String 可选 256 退款的原因说明 正常退款
      * out_request_no	String	可选	64	标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。	HZ01RF001
      * operator_id String 可选 30 商户的操作员编号 OP001
      * store_id String 可选 32 商户的门店编号 NJ_S_001
      * terminal_id String 可选 32 商户的终端编号 NJ_T_001
      *
      * @param bills
      *
      * @return
      * @throws SQLException
      * @throws AlipayApiException
      */
     public JSONObject zfbRefund(ThreePartyBalance bills) throws SQLException, AlipayApiException {
          AlipayClient alipayClient = alipayClientFactory.init();
          AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();

          /* 商户订单号*/
          String outTradeNo = bills.getOrderId();
          /* 支付宝订单号*/
          String tradeNo = bills.getChannelOrderId();
          JSONObject bizContent = new JSONObject();
          if (!StringUtil.isEmpty(outTradeNo)) {
               bizContent.put("out_trade_no", outTradeNo);
          } else if (!StringUtil.isEmpty(tradeNo)) {
               bizContent.put("trade_no", tradeNo);
          }

          /* 订单退费金额入参单位分，调用支付宝退费单位元要转换 */
          /* 需要根据当前订单的类型判断退费时使用的金额是哪个字段
           *  QlcOrderState 为1时使用AlreadyReceivedMoney
           *  QlcOrderState 为2时使用RefundMoney*/
          String refundPrice;
          if (Constant.QLCCHANNELORDERTYPE.equals(bills.getQlcOrderState())){
               refundPrice = bills.getAlreadyReceivedMoney();
          }else {
               refundPrice = bills.getRefundMoney();
          }
          /* 订单退费金额入参单位分，调用支付宝退费单位元要转换 */
          bizContent.put("refund_amount", ArithUtil.div(refundPrice, "100"));
          /* 退费原因(由于目前是直接在对账后台进行的操作，所以这边先写死)*/
          bizContent.put("refund_reason", "对账平台后台操作!!!");
          /* 商户退费订单号 退款时新增这个节点参数作为退款唯一标识（refundOrderId）,对账时使用*/
          String outRefundNo = CommonUtils.getUUID();
          bizContent.put("out_request_no", outRefundNo);
          bizContent.put("operator_id", "");
          bizContent.put("store_id", "");
          bizContent.put("terminal_id", "");

          refundRequest.setBizContent(bizContent.toString());
          log.info("订单号为:{} 发起退款\r\n支付宝退款AlipayTradeRefund的BizContent入参{}\r\n", bills.getOrderId(), bizContent.toString());
          AlipayTradeRefundResponse response = alipayClient.execute(refundRequest);
          log.info("\r\n支付宝退款AlipayTradeRefund的出参:{}\r\n", response.getBody());

          //记录退款日志
          NoticeLog noticeLog = new NoticeLog();
          noticeLog.setNOTICE_ID(StringUtil.getUUID());
          //退款记录
          noticeLog.setOP_TYPE(3);
          noticeLog.setCHANEL_TYPE(2);
          noticeLog.setOP_TIME(DateOper.getNowDateTime());
          JSONObject retJson = new JSONObject();
          if (response.isSuccess()) {
               log.info("INFO[" + "申请支付宝退款成功|outRefundNo:" + outRefundNo + "]");
               // 退款成功的查询请求
               AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();
               bizContent.clear();
               bizContent.put("trade_no", response.getTradeNo());
               bizContent.put("out_trade_no", outRefundNo);
               bizContent.put("out_request_no", outRefundNo);
               refundQueryRequest.setBizContent(bizContent.toString());
               AlipayTradeFastpayRefundQueryResponse refundQueryResponse = alipayClient.execute(refundQueryRequest);
               if (refundQueryResponse.isSuccess() && outRefundNo.equals(refundQueryResponse.getOutRequestNo())) {
                    log.info("INFO[" + "查询支付宝退款成功|outRefundNo:" + outRefundNo + "]");
                    retJson.put("RefundId", outRefundNo);
                    retJson.put(Constant.RESPCODE, Constant.RET_10000);
                    retJson.put(Constant.RESPMESSAGE, "申请退款成功！");
                    noticeLog.setRESULT_CODE(refundQueryResponse.getCode());
                    noticeLog.setOUT_TRADE_NO(outRefundNo);
                    noticeLog.setTRANSACTION_ID(outTradeNo);
                    noticeLog.setFINAL_RESULT("申请退款成功!");
               } else {
                    log.info("INFO[" + "查询支付宝退款失败|outRefundNo:" + outRefundNo + "]");
                    noticeLog.setRESULT_CODE(response.getCode() + "|" + response.getSubCode());
                    noticeLog.setOUT_TRADE_NO(outRefundNo);
                    noticeLog.setTRANSACTION_ID(outTradeNo);
                    noticeLog.setFINAL_RESULT(response.getMsg() + "|" + response.getSubMsg());
                    retJson.put(Constant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
                    retJson.put(Constant.RESPMESSAGE, response.getMsg() + "|" + response.getSubMsg());
               }
          } else {
               noticeLog.setRESULT_CODE(response.getCode() + "|" + response.getSubCode());
               noticeLog.setOUT_TRADE_NO(outRefundNo);
               noticeLog.setTRANSACTION_ID(outTradeNo);
               retJson.put(Constant.RESPCODE, RetCode.Common.ERROR_SYSTEM.getCode());
               retJson.put(Constant.RESPMESSAGE, response.getMsg() + "|" + response.getSubMsg());
          }
          /* 保留支付宝的结果集,方便后续查询*/
          noticeLog.setMESSAGE(response.getBody());
          payDao.insertNoticeLog(noticeLog);
          return retJson;

     }

     /*
      * 调用微信退费接口
      * @return
      * @throws Exception
      */
     public JSONObject wxRefund(ThreePartyBalance bills) throws Exception {
          SortedMap<String, String> packageParams = tenPayUtil.initSortedMap();

          if(!StringUtil.isEmpty(bills.getOrderId())) {
               //商户单号
               packageParams.put("out_trade_no",bills.getOrderId() );
          }else if( !StringUtil.isEmpty(bills.getChannelOrderId())) {
               //微信订单号
               packageParams.put("transaction_id",bills.getChannelOrderId() );
          }

          packageParams.put("nonce_str", TenPayUtil.getNonceStr());
          //商户退款单号
          packageParams.put("out_refund_no", StringUtil.getUUID());

          /* 订单退费金额入参单位分，调用支付宝退费单位元要转换 */
          /* 需要根据当前订单的类型判断退费时使用的金额是哪个字段
           *  QlcOrderState 为1时使用AlreadyReceivedMoney
           *  QlcOrderState 为2时使用RefundMoney*/
          String refundPrice;
          if (Constant.QLCCHANNELORDERTYPE.equals(bills.getQlcOrderState())){
               refundPrice = bills.getAlreadyReceivedMoney()+"";
          }else {
               refundPrice = bills.getRefundMoney()+"";
          }
          //订单金额
          packageParams.put("total_fee", refundPrice);
          //退款金额
          packageParams.put("refund_fee", refundPrice);
          packageParams.put("op_user_id", requestHandlerParam.merchantId);

          String sign = tenPayUtil.createSign(packageParams);
          packageParams.put("sign", sign);
          //记录退款日志
          NoticeLog noticeLog = new NoticeLog();
          noticeLog.setNOTICE_ID(StringUtil.getUUID());
          //退款记录
          noticeLog.setOP_TYPE(3);
          noticeLog.setOP_TIME(DateOper.getNowDateTime());
          noticeLog.setCHANEL_TYPE(1);
          String arrayToXml = XmlParamUtils.arrayToXml(packageParams);
          noticeLog.setMESSAGE(arrayToXml);
          String returnXmlStr = "";

          log.info("调用微信退费接口入参XML:\r\n{}",arrayToXml);
          returnXmlStr = clientCustomSSL.call(arrayToXml);
          log.info("调用微信退费接口返回XML:\r\n{}",returnXmlStr);

          TenPayUtil.doParse(returnXmlStr);
          boolean flag = tenPayUtil.isValidWXSign();
          JSONObject retJson = new JSONObject();
          if(flag) {
               String returnCode = TenPayUtil.getParameter("return_code");
               String returnMsg =  TenPayUtil.getParameter("return_msg");
               String resultCode = TenPayUtil.getParameter("result_code").toString();
               if( Constant.RETURN_CODE_SUCCESS.equals(returnCode.trim())) {
                    if( Constant.RETURN_CODE_SUCCESS.equals(resultCode.trim())) {
                         String refundId = TenPayUtil.getParameter("refund_id").toString();
                         log.info("INFO[" + "申请微信退款成功|refund_id:\"{}\"]",refundId);
                         retJson.put("RefundId", refundId);
                         retJson.put(Constant.RESPCODE, Constant.RET_10000);
                         retJson.put(Constant.RESPMESSAGE, "申请退款成功！");
                         noticeLog.setRETURN_CODE(returnCode);
                         noticeLog.setRESULT_CODE(resultCode);
                         noticeLog.setOUT_TRADE_NO(refundId);
                         noticeLog.setTRANSACTION_ID(bills.getOrderId());
                         noticeLog.setFINAL_RESULT("申请退款成功!");
                    }else {
                         log.info("INFO[" + "退费申请失败|\"{}\"]",returnMsg);
                         String errorMsg = TenPayUtil.getParameter("err_code_des").toString();
                         noticeLog.setRETURN_CODE(returnCode);
                         noticeLog.setRESULT_CODE(resultCode);
                         noticeLog.setFINAL_RESULT(errorMsg);
                         noticeLog.setTRANSACTION_ID(bills.getOrderId());
                         retJson.put(Constant.RESPCODE, Constant.FAIL_10000);
                         retJson.put(Constant.RESPMESSAGE, errorMsg);
                    }
               }else {
                    log.info("INFO[ " + "退费申请失败|\"{}\"]",returnMsg );
                    String errorMsg = TenPayUtil.getParameter("err_code_des").toString();
                    noticeLog.setRETURN_CODE(returnCode);
                    noticeLog.setRESULT_CODE(resultCode);
                    noticeLog.setFINAL_RESULT(errorMsg);
                    noticeLog.setTRANSACTION_ID(bills.getOrderId());
                    retJson.put(Constant.RESPCODE,  Constant.FAIL_10000);
                    retJson.put(Constant.RESPMESSAGE, errorMsg);
               }
          }else {
               log.info("INFO[" + "微信签名验证失败" + "]");
               noticeLog.setRESULT_CODE("-3");
               noticeLog.setFINAL_RESULT("微信签名验证失败");
               retJson.put(Constant.RESPCODE, Constant.FAIL_10000);
               retJson.put(Constant.RESPMESSAGE, "微信签名验证失败");
          }
          /* 保留微信的结果集,方便后续查询*/
          noticeLog.setMESSAGE(returnXmlStr);
          payDao.insertNoticeLog(noticeLog);
          return retJson;
     }

     @Override
     public JSONObject queryOrder(QueryBillParam queryBillParam) throws JDOMException, IOException, ParseException, AlipayApiException {
          JSONObject result = null;
          if (Constant.WXCHANNELID.equals(queryBillParam.getChannelId())) {
               result = queryWXOrder(queryBillParam);
          } else if (Constant.ZFBCHANNELID.equals(queryBillParam.getChannelId())) {
               result = queryZFBOrder(queryBillParam);
          }
          return result;
     }

     /*
      * 查询支付宝订单详情
      * @param queryBillParam
      * @return
      */
	private JSONObject queryZFBOrder(QueryBillParam queryBillParam) throws ParseException, AlipayApiException {
		/* 初始化支付宝必须参数 */
		AlipayClient alipayClient = alipayClientFactory.init();
		AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
		JSONObject bizContent = new JSONObject();
		// 商户订单号
		bizContent.put("out_trade_no", queryBillParam.getOrderId());

		queryRequest.setBizContent(bizContent.toString());
		log.info("订单号为:{} 发起支付宝订单查询AlipayTradeQuery的BizContent入参{}", queryBillParam.getOrderId(),
				bizContent.toString());
		AlipayTradeQueryResponse response = alipayClient.execute(queryRequest);
		log.info("\r\n支付宝订单查询AlipayTradeQuery的出参:{}\r\n", response.getBody());

		// 记录退款日志
		NoticeLog noticeLog = new NoticeLog();
		noticeLog.setNOTICE_ID(StringUtil.getUUID());
		// 退款记录
		noticeLog.setOP_TYPE(3);
		noticeLog.setCHANEL_TYPE(2);
		noticeLog.setOP_TIME(DateOper.getNowDateTime());
		JSONObject retJson = new JSONObject();
		if (response.isSuccess() && queryBillParam.getOrderId().equals(response.getOutTradeNo())) {
			log.info("INFO[" + "支付宝订单查询成功|OutTradeNo:" + queryBillParam.getOrderId() + "]");
			if (response.getTradeStatus().equals("TRADE_SUCCESS")) {
				retJson.put(Constant.RESPCODE, Constant.RET_10000);
				retJson.put(Constant.RESPMESSAGE, "支付宝订单查询成功！!!");
			} else {
				retJson.put(Constant.RESPCODE, Constant.RET_10001);
				retJson.put(Constant.RESPMESSAGE, response.getTradeStatus());
			}

			retJson.put("orderId", response.getOutTradeNo());
			retJson.put("channelOrderId", response.getTradeNo());
			// 支付宝金额单位为元
			retJson.put("price", ArithUtil.mul(response.getTotalAmount(), "100"));

			noticeLog.setRESULT_CODE(response.getCode());
			noticeLog.setOUT_TRADE_NO(queryBillParam.getOrderId());
			noticeLog.setTRANSACTION_ID("");
			noticeLog.setFINAL_RESULT("支付宝订单查询成功!");
		} else {
			log.info("INFO[" + "支付宝订单查询失败|OutTradeNo:" + queryBillParam.getOrderId() + "]");
			noticeLog.setRESULT_CODE(response.getCode() + "|" + response.getSubCode());
			noticeLog.setOUT_TRADE_NO(queryBillParam.getOrderId());
			noticeLog.setTRANSACTION_ID("");
			noticeLog.setFINAL_RESULT(response.getMsg() + "|" + response.getSubMsg());
			retJson.put(Constant.RESPCODE, Constant.FAIL_10000);
			retJson.put(Constant.RESPMESSAGE, response.getMsg() + "|" + response.getSubMsg());
		}
		/* 保留支付宝的结果集,方便后续查询 */
		noticeLog.setMESSAGE(response.getBody());
		payDao.insertNoticeLog(noticeLog);
		return retJson;
	}


     /*
      * 查询微信订单详情
      * @param queryBillParam
      * @return
      * @throws JDOMException
      * @throws IOException
      */
     public JSONObject queryWXOrder(QueryBillParam queryBillParam) throws JDOMException, IOException {
          SortedMap<String, String> packageParams = tenPayUtil.initSortedMap();

          if(!StringUtil.isEmpty(queryBillParam.getOrderId())) {
               //商户单号
               packageParams.put("out_trade_no",queryBillParam.getOrderId() );
          }
          //随机字符串
          packageParams.put("nonce_str", TenPayUtil.getNonceStr());
          //公众账号ID
          packageParams.put("appid", requestHandlerParam.appId);
          //父商户号
          packageParams.put("mch_id", requestHandlerParam.parentMerchantId);
          //子商户号
          packageParams.put("sub_mch_id", requestHandlerParam.merchantId);
          //生成签名
          String sign = tenPayUtil.createSign(packageParams);
          packageParams.put("sign", sign);
          String arrayToXml = XmlParamUtils.arrayToXml(packageParams);
          //记录订单查询日志
          NoticeLog noticeLog = new NoticeLog();
          noticeLog.setNOTICE_ID(StringUtil.getUUID());
          //退款记录
          noticeLog.setOP_TYPE(3);
          noticeLog.setOP_TIME(DateOper.getNowDateTime());
          noticeLog.setCHANEL_TYPE(1);
          noticeLog.setMESSAGE(arrayToXml);
          //接口调用
          HttpRequstBusSender sender = HttpRequestBus.create(requestHandlerParam.queryOrderUrl, RequestType.post)
                  .contentType("application/json;charset=utf-8")
                  .setHeaderHttpParam("Accept-Encoding", "UTF-8")
                  .setParam(arrayToXml);
          String returnXmlStr = sender.send().getResult();
          log.info("查询微信订单返回参数:{}",returnXmlStr);
          TenPayUtil.doParse(returnXmlStr);
          boolean flag = tenPayUtil.isValidWXSign();
          JSONObject retJson = new JSONObject();
          if(flag) {
               String returnCode = TenPayUtil.getParameter("return_code");
               String returnMsg =  TenPayUtil.getParameter("return_msg");
               String resultCode = TenPayUtil.getParameter("result_code");
               if( Constant.RETURN_CODE_SUCCESS.equals(returnCode.trim())) {
                    if( Constant.RETURN_CODE_SUCCESS.equals(resultCode.trim())) {
                    	log.info("INFO[" + "查询订单详情成功|订单号是:{}]",queryBillParam.getOrderId());
                     
                    	if (Constant.RETURN_CODE_SUCCESS.equals(TenPayUtil.getParameter("trade_state"))) {
                    		retJson.put(Constant.RESPCODE, Constant.RET_10000);
                            retJson.put(Constant.RESPMESSAGE, "查询订单详情成功!!!");
						}else {
							retJson.put(Constant.RESPCODE, Constant.RET_10001);
	                        retJson.put(Constant.RESPMESSAGE,TenPayUtil.getParameter("trade_state_desc"));
						}
                        //全流程订单号
                        retJson.put("orderId",queryBillParam.getOrderId());
                        //交易流水号
                        retJson.put("channelOrderId", TenPayUtil.getParameter("transaction_id"));
                        //支付宝金额单位为元
                        retJson.put("price", TenPayUtil.getParameter("total_fee"));
                         noticeLog.setRETURN_CODE(returnCode);
                         noticeLog.setRESULT_CODE(resultCode);
                         noticeLog.setOUT_TRADE_NO(queryBillParam.getOrderId());
                         noticeLog.setTRANSACTION_ID(TenPayUtil.getParameter("transaction_id"));
                         noticeLog.setFINAL_RESULT("查询订单详情成功!!!");
                    }else {
                         log.info("INFO[" + "查询订单详情失败|{}]",returnMsg);
                         String errorMsg = TenPayUtil.getParameter("err_code_des").toString();
                         noticeLog.setRETURN_CODE(returnCode);
                         noticeLog.setRESULT_CODE(resultCode);
                         noticeLog.setFINAL_RESULT(errorMsg);
                         noticeLog.setOUT_TRADE_NO(queryBillParam.getOrderId());
                         retJson.put(Constant.RESPCODE, Constant.FAIL_10000);
                         retJson.put(Constant.RESPMESSAGE, errorMsg);
                    }
               }else {
                    log.info("INFO[ " + "查询订单详情失败|{}]",returnMsg );
                    String errorMsg = TenPayUtil.getParameter("err_code_des").toString();
                    noticeLog.setRETURN_CODE(returnCode);
                    noticeLog.setRESULT_CODE(resultCode);
                    noticeLog.setFINAL_RESULT(errorMsg);
                    noticeLog.setOUT_TRADE_NO(queryBillParam.getOrderId());
                    retJson.put(Constant.RESPCODE,  Constant.FAIL_10000);
                    retJson.put(Constant.RESPMESSAGE, errorMsg);
               }
          }else {
               log.info("INFO[" + "微信签名验证失败|订单号是:{}]",queryBillParam.getOrderId());
               noticeLog.setRESULT_CODE("-3");
               noticeLog.setFINAL_RESULT("微信签名验证失败");
               retJson.put(Constant.RESPCODE, Constant.FAIL_10000);
               retJson.put(Constant.RESPMESSAGE, "微信签名验证失败");
          }
          /* 保留微信的结果集,方便后续查询*/
          noticeLog.setMESSAGE(returnXmlStr);
          payDao.insertNoticeLog(noticeLog);
          return retJson;
     }

}
