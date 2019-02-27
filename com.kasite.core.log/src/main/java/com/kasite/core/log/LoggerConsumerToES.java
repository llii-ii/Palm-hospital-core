/**
 * 
 */
package com.kasite.core.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.IApplicationStartUp;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.log.LogFileUtils;
import com.kasite.core.common.log.LogInfo;
import com.kasite.core.common.util.wxmsg.IDSeed;
import com.kasite.core.common.util.wxmsg.Zipper;
@Component
public class LoggerConsumerToES implements IApplicationStartUp{
	private static final long DELETE_MILLS=Long.getLong("guard.DELETE_MILLS",1000*3600*24*2);//3天以上的日志文件就可以删除了
	private final static Pattern ln=Pattern.compile("\r\n");
	private static boolean isStart = false;
	private static boolean isStartIng = false;
	@Autowired
	private LogDeal logDeal;
	public LoggerConsumerToES(){
		if(null == logDeal) {
			logDeal = new LogDealImpl();
		}
		
	}
	@Override
	public void init(ContextRefreshedEvent event) {	
		if(!isStart) {
			Thread t=new Thread("LoggerConsumerToES"){
				private boolean stopped=false;
				private Thread thread;
				@Override
				public void run() {
					thread=Thread.currentThread();
					try{
						Runtime.getRuntime().addShutdownHook(new Thread(){
							@Override
							public void run() {
								stopped=true;
								thread.interrupt();
							}
						});
					}catch (Exception e) {
						e.printStackTrace();
					} 
					while(!stopped){
						try{
							boolean customLog = KasiteConfig.getESLog();
							if(!isStartIng && customLog) {
								isStartIng = true;
								KasiteConfig.print("======================== 启动 ES 方式处理日志线程===========================");	
							}
							if(customLog) {
								File[] logs = listLogs();
								if(logs==null||logs.length==0){
									Thread.sleep(2);
									continue;
								}
								for(File log:logs){
									send(log);
									File newFileDir = LogFileUtils.getTempFileDir();
									if(!newFileDir.exists()) {
										newFileDir.mkdirs();
									}
									String newfilepath = newFileDir.getAbsolutePath()+File.separator+log.getName();
									File newFile = new File(newfilepath);
									log.renameTo(newFile);
									log.delete();
								}
								
								File newFileDir = LogFileUtils.getTempFileDir();
								//100个文件压缩一次
								if(null != newFileDir && newFileDir.listFiles().length > 100) {
									//压缩临时文件夹
									String uploadFileDir_zip = LogFileUtils.getZipFileDir().getAbsolutePath();
									String filename = System.currentTimeMillis() + "_" +IDSeed.next();
									Zipper.zipFileForAll(newFileDir, uploadFileDir_zip + File.separator + filename + ".zip",null);
									File[] fs = newFileDir.listFiles();
									for (File file : fs) {
										file.delete();
									}
								}
								
							}else {
								Thread.sleep(10000);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			};
			t.start();
			isStart = true;
		}
	}
	
	private File[] listLogs(){
		File f=LogFileUtils.getLogedPath();
		if(!f.exists()){
			return null;
		}
		File[] files=f.listFiles();
		if (files == null || files.length == 0) {
			return null;
        }
		
		//按时间升序，时间最早的排第一个
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File file, File newFile) {
				if (file.lastModified() < newFile.lastModified()) {
					return -1;
				} else if (file.lastModified() == newFile.lastModified()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		
		if(files.length>5000){
			KasiteConfig.print("loged files:"+files.length);
		}
		return files;
	}
	
	
	/**
	 * true表示处理成功，而且比较闲，还可以再处理
	 */
	private boolean send(File log) throws IOException {
//		int timeout=1000*120;
		if(log.length()>1024*1024*40){
			KasiteConfig.print("log is too big,size="+log.length());
//			timeout=1000*60*10;
			log.delete();
			return true;
		}
		StringBuilder sb=new StringBuilder((int) (log.length()/1024+10));
		InputStreamReader in = null;
		//被其它进程占用，导致打不开文件的，或被删除。这样不会影响下个流程
		try {
			in = new InputStreamReader(new FileInputStream(log),LogFileUtils.charset);
			int len=-1;
			char[] cs=new char[1024];
			while((len=in.read(cs))>-1){
				if(len==0){
					continue;
				}
				sb.append(new String(cs,0,len));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}catch(OutOfMemoryError e2){
			log.delete();
			KasiteConfig.print("OutOfMemoryError，delete log:"+log.getName());
			throw e2;
		}finally{
			if(in!=null){
				in.close();
			}
		}
		String text=sb.toString();
		try {
			String isCompress=KasiteConfig.getValue("Compress");
			if(isCompress!=null){
				if("1".equals(isCompress)||"true".equalsIgnoreCase(isCompress)){
					text=this.compress(text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(text==null||text.isEmpty()){
			log.delete();
			KasiteConfig.print(log.getName()+" deleted because empty!!!");
			return true;
		}
		String ret="";
		if(text.length()>1){
			String orgId=KasiteConfig.getOrgCode();
			// 发送到ES服务器
			ret = logDeal.handle(text, orgId);
			if(!ret.startsWith("OK")){
				if(System.currentTimeMillis()-log.lastModified()>DELETE_MILLS){//超过保留时间，即使消费失败，也要删除掉
					KasiteConfig.print("resp:"+ret+","+log.getName()+" send failed,delete because too old--"+new Date(log.lastModified()));
					log.delete();
				}
			}
		}
		return !ret.equals("OK#BUSY");
	}
	
	
	
	private String compress(final String text) throws Exception{
		if(text==null||text.isEmpty()){
			return null;
		}
		String[] js=ln.split(text);
		
		List<String> list=new ArrayList<String>(js.length);
		for(final String oneLog:js){
			if(oneLog==null||oneLog.isEmpty()){
				continue;
			}
			
			LogInfo info= JSON.toJavaObject(JSON.parseObject(oneLog), LogInfo.class);
//			JSONObject context_json=JSONObject.fromObject(info.getContent());
			JSONObject context_json = JSON.parseObject(info.getContent());
			String r=context_json.getString("Result");
			if(r==null||r.isEmpty()){
				list.add(oneLog);
				continue;
			}
			JSONObject result = getResult(r,context_json);
			context_json.remove("Result");
			if(result!=null && !result.isEmpty()){
				context_json.put("Result", result.toString());
			}
			info.setContent(context_json.toString());
			JSONObject info_json = (JSONObject) JSON.toJSON(info);
			list.add(info_json.toJSONString());
		}
		StringBuilder newLogs=new StringBuilder();
		for(String s:list){
			if(newLogs.length()>0){
				newLogs.append("\r\n");
			}
			newLogs.append(s);
		}
		String tmp=newLogs.toString();
		if(KasiteConfig.getDebug()){
			KasiteConfig.print("compress log,log count="+list.size()+", text from "+text.length()+" to "+tmp.length());
			if(text.length()<tmp.length()){
				KasiteConfig.print("orign log:"+text);
				KasiteConfig.print("compress :"+tmp);
			}
		}
		return tmp;
	}
	
	private JSONObject getResult(String result,JSONObject log){
		JSONObject doc = new JSONObject();
		try
		{
			JSONObject resultJson = JSON.parseObject(result);
			Integer code = resultJson.getInteger("Code");
			if(null == code) {
				code = resultJson.getInteger("code");
			}
			if(null == code) {
				code = resultJson.getInteger("RespCode");
			}
			if(null != code){
				doc.put("Code", code);
			}
			String message = resultJson.getString("Message");
			if(null == message) {
				message = resultJson.getString("message");
			}
			if(null == message) {
				message = resultJson.getString("RespMessage");
			}
//			String message=resultJson.optString("Message", resultJson.optString("message",""));
			if(message!=null&&message.length()>0){
				doc.put("Message", message);
			}
		}
		catch(Exception e)
		{
			if(null != log.getInteger("OutType") && log.getInteger("OutType")==1){
				try
				{
					Document document = DocumentHelper.parseText(log.getString("Result"));
					Element codeElement = document.getRootElement().element("Code");
					Element messageElement = document.getRootElement().element("Message");
					if (codeElement != null) {
						doc.put("Code", Integer.parseInt(codeElement.getText()));
					}
					if (messageElement != null) {
						doc.put("Message", messageElement.getTextTrim());
					}
				}
				catch(Throwable e1)
				{
					
				}
			}else{
				System.err.println("result error:OutType");
			}
		}
		return doc;
	}

}
