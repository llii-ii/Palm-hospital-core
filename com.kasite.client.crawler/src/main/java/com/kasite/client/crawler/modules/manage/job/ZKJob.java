package com.kasite.client.crawler.modules.manage.job;

import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.common.json.JSONArray;
import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.ConfigBuser;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.XMDataCloudConfig;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

import net.sf.json.JSONObject;

@Component
public class ZKJob {
	private static final Logger logger = LoggerFactory.getLogger(ZKJob.class);
	private static boolean isNotRun = true;
	@Autowired
	private XMDataCloudConfig xMDataCloudConfig;
	
	/**
	 * 每2秒执行一次 sql读取
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void readSqlJob() {
		if(isNotRun && Convent.getIsStartReport()) {
			isNotRun = false;
			try {
				SoapResponseVo respon = HttpRequestBus.create(ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/getSql.do", RequestType.post)
						.addHttpParam("id", xMDataCloudConfig.getOrg_code())
						.send();
				com.common.json.JSONObject result = null;
				com.common.json.JSONObject resultSql = new com.common.json.JSONObject();
				JSONObject json = null;
				if(respon.getCode()==200 && null != respon.getResult()){
					json = JSONObject.fromObject(respon.getResult());
					json = json.getJSONObject("data");
					if(null != json && json.toString().indexOf("sqlStr")!=-1){
						int PageStart2 = Integer.parseInt(json.getString("PageStart"));
						int pageSize2 = Integer.parseInt(json.getString("pageSize"));
						String sqlStr = URLDecoder.decode(json.getString("sqlStr"));
						String db = json.getString("db");
						if(StringUtil.isNotBlank(sqlStr)) {
							Sql sql = new Sql(sqlStr);
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
							result = DB.me().queryForJson(myDatabase, sql,start,pageSize2);
//							System.out.println(result);
							com.common.json.JSONArray arr = new JSONArray();
							arr.put("{\"sql\":\""+sqlStr+"\"}");
							arr.put(result.get("result"));
							resultSql.put("result", arr);
							resultSql.put("totalProperty", result.get("totalProperty"));
							System.out.println(resultSql.toString());
							HttpRequestBus.create(ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/receiveResult.do", RequestType.post)
									.encode("UTF-8")
									.addHttpParam("id", xMDataCloudConfig.getOrg_code())
									.addHttpParam("result", resultSql.toString())
									.send();
						}
					}
				}else{
					HttpRequestBus.create(ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/receiveResult.do", RequestType.post)
					.addHttpParam("id", xMDataCloudConfig.getOrg_code())
					.addHttpParam("result", respon.getResult())
					.send();
				}
			}catch (Exception e) {
				logger.error("数据处理异常",e);
				HttpRequestBus.create(ConfigBuser.create().getDatacloudConfigVo().getMedical_Url()+"online/receiveResult.do", RequestType.post)
						.addHttpParam("id", xMDataCloudConfig.getOrg_code())
						.addHttpParam("result", e.getMessage())
						.send();
				isNotRun = true;
			}
			isNotRun = true;
		}
	}
	
}