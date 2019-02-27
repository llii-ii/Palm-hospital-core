//package com.kasite.client.crawler.modules.api;
//
//import java.sql.SQLException;
//
//import com.alibaba.fastjson.JSONObject;
//import com.coreframework.db.DB;
//import com.coreframework.db.Sql;
//import com.kasite.client.crawler.config.MyDatabaseEnum;
//import com.kasite.core.common.service.IGuardApi;
//import com.yihu.wsgw.api.InterfaceMessage;
//import com.yihu.wsgw.api.WsUtil;
//
//public class GuardApiImpl implements IGuardApi {
//
//	@Override
//	public String query(InterfaceMessage msg) {
//		try {
//			String param = msg.getParam();
//			JSONObject json = JSONObject.parseObject(param);
//			String dbName = json.getString("dbName");
//			String sql = json.getString("sql");
//			int pageIndex = json.getIntValue("pageIndex");
//			int pageSize = json.getIntValue("pageSize");
//			com.common.json.JSONObject ret = DB.me().queryForJson(MyDatabaseEnum.valueOf(dbName), new Sql(sql) ,pageIndex,pageSize);
//			return WsUtil.getRetVal(10000, "查询成功。", ret);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return WsUtil.getRetVal(msg.getOutType(), -14444, e.getMessage());
//		}
//	}
//
//	@Override
//	public String update(InterfaceMessage msg) {
//		try {
//			String param = msg.getParam();
//			JSONObject json = JSONObject.parseObject(param);
//			String dbName = json.getString("dbName");
//			String sql = json.getString("sql");
//			int i = DB.me().update(MyDatabaseEnum.valueOf(dbName),new Sql(sql));
//			return WsUtil.getRetVal(msg.getOutType(), 10000, i+"");
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return WsUtil.getRetVal(msg.getOutType(), -14444, e.getMessage());
//		}
//	}
//
//
//	@Override
//	public String add(InterfaceMessage msg) {
//		try {
//			String param = msg.getParam();
//			JSONObject json = JSONObject.parseObject(param);
//			String dbName = json.getString("dbName");
//			String sql = json.getString("sql");
//			int i = DB.me().insert(MyDatabaseEnum.valueOf(dbName), new Sql(sql));
//			return WsUtil.getRetVal(msg.getOutType(), 10000, i+"");
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return WsUtil.getRetVal(msg.getOutType(), -14444, e.getMessage());
//		}
//	}
//
//	@Override
//	public String ddl(InterfaceMessage msg) {
////		JdbcConnection conn = null;
////		try {
////			
////			String param = msg.getParam();
////			JSONObject json = JSONObject.parseObject(param);
////			String dbName = json.getString("dbName");
////			String sql = json.getString("sql");
////			conn = DB.me().getConnection(MyDatabaseEnum.valueOf(dbName));
////			conn.beginTransaction(8000);
////			PreparedStatement ps = conn.getConn().prepareStatement(sql);
////			
////		} catch (SQLException e) {
////			e.printStackTrace();
////			if(null != conn) {
////				conn.rollbackAndclose();
////			}
////			return WsUtil.getRetVal(msg.getOutType(), -14444, e.getMessage());
////		}finally {
////			if(null != conn) {
////				conn.close();
////			}
////		}
//		return null;
//	}
//
//}
