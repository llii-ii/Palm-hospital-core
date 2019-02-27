//package com.yihu.wsgw.api;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URISyntaxException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import com.coreframework.remoting.Dict;
//import com.coreframework.remoting.Url;
//import com.kasite.core.common.config.KasiteConfig;
//import com.kasite.core.common.exception.RRException;
//import com.kasite.core.httpclient.http.StringUtils;
//import com.yihu.wsgw.vo.MappingRouteVo;
//import com.yihu.wsgw.vo.RouteApiVo;
//import com.yihu.wsgw.vo.RouteModuleVo;
//import com.yihu.wsgw.vo.RouteParamVo;
//import com.yihu.wsgw.vo.RouteVo;
//
//import net.sf.json.JSONObject;
///**
// * 路由解释工具，主要功能：
// *  1、ZK中路由配置信息（XML）的解释及初始化
// * 	2、对外提供获取路由的方法queryReturnRouteVo，返回MappingRouteVo实例。 
// * 【支持以下4种情况： 
// * 	1、应用名称：account 
// * 	2、模块：account.AccountWs 
// * 	3、API方法：account.AccountWs.a
// * 	4、API方法带参数：account.AccountWs.a_参数名称_参数值 】
// * 
// */
//public class KasiteRouteParser extends RouteParser{
//
//		protected static Logger logger = LoggerFactory.getLogger(KasiteRouteParser.class);
//		protected static String RouteApiPath = "/ApiRouteRoot_";
//		public static final String ROUTENAMELASTNAME = "route";
//	// 节点
//		protected static final String XML_Config = "Config";
//		protected static final String XML_Mapping = "Mapping";
//		protected static final String XML_Route = "Route";
//		protected static final String XML_module = "module";
//		protected static final String XML_api = "api";
//		protected static final String XML_param = "param";
//		// 属性
//		protected static final String XML_url = "url";
//		protected static final String XML_proxyUrl = "proxyUrl";
//		protected static final String XML_name = "name";
//		protected static final String XML_value = "value";
//		protected static final String XML_remoteClass = "remoteClass";
//		private static final String XML_CHARSET = "UTF-8";
//		private static volatile RouteParser instance = null;
//		private Properties urlMappings=new Properties();
//		/*
//		 * 构造：初始化
//		 */
//		public KasiteRouteParser() throws Exception {
//			//从本地读取数据
//			FileWatcher fw=new FileWatcher(this);
//			try {
//				fw.deal();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Thread t=new Thread(fw);
//			t.setDaemon(true);
//			t.setName("KasiteRouteParseThread");
//			t.start();
//		}
//
//		public static RouteParser instance(){
//			return instance;
//		}
//		
//		/*
//		 * 接口实现的映射：Map<String, Map<String,String>> zkServiceMap
//		 * key:应用节点路径，结构:rootPath+"/"+应用节点名称。如：/ApiRouteRoot/account
//		 * value:Map<String,String>【key:name属性值】【value:remoteClass属性值】
//		 * 
//		 * key的存在为为了寻址，它跟a.b.c中的a有关联关系，该关系通过key(module)方法指定
//		 */
////		protected final Map<String, Map<String, String>> zkServiceMap = new ConcurrentHashMap<>();
//		/*
//		 * 调用路由的映射：Map<String,RouteVo> key:应用节点路径 ，与zkServiceMap的key一样 value:RouteVo
//		 */
////		protected static Map<String, RouteVo> proxyRouteMap = new ConcurrentHashMap<>();
//		
//		protected static Map<String,Map<String, String>> proxyzkServiceMap = new ConcurrentHashMap<>();
//
//		private static Map<String, String> proxyUrlMap = new ConcurrentHashMap<>();
//		/***
//		 * 根据a确定key，如果是zk，返回的就是zk的全路径。如果是本地方式，返回的是就是a
//		 * 
//		 * @param module
//		 *            a.b.c中的a
//		 * @return
//		 */
//		public String key(String appRoutePath) {
//			String mo = appRoutePath.substring(appRoutePath.lastIndexOf("/")+1,appRoutePath.length());
//			return mo;
//		}
//		
//		/***
//		 * 这个是key()的逆向操作，从zkServiceMap的key中得出module
//		 * 
//		 * @param key
//		 * @return
//		 */
//		protected String module(String key) {
//			return key;
//		}
//
//		public static RouteParser getInstance() {
//			if (instance == null) {
//				synchronized (KasiteRouteParser.class) {
//					if (instance == null) {
//						try {
//							instance = new KasiteRouteParser();
//						} catch (Exception e) {
//							e.printStackTrace();
//							throw new RuntimeException("error in create RouteParser",e);
//						}
//					} 
//				}
//			}
//			return instance;
//		}
//		/**
//		 * 获取本地配置文件对象
//		 * @return
//		 */
//		public static KasiteRouteParser getInstall() {
//			return (KasiteRouteParser) getInstance();
//		}
//	
//		protected static AppRoute parseXmlNode(String appId,String xml) throws Exception {
//			xml = xml.trim();
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dbd = dbf.newDocumentBuilder();
//			Document doc = dbd.parse(new ByteArrayInputStream(xml.getBytes(XML_CHARSET)));
//			return parseXmlNode(appId,doc);
//		}
//
//		protected static AppRoute parseXmlNode(InputStream in) throws Exception {
//			try {
//				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//				DocumentBuilder dbd = dbf.newDocumentBuilder();
//				Document doc = dbd.parse(in);
//				return parseXmlNode(doc);
//			} finally {
//				in.close();
//			}
//		}
//		
//		protected MappingRouteVo doQueryMappingRouteVo(String api, String paramJson) throws Exception{
//			return this.queryMappingRouteVoLocal(api, paramJson);
//		}
//
//		protected MappingRouteVo queryMappingRouteVoLocal(String api, String paramJson) throws Exception {
//			String[] t1 = api.split("@");
//			if(t1.length == 2) {
//				api = t1[0];
//				String[] tmp = api.split("\\.");
//				// 1、实现类 a.b.c
//				String remoteClass = null;
//				String appId = t1[1];
//				if(StringUtils.isNotBlank(appId)) {
//					
//					KasiteConfig.print("====="+api);
//					Map<String, String> serviceMap = proxyzkServiceMap.get(appId);
//					if(null == serviceMap) {
//						throw new RRException("未找到对应机构的路由配置,请查看本地路由配置文件夹。：appId = "+ appId +" localConfigPath = "+KasiteConfig.localConfigPath());
//					}
//					KasiteConfig.print("===================================");
//					if (serviceMap.get(api) != null && !"".equals(serviceMap.get(api))) {
//						remoteClass = serviceMap.get(api);// a.b.c
//					} else if (serviceMap.get(tmp[0] + "." + tmp[1]) != null) {
//						remoteClass = serviceMap.get(tmp[0] + "." + tmp[1]);// a.b
//					}
//					
//					
//					
//					String key =  appId;
//					String proxyUrl = proxyUrlMap.get(key);
//					Url url = new Url(proxyUrl, 0);
//					Url[] urlArray = new Url[1];
//					urlArray[0] = url;
//					MappingRouteVo vo = new MappingRouteVo(remoteClass, urlArray);
//					return vo;
//				}
//				// 2、路由地址url、proxyUrl
//				String url = null;
//				RouteVo routeVo = zkRouteMap.get(appId);
//				if (routeVo != null) {
//					Map<String, RouteApiVo> apiMap = routeVo.getApiMap();
//					Map<String, RouteModuleVo> moduleMap = routeVo.getModuleMap();
//					if (apiMap != null && apiMap.get(api) != null) {// a.b.c
//						RouteApiVo routeApiVo = apiMap.get(api);
//						url = routeApiVo.getUrl();// 地址
//						List<RouteParamVo> paramList = routeApiVo.getList();// 参數
//						if (paramList != null && paramList.size() > 0) {
//							for (RouteParamVo routeParamVo : paramList) {
//								if (routeParamVo == null) {
//									logger.error(Dict.LOG_Module_Name + " 存在空routeApiVo api="+ api);
////									Logger.get().error(Dict.LOG_Module_Name, new LogBody().set("存在空routeApiVo", api));
//									continue;
//								}
//								String paramName = routeParamVo.getParamName();
//								String paramValue = routeParamVo.getParamValue();
//								// 参数配置查找
//								try {
//									JSONObject jo = JSONObject.fromObject(paramJson);
//									if (jo.has(paramName)) {
//										String paramV = String.valueOf(jo.get(paramName));
//										if (paramValue.equals(paramV)) {
//											url = routeParamVo.getUrl();// 地址
//											break;
//										}
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}
//					} else if (moduleMap != null && moduleMap.get(tmp[0] + "." + tmp[1]) != null) {// a.b
//						RouteModuleVo routeModuleVo = moduleMap.get(tmp[0] + "." + tmp[1]);
//						if (routeModuleVo != null) {
//							url = routeModuleVo.getUrl();// 地址
//						}
//					} else {// a
//						url = routeVo.getUrl();// 地址
//					}
//				}
//				if (url == null) {
//					return null;
//				}
//				// 支持多个Url，以英文半角逗号,隔开
//				String[] urlTmp = url.split(",");
//				Set<Url> set = new HashSet<Url>();
//				StringBuilder msg = null;
//				for (String str : urlTmp) {
//					if (!"".equals(str.trim())) {
//						try {
//							Url u = getUrl(str);
//							// KasiteConfig.print(u);
//							set.add(u);
//						} catch (Exception e) {
////							e.printStackTrace();
//							if (msg == null) {
//								msg = new StringBuilder();
//							}
//							msg.append("【" + e.getMessage() + "】");
//						}
//					}
//				}
//				Url[] urlArray = new Url[set.size()];
//				set.toArray(urlArray);// 数组
//				if (msg != null && urlArray.length == 0) {// 只要有一个地址能正确解释，请求将继续下去，否则抛出异常信息
////					throw new Exception(msg.toString());
//					logger.error("url 解析异常 "+msg.toString());
//				}
//
//				return new MappingRouteVo(remoteClass, urlArray);
//			}
//			return null;
//		}
//
//		/**
//		 * 如果name中没有：,就获取地址映射中的真正值，否则就说明它配置的就是zk
//		 * 
//		 * @param name
//		 * @return
//		 */
//		private Url getUrl(String name) throws Exception {
//			if (name == null || "".equals(name)) {
//				return null;
//			}
//			if (!name.contains(":")) {
//				String tmp = getUrlByName(name);
//				if(tmp==null || tmp.isEmpty()){
//					logger.debug("--------------"+name+" can not find its ip:port");
//					throw new Exception(name+" can not find in name-url mapping");
//				}
//				String[] array = tmp.split(":");
//				return new Url(array[0], Integer.parseInt(array[1]));
//			}
//			String[] tmp = name.split(":");
//			return new Url(tmp[0], Integer.parseInt(tmp[1]));
//		}
//
//		/**
//		 * 根据name获取它的地址映射
//		 * 
//		 * @param name
//		 * @return
//		 */
//		@Override
//		public String getUrlByName(String name) throws Exception {
//			String value = urlMappings.getProperty(name);
//			if (value == null) 
//			{
//				throw new Exception("未找到" + name + "的名称映射");
//			}
//			return value;
//		}
//		/**
//		 * @param element
//		 * @param attr
//		 * @return 含有去空格的功能
//		 */
//		private static String getAttribute(Element element, String attr) {
//			String v = element.getAttribute(attr);
//			if (v == null) {
//				return v;
//			}
//			return v.trim();
//		}
//
//		private static NameUrl parseRoute(Element element) {
//			String key = getAttribute(element, XML_name);
//			String url = getAttribute(element, XML_url);
//			if (key == null || key.isEmpty() || url == null || url.isEmpty()) {
//				logger.debug("--------------name='" + key + "' url='" + url + "' is not valid");
//				return null;
//			}
//			return new NameUrl(key, url);
//		}
//
//		/**
//		 * 解析路由xml节点内容
//		 * 
//		 */
//		protected static AppRoute parseXmlNode(String appId,Document doc) throws Exception {
//			Element root = doc.getDocumentElement();
//
//			// 1、解析Mapping->module->api
//			Map<String, String> serviceMap=parseMapping(root);
//			
//			// 2、<Route>
//			RouteVo routeVo = new RouteVo();
//			Element routeNode = (Element) root.getElementsByTagName(XML_Route).item(0);
//			routeVo.setUrl(getAttribute(routeNode,XML_url));
//			String proxyUrl = getAttribute(routeNode, XML_proxyUrl);
//			if(StringUtils.isNotBlank(proxyUrl)) {
//				proxyUrlMap.put(appId, proxyUrl);
//			}
//			parseModule(routeNode, routeVo);
//			parseApi(routeNode, routeVo);
//			
//			return new AppRoute(serviceMap, routeVo);
//		}
//
//		private static boolean parseMapping(Element element, Map<String, String> serviceMap) {
//			String key = getAttribute(element, XML_name);
//			String val = getAttribute(element, XML_remoteClass);
//			if (key != null && key.length() > 0 && val != null && val.length() > 0) {
//				serviceMap.put(key, val);
//				return true;
//			}
//			logger.debug("--------------name='" + key + "' remoteClass='" + val + "' is not valid");
//			return false;
//		}
//		
//		/**
//		 * @param root
//		 * @return 
//		 */
//		private static Map<String, String> parseMapping(Element root) {
//			Map<String, String> serviceMap = new HashMap<String, String>(); // module中的name:remoteClass
//			Element mappingElement = (Element) root.getElementsByTagName(XML_Mapping).item(0);
//			NodeList mappingModuleList = mappingElement.getElementsByTagName(XML_module);
//			if (mappingModuleList.getLength() > 0) {
//				for (int i = 0; i < mappingModuleList.getLength(); i++) {
//					Element element = (Element) mappingModuleList.item(i);
//					parseMapping(element, serviceMap);//即使module里面的remoteClass为空，api里的remoteClass也是可以解析的
//					NodeList mappingApiList = element.getElementsByTagName(XML_api);
//					if (mappingApiList.getLength() > 0) {
//						for (int j = 0; j < mappingApiList.getLength(); j++) {
//							Element element1 = (Element) mappingApiList.item(j);
//							parseMapping(element1, serviceMap);
//						}
//					}
//				}
//			}
//			return serviceMap;
//		}
//
//		/**
//		 * 先解析api列表，然后解析api中的param列表
//		 * @param routeNode
//		 * @param routeVo
//		 */
//		private static void parseApi(Element routeNode, RouteVo routeVo) {
//			NodeList apiList = routeNode.getElementsByTagName(XML_api);
//			if (apiList.getLength() == 0) {
//				return;
//			}
//			Map<String, RouteApiVo> apiMap = new HashMap<String, RouteApiVo>();
//			routeVo.setApiMap(apiMap);
//			for (int indexApi = 0; indexApi < apiList.getLength(); indexApi++) {
//				Element element = (Element) apiList.item(indexApi);
//				String api_key = getAttribute(element, XML_name);
//				String api_url = getAttribute(element, XML_url);
//				if(api_key==null||api_key.isEmpty()){
//					continue;
//				}
//				if(api_url==null || api_url.isEmpty()){
//					api_url=routeVo.getUrl();// api的url为空，就是用route节点的url
//				}
//				RouteApiVo routeApiVo = new RouteApiVo();
//				routeApiVo.setApi(api_key);
//				routeApiVo.setUrl(api_url);
//				apiMap.put(api_key, routeApiVo);
//					
//				NodeList paramList = element.getElementsByTagName(XML_param);
//				if (paramList.getLength() == 0) {
//					continue;
//				}
//				List<RouteParamVo> listParam = new ArrayList<RouteParamVo>();
//				routeApiVo.setList(listParam);
//				for (int indexParam = 0; indexParam < paramList.getLength(); indexParam++) {
//					element = (Element) paramList.item(indexParam);
//					NameUrl nameUrl=parseRoute(element);
//					if(nameUrl==null){
//						continue;
//					}
//					String paramValueValues = getAttribute(element,XML_value);
//					if (paramValueValues == null|| paramValueValues.length() == 0) {
//						logger.debug("--------------name='"+nameUrl.name+"' url='"+nameUrl.url+"' value is null or empty");
//						continue;
//					}
//					String[] paramValueValueArray = paramValueValues.replace("，", ",").split(",");
//					for (int i = 0; i < paramValueValueArray.length; i++) {
//						String paramValueValue = paramValueValueArray[i];
//						if (paramValueValue == null
//								|| (paramValueValue = paramValueValue.trim()).length() == 0) {
//							continue;
//						}
//						RouteParamVo routeParamVo = new RouteParamVo();
//						routeParamVo.setParamName(nameUrl.name);
//						routeParamVo.setUrl(nameUrl.url);
//						routeParamVo.setParamValue(paramValueValue);
//						listParam.add(routeParamVo);
//					}
//				}
//			}
//						
//		}
//
//		/**
//		 * @param routeNode
//		 * @param routeVo
//		 */
//		private static void parseModule(Element routeNode, RouteVo routeVo) {
//			NodeList moduleList = routeNode.getElementsByTagName(XML_module);
//			if (moduleList.getLength() > 0) {
//				Map<String, RouteModuleVo> moduleMap = new HashMap<String, RouteModuleVo>();
//				for (int i = 0; i < moduleList.getLength(); i++) {
//					Element element = (Element) moduleList.item(i);
//					NameUrl nameUrl = parseRoute(element);
//					if (nameUrl != null) {
//						moduleMap.put(nameUrl.name, new RouteModuleVo(nameUrl)); 
//					}
//				}
//				routeVo.setModuleMap(moduleMap);
//			}
//		}
//
//		/**
//		 * @param node
//		 * @return
//		 */
//		public String route(String node) {
//			Map<String, String> services=zkServiceMap.get(node);
//			com.yihu.wsgw.vo.RouteVo route=zkRouteMap.get(node);
//			return new AppRoute(services,route).toString();
//		}
//
//		/**
//		 * @return
//		 */
//		public String apps(String orgCode) {
//			return proxyzkServiceMap.get(orgCode).keySet().toString();
//		}
//		
//		
//		public void reload(String appId,String routerStr) throws Exception {
//			AppRoute route = parseXmlNode(appId,routerStr);
////			Map<String, Map<String, String>> zkServiceMap = new ConcurrentHashMap<String, Map<String, String>>();
////			Object o = ReflectionUtils.getFieldValue(route, "serviceMap");
////			zkServiceMap.put(appId, (Map<String, String>) o);
//			proxyzkServiceMap.put(appId, route.serviceMap);
////			Map<String, RouteVo> zkRouteMap = new ConcurrentHashMap<String, RouteVo>();
////			Object o2 = ReflectionUtils.getFieldValue(route, "routeVo");
//			zkRouteMap.put(appId, route.routeVo);
////			proxyRouteMap.put(appId,  route.routeVo);
//		}
//		
//		
//		File[] listFiles() throws URISyntaxException{
//			String root = KasiteConfig.localConfigPath()+File.separator+ROUTENAMELASTNAME;
//			File path = new File(root);
//			if(!(path.exists() && path.isDirectory()) && KasiteConfig.getDebug()) {
//				logger.error("未找到配置文件目录："+ root);
//			}
//			return path.listFiles();
//		}
//		private static class FileWatcher implements Runnable{
//			Map<String,Long> map=new HashMap<String,Long>();
//			private static long delay = 20000;
//			private KasiteRouteParser parser;
//			static { 
//				logger.debug("配置文件动态加载时间："+delay);
//			}
//			
//			private FileWatcher(KasiteRouteParser parser) {
//				this.parser = parser;
//			}
//			void deal() throws Exception
//			{
//				boolean modify=false;
//				File[] files=parser.listFiles();
//				if(files==null|| files.length <= 0 ){
//					return;
//				}
//				Map<String,Long> newMap=new HashMap<String,Long>();
//				for(File f:files){
//					if(f.isFile() && f.getName().toLowerCase().endsWith(".xml")) {
//						newMap.put(f.getName(), f.lastModified());
//						Long v=map.get(f.getName());
//						if(v==null || !v.equals(f.lastModified())){
//							modify=true;
//						}
//					}
//				}
//				if(map.size()!=newMap.size()){
//					modify=true;
//				}
//				if(modify){
//					parser.reload(files);
//					this.map=newMap;
//				}
////				
////				Map<String,File> updateFiles = new HashMap<>();
////				for (File file : files) {
////					if(file.isFile()) {
////						String fileName = file.getName();
////						if(fileName.toLowerCase().endsWith(".xml".toLowerCase())) {
////							String appId = fileName.substring(0, fileName.lastIndexOf("."));
////							newMap.put(fileName+"_"+file.getName(), file.lastModified());
////							Long v = map.get(fileName+"_"+file.getName());
////							if(v==null || !v.equals(file.lastModified())){
////								updateFiles.put(appId, file);
////								modify=true;
////							}
////						}
////					}
////				}
////				if(map.size()!=newMap.size()){
////					modify=true;
////				}
////				if(modify){
////					logger.debug("配置文件被修改需要重新加载");
////					for (Map.Entry<String, File> entity : updateFiles.entrySet()) {
////						String callOrgCode = entity.getKey();
////						String orgCode = callOrgCode.substring(0, callOrgCode.indexOf("_"));
////						File file = entity.getValue();
////						File[] fs = new File[] {file};
////						parser.reload(orgCode,fs);
////					}
////					this.map=newMap;
////				}
//			}
//			@Override
//			public void run() {
//				while(true)
//				{
//					try 
//					{
//						Thread.sleep(delay);
//						deal();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}
//			}
//
//		}
//		
//		private void reload(File[] fs) throws URISyntaxException {
//			if(fs==null){
//				return;
//			}
//			for (File f : fs) {
//				String name = f.getName();
//				if (name.toLowerCase().endsWith(".xml")) {
//					logger.error("加载配置文件 routeXml = "+ f.getPath());
//					InputStream in = null;
//					try {
////						AppRoute route = parseXmlNode(in);
//						String appId = name.substring(0, name.lastIndexOf("."));
//						String routeXml = "";
//						if(f.exists() && f.isFile()) {
//							byte[] data = Files.readAllBytes(f.toPath());
//							routeXml = new String(data, StandardCharsets.UTF_8);
//						}
//						//加载路由信息到本地内存中
//						reload(appId, routeXml);
////						zkServiceMap.put(key2(module), route.serviceMap);
////						zkRouteMap.put(key2(module), route.routeVo);
//					} catch (Exception e) {
//						e.printStackTrace();
//					} finally {
//						if (in != null) {
//							try {
//								in.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}else if("url.properties".equals(name)){
//					logger.error("加载Url配置文件 url.properties = "+ f.getPath());
//					Properties p=new Properties();
//					InputStream in = null;
//					try {
//						in = new FileInputStream(f);
//						p.load(in);
//						this.urlMappings=p;
//					} catch (Exception e) {
//						e.printStackTrace();
//					} finally {
//						if (in != null) {
//							try {
//								in.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//
//		}
//
//		private String key2(String module) {
//			return module;
//		}
//}
