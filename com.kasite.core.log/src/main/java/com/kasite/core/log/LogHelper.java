package com.kasite.core.log;

/**
 * 
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiList;
import com.kasite.core.common.log.LogInfo;
import com.kasite.core.common.log.LogSourceKey;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.StringUtil;

/*
 * @author zhangzz
 * @company yihu.com
 * 2014-3-19下午3:10:32
 */
public class LogHelper {
	protected static final Logger logger = LoggerFactory.getLogger(LogHelper.class);
	private static final int NOTEXIST=-1234345344;
	//path字段，以#开头
//	public static final String GID="gid";
	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		String s="abcd";
		KasiteConfig.print(s.substring(0,s.length()));
//		File path=new File("C:\\Users\\youtl\\Desktop\\loged");
//		File[] files=path.listFiles();
//		for(File f:files){
//			List<String> jsons=Files.readLines(f, Charset.forName("UTF-8"));
//			for(String json:jsons){
//				JSONObject raw=JSONObject.fromObject(json);
//				if(!"wsgw-log".equals(raw.optString("moduleName"))){
//					continue;
//				}
//				JSONObject log=createLog(raw);
//				KasiteConfig.print(log.optInt(LogKey.code)+" "+log.optString("param"));
//				if("UserMgmt.User.queryUserInfoByID".equals(log.optString(LogSourceKey.Api))
//						&&log.optInt(LogKey.code)!=10000){
//					KasiteConfig.print(json);
//				}
//			}
//			
//		}

	}
	public static String currTimes2String(long t) throws ParseException{
		String strFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(strFormat);
	    return format.format(new Date(t));
	}
	/**
	 * 如果api不是a.b.c格式，就返回null
	 * @param log
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static JSONObject createLog(JSONObject log) throws UnsupportedEncodingException, ParseException{
		LogInfo info=(LogInfo) JSON.toJavaObject(log, LogInfo.class);
		Map<String,Object> doc = new HashMap<String,Object>();
//		String path=info.getHeader().getPath();
//		if(path!=null && path.startsWith("#")){
//			doc.put(GID, path.substring(1));
//		}
		JSONObject json= JSON.parseObject(info.getContent());//JSONObject.fromObject();
		String api = json.getString(LogSourceKey.Api);
		if(StringUtil.isBlank(api)) {
			api = "";
		}
//		if(api.split("\\.").length!=3){
//			return null;
//		}
		String sequenceNo = json.getString(LogSourceKey.SequenceNo);//"0";
		if(StringUtil.isBlank(sequenceNo)){
			sequenceNo = "0";
		}
		String uniqueReqId = json.getString(LogSourceKey.uniqueReqId);//"0";
		if(StringUtil.isBlank(uniqueReqId)){
			uniqueReqId = "0";
		}
		Integer outType = json.getInteger(LogSourceKey.OutType);//"0";
		if(StringUtil.isBlank(outType)){
			outType = NOTEXIST;
		}
		Integer paramType = json.getInteger(LogSourceKey.ParamType);//"0";
		if(StringUtil.isBlank(paramType)){
			paramType = NOTEXIST;
		}
		String v = !json.containsKey(LogSourceKey.V) ? "1" : json.getString(LogSourceKey.V);
//		JSONObject header=json.getJSONObject("header");
		
		//
//		doc.put(LogKey.id, UUID.randomUUID().replaceAll("-", ""));
		doc.put(LogKey.sequenceno, sequenceNo);
		doc.put(LogKey.uniquereqid, uniqueReqId);
		if(api!=null){
			doc.put(LogKey.api, api);
		}
		if(paramType!=NOTEXIST)
			doc.put(LogKey.paramtype, paramType);
		if(outType!=NOTEXIST)
			doc.put(LogKey.outtype, outType);
		
		if(StringUtil.isNotBlank(api)) {
			String apiName = ApiList.me().apiJson().getString(api);
			if(StringUtil.isNotBlank(apiName)) {
				doc.put("_apiName", apiName);
			}
		}
		doc.put(LogKey.v, KasiteConfig.getAppId());
		doc.put(LogKey.orderid, json.getString(LogSourceKey.OrderId));
		doc.put(LogKey.clienturl, json.getString(LogSourceKey.ClientUrl));
		doc.put(LogKey.wsgwurl, json.getString(LogSourceKey.WsgwUrl));
		doc.put(LogKey.url, json.getString(LogSourceKey.Url));
		doc.put(LogKey.code, json.getString(LogSourceKey.Code));
		doc.put(LogKey.classname, json.getString(LogSourceKey.ClassName));
		doc.put(LogKey.methodname, json.getString(LogSourceKey.MethodName));
		doc.put(LogKey.mills, !json.containsKey(LogSourceKey.Times) ? 0 :json.getInteger(LogSourceKey.Times));
		
		if(json.containsKey(LogSourceKey.Times)){
			doc.put("resp_mills", json.getLong("resp_mills"));
		}
		String currTimes = "";
		String strFormat = "yyyy-MM-dd HH:mm:ss";
		if(log.containsKey("currTimes")){//yyyy-MM-dd HH:mm:ss
			Date date = new Date(log.getLong("currTimes"));
			currTimes = DateOper.formatDate(date, strFormat);
		}else{
			logger.debug("CurrTimes is not exist!!!{}.{}",json.getString(LogSourceKey.ClassName),json.getString(LogSourceKey.MethodName));
			currTimes = DateOper.getNow(strFormat);
		}
		doc.put(LogKey.inserttime, currTimes);
		
		
		JSONObject authInfo = json.getJSONObject(LogSourceKey.AuthInfo);
		if(null != authInfo) {
			doc.put(LogKey.sessionkey, authInfo.get("sessionKey"));
			doc.put(LogKey.sign, authInfo.get("sign"));
			doc.put(LogKey.clientid, authInfo.get("clientId"));
			doc.put(LogKey.configKey, authInfo.get("configKey"));
			doc.put(LogKey.clientversion, authInfo.get("clientVersion"));
		}
		
//		//处理鉴权信息(调用方)
//		if (json.containsKey(LogSourceKey.AuthInfo)) 
//		{
//			net.sf.json.JSONObject authInfoJson =net.sf.json.JSONObject.fromObject(json.getString(LogSourceKey.AuthInfo));
//			doc.put(LogKey.clientid,!authInfoJson.containsKey(LogSourceKey.clientid)?"":authInfoJson.getString(LogSourceKey.clientid));
//			doc.put(LogKey.sign,!authInfoJson.containsKey(LogSourceKey.sign)?"":authInfoJson.getString(LogSourceKey.sign));
//			doc.put(LogKey.clientversion,!authInfoJson.containsKey(LogSourceKey.clientversion)?"":authInfoJson.getString(LogSourceKey.clientversion));
//			doc.put(LogKey.sessionkey,!authInfoJson.containsKey(LogSourceKey.sessionkey)?"":authInfoJson.getString(LogSourceKey.sessionkey));
////			KasiteConfig.print(doc);
//		}
		//处理入参
		String param = json.getString(LogSourceKey.Param);
		doc.put("param", param);
//		net.sf.json.JSONObject paramJson =net.sf.json.JSONObject.fromObject(json.getString(LogSourceKey.Param));
//		Iterator iterator = paramJson.keys();
//		while (iterator.hasNext()) {
//			String key = (String) iterator.next();
//			Object value=paramJson.get(key);
//			if(value!=null&& value instanceof String)
//			{
//				doc.put(key.toLowerCase(), java.net.URLDecoder.decode((String)value,"utf-8"));
//			}
//			else
//			{
//				doc.put(key.toLowerCase(),value);
//			}
//		}
		//处理结果集
		if (json.containsKey(LogSourceKey.Result)) {
			String r=json.getString(LogSourceKey.Result);
			int max=Integer.getInteger("result.max",1024*1024*10);
			if(r.length()<=max){
				doc.put("result", r);
			}else{
				doc.put("result", r.substring(0,max)+"..");
			}
			if(r.length()<1024*1024*10 && r.length()>0){
				try
				{
					JSONObject resultJson = JSON.parseObject(r);
					//如果是微信的返回值 则判断结果集总是否有errcode 如果有则code 记录 为 errcode
					if(api.startsWith("wechat_")) {
						if (resultJson.containsKey("errcode")) {
							doc.put(LogKey.code, resultJson.get("errcode"));
						}
					}
					if (resultJson.containsKey(LogSourceKey.Code)) {
						doc.put(LogKey.code, resultJson.get(LogSourceKey.Code));
					}
					if (resultJson.containsKey(LogSourceKey.Message)) {
						doc.put(LogKey.message, resultJson.get(LogSourceKey.Message));
					}
					if (resultJson.containsKey("code")) {
						doc.put(LogKey.code, resultJson.get("code"));
					}
					if (resultJson.containsKey("message")) {
						doc.put(LogKey.message, resultJson.get("message"));
					}
					if (resultJson.containsKey("RespCode")) {
						doc.put(LogKey.code, resultJson.get("RespCode"));
					}
					if (resultJson.containsKey("RespMessage")) {
						doc.put(LogKey.message, resultJson.get("RespMessage"));
					}
				}
				catch(Exception e)
				{
					if(outType==1){
						try
						{
							Document document = DocumentHelper.parseText(json.getString(LogSourceKey.Result));
							Element codeElement = document.getRootElement().element(LogSourceKey.Code);
							if(null == codeElement) {
								codeElement = document.getRootElement().element("RespCode");
							}
							Element messageElement = document.getRootElement().element(LogSourceKey.Message);
							if(null == messageElement) {
								messageElement = document.getRootElement().element("RespMessage");
							}
							if (codeElement != null) {
								doc.put(LogKey.code, Integer.parseInt(codeElement.getText()));
							}
							if (messageElement != null) {
								doc.put(LogKey.message, messageElement.getTextTrim());
							}
						}
						catch(Throwable e1)
						{
							
						}
					}else{
						System.err.println("result error:"+r);
					}
				}
			}else{
				doc.put(LogKey.message, "返回值不正确。长度为"+r.length());
			}
			Object msgObj = doc.get(LogKey.message);
			if(null != msgObj && msgObj.toString().length() > 200) {
				doc.put(LogKey.message, msgObj.toString().subSequence(0, 200));
			}
		}
		Iterator<String> it=doc.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			if(doc.get(key)==null){
				it.remove();
			}
		}
		
		JSONObject js = (JSONObject) JSON.toJSON(doc);
		return js;
	}
	/**
	 * @param logInfo
	 * @return
	 * @throws ParseException 
	 */
	public static JSONObject createDiyLog(LogInfo logInfo) throws ParseException {
		JSONObject doc = new JSONObject();
		String appName = logInfo.getHeader().getAppName();//应用名称
		String className = logInfo.getHeader().getClassName();//类名
		String ip = logInfo.getHeader().getIp();//Ip
		int lineNumber = logInfo.getHeader().getLineNumber();//行号
		String methodName = logInfo.getHeader().getMethodName();//方法名
		String content = logInfo.getContent()==null?"":logInfo.getContent();//"yyyy-MM-dd HH:mm:ss" + "|" + 日志体
		String level = logInfo.getLevel().name();//日志等级
		String moduleName = logInfo.getModuleName();//模块名称
		String inserttime = currTimes2String(logInfo.getCurrTimes());
		doc.put("_appname",appName);
		doc.put("_modulename",moduleName);
		doc.put("_classname",className);
		doc.put("_linenumber",lineNumber);
		doc.put("_methodname",methodName);
		doc.put("_level",level);
		doc.put("_inserttime", inserttime);// 
		doc.put("_ip",ip);
		doc.put("content",content);

		JSONObject json = JSON.parseObject(content);
		JSONObject authInfo = json.getJSONObject("authInfo");
		String orderId = json.getString("_orderId");
		doc.put("_orderId",orderId);
		if(null != authInfo) {
			doc.put(LogKey.sessionkey, authInfo.get("sessionKey"));
			doc.put(LogKey.sign, authInfo.get("sign"));
			doc.put(LogKey.clientid, authInfo.get("clientId"));
			doc.put(LogKey.configKey, authInfo.get("configKey"));
			doc.put(LogKey.uniquereqid, authInfo.get("uuid"));
			json.remove("authInfo");
			doc.put("content",json.toJSONString());
		}
		
//		String path=logInfo.getHeader().getPath();
//		if(path!=null && path.startsWith("#")){
//			doc.put(GID, path.substring(1));
//		}
		
//		net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(doc);
//		JSONObject js = (JSONObject) JSON.toJSON(doc);
		return doc;
	}
	
	public static JSONObject createContentyLog(LogInfo logInfo) throws ParseException {
		Map<String,Object> doc = new HashMap<String,Object>();
		String appName = logInfo.getHeader().getAppName();//应用名称
		String ip = logInfo.getHeader().getIp();//Ip
		String content = logInfo.getContent();
		try{
			JSONObject j = JSON.parseObject(content);
			content=j.getString("msg");
			doc.put("_classname",j.getString("class"));
			doc.put("_linenumber",j.getInteger("line"));
			doc.put("_methodname",j.getString("method"));
		}catch(Exception e){
			e.printStackTrace();
		}
		String level = logInfo.getLevel().name();//日志等级
		String moduleName = logInfo.getModuleName();//模块名称
		String inserttime = currTimes2String(logInfo.getCurrTimes());
		doc.put("_appname",appName);
		doc.put("_modulename",moduleName);
		doc.put("_level",level);
		doc.put("_inserttime", inserttime);// 
		doc.put("_ip",ip);
		doc.put("content",content);
//		String path=logInfo.getHeader().getPath();
//		if(path!=null && path.startsWith("#")){
//			doc.put(GID, path.substring(1));
//		}
		
//		net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(doc);
		JSONObject js = (JSONObject) JSON.toJSON(doc);
		return js;
	}
	/**
	 * @param logInfo
	 * @return
	 * @throws ParseException 
	 */
	public static String createResponseLog(LogInfo logInfo) throws ParseException {
		//响应日志
		String inserttime =  currTimes2String(logInfo.getCurrTimes());
		String content = logInfo.getContent()==null?"":logInfo.getContent();//"yyyy-MM-dd HH:mm:ss" + "|" + 日志体
		net.sf.json.JSONObject doc = net.sf.json.JSONObject.fromObject(content);
		//其它字段
		doc.put("callername",logInfo.getHeader().getAppName());
		doc.put("_ip", logInfo.getHeader().getIp());
		doc.put("_inserttime", inserttime);//
		return doc.toString();
	}
