package com.kasite.client.crawler.modules.manage.api;


import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.coreframework.util.JsonUtil;
import com.kasite.client.crawler.modules.manage.bean.online.dbo.Online;
import com.kasite.client.crawler.modules.manage.bean.report.dbo.Report;
import com.kasite.client.crawler.modules.manage.bean.workTime.dbo.WorkTime;
import com.kasite.client.crawler.modules.manage.dao.online.OnlineDao;
import com.kasite.client.crawler.modules.manage.dao.report.ReportDao;
import com.kasite.client.crawler.modules.manage.dao.workTime.WorkTimeDao;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.kasite.client.crawler.modules.utils.RSA;
import com.kasite.client.crawler.modules.utils.Uploadutil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.CommonUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

import net.sf.json.JSONObject;

/**
 * 质控后台api
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年8月27日 
 */
@Controller
@RequestMapping(value = "/api/crawler")
public class CrawlerApi {
	
	private final static Log log = LogFactory.getLog(CrawlerApi.class);
	
	/**
	 * 新增在线/断线设备
	 * @param access_token 鉴权
	 * @param id 医院id 
	 * @param state 状态 0-在线  1-离线
	 * @param name 名称
	 * @param municipal 市  county 区/县
	 * @param insertDate 记录插入时间         lastOnlineDate 最后上线时间
	 * @param url 医院url  
	 * @param folder 标准集放置的文件夹
	 * @param belong 所属 0-锐软 1-卡思特 2-其他
	 * */
	@RequestMapping(value = "/addOnline.do")
	@ResponseBody
	public String addOnline(String access_token,String id,String name,String municipal,String county,String state,String url,String folder,String insertDate,String lastOnlineDate,String belong) {
		try {
			Online online = new Online();
			online.setId(id);
			online.setState(state);
			online.setName(name);
			online.setMunicipal(municipal);
			online.setCounty(county);
			online.setInsert_date(DateUtils.formatStringtoDate(insertDate, "yyyy-MM-dd HH:mm:ss"));
			online.setLast_online_date(DateUtils.formatStringtoDate(lastOnlineDate, "yyyy-MM-dd HH:mm:ss"));
			online.setUrl(url);
			online.setFolder(folder);
			online.setBelong(belong);
			OnlineDao.addOnline(online);
			return "success";
		} catch (Exception e) {
			return "fail";
		}
	}
	
	
	//http://127.0.0.1:8182/api/crawler/addOnlineDetail.do?access_token=&state=1&onlineBreakDate=2018-09-04&id=1111
	/**
	 * 新增在线/断线明细
	 * @param access_token 鉴权
	 * @param id 医院id 
	 * @param state 状态 0-在线  1-离线
	 * @onlineBreakDate 时间"yyyy-MM-dd HH:mm:ss"
	 * */
	@RequestMapping(value = "/addOnlineDetail.do")
	@ResponseBody
	public String addOnlineDetail(String access_token,String id,String state,String onlineBreakDate)  {
		try {
			Online online = new Online();
			online.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			online.setState(state);
			online.setEquipment_id(id);
			online.setOnline_break_date(DateUtils.formatStringtoDate(onlineBreakDate, "yyyy-MM-dd HH:mm:ss"));
			OnlineDao.addOnlineDetail(online);
			return "success";
		} catch (Exception e) {
			return "fail";
		}

	}
	
	
	
