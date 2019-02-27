package com.kasite.core.common.sys.verification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.ChannelTypeEnum;
import com.kasite.core.common.config.KasiteAppConfigKeyEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.config.KasiteWxAndZfbConfigUtil;
import com.kasite.core.common.config.NetPayEnum;
import com.kasite.core.common.config.QyWeChatConfig;
import com.kasite.core.common.config.SwiftpassEnum;
import com.kasite.core.common.config.UnionPayEnum;
import com.kasite.core.common.config.WXConfigEnum;
import com.kasite.core.common.config.WXPayEnum;
import com.kasite.core.common.config.ZFBConfigEnum;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.threads.ThreadMessageStatus;
import com.kasite.core.common.sys.verification.vo.TokenVo;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.FileToBase64;
import com.kasite.core.common.util.GetIP;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.validator.Assert;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.HttpRequstBusSender;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

/**
 * 权限验证根目录
 * @author daiyanshui
 *
 */
public class VerificationBuser {
	public final static String LOCALPRIVATEPATHNAME = "privatekey";
	public static final String ROUTENAMELASTNAME = "route";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static VerificationBuser install;
	private final static Object obj = new Object();
	
	public static VerificationBuser create(String appId,String appSecret,String publicKey,String orgCode) {
		synchronized (obj) {
			if(null == install) {
				install = new VerificationBuser(appId, appSecret, publicKey,orgCode);
			}
			return install;
		}
	}
	public static VerificationBuser create() {
		return create(KasiteConfig.getAppId(), KasiteConfig.getAppSecret() ,KasiteConfig.getPublicKey(),KasiteConfig.getOrgCode());
	}
	
	private VerificationBuser(String appId,String appSecret,String publicKey,String orgCode) {
		this.appId = appId;
		this.appSecret = appSecret;
		this.publicKey = publicKey;
		this.orgCode = orgCode;
	}
	
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 应用安全码
	 */
	private String appSecret;
	
	/**
	 * 公钥
	 */
	private String publicKey;
	/**
	 * 当前实例的orgCode
	 */
	private String orgCode;
	
	public String getAppId() {
		return appId;
	}

	public String getAppSecret() {
		return appSecret;
	}
	
	public String getRoute(String gid,String appId) throws Exception {
		HttpRequstBusSender sender = HttpRequestBus.create(KasiteConfig.getGetOrgRoute(), RequestType.post);
		sender.addHttpParam("gid", gid);
		SoapResponseVo vo = send0(sender,appId);
		return vo.getResult();
	}
	
	private static boolean isSyncTrue = true;
	
	/**
	 * 初始化本地配置文件目录
	 * @param orgCode
	 * @throws Exception 
	 */
	public void init() throws Exception {
		LocalOAuthUtil parseOauth = LocalOAuthUtil.getInstall();
		KasiteWxAndZfbConfigUtil configUtil = KasiteWxAndZfbConfigUtil.getInstall();
		if(KasiteConfig.isConnectionKastieCenter()) {
			synchronized (getClass()) {
				LogUtil.info(logger,"连接到注册中心拉取配置文件到本地。");
				//从中心获取配置信息
				String access_token = getCenterToken(false);
				try {
					initWxAndZfbConfig(appId, access_token, configUtil,true);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("初始化配置信息异常。",e);
				}
				try {
					initPrivateKeyList(appId, access_token, parseOauth);
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("初始化配置信息异常。",e);
				}
				if(isSyncTrue) {
					ThreadMessageStatus syncThread = new ThreadMessageStatus();
					Thread t = new Thread(syncThread);
					t.setDaemon(true);
					t.setName("VerificationBuser_Thread_SyncMessage");
					t.start();
					isSyncTrue = false;
				}
			}
		}
	}
	
