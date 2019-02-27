//package com.kasite.core.common.util;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import com.coreframework.db.Sql;
//
///**
// * sql工具类
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:40:21
// */
//public class SqlUtil {
//	/**
//	 * 获得in语句的预编译格式.替换sql中的@
//	 * 
//	 * @param paramList
//	 *            in语句中的参数; sqlReplaceStr Sql中的替换字符
//	 * @return
//	 * @throws SQLException
//	 */
//	public static Sql getPreCompliedSqlForIN(List<Object> paramList, String sqlReplaceStr, Sql sql) {
//		StringBuffer sqlSB = new StringBuffer(2048);
//		int size = paramList.size();
//		if (size > 0) {
//			sqlSB.append("?");
//			while (size > 1) {
//				sqlSB.append(",?");
//				size--;
//			}
//			sql.addVar(sqlReplaceStr, sqlSB.toString());
//			for (Object param : paramList) {
//				sql.addParamValue(param);
//			}
//		}
//		return sql;
//	}
//
//	/**
//	 * 获得in语句的预编译格式.替换sql中的@
//	 * 
//	 * @param params
//	 *            in语句中的参数;regex 分割params的字符; sqlReplaceStr Sql中的替换字符
//	 * @return
//	 * @throws SQLException
//	 */
//	public static Sql getPreCompliedSqlForIN(String params, String regex, Sql sql, String sqlReplaceStr) {
//		StringBuffer sqlSB = new StringBuffer(2048);
//		String[] paramArr = params.split(regex);
//		int size = paramArr.length;
//		if (size > 0) {
//			sqlSB.append("?");
//			while (size > 1) {
//				sqlSB.append(",?");
//				size--;
//			}
//			sql.addVar(sqlReplaceStr, sqlSB.toString());
//			for (String param : paramArr) {
//				sql.addParamValue(param);
//			}
//		}
//		return sql;
//	}
//}
