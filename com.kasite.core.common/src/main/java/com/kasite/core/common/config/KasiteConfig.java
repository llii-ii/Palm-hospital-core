package com.kasite.core.common.config;

import java.io.File;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.kasite.core.common.bean.KasiteDataSourceConfig;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.handler.IWechatAndZfbConfigHandler;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.ParamException;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.log.NetworkUtil;
import com.kasite.core.common.service.BusinessTypeEnum;
import com.kasite.core.common.sys.KasiteSysApiEnum;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.service.ShiroService;
import com.kasite.core.common.sys.service.pojo.SysUserEntity;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.validator.Assert;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 配置文件设计原则
 * 
 * 保证配置清晰的情况下 不允许随意在 kasite.appConfig. 下新增4层配置 ，第四层配置留给渠道使用 即
 * kasite.appConfig.{clientId}.{xxx} 只要是4层的结构都认为 该配置的 第三层为渠道ID
 * 
 * 如果有需要可以在【kasite.】其它节点下配置相关信息 如日志 【kasite.log.esUrl】
 * 
 * @author daiyanshui
 *
 */
public class KasiteConfig extends Observable {
	// private static KasiteConfig inst = new KasiteConfig();
	// private boolean watch = false;
	private static Map<String, String> map = new HashMap<>();

	private static IWechatAndZfbConfigHandler handler;

	public static void setIWechatAndZfbConfigHandler(IWechatAndZfbConfigHandler handlero) {
		handler = handlero;
	}

	public static final String default_centerUrl = "https://verification.kasitesoft.com,http://verification2.kasitesoft.com";
	/**
	 * 位置支付回调地址配置 /wxPay/{clientId}/{configKey}/{openId}/{token}/{orderId}/payNotify.do
	 */
	private static final String WeChatPayNotifyUrl = "/wxPay/{0}/{1}/{2}/{3}/{4}/payNotify.do";

	/**
	 * 支付宝回调地址配置 /alipay/{clientId}/{configKey}/{openId}/{token}/payNotify.do
	 */
	private static final String ZfbPayNotifyUrl = "/alipay/{0}/{1}/{2}/{3}/{4}/payNotify.do";

	/**
	 * 银联回调地址配置 /unionPay/{clientId}/{configKey}/{openId}/{token}/payNotify.do
	 */
	private static final String UnionPayNotifyUrl = "/unionPay/{0}/{1}/{2}/{3}/{4}/payNotify.do";
	
	/**
	 *威富通回调地址配置 /swiftpass/{clientId}/{configKey}/{openId}/{token}/payNotify.do
	 */
	private static final String SwiftpassNotifyUrl = "/swiftpass/{0}/{1}/{2}/{3}/{4}/payNotify.do";
	
	/**
	 *威富通回调地址配置 /swiftpass/{clientId}/{configKey}/{openId}/{token}/payNotify.do
	 */
	private static final String netPayNotifyUrl = "/netPay/{0}/{1}/{2}/{3}/{4}/payNotify.do";
	
	/**
	 * 支付宝回调地址配置 /alipay/{clientId}/{configKey}/{openId}/{token}/payNotify.do
	 */
	private static final String UnionPayRefundNotifyUrl = "/unionPay/{0}/{1}/{2}/{3}/{4}/refundNotify.do";

	/**
	 * 微信鉴权回调地址 /weixin/{clientId}/{configKey}/gotoOauth.do
	 */
	private static final String WeChatOauthCallBackUrl = "/weixin/{0}/{1}/oauthCallback.do";

	private static final String WeChatOauthGotoOauthUrl = "/weixin/{0}/{1}/gotoOauth.do";
	/**
	 * 系统消息模版配置 
		String key = "MsgTmp.【clientId: 100123】.【消息类型:10101110】
		如：
		挂号成功已缴费【10101110】
		挂号成功未缴费【10101111】
		取消成功已退费【10101112】
		取消成功已关闭【10101113】
		。。。 具体看 KstHosConstant 常量类中定义的 MODETYPE 类型
		 */
	private static final String MSGTEMPKEY = "MsgTmp.{0}.{1}";
	
	/**
	 * 企业微信配置信息模版
	 */
	public static final String QYWECHATKEY = "kasite.QyWeChatConfig.{0}.{1}";
	
	/**
	 * 支付宝鉴权回调地址 /aliPay/{clientId}/{configKey}/gotoOauth.do
	 */
	private static final String ZfbOauthCallBackUrl = "/alipay/{0}/{1}/oauthCallback.do";

	private static final String ZfbOauthGotoOauthUrl = "/weixin/{0}/{1}/gotoOauth.do";
	
	private static final String qywxOauthGotoOauthUrl = "/qywechat/{0}/{1}/oauthCallback.do";
	
	private static final String GOOGLEAUTH_APPID = "googleauth.auth.appId";
	private static final String GOOGLEAUTH_APPSECRET = "googleauth.auth.appSecret";

	final static String RANDOM = "kasite.appConfig.random";
	final static String ID = "kasite.appConfig.id";
	final static String APPID = "kasite.appConfig.appId";
	final static String APPSECRET = "kasite.appConfig.appSecret";
	final static String LOGPATH = "kasite.appConfig.logPath";
	

	/**微信代理地址*/
	final static String PROXY_WECHAT = "kasite.appConfig.wechatProxy";
	/**微信支付代理地址*/
	final static String PROXY_WECHAT_PAY = "kasite.appConfig.wechatPayProxy";
	/**支付宝代理地址*/
	final static String PROXY_ZFB = "kasite.appConfig.zfbProxy";
	
	final static String ESURL = "kasite.log.esUrl";
	final static String ESLOG = "kasite.log.esLog";
	final static String ORGCODE = "kasite.appConfig.orgCode";
	final static String ORGNAME = "kasite.appConfig.orgName";
	//预约成功后的跳转地址  不要配置 前置目录 直接从 ／开始配置就行 如： "/business/yygh/yyghAppointmentDetails.html?orderId={0}";
	//kasite.appConfig.tempmsg_bussiness_【serviceId】 
	final static String TEMPMSG_BUSSINESS_ = "kasite.appConfig.tempmsg_bussiness_{0}";
	final static String CUSTOMLOG = "kasite.appConfig.customLog";
	final static String CENTERURL = "kasite.appConfig.centerUrl";
	final static String PUBLICKEY = "kasite.appConfig.publicKey";
	final static String ORDERMODEL = "kasite.appConfig.{0}.IsOnlinePay_ORDERTYPE_{1}";
	final static String ISSAVERESULT = "kasite.appConfig.{0}.CallHisResult_{1}";
	final static String CLIENTCONFIG = "kasite.appConfig.{0}.{1}";
//	final static String CLIENTCONFIG_APPINFO = "kasite.appConfig.{0}.{1}.{2}.{3}";// 保存渠道对应appId相关信息 指这个渠道下的应用ID信息

	/**
	 * 订单列表查询历史数据天数 默认 180 天
	 */
	final static String ORDERLISTDAYS = "kasite.appConfig.orderDays";
	/**
	 * 自定义参数
	 */
	final static String DIYCONFIG = "kasite.diy.{0}";
	/**
	 * 数据源配置（从云端读取的） 默认本地没有配置就走云端读取
	 */
	final static String DATASOURCECONFIG = "kasite.appConfig.dataSource";

//	final private static  String WechatPayNotifySign= "WechatPayNotifySign";
//	final private static  String WechatPayNotifyClientVersion = "1.0";
//	final private static  String ZfbPayNotifySign= "ZfbPayNotifySign";
//	final private static  String ZfbPayNotifyClientVersion = "1.0";
	/**
	 * 渠道ID列表
	 */
	private static Set<String> clientIds = new HashSet<>();

	/**
	 * sessionKey 默认的前坠
	 */
	public final static String AUTHINFO_TOKEN_TIME = "time_";
	public static KasiteConfig inst = new KasiteConfig();

	private KasiteConfig() {

	}
	/**
	 * 配置企业微信属性
	 */
	public static void setQyWeChatConfig(QyWeChatConfig config,String configKey,String value) {
		//"kasite.QyWeChatConfig.{0}.{1}"
		String key = MessageFormat.format(QYWECHATKEY, configKey,config.name());
		map.put(key, value);
	}
	/**
	 * 获取企业微信配置信息
	 * @param config
	 * @param configKey
	 * @return
	 */
	public static String getQyWeChatConfig(QyWeChatConfig config,String configKey) {
		String key = MessageFormat.format(QYWECHATKEY, configKey,config.name());
		return map.get(key);
	}
	
	public static void setConfigId(Integer id) {
		map.put(ID, id+"");
	}
	public static String getConfigId(boolean must) {
		String id = map.get(ID);
		if(StringUtil.isBlank(id)) {
			id = "0";
			if(must) {
				return String.format("%02d", Long.parseLong(id));
			}else {
				return null;
			}
		}
		return String.format("%02d", Long.parseLong(id));
	}
	public static String getConfigId() {
		return getConfigId(true);
	}
//	/**升序种子 当 达到 1000 的时候直接返回0 */
//	private static int randomNum = 0;
//	private static String timestr = "";
//	private static synchronized String getNum(String timeStr) {
//		if(randomNum >= 9999999 || !timestr.equals(timeStr)) {
//			randomNum = 0;
//		}
//		randomNum = randomNum + 1;
//		timestr = timeStr;
//		return ""+randomNum;
//	}
	public static void main(String[] args) throws ParseException, Exception {
//		for (int i = 0; i < 1000; i++) {
//			Thread.sleep(10);
//			System.out.println(getRandomId());
//		}
//		String jsonStr = "{\"100123_creditCardsAccepted\":\"true\",\"singleLimitStart\":0,\"singleLimitEnd\":2000,\"payTimeStart\":\"01:00\",\"payTimeEnd\":\"22:30\",\"creditCardsAccepted\":\"false\",\"refundTimeStart\":\"01:00\",\"refundTimeEnd\":\"22:00\"}";

//		 //接收事件服务器Token
//		 public static String evenToken ="cjU9pjqy079wLAXLfCBS2Bwfh";
//		 //接收事件服务器EncodingAESKey
//		 public static String encodingAESKey ="mOiVXt0M59ianUgOs6KU34NcUPzujiTJFBnF8srkoLP";
		//		JSONObject json = JSONObject.parseObject(jsonStr);
//		PayRule rule = JSON.toJavaObject(json, PayRule.class);
//		rule.setJson(json);
//		System.out.println(rule.getCreditCardsAcceptedByClientId("100123"));
//		[{"corpid":"123123","configKey":"cfgKey"}]
		/*

[{
"corpid":"wwda4e2c1313550d93","agentsecret":"AKTAj1ClLLjIZdY9-H7CO1zzLCOkg-Ynpl-IPPS90jk",
"contactssecret":"zzTujtKFQ4nytlstGVndPiB7rO9dFRLE-G-NUqK0lAU","agentid":"1000002",
"configkey":"QW001","eventoken":"cjU9pjqy079wLAXLfCBS2Bwfh","encodingaeskey":"mOiVXt0M59ianUgOs6KU34NcUPzujiTJFBnF8srkoLP"
}]
		 */
//		
//		jsonStr = "[{\"corpid\":\"123123\",\"configkey\":\"111cfgKey22\"},{\"corpid\":\"123123\",\"configkey\":\"cfgKey\"}]";
//		String text = jsonStr;
//		if(StringUtil.isNotBlank(text)) {
//			JSONArray diyJson = JSONArray.parseArray(text);
//			for (Object object : diyJson) {
//				JSONObject qywxJson = (JSONObject) object;
//				QyWeChatConfig[] enums = QyWeChatConfig.values();
//				String configKey = qywxJson.getString(QyWeChatConfig.configkey.name());
//				for (QyWeChatConfig e : enums) {
//					if(QyWeChatConfig.configkey != e) {
//						String value = qywxJson.getString(e.name());
//						KasiteConfig.setQyWeChatConfig(e, configKey, value);
//					}
//				}
//			}
//		}
//		System.out.println(KasiteConfig.print(true));
		String module = "asdf<asdf><\"x\"^723#{a#{_deptCode}sdf>as#{_a}df</>dfsdf###";
		JSONObject json = new JSONObject();
		json.put("_deptCode", "======");
		json.put("_a", "1xxx测试xx1");
		System.out.println(getCallModule(json, module));
		
	}
	public static String getRandomId() throws ParseException {
		return CommonUtil.getOrderNum();
//		String times = "";
//		try {
//			times = DateOper.getNow("yyyyMMddHHmmss");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		//当前服务的配置ID 默认0   如果多个实例从配置文件修改   + 当前应用在服务器上的配置ID + 升序的ID（自增100内）
//		StringBuffer sbf = new StringBuffer(getRandomStr()+getConfigId()+getNum(times));
//		sbf.append(times);
//		return sbf.toString();
	}
//	
//	private static String getRandomStr() {
//		String v = map.get(RANDOM);
//		if(StringUtil.isBlank(v)) {
//			v ="A";
//		}
//		return v;
//	}
	/**
	 * 设置diy参数
	 * @param key
	 * @param value
	 */
	public static void setDiyParam(Properties props ,String key,String value) {
		String k = MessageFormat.format(DIYCONFIG, key);
		props.put(k, value);
		map.put(k, value);
	}
	/**
	 * 获取自定义医院对应的配置信息
	 * 尽量不要做个性化自定义配置。如果需要自定义配置
	 * @param key
	 * @return
	 */
	public static String getDiyVal(KasiteDiyConfig key) {
		String k = MessageFormat.format(DIYCONFIG, key.getNodeName());
		return map.get(k);
	}
	