	/**
	 * 获取指定appId对应的配置信息
	 * @param orgCode
	 * @param access_token
	 * @return
	 * @throws Exception
	 */
	private SoapResponseVo getConfigByAppId(String appId,String access_token) throws Exception {
		return getConfigByAppId(appId,access_token,IDSeed.next());
	}
	/**
	 * 获取指定appId对应的配置信息
	 * @param orgCode
	 * @param access_token
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	private SoapResponseVo getConfigByAppId(String appId,String access_token,String gid) throws Exception {
		HttpRequstBusSender sender = HttpRequestBus.create(KasiteConfig.getAppConfig(), RequestType.post);
		sender.addHttpParam("gid", gid);
		sender.addHttpParam("appId", appId);
		SoapResponseVo vo = send0(sender,access_token);
		return vo;
	}
	
	
//	/**
//	 * 获取指定机构下的路由信息
//	 * @param orgCode
//	 * @param access_token
//	 * @return
//	 * @throws Exception
//	 */
//	private SoapResponseVo getRouteByAppId(String appId,String access_token) throws Exception {
//		return getRouteByAppId(appId,access_token,IDSeed.next());
//	}
//	/**
//	 * 获取指定机构下的路由信息
//	 * @param orgCode
//	 * @param access_token
//	 * @param gid
//	 * @return
//	 * @throws Exception
//	 */
//	private SoapResponseVo getRouteByAppId(String appId,String access_token,String gid) throws Exception {
//		HttpRequstBusSender sender = HttpRequestBus.create(KasiteConfig.getGetOrgRoute(), RequestType.post);
//		sender.addHttpParam("gid", gid);
//		sender.addHttpParam("appId", appId);
//		return send0(sender,access_token);
//	}
	
//	
//	/**
//	 * 获取我可以调用的客户端的publickey列表
//	 * @param appId
//	 * @param access_token
//	 * @param gid
//	 * @return
//	 * @throws Exception
//	 */
//	public SoapResponseVo getPublicKeyListByAppId(String appId,String access_token,String gid) throws Exception {
//		HttpRequstBusSender sender = HttpRequestBus.create(KasiteConfig.getQueryPublicKeyList(), RequestType.post);
//		sender.addHttpParam("gid", gid);
//		sender.addHttpParam("appId", appId);
//		return send0(sender,access_token);
//	}
	/**
	 * 获取可以调用我的客户端的privateKey列表
	 * @param appId
	 * @param access_token
	 * @param gid
	 * @return
	 * @throws Exception
	 */
	public SoapResponseVo getPrivateKeyListByAppId(String appId,String access_token,String gid) throws Exception {
		HttpRequstBusSender sender = HttpRequestBus.create(KasiteConfig.getQueryPrivateKeyList(), RequestType.post);
		sender.addHttpParam("gid", gid);
		sender.addHttpParam("appId", appId);
		return send0(sender,access_token);
	}
	/**
	 * 判断如果token超时重新获取token然后再调用接口
	 * @param sender
	 * @return
	 * @throws Exception
	 */
	private SoapResponseVo send0(HttpRequstBusSender sender,String access_token) throws Exception {
		sender.setHeaderHttpParam("access_token", access_token);
//		sender.addHttpParam("access_token", access_token);
		SoapResponseVo vo = sender.sendCluster();
		if(vo.getCode() == 40000) {
			vo = sender.sendCluster();
		}
		return vo;
	}

	/**
	 * 获取 可以调用我的私钥和密码 
	 * @param appId
	 * @param access_token
	 * @param localOauth
	 * @throws Exception
	 */
	private void initPrivateKeyList(String appId,String access_token,LocalOAuthUtil localOauth) throws Exception {
		SoapResponseVo privateKeyListVo = getPrivateKeyListByAppId(appId, access_token, IDSeed.next());//(appId, access_token, IDSeed.next());
		if(privateKeyListVo.getCode() == 200) {
			String appServerPropertiesListStr =privateKeyListVo.getResult();//(orgCode,appId,access_token);
			JSONObject obj = JSON.parseObject(appServerPropertiesListStr);
			int c = obj.getIntValue("code");
			if(c == 10000 || c == 0) {
				JSONArray arrs = obj.getJSONArray("result");
				for (Object object : arrs) {
					JSONObject jobj = (JSONObject) object;
					String cappId = jobj.getString("appId");
					String cappName = jobj.getString("appName");
					String corgCode = jobj.getString("orgCode");
					String privateKey = jobj.getString("privateKey");
					String appSecret = jobj.getString("appSecret");
					Properties props = new Properties();
					props.setProperty("AppId", cappId);
					props.setProperty("PrivateKey", java.net.URLDecoder.decode(privateKey, "UTF-8"));
					props.setProperty("AppSecret", java.net.URLDecoder.decode(appSecret, "UTF-8"));
					props.setProperty("OrgCode", java.net.URLDecoder.decode(corgCode, "UTF-8"));
					props.setProperty("AppName", java.net.URLDecoder.decode(cappName, "UTF-8"));
					File file = new File(getCallAppProperitesPrivateKeyDir(corgCode, cappId));
					OutputStream fw = null;
					try {
						fw = new FileOutputStream(file);
						props.store(new OutputStreamWriter(fw, "utf-8"), "lll");
//			            props.store(fw, "conmments");
			            fw.close();
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("保存调用客户端的配置信息异常。");
					}finally {
						if(null != fw) {
							fw.close();
						}
					}
					File[] fs = localOauth.listFiles();
					if(fs==null){
						return;
					}else {
						localOauth.reload(fs);
					}
				}
			}
		}
	}

