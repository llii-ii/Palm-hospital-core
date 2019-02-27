package com.kasite.client.business.module.sys.controller;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.kasite.client.wechat.service.WeiXinService;
import com.kasite.client.zfb.constants.AlipayServiceEnvConstants;
import com.kasite.client.zfb.service.AlipayService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.ClientConfigEnum;
import com.kasite.core.common.config.DefaultClientEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteDiyConfig;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.resp.ApiKey;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.SendQVWarnUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.common.controller.AbsAddUserController;
import com.kasite.core.serviceinterface.module.basic.IBasicService;
import com.kasite.core.serviceinterface.module.basic.cache.BatUserCache;
import com.kasite.core.serviceinterface.module.basic.cache.IBatUserLocalCache;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddMember;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryMemberList;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryMemberList;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IDirectCardPayService;
import com.kasite.core.serviceinterface.module.his.resp.HisQueryEntityCardInfoResp;
import com.kasite.core.serviceinterface.module.order.IOrderService;
import com.kasite.core.serviceinterface.module.order.ISmartPayService;
import com.kasite.core.serviceinterface.module.order.req.ReqAddOrderLocal;
import com.kasite.core.serviceinterface.module.order.req.ReqGetOrder;
import com.kasite.core.serviceinterface.module.order.resp.RespGetOrder;
import com.kasite.core.serviceinterface.module.pay.GetPayConfigKeyUtil;
import com.kasite.core.serviceinterface.module.pay.req.ReqGetGuide;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;

/**
 * 扫码付入口
 * @author Administrator
 */
@Controller
public class WeChatSanPayController extends AbsAddUserController{
	public static final String ERRORPAGE= "/pageError.html?code={0}&msg={1}";
	@Autowired
	private ISmartPayService smartPayService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IBasicService basicService;
	
	private com.alibaba.fastjson.JSONObject getSanPayConfig(){
		String scanPayConfig = KasiteConfig.getDiyVal(SanPayDiyConfig.SanPayConfig);
		if(StringUtil.isNotBlank(scanPayConfig)) {
			return com.alibaba.fastjson.JSONObject.parseObject(scanPayConfig);
		}
		return null;
	}
	
	enum SanPayDiyConfig implements KasiteDiyConfig{
		SanPayConfig
		;
		@Override
		public String getNodeName() {
			// TODO Auto-generated method stub
			return name();
		}
	}
	
