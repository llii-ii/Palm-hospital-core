//package com.kasite.core.common.dao;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Type;
//import java.sql.CallableStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.JdbcConnection;
//import com.coreframework.db.Sql;
//import com.coreframework.util.ReflectionUtils;
//import com.kasite.core.common.config.KasiteConfig;
//
//
///**
// * 操作数据库
// * @author daiys
// * @date 2014-11-24
// */
//public class Dao {
//	/**
//	 * Mysql
//	 * @throws SQLException
//	 */
//	public static void testMysqlServer() throws SQLException {
//		Sql sql =  new Sql("Select curdate()");
//		Object obj = DB.me().queryForObject(MyDatabaseEnum.hos, sql);
//		KasiteConfig.print("Mysql test result : "+obj.toString());
//	}
//	
//	/**
//	 * Sqlserver
//	 * @throws SQLException
//	 */
//	public static void testSqlServer() throws SQLException {
//		Sql sql =  new Sql("Select getdate()");
//		Object obj = DB.me().queryForObject(MyDatabaseEnum.hos, sql);
//		KasiteConfig.print(obj.toString());
//	}
//	/**
//	 * Oracle
//	 * @throws SQLException
//	 */
//	public static void testOracle() throws SQLException {
//		Sql sql =  new Sql("select sysdate value from dual");
//		Object obj = DB.me().queryForObject(MyDatabaseEnum.hos, sql);
//		KasiteConfig.print(obj.toString());
//	}
//	public static void loadCfgInfo() throws SQLException {
//		
//		
//	}
//	/**
//	 * 调用oracle存储过程查询数据（最后一个参数必须为oracle sys_refcursor类型的返回值）
//	 * @param <T>
//	 * @param sql
//	 * @param values
//	 * @param con
//	 * @param cls
//	 * @return
//	 * @throws SQLException
//	 */
//	public static <T> List<T>  execOraPro(String sql,Object[] values,JdbcConnection con,Class<T> cls) throws SQLException{
//		List<T> list = new ArrayList<T>();
//		try {
//			int col = 0;
//			CallableStatement cstmt = con.getConn().prepareCall(sql);
//			if(values!=null){
//				for (int i=0;i<values.length;i++) {
//					if(i==values.length-1){
//						col = i+1;
//						cstmt.registerOutParameter(i+1, Integer.parseInt(values[i].toString()));
//					}else{
//						cstmt.setObject(i+1,values[i]);
//					}
//				}
//			}
//			cstmt.execute();
//			ResultSet rs = (ResultSet) cstmt.getObject(col);
//			ResultSetMetaData md = rs.getMetaData(); 
//			int rsCount = md.getColumnCount(); 
//			while(rs.next()){
//				if(cls!=null){
//					Field[] field = cls.getDeclaredFields();
//					T t = ReflectionUtils.newInstance(cls);
//					for(int i=1;i<=rsCount;i++){
//						for (Field ff : field) {
//							Type  type = ff.getGenericType();
//							String keyName = ff.getName();
//							if(keyName.equalsIgnoreCase(md.getColumnName(i))){
//								Object obj = getValue(rs, i, type);
//								ReflectionUtils.setFieldValue(t, keyName, obj);
//								break;
//							}
//						}
//					}
//					list.add(t);
//				}
//			}
//			cstmt.close();
//		}catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		}finally{
//			if(con!=null){
//				try {
//					con.commit();
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return list;
//	}
//	/**
//	 * 原生查询
//	 * @param <T>
//	 * @param sql
//	 * @param values
//	 * @param con
//	 * @param cls
//	 * @return
//	 * @throws SQLException
//	 */
//	public static <T> List<T> execSql(String sql,Object[] values,JdbcConnection con,Class<T> cls) throws SQLException{
//		List<T> list = new ArrayList<T>();
//		try {
//			CallableStatement cstmt = con.getConn().prepareCall(sql);
//			if(values!=null){
//				for (int i=0;i<values.length;i++) {
//					cstmt.setObject(i+1,values[i]);
//				}
//			}
//			ResultSet rs = cstmt.executeQuery();
//			ResultSetMetaData md = rs.getMetaData(); 
//			int rsCount = md.getColumnCount(); 
//			while(rs.next()){
//				if(cls!=null){
//					Field[] field = cls.getDeclaredFields();
//					T t = ReflectionUtils.newInstance(cls);
//					for(int i=1;i<=rsCount;i++){
//						for (Field ff : field) {
//							Type  type = ff.getGenericType();
//							String keyName = ff.getName();
//							if(keyName.equalsIgnoreCase(md.getColumnName(i))){
//								Object obj = getValue(rs, i, type);
//								ReflectionUtils.setFieldValue(t, keyName, obj);
//								break;
//							}
//						}
//					}
//					list.add(t);
//				}
//			}
//			cstmt.close();
//		}catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		}finally{
//			if(con!=null){
//				try {
//					con.commit();
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return list;
//	}
//	public static int execSqlForInt(String sql,Object[] values,JdbcConnection con) throws SQLException{
//		int count = 0;
//		try {
//			CallableStatement cstmt = con.getConn().prepareCall(sql);
//			if(values!=null){
//				for (int i=0;i<values.length;i++) {
//					cstmt.setObject(i+1,values[i]);
//				}
//			}
//			ResultSet rs = cstmt.executeQuery();
//			if(rs.next()){
//				count = rs.getInt(1);
//			}
//			cstmt.close();
//		}catch (SQLException e) {
//			e.printStackTrace();
//			throw e;
//		}finally{
//			if(con!=null){
//				try {
//					con.commit();
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return count;
//	}
//	/**
//	 * 根据传入bean属性的类型获取对应查询到的ResultSet中的值
//	 * @param rs
//	 * @param index
//	 * @param propType
//	 * @return
//	 * @throws SQLException
//	 */
//	private static Object getValue(ResultSet rs,int index,Type propType) throws SQLException{
//		if (propType.equals(String.class)) {
//            return rs.getString(index);
//        } else if (
//            propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
//            return Integer.valueOf(rs.getInt(index));
//
//        } else if (
//            propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
//            return Boolean.valueOf(rs.getBoolean(index));
//
//        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
//            return Long.valueOf(rs.getLong(index));
//
//        } else if (
//            propType.equals(Double.TYPE) || propType.equals(Double.class)) {
//            return Double.valueOf(rs.getDouble(index));
//
//        } else if (
//            propType.equals(Float.TYPE) || propType.equals(Float.class)) {
//            return Float.valueOf(rs.getFloat(index));
//
//        } else if (
//            propType.equals(Short.TYPE) || propType.equals(Short.class)) {
//            return Short.valueOf(rs.getShort(index));
//
//        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
//            return Byte.valueOf(rs.getByte(index));
//
//        } else if (propType.equals(Timestamp.class)) {
//            return rs.getTimestamp(index);
//
//        } else {
//            return rs.getObject(index);
//        }
//	}
//	
//
//	
//}