//	/**
//	 * @param logInfo
//	 * @return
//	 * @throws ParseException 
//	 */
//	public static String createTimeOutLog(LogInfo logInfo) throws ParseException {
//		//SQL超时日志
//		Map<String,Object> doc = new HashMap<String,Object>();
//		String appName = logInfo.getHeader().getAppName();//应用名称
//		String ip = logInfo.getHeader().getIp();//Ip
//		String inserttime = currTimes2String(logInfo.getCurrTimes());
//		String content = logInfo.getContent()==null?"":logInfo.getContent();//"yyyy-MM-dd HH:mm:ss" + "|" + 日志体
//		net.sf.json.JSONObject json_c = net.sf.json.JSONObject.fromObject(content);
//		String time = json_c.has("time")?json_c.getString("time").trim():"default";
//		String source = json_c.has("source")?json_c.getString("source").trim():"default";
//		String sqlmd5 = json_c.has("sqlmd5")?json_c.getString("sqlmd5").trim():"default";
//		String sqls = json_c.has("sqls")?json_c.getString("sqls").trim():"default";
//		String connid = json_c.has("connid")?json_c.getString("connid").trim():"default";
//		int mills = json_c.has("mills")?json_c.getInt("mills"):-1;
//		//其它字段
////		String appendcontent = json_c.has("appendContent")?json_c.getString("appendContent"):"";
//		doc.put("_appname",appName);
//		doc.put("source",source);
//		doc.put("sqlmd5",sqlmd5);
//		doc.put("sqls",sqls);
//		doc.put("connid",connid);
//		doc.put("mills",mills);
//		doc.put("_ip",ip);
//		doc.put("time", time);//
//		doc.put("_inserttime", inserttime);//
////		doc.put("appendcontent",appendcontent);
//		net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(doc);
//		return js.toString();
//	}
//	public static JSONObject createDbLog(LogInfo logInfo) throws ParseException {
//		Map<String,Object> doc = new HashMap<String,Object>();
//		String appName = logInfo.getHeader().getAppName();//应用名称
//		String className = logInfo.getHeader().getClassName();//类名
//		String ip = logInfo.getHeader().getIp();//Ip
//		int lineNumber = logInfo.getHeader().getLineNumber();//行号
//		String methodName = logInfo.getHeader().getMethodName();//方法名
//		String content = logInfo.getContent()==null?"":logInfo.getContent();//"yyyy-MM-dd HH:mm:ss" + "|" + 日志体
//		String level = logInfo.getLevel().name();//日志等级
////		String moduleName = logInfo.getModuleName();//模块名称
//		String inserttime = currTimes2String(logInfo.getCurrTimes());
//		doc.put("type","DB");
//		doc.put("_appname",appName);
//		doc.put("_level",level);
//		doc.put("_inserttime", inserttime);// 
//		doc.put("_ip",ip);
////		String path=logInfo.getHeader().getPath();
////		if(path!=null && path.startsWith("#")){
////			doc.put(GID, path.substring(1));
////		}
//		JSONObject json=JSONObject.fromObject(content);
//		doc.put("db", json.optString("db"));
//		doc.put("param", json.optString("param"));
//		doc.put("result", json.optString("result"));
//		doc.put("time", json.optInt("time",-1));
//		doc.put("time_total", json.optInt("time_total",-1));
//		doc.put("conid", json.optLong("conid"));
//		doc.put("insert", json.optInt("insert"));
//		doc.put("update", json.optInt("update"));
////		doc.put("sysid", IPMapper.getSysId(ip));
//		doc.put("sysid", KasiteConfig.getAppId());
//		String sql=json.optString("sql");
//		if(sql!=null && !sql.isEmpty()){
//			doc.put("sql", sql);
//			doc.put("md5", MD5.compile(sql));
//		}
//		if(json.has("log-version")){//新版
//			doc.put("optype",json.optString("sqltype"));
//			doc.put("classname", className);//新增
//			doc.put("methodName", methodName);//新增
//			doc.put("lineNumber", lineNumber);//新增
//			doc.put("stack", json.optString("exception"));//新增
//		}else{
//			doc.put("optype",methodName);
//			if("createConnection".equals(methodName)){
//				doc.put("param", json.optString("create_conn"));
//			}
//		}
//		net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(doc);
//		return js;
//	}
	
