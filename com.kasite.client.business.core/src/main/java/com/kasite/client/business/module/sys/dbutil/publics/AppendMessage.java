package com.kasite.client.business.module.sys.dbutil.publics;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 * 
 * @author zhaoy
 * @date 2019-01-28
 * 
 * @version 1.0
 */
public class AppendMessage {
	
    /**
     * 拼接头部信息
     * @throws Exception
     */
    public static String headerMessage(String ip, String dbName) throws Exception{
    	StringBuilder strBuilder = null;
    	try {
    		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strBuilder = new StringBuilder();
			strBuilder.append("/*" + "\n")
			          .append("Data Transfer" + "\n")
			          .append("Author: sys" + "\n")
//			          .append("QQ: 493000806" + "\n")
			          .append("Source Host: " + ip + "\n")
			          .append("Source Database: " + dbName + "\n")
			          .append("Create Date: " +smf.format(new Date())+ "\n")
			          .append("*/" + "\n");
		} catch (Exception e) {
			throw e;
		}
		return strBuilder.toString();
    }
    
    /**
     * 拼接表之前的信息
     * @throws Exception
     */
    public static String tableHeaderMessage(String tableName) throws Exception{
    	StringBuilder strBuilder = null;
    	try {
			strBuilder = new StringBuilder();
			strBuilder.append("-- ----------------------------" + "\n")
			          .append("-- Create Table " + tableName + "\n")
			          .append("-- ----------------------------");
		} catch (Exception e) {
			throw e;
		}
		return strBuilder.toString();
    }
    
    /**
     * 拼接表之前的信息
     * @throws Exception
     */
    public static String insertHeaderMessage() throws Exception{
    	StringBuilder strBuilder = null;
    	try {
			strBuilder = new StringBuilder();
			strBuilder.append("-- ----------------------------" + "\n")
			          .append("-- Create Datas  " + "\n")
			          .append("-- ----------------------------");
		} catch (Exception e) {
			throw e;
		}
		return strBuilder.toString();
    }
}