	/**
	 * 获取自定义医院对应的配置信息
	 * 尽量不要做个性化自定义配置。如果需要自定义配置
	 * @param key
	 * @return
	 */
	public static String getDiyVal(String key) {
		String k = MessageFormat.format(DIYCONFIG, key);
		return map.get(k);
	}
	/**
	 * 获取自定义医院对应的配置信息
	 * 尽量不要做个性化自定义配置。如果需要自定义配置
	 * @param key
	 * @return
	 */
	public static JSONObject getWebUiDiy() {
		String k = MessageFormat.format(DIYCONFIG, "WEB_UI_DIY");
		String str = map.get(k);
		if(StringUtil.isNotBlank(str)) {
			return JSONObject.parseObject(str);
		}
		return new JSONObject();
	}
	/**
	 * 获取退款限制天数 即 允许退几天前的款。超过的到人工柜台进行退款。
	 * ！！！注意 这里 跟前端UI不是同一个业务逻辑 前端只是限制UI显示  这里是限制所有的接口
	 * @return
	 */
	public static String getSelfRefundLimitDateDiy() {
		String k = MessageFormat.format(DIYCONFIG, "selfRefundLimitDate");
		String selfRefundLimitDateDiy = map.get(k);
		String selfRefundLimitDate = "90";//用户自助退默认90天的订单
		if(!StringUtil.isEmpty(selfRefundLimitDateDiy) ) {//diy配置不为空
			Integer diyInt = new Integer(selfRefundLimitDateDiy);
			if( diyInt.intValue()>0) {//diy配置大于0
				selfRefundLimitDate = selfRefundLimitDateDiy;
			}
		}
		return selfRefundLimitDate;
	}
	/**
	 * 验证码是否开通
	 * @return
	 */
	public static boolean isCheckPorvingCode() {
		JSONObject obj = getWebUiDiy();
		if(null != obj) {
			if(null != obj.getBoolean("checkPorvingCode")) {
				return obj.getBoolean("checkPorvingCode");
			}
		}
		return true;
	}
	
	/**
	 * 获取ES配置是否启动ES默认是 true
	 * @return
	 */
	public static boolean getESLog() {
		String v = getValue(ESLOG);
		if (StringUtil.isBlank(v)) {
			return true;
		}
		return Boolean.parseBoolean(v);
	}
	/**
	 * 获取 ES url 地址
	 * 
	 * @return
	 */
	public static String getESUrl() {
		String v = map.get(ESURL);
		if (StringUtil.isBlank(v)) {
			return "127.0.0.1:9200";
		}
		return v;
	}

	/**
	 * 获取谷歌验证的账号
	 * 
	 * @return
	 */
	public static String getGoogleAuthAppId() {
		return map.get(GOOGLEAUTH_APPID);
	}

	/**
	 * 获取谷歌验证的密钥
	 * 
	 * @return
	 */
	public static String getGoogleAuthAppSecret() {
		return map.get(GOOGLEAUTH_APPSECRET);
	}

	public String getOpenWhiteList() {
		return map.get(KASITE_OPENWHITELIST);
	}
	/**
	 * 判断渠道是否需要调用his接口
	 * 
	 * @return
	 */
	public static boolean caseChannelIsCallHis(String clientId, String configKey, String serviceId) {
		//当面付支付成功后做判断统一不调用HIS
		if ("sweepcodepay".equals(clientId)){
			return false;
		}
		//挂号业务需要调用HIS接口
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_0.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_005.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_006.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_007.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_002.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_008.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_003.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_004.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_009.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_001.getCode())) {
			return true;
		}
		if(serviceId.equals(BusinessTypeEnum.ORDERTYPE_011.getCode())) {
			return true;
		}
		// 默认不调用
		return false;
	}

	/**
	 * 新增渠道ID
	 * 
	 * @param clientId
	 */
	public static void addClientId(String clientId) {
		clientIds.add(clientId);
	}

	public static void addAppObserver(Observer o) {
		inst.addObserver(o);
	}
	/**
	 * 获取渠道对应的接口api配置信息
	 * @param clientId
	 * @return
	 */
	public static ClientApiConfig getClientApiConfig(String clientId) {
		String apiConfig = getClientConfig(ClientConfigEnum.apiConfig, clientId);
		if(StringUtil.isNotBlank(apiConfig)) {
			JSONObject json = JSON.parseObject(apiConfig);
			return JSON.toJavaObject(json, ClientApiConfig.class);
		}
		return null;
	}

	/**
	 * 获取渠道对应的ip白名单  在此名单内的ip才可以访问接口
	 * @param clientId
	 * @return
	 */
	public static Set<String> getClientIpWhite(String clientId) {
		String ipWhiteList = getClientConfig(ClientConfigEnum.ipWhiteList, clientId);
		if(StringUtil.isNotBlank(ipWhiteList)) {
			JSONArray array = JSON.parseArray(ipWhiteList);
			Set<String> sets = new HashSet<>(array.size());
			for (Object object : array) {
				sets.add(object.toString());
			}
			return sets;
		}
		return null;
	}
	/**
	 * 获取渠道对应的配置信息
	 * 
	 * @return
	 */
	public static String getClientConfig(ClientConfigEnum key, String clientId) {
		String v = map.get(MessageFormat.format(CLIENTCONFIG, clientId, key.name()));
		if(StringUtil.isBlank(v)) {
			//配置默认值
			switch (key) {
			//渠道开关默认是开通的
			case isOpen: 
				return "true";
			case isOpenYYClinicCard:
				return "false";
			default:
				break;
			}
		}
		return v;
	}