	/**
	 * 新增上报
	 * @param access_token 鉴权
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77) business_name 表名(例：检验-报告单)
	 * @param report_false report_true 错误/正确上报数
	 * @param check_num 校验数 
	 * @param convert_num 转换数 
	 * @return "success"/"fail"
	 * */
	@RequestMapping(value = "/addReport.do")
	@ResponseBody
	public String addReport(String access_token,String date,String hospital_id,String business_id,String business_name,int report_false,int report_true,int check_num,int convert_num) {
		Report report = new Report();
		report.setHospital_id(hospital_id);
		report.setBusiness_id(business_id);
		report.setDate(DateUtils.formatStringtoDate(date, "yyyy-MM-dd"));
		try {
			Report reportSelect =  ReportDao.reportSelect(report);
			if (reportSelect == null ) {
				report.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				String hosName = OnlineDao.getOnlineById(hospital_id).getName();
				report.setHospital_name(hosName);
				report.setBusiness_name(business_name);
				report.setReport_false(report_false);
				report.setReport_true(report_true);
				report.setCheck_num(check_num);
				report.setConvert_num(convert_num);
				ReportDao.addReport(report);				
			}else {
				Report updateReport = new Report();
				updateReport.setId(reportSelect.getId());
				report_true += reportSelect.getReport_true();
				report_false += reportSelect.getReport_false();
				check_num += reportSelect.getCheck_num();
				convert_num += reportSelect.getConvert_num();
				updateReport.setReport_false(report_false);
				updateReport.setReport_true(report_true);
				updateReport.setCheck_num(check_num);
				updateReport.setConvert_num(convert_num);
				ReportDao.updateReport(updateReport);
			}
			return "success";
		} catch (SQLException e) {
			return "fail";
		}
	}
	
	
	/**
	 * 新增作业时间
	 * @param access_token 鉴权
	 * @param hospital_id 医院id 
	 * @param business_id 表id(例：HDSD00_77)
	 * @param report_time 上报时间
	 * @param check_time 校验时间
	 * @param convert_time 转换时间
	 * @param collect_time 采集时间
	 * @return "success"/"fail"
	 * */
	@RequestMapping(value = "/addWorkTime.do")
	@ResponseBody
	public String addWorkTime(String access_token,String date,String hospital_id,int report_time,int check_time,int convert_time,int collect_time) {
		WorkTime workTime = new WorkTime();
		workTime.setHospital_id(hospital_id);
		workTime.setWork_date(DateUtils.formatStringtoDate(date, "yyyy-MM-dd"));
		try {
			WorkTime select = WorkTimeDao.selectWorkTime(workTime);
			if (select == null) {
				workTime.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				workTime.setReport_time(report_time);
				workTime.setCheck_time(check_time);
				workTime.setCollect_time(collect_time);
				workTime.setConvert_time(convert_time);
				WorkTimeDao.addWorkTime(workTime);
			}else {
				WorkTime update = new WorkTime();
				update.setId(select.getId());
				report_time += select.getReport_time();
				check_time += select.getCheck_time();
				convert_time += select.getConvert_time();
				collect_time += select.getCollect_time();
				update.setReport_time(report_time);
				update.setCheck_time(check_time);
				update.setCollect_time(collect_time);
				update.setConvert_time(convert_time);
				WorkTimeDao.updateWorkTime(update);
			}

			return "success";
		} catch (Exception e) {
			return "fail";
		}
	}
	
	/**
	 * 健康档案上传
	 * @param request
	 * @param response
	 *  access_token	是	String	Token接口获取 
		pack 	是	MultipartFile	健康档案zip包（详见健康档案上传规范.pdf）
		packType	是	int	1.结构化(默认) 2.非结构化 3.影像 档案 4、质控包
		package_crypto 	是	String	档案包密码，使用公钥加密
	 * @return
	 */
	@RequestMapping(value = "/packages.do")
	@ResponseBody
	public String packages(HttpServletRequest request, HttpServletResponse response) {
		try {
			StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest)request;
//			String accessToken = req.getParameter("access_token");
			String packType = req.getParameter("packType");
			String packageCrypto = req.getParameter("package_crypto");	
			MultipartFile pack =req.getFile("pack");
			System.out.println("packType="+packType+"|package_crypto="+packageCrypto);
			byte[] content = pack.getBytes();
			String fileName = pack.getOriginalFilename();
			System.out.println("fileName="+fileName);
			fileName = new String(fileName.getBytes("UTF-8"));
			/**zip保存目录*/
	        String dir = System.getProperty("user.dir") +File.separator+ "file"+File.separator+"zip";
	        /**创建当前日期的文件夹*/
	        String dateStr = DateUtils.formatDateToString(new Date(), "yyyyMMdd");	
	        File dateFile = new File(dir+File.separator+dateStr);
	        if(!dateFile.exists()){
	        	dateFile.mkdir();
	        }
	        /**保存文件*/
	        String filePath = dir+File.separator+dateStr+File.separator+fileName;
	        FileOper.write(filePath,content);
	        /**判断是否保存成功*/
	        File zipfile = new File(filePath);
	        boolean isWrite =false;
	        if(zipfile.exists()){
	        	String md5 = Uploadutil.getFileMd5(zipfile);
//	        	System.out.println("md5="+md5);
	        	/**解密*/
//	        	System.out.println("解密前密文="+packageCrypto);
//	        	String pwd = Uploadutil.getPackage_cryptPwd(packageCrypto);
//	        	System.out.println("pwd="+pwd);
	        	/**写入日志  未加密密码*/
	        	isWrite = Uploadutil.writeErrorLog(zipfile, packageCrypto, md5, -144443, packType.equals("4")?true:false,packType,false);
	        	if(isWrite){
	        		/**文件、日志保存成功 返回成功*/
		        	return CommonUtil.getRetVal(0,RetCode.Success.RET_10000.getCode(),RetCode.Success.RET_10000.getMessage() );
		        }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getRetVal(0,RetCode.Success.RET_10000.getCode(),e.getMessage());
		}
		return CommonUtil.getRetVal(0,RetCode.Common.ERROR_SYSTEM.getCode(),RetCode.Common.ERROR_SYSTEM.getMessage());
	}
	
