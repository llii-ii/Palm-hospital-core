package com.kasite.core.common.db.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.core.common.config.DBType;
import com.kasite.core.common.config.datasource.MasterDataSourceConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.util.R;
import com.kasite.core.common.xss.SQLFilter;

/**
 * 
 * @className: SysUserController
 * @author: lcz
 * @date: 2018年8月28日 下午8:13:29
 */
@RequestMapping("/dboper")
@RestController
public class DBOperContrller extends AbstractController{

	@Autowired
	private MasterDataSourceConfig config;
	/**默认查询次数*/
	public static int QueryTimes=0;
	public static long Times;
	static {
		QueryTimes = 50;
		Times=System.currentTimeMillis();
	}
	private static boolean caseTimes() {
		QueryTimes = QueryTimes-1;
		if(QueryTimes > 0 ) {
			return true;
		}
		long t = System.currentTimeMillis();
		if( t - Times > 86400000) {
			QueryTimes = 50;
			return true;
		}else {
			return false;
		}
	}
	@Autowired
	SqlSessionFactory masterSqlSessionFactory;
	
	
	@PostMapping("/db/queryTableInfo.do")
	@RequiresPermissions("dboper:db:queryTableInfo")
	public R selectTablesInfo(String tableName,HttpServletRequest request) {
		if(caseTimes()) {
			SqlSession session = null;
			List<Object> list = null;
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				String sqlStr = "";
				DBType type = config.getDbType();
				switch (type) {
				case mysql:
					sqlStr = "select column_name,column_comment from information_schema.columns where table_schema = '"+ config .getDatabaseName() +"' "
							+ "and table_name='"+ tableName +"'";
					break;
				default:
					break;
				}
				SQLFilter.sqlInject(sqlStr);
				StringBuffer sql = new StringBuffer(sqlStr);
				session = masterSqlSessionFactory.openSession(); 
				conn = session.getConnection();
				ps = conn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				list = convertList(rs);
			}catch (Exception e) {
				e.printStackTrace();
				return R.error(e);
			}finally {
				if(null != ps)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != conn)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != session) session.close();
			}
			return R.ok(list);
		}
		return R.error("超过调用次数无法调用，请联系管理员");
	}
	
	
	
	@PostMapping("/db/queryTables.do")
	@RequiresPermissions("dboper:db:queryTables")
	public R selectTables(HttpServletRequest request) {
		if(caseTimes()) {
			SqlSession session = null;
			List<Object> list = null;
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				String sqlStr = "";
				DBType type = config.getDbType();
				switch (type) {
				case mysql:
					sqlStr = "select DISTINCT table_name from information_schema.columns where table_schema = '"+ config .getDatabaseName() +"'";
					break;
				default:
					break;
				}
				SQLFilter.sqlInject(sqlStr);
				StringBuffer sql = new StringBuffer(sqlStr);
				session = masterSqlSessionFactory.openSession(); 
				conn = session.getConnection();
				ps = conn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				list = convertList(rs);
			}catch (Exception e) {
				e.printStackTrace();
				return R.error(e);
			}finally {
				if(null != ps)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != conn)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != session) session.close();
			}
			return R.ok(list);
		}
		return R.error("超过调用次数无法调用，请联系管理员");
	}
	
	
	@PostMapping("/db/query.do")
	@RequiresPermissions("dboper:db:query")
	public R select(int pageNo,int pageSize,HttpServletRequest request) {
		if(caseTimes()) {
			SqlSession session = null;
			List<Object> list = null;
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				String sqlStr = request.getParameter("sqlStr");
				SQLFilter.sqlInject(sqlStr);
				StringBuffer sql = new StringBuffer(sqlStr);
				sql.append(" limit ").append(pageNo).append(",").append(pageSize);
				session = masterSqlSessionFactory.openSession(); 
				conn = session.getConnection();
				ps = conn.prepareStatement(sql.toString());
				ResultSet rs = ps.executeQuery();
				list = convertList(rs);
			}catch (Exception e) {
				e.printStackTrace();
				return R.error(e);
			}finally {
				if(null != ps)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != conn)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if(null != session) session.close();
			}
			return R.ok(list);
		}
		return R.error("超过调用次数无法调用，请联系管理员");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<Object> convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
	}
}
