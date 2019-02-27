package com.kasite.core.common.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coreframework.util.DateOper;
import com.coreframework.util.FileOper;
import com.kasite.core.common.annotation.AuthIgnore;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.sys.oauth.LocalOAuthUtil;
import com.kasite.core.common.sys.runcmd.RunCmdCallable;
import com.kasite.core.common.sys.verification.VerificationBuser;
import com.kasite.core.common.util.DesUtil;
import com.kasite.core.common.util.JdbcUtils;
import com.kasite.core.common.util.R;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.common.util.rsa.KasiteRSAUtil;
import com.kasite.core.common.util.wxmsg.Zipper;

@RestController
@RequestMapping("/systemApi")
public class SysUpdateController{
	public final static String fileDir = "update_upload";
	private final static String charset = "UTF-8";
	private final static String opt_delete = "delete";
	private final static String opt_insert = "insert";
	private final static String opt_update = "update";
	
	@AuthIgnore
	@GetMapping("/ping.do")
	public void ping(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	   response.getOutputStream().print("success");
	}

	@AuthIgnore
	@GetMapping("/sysConfig.do")
	R sysConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		return R.ok(json);
	}
	@AuthIgnore
	@GetMapping("/syncConfig.do")
	R syncConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回当前系统配置信息
		// 从云端同步配置信息
		boolean isConn = KasiteConfig.isConnectionKastieCenter();
		if(isConn) {
			VerificationBuser.create().init();
			LocalOAuthUtil.getInstall().init();
		}
		// 返回当前系统配置信息
		JSONObject json = new JSONObject();
		parseJSON(json,false);
		return R.ok(json);
	}
	
	public static String getDiskInfo() {
		StringBuffer sb = new StringBuffer();
		File[] roots = File.listRoots();// 获取磁盘分区列表
		for (File file : roots) {
			long totalSpace = file.getTotalSpace();
//			long freeSpace = file.getFreeSpace();
			long usableSpace = file.getUsableSpace();
			if (totalSpace > 0) {
				sb.append(file.getPath() + ": (总计：");
				sb.append(Math.round(((double) totalSpace / (1024 * 1024 * 1024)) * 100 / 100.0) + "GB  ");
				if (Math.round((((double) usableSpace / (1024 * 1024 * 1024)) * 100) / 100.0) <= 1) {
					sb.append("剩余：" + Math.round((((double) usableSpace / (1024 * 1024)) * 100) / 100.0) + "MB)");
				} else {
					sb.append("剩余：" + Math.round((((double) usableSpace / (1024 * 1024 * 1024)) * 100) / 100.0)
							+ "GB)");
				}
			}
		}
		return sb.toString();
	}


	public static String getDiskFileList() {
		StringBuffer sb = new StringBuffer();
		String[] fileList = null;
		File[] roots = File.listRoots();// 获取硬盘分区列表；
		for (File file : roots) {
			long totalSpace = file.getTotalSpace();
			fileList = file.list();
			if (totalSpace > 0) {
				sb.append(file.getPath() + "下目录和文件：");
				for (int i = 0; i < fileList.length; i++) {
					sb.append(fileList[i] + "/n");
				}
			}
		}
		return sb.toString();
	}
	
	private void parseJSON(JSONObject json,boolean isAll) throws Exception {
		json.put("channelTypes", KasiteConfig.getPayChannelTypes());
		json.put("channelInfos", KasiteConfig.getAllClientConfig());
		json.put("kasiteConfig", KasiteConfig.print(isAll));
		json.put("system.DiskInfo", getDiskInfo());
		json.put("nowTime", DateOper.getNow("yyyy-MM-dd HH:mm:sss"));
	}
	
	private JSONObject getRequestParam(String reqParam) throws Exception {
		reqParam = reqParam.replace(" ","+");
		String decryContent = KasiteRSAUtil.rsaDecrypt(reqParam, KasiteConfig.getUpdatePrivateKey(), "utf-8");
		JSONObject json = JSONObject.parseObject(decryContent);
		return json;
	}
	
	public static String task(RunCmdCallable target,ExecutorService exec, int timeout) {  
        Future<String> future = exec.submit(target);  
        String taskResult = null;  
        try {  
            // 等待计算结果，最长等待timeout秒，timeout秒后中止任务  
            taskResult = future.get(timeout, TimeUnit.SECONDS);  
        } catch (InterruptedException e) {  
        	e.printStackTrace();
        	taskResult = "主线程在等待计算结果时被中断！";  
        } catch (ExecutionException e) {  
        	e.printStackTrace();
        		taskResult = "主线程等待计算结果，但计算抛出异常！";  
        } catch (TimeoutException e) {  
        	e.printStackTrace();
        		taskResult = "主线程等待计算结果超时，因此中断任务线程！";  
            exec.shutdownNow();  
        }
        return taskResult;
    }  
	
	/*
	 * 获取指定文件目录的文件列表
	 */
	@AuthIgnore
	@PostMapping("/excuteCmd.do")
	R excuteCmd(String reqParam) throws Exception {
		R r = R.ok();
		JSONObject json = getRequestParam(reqParam);
		String cmd = json.getString("cmd");
		String pwd = json.getString("pwd");
		String path = json.getString("path");
		Integer timeOut = json.getInteger("timeOut");
		if(null == timeOut || timeOut < 15) {
			timeOut = 8;
		}
		RunCmdCallable target = new RunCmdCallable(cmd, path);
		ExecutorService exec = Executors.newCachedThreadPool(); 
		try {
			String result = task(target,exec, timeOut); // 任务成功结束后等待计算结果，不需要等到15秒  
	        String retVal = DesUtil.encrypt(result, "UTF-8",pwd);
	        r.put("result", retVal);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			exec.shutdown(); 
		}
		return r;
	}
	
	/*
	 * 读取制定文件的最后指定行
	 */
	@AuthIgnore
	@PostMapping("/read.do")
	R read(String reqParam) throws Exception {
		R r = R.ok();
		JSONObject json = getRequestParam(reqParam);
		String pwd = json.getString("pwd");
		String path = json.getString("path");
		String charsetS = json.getString("charset");
		if(StringUtil.isBlank(charsetS)) {
			charsetS = charset;
		}
		Integer line = json.getInteger("line");
		if(null == line || line<=0) {
			line = 10;
		}
        String retVal = DesUtil.encrypt(readFileFromEnd(path, line, charsetS).toJSONString(), charset, pwd);
        r.put("result", retVal);
		return r;
	}
	
	/**
	    * 从文件末尾开始读取文件，并逐行打印
	    * @param filename  file path
	    * @param charset character
	    */
	public static JSONArray readFileFromEnd(String filename,int lines, String charset) {
	    RandomAccessFile rf = null;
	    JSONArray sbf = new JSONArray();
	    try {
	        rf = new RandomAccessFile(filename, "r");
	        long fileLength = rf.length();
	        long start = rf.getFilePointer();// 返回此文件中的当前偏移量
	        long readIndex = start + fileLength -1;
	        String line;
	        rf.seek(readIndex);// 设置偏移量为文件末尾
	        int c = -1;
	        int readLineSize = 0;
	        while (readIndex > start && readLineSize < lines) {
	            c = rf.read();
	            String readText = null;
	            if (c == '\n' || c == '\r') {
	                line = rf.readLine();
	                if (line != null) {
	                	readLineSize++;
	                    readText = new String(line.getBytes("ISO-8859-1"), charset);
	                } else {
	                	sbf.add("read index:"+ readIndex+" line : " + line);
	                }
	                readIndex--;
	            }
	            readIndex--;
	            rf.seek(readIndex);
	            if (readIndex == 0) {// 当文件指针退至文件开始处，输出第一行
	            	readLineSize++;
	                readText = new String(rf.readLine().getBytes("ISO-8859-1"), charset);
	            }
	            if (readText != null) {
	            	sbf.add(readText);
	            }
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rf != null)
	                rf.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sbf;
	}
	
	/*
	 *下载文件
	 */
	@AuthIgnore
	@PostMapping("/downLoad.do")
	void downLoad(String reqParam,HttpServletResponse response) throws Exception {
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		byte[] bytes = readByteArrayFromResource(path);
		if (bytes != null) {
			response.getOutputStream().write(bytes);
		}
		return;
	}
	
	
   public static byte[] readByteArrayFromResource(String resource) throws IOException {
        InputStream in = null;
        try {
        	File f = new File(resource);
        	if(!f.exists()) {
        		return null;
        	}
        	if(!f.isFile()) {
        		return null;
        	}
            in = new FileInputStream(f);
            return readByteArray(in);
        } finally {
            JdbcUtils.close(in);
        }
    }
    public final static int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static long copy(InputStream input, OutputStream output) throws IOException {
        final int EOF = -1;

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    public static byte[] readByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }
	
	/*
	 * 获取指定文件目录的文件列表
	 */
    @AuthIgnore
	@PostMapping("/filelist.do")
	R filelist(String reqParam) throws Exception {
		R r = R.ok();
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		String pwd = json.getString("pwd");
		File f = new File(path);
		File[] fs = f.listFiles();
		JSONArray result = new JSONArray();
		for (File file : fs) {
			JSONObject fileJson = new JSONObject();
			fileJson.put("name", file.getName());
			fileJson.put("path", file.getAbsolutePath());
			fileJson.put("isFile", file.isFile());
			fileJson.put("isDirectory", file.isDirectory());
			fileJson.put("lastModified", DateOper.formatDate(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
			result.add(fileJson);
		}
		r.put("result", DesUtil.encrypt(result.toJSONString(), "UTF-8",pwd));
		return r;
	}
	//查询更新日志
    @AuthIgnore
	@PostMapping("/updatelog.do")
	R updateLog(String reqParam) throws Exception {
		R r = R.ok();
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		String pwd = json.getString("pwd");
		File f = new File(path);
		File[] fs = f.listFiles();
		JSONArray result = new JSONArray();
		for (File file : fs) {
			JSONObject fileJson = new JSONObject();
			fileJson.put("name", file.getName());
			fileJson.put("path", file.getAbsolutePath());
			fileJson.put("isFile", file.isFile());
			fileJson.put("isDirectory", file.isDirectory());
			fileJson.put("lastModified", DateOper.formatDate(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
			result.add(fileJson);
		}
		r.put("result", DesUtil.encrypt(result.toJSONString(), "UTF-8",pwd));
		return r;
	}
	//回滚
    @AuthIgnore
	@PostMapping("/rockback.do")
	R rockback(String reqParam) throws Exception {
		R r = R.ok();
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		String pwd = json.getString("pwd");
		File file = new File(path);
		if(file.exists()) {
			String jsonStr = FileUtil.readAsString(file);
			JSONObject result = new JSONObject();
			JSONArray arr = JSONArray.parseArray(jsonStr);
			for (Object o : arr) {
				JSONObject j = (JSONObject) o;
				String opt = j.getString("opt");
				String filePath = j.getString("filePath");
				if(opt_insert.equals(opt)) {
					// 删除 1.更新的文件  2.覆盖旧的文件回去。
					File newFile = new File(filePath);
					if(newFile.exists()) {
						newFile.delete();
						result.put("rockback_delete", filePath);
					}
				}
			}
			for (Object o : arr) {
				JSONObject j = (JSONObject) o;
				String opt = j.getString("opt");
				String filePath = j.getString("filePath");
				String oldFilePath = j.getString("oldFilePath");
				if(opt_delete.equals(opt)) {
					// 删除 1.更新的文件  2.覆盖旧的文件回去。
					FileOper.moveFile(filePath, oldFilePath);
					result.put("rockback_oldFilePath", oldFilePath);
					result.put("rockback_filePath", filePath);
				}
			}
			r.put("result", DesUtil.encrypt(result.toJSONString(), "UTF-8",pwd));
		}
		return r;
	}
	
	private void writeLog(JSONArray jsonArray,String type) throws Exception {
		if(jsonArray.size()>0) {
			StringBuffer logRootDir = getLogPathDir();
			File f2 = new File(logRootDir.toString());
			if(!f2.exists()) {
				f2.mkdirs();
			}
			logRootDir.append(File.separator).append(KasiteConfig.getRandomId()).append("_").append(type).append(".json");
			f2 = new File(logRootDir.toString());
			FileOper.write(logRootDir.toString(), jsonArray.toJSONString(), false);
		}
	}
	@AuthIgnore
	@PostMapping("/update.do")
	R update(String reqParam,@RequestParam("newsFile") MultipartFile newsFile, HttpServletRequest request) throws Exception {
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		String pwd = json.getString("pwd");
		String fileName = json.getString("fileName");
		//先存放临时目录 
		String tempPath = getInsertPathDir(pwd);
		this.uploadFile(newsFile.getBytes(), tempPath, fileName);
		
		File f = new File(tempPath, fileName);
		File[] fs = Zipper.unzip(f, pwd);
		String oldDir =  getDeletePathDir(pwd);
		File oldfile = new File(oldDir);
		if(!oldfile.exists()) {
			oldfile.mkdirs();
		}
//		FileOper.moveFile(file.getAbsolutePath(), tempPath);
		JSONArray jsonArray = new JSONArray();
		for (File file : fs) {
			//需要移动到目录下是否有该文件
			String toPath = path+File.separator+file.getName();
			File toPathFile = new File(toPath);
			//移除文件夹下对应的文件 移到历史记录中
			//1.将要更新到文件夹到内容移动到历史文件夹中
			if(toPathFile.exists()) {
				String deleteFilePath = oldDir + file.getName();
				FileOper.moveFile(toPathFile.getAbsolutePath(), deleteFilePath);
				JSONObject log = getLogJson(deleteFilePath, toPathFile.getAbsolutePath(),opt_delete, pwd);
				jsonArray.add(log);
			}
			//更新新文件到指定目录下
			FileOper.moveFile(file.getAbsolutePath(), toPath);
			JSONObject log = getLogJson(toPath, file.getAbsolutePath(),opt_insert, pwd);
			jsonArray.add(log);
		}
		writeLog(jsonArray,opt_update);
		return R.ok().put("result", DesUtil.encrypt(jsonArray.toJSONString(), "UTF-8",pwd));
	}
	@AuthIgnore
	@PostMapping("/uploadFile.do")
	R upload(String reqParam,@RequestParam("newsFile") MultipartFile newsFile, HttpServletRequest request) throws Exception {
		JSONObject json = getRequestParam(reqParam);
		String path = json.getString("path");
		String pwd = json.getString("pwd");
		String fileName = json.getString("fileName");
		//先存放临时目录 
		String tempPath = getInsertPathDir(pwd);
		this.uploadFile(newsFile.getBytes(), tempPath, fileName);
		File f = new File(tempPath, fileName);
		File[] fs = Zipper.unzip(f, pwd);
		JSONArray jsonArray = new JSONArray();
		for (File file : fs) {
			String toPath = path+File.separator+file.getName();
			FileOper.moveFile(file.getAbsolutePath(), toPath);
			JSONObject log = getLogJson(toPath, file.getAbsolutePath(),opt_delete, pwd);
			jsonArray.add(log);
		}
		writeLog(jsonArray,opt_insert);
		return R.ok();
	}
	
	public void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }
	
	private String getDeletePathDir(String pwd) throws Exception {
		StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
		rootDir.append(File.separator).append("backups_delete").append(File.separator).append(pwd).append(File.separator);
		return rootDir.toString();
	}
	private String getInsertPathDir(String pwd) throws Exception {
		StringBuffer rootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, true, true);
		rootDir.append(File.separator).append("backups_insert").append(File.separator).append(pwd).append(File.separator);
		return rootDir.toString();
	}
	private StringBuffer getLogPathDir() throws Exception {
		StringBuffer logRootDir = KasiteConfig.getLocalConfigPathByName(fileDir, true, false, false);
		logRootDir.append(File.separator).append("log");
		return logRootDir;
	}
	/**
	 * 获取日志消息
	 * @param filePath 备份的文件位置
	 * @param oldFilePath  被删除的文件旧的位置
	 * @param opt 操作方式 删除／更新／新增
	 * @param pwd 操作的压缩文件密钥
	 * @return
	 * @throws Exception
	 */
	private JSONObject getLogJson(String filePath,String oldFilePath,String opt,String pwd) throws Exception {
		JSONObject log = new JSONObject();
		log.put("filePath", filePath);
		log.put("id", pwd);
		log.put("opt", opt);
		log.put("time", DateOper.getNow("yyyy-MM-dd HH:mm:ss"));
		log.put("oldFilePath", oldFilePath);
		return log;
	}
	
	/*
	 * 获取指定文件目录的文件列表
	 */
	@AuthIgnore
	@PostMapping("/delete.do")
	R delete(String reqParam) throws Exception {
		R r = R.ok();
		JSONArray jsonarr = new JSONArray();
		JSONObject json = getRequestParam(reqParam);
		String pathstr = json.getString("path");
		String pwd = json.getString("pwd");
		if(StringUtil.isNotBlank(pathstr)) {
			String[] paths = pathstr.split(",");
			for (String path : paths) {
				File file = new File(path);
				if(file.exists() && file.isFile()) {
					String dir = getDeletePathDir(pwd);
					String tempPath = dir+file.getName();
					File f = new File(dir);
					if(!f.exists()) {
						f.mkdirs();
					}
					FileOper.moveFile(file.getAbsolutePath(), tempPath);
					JSONObject log = getLogJson( tempPath, file.getAbsolutePath(),opt_delete, pwd);
					jsonarr.add(log);
				}
			}
			writeLog(jsonarr,opt_delete);
		}
		
		return r.put("result", DesUtil.encrypt(jsonarr.toJSONString(), "UTF-8",pwd));
	}
}
