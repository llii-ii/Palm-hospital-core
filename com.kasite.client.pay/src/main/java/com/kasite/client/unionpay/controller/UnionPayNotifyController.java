package com.kasite.client.unionpay.controller;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.client.unionpay.constants.UnionPayConstant;
import com.kasite.client.unionpay.sdk.AcpService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.wsgw.api.InterfaceMessage;


@Controller
@RequestMapping(value = "/unionPay/{clientId}/{configKey}")
public class UnionPayNotifyController  extends AbstractController {
	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);

	@Autowired
	private PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/{openId}/{token}/{orderId}/payNotify.do")
	@ResponseBody
	@SysLog(value="unionPay_payNotify")
	public void payNotify(
			@PathVariable("configKey") String configKey, 
			@PathVariable("openId") String openId,
			@PathVariable("token") String token,
			@PathVariable("orderId") String orderId,
			@PathVariable("clientId") String clientId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//TODO 网关相关鉴权
		String uuid = "UnionPayNotify_payNotify_"+IDSeed.next();
		AuthInfoVo authInfo = createAuthInfoVo(uuid, token, getUser());
		
		try {
			String encoding = request.getParameter(UnionPayConstant.PARAM_ENCODING);
			// 获取银联通知服务器发送的后台通知参数
			Map<String, String> reqParam = getAllRequestParam(request);
			JSONObject paramJson = new JSONObject();
			paramJson.putAll(reqParam);
			LogUtil.info(log, new LogBody(authInfo).set("uuid", uuid).set("desc", "银联支付异步通知-收到通知报文").set(paramJson));
			Map<String, String> valideData = null;
			if (null != reqParam && !reqParam.isEmpty()) {
				Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
				valideData = new HashMap<String, String>(reqParam.size());
				while (it.hasNext()) {
					Entry<String, String> e = it.next();
					String key = (String) e.getKey();
					String value = (String) e.getValue();
					valideData.put(key, value);
				}
			}

			//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			if (!AcpService.validate(valideData, encoding,configKey)) {
				LogUtil.info(log, new LogBody(authInfo).set("uuid", uuid).set("desc", "银联支付异步通知-收到通知报文").set("验证签名结果","失败"));
				//验签失败，需解决验签问题
			} else {
				LogUtil.info(log, new LogBody(authInfo).set("uuid", uuid).set("desc", "银联支付异步通知-收到通知报文").set("验证签名结果","成功"));
				//【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态				
				orderId =valideData.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
				String respCode = valideData.get("respCode");
				String txnAmt = valideData.get("txnAmt");
				String queryId = valideData.get("queryId");
				String accNo = valideData.get("accNo");
				//判断respCode=00、A6后，对涉及资金类的交易，请再发起查询接口查询，确定交易成功后更新数据库。
				if( "00".equals(respCode)) {//交易成功
					InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(UnionPayNotifyController.class, "TenpayNotifyServlet", null, orderId, null, clientId, configKey,openId);
					payBackCallUtil.addPayNotify(msg,orderId, queryId, clientId,new Integer(txnAmt),configKey,accNo);
					//返回给银联服务器http 200  状态码
					response.getWriter().print("ok");
				}else {
					//交易失败，不做任何操作
					LogUtil.info(log, new LogBody(authInfo).set("uuid", uuid).set("desc", "银联支付异步通知-收到通知报文-交易失败").set("respCode",respCode));
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
			LogUtil.error(log, authInfo,e);
		}

	}
	
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
	
}
