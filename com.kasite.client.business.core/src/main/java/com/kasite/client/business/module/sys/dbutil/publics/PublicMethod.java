package com.kasite.client.business.module.sys.dbutil.publics;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 备份和还原公共的方法
 * 
 * @author zhaoy
 * @version 1.0
 * 2019-01-28
 */
@SuppressWarnings("serial")
public class PublicMethod {
	
	/**
	 * 执行SQL返回查询结果集
	 * @return ResultSet
	 * @throws Exception
	 */
     public static ResultSet queryResult(Connection conn, String sqlStr) throws Exception {
    	 ResultSet rs = null;
    	 try {
    		 Statement stmt = conn.createStatement();
    		 rs = stmt.executeQuery(sqlStr);
		} catch (Exception e) {
			throw e;
		}
		return rs;
	}
	
     /**
      * 查询所有的表或者是表结构
      * @throws Exception
      */
	public static List<String> getAllTableName(Connection conn, String sqlStr) throws Exception{ 
		ResultSet rs = null;
		Statement stmt = null;
		List<String> list =null;
		try {
	    	list =new ArrayList<String>();
	    	rs = queryResult(conn, sqlStr);            
	    	while(rs.next()){
	    		  list.add(rs.getString(1));
	    	}
		} catch (Exception e) {
			throw e;
		}finally{
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
		}
		return list;
	}

	/**
	 * 生成建表结构
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAllColumns(Connection conn, String sqlStr) throws Exception{
		ResultSet rs = null;
		Statement stmt = null;
		List<String> list = null;
		try {
			list =new ArrayList<String>();
			rs = queryResult(conn, sqlStr);
	    	while(rs.next()){
	    		list.add(rs.getString(2));
	    	}
		} catch (Exception e) {
			throw e;
		}finally{
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
		}
		return list;
	}
	
		
	/**
	 * 查询表的列名，以及当前列对应的类型
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public static List<TableColumn> getDescribe(Connection conn, String sqlStr) throws Exception{
		ResultSet rs = null;
		Statement stmt = null;
		List<TableColumn> list = null;
		try {
			list =new ArrayList<TableColumn>();
			rs = PublicMethod.queryResult(conn, sqlStr);
	    	while(rs.next()){
	    		TableColumn dbColumns =new TableColumn();
	    		dbColumns.setColumnsFiled(rs.getString(1));	
	    		dbColumns.setColumnsType(rs.getString(2));
	    		dbColumns.setColumnsNull(rs.getString(3));
	    		dbColumns.setColumnsKey(rs.getString(4));
	    		dbColumns.setColumnsDefault(rs.getString(5));
	    		dbColumns.setColumnsExtra(rs.getString(6));
	    		list.add(dbColumns);
	    	}
		} catch (Exception e) {
			throw e;
		} finally{
			if(stmt!=null){
				stmt.close();
			}
			if(rs!=null){
				rs.close();
			}
		}	
		return list;
	}
	
	/**
	 * 读取指定路径的sql脚本
	 * @param filePath
	 * @return List<String>
	 * @throws Exception
	 */
	public static List<String> loadSqlScript(String filePath) throws Exception {
		List<String> sqlList = null;
		InputStreamReader inReader= null;
		StringBuffer sqlStr =null; 
		try {
			sqlList=new ArrayList<String>();
			sqlStr =new StringBuffer();
			inReader =new InputStreamReader(new FileInputStream(filePath),"utf8");
			char[] buff = new char[1024];
			int byteRead = 0;
	        while ((byteRead = inReader.read(buff)) != -1) {
	            sqlStr.append(new String(buff, 0, byteRead));
	         }
	        //Windows下换行是\r\n, Linux 下是 \n
            String[] sqlStrArr = sqlStr.toString().split("(;\\s*\\r\\n)|(;\\s*\\n)");
            for (String str : sqlStrArr) {
            	String sql = str.replaceAll("--.*", "").trim();
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
			}
		} catch (Exception ex) {
			throw ex;
		}
		return sqlList;
	}
	
	/**
	 * 去掉字符串中的换行
	 * @return
	 * @throws Exception
	 */
    public String trim(String obj) throws Exception{
    	Matcher matcher = null;
    	Pattern pattern =null;
    	try {
    		 pattern = Pattern.compile("\\s*\n");
    		 matcher= pattern.matcher(obj.toString());
		} catch (Exception e) {
			throw e;
		}
		return matcher.replaceAll("");
    }
}
