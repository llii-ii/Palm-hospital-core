//package com.yihu.hos.util;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import com.coreframework.db.DB;
//import com.coreframework.db.DatabaseEnum;
//import com.coreframework.db.Sql;
//import com.yihu.hos.bean.SysRoute;
//import com.yihu.hos.exception.AbsHosException;
//import com.yihu.hos.exception.CommonServiceException;
//import com.yihu.hos.service.CommonServiceRetCode;
//
//public class CommonUtilsDao {
//	/**
//	 * 获取系统路由配置
//	 * @param db
//	 * @return
//	 * @throws AbsHosException
//	 */
//	public static List<Map<String,Object>> queryRoute(DatabaseEnum db) throws AbsHosException{
//		try {
//			Sql sql = DB.me().createSelect(new SysRoute(), CommonTableEnum.SYS_ROUTE);
//			return DB.me().queryForMapList( db, sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new CommonServiceException(CommonServiceRetCode.Common.ERROR_ROUTE_NOT,e.getMessage());
//		}
//	}
//	
//	/**
//	 * 获取系统配置信息
//	 * @param config
//	 * @return
//	 * @throws SQLException
//	 */
//	public static List<SysConfig> queryConfig(DatabaseEnum db,SysConfig config) throws SQLException{
//		Sql sql = DB.me().createSelect(config, CommonTableEnum.SYS_CONFIG);
//		return DB.me().queryForBeanList(db, sql, SysConfig.class);
//	}
//}