	/**
	 * 授权回调
	 * @throws Exception 
	 */
	@RequestMapping(value = "{clientId}/{guid}/qrPay.do")
	@ResponseBody
	@SysLog(value="扫码授权回调")
	public void miniPayOauthCallback(@PathVariable("clientId") String clientId,@PathVariable("guid") String guid,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(KstHosConstant.QR_USAGETYPE_1.toString().equals(clientId)) {
			clientId = KstHosConstant.MINIPAY_CHANNEL_ID;
		}else if(KstHosConstant.QR_USAGETYPE_2.toString().equals(clientId)) {
			clientId = KstHosConstant.WRISTBANDCODEPAY_CHANNEL_ID;
		}
		
		String toUrl = "/business/pay/intelligentPayMent.html";//默认收银台支付页面
		String toUrlParam = "?guid="+guid+"&clientId="+clientId;
		String errorcode = "";
    	String errormsg = "";
    	AuthInfoVo vo = null;
    	//TODO 判断是微信还是支付宝  通过配置获取 configKey
    	Boolean isWeChat = isWeChat(request);
    	if(null == isWeChat) {
    		errorcode = RetCode.Common.ERROR_TOKEN_INVOKE.getCode()+"";
    		errormsg = "请在支付宝或微信进行扫码操作！";
    		//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}
			sendRedirect(response,KasiteConfig.getKasiteHosWebAppUrl()+toUrl);
    		return;
    	}
    	
    	String configKey = "";
    	//改成通过 clientId 获取不同渠道对应的支付宝和微信的支付configKey
    	//取到对应的微信配置
		String authCode = null;
    	if(!isWeChat) {//支付宝
    		configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
    		if(StringUtil.isNotBlank(configKey)) {
				String[] cfs = configKey.split(",");
				configKey = cfs[0];
			}
    		vo = KasiteConfig.createAuthInfoVo(WeChatSanPayController.class);
    		vo.setConfigKey(configKey);
    		RespMap resp = GetPayConfigKeyUtil.get(clientId, guid, null, vo);
    		authCode = request.getParameter("auth_code");
    		if(null != resp && StringUtil.isNotBlank(resp.getString(ApiKey.GetPayConfigKey.ZfbConfigKey))) {
        		configKey = resp.getString(ApiKey.GetPayConfigKey.ZfbConfigKey);
    		}
    	}else if(isWeChat) {//微信
    		configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
    		authCode = request.getParameter("code");
    	}
    	
    	logger.info("configKey="+configKey+"&code="+authCode+"&clientId="+clientId+"&guid="+guid);
    	//判断是否已经微信回调
		if(StringUtil.isBlank(authCode)) {
			String times = request.getParameter("times");
			if(null == times) {
				times = "1";
			}else {
				Integer timesi = Integer.parseInt(times);
				if(timesi > 3) {
					throw new RRException("访问地址操过3次未取到微信的token");
				}
				times = (timesi + 1) + "";
			}
    		String redirectUri = KasiteConfig.getQrPayUrl(clientId,guid)+"?times="+times;//跳转到微信进行鉴权回调
    		if(isWeChat) {
        		gotoWeChatOauth(clientId, configKey,"snsapi_userinfo", redirectUri, request, response);
    		}else {
    			gotoAliOauth(clientId, configKey, redirectUri,request, response);
    		}
    		return;
		}
		try {
    		String openid = "";//微信openid。支付宝用户id
	    	//鉴权回调跳转
	    	if(isWeChat) {
    			//获取到用户微信相关信息
    			// 通过code换取网页授权access_token
				JSONObject json = WeiXinService.getAuthToken(authCode, configKey);
				if (json != null && StringUtil.isNotBlank(json.getString("openid"))) {
					openid = json.getString("openid");
					String access_token = json.getString("access_token");
			    	JSONObject userJs = WeiXinService.getSnsapiUserInfo(openid,access_token);//getUserInfo(openid, configKey);
					if(null != userJs) {
						vo = login(false, openid, clientId, configKey,null, request, response);
						//addWeChatUser(openid, vo, userJs);
					}else {
						throw new RRException("调用微信获取用户信息接口异常，请核实与微信端的请求是否正常。wechat_user_info");
					}
			    	
				}else {
					throw new RRException("未获取到微信用户信息 OpenId 请联系管理员。wechat_sns_oauth2_access_token");
				}
	    	}else {//支付宝
	    		AlipaySystemOauthTokenResponse tokenResp = AlipayService.getAuthTokenFromAlipay(configKey,authCode);
				if (tokenResp != null && StringUtils.isNotBlank(tokenResp.getUserId())) {
					openid = tokenResp.getUserId();
					//调用支付宝接口获取用户信息
					AlipayUserInfoShareResponse userResp = AlipayService.getUserInfo(configKey,openid,tokenResp.getAccessToken());
					//判断本地用户是否存在，不存在新增
					IBatUserLocalCache userLocalCache =  SpringContextUtil.getBean(IBatUserLocalCache.class);
					String zfbId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
					vo = login(false, openid, clientId, configKey, null,request, response);
					if (userResp.isSuccess()) {
						BatUserCache cache = new BatUserCache();
						cache.setOpenId(userResp.getUserId());
						cache.setNickName(java.net.URLEncoder.encode(
								null == userResp.getNickName() ? "" : userResp.getNickName(), "utf-8"));
						if (AlipayServiceEnvConstants.GENDER_M.equals(userResp.getGender())) {
							cache.setSex(1);
						} else if (AlipayServiceEnvConstants.GENDER_F.equals(userResp.getGender())) {
							cache.setSex(2);
						} else {
							cache.setSex(0);
						}
						cache.setCity(userResp.getCity());
						cache.setProvince(userResp.getProvince());
						cache.setSubscribe(1);
						cache.setRemark(userResp.getIsCertified());
						cache.setHeadImgUrl(userResp.getAvatar());
						cache.setSysId(userResp.getUserType());
						cache.setConfigKey(configKey);
						userLocalCache.put(openid, cache);
						addUser(openid, zfbId, vo, cache);
					}
				}
	    	}
	    	if(null == vo) {
	    		throw new RRException("未获取到用户的鉴权信息。");
	    	}
	    	InterfaceMessage msg = createInterfaceMsg(ApiModule.SmartPay.GetGuide, null, vo);
			//获取信息点信息后新增订单；
			CommonReq<ReqGetGuide> commReq = new CommonReq<ReqGetGuide>(new ReqGetGuide(msg,openid,guid));
			CommonResp<RespMap> guidMapResult = smartPayService.getGuide(commReq);
			if(KstHosConstant.SUCCESSCODE.equals(guidMapResult.getCode())) {//调用获取成功
				//判断二维码信息点用途类型
				RespMap respMap = guidMapResult.getResultData();
				Integer usageType = respMap.getInteger(ApiKey.GetGuide.USAGETYPE);
				String content = respMap.getString(ApiKey.GetGuide.Content);
				//获取信息点保存的数据
				com.alibaba.fastjson.JSONObject json = null;
				if(StringUtil.isNotBlank(content)) {
					json = com.alibaba.fastjson.JSONObject.parseObject(content); 
				}
				String orderId = null;
				if(null != json) {
					orderId = json.getString("orderId");
					if(StringUtil.isNotBlank(orderId)) {
						toUrlParam = toUrlParam + "&orderId="+orderId;
					}
				}
				if( KstHosConstant.QR_USAGETYPE_1.equals(usageType) ) {//二维码-迷你付
					//判断订单是否已经生成，如果生成订单则跳转到收银台。如果未生成订单
					String prescNo = json.getString("prescNo");
					//判断渠道ID是否是mini 付渠道 如果是的话则新增一条订单 否则直接跳转到收银台
					String createOrder = json.getString("createOrder"); 
					if(StringUtil.isNotBlank(createOrder) && createOrder.equals("true")) {
						CommonReq<ReqGetOrder> req = new CommonReq<ReqGetOrder>(new ReqGetOrder(msg, orderId));
						CommonResp<RespGetOrder> getOrderResp = orderService.getOrder(req);
						if(KstHosConstant.SUCCESSCODE.equals(getOrderResp.getCode())) {//调用获取成功
							RespGetOrder order = getOrderResp.getResultData();
							if(null == order) {//获取订单是否已经新增，如果已经新增则不继续新增
								Integer payMoney = json.getInteger("payMoney");
								Integer totalMoney = payMoney;
								String priceName = json.getString("priceName");
								String cardType = json.getString("cardType");
								String cardNo = json.getString("cardNo");
								String grudeClientId = json.getString("clientId");//信息点数据渠道id
//									String operatorId = json.getString("operatorId");
								String memberName = json.getString("memberName");
								String operatorName = json.getString("operatorName");
								String hisMemberId = json.getString("hisMemberId");
								String serviceId = json.getString("serviceId");
								Integer isOnlinePay = json.getInteger("isOnlinePay");
								Integer eqptType = json.getInteger("eqptType");
								ReqAddOrderLocal reqAddOrderLocal = new ReqAddOrderLocal(msg, 
										orderId,  prescNo, payMoney, totalMoney,
										priceName, cardNo, cardType, openid, operatorName, 
										serviceId, isOnlinePay, eqptType,null);
								reqAddOrderLocal.setChannelId(grudeClientId);
								reqAddOrderLocal.setMemberName(memberName);
								reqAddOrderLocal.setHisMemberId(hisMemberId);
								CommonReq<ReqAddOrderLocal> commReqAddOrder = new CommonReq<ReqAddOrderLocal>(reqAddOrderLocal);		
								CommonResp<RespMap> addOrderResp = orderService.addOrderLocal(commReqAddOrder);
								if(KstHosConstant.SUCCESSCODE.equals(addOrderResp.getCode())) {//调用获取成功  新增订单完成 跳转到收银台页面
									orderId = guidMapResult.getResultData().getString(ApiKey.AddOrderLocal.OrderId);
								}else {
									errorcode = addOrderResp.getCode();
									errormsg = addOrderResp.getMessage();
								}
							}
						}else {
							errorcode = getOrderResp.getCode();
							errormsg = getOrderResp.getMessage();
						}
					}
				}else if( KstHosConstant.QR_USAGETYPE_2.equals(usageType) ) {//二维码-腕带付
					toUrl = "/business/inHospital/hospitalNoRechargeQr.html";
				}else if( KstHosConstant.QR_USAGETYPE_3.equals(usageType) ) {//二维码-处方付
					toUrl = "/business/order/orderPrescriptionInfoQr.html";
				}else if( KstHosConstant.QR_USAGETYPE_4.equals(usageType) ) {//二维码-卡面付
					toUrl = "/business/outpatientDept/outpatientRechargeQr.html";
				}
				//判断是否有个性化扫码付页面
				com.alibaba.fastjson.JSONObject configJson = getSanPayConfig();
				if(null != configJson) {
					String diyToUrl = configJson.getString(clientId + "_" + usageType +"_toUrl");
					if(StringUtil.isNotBlank(diyToUrl)) {
						toUrl = diyToUrl;
					}
				}
				
				
			}else {
				errorcode = guidMapResult.getCode();
				errormsg = guidMapResult.getMessage();
			}
    	}catch (Exception e) {
    		e.printStackTrace();
    		if(e instanceof RRException){
        		RRException exp = (RRException) e;
        		errorcode = exp.getCode()+"";
        		errormsg = exp.getMessage();
        	}else {
        		errorcode = "500";
        		errormsg = "系统出现异常："+e.getMessage();
        	}
    		LogUtil.error(logger, e);
    		SendQVWarnUtil.sendWarnInfo(clientId, "扫码授权回调", DateOper.getNowDate().getTime(), e);
    		throw e;
		}finally {
			//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}else {
				toUrl  = toUrl + toUrlParam;
			}
			sendRedirect(response, KasiteConfig.getKasiteHosWebAppUrl()+toUrl);
		}
	}
	
	
	/**
	 * 直接卡面付-全流程/掌医不用预生成QR图片
	 * @param clientId
	 * @param guid
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/directCardPay/qrPay.do")
	@ResponseBody
	@SysLog(value="无预先生成二维码-卡面付")
	public void directCardPay(@RequestParam("cardId") String cardId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		AuthInfoVo authInfoVo = null;
		String clientId = DefaultClientEnum.cardfacecodepay.getClientId(); //默认卡面付渠道
		String toUrl = "/business/outpatientDept/outpatientRechargeQr.html";
		
		String errorcode = "";
    	String errormsg = "";
		Boolean isWeChat = isWeChat(request);
    	if(null == isWeChat) {
    		errorcode = RetCode.Common.ERROR_TOKEN_INVOKE.getCode()+"";
    		errormsg = "请在支付宝或微信进行扫码操作！";
    		//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}
			sendRedirect(response, KasiteConfig.getKasiteHosWebAppUrl()
					+"/business/commonError.html?errorCode="+errorcode+"&errorMsg="+errormsg);
    		return;
    	}
    	
    	String configKey = "";
    	//改成通过 clientId 获取不同渠道对应的支付宝和微信的支付configKey
    	//取到对应的微信配置
		String authCode = null;
    	if(!isWeChat) {//支付宝
    		configKey = KasiteConfig.getClientConfig(ClientConfigEnum.ZfbConfigKey,clientId);
    		if(StringUtil.isNotBlank(configKey)) {
				String[] cfs = configKey.split(",");
				configKey = cfs[0];
			}
    		authInfoVo = KasiteConfig.createAuthInfoVo(WeChatSanPayController.class);
    		authInfoVo.setConfigKey(configKey);
    		authCode = request.getParameter("auth_code");
    	}else if(isWeChat) {//微信
    		configKey = KasiteConfig.getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
    		authCode = request.getParameter("code");
    	}
    	
    	logger.info("configKey="+configKey+"&code="+authCode+"&clientId="+clientId);
    	//判断是否已经微信回调
		if(StringUtil.isBlank(authCode)) {
			String times = request.getParameter("times");
			if(null == times) {
				times = "1";
			}else {
				Integer timesi = Integer.parseInt(times);
				if(timesi > 3) {
					throw new RRException("访问地址操过3次未取到微信的token");
				}
				times = (timesi + 1) + "";
			}
    		String redirectUri =KasiteConfig.getKasiteHosWebAppUrl() + "/directCardPay/qrPay.do?times="+times+"&cardId="+cardId;//跳转到微信进行鉴权回调
    		if(isWeChat) {
        		gotoWeChatOauth(clientId, configKey,"snsapi_userinfo", redirectUri, request, response);
    		}else {
    			gotoAliOauth(clientId, configKey, redirectUri,request, response);
    		}
    		return;
		}
		try {
    		String openid = "";//微信openid。支付宝用户id
	    	//鉴权回调跳转
	    	if(isWeChat) {
    			//获取到用户微信相关信息
    			// 通过code换取网页授权access_token
				JSONObject json = WeiXinService.getAuthToken(authCode, configKey);
				if (json != null && StringUtil.isNotBlank(json.getString("openid"))) {
					openid = json.getString("openid");
					String access_token = json.getString("access_token");
			    	JSONObject userJs = WeiXinService.getSnsapiUserInfo(openid,access_token);//getUserInfo(openid, configKey);
					if(null != userJs) {
						authInfoVo = login(false, openid, clientId, configKey,null, request, response);
						addWeChatUser(openid, authInfoVo, userJs);
					}else {
						throw new RRException("调用微信获取用户信息接口异常，请核实与微信端的请求是否正常。wechat_user_info");
					}
			    	
				}else {
					throw new RRException("未获取到微信用户信息 OpenId 请联系管理员。wechat_sns_oauth2_access_token");
				}
	    	}else {//支付宝
	    		AlipaySystemOauthTokenResponse tokenResp = AlipayService.getAuthTokenFromAlipay(configKey,authCode);
				if (tokenResp != null && StringUtils.isNotBlank(tokenResp.getUserId())) {
					openid = tokenResp.getUserId();
					//调用支付宝接口获取用户信息
					AlipayUserInfoShareResponse userResp = AlipayService.getUserInfo(configKey,openid,tokenResp.getAccessToken());
					//判断本地用户是否存在，不存在新增
					IBatUserLocalCache userLocalCache =  SpringContextUtil.getBean(IBatUserLocalCache.class);
					String zfbId = KasiteConfig.getZfbConfig(ZFBConfigEnum.zfb_appId, configKey);
					authInfoVo = login(false, openid, clientId, configKey, null,request, response);
					if (userResp.isSuccess()) {
						BatUserCache cache = new BatUserCache();
						cache.setOpenId(userResp.getUserId());
						cache.setNickName(java.net.URLEncoder.encode(
								null == userResp.getNickName() ? "" : userResp.getNickName(), "utf-8"));
						if (AlipayServiceEnvConstants.GENDER_M.equals(userResp.getGender())) {
							cache.setSex(1);
						} else if (AlipayServiceEnvConstants.GENDER_F.equals(userResp.getGender())) {
							cache.setSex(2);
						} else {
							cache.setSex(0);
						}
						cache.setCity(userResp.getCity());
						cache.setProvince(userResp.getProvince());
						cache.setSubscribe(1);
						cache.setRemark(userResp.getIsCertified());
						cache.setHeadImgUrl(userResp.getAvatar());
						cache.setSysId(userResp.getUserType());
						cache.setConfigKey(configKey);
						userLocalCache.put(openid, cache);
						addUser(openid, zfbId, authInfoVo, cache);
					}
				}
	    	}
	    	if(null == authInfoVo) {
	    		throw new RRException("未获取到用户的鉴权信息。");
	    	}
	    	//直接查询HIS接口，查询该卡的信息
			IDirectCardPayService directCardPayService = HandlerBuilder.get().getCallHisService(authInfoVo,IDirectCardPayService.class);
			if(directCardPayService == null ) {
				throw new RRException("未实现IDirectCardPayService接口，请联系管理员！");
			}
			InterfaceMessage msg = createInterfaceMsg(ApiModule.His.queryEntityCardInfo, null, authInfoVo);
			Map<String,String> paramMap = new HashMap<String,String>(16);
			paramMap.put("cardId", cardId);
			HisQueryEntityCardInfoResp hisQueryEntityCardInfoResp = directCardPayService.queryEntityCardInfo(msg, paramMap).getDataCaseRetCode();
			String cardInfoJsonStr = com.alibaba.fastjson.JSONObject.toJSONString(hisQueryEntityCardInfoResp);
			toUrl+="?cardInfo="+URLEncoder.encode(cardInfoJsonStr, "UTF-8");
    	}catch (Exception e) {
    		e.printStackTrace();
    		if(e instanceof RRException){
        		RRException exp = (RRException) e;
        		errorcode = exp.getCode()+"";
        		errormsg = exp.getMessage();
        	}else {
        		errorcode = "500";
        		errormsg = "系统出现异常："+e.getMessage();
        	}
    		LogUtil.error(logger, e);
    		SendQVWarnUtil.sendWarnInfo(clientId, "无预先生成二维码-卡面付", DateOper.getNowDate().getTime(), e);
    		throw e;
		}finally {
			//如果异常调转到异常页面
			if(StringUtil.isNotBlank(errorcode)) {
				toUrl = MessageFormat.format(ERRORPAGE, errorcode,URLEncoder.encode(errormsg, "utf-8"));
			}
			sendRedirect(response, KasiteConfig.getKasiteHosWebAppUrl()+toUrl);
		}
	}
	
	
	public void autoBindUser(AuthInfoVo vo,
			String opId,String memberName, String idCardNo,String channelId,
			String cardNo,String cardType, String cardTypeName,
			String hisMemberId, String hospitalNo
			) throws Exception {
		InterfaceMessage msg = KasiteConfig.createJobInterfaceMsg(getClass(), ApiModule.Basic.QueryMemberCardList.getName(),null, vo);
		//判断用户是否有已经绑定这张卡 如果没有绑定则默认帮用户绑定
		ReqQueryMemberList queryMemberlist = new ReqQueryMemberList(msg, 
				null, cardNo, cardType, opId, channelId, hospitalNo, memberName, idCardNo, true);
		CommonReq<ReqQueryMemberList> reqComm1 = new CommonReq<ReqQueryMemberList>(queryMemberlist);
		CommonResp<RespQueryMemberList> resp = basicService.queryMemberCardList(reqComm1);
		List<RespQueryMemberList> list = resp.getListCaseRetCode();
		if(null != list) {
			for (RespQueryMemberList m : list) {
				String existCardNo = m.getCardNo();
				String existCardtype = m.getCardType();
				//判断卡是否有存在，不存在则绑定用户的卡
				if(null != existCardNo && existCardNo.equals(cardNo) && null != existCardtype && existCardtype.equals(cardType)) {
					return;
				}
				String hosno = m.getHospitalNo();
				if(null != hosno && hosno.equals(hospitalNo)) {
					return;
				}
			}
		}
		String hosId = KasiteConfig.getOrgCode();
		String mobile = null;
		String isVirtualCard = null;
		String birthDate = null;
		Integer sex = null;
		String address = null;
		String mcardNo = null;
		String birthNumber = null;
		Integer isChildren = null;
		String pCId = null;
		String code = null;
		String guardianName = null;
		Integer guardianSex = null;
		String guardianCertType = null;
		String guardianCertNum = idCardNo;
		if(StringUtil.isNotBlank(idCardNo)) {
			String certType =  "01";
			String certNum = idCardNo;
			msg.setApiName(ApiModule.Basic.AddMember.getName());
			ReqAddMember t = new ReqAddMember(msg, 
					opId, hosId, memberName, mobile, 
					idCardNo, birthDate, channelId, sex, 
					cardNo, cardType, cardTypeName, address, 
					mcardNo, birthNumber, isChildren, pCId, 
					code, hisMemberId, certType, certNum, 
					guardianName, guardianSex, guardianCertType, guardianCertNum, 
					isVirtualCard, hospitalNo);
			//不做卡有效校验。之前已经校验过了
			t.setIsValidateCard(false);
			CommonReq<ReqAddMember> reqComm = new CommonReq<ReqAddMember>(t);
			CommonResp<RespMap> respMapResult = basicService.addMember(reqComm);
			respMapResult.getDataCaseRetCode();
		}
	}
	
	
}
