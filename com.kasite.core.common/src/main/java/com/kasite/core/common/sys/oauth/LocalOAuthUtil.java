package com.kasite.core.common.sys.oauth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.config.AppInfoEnum;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.oauth.entity.AppAccessToken;
import com.kasite.core.common.sys.oauth.entity.AppEntity;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.common.validator.Assert;
import com.kasite.core.httpclient.http.StringUtils;

public class LocalOAuthUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(LocalOAuthUtil.class);
	private static LocalOAuthUtil install;
	public static LocalOAuthUtil getInstall() {
		if(null == install) {
			synchronized (LocalOAuthUtil.class) {
				if(null == install) {
					install = new LocalOAuthUtil();
				}
			}
		}
		return install;
	}
	private final static String URL = "Url";
	public final static String DIRNAME = "oauth";
	
	private String getPropertiesValue(Map<String,Properties> m,String appId,String key) {
		if(null != m) {
			Properties p = m.get(appId);
			if(null != p) {
				return p.getProperty(key);
			}
		}
		return null;
	}
	
	public String getPrivateKey(String appId) {
		return getPropertiesValue(privateKeyMap, appId, AppInfoEnum.PrivateKey.name());
	}
	
	public String getAppSecret(String appId) {
		return getPropertiesValue(privateKeyMap, appId, AppInfoEnum.AppSecret.name());
	}
	public String getUrl(String appId) {
		return getPropertiesValue(privateKeyMap, appId, URL);
	}
	public String getAppName(String appId) {
		return getPropertiesValue(privateKeyMap, appId, AppInfoEnum.AppName.name());
	}
//	Cache<String, AppAccessToken> appAccessTokenMap =CacheBuilder.newBuilder().maximumSize(200l).build();
	private ExpiryMap<String, AppAccessToken> appAccessTokenMap = new ExpiryMap<>();
	private Map<String, AppEntity> appEntityMap = new HashMap<>();
	
	public AppAccessToken getAccessToken(String accessToken) {
		return appAccessTokenMap.get(accessToken);
	}
	
	public void addAccessToken(long lv, String appId,String accessToken,String orgCode,String create_time,String invalidTime) {
		Assert.isBlank(appId, "appId 不能为空");
		Assert.isBlank(accessToken, "accessToken 不能为空");
		Assert.isBlank(accessToken, "orgCode 不能为空");
		
		AppAccessToken token = new AppAccessToken();
		token.setAccessToken(accessToken);
		token.setAppId(appId);
		token.setCreateTime(create_time);
		token.setInvalidTime(invalidTime);
		appAccessTokenMap.put(accessToken, token, lv + 20000);
		
		AppEntity app = new AppEntity();
		app.setAppId(appId);
		app.setAppName(getAppName(appId));
		app.setCreateTime(DateOper.getNowDate());
		appEntityMap.put(appId, app);
	}
	private Map<String,Properties> privateKeyMap = new HashMap<>();
	
	public void init() {
		File[] fs = listFiles();
		reload(fs);
	}
	public File[] listFiles() {
		String oauthPath = KasiteConfig.localConfigPath()+File.separator+DIRNAME;
		if(StringUtils.isNotBlank(oauthPath)) {
			File oauthFile = new File(oauthPath);
			if(oauthFile.exists()) {
				return oauthFile.listFiles();
			}
		}
		return null;
	}
	
	private LocalOAuthUtil() {
		//否则从本地读取数据
		FileWatcher fw=new FileWatcher(this);
		try {
			fw.deal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread t=new Thread(fw);
		t.setName("LocalOauthUtil_SyncLocalFile_Thread");
		t.setDaemon(true);
		t.start();
	}
	public void reload(File[] fs) {
		if(null != fs) {
			for (File file : fs) {
				String pname = file.getName();
				if (pname.endsWith(".properties")) {
					String appId = pname.substring(0, pname.lastIndexOf("."));
					//privatekey
					Properties p = new Properties();
					BufferedReader br = null;
					try {
						br = new BufferedReader(new InputStreamReader(new  FileInputStream(file), "UTF-8"));
						p.load(br);
						logger.debug("读取配置文件："+ file.getPath());
						this.privateKeyMap.put(appId, p);
//						KasiteConfig.initClientOauthInfo(appId, p);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}						 
			}
		}
	}
	public AppEntity getApp(String appId) {
		return appEntityMap.get(appId);
	}
	private static class FileWatcher implements Runnable{
		
		Map<String,Long> map=new HashMap<String,Long>();
		private static long delay = 20000;
		private LocalOAuthUtil parser;
		static { 
			logger.debug("配置文件动态加载时间："+delay);
		}
		private FileWatcher(LocalOAuthUtil parser) {
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