	private static String doctorApi="rhip.doctor.post";
	/**
	 * 上传医疗资源
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/basic.do")
	@ResponseBody
	public String basic(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("param");
		System.out.println("param="+param);
		com.common.json.JSONObject json = null;
		try {
			param = URLDecoder.decode(param,"UTF-8");
			System.out.println("URLDecoder-param="+param);
			json = new com.common.json.JSONObject(param);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getRetVal(0,RetCode.Common.ERROR_PARAM.getCode(),"参数param不是一个有效的json对象:"+e.getMessage());
		}
		SoapResponseVo result = null;
		try {
			result = Uploadutil.uploadBasicInfo(doctorApi, json);
			System.out.println("result="+result.getResult());
			if (null != result && result.getCode() == 200) {
				if(null!=result.getResult()){
					JSONObject res = JSONObject.fromObject(result.getResult());
					if(!"0".equals(JsonUtil.getJsonString(res, "errorCode"))){
						return CommonUtil.getRetVal(0,RetCode.Common.ERROR_PARAM.getCode(),JsonUtil.getJsonString(res, "errorCode") +","+ JsonUtil.getJsonString(res, "errorMsg"));
					}else {
						return CommonUtil.getRetVal(0,RetCode.Success.RET_10000.getCode(),RetCode.Success.RET_10000.getMessage() );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.getRetVal(0,RetCode.Common.ERROR_SYSTEM.getCode(),e.getMessage());
		}
		return CommonUtil.getRetVal(0,result.getCode(),result.getResult());
	}
	
	 /**
	  * access_token	是	String	Token接口获取 
		pack 	是	MultipartFile	健康档案zip包（详见健康档案上传规范.pdf）
		packType	是	int	1.结构化(默认) 2.非结构化 3.影像 档案 4、质控包
		package_crypto 	是	String	档案包密码，使用公钥加密
	  * @param args
	 * @throws Exception 
	  */
	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("user.dir"));
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJleJbKVUx36qRgRalUPzIW3UMN+DBiKu/L15XvFHNFSBpWHdv1pbNcy2J2SvBekmu2tfMgknZRF7vx8pNHaGHe8U4X6AY3YEOVmFrcYKDPJP3ZX2Dv8fQHdPIjwqDWjTxrVuRlIT2eETeocgvLZNh2oZeqDwK82Yvd1W9se9V6wIDAQAB";
//		String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAImV4lspVTHfqpGBFqVQ/MhbdQw34MGIq78vXle8Uc0VIGlYd2/Wls1zLYnZK8F6Sa7a18yCSdlEXu/Hyk0doYd7xThfoBjdgQ5WYWtxgoM8k/dlfYO/x9Ad08iPCoNaNPGtW5GUhPZ4RN6hyC8tk2Hahl6oPArzZi93Vb2x71XrAgMBAAECgYBJme/sWpEjzAYgLpFhwJBp0lssPRw0DBvse7eZcbnE3x9mm5fRMVec8peS3aWFrBiMvK+izLQa0XOEDDoRxN8qpOwPrGHrQkleQbPBn813bjA9etQkGShnW++281kHJKaxhDrRnVR1B4KAHaHZ83q53MlGJjztIidJfxSrbIGNkQJBAMj7qWHKWhX85ru8rN9f7/xbC+8JDP4NP+6Yfs6KgLxOXaCihqlNUjux7BlEOj/QMWpYny9AP4SALwU+vhDzgpkCQQCvP37oyyrTQNOWRuAIHqb/eIebkPxnFA9NWCsRjzd2qxo9GnADezYfgD/wx9bGx5Fh/0zv9iAoTqJi3w3uoDMjAkAsgdaLqlTyw5ORBYI+4S7y8nKdF6MB64s52hlSwccqbCw9s2Agw9evEoKXQc2obgFTbJarzw2B2SUQg0lx1YsZAkAghjVEeShaiqZ6Nm8vspilwzXPvmnJq6RnY1yi5qSRhpHZE0YEr2JNGF1Fj6EhxToLKPmbc19me6mRlfoc9mhnAkEAtw0smRSHviCr3gEOuvtme99Z1Mw1AgvBpOLOh6ri1PSNIQxiOwuvMsn3SkyGn91qX3ezy0nxVwHx81ms+y+ysA==";
		String path = "119.23.210.15:8180/crawler";
