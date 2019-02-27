package com.kasite.client.crawler.modules.manage.dao.report;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.MyTableEnum;
import com.kasite.client.crawler.modules.manage.bean.report.dbo.Report;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 数据占比数据库操作类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月25日 上午16:54:00
 */
public class ReportDao {
	
	
	//按 hospital_id 获得上报数；
	public static JSONObject reportByhosId(String municipal,String county,String hosName,String date) throws Exception {
		Integer allTrueNum = 0;
		Integer allFalseNum = 0;
		Sql sql = new Sql("select hospital_id,hospital_name,sum(report_false),sum(report_true) from tb_report_statistics where hospital_id in (select id from tb_equipment_online where 1=1 @a) @b group by hospital_id,hospital_name");

		StringBuffer paramSql = new StringBuffer();
		select(municipal, county, hosName, sql, paramSql);
		//时间查询
		StringBuffer paramTimeSql = new StringBuffer();
		if (StringUtil.isNotBlank(date)) {
			paramTimeSql.append(" and date = ? ");
			sql.addParamValue(date);
		}
		
		sql.addVar("@a", paramSql.toString());
		sql.addVar("@b", paramTimeSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("hospitalId", StringUtil.getJSONValue(map.get("hospital_id")));
			obj.put("hospitalName", StringUtil.getJSONValue(map.get("hospital_name")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			//累加统计总和
			allTrueNum = allTrueNum + Integer.parseInt(StringUtil.getJSONValue(map.get("sum(report_true)")));
			allFalseNum = allFalseNum + Integer.parseInt(StringUtil.getJSONValue(map.get("sum(report_false)")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("allFalseNum", allFalseNum);
		jsonObject.put("allTrueNum", allTrueNum);
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	//按 hospital_id,date 获得上报数；
	public static JSONObject reportByhosIdAndDate(Integer PageSize,Integer PageStart,String municipal,String county,String hosName,String startTime,String endTime) throws Exception {
		
		Sql sql = new Sql("select hospital_id,sum(report_false),sum(report_true),sum(check_num),sum(convert_num),date from tb_report_statistics where hospital_id in (select id from tb_equipment_online where 1=1 @a) @b group by hospital_id,date");

		StringBuffer paramSql = new StringBuffer();
		select(municipal, county, hosName, sql, paramSql);
		
		//时间查询
		StringBuffer paramTimeSql = new StringBuffer();
		if (StringUtil.isNotBlank(startTime) || StringUtil.isNotBlank(endTime)) {
			paramTimeSql.append(" and date BETWEEN ? AND ? ");
			sql.addParamValue(startTime);
			sql.addParamValue(endTime);
		}
		
		sql.addVar("@a", paramSql.toString());
		sql.addVar("@b", paramTimeSql.toString());
		
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("date", StringUtil.getJSONValue(map.get("date")));
			obj.put("hospitalId", StringUtil.getJSONValue(map.get("hospital_id")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			obj.put("checkNum", StringUtil.getJSONValue(map.get("sum(check_num)")));
			obj.put("convertNum", StringUtil.getJSONValue(map.get("sum(convert_num)")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("dayBetween", DateUtils.daysBetween(startTime, endTime));
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	
	//按 date 获得上报数；
	public static JSONObject reportByDate(Integer PageSize,Integer PageStart,String municipal,String county,String hosName,String startTime,String endTime) throws Exception {
		Sql sql = new Sql("select sum(report_false),sum(report_true),sum(check_num),sum(convert_num),date from tb_report_statistics where hospital_id in (select id from tb_equipment_online where 1=1 @a) @b group by date");

		StringBuffer paramSql = new StringBuffer();
		select(municipal, county, hosName, sql, paramSql);
		
		//时间查询
		StringBuffer paramTimeSql = new StringBuffer();
		if (StringUtil.isNotBlank(startTime) || StringUtil.isNotBlank(endTime)) {
			paramTimeSql.append(" and date BETWEEN ? AND ? ");
			sql.addParamValue(startTime);
			sql.addParamValue(endTime);
		}
		
		sql.addVar("@a", paramSql.toString());
		sql.addVar("@b", paramTimeSql.toString());
		
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("date", StringUtil.getJSONValue(map.get("date")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			obj.put("checkNum", StringUtil.getJSONValue(map.get("sum(check_num)")));
			obj.put("convertNum", StringUtil.getJSONValue(map.get("sum(convert_num)")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	
	//按 business_id 获得上报数；
	public static JSONObject reportByBusId(Integer PageSize,Integer PageStart,String municipal,String county,String hosName,String startTime,String endTime) throws Exception {
		Integer allTrueNum = 0;
		Integer allFalseNum = 0;
		
		Sql sql = new Sql("select business_id,business_name,sum(report_false),sum(report_true) from tb_report_statistics where hospital_id in (select id from tb_equipment_online where 1=1 @a) @b group by business_id,business_name limit ?,?");
		Sql all_sql = new Sql("select business_id,business_name,sum(report_false),sum(report_true) from tb_report_statistics where hospital_id in (select id from tb_equipment_online where 1=1 @a) @b group by business_id,business_name");

		
		StringBuffer paramSql = new StringBuffer();	
		select(municipal, county, hosName, sql, paramSql);
		select(municipal, county, hosName, all_sql, new StringBuffer());
		
		//时间查询
		StringBuffer paramTimeSql = new StringBuffer();
		if (StringUtil.isNotBlank(startTime) || StringUtil.isNotBlank(endTime)) {
			paramTimeSql.append(" and date BETWEEN ? AND ? ");
			sql.addParamValue(startTime);
			sql.addParamValue(endTime);
			all_sql.addParamValue(startTime);
			all_sql.addParamValue(endTime);
		}
		
		//分页
		Integer pageStart = PageStart * PageSize;
		Integer pageEnd = PageSize;
		sql.addParamValue(pageStart);
		sql.addParamValue(pageEnd);
		
		sql.addVar("@a", paramSql.toString());
		sql.addVar("@b", paramTimeSql.toString());
		all_sql.addVar("@a", paramSql.toString());
		all_sql.addVar("@b", paramTimeSql.toString());
	
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		List all_list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, all_sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("businessId", StringUtil.getJSONValue(map.get("business_id")));
			obj.put("businessName", StringUtil.getJSONValue(map.get("business_name")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			//累加统计总和
			allTrueNum = allTrueNum + Integer.parseInt(StringUtil.getJSONValue(map.get("sum(report_true)")));
			allFalseNum = allFalseNum + Integer.parseInt(StringUtil.getJSONValue(map.get("sum(report_false)")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("allFalseNum", allFalseNum);
		jsonObject.put("allTrueNum", allTrueNum);
		jsonObject.put("totalProperty",all_list.size());//总条数
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	//获取作业效率
	public static JSONObject getEfficiency(Integer PageSize,Integer PageStart,String municipal,String county,String hosName,String date) throws Exception {
		
		String sqlStr = "select * from tb_work_time w LEFT JOIN "
					  + "(select hospital_id,hospital_name,date,SUM(report_false),SUM(report_true),SUM(check_num),SUM(convert_num) "
					  + "from tb_report_statistics GROUP BY hospital_id,hospital_name,date) r "
					  + "on w.hospital_id = r.hospital_id and r.date = w.work_date where w.hospital_id in (select id from tb_equipment_online where 1=1 @a) @b limit ?,?";
		Sql sql = new Sql(sqlStr);

		StringBuffer paramSql = new StringBuffer();
		select(municipal, county, hosName, sql, paramSql);
		
		//时间查询
		StringBuffer paramTimeSql = new StringBuffer();
		if (StringUtil.isNotBlank(date)) {
			paramTimeSql.append(" and r.date = ? ");
			sql.addParamValue(date);
		}
		
		//分页
		Integer pageStart = PageStart * PageSize;
		Integer pageEnd = PageSize;
		sql.addParamValue(pageStart);
		sql.addParamValue(pageEnd);
		
		sql.addVar("@a", paramSql.toString());
		sql.addVar("@b", paramTimeSql.toString());

		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("hospitalName", StringUtil.getJSONValue(map.get("hospital_name")));
			
			//获取所有数据
			int report_true = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(report_true)")));
			int report_false = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(report_false)")));
			int check_num = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(check_num)")));
			int convert_num = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(convert_num)")));
			
			int report_time = Integer.parseInt(StringUtil.getJSONValue(map.get("report_time")));
			int collect_time = Integer.parseInt(StringUtil.getJSONValue(map.get("collect_time")));
			int convert_time = Integer.parseInt(StringUtil.getJSONValue(map.get("convert_time")));
			int check_time = Integer.parseInt(StringUtil.getJSONValue(map.get("check_time")));
			
			
			obj.put("reportEff", df.format((float)report_true/report_time));
			obj.put("collectEff", df.format((float)(report_true+report_false)/collect_time));
			obj.put("convertEff", df.format((float)convert_num/convert_time));
			obj.put("checkEff", df.format((float)check_num/check_time));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	
	//获取作业效率-图
	public static JSONObject getChartEfficiency(String startTime,String endTime) throws Exception {	
		String sqlStr = "select * from tb_work_time w LEFT JOIN "
				  + "(select hospital_id,hospital_name,date,SUM(report_false),SUM(report_true),SUM(check_num),SUM(convert_num) "
				  + "from tb_report_statistics GROUP BY hospital_id,hospital_name,date) r "
				  + "on w.hospital_id = r.hospital_id and r.date = w.work_date where 1=1 @a";

		
		Sql sql = new Sql(sqlStr);

		StringBuffer paramSql = new StringBuffer();
		
		//时间查询
		if (StringUtil.isNotBlank(startTime) || StringUtil.isNotBlank(endTime)) {
			paramSql.append(" and r.date BETWEEN ? AND ? ");
			sql.addParamValue(startTime);
			sql.addParamValue(endTime);
		}
		sql.addVar("@a", paramSql.toString());

		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数  
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("hospitalId", StringUtil.getJSONValue(map.get("hospital_id")));
			obj.put("date", StringUtil.getJSONValue(map.get("date")));
			obj.put("hospitalName", StringUtil.getJSONValue(map.get("hospital_name")));
			
			//获取所有数据
			int report_true = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(report_true)")));
			int report_false = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(report_false)")));
			int check_num = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(check_num)")));
			int convert_num = Integer.parseInt(StringUtil.getJSONValue(map.get("SUM(convert_num)")));
			
			int report_time = Integer.parseInt(StringUtil.getJSONValue(map.get("report_time")));
			int collect_time = Integer.parseInt(StringUtil.getJSONValue(map.get("collect_time")));
			int convert_time = Integer.parseInt(StringUtil.getJSONValue(map.get("convert_time")));
			int check_time = Integer.parseInt(StringUtil.getJSONValue(map.get("check_time")));
			
			
			obj.put("reportEff", df.format((float)report_true/report_time));
			obj.put("collectEff", df.format((float)(report_true+report_false)/collect_time));
			obj.put("convertEff", df.format((float)convert_num/convert_time));
			obj.put("checkEff", df.format((float)check_num/check_time));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Common.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Common.Success.RET_10000.getMessage());
		jsonObject.put("dayBetween", DateUtils.daysBetween(startTime, endTime));
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	//新增
	public static void addReport(Report report) throws SQLException {
		Sql sql = DB.me().createInsertSql(report, MyTableEnum.tb_report_statistics);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	//修改
	public static void updateReport(Report report) throws SQLException {
		String id = report.getId();
		report.setId(null);
		Sql sql = DB.me().createUpdateSql(report, MyTableEnum.tb_report_statistics," id = ? ");
		sql.addParamValue(id);
		DB.me().update(MyDatabaseEnum.crawler_zk, sql);
	}
	
	
	//查询是否存在记录
	public static Report reportSelect(Report report) throws SQLException {
		Sql sql = DB.me().createSelect(report, MyTableEnum.tb_report_statistics);
		return DB.me().queryForBean(MyDatabaseEnum.crawler_zk, sql,Report.class);
	}
	
	
	//三个查询条件
	private static void select(String municipal,String county,String hosName,Sql sql,StringBuffer paramSql) {
		//市查询
		if (StringUtil.isNotBlank(municipal)) {
			paramSql.append(" and municipal = ? ");
			sql.addParamValue(municipal);
		}
		//县区查询
		if (StringUtil.isNotBlank(county)) {
			paramSql.append(" and county = ? ");
			sql.addParamValue(county);
		}
		//医院查询
		if (StringUtil.isNotBlank(hosName)) {
			paramSql.append(" and name = ? ");
			sql.addParamValue(hosName);
		}
	}
}
