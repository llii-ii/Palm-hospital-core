//package com.kasite.core.common.util.log;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import com.yihu.wsgw.ConfigUtil;
//import com.yihu.wsgw.Convent;
//import com.yihu.wsgw.api.InterfaceMessage;
//
//
///**
// * 日志管理类
// * @author daiys
// * @date 2014-11-24
// */
//public class LogUtil {
//	private LogCfgVo vo = null;
//	public LogUtil(Class clazz) {
////		String clazzName = clazz.getName();
////		try {
////			this.vo = ConfigUtil.getInstance().getLogCfg(clazzName);
////		} catch (Exception e) {
////			e.printStackTrace();
////			this.vo = new LogCfgVo(clazzName,Convent.DEFAULTLEVEL_INFO,Convent.DEFAULTFILENAME);
////		}
//	}
////	public void waring(InterfaceMessage msg,String resp,long times){
////
////		String apiName = msg.getApiName();
////		String clientip = msg.getClientIp();
////		String param = msg.getParam();
////		String authinfo = msg.getAuthInfo();
////		String version = msg.getVersion();
////		Integer outtype = msg.getOutType();
////		Integer paramtype = msg.getParamType();
////		
////		if(isWaring(Convent.DEFAULTLEVEL_WARING)){
////			
////		}
////		
////	}
//	/**
//	 * 判断该等级是否需要记录日志
//	 * @param level 等级  waring , info , error
//	 * @return
//	 */
//	private boolean isWaring(String level){
//		if(null == vo.getLevel()){
//			return false;
//		}
//		
//		if(vo.getLevel().equals(Convent.DEFAULTLEVEL_WARING) || Convent.DEFAULTLEVEL_ERROR.equals(vo.getLevel())){
//			return true;
//		}
//		
//		return false;
//	}
//
//	private static Set<String> read() throws IOException, URISyntaxException {
//		synchronized (notSaveApi) {
//			notSaveApi = new HashSet<String>();
//			String filePath = System.getProperty("user.dir")+File.separator +"calllog"+File.separator+"notsavelogapi.txt";
//			File notsavelogapiFile = new File(filePath);
//			if(!notsavelogapiFile.exists()) {
//				notsavelogapiFile.createNewFile();
//			}else {
//				BufferedReader reader = null;
//				String temp = null;
//				try {
//					reader = new BufferedReader(new FileReader(filePath));
//					while ((temp = reader.readLine()) != null) {
//						notSaveApi.add(temp);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (reader != null) {
//						try {
//							reader.close();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			return notSaveApi;
//		}
//	}
//
//	private static Set<String> notSaveApi = null;
//	/**
//	 * 日志写入到本地文件
//	 * @param msg
//	 * @param sequenceNo
//	 * @param className
//	 * @param methodName
//	 * @param url
//	 * @param localip
//	 * @param resp
//	 * @param times
//	 * @param logid
//	 */
//	public void info(InterfaceMessage msg,String sequenceNo,String className,
//			String methodName,String url,String localip,
//			String resp,Long times,String logid){
//		String param = msg.getParam();
//		String auth = msg.getAuthInfo();
//		String api = msg.getApiName();
//		String str ="\r\n";
//		boolean isNotWrite = false;
//		if(null == notSaveApi || notSaveApi.size() > 0) {
//			try {
//				notSaveApi = read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//		}
//		for (String apiStr : notSaveApi) {
//			if(null != api && api.equalsIgnoreCase(apiStr)) {
//				//如果查询的返回值都是10000 那么不保存
//				if(null != resp && resp.indexOf("<RespCode>10000</RespCode>") >=0 ) {
//					isNotWrite = true;
//					return;
//				}
//			}
//		}
//		//如果返回 
//		if(!isNotWrite) {
//			boolean hasAppend = true;
//			 try {
//				StringBuffer sbf = new StringBuffer();
//				sbf
//				.append("============================= ").append(logid).append("$").append(sequenceNo).append(str)
//				.append("============================= API : ").append(api) .append("\t").append(className).append(".").append(methodName).append(str)
//				.append("============================= AuthInfo:").append(auth).append(str)
//				.append("============================= Req \r\n")
//				.append(param)
//				.append(str)
//				.append("============================= Resp \r\n")
//				.append(resp)
//				.append(str)
//				.append("============================= Times:"+times).append(str);
//				Date date = new Date();
//		        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//		        String nowdate = format.format(date);
//		        //日志文件夹每天一个
//				String localLogFileDir = System.getProperty("user.dir")+File.separator+"calllog"+File.separator+nowdate+File.separator+api;
//				File filedir = new File(localLogFileDir);
//				if(!filedir.exists() || !filedir.isDirectory()) {
//					filedir.mkdirs();
//				}
//				//文件每小时一个
//				format = new SimpleDateFormat("yyyyMMddHH");
//		        nowdate = format.format(date);
//				String path = localLogFileDir + File.separator + nowdate +".log";
//			
//			      File writefile = new File(path);
//			      if (!writefile.exists())
//			      {
//			        writefile.createNewFile();
//			        writefile = new File(path);
//			      }
//			      FileWriter filewriter = new FileWriter(writefile, hasAppend);
//			      
//			      filewriter.write(sbf.toString());
//			      filewriter.flush();
//			      filewriter.close();
//			    }
//			    catch (Exception d)
//			    {
//			    	d.printStackTrace();
//			    }
//		}
//	}
//	
//	
////	public void info(InterfaceMessage msg,String sequenceNo,String className,String methodName,String url,String localip,String resp,Long times,String logid){
////		com.yihu.wsgw.vo.LogInfo info = new LogInfo();
////		info.setClassName(className);
////		info.setLocalip(localip);
////		info.setMethodName(methodName);
////		info.setMsg(msg);
////		info.setResp(resp);
////		info.setSequenceNo(sequenceNo);
////		info.setTimes(times);
////		info.setUrl(url);
////		info.setGuid(logid);
////		LogManager.add(info);
////		
////	}
//	
//	public static void main(String[] args) throws Exception {
//		LogUtil log = new LogUtil(LogUtil.class);
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setApiName("test.api.name");
//		msg.setAuthInfo("authinfo");
//		msg.setClientIp("cip");
//		msg.setParam("param");
//		msg.setVersion("v");
//		msg.setParamType(1);
//		msg.setOutType(2);
//		log.info(msg,"","","","","","responseMsg",22l,UUID.randomUUID().toString());
////		ConfigUtil.getInstance();
//////		Object f = DB.me().queryForInteger(MyDatabaseEnum.HOS, new Sql(" SELECT  count(1) FROM dbo.SysObjects WHERE ID = object_id(N'call_log_201503')"));
//////		KasiteConfig.print(f);
////		Object f = DB.me().queryForObject(MyDatabaseEnum.HOS, new Sql("select object_id (N'call_log_201504',N'U');"));
////		KasiteConfig.print(f);
////		Statement stme = DB.me().getConnection(MyDatabaseEnum.HOS).getConn().createStatement();
////		ResultSet set = stme.executeQuery(" select  object_id(N'call_log_201504',N'U')");
////		KasiteConfig.print(set);
//	}
//}
