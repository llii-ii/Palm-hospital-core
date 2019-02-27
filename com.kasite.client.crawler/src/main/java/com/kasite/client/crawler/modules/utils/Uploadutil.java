package com.kasite.client.crawler.modules.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.common.json.HTTP;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.coreframework.util.IDSeed;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.ConfigBuser;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileVo;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class Uploadutil {
	
	private static final Logger logger = LoggerFactory.getLogger(Uploadutil.class);
//	private static Zipper ziper = new Zipper();
//	private static String filePath = "/Users/daiyanshui/Desktop/上饶/demo/0dae00055a2c2010bdedcbbb5c9d86f6.zip";
	public static void main(String[] args) throws Exception {
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCKrm4ZHMD3oSZCwcRdcvB6BMHHEUOM/VQDw+DBN8lnQb1AzehKcJhFbJSisg8DvKxJ9y+8TRicI/uY/Ds6VIfRuM79dssH3dmqS0gbNNh/HuVVgVcz0g1mJLA16teaS+VdAlD8gan7YIHtnzlPQV6tbICrPut9sCUsNvZfWdrJQIDAQAB";
		String org_code = "jkzl";
		String password = "d7ZyhqhH";
		String url = "http://127.0.0.1:8182/api/crawler/addOnlineDetail.do";
		SoapResponseVo result = HttpRequestBus.create(url, RequestType.post)
        		.addHttpParam("access_token", "1312312")
        		.addHttpParam("state", "1")
        		.addHttpParam("onlineBreakDate","2018-08-24 10:42:00")
        		.addHttpParam("id", "12313213123").send();
        
		System.out.println(result.getResult());
//		HashMap<String, Key>  map = RSA.generateKeys();
//		Key pk = map.get(RSA.PUBLIC_KEY);
//		Key sk = map.get(RSA.PRIVATE_KEY);
//		String pks = RSA.encodeKey(pk);
//		String sks = RSA.encodeKey(sk);
//
//		System.out.println(pks);
//		System.out.println(sks);
//		
//		
//			Key key = RSA.genPublicKey(pks);
//			String package_crypto = RSA.encrypt(password, key);
//			System.out.println(package_crypto);
//			
//			System.out.println(RSA.decrypt(package_crypto, sk));
			
			
//			
//			System.out.println(RSA.decrypt(package_crypto, key));
//			uploadMultiFile(filePath,url,org_code, package_crypto, getFileMd5(filePath));
//			System.out.println(getStringRandom(8));
		
		
		
		//指定文件夹  加密并打包成 zip文件。
//		String fileP = "/Users/daiyanshui/Desktop/上饶/1dae012055a2c2010bdedcbbb5c9d86f6";
//		File f = new File(fileP);
//		String pwd = getStringRandom(Convent.passwordsize);
//		System.out.println("pwd:"+pwd);
//			ziper.zipFile(f,"/Users/daiyanshui/Desktop/上饶/1dae012055a2c2010bdedcbbb5c9d86f6.zip", pwd);
			
	}
	
	/**
	 * 异常日志写入
	 * @param file
	 * @param password
	 * @param md5
	 * @param code
	 * @param isZkPackage
	 * @param packType
	 * @param isRSA password是否rsa加密过
	 * @return
	 * @throws ParseException
	 */
	public static boolean writeErrorLog(File file,String password,String md5,int code,boolean isZkPackage,String packType,boolean isRSA) throws ParseException {
		//file.delete();//zip 文件不删除。另外写入一个本地异常日志文件。启另外一个线程重新上传并判断接口是否处于正常可用状态。
		String date = DateOper.getNow("yyyyMMdd");
		String logfilepath = Convent.getErrorlogDir()+ File.separator + date;
		File logFile = new File(logfilepath);
		if(!logFile.exists()) {
			logFile.mkdirs();
		}
		String path = logfilepath +  File.separator + IDSeed.next()+".log";
		StringBuffer content = new StringBuffer();
		content.append(file.getPath());
		content.append("\t");
		content.append(password);
		content.append("\t");
		content.append(md5);
		content.append("\t")
		.append(code)
		.append("\t")
		.append(isZkPackage)
		.append("\t")
		.append(packType)
		.append("\t")
		.append(isRSA);
		logger.debug(content.toString());
		FileOper.write(path, content.toString());
		return true;
	
	}
	
	/**
	 * 异常日志写入
	 * @param file
	 * @param password
	 * @param md5
	 * @param code
	 * @throws ParseException
	 */
	public static boolean writeErrorLog(File file,String password,String md5,int code,boolean isZkPackage,String packType) throws ParseException {
		return writeErrorLog(file, password, md5, code, isZkPackage, packType,false);
	}
	
	
	/**
	 * 将json文件写入到指定到文件目录
	 * @return
	 */
	public static boolean  writeJson2Dir(JSONObject jsonObj,String filePath) {
		return FileOper.write(filePath, jsonObj.toJSONString());
	}
	
	//生成随机数字和字母,  
    public static String getStringRandom(int length) {  
        String val = "";  
        Random random = new Random();        
        //length为几位密码 
        for(int i = 0; i < length; i++) {          
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
    
	/**
	 * 上传文件
	 * @param filePath
	 * @return SoapResponseVo  code == 200 表示成功。失败需要重新上传
	 * @throws Exception 
	 */
	public  static SoapResponseVo UploadFile(File file,String password,String md5,UploadFileVo vo) throws Exception {
		String org_code = Convent.getOrg_Code();
		String uploadUrl = Convent.getFileUploadUrl();
		if(vo.isZkPackage()) {
			uploadUrl = Convent.getZKFileUploadUrl();
//			logger.info("上传质控包文件地址：" + uploadUrl);
		}
		/**如果已是密文，不用再加密了*/
		String package_crypto = "";
		if(vo.isRSA()){
			package_crypto = password;
		}else{
			package_crypto = getPackage_crypto(password);
		}
		return uploadMultiFile(file,uploadUrl,org_code, package_crypto, md5,vo);
	}
	
	/**
	 * RSA公钥加密
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String getPackage_crypto(String password) throws Exception {
		String pk = Convent.getGroupPublicKey();
		String publicKey = null;
		if(StringUtil.isNotBlank(pk)) {
			publicKey = pk;
		}else {
			publicKey = Convent.getPublicKey();
		}
		Key key = RSA.genPublicKey(publicKey);
		return RSA.encrypt(password, key);
	}
	
	/**
	 * RSA秘钥解密
	 * @param packageCrypto
	 * @return
	 * @throws Exception
	 */
//	public static String getPackage_cryptPwd(String packageCrypto) throws Exception {
//		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAImV4lspVTHfqpGBFqVQ/MhbdQw34MGIq78vXle8Uc0VIGlYd2/Wls1zLYnZK8F6Sa7a18yCSdlEXu/Hyk0doYd7xThfoBjdgQ5WYWtxgoM8k/dlfYO/x9Ad08iPCoNaNPGtW5GUhPZ4RN6hyC8tk2Hahl6oPArzZi93Vb2x71XrAgMBAAECgYBJme/sWpEjzAYgLpFhwJBp0lssPRw0DBvse7eZcbnE3x9mm5fRMVec8peS3aWFrBiMvK+izLQa0XOEDDoRxN8qpOwPrGHrQkleQbPBn813bjA9etQkGShnW++281kHJKaxhDrRnVR1B4KAHaHZ83q53MlGJjztIidJfxSrbIGNkQJBAMj7qWHKWhX85ru8rN9f7/xbC+8JDP4NP+6Yfs6KgLxOXaCihqlNUjux7BlEOj/QMWpYny9AP4SALwU+vhDzgpkCQQCvP37oyyrTQNOWRuAIHqb/eIebkPxnFA9NWCsRjzd2qxo9GnADezYfgD/wx9bGx5Fh/0zv9iAoTqJi3w3uoDMjAkAsgdaLqlTyw5ORBYI+4S7y8nKdF6MB64s52hlSwccqbCw9s2Agw9evEoKXQc2obgFTbJarzw2B2SUQg0lx1YsZAkAghjVEeShaiqZ6Nm8vspilwzXPvmnJq6RnY1yi5qSRhpHZE0YEr2JNGF1Fj6EhxToLKPmbc19me6mRlfoc9mhnAkEAtw0smRSHviCr3gEOuvtme99Z1Mw1AgvBpOLOh6ri1PSNIQxiOwuvMsn3SkyGn91qX3ezy0nxVwHx81ms+y+ysA==";
//		return RSA.decrypt(packageCrypto, RSA.genPrivateKey(privateKey));
//	}
	
	public static String getFileMd5(File file) {
		String md5 = "";
		BufferedInputStream is = null;
		try {
			if(null != file && file.exists()) {
				is = new BufferedInputStream( new FileInputStream(file));
				//读取文件内容  
		        byte[] b = new byte[is.available()];  
		        is.read(b);
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(b);
	            BigInteger bigInt = new BigInteger(1, md.digest());
	            md5 = bigInt.toString();
	            b=null;
				is.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return md5;
	}

	/**
	 * 厦门上饶区域平台文件上传。
	 * packType = 2 非结构化档案
	 * packType=1  结构化档案
	 * packType=3 影像档案
	 * */
	private static SoapResponseVo uploadMultiFile(File file,String url,String org_code,String package_crypto,String md5,UploadFileVo vo) {
        SoapResponseVo result = HttpRequestBus.create(url, RequestType.fileUploadPost)
        		.addHttpParam("package_crypto", package_crypto)
        		.addHttpParam("md5", md5)
        		.setFile("pack",file)
        		.addHttpParam("org_code", org_code)
        		.addHttpParam("packType", vo.getPackType()+"").send();
        return result;
	}
	
	
	
	/**
	 * 上传医疗基础信息
	 *
	 * @return
	 * @author 無
	 * @throws Exception 
	 * @date 2018年6月26日 下午3:52:50
	 */
	public static SoapResponseVo uploadBasicInfo(String apiName,com.common.json.JSONObject param) throws Exception{
		String appKey = Convent.getGtoupAppKey();
		String secret = Convent.getGtoupSecret();
		String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZ");
		String v = "1.0";
		//签名
		TreeMap<String, String> paramMap = new TreeMap<>();
		paramMap.put("api", apiName);
		paramMap.put("appKey", appKey);
		paramMap.put("param", param.toString());
		paramMap.put("timestamp", timestamp);
		paramMap.put("v", v);
		
		String sign = MD5.signParam(paramMap, secret);
		//远程获取sign
//		String sign ="";
//		SoapResponseVo signResult = HttpRequestBus.create(Convent.getBasicUploadUrl().replace("api", "sign"), RequestType.post)
//				.contentType("application/x-www-form-urlencoded")
//				.addHttpParam("api", URLEncoder.encode(apiName, "UTF-8"))
//				.addHttpParam("param",URLEncoder.encode(param.toString(), "UTF-8"))
//				.addHttpParam("timestamp", URLEncoder.encode(timestamp, "UTF-8"))
//				.addHttpParam("v", URLEncoder.encode(v, "UTF-8"))
//				.addHttpParam("appKey", URLEncoder.encode(appKey, "UTF-8"))
//        		.send();
//		System.out.println("signResult="+signResult.getResult());
//		if(signResult.getCode() == HttpStatus.SC_OK){
//			if(StringUtil.isNotBlank(signResult.getResult()) && signResult.getResult().indexOf(",")!=-1){
//				sign = signResult.getResult().split(",")[1];
//			}
//		}
//		//本地生成sign
//		if(StringUtil.isBlank(sign)){
//			System.out.println("获取sign异常:"+signResult.getResult());
//			sign = MD5.signParam(paramMap, secret);
//		}
		//请求
		SoapResponseVo result = HttpRequestBus.create(Convent.getBasicUploadUrl(), RequestType.post)
					.contentType("application/x-www-form-urlencoded")
					.addHttpParam("api", URLEncoder.encode(apiName, "UTF-8"))
					.addHttpParam("param",URLEncoder.encode(param.toString(), "UTF-8"))
					.addHttpParam("timestamp", URLEncoder.encode(timestamp, "UTF-8"))
					.addHttpParam("v", URLEncoder.encode(v, "UTF-8"))
					.addHttpParam("appKey", URLEncoder.encode(appKey, "UTF-8"))
					.addHttpParam("sign", URLEncoder.encode(sign, "UTF-8"))
	        		.send();
		
//		System.out.println("url="+Convent.getBasicUploadUrl());
//		System.out.println("secret:"+secret);
//		System.out.println("api:"+apiName);
//		System.out.println("param:"+param);
//		System.out.println("appKey:"+appKey);
//		System.out.println("timestamp:"+timestamp);	
//		System.out.println("v:"+v);	
//		System.out.println("sign:"+sign);
		logger.info("result:"+result.getResult());
		return result;
	}
	
	
	
	/**
	 * 质控后台上报新增
	 * @param date 日期
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77) business_name 表名(例：检验-报告单)
	 * @param report_false report_true 错误/正确上报数
	 * @param check_num 校验数 
	 * @param convert_num 转换数 
	 * @return "success"/"fail"
	 * */
	public static SoapResponseVo uploadAddReport(String date,String hospital_id,String business_id,String business_name,int report_false,int report_true,int check_num,int convert_num) {
        SoapResponseVo result = null;
		try {
			if(Convent.getIsStartReport()){
				String url = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"report/addReport.do";
				result = HttpRequestBus.create(url, RequestType.post)
						.addHttpParam("hospital_id", hospital_id)
						.addHttpParam("business_id", business_id)
						.addHttpParam("business_name", business_name)
						.addHttpParam("report_false", Integer.toString(report_false))
						.addHttpParam("report_true", Integer.toString(report_true))
						.addHttpParam("check_num", Integer.toString(check_num))
						.addHttpParam("convert_num", Integer.toString(convert_num))
						.addHttpParam("date", date)
						.send();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	
	
	/**
	 * 质控后台作业时间新增
	 * @param date 日期
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77)
	 * @param report_time 上报时间
	 * @param check_time 校验时间
	 * @param convert_time 转换时间
	 * @param collect_time 采集时间
	 * @return "success"/"fail"
	 * */
	public static SoapResponseVo uploadWorkTime(String date,String hospital_id,String report_time,String check_time,String convert_time,String collect_time) {
        SoapResponseVo result = null;
		try {
			if(Convent.getIsStartReport()){
				String url = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"workTime/addWorkTime.do";
				result = HttpRequestBus.create(url, RequestType.post)
						.addHttpParam("hospital_id", hospital_id)
						.addHttpParam("report_time", report_time)
						.addHttpParam("check_time", check_time)
						.addHttpParam("date", date)
						.addHttpParam("convert_time", convert_time)
						.addHttpParam("collect_time", collect_time)
						.send();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}
	
	
	
	/**
	 * 质控后台新增在线/断线设备
	 * @param id 医院机构代码 
	 * @param state 状态 0-在线  1-离线
	 * @param name 名称
	 * @param municipal 市  county 区/县
	 * @param insertDate 记录插入时间         lastOnlineDate 最后上线时间
	 * @param url 医院url  
	 * @param folder 标准集放置的文件夹
	 * @param belong 所属 0-锐钦 1-卡思特 2-其他
	 * */
	/*public static SoapResponseVo addOnline(String id, String name, String municipal, String county, String state,
			 String insertDate, String lastOnlineDate,String belong) {
		SoapResponseVo result = null;
		try {
			
			String url_zk = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/addOnline.do";
			result = HttpRequestBus.create(url_zk, RequestType.post)
					.addHttpParam("id", id)
					.addHttpParam("name", name)
					.addHttpParam("municipal", municipal)
					.addHttpParam("county", county)
					.addHttpParam("state", state)
					//.addHttpParam("url", url)
					//.addHttpParam("folder", folder)
					.addHttpParam("insertDate", insertDate)
					.addHttpParam("lastOnlineDate", lastOnlineDate)
					.addHttpParam("belong", belong)
					.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}*/
	
	
	
	
	/**
	 * 新增在线/断线明细
	 * @param id 医院id 
	 * @param state 状态 0-在线  1-离线
	 * @param onlineBreakDate 上线/断线时间
	 * */
	/*public static SoapResponseVo addOnlineDetail(String id,String state,String onlineBreakDate) {
        SoapResponseVo result = null;
		try {
			String url_zk = ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/addOnlineDetail.do";
			result = HttpRequestBus.create(url_zk, RequestType.post)
					.addHttpParam("id", id)
					.addHttpParam("state", state)
					.addHttpParam("onlineBreakDate", onlineBreakDate)
					.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}*/
}
