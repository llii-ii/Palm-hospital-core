package com.kasite.client.crawler.modules.manage.dao.statistics;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.MyTableEnum;
import com.kasite.client.crawler.modules.manage.bean.online.dbo.Online;
import com.kasite.client.crawler.modules.manage.bean.statistics.dbo.Statistics;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 数据统计数据库操作类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月26日 上午16:54:00
 */
public class StatisticsDao {
	
	public static JSONObject getStatisticsList(String hospitalName,String startDate,String endDate,Integer start, Integer pageSize) throws Exception {

		//医院总数 即前端表格tr数
		Sql sql = new Sql("select hospital_id,hospital_name,sum(report_false),sum(report_true) from tb_report_statistics where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		if (StringUtil.isNotBlank(hospitalName)) {
			paramSql.append(" and hospital_name like '%"+hospitalName+"%' ");
			//sql.addParamValue(equipName);
		}
		if (StringUtil.isNotBlank(startDate) || StringUtil.isNotBlank(endDate) ) {
			paramSql.append(" and date between '"+startDate+"' and '"+endDate+"' ");
		}
		
		paramSql.append(" group by hospital_id,hospital_name ");

		Integer pageStart = start * pageSize;
		Integer pageEnd = start * pageSize + pageSize;
		paramSql.append("limit "+pageStart+","+pageEnd+" ");
		
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		List allNum = getAllNum(hospitalName,startDate,endDate);
		
		for (int i = 0; i < allNum.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) allNum.get(i);
			obj.put("date", StringUtil.getJSONValue(map.get("date")));
			obj.put("hospitalId", StringUtil.getJSONValue(map.get("hospital_id")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			array.add(obj);
		}
		
		JSONArray array_hos = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("hospitalId", StringUtil.getJSONValue(map.get("hospital_id")));
			obj.put("hospitalName", StringUtil.getJSONValue(map.get("hospital_name")));
			obj.put("reportTrue", StringUtil.getJSONValue(map.get("sum(report_true)")));
			obj.put("reportFalse", StringUtil.getJSONValue(map.get("sum(report_false)")));
			array_hos.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Success.RET_10000.getMessage());
		jsonObject.put("totalProperty", getAllHospital(hospitalName,startDate,endDate).size());
		jsonObject.put("result", array);
		jsonObject.put("allHospital", array_hos);
		return jsonObject;
	}
	
	
	//得到对应的统计数据
	private static List getAllNum(String hospitalName,String startDate,String endDate) throws Exception {
		Sql sql = new Sql("select hospital_id,date,sum(report_false),sum(report_true) from tb_report_statistics where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		if (StringUtil.isNotBlank(hospitalName)) {
			paramSql.append(" and hospital_name like '%"+hospitalName+"%' ");
		}
		if (StringUtil.isNotBlank(startDate) || StringUtil.isNotBlank(endDate) ) {
			paramSql.append(" and date between '"+startDate+"' and '"+endDate+"' ");
		}

		paramSql.append(" group by date,hospital_id ");
		
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		return list;
	}
	
	
	private static List getAllHospital(String hospitalName,String startDate,String endDate) throws Exception {
		Sql sql = new Sql("select hospital_id,hospital_name from tb_report_statistics where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		if (StringUtil.isNotBlank(hospitalName)) {
			paramSql.append(" and hospital_name like '%"+hospitalName+"%' ");
			//sql.addParamValue(equipName);
		}
		if (StringUtil.isNotBlank(startDate) || StringUtil.isNotBlank(endDate) ) {
			paramSql.append(" and date between '"+startDate+"' and '"+endDate+"' ");
		}
		
		paramSql.append(" group by hospital_id,hospital_name ");
		
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		return list;
	}
	
	
	
	public static List getStatiByHEidAndDate(String hosId,String tableId,String date) throws Exception {
		Sql sql = new Sql("select * from tb_report_statistics where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		paramSql.append(" and hospital_id = ? ");
		paramSql.append(" and business_id = ? ");
		paramSql.append(" and date = ? ");
		sql.addParamValue(hosId);
		sql.addParamValue(tableId);
		sql.addParamValue(date);
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		return list;
	}
	
	
	public static void addStatistics(Statistics statistics) throws Exception {
		Sql sql = DB.me().createInsertSql(statistics, MyTableEnum.tb_report_statistics);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	
	public static JSONArray getHos(String hosId) throws Exception  {
		
		Sql sql = new Sql("select * from tb_hospital where id = ? ");
		sql.addParamValue(hosId);
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("id", StringUtil.getJSONValue(map.get("id")));
			obj.put("name", StringUtil.getJSONValue(map.get("name")));
			obj.put("url", StringUtil.getJSONValue(map.get("url")));
			array.add(obj);
		}

		return array;
	}
	
}