//
//	private static void setValue(String key, AppInfoEnum pkey, Properties p, Map<String, String> map) {
//		Object o = p.get(pkey.name());
//		map.put(key, o.toString());
//	}
//	/**
//	 * 初始化渠道对应的配置信息
//	 * 
//	 * @param appId
//	 * @param p
//	 */
//	public static void initClientOauthInfo(String appId, Properties p) {
//		// 将渠道鉴权相关的信息写入到本地缓存
//		String mapKey = null;
//		String mapKey2 = null;
//		String mapKey3 = null;
//		Map<String, String> tmap = new HashMap<>();
//
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			String key = entry.getKey();
//			if (key.startsWith("kasite.appConfig.")) {
//				String[] strs = key.split("\\.");
//				if (strs.length == 4) {
//					String clientId = strs[2];
//					String clientAppId = map
//							.get(MessageFormat.format(CLIENTCONFIG, clientId, ClientConfigEnum.clientAppId.name()));
//					// 渠道 appId的数量 kasite.appConfig.{clientId}.clientAppId = appId-A,appId-B
//					if (null != clientAppId && clientAppId.indexOf(appId + ",") >= 0) {
//						// 渠道信息保存的键 kasite.appConfig.{clientId}.clientAppId.{appId}.AppName
//						mapKey = MessageFormat.format(CLIENTCONFIG_APPINFO, clientId,
//								ClientConfigEnum.clientAppName.name(), appId, AppInfoEnum.AppName.name());
//						mapKey2 = MessageFormat.format(CLIENTCONFIG_APPINFO, clientId,
//								ClientConfigEnum.clientPrivateKey.name(), appId, AppInfoEnum.PrivateKey.name());
//						mapKey3 = MessageFormat.format(CLIENTCONFIG_APPINFO, clientId,
//								ClientConfigEnum.clientSecret.name(), appId, AppInfoEnum.AppSecret.name());
//						if (null != mapKey)
//							setValue(mapKey, AppInfoEnum.AppName, p, tmap);
//						if (null != mapKey2)
//							setValue(mapKey2, AppInfoEnum.PrivateKey, p, tmap);
//						if (null != mapKey3)
//							setValue(mapKey3, AppInfoEnum.AppSecret, p, tmap);
//						continue;
//					}
//				}
//			}
//		}
//		for (Map.Entry<String, String> e : tmap.entrySet()) {
//			String k = e.getKey();
//			String v = e.getValue();
//			setValue(k, v);
//		}
//	}

	/**
	 * 返回系统支持的支付方式列表
	 * 
	 * @return
	 */
	public static ChannelTypeEnum[] getPayChannelTypes() {
		return ChannelTypeEnum.values();
	}

	/**
	 * 获取渠道ID列表
	 * 
	 * @return
	 */
	public static Set<String[]> getClientIds() {
		Map<String, Map<ClientConfigEnum, String>> maps = getAllClientConfig();
		Set<String[]> clientids = new HashSet<>();
		for (Map.Entry<String, Map<ClientConfigEnum, String>> e : maps.entrySet()) {
			String[] ss = new String[3];
			ss[0] = e.getKey();
			ss[1] = e.getValue().get(ClientConfigEnum.clientName);
			ss[2] = StringUtil.isEmpty(e.getValue().get(ClientConfigEnum.isOpen))?"true":e.getValue().get(ClientConfigEnum.isOpen);
			clientids.add(ss);
		}
		return clientids;
	}

	/**
	 * 获取渠道对应的配置信息 第一个map 的key = clientId value = map map的key = ClientConfigEnum
	 * 
	 * @return
	 */
	public static Map<String, Map<ClientConfigEnum, String>> getAllClientConfig() {
		Map<String, Map<ClientConfigEnum, String>> retMap = new HashMap<>();
		ClientConfigEnum[] cces = ClientConfigEnum.values();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			// static String CLIENTCONFIG ="kasite.appConfig.{0}.{1}";
			// kasite.appConfig.100123.AppId=123123
			if (key.startsWith("kasite.appConfig.")) { 
				String[] strs = key.split("\\.");
				if (strs.length == 4) {
					String k = strs[3];
					String clientId = strs[2];
					Map<ClientConfigEnum, String> clientV = retMap.get(clientId);
					if (null == clientV) {
						clientV = new HashMap<>();
						retMap.put(clientId, clientV);
					}
					for (ClientConfigEnum c : cces) {
						if (c.name().equals(k)) {
							clientV.put(c, value);
						}
					}
				}
			}
		}
		return retMap;
	}

	/**
	 * 判断该渠道是否支持 对应的支付配置信息 如果有这个渠道有配置此支付路径
	 * 
	 * @return
	 */
	public static boolean existClientPayConfig(String clientId, String configKey) {
		ClientConfigEnum[] ems = ClientConfigEnum.values();
		for (ClientConfigEnum em : ems) {
			boolean isPayChannnel = em.isPayChannel();
			if (isPayChannnel) {
				String value = map.get(MessageFormat.format(CLIENTCONFIG, clientId, em.name()));
				if(null != value ) {
					String[] values = value.split(",");
					if(values.length > 1) {
						for (String string : values) {
							if(string.equals(configKey)) {
								return true;
							}
						}
					}else if (value.equals(configKey)) {
						return true;
					}
				}
				
				
			}
		}
		return false;
	}

	private static Map<String, ChannelTypeEnum> configKeyMap = new HashMap<>();
	
	/**
	 * 根据configKey查询支付方式
	 * 
	 * @param configKey
	 * @param channelId
	 * @return
	 */
	public static ChannelTypeEnum getPayTypeByConfigKey(String configKey) {
		ChannelTypeEnum t = configKeyMap.get(configKey);
		if(null == t) {
			Set<String[]> clientids = getClientIds();
			for (String[] strings : clientids) {
				String clientId = strings[0];
				String wxConfigKey = getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
				String weChatConfigKey = getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
				String zfbConfigKey = getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
				String unionPayConfigKey = getClientConfig(ClientConfigEnum.UnionPayConfigKey, clientId);
				String netPayConfigKey = getClientConfig(ClientConfigEnum.NetPayConfigKey, clientId);
				String swiftpassConfigKey = getClientConfig(ClientConfigEnum.SwiftpassConfigKey, clientId);
				if(StringUtil.isNotBlank(wxConfigKey)) {
					String[] wxArr = wxConfigKey.split(",");
					for (int i = 0; i < wxArr.length; i++) {
						configKeyMap.put(wxArr[i], ChannelTypeEnum.wechat);
					}
				}
				if(StringUtil.isNotBlank(weChatConfigKey)) {
					String[] wxArr = weChatConfigKey.split(",");
					for (int i = 0; i < wxArr.length; i++) {
						configKeyMap.put(wxArr[i], ChannelTypeEnum.wechat);
					}
				}
				if(StringUtil.isNotBlank(zfbConfigKey)) {
					String[] zfbArr = zfbConfigKey.split(",");
					for (int i = 0; i < zfbArr.length; i++) {
						configKeyMap.put(zfbArr[i], ChannelTypeEnum.zfb);
					}
				}
				if(StringUtil.isNotBlank(swiftpassConfigKey)) {
					String[] swiftpassArr = swiftpassConfigKey.split(",");
					for (int i = 0; i < swiftpassArr.length; i++) {
						configKeyMap.put(swiftpassArr[i], ChannelTypeEnum.swiftpass);
					}
				}
				if(StringUtil.isNotBlank(netPayConfigKey)) {
					String[] netPayArr = netPayConfigKey.split(",");
					for (int i = 0; i < netPayArr.length; i++) {
						configKeyMap.put(netPayArr[i], ChannelTypeEnum.netpay);
					}
				}
				if(StringUtil.isNotBlank(unionPayConfigKey)) {
					String[] unionPayArr = unionPayConfigKey.split(",");
					for (int i = 0; i < unionPayArr.length; i++) {
						configKeyMap.put(unionPayArr[i], ChannelTypeEnum.unionpay);
					}
				}
				String wx_original_id = KasiteWxAndZfbConfigUtil.getInstall().getValue(WXConfigEnum.wx_original_id, configKey);
				if(StringUtil.isNotBlank(wx_original_id)) {
					configKeyMap.put(wx_original_id, ChannelTypeEnum.wechat);
				}
			}
		}
		return configKeyMap.get(configKey);
	}
	
	public static void clear() {
		configKeyMap.clear();
		payRuleMap.clear();
	}
	
	
	
	/**
	 * 获取渠道下的所有configKey
	 * 
	 * @param configKey
	 * @param channelId
	 * @return
	 */
	public static Set<String> getConfigKeyByChannelId(String channelId) {
		String wxConfigKey = getClientConfig(ClientConfigEnum.WeChatConfigKey, channelId);
		String zfbConfigKey = getClientConfig(ClientConfigEnum.ZfbConfigKey, channelId);
		Set<String> set = new HashSet<>();
		if(StringUtil.isNotBlank(wxConfigKey)) {
			String[] wxArr = wxConfigKey.split(",");
			for (int i = 0; i < wxArr.length; i++) {
				set.add(wxArr[i]);
			}
		}
		if(StringUtil.isNotBlank(zfbConfigKey)) {
			String[] zfbArr = zfbConfigKey.split(",");
			for (int i = 0; i < zfbArr.length; i++) {
				set.add(zfbArr[i]);
			}
		}
		return set;
	}
	
	/**
	 * 获取所有的configkey
	 * 
	 * @param configKey
	 * @param channelId
	 * @return
	 */
	public static Map<String, ChannelTypeEnum> getAllConfigKey() {
		if(null == configKeyMap || configKeyMap.size() == 0) {
			Set<String[]> clientids = getClientIds();
			for (String[] strings : clientids) {
				String clientId = strings[0];
				String wxConfigKey = getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
				String zfbConfigKey = getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
				String unionPayConfigKey = getClientConfig(ClientConfigEnum.UnionPayConfigKey, clientId);
				String swiftpassConfigKey = getClientConfig(ClientConfigEnum.SwiftpassConfigKey, clientId);
				String netPayConfigKey = getClientConfig(ClientConfigEnum.NetPayConfigKey, clientId);
				if(StringUtil.isNotBlank(wxConfigKey)) {
					String[] wxArr = wxConfigKey.split(",");
					for (int i = 0; i < wxArr.length; i++) {
						configKeyMap.put(wxArr[i], ChannelTypeEnum.wechat);
					}
				}
				if(StringUtil.isNotBlank(zfbConfigKey)) {
					String[] zfbArr = zfbConfigKey.split(",");
					for (int i = 0; i < zfbArr.length; i++) {
						configKeyMap.put(zfbArr[i], ChannelTypeEnum.zfb);
					}
				}
				if(StringUtil.isNotBlank(unionPayConfigKey)) {
					String[] unionPayArr = unionPayConfigKey.split(",");
					for (int i = 0; i < unionPayArr.length; i++) {
						configKeyMap.put(unionPayArr[i], ChannelTypeEnum.unionpay);
					}
				}
				if(StringUtil.isNotBlank(swiftpassConfigKey)) {
					String[] swiftpassPayArr = swiftpassConfigKey.split(",");
					for (int i = 0; i < swiftpassPayArr.length; i++) {
						configKeyMap.put(swiftpassPayArr[i], ChannelTypeEnum.swiftpass);
					}
				}
				if(StringUtil.isNotBlank(netPayConfigKey)) {
					String[] netPayConfigArr = netPayConfigKey.split(",");
					for (int i = 0; i < netPayConfigArr.length; i++) {
						configKeyMap.put(netPayConfigArr[i], ChannelTypeEnum.netpay);
					}
				}
				
			}
		}
		return configKeyMap;
	}

	/**
	 * 根据channelId获取channelName
	 * 
	 * @param channelId
	 * @return
	 */
	public static String getChannelById(String channelId) {
		Set<String[]> channelSet = getClientIds();
		String channelName = "";
		if (channelSet != null) {
			for (String[] obj : channelSet) {
				if (channelId.equals(obj[0])) {
					channelName = obj[1];
					break;
				}
			}
		}
		return channelName;
	}

	/**
	 * 根据payMethod获取支付枚举
	 * 
	 * @param channelId
	 * @return
	 */
	public static ChannelTypeEnum getPayInfoByType(String payMethod) {
		Map<String, ChannelTypeEnum> payMap = new HashMap<>();
		payMap.put(ChannelTypeEnum.wechat.name(), ChannelTypeEnum.wechat);
		payMap.put(ChannelTypeEnum.zfb.name(), ChannelTypeEnum.zfb);
		return payMap.get(payMethod);
	}

	static String CONNECTIONKASTIECENTER = "kasite.appConfig.isConnectionKastieCenter";
	static String LOCALHOSTROUTEPATH = "kasite.appConfig.configPath";  //本地保存配置文件的目录
	static String TEMPFILEPATH = "tempfiles/";  //临时文件目录
	/** 鉴权时间 */
	static String ACCESSTOKENIVNALIDTIME = "kasite.appConfig.oauthTimes";
	/** 鉴权时间 */
	static String ACCESSTOKENAPI = "oauth.gettoken.api";
	/** 医院ID */
	static String HOSID = "kasite.appConfig.hosId";
	/** 医院名称 */
	static String HOSNAME = "kasite.appConfig.hosName";
	/** 数据库名称 **/
	static String DATABASENAME = "kasite.appConfig.dataBaseName";
	/** 请求签名类型 */
	static String SINGTYPE = "kasite.appConfig.signType";
	/** APM推送消息配置 */
	static String LICENSEKEY = "kasite.appConfig.licenseKey";
	/** 支付二维地址 */
	static String QRPAYURL = "kasite.appConfig.qrPayUrl";

	/** 收银台地址 默认：/business/pay/payment.html */
	static String PAYMENTURL = "kasite.appConfig.paymentUrl";
	/** 腕带二维码地址 */
	static String QRPATIENTPAYURL = "kasite.appConfig.qrPatientPayUrl";
	/** 二维码图片保存地址 */
	private static String QRFILEURL = "kasite.appConfig.qrFileUrl";
	/** 二维码图片宽 */
	private static String QRFILEWIDTH = "kasite.appConfig.qrFileWidth";
	/** 二维码图片长 */
	private static String QRFILEHEIGTH = "kasite.appConfig.qrFileHeigth";
	/** 二维码图片后缀 */
	private static String QRFILESUFFIX = "kasite.appConfig.qrFileSuffix";
	/** 是否启用测试 */
	private static String DEBUG = "kasite.appConfig.Debug";

//	/******************************支付模块配置**************************/
//	/**公众号APPID**/
//	static String TENPAY_CONFIG_APPID = "TenPay.payConfig.appId";
//	/**token**/
//	static String TENPAY_CONFIG_TOKEN = "TenPay.payConfig.token";
//	/**公众号密钥**/
//	static String TENPAY_CONFIG_SECRET = "TenPay.payConfig.secret";
//	/**商户密钥**/
//	static String TENPAY_CONFIG_MERCHANTKEY = "TenPay.payConfig.merchantKey";
//	/**子商户号**/
//	static String TENPAY_CONFIG_MERCHANTID = "TenPay.payConfig.merchantId";
//	/**父APPID（子商户模式启用）**/
//	static String TENPAY_CONFIG_PARENTAPPID = "TenPay.payConfig.parentAppId";
//	/**父商户号（子商户模式启用）**/
//	static String TENPAY_CONFIG_PARENTMERCHANTID = "TenPay.payConfig.parentMerchantId";
//	/**商户证书地址**/
//	static String TENPAY_CONFIG_CERTPATH = "TenPay.payConfig.certPath";
//	/**通知地址**/
//	static String TENPAY_CONFIG_PAYNOTIFYURL = "TenPay.payConfig.payNotifyUrl";
//	/**鉴权回调地址**/
//	static String TENPAY_CONFIG_OAUTHCALLBACKURL = "TenPay.payConfig.oauthCallbackUrl";
//	