	/*
	 * 
	 [
{
"msgTempCfg":[{"100123":["10101111","10101112"]}], 
"channelType": " 1或2【1:微信，2:支付宝】",
"parmContent": {
"topcolor": "#FF0000",
"data": {
"remark": {
"color": "#173177",
"value": " "
},
"keyword1": {
"color": "#173177",
"value": "<UserName>"
},
"keyword2": {
"color": "#173177",
"value": "<RegisterDate>"
},
"first": {
"color": "#173177",
"value": "您好，您已取消成功"
},
"keyword3": {
"color": "#173177",
"value": "<DeptName>"
},
"keyword4": {
"color": "#173177",
"value": "<DoctorName>"
}
},
"template_id": "【微信后台->模版消息->模版ID】",
"touser": "<operId>",
"url": "<URL>"
},
"msgType ": "1或3【1:普通客服消息，3:模版消息】"
}]
	 */
	
	/**
	 * 重新从服务端抓取机构的配置信息
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	private void initWxAndZfbConfig(String appId,String access_token,KasiteWxAndZfbConfigUtil configUtil,boolean isFirst) throws Exception {
		SoapResponseVo retVo = getConfigByAppId(appId,access_token);
		if(retVo.getCode() == 200) {
			String result =retVo.getResult();//(orgCode,appId,access_token);
			JSONObject obj = JSON.parseObject(result);
			int c = obj.getIntValue("code");
			if(c == 10000 || c == 0) {
				KasiteConfig.clear();
				String res = obj.getString("result");
				JSONArray array = JSON.parseArray(DesUtil.decrypt(res, "utf-8"));
				Properties props = new Properties();
				KasiteConfig.print(array.toJSONString());
				if(null != array && array.size() > 0) {
					for (Object o : array) {
						JSONObject json = (JSONObject) o;
						Integer configId = json.getInteger("id");
						KasiteConfig.setConfigId(configId);
//						String clientId = json.getString(KasiteAppConfigKeyEnum.clientid.name());

						//微信公众号相关配置 =======================================================start
						JSONArray arrs = json.getJSONArray(KasiteAppConfigKeyEnum.wx.name());
						if(null != arrs) {
							for (Object object : arrs) {
								JSONObject jobj = (JSONObject) object;
								WXConfigEnum[] enums = WXConfigEnum.values();
								String id = jobj.getString(WXConfigEnum.wx_original_id.name());
								for (WXConfigEnum e : enums) {
									String value = jobj.getString(e.name());
									if(null == value) {
										value = "";
									}
									props.setProperty(KasiteWxAndZfbConfigUtil.getWxConfigKey(e, id), value);
								}
							}
						}
						//微信公众号相关配置 =======================================================end
						
						//获取微信配置信息
						//微信商户关配置 =======================================================start
						arrs = json.getJSONArray(KasiteAppConfigKeyEnum.wxpay.name());
						if(null != arrs) {
							KasiteConfig.print(arrs.toJSONString());
							for (Object object : arrs) {
								JSONObject jobj = (JSONObject) object;
								WXPayEnum[] enums = WXPayEnum.values();
								String id = jobj.getString(WXPayEnum.wx_pay_key.name());
//								props.setProperty(KasiteWxAndZfbConfigUtil.getOAuth(ChannelTypeEnum.wechat, ClientIdAndConfigKeyOAuthEnum.wxpay, clientId), id);
								for (WXPayEnum e : enums) {
									String value = jobj.getString(e.name());
									if(null == value) {
										value = "";
									}
									props.setProperty(KasiteWxAndZfbConfigUtil.getWxPayKey(e, id), value);
								}
								props.setProperty(KasiteWxAndZfbConfigUtil.getWxPayKey(WXPayEnum.channelType, id), ChannelTypeEnum.wechat.name());
								
								//如果存在证书则将证书写入文件保存
								String wx_cert = jobj.getString(WXPayEnum.wx_cert.name());
								String filePath = "";
								if(StringUtil.isNotBlank(wx_cert)) {
									filePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(WXPayEnum.wx_cert,id);
									FileToBase64.toFile(wx_cert, filePath);
								}
								props.setProperty(KasiteWxAndZfbConfigUtil.getWxPayKey(WXPayEnum.wx_cert_path, id), filePath);
								
								String wx_parent_cert = jobj.getString(WXPayEnum.wx_parent_cert.name());
								String wxParent_CertFilePath = "";
								if(StringUtil.isNotBlank(wx_parent_cert)) {
									wxParent_CertFilePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(WXPayEnum.wx_parent_cert,id);
									FileToBase64.toFile(wx_parent_cert, wxParent_CertFilePath);
								}
								props.setProperty(KasiteWxAndZfbConfigUtil.getWxPayKey(WXPayEnum.wx_parent_cert_path, id), wxParent_CertFilePath);
							}
						}
						//微信商户关配置 =======================================================end
						
						//支付宝相关配置 =======================================================start
						arrs = json.getJSONArray(KasiteAppConfigKeyEnum.zfb.name());
						if(null != arrs) {
							for (Object object : arrs) {
								JSONObject jobj = (JSONObject) object;
								ZFBConfigEnum[] enums = ZFBConfigEnum.values();
								String id = jobj.getString(ZFBConfigEnum.zfb_appId.name().toLowerCase());
								for (ZFBConfigEnum e : enums) {
									String value = jobj.getString(e.name().toLowerCase());
									if(null == value) {
										value = "";
									}
									props.setProperty(KasiteWxAndZfbConfigUtil.getZfbKey(e, id), value);
								}
								props.setProperty(KasiteWxAndZfbConfigUtil.getZfbKey(ZFBConfigEnum.channelType, id), ChannelTypeEnum.zfb.name());
							}
						}
						//支付宝相关配置 =======================================================end
						
						//微信掌医H5菜单配置 =======================================================start
						String webappMenu = json.getString(KasiteAppConfigKeyEnum.webapp_menu.name());
						if(StringUtil.isNotBlank(webappMenu)) {
							props.setProperty(KasiteAppConfigKeyEnum.webapp_menu.name(), webappMenu);
						}
						//微信掌医H5菜单配置 =======================================================end
						
						//支付宝掌医菜单配置 =======================================================start
						String webappMenuzfb = json.getString(KasiteAppConfigKeyEnum.webapp_menu_zfb.name());
						if(StringUtil.isNotBlank(webappMenuzfb)) {
							props.setProperty(KasiteAppConfigKeyEnum.webapp_menu_zfb.name(), webappMenuzfb);
						}
						//支付宝掌医菜单配置 =======================================================end
						
						//数据源配置 =======================================================start
						String dataSourceConfigStr = json.getString(KasiteAppConfigKeyEnum.datasource_master.name());
						if(StringUtil.isNotBlank(dataSourceConfigStr)) {
//							props.setProperty(KasiteAppConfigKeyEnum.datasource_master.name(), dataSourceConfigStr);
							KasiteConfig.setDataSource(dataSourceConfigStr);
						}
						//数据源配置 =======================================================end
						
						
						Set<String> keySet = json.keySet();
						for (String key : keySet) {
							//第三方入口渠道配置 =======================================================start
							String startW = "clientid_";
							if(key.toLowerCase().startsWith(startW)) {
								String k = key.substring(startW.length());
								KasiteConfig.addClientId(k);
								JSONObject v = json.getJSONObject(key);
								Set<String> keySet2 = v.keySet();
								for (String clientKey : keySet2) {
									String value = v.getString(clientKey);
									if(StringUtil.isNotBlank(value)) {
										String rk = KasiteWxAndZfbConfigUtil.getClientKey(clientKey, k);
										props.setProperty(rk, value);
									}
								}
							}
							//第三方入口渠道配置 =======================================================end
							//支付渠道配置 =======================================================start
							String startPayConfig = "paychannel_";
							if(key.toLowerCase().startsWith(startPayConfig)) {
								ChannelTypeEnum[] channelTypes = ChannelTypeEnum.values();
								for (ChannelTypeEnum channelType : channelTypes) {
									String name = (startPayConfig+channelType.name());
									if(key.equals(name)) {
										JSONArray v = json.getJSONArray(key);
										switch (channelType) {
										case netpay:{
											initPayChannelType_Netpay(v, props);
											break;
										}
										case unionpay:{
											initPayChannelType_UnionPay(v, props);
											break;
										}
										case swiftpass:{
											initPayChannelType_Swiftpass(v, props);
											break;
										}
										default:
											break;
										}
									}
								}
							}
							//支付渠道配置 =======================================================end
						}
						//个性化配置=======================================================start
						String diy_config = json.getString(KasiteAppConfigKeyEnum.diy_config.name());
						if(StringUtil.isNotBlank(diy_config)) {
							JSONObject diyJson = JSONObject.parseObject(diy_config);
							Set<String> keys = diyJson.keySet();
							for (String key : keys) {
								KasiteConfig.setDiyParam(props, key, diyJson.getString(key));
							}
						}
						//个性化配置=======================================================end
						
						//企业微信配置=======================================================start
						String text = json.getString(KasiteAppConfigKeyEnum.qywechat_config.name());
						if(StringUtil.isNotBlank(text)) {
							JSONArray diyJson = JSONArray.parseArray(text);
							for (Object object : diyJson) {
								JSONObject qywxJson = (JSONObject) object;
								QyWeChatConfig[] enums = QyWeChatConfig.values();
								String configKey = qywxJson.getString(QyWeChatConfig.configkey.name());
								for (QyWeChatConfig e : enums) {
									if(QyWeChatConfig.configkey != e) {
										String value = qywxJson.getString(e.name());
										KasiteConfig.setQyWeChatConfig(e, configKey, value);
									}
								}
							}
						}
						//企业微信配置=======================================================end
					}
					
					
					
					
				}
				
				File file = new File(getWxAndZfbConfigPrivateKeyDir());
				OutputStream fw = null;
				try {
					fw = new FileOutputStream(file);
					props.store(new OutputStreamWriter(fw, "utf-8"), "lll");
//		            props.store(fw, "conmments");
		            fw.close();
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("保存调用客户端的配置信息异常。");
				}finally {
					if(null != fw) {
						fw.close();
					}
				}
				File[] fs = configUtil.listFiles();
				if(fs==null){
					return;
				}else {
					configUtil.reload(fs);
				}
			}else if( c == 504) {
				
				logger.info("该AppId未在服务端配置相应配置信息。appid="+appId);
				
			}else if( c == 40000) {
				if(isFirst) {
					//如果是第一次请求就40000 就重新再申请一次
					initWxAndZfbConfig(appId, getCenterToken(true), configUtil, false);
				}
				logger.info("token 失效重新获取token。appid="+appId);
			}else {
				LogUtil.info(logger, "服务异常，未取到应用配置信息：code = "+c);
			}
		}
		
	}

	/**
	 * 从云端初始化本地 威富通配置
	 * @param arrs
	 * @param props
	 */
	private void initPayChannelType_Swiftpass(JSONArray arrs,Properties props) {
		if(null != arrs) {
			KasiteConfig.print(arrs.toJSONString());
			for (Object object : arrs) {
				JSONObject jobj = (JSONObject) object;
				SwiftpassEnum[] enums = SwiftpassEnum.values();
				String id = jobj.getString(SwiftpassEnum.configKey.name());
				for (SwiftpassEnum e : enums) {
					String value = jobj.getString(e.name());
					if(null == value) {
						value = "";
					}
					props.setProperty(KasiteWxAndZfbConfigUtil.getSwiftpassKey(e, id), value);
				}
			}
		}
	}
	/**
	 * 从云端初始化本地一网通配置
	 * @param arrs
	 * @param props
	 */
	private void initPayChannelType_Netpay(JSONArray arrs,Properties props) {
		/*
appConfig.100123.NetPayConfigKey=NetPay000013
appConfig.100125.NetPayConfigKey=NetPay000013
NetPay.NetPay000013.branchNo=0595
NetPay.NetPay000013.merchantNo=000013
NetPay.NetPay000013.secretKey=sIJ7bZAVetd1p1cE
NetPay.NetPay000013.operatorNo=9999
		 */
		if(null != arrs) {
			KasiteConfig.print(arrs.toJSONString());
			for (Object object : arrs) {
				JSONObject jobj = (JSONObject) object;
				NetPayEnum[] enums = NetPayEnum.values();
				String id = jobj.getString(NetPayEnum.configKey.name());
//				props.setProperty(KasiteWxAndZfbConfigUtil.getOAuth(ChannelTypeEnum.wechat, ClientIdAndConfigKeyOAuthEnum.wxpay, clientId), id);
				for (NetPayEnum e : enums) {
					String value = jobj.getString(e.name());
					if(null == value) {
						value = "";
					}
					props.setProperty(KasiteWxAndZfbConfigUtil.getNetPayKey(e, id), value);
				}
			}
		}
	}
	/**
	 * 从云端初始化本地银联配置
	 * @param arrs
	 * @param props
	 * @throws Exception
	 */
	private void initPayChannelType_UnionPay(JSONArray arrs,Properties props) throws Exception {
		//银联相关配置信息
		if(null != arrs) {
			KasiteConfig.print(arrs.toJSONString());
			for (Object object : arrs) {
				JSONObject jobj = (JSONObject) object;
				UnionPayEnum[] enums = UnionPayEnum.values();
				String id = jobj.getString(UnionPayEnum.merId.name());
//				props.setProperty(KasiteWxAndZfbConfigUtil.getOAuth(ChannelTypeEnum.wechat, ClientIdAndConfigKeyOAuthEnum.wxpay, clientId), id);
				for (UnionPayEnum e : enums) {
					String value = jobj.getString(e.name());
					if(null == value) {
						value = "";
					}
					props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(e, id), value);
				}
				props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(UnionPayEnum.merId, id), ChannelTypeEnum.unionpay.name());
				
				//encryptCert 如果存在证书则将证书写入文件保存
				String encryptCert = jobj.getString(UnionPayEnum.encryptCert.name());
				String filePath = "";
				if(StringUtil.isNotBlank(encryptCert)) {
					filePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(UnionPayEnum.encryptCert,id,".cer");
					FileToBase64.toFile(encryptCert, filePath);
				}
				props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(UnionPayEnum.encryptCertPath, id), filePath);
				
