package com.kasite.client.business.module.sys.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kasite.client.business.module.backstage.UploadFileController;
import com.kasite.client.business.module.sys.dbutil.publics.AppendMessage;
import com.kasite.client.business.module.sys.dbutil.publics.PublicMethod;
import com.kasite.client.business.module.sys.dbutil.publics.TableColumn;
import com.kasite.core.common.annotation.SysLog;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.controller.AbstractController;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.wxmsg.Zipper;
import com.kasite.core.httpclient.http.StringUtils;
 
/**
 * @Author: zhaoy
 * 数据库备份与还原
 * 
 * @Date: 2019/01/03 15:56
 */

@RequestMapping("/sysdb")
@RestController
public class DbOperateController extends AbstractController {
	
	@Autowired
	SqlSessionFactory masterSqlSessionFactory;
	
	@PostMapping("/downloadData1.do")
	@RequiresPermissions("sysdb:db:download")
	@SysLog(value="数据库备份")
	public R downloadData1(HttpServletRequest request, HttpServletResponse response) {
		String hostIP = request.getParameter("hostIP");
		String configPath = request.getParameter("configFile");
		String tableName = request.getParameter("tableName");
		String databaseName = request.getParameter("databaseName");
		
		try {
			String backName = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".sql";
			String filePath = this.dbBackUp(hostIP, databaseName, backName, configPath, tableName);
			if(filePath == null) {
				return R.error();
			}
			return R.ok().put("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.error();
	}
	
    /**
     * 备份数据库db
     * 
     * @param root
     * @param pwd
     * @param dbName
     * @param backPath
     * @param backName
     */
    private String dbBackUp(String hostIP, String dbName, String sqlFile, String configFliePath, String tableName) throws Exception {
        File fileSql = new File(sqlFile);
        //创建备份sql文件
        if (!fileSql.exists()){
            fileSql.createNewFile();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" --defaults-extra-file="+configFliePath);
        sb.append(" " + dbName);
        if(StringUtils.isNotBlank(tableName) && !"*".equals(tableName)) {
        	sb.append(" " + tableName);
        }
        sb.append(" >");
        String filePath = KasiteConfig.localConfigPath() + File.separator + UploadFileController.fileDir + "/dbReload";
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        sb.append(filePath + File.separator + sqlFile);
        sb.append(" --skip-extended-insert ");
        System.out.println("cmd命令为："+sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("开始备份：" + dbName);
        Process process = runtime.exec("cmd /c"+sb.toString());
        process.getOutputStream().close(); 
        int exitValue = process.waitFor();  
        if(exitValue == 0) {
        	File file = new File(filePath + File.separator + sqlFile);
            Zipper.zipFile(file, filePath + File.separator + dbName + ".zip", null);
            System.out.println("备份SQL完成：" + dbName);
            return UploadFileController.fileDir + "/dbReload/" + dbName + ".zip";
        }
        return null;
    }
    
    @PostMapping("/downloadData.do")
	@RequiresPermissions("sysdb:db:download")
	@SysLog(value="数据库备份")
	public R downloadData(HttpServletRequest request, HttpServletResponse response) {
		String hostIP = request.getParameter("hostIP");
		String tableName = request.getParameter("tableName");
		String databaseName = request.getParameter("databaseName");
		
		try {
			String filePath = this.createSqlScript(hostIP, databaseName, tableName);
			return R.ok().put("filePath", filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.error();
	}
 
    /**
     * 恢复数据库
     * 
     * @param root
     * @param pwd
     * @param dbName
     * @param filePath
     * mysql -hlocalhost -uroot -p123456 db < /home/back.sql
     */
    private void dbRestore(String hostIP,String dbName,String filePath){
        StringBuilder sb = new StringBuilder();
        sb.append("mysql");
        sb.append(" -h"+hostIP);
        sb.append(" "+dbName+" <");
        sb.append(filePath);
        System.out.println("cmd命令为："+sb.toString());
        Runtime runtime = Runtime.getRuntime();
        System.out.println("开始还原数据");
        try {
            Process process = runtime.exec("cmd /c"+sb.toString());
            InputStream is = process.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is,"utf8"));
            String line = null;
            while ((line=bf.readLine())!=null){
                System.out.println(line);
            }
            is.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("还原成功！");
    }
	
	/**
	 * 创建mysql备份
	 */
	private String createSqlScript(String ip, String dbName, String tableName) throws Exception{
		Long startTime = System.currentTimeMillis();
		Connection conn = masterSqlSessionFactory.openSession().getConnection();
		File file;
    	PrintWriter pwrite;
    	String rootPath = KasiteConfig.localConfigPath() + File.separator;
    	try {
    		SimpleDateFormat smf = new SimpleDateFormat("yyyy:MM:dd");
    		String folder = dbName + "_" + smf.format(new Date()).replaceAll(" |:", "");
    		String filePath = UploadFileController.fileDir + File.separator + folder;
			file = new File(rootPath + filePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			List<String> tablelists = PublicMethod.getAllTableName(conn,"show tables");
			if(tablelists != null) {
				for (int i=0;i<tablelists.size();i++) {
					String table = tablelists.get(i);
					String headerInfo = "";
					if(i == 0) {
						headerInfo = AppendMessage.headerMessage(ip, dbName);
					}
					// *全部表导出
					if(!"*".equals(tableName) && !tableName.contains(table.toLowerCase())) {
						continue;
					}
					String tempfilePath =  filePath + "/" + table + ".sql";
					File newFile = new File(rootPath + tempfilePath); 
					pwrite = new PrintWriter(new OutputStreamWriter(new FileOutputStream(newFile,false), "UTF8"));
					//写入头部信息
					pwrite.println(headerInfo);
					pwrite.println("SET FOREIGN_KEY_CHECKS=0;" + "\n");
					StringBuilder strBuilder =new StringBuilder();
					strBuilder.append("show create table ")
					          .append(table);
					List<String> list = PublicMethod.getAllColumns(conn, strBuilder.toString());
					for (String line : list) {
						//在建表前加说明
						pwrite.println(AppendMessage.tableHeaderMessage(table));
						//生成建表语句
						pwrite.println("DROP TABLE IF EXISTS " + " `" + table + "`;");
						pwrite.println(line + ";" + "\n");
				    }
					pwrite.println(AppendMessage.insertHeaderMessage());
					//生成insert语句
					List<Vector<Object>> insertList  =  getAllDatas(table.toString());
					for (int k = 0; k < insertList.size(); k++) {
			        	Vector<Object> vector = insertList.get(k);
			        	String tempStr = vector.toString();
			        	tempStr =tempStr.substring(1, tempStr.length()-1);
			        	tempStr ="INSERT INTO " + "`"+ table + "`" + " VALUES(" + tempStr + ");";
			            pwrite.println(tempStr);
					}
					pwrite.flush();
					pwrite.close();
				}
			}
			Zipper.zipFileForAll(file, rootPath + UploadFileController.fileDir + File.separator + dbName + ".zip", null);
			Long endTime = System.currentTimeMillis();
			System.out.println("============》SQL导出完成《==================" + ";共耗时:" + (endTime-startTime));
			return filePath + ".zip";
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("出现错误,创建备份文件失败!");
		}finally {
			if(conn != null) {
				conn.close();
			}
		}
	}
	/**
	 * 还原mysql备份
	 */
	public void executSqlScript(String filePath) throws Exception {
		Connection conn = masterSqlSessionFactory.openSession().getConnection();
		Statement stmt = null;
		List<String> sqlStrList = null;
		try {
			sqlStrList = PublicMethod.loadSqlScript(filePath);
			stmt = conn.createStatement();
			// 禁止自动提交
			conn.setAutoCommit(false);
			for (String sqlStr : sqlStrList) {
				int index = sqlStr.indexOf("INSERT");
				if (-1 == index) {
					stmt.addBatch(sqlStr);
				}
			}
			stmt.executeBatch();
			// INSERT语句跟建表语句分开执行，防止未建表先INSERT
			for (String sqlStr : sqlStrList) {
				int index = sqlStr.indexOf("INSERT");
				if (-1 != index) {
					stmt.executeUpdate(sqlStr);
				}																			
			}
			stmt.executeBatch();
			conn.commit();
		} catch (Exception ex) {	
			conn.rollback();
			throw ex;
		}finally{
			if(conn!=null){
				conn.close();
			}
			if(stmt!=null){
				stmt.close();
			}
		}
	}
	
	/**
	 * 查询生成Insert语句所需的数据
	 * @param tableName
	 * @return List<Vector<Object>>
	 * @throws Exception
	 */
	public List<Vector<Object>> getAllDatas(String tableName) throws Exception {
		Connection conn = masterSqlSessionFactory.openSession().getConnection();
		List<Vector<Object>> list ;
		Vector<Object> vector;
		StringBuilder typeStr = null;
		List<TableColumn> columnList;
		StringBuilder sqlStr;
		ResultSet rs= null;
		StringBuilder columnsStr ;
		try {
			//生成查询语句
			typeStr =new StringBuilder();
			sqlStr =new StringBuilder();
			columnsStr =new StringBuilder().append("describe ").append(tableName);
			columnList = PublicMethod.getDescribe(conn, columnsStr.toString());
			sqlStr.append("SELECT ");
			for (TableColumn bColumn  : columnList) {
				//处理BLOB类型的数据
				String columnsType = bColumn.getColumnsType();
				if("longblob".equals(columnsType)||"blob".equals(columnsType)||"tinyblob".equals(columnsType)||"mediumblob".equals(columnsType)){
				   typeStr.append("hex(" + "`" + bColumn.getColumnsFiled() + "`" + ") as " + "`" + bColumn.getColumnsFiled() + "`" + " ,");
				}else{
				   typeStr.append("`" + bColumn.getColumnsFiled() + "`" + " ,");
				}
			}
			sqlStr.append(typeStr.substring(0,typeStr.length()-1));
			sqlStr.append(" FROM ").append("`"+tableName + "`;");
				
			//查询insert语句所需的数据
			list = new ArrayList<Vector<Object>>();	
			rs = PublicMethod.queryResult(conn, sqlStr.toString());
			while (rs.next()) {
				vector = new Vector<Object>();
				for (TableColumn dbColumn : columnList) {
					String columnsType = dbColumn.getColumnsType();
					String columnsFile = dbColumn.getColumnsFiled();
					if(null == rs.getString(columnsFile)){
						vector.add( rs.getString(columnsFile));
					//处理BIT类型的数据
					}else if("bit".equals(columnsType.substring(0,3))){
						vector.add(Integer.valueOf(rs.getString(columnsFile)).intValue());
					}else if("bit".equals(columnsType.substring(0,3)) && 0 == Integer.valueOf(rs.getString(columnsFile)).intValue()){
						vector.add("\'"+"\'");
					}else if("longblob".equals(columnsType)||"blob".equals(columnsType)||"tinyblob".equals(columnsType)||"mediumblob".equals(columnsType)){
						vector.add("0x"+rs.getString(columnsFile));
					//处理
					}else if("text".equals(columnsType)||"longtext".equals(columnsType)||"tinytext".equals(columnsType)||"mediumtext".equals(columnsType)){				
						String tempStr =  rs.getString(columnsFile);
						tempStr = tempStr.replace("\'", "\\'");
						tempStr = tempStr.replace("\"", "\\\"");
						vector.add("\'" + tempStr +"\'");
					}else{
						vector.add("\'" + rs.getString(columnsFile) + "\'");
					}
				}
				list.add(vector);
			}
		} catch (Exception e) {
			throw e;
		} finally {				
			if(conn!=null){
				conn.close();
			}
			if(rs!=null){
				rs.close();
			}
		}
		return list;
	}
}