//	
//	
//	/**支付宝服务窗APPID**/
//	static String ALIPAY_CONFIG_APPID = "AliPay.payConfig.appId";
//	/**开发者账号PID**/
//	static String ALIPAY_CONFIG_PARTNER = "AliPay.payConfig.partner";
//	/**签约合作服务商PID**/
//	static String ALIPAY_CONFIG_SIGNPARTNER = "AliPay.payConfig.signPartner";
//	/**签名类型**/
//	static String ALIPAY_CONFIG_SIGNTYPE = "AliPay.payConfig.signType";
//	/**传给支付宝的编码 **/
//	static String ALIPAY_CONFIG_CHARSET = "AliPay.payConfig.charset";
//	/**开放平台密钥 应用私钥**/
//	static String ALIPAY_CONFIG_APPPRIVATEKEY = "AliPay.payConfig.appPrivateKey";
//	/**开放平台密钥 应用公钥**/
//	static String ALIPAY_CONFIG_APPPUBLICKEY = "AliPay.payConfig.appPublicKey";
//	/**开放平台密钥 支付宝公钥**/
//	static String ALIPAY_CONFIG_ALIPAYPUBLICKEY = "AliPay.payConfig.alipayPublicKey";
//	/**通知地址**/
//	static String ALIPAY_CONFIG_PAYNOTIFYURL = "AliPay.payConfig.payNotifyUrl";
//	/**鉴权回调地址**/
//	static String ALIPAY_CONFIG_OAUTHCALLBACKURL = "AliPay.payConfig.oauthCallbackUrl";
//	
//	/**网络咨询URL**/
//	static String KASITE_WLZXURL = "kasite.wlzxUrl";
//	/**智能导诊URL**/
//	static String KASITE_ZNDZURL = "kasite.zndzUrl";
	/** webAppUrl **/
	private static String KASITE_HOSWEBAPPURL = "kasite.appConfig.hosWebAppUrl";
	/** surveyUrl **/
	private static String KASITE_SURVEYURL = "kasite.appConfig.surveyUrl";
	/**是否打开白名单**/
	private static final String KASITE_OPENWHITELIST = "kasite.appConfig.openWhiteList";
	/**
	 * 默认的满意度调查页面
	 */
	private final static String DEFAULT_KASITE_SURVEY_ENTER_URL = "/business/survey/survey_enter.html";

	/**
	 * 获取满意度调查页面URL {hosWebAppUrl}/business/survey/survey_enter.html
	 */
	public static String getSurverUrl() {
		String u = map.get(KASITE_SURVEYURL);
		String webappurl = getKasiteHosWebAppUrl();
		if (null == u) {
			u = DEFAULT_KASITE_SURVEY_ENTER_URL;
		}
		return webappurl + u;
	}

	public static void setEsUrl(String esUrl) {
		map.put(ESURL, esUrl);
	}
//	
//	public static String getKasiteWlzxUrl() {
//		return map.get(KASITE_WLZXURL);
//	}
//	public static void setKasiteWlzxUrl(String wlzxUrl) {
//		map.put(KASITE_WLZXURL, wlzxUrl);
//	}
//	public static String getKasiteZndzUrl() {
//		return map.get(KASITE_ZNDZURL);
//	}
//	public static void setKasiteZndzUrl(String zndzUrl) {
//		map.put(KASITE_ZNDZURL, zndzUrl);
//	}

	/**
	 * 项目的前置全路径 如： https://cs001.kasitesoft.com 后面不用加 ／ 可以通过 配置文件
	 * kasite.appConfig.hosWebAppUrl 进行修改
	 * 
	 * @return
	 */
	public static String getKasiteHosWebAppUrl() {
		return map.get(KASITE_HOSWEBAPPURL);
	}

	/**
	 * 获取线上支付功能是否开启：1 开启线上支付，2 关闭线上支付
	 * @return 默认返回1 订单模式，有设置值 则为非订单模式
	 */
	public static Integer getIsOnlinePay(String clientId, BusinessTypeEnum busType) {
		//判断是否有配置全业务都关闭或开通
		String o999 = map.get(MessageFormat.format(ORDERMODEL, clientId, BusinessTypeEnum.ORDERTYPE_999.getCode()));
		if(null != o999) {
			return Integer.parseInt(o999);
		}
		String v = map.get(MessageFormat.format(ORDERMODEL, clientId, busType.getCode()));
		if (null != v) {
			int vi =  Integer.parseInt(v);
			if(vi != 1 && vi !=2) {
				throw new RRException("【"+vi +" 】未定义，支付方式。是否开启线上支付只有2个状态：1 开启线上支付，2 关闭线上支付");
			}
			return vi;
		}
	
		return 1;
	}

	public static void setKasiteHosWebAppUrl(String hosWeAppUrl) {
		map.put(KASITE_HOSWEBAPPURL, hosWeAppUrl);
	}
	/**
	 * 微信公众号相关配置信息获取 包含微信appid／密钥／token验证
	 * 
	 * @param name
	 * @param appId
	 * @return
	 */
	public static String getWxConfig(WXConfigEnum name, String appId) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, appId);
	}
	
	
	
	

	/**
	 * 获取微信支付配置信息
	 * 
	 * @param name 支付配置相关属性
	 * @param id   商户配置ID 【代码中： configKey 】
	 * @return
	 */
	public static String getWxPay(WXPayEnum name, String id) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, id);
	}

	public static String getUnionPay(UnionPayEnum name, String id) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, id);
	}
	
	public static String getSwiftpass(SwiftpassEnum name, String id) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, id);
	}
	
	public static String getNetPay(NetPayEnum name, String id) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, id);
	}
	
	public static String getDragonPay(DragonPayEnum name, String id) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, id);
	}
	
	private static Map<String, PayRule> payRuleMap = new HashMap<>();
	
	/**
	 * 获取支付配置信息
	 * @param configKey
	 * @return
	 */
	public static PayRule getPayRule(String configKey) {
		PayRule rule = payRuleMap.get(configKey);
		if(null != rule) {
			return rule;
		}
		String payRule = getWxPay(WXPayEnum.pay_rule, configKey);
		if(StringUtil.isBlank(payRule)) {
			payRule = getZfbConfig(ZFBConfigEnum.pay_rule, configKey);
		}
		if(StringUtil.isNotBlank(payRule)) {
			JSONObject json = JSONObject.parseObject(payRule);
			rule = JSON.toJavaObject(json, PayRule.class);
			rule.setJson(json);
			payRuleMap.put(configKey, rule);
			return rule;
		}
		return null;
	}

	/**
	 * 支付规则限制
	 * @param configKey 支付配置
	 * @param price 支付金额
	 * @throws Exception
	 */
	public static void payRule(String configKey,Integer price) throws Exception {
		PayRule rule = getPayRule(configKey);
		//系统当前日期
		String nowTime = DateOper.getNow("HH:mm");
		String[] nowTimes = nowTime.split(":");
		//如果没有限制，则直接通过
		if(rule!=null) {
			//限制支付时段
			if(StringUtil.isNotBlank(rule.getPayTimeStart())) {
				String[] startTimes = rule.getPayTimeStart().split(":");
				String[] endTimes = rule.getPayTimeEnd().split(":");
				//如果当前时间小于限制开始时间||当前时间大于限制结束时间
				if(Integer.parseInt(nowTimes[0])<Integer.parseInt(startTimes[0]) ||
						((Integer.parseInt(nowTimes[0])==Integer.parseInt(startTimes[0])&& Integer.parseInt(nowTimes[1])<Integer.parseInt(startTimes[1])))
						|| Integer.parseInt(nowTimes[0])>Integer.parseInt(endTimes[0]) 
								|| (Integer.parseInt(nowTimes[0])==Integer.parseInt(endTimes[0])&&Integer.parseInt(nowTimes[1])>Integer.parseInt(endTimes[1]))) {
					throw new RRException(RetCode.Pay.ERROR_PAYRULE,"不在支付时间之内！支付时间："+rule.getPayTimeStart()+" ~ "+rule.getPayTimeEnd());
				}
			}

			//限制支付金额
			if(StringUtil.isNotBlank(rule.getSingleLimitStart())) {
				if( price.intValue() < StringUtil.yuanChangeFenInt(rule.getSingleLimitStart()+"")
						|| price.intValue()> StringUtil.yuanChangeFenInt(rule.getSingleLimitEnd()+"")) {
					throw new RRException(RetCode.Pay.ERROR_PAYRULE,"金额超过限制！限制："+rule.getSingleLimitEnd()+"(元)");
				}
			}
		}
	}
	/**
	 * 退款规则限制
	 * @param configKey 支付配置
	 * @throws RRException 
	 * @throws Exception
	 */
	public static void refundRule(String configKey) throws RRException, Exception {
		PayRule rule = getPayRule(configKey);
		//系统当前日期
		String nowTime = DateOper.getNow("HH:mm");
		String[] nowTimes = nowTime.split(":");
		if(rule!=null ) {
			if(!StringUtil.isEmpty(rule.getRefundTimeStart())) {
				String[] startTimes = rule.getRefundTimeStart().split(":");
				String[] endTimes = rule.getRefundTimeEnd().split(":");
				//如果当前时间小于限制开始时间||当前时间大于限制结束时间
				if(Integer.parseInt(nowTimes[0])<Integer.parseInt(startTimes[0]) ||
						((Integer.parseInt(nowTimes[0])==Integer.parseInt(startTimes[0])&& Integer.parseInt(nowTimes[1])<Integer.parseInt(startTimes[1])))
						|| Integer.parseInt(nowTimes[0])>Integer.parseInt(endTimes[0]) 
								|| (Integer.parseInt(nowTimes[0])==Integer.parseInt(endTimes[0])&&Integer.parseInt(nowTimes[1])>Integer.parseInt(endTimes[1]))) {
					throw new RRException(RetCode.Pay.ERROR_PAYRULE,"不在退费时间之内！退费时间："+rule.getRefundTimeStart()+" ~ "+rule.getRefundTimeEnd());
				}
			}
		}
	}
	/**
	 * 获取鉴权回调地址
	 * 
	 * @param channelType
	 * @param clientId
	 * @param configKey
	 * @return
	 */
	public static String getOauthCallBackUrl(ChannelTypeEnum channelType, String clientId, String configKey) {
		// 获取微信鉴权回调地址
		String mainUrl = getKasiteHosWebAppUrl();
		String webAppUrl = getWxConfig(WXConfigEnum.wx_oauthCallbackUrl, configKey);
		if(null != webAppUrl && StringUtil.isNotBlank(webAppUrl.trim())) {
			mainUrl = webAppUrl;
		}
		if (ChannelTypeEnum.wechat.equals(channelType)) {
			return mainUrl + MessageFormat.format(WeChatOauthCallBackUrl, clientId, configKey);
		} else if (ChannelTypeEnum.zfb.equals(channelType)) {
			return mainUrl + MessageFormat.format(ZfbOauthCallBackUrl, clientId, configKey);
		}else if (ChannelTypeEnum.qywechat.equals(channelType)) {
			return mainUrl + MessageFormat.format(qywxOauthGotoOauthUrl, clientId, configKey);
		}
		throw new RRException("未配置鉴权回调渠道无法进行回调。");
	}

	/**
	 * 获取支付回调地址
	 * 
	 * @param channelType
	 * @param configKey
	 * @param clientId
	 * @param openId
	 * @param token
	 * @return
	 */
	public static String getPayCallBackUrl(ChannelTypeEnum channelType, String configKey, String clientId,
			String openId, String token,String orderId) {
		if (null != handler) {
			return handler.getPayCallBackUrl(channelType, configKey, clientId, openId, token,orderId);
		}
		// 获取微信支付回调地址
		String mainUrl = getKasiteHosWebAppUrl();
		if (ChannelTypeEnum.wechat.equals(channelType)) {
			// 获取微信支付回调地址
			return mainUrl + MessageFormat.format(WeChatPayNotifyUrl, clientId, configKey, openId, token,orderId);
		} else if (ChannelTypeEnum.zfb.equals(channelType)) {
			// 获取微信支付回调地址
			return mainUrl + MessageFormat.format(ZfbPayNotifyUrl, clientId, configKey, openId, token,orderId);
		}else if (ChannelTypeEnum.unionpay.equals(channelType)) {
			// 获取微信支付回调地址
			return mainUrl + MessageFormat.format(UnionPayNotifyUrl, clientId, configKey, openId, token,orderId);
		}else if (ChannelTypeEnum.swiftpass.equals(channelType)) {
			// 获取威富通支付回调地址
			return mainUrl + MessageFormat.format(SwiftpassNotifyUrl, clientId, configKey, openId, token,orderId);
		}else if (ChannelTypeEnum.netpay.equals(channelType)) {
			// 获取招行一网通支付回调地址
			return mainUrl + MessageFormat.format(netPayNotifyUrl, clientId, configKey, openId, token,orderId);
		}
		
		throw new RRException("未配置的支付渠道无法进行支付回调。");
	}
	
	/**
	 * 获取支付回调地址
	 * 
	 * @param channelType
	 * @param configKey
	 * @param clientId
	 * @param openId
	 * @param token
	 * @return
	 */
	public static String getRefundCallBackUrl(ChannelTypeEnum channelType, String configKey, String clientId,
			String openId, String token,String orderId) {
		// 获取微信支付回调地址
		String mainUrl = getKasiteHosWebAppUrl();
		if (ChannelTypeEnum.unionpay.equals(channelType)) {
			// 获取微信支付回调地址
			return mainUrl + MessageFormat.format(UnionPayRefundNotifyUrl,clientId, configKey, openId, token,orderId);
		}
		throw new RRException("未配置的支付渠道无法进行支付回调。");
	}

	/**
	 * 获取支付密钥，已经做父子商户判断，如果是父商户的则取父商户密钥
	 * 
	 * @param id
	 * @return
	 */
	public static String getWxPayMchKey(String id) {
		String isParentMode = KasiteWxAndZfbConfigUtil.getInstall().getValue(WXPayEnum.is_parent_mode, id);
		if ("T".equals(isParentMode)) {
			return KasiteWxAndZfbConfigUtil.getInstall().getValue(WXPayEnum.wx_parent_mch_key, id);
		}
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(WXPayEnum.wx_mch_key, id);
	}

	/**
	 * 获取支付的证书地址，已经做父子商户判断，如果是父商户的则取父商户
	 * 
	 * @param id
	 * @return
	 */
	public static String getWxPayCertPath(String id) {
		
		String isParentMode = KasiteConfig.getValue(MessageFormat.format("kasite.WxPay.{0}.is_parent_mode", id));
		if ("T".equals(isParentMode)) {
			return KasiteConfig.getValue(MessageFormat.format("kasite.WxPay.{0}.wx_parent_cert_path", id));
//			return KasiteWxAndZfbConfigUtil.getInstall().getValue(WXPayEnum.wx_parent_cert_path, id);
		}
		return KasiteConfig.getValue(MessageFormat.format("kasite.WxPay.{0}.wx_cert_path", id));
//		return KasiteWxAndZfbConfigUtil.getInstall().getValue(WXPayEnum.wx_cert_path, id);
	}

	/**
	 * 根据 配置信息获取 渠道类型 目前支持 微信／支付宝／银联/企业微信/招行一网通 2种渠道
	 * 
	 * @param clientId
	 * @param configKey
	 * @return
	 */
	public static ChannelTypeEnum getChannelType(String clientId, String configKey) {
		String wchat = getClientConfig(ClientConfigEnum.WeChatConfigKey, clientId);
		String[] wechats = null;
		if(StringUtil.isNotBlank(wchat) && wchat.split(",").length >1) {
			wechats = wchat.split(",");
		}
		String k = getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
		String[] payWeChats = null;
		if(StringUtil.isNotBlank(k) && k.split(",").length >1) {
			payWeChats = k.split(",");
		}
		String k2 = getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
		String[] payAlis = null;
		if(StringUtil.isNotBlank(k2) && k2.split(",").length >1) {
			payAlis = k2.split(",");
		}
		String k3 = getClientConfig(ClientConfigEnum.UnionPayConfigKey, clientId);
		String[] payUnion = null;
		if(StringUtil.isNotBlank(k3) && k3.split(",").length >1) {
			payUnion = k3.split(",");
		}
		String k4 = getClientConfig(ClientConfigEnum.QyWeChatConfigKey, clientId);
		String[] qyWechats = null;
		if(StringUtil.isNotBlank(k4) && k4.split(",").length >1) {
			qyWechats = k4.split(",");
		}
		String k5 = getClientConfig(ClientConfigEnum.NetPayConfigKey, clientId);
		String[] netPays = null;
		if(StringUtil.isNotBlank(k5) && k5.split(",").length >1) {
			netPays = k5.split(",");
		}
		if(null != wechats && null != configKey) {
			for (String string : wechats) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.wechat;
				}
			}
		}
		if(null != payWeChats && null != configKey) {
			for (String string : payWeChats) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.wechat;
				}
			}
		}
		if(null != payAlis && null != configKey) {
			for (String string : payAlis) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.zfb;
				}
			}
		}
		if(null != payUnion && null != configKey) {
			for (String string : payUnion) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.unionpay;
				}
			}
		}
		if(null != qyWechats && null != configKey) {
			for (String string : qyWechats) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.qywechat;
				}
			}
		}
		if(null != netPays && null != configKey) {
			for (String string : netPays) {
				if ((null != string && configKey.equals(string))){
					return ChannelTypeEnum.netpay;
				}
			}
		}
		if ((null != wchat && configKey.equals(wchat)) || (null != configKey && configKey.equals(k))) {
			return ChannelTypeEnum.wechat;
		} else if (null != configKey && configKey.equals(k2)) {
			return ChannelTypeEnum.zfb;
		}else if (null != configKey && configKey.equals(k3)) {
			return ChannelTypeEnum.unionpay;
		}else if (null != configKey && configKey.equals(k4)) {
			return ChannelTypeEnum.qywechat;
		}else if (null != configKey && configKey.equals(k5)) {
			return ChannelTypeEnum.netpay;
		}
		throw new RRException("请确认渠道ID【"+clientId+"】与 configKey【"+configKey+"】的映射关系。");
