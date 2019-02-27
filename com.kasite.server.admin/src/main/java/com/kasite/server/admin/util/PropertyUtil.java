package com.kasite.server.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coreframework.util.StringUtil;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;

/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	private static Properties props;
//	static {
//		loadProps();
//	}

	synchronized static public String loadProps() {
//		logger.info("开始加载wechat.properties文件内容.......");
		props = new Properties();
		InputStream in = null;
		try {
//　　　　　　　<!--第一种，通过类加载器进行获取properties文件流-->
//			in = PropertyUtil.class.getClassLoader().getResourceAsStream("wechat.properties");
			in = new BufferedInputStream (new FileInputStream("wechat.properties"));
//　　　　　　  <!--第二种，通过类进行获取properties文件流-->
			// in = PropertyUtil.class.getResourceAsStream("/wechat.properties");
			props.load(in);
		} catch (FileNotFoundException e) {
			logger.error("wechat.properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("wechat.properties文件流关闭出现异常");
			}
		}
//		logger.info("加载wechat.properties文件内容完成...........");
		logger.info("wechat.properties文件内容：" + props);
		return props.toString();
	}
	
//	synchronized static private void updatedProps2() {
//	String profilepath = DemonstrateRestService.class.getResource("/").getPath() + "engine.properties"
//	try{
//		PropertiesConfiguration config = new PropertiesConfiguration(profilepath);
//		String uiserverport = config.getString("server.uiserverport");
//		config.setAutoSave(true);
//		config.setProperty("server.uiserverport", "445");
//		System.out.println(config.getString("server.uiserverport"));
//	}catch(ConfigurationException cex){
//		System.err.println("loading of the configuration file failed");
//	}
//}

	synchronized static public String updatedProps(String hosId,String toUser,Boolean isDel) {
		InputStream input = null;
		FileOutputStream output = null;
		String result = "";
		try {
//			input = PropertyUtil.class.getClassLoader().getResourceAsStream("wechat.properties");
			input = new BufferedInputStream (new FileInputStream("wechat.properties"));
			SafeProperties safeProp = new SafeProperties();
			safeProp.load(input);
			input.close();
			//判断是否已存在
			String oldUser = safeProp.getProperty(hosId,"");
			String [] userArr =toUser.replace("，",	",").split(",");
			if(isDel) {
				if(StringUtil.isNotBlank(oldUser)) {
					for (int i = 0; i < userArr.length; i++) {
						if(oldUser.indexOf(userArr[i])==-1) {
							System.out.println(userArr[i]+"不存在，无需删除");
						}else {
							System.out.println(userArr[i]+"删除");
							oldUser = oldUser.replace("|"+userArr[i], "");
							oldUser = oldUser.replace(userArr[i]+"|", "");
							oldUser = oldUser.replace(userArr[i], "");
						}
					}
				}else {
					result = "删除失败,医院ID【"+hosId+"】的配置不存在";
				}
			}else {
				for (int i = 0; i < userArr.length; i++) {
					if(oldUser.indexOf(userArr[i])==-1) {
						System.out.println(userArr[i]+"添加");
						if(StringUtil.isBlank(oldUser)) {
							oldUser = userArr[i];
						}else {
							oldUser = oldUser +"|"+userArr[i];
						}
					}else {
						System.out.println(userArr[i]+"已存在");
					}
				}
			}
			
			safeProp.setProperty(hosId, oldUser);
			result = "操作成功,医院ID【"+hosId+"】当前配置:"+safeProp.getProperty(hosId);
			
			output = new FileOutputStream("wechat.properties");
			safeProp.store(output, null);
			output.close();
		} catch (FileNotFoundException e) {
			logger.error("wechat.properties文件未找到");
		} catch (IOException e) {
			logger.error("出现IOException");
		} finally {
			try {
				if (null != input) {
					input.close();
				}
				if (null != output) {
					output.close();
				}
			} catch (IOException e) {
				logger.error("wechat.properties文件流关闭出现异常");
			}
		}
		
		return result;
	}
	
	

	public static String getProperty(String key) {
//		if (null == props) {
			loadProps();
//		}
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
//		if (null == props) {
			loadProps();
//		}
		return props.getProperty(key, defaultValue);
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println(PropertyUtil.getProperty("10000"));
		String touser="ruansunzhong|honghuayu";
		String deaulf=PropertyUtil.getProperty("10000");
		if(StringUtil.isNotBlank(deaulf)) {
			String[] deaulfArr= deaulf.split("\\|");
			for (int i = 0; i < deaulfArr.length; i++) {
				if(touser.indexOf(deaulfArr[0])==-1) {
					touser +="|"+deaulfArr[0];
				}
			}
		}
		System.out.println(touser);
//		System.out.println(PropertyUtil.loadProps());
//		System.out.println("ss="+PropertyUtil.getProperty("10010"));
//		System.out.println(updatedProps("10010","shuige,fage，sunge",false));
//		System.out.println(updatedProps("10010","shuige",true));
	}
}