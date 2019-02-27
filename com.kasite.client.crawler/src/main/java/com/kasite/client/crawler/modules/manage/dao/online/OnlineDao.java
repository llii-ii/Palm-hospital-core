package com.kasite.client.crawler.modules.manage.dao.online;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.MyTableEnum;
import com.kasite.client.crawler.modules.manage.bean.online.dbo.Online;
import com.kasite.client.crawler.modules.manage.business.online.OnlinetController;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 设备在线情况数据库操作类
 * 
 * @author 無
 * @version V1.0
 * @date 2018年6月21日 上午11:45:29
 */
public class OnlineDao {
	
	public static JSONObject getOnlineList(Online online, Integer start, Integer pageSize) throws Exception {
		Sql sql = new Sql("select * from tb_equipment_online where 1=1 @a");
		Sql sql_all = new Sql("select * from tb_equipment_online where 1=1 @b");
		
		StringBuffer paramSql = new StringBuffer();
		
		//设备名称查询
		if (StringUtil.isNotBlank(online.getName())) {
			paramSql.append(" and name = ? ");
			sql.addParamValue(online.getName());
			sql_all.addParamValue(online.getName());
		}
		
		//状态查询
		if (StringUtil.isNotBlank(online.getState())) {
			paramSql.append(" and state = ? ");
			sql.addParamValue(online.getState());
			sql_all.addParamValue(online.getState());
		}
		
		//市查询
		if (StringUtil.isNotBlank(online.getMunicipal())) {
			paramSql.append(" and municipal = ?  ");
			sql.addParamValue(online.getMunicipal());
			sql_all.addParamValue(online.getMunicipal());
		}
		
		//县区查询
		if (StringUtil.isNotBlank(online.getCounty())) {
			paramSql.append(" and county = ?  ");
			sql.addParamValue(online.getCounty());
			sql_all.addParamValue(online.getCounty());
		}

		//时间查询
		if (StringUtil.isNotBlank(online.getStartTime()) || StringUtil.isNotBlank(online.getEndTime())) {
			paramSql.append(" and insert_date BETWEEN ? AND ? ");
			sql.addParamValue(online.getStartTime());
			sql.addParamValue(online.getEndTime());
			sql_all.addParamValue(online.getStartTime());
			sql_all.addParamValue(online.getEndTime());
		}
		
		//获得数据总条数
		sql_all.addVar("@b", paramSql.toString());
		List allList = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql_all);
		
		//分页
		Integer pageStart = start * pageSize;
		Integer pageEnd = pageSize;
		paramSql.append("limit ?,?");
		sql.addParamValue(pageStart);
		sql.addParamValue(pageEnd);
		
