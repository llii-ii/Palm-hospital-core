package com.kasite.client.swiftpass.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.client.pay.util.PayBackCallUtil;
import com.kasite.client.swiftpass.util.SwiftpassUtils;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.log.LogBody;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author linjf
 * 威富通前端控制类
 */
@Controller
@RequestMapping(value = "/swiftpass/{clientId}/{configKey}")
public class SwiftpassNotifyController extends AbstractController {

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_PAY);
	
	@Autowired
	private PayBackCallUtil payBackCallUtil;
	
	@RequestMapping(value = "/{openId}/{token}/{orderId}/payNotify.do")
	@ResponseBody
	public void payNotify(
			@PathVariable("configKey") String configKey, 
			@PathVariable("openId") String openId,
			@PathVariable("token") String token,
			@PathVariable("clientId") String clientId,
			@PathVariable("orderId") String orderId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String uuid = "SwiftpassController_payNotify_"+IDSeed.next();
			AuthInfoVo authInfo = createAuthInfoVo(uuid, token, getUser());
			authInfo.setSign(openId);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = SwiftpassUtils.parseRequst(request);
            LogUtil.info(log, new LogBody(authInfo).set("威富通异步通知入参", resString));
            
            ChannelTypeEnum channelTypeEnum = KasiteConfig.getChannelType(clientId,authInfo.getConfigKey());
            String channelId = KstHosConstant.WX_CHANNEL_ID;
            if (ChannelTypeEnum.wechat.equals(channelTypeEnum)) {
            	channelId = KstHosConstant.WX_CHANNEL_ID;
            }else if (ChannelTypeEnum.zfb.equals(channelTypeEnum)) {
            	channelId = KstHosConstant.ZFB_CHANNEL_ID;
    		} 
            String respString = "error";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = SwiftpassUtils.xml2Map(resString.getBytes(), "utf-8");
                if(map.containsKey("sign")){
                    if(!SwiftpassUtils.checkParam(map,KasiteConfig.getSwiftpass(SwiftpassEnum.key,authInfo.getConfigKey()))){
                        respString = "error";
                        LogUtil.warn(log, new LogBody(authInfo).set("威富通异步通知入参", "签名失败！"));
                    }else{
                        String status = map.get("status");
                        if(status != null && "0".equals(status)){
                            String result_code = map.get("result_code");
                            if(result_code != null && "0".equals(result_code)){
                            	String outTransactionId = map.get("out_transaction_id");
                            	String transactionId = map.get("transaction_id");
                            	Integer totalFee = new Integer(map.get("total_fee"));
                                InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(SwiftpassNotifyController.class, 
                                		"SwiftpassController", null, orderId, null, clientId, configKey,openId);
        						payBackCallUtil.addPayNotify(msg,outTransactionId, transactionId,channelId,totalFee,configKey);
        						// 调用订单状态更新接口
        						// 返回状态成功 交易结果成功
                                respString = "success";
                            }else {
                            	 LogUtil.warn(log, new LogBody(authInfo).set("威富通异步通知异常-result_code", result_code));
                            } 
                        }else {
                        	 LogUtil.warn(log, new LogBody(authInfo).set("威富通异步通知异常-status", status));
                        } 
                    }
                }
            }
            response.getWriter().write(respString);
        } catch (Exception e) {
           log.error("操作失败，原因：",e);
        }

	}
	
}
