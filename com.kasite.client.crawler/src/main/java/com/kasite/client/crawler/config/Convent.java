package com.kasite.client.crawler.config;

import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.AppConfig;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.ExpiryMap;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.core.httpclient.http.StringUtils;
@Configuration
public class Convent { 
	public final static boolean ISDEBUG = false;
	public final static String DATAFILESE = ".json";
	public final static String XML_FILESE = ".xml";
	public final static String TEXT_FILESE = ".txt";
	public final static String PDF_FILESE = ".pdf";
	public final static String JPG_FILESE = ".jpg";
	public final static String XML_ZIP_FILESE = ".xml.zip";
	public final static String TEMPDATAFILENAME = "_TEMP";
	public final static String TIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public final static String TIME_D8_FORMAT = "yyyy-MM-dd";
	public final static String TIME_T_FORMAT = "HH:mm:ss";
	public final static String ZKPACKAGE = "_zk";//质控包后缀名
	public final static String FJGPACKAGE = "_fjg";//非结构包后缀名
	public final static int passwordsize = 8;//随机生成8位数的密码
	
	public static boolean getIsCheck() {
		if(isDebug()) {
			return true;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isCheck();
	}
	public static boolean getIsUpload() {
		if(isDebug()) {
			return false;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isUpload();
	}
	public static boolean getIsPrint() {
		if(isDebug()) {
			return true;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isPrint();
	}
	public static boolean getIsStartJob() {
		if(isDebug()) {
			return true;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isStartJob();
	}
	public static boolean getIsStartReport() {
		if(isDebug()) {
			return false;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isStartReport();
	}
	public static boolean getIsZK() {
		if(isDebug()) {
			return true;
		}
		return ConfigBuser.create().getDatacloudConfigVo().isZK();
	}
	//默认有效期 1 秒
	public static ExpiryMap<String, String> map = new ExpiryMap<>(1000);
	public static int getPageSize() {
		if(isDebug()) {
			return 100;
		}
		return ConfigBuser.create().getDatacloudConfigVo().getPageSize();
	}
	public static String getFileName() {
		return AppConfig.getValue("FileName"); 
	}
	public static boolean isDebug() {
		if(ISDEBUG  || "true".equals(AppConfig.getValue("isDebug"))) {
			return true;
		}
		return false;
	}
	public static int getFileUploadThreadSize() {
		if(isDebug()) {
			return 5;
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFileupload_threads();
	}
	public static String getSysDataFilePath() {
		if(isDebug()) {
			return "standard/dev/data.xls";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFile_data();
	}
	public static String getSysRuleFilePath() {
		if(isDebug()) {
			return "standard/dev/rule.xls";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFile_rule();
	}
	public static String getPingAnFilePath() {
		if(isDebug()) {
			return "standard/dev/pingan.xls";
		}
		return ConfigBuser.create().getHisHearthDataCrawlerConfig().getFile_data();
	}
	
	public static String getSysDictFilePath() {
		if(isDebug()) {
			return "standard/dev/dict.xls";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFile_dict();
	}
	public static String getSysMyDictFilePath() {
		if(isDebug()) {
			return "standard/dev/dict.xml";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getMydict();
	}
	public static String getZipDir() {
		if(isDebug()) {
			return "file/zip";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFileupload_dir_zip();
	}
	/**
	 * zip文件缓存天数。如果超过这个天数默认删除
	 * @return
	 */
	public static int getZipTempDays() {
		if(isDebug()) {
			return 1;
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFileupload_tempdates();
	}
	/**
	 * 保存文件夹的目录
	 * @return
	 */
	public static String getDataDir() {
		if(isDebug()) {
			return "file/data";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFileupload_dir_data();
//		return AppConfig.getValue("xm.datacloud.craller.fileupload.filepath.data");
	}
	/**
	 * 上传失败保存文件日志。
	 * @return
	 */
	public static String getErrorlogDir() {
		if(isDebug()) {
			return "file/log";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getFileupload_dir_log();
	}
	
//	public static XMDataCloudConfig getXMDataCloudConfig() {
//		String configJsonStr = AppConfig.getValue("xm.datacloud.craller.config");
//		JSONObject obj = JSONObject.parseObject(configJsonStr);
//		return JSONObject.toJavaObject(obj, XMDataCloudConfig.class);
//	}
	// http://27.154.233.186:9999/api/v1.0/organizations/jkzl/key
	// GET /v1.0/organizations/{org_code}/key  xm.datacloud.url.publickey
	public static String getGetPublicKeyUrl() {
		if(isDebug()) {
			return "http://27.154.233.186:9999/api/v1.0/organizations/jkzl/key";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getApi_publickey();
	}
	public static String getFileUploadUrl() {
		if(isDebug()) {
			return "http://27.154.233.186:9999/api/packages";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getApi_upload();
	}
	public static String getZKFileUploadUrl() {
		if(isDebug()) {
			return "http://27.154.233.186:9999/api/qcPackages";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getApi_zkUpload();
	}
	public static String getOrg_Code() {
		if(isDebug()) {
			return "jkzl";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getGroup_org_code();
	}
	public static String getGroupPublicKey() {
		if(isDebug()) {
			try {
				return getPublicKey();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ConfigBuser.create().getDatacloudConfigVo().getGroup_publicKey();
	}
	
	public static String getBasicUploadUrl() {
		if(isDebug()) {
			return "http://27.154.233.186:9999/api";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getBasic_Upload();
	}
	
	public static String getGtoupAppKey() {
		if(isDebug()) {
			return "u5GEgEuoyc";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getGroup_appKey();
	}
	
	public static String getGtoupSecret() {
		if(isDebug()) {
			return "MNhQXnCSaaRc8kcS";
		}
		return ConfigBuser.create().getDatacloudConfigVo().getGroup_appSecret();
	}
	
	public static String getPublicKey() throws Exception{
//		map.put("test2", "ankang", 2*60*1000);//缓存2小时
		String publicKey = null;
		String org_code = getOrg_Code();
		publicKey = map.get(org_code);
		if(StringUtils.isNotBlank(publicKey)) {
			return publicKey;
		}
		String url = getGetPublicKeyUrl();
		SoapResponseVo result = HttpRequestBus.create(url, RequestType.get).send();
		if(200 == result.getCode()) {
			String resultStr = result.getResult();
			JSONObject json = JSONObject.parseObject(resultStr);
			publicKey = json.getString("publicKey");
			map.put(org_code, publicKey, 2*60*1000);//缓存2小时
		}else {
			throw new RRException("无法获取公钥。HttpStatus="+result.getCode());
		}
		return publicKey;
	}
}
