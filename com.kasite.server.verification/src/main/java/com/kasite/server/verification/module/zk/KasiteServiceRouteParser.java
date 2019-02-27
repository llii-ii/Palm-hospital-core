package com.kasite.server.verification.module.zk;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.util.DesUtil;
import com.yihu.zk.AbstractZKListener;
import com.yihu.zk.ZkClientFactory;
import com.yihu.zk.ZkClientWrapper;

/**
 * 路由解释工具，主要功能：
 *  1、ZK中路由配置信息（XML）的解释及初始化
 * 	2、对外提供获取路由的方法queryReturnRouteVo，返回MappingRouteVo实例。 
 * 【支持以下4种情况： 
 * 	1、应用名称：account 
 * 	2、模块：account.AccountWs 
 * 	3、API方法：account.AccountWs.a
 * 	4、API方法带参数：account.AccountWs.a_参数名称_参数值 】
 * 
 */
public class KasiteServiceRouteParser {
	protected final static Logger logger = LoggerFactory.getLogger(KasiteServiceRouteParser.class);
	private static final String charsetName = "GBK";
	private String zkService;
	private ZkClientWrapper wrapper = null;
	private ZkClient zkClient = null;
	private static KasiteServiceRouteParser instance;
	private final static String ROOTPATH = "/ApiRouteRoot";
	
	/**
	 * 保存加密前的字符串
	 */
	protected final Map<String, String> zkRouteMap = new ConcurrentHashMap<String, String>();
	
	//保存加密后的字符串
	protected final Map<String, String> zkServiceMap = new ConcurrentHashMap<String, String>();
	
//	public static KasiteServiceRouteParser instance(String nodeName){
//		String rootPath = ROOTPATH;
//		return parserMap.get(rootPath);
//	}
	
	public static KasiteServiceRouteParser instance(String zkService) throws Exception {
		if (instance == null) {
			synchronized (KasiteServiceRouteParser.class) {
				if (instance == null) {
					try {
						instance = new KasiteServiceRouteParser(zkService);
					} catch (Exception e) {
						throw e;
					}
				} 
			}
		}
		return instance;
	}
	
	public String queryAllEncryptRoute(String appId) {
		StringBuffer sbf = new StringBuffer();
		String key = ROOTPATH +"/"+ appId;
		String value = zkServiceMap.get(key);
		sbf.append(key)
		.append("_@_")
		.append(value);
		return sbf.toString();
	}
	
