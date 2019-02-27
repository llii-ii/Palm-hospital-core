//package com.yihu.hos.util;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import com.common.json.JSONArray;
//import com.common.json.JSONException;
//import com.coreframework.db.DatabaseEnum;
//import com.coreframework.remoting.Url;
//import com.kasite.core.common.config.KasiteConfig;
//import com.yihu.hos.exception.AbsHosException;
//import com.yihu.hos.exception.CommonServiceException;
//import com.yihu.hos.service.CommonServiceRetCode;
//
//import net.sf.json.JSONObject;
//
///**
// * 路由解释工具，主要功能：
// * 1、ZK中路由配置信息（XML）的解释及初始化
// * 2、对外提供获取路由的方法queryReturnRouteVo，返回MappingRouteVo实例。
// * 【支持以下4种情况：
// * 1、应用名称：account
// * 2、模块：account.AccountWs
// * 3、API方法：account.AccountWs.a
// * 4、API方法带参数：account.AccountWs.a_参数名称_参数值
// * 】
// * @author zhangzz
// * @company yihu.com
// * 2014-7-18下午7:40:39
// */
//public class RouteParseUtil {
////	public static void main(String[] args) throws Exception {
////		String info = RouteParseUtil.getInstance().queryAllRouteList();
////		System.out.println(info);
////	}
//	/**
//	 * 
//	 */
//	private static Thread load = null;
//	
//	private static volatile RouteParseUtil instance = null;
//	/*
//	 * 接口实现的映射：Map<String, Map<String,String>> zkServiceMap
//	 * key:应用节点路径，结构:rootPath+"/"+应用节点名称。如：/ApiRouteRoot/account
//	 * value:Map<String,String>【key:name属性值】【value:remoteClass属性值】
//	 */
//	private static Map<String, Map<String,String>> zkServiceMap = new HashMap<String, Map<String,String>>();
//	/*
//	 * 调用路由的映射：Map<String,RouteVo>
//	 * key:应用节点路径
//	 * value:RouteVo
//	 */
//	private static Map<String, RouteVo> zkRouteMap = new HashMap<String, RouteVo>();
//	/**
//	 * 通过配置文件 route.xml 的文件来路由地址信息
//	 */
//	private static Map<String, String> routeConfigListMap = new HashMap<String, String>();
//	
//	private static String rootPath = "/ApiRouteRoot";
//	private static final String charsetName = "GBK";
//	//private String zkService;
////	private ZkClientWrapper wrapper = null;
////	private ZkClient zkClient = null;
//	//节点
//	private String Config = "Config";
//	private String Mapping = "Mapping";
//	private String Route = "Route";
//	private String module = "module";
//	private String api = "api";
//	private String param = "param";
//	//属性
//	private String url = "url";
//	private String proxyUrl = "proxyUrl";
//	private String name = "name";
//	private String remark = "remark";
//	private String value = "value";
//	private String remoteClass = "remoteClass";
//	
//	
//	
//	public void addConfig(String nodeName,String nodeValue) throws AbsHosException{
//		String nodePath =rootPath+"/"+nodeName;
//		routeConfigListMap.put(nodePath, nodeValue);
//		parseXmlNode(nodePath, nodeName, nodeValue);
//	}
//	
//	public Map<String,String> getMap(){
//		return routeConfigListMap;
//	}
//	
//	
//	/**
//	 * 查询路由配置信息，注：先从本地缓存数据查找，如果没找到合适的，则再进行一次ZK应用节点的解析获取。
//	 * @param api 格式为a.b.c
//	 * @param paramJson 请求参数JSON/String
//	 * @return MappingRouteVo 路由配置信息
//	 * @throws Exception 异常抛出（其中各关健方法进行了异常时日志记录）
//	 */
//	public MappingRouteVo queryMappingRouteVo(String api,String paramJson,JSONObject authInfoJson) throws Exception{
//		long start = System.currentTimeMillis();
//		MappingRouteVo mappingRouteVo = null;
//		//1、先本地池查询
//		mappingRouteVo = queryMappingRouteVoByLoc(api, paramJson, authInfoJson);
//		//2、如果没找到合适的再进行一次ZK硬解析获取
//		if(mappingRouteVo == null){
//			mappingRouteVo = queryMappingRouteVoByZk(api, paramJson,authInfoJson);
//		}else if(mappingRouteVo.remoteClass==null || (mappingRouteVo.url==null && mappingRouteVo.proxyUrl==null)){
//			mappingRouteVo = queryMappingRouteVoByZk(api, paramJson,authInfoJson);	
//		}
//		//日志记录
////		try{
////			if("1".equals(ConfigUtil.getInstance().getLogFlag())){
////				long times = (System.currentTimeMillis()-start);
////				System.out.println("queryReturnRouteVo耗时："+times);
////				LogBody logBody = new LogBody();
////				logBody.set("方法",Thread.currentThread().getStackTrace()[1].getMethodName());
////				logBody.set("api",api);
////				logBody.set("paramJson",paramJson);
////				logBody.set("times",times);
////				logBody.set(new com.coreframework.json.JSONObject(mappingRouteVo));
////				Logger.get().info(this.getClass().getSimpleName(), logBody);
////			}
////		}catch (Exception e) {
////			e.printStackTrace();
////		}
//		return mappingRouteVo;
//	}
//	/**
//	 * 通过查询本地（已初始化）数据池方式获取当前传入的api、paramJson参数所对应的路由配置信息
//	 * @param api 格式为a.b.c
//	 * @param paramJson 请求参数JSON/String
//	 * @return MappingRouteVo 路由配置信息
//	 * @throws Exception 异常抛出并记录日志
//	 */
//	private MappingRouteVo queryMappingRouteVoByLoc(String api,String paramJson,JSONObject authInfoJson) throws Exception{
//		long start = System.currentTimeMillis();
//		try{
//			String[] tmp=api.split("\\.");
//			//1、实现类
//			String remoteClass = null;
//			Map<String,String> serviceMap = zkServiceMap.get(rootPath+"/"+tmp[0]);
//			if(serviceMap!=null){
//				if(serviceMap.get(api) != null && !"".equals(serviceMap.get(api))){
//					remoteClass = serviceMap.get(api);//a.b.c
//				}else if(serviceMap.get(tmp[0]+"."+tmp[1]) != null){
//					remoteClass = serviceMap.get(tmp[0]+"."+tmp[1]);//a.b
//				}
//			}
//			//2、路由地址url、proxyUrl
//			String url = null;
//			String proxyUrl = null;
//			RouteVo routeVo = zkRouteMap.get(rootPath+"/"+tmp[0]);
//			if(routeVo != null){
//				Map<String,RouteApiVo> apiMap = routeVo.getApiMap();
//				Map<String,RouteModuleVo> moduleMap = routeVo.getModuleMap();
//				if(apiMap != null && (apiMap.get(api)!=null || apiMap.get(tmp[0]+"."+tmp[1])!=null)){//a.b.c
//					RouteApiVo routeApiVo = apiMap.get(api);
//					if(routeApiVo == null){
//						routeApiVo = apiMap.get(tmp[0]+"."+tmp[1]);
//					}
//					url = routeApiVo.getUrl();//地址
//					proxyUrl = routeApiVo.getProxyUrl();//代理地址
//					List<RouteParamVo> paramList = routeApiVo.getList();//参數
//					if(paramList!=null && paramList.size()>0){
//						for(RouteParamVo routeParamVo : paramList){
//							String paramName = routeParamVo.getParamName();
//							String paramValue = routeParamVo.getParamValue();
//							boolean urlisfind = false;
//							//头参中查找
//							try{
//								JSONObject jo = authInfoJson;
//								if(jo.has(paramName)){
//									String paramV = String.valueOf(jo.get(paramName));
//									if(paramValue.equals(paramV)){
//										url = routeParamVo.getUrl();//地址 
//										proxyUrl = routeParamVo.getProxyUrl();//代理地址
//										urlisfind = true;
//										break;
//									}
//								}
//							}catch (Exception e) {
//							}
//							//参数配置查找
//							if(!urlisfind){
//								try{
//									JSONObject jo = JSONObject.fromObject(paramJson);
//									if(jo.has(paramName)){
//										String paramV = String.valueOf(jo.get(paramName));
//										if(paramValue.equals(paramV)){
//											url = routeParamVo.getUrl();//地址 
//											proxyUrl = routeParamVo.getProxyUrl();//代理地址
//											break;
//										}
//									}
//								}catch (Exception e) {
//								}
//							}
//						}
//					}
//				}else if(moduleMap !=null && moduleMap.get(tmp[0]+"."+tmp[1]) != null){//a.b
//					RouteModuleVo routeModuleVo = moduleMap.get(tmp[0]+"."+tmp[1]);
//					if(routeModuleVo != null){
//						url = routeModuleVo.getUrl();//地址
//						proxyUrl = routeModuleVo.getProxyUrl();//代理地址
//					}
//				}else{//a
//					url = routeVo.getUrl();//地址
//					proxyUrl = routeVo.getProxyUrl();//代理地址
//				}
//			}
//				
//			MappingRouteVo mappingRouteVo = new MappingRouteVo(remoteClass,getUrl(tmp[0],url),getProxyUrl(proxyUrl));
//			//日志记录
////			try{
////				if("1".equals(ConfigUtil.getInstance().getLogFlag())){
////					long times = (System.currentTimeMillis()-start);
////					System.out.println("queryMappingRouteVoByLoc耗时："+times);
////					LogBody logBody = new LogBody();
////					logBody.set("方法",Thread.currentThread().getStackTrace()[1].getMethodName());
////					logBody.set("api",api);
////					logBody.set("paramJson",paramJson);
////					logBody.set("times",times);
////					logBody.set(new com.coreframework.json.JSONObject(routeVo));
////					Logger.get().info(this.getClass().getSimpleName(), logBody);
////				}
////			}catch (Exception e) {
////				e.printStackTrace();
////			}
//			return mappingRouteVo;
//		}catch (Exception e) {
//			e.printStackTrace();
//			StringWriter sw = new StringWriter();
//			PrintWriter w = new PrintWriter(sw);
//			e.printStackTrace(w);
//			throw e;
//		}
//	}
//
//	/**
//	 * 通过解析ZK应用节点的方式获取当前传入的api、paramJson参数所对应的路由配置信息
//	 * @param api 格式为a.b.c
//	 * @param paramJson 请求参数JSON/String
//	 * @return MappingRouteVo 路由配置信息
//	 * @throws Exception 异常抛出并记录日志
//	 */
//	private MappingRouteVo queryMappingRouteVoByZk(String api,String paramJson,JSONObject authInfoJson) throws Exception{
//		long start = System.currentTimeMillis();
//		try{
//			MappingRouteVo  mappingRouteVo  = new MappingRouteVo();
//			String[] tmp=api.split("\\.");
//			//1、配置节点
//			String nodeName = tmp[0];
//			String nodePath = rootPath+"/"+tmp[0];
//			String nodeValue = routeConfigListMap.get(nodePath);
//			if(null != nodeValue && !"".equals(nodeValue)){
////				parseXmlNode(nodePath, nodeName, nodeValue);
//				mappingRouteVo = queryMappingRouteVoByLoc(api,paramJson,authInfoJson);
//			}
////			Object obj = zkClient.readData(nodePath, true);
////			if(obj!=null){
////				byte[] b_nodeValue=(byte[])obj;
////				String nodeValue = new String(b_nodeValue,charsetName);
////				parseXmlNode(nodePath, nodeName, nodeValue);
////				mappingRouteVo = queryMappingRouteVoByLoc(api,paramJson);
////			}
//			
//			//日志记录
////			try{
////				if("1".equals(ConfigUtil.getInstance().getLogFlag())){
////					long times = (System.currentTimeMillis()-start);
////					System.out.println("queryMappingRouteVoByZk耗时："+times);
////					LogBody logBody = new LogBody();
////					logBody.set("方法",Thread.currentThread().getStackTrace()[1].getMethodName());
////					logBody.set("api",api);
////					logBody.set("paramJson",paramJson);
////					logBody.set("times",times);
////					logBody.set(new com.coreframework.json.JSONObject(mappingRouteVo));
////					Logger.get().info(this.getClass().getSimpleName(), logBody);
////				}
////			}catch (Exception e) {
////				e.printStackTrace();
////			}
//			return mappingRouteVo;
//		}catch (Exception e) {
//			StringWriter sw = new StringWriter();
//			PrintWriter w = new PrintWriter(sw);
//			e.printStackTrace(w);
////			Logger.get().error(this.getClass().getSimpleName(), new LogBody().set("方法",Thread.currentThread().getStackTrace()[1].getMethodName()).set("异常",sw.toString()));
//			throw e;
//		}
//	}
//	private DatabaseEnum db;
//	public void init(DatabaseEnum db) throws AbsHosException{
//		this.db = db;
//		List<Map<String,Object>> maps = CommonUtilsDao.queryRoute(db);
//		for (Map<String, Object> map : maps) {
//			String nodeName = (String) map.get("node");
//			String docxml = (String) map.get("config");
//			addConfig(nodeName, docxml);
//		}
//	}
//	
//	
//	/*
//	 * 构造：初始化
//	 */
////	private RouteParseUtil(String zkService) throws Exception{
//	private RouteParseUtil(final DatabaseEnum db) throws Exception{
//		super();
//		//this.zkService=zkService;
//		//createZkClient(zkService);
//		//getDataByZk();
//		if (null == load) {
//			RouteWatch runnable = new RouteWatch(new RouteChange() {
//				public void onChange() {
//					try {
//						init(db);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//			Thread thread = new Thread(runnable);
//			thread.setName("RouteWatch-Thread");
//			thread.start();
//			load = thread;
//		}
//		init(db);
//	}
//
//	
////	public static RouteParseUtil getInstance(String zkService) throws Exception {
////		if (instance == null) {
////			synchronized (RouteParseUtil.class) {
////				if (instance == null) {
////					//System.out.println("init RouteParseUtil...");
////					instance = new RouteParseUtil(zkService);
////				}
////			}
////		}
////		return instance;
////	}
//	public static RouteParseUtil getInstance(DatabaseEnum db) throws Exception {
//		if (instance == null) {
//			synchronized (RouteParseUtil.class) {
//				if (instance == null) {
//					//System.out.println("init RouteParseUtil...");
//					instance = new RouteParseUtil(db);
//				}
//			}
//		}
//		return instance;
//	}
//	/**
//	 * 创建ZK客户端
//	 * @param zkService zk地址
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unused")
//	private void createZkClient(String zkService) throws Exception{
////			zkService = ConfigUtil.getInstance().getCenterServerUrl().toString();
////			zkService = "172.18.20.21:2181";
////			System.out.println("zk服务器："+zkService);
////			wrapper=ZkClientFactory.getInstance().getZkClient(zkService);
////			zkClient = wrapper.getZkClient();
//	}
//	/**
//	 * 对根目录下所有节点进行解析并初始化缓存数据
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unused")
////	private void getDataByZk() throws Exception{
////		List<String> list = zkClient.getChildren(rootPath);
////		if(list!=null && list.size()>0){
////			//System.out.println("zk：【"+rootPath+"】总应用节点个数为："+list.size());
////			int i = 0;
////			for (String nodeName : list) {
////				i++;
////				Object obj = zkClient.readData(rootPath+"/"+nodeName, true);
////				if(obj!=null){
////					byte[] b_nodeValue=(byte[])obj;
////					String nodeValue;
////					nodeValue = new String(b_nodeValue,charsetName);
////					parseXmlNode(rootPath+"/"+nodeName,nodeName,nodeValue);
////					System.out.println("第"+i+"个应用节点【"+rootPath+"/"+nodeName+"】\n"+nodeValue);
////
////				}
////			}
////		}
////		//打印
////		//printMap();
////	}
//	private void printMap(){
//		System.out.println("------------zkServiceMap("+zkServiceMap.size()+")------------");
//		int i = 0;
//		for(Map.Entry<String,Map<String,String>> entry : zkServiceMap.entrySet()){
//			System.out.println((++i) +"、节点【"+entry.getKey()+"】实现类映射，如下:");
//			int j = 0;
//			for(Map.Entry<String,String> entry1 : entry.getValue().entrySet()){
//				System.out.println("("+(++j) +")、【"+entry1.getKey()+"】【"+entry1.getValue()+"】");
//			}
//		}
//		System.out.println(JSONObject.fromObject(zkServiceMap));
//		System.out.println("------------zkRouteMap("+zkRouteMap.size()+")------------");
//		i = 0;
//		for(Map.Entry<String, RouteVo> entry : zkRouteMap.entrySet()){
//			System.out.println((++i) +"、节点【"+entry.getKey()+"】路由地址映射，如下:");
//			com.coreframework.json.JSONObject js = new com.coreframework.json.JSONObject(entry.getValue());
//			System.out.println(js);
//		}
//	}
//	
//	
//	public String queryAllRouteList(){
//		Set<String> keys = routeConfigListMap.keySet();
//		JSONArray array = new JSONArray();
//		for (String key : keys) {
//			String routeName = key.substring(key.lastIndexOf("/")+1, key.length());
//			RouteVo vo = zkRouteMap.get(key);
//			RouteApiInfo info = new RouteApiInfo();
//			Map<String, String> map = zkServiceMap.get(key);
//			JSONArray arr2 = new JSONArray();
//			for (Map.Entry<String, String> val : map.entrySet()) {
//				String apiM = val.getKey();
//				com.common.json.JSONObject module = new com.common.json.JSONObject();
//				try {
//					module.put("name", apiM);
//					module.put("remoteClass", val.getValue());
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				arr2.put(module);
//			}
//			info.setRoutename(routeName);
//			info.setRouteurl(vo.getUrl());
//			info.setRenark(vo.getRemark());
//			com.common.json.JSONObject obj = new com.common.json.JSONObject(info);
//			try {
//				obj.put("apimodule", arr2);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			array.put(obj);
//		}
//		return array.toString();
//		
//	}
//	
//	
//	/**
//	 * 解析zk节点内容，并将解析结果存入缓存
//	 * @param nodePath 节点路径
//	 * @param nodeName 节点名称
//	 * @param nodeValue 节点内容：xml格式的二进制字节
//	 * @throws Exception 该过程异常时抛出，并写入日志
//	 */
//	@SuppressWarnings("unused")
//	private void parseXmlNode(String nodePath,String nodeName,String nodeValue) throws AbsHosException {
//		Document doc = null;
//		Element element = null;
//		String xPath = "";
//		String url = null;
//		String proxyUrl = null;
//		Map<String,String> serviceMap = new HashMap<String,String>();
//		RouteVo routeVo = new RouteVo();
//		
//		try {
//			doc = DocumentHelper.parseText(nodeValue);
//		
//			Element root = doc.getRootElement();
//			//1、接口实现映射节点
//			xPath = "/"+Config+"/"+Mapping;
//			List<Element> mappingList = root.selectNodes(xPath);
//			Element mappingElement = mappingList.get(0);
//			String remark = root.attributeValue(this.remark);
//			routeVo.setModuleName(nodeName);
//			routeVo.setRemark(remark);
//			List<Element> mappingModuleList = mappingElement.selectNodes(this.module);
//			if(mappingModuleList.size()>0){
//				for(Iterator<Element> it = mappingModuleList.iterator();it.hasNext();){
//					element = it.next();
//					String key = element.attributeValue(this.name);
//					if(key!=null && !"".equals(key)){
//						String val = element.attributeValue(this.remoteClass);
//						serviceMap.put(key, val);
//					}
//					//add by zzz 20140910 将原来api为modul同级关系改为子节点。
//					List<Element> mappingApiList = element.selectNodes(this.api);
//					if(mappingApiList.size()>0){
//						for(Iterator<Element> it1 = mappingApiList.iterator();it1.hasNext();){
//							Element element1 = it1.next();
//							String key1 = element1.attributeValue(this.name);
//							if(key1!=null && !"".equals(key1)){
//								String val1 = element1.attributeValue(this.remoteClass);
//								serviceMap.put(key1, val1);
//							}
//						}
//					}
//					//end
//				}
//			}
//			//2、调用路由映射
//			xPath = "/"+Config+"/"+Route;
//			List<Element> routeList = root.selectNodes(xPath);
//			Element routeNode = routeList.get(0);
//			
//			String p_url = routeNode.attributeValue("url");
//			getUrl(nodeName,p_url);
//			
//			String p_proxyUrl = routeNode.attributeValue("proxyUrl");
//			
//			routeVo.setUrl(p_url);
//			routeVo.setProxyUrl(p_proxyUrl);
//	//		routeMap.put(nodeName, new RouteNodeVo(nodeName,p_url,p_proxyUrl));
//			
//			xPath = "/"+Config+"/"+Route+"/"+module;
//			List<Element> moduleList = root.selectNodes(xPath);
//			if(moduleList.size()>0){
//				Map<String,RouteModuleVo> moduleMap = new HashMap<String,RouteModuleVo>();
//				for(Iterator<Element> it = moduleList.iterator();it.hasNext();){
//					element = it.next();
//					String key = element.attributeValue(this.name);
//					if(key!=null && !"".equals(key)){
//						RouteModuleVo routeModuleVo = new RouteModuleVo();
//						url = element.attributeValue(this.url);
//						proxyUrl = element.attributeValue(this.proxyUrl);
//						if(url==null || "".equals(url)) url = p_url;
//						if(proxyUrl==null || "".equals(proxyUrl)) proxyUrl = p_proxyUrl;
//	//					routeMap.put(key, new RouteNodeVo(key,url,proxyUrl));
//						routeModuleVo.setUrl(url);
//						routeModuleVo.setProxyUrl(proxyUrl);
//						routeModuleVo.setModuleName(key);
//						moduleMap.put(key,routeModuleVo);
//					}
//				}
//				routeVo.setModuleMap(moduleMap);
//			}
//			xPath = "/"+Config+"/"+Route+"/"+api;
//			List<Element> apiList = root.selectNodes(xPath);
//			if(apiList.size()>0){
//				Map<String,RouteApiVo> apiMap = new HashMap<String,RouteApiVo>();
//				for(Iterator<Element> it = apiList.iterator();it.hasNext();){
//					element = it.next();
//					String key = element.attributeValue(this.name);
//					String c_p_url = null;
//					String c_p_proxyUrl = null;
//					if(key!=null && !"".equals(key)){
//						String apiKey = key;
//						RouteApiVo routeApiVo = new RouteApiVo();
//						url = element.attributeValue(this.url);
//						getUrl(nodeName,url);
//						proxyUrl = element.attributeValue(this.proxyUrl);
//						if(url==null || "".equals(url)){
//							url = p_url;
//						}
//						if(proxyUrl==null || "".equals(proxyUrl)){
//							proxyUrl = p_proxyUrl;
//						}
//						c_p_url = url;
//						c_p_proxyUrl = proxyUrl;
//	//					routeMap.put(key, new RouteNodeVo(key,url,proxyUrl));
//						routeApiVo.setApi(key);
//						routeApiVo.setUrl(url);
//						routeApiVo.setProxyUrl(proxyUrl);
//						
//						List<Element> paramList = element.selectNodes(this.param);
//						if(paramList.size()>0){
//							List<RouteParamVo> listParam = new ArrayList<RouteParamVo>();
//							for(Iterator<Element> it1 = paramList.iterator();it1.hasNext();){
//								element = it1.next();
//								String paramNameValue = element.attributeValue(this.name);
//								String paramValueValue = element.attributeValue(this.value);
//								if(paramNameValue!=null && !"".equals(paramNameValue) && paramValueValue!=null && !"".equals(paramValueValue)){
//									RouteParamVo routeParamVo = new RouteParamVo();
//									url = element.attributeValue(this.url);
//									proxyUrl = element.attributeValue(this.proxyUrl);
//									if(url==null || "".equals(url)) url = c_p_url;
//									if(proxyUrl==null || "".equals(proxyUrl)) proxyUrl = c_p_proxyUrl;
//									String key1 = key + "_" + paramNameValue + "_" + paramValueValue;
//	//							routeMap.put(key1, new RouteNodeVo(key1,url,proxyUrl));
//									//
//									routeParamVo.setUrl(url);
//									routeParamVo.setProxyUrl(proxyUrl);
//									routeParamVo.setParamName(paramNameValue);
//									routeParamVo.setParamValue(paramValueValue);
//									listParam.add(routeParamVo);
//								}
//							}
//							routeApiVo.setList(listParam);
//						}
//						apiMap.put(apiKey, routeApiVo);
//					}
//				}
//				routeVo.setApiMap(apiMap);
//			}
//			//
//			zkServiceMap.put(nodePath, serviceMap);
//			zkRouteMap.put(nodePath, routeVo);
//		} catch (Exception e) {
//			StringWriter sw = new StringWriter();
//			PrintWriter w = new PrintWriter(sw);
//			e.printStackTrace(w);
//			String error = Thread.currentThread().getStackTrace()[1].getMethodName() +": " + sw.toString();;
//			throw new CommonServiceException(CommonServiceRetCode.Common.ERROR_ROUTE_PARSE,error);
//		}
//	}
//	
//	private Url[] getUrl(String nodeName,String urlStr) throws Exception{
//		if(urlStr == null || "".equals(urlStr)){
//			return null;
//		}
//		String prefix="url\\.";//url.
//		String regex="^"+prefix+".*";
//		boolean boo=urlStr.matches(regex);
//		if(boo){
//			throw new Exception("Method=parseXmlNode  node = "+ nodeName +" ExceptionMessage =请填写正确的路由的地址: [ ip ] : [ port ] / ip + : + 端口");
//		}
//		String urls[] = null;
//		List<Url> urlss = new ArrayList<Url>();
//		urls = urlStr.split(",");
//		for (String string : urls) {
//			String[] tmp=string.split(":");
//			if(null != tmp && tmp.length > 1){
//				urlss.add(new Url(tmp[0], Integer.parseInt(tmp[1])));
//			}
//		}
//		if(urlss.size() > 0){
//			return urlss.toArray(new Url[urlss.size()]);
//		}
//		throw new Exception("Method=parseXmlNode  node = "+ nodeName +" ExceptionMessage =请填写正确的路由的地址: [ ip ] : [ port ] / ip + : + 端口");
//	}
//
//	private String getProxyUrl(String proxyUrl) throws Exception {
//		if(proxyUrl == null || "".equals(proxyUrl)){
//			return null;
//		}
//		return proxyUrl;
//	}
//
//}
//
