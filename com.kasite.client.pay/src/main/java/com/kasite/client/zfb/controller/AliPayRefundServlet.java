//package com.kasite.client.zfb.controller;
//
//import java.io.UnsupportedEncodingException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.kasite.core.serviceinterface.common.controller.AbstractController;
//
//
///**
// * 2017.05.11 支付宝1.0支付接口，已弃用。
// * 
// * @author linjianfa
// *
// */
//@Controller
//@RequestMapping(value = "/alipay")
//public class AliPayRefundServlet  extends AbstractController {
//
//	/**
//	 * 支付宝后台支付通知处理
//	 */
//	private final static Log log = LogFactory.getLog(AliPayRefundServlet.class);
//
//	private static final long serialVersionUID = 1L;
//
//	private String logTag = "";
//
//	private String handleMsg = "";
//
//	@RequestMapping(value = "/{wxPayKey}/refundNotify.do")
//	@ResponseBody
//	public void payNotify(@PathVariable("wxPayKey") String wxPayKey, 
//			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//		// try {
//		// LogBody logBody = new LogBody();
//		// logBody.set("resp", "支付宝后台支付通知处理");
//		// Logger.get().info("Hos-Pay", logBody);
//		//
//		// configMap=ConfigUtil.getInstance().getPayCfg().get("zfb");
//		// AlipayConfig.key=configMap.get("merchantkey").toString();
//		// AlipayConfig.partner=configMap.get("merchantid").toString();
//		// AlipayConfig.sign_type="MD5";
//		// // 获取支付宝POST过来反馈信息
//		// Map<String, String> params = new HashMap<String, String>();
//		// Map requestParams = request.getParameterMap();
//		//
//		// for (Iterator iter = requestParams.keySet().iterator(); iter
//		// .hasNext();) {
//		// String name = (String) iter.next();
//		// String[] values = (String[]) requestParams.get(name);
//		// String valueStr = "";
//		// for (int i = 0; i < values.length; i++) {
//		// valueStr = (i == values.length - 1) ? valueStr + values[i]
//		// : valueStr + values[i] + ",";
//		// }
//		// // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//		// valueStr = new String(valueStr.getBytes("ISO-8859-1"),"gbk");
//		// params.put(name, valueStr);
//		//
//		// }
//		// logBody = new LogBody();
//		// logBody.set("req", "params00000 >>> "+params.toString());
//		// Logger.get().info("Hos-Pay", logBody);
//		//
//		// //vtemp =
//		// "service=alipay.wap.trade.create.direct,sign=d912e18eef5bade3eb47f68d1f8e48fc,sec_id=MD5,v=1.0,notify_data=<notify><payment_type>1</payment_type><subject>mzcz</subject><trade_no>2015061300001000440062341273</trade_no><buyer_email>276577647@qq.com</buyer_email><gmt_create>2015-06-13
//		// 09:47:41</gmt_create><notify_type>trade_status_sync</notify_type><quantity>1</quantity><out_trade_no>D5B026CC01884507B8BDA173FAD7EDB9</out_trade_no><notify_time>2015-06-13
//		// 11:11:54</notify_time><seller_id>2088911439111912</seller_id><trade_status>TRADE_SUCCESS</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.01</total_fee><gmt_payment>2015-06-13
//		// 09:47:42</gmt_payment><seller_email>qzfeyy@qzfeyy.com</seller_email><price>0.01</price><buyer_id>2088302098643443</buyer_id><notify_id>edb718012cb1e25599d106c91e761ded4g</notify_id><use_coupon>N</use_coupon></notify>";
//		// Map<String, String> paramsNew = new HashMap<String, String>();
//		// String ps = params.toString().replace(" ", "");
//		// String vtemp = ps.substring(1,ps.length()-1);
//		// String[] temparr = vtemp.split(",");
//		//
//		// if(temparr!=null&&temparr.length>0) {
//		// for(String node : temparr) {
//		// String[] arr = node.split("=");
//		// if("notify_time".equals(arr[0])){
//		// paramsNew.put(arr[0],DateOper.formatDate(arr[1],
//		// "yyyy-MM-ddHH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
//		// }else{
//		// paramsNew.put(arr[0], arr[1]);
//		// }
//		// }
//		// }
//		//
//		// String notifyContent=AlipayCore.createLinkString(paramsNew);
//		//
//		// logBody = new LogBody();
//		// logBody.set("resp", "[商户端接收支付宝退款通知]:" + notifyContent);
//		// Logger.get().info("Hos-Pay", logBody);
//		//
//		// log.info("[商户端接收支付宝退款通知]:" + notifyContent);
//		// NoticeLog noticeLog = new NoticeLog();
//		// noticeLog.setNOTICE_ID(StringUtil.getUUID());
//		// noticeLog.setOP_TYPE(1);
//		// noticeLog.setCHANEL_TYPE(2);
//		// noticeLog.setMESSAGE(notifyContent);
//		// if (configMap == null) {
//		// Logger.get().error("Hos-Pay", new ParamException("参数错误"));
//		// throw new ParamException("参数错误");
//		// }
//		//
//		// if("0001".equals(AlipayConfig.sign_type)) {
//		// ConfigUtil.print("sign_type="+AlipayConfig.sign_type);
//		// paramsNew = AlipayNotify.decrypt(paramsNew);
//		// }
//		//
//		//
//		// //退款批次号
//		// String outTradeNo = paramsNew.get("batch_no").toString();
//		// //商户订单号
//		// outTradeNo = outTradeNo.substring(8, outTradeNo.length());
//		//
//		// //退款结果明细 交易号^退款金额^处理结果
//		//
//		// String resultDetails = paramsNew.get("result_details").toString();
//		// String details[] = resultDetails.split("\\$");
//		// String trade[] = details[0].split("\\^");
//		// //支付宝单号
//		// String batchNo = trade[0];
//		// String price = trade[1];
//		// String tradeStatus = trade[2];
//		//
//		//
//		// logTag = "TAG[" + "out_trade_no:" + outTradeNo + "|"
//		// + "transaction_id:" + batchNo + "]";
//		//
//		// noticeLog.setOUT_TRADE_NO(outTradeNo);
//		// noticeLog.setTRANSACTION_ID(batchNo);
//		// noticeLog.setRETURN_CODE(tradeStatus);
//		//// noticeLog.setRESULT_CODE(trade_status);
//		//
//		// logTag = "TAG[" + "out_trade_no:" + outTradeNo + "|"
//		// + "transaction_id:" + batchNo + "]";
//		// logBody = new LogBody();
//		// logBody.set("resp","支付宝验证结果："+AlipayNotify.verifyReturn(paramsNew) );
//		// Logger.get().info("Hos-Pay", logBody);
//		//
//		// if (AlipayNotify.verifyReturn(paramsNew)) {// 验证成功
//		// // 请在这里加上商户的业务逻辑程序代码
//		// doPayLog("支付宝签名验证成功");
//		// logBody = new LogBody();
//		// logBody.set("info","支付宝签名验证成功" );
//		// logBody.set("trade_status>>>",tradeStatus );
//		// Logger.get().info("Hos-Pay", logBody);
//		// // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
//		// if ("SUCCESS".equals(tradeStatus)) {
//		//
//		// String resps = webServiceClient.cancelOrder(outTradeNo,
//		// batchNo,null,Convent.ZFB_CHANNELID);
//		//
//		// ConfigUtil.print(logTag);
//		//
//		// Document doc = XMLUtil.parseXml(resps);
//		// Element respElement = (Element) doc
//		// .selectSingleNode("/Resp");
//		//
//		// String respCode = XMLUtil.getString(respElement,
//		// "RespCode", true);// 返回状态
//		// String transactionCode = XMLUtil.getString(respElement,
//		// "TransactionCode", true);// 交易结果
//		//
//		// // 调用订单状态更新接口
//		// if (respCode.equals(ConstantList.InterfaceRespCode.SUCCESS)) {
//		// logBody = new LogBody();
//		// logBody.set("info","更新订单成功，通知支付宝" );
//		// Logger.get().info("Hos-Pay", logBody);
//		//
//		// doPayLog("更新订单成功");
//		// // 交易结果成功
//		// // 订单更新成功通知财付通 6002
//		// // log.info(logTag + "INFO[" + "更新订单成功" + "]");
//		// doPayLog("更新订单成功，通知支付宝");
//		// PrintWriter out = response.getWriter();
//		// out.println("success");
//		// out.flush();
//		// out.close();
//		// noticeLog.setRESULT_CODE("1");
//		// } else {
//		// logBody = new LogBody();
//		// logBody.set("info","更新订单失败" );
//		// Logger.get().error("Hos-Pay", logBody);
//		// doPayLog("更新订单失败");
//		// noticeLog.setRESULT_CODE("-1");
//		// }
//		// }
//		// } else {// 验证失败
//		// logBody = new LogBody();
//		// logBody.set("info","支付宝签名验证失败" );
//		// Logger.get().error("Hos-Pay", logBody);
//		// doPayLog("支付宝签名验证失败");
//		// noticeLog.setRESULT_CODE("-3");
//		// }
//		// noticeDao.insertNoticeLog(noticeLog);
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// log.error(logTag + "INFO[" + e.toString() + "]");
//		// }
//
//	}
//
//
//}