	public String queryAllRoute() {
		StringBuffer sbf = new StringBuffer();
		for (Map.Entry<String, String> entity : zkRouteMap.entrySet()) {
			String key = entity.getKey();
			String value = entity.getValue();
			sbf.append(key)
			.append("_@_")
			.append(value)
			.append(",");
		}
		return sbf.toString();
	}
	public String getRouteXml(String orgCode,String nickName) {
		String key = "/ApiRouteRoot_"+orgCode+"/"+nickName;
		return zkRouteMap.get(key);
	}
	/*
	 * 构造：初始化
	 */
	KasiteServiceRouteParser(String zkService) throws Exception {
		this.zkService = zkService;
		createZkClient(this.zkService);
		getDataByZk();
		wrapper.subscribePath(ROOTPATH, new AbstractZKListener() {//wrapper.subscribePath会对它的子节点注册相同的Listener

			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				String nodeName = dataPath.replace(ROOTPATH, "");
				parseXmlNode(dataPath, nodeName, new String((byte[]) data,
						charsetName));
			}

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
			}

			@Override
			public void handleChildChange(String parentPath,
					List<String> currentChilds) throws Exception {
				Set<String> currentSet = new HashSet<String>();
				for (String string : currentChilds) {
					currentSet.add(parentPath + "/" + string);
				}
				Set<String> serviceSet = zkServiceMap.keySet();

				Set<String> currentSetMore = new HashSet<String>();
				Set<String> currentSetLess = new HashSet<String>();
				Set<String> serviceSetClone = new HashSet<String>();
				Set<String> currentSetClone = new HashSet<String>();

				serviceSetClone.addAll(serviceSet);
				currentSetClone.addAll(currentSet);

				currentSetMore.addAll(currentSet);
				currentSetLess.addAll(serviceSet);

				currentSetMore.removeAll(serviceSetClone);
				currentSetLess.removeAll(currentSetClone);
				for (String nodePath : currentSetMore) {
					// 对新增节点建立监控
					wrapper.subscribePath(nodePath, new AbstractZKListener() {

						@Override
						public void handleDataChange(String dataPath,
								Object data) throws Exception {
							String nodeName = dataPath.replace(ROOTPATH, "");
							parseXmlNode(dataPath, nodeName, (String) data);
						}

						@Override
						public void handleDataDeleted(String dataPath)
								throws Exception {
						}

						@Override
						public void handleChildChange(String parentPath,
								List<String> currentChilds) throws Exception {
						}

					});
					Object obj = zkClient.readData(nodePath, true);
					if (obj != null) {
						String nodeValue = (String) obj;
						String nodeName = nodePath.replace(ROOTPATH, "");
						parseXmlNode(nodePath, nodeName, nodeValue);
					}
				}
				if(currentChilds!=null&&currentChilds.size()>0){
					for (String nodePath : currentSetLess) {
						zkServiceMap.remove(nodePath);
						zkRouteMap.remove(nodePath);
					}
				}
			}

		});
		System.out.println("=============zk listner builded=============");
	}


	private void getDataByZk() throws Exception {
		boolean isexist = zkClient.exists(ROOTPATH);
		if(!isexist) {
			throw new RRException("zk路由未配置和初始化，请联系：系统管理员。rootPath :"+ ROOTPATH);
		}
		List<String> list = zkClient.getChildren(ROOTPATH);
		if (list == null ||list.isEmpty()) {
			return;
		}
		for (String nodeName : list) {
			Object obj = zkClient.readData(ROOTPATH + "/" + nodeName, true);
			if (obj != null) {
				String nodeValue = (String) obj;
				parseXmlNode(ROOTPATH + "/" + nodeName, nodeName, nodeValue);
			}
		}
	}

	public void createNode(String orgCode,String nickName,String routeXml) throws Exception {
		String rootPath = ROOTPATH +"_"+orgCode;
		String routeName = nickName;
		String charsetName = "GBK";
		boolean isExistRoot = wrapper.exists(rootPath);
		if(!isExistRoot) {
			wrapper.createPersistent(rootPath);
			System.out.println("创建根节点");
		}
		String path = rootPath + "/" + routeName.trim();
		boolean bool = wrapper.exists(path);
		if(bool) {
			Object obj = wrapper.readData(path);
			if (obj != null) {
				String nodeValue = (String) obj;
				logger.error("删除节点="+path+"="+nodeValue);
			}
			wrapper.delete(path);
		}
		String xmlContent =routeXml;
		String nodeValue = new String(xmlContent.getBytes(), charsetName);
		logger.error("新增节点="+path+"="+nodeValue);
		wrapper.createPersistent(path, nodeValue);
		parseXmlNode(path, nickName, nodeValue);
		System.out.println("创建路由节点。");
	}
	
	/**
	 * 创建ZK客户端
	 * 
	 * @param zkService
	 *            zk地址
	 * @throws Exception
	 */
	private void createZkClient(String zkService) throws Exception {
		System.out.println("zk server：" + zkService);
		wrapper = ZkClientFactory.getInstance().getZkClient(zkService);
		zkClient = wrapper.getZkClient();
	}

	/**
	 * 解析zk节点内容，并将解析结果存入缓存
	 * 
	 * @param nodePath
	 *            节点路径
	 * @param nodeName
	 *            节点名称
	 * @param nodeValue
	 *            节点内容：xml格式的二进制字节
	 * @throws Exception
	 *             该过程异常时抛出，并写入日志
	 */
	private void parseXmlNode(String nodePath,final String nodeName,final String nodeValue)
			throws Exception {
		try {
			String des = DesUtil.encrypt(nodeValue, charsetName);
			zkServiceMap.put(nodePath, des);
			zkRouteMap.put(nodePath, nodeValue);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			e.printStackTrace(w);
			logger.error("解析zk节点出现异常!",e);
//			Logger.get().error(
//					Dict.LOG_Module_Name,
//					new LogBody().set(
//							"方法",
//							Thread.currentThread().getStackTrace()[1]
//									.getMethodName()).set("异常", sw.toString()));
			throw e;
		}
	}

}