				//signCert
				String signCert = jobj.getString(UnionPayEnum.signCert.name());
				filePath = "";
				if(StringUtil.isNotBlank(signCert)) {
					filePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(UnionPayEnum.signCert,id,".pfx");
					FileToBase64.toFile(signCert, filePath);
				}
				props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(UnionPayEnum.signCertPath, id), filePath);
				
				//middleCert
				String middleCert = jobj.getString(UnionPayEnum.middleCert.name());
				filePath = "";
				if(StringUtil.isNotBlank(middleCert)) {
					filePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(UnionPayEnum.middleCert,id,".cer");
					FileToBase64.toFile(middleCert, filePath);
				}
				props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(UnionPayEnum.middleCertPath, id), filePath);
				
				//rootCert
				String rootCert = jobj.getString(UnionPayEnum.rootCert.name());
				filePath = "";
				if(StringUtil.isNotBlank(rootCert)) {
					filePath = KasiteWxAndZfbConfigUtil.getInstall().getCertFilePath(UnionPayEnum.rootCert,id,".pfx");
					FileToBase64.toFile(rootCert, filePath);
				}
				props.setProperty(KasiteWxAndZfbConfigUtil.getUnionPayKey(UnionPayEnum.rootCertPath, id), filePath);
			}
		}
	}
	
	/**
	 * 查询可以调用的配置文件的地址
	 * @return
	 */
	public String getCallAppProperitesPrivateKeyDir(String orgCode,String appId) {
		String localConfigPath = KasiteConfig.localConfigPath();
		Assert.isBlank(localConfigPath, "本地配置文件目录不能为空。请确认本地配置文件目录的正确性");
		StringBuffer sbf = new StringBuffer();
		sbf.append(localConfigPath).append(File.separator).append(LocalOAuthUtil.DIRNAME);
		File f = new File(sbf.toString());
		if(!f.exists()) {
			f.mkdirs();
		}
		sbf.append(File.separator).append(appId).append(".properties");
		logger.debug("写入配置信息：privateKeyList = "+ sbf.toString());
		return sbf.toString();
	}
	
	/**
	 * 查询可以调用的配置文件的地址
	 * @return
	 */
	public String getWxAndZfbConfigPrivateKeyDir() {
		String localConfigPath = KasiteConfig.localConfigPath();
		Assert.isBlank(localConfigPath, "本地配置文件目录不能为空。请确认本地配置文件目录的正确性");
		StringBuffer sbf = new StringBuffer();
		sbf.append(localConfigPath).append(File.separator).append(KasiteWxAndZfbConfigUtil.DIRNAME);
		File f = new File(sbf.toString());
		if(!f.exists()) {
			f.mkdirs();
		}
		sbf.append(File.separator).append("sys.properties");
		logger.debug("写入配置信息：sys.properties = "+ sbf.toString());
		return sbf.toString();
	}
	
	
	/**
	 * 获取一个新的 accessToken 保存到本地。
	 * @return
	 * @throws Exception
	 */
	public TokenVo refreshToken(String getTokenUrl) throws Exception {
		//获取密文
		String encrypt = getPackage_crypto();
		logger.debug("请求密文："+ encrypt);
		logger.debug("orgCode:"+ orgCode);
		logger.debug("appId:"+appId);
		String localIp = GetIP.getLocalIP();
		String signType = KasiteConfig.getSignType();
		SoapResponseVo vo = HttpRequestBus.create(getTokenUrl, RequestType.post)
				.addHttpParam("encrypt", encrypt)
				.addHttpParam("signType", signType)
				.addHttpParam("orgCode", orgCode)
				.addHttpParam("appId", appId)
				.addHttpParam("localIp", localIp)
				.sendCluster();//HttpRequestBus.create(vurl, RequestType.get).send();
		int code = vo.getCode();
		if(code == 200) {
			String result = vo.getResult();
			JSONObject robj = JSON.parseObject(result);
			int rcode = robj.getInteger("code");
			if(rcode == 0 ||rcode == 10000) {
				String access_token = robj.getString("access_token");
				String invalid_time = robj.getString("invalid_time");
				String create_time = robj.getString("create_time");
				String now_time = robj.getString("now_time");
				TokenVo newtokenVo = new TokenVo();
				newtokenVo.setAccess_token(access_token);
				newtokenVo.setCreate_time(create_time);
				newtokenVo.setInvalid_time(invalid_time);
				newtokenVo.setNow_time(now_time);
				String nowMyTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
				if(nowMyTime.compareTo(invalid_time) > 0) {
					throw new RRException("双方服务器时间不一致，需要重新匹配下双方服务器时间。");
				}
				return newtokenVo;
			}else {
				logger.error("获取 access_token 异常：\r\n Url: {} \r\n Error: {}", getTokenUrl,vo.getResult());
				throw new RRException(vo.getResult());
			}
		}else {
			logger.error("调用接口异常：\r\n Url: {} \r\n Error: {}",getTokenUrl,vo.getResult());
			throw new RRException(vo.getCode()+":"+ vo.getResult());
		}
	}
	
	private TokenVo centerTokenVo;
	private Map<String, TokenVo> appTokenVo = new HashMap<>();
	/**
	 * 获取指定 应用的toekn 如果token 返回为空需要重新获取。
	 * @param api
	 * @return
	 * @throws ParseException
	 */
	public TokenVo getToken(String api) throws ParseException {
		Assert.isBlank(api, "api 不能为空。");
		TokenVo tokenVo = appTokenVo.get(api);
		if(null != tokenVo) {
			String invalidTime = tokenVo.getInvalid_time();
			String nowTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
			if(invalidTime.compareTo(nowTime) > 0) {
				return tokenVo;
			}
		}
		return null;
	}