//		path = "127.0.0.1:8080";
		//获取token
		//【appId,appSecret,orgCode,系统时间:yyyy-MM-dd hh:mm:ss】
		String a = "KASITE-CLIENT-CRAWLER,zzXHt3e7wrKQ8eD3L,135050300065,2018-09-06 15:00:00";
		String url = "http://"+path+"/api/verificat/getToken.do";
		SoapResponseVo result = HttpRequestBus.create(url, RequestType.post)
        		.addHttpParam("encrypt", RSA.encrypt(a, RSA.genPublicKey(publicKey)))
        		.addHttpParam("orgCode","135050300065")
        		.addHttpParam("appId", "KASITE-CLIENT-CRAWLER")
        		.addHttpParam("encryptType", "RSA").send();
		System.out.println("result="+result.getResult());
		String access_token = new com.common.json.JSONObject(result.getResult()).getString("access_token");
		System.out.println("access_token="+access_token);
//		String access_token = "11";
		
		//健康档案上传
		String package_crypto = "111111";
//		package_crypto = RSA.encrypt(package_crypto, RSA.genPublicKey(publicKey));
//		System.out.println("加密后的密文="+package_crypto);
//		String pwd = RSA.decrypt(package_crypto, RSA.genPrivateKey(privateKey));
//		System.out.println("解密后的密文="+pwd);
		url = "http://"+path+"/api/crawler/packages.do";
		String packType = "1";
		File file = new File("C:\\Users\\無\\Desktop\\test\\299017f4aba441f4a77b77913e3f453c.zip");
		SoapResponseVo result3 = HttpRequestBus.create(url, RequestType.fileUploadPost)
	        		.addHttpParam("package_crypto", package_crypto)
	        		.setFile("pack",file)
	        		.addHttpParam("access_token", access_token)
	        		.addHttpParam("packType", packType).send();
		System.out.println("result3="+result3.getResult());
		
		//医疗资源上传
		url = "http://"+path+"/api/crawler/basic.do";
		com.common.json.JSONObject param = new com.common.json.JSONObject("{\"doctor_json_data\":{\"code\":\"8974\",\"sex\":\"0\",\"idCardNo\":\"123456778786724323\",\"name\":\"王艺婷\",\"status\":\"1\"},\"model\":{\"deptName\":\"未分配\",\"orgCode\":\"492270292\"}}");
		SoapResponseVo result2 = HttpRequestBus.create(url, RequestType.post)
	        		.addHttpParam("access_token", access_token)
	        		.addHttpParam("param", URLEncoder.encode(param.toString(), "UTF-8")).send();
		System.out.println("result2="+result2.getCode()+result2.getResult());
	}
	
}
