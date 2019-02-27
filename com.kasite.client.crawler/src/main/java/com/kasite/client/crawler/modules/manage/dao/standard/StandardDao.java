package com.kasite.client.crawler.modules.manage.dao.standard;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.coreframework.db.DB;
import com.coreframework.db.Sql;
import com.kasite.client.crawler.config.MyDatabaseEnum;
import com.kasite.client.crawler.config.MyTableEnum;
import com.kasite.client.crawler.modules.manage.bean.standard.dbo.Standard;
import com.kasite.client.crawler.modules.utils.DateUtils;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 数据标准集数据库操作类
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年6月27日 上午16:54:00
 */
public class StandardDao {
	
	public static JSONObject getStandardList(Integer start, Integer pageSize,Integer standardType) throws Exception {

		Sql sql_all = new Sql("select * from tb_standard_information where del_flag = 0 and standard_type = ?");
		sql_all.addParamValue(standardType);
		List allNum = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql_all);

		
		Sql sql = new Sql("select * from tb_standard_information where del_flag = 0 @a");
		StringBuffer paramSql = new StringBuffer();


		paramSql.append(" and standard_type = ? ");
		sql.addParamValue(standardType);

		Integer pageStart = start * pageSize;
		Integer pageEnd = pageSize;
		paramSql.append("limit "+pageStart+","+pageEnd+" ");
		sql.addVar("@a", paramSql.toString());
		List list = DB.me().queryForMapList(MyDatabaseEnum.crawler_zk, sql);
		
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject();
			Map map = (Map) list.get(i);
			obj.put("id", StringUtil.getJSONValue(map.get("id")));
			obj.put("name", StringUtil.getJSONValue(map.get("name")));
			obj.put("url", StringUtil.getJSONValue(map.get("url")));
			obj.put("describe", StringUtil.getJSONValue(map.get("standard_describe")));
			obj.put("version", StringUtil.getJSONValue(map.get("version")));
			obj.put("currently", StringUtil.getJSONValue(map.get("currently")));
			array.add(obj);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Code", RetCode.Success.RET_10000.getCode());
		jsonObject.put("Message", RetCode.Success.RET_10000.getMessage());
		jsonObject.put("totalProperty", allNum.size());
		jsonObject.put("result", array);
		jsonObject.put("allHospital", list);
		return jsonObject;
	}

	
	public static void delStandard(Standard vo) {
		String id = vo.getId();
		vo.setId(null);
		try {
			Sql sql = DB.me().createUpdateSql(vo, MyTableEnum.tb_standard_information, "id = ? ");
			sql.addParamValue(id);
			DB.me().update(MyDatabaseEnum.crawler_zk, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String addStandard(Standard vo) {
		Standard standard = new Standard();
		standard.setName(vo.getName());
		standard.setStandard_describe(vo.getVersion());

		try {
			Sql sql = DB.me().createSelect(standard, MyTableEnum.tb_standard_information);
			standard = DB.me().queryForBean(MyDatabaseEnum.crawler_zk, sql, Standard.class);
			if (standard == null) {
				Sql sql_add = DB.me().createInsertSql(vo, MyTableEnum.tb_standard_information);
				DB.me().insert(MyDatabaseEnum.crawler_zk, sql_add);
			}else {
				return "该标准集版本已经存在";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "成功";
	}
	
	

	public static Standard getStandardById(String id) throws SQLException {
		Standard standard = new Standard();
		standard.setId(id);
		Sql sql = DB.me().createSelect(standard, MyTableEnum.tb_standard_information);
		return DB.me().queryForBean(MyDatabaseEnum.crawler_zk, sql, Standard.class);
	}


	public static List<Standard> getStanByCurrently() throws SQLException {
		Standard standard = new Standard();
		standard.setDel_flag("0");
		standard.setCurrently("1");
		Sql sql = DB.me().createSelect(standard, MyTableEnum.tb_standard_information);
		return DB.me().queryForBeanList(MyDatabaseEnum.crawler_zk, sql, Standard.class);
	}
	
	//修改标准集的发布
	public static void updateCurrently(Standard vo) {
		String id = vo.getId();
		vo.setId(null);
		
		String type = vo.getStandard_type();
		vo.setStandard_type(null);
		try {
			vo.setCurrently("0");
			Sql sql = DB.me().createUpdateSql(vo, MyTableEnum.tb_standard_information, " standard_type = ? ");
			sql.addParamValue(type);
			DB.me().update(MyDatabaseEnum.crawler_zk, sql);
			
			vo.setCurrently("1");
			sql = DB.me().createUpdateSql(vo, MyTableEnum.tb_standard_information, " id = ? ");
			sql.addParamValue(id);
			DB.me().update(MyDatabaseEnum.crawler_zk, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