//	/**
//	 * 获取 需要调用的 服务端的 access_token
//	 * @param orgCode
//	 * @param api
//	 * @param vo
//	 * @return
//	 * @throws Exception
//	 */
//	public TokenVo getToken(String appId,String api,MappingRouteVo vo) throws Exception {
//		Assert.isBlank(appId, "appId 不能为空。");
//		TokenVo tokenVo = getToken(api);
//		if(null != tokenVo) {
//			return tokenVo;
//		}
//		String[] tmp = api.split("\\.");
//		
//		String clazz = vo.getRemoteClass();
//		if(null != clazz && clazz.indexOf("/")>=0) {///api/verificat
//			//如果路径是 api/b 说明是通过 rest方式调用。如果是  class的全路径说明是通过 rpc的方式访问。但是在对端都是通过 http进行访问只是2者访问的路径不一样
//			clazz = clazz +"/"+tmp[2];
//		}else {
//			throw new RRException("请配置调用应用的 getToken 的路由api "+ api);
//		}
//		
//		Url[] urlss = vo.getUrl();
//		StringBuffer urlsbf = new StringBuffer();
//		for (int i = 0; i < urlss.length; i++) {
//			Url url = urlss[i];
//			String u = url.getIp();
//			if(u.startsWith("http://") ||u.startsWith("https://")) {
//				urlsbf.append(url.getIp()).append("/").append(clazz).append(",");
//			}else {
//				throw new RRException("配置的路由代理地址请求地址（ proxyUrl ）必须是 http:// 或者 https:// 的地址");
//			}
//		}
//		if(StringUtils.isBlank(urlsbf.toString())) {
//			throw new RRException("请求的服务端api地址未开通外网访问服务，请与管理员联系配置外网访问地址。");
//		}
//		String getTokenUrl = urlsbf.substring(0, urlsbf.length()-1);
//		tokenVo = refreshToken(getTokenUrl);
//		appTokenVo.put(api, tokenVo);
//		return tokenVo;
//	}
	
	/**
	 * 从中心获取 access_token
	 * @param isNew 是否获取最新token
	 * @return
	 * @throws Exception
	 */
	public String getCenterToken(boolean isNew) throws Exception {
		String getTokenUrl = KasiteConfig.getCenterTokenUrl();
		Assert.isBlank(orgCode, "orgCode 不能为空。");
		Assert.isBlank(getTokenUrl, "getTokenUrl 不能为空。");
		if(null != centerTokenVo && !isNew) {
			String invalidTime = centerTokenVo.getInvalid_time();
			String nowTime = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
			if(invalidTime.compareTo(nowTime) > 0) {
				return centerTokenVo.getAccess_token();
			}
		}
		logger.debug("获取最新的token "+ getTokenUrl);
		centerTokenVo = refreshToken(getTokenUrl);
		logger.debug("获取到最新到 access_token="+centerTokenVo.getAccess_token());
		logger.debug("access_token 失效时间 invalid_time = "+ centerTokenVo.getInvalid_time());
		logger.debug("服务端时间：now = "+ centerTokenVo.getNow_time());
		logger.debug("access_token 创建时间：create_time = "+ centerTokenVo.getCreate_time());
		
		
		return centerTokenVo.getAccess_token();
	}
	
	//通过 公钥加密后获得私钥并向平台获取一个 token
	private String getPackage_crypto() throws Exception {
//		String pk = publicKey;
//		if(StringUtil.isBlank(pk)) {
//			publicKey = getPublicKey(orgCode);
//		}
		Assert.isBlank(appId, "appId 不能为空。");
		Assert.isBlank(appSecret, "appSecret 不能为空。");
		String now = DateOper.getNow("yyyy-MM-dd HH:mm:ss");
		String data = appId + ","+appSecret +","+orgCode +","+ now;
		//加密后的密文
		if("RSA2".equals(KasiteConfig.getSignType())) {
			//应答方返回的报文
			String content = data;
			String encryContent = KasiteRSAUtil.rsaEncrypt(content, publicKey, "UTF-8");
			return encryContent;
		}else {
			String encryptStr = RSA.encrypt(data, RSA.genPublicKey(publicKey));
			return encryptStr;
		}
		
		
	}
}