//		throw new RRException(RetCode.Pay.ERROR_CHANNELID);
	}

	/**
	 * 根据 配置信息获取 渠道类型 目前支持 微信／支付宝 2种渠道
	 * 
	 * @param clientId
	 * @param configKey
	 * @return
	 */
	public static ChannelTypeEnum getChannelType(InterfaceMessage msg) {
		if (null == msg || StringUtil.isBlank(msg.getAuthInfo())) {
			throw new RRException(RetCode.Common.OAUTH_ERROR_NOAUTHODINFO);
		}
		AuthInfoVo vo = getAuthInfo(msg.getAuthInfo());
		return getChannelType(vo.getClientId(), vo.getConfigKey());
	}

	/**
	 * 获取支付宝相关配置
	 * 
	 * @param name 属性
	 * @param id   商户配置ID configKey
	 * @return
	 */
	public static String getZfbConfig(ZFBConfigEnum name, String conigKey) {
		return KasiteWxAndZfbConfigUtil.getInstall().getValue(name, conigKey);
	}

	public static String getDataBaseName() {
		return map.get(DATABASENAME);
	}

	public static String setDataBaseName(String dataBaseName) {
		return map.put(DATABASENAME, dataBaseName);
	}

//	public static String getHosId(AuthInfoVo vo) {
//		return getOrgCode();
//	}
//
//	public static String getHosName() {
//		return map.get(HOSNAME);
//	}
//
//	public static void setHosName(String hosName) {
//		map.put(HOSNAME, hosName);
//	}

	public static String getSignType() {
		return map.get(SINGTYPE);
	}
