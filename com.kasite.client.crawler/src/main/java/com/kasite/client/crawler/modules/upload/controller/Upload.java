package com.kasite.client.crawler.modules.upload.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.common.json.JSONException;
import com.common.json.JSONObject;
import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.modules.utils.FileOper;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.DataCloudConfig;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.modules.upload.datacloud.IXMDataCloudService;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.R;
/**
 * 用于重传／补传数据等操作
 * @author daiyanshui
 *
 */
@RestController
@RequestMapping("/job")
public class Upload {
	private static final Logger logger = LoggerFactory.getLogger(Upload.class);
	@Autowired
	private IXMDataCloudService xmDataCloudService;
	@Autowired DataCloudConfig dataCloudConfig;
	
	private static boolean isNotRun = true;
	private static boolean isNotRunOld = true;
	private static boolean isNotRunEventNo = true;
	
	/**
	 * 采集date当天的数据
	 *
	 * @param date
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:06:46
	 */
	@GetMapping("/start")
    @ResponseBody
    R start(String date) {
		R r = new R();
		try {
			if(isNotRun) {
				isNotRun = false;
				String endDate = DateOper.addDate(date,1);
//				String startDate = DateOper.addDate(date,-2);
				xmDataCloudService.assemblyData(date, endDate.trim(),null);
				isNotRun = true;
			}
		}catch (Exception e) {
			isNotRun = true;
			logger.error("数据处理异常",e);
		}
		return r;
	}
	
	/**
	 * 采集事件号为eventNo的单条数据
	 *
	 * @param date 可为空
	 * @param eventNo 事件号
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:07:28
	 */
	@GetMapping("/startOne")
    @ResponseBody
    R startOne(String eventNo) {
		logger.info("eventNo="+eventNo);
		R r = new R();
		try {
//			String str ="操作完上面几步后，即使你原来的字体里面没有显示Lucida Console这个字体，现在应该也能看到了。选择它！如果原来就有，可以选上它先试试，不行在执行上述步骤（这里补充：至少我本机需要CHCP 65001下，有朋友说不要）；";
//			FileOper.write("D:\\K_S_T\\CrawlerServer\\test.json", str,"UTF-8");
//			FileOper.write("D:\\K_S_T\\CrawlerServer\\test2.json","操作完上面几步后，即使你原来的字体里面没有显示Lucida Console这个字体，现在应该也能看到了。选择它！如果原来就有，可以选上它先试试，不行在执行上述步骤（这里补充：至少我本机需要CHCP 65001下，有朋友说不要）；");
//			FileOutputStream outSTr = new FileOutputStream(new File("D:\\K_S_T\\CrawlerServer\\test4.json"));
//			outSTr.write(str.getBytes("UTF-8"));
			
			if(isNotRunEventNo) {
				isNotRunEventNo = false;
				xmDataCloudService.assemblyData(null, null,eventNo.trim());
				isNotRunEventNo = true;
			}
		}catch (Exception e) {
			isNotRunEventNo = true;
			logger.error("数据处理异常",e);
		}
		return r;
	}
	
	/**
	 * 质检包上传
	 *
	 * @param date
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:08:24
	 */
	@GetMapping("/controlPackage")
    @ResponseBody
    R controlPackage(String date) {
		R r = new R();
		try {
			if(isNotRun) {
				isNotRun = false;
				String endDate = DateOper.addDate(date,1);
//				String startDate = DateOper.addDate(date,-2);
				xmDataCloudService.DealQualityControlPackage(date, endDate);
				isNotRun = true;
			}
		}catch (Exception e) {
			isNotRun = true;
			logger.error("数据处理异常",e);
		}
		return r;
	}
	
	/**
	 * 文件读取
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @author 無
	 * @date 2018年6月25日 上午10:08:39
	 */
	private static String read(String filePath) throws IOException, URISyntaxException {
		filePath = filePath+"/sql.txt";
		File file = new File(filePath);
		if(file.exists() && file.isFile()) {
			byte[] data = Files.readAllBytes(new File(filePath).toPath());
			return new String(data, StandardCharsets.UTF_8);
		}
		return null;
	}
	
	/**
	 * 执行sqlStr语句
	 *
	 * @param sqlStr
	 * @return
	 * @author 無
	 * @throws JSONException 
	 * @date 2018年6月25日 上午10:08:48
	 */
	@GetMapping("/sql")
    @ResponseBody
	R sql(String sqlStr,String PageStart,String pageSize,String db){
		logger.info("sql="+sqlStr);
		JSONObject json = new JSONObject();
		R r = new R();
		if(StringUtil.isNotBlank(sqlStr)) {
			try {
				Sql sql = new Sql(URLDecoder.decode(sqlStr));
				int PageStart2 = Integer.parseInt(PageStart);
				int pageSize2 = Integer.parseInt(pageSize);
				int start = 0;
				if(PageStart2 == 0){
					start = 0;
				}else {
					start = PageStart2 * pageSize2;
				}
				MyDatabaseEnum myDatabase = MyDatabaseEnum.hisdb;
				if("hisdb".equals(db)){
					myDatabase = MyDatabaseEnum.hisdb;
				}else if("zk".equals(db)){
					myDatabase = MyDatabaseEnum.his_ecg;
				}
				json = DB.me().queryForJson(myDatabase, sql,start,pageSize2);
				logger.info(sqlStr+"="+json.toString());
			}catch (Exception e) {
				e.printStackTrace();
				return r.error(-1, e.getMessage());
			}
		}
		return r.put("result", json.toString());
	}
	
	

