package com.kasite.core.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.handler.IWechatAndZfbConfigHandler;
import com.kasite.core.common.util.StringPool;
import com.kasite.core.httpclient.http.StringUtils;

public class KasiteWxAndZfbConfigUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(KasiteWxAndZfbConfigUtil.class);
	public static final String DIRNAME = "config";
	public static final String CERT = "cert";
	private static final String FILEWIDTH = ".properties";
	public static final String WEBAPPMENU = "WebAppMenu";
	public static final String WEBAPPMENUZFB = "WebAppMenuzfb";
	//注入微信和支付宝的配置表信息，如果有存在则读取配置表信息，如果不存在则直接返回本地配置文件信息
	//通过外部启动的时候注入，如果不注入则默认走本地配置文件读取
	private IWechatAndZfbConfigHandler handler;
	public void setIWechatAndZfbConfigHandler(IWechatAndZfbConfigHandler handler) {
		this.handler = handler;
	}
	private static KasiteWxAndZfbConfigUtil install;
	public static KasiteWxAndZfbConfigUtil getInstall() {
		if(null == install) {
			synchronized (KasiteWxAndZfbConfigUtil.class) {
				if(null == install) {
					install = new KasiteWxAndZfbConfigUtil();
				}
			}
		}
		return install;
	}
	private Properties properties = null;
	public JSONObject print() {
		JSONObject o =  (JSONObject) JSON.toJSON(properties);
		logger.info("系统配置信息：KasiteWxAndZfbConfig : "+o.toJSONString());
   		return o;
	}
	/**
	 *	获取微信菜单配置
	 * @return
	 */
	public String getWxWebAppMenu() {
		if(null != properties) {
			return properties.getProperty(KasiteAppConfigKeyEnum.webapp_menu.name());
		}
		return null;
	}
	/**
	 *	获取微信菜单配置
	 * @return
	 */
	public String getZfbWebAppMenu() {
		if(null != properties) {
			return properties.getProperty(KasiteAppConfigKeyEnum.webapp_menu_zfb.name());
		}
		return null;
	}
	/**
	 * 获取配置文件中的值
	 * @param key
	 * @param id 传入微信公众号Key wx_app_id 来定位
	 * @return
	 */
	public String getValue(WXConfigEnum key,String id) {
		if(null != handler) {
			return handler.getWeChatConfig(key, id);
		}else if(null != properties) {
			String keyStr = getWxConfigKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	/**
	 * 获取配置文件中的值
	 * @param key
	 * @param id 传入微信支付Key wx_pay_key 来定位
	 * @return
	 */
	public String getValue(WXPayEnum key,String id) {
		if(null != handler) {
			return handler.getWeChatPay(key, id);
		}else if(null != properties) {
			String keyStr = getWxPayKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的值
	 * @param key 
	 * @param id 传入支付宝id  zfb_appId
	 * @return
	 */
	public String getValue(ZFBConfigEnum key,String id) {
		if(null != handler) {
			return handler.getZfbConfig(key, id);
		}else if(null != properties) {
			String keyStr = getZfbKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的值
	 * @param key 
	 * @param id 传入支付宝id  zfb_appId
	 * @return
	 */
	public String getValue(UnionPayEnum key,String id) {
		if(null != handler) {
			return handler.getUnionPayConfig(key, id);
		}else if(null != properties) {
			String keyStr = getUnionPayKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的值
	 * @param key 
	 * @param id 传入支付宝id  zfb_appId
	 * @return
	 */
	public String getValue(SwiftpassEnum key,String id) {
		if(null != handler) {
			return handler.getSwiftpassConfig(key, id);
		}else if(null != properties) {
			String keyStr = getSwiftpassKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的值
	 * @param key 
	 * @param id 传入支付宝id  zfb_appId
	 * @return
	 */
	public String getValue(NetPayEnum key,String id) {
		if(null != handler) {
			return handler.getNetPayConfig(key, id);
		}else if(null != properties) {
			String keyStr = getNetPayKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的值
	 * @param key 
	 * @param id 传入支付宝id  zfb_appId
	 * @return
	 */
	public String getValue(DragonPayEnum key,String id) {
		if(null != handler) {
			return handler.getDragonPayConfig(key, id);
		}else if(null != properties) {
			String keyStr = getDragonPayKey(key, id);
			return properties.getProperty(keyStr);
		}
		return null;
	}
	
	/**
	 * 获取证书的保存地址，保存系统中的证书  后缀名 = .p12
	 * @return
	 */
	public String getCertFilePath(WXPayEnum e,String configKey) {
		String path = KasiteConfig.localConfigPath()+File.separator+DIRNAME+File.separator+CERT+File.separator+configKey;
		if(StringUtils.isNotBlank(path)) {
			File oauthFile = new File(path);
			if(!oauthFile.exists() || !oauthFile.isDirectory()) {
				oauthFile.mkdirs();
			}
		}
		return path+File.separator+e.name()+".p12";
	}
	/**
	 * 获取证书的保存地址，保存系统中的证书  后缀名 = .p12
	 * @return
	 */
	public String getCertFilePath(UnionPayEnum e,String configKey,String suffix) {
		String path = KasiteConfig.localConfigPath()+File.separator+DIRNAME+File.separator+CERT+File.separator+configKey;
		if(StringUtils.isNotBlank(path)) {
			File oauthFile = new File(path);
			if(!oauthFile.exists() || !oauthFile.isDirectory()) {
				oauthFile.mkdirs();
			}
		}
		return path+File.separator+e.name()+suffix;
	}
	public File[] listFiles() {
		String path = KasiteConfig.localConfigPath()+File.separator+DIRNAME;
		if(StringUtils.isNotBlank(path)) {
			File oauthFile = new File(path);
			if(oauthFile.exists() && oauthFile.isDirectory()) {
				return oauthFile.listFiles();
			}else {
				oauthFile.mkdirs();
			}
		}
		return null;
	}
	
	private KasiteWxAndZfbConfigUtil() {
		//否则从本地读取数据
		FileWatcher fw=new FileWatcher(this);
		try {
			fw.deal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread t=new Thread(fw);
		t.setDaemon(true);
		t.setName("KasiteWxAndZfbConfig_SyncLocalFileThread");
		t.start();
	}
	
	public void reload(File[] fs) {
		if(null != fs) {
			for (File file : fs) {
				String pname = file.getName();
				if (pname.endsWith(FILEWIDTH)) {
//					Properties p = new Properties();
					BufferedReader br = null;
					InputStream in = null;
					try {
						in = new FileInputStream(file);
//						p.load(in);
						Properties datas = new Properties();
						br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
						datas.load(br);
						Set<Map.Entry<Object,Object>> sets = datas.entrySet();
						for (Map.Entry<Object,Object> map : sets) {
							String key = map.getKey().toString();
							String value = map.getValue().toString();
							KasiteConfig.setValue("kasite."+key, value);
						}
						logger.info("加载读取配置文件："+ file.getPath());
						this.properties = datas;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 掌医应用相关配置 
	 */
	public final static String APPCONFIG = "appConfig";
	/**
	 * 微信公众号相关配置 
	 */
	public final static String WXCONFIG = "WxConfig";
	/**
	 * 微信支付相关配置 
	 */
	public final static String WXPAY = "WxPay";
	/**
	 * 支付宝相关配置 
	 */
	public final static String ZFB = "Zfb";
	/**
	 * 银联相关配置 
	 */
	public final static String UNIONPAY = "UnionPay";
	/**
	 * 威富通相关配置 
	 */
	public final static String SWIFTPASS = "Swiftpass";
	/**
	 * 招行一网通相关配置 
	 */
	public final static String NETPAY = "NetPay";
	/**
	 * 建行相关配置
	 */
	public final static String DRAGONPAY = "DragonPay";
	
	
	public final static String SPOM = StringPool.PERIOD;
	
	public static String getWxConfigKey(WXConfigEnum key, String id) {
		StringBuffer sbf = new StringBuffer(WXCONFIG);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getWxPayKey(WXPayEnum key, String id) {
		StringBuffer sbf = new StringBuffer(WXPAY);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getZfbKey(ZFBConfigEnum key, String id) {
		StringBuffer sbf = new StringBuffer(ZFB);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getUnionPayKey(UnionPayEnum key, String id) {
		StringBuffer sbf = new StringBuffer(UNIONPAY);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getSwiftpassKey(SwiftpassEnum key, String id) {
		StringBuffer sbf = new StringBuffer(SWIFTPASS);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getNetPayKey(NetPayEnum key, String id) {
		StringBuffer sbf = new StringBuffer(NETPAY);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}

	public static String getDragonPayKey(DragonPayEnum key, String id) {
		StringBuffer sbf = new StringBuffer(DRAGONPAY);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	
	public static String getClientKey(ClientConfigEnum key, String id) {
		StringBuffer sbf = new StringBuffer(APPCONFIG);
		sbf.append(SPOM).append(id).append(SPOM).append(key.name());
		return sbf.toString();
	}
	public static String getClientKey(String key, String id) {
		StringBuffer sbf = new StringBuffer(APPCONFIG);
		sbf.append(SPOM).append(id).append(SPOM).append(key);
		return sbf.toString();
	}
	
	private static class FileWatcher implements Runnable{
		Map<String,Long> map=new HashMap<String,Long>();
		private static long delay = 20000;
		private KasiteWxAndZfbConfigUtil parser;
		static { 
			logger.debug("配置文件动态加载时间："+delay);
		}
		private FileWatcher(KasiteWxAndZfbConfigUtil parser) {
			this.parser = parser;
		}
		void deal() throws Exception
		{
			boolean modify=false;
			File[] fs = parser.listFiles();
			if(fs==null){
				return;
			}
			Map<String,Long> newMap=new HashMap<String,Long>();
			for(File f:fs){
				newMap.put(f.getName(), f.lastModified());
				Long v=map.get(f.getName());
				if(v==null || !v.equals(f.lastModified())){
					modify=true;
				}
			}
			if(map.size()!=newMap.size()){
				modify=true;
			}
			if(modify){
				parser.reload(fs);
				this.map=newMap;
			}
		}
		@Override
		public void run() {
			while(true)
			{
				try 
				{
					Thread.sleep(delay);
					deal();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

	}
}
