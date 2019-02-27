package com.kasite.core.common.log;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.ParseException;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.wxmsg.IDSeed;

public class LogFileUtils {

	private final static String default_guard_LINUX = "/usr/local/Client/Guard";

	private final static String LOGGING = "logs/logging";
	private final static String LOGGED = "logs/loged";
	private final static String TEMP = "logs/temp";
	private final static String ZIP = "logs/zip";
	private final static String LOGINFO = "logs/loginfo";
	public final static String STR_LINE_SPLIT="\r\n";
	public final static byte[] LINE_SPLIT=STR_LINE_SPLIT.getBytes();
	public final static String charset="UTF-8";
	// private final static String LOG="logs/log";
	private static File guardPath;

	public static void move2Logged(File logging) {
		if(logging==null){
			return;
		}
		File dest=new File(getLogedPath(), logging.getName());
		File p=dest.getParentFile();
		if(!p.exists()){
			p.mkdirs();
		}
		if(!logging.renameTo(dest)){
			KasiteConfig.print(logging.getName()+" remove to path(logged) failed!!!");
		}
	}

	public static File getLogedPath() {
		return new File(getGuardPath(), LOGGED);
	}
	
	public static File getTempFileDir() {
		return new File(getGuardPath(), TEMP);
	}
	
	public static File getZipFileDir() {
		try {
			String now = DateOper.getNow("yyyyMMdd");
			StringBuffer sbf = new StringBuffer(getGuardPath().getAbsolutePath());
			sbf.append(File.separator).append(ZIP).append(File.separator)
			.append(now.substring(0, 4)).append(File.separator)
			.append(now.substring(0,6)).append(File.separator)
			.append(now);
			File f = new File(sbf.toString());
			if(!f.exists()) {
				f.mkdirs();
			}
			return f;
		} catch (ParseException e) {
			e.printStackTrace();
			return new File(getGuardPath(), ZIP);
		}
	}
	
	public static File getLogInfoPath(String sessionKey, String clientId, String sign) {
		try {
			String now = DateOper.getNow("yyyyMMddHH");
			StringBuffer sbf = new StringBuffer(LOGINFO);
			sbf.append(File.separator).append(now).append(File.separator).append(clientId)
			.append(File.separator).append(sign);
			File f = new File(getGuardPath(), sbf.toString()); 
			if(!f.exists() && !f.isDirectory()) {
				f.mkdirs();
			}
			sbf.append(File.separator).append(sessionKey).append(".log");
			return new File(getGuardPath(), sbf.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取日志文件的写入目录
	 */
	public static File getLogingPath() {
		return new File(getGuardPath(), LOGGING);
	}
	
	private static File getGuardPath(){
		String logsDir = KasiteConfig.getLogPath();
		if(null != logsDir && logsDir.length() > 2) {
			File f = new File(logsDir);
			if(!f.exists() || !f.isDirectory()) {
				f.mkdirs();
			}
			guardPath = f;
			return new File(logsDir);
		}
		
		if (guardPath != null) {
			return guardPath;
		}
		guardPath=getDefaultLoginPath();
		KasiteConfig.print("guardPath:"+guardPath.getAbsolutePath());
		return guardPath;
	}


	/*
	 * window默认放在d盘，如果没有d盘,就放在第一个盘里
	 */
	private static File getDefaultLoginPath() {
		String path=System.getProperty("path.guard");
		if(path!=null&&path.length()>2){
			return new File(path);
		}
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			File f=new File("D:");
			if(f.exists()&&f.getFreeSpace()>2000){
				return new File("D:\\Client");
			}
			return new File("C:\\Client");
		}
		
		return new File(default_guard_LINUX);
	}

	/**
	 * 产生一个唯一的文件名，并且以.timestamp结尾
	 * 
	 * @return
	 */
	static String buildLogFileName() {
		return  getUUID();
	}

	private static String PROCNAME = null;
	private static volatile int failed = 0;

	public static String getUUID() {
		if (PROCNAME == null||PROCNAME.length()==0) {
			try {
				PROCNAME = ManagementFactory.getRuntimeMXBean().getName()
						.replace("@", "_")+"_";
			} catch (Exception e) {
				failed++;
				if (failed > 20 && PROCNAME == null) {
					PROCNAME = "";
				}
			}
		}
		return PROCNAME+IDSeed.next();
	}
}