		//获得分页后的数据
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("id", StringUtil.getJSONValue(map.get("id")));
			obj.put("name", StringUtil.getJSONValue(map.get("name")));
			obj.put("state", StringUtil.getJSONValue(map.get("state")));
			obj.put("dateTotal", DateUtils.daysBetween((Date)map.get("insert_date"), new Date()));
			int dateOnline = getOnlineDay(StringUtil.getJSONValue(map.get("id")),(Date)map.get("insert_date"));
			obj.put("dateOnline", dateOnline);
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Success.RET_10000.getMessage());
		jsonObject.put("totalProperty",allList.size());//总条数
		jsonObject.put("result", array);//数据
		return jsonObject;
	}
	
	public static JSONObject getAllHos(String county,String name) throws Exception {
		Sql sql = new Sql("select * from tb_equipment_online where 1=1 @a");
		
		StringBuffer paramSql = new StringBuffer();
		//县区查询
		if (StringUtil.isNotBlank(county)) {
			paramSql.append(" and county = ?  ");
			sql.addParamValue(county);
		}
		
		//医院名字
		if (StringUtil.isNotBlank(name)) {
			paramSql.append(" and name = ?  ");
			sql.addParamValue(name);
		}
		
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			//存入map缓存
			if(null == OnlinetController.hosMap || list.size()> OnlinetController.hosMap.size()){
				OnlinetController.hosMap.put(map.get("id").toString(), map);
			}
			obj.put("id", StringUtil.getJSONValue(map.get("id")));
			obj.put("url", StringUtil.getJSONValue(map.get("url")));
			obj.put("name", StringUtil.getJSONValue(map.get("name")));
			obj.put("county", StringUtil.getJSONValue(map.get("county")));
			obj.put("state", StringUtil.getJSONValue(map.get("state")));
			array.add(obj);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Success.RET_10000.getMessage());
		jsonObject.put("result", array);//数据
		return jsonObject;
	}


	public static JSONObject onlineDetailById(String id ,Integer start, Integer pageSize,String startTime, String endTime) throws Exception {
		
		//得到总数
		Sql sql_all = new Sql("select * from tb_break_and_online where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		paramSql.append(" and equipment_id = ? ");
		sql_all.addParamValue(id);
		
		//分页后的数据
		Sql sql = new Sql("select * from tb_break_and_online where 1=1 @b");
		sql.addParamValue(id);
		
		if (StringUtil.isNotBlank(startTime) || StringUtil.isNotBlank(endTime)) {
			paramSql.append(" and online_break_date between ? and ? order by online_break_date desc ");
			sql_all.addParamValue(startTime);
			sql_all.addParamValue(endTime);
			sql.addParamValue(startTime);
			sql.addParamValue(endTime);
		}

		sql_all.addVar("@a", paramSql.toString());
		List allList = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql_all);

	
		Integer pageStart = start * pageSize;
		Integer pageEnd = start * pageSize + pageSize;
		paramSql.append("limit ?,?");
		sql.addParamValue(pageStart);
		sql.addParamValue(pageEnd);
		
		sql.addVar("@b", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("id", StringUtil.getJSONValue(map.get("id")));
			obj.put("state", StringUtil.getJSONValue(map.get("state")));
			obj.put("date", StringUtil.getJSONValue(map.get("online_break_date")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Success.RET_10000.getMessage());
		jsonObject.put("totalProperty", allList.size());
		jsonObject.put("hosName", getOnlineById(id).getName());
		jsonObject.put("result", array);
		return jsonObject;
	}
	
	
	
	public static Online getOnlineById(String id) throws SQLException {
		Online online = new Online();
		online.setId(id);
		Sql sql = DB.me().createSelect(online, MyTableEnum.tb_equipment_online);
		return DB.me().queryForBean(MyDatabaseEnum.crawler_zk, sql, Online.class);
	}
	
	
	public static void addOnline(Online online) throws SQLException {
		Sql sql = DB.me().createInsertSql(online, MyTableEnum.tb_equipment_online);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	
	public static void addOnlineDetail(Online online) throws SQLException {
		Sql sql = DB.me().createInsertSql(online, MyTableEnum.tb_break_and_online);
		DB.me().insert(MyDatabaseEnum.crawler_zk, sql);
	}
	
	public static void update(Online vo) throws SQLException {
		String id = vo.getId();
		vo.setId(null);
		Sql sql = DB.me().createUpdateSql(vo, MyTableEnum.tb_equipment_online, "id = ? ");
		sql.addParamValue(id);
		DB.me().update(MyDatabaseEnum.crawler_zk, sql);
	}
	
	
	//获取在线天数
	private static int getOnlineDay(String id,Date insertDate) throws SQLException, ParseException {
		
		String state = "";
		Date d = null;
		int allDay = 0;
		//获得医院的所有明细
		Sql sql = new Sql("select * from tb_break_and_online where 1=1 @a");
		StringBuffer paramSql = new StringBuffer();
		paramSql.append(" and equipment_id = ? order by online_break_date ");
		sql.addParamValue(id);
		sql.addVar("@a", paramSql.toString());
		List allList = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		if (!allList.isEmpty()) {
			//判断第一条记录状态为0/1
			Map first = (Map) allList.get(0);
			if ("1".equals(StringUtil.getJSONValue(first.get("state")))) {
				int day = DateUtils.daysBetween(insertDate, (Date)first.get("online_break_date"));
				allDay += day;
			}else {
				d = (Date)first.get("online_break_date");
			}
			
			for (int i = 1; i < allList.size(); i++) {
				Map map = (Map) allList.get(i);
				state = StringUtil.getJSONValue(map.get("state"));
				if (state.equals("0")) {
					d = (Date)map.get("online_break_date");
				}else if (state.equals("1")) {
					int day = DateUtils.daysBetween(d, (Date)map.get("online_break_date"));
					allDay += day;
				}	
			}
		
			//判断最后一条记录状态为0/1
			Map last = (Map) allList.get(allList.size()-1);
			if ("0".equals(StringUtil.getJSONValue(last.get("state")))) {
				int day = DateUtils.daysBetween((Date)last.get("online_break_date"), new Date());
				allDay += day;
			}		
		}

		
		return allDay;
	}
}
