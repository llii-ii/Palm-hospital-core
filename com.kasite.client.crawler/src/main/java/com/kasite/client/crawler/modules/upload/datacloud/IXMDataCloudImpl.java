package com.kasite.client.crawler.modules.upload.datacloud;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.json.JSONArray;
import com.common.json.JSONObject;
import com.coreframework.db.DB;
import com.coreframework.db.DatabaseEnum;
import com.coreframework.db.Sql;
import com.coreframework.util.JsonUtil;
import com.coreframework.util.StringUtil;
import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.config.DruidConfig;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.XMDataCloudConfig;
import com.kasite.client.crawler.config.data.Rule1_5Bus;
import com.kasite.client.crawler.config.data.vo.Data15PkVo;
import com.kasite.client.crawler.modules.utils.Uploadutil;
import com.kasite.core.common.config.DBType;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.httpclient.http.SoapResponseVo;

@Service("xmDataCloudService")
public class IXMDataCloudImpl implements IXMDataCloudService{
	
	@Autowired
	private DruidConfig druidConfig;
	
	@Autowired
	private XMDataCloudConfig xMDataCloudConfig;
	private static final Logger logger = LoggerFactory.getLogger(IXMDataCloudImpl.class);
	//如果来不及处理的文件了 需要暂停一会生成 传输完成后再启动上传。
	private static boolean isStop = false;
	//医疗资源api
	private static String doctorApi="rhip.doctor.post";
	private static String orgApi="rhip.organizations.post";
	public static synchronized void stop() {
		isStop = true;
	}
	public static synchronized void start() {
		isStop = false;
	}
	