//	public static JSONObject createMongoLog(LogInfo logInfo) throws ParseException {
//		Map<String,Object> doc = new HashMap<String,Object>();
//		String appName = logInfo.getHeader().getAppName();//应用名称
//		String className = logInfo.getHeader().getClassName();//类名
//		String ip = logInfo.getHeader().getIp();//Ip
//		int lineNumber = logInfo.getHeader().getLineNumber();//行号
//		String methodName = logInfo.getHeader().getMethodName();//方法名
//		String content = logInfo.getContent()==null?"":logInfo.getContent();//"yyyy-MM-dd HH:mm:ss" + "|" + 日志体
//		String level = logInfo.getLevel().name();//日志等级
////		String moduleName = logInfo.getModuleName();//模块名称
//		String inserttime = currTimes2String(logInfo.getCurrTimes());
//		doc.put("type","MONGO");
//		doc.put("_appname",appName);
//		doc.put("_level",level);
//		doc.put("_inserttime", inserttime);// 
//		doc.put("_ip",ip);
////		String path=logInfo.getHeader().getPath();
////		if(path!=null && path.startsWith("#")){
////			doc.put(GID, path.substring(1));
////		}
//		JSONObject json=JSONObject.fromObject(content);
//		String namespace=json.getString("namespace");
//		doc.put("db", namespace);
//		doc.put("result", json.optString("result"));
//		doc.put("time", json.optInt("time",-1));
//		doc.put("time_total", json.optInt("time",-1));
////		doc.put("sysid", IPMapper.getSysId(ip));
//		doc.put("sysid", KasiteConfig.getAppId());
//		doc.put("classname", className);//新增
//		doc.put("methodName", methodName);//新增
//		doc.put("lineNumber", lineNumber);//新增
//		doc.put("stack", json.optString("stack"));//新增
//		doc.put("sql", json.optString("sql"));
//		doc.put("optype",json.optString("optype"));
//		doc.put("serverip",json.optString("serverip"));
//		
////		net.sf.json.JSONObject js = net.sf.json.JSONObject.fromObject(doc);
//		JSONObject js = (JSONObject) JSON.toJSON(doc)
//		return js;
//	}
}
