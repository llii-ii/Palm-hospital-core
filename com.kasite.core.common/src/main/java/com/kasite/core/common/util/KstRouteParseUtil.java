//package com.kasite.core.common.util;
//
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import com.kasite.core.common.config.KasiteConfig;
//import com.yihu.hos.util.JSONUtil;
//import com.yihu.hos.util.MappingRouteVo;
//
//import net.sf.json.JSONObject;
//
///**
// * 从XML配置文件获取路由信息：代理地址、实现类
// * 
// * @author 無
// * @version V1.0
// * @date 2018年4月24日 下午3:33:17
// */
//public class KstRouteParseUtil {
//	private static volatile KstRouteParseUtil instance = null;
//	/**
//	 * 路由配置信息
//	 */
//	private static Map<String, String> KstRouteConfigListMap = new HashMap<String, String>();
//	/** 节点 */
//	private String node = "node";
//	private String config = "Config";
//	private String proxyUrl = "proxyUrl";
//	private String remoteClass = "remoteClass";
//	private String fileName = "httpConfig.xml";
//
//	/**
//	 * <Config node="pay" remoteClass="com.yihu.hos.ws.PayWs" proxyUrl=
//	 * 'http://127.0.0.1:8080/Hos-Pay/kstRest'></Config>
//	 */
//	@SuppressWarnings("unchecked")
//	public void init() throws Exception {
//		KasiteConfig.print("=============初始化kst-ZK数据 begin=============");
//		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
//		SAXReader reader = new SAXReader();
//		Document doc = reader.read(inputStream);
//		KasiteConfig.print(doc.asXML());
//		String xPath = "//" + this.config;
//		List<Element> list = doc.selectNodes(xPath);
//		JSONObject json = new JSONObject();
//		if (list != null) {
//			/** 清空重新获取 */
//			KstRouteConfigListMap.clear();
//			for (Element ele : list) {
//				json = new JSONObject();
//				json.put(this.remoteClass, ele.attributeValue(this.remoteClass));
//				json.put(this.proxyUrl, ele.attributeValue(this.proxyUrl));
//				KstRouteConfigListMap.put(ele.attributeValue(this.node), json.toString());
//			}
//		}
//		KasiteConfig.print(KstRouteConfigListMap.toString());
//		KasiteConfig.print("=============初始化kst-ZK数据 end=============");
//	}
//
//	/**
//	 * 根据api获取路由信息
//	 * 
//	 * @param api
//	 * @return
//	 * @throws Exception
//	 */
//	public MappingRouteVo queryMappingRouteVo(String api) throws Exception {
//		MappingRouteVo vo = new MappingRouteVo();
//		String[] tmp = api.split("\\.");
//		JSONObject json = JSONObject.fromObject(KstRouteConfigListMap.get(tmp[0]));
//		if (!json.isEmpty()) {
//			vo.setRemoteClass(JSONUtil.getJsonString(json, this.remoteClass, true));
//			vo.setProxyUrl(JSONUtil.getJsonString(json, this.proxyUrl, true));
//		}
//		return vo;
//	}
//
//	/**
//	 * 构造：初始化
//	 */
//	private KstRouteParseUtil() throws Exception {
//		init();
//	}
//
//	public static KstRouteParseUtil getInstance() throws Exception {
//		if (instance == null) {
//			synchronized (KstRouteParseUtil.class) {
//				if (instance == null) {
//					instance = new KstRouteParseUtil();
//				}
//			}
//		}
//		return instance;
//	}
//}