	/**
	 * 数据采集
	 */
	@Override
	public void assemblyData(String startDate,String endDate,String eventNo) {
		try {
			logger.info("执行作业：查询范围 ["+startDate +"]-["+endDate+"]-["+eventNo+"].");
			//根据采集范围采集数据并上传
			Map<String,Data15PkVo> tableMap = Rule1_5Bus.getInstall().getData15Map("crawler");
			for (String crawlerName:tableMap.keySet()) {
				Data15PkVo vo = tableMap.get(crawlerName);
				/**医疗资源跳过  另一个上传接口*/
				if("MedicalResourcesTable".equals(vo.getPrivateName()) || vo.getIsNotNum()==0){
					continue;
				}
				dealData(startDate, endDate,vo.getPrivateName(),eventNo);
			}
			//质检包处理上传
			if(StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)){
				DealQualityControlPackage(startDate, endDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据处理出现异常："+StringUtil.getExceptionStack(e)+"参数：startDate="+startDate+",endDate="+endDate);
		}
	}
	
	/**
	 * 数据处理
	 * @param dateTime
	 * @throws SQLException 
	 */
	public void dealData(String startDate,String endDate,String fileName,String eventNoParam) throws Exception {
		//门诊表获取
		Map<String,Data15PkVo> tableMap = Rule1_5Bus.getInstall().getData15Map(fileName);
		if(null==tableMap) {
			logger.error("数据处理出现异常：未配置表文件 == "+fileName);
			return;
		}
		//规则获取
		Map<String,Data15PkVo> ruleMap = Rule1_5Bus.getInstall().getData15Map("RULE");
		if(null==ruleMap) {
			logger.error("数据处理出现异常：未配置规则文件 == RULE");
			return;
		}
		//系统常量
		XMDataCloudConfig config = xMDataCloudConfig;
		//门诊主表获取
		Data15PkVo vo = tableMap.get("0");
		String tableName =vo.getHeadIndex();
		
		Data15PkVo v_event_no = ruleMap.get(tableName+"_event_no");
		if(null == v_event_no) {
			logger.error("获取表事件ID异常："+tableName+"_event_no");
			return ;
		}
		Data15PkVo v_patient_id = ruleMap.get(tableName+"_patient_id");
		if(null == v_patient_id) {
			logger.error("获取表patient_id异常："+tableName+"_patient_id");
			return ;
		}
		//获取规则中的头部数据
		String event_no = v_event_no.getHeadIndex();
		String patient_id = v_patient_id.getHeadIndex();
		String event_time = ruleMap.get(tableName+"_event_time").getHeadIndex();
		//根据时间时间对主表进行数据捞取
		SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
		//根据时间时间对主表进行数据捞取 Convent
		StringBuffer selectSql = new StringBuffer("select * from ");
		StringBuffer countSql = new StringBuffer("select count(*) from ");
		/**支持视图名 或 sql 方式查询*/
		if(StringUtil.isNotBlank(vo.getSql())) {
			selectSql.append("("+vo.getSql()+") s");
			countSql.append("("+vo.getSql()+") s");
		}else if(StringUtil.isNotBlank(tableName)) {
			selectSql.append(tableName);
			countSql.append(tableName);
		}else {
			return ;
		}
		selectSql.append(" where 1=1 ");
		countSql.append(" where 1=1 ");
		if(StringUtil.isNotBlank(eventNoParam)) {
			if(druidConfig.getDbType().equals(DBType.oracle)) {
				selectSql.append(" and ").append(event_no).append(" ='").append(eventNoParam).append("'");
				countSql.append(" and ").append(event_no).append(" ='").append(eventNoParam).append("'");
			}else {
				selectSql.append(" and ").append(event_no).append(" ='").append(eventNoParam).append("'");
				countSql.append(" and ").append(event_no).append(" ='").append(eventNoParam).append("'");
			}
		}
		if(StringUtil.isNotBlank(event_time) && StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
			if(druidConfig.getDbType().equals(DBType.oracle)) {
				selectSql.append(" and ").append(event_time)
				.append(" between to_date('").append(startDate).append("','yyyy-MM-dd')")
				.append(" and to_date('").append(endDate).append("','yyyy-MM-dd')").append(" order by ").append(event_time);
				countSql.append(" and ").append(event_time)
				.append(" between to_date('").append(startDate).append("','yyyy-MM-dd')")
				.append(" and to_date('").append(endDate).append("','yyyy-MM-dd')");
			}else {
				selectSql.append(" and ").append(event_time)
				.append(" between '").append(startDate).append("'")
				.append(" and '").append(endDate).append("'").append(" order by ").append(event_time);
				countSql.append("and ").append(event_time)
				.append(" between '").append(startDate).append("'")
				.append(" and '").append(endDate).append("'");
			}
		}
		if(Convent.getIsPrint()) {
			logger.info("主表Sql="+selectSql);
		}
		//查询符合时间的记录总数
		int count = DB.me().queryForInteger(MyDatabaseEnum.hisdb, new Sql(countSql.toString()));
		//配置文件获取分页条数
		int pageSize = Convent.getPageSize();
		//计算页数
		int pageIndex = (int) Math.ceil(count/pageSize)+1;
		int fileCount = 0;
		/**副表的上报数*/
		Map<String, Integer> otherMap = new HashMap<String, Integer>();
		for (int i = 0; i < pageIndex; i++) {
			if(isStop) {
				logger.error("需要休息1分钟");
				Thread.sleep(60*1000);//如果上传无法及时提交需要休息1分钟后再执行
			}
			logger.info("开始查询:"+tableName + "->" +startDate);
			long t = System.currentTimeMillis();
			com.common.json.JSONObject queryJson = DB.me().queryForJson(MyDatabaseEnum.hisdb, new Sql(selectSql.toString()),i*pageSize,pageSize);
			logger.info("此次查询耗时："+ (System.currentTimeMillis() - t));
			if(queryJson!=null && queryJson.getJSONArray("result")!=null) {
				com.common.json.JSONArray arr = queryJson.getJSONArray("result");
				logger.info("数量："+ arr.length());
				for (int j = 0; j < arr.length(); j++) {
					com.common.json.JSONObject obj = arr.getJSONObject(j);
					com.common.json.JSONObject json_head = new com.common.json.JSONObject();
					json_head.put("inner_version", config.getInner_version());
					String patientId = JsonUtil.getJsonString(obj, patient_id.toLowerCase());
					if (null != patientId) {
						patientId = patientId.trim();
					}
					json_head.put("patient_id", patientId);
					String eventNo = JsonUtil.getJsonString(obj, event_no.toLowerCase());
					if (null != eventNo) {
						eventNo = eventNo.trim();
					}
					json_head.put("event_no", eventNo);
					json_head.put("org_code", config.getOrg_code());
					String eventTime=JsonUtil.getJsonString(obj, event_time.toLowerCase());
					if(StringUtil.isNotBlank(eventTime)) {
						String dateStr = JsonUtil.getJsonString(obj, event_time.toLowerCase());
						if(null != dateStr && dateStr.length() > 19) {
							dateStr = dateStr.substring(0, 19);
						}
						Date date = DateOper.parse(DateOper.formatDate(dateStr, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss"));
						json_head.put("event_time", df2.format(date));
					}else {
						json_head.put("event_time", "");
					}
					json_head.put("create_date", df2.format(new Date()));
					json_head.put("code", vo.getPrivateType());
					//数据组装
					com.common.json.JSONObject data_Json = json_head;
					com.common.json.JSONArray data = new com.common.json.JSONArray();
					data.put(obj);
					data_Json.put("data", data);
					Map<String, String> queryConditions = getRule(ruleMap, obj, tableName);
					//打包数据-结构化
					String dirName = UUID.randomUUID().toString();
					//打包数据-非结构化
					String dirNameFjg = UUID.randomUUID().toString();
					try {
						//当一条记录写入出现异常的时候就不写入了
						//数据主表  ** 当主表写入失败，关联的表就不用写入了，一样会异常
						DataCloudFileUtil.write(data_Json, dirName,false);
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("数据写入异常："+ data_Json);
						continue;
					}
					/**处理副表*/
					for (int k = 1; k < tableMap.size(); k++) {
						String tableKey = k+"";
						Data15PkVo data15PkVo = tableMap.get(tableKey);
						if(null==data15PkVo) {
							try {
								StringBuffer sbf = new StringBuffer("数据处理出现异常：未取到标识为"+k+"对应的表名,tabName="+tableName);
								if(null != vo) {
									sbf.append("code=").append(vo.getPrivateType());
								}
								if(null != tableMap) {
									sbf.append("tableMap").append(com.alibaba.fastjson.JSONObject.toJSON(tableMap).toString());
								}
								logger.error(sbf.toString());
							}catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						
						if(null != data15PkVo.getIsNotNum() && data15PkVo.getIsNotNum() != 0) {
							/**插入一个初始副表*/
							if (StringUtil.isBlank(otherMap.get(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes()))) {	
								otherMap.put(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes(), 0);			
							}
						}
						
						String otherTableName = data15PkVo.getHeadIndex();
						json_head.put("code", data15PkVo.getPrivateType());
						/**1=结构化文档、2=ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、5=Base64 XML 和 String XML 混合*/
						if(null == data15PkVo.getIsNotNum() || data15PkVo.getIsNotNum() == 0) {
							continue;
						}else if(data15PkVo.getIsNotNum() == 1){
							/*** 处理副表数据 */
							dealOtherDate(otherMap,json_head, fileName,otherTableName, queryConditions, ruleMap, dirName, data15PkVo,obj);
						}else{
							/*** 处理非结构化副表数据*/
							dealOtherDate(otherMap,json_head, fileName,otherTableName, queryConditions, ruleMap, dirNameFjg, data15PkVo,obj);
						}
					}
					//结束上传数据
					DataCloudFileUtil.writEnd(dirName,false,1);
					DataCloudFileUtil.writEnd(dirNameFjg,false,2);
					fileCount++;
				}
			}
		}
		logger.error("表："+tableName+",开始时间："+startDate+"结束时间:"+endDate+"共有"+count+"条数据,上传文件数："+fileCount+"个。");
		
		try {
			/**质控后台上报新增-主表*/
			Uploadutil.uploadAddReport(startDate, config.getOrg_code(), vo.getPrivateType(), vo.getPrivateDes(), count-fileCount, fileCount, fileCount, fileCount);
			/**质控后台上报新增-副表*/
			for (String key : otherMap.keySet()) {
				Integer c = otherMap.get(key);
				System.out.println(key+","+c);
				Uploadutil.uploadAddReport(startDate, config.getOrg_code(), key.split("\\|")[0], key.split("\\|")[1], 0, c, c, c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取规则
	 * @param ruleMap
	 * @param json
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getRule(Map<String,Data15PkVo> ruleMap,com.common.json.JSONObject json,String tableName) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		for (String key:ruleMap.keySet()) {
			Data15PkVo vo = ruleMap.get(key);
			if(vo.getPrivateName().indexOf(tableName)>=0) {
				String mapKey = vo.getPrivateName().substring(vo.getPrivateName().indexOf(tableName)+tableName.length()+1, vo.getPrivateName().length());
				map.put(mapKey, JsonUtil.getJsonString(json, vo.getHeadIndex().toLowerCase()));
			}
		}
		return map;
	}
	
	/**
	 * 处理副表
	 * @param json_head
	 * @param tableName
	 * @param otherTableName
	 * @param queryConditions
	 * @param ruleMap
	 * @param dirName
	 * @param data15PkVo
	 * @param obj
	 * @throws Exception
	 */
	private void dealOtherDate(Map<String, Integer> otherMap,com.common.json.JSONObject json_head,String tableName,String otherTableName,Map<String, String> queryConditions,Map<String,Data15PkVo> ruleMap,String dirName,Data15PkVo data15PkVo,JSONObject obj) throws Exception {
		StringBuffer selectSql = new StringBuffer("select * from ");
		/**支持视图名 或 sql 方式查询*/
		if(StringUtil.isNotBlank(data15PkVo.getSql())) {
			selectSql.append("("+data15PkVo.getSql()+") s");
		}else if(StringUtil.isNotBlank(otherTableName)) {
			selectSql.append(otherTableName);
		}else {
			return ;
		}
		selectSql.append(" where 1=1");
		for (String key : queryConditions.keySet()) {
			//sql组装
			if (null != ruleMap.get(otherTableName + "_" + key)) {
				String condition = ruleMap.get(otherTableName + "_" + key).getHeadIndex().toLowerCase();
				if (StringUtil.isNotBlank(queryConditions.get(key))) {
					String keyValue= queryConditions.get(key).trim();
					selectSql.append(" and ").append(condition).append("='").append(keyValue).append("'");
				}
			}
		}
		Integer isNotNum = data15PkVo.getIsNotNum();
		if(Convent.getIsPrint()) {
			logger.info("副表Sql="+selectSql);
		}
		/**选择副表的数据源*/
		DatabaseEnum db = MyDatabaseEnum.hisdb;
		if ("db".equals(data15PkVo.getPrivateDictName())) {
			db = MyDatabaseEnum.hisdb;
		}else if ("zk".equals(data15PkVo.getPrivateDictName())) {
			db = MyDatabaseEnum.his_ecg;
		}else if ("lis".equals(data15PkVo.getPrivateDictName())) {
			db = MyDatabaseEnum.lis;
		}else {
			db = MyDatabaseEnum.hisdb;
		}
		/**若查询副表超时、出错、 重新查询一次*/
		com.common.json.JSONObject queryJson = null;
		try {
			queryJson = DB.me().queryForJson(db, new Sql(selectSql.toString()));
			if(queryJson ==null){
				queryJson = DB.me().queryForJson(db, new Sql(selectSql.toString()));
			}
		} catch (Exception e) {
			try {
				queryJson = DB.me().queryForJson(db, new Sql(selectSql.toString()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		int count = 0;
		if(queryJson!=null && queryJson.getJSONArray("result")!=null) {
			com.common.json.JSONArray arr = queryJson.getJSONArray("result");
			count = arr.length();
			if(Convent.getIsPrint()) {
				logger.info("副表数量="+count+"条");
			}
			if(count>0) {
				/**上报数*/
				try {
					Integer otherCount = otherMap.get(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes());
					if(null!=otherCount){
						otherMap.put(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes(), otherCount + 1);
					}else {
						otherMap.put(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes(),1);
					}
				} catch (Exception e1) {
					otherMap.put(data15PkVo.getPrivateType()+"|"+data15PkVo.getPrivateDes(), 1);
					e1.printStackTrace();
				}
				//数据组装
				com.common.json.JSONObject data_Json = json_head;
				data_Json.put("data", arr);
				try {
					/**1=结构化文档、2=ZIP byte XML、3=文件地址  jpg、pdf 4=String XML、5=Base64 XML 和 String XML 混合*/
					if (isNotNum == 1) {
						DataCloudFileUtil.write(data_Json, dirName, false);
					}else {
						/*** 处理非结构化副表数据 dirName将重新生成*/
						DataCloudFileUtil.writeDocument(data_Json,obj,tableName,otherTableName,isNotNum,dirName);
					}
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("数据写入异常："+ data_Json);
				}
			}
		}
	}
	
	/**
	 * 质检包上传
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	public void DealQualityControlPackage(String startDate,String endDate)throws Exception {
		logger.info("开始上传质控包");
		//系统常量
		XMDataCloudConfig config = xMDataCloudConfig;
		//根据时间时间对主表进行数据捞取
		SimpleDateFormat df2 = new SimpleDateFormat(Convent.TIMEFORMAT);
		com.common.json.JSONObject json_head = new com.common.json.JSONObject();
		json_head.put("org_code", config.getOrg_code());
		json_head.put("create_date", df2.format(new Date()));
		json_head.put("inner_version", config.getInner_version());
		json_head.put("code", "HSI07_01");
		Map<String,Data15PkVo> tableMap = Rule1_5Bus.getInstall().getData15Map("QualityControlPackage");
		if(null == tableMap) {
			logger.error("质控包未配置：QualityControlPackage");
		}
		int sum = 0;
		JSONArray dataArray = new JSONArray();
		JSONObject data = new JSONObject();
		data.put("event_date", startDate);
		Data15PkVo sumVo = null;
		for (String crawlerName:tableMap.keySet()) {
			Data15PkVo vo = tableMap.get(crawlerName);
			if(vo.getIsNotNum()==0) {
				data.put(vo.getPrivateName(), 0);
				if("sum".equals(vo.getHeadIndex())) {
					sumVo = vo;
				}
				continue;
			}
			/**选择副表的数据源*/
			DatabaseEnum db = MyDatabaseEnum.hisdb;
			if ("db".equals(vo.getPrivateDictName())) {
				db = MyDatabaseEnum.hisdb;
			}else if ("zk".equals(vo.getPrivateDictName())) {
				db = MyDatabaseEnum.his_ecg;
			}else if ("lis".equals(vo.getPrivateDictName())) {
				db = MyDatabaseEnum.lis;
			}else {
				db = MyDatabaseEnum.hisdb;
			}
			StringBuffer countSql = new StringBuffer("select count(*) from ");
			/**支持视图名 或 sql 方式查询*/
			if(StringUtil.isNotBlank(vo.getSql())) {
				countSql.append("("+vo.getSql()+") s");
			}else if(StringUtil.isNotBlank(vo.getPrivateType())) {
				countSql.append(vo.getPrivateType());
			}else {
				return ;
			}
			if(StringUtil.isNotBlank(vo.getHeadIndex())) {
				if(druidConfig.getDbType().equals(DBType.oracle)) {
					countSql.append(" where 1=1 ").append("and ").append(vo.getHeadIndex())
					.append(" between to_date('").append(startDate).append("','yyyy-MM-dd')")
					.append(" and to_date('").append(endDate).append("','yyyy-MM-dd')");
				}else {
					countSql.append(" where 1=1 ").append("and ").append(vo.getHeadIndex())
					.append(" between '").append(startDate).append("'")
					.append(" and '").append(endDate).append("'");
				}
			}
			//查询符合时间时间的记录总数
			int count = DB.me().queryForInteger(db, new Sql(countSql.toString()));
			data.put(vo.getPrivateName(), count);
			System.out.println(vo.getPrivateDes()+"["+count+"]="+countSql.toString());
			sum = sum+count;
		}
		data.put(sumVo.getPrivateName(), sum);
		com.common.json.JSONObject data_Json = json_head;
		dataArray.put(data);
		data_Json.put("data", dataArray);
		logger.info("质控包：" + data_Json);
		//打包数据
		String dirName = UUID.randomUUID().toString();
		DataCloudFileUtil.writeControlPackage(data_Json, dirName,true);
		//结束上传数据
		DataCloudFileUtil.writEnd(dirName,true,null);
	}
	
	
	/**
	 * 采集医疗资源数据
	 *
	 * @author 無
	 * @date 2018年6月26日 下午6:05:28
	 */
	public void assemblyBasicData() {
		try {
			logger.info("执行作业：采集医疗资源数据");
			//根据采集范围采集数据并上传
			Map<String,Data15PkVo> tableMap = Rule1_5Bus.getInstall().getData15Map("crawler");
			for (String crawlerName:tableMap.keySet()) {
				Data15PkVo vo = tableMap.get(crawlerName);
				/**医院资源信息&&采集=1的才采集*/
				if(!"MedicalResourcesTable".equals(vo.getPrivateName()) || vo.getIsNotNum()==0){
					continue;
				}
				dealBasicData(vo.getPrivateName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("采集医疗资源数据出现异常："+StringUtil.getExceptionStack(e));
		}
	}
	
	
	
	/**
	 * 处理基础数据-医疗资源
	 *
	 * @param startDate
	 * @param endDate
	 * @param fileName
	 * @param eventNo
	 * @throws Exception
	 * @author 無
	 * @date 2018年6月26日 下午5:58:55
	 */
	public void dealBasicData(String fileName) throws Exception {
		//资源采集表获取
		Map<String,Data15PkVo> tableMap = Rule1_5Bus.getInstall().getData15Map(fileName);
		if(null==tableMap) {
			logger.error("数据处理出现异常：未配置表文件 == "+fileName);
			return;
		}
		//规则获取
		Map<String,Data15PkVo> ruleMap = Rule1_5Bus.getInstall().getData15Map("RULE");
		if(null==ruleMap) {
			logger.error("数据处理出现异常：未配置规则文件 == RULE");
			return;
		}
		//系统常量
		XMDataCloudConfig config = xMDataCloudConfig;
		//主表获取
		Data15PkVo vo = tableMap.get("0");
		String tableName =vo.getHeadIndex();
		StringBuffer selectSql = new StringBuffer("select * from ");
		StringBuffer countSql = new StringBuffer("select count(*) from ");
		/**视图或sql语句方式*/
		if(StringUtil.isNotBlank(vo.getSql())) {
			selectSql.append("("+vo.getSql()+") s");
			countSql.append("("+vo.getSql()+") s");
		}else if(StringUtil.isNotBlank(tableName)) {
			selectSql.append(tableName);
			countSql.append(tableName);
		}else {
			return ;
		}
		if(Convent.getIsPrint()) {
			logger.info("selectSql="+selectSql);
		}
		//查询记录总数
		int count = DB.me().queryForInteger(MyDatabaseEnum.hisdb, new Sql(countSql.toString()));
		//配置文件获取分页条数
		int pageSize = Convent.getPageSize();
		//计算页数
		int pageIndex = (int) Math.ceil(count/pageSize)+1;
		int fileCount = 0;
		for (int i = 0; i < pageIndex; i++) {
			if(isStop) {
				logger.error("需要休息1分钟");
				Thread.sleep(60*1000);//如果上传无法及时提交需要休息1分钟后再执行
			}
			logger.info("开始查询。");
			long t = System.currentTimeMillis();
			com.common.json.JSONObject queryJson = DB.me().queryForJson(MyDatabaseEnum.hisdb, new Sql(selectSql.toString()),i*pageSize,pageSize);
			logger.info("此次查询耗时："+ (System.currentTimeMillis() - t));
			if(queryJson!=null && queryJson.getJSONArray("result")!=null) {
				com.common.json.JSONArray arr = queryJson.getJSONArray("result");
				for (int j = 0; j < arr.length(); j++) {
					com.common.json.JSONObject obj = arr.getJSONObject(j);
					com.common.json.JSONObject mOrganizationJsonData = new com.common.json.JSONObject();
					mOrganizationJsonData.put("orgCode", config.getOrg_code());
					mOrganizationJsonData.put("fullName", JsonUtil.getJsonString(obj, "hdsd00_18_010"));
					mOrganizationJsonData.put("shortName", JsonUtil.getJsonString(obj, "hdsd00_18_010"));
					mOrganizationJsonData.put("orgType", "Hospital");
					mOrganizationJsonData.put("legalPerson", JsonUtil.getJsonString(obj, "hdsd00_18_007"));
					mOrganizationJsonData.put("admin", JsonUtil.getJsonString(obj, "hdsd00_18_007"));
					mOrganizationJsonData.put("tel", JsonUtil.getJsonString(obj, "hdsd00_18_008"));
					mOrganizationJsonData.put("location",
									JsonUtil.getJsonString(obj, "hdsd00_18_003") 
									+ JsonUtil.getJsonString(obj, "hdsd00_18_004")
									+ JsonUtil.getJsonString(obj, "hdsd00_18_005")
									+ JsonUtil.getJsonString(obj, "hdsd00_18_006")
									+ JsonUtil.getJsonString(obj, "hdsd00_18_001")
									+ JsonUtil.getJsonString(obj, "hdsd00_18_002"));
					mOrganizationJsonData.put("phone", JsonUtil.getJsonString(obj, "hdsd00_18_009"));
					mOrganizationJsonData.put("administrativeDivision", JsonUtil.getJsonString(obj, "hdsd00_18_012"));
					
					com.common.json.JSONObject geographyModelJsonData = new com.common.json.JSONObject();
					geographyModelJsonData.put("province", JsonUtil.getJsonString(obj, "hdsd00_18_003"));
					geographyModelJsonData.put("city", JsonUtil.getJsonString(obj, "hdsd00_18_004"));
					geographyModelJsonData.put("district", JsonUtil.getJsonString(obj, "hdsd00_18_005"));
					geographyModelJsonData.put("town", "");
					geographyModelJsonData.put("street", JsonUtil.getJsonString(obj, "hdsd00_18_006"));
					
					//上传机构信息
					try {
						JSONObject param = new JSONObject();
						param.put("mOrganizationJsonData", mOrganizationJsonData.toString());
						param.put("geography_model_json_data", geographyModelJsonData.toString());
						SoapResponseVo result = Uploadutil.uploadBasicInfo(orgApi, param);
						if (null != result && result.getCode() == 200) {
							if(null!=result.getResult()){
								JSONObject res = new JSONObject(result.getResult());
								if(!"0".equals(JsonUtil.getJsonString(res, "errorCode"))){
									logger.error("数据上传异常："+ obj +",返回:"+result.getResult());
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("数据写入异常："+ obj);
						continue;
					}
					Map<String, String> queryConditions = getRule(ruleMap, obj, tableName);
					for (int k = 1; k < tableMap.size(); k++) {
						String tableKey = k+"";
						Data15PkVo data15PkVo = tableMap.get(tableKey);
						if(null==data15PkVo) {
							try {
								StringBuffer sbf = new StringBuffer("数据处理出现异常：未取到标识为"+k+"对应的表名,tabName="+tableName);
								if(null != vo) {
									sbf.append("code=").append(vo.getPrivateType());
								}
								if(null != tableMap) {
									sbf.append("tableMap").append(com.alibaba.fastjson.JSONObject.toJSON(tableMap).toString());
								}
								logger.error(sbf.toString());
							}catch (Exception e) {
								e.printStackTrace();
							}
							return;
						}
						if(data15PkVo.getIsNotNum()==0) {
							continue;
						}
						String otherTableName = data15PkVo.getHeadIndex();
						dealBasicOtherDate(otherTableName, queryConditions, ruleMap,data15PkVo);
					}
					fileCount++;
				}
			}
		}
		logger.error("表："+tableName+",共有"+count+"条数据,上传："+fileCount+"个。");
	
	}
	
	private void dealBasicOtherDate(String tableName,Map<String, String> queryConditions,Map<String,Data15PkVo> ruleMap,Data15PkVo data15PkVo) throws Exception {
		StringBuffer selectSql = new StringBuffer("select * from ");
		/**支持视图名 或 sql 方式查询*/
		if(StringUtil.isNotBlank(data15PkVo.getSql())) {
			selectSql.append("("+data15PkVo.getSql()+") s");
		}else if(StringUtil.isNotBlank(tableName)) {
			selectSql.append(tableName);
		}else {
			return ;
		}
		selectSql.append(" where 1=1");
		for (String key : queryConditions.keySet()) {
			//sql组装
			if (null != ruleMap.get(tableName + "_" + key)) {
				String condition = ruleMap.get(tableName + "_" + key).getHeadIndex().toLowerCase();
				if (StringUtil.isNotBlank(queryConditions.get(key))) {
					selectSql.append(" and ").append(condition).append("='").append(queryConditions.get(key)).append("'");
				}
			}
		}
		if(Convent.getIsPrint()) {
			logger.info("dealBasicOtherDate="+selectSql);
		}
		com.common.json.JSONObject queryJson = DB.me().queryForJson(MyDatabaseEnum.hisdb, new Sql(selectSql.toString()));
		if(queryJson!=null && queryJson.getJSONArray("result")!=null) {
			com.common.json.JSONArray array = queryJson.getJSONArray("result");
			if(array.length()>0) {
				//数据组装
				com.common.json.JSONObject doctor;
				com.common.json.JSONObject doctorJsonData;
				com.common.json.JSONObject model;
				for (int i = 0; i < array.length(); i++) {
					doctor = array.getJSONObject(i);
					doctorJsonData = new com.common.json.JSONObject();
					doctorJsonData.put("code", JsonUtil.getJsonString(doctor, "jdsc00_04_001"));
					doctorJsonData.put("name", JsonUtil.getJsonString(doctor, "jdsc00_04_002"));
					String sex = JsonUtil.getJsonString(doctor, "hdsc00_04_082");
					if("-".equals(sex) || StringUtil.isBlank(sex)){
						sex = "0";
					}
					doctorJsonData.put("sex", sex);
					String idCardNo = JsonUtil.getJsonString(doctor, "hdsa00_01_017");
					if("-".equals(idCardNo)){
						idCardNo = "";
					}
					doctorJsonData.put("idCardNo", idCardNo);
					doctorJsonData.put("status", "1");
					String skill = JsonUtil.getJsonString(doctor, "hdsc00_04_101");
					if("-".equals(skill)){
						skill = "";
					}
					doctorJsonData.put("skill", skill);
					model = new com.common.json.JSONObject();
					model.put("orgCode", JsonUtil.getJsonString(doctor, "hdsa00_01_003"));
					String deptName = JsonUtil.getJsonString(doctor, "hdsc00_04_100");
					//先用未分配
//					if(StringUtil.isBlank(deptName)){
						deptName = "未分配";
//					}
					model.put("deptName", deptName);
					try {
						/**当一条数据上传失败时、退出循环*/
						JSONObject param = new JSONObject();
						param.put("doctor_json_data", doctorJsonData);
						param.put("model", model);
						SoapResponseVo result = Uploadutil.uploadBasicInfo(doctorApi, param);
						if (null != result && result.getCode() == 200) {
							if(null!=result.getResult()){
								JSONObject res = new JSONObject(result.getResult());
								if(!"0".equals(JsonUtil.getJsonString(res, "errorCode"))){
									logger.error("数据上传异常："+ doctor +",返回:"+result.getResult());
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("数据上传异常："+ doctor);
						break;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String dbType = "sqlserver";
		System.out.println(DBType.valueOf(dbType).equals(DBType.oracle));
	}
}
