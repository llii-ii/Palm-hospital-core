/**
 * 
 */
package com.kasite.core.common.log;

import java.io.File;
import java.io.FileWriter;

import com.kasite.core.common.config.KasiteConfig;

/**
 * @author chenwu
 * 
 */
public class FileLogOper {

	public static String logPath = null;
	static {
		try
		{
			logPath = KasiteConfig.localConfigPath();
			int endIndex = logPath.lastIndexOf(File.separator);
			logPath = logPath.substring(0, endIndex) + File.separator + "logs";
			File file = new File(logPath);
			if (!file.exists()) {
				file.mkdir();
			}
		}
		catch(Exception e)
		{
			KasiteConfig.print(e.getMessage());
		}
	}

	public static void write(String fileName, String str) {
		try {
			//StackTraceElement stack[] = (new Throwable()).getStackTrace();
			FileWriter fw = new FileWriter(logPath + File.separator + fileName,
					true);
//			if(stack!=null&&stack.length>1)
//			{
//				fw.write(stack[1].getClassName()+"\t"+stack[1].getLineNumber()+"\t"+new java.sql.Timestamp(System.currentTimeMillis())+"\t"+str);
//			}
//			else
//			{
			fw.write(new java.sql.Timestamp(System.currentTimeMillis())+"\t"+str);
			//}
			fw.write("\r\n");
			fw.close();
		} catch (Exception e) {
			KasiteConfig.print(e.getMessage());
		}
	}
	public static void write(String fileName, LogInfo info) {
		try {
			
			FileWriter fw = new FileWriter(logPath + File.separator + fileName,
					true);
			fw.write(info.toString());
			fw.write("\r\n");
			fw.close();
		} catch (Exception e) {
			KasiteConfig.print(e.getMessage());
		}
	}
//	public static void del(String fileName) {
//		try {
//			File file = new File(logPath + File.separator + fileName);
//			   if(file.exists())
//			   {
//				  file.delete();
//			   }
//			   
//		} catch (Exception e) {
//			KasiteConfig.print(e.getMessage());
//		}
//	}
}