	/**
	 * 执行sql文件 多条语句回车换行
	 *
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:09:31
	 */
	@GetMapping("/execute")
    @ResponseBody
	R execute() {
		try {
			logger.info(dataCloudConfig.getExecute());
			String executeSql = read(dataCloudConfig.getExecute());
			if(StringUtil.isNotBlank(executeSql)) {
				String[] strs = executeSql.split("\n");
				long start = 0;
				for (String sqlStr : strs) {
					if(StringUtil.isNotBlank(sqlStr)) {
						Sql sql = new Sql(sqlStr);
						try {
							start = System.currentTimeMillis();
							JSONObject json = DB.me().queryForJson(MyDatabaseEnum.hisdb, sql,0,10);
							logger.info("耗时1="+(System.currentTimeMillis()-start));
							logger.info(sqlStr+"="+json.toString());
							FileOper.write(dataCloudConfig.getExecute()+"/"+sqlStr+".json", json.toString());
						}catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception="+e.getMessage());
						}finally {
							logger.info("耗时2="+(System.currentTimeMillis()-start));
						}
					}
				}
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return R.ok();
	}
	
	/**
	 * 执行sql文件 多条语句回车换行
	 *
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:09:31
	 */
	@GetMapping("/execute2")
    @ResponseBody
	R execute2() {
		try {
			logger.info(dataCloudConfig.getExecute());
			String executeSql = read(dataCloudConfig.getExecute());
			if(StringUtil.isNotBlank(executeSql)) {
				String[] strs = executeSql.split("\n");
				for (String sqlStr : strs) {
					if(StringUtil.isNotBlank(sqlStr)) {
						Sql sql = new Sql(sqlStr);
						try {
							JSONObject json = DB.me().queryForJson(MyDatabaseEnum.his_ecg, sql,0,10);
							logger.info(sqlStr+"="+json.toString());
							FileOper.write(dataCloudConfig.getExecute()+"/"+sqlStr+".json", json.toString());
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return R.ok();
	}
	
	/**
	 * 补传start-endDate的数据
	 *
	 * @param start
	 * @param endDate
	 * @return
	 * @author 無
	 * @date 2018年6月25日 上午10:10:08
	 */
	@GetMapping("/old")
    @ResponseBody
    R all(String start,String endDate) {
		logger.error("start="+start +"|| end="+endDate);
		String end = endDate;
		if(StringUtil.isBlank(endDate)) {
			try {
				end = DateOper.getNow("yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		R r = new R();
		try {
			if(isNotRunOld) {
				isNotRunOld = false;
//				xmDataCloudService.assemblyData(startDate, endDate);
				if(StringUtil.isNotBlank(start) && StringUtil.isNotBlank(end)) {
					int dif = DateOper.getDateDiff(DateOper.parse(end), DateOper.parse(start), "d");
					for (int i = 0; i <= dif; i++) { 
						logger.error("["+DateOper.addDate(start, i) +"]-["+DateOper.addDate(start, i+1)+"]");
						xmDataCloudService.assemblyData(DateOper.addDate(start, i), DateOper.addDate(start, i+1),null);
						try {
							Thread.sleep( 1*30*1000 );///每执行一天 休息 30S
						}catch (Exception e) {
							e.printStackTrace();
							logger.error("休眠异常。",e);
						}
					}
				}
				isNotRunOld = true;
			}
		}catch (Exception e) {
			isNotRunOld = true;
			logger.error("数据处理异常",e);
		}
		return r;
	}
		
	/**
	 * 基础信息-医疗资源采集
	 *
	 * @return
	 * @author 無
	 * @date 2018年6月27日 上午10:51:24
	 */
	@GetMapping("/basic")
    @ResponseBody
    R basic() {
		logger.error("开始医疗资源采集...");
		R r = new R();
		try {
			if(isNotRun) {
				isNotRun = false;
				xmDataCloudService.assemblyBasicData();
				isNotRun = true;
			}
		}catch (Exception e) {
			isNotRun = true;
			logger.error("数据处理异常",e);
		}
		return r;
	}
	
	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
//		String start = "2018-01-01",end="2018-02-01";
//		if(StringUtil.isNotBlank(start) && StringUtil.isNotBlank(end)) {
//			int dif = DateOper.getDateDiff(DateOper.parse(end), DateOper.parse(start), "d");
//			for (int i = 0; i <= dif; i++) {
//				System.out.println(DateOper.addDate(start, i));
//				System.out.println(DateOper.addDate(start, i+1));
//			}
//		}
//		select+*+from+HDSA00_01
		
		//curl http://localhost:8889/Kasite/job/sql?sqlStr=select * from
		System.out.println(java.net.URLEncoder.encode("select * from HDSA00_01"));
		System.out.println(java.net.URLDecoder.decode("http://localhost:8889/Kasite/job/sql?sqlStr=select * from", "utf-8"));
	}
}