//
//	public static void setAgentServerRpc(String agentServerRpc) {
//		map.put(AGENTSERVERRPC, agentServerRpc);
//	}

	public static String getLicenseKey() {
		return map.get(LICENSEKEY);
	}

	public static void setLicenseKey(String licenseKey) {
		map.put(LICENSEKEY, licenseKey);
	}

	/**
	 * 智付场景中：扫码付获取的二维码支付地址。
	 * 
	 * @param clientId
	 * @param guid
	 * @return
	 */
	public static String getQrPayUrl(String clientId, String guid) {
		String qrUrl = map.get(QRPAYURL);
		if (com.kasite.core.common.util.StringUtil.isNotBlank(qrUrl) && qrUrl.startsWith("http")) {
			return qrUrl + "?clientId=" + clientId + "&guid=" + guid;
		} else if (StringUtil.isBlank(qrUrl)) {
			qrUrl = "/qrPay.do";
		}
		return getKasiteHosWebAppUrl() + "/" + clientId + "/" + guid + qrUrl;
	}

	/**
	 * 获取收银台地址 默认值：/business/pay/payment.html 需要个性化的时候通过配置yml的
	 * kasite.appConfig.paymentUrl 来配置
	 * 
	 * @param clientId
	 * @param guid
	 * @return
	 */
	public static String getPayMentUrl(String clientId) {
		String url = map.get(PAYMENTURL);
		if (com.kasite.core.common.util.StringUtil.isNotBlank(url) && url.startsWith("http")) {
			return url;
		} else if (StringUtil.isBlank(url)) {
			url = "/business/pay/payment.html";
		}
		return getKasiteHosWebAppUrl() + url;
	}

	public static StringBuffer getQrFileUrl() throws ParseException {
		return getLocalConfigPathByName("QrPicFile", true, true,true);
	}

	public static void setQrFileUrl(String qrFileUrl) {
		map.put(QRFILEURL, qrFileUrl);
	}

	public static Integer getQrFileWidth() {
		try {
			return Integer.parseInt(map.get(QRFILEWIDTH));
		} catch (Exception e) {
			return 400;
		}
	}

	public static void setQrFileWidth(String qrFileWidth) {
		map.put(QRFILEWIDTH, qrFileWidth);
	}

	public static Integer getQrFileHeigth() {
		try {
			return Integer.parseInt(map.get(QRFILEHEIGTH));
		} catch (Exception e) {
			return 400;
		}
	}

	public static void setQrFileHeigth(String qrFileHeigth) {
		map.put(QRFILEHEIGTH, qrFileHeigth);
	}

	/**
	 * 获取二维码文件后缀
	 * 
	 * @param qrFileSuffix
	 */
	public static String getQrFileSuffix() {
		String suffix = map.get(QRFILESUFFIX);
		if (StringUtil.isBlank(suffix)) {
			return "jpg";
		}
		return suffix;
	}

	public static void setQrFileSuffix(String qrFileSuffix) {
		map.put(QRFILESUFFIX, qrFileSuffix);
	}

	public static boolean getDebug() {
		try {
			return Boolean.parseBoolean(map.get(DEBUG));
		} catch (Exception e) {
			return false;
		}
	}

	public static void setDebug(String debug) {
		map.put(DEBUG, debug);
	}

	/**
	 * 判断是否要加密打印
	 * @param obj
	 */
	public static void print(String obj) {
//		if (getDebug()) {
		logger.info(obj);
//		}
	}

	/**
	 * 获取本地配置文件目录
	 * 
	 * @return
	 */
	public static String wechatAndzfbMsgPath() {
		return localConfigPath()+File.separator + "callbackmessage";
	}
	/**
	 * 获取本地配置文件目录
	 * 
	 * @return
	 */
	public static String localConfigPath() {
		return map.get(LOCALHOSTROUTEPATH);
	}
	/**
	 * 获取本地配置文件目录
	 * @param dirName 目录下文件夹名称
	 * @param isYear 是否按年分文件夹
	 * @param isMouth 是否按月分文件夹
	 * @return
	 * @throws ParseException 
	 */
	public static StringBuffer getLocalConfigPathByName(String dirName,boolean isYear,boolean isMouth,boolean day) throws ParseException {
		StringBuffer sbf = new StringBuffer(localConfigPath());
		sbf.append(File.separator).append(dirName);
		String now = DateOper.getNow("yyyyMMdd");
		if(isYear) {
			String year = now.substring(0, 4);
			sbf.append(File.separator).append(year);
		}
		if(isYear && isMouth) {
			String yyyyMM  = now.substring(4, 6);
			sbf.append(File.separator).append(yyyyMM);
		}else if(!isYear && isMouth) {
			String yyyyMM  = now.substring(0, 6);
			sbf.append(File.separator).append(yyyyMM);
		}
		if(isYear && isMouth && day) {
			String yyyyMMdd = now.substring(6, 8);;
			sbf.append(File.separator).append(yyyyMMdd);
		}else if(day) {
			String yyyyMMdd = now;
			sbf.append(File.separator).append(yyyyMMdd);
		}
		String path = sbf.toString();
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		return sbf;
	}
	public static void localConfigPath(String path) {
		map.put(LOCALHOSTROUTEPATH, path);
	}
	/**
	 * 获取临时文件目录
	 * 
	 * @return
	 */
	public static String getTempfilePath() {
		return TEMPFILEPATH;
	}

	/**
	 * 判断是否需要连接配置中心
	 * 
	 * @return
	 */
	public static boolean isConnectionKastieCenter() {
		String s = map.get(CONNECTIONKASTIECENTER);
		if (StringUtil.isBlank(s)) {
			return true;
		}
		if ("true".equals(s)) {
			return true;
		}
		return false;
	}

	public static String isConnectionKastieCenter(String isConnectionKastieCenter) {
		return map.put(CONNECTIONKASTIECENTER, isConnectionKastieCenter);
	}

	public static String getMessageStatusUrl() {
		return getUrl(KasiteSysApiEnum.API_GETMESSAGESTATUS);
	}

	// static void refresh() {
	// if (!inst.watch) {
	// return;
	// }
	// inst.setChanged();
	// inst.notifyObservers();
	// }

	public static void setValue(String key, String value) {
		map.put(key, value);
	}

	public static void setAppId(String appId) {
		map.put(APPID, appId);
	}

	public static void setAppSecret(String appSecret) {
		map.put(APPSECRET, appSecret);
	}
	// public static void setGetTokenUrl(String getTokenUrl) {
	// map.put(GETTOKENURL, getTokenUrl);
	// }

	// public static void addAppObserver(Observer o) {
	// inst.addObserver(o);
	// }

	public static String getValue(String name) {
		String value = map.get(name);
		return value;
	}

	public static String getAppId() {
		return map.get(APPID);
	}

	public static String getAppSecret() {
		return map.get(APPSECRET);
	}

	/**
	 * 获取渠道下 允许调用的AppId对应的密钥
	 * kasite.appConfig.smallpro.clientSecret.KASITE-CLIENT-ANDROID-MINIPay.59.AppSecret:
	 * "******",
	 * 
	 * @param clientId
	 * @param appId
	 * @return
	 */
	public static String getClientAppSecret(String clientId, String appId) {
		String appIds = getClientConfig(ClientConfigEnum.clientAppId, clientId);
		if(StringUtil.isNotBlank(appIds) && appIds.indexOf(appId+",") >= 0) {
			return LocalOAuthUtil.getInstall().getAppSecret(appId);
		}else {
			return null;
		}
	}

	public static String getAppConfig() {
		return getUrl(KasiteSysApiEnum.API_GETAPPCONFIG);
	}

	public static String getGetOrgRoute() {
		return getUrl(KasiteSysApiEnum.API_GETORGROUTE);
	}

	public static String getQueryPrivateKeyList() {
		return getUrl(KasiteSysApiEnum.API_queryPrivateKeyList);
	}

	public static String getUrl(KasiteSysApiEnum api) {
		String url = map.get(api.name());
		String[] centerUrl = getCenterServerUrl();
		if (StringUtil.isBlank(url)) {
			url = api.getApis(centerUrl);
		}
		return url;
	}

	//
	public static String getGetTokenUrl() {
		return getUrl(KasiteSysApiEnum.API_GETTOKENURL);
	}

	public static String getCenterTokenUrl() {
		return getUrl(KasiteSysApiEnum.API_GETCEBETRTOKENURL);
	}

	public static String getOrgCode() {
		return map.get(ORGCODE);
	}
	public static String getOrgName() {
		String orgName = map.get(ORGNAME);
		if(StringUtil.isBlank(orgName)) {
			orgName = map.get("hosName");
		}
		return orgName;
	}
	public static String setOrgCode(String orgCode) {
		return map.put(ORGCODE, orgCode);
	}
	public static String getPublicKey() {
		return map.get(PUBLICKEY);
	}

	public static String setPublicKey(String publicKey) {
		return map.put(PUBLICKEY, publicKey);
	}

	public static String[] getCenterServerUrl() {
		String centerUrl = map.get(CENTERURL);
		if (StringUtil.isBlank(centerUrl)) {
			centerUrl = default_centerUrl;
		}
		return centerUrl.split(",");
	}

	public static void setCenterServerUrl(String centerServerUrl) {
		Assert.isBlank(centerServerUrl, "centerServerUrl 不能为空。");
		map.put(CENTERURL, centerServerUrl);
	}

	public static String getValue(String name, String defaultValue) {
		String v = getValue(name);
		if ((v == null) || (v.isEmpty())) {
			return defaultValue;
		}
		return v;
	}

	public static long getLong(String name, long defaultValue) {
		try {
			return Long.parseLong(getValue(name));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public static int getInteger(String name, int defaultValue) {
		try {
			return Integer.parseInt(getValue(name));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public static int getAccessTokenValidTime() {
		return getInteger(ACCESSTOKENIVNALIDTIME, 120);
	}

	/**
	 * 获取鉴权头部信息
	 * 
	 * @param msg
	 * @return
	 * @throws ParamException
	 */
	public static AuthInfoVo parse(InterfaceMessage msg) throws ParamException {
		String authInfoStr = msg.getAuthInfo();
		if (StringUtil.isNotBlank(authInfoStr)) {
			return getAuthInfo(authInfoStr);
		}
		throw new ParamException(" authInfo 参数不允许为空。");
	}

	/**
	 * 获取鉴权信息。
	 * 
	 * @param authInfoStr
	 * @return
	 */
	public static AuthInfoVo getAuthInfoNoException(String authInfoStr) {
		if (null != authInfoStr) {
			JSONObject json = JSON.parseObject(authInfoStr);
			return JSON.toJavaObject(json, AuthInfoVo.class);
		}
		return null;
	}
	/**
	 * 获取鉴权信息。
	 * 
	 * @param authInfoStr
	 * @return
	 */
	public static AuthInfoVo getAuthInfo(InterfaceMessage msg) {
		if(null != msg) {
			return getAuthInfo(msg.getAuthInfo());
		}
		return null;
	}
	/**
	 * 获取鉴权信息。
	 * 
	 * @param authInfoStr
	 * @return
	 */
	public static AuthInfoVo getAuthInfo(String authInfoStr) {
		if(null == authInfoStr) {
			return new AuthInfoVo();
		}
		JSONObject json = JSON.parseObject(authInfoStr);
		checkParam(json, "sessionKey");
		checkParam(json, "clientId");
		checkParam(json, "sign");
		checkParam(json, "configKey");
		return JSON.toJavaObject(json, AuthInfoVo.class);
	}

	private static void checkParam(JSONObject json, String key) throws ParamException {
		Object o = json.get(key);
		if (null == o || "".equals(o)) {
			throw new ParamException(" authInfo 参数中 ： " + key + " 参数不能为空。");
		}
	}

	public static String getIp() {
		return getValue("IP");
	}

	public static String getLogPath() {
		String logPath = getValue(LOGPATH);
		if (StringUtil.isBlank(logPath)) {
			return localConfigPath();
		}
		return logPath;
	}
	/**
	 * 获取查询历史订单列表天数
	 * appConfig.orderDays
	 * yml配置 orderDays 节点
	 * 默认返回180天
	 * @return
	 */
	public static Integer getHistoryOrderListDays() {
		String str = map.get(ORDERLISTDAYS);
		if(StringUtil.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return 180;
	}
	

	public static boolean getCustomLog() {
		String v = getValue(CUSTOMLOG);
		if (StringUtil.isBlank(v)) {
			return false;
		}
		return true;
	}

	

	private static AuthInfoVo createSystemAuthInfoVo() {
		AuthInfoVo vo = new AuthInfoVo();
		String token = AUTHINFO_TOKEN_TIME + System.currentTimeMillis();
		vo.setSessionKey(token);// token
		vo.setClientId(KstHosConstant.SYSOPERATORID);
		vo.setSign(KstHosConstant.SYSOPERATORID);// openid/userid
		vo.setConfigKey(KstHosConstant.SYS);
		vo.setClientVersion(KasiteConfig.getOrgCode());
		return vo;
	}

	/**
	 * 创建定时任务系统需要的 InterfaceMessage 对象
	 * 
	 * @param api     定时任务调用的api
	 * @param params  入参
	 * @param orderId 订单ID
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static InterfaceMessage createJobInterfaceMsg(Class<?> clazz, String api, String params, String orderId,
			String name, String clientId, String configKey,String openId) throws Exception {
		InterfaceMessage message = new InterfaceMessage();
		message.setApiName(api);
		AuthInfoVo vo = new AuthInfoVo();
		if (StringUtil.isBlank(clientId)) {
			clientId = KstHosConstant.SYSOPERATORID;
		}
		if (StringUtil.isNotBlank(configKey)) {
			vo.setConfigKey(configKey);
		} else {
			vo.setConfigKey(KstHosConstant.SYSOPERATORID);
		}
		vo.setClientId(clientId);
		if(null != openId) {
			vo.setSign(openId);
		}else {
			vo.setSign(KstHosConstant.SYSOPERATORID);
		}
		vo.setSessionKey(AUTHINFO_TOKEN_TIME + System.currentTimeMillis() + "_" + orderId);
		vo.setClientVersion(KasiteConfig.getOrgCode());
		vo.setUuid(clazz.getName() + "_" + IDSeed.next());

		message.setAuthInfo(vo.toString());
		message.setOutType(KstHosConstant.OUTTYPE);
		String p = params;
		message.setParam(p);
		message.setParamType(KstHosConstant.INTYPE);
		message.setSeq(Long.toString(System.currentTimeMillis()));
		message.setClientIp(NetworkUtil.getLocalIP());
		return message;
	}
	public static InterfaceMessage createJobInterfaceMsg(Class<?> clazz, String api, String params, AuthInfoVo vo) throws Exception {
		InterfaceMessage message = new InterfaceMessage();
		message.setApiName(api);
		message.setAuthInfo(vo.toString());
		message.setOutType(KstHosConstant.OUTTYPE);
		String p = params;
		message.setParam(p);
		message.setParamType(KstHosConstant.INTYPE);
		message.setSeq(Long.toString(System.currentTimeMillis()));
		message.setClientIp(NetworkUtil.getLocalIP());
		return message;
	}
	public static InterfaceMessage createJobInterfaceMsg(Class<?> clazz, String api, String params, String random,
			String clientId, String configKey) throws Exception {
		InterfaceMessage message = new InterfaceMessage();
		message.setApiName(api);
		AuthInfoVo vo = new AuthInfoVo();
		if (StringUtil.isBlank(clientId)) {
			clientId = KstHosConstant.SYSOPERATORID;
		}
		if (StringUtil.isNotBlank(configKey)) {
			vo.setConfigKey(configKey);
		} else {
			vo.setConfigKey(KstHosConstant.SYSOPERATORID);
		}

		vo.setClientId(clientId);
		vo.setSign(KstHosConstant.SYSOPERATORID);
		vo.setSessionKey(AUTHINFO_TOKEN_TIME + System.currentTimeMillis() + "_" + random);
		vo.setClientVersion(KasiteConfig.getOrgCode());
		vo.setUuid(clazz.getName() + "_" + IDSeed.next());

		message.setAuthInfo(vo.toString());
		message.setOutType(KstHosConstant.OUTTYPE);
		String p = params;
		message.setParam(p);
		message.setParamType(KstHosConstant.INTYPE);
		message.setSeq(Long.toString(System.currentTimeMillis()));
		message.setClientIp(NetworkUtil.getLocalIP());
		return message;
	}

	/**
	 * 鉴权登录信息获取
	 * 
	 * @param sysUser
	 * @param token
	 * @return
	 */
	public static AuthInfoVo createAuthInfoVo(String uuid, String token, SysUserEntity sysUser) {
		AuthInfoVo vo = new AuthInfoVo();
		vo.setSessionKey(token);// token
		vo.setClientId(sysUser.getClientId());
		vo.setSign(sysUser.getUsername());// openid/userid
		vo.setClientVersion(KasiteConfig.getOrgCode());
		vo.setUuid(uuid);
		vo.setConfigKey(sysUser.getChannelId());
		return vo;
	}

	/**
	 * 无法获取登录用户信息的 比如通过job进入调用系统／通过 微信回调等方式统一的一个鉴权参数赋值 用于做日志的不能用于业务处理
	 * 
	 * @param sysUser
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static AuthInfoVo createAuthInfoVo(Class<?> clazz) {
		String uuid = clazz.getName() + "_" + IDSeed.next();
		return createAuthInfoVo(uuid);
	}

	/**
	 * 无法获取登录用户信息的 比如通过job进入调用系统／通过 微信回调等方式统一的一个鉴权参数赋值 用于做日志的不能用于业务处理
	 * 
	 * @param sysUser
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static AuthInfoVo createAuthInfoVo(String uuid) {
		AuthInfoVo vo = null;
		ShiroService shiroService = SpringContextUtil.getBean(ShiroService.class);
		if (null != shiroService) {
			SysUserEntity user = shiroService.getUser();
			if (null != user) {
				Long userId = user.getId();
				SysUserTokenEntity token = null;
				try {
					token = shiroService.getSysUserToken(userId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null != token) {
					vo = KasiteConfig.createAuthInfoVo(uuid, token.getToken(), user);
				}
			}
		}
		if (null == vo) {
			vo = createSystemAuthInfoVo();
		}
		return vo;
	}

	/**
	 * 获取前端页面的菜单配置
	 * 
	 * @return
	 */
	public static String getWxWebAppMenu() {
		return KasiteWxAndZfbConfigUtil.getInstall().getWxWebAppMenu();
	}

	/**
	 * 获取前端页面的菜单配置
	 * 
	 * @return
	 */
	public static String getZfbWebAppMenu() {
		return KasiteWxAndZfbConfigUtil.getInstall().getZfbWebAppMenu();
	}

	public static boolean isSystemCall(AuthInfoVo vo) {
		if (null != vo) {
			String sessionKey = vo.getSessionKey();
			if (StringUtil.isNotBlank(sessionKey)) {
				if (sessionKey.startsWith(AUTHINFO_TOKEN_TIME)) {
					return true;
				}
			}
		}
		return false;
	}

	protected static Logger logger = LoggerFactory.getLogger(KasiteConfig.class);

	public static JSONObject print(boolean isAll) {
		JSONObject o = (JSONObject) JSON.toJSON(map);
		if(!isAll) {
			Set<String> keys = o.keySet();
			for (String key : keys) {
				if (key.toLowerCase().indexOf("key") >= 0 || key.toLowerCase().indexOf("password") >= 0 
						|| key.toLowerCase().indexOf("secret") >= 0 || key.toLowerCase().indexOf("pwd") >= 0
						|| key.toLowerCase().indexOf("cert") >= 0 || key.toLowerCase().indexOf("datasource") >= 0
						|| key.toLowerCase().indexOf("guardconfig") >= 0) {
					o.put(key, "******");
				}
			}
		}
		logger.info("系统配置信息：KasiteConfig : " + o.toJSONString());
		return o;
	}
//	/**
//	 * 判断HIS的接口调用是否需要保存结果集 默认设置：查询科室／医生／排班／号源 都不保存结果集 后续要改动结果集的是否保存，请从配置文件中修改
//	 * 或者从中心修改
//	 * 
//	 * @param vo
//	 * @param api
//	 */
//	public static boolean getIsSaveZfbCallResult(AuthInfoVo vo, ApiModule.Zfb api) {
//		String apiName = api.name();
//		String key = MessageFormat.format(ISSAVERESULT, vo.getClientId(), apiName);
//		String value = map.get(key);
//		if (null == value) {
//			// 设置调用HIS接口默认不保存结果集的配置
//			switch (api) {
//			case media_get:
//				return false;
//			case user_info_batchget:
//				return false;
//			case groups_get:
//				return false;
//			default:
//				return true;
//			}
//		} else {
//			// 值 = 2 的时候不保存，1 的时候要保存
//			if ("2".equals(value)) {
//				return false;
//			}
//		}
//		// 其它默认都保存
//		return true;
//	}
//	/**
//	 * 判断HIS的接口调用是否需要保存结果集 默认设置：查询科室／医生／排班／号源 都不保存结果集 后续要改动结果集的是否保存，请从配置文件中修改
//	 * 或者从中心修改
//	 * 
//	 * @param vo
//	 * @param api
//	 */
//	public static boolean getIsSaveWeChatCallResult(AuthInfoVo vo, ApiModule.WeChat api) {
//		String apiName = api.name();
//		String key = MessageFormat.format(ISSAVERESULT, vo.getClientId(), apiName);
//		String value = map.get(key);
//		if (null == value) {
//			// 设置调用HIS接口默认不保存结果集的配置
//			switch (api) {
//			case media_get:
//				return false;
//			case pay_downloadbill:
//				return false;
//			case user_info_batchget:
//				return false;
//			case groups_get:
//				return false;
//			default:
//				return true;
//			}
//		} else {
//			// 值 = 2 的时候不保存，1 的时候要保存
//			if ("2".equals(value)) {
//				return false;
//			}
//		}
//		// 其它默认都保存
//		return true;
//	}
	
//	/**
//	 * 
//	 * @param vo
//	 * @param api
//	 */
//	public static boolean getIsSaveUnionPayCallResult(AuthInfoVo vo, ApiModule.UnionPay api) {
//		String apiName = api.name();
//		String key = MessageFormat.format(ISSAVERESULT, vo.getClientId(), apiName);
//		String value = map.get(key);
//		if (null == value) {
//			switch (api) {
//			case filedownload://账单下载不保存，数据量太大
//				return false;
//			default:
//				return true;
//			}
//		} else {
//			// 值 = 2 的时候不保存，1 的时候要保存
//			if ("2".equals(value)) {
//				return false;
//			}
//		}
//		// 其它默认都保存
//		return true;
//	}
	private static Map<String, Boolean> NOTSAVEAPIMAP = new HashMap<>();
	static {

		NOTSAVEAPIMAP.put(ApiModule.His.queryDoctor.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.His.queryDept.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.His.queryArrange.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.His.queryNumber.name(), false); 
		

		NOTSAVEAPIMAP.put(ApiModule.Zfb.media_get.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.Zfb.user_info_batchget.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.Zfb.groups_get.name(), false); 
		
		
		NOTSAVEAPIMAP.put(ApiModule.WeChat.media_get.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.WeChat.pay_downloadbill.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.WeChat.user_info_batchget.name(), false); 
		NOTSAVEAPIMAP.put(ApiModule.WeChat.groups_get.name(), false); 
		
		
		NOTSAVEAPIMAP.put(ApiModule.NetPay.NETPAY_QUERYSETTLEDORDERBYMERCHANTDATE.name(), false);//账单下载不保存，数据量太大
		NOTSAVEAPIMAP.put(ApiModule.NetPay.NETPAY_QUERYREFUNDBYDATE.name(), false);//账单下载不保存，数据量太大
		NOTSAVEAPIMAP.put(ApiModule.Swiftpass.filedownload.name(), false);//账单下载不保存，数据量太大
		NOTSAVEAPIMAP.put(ApiModule.UnionPay.filedownload.name(), false);//账单下载不保存，数据量太大
	}
	private static boolean isSaveCallResult(String apiName) {
		if(null != NOTSAVEAPIMAP.get(apiName)) {
			return NOTSAVEAPIMAP.get(apiName);
		}
		return true;
	}
	
	/**
	 * 判断接口是否需要保存调用接口的结果集
	 * @param vo
	 * @param api
	 */
	public static boolean getIsSaveCallResult(AuthInfoVo vo, ApiModule api) {
		String apiName = api.name();
		String key = MessageFormat.format(ISSAVERESULT, vo.getClientId(), apiName);
		String value = map.get(key);
		if (null == value) {
			return isSaveCallResult(apiName);
		} else {
			// 值 = 2 的时候不保存，1 的时候要保存
			if ("2".equals(value)) {
				return false;
			}
		}
		// 其它默认都保存
		return true;
	}
//	/**
//	 * 判断HIS的接口调用是否需要保存结果集 默认设置：查询科室／医生／排班／号源 都不保存结果集 后续要改动结果集的是否保存，请从配置文件中修改
//	 * 或者从中心修改
//	 * 
//	 * @param vo
//	 * @param api
//	 */
//	public static boolean getIsSaveHisCallResult(AuthInfoVo vo, His api) {
//		String apiName = api.name();
//		String key = MessageFormat.format(ISSAVERESULT, vo.getClientId(), apiName);
//		String value = map.get(key);
//		if (null == value) {
//			// 设置调用HIS接口默认不保存结果集的配置
//			switch (api) {
//			case queryDoctor:
//				return false;
//			case queryDept:
//				return false;
//			case queryArrange:
//				return false;
//			case queryNumber:
//				return false;
//			default:
//				return true;
//			}
//		} else {
//			// 值 = 2 的时候不保存，1 的时候要保存
//			if ("2".equals(value)) {
//				return false;
//			}
//		}
//		// 其它默认都保存
//		return true;
//	}
	
	/**
	 * 返回时段对应的中文
	 * @param timeId
	 * @return
	 */
	public static String getTimeIdStr(Integer timeId) {
		if(null != timeId) {
			if(timeId == 1) return "上午";
			if(timeId == 2) return "下午";
			if(timeId == 3) return "晚上";
			if(timeId == 0) return "全天";
		}
		return "未知";
	}
	
	/**
	 * 获取微信鉴权回调跳转地址
	 * @param configKey
	 * @param clientId
	 * @param toUrl
	 * @return
	 * @throws Exception
	 */
	public static String getGotoOauthUrl(String clientId,String configKey,String toUrl) throws Exception {
		String hosUrl = getKasiteHosWebAppUrl();
		String toUrlStr = hosUrl + toUrl;
		toUrlStr = URLEncoder.encode(toUrlStr, "utf-8");
//		WeChatOauthCallBackUrl "/weixin/{0}/{1}/oauthCallback.do"
		ChannelTypeEnum type = getChannelType(clientId, configKey);
		switch (type) {
		case wechat:
			return hosUrl + MessageFormat.format(WeChatOauthGotoOauthUrl, clientId,configKey) +"?toUrl="+toUrlStr;
		case zfb:
			return hosUrl + MessageFormat.format(ZfbOauthGotoOauthUrl, clientId,configKey) +"?toUrl="+toUrlStr;
		default:
			break;
		}
		throw new RRException("未定义渠道对应的回调鉴权地址，请联系管理员。");
	}

	
	public static void setMsgTmp(String clientId,String modeType,String value) {
		String key = MessageFormat.format(MSGTEMPKEY, clientId,modeType);
		map.put(key, value);
	}
	
//	/**
//	 * 发送的模版消息
//	 * 比如预约成功后要推送多条模版消息
//	 * @param configKey
//	 * @param modeType
//	 * @return
//	 */
//	public static List<MsgTempConfig> getMsgTmp(String clientId,String modeType) {
//		/*
//		 * 系统消息模版配置
//		 * kasite.appConfig.MsgTmp.100123.10101111=
//		 * [{"channelType":" 1或2【1:微信，2:支付宝】","parmContent":{"topcolor":"#FF0000","data":{"remark":{"color":"#173177","value":" "},"keyword1":{"color":"#173177","value":"<UserName>"},"keyword2":{"color":"#173177","value":"<RegisterDate>"},"first":{"color":"#173177","value":"您好，您已取消成功"},"keyword3":{"color":"#173177","value":"<DeptName>"},"keyword4":{"color":"#173177","value":"<DoctorName>"}},"template_id":"【微信后台->模版消息->模版ID】","touser":"<operId>","url":"<URL>"},"msgType ":"1或3【1:普通客服消息，3:模版消息】"}]
//		 * */
//		List<MsgTempConfig> retVal = null;
//		String key = MessageFormat.format(MSGTEMPKEY, clientId,modeType);
//		String msgJsonStr = map.get(key);
//		if(StringUtil.isNotBlank(msgJsonStr)) {
//			JSONArray array = JSONArray.parseArray(msgJsonStr);
//			retVal = new ArrayList<>(array.size());
//			for (Object o : array) {
//				JSONObject json = (JSONObject) o;
//				MsgTempConfig config = new MsgTempConfig();
//				String channelType = json.getString("channelType");
//				String parmContent = json.getString("parmContent");
//				String msgType = json.getString("msgType");
//				if(StringUtil.isNotBlank(channelType) && channelType.equals("1")) {
//					config.setChannelType(ChannelTypeEnum.wechat);
//				}else if(StringUtil.isNotBlank(channelType) && channelType.equals("2")) {
//					config.setChannelType(ChannelTypeEnum.zfb);
//				}else {
//					LogUtil.info(logger,"字段 channelType 未定义。"+json.toJSONString());
//					continue;
//				}
//				if(StringUtil.isNotBlank(msgType) && msgType.equals(MsgTempTypeEnum.CustomMessage.getMsgType())) {
//					config.setMsgType(MsgTempTypeEnum.CustomMessage);
//				}else if(StringUtil.isNotBlank(msgType) && msgType.equals(MsgTempTypeEnum.TemplateMessage.getMsgType())) {
//					config.setMsgType(MsgTempTypeEnum.TemplateMessage);
//				}else if(StringUtil.isNotBlank(msgType) && msgType.equals(MsgTempTypeEnum.smsMessage.getMsgType())) {
//					config.setMsgType(MsgTempTypeEnum.smsMessage);
//				}else {
//					LogUtil.info(logger,"字段 msgType 未定义。"+json.toJSONString());
//					continue;
//				}
//				config.setContent(parmContent);
//				retVal.add(config);
//			}
//		}
//		return retVal;
//	}
	/**
	 * 获取业务执行成功后的消息模版跳转页面地址
	 * 需要加鉴权才能访问，如果不加鉴权地址任何地方都可以访问。
	 * @param clientId
	 * @param configKey
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public static String getServiceSuccessMessageUrl(BusinessTypeEnum busType,String clientId,String configKey,String orderId) throws Exception {
		String url = map.get(TEMPMSG_BUSSINESS_);
		if(StringUtil.isBlank(url)) {
			switch (busType) {
			case ORDERTYPE_0:
				url = "/business/yygh/yyghAppointmentDetails.html?orderId={0}";
				break;
			case ORDERTYPE_009:
				url = "/business/yygh/yyghAppointmentDetails.html?orderId={0}";
				break;
//			case ORDERTYPE_005:
//				url = "/business/yygh/yyghAppointmentDetails.html?orderId={0}";
//				break;
			case ORDERTYPE_011:
				url = "/business/order/orderReceiptList.html";
				String webAppMenu = KasiteConfig.getWxWebAppMenu();
				JSONObject json = JSONObject.parseObject(webAppMenu);
				JSONArray jsonArray = json.getJSONArray("diyMenu");
				if(jsonArray != null && jsonArray.size() > 0) {
					for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
						JSONObject object = (JSONObject) iterator.next();
						if(object.getString("fromUrl").indexOf("order/orderReceiptList.html") != -1) {
							url = object.getString("toUrl");
							break;
						}
					}
				}
				break;
			case ORDERTYPE_012:
				url = "/business/yjyy/bookinfo.html?HisKey={0}&CardType=1";
				break;
			default:
				//默认返回 null
				return null;
			}
		}
		return getGotoOauthUrl(clientId, configKey,MessageFormat.format(url, orderId));
	}

	/**
	 * 根据业务编码获取服务产品类型
	 * @param serviceId
	 * @return
	 */
	public static BusinessTypeEnum getBusinessTypeEnum(String serviceId) {
		if(serviceId != null && !serviceId.equals("999")) {
			BusinessTypeEnum[] busTypes = BusinessTypeEnum.values();
			for (BusinessTypeEnum type : busTypes) {
				if(type.getCode().equals(serviceId)) {
					return type;
				}
			}
		}
		throw new RRException("未定义的业务代码，请在：BusinessTypeEnum 中定义该产品代码。");
	}
	private final static String spstr = ",";
	public static String setCallBackToken(String start,String clientId,String configKey,String openId,String orgCode) {
		StringBuffer sbf = new StringBuffer(start);			//0
	        sbf
	        .append(spstr).append(clientId)						//1
	        .append(spstr).append(configKey)					//2
	        .append(spstr).append(openId)						//3
	        .append(spstr).append(orgCode);						//4
	        return sbf.toString();
	}
	
	public static AuthInfoVo getAuthInfo2CallBackToken(String token) {
		AuthInfoVo vo = new AuthInfoVo();
		String[] tokens = token.split(spstr);
		vo.setClientId(tokens[1]);								//1
		vo.setConfigKey(tokens[2]);								//2
		vo.setSessionKey(IDSeed.next());						
		vo.setSign(tokens[3]);									//3
		vo.setClientVersion(tokens[4]);							//4
		return vo;
	}
	private static final String wechat_url_start = "https://api.weixin.qq.com";
	private static final String wechat_pay_url_start = "https://api.mch.weixin.qq.com";
	private static final String ali_url_start = "https://openapi.alipay.com";
	/**
	 * 通过代理访问接口
	 * @param t
	 * @param sourceUrl
	 */
	public static String proxyUrl(String sourceUrl) {
		ProxyType t = null;
		if(sourceUrl.startsWith(wechat_url_start)) {
			t = ProxyType.wechat;
		}else if(sourceUrl.startsWith(wechat_pay_url_start)) {
			t = ProxyType.wechatPay;
		}else if(sourceUrl.startsWith(ali_url_start)) {
			t = ProxyType.zfb;
		}
		if(null != t) {
			String proxyUrl = null;
			String startUrl = null;
			switch (t) {
			case wechat:{
				proxyUrl = map.get(PROXY_WECHAT);
				startUrl = wechat_url_start;
				break;
			}
			case wechatPay:{
				proxyUrl = map.get(PROXY_WECHAT_PAY);
				startUrl = wechat_pay_url_start;
				break;
			}
			case zfb:{
				proxyUrl = map.get(PROXY_ZFB);
				startUrl = ali_url_start;
				break;
			}
			default:
				break;
			}
			if(StringUtil.isNotBlank(proxyUrl)) {
				sourceUrl.replaceAll(startUrl, proxyUrl);
			}
		}
		return sourceUrl;
	}
	
	
//	private final static String paramStartFlag = "_";
	/**
	 * 请求的参数模版中通过 #{_deptCode} 这个来做节点标记
	 * 
	 * deptCode = param(json.key)
	 * 
	 * @param param
	 * @param module
	 * @return
	 */
	
	private static StringBuffer replaceKey(JSONObject param,StringBuffer module,int times) {
		String key = "";
		int s = 0;
		int length = module.length();
		for (int i = 0;i<length;i++) {
			char c = module.charAt(i);
			if(c == '#' && i+1<length && module.charAt(i+1) == '{') {
				s = i;
			}
			if(c=='}') {
				key = module.substring(s+2, i);
				String str = param.getString(key);
				if(null == str) {
					str = "";
				}
				module = module.replace(s, i+1, str);
				break;
			}
		}
		if(module.indexOf("#{") > 0 && times<40) {
			times++;
			replaceKey(param, module,times);
		}
		return module;
	}
	
	public static String getCallModule(JSONObject param,String module) {
		if(StringUtil.isNotBlank(module)) {
			StringBuffer newModule = new StringBuffer(module);
			return replaceKey(param, newModule,0).toString();
		}
		return null;
	}
	
	public static String getUpdatePrivateKey() {
		String privateKey = map.get("kasite.appConfig.privateKey");
		if(StringUtil.isBlank(privateKey)) {
			return getDiyVal("UpdatePrivateKey");
		}else {
			return privateKey;
		}
	}
	/**
	 * 应用共钥  通过RSA2加密
	 * @param appId
	 * @return
	 */
	public static String getAppPublicKey(String appId) {
		return getDiyVal(appId+"_publicKey");
	}
	
	
	public static String getDiskInfo() {
		StringBuffer sb = new StringBuffer();
		File[] roots = File.listRoots();// 获取磁盘分区列表
		for (File file : roots) {
			long totalSpace = file.getTotalSpace();
//			long freeSpace = file.getFreeSpace();
			long usableSpace = file.getUsableSpace();
			if (totalSpace > 0) {
				sb.append(file.getPath() + ": (总计：");
				sb.append(Math.round(((double) totalSpace / (1024 * 1024 * 1024)) * 100 / 100.0) + "GB  ");
				if (Math.round((((double) usableSpace / (1024 * 1024 * 1024)) * 100) / 100.0) <= 1) {
					sb.append("剩余：" + Math.round((((double) usableSpace / (1024 * 1024)) * 100) / 100.0) + "MB)");
				} else {
					sb.append("剩余：" + Math.round((((double) usableSpace / (1024 * 1024 * 1024)) * 100) / 100.0)
							+ "GB)");
				}
				// sb.append("已使用" +
				// Math.round((((double)(totalSpace-usableSpace)/(1024*1024*1024))*100)/100.0) +
				// "G<br>");
			}
		}
		return sb.toString();
	}


	public static String getDiskFileList() {
		StringBuffer sb = new StringBuffer();
		String[] fileList = null;
		File[] roots = File.listRoots();// 获取硬盘分区列表；
		for (File file : roots) {
			long totalSpace = file.getTotalSpace();
			fileList = file.list();
			if (totalSpace > 0) {
				sb.append(file.getPath() + "下目录和文件：");
				for (int i = 0; i < fileList.length; i++) {
					sb.append(fileList[i] + "/n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * 获取渠道的支付方式
	 * @param clientId
	 * @param configKey
	 * @return
	 */
	public static List<String> getPayChannelType(String clientId) {
		List<String> list = new ArrayList<String>();
		String k = getClientConfig(ClientConfigEnum.WxPayConfigKey, clientId);
		if(StringUtil.isNotBlank(k)) {
			list.add(ChannelTypeEnum.wechat.name());
		}
		String k2 = getClientConfig(ClientConfigEnum.ZfbConfigKey, clientId);
		if(StringUtil.isNotBlank(k2)) {
			list.add(ChannelTypeEnum.zfb.name());
		}
		String k3 = getClientConfig(ClientConfigEnum.UnionPayConfigKey, clientId);
		if(StringUtil.isNotBlank(k3)) {
			list.add(ChannelTypeEnum.unionpay.name());
		}
		String k5 = getClientConfig(ClientConfigEnum.NetPayConfigKey, clientId);
		if(StringUtil.isNotBlank(k5)) {
			list.add(ChannelTypeEnum.netpay.name());
		}
		return list;
	}
	/**
	 * 设置数据源配置信息（一次设置，不能重复生效，修改数据库相关配置重启后才生效）
	 * @param configJsonStr
	 */
	public static void setDataSource(String configJsonStr) {
		if(StringUtil.isNotBlank(configJsonStr)) {
			map.put(DATASOURCECONFIG, JSONObject.parse(configJsonStr).toString());
		}
	}
	
	/**
	 * 获取云端数据源配置信息
	 */
	public static KasiteDataSourceConfig getKasiteDataSourceConfigs(){
		String configStr = map.get(DATASOURCECONFIG);
		if(StringUtil.isNotBlank(configStr)) {
			JSONObject json = JSONObject.parseObject(configStr);
			return JSON.toJavaObject(json, KasiteDataSourceConfig.class);
		}
		return null;
	}
}